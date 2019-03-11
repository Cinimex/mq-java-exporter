package ru.cinimex.exporter.prometheus.metrics;

import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.mq.pcf.PCFElementRow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class is used to manage work of all gauges.
 */
public class GaugeManager {
    public static HashMap<String, SimpleGauge> metrics;

    /**
     * Creates all required gauges
     *
     * @param elements - Array, which contains all PCFElements, retrieved from queue manager.
     */
    public static void initGauges(ArrayList<PCFElement> elements) {
        metrics = new HashMap<>();
        for (PCFElement element : elements) {
            for (PCFElementRow row : element.getRows()) {
                String metricName = MetricsReference.getMetricName(row.getRowDesc(), element.requiresMQObject(),
                        row.getRowDatatype());
                if (element.requiresMQObject()) {
                    metrics.put(metricName, new SimpleGauge(metricName, row.getRowDesc(), Labels.QMGR_NAME.name(), Labels.MQ_OBJECT_NAME.name()));
                } else {
                    metrics.put(metricName, new SimpleGauge(metricName, row.getRowDesc(), Labels.QMGR_NAME.name()));
                }
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
    public enum Labels {
        QMGR_NAME, MQ_OBJECT_NAME
    }
}
