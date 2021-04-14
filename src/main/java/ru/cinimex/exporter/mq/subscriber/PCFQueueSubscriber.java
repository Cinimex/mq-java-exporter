package ru.cinimex.exporter.mq.subscriber;

public class PCFQueueSubscriber extends PCFSubscriber {

  /**
   * PCFSubscriber constructor.
   *
   * @param queueManagerName - queue manager name.
   */
  public PCFQueueSubscriber(String queueManagerName) {
    super(queueManagerName);
  }
}
