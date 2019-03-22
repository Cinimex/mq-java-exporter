package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.Counter;
import ru.cinimex.exporter.prometheus.Registry;

/**
 * Represents Prometheus basic metric. Counter, which can only increase or reset to 0.
 */
public class SimpleCounter implements MetricInterface {
    private Counter counter;

    /**
     * Counter constructor.
     *
     * @param metricName - metric name.
     * @param metricInfo - metric info (description).
     * @param labelNames - labels.
     *                   <p>
     *                   More about metrics <a href=https://prometheus.io/docs/practices/naming/>here</a>.
     */
    public SimpleCounter(String metricName, String metricInfo, String... labelNames) {
        counter = Counter.build().name(metricName).help(metricInfo).labelNames(labelNames).register(Registry.getRegistry());
    }

    /**
     * Increment counter.
     *
     * @param value       - value, which would be added to current value.
     * @param labelValues - value labels.
     */
    private void incCounter(double value, String... labelValues) {
        counter.labels(labelValues).inc(value);
    }

    @Override
    public void update(double value, String... labels) {
        incCounter(value, labels);
    }

    @Override
    public void notifyWasScraped() {
        //There is no need to do any work after counter was scraped.
    }
}
