package ru.cinimex.exporter.mq.subscriber;

import static com.ibm.mq.constants.CMQC.MQRC_CONNECTION_BROKEN;
import static com.ibm.mq.constants.CMQC.MQRC_NOT_CONNECTED;
import static com.ibm.mq.constants.CMQC.MQRC_Q_MGR_QUIESCING;
import static com.ibm.mq.constants.CMQC.MQRC_UNKNOWN_OBJECT_NAME;
import static com.ibm.mq.constants.CMQC.MQSVC_STATUS_STOPPED;
import static com.ibm.mq.constants.CMQCFC.MQCHS_INACTIVE;
import static com.ibm.mq.constants.CMQCFC.MQRCCF_CHL_STATUS_NOT_FOUND;

import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

public class PCFListenerSubscriber extends PCFSubscriber {

  private static final Logger logger = LogManager.getLogger(PCFListenerSubscriber.class);

  /**
   * PCFlSubscriber constructor.
   *
   * @param queueManagerName - queue manager name.
   */
  public PCFListenerSubscriber(String queueManagerName) {
    super(queueManagerName);
  }

  /**
   * Retrieves info about all objects from input array via direct pcf commands.
   *
   * @param objectNames - input list with objects.
   */
  @Override
  protected void updateWithDirectPCFCommand(List<String> objectNames) {
    for (String objectName : objectNames) {
      MQObject directObject = new MQObject(objectName, object.getType());
      try {
        sendDirectPCFAndUpdateMetrics(directObject);
      } catch (MQDataException e) {
        if (object.getType() == MQObject.MQType.LISTENER && e.reasonCode == MQRC_UNKNOWN_OBJECT_NAME) {
          MetricsManager.updateMetric(MetricsReference.getMetricsForType(object.getType()).get(0).name,
              MetricsReference.getMetricValue(object.getType(), MQSVC_STATUS_STOPPED),
              queueManagerName, objectName);
          logger.debug("Listener {} is possibly stopped.", objectName);
        } else {
          logger.error("Error occurred during sending PCF command: ", e);
        }
      } catch (IOException e1) {
        logger.error("Error occurred during sending PCF command: ", e1);
      }
    }
  }

  @Override
  protected void updateWithWildcardPCFCommand() {
    try {
      sendWildcardPCFAndUpdateMetrics();
    } catch (PCFException e) {
      if (e.reasonCode == MQRCCF_CHL_STATUS_NOT_FOUND) {
        logger.debug("All channels are possibly inactive.");
        for (MQObject channel : objects) {
          double prometheusValue = MetricsReference.getMetricValue(channel.getType(), MQCHS_INACTIVE);
          MetricsManager.updateMetric(MetricsReference.getMetricsForType(object.getType()).get(0).name,
              prometheusValue, queueManagerName, channel.getName());
        }
      } else if (e.getReason() == MQRC_Q_MGR_QUIESCING) {
        logger.error("Queue manager is quiescing: ", e);
        System.exit(1);
      } else {
        logger.error("Error occurred during sending PCF command: ", e);
      }
    } catch (MQDataException e) {
      if (e.reasonCode == MQRC_UNKNOWN_OBJECT_NAME) {
        logger.debug("Listeners are possibly stopped.");
        for (MQObject listener : objects) {
          double prometheusValue = MetricsReference
              .getMetricValue(listener.getType(), MQSVC_STATUS_STOPPED);
          MetricsManager.updateMetric(MetricsReference.getMetricsForType(object.getType()).get(0).name,
              prometheusValue, queueManagerName, listener.getName());
        }
      } else if (e.getReason() == MQRC_CONNECTION_BROKEN || e.getReason() == MQRC_Q_MGR_QUIESCING
          || e.getReason() == MQRC_NOT_CONNECTED) {
        logger.error("Connection with queue manager was closed: ", e);
        System.exit(1);
      }
    } catch (IOException e1) {
      logger.error("Error occurred during sending PCF command: ", e1);
    }
  }
}
