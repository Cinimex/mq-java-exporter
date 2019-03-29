package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.Gauge;
import ru.cinimex.exporter.prometheus.Registry;

/**
 * Represents Prometheus basic metric. Counter, which can increase and decrease.
 */
public class SimpleGauge implements MetricInterface {
    private Gauge gauge;

    /**
     * Gauge constructor.
     *
     * @param metricName - metric name.
     * @param metricInfo - metric info (description).
     * @param labelNames - labels.
     *                   <p>
     *                   More about metrics <a href=https://prometheus.io/docs/practices/naming/>here</a>.
     */
    public SimpleGauge(String metricName, String metricInfo, String... labelNames) {
        gauge = Gauge.build().name(metricName).help(metricInfo).labelNames(labelNames).register(Registry.getRegistry());
    }

    /**
     * Sets gauge to exact value.
     *
     * @param value       - new value.
     * @param labelValues - value labels.
     */
    private void setGauge(double value, String... labelValues) {
        gauge.labels(labelValues).set(value);
    }

    @Override
    public void update(double value, String... labels) {
        setGauge(value, labels);
    }

    @Override
    public void notifyWasScraped() {
        //There is no need to do any work after gauge was scraped.
    }
}