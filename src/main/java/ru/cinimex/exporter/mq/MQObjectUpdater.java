package ru.cinimex.exporter.mq;

import static com.ibm.mq.constants.CMQCFC.MQCACF_Q_NAMES;
import static com.ibm.mq.constants.CMQCFC.MQCACH_CHANNEL_NAMES;
import static com.ibm.mq.constants.CMQCFC.MQCACH_LISTENER_NAME;

import com.ibm.mq.MQException;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.Config;
import ru.cinimex.exporter.mq.MQObject.MQType;
import ru.cinimex.exporter.mq.pcf.PCFCommand;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;

/**
 * Runnable for updating monitoring object (in case some objects were created or removed).
 */
public class MQObjectUpdater implements Runnable {

  private static final Logger logger = LogManager.getLogger(MQObjectUpdater.class);

  private final Config config;
  private final List<PCFElement> pcfElements;
  private final List<MQTopicSubscriber> mqTopicSubscribers;
  private final Map<MQType, MQPCFSubscriber> mqpcfSubscribers;
  private Set<MQObject> mqObjects;

  public MQObjectUpdater(final Config config,
      List<MQTopicSubscriber> topicSubscribers, Map<MQType, MQPCFSubscriber> mqpcfSubscribers,
      List<PCFElement> pcfElements) {
    this.config = config;
    this.pcfElements = pcfElements;
    this.mqTopicSubscribers = topicSubscribers;
    this.mqpcfSubscribers = mqpcfSubscribers;
  }

  @Override
  public void run() {
    logger.debug("Sending command to update monitoring objects.");
    Set<MQObject> monitoringObjects;
    try {
      //Get all objects from MQ
      monitoringObjects = getMonitoringObjects(config);
      Set<MQObject> objectsToRemove = new HashSet<>();
      Set<MQObject> objectsToCreate = new HashSet<>();

      //Define new objects, which should be monitored (were created)
      // and objects, which should be destroyed (were removed)
      if (mqObjects == null) {
        objectsToCreate.addAll(monitoringObjects);
      } else {
        for (MQObject mqObject : mqObjects) {
          if (!monitoringObjects.contains(mqObject)) {
            objectsToRemove.add(mqObject);
          }
        }

        for (MQObject mqObject : monitoringObjects) {
          if (!mqObjects.contains(mqObject)) {
            objectsToCreate.add(mqObject);
          }
        }

      }

      List<MQTopicSubscriber> removeSubscribers = new ArrayList<>();
      for (MQObject mqObject : objectsToRemove) {
        for (MQTopicSubscriber subscriber : mqTopicSubscribers) {
          if (subscriber.relatedToObject(mqObject.getName())) {
            subscriber.stopProcessing();
            removeSubscribers.add(subscriber);
          }
        }
      }

      mqTopicSubscribers.removeAll(removeSubscribers);

      for (MQObject mqObject : objectsToCreate) {
        for (PCFElement element : pcfElements) {
          if (element.requiresMQObject()) {
            addTopicSubscriber(mqObject, element);
          }
        }
      }

      mqObjects = monitoringObjects;

      if (config.sendPCFCommands()) {
        EnumMap<MQObject.MQType, CopyOnWriteArrayList<MQObject>> removeGroups = groupMQObjects(objectsToRemove);

        for (MQType type : removeGroups.keySet()) {
          mqpcfSubscribers.get(type).removeAll(removeGroups.get(type));
        }

        EnumMap<MQObject.MQType, CopyOnWriteArrayList<MQObject>> addGroups = groupMQObjects(objectsToCreate);

        for (MQType type : addGroups.keySet()) {
          mqpcfSubscribers.get(type).addAll(addGroups.get(type));
        }
      }

      for(MQObject mqObject : objectsToRemove){
        MetricsManager.removeMetrics(config.getQmgrName(), mqObject.getName());
      }

    } catch (IOException | MQDataException | MQException e) {
      logger.warn("Error occurred during objects update: ", e);
    }

  }

  /**
   * Adds topic subscriber for specific MQ object
   *
   * @param object  - monitored MQ object.
   * @param element - PCFElement, received from MQ.
   */
  private void addTopicSubscriber(MQObject object, PCFElement element) {
    if (object.getType().equals(MQObject.MQType.QUEUE)) {
      PCFElement objElement = new PCFElement(element.getTopicString(), element.getRows());
      objElement.formatTopicString(object.getName());
      mqTopicSubscribers.add(new MQTopicSubscriber(objElement, config.getQmgrName(), object.getName()));
    }
  }

  /**
   * Method groups elements by type.
   *
   * @param objects - set with MQ objects.
   * @return - grouped map with objects.
   */
  private EnumMap<MQType, CopyOnWriteArrayList<MQObject>> groupMQObjects(Set<MQObject> objects) {
    EnumMap<MQObject.MQType, CopyOnWriteArrayList<MQObject>> groupedObjects = new EnumMap<>(MQObject.MQType.class);
    for (MQObject.MQType type : MQObject.MQType.values()) {
      groupedObjects.put(type, new CopyOnWriteArrayList<>());
    }
    for (MQObject object : objects) {
      groupedObjects.get(object.getType()).add(object);
      logger.debug("{} {} was added for additional monitoring.", object.getType().name(), object.getName());
    }
    return groupedObjects;
  }

  private static Set<MQObject> getMonitoringObjects(Config config) throws IOException, MQDataException {
    Set<MQObject> objects = new HashSet<>();

    for (MQObject.MQType type : MQObject.MQType.values()) {
      switch (type) {
        case QUEUE:
          objects
              .addAll(inquireMQObjectsByPatterns(config.getQueues().get("include"), type, MQCACF_Q_NAMES));
          objects.removeAll(
              inquireMQObjectsByPatterns(config.getQueues().get("exclude"), type, MQCACF_Q_NAMES));
          break;
        case CHANNEL:
          objects.addAll(
              inquireMQObjectsByPatterns(config.getChannels().get("include"), type, MQCACH_CHANNEL_NAMES));
          objects.removeAll(
              inquireMQObjectsByPatterns(config.getChannels().get("exclude"), type, MQCACH_CHANNEL_NAMES));
          break;
        case LISTENER:
          objects.addAll(
              inquireMQObjectsByPatterns(config.getListeners().get("include"), type, MQCACH_LISTENER_NAME));
          objects.removeAll(
              inquireMQObjectsByPatterns(config.getListeners().get("exclude"), type, MQCACH_LISTENER_NAME));
          break;
      }
    }
    return objects;
  }

  private static List<MQObject> inquireMQObjectsByPatterns(List<String> rules, MQObject.MQType type, int getValueParam)
      throws IOException, MQDataException {

    List<MQObject> objects = new ArrayList<>();
    if (rules != null && !rules.isEmpty()) {
      PCFMessageAgent agent = new PCFMessageAgent();
      agent.connect(MQConnection.getQueueManager());
      for (String rule : rules) {
        PCFMessage pcfCommand = PCFCommand.preparePCFCommand(type, rule);
        PCFMessage[] pcfResponse = agent.send(pcfCommand);
        if (!type.equals(MQObject.MQType.LISTENER)) {
          String[] names = (String[]) pcfResponse[0].getParameterValue(getValueParam);

          for (String name : names) {
            String objName = name.trim();
            if (!objName.startsWith("AMQ.")) {
              objects.add(new MQObject(objName, type));
            }
          }
        } else {
          for (PCFMessage pcfMessage : pcfResponse) {
            objects.add(
                new MQObject(pcfMessage.getParameterValue(MQCACH_LISTENER_NAME).toString().trim(),
                    type));
          }
        }
      }
      agent.disconnect();
    }

    return objects;
  }

}
