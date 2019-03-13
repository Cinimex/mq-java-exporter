package ru.cinimex.exporter.prometheus.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.mq.pcf.PCFElementRow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class is used to manage work of all metrics.
 */
public class MetricsManager {
    private static final Logger logger = LogManager.getLogger(MetricsManager.class);
    private static HashMap<String, MetricInterface> metrics;

    /**
     * Creates all required metrics
     *
     * @param elements - Array, which contains all PCFElements, retrieved from queue manager.
     * @param types    - Array, which contains all MQObject types, which should be monitored via direct PCFCommands.
     */
    public static void initMetrics(ArrayList<PCFElement> elements, ArrayList<MQObject.MQType> types) {
        logger.debug("Preparing to initialize metrics. {} metrics will be received from MQ topics and {} metrics will be received via direct PCF commands.", elements.size(), types.size());
        metrics = new HashMap<String, MetricInterface>();
        for (PCFElement element : elements) {
            for (PCFElementRow row : element.getRows()) {
                String metricName = MetricsReference.getMetricName(row.getRowDesc(), element.requiresMQObject(), row.getRowDatatype());
                MetricsReference.Metric.Type metricType = MetricsReference.getMetricType(row.getRowDesc(), element.requiresMQObject());
                ArrayList<String> labels = new ArrayList<>();
                labels.add(Labels.QMGR_NAME.name());
                MetricInterface metric;
                if (element.requiresMQObject()) {
                    labels.add(Labels.MQ_OBJECT_NAME.name());
                }
                switch (metricType) {
                    case SimpleGauge:
                        metric = new SimpleGauge(metricName, row.getRowDesc(), labels.stream().toArray(String[]::new));
                        metrics.put(metricName, metric);
                        logger.trace("New gauge created! Name: {}, description: {}, labels: {}.", metricName, row.getRowDesc(), labels);
                        break;
                    case SimpleCounter:
                        metric = new SimpleCounter(metricName, row.getRowDesc(), labels.stream().toArray(String[]::new));
                        metrics.put(metricName, metric);
                        logger.trace("New counter created! Name: {}, description: {}, labels: {}.", metricName, row.getRowDesc(), labels);
                        break;
                    default:
                        logger.error("Error during metrics initialization: Unknown metric type! Make sure it is one " + "of: {}", MetricsReference.Metric.Type.values());
                }
            }
        }
        for (MQObject.MQType type : types) {
            String metricName = MetricsReference.getMetricName(type);
            metrics.put(metricName, new SimpleGauge(metricName, MetricsReference.getMetricHelp(type), Labels.QMGR_NAME.name(), Labels.MQ_OBJECT_NAME.name()));
            logger.trace("New gauge created! Name: {}, description: {}, labels: {}.", metricName, MetricsReference.getMetricHelp(type), Labels.MQ_OBJECT_NAME.name());
        }
        logger.info("Successfully initialized {} metrics!", metrics.size());
    }

    /**
     * Updates specific metric
     *
     * @param metricName - metric name
     * @param value      - metric value
     * @param labels     - metric labels
     */
    public static void updateMetric(String metricName, Double value, String... labels) {
        metrics.get(metricName).update(value, labels);
    }

    /**
     * Contains all possible labels
     */
    public enum Labels {QMGR_NAME, MQ_OBJECT_NAME}
}
