package ru.cinimex.exporter.prometheus.metrics;

/**
 * Interface should be implemented by all metrics.
 */
public interface MetricInterface {

    /**
     * Method should contain all logic about updating exact metric.
     *
     * @param value  - incoming value.
     * @param labels - incoming labels.
     */
    void update(double value, String... labels);

    /**
     * Method should contain additional processing for metric, when it was scraped by Prometheus.
     * Leave empty, if there is no need to do anything after metric was scraped.
     */
    void notifyWasScraped();

}
