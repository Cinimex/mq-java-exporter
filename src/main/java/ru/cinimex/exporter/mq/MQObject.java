package ru.cinimex.exporter.mq;

import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class represents MQObject (Queue, channel or listener). It stores object type and all PCFParameters, required for correct request.
 */
public class MQObject {
    private static final Logger logger = LogManager.getLogger(MQObject.class);
    private String name;
    private MQType type;
    private PCFMessage PCFCmd;
    private int PCFHeader;

    /**
     * MQObject constructor.
     *
     * @param name - object name.
     * @param type - object type.
     */
    public MQObject(String name, MQType type) {
        this.name = name;
        this.type = type;
/**
 * PCF commands are used to retrieve some specific statistics from queue manager.
 */
        switch (type) {
            case QUEUE:
                PCFCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q); //if object type is queue, exporter would inquire it.
                PCFCmd.addParameter(MQConstants.MQCA_Q_NAME, name); //PCF command would try to retrieve statistics about queue with specific name
                PCFCmd.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL); // and specific type
                PCFHeader = MQConstants.MQIA_MAX_Q_DEPTH; //the only statistics we want to know about queue is it's max depth.
                break;
            case LISTENER:
                PCFCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_LISTENER_STATUS); //if object type is listener, exporter would inquire it.
                PCFCmd.addParameter(MQConstants.MQCACH_LISTENER_NAME, name);//PCF command would try to retrieve statistics about listener with specific name
                PCFHeader = MQConstants.MQIACH_LISTENER_STATUS;//the only statistics we want to know about listener is it's status.
                break;
            case CHANNEL:
                PCFCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS); //if object type is channel, exporter would inquire it.
                PCFCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, name); //PCF command would try to retrieve statistics about channel with specific name
                PCFHeader = MQConstants.MQIACH_CHANNEL_STATUS;//the only statistics we want to know about channel is it's status.
                break;
            default: {
                logger.error("Unknown type for MQObject: {}", type.name());
                throw new RuntimeException("Unable to create new MQObject. Received unexpected MQObject type: " + type.name());
            }

        }
    }

    /**
     * This method returns MQConstant int code, which represents name for input object.
     *
     * @param type - object type.
     * @return - integer code. Returns -1 if code wasn't found.
     */
    public static int objectNameCode(MQObject.MQType type) {
        int code = -1;
        switch (type) {
            case QUEUE:
                code = MQConstants.MQCA_Q_NAME;
                break;
            case CHANNEL:
                code = MQConstants.MQCACH_CHANNEL_NAME;
                break;
            case LISTENER:
                code = MQConstants.MQCACH_LISTENER_NAME;
                break;
        }
        return code;
    }


    /**
     * Getter for object name.
     *
     * @return object name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for PCFHeader.
     *
     * @return - MQConstant integer code.
     */
    public int getPCFHeader() {
        return PCFHeader;
    }

    /**
     * Getter for object type.
     *
     * @return object type.
     */
    public MQType getType() {
        return type;
    }

    /**
     * Getter for PCF command.
     *
     * @return - prepared PCF command object.
     */
    public PCFMessage getPCFCmd() {
        return PCFCmd;
    }

    /**
     * This enum represents all supported MQObject types.
     */
    public enum MQType {QUEUE, CHANNEL, LISTENER}
}
