package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MQPCFSubscriber is technically not a subscriber, but a runnable object, which sends PCFCommands every n seconds to
 * retrieve specific statistics, which couldn't be retrieved by MQTopicSubscriber.
 */
public class MQPCFSubscriber extends Thread implements MQSubscriber {
    private static final Logger logger = LogManager.getLogger(MQPCFSubscriber.class);
    private String queueManagerName;
    private MQObject object;
    private PCFMessageAgent agent;
    private List<MQObject> objects;
    private boolean isRunning;

    /**
     * MQPCFSubscriber constructor which is used, when exporter is configured to use 1 MQPCFSubscriber per 1 MQObject.
     *
     * @param object - MQObject which should be monitored.
     */
    public MQPCFSubscriber(MQObject object) {
        this.object = object;
        this.isRunning = true;
    }

    /**
     * MQPCFSubscriber constructor which is used, when exporter is configured to use 1 MQPCFSubscriber per all MQObjects of the same type.
     *
     * @param queueManagerName - queue manager name.
     * @param objects          - List with all MQObjects.
     */
    public MQPCFSubscriber(String queueManagerName, List<MQObject> objects) {
        this.objects = objects;
        this.queueManagerName = queueManagerName;
        this.object = new MQObject("*", objects.get(0).getType());
        try {
            this.agent = new PCFMessageAgent(MQConnection.getQueueManager());
            this.isRunning = true;
        } catch (MQException e) {
            logger.error("Error occured during creating PCF subscriber: ", e);
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
        for (MQObject monitoredObject : objects) {
            objectNames.add(monitoredObject.getName());
        }
        for (PCFMessage response : pcfResponse) {
            String objectName = (String) response.getParameterValue(MQObject.objectNameCode(object.getType()));
            objectName = objectName.trim();
            //if temporary array contains metric, then remove it from temporary array and update metric
            if (objectNames.contains(objectName)) {
                objectNames.remove(objectName);
                updateMetricWithoutWildcards(response, objectName);
            }
        }
        //Are there any objects left in temporary array? It means that "*" wildcard didn't return all values.
        //There are multiple reasons why it could happen. For example, MQ channel has status "inactive".
        //Then we send direct PCF command for specific object. If some error occurs, we have custom processing for it.
        updateWithDirectPCFCommand(objectNames);
    }

    /**
     * Retrieves info about all objects from input array via direct pcf commands.
     *
     * @param objectNames - input list with objects.
     */
    private void updateWithDirectPCFCommand(List<String> objectNames) {
        for (String objectName : objectNames) {
            MQObject directObject = new MQObject(objectName, object.getType());
            try {
                PCFMessage[] directPCFResponse = agent.send(directObject.getPcfCmd());
                updateMetricWithoutWildcards(directPCFResponse[0], objectName);
            } catch (PCFException e) {
                //This error means, that channel has status "inactive".
                if (object.getType() == MQObject.MQType.CHANNEL && e.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
                    logger.warn("Channel {} is possibly inactive.", objectName);
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQCHS_INACTIVE), queueManagerName, objectName);
                } else if (object.getType() == MQObject.MQType.LISTENER && e.reasonCode == MQConstants.MQRC_UNKNOWN_OBJECT_NAME) {
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQSVC_STATUS_STOPPED), queueManagerName, objectName);
                    logger.warn("Listener {} is possibly stopped.", objectName);
                } else {
                    logger.error("Error occurred during sending PCF command: ", e);
                }
            } catch (IOException | MQException e) {
                logger.error("Error occurred during sending PCF command: ", e);
            }
        }
    }

    /**
     * Stops subscriber.
     */
    public void stopProcessing() {
        isRunning = false;
        try {
            if (agent != null) {
                agent.disconnect();
            }
        } catch (MQException e) {
            logger.warn("Unable to close PCF agent gracefully: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        if (isRunning) {
            try {
                logger.debug("Sending PCF command for object type {} with name {}...", object.getType(), object.getName());
                PCFMessage[] pcfResponse = agent.send(object.getPcfCmd());
                if (objects != null && !objects.isEmpty()) {
                    updateMetricsWithWildcards(pcfResponse);
                } else {
                    for (PCFMessage response : pcfResponse) {
                        updateMetricWithoutWildcards(response, object.getName());
                    }
                }
                logger.debug("PCF response for object type {} with name {} was processed successfully.", object.getType(), object.getName());
            } catch (PCFException e) {
                if (object.getType() == MQObject.MQType.CHANNEL && e.reasonCode == MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND) {
                    logger.warn("Channel {} is possibly inactive.", object.getName());
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQCHS_INACTIVE), queueManagerName, object.getName());
                } else if (object.getType() == MQObject.MQType.LISTENER && e.reasonCode == MQConstants.MQRC_UNKNOWN_OBJECT_NAME) {
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()), MetricsReference.getMetricValue(object.getType(), MQConstants.MQSVC_STATUS_STOPPED), queueManagerName, object.getName());
                    logger.warn("Listener {} is possibly stopped.", object.getName());
                } else if (e.getReason() == MQConstants.MQRC_Q_MGR_QUIESCING) {
                    logger.error("Queue manager is quiescing: ", e.getLocalizedMessage());
                    System.exit(1);
                } else {
                    logger.error("Error occurred during sending PCF command: ", e);
                }
            } catch (MQException e) {
                if (e.getReason() == MQConstants.MQRC_CONNECTION_BROKEN || e.getReason() == MQConstants.MQRC_Q_MGR_QUIESCING || e.getReason() == MQConstants.MQRC_NOT_CONNECTED) {
                    logger.error("Connection with queue manager was closed: ", e);
                    System.exit(1);
                }
                logger.error("Error occurred during sending PCF command: ", e);
            } catch (IOException e1) {
                logger.error("Error occurred during sending PCF command: ", e1);
            }
        }
    }

}
