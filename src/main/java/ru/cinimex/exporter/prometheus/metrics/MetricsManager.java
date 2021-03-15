package ru.cinimex.exporter.prometheus.metrics;

import static ru.cinimex.exporter.prometheus.metrics.MetricManagerUtils.getConversionFunction;
import static ru.cinimex.exporter.prometheus.metrics.MetricManagerUtils.getMetricsUsedToUpdate;
import static ru.cinimex.exporter.prometheus.metrics.MetricManagerUtils.getUpdatedMetricNames;
import static ru.cinimex.exporter.prometheus.metrics.MetricsReference.getAdditionalMqObjectMetricsReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.mq.MQObject.MQType;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;
import ru.cinimex.exporter.mq.pcf.model.PCFElementRow;

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
     */
    public static void initMetrics(List<PCFElement> elements) {
        logger.debug("Preparing to initialize metrics. {} metrics will be received from MQ topics and {} metrics will be received via direct PCF commands.", elements.size(), MQType.values().length);
        String logString = " created! Name: {}, description: {}, labels: {}.";
        metrics = new HashMap<>();
        for (PCFElement element : elements) {
            for (PCFElementRow row : element.getRows()) {
                String metricName = MetricsReference.getMetricName(row.getRowDesc(), element.requiresMQObject(), row.getRowDatatype());
                MetricsReference.Metric.Type metricType = MetricsReference.getMetricType(row.getRowDesc(), element.requiresMQObject());
                ArrayList<String> labels = new ArrayList<>();
                labels.add(Labels.QMGR_NAME.name());
                MetricInterface metric;
                if (element.requiresMQObject()) labels.add(Labels.MQ_OBJECT_NAME.name());
                switch (metricType) {
                    case SIMPLE_GAUGE:
                        metric = new SimpleGauge(metricName, row.getRowDesc(), labels.toArray(new String[0]));
                        metrics.put(metricName, metric);
                        logger.trace("New gauge" + logString, metricName, row.getRowDesc(), labels);
                        break;
                    case SIMPLE_COUNTER:
                        metric = new SimpleCounter(metricName, row.getRowDesc(), labels.toArray(new String[0]));
                        metrics.put(metricName, metric);
                        logger.trace("New counter " + logString, metricName, row.getRowDesc(), labels);
                        break;
                    case EXTREME_GAUGE_MAX:
                        metric = new ExtremeGauge(metricName, row.getRowDesc(), true, labels.toArray(new String[0]));
                        metrics.put(metricName, metric);
                        logger.trace("New extreme gauge" + logString, metricName, row.getRowDesc(), labels);
                        break;
                    case EXTREME_GAUGE_MIN:
                        metric = new ExtremeGauge(metricName, row.getRowDesc(), false, labels.toArray(new String[0]));
                        metrics.put(metricName, metric);
                        logger.trace("New extreme gauge" + logString, metricName, row.getRowDesc(), labels);
                        break;
                    default:
                        logger.error(
                                "Error during metrics initialization: Unknown metric type! Make sure it is one " + "of: {}",
                                MetricsReference.Metric.Type.values());
                }
            }
        }
        for (MQObject.MQType type : MQObject.MQType.values()) {
            String metricName = MetricsReference.getMetricName(type);
            metrics.put(metricName, new SimpleGauge(metricName, MetricsReference.getMetricHelp(type), Labels.QMGR_NAME.name(), Labels.MQ_OBJECT_NAME.name()));
            logger.trace("New gauge created! Name: {}, description: {}, labels: {}.", metricName, MetricsReference.getMetricHelp(type), Labels.MQ_OBJECT_NAME.name());
        }
        initAdditionalMetrics();

        logger.info("Successfully initialized {} metrics!", metrics.size());
    }

    private static void initAdditionalMetrics() {
        getAdditionalMqObjectMetricsReference().forEach((metricInfo, metric) -> {
            List<String> labels = Arrays.asList(Labels.QMGR_NAME.name(), Labels.MQ_OBJECT_NAME.name());
            metrics.put(metric.name, new SimpleGauge(metric.name, metricInfo, labels.toArray(new String[0])));
            logger.trace("New gauge created! Name: {}, description: {}, labels: {}.", metric.name, metricInfo, labels);
        });
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
     * Removes all metrics with specific labels
     *
     * @param labels     - metric labels
     */
    public static void removeMetrics(String... labels) {
        metrics.values().forEach( metricInterface -> metricInterface.remove(labels));
    }

    /**
     * Updates additional metrics, for which need special conversion
     *
     * @param parsedQuery - parameter, needed for getting metric in special family metrics
     */
    public static void updateAdditionalMetrics(Set<String> parsedQuery) {
        getUpdatedMetricNames().forEach(updatedMetricName -> complexUpdateMetrics(
                getMetricsUsedToUpdate(
                        parsedQuery,
                        updatedMetricName),
                updatedMetricName));
        logger.trace("Additional metrics was updated");
    }

    private static void complexUpdateMetrics(
            Map<List<String>, List<Double>> metricsUsedToUpdate,
            String updatedMetricName) {
        if (metricsUsedToUpdate != null) {
            metricsUsedToUpdate.forEach((k, l) -> updateMetric(
                    updatedMetricName, getConversionFunction(updatedMetricName).apply(l), k.toArray(new String[0])));
            logger.trace("Additional metrics {} were updated", updatedMetricName);
        }
    }

    /**
     * Notifies all metrics after each Prometheus scrape.
     */
    public static void notifyMetricsWereScraped() {
        metrics.values().forEach(MetricInterface::notifyWasScraped);
    }

    /**
     * Contains all possible labels
     */
    public enum Labels {QMGR_NAME, MQ_OBJECT_NAME}
}
