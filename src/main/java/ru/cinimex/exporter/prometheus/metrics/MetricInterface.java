package ru.cinimex.exporter.prometheus.metrics;

public interface MetricInterface {

    void update(double value, String... labels);

}
