package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import io.prometheus.client.Collector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;
import ru.cinimex.exporter.mq.pcf.model.PCFElementRow;
import ru.cinimex.exporter.prometheus.Registry;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricsManagerTest {
    private static List<MQObject.MQType> types = new ArrayList<>();
    private static List<PCFElement> elements = new ArrayList<>();
    private static List<PCFElementRow> rows = new ArrayList<>();
    private static List<String> descriptions = new ArrayList<>();

    @BeforeAll
    static void prepareData() {
        descriptions.add("Durable subscriber - high water mark");
        descriptions.add("Durable subscriber - low water mark");
        descriptions.add("MQ trace file system - bytes in use");
        descriptions.add("Log - write latency");
        descriptions.add("MQCLOSE count");
        descriptions.add("User CPU time percentage");
        descriptions.add("The maximum number of messages that are allowed on the queue");
        descriptions.add("The status of the channel");
        descriptions.add("The status of the listener");

        rows.add(new PCFElementRow(0, MQConstants.MQIAMO_MONITOR_HUNDREDTHS, descriptions.get(0)));
        rows.add(new PCFElementRow(1, MQConstants.MQIAMO_MONITOR_KB, descriptions.get(1)));
        rows.add(new PCFElementRow(2, MQConstants.MQIAMO_MONITOR_MB, descriptions.get(2)));
        rows.add(new PCFElementRow(4, MQConstants.MQIAMO_MONITOR_MICROSEC, descriptions.get(3)));
        rows.add(new PCFElementRow(5, MQConstants.MQIAMO_MONITOR_UNIT, descriptions.get(4)));
        rows.add(new PCFElementRow(6, MQConstants.MQIAMO_MONITOR_PERCENT, descriptions.get(5)));

        types.add(MQObject.MQType.QUEUE);
        types.add(MQObject.MQType.CHANNEL);
        types.add(MQObject.MQType.LISTENER);
    }


    @BeforeEach
    @AfterEach
    void clearRegistry() {
        Registry.getRegistry().clear();
    }

    @Test
    void initMetricsWithoutObjects() {
        elements = new ArrayList<>();
        elements.add(new PCFElement("TEST.TOPIC", rows));
        MetricsManager.initMetrics(elements);
        Enumeration<Collector.MetricFamilySamples> initializedMetrics = Registry.getRegistry().metricFamilySamples();
        int metricsNum = 0;
        while (initializedMetrics.hasMoreElements()) {
            Collector.MetricFamilySamples sample = initializedMetrics.nextElement();
            if (descriptions.contains(sample.help)) {
                metricsNum++;
            }
        }
        assertEquals(descriptions.size(), metricsNum);
    }

    @Test
    void initMetricsWithObjects() {
        elements = new ArrayList<>();
        elements.add(new PCFElement("TEST.TOPIC.%s", rows));
        MetricsManager.initMetrics(elements);
        Enumeration<Collector.MetricFamilySamples> initializedMetrics = Registry.getRegistry().metricFamilySamples();
        int metricsNum = 0;
        while (initializedMetrics.hasMoreElements()) {
            Collector.MetricFamilySamples sample = initializedMetrics.nextElement();
            if (descriptions.contains(sample.help)) {
                metricsNum++;
            }
        }
        assertEquals(descriptions.size(), metricsNum);

    }

    @Test
    void updateMetric() {
        double value = 100d;
        String result = "Metric wasn't updated!";
        elements = new ArrayList<>();
        elements.add(new PCFElement("TEST.TOPIC.%s", rows));
        MetricsManager.initMetrics(elements);
        MetricsManager.updateMetric("mq_object_durable_subscriber_high_water_mark_hundredths", value, "TEST1", "TEST2");
        Enumeration<Collector.MetricFamilySamples> initializedMetrics = Registry.getRegistry().metricFamilySamples();
        while (initializedMetrics.hasMoreElements()) {
            Collector.MetricFamilySamples sample = initializedMetrics.nextElement();
            if (sample.samples.size() == 1) {
                assertEquals(value, sample.samples.get(0).value);
                result = "Metric was updated!";
                break;
            }
        }
        assertEquals("Metric was updated!", result);
    }

    @Test
    void notifyMetricsWereScraped() {
        String metricName = "mq_subscribe_durable_subscriber_high_water_mark_subscriptions";
        String[] labels = {"TEST1"};
        String result = "Metric wasn't updated!";
        elements = new ArrayList<>();
        elements.add(new PCFElement("TEST.TOPIC", rows));
        MetricsManager.initMetrics(elements);
        MetricsManager.updateMetric(metricName, 100d, labels);
        MetricsManager.updateMetric(metricName, 50d, labels);
        MetricsManager.updateMetric(metricName, 99d, labels);
        Enumeration<Collector.MetricFamilySamples> initializedMetrics = Registry.getRegistry().metricFamilySamples();
        while (initializedMetrics.hasMoreElements()) {
            Collector.MetricFamilySamples sample = initializedMetrics.nextElement();
            if (sample.name.equals(metricName) && sample.samples.size() == 1) {
                assertEquals(100d, sample.samples.get(0).value);
                result = "Metric was updated!";
                break;
            }
        }
        assertEquals("Metric was updated!", result);
        MetricsManager.notifyMetricsWereScraped();
        MetricsManager.updateMetric(metricName, 10d, labels);
        MetricsManager.updateMetric(metricName, 7d, labels);
        initializedMetrics = Registry.getRegistry().metricFamilySamples();
        while (initializedMetrics.hasMoreElements()) {
            Collector.MetricFamilySamples sample = initializedMetrics.nextElement();
            if (sample.name.equals(metricName) && sample.samples.size() == 1) {
                assertEquals(10d, sample.samples.get(0).value);
                result = "Metric was updated again!";
                break;
            }
        }
        assertEquals("Metric was updated again!", result);

    }

}