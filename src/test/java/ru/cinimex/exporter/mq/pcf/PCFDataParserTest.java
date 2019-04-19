package ru.cinimex.exporter.mq.pcf;

import com.ibm.mq.constants.MQConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCFDataParserTest {


    @Test
    void getExactValue() {
        Assertions.assertAll(
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_HUNDREDTHS)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_KB)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_MB)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_GB)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_MICROSEC)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_UNIT)),
                () -> assertEquals(10d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_PERCENT)),
                () -> assertEquals(1000d, PCFDataParser.getExactValue(1000d, MQConstants.MQIAMO_MONITOR_DELTA))
        );
    }
}