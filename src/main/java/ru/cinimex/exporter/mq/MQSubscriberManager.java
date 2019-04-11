package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.pcf.PCFElement;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class is used to manage work of all subscribers.
 */
public class MQSubscriberManager {
    private static final Logger logger = LogManager.getLogger(MQSubscriberManager.class);
    private Hashtable<String, Object> connectionProperties;
    private String queueManagerName;
    private ArrayList<MQSubscriber> subscribers;
    private ScheduledExecutorService executor;

    /**
     * Constructor sets params for connecting to target queue manager.
     *
     * @param host     - host, where queue manager is located.
     * @param port     - queue manager's port.
     * @param channel  - queue manager's channel.
     * @param qmName   - queue manager's name.
     * @param user     - user, which has enough privilege on the queue manager (optional).
     * @param password - password, which is required to establish connection with queue manager (optional).
     * @param useMQCSP - flag, which indicates, if MQCSP auth should be used.
     */
    public MQSubscriberManager(String host, int port, String channel, String qmName, String user, String password, boolean useMQCSP) {
        connectionProperties = MQConnection.createMQConnectionParams(host, port, channel, user, password, useMQCSP);
        queueManagerName = qmName;
    }

    /**
     * Creates pool with subscribers and starts them.
     *
     * @param elements        - elements, received via MQ monitoring topics.
     * @param objects         - objects, retrieved from configuration file.
     * @param sendPCFCommands - this flag indicates, if we should send additional PCF commands (To get queues max depth, channels and listeners statuses).
     * @param usePCFWildcards - this flag indicates, if we should use wildcards (uses only 1 connection per MQObject type, but longer response processing).
     * @param interval        - interval in seconds, at which additional PCF commands are sent.
     * @param timeout         - timeout for MQGET operation (milliseconds).
     */
    public void runSubscribers(List<PCFElement> elements, List<MQObject> objects, boolean sendPCFCommands, boolean usePCFWildcards, int interval, int timeout) {
        logger.info("Launching subscribers...");
        subscribers = new ArrayList<>();
        addTopicSubscribers(elements, objects, timeout);
        if (sendPCFCommands) {
            if (usePCFWildcards) {
                EnumMap<MQObject.MQType, ArrayList<MQObject>> groups = groupMQObjects(objects);
                addPCFSubscribers(groups, interval);
            } else {
                addPCFSubscribers(objects, interval);
            }
        }
        for (MQSubscriber subscriber : subscribers) {
            subscriber.start();
        }
        if (!subscribers.isEmpty()) {
            logger.info("Successfully launched {} subscribers!", subscribers.size());
        } else {
            logger.warn("Didn't launch any subscriber. Exporter finishes it's work!", subscribers.size());
            System.exit(1);
        }

    }

    public void stopSubscribers() {
        if (executor != null) {
            executor.shutdown();
        }
        for (MQSubscriber subscriber : subscribers) {
            subscriber.stopProcessing();
        }
    }

    /**
     * Add topic subscribers for each MQ object being monitored.
     *
     * @param elements - list with PCFElements, received from MQ.
     * @param objects  - list with monitored MQ objects.
     * @param timeout  - timeout for MQGET operation (milliseconds).
     */
    private void addTopicSubscribers(List<PCFElement> elements, List<MQObject> objects, int timeout) {
        for (PCFElement element : elements) {
            if (element.requiresMQObject()) {
                for (MQObject object : objects) {
                    addTopicSubscriber(object, element, timeout);
                }
            } else {
                addTopicSubscriber(element, timeout);
            }
        }
    }

    /**
     * Adds topic subscriber for specific MQ object
     *
     * @param object  - monitored MQ object.
     * @param element - PCFElement, received from MQ.
     * @param timeout - timeout for MQGET operation (milliseconds).
     */
    private void addTopicSubscriber(MQObject object, PCFElement element, int timeout) {
        if (object.getType().equals(MQObject.MQType.QUEUE)) {
            PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
            objElement.formatTopicString(object.getName());
            try {
                subscribers.add(new MQTopicSubscriber(objElement, queueManagerName, connectionProperties, timeout, queueManagerName, object.getName()));
            } catch (MQException e) {
                logger.error("Error during creating topic subscriber: ", e);
            }
        }
    }

    /**
     * Adds topic subscriber
     *
     * @param element - PCFElement, received from MQ.
     * @param timeout - timeout for MQGET operation (milliseconds).
     */
    private void addTopicSubscriber(PCFElement element, int timeout) {
        try {
            subscribers.add(new MQTopicSubscriber(element, queueManagerName, connectionProperties, timeout, queueManagerName));
        } catch (MQException e) {
            logger.error("Error during creating topic subscriber: ", e);
        }
    }

    /**
     * Adds PCF subscribers
     *
     * @param objects  - grouped map with objects, which require PCF subscribers
     * @param interval - interval between sending PCF commands.
     */
    private void addPCFSubscribers(Map<MQObject.MQType, ArrayList<MQObject>> objects, int interval) {
        int corePoolSize = MQObject.MQType.values().length;
        executor = Executors.newScheduledThreadPool(corePoolSize);
        for (Map.Entry<MQObject.MQType, ArrayList<MQObject>> entry : objects.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, entry.getValue());
                subscribers.add(subscriber);
                logger.debug("Starting subscriber for sending direct PCF commands to retrieve statistics about object with type {} and name {}.", entry.getKey().name());
                executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                logger.debug("Subscriber for sending direct PCF commands for objects with type {} successfully started.", entry.getKey().name());
            }
        }
    }

    /**
     * Adds PCF subscribers
     *
     * @param objects  - list with objects, which require PCF subscribers
     * @param interval - interval between sending PCF commands.
     */
    private void addPCFSubscribers(List<MQObject> objects, int interval) {
        int corePoolSize = objects.size();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(corePoolSize);
        for (MQObject object : objects) {
            MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, object);
            subscribers.add(subscriber);
            logger.debug("Starting subscriber for sending direct PCF commands to retrieve statistics about object with type {} and name {}.", object.getType().name(), object.getName());
            executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
            logger.debug("Subscriber for sending direct PCF commands to retrieve statistics about object with type {} and name {} successfully started.", object.getType().name(), object.getName());
        }
    }

    /**
     * Method groups elements by type.
     *
     * @param objects - list with unsorted MQ objects.
     * @return - grouped map with objects.
     */
    private EnumMap<MQObject.MQType, ArrayList<MQObject>> groupMQObjects(List<MQObject> objects) {
        EnumMap<MQObject.MQType, ArrayList<MQObject>> groupedObjects = new EnumMap<>(MQObject.MQType.class);
        for (MQObject.MQType type : MQObject.MQType.values()) {
            groupedObjects.put(type, new ArrayList<>());
        }
        for (MQObject object : objects) {
            groupedObjects.get(object.getType()).add(object);
            logger.debug("{} {} was added for additional monitoring.", object.getType().name(), object.getName());
        }
        return groupedObjects;
    }
}
