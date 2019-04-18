package ru.cinimex.exporter.mq.pcf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCFClassTest {
    private static PCFClass pcfClass;
    private int monitorId = 1;
    private int monitorFlag = 2;
    private String monitorName = "SampleName";
    private String monitorDescription = "Some useful description";
    private String topicString = "TOPIC.STRING";

    @BeforeEach
    void initClass() {
        pcfClass = new PCFClass(monitorId, monitorFlag, monitorName, monitorDescription, topicString);
    }

    @Test
    void getTopicString() {
        assertEquals(topicString, pcfClass.getTopicString());
    }

    @Test
    void toString1() {
        assertEquals(String.format("PCFClass{monitorId=%d, monitorFlag=%d, monitorName='%s', monitorDesc='%s', topicString='%s'}", monitorId, monitorFlag, monitorName, monitorDescription, topicString), pcfClass.toString());
    }
}