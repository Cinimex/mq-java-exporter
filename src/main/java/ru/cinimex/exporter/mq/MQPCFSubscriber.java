package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * MQPCFSubscriber is technically not a subscriber, but a runnable object, which sends PCFCommands every n seconds to
 * retrieve specific statistics, which couldn't be retrieved by MQTopicSubscriber.
 */
public class MQPCFSubscriber implements Runnable {
    private MQConnection connection;
    private String queueManagerName;
    private MQObject object;
    private PCFMessageAgent agent;
    private ArrayList<MQObject> objects;

    /**
     * MQPCFSubscriber constructor which is used, when exporter is configured to use 1 MQPCFSubscriber per 1 MQObject.
     *
     * @param queueManagerName     - queue manager name.
     * @param connectionProperties - connection properties.
     * @param object               - MQObject which should be monitored.
     */
    public MQPCFSubscriber(String queueManagerName, Hashtable<String, Object> connectionProperties, MQObject object) {
        establishMQConnection(queueManagerName, connectionProperties);
        this.object = object;
    }

    /**
     * MQPCFSubscriber constructor which is used, when exporter is configured to use 1 MQPCFSubscriber per all MQObjects of the same type.
     *
     * @param queueManagerName     - queue manager name.
     * @param connectionProperties - connection properties.
     * @param objects              - Array with all MQObjects.
     */
    public MQPCFSubscriber(String queueManagerName, Hashtable<String, Object> connectionProperties, ArrayList<MQObject> objects) {
        establishMQConnection(queueManagerName, connectionProperties);
        this.objects = objects;
        this.object = new MQObject("*", objects.get(0).getType());
    }

    /**
     * Establishes connection with queue manager.
     *
     * @param queueManagerName     - queue manager name.
     * @param connectionProperties - map with all required connection params.
     */
    private void establishMQConnection(String queueManagerName, Hashtable<String, Object> connectionProperties) {
        if (connection == null) {
            connection = new MQConnection();
            connection.establish(queueManagerName, connectionProperties);
        }
        this.queueManagerName = queueManagerName;
        //TODO: error handling
        try {
            this.agent = new PCFMessageAgent(connection.getQueueManager());
        } catch (MQException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates specific metric which name doesn't contain wildcards.
     *
     * @param response   - PCFMessage object which contains response from queue manager about MQ object.
     * @param objectName - MQ object name (will be used as label for Prometheus).
     */
    private void updateMetricWithoutWildcards(PCFMessage response, String objectName) {
        Object result = response.getParameterValue(object.getPCFHeader());
        double prometheusValue = MetricsReference.getMetricValue(object.getType(), (Integer) result);
        MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), prometheusValue, queueManagerName, objectName);
    }

    /**
     * Parses PCF response from queue manager, retrieves info about all required objects and updates metrics.
     *
     * @param pcfResponse - PCFMessage object which contains response from queue manager about all MQ objects of specific type.
     */
    private void updateMetricsWithWildcards(PCFMessage[] pcfResponse) {
        ArrayList<String> objectNames = new ArrayList<>();
        //copy all objects names to temporary array
        for (MQObject object : objects) {
            objectNames.add(object.getName());
        }
        for (PCFMessage response : pcfResponse) {
            String objectName = (String) response.getParameterValue(MQObject.objectNameCode(object.getType()));
            objectName = objectName.trim();
            //if temporary array contains metric, then remove it from temporary array and update metric
            if (objectNames.contains(objectName)) {
                objectNames.remove(objectName);
                Object result = response.getParameterValue(object.getPCFHeader());
                double prometheusValue = MetricsReference.getMetricValue(object.getType(), (Integer) result);
                MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), prometheusValue, queueManagerName, objectName);
            }
        }

        //There are some objects in temporary array? It means that "*" wildcard didn't return all values.
        //There are multiple reasons why it could happen. For example, MQ channel has status "inactive".
        //Then we send direct PCF command for specific object. If some error occurs, we have custom processing for it.
        if (objectNames.size() > 0) {
            for (String objectName : objectNames) {
                MQObject directObject = new MQObject(objectName, object.getType());
                try {
                    PCFMessage[] directPCFResponse = agent.send(directObject.getPCFCmd());
                    updateMetricWithoutWildcards(directPCFResponse[0], objectName);
                } catch (PCFException e) {
                    //This error means, that channel has status "inactive".
                    if (e.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
                        MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQCHS_INACTIVE), queueManagerName, objectName);
                    }
                } catch (MQException e) {
                    //TODO: error handling
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            PCFMessage[] pcfResponse = agent.send(object.getPCFCmd());
            if (!objects.isEmpty()) {
                updateMetricsWithWildcards(pcfResponse);
            } else {
                for (PCFMessage response : pcfResponse) {
                    updateMetricWithoutWildcards(response, object.getName());
                }
            }
        } catch (PCFException e) {
            if (e.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
                MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQCHS_INACTIVE), queueManagerName, object.getName());
            }
        } catch (MQException e) {
            //TODO: error handling
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
