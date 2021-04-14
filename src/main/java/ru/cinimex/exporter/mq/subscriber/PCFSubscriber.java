package ru.cinimex.exporter.mq.subscriber;

import static com.ibm.mq.constants.CMQC.MQRC_CONNECTION_BROKEN;
import static com.ibm.mq.constants.CMQC.MQRC_NOT_CONNECTED;
import static com.ibm.mq.constants.CMQC.MQRC_Q_MGR_QUIESCING;

import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQConnection;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;
import ru.cinimex.exporter.util.Pair;

/**
 * MQPCFSubscriber is technically not a subscriber, but a runnable object, which sends PCFCommands every n seconds to
 * retrieve specific statistics, which couldn't be retrieved by MQTopicSubscriber.
 */
public abstract class PCFSubscriber extends Thread {

    private static final Logger logger = LogManager.getLogger(PCFSubscriber.class);
    protected final String queueManagerName;
    protected MQObject object;
    private PCFMessageAgent agent;
    protected CopyOnWriteArraySet<MQObject> objects;
    private AtomicBoolean isRunning;

    /**
     * PCFSubscriber constructor.
     *
     * @param queueManagerName - queue manager name.
     */
    protected PCFSubscriber(String queueManagerName) {
        this.queueManagerName = queueManagerName;
        try {
            this.agent = new PCFMessageAgent(MQConnection.getQueueManager(false));
            this.isRunning = new AtomicBoolean(true);
        } catch (MQDataException e) {
            logger.error("Error occurred during creating PCF subscriber: ", e);
        }
    }

    /**
     * Updates specific metric which name doesn't contain wildcards.
     *
     * @param response   - PCFMessage object which contains response from queue manager about MQ object.
     * @param objectName - MQ object name (will be used as label for Prometheus).
     */
    protected void updateMetrics(PCFMessage response, String objectName) {
        for (Pair<Integer, String> mapping : object.getPcfHeadersToMetricMappings()) {
            Object result = response.getParameterValue(mapping.getFirst());
            double prometheusValue = MetricsReference.getMetricValue(object.getType(), (Integer) result);
            MetricsManager
                .updateMetric(mapping.getSecond(), prometheusValue, queueManagerName,
                    objectName);
        }
    }

    /**
     * Parses PCF response from queue manager, retrieves info about all required objects and updates metrics.
     *
     * @param pcfResponse - PCFMessage object which contains response from queue manager about all MQ objects of specific type.
     */
    protected void updateMetricsWithWildcards(PCFMessage[] pcfResponse) {
        List<String> monitoredObjectNames = new ArrayList<>();
        Map<String, PCFMessage> retrievedMetrics = new HashMap<>();

        //copy all objects names to temporary array
        for (MQObject monitoredObject : objects) {
            monitoredObjectNames.add(monitoredObject.getName());
        }

        //put all retrieved objects names to temporary array
        for (PCFMessage response : pcfResponse) {
            String objectName = (String) response.getParameterValue(MQObject.objectNameCode(object.getType()));
            retrievedMetrics.put(objectName.trim(), response);
        }

        retrievedMetrics.forEach((objectName, pcfMessage) -> {
            //if temporary array contains metric, then remove it from temporary array and update metric
            if (monitoredObjectNames.contains(objectName)) {
                monitoredObjectNames.remove(objectName);
                updateMetrics(pcfMessage, objectName);
            }
        });

        //Are there any objects left in temporary array? It means that "*" wildcard didn't return all values.
        //There are multiple reasons why it could happen. For example, MQ channel has status "inactive".
        //Then we send direct PCF command for specific object. If some error occurs, we have custom processing for it.
        updateWithDirectPCFCommand(monitoredObjectNames);
    }

    /**
     * Retrieves info about all objects from input array via direct pcf commands.
     *
     * @param objectNames - input list with objects.
     */
    protected void updateWithDirectPCFCommand(List<String> objectNames) {
        for (String objectName : objectNames) {
            MQObject directObject = new MQObject(objectName, object.getType());
            try {
                sendDirectPCFAndUpdateMetrics(directObject);
            } catch (IOException | MQDataException e) {
                logger.error("Error occurred during sending PCF command: ", e);
            }
        }
    }

    protected void updateWithWildcardPCFCommand() {
        try {
            sendWildcardPCFAndUpdateMetrics();
        } catch (PCFException e) {
            if (e.getReason() == MQRC_Q_MGR_QUIESCING) {
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

    /**
     * Retrieves info about all objects from input array via wildcard pcf commands.
     */
    protected void sendWildcardPCFAndUpdateMetrics() throws IOException, MQDataException {
        logger
            .debug("Sending PCF command for object type {} with name {}...", object.getType(),
                object.getName());
        PCFMessage[] pcfResponse = agent.send(object.getPcfCmd());
        updateMetricsWithWildcards(pcfResponse);
        logger
            .debug("PCF response for object type {} with name {} was processed successfully.", object.getType(),
                object.getName());
    }

    protected void sendDirectPCFAndUpdateMetrics(MQObject mqObject) throws IOException, MQDataException {
        PCFMessage[] directPCFResponse = agent.send(mqObject.getPcfCmd());
        updateMetrics(directPCFResponse[0], mqObject.getName());
    }

    @Override
    public void run() {
        if (isRunning.get() && objects != null && !objects.isEmpty()) {
            updateWithWildcardPCFCommand();
        }
    }

    /**
     * Stops subscriber.
     */
    public void stopProcessing() {
        isRunning.set(false);
        try {
            if (agent != null) {
                agent.disconnect();
            }
        } catch (MQDataException e) {
            logger.warn("Unable to close PCF agent gracefully: {}", e.getMessage());
        }
    }

    /**
     * Removes objects from monitoring.
     *
     * @param objectsToRemove - objects to be removed.
     */
    public void removeAll(List<MQObject> objectsToRemove) {
        if (objectsToRemove != null && !objectsToRemove.isEmpty()) {
            objectsToRemove.forEach(objects::remove);
        }
    }

    /**
     * Adds objects for monitoring.
     *
     * @param objectsToAdd - objects for monitoring.
     */
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
