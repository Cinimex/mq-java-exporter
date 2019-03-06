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
     * Creates subscribers and starts them.
     *
     * @param elements         - all elements, retrieved from target queue manager.
     * @param monitoringQueues - all monitoring objects.
     */
    public void runSubscribers(ArrayList<PCFElement> elements, ArrayList<String> monitoringQueues, ArrayList<String> monitoringListeners, ArrayList<String> monitoringChannels, boolean sendPCFCommands, boolean collectQueueMaxDepth, boolean usePCFWildcards) {
        subscribers = new ArrayList<>();
        int i = 0;
        for (PCFElement element : elements) {
            if (!element.requiresMQObject()) {
                subscribers.add(i, new Thread(new MQTopicSubscriber(element, queueManagerName, connectionProperties, queueManagerName)));
                subscribers.get(i).start();
                i++;
            } else {
                for (String object : monitoringQueues) {
                    PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
                    objElement.formatTopicString(object);
                    subscribers.add(i, new Thread(new MQTopicSubscriber(objElement, queueManagerName, connectionProperties, queueManagerName, object)));
                    subscribers.get(i).start();
                    i++;
                }
            }
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        for(String queue : monitoringQueues){
            MQPCFSubscriber subscriber = new MQPCFSubscriber(queueManagerName, connectionProperties,
                    new MQObject(queue, MQObject.MQType.QUEUE));
            subscribers.add(i, new Thread(subscriber));
            executor.scheduleAtFixedRate(subscriber, 0, 10, TimeUnit.SECONDS);
        }
   /*     if (sendPCFCommands) {
            if (collectQueueMaxDepth) {
                if (usePCFWildcards) {
                    subscribers.add(i, new Thread(new MQPCFSubscriber(String queueManagerName, Hashtable<String, Object> connectionProperties, MQObject object)));
                    subscribers.get(i).start();
                    i++;
                } else {
                    for (String object : monitoringQueues) {
                        subscribers.add(i, new Thread(new MQPCFSubscriber()));
                        subscribers.get(i).start();
                        i++;
                    }
                }
            }
        }*/

    }
}
