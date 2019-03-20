package ru.cinimex.exporter.mq.pcf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents PCFMessage parameter, which could be received from
 * $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/METADATA/CLASSES MQ topic.
 */
public class PCFClass {
    private static final Logger logger = LogManager.getLogger(PCFClass.class);
    private int monitorId;
    private int monitorFlag;
    private String monitorName;
    private String monitorDesc;
    private String topicString;

    /**
     * Constructor creates PCFClass object, which contains all required information about exact PCF class.
     *
     * @param monitorId   - class identifier
     * @param monitorFlag - flag, which defines if it's a system metric (not sure)
     * @param monitorName - monitoring class name (CPU, DISK, STATMQI, STATQ)
     * @param monitorDesc - short description (For example, for CPU it is "Platform central processing units")
     * @param topicString - MQ topic, where  detailed information is published (each class has it's own topic)
     */
    protected PCFClass(int monitorId, int monitorFlag, String monitorName, String monitorDesc, String topicString) {
        this.monitorId = monitorId;
        this.monitorFlag = monitorFlag;
        this.monitorName = monitorName;
        this.monitorDesc = monitorDesc;
        if (topicString == null) {
            logger.warn("Topic string is empty: ", this.toString());
        }
        this.topicString = topicString;
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
        return "PCFClass{" + "monitorId=" + monitorId + ", monitorFlag=" + monitorFlag + ", monitorName='" + monitorName + '\'' + ", monitorDesc='" + monitorDesc + '\'' + ", topicString='" + topicString + '\'' + '}';
    }

}
