package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.MQObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Assertions.assertAll(
                () -> assertEquals("mqobject_queue_queue_max_depth_messages", MetricsReference.getMetricName(MQObject.MQType.QUEUE)),
                () -> assertEquals("mqobject_channel_channel_status_hundredths", MetricsReference.getMetricName(MQObject.MQType.CHANNEL)),
                () -> assertEquals("mqobject_listener_listener_status_hundredths", MetricsReference.getMetricName(MQObject.MQType.LISTENER)));
    }

    @Test
    void getMetricHelp() {
        Assertions.assertAll(
                () -> assertEquals("The maximum number of messages that are allowed on the queue", MetricsReference.getMetricHelp(MQObject.MQType.QUEUE)),
                () -> assertEquals("The status of the channel", MetricsReference.getMetricHelp(MQObject.MQType.CHANNEL)),
                () -> assertEquals("The status of the listener", MetricsReference.getMetricHelp(MQObject.MQType.LISTENER)));
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