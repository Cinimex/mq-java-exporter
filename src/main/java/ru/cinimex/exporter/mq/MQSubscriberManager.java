package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.Config;
import ru.cinimex.exporter.mq.MQObject.MQType;
import ru.cinimex.exporter.mq.pcf.model.PCFElement;
import ru.cinimex.exporter.mq.subscriber.MQTopicSubscriber;
import ru.cinimex.exporter.mq.subscriber.PCFChannelSubscriber;
import ru.cinimex.exporter.mq.subscriber.PCFListenerSubscriber;
import ru.cinimex.exporter.mq.subscriber.PCFQueueSubscriber;
import ru.cinimex.exporter.mq.subscriber.PCFSubscriber;

/**
 * Class is used to manage work of all subscribers.
 */
public class MQSubscriberManager {

    private static final Logger logger = LogManager.getLogger(MQSubscriberManager.class);
    private final Config config;
    private MQObjectUpdater mqObjectUpdater;
    private final Map<String, MQTopicSubscriber> subscribers = new ConcurrentHashMap<>();
    private final Map<MQType, PCFSubscriber> pcfSubscribers = new HashMap<>();
    private ScheduledExecutorService executor;
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
     */
    public void runSubscribers(List<PCFElement> elements)
    {
        logger.info("Launching subscribers...");

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
        subscribers.put(element.getTopicString(), new MQTopicSubscriber(element, config.getQmgrName()));
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
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.warn("Some threads were terminating too long. Will be terminated forcibly.");
                    executor.shutdownNow();
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        for (MQTopicSubscriber subscriber : subscribers.values()) {
            subscriber.stopProcessing();
        }

        for (PCFSubscriber subscriber : pcfSubscribers.values()) {
            subscriber.stopProcessing();
        }

        for (PCFSubscriber subscriber : pcfSubscribers.values()) {
            subscriber.join(config.getScrapeInterval());
        }
    }

    /**
     * Adds 1 PCF subscriber for each type of MQ objects (queue, channel, listener).
     */
    private void addPCFSubscribers() {
        PCFSubscriber subscriber = new PCFChannelSubscriber(config.getQmgrName(),
            config.monitorAutoDefinedClusterChannels(),
            config.getChannels());
        pcfSubscribers.put(MQType.CHANNEL, subscriber);
        executor.scheduleAtFixedRate(subscriber, 0, config.getScrapeInterval(), TimeUnit.SECONDS);

        subscriber = new PCFListenerSubscriber(config.getQmgrName());
        pcfSubscribers.put(MQType.LISTENER, subscriber);
        executor.scheduleAtFixedRate(subscriber, 0, config.getScrapeInterval(), TimeUnit.SECONDS);

        subscriber = new PCFQueueSubscriber(config.getQmgrName());
        pcfSubscribers.put(MQType.QUEUE, subscriber);
        executor.scheduleAtFixedRate(subscriber, 0, config.getScrapeInterval(), TimeUnit.SECONDS);
    }

}
