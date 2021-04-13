package ru.cinimex.exporter.mq.pcf;

import com.ibm.mq.constants.MQConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.pcf.model.PCFElementRow;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCFElementRowTest {
    private static PCFElementRow pcfElementRow;
    private int rowId = 1;
    private int rowDatatype = MQConstants.MQIAMO_MONITOR_PERCENT;
    private String rowDescription = "Some useful description";

    @BeforeEach
    void initElementRow() {
        pcfElementRow = new PCFElementRow(rowId, rowDatatype, rowDescription);
    }


    @Test
    void getRowId() {
        assertEquals(rowId, pcfElementRow.getRowId());
    }

    @Test
    void getRowDatatype() {
        assertEquals(rowDatatype, pcfElementRow.getRowDatatype());
    }

    @Test
    void getRowDesc() {
        assertEquals(rowDescription, pcfElementRow.getRowDesc());
    }

    @Test
    void toString1() {
        assertEquals(String.format("PCFElementRow{rowId='%s', rowDatatype='%s', rowDesc='%s'}", pcfElementRow.getRowId(), MQConstants.lookup(pcfElementRow.getRowDatatype(), "MQIAMO_MONITOR.*"), pcfElementRow.getRowDesc()), pcfElementRow.toString());
    }
}