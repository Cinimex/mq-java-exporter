package ru.cinimex.exporter.mq;

import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MQObjectTest {
    private MQObject queue;
    private MQObject channel;
    private MQObject listener;

    @BeforeEach
    void initObjects() {
        queue = new MQObject("queue", MQObject.MQType.QUEUE);
        channel = new MQObject("channel", MQObject.MQType.CHANNEL);
        listener = new MQObject("listener", MQObject.MQType.LISTENER);
    }

    @Test
    void objectNameCode() {
        assertEquals(MQConstants.MQCA_Q_NAME, MQObject.objectNameCode(MQObject.MQType.QUEUE));
        assertEquals(MQConstants.MQCACH_LISTENER_NAME, MQObject.objectNameCode(MQObject.MQType.LISTENER));
        assertEquals(MQConstants.MQCACH_CHANNEL_NAME, MQObject.objectNameCode(MQObject.MQType.CHANNEL));
    }

    @Test
    void getName() {
        assertEquals("queue", queue.getName());
        assertEquals("channel", channel.getName());
        assertEquals("listener", listener.getName());
    }

    @Test
    void getPCFHeader() {
        assertEquals(MQConstants.MQIA_MAX_Q_DEPTH, queue.getPCFHeader());
        assertEquals(MQConstants.MQIACH_CHANNEL_STATUS, channel.getPCFHeader());
        assertEquals(MQConstants.MQIACH_LISTENER_STATUS, listener.getPCFHeader());
    }

    @Test
    void getType() {
        assertEquals(MQObject.MQType.QUEUE, queue.getType());
        assertEquals(MQObject.MQType.CHANNEL, channel.getType());
        assertEquals(MQObject.MQType.LISTENER, listener.getType());
    }

    @Test
    void getPcfCmd() {
        PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q);
        pcfCmd.addParameter(MQConstants.MQCA_Q_NAME, queue.getName());
        pcfCmd.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL);
        assertEquals(pcfCmd, queue.getPcfCmd());

        pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
        pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, channel.getName());
        assertEquals(MQObject.MQType.CHANNEL, channel.getType());

        pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_LISTENER_STATUS);
        pcfCmd.addParameter(MQConstants.MQCACH_LISTENER_NAME, listener.getName());
        assertEquals(MQObject.MQType.LISTENER, listener.getType());
    }
}