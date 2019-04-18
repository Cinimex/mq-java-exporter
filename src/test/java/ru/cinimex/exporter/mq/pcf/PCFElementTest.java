package ru.cinimex.exporter.mq.pcf;

import com.ibm.mq.constants.MQConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCFElementTest {
    private static PCFElement pcfElement;
    private List<PCFElementRow> rows;
    private String sourceTopicString;
    private String objectName;

    @BeforeEach
    void initData() {
        rows = new ArrayList<>();
        rows.add(new PCFElementRow(1, MQConstants.MQIAMO_MONITOR_KB, "What"));
        rows.add(new PCFElementRow(2, MQConstants.MQIAMO_MONITOR_MB, "A"));
        rows.add(new PCFElementRow(3, MQConstants.MQIAMO_MONITOR_GB, "Day"));
        sourceTopicString = "TEST.%s.TOPIC";
        pcfElement = new PCFElement(sourceTopicString, rows);
        objectName = "QUEUE";
    }

    @Test
    void requiresMQObject() {
        assertEquals(sourceTopicString.contains("%s"), pcfElement.requiresMQObject());
    }

    @Test
    void formatTopicString() {
        pcfElement.formatTopicString(objectName);
        assertEquals(String.format(sourceTopicString, objectName), pcfElement.getTopicString());
    }

    @Test
    void getTopicString() {
        assertEquals(sourceTopicString, pcfElement.getTopicString());
    }

    @Test
    void getMetricDescription() {
        for (PCFElementRow row : rows) {
            assertEquals(row.getRowDesc(), pcfElement.getMetricDescription(row.getRowId()));
        }
    }

    @Test
    void getRowDatatype() {
        for (PCFElementRow row : rows) {
            assertEquals(row.getRowDatatype(), pcfElement.getRowDatatype(row.getRowId()));
        }
    }

    @Test
    void getRows() {
        assertEquals(rows, pcfElement.getRows());
    }

    @Test
    void toString1() {
        assertEquals(String.format("PCFElement{topicString='%s', rows=[PCFElementRow{rowId='%s', rowDatatype='%s', rowDesc='%s'}, PCFElementRow{rowId='%s', rowDatatype='%s', rowDesc='%s'}, PCFElementRow{rowId='%s', rowDatatype='%s', rowDesc='%s'}]}", sourceTopicString, rows.get(0).getRowId(), MQConstants.lookup(rows.get(0).getRowDatatype(), "MQIAMO_MONITOR.*"), rows.get(0).getRowDesc(), rows.get(1).getRowId(), MQConstants.lookup(rows.get(1).getRowDatatype(), "MQIAMO_MONITOR.*"), rows.get(1).getRowDesc(), rows.get(2).getRowId(), MQConstants.lookup(rows.get(2).getRowDatatype(), "MQIAMO_MONITOR.*"), rows.get(2).getRowDesc()), pcfElement.toString());
    }
}