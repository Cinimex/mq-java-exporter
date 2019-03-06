package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.Counter;
import ru.cinimex.exporter.prometheus.Registry;

public class SimpleCounter implements MetricInterface {
    private Counter counter;

    public SimpleCounter(String metricName, String metricInfo, String... labelNames) {
        counter = Counter.build().name(metricName).help(metricInfo).labelNames(labelNames).register(Registry.getRegistry());
    }

    private void intCounter(double value, String... labelValues) {
        counter.labels(labelValues).inc(value);
    }

    @Override
    public void update(double value, String... labels) {
        intCounter(value, labels);
    }
}
