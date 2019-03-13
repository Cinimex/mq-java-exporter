package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.pcf.PCFElement;

import java.util.ArrayList;
import java.util.Hashtable;
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
    private ArrayList<Thread> subscribers;

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
     */
    public void runSubscribers(ArrayList<PCFElement> elements, ArrayList<MQObject> objects, boolean sendPCFCommands, boolean usePCFWildcards, int interval) {
        logger.info("Launching subscribers...");
        subscribers = new ArrayList<>();
        int i = 0;
        for (PCFElement element : elements) {
            if (!element.requiresMQObject()) {
                try {
                    subscribers.add(i, new Thread(new MQTopicSubscriber(element, queueManagerName, connectionProperties, queueManagerName)));
                    logger.debug("Starting subscriber for {}...", element.getTopicString());
                    subscribers.get(i).start();
                    logger.debug("Subscriber for {} was started.", element.getTopicString());
                    i++;
                } catch (MQException e) {
                    logger.error("Error during creating topic subscriber: ", e);
                }
            } else {
                for (MQObject object : objects) {
                    if (object.getType() == MQObject.MQType.QUEUE) {
                        PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
                        objElement.formatTopicString(object.getName());
                        try {
                            subscribers.add(i, new Thread(new MQTopicSubscriber(objElement, queueManagerName, connectionProperties, queueManagerName, object.getName())));
                            logger.debug("Starting subscriber for {}...", objElement.getTopicString());
                            subscribers.get(i).start();
                            logger.debug("Subscriber for {} was started.", objElement.getTopicString());
                            i++;
                        } catch (MQException e) {
                            logger.error("Error during creating topic subscriber: ", e);
                        }
                    }
                }
            }
        }

        if (sendPCFCommands) {
            int corePoolSize = usePCFWildcards ? MQObject.MQType.values().length : objects.size();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(corePoolSize);
            if (usePCFWildcards) {
                ArrayList<MQObject> queues = new ArrayList<MQObject>();
                ArrayList<MQObject> channels = new ArrayList<MQObject>();
                ArrayList<MQObject> listeners = new ArrayList<MQObject>();
                for (MQObject object : objects) {
                    switch (object.getType()) {
                        case QUEUE:
                            queues.add(object);
                            break;
                        case CHANNEL:
                            channels.add(object);
                            break;
                        case LISTENER:
                            listeners.add(object);
                            break;
                        default:
                            logger.error("Error during parsing objects list: Unknown object type! Make sure it is one of: {}", MQObject.MQType.values());
                    }
                }

                if (queues.size() > 0) {
                    MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, queues);
                    subscribers.add(i++, new Thread(subscriber));
                    logger.debug("Starting subscriber for sending direct PCF commands about queues max depth...");
                    executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                    logger.debug("Subscriber for sending direct PCF commands about queues max depth successfully " + "started.");
                }
                if (channels.size() > 0) {
                    MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, channels);
                    subscribers.add(i++, new Thread(subscriber));
                    logger.debug("Starting subscriber for sending direct PCF commands about channels statuses...");
                    executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                    logger.debug("Subscriber for sending direct PCF commands about channels statuses successfully " + "started.");
                }
                if (listeners.size() > 0) {
                    MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, listeners);
                    subscribers.add(i++, new Thread(subscriber));
                    logger.debug("Starting subscriber for sending direct PCF commands about listeners statuses...");
                    executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                    logger.debug("Subscriber for sending direct PCF commands about listeners statuses successfully " + "started.");
                }
            } else {
                for (MQObject object : objects) {
                    MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, object);
                    subscribers.add(i++, new Thread(subscriber));
                    logger.debug("Starting subscriber for sending direct PCF commands to retrieve statistics about " + "object with type {} and name {}.", object.getType().name(), object.getName());
                    executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                    logger.debug("Subscriber for sending direct PCF commands to retrieve statistics about " + "object with type {} and name {} successfully started.", object.getType().name(), object.getName());
                }
            }
        }
        if (subscribers.size() > 0) {
            logger.info("Successfully launched {} subscribers!", subscribers.size());
        } else {
            logger.warn("Didn't launch any subscriber. Exporter finishes it's work!", subscribers.size());
            System.exit(1);
        }

    }

}
