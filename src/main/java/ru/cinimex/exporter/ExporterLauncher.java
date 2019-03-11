package ru.cinimex.exporter;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import ru.cinimex.exporter.mq.MQConnection;
import ru.cinimex.exporter.mq.MQObject;
import ru.cinimex.exporter.mq.MQSubscriberManager;
import ru.cinimex.exporter.mq.pcf.PCFClass;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.mq.pcf.PCFType;
import ru.cinimex.exporter.prometheus.HTTPServer;
import ru.cinimex.exporter.prometheus.Registry;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Main class of mq exporter tool. Parses config, scans topics, starts subscribers.
 */
public class ExporterLauncher {
    private static final String topicString = "$SYS/MQ/INFO/QMGR/%s/Monitor/METADATA/CLASSES";
    private static final int getMsgOpt = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_COMPLETE_MSG | MQConstants.MQGMO_SYNCPOINT;

    public static void main(String[] args) throws MQException, IOException {
        if (args.length == 0) {
            System.err.println("It seems that you forgot to specify the path to the config file.");
            System.exit(1);
        }
        Config config = new Config(args[0]);
        try {
            ArrayList<PCFElement> elements = getAllPublishedMetrics(config);
            ArrayList<MQObject.MQType> monitoringTypes = new ArrayList<>();
            ArrayList<MQObject> objects = new ArrayList<>();

            if (config.sendPCFCommands()) {
                if (config.getQueues().size() > 0) {
                    monitoringTypes.add(MQObject.MQType.QUEUE);
                    for (String queueName : config.getQueues()) {
                        objects.add(new MQObject(queueName, MQObject.MQType.QUEUE));
                    }
                }
                if (config.getChannels().size() > 0) {
                    monitoringTypes.add(MQObject.MQType.CHANNEL);
                    for (String channelName : config.getChannels()) {
                        objects.add(new MQObject(channelName, MQObject.MQType.CHANNEL));
                    }
                }
                if (config.getListeners().size() > 0) {
                    monitoringTypes.add(MQObject.MQType.LISTENER);
                    for (String listenerName : config.getListeners()) {
                        objects.add(new MQObject(listenerName, MQObject.MQType.LISTENER));
                    }
                }
            }
            MetricsManager.initMetrics(elements, monitoringTypes);
            MQSubscriberManager manager = new MQSubscriberManager(config.getQmgrHost(), config.getQmgrPort(), config.getQmgrChannel(), config.getQmgrName(), config.getUser(), config.getPassword(), config.useMqscp());
            manager.runSubscribers(elements, objects, config.sendPCFCommands(), config.usePCFWildcards(), config.getScrapeInterval());
            new HTTPServer(new InetSocketAddress("0.0.0.0", config.getEndpPort()), config.getEndpURL(), Registry.getRegistry(), false);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method goes through system topics structure and returns metrics headers, which are represented by PCFElement
     *
     * @param config - parsed config file.
     * @return - array, filled with metrics headers.
     */
    private static ArrayList<PCFElement> getAllPublishedMetrics(Config config) {
        MQConnection connection = new MQConnection();
        MQTopic topic = null;
        ArrayList<PCFElement> elements = new ArrayList<PCFElement>();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = getMsgOpt;
        gmo.waitInterval = 30000;
        connection.establish(config.getQmgrHost(), config.getQmgrPort(), config.getQmgrChannel(), config.getQmgrName(), config.getUser(), config.getPassword(), config.useMqscp());

        try {
            topic = connection.createTopic(String.format(topicString, config.getQmgrName()));
            MQMessage msg = getEmptyMessage();
            topic.get(msg, gmo);
            PCFMessage pcfResponse = new PCFMessage(msg);
            ArrayList<PCFClass> classes = PCFDataParser.getPCFClasses(pcfResponse);
            for (PCFClass pcfClass : classes) {
                topic = connection.createTopic(pcfClass.getTopicString());
                msg = getEmptyMessage();
                topic.get(msg, gmo);
                pcfResponse = new PCFMessage(msg);
                ArrayList<PCFType> types = PCFDataParser.getPCFTypes(pcfResponse);
                for (PCFType type : types) {
                    topic = connection.createTopic(type.getTopicString());
                    msg = getEmptyMessage();
                    topic.get(msg, gmo);
                    pcfResponse = new PCFMessage(msg);
                    elements.addAll(PCFDataParser.getPCFElements(pcfResponse));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (topic != null && topic.isOpen()) {
                    topic.close();
                }
                connection.close();
            } catch (MQException e) {
                System.err.println(String.format("Error occured during disconnecting from topic  %s. Error: %s", topic.toString(), e.getStackTrace()));
            }
            return elements;
        }
    }

    private static MQMessage getEmptyMessage() {
        MQMessage message = new MQMessage();
        message.messageId = null;
        message.encoding = 546;
        message.characterSet = 1208;
        return message;
    }
}
