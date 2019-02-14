package ru.cinimex.exporter.mq.pcf;

/**
 * This class represents part of the PCF message, which could be received from
 * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/{CLASS}/{TYPE} MQ topic.
 * MQ publishes multiple metrics to each topic. This class contains info only about one metric.
 */
public class PCFElementRow {
    private int rowId;
    private int rowDatatype;
    private String rowDesc;

    /**
     * Constructor creates PCFElement object, which contains all required information about exact metric.
     *
     * @param rowId       - metric identifier. Is used to map value, received from $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/STATQ/%s/GET
     * @param rowDatatype - metric datatype. Is used to parse data correctly.
     * @param rowDesc     - metric description. Used for Prometheus labels, metrics names.
     */
    public PCFElementRow(int rowId, int rowDatatype, String rowDesc) {
        this.rowId = rowId;
        this.rowDatatype = rowDatatype;
        this.rowDesc = rowDesc;
    }

    /**
     * @return - returns row identifier.
     */
    public int getRowId() {
        return rowId;
    }

    /**
     * Created for later use.
     *
     * @return - returns row datatype.
     */
    //TODO: Check all metrics to make sure if it's usefull
    public int getRowDatatype() {
        return rowDatatype;
    }

    /**
     * @return - returns row description.
     */
    public String getRowDesc() {
        return rowDesc;
    }
}
