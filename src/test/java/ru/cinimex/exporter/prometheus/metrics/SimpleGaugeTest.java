package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.prometheus.Registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.cinimex.exporter.testutils.Utils.getPrometheusMetricValue;

public class SimpleGaugeTest {
    private static final String metricName = "TestMetric";
    private static final String metricInfo = "I am a very useful info.";
    private static final String[] labelNames = {"instance", "queue"};
    private static final String[] labelValues = {"test", "some.queue"};
    private static CollectorRegistry registry;

    @BeforeEach
    void initRegistry() {
        registry = Registry.getRegistry();
    }

    @Test
    void createValidSimpleGauge() {
        SimpleGauge gauge = new SimpleGauge(metricName, metricInfo, labelNames);
        gauge.update(10d, labelValues);
        assertEquals(10d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
    }

    @AfterEach
    void clearRegistry() {
        registry.clear();
    }

}
