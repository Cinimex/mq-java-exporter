package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.MQConstants;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.prometheus.metrics.GaugeManager;

import java.util.*;

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
     * @param elements          - all elements, retrieved from target queue manager.
     * @param monitoringObjects - all monitoring objects.
     */
    public void runSubscribers(ArrayList<PCFElement> elements, ArrayList<String> monitoringObjects) {
        subscribers = new ArrayList<>();
        int i = 0;
        for (PCFElement element : elements) {
            if (!element.requiresMQObject()) {
                subscribers.add(i, new Thread(new Subscriber(element, queueManagerName)));
                subscribers.get(i).start();
                i++;
            } else {
                for (String object : monitoringObjects) {
                    PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
                    objElement.formatTopicString(object);
                    subscribers.add(i, new Thread(new Subscriber(objElement, queueManagerName, object)));
                    subscribers.get(i).start();
                    i++;
                }
            }
        }
    }

    /**
     * Subscriber is used to subscribe to specific topic.
     */
    class Subscriber implements Runnable {
        private MQTopic topic;
        private PCFElement element;
        private MQConnection connection;
        private String[] labels;

        /**
         * Subscriber constructor
         *
         * @param element - PCF message data, which is required for parsing statistics.
         * @param labels  - labels array, which should be used for metrics.
         */
        public Subscriber(PCFElement element, String... labels) {
            this.element = element;
            if (connection == null) {
                connection = new MQConnection();
                connection.establish(queueManagerName, connectionProperties);
            }
            this.labels = labels;
        }

        /**
         * Starts subscriber.
         */
        public void run() {
            try {
                topic = connection.createTopic(element.getTopicString());
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_COMPLETE_MSG;
                gmo.waitInterval = 12000;
                while (true) {
                    try {
                        MQMessage msg = new MQMessage();
                        topic.get(msg, gmo);
                        HashMap<Integer, Double> receivedMetrics = PCFDataParser.getParsedData(PCFDataParser.convertToPCF(msg));
                        Iterator<Map.Entry<Integer, Double>> it = receivedMetrics.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<Integer, Double> pair = it.next();
                            //TODO: There is some exception during startup. Need do define, why it occurs (there is unmapped id)
                            GaugeManager.updateMetric(PCFDataParser.getMetricName(element.getMetricDescription(pair.getKey()), element.requiresMQObject()), pair.getValue(), labels);
                            it.remove();
                        }
                    } catch (MQException e) {
                        if (e.getReason() == 2033) System.out.println("No messages in " + element.getTopicString());
                    }
                }
            } catch (MQException e) {
                System.out.println("An error occured while trying to get queue object " + element.getTopicString());
                System.err.println(e.getStackTrace());
            } finally {
                if (topic != null && topic.isOpen()) {
                    try {
                        topic.close();
                        connection.close();
                    } catch (MQException e) {
                        System.err.println(e.getStackTrace());
                    }
                }
            }
        }
    }
}
