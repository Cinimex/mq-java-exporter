package ru.cinimex.exporter.prometheus.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ibm.mq.constants.MQConstants;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference.AdditionalMetric;

class MetricsReferenceTest {

    @Test
    void getMetricNameByDescription() {
        String description = "Non-persistent message MQPUT1 count";
        String result = MetricsReference.getMetricName(description, false, -1);
        assertEquals("mq_put_non_persistent_message_mqput1_count_totalmessages", result);
    }

    @Test
    void getMetricNameByGeneratingTheNewOne() {
        String description = "this is new metric";
        String result = MetricsReference.getMetricName(description, true, MQConstants.MQIAMO_MONITOR_GB);
        assertEquals("mq_object_this_is_new_metric_gigabytes", result);
    }

    @Test
    void getMetricNameByType() {

        List<AdditionalMetric> queueMetrics = MetricsReference.getMetricsForType(MQObject.MQType.QUEUE);
        List<AdditionalMetric> channelMetrics = MetricsReference.getMetricsForType(MQObject.MQType.CHANNEL);
        List<AdditionalMetric> listenerMetrics = MetricsReference.getMetricsForType(MQObject.MQType.LISTENER);
        Assertions.assertAll(
            () -> assertNotNull(queueMetrics),
            () -> assertNotNull(channelMetrics),
            () -> assertNotNull(listenerMetrics));

        Assertions.assertAll(
            () -> assertEquals("mqobject_queue_queue_max_depth_messages",
                queueMetrics.get(0).name),
            () -> assertEquals("mqobject_queue_queue_get_inhibited_untyped",
                queueMetrics.get(1).name),
            () -> assertEquals("mqobject_queue_queue_put_inhibited_untyped",
                queueMetrics.get(2).name),
            () -> assertEquals("mqobject_channel_channel_status_untyped", channelMetrics.get(0).name),
            () -> assertEquals("mqobject_listener_listener_status_untyped", listenerMetrics.get(0).name));
    }

    @Test
    void getMetricHelp() {
        List<AdditionalMetric> queueMetrics = MetricsReference.getMetricsForType(MQObject.MQType.QUEUE);
        List<AdditionalMetric> channelMetrics = MetricsReference.getMetricsForType(MQObject.MQType.CHANNEL);
        List<AdditionalMetric> listenerMetrics = MetricsReference.getMetricsForType(MQObject.MQType.LISTENER);
        Assertions.assertAll(
            () -> assertNotNull(queueMetrics),
            () -> assertNotNull(channelMetrics),
            () -> assertNotNull(listenerMetrics));

        Assertions.assertAll(
            () -> assertEquals("The maximum number of messages that are allowed on the queue",
                queueMetrics.get(0).help),
            () -> assertEquals("0 if get is allowed and 1 otherwise",
                queueMetrics.get(1).help),
            () -> assertEquals("0 if put is allowed and 1 otherwise",
                queueMetrics.get(2).help),
            () -> assertEquals("The status of the channel", channelMetrics.get(0).help),
            () -> assertEquals("The status of the listener", listenerMetrics.get(0).help));
    }

    @Test
    void getMetricValue() {
        Assertions.assertAll(
                () -> assertEquals(50, MetricsReference.getMetricValue(MQObject.MQType.QUEUE, 50)),
                () -> assertEquals(50, MetricsReference.getMetricValue(MQObject.MQType.CHANNEL, MQConstants.MQCHS_INITIALIZING)),
                () -> assertEquals(50, MetricsReference.getMetricValue(MQObject.MQType.LISTENER, MQConstants.MQSVC_STATUS_STOPPING)));

    }

    @Test
    void getMetricType() {
        Assertions.assertAll(
                () -> assertEquals(MetricsReference.Metric.Type.SIMPLE_COUNTER, MetricsReference.getMetricType("Failed MQPUT1 count", false)),
                () -> assertEquals(MetricsReference.Metric.Type.SIMPLE_GAUGE, MetricsReference.getMetricType("Queue depth", true)),
                () -> assertEquals(MetricsReference.Metric.Type.EXTREME_GAUGE_MAX, MetricsReference.getMetricType("Non-durable subscriber - high water mark", false)),
                () -> assertEquals(MetricsReference.Metric.Type.EXTREME_GAUGE_MIN, MetricsReference.getMetricType("Non-durable subscriber - low water mark", false)));
    }
}