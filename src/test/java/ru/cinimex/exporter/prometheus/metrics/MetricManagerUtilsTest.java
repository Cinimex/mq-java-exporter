package ru.cinimex.exporter.prometheus.metrics;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricManagerUtilsTest {
    private static List<String> updatedMetricNames = new ArrayList<>();

    @BeforeAll
    static void initMetrics() {
        updatedMetricNames.add("mqobject_get_average_destructive_mqget_persistent_message_size_bytes");
        updatedMetricNames.add("mqobject_get_average_destructive_mqget_non_persistent_message_size_bytes");
        updatedMetricNames.add("mqobject_get_average_destructive_mqget_persistent_and_non_persistent_message_size_bytes");
    }

    @Test
    void getUpdatedMetricNames() {
        assertEquals(updatedMetricNames, MetricManagerUtils.getUpdatedMetricNames());
    }

    @Test
    void getMetricsNamesUsedToUpdate() {
        List<String> listWithNames = new ArrayList<>();
        listWithNames.add("mqobject_get_destructive_mqget_persistent_byte_count_totalbytes");
        listWithNames.add("mqobject_get_destructive_mqget_persistent_message_count_totalmessages");

        assertEquals(listWithNames, MetricManagerUtils.getMetricsNamesUsedToUpdate(updatedMetricNames.get(0)));

        listWithNames = new ArrayList<>();
        listWithNames.add("mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes");
        listWithNames.add("mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages");

        assertEquals(listWithNames, MetricManagerUtils.getMetricsNamesUsedToUpdate(updatedMetricNames.get(1)));

        listWithNames = new ArrayList<>();
        listWithNames.add("mqobject_get_destructive_mqget_persistent_byte_count_totalbytes");
        listWithNames.add("mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes");
        listWithNames.add("mqobject_get_destructive_mqget_persistent_message_count_totalmessages");
        listWithNames.add("mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages");

        assertEquals(listWithNames, MetricManagerUtils.getMetricsNamesUsedToUpdate(updatedMetricNames.get(2)));
    }
}