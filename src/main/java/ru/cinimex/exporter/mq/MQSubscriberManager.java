package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.Config;
import ru.cinimex.exporter.mq.MQObject.MQType;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;

/**
 * Class is used to manage work of all subscribers.
 */
public class MQSubscriberManager {

    private static final Logger logger = LogManager.getLogger(MQSubscriberManager.class);
    private final Config config;
    private MQObjectUpdater mqObjectUpdater;
    private final List<MQTopicSubscriber> subscribers = new CopyOnWriteArrayList();
    private final Map<MQType, MQPCFSubscriber> pcfSubscribers = new HashMap<>();
    private ScheduledExecutorService executor;
    private int timeout;
    private MQMetricQueue queue;

    /**
     * Constructor sets params for connecting to target queue manager.
     *
     * @param config - object containing different properties
     */
    public MQSubscriberManager(Config config) {
       this.config = config;
    }

    /**
     * Creates pool with subscribers and starts them.
     *
     * @param elements        - elements, received via MQ monitoring topics.
     * @param sendPCFCommands - this flag indicates, if we should send additional PCF commands (To get queues max depth, channels and listeners statuses).
     * @param interval        - interval in seconds, at which additional PCF commands are sent.
     * @param timeout         - timeout for MQGET operation (milliseconds).
     */
    public void runSubscribers(List<PCFElement> elements, boolean sendPCFCommands, int interval, int timeout) {
        logger.info("Launching subscribers...");
        this.timeout = timeout;

        //One thread per MQ object type (queue, listener, etc) and one for object updates.
        executor = Executors.newScheduledThreadPool(MQType.values().length + 1);

        addStaticTopicSubscribers(elements);

        if (config.sendPCFCommands()) {
            addPCFSubscribers();
        }

        mqObjectUpdater = new MQObjectUpdater(config, subscribers, pcfSubscribers, elements);
        executor.scheduleAtFixedRate(mqObjectUpdater, 0, config.getUpdateInterval(), TimeUnit.SECONDS);
        try {
            this.queue = new MQMetricQueue(subscribers);
        } catch (MQException e) {
            logger.error("Error occurred during queue initialization: ", e);
            System.exit(1);
        }
        queue.start();
        if (!subscribers.isEmpty()) {
            logger.info("Successfully launched {} topic subscribers!", subscribers.size());
        } else {
            logger.warn("Didn't launch any topic subscriber. Exporter finishes it's work!");
            System.exit(1);
        }

    }

    private void addStaticTopicSubscribers(List<PCFElement> elements) {
        for (PCFElement element : elements) {
            if (!element.requiresMQObject()) {
                addTopicSubscriber(element);
            }
        }
    }

    /**
     * Adds topic subscriber
     *
     * @param element - PCFElement, received from MQ.
     */
    private void addTopicSubscriber(PCFElement element) {
        subscribers.add(new MQTopicSubscriber(element, config.getQmgrName()));
    }

    /**
     * Stops all running subscribers in managed mode. All connections will be closed, all threads will be finished.
     *
     * @throws InterruptedException
     */
    public void stopSubscribers() throws InterruptedException {
        if (queue != null) {
            queue.stopProcessing();
        }

        if (executor != null) {
            executor.shutdown();
            if(!executor.awaitTermination(60, TimeUnit.SECONDS)){
                logger.warn("Some threads were terminating too long. Will be terminated forcibly.");
            }
        }

        for (MQTopicSubscriber subscriber : subscribers) {
            subscriber.stopProcessing();
        }

        for (MQPCFSubscriber subscriber : pcfSubscribers.values()) {
            subscriber.stopProcessing();
        }

        for (MQPCFSubscriber subscriber : pcfSubscribers.values()) {
            subscriber.join(timeout);
        }
    }

    /**
     * Adds 1 PCF subscriber for each type of MQ objects (queue, channel, listener).
     */
    private void addPCFSubscribers() {
        for (MQType type : MQObject.MQType.values()) {
            MQPCFSubscriber subscriber = new MQPCFSubscriber(config.getQmgrName());
            pcfSubscribers.put(type, subscriber);
            executor.scheduleAtFixedRate(subscriber, 0, config.getScrapeInterval(), TimeUnit.SECONDS);
        }
    }

}
