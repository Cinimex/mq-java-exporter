package ru.cinimex.exporter.mq.pcf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents PCFMessage parameter, which could be received from
 * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/METADATA/{CLASS}/TYPES MQ topic.
 */
public class PCFType {
    private static final Logger logger = LogManager.getLogger(PCFType.class);
    private String monitorName;
    private String monitorDesc;
    private String topicString;

    /**
     * Constructor creates PCFType object, which contains all required information about exact PCF type.
     *
     * @param monitorName - type name (For example, for PCFClass with monitorName = "CPU" possible values are: SystemSummary, QMgrSummary)
     * @param monitorDesc - detailed description (For CPU PCFCLass with SystemSummary PCFType it would be "CPU performance - platform wide")
     * @param topicString - MQ topic, where  detailed information is published (each type has it's own topic)
     */
    protected PCFType(String monitorName, String monitorDesc, String topicString) {
        this.monitorName = monitorName;
        this.monitorDesc = monitorDesc;
        this.topicString = topicString;
        if (topicString == null) {
            logger.warn("Topic string is empty: ", this.toString());
        }
    }

    /**
     * Method allows to get topic, which is required for further metrics processing
     *
     * @return - topic string
     */
    public String getTopicString() {
        return topicString;
    }

    @Override
    public String toString() {
        return "PCFType{" + "monitorName='" + monitorName + '\'' + ", monitorDesc='" + monitorDesc + '\'' + ", topicString='" + topicString + '\'' + '}';
    }

}
