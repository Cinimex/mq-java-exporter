package ru.cinimex.exporter.testutils;

import io.prometheus.client.CollectorRegistry;

public class Utils {
    public static double getPrometheusMetricValue(CollectorRegistry registry, String metricName,
                                                  String[] labelNames, String[] labelValues) {
        return registry.getSampleValue(metricName, labelNames, labelValues);
    }
}

