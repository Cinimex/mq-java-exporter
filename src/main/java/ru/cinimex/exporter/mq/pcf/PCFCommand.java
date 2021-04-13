package ru.cinimex.exporter.mq.pcf;

import static com.ibm.mq.constants.CMQCFC.MQCMD_INQUIRE_Q_NAMES;
import static com.ibm.mq.constants.MQConstants.MQCACH_CHANNEL_NAME;
import static com.ibm.mq.constants.MQConstants.MQCACH_LISTENER_NAME;
import static com.ibm.mq.constants.MQConstants.MQCA_Q_NAME;
import static com.ibm.mq.constants.MQConstants.MQCMD_INQUIRE_CHANNEL_NAMES;
import static com.ibm.mq.constants.MQConstants.MQCMD_INQUIRE_LISTENER;
import static com.ibm.mq.constants.MQConstants.MQIA_Q_TYPE;
import static com.ibm.mq.constants.MQConstants.MQQT_LOCAL;

import com.ibm.mq.headers.pcf.PCFMessage;
import ru.cinimex.exporter.mq.MQObject;

public class PCFCommand {

  private PCFCommand(){

  }

  public static PCFMessage preparePCFCommand(MQObject.MQType type, String name) {
    PCFMessage command;
    switch (type) {
      case QUEUE:
        command = new PCFMessage(MQCMD_INQUIRE_Q_NAMES);
        command.addParameter(MQCA_Q_NAME, name);
        command.addParameter(MQIA_Q_TYPE, MQQT_LOCAL);
        break;
      case CHANNEL:
        command = new PCFMessage(MQCMD_INQUIRE_CHANNEL_NAMES);
        command.addParameter(MQCACH_CHANNEL_NAME, name);
        break;
      case LISTENER:
        command = new PCFMessage(MQCMD_INQUIRE_LISTENER);
        command.addParameter(MQCACH_LISTENER_NAME, name);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
    return command;
  }

}
