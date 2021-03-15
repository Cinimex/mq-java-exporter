package ru.cinimex.exporter.mq;

public interface MQSubscriber {

  /**
   * Stops subscriber.
   */
  void stopProcessing();
}
