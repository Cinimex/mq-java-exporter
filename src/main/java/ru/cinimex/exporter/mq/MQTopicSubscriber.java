package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.MQConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * MQTopicSubscriber is used to subscribe to specific topic.
 */
public class MQTopicSubscriber implements Runnable {
    private static final Logger logger = LogManager.getLogger(MQTopicSubscriber.class);
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
    public MQTopicSubscriber(PCFElement element, String queueManagerName, Hashtable<String, Object> connectionProperties, String... labels) throws MQException {
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
                    logger.debug("Waiting for message on {} ...", element.getTopicString());
                    topic.get(msg, gmo);
                    logger.debug("Message received on {}", element.getTopicString());
                    HashMap<Integer, Double> receivedMetrics = PCFDataParser.getParsedData(PCFDataParser.convertToPCF(msg));
                    Iterator<Map.Entry<Integer, Double>> it = receivedMetrics.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer, Double> pair = it.next();
                        MetricsManager.updateMetric(MetricsReference.getMetricName(element.getMetricDescription(pair.getKey()), element.requiresMQObject(), element.getRowDatatype(pair.getKey().intValue())), pair.getValue(), labels);
                        it.remove();
                    }
                } catch (MQException e) {
                    if (e.getReason() == MQConstants.MQRC_NO_MSG_AVAILABLE)
                        logger.warn("No messages found in {}", element.getTopicString());
                    else
                        logger.error("Error occurred during retrieving message from {}: ", element.getTopicString(), e);
                }
            }
        } catch (MQException e) {
            logger.error("Error occurred during establishing connection with topic {}", element.getTopicString(), e);
        } finally {
            if (topic != null && topic.isOpen()) {
                try {
                    topic.close();
                    connection.close();
                } catch (MQException e) {
                    logger.error("Error occurred during disconnecting from topic {}. Error: ", topic.toString(), e);
                }
            }
        }
    }
}
