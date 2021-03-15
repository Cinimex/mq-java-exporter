package ru.cinimex.exporter.mq;

import static com.ibm.mq.constants.MQConstants.MQCHS_INACTIVE;
import static com.ibm.mq.constants.MQConstants.MQRCCF_CHL_STATUS_NOT_FOUND;
import static com.ibm.mq.constants.MQConstants.MQRC_CONNECTION_BROKEN;
import static com.ibm.mq.constants.MQConstants.MQRC_NOT_CONNECTED;
import static com.ibm.mq.constants.MQConstants.MQRC_Q_MGR_QUIESCING;
import static com.ibm.mq.constants.MQConstants.MQRC_UNKNOWN_OBJECT_NAME;
import static com.ibm.mq.constants.MQConstants.MQSVC_STATUS_STOPPED;

import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

/**
 * MQPCFSubscriber is technically not a subscriber, but a runnable object, which sends PCFCommands every n seconds to
 * retrieve specific statistics, which couldn't be retrieved by MQTopicSubscriber.
 */
public class MQPCFSubscriber extends Thread implements MQSubscriber {

    private static final Logger logger = LogManager.getLogger(MQPCFSubscriber.class);
    private final String queueManagerName;
    private MQObject object;
    private PCFMessageAgent agent;
    private CopyOnWriteArraySet<MQObject> objects;
    private boolean isRunning;

    /**
     * MQPCFSubscriber constructor which is used, when exporter is configured to use 1 MQPCFSubscriber per all MQObjects
     * of the same type.
     *
     * @param queueManagerName - queue manager name.
     */
    public MQPCFSubscriber(String queueManagerName) {
        this.queueManagerName = queueManagerName;
        try {
            this.agent = new PCFMessageAgent(MQConnection.getQueueManager());
            this.isRunning = true;
        } catch (MQDataException e) {
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
                if (object.getType() == MQObject.MQType.CHANNEL && e.reasonCode == MQRCCF_CHL_STATUS_NOT_FOUND) {
                    logger.debug("Channel {} is possibly inactive.", objectName);
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()),
                        MetricsReference.getMetricValue(object.getType(), MQCHS_INACTIVE), queueManagerName,
                        objectName);
                } else if (object.getType() == MQObject.MQType.LISTENER && e.reasonCode == MQRC_UNKNOWN_OBJECT_NAME) {
                    MetricsManager.updateMetric(MetricsReference.getMetricName(object.getType()),
                        MetricsReference.getMetricValue(object.getType(), MQSVC_STATUS_STOPPED),
                        queueManagerName, objectName);
                    logger.debug("Listener {} is possibly stopped.", objectName);
                } else {
                    logger.error("Error occurred during sending PCF command: ", e);
                }
            } catch (IOException | MQDataException e) {
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
        } catch (MQDataException e) {
            logger.warn("Unable to close PCF agent gracefully: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        if (isRunning) {
            try {
                if (objects == null) {
                    return;
                }
                logger
                    .debug("Sending PCF command for object type {} with name {}...", object.getType(), object.getName());
                PCFMessage[] pcfResponse = agent.send(object.getPcfCmd());
                updateMetricsWithWildcards(pcfResponse);
                logger
                    .debug("PCF response for object type {} with name {} was processed successfully.", object.getType(),
                        object.getName());
            } catch (PCFException e) {
                if (object.getType() == MQObject.MQType.CHANNEL && e.reasonCode == MQRCCF_CHL_STATUS_NOT_FOUND) {
                    logger.debug("All channels are possibly inactive.");
                    for (MQObject channel : objects){
                        double prometheusValue = MetricsReference.getMetricValue(channel.getType(), MQCHS_INACTIVE);
                        MetricsManager.updateMetric(MetricsReference.getMetricName(channel.getType()), prometheusValue, queueManagerName, channel.getName());
                    }
                } else if (object.getType() == MQObject.MQType.LISTENER && e.reasonCode == MQRC_UNKNOWN_OBJECT_NAME) {
                    logger.debug("Listeners are possibly stopped.");
                    for (MQObject listener : objects){
                        double prometheusValue = MetricsReference.getMetricValue(listener.getType(), MQSVC_STATUS_STOPPED);
                        MetricsManager.updateMetric(MetricsReference.getMetricName(listener.getType()), prometheusValue, queueManagerName, listener.getName());
                    }
                } else if (e.getReason() == MQRC_Q_MGR_QUIESCING) {
                    logger.error("Queue manager is quiescing: ", e);
                    System.exit(1);
                } else {
                    logger.error("Error occurred during sending PCF command: ", e);
                }
            } catch (MQDataException e) {
                if (e.getReason() == MQRC_CONNECTION_BROKEN || e.getReason() == MQRC_Q_MGR_QUIESCING
                    || e.getReason() == MQRC_NOT_CONNECTED) {
                    logger.error("Connection with queue manager was closed: ", e);
                    System.exit(1);
                }
                logger.error("Error occurred during sending PCF command: ", e);
            } catch (IOException e1) {
                logger.error("Error occurred during sending PCF command: ", e1);
            }
        }
    }

    public void removeAll(List<MQObject> objectsToRemove) {
        if (objectsToRemove != null && !objectsToRemove.isEmpty()) {
            objects.removeAll(objectsToRemove);
        }
    }

    public void addAll(List<MQObject> objectsToAdd) {
        if (objectsToAdd != null && !objectsToAdd.isEmpty()) {
            if (this.objects == null) {
                this.objects = new CopyOnWriteArraySet<>(objectsToAdd);
                this.object = new MQObject("*", objectsToAdd.get(0).getType());
            } else {
                objects.addAll(objectsToAdd);
            }
        }
    }

}
