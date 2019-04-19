package ru.cinimex.exporter.prometheus.metrics;

import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.prometheus.Registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.cinimex.exporter.testutils.Utils.getPrometheusMetricValue;

public class ExtremeGaugeTest {
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
    void updateMaxExtremeGauge() {
        ExtremeGauge gauge = new ExtremeGauge(metricName, metricInfo, true, labelNames);
        gauge.update(10d, labelValues);
        gauge.update(-5d, labelValues);
        gauge.update(1d, labelValues);
        gauge.update(0d, labelValues);
        assertEquals(10d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
    }

    @Test
    void scrapeMaxExtremeGauge() {
        ExtremeGauge gauge = new ExtremeGauge(metricName, metricInfo, true, labelNames);
        gauge.update(10d, labelValues);
        gauge.update(-5d, labelValues);
        assertEquals(10d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
        gauge.notifyWasScraped();
        gauge.update(-10d, labelValues);
        gauge.update(-7d, labelValues);
        assertEquals(-7d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
    }

    @Test
    void updateMinExtremeGauge() {
        ExtremeGauge gauge = new ExtremeGauge(metricName, metricInfo, false, labelNames);
        gauge.update(3d, labelValues);
        gauge.update(7d, labelValues);
        gauge.update(-1d, labelValues);
        gauge.update(0d, labelValues);
        assertEquals(-1d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
    }

    @Test
    void scrapeMinExtremeGauge() {
        ExtremeGauge gauge = new ExtremeGauge(metricName, metricInfo, false, labelNames);
        gauge.update(4d, labelValues);
        gauge.update(-5d, labelValues);
        assertEquals(-5d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
        gauge.notifyWasScraped();
        gauge.update(-10d, labelValues);
        gauge.update(-7d, labelValues);
        assertEquals(-10d, getPrometheusMetricValue(registry, metricName, labelNames, labelValues));
    }

    @AfterEach
    void clearRegistry() {
        registry.clear();
    }
}
