package ru.cinimex.exporter.mq;

import static com.ibm.mq.constants.MQConstants.*;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.pcf.PCFDataParser;

import java.util.List;

/**
 * Class represents all activities about reading and processing messages from single queue.
 */
public class MQMetricQueue extends Thread implements MQSubscriber {
    private static final Logger logger = LogManager.getLogger(MQMetricQueue.class);
    private final List<MQTopicSubscriber> subscribers;
    private final MQQueue queue;
    private volatile boolean isRunning;

    /**
     * MQMetricQueue constructor.
     *
     * @param subscribers - subscribers list. Each message, received from queue, contains custom metrics. They are passed to related subscriber, who knows how to handle them.
     * @throws MQException - MQ exception, which contains mqrc error code. More info <a href="https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.javadoc.doc/WMQJMSClasses/com/ibm/mq/MQException.html">here</a>.
     */
    public MQMetricQueue(List<MQTopicSubscriber> subscribers) throws MQException {
        this.subscribers = subscribers;
        this.queue = MQConnection.getQueue();
    }

    @Override
    public void run() {
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = MQGMO_WAIT | MQGMO_COMPLETE_MSG | MQGMO_FAIL_IF_QUIESCING;
        gmo.waitInterval = 16000;
        isRunning = true;
        while (isRunning) {
            MQMessage msg = new MQMessage();
            try {
                queue.get(msg, gmo);
                String name = msg.getStringProperty("mqps.Top");
                MQTopicSubscriber sub = subscribers.stream().filter(subscriber -> name.equals(subscriber.getTopicName())).findFirst().orElse(null);
                if (sub != null) {
                    sub.update(PCFDataParser.convertToPCF(msg));
                    logger.debug("Message from {} was successfully processed.", name);
                } else {
                    logger.warn("Subscriber for topic {} wasn't found", name);
                }
            } catch (MQException e1) {
                if (e1.getReason() == MQRC_CONNECTION_BROKEN) {
                    logger.error("Connection with queue manager was broken: ", e1);
                    System.exit(1);
                }
            } catch (Exception e) {
                logger.error("Error occurred during processing metrics: ", e);
            }
        }

    }

    @Override
    public void stopProcessing() {
        isRunning = false;
    }
}
