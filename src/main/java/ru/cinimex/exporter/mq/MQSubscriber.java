package ru.cinimex.exporter.mq;

public abstract class MQSubscriber extends Thread {

    /**
     * Stops subscriber.
     */
    public abstract void stopProcessing();
}
