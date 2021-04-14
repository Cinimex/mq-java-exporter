package ru.cinimex.exporter.mq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.util.Pair;

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
        List<Pair<Integer, String>> mappings = queue.getPcfHeadersToMetricMappings();
        assertNotNull(mappings);
        assertEquals(3, mappings.size());
        assertEquals(MQConstants.MQIA_MAX_Q_DEPTH, mappings.get(0).getFirst());
        assertEquals(MQConstants.MQIA_INHIBIT_PUT, mappings.get(1).getFirst());
        assertEquals(MQConstants.MQIA_INHIBIT_GET, mappings.get(2).getFirst());

        mappings = channel.getPcfHeadersToMetricMappings();
        assertNotNull(mappings);
        assertEquals(1, mappings.size());
        assertEquals(MQConstants.MQIACH_CHANNEL_STATUS, mappings.get(0).getFirst());

        mappings = listener.getPcfHeadersToMetricMappings();
        assertNotNull(mappings);
        assertEquals(1, mappings.size());
        assertEquals(MQConstants.MQIACH_LISTENER_STATUS, mappings.get(0).getFirst());

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