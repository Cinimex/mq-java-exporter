package ru.cinimex.exporter.mq;

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
     * @param elements - elements, received via MQ monitoring topics.
     * @param objects - objects, retrieved from configuration file.
     * @param sendPCFCommands - this flag indicates, if we should send additional PCF commands (To get queues max depth, channels and listeners statuses).
     * @param usePCFWildcards - this flag indicates, if we should use wildcards (uses only 1 connection per MQObject type, but longer response processing).
     * @param interval - interval in seconds, at which additional PCF commands are sent.
     */
    public void runSubscribers(ArrayList<PCFElement> elements, ArrayList<MQObject> objects, boolean sendPCFCommands, boolean usePCFWildcards, int interval) {
        subscribers = new ArrayList<>();
        int i = 0;
        for (PCFElement element : elements) {
            if (!element.requiresMQObject()) {
                subscribers.add(i, new Thread(new MQTopicSubscriber(element, queueManagerName, connectionProperties, queueManagerName)));
                subscribers.get(i).start();
                i++;
            } else {
                for (MQObject object : objects) {
                    if (object.getType() == MQObject.MQType.QUEUE) {
                        PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
                        objElement.formatTopicString(object.getName());
                        subscribers.add(i, new Thread(new MQTopicSubscriber(objElement, queueManagerName, connectionProperties, queueManagerName, object.getName())));
                        subscribers.get(i).start();
                        i++;
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
                    }
                }

                MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, queues);
                subscribers.add(i++, new Thread(subscriber));
                executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);

                subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, channels);
                subscribers.add(i++, new Thread(subscriber));
                executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);

                subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, listeners);
                subscribers.add(i++, new Thread(subscriber));
                executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
            } else {
                for (MQObject object : objects) {
                    MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties, object);
                    subscribers.add(i++, new Thread(subscriber));
                    executor.scheduleAtFixedRate(subscriber, 0, interval, TimeUnit.SECONDS);
                }
            }
        }

    }
}
