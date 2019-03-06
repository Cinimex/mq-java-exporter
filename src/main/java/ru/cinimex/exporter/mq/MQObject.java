package ru.cinimex.exporter.mq;

import com.ibm.mq.constants.MQConstants;

import java.util.HashMap;

public class MQObject {
    private String name;
    private MQType type;
    private int PCFCmd;
    private int PCFHeader;
    private HashMap<Integer, Object> PCFParameters = new HashMap<>();


    public MQObject(String name, MQType type) {
        this.name = name;
        this.type = type;

        switch (type) {
            case QUEUE:
                PCFCmd = MQConstants.MQCMD_INQUIRE_Q;
                PCFParameters.put(MQConstants.MQCA_Q_NAME, name);
                PCFHeader = MQConstants.MQIA_MAX_Q_DEPTH;
                break;
            case LISTENER:
                PCFCmd = MQConstants.MQCMD_INQUIRE_LISTENER_STATUS;
                PCFParameters.put(MQConstants.MQCACH_LISTENER_NAME, name);
                PCFHeader = MQConstants.MQIACH_LISTENER_STATUS;
                break;
            case CHANNEL:
                PCFCmd = MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS;
                PCFParameters.put(MQConstants.MQCACH_CHANNEL_NAME, name);
                PCFHeader = MQConstants.MQIACH_LISTENER_STATUS;
                break;
            default:
                //TODO:Exception
        }
    }

    public String getName() {
        return name;
    }

    public int getPCFHeader() {
        return PCFHeader;
    }

    public MQType getType() {
        return type;
    }

    public int getPCFCmd() {
        return PCFCmd;
    }

    public HashMap<Integer, Object> getPCFParameters() {
        return PCFParameters;
    }

    public enum MQType {
        QUEUE, CHANNEL, LISTENER;
    }
}
