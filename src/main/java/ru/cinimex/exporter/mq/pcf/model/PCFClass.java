package ru.cinimex.exporter.mq.pcf.model;

/**
 * This class represents PCFMessage parameter, which could be received from $SYS/MQ/INFO/QMGR/{QMGR_NAME}/Monitor/METADATA/CLASSES
 * MQ topic.
 */
public class PCFClass {

    private final int monitorId;
    private final int monitorFlag;
    private final String monitorName;
    private final String monitorDesc;
    private final String topicString;

    /**
     * Constructor creates PCFClass object, which contains all required information about exact PCF class.
     *
     * @param monitorId   - class identifier
     * @param monitorFlag - flag, which defines if it's a system metric (not sure)
     * @param monitorName - monitoring class name (CPU, DISK, STATMQI, STATQ)
     * @param monitorDesc - short description (For example, for CPU it is "Platform central processing units")
     * @param topicString - MQ topic, where  detailed information is published (each class has it's own topic)
     */
    public PCFClass(int monitorId, int monitorFlag, String monitorName, String monitorDesc, String topicString) {
        this.monitorId = monitorId;
        this.monitorFlag = monitorFlag;
        this.monitorName = monitorName;
        this.monitorDesc = monitorDesc;
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
