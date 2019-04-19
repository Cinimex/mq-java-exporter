package ru.cinimex.exporter.mq.pcf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCFTypeTest {
    private static PCFType pcfType;
    private String monitorName;
    private String monitorDesc;
    private String topicString;

    @BeforeEach
    void initData() {
        monitorName = "CPU";
        monitorDesc = "CPU performance - platform wide";
        topicString = "TOPIC.STRING";
        pcfType = new PCFType(monitorName, monitorDesc, topicString);
    }

    @Test
    void getTopicString() {
        assertEquals(topicString, pcfType.getTopicString());
    }

    @Test
    void toString1() {
        assertEquals(String.format("PCFType{monitorName='%s', monitorDesc='%s', topicString='%s'}", monitorName, monitorDesc, topicString), pcfType.toString());
    }
}