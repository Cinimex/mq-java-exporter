package ru.cinimex.exporter.mq.subscriber;

import com.ibm.mq.MQException;
import com.ibm.mq.MQTopic;
import com.ibm.mq.headers.pcf.PCFMessage;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQConnection;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

/**
 * MQTopicSubscriber is used to process metrics from specific topic.
 */
public class MQTopicSubscriber {

    private static final Logger logger = LogManager.getLogger(MQTopicSubscriber.class);
    private final PCFElement element;
    private final String[] labels;
    private final Map<Integer, String> metricNames;
    private MQTopic topic;

    /**
     * Subscriber constructor
     *
     * @param element - PCF message data, which is required for parsing statistics.
     * @param labels  - labels array, which should be used for metrics.
     */
    public MQTopicSubscriber(PCFElement element, String... labels) {
        this.element = element;
        this.labels = labels;
        this.metricNames = new ConcurrentHashMap<>();

        try {
            topic = MQConnection.createSpecificTopic(element.getTopicString());
        } catch (MQException e) {
            logger.error("Error occurred during establishing connection with topic {}", element.getTopicString(), e);
        }
    }

    public boolean relatedToObject(String objectName) throws MQException {
        if(!element.requiresMQObject()){
            return false;
        }

        return topic.getName().contains("/" + objectName + "/");
    }

    /**
     * Stops subscriber.
     */
    public void stopProcessing() {
        if (topic != null && topic.isOpen()) {
            try {
                topic.getSubscriptionReference().close();
            } catch (MQException e) {
                logger.warn("Unable to close topic gracefully: {}", e.getMessage());
            }
        }
    }

    /**
     * Method is used by MQMetricQueue for processing metrics.
     *
     * @param pcf - message with metrics, received from queue manager.
     */
    public void update(PCFMessage pcf) {
        Map<Integer, Double> receivedMetrics = PCFDataParser.getParsedData(pcf);
        Iterator<Map.Entry<Integer, Double>> it = receivedMetrics.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Double> pair = it.next();
            int id = pair.getKey();
            double value = PCFDataParser.getExactValue(pair.getValue(), element.getRowDatatype(id));

            String metricName = metricNames.get(id);
            if(metricName == null){
                metricName = MetricsReference.getMetricName(element.getMetricDescription(id), element.requiresMQObject(), element.getRowDatatype(id));
                metricNames.put(id, metricName);
            }

            MetricsManager.updateMetric(metricName, value, labels);
            it.remove();
        }
    }

    /**
     * Getter for topic string. Returns null (and writes error into logs) if retrieving topic string was unsuccessful.
     *
     * @return - topic string.
     */
    public String getTopicName() {
        try {
            return topic.getName();
        } catch (MQException e) {
            logger.error("Unable to get topic name.", e);
        }
        return null;
    }
}
