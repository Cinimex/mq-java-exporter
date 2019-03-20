package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.Gauge;
import ru.cinimex.exporter.prometheus.Registry;

/**
 * Represents custom Prometheus metric. Counter, which can increase and decrease. Used to store max or min value
 * between Prometheus scrapes.
 */
public class ExtremeGauge implements MetricInterface {
    private Gauge gauge;
    private boolean storeMax;
    private volatile boolean wasScraped = true;

    /**
     * ExtremeGauge constructor.
     *
     * @param metricName    - metric name.
     * @param metricInfo    - metric info (description).
     * @param storeMaxValue - true, if gauge should keep max value between scrapes, false otherwise.
     * @param labelNames    - labels.
     */
    public ExtremeGauge(String metricName, String metricInfo, boolean storeMaxValue, String... labelNames) {
        this.storeMax = storeMaxValue;
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
    public synchronized void update(double value, String... labels) {
        double currentValue = gauge.labels(labels).get();
        if (wasScraped) {
            setGauge(value, labels);
            wasScraped = false;
        } else if (storeMax && value > currentValue) {
            setGauge(value, labels);
        } else if (!storeMax && value < currentValue) {
            setGauge(value, labels);
        }
    }

    @Override
    public void notifyWasScraped() {
        wasScraped = true;
    }
}
