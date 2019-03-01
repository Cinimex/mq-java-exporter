# mq-java-exporter

IBM MQ exporter for Prometheus. 

Compatibility
-------------

Support [IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm) version 9.0.0.0 (and later).

Dependency
----------

-	[Prometheus](https://prometheus.io)
-	[Maven](https://maven.apache.org/)
-	[IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm)

Download
--------

At the moment build from sources is only available.


Preparations
-------

1. Download current repository.
2. Go to mq-java-exporter\src\main\resources. exporter_config.yaml file is located there. Fill it with actual data. 

Build
-------

1. Download current repository.
2. Install [Maven](https://maven.apache.org/).
3. Go to mq-java-exporter root folder (where pom.xml is located) and run: 

```shell
mvn package
```

4. After processing is completed, go to mq-java-exporter/target. dependency-jars directory and webspheremq_exporter.jar should appear there. 
5. To run exporter, dependency-jars directory (and all jars in it) and webspheremq_exporter.jar should be located in the same folder.
6. To run exporter execute the following command: 

```shell
 java -jar webspheremq_exporter.jar /opt/mq_exporter/exporter_config.yaml
```

Metrics
-------

<table class="wrapped"><colgroup><col /><col /><col /><col /><col /><col /><col /><col /></colgroup>
<tbody>
<tr>
<td><strong>MQ metric class</strong></td>
<td><strong>MQ metric type</strong></td>
<td><strong>MQ metric element</strong></td>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Short description</strong></td>
<td><strong>Requires object</strong></td>
<td><strong>Is a counter</strong></td>
<td><strong>Datatype</strong></td></tr>
<tr>
<td rowspan="10">Platform central processing units</td>
<td rowspan="7">CPU performance - platform wide</td>
<td>User CPU time percentage</td>
<td>user_cpu_time_percentage</td>
<td>Shows the percentage of CPU busy in user state.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>System CPU time percentage</td>
<td>system_cpu_time_percentage</td>
<td>Shows the percentage of CPU busy in system state</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>CPU load - one minute average</td>
<td>cpu_load_one_minute_average</td>
<td>Shows the load average over 1 minute.</td>
<td>No</td>
<td>No</td>
<td>hundredths</td></tr>
<tr>
<td>CPU load - five minute average</td>
<td>cpu_load_five_minute_average</td>
<td>Shows the load average over 5 minutes.</td>
<td>No</td>
<td>No</td>
<td>hundredths</td></tr>
<tr>
<td>CPU load - fifteen minute average</td>
<td>cpu_load_fifteen_minute_average</td>
<td>Shows the load average over fifteen minutes.&nbsp;</td>
<td>No</td>
<td>No</td>
<td>hundredths</td></tr>
<tr>
<td>RAM free percentage</td>
<td>ram_free_percentage</td>
<td>Shows the percentage of free RAM memory.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>RAM total bytes</td>
<td>ram_total_bytes</td>
<td>Shows the total bytes of RAM configured.</td>
<td>No</td>
<td>No</td>
<td>megabytes</td></tr>
<tr>
<td rowspan="3">CPU performance - running queue manager</td>
<td>User CPU time - percentage estimate for queue manager</td>
<td>user_cpu_time_percentage_estimate_for_queue_manager</td>
<td>Estimates the percentage of CPU use in user state for processes that are related to the queue managers that are being monitored.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>System CPU time - percentage estimate for queue manager</td>
<td>system_cpu_time_percentage_estimate_for_queue_manager</td>
<td>Estimates the percentage of CPU use in system state for processes that are related to the queue managers that are being monitored</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>RAM total bytes - estimate for queue manager</td>
<td>ram_total_bytes_estimate_for_queue_manager</td>
<td>Estimates the total bytes of RAM in use by the queue managers that are being monitored.</td>
<td>No</td>
<td>No</td>
<td>megabytes</td></tr>
<tr>
<td rowspan="14">&nbsp;Platform persistent data stores</td>
<td rowspan="5">Disk usage - platform wide</td>
<td>MQ trace file system - bytes in use</td>
<td>mq_trace_file_system_bytes_in_use</td>
<td>Shows the number of bytes of disk storage that are being used by the trace file system.</td>
<td>No</td>
<td>No</td>
<td>megabytes</td></tr>
<tr>
<td>MQ trace file system - free space</td>
<td>mq_trace_file_system_free_space</td>
<td>Shows the disk storage that is reserved for the trace file system that is free.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>MQ errors file system - bytes in use</td>
<td>mq_errors_file_system_bytes_in_use</td>
<td>Shows the number of bytes of disk storage that is being used by error data.</td>
<td>No</td>
<td>No</td>
<td>megabytes</td></tr>
<tr>
<td>MQ errors file system - free space</td>
<td>mq_errors_file_system_free_space</td>
<td>Shows the disk storage that is reserved for error data that is free.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>MQ FDC file count</td>
<td>mq_fdc_file_count</td>
<td>Shows the current number of FDC files.</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td rowspan="2">Disk usage - running queue managers</td>
<td>Queue Manager file system - bytes in use</td>
<td>queue_manager_file_system_bytes_in_use</td>
<td>Shows the number of bytes of disk storage that is used by queue manager files for the queue managers that you are monitoring.</td>
<td>No</td>
<td>No</td>
<td>megabytes</td></tr>
<tr>
<td>Queue Manager file system - free space</td>
<td>queue_manager_file_system_free_space</td>
<td>Shows the disk storage that is reserved for queue manager files that is free.</td>
<td>No</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td rowspan="7">Disk usage - queue manager recovery log</td>
<td>Log - bytes in use</td>
<td>log_bytes_in_use</td>
<td>Shows the number of bytes of disk storage that is used for the recovery logs of the queue managers that you are monitoring.</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td>Log - bytes max</td>
<td>log_bytes_max</td>
<td>Shows the maximum bytes of disk storage that is configured to be used for queue manager recovery logs.</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td>Log file system - bytes in use</td>
<td>log_file_system_bytes_in_use</td>
<td>Shows the total number of disk bytes in use for the log file system.</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td>Log file system - bytes max</td>
<td>log_file_system_bytes_max</td>
<td>Shows the number of disk bytes that are configured for the log file system.&nbsp;</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td>Log - physical bytes written</td>
<td>log_physical_bytes_written</td>
<td>Shows the number of bytes being written to the recovery logs.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Log - logical bytes written</td>
<td>log_logical_bytes_written</td>
<td>Shows the logical number of bytes written to the recovery logs.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Log - write latency</td>
<td>log_write_latency</td>
<td>Shows a measure of the latency when writing synchronously to the queue manager recovery log.</td>
<td>No</td>
<td>No</td>
<td>microseconds</td></tr>
<tr>
<td rowspan="63">&nbsp;API usage statistics</td>
<td rowspan="4">MQCONN and MQDISC</td>
<td>MQCONN/MQCONNX count</td>
<td>mqconn_mqconnx_count</td>
<td>Shows the number of calls to MQCONN and MQCONNX.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQCONN/MQCONNX count</td>
<td>failed_mqconn_mqconnx_count</td>
<td>Shows the number of failed calls to MQCONN and MQCONNX.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Concurrent connections - high water mark</td>
<td>concurrent_connections_high_water_mark</td>
<td>Shows the maximum number of concurrent connections in the current statistics interval.</td>
<td>No</td>
<td>No</td>
<td>units</td></tr>
<tr>
<td>MQDISC count</td>
<td>mqdisc_count</td>
<td>Shows the number of calls to MQDISC.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="4">MQOPEN and MQCLOSE</td>
<td>MQOPEN count</td>
<td>mqopen_count</td>
<td>Shows the number of calls to MQOPEN.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQOPEN count</td>
<td>failed_mqopen_count</td>
<td>Shows the number of failed calls to MQOPEN.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQCLOSE count</td>
<td>mqclose_count</td>
<td>Shows the number of calls to MQCLOSE.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQCLOSE count</td>
<td>failed_mqclose_count</td>
<td>Shows the number of failed calls to MQCLOSE.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="4">MQINQ and MQSET</td>
<td>MQINQ count</td>
<td>mqinq_count</td>
<td>Shows the number of calls to MQINQ.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQINQ count</td>
<td>failed_mqinq_count</td>
<td>Shows the number of failed calls to MQINQ.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQSET count</td>
<td>mqset_count</td>
<td>Shows the number of calls to MQSET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQSET count</td>
<td>failed_mqset_count</td>
<td>Shows the number of failed calls to MQSET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="11">MQPUT</td>
<td>Interval total MQPUT/MQPUT1 count</td>
<td>interval_total_mqput_mqput1_count</td>
<td>Shows the number of calls to MQPUT and MQPUT1.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Interval total MQPUT/MQPUT1 byte count</td>
<td>interval_total_mqput_mqput1_byte_count</td>
<td>Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent message MQPUT count</td>
<td>non_persistent_message_mqput_count</td>
<td>Shows the number of non-persistent messages that are put by MQPUT.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent message MQPUT count</td>
<td>persistent_message_mqput_count</td>
<td>Shows the number of persistent messages that are put by MQPUT.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQPUT count</td>
<td>failed_mqput_count</td>
<td>Shows the number of failed calls to MQPUT.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent message MQPUT1 count</td>
<td>non_persistent_message_mqput1_count</td>
<td>Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent message MQPUT1 count</td>
<td>persistent_message_mqput1_count</td>
<td>Shows the number of persistent messages that are put by MQPUT1.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQPUT1 count</td>
<td>failed_mqput1_count</td>
<td>Shows the number of failed calls to MQPUT1.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Put non-persistent messages - byte count</td>
<td>put_non_persistent_messages_byte_count</td>
<td>Shows the number of bytes put in non-persistent messages.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Put persistent messages - byte count</td>
<td>put_persistent_messages_byte_count</td>
<td>Shows the number of bytes put in persistent messages.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQSTAT count</td>
<td>mqstat_count</td>
<td>Shows the number of calls to MQSTAT.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="17">MQGET</td>
<td>Interval total destructive get- count</td>
<td>interval_total_destructive_get_count</td>
<td>Number of messages that are removed from queues by MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Interval total destructive get - byte count</td>
<td>interval_total_destructive_get_byte_count</td>
<td>Bytes of data that is removed from queues by MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent message destructive get - count</td>
<td>non_persistent_message_destructive_get_count</td>
<td>Number of non-persistent messages that are removed from queues by MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent message destructive get - count</td>
<td>persistent_message_destructive_get_count</td>
<td>Number of persistent messages that are removed from queues by MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQGET - count</td>
<td>failed_mqget_count</td>
<td>Shows the number of failed calls to MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Got non-persistent messages - byte count</td>
<td>got_non_persistent_messages_byte_count</td>
<td>Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Got persistent messages - byte count</td>
<td>got_persistent_messages_byte_count</td>
<td>Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent message browse - count</td>
<td>non_persistent_message_browse_count</td>
<td>Shows a count of non-persistent messages that have been browsed.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent message browse - count</td>
<td>persistent_message_browse_count</td>
<td>Shows a count of persistent messages that have been browsed.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed browse count</td>
<td>failed_browse_count</td>
<td>Shows a count of failed message browses.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent message browse - byte count</td>
<td>non_persistent_message_browse_byte_count</td>
<td>Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent message browse - byte count</td>
<td>persistent_message_browse_byte_count</td>
<td>Shows the number of bytes of persistent messages that have been browsed.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Expired message count</td>
<td>expired_message_count</td>
<td>Shows a count of expired messages.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Purged queue count</td>
<td>purged_queue_count</td>
<td>Shows a count of queues that have been purged.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQCB count</td>
<td>mqcb_count</td>
<td>Shows the number of calls to MQCB.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQCB count</td>
<td>failed_mqcb_count</td>
<td>Shows the number of failed calls to MQCB.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQCTL count</td>
<td>mqctl_count</td>
<td>Shows the number of calls to MQCTL.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="2">Commit and rollback</td>
<td>Commit count</td>
<td>commit_count</td>
<td>Shows the number of calls to MQCMIT.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Rollback count</td>
<td>rollback_count</td>
<td>Shows the number of calls to MQBACK.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="14">Subscribe</td>
<td>Create durable subscription count</td>
<td>create_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to create durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Alter durable subscription count</td>
<td>alter_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to alter durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Resume durable subscription count</td>
<td>resume_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to resume durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Create non-durable subscription count</td>
<td>create_non_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to create non-durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed create/alter/resume subscription count</td>
<td>failed_create_alter_resume_subscription_count</td>
<td>Shows the number of failed calls to MQSUBRQ to create, alter, or resume subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Delete durable subscription count</td>
<td>delete_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to delete durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Delete non-durable subscription count</td>
<td>delete_non_durable_subscription_count</td>
<td>Shows the number of calls to MQSUB to delete non-durable subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Subscription delete failure count</td>
<td>subscription_delete_failure_count</td>
<td>Shows the number of calls to MQSUB to delete subscriptions.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQSUBRQ count</td>
<td>mqsubrq_count</td>
<td>Shows the number of calls to MQSUBRQ</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed MQSUBRQ count</td>
<td>failed_mqsubrq_count</td>
<td>Shows the number of failed calls to MQSUBRQ</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Durable subscriber - high water mark</td>
<td>durable_subscriber_high_water_mark</td>
<td>Shows the maximum number of durable subscriptions in the current statistics interval.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Durable subscriber - low water mark</td>
<td>durable_subscriber_low_water_mark</td>
<td>Shows the minimum number of durable subscriptions in the current statistics interval.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-durable subscriber - high water mark</td>
<td>non_durable_subscriber_high_water_mark</td>
<td>Shows the maximum number of non-durable subscriptions in the current statistics interval.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-durable subscriber - low water mark</td>
<td>non_durable_subscriber_low_water_mark</td>
<td>Shows the minimum number of non-durable subscriptions in the current statistics interval.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="7">Publish</td>
<td>Topic MQPUT/MQPUT1 interval total</td>
<td>topic_mqput_mqput1_interval_total</td>
<td>The number of messages that are put to topics.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Interval total topic bytes put</td>
<td>interval_total_topic_bytes_put</td>
<td>The number of message bytes put to topics.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Published to subscribers - message count</td>
<td>published_to_subscribers_message_count</td>
<td>Shows the number of messages that are published to subscribers.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Published to subscribers - byte count</td>
<td>published_to_subscribers_byte_count</td>
<td>Shows the byte count of messages that are published to subscribers.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Non-persistent - topic MQPUT/MQPUT1 count</td>
<td>non_persistent_topic_mqput_mqput1_count</td>
<td>Shows the number of non-persistent messages that are put to topics.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Persistent - topic MQPUT/MQPUT1 count</td>
<td>persistent_topic_mqput_mqput1_count</td>
<td>Shows the number of persistent messages that are put to topics.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>Failed topic MQPUT/MQPUT1 count</td>
<td>failed_topic_mqput_mqput1_count</td>
<td>Shows the number of failed attempts to put to a topic.</td>
<td>No</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="29">&nbsp;API per-queue usage statistics</td>
<td rowspan="2">MQOPEN and MQCLOSE</td>
<td>MQOPEN count</td>
<td>object_mqopen_count</td>
<td>Shows the number of calls to MQOPEN.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQCLOSE count</td>
<td>object_mqclose_count</td>
<td>Shows the number of calls to MQCLOSE.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="2">MQINQ and MQSET</td>
<td>MQINQ count</td>
<td>object_mqinq_count</td>
<td>Shows the number of calls to MQINQ.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQSET count</td>
<td>object_mqset_count</td>
<td>Shows the number of calls to MQSET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td rowspan="11">MQPUT and MQPUT1</td>
<td>MQPUT/MQPUT1 count</td>
<td>object_mqput_mqput1_count</td>
<td>Shows the number of calls to MQPUT and MQPUT1.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQPUT byte count</td>
<td>object_mqput_byte_count</td>
<td>Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQPUT non-persistent message count</td>
<td>object_mqput_non_persistent_message_count</td>
<td>Shows the number of non-persistent messages that are put by MQPUT.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQPUT persistent message count</td>
<td>object_mqput_persistent_message_count</td>
<td>Shows the number of persistent messages that are put by MQPUT.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQPUT1 non-persistent message count</td>
<td>object_mqput1_non_persistent_message_count</td>
<td>Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQPUT1 persistent message count</td>
<td>object_mqput1_persistent_message_count</td>
<td>Shows the number of persistent messages that are put by MQPUT1.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>non-persistent byte count</td>
<td>object_non_persistent_byte_count</td>
<td>Shows the number of bytes put in non-persistent messages.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>persistent byte count</td>
<td>object_persistent_byte_count</td>
<td>Shows the number of bytes put in persistent messages.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>queue avoided puts</td>
<td>object_queue_avoided_puts</td>
<td><span style="color: rgb(0,0,0);">Shows the percentage of messages that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</span></td>
<td>Yes</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>queue avoided bytes</td>
<td>object_queue_avoided_bytes</td>
<td><span style="color: rgb(0,0,0);">Shows the percentage of bytes that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</span></td>
<td>Yes</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td>lock contention</td>
<td>object_lock_contention</td>
<td><span style="color: rgb(0,0,0);">Shows the percentage of attempts to lock the queue that resulted in waiting for another process to release the lock first.</span></td>
<td>Yes</td>
<td>No</td>
<td>percent</td></tr>
<tr>
<td rowspan="14">MQGET</td>
<td>MQGET count</td>
<td>object_mqget_count</td>
<td>Shows the number of calls to MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQGET byte count</td>
<td>object_mqget_byte_count</td>
<td>Shows the total bytes of data that is got by calls to MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>destructive MQGET non-persistent message count</td>
<td>object_destructive_mqget_non_persistent_message_count</td>
<td>Number of non-persistent messages that are removed from the queue by MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>destructive MQGET persistent message count</td>
<td>object_destructive_mqget_persistent_message_count</td>
<td>Number of persistent messages that are removed from the queue by MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>destructive MQGET non-persistent byte count</td>
<td>object_destructive_mqget_non_persistent_byte_count</td>
<td>Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>destructive MQGET persistent byte count</td>
<td>object_destructive_mqget_persistent_byte_count</td>
<td>Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQGET browse non-persistent message count</td>
<td>object_mqget_browse_non_persistent_message_count</td>
<td>Shows a count of non-persistent messages that have been browsed.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQGET browse persistent message count</td>
<td>object_mqget_browse_persistent_message_count</td>
<td>Shows a count of persistent messages that have been browsed.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQGET browse non-persistent byte count</td>
<td>object_mqget_browse_non_persistent_byte_count</td>
<td>Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>MQGET browse persistent byte count</td>
<td>object_mqget_browse_persistent_byte_count</td>
<td>Shows the number of bytes of persistent messages that have been browsed.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>messages expired</td>
<td>object_messages_expired</td>
<td>Shows a count of expired messages.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>queue purged count</td>
<td>object_queue_purged_count</td>
<td>Shows a count of queues that have been purged.</td>
<td>Yes</td>
<td>Yes</td>
<td>delta</td></tr>
<tr>
<td>average queue time</td>
<td>object_average_queue_time</td>
<td>Shows the average latency of messages that are retrieved from the queue.</td>
<td>Yes</td>
<td>No</td>
<td>microseconds</td></tr>
<tr>
<td>Queue depth</td>
<td>object_queue_depth</td>
<td>Shows&nbsp;<span style="color: rgb(34,34,34);">the number of messages on the<span>&nbsp;</span></span>queue.</td>
<td>Yes</td>
<td>No</td>
<td>units</td></tr></tbody></table>

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
