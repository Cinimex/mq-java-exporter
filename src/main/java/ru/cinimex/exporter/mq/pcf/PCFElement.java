package ru.cinimex.exporter.mq.pcf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This class represents PCFMessage parameter, which could be received from
 * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/{CLASS}/{TYPE} MQ topic.
 */
public class PCFElement {
    private static final Logger logger = LogManager.getLogger(PCFElement.class);
    private final ArrayList<PCFElementRow> rows;
    private String sourceTopicString;
    private String topicString;
    private boolean mqObjectRequired;

    /**
     * Constructor creates PCFElement object, which contains all required information about exact PCF element.
     * Some topics publish information about specific MQ objects. They are useless without MQ object's name.
     * For example, topic string $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/STATQ/%s/GET is invalid. It should be formatted with some MQ object.
     * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/STATQ/SAMPLE.QUEUE/GET will publish all metrics about SAMPLE.QUEUE GET-actions.
     *
     * @param topicString - MQ topic, where  final metric is published (each element has it's own topic)
     * @param rows        - Array, which contains detailed information about published statistics
     */
    public PCFElement(String topicString, ArrayList<PCFElementRow> rows) {
        this.topicString = topicString;
        this.rows = rows;
        this.mqObjectRequired = topicString.contains("%s");
    }


    /**
     * Some topics publish information about specific MQ objects. They are useless without MQ object's name.
     * For example, topic string $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/STATQ/%s/GET is invalid. It should be formatted with some MQ object.
     * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/STATQ/SAMPLE.QUEUE/GET will publish all metrics about SAMPLE.QUEUE GET-actions.
     *
     * @return - returns true, if object requires MQ object name, otherwise returns false.
     */
    public boolean requiresMQObject() {
        return mqObjectRequired;
    }

    /**
     * Formats element's topic with received object.
     *
     * @param objectName - object name, which will be used for this topic.
     */
    public void formatTopicString(String objectName) {
        if (sourceTopicString == null) {
            sourceTopicString = topicString;
        }
        topicString = String.format(sourceTopicString, objectName);
    }


    /**
     * @return - returns topic string. Returns formatted topic string, if it has been formatted before.
     */
    public String getTopicString() {
        return topicString;
    }

    /**
     * Method returns description of exact row by it's id.
     *
     * @param rowId - row identifier (extracted from PCFElementRow object)
     * @return - description for exact row.
     */
    public String getMetricDescription(int rowId) {
        for (PCFElementRow row : rows) {
            if (row.getRowId() == rowId) {
                return row.getRowDesc();
            }
        }
        return null;
    }

    public int getRowDatatype(int rowId) {
        for (PCFElementRow row : rows) {
            if (row.getRowId() == rowId) {
                return row.getRowDatatype();
            }
        }
        return -1;
    }

    /**
     * @return - returns all rows, which contain ids, descriptions and datatypes.
     */
    public ArrayList<PCFElementRow> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "PCFElement{" + "topicString='" + topicString + '\'' + ", rows=" + rows + '}';
    }

}
