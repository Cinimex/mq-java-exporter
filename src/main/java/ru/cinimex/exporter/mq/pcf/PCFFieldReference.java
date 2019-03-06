package ru.cinimex.exporter.mq.pcf;

import com.ibm.mq.constants.MQConstants;
import ru.cinimex.exporter.mq.MQObject;

import java.util.HashMap;


public class PCFFieldReference {

    private static HashMap<Integer, Double> channelStatus = new HashMap() {
        {
            put(Integer.valueOf(MQConstants.MQCHS_BINDING), Double.valueOf(6));
            put(Integer.valueOf(MQConstants.MQCHS_STARTING), Double.valueOf(2));
            put(Integer.valueOf(MQConstants.MQCHS_RUNNING), Double.valueOf(1));
            put(Integer.valueOf(MQConstants.MQCHS_PAUSED), Double.valueOf(3));
            put(Integer.valueOf(MQConstants.MQCHS_STOPPING), Double.valueOf(4));
            put(Integer.valueOf(MQConstants.MQCHS_RETRYING), Double.valueOf(5));
            put(Integer.valueOf(MQConstants.MQCHS_STOPPED), Double.valueOf(0));
            put(Integer.valueOf(MQConstants.MQCHS_REQUESTING), Double.valueOf(7));
            put(Integer.valueOf(MQConstants.MQCHS_SWITCHING), Double.valueOf(8));
            put(Integer.valueOf(MQConstants.MQCHS_INITIALIZING), Double.valueOf(9));
        }
    };

    private static HashMap<Integer, Double> listenerStatus = new HashMap() {
        {
            put(Integer.valueOf(MQConstants.MQSVC_STATUS_STARTING), Double.valueOf(2));
            put(Integer.valueOf(MQConstants.MQSVC_STATUS_RUNNING), Double.valueOf(1));
            put(Integer.valueOf(MQConstants.MQSVC_STATUS_STOPPING), Double.valueOf(0));
        }
    };

    //TODO: add error processing, think about returnValue possible values.
    public static double getMetricValue(MQObject.MQType type, int value) {
        double returnValue = -1;
        switch (type) {
            case QUEUE:
                returnValue = value;
                break;
            case CHANNEL:
                returnValue = channelStatus.get(value);
                break;
            case LISTENER:
                returnValue = channelStatus.get(value);
                break;
            default:
                break;
        }
        return returnValue;
    }

}
