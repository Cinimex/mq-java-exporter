package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.mq.pcf.PCFElementRow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class is used to manage work of all metrics.
 */
public class MetricsManager {
    public static HashMap<String, MetricInterface> metrics;

    /**
     * Creates all required metrics
     *
     * @param elements - Array, which contains all PCFElements, retrieved from queue manager.
     */
    public static void initMetrics(ArrayList<PCFElement> elements) {
        metrics = new HashMap<String, MetricInterface>();
        for (PCFElement element : elements) {
            for (PCFElementRow row : element.getRows()) {
                String metricName = MetricsReference.getMetricName(row.getRowDesc(), element.requiresMQObject(), row.getRowDatatype());
                ArrayList<String> labels = new ArrayList<>();
                labels.add(Labels.QMGR_NAME.name());
                MetricInterface metric;
                if (element.requiresMQObject()) {
                    labels.add(Labels.MQ_OBJECT_NAME.name());
                }
                if (row.getRowDatatype() == MQConstants.MQIAMO_MONITOR_DELTA) {
                    metric = new SimpleCounter(metricName, row.getRowDesc(), labels.stream().toArray(String[]::new));
                } else {
                    metric = new SimpleGauge(metricName, row.getRowDesc(), labels.stream().toArray(String[]::new));
                }

                metrics.put(metricName, metric);
            }
        }

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
