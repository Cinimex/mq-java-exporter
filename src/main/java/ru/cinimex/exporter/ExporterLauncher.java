package ru.cinimex.exporter;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import ru.cinimex.exporter.mq.MQConnection;
import ru.cinimex.exporter.mq.MQSubscriberManager;
import ru.cinimex.exporter.mq.pcf.PCFClass;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;
import ru.cinimex.exporter.mq.pcf.PCFElement;
import ru.cinimex.exporter.mq.pcf.PCFType;
import ru.cinimex.exporter.prometheus.HTTPServer;
import ru.cinimex.exporter.prometheus.Registry;
import ru.cinimex.exporter.prometheus.metrics.GaugeManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Main class of mq exporter tool. Parses config, scans topics, starts subscribers.
 */
public class ExporterLauncher {
    private static final String topicString = "$SYS/MQ/INFO/QMGR/%s/Monitor/METADATA/CLASSES";
    private static final int getMsgOpt = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_COMPLETE_MSG | MQConstants.MQGMO_SYNCPOINT;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("It seems that you forgot to specify the path to the config file.");
            System.exit(1);
        }
        Config config = new Config(args[0]);
        try {
            ArrayList<PCFElement> elements = getAllPublishedMetrics(config);
            GaugeManager.initGauges(elements);
            MQSubscriberManager manager = new MQSubscriberManager(config.getQmgrHost(), config.getQmgrPort(), config.getQmgrChannel(), config.getQmgrName(), config.getUser(), config.getPassword(), config.getMqscp());
            manager.runSubscribers(elements, config.getQueues());
            new HTTPServer(new InetSocketAddress("localhost", config.getEndpPort()), config.getEndpURL(), Registry.getRegistry(), false);
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Method goes through system topics structure and returns metrics headers, which are represented by PCFElement
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
        connection.establish(config.getQmgrHost(), config.getQmgrPort(), config.getQmgrChannel(), config.getQmgrName(), config.getUser(), config.getPassword(), config.getMqscp());

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
            System.err.println(e.getStackTrace());
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
