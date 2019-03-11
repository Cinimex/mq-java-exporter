package ru.cinimex.exporter.prometheus.metrics;

/**
 * Interface should be implemented by all metrics.
 */
public interface MetricInterface {

    /**
     * Method should contain all logic about updating exact metric.
     * @param value - incoming value.
     * @param labels - incoming labels.
     */
    void update(double value, String... labels);

}
