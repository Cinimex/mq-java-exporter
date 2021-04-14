package ru.cinimex.exporter.mq.subscriber;

import static com.ibm.mq.constants.CMQC.MQRC_CONNECTION_BROKEN;
import static com.ibm.mq.constants.CMQC.MQRC_NOT_CONNECTED;
import static com.ibm.mq.constants.CMQC.MQRC_Q_MGR_QUIESCING;
import static com.ibm.mq.constants.CMQCFC.MQCHS_INACTIVE;
import static com.ibm.mq.constants.CMQCFC.MQIACH_CHANNEL_TYPE;
import static com.ibm.mq.constants.CMQCFC.MQRCCF_CHL_STATUS_NOT_FOUND;
import static com.ibm.mq.constants.CMQXC.MQCHT_CLUSRCVR;
import static com.ibm.mq.constants.CMQXC.MQCHT_CLUSSDR;
import static ru.cinimex.exporter.util.PatternUtil.nameMatchesWithPatterns;

import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

public class PCFChannelSubscriber extends PCFSubscriber {

  private static final Logger logger = LogManager.getLogger(PCFChannelSubscriber.class);
  private final boolean monitorAutoClusterChannels;
  private final Map<String, List<String>> channelNamingPatterns;
  private List<String> autoDefinedClusterChannelsNames;

  /**
   * PCFChannelSubscriber constructor.
   *
   * @param queueManagerName           - queue manager name.
   * @param monitorAutoClusterChannels - true if subscriber should check auto-defined cluster channels statuses.
   * @param channelNamingPatterns      - patterns which should be applied to channels names.
   */
  public PCFChannelSubscriber(String queueManagerName, boolean monitorAutoClusterChannels,
      Map<String, List<String>> channelNamingPatterns) {
    super(queueManagerName);
    this.monitorAutoClusterChannels = monitorAutoClusterChannels;
    this.channelNamingPatterns = channelNamingPatterns;

    if (monitorAutoClusterChannels) {
      this.autoDefinedClusterChannelsNames = new ArrayList<>();
    }
  }

  /**
   * Parses PCF response from queue manager, retrieves info about all required objects and updates metrics.
   *
   * @param pcfResponse - PCFMessage object which contains response from queue manager about all MQ objects of specific
   *                    type.
   */
  @Override
  protected void updateMetricsWithWildcards(PCFMessage[] pcfResponse) {
    List<String> monitoredObjectNames = new ArrayList<>();
    Map<String, PCFMessage> retrievedMetrics = new HashMap<>();
    List<String> clusterObjectNamesCopy = new ArrayList<>(autoDefinedClusterChannelsNames);

    //copy all objects names to temporary array
    for (MQObject monitoredObject : super.objects) {
      monitoredObjectNames.add(monitoredObject.getName());
    }

    //put all retrieved objects names to temporary array
    for (PCFMessage response : pcfResponse) {
      String objectName = (String) response.getParameterValue(MQObject.objectNameCode(object.getType()));
      retrievedMetrics.put(objectName.trim(), response);
    }

    retrievedMetrics.forEach((objectName, pcfMessage) -> {
      boolean isClusterChannel = false;
      //if temporary array contains metric, then remove it from temporary array and update metric
      if (monitoredObjectNames.contains(objectName)) {
        monitoredObjectNames.remove(objectName);
        updateMetrics(pcfMessage, objectName);
      } else if (monitorAutoClusterChannels) {
        try {
          isClusterChannel = isClusterChannel(pcfMessage.getIntParameterValue(MQIACH_CHANNEL_TYPE));
        } catch (PCFException e) {
          logger.warn("Error occurred during retrieving channel type: ", e);
        }
        if (isClusterChannel && nameMatchesWithPatterns(objectName, channelNamingPatterns)) {
          //only auto-defined cluster channels achieve this point
          updateMetrics(pcfMessage, objectName);

          if (clusterObjectNamesCopy.contains(objectName)) {
            clusterObjectNamesCopy.remove(objectName);
          } else {
            autoDefinedClusterChannelsNames.add(objectName);
          }
        }
      }
    });

    //list contains auto-defined cluster channels which were removed from mq (or have inactive status)
    for (String removedClusterObjectName : clusterObjectNamesCopy) {
      autoDefinedClusterChannelsNames.remove(removedClusterObjectName);
      MetricsManager.removeMetrics(queueManagerName, removedClusterObjectName);
    }

    //Are there any objects left in temporary array? It means that "*" wildcard didn't return all values.
    //There are multiple reasons why it could happen. For example, MQ channel has status "inactive".
    //Then we send direct PCF command for specific object. If some error occurs, we have custom processing for it.
    updateWithDirectPCFCommand(monitoredObjectNames);
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
        if (e.reasonCode == MQRCCF_CHL_STATUS_NOT_FOUND) {
          logger.debug("Channel {} is possibly inactive.", objectName);
          MetricsManager.updateMetric(MetricsReference.getMetricsForType(object.getType()).get(0).name,
              MetricsReference.getMetricValue(object.getType(), MQCHS_INACTIVE), queueManagerName,
              objectName);
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
      if (e.getReason() == MQRC_CONNECTION_BROKEN || e.getReason() == MQRC_Q_MGR_QUIESCING
          || e.getReason() == MQRC_NOT_CONNECTED) {
        logger.error("Connection with queue manager was closed: ", e);
        System.exit(1);
      }
      logger.error("Error occurred during sending PCF command: ", e);
    } catch (IOException e1) {
      logger.error("Error occurred during sending PCF command: ", e1);
    }
  }

  @Override
  public void run() {
    if (super.object == null || (super.objects.isEmpty() && !monitorAutoClusterChannels)) {
      return;
    }
    super.run();
  }

  private static boolean isClusterChannel(int channelTypeId) {
    return channelTypeId == MQCHT_CLUSRCVR || channelTypeId == MQCHT_CLUSSDR;
  }

}
