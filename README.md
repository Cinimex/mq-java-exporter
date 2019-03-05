# mq-java-exporter

Prometheus exporter for IBM MQ, written in Java. Exposes API of IBM MQ and system metrics of it's host machine.

## Getting Started

#### Compatibility

Support [IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm) version 9.0.x.x.

#### Prerequisites
- [IBM JRE 8 or higher](https://developer.ibm.com/javasdk/downloads/sdk8/) \ [Oracle JRE 8 or higher](https://www.oracle.com/technetwork/java/javase/downloads/index.html) \ [OpenJDK JRE 8 or higher](https://jdk.java.net/java-se-ri/8)
-	[Maven](https://maven.apache.org/)

#### Dependencies
-	[Prometheus](https://prometheus.io)
-	[IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm)

#### Configuration
All settings have to be set in mq-java-exporter\src\main\resources\exporter_config.yaml.
- MQ connection information. Describes MQ connection information.
- Prometheus connection information. Describes Prometheus connection information.
- Monitoring objects. Sets names of objects, that have to be monitored: queues, channels.

1. Fill exporter_config.yaml with your enviroments configuration. 

#### Build

1. Download current repository.
2. Install [Maven](https://maven.apache.org/).
3. Go to mq-java-exporter root folder (where pom.xml is located) and run: 

```shell
mvn package
```

4. After processing is completed, go to mq-java-exporter/target. dependency-jars directory and webspheremq_exporter.jar should appear there. 
5. To run exporter, dependency-jars directory (and all jars in it) and mq_exporter.jar should be located in the same folder.
6. To run exporter execute the following command: 

```shell
 java -jar mq_exporter.jar /opt/mq_exporter/exporter_config.yaml
```

## Metrics
#### Platform central processing units
###### CPU performance - platform wide
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>system_cpu_user_cpu_time_percentage</td>
<td>gauge</td>
<td>Shows the percentage of CPU busy in user state.</td>
<td>User CPU time percentage</td>
</tr>
<tr>
<td>system_cpu_cpu_time_percentage</td>
<td>gauge</td>
<td>Shows the percentage of CPU busy in system state</td>
<td>System CPU time percentage</td>
</tr>
<tr>
<td>system_cpu_cpu_load_one_minute_average_hundredths</td>
<td>gauge</td>
<td>Shows the load average over 1 minute.</td>
<td>CPU load - one minute average</td>
</tr>
<tr>
<td>system_cpu_cpu_load_five_minute_average_hundredths</td>
<td>gauge</td>
<td>Shows the load average over 5 minutes.</td>
<td>CPU load - five minute average</td>
</tr>
<tr>
<td>system_cpu_cpu_load_fifteen_minute_average_hundredths</td>
<td>gauge</td>
<td>Shows the load average over fifteen minutes.&nbsp;</td>
<td>CPU load - fifteen minute average</td>
</tr>
<tr>
<td>system_ram_ram_free_percentage</td>
<td>gauge</td>
<td>Shows the percentage of free RAM memory.</td>
<td>RAM free percentage</td>
</tr>
<tr>
<td>system_ram_ram_total_megabytes</td>
<td>gauge</td>
<td>Shows the total bytes of RAM configured.</td>
<td>RAM total bytes</td>
</tr>
</tbody>
</table>

###### CPU performance - running queue manager
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_cpu_user_cpu_time_estimate_percentage</td>
<td>gauge</td>
<td>Estimates the percentage of CPU use in user state for processes that are related to the queue managers that are being monitored.</td>
<td>User CPU time - percentage estimate for queue manager</td>
</tr>
<tr>
<td>mq_cpu_system_cpu_time_estimat_percentage</td>
<td>gauge</td>
<td>Estimates the percentage of CPU use in system state for processes that are related to the queue managers that are being monitored</td>
<td>System CPU time - percentage estimate for queue manager</td>
</tr>
<tr>
<td>mq_cpu_ram_total_estimate_megabytes</td>
<td>gauge</td>
<td>Estimates the total bytes of RAM in use by the queue managers that are being monitored.</td>
<td>RAM total bytes - estimate for queue manager</td>
</tr>
</tbody>
</table>

#### Platform persistent data stores
###### Disk usage - platform wide
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_disk_trace_file_system_in_use_megabytes</td>
<td>gauge</td>
<td>Shows the number of bytes of disk storage that are being used by the trace file system.</td>
<td>MQ trace file system - bytes in use</td>
</tr>
<tr>
<td>mq_disk_trace_file_system_free_space_percentage</td>
<td>gauge</td>
<td>Shows the disk storage that is reserved for the trace file system that is free.</td>
<td>MQ trace file system - free space</td>
</tr>
<tr>
<td>mq_disk_errors_file_system_in_use_megabytes</td>
<td>gauge</td>
<td>Shows the number of bytes of disk storage that is being used by error data.</td>
<td>MQ errors file system - bytes in use</td>
</tr>
<tr>
<td>mq_disk_errors_file_system_free_space_percentage</td>
<td>gauge</td>
<td>Shows the disk storage that is reserved for error data that is free.</td>
<td>MQ errors file system - free space</td>
</tr>
<tr>
<td>mq_disk_fdc_file_count_files</td>
<td>gauge</td>
<td>Shows the current number of FDC files.</td>
<td>MQ FDC file count</td>
</tr>
</tbody>
</table>

###### Disk usage - running queue managers
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_disk_file_system_in_use_megabytes</td>
<td>gauge</td>
<td>Shows the number of bytes of disk storage that is used by queue manager files for the queue managers that you are monitoring.</td>
<td>Queue Manager file system - bytes in use</td>
</tr>
<tr>
<td>mq_disk_file_system_free_space_percentage</td>
<td>gauge</td>
<td>Shows the disk storage that is reserved for queue manager files that is free.</td>
<td>Queue Manager file system - free space</td>
</tr>
</tbody>
</table>

###### Disk usage - queue manager recovery log
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_rlog_log_bytes_in_use_bytes</td>
<td>gauge</td>
<td>Shows the number of bytes of disk storage that is used for the recovery logs of the queue managers that you are monitoring.</td>
<td>Log - bytes in use</td>
</tr>
<tr>
<td>mq_rlog_log_bytes_max_bytes</td>
<td>gauge</td>
<td>Shows the maximum bytes of disk storage that is configured to be used for queue manager recovery logs.</td>
<td>Log - bytes max</td>
</tr>
<tr>
<td>mq_rlog_log_file_system_bytes_in_use_bytes</td>
<td>gauge</td>
<td>Shows the total number of disk bytes in use for the log file system.</td>
<td>Log file system - bytes in use</td>
</tr>
<tr>
<td>mq_rlog_log_file_system_bytes_max_bytes</td>
<td>gauge</td>
<td>Shows the number of disk bytes that are configured for the log file system.&nbsp;</td>
<td>Log file system - bytes max</td>
</tr>
<tr>
<td>mq_rlog_log_physical_bytes_written_bytes</td>
<td>counter</td>
<td>Shows the number of bytes being written to the recovery logs.</td>
<td>Log - physical bytes written</td>
</tr>
<tr>
<td>mq_rlog_log_logical_bytes_written_bytes</td>
<td>counter</td>
<td>Shows the logical number of bytes written to the recovery logs.</td>
<td>Log - logical bytes written</td>
</tr>
<tr>
<td>mq_rlog_log_write_latency_microseconds</td>
<td>gauge</td>
<td>Shows a measure of the latency when writing synchronously to the queue manager recovery log.</td>
<td>Log - write latency</td>
</tr>
</tbody>
</table>

#### API usage statistics
###### MQCONN and MQDISC
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_mqconn_mqconnx_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCONN and MQCONNX.</td>
<td>MQCONN/MQCONNX count</td>
</tr>
<tr>
<td>mq_mqconn_failed_mqconn_mqconnx_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQCONN and MQCONNX.</td>
<td>Failed MQCONN/MQCONNX count</td>
</tr>
<tr>
<td>mq_mqconn_concurrent_connections_high_water_mark_totalconnections</td>
<td>gauge</td>
<td>Shows the maximum number of concurrent connections in the current statistics interval.</td>
<td>Concurrent connections - high water mark</td>
</tr>
<tr>
<td>mq_mqdisc_mqdisc_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQDISC.</td>
<td>MQDISC count</td>
</tr>
</tbody>
</table>

###### MQOPEN and MQCLOSE
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_mqopen_mqopen_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQOPEN.</td>
<td>MQOPEN count</td>
</tr>
<tr>
<td>mq_mqopen_failed_mqopen_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQOPEN.</td>
<td>Failed MQOPEN count</td>
</tr>
<tr>
<td>mq_mqclose_mqclose_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCLOSE.</td>
<td>MQCLOSE count</td>
</tr>
<tr>
<td>mq_mqclose_failed_mqclose_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQCLOSE.</td>
<td>Failed MQCLOSE count</td>
</tr>
</tbody>
</table>

###### MQINQ and MQSET
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_mqinq_mqinq_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQINQ.</td>
<td>MQINQ count</td>
</tr>
<tr>
<td>mq_mqinq_failed_mqinq_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQINQ.</td>
<td>Failed MQINQ count</td>
</tr>
<tr>
<td>mq_mqset_mqset_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSET.</td>
<td>MQSET count</td>
</tr>
<tr>
<td>mq_mqset_failed_mqset_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQSET.</td>
<td>Failed MQSET count</td>
</tr>
</tbody>
</table>

###### MQPUT
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_put_interval_total_mqput_mqput1_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQPUT and MQPUT1.</td>
<td>Interval total MQPUT/MQPUT1 count</td>
</tr>
<tr>
<td>mq_put_interval_total_mqput_mqput1_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td>Interval total MQPUT/MQPUT1 byte count</td>
</tr>
<tr>
<td>mq_put_non_persistent_message_mqput_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of non-persistent messages that are put by MQPUT.</td>
<td>Non-persistent message MQPUT count</td>
</tr>
<tr>
<td>mq_put_persistent_message_mqput_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of persistent messages that are put by MQPUT.</td>
<td>Persistent message MQPUT count</td>
</tr>
<tr>
<td>mq_put_failed_mqput_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQPUT.</td>
<td>Failed MQPUT count</td>
</tr>
<tr>
<td>mq_put_non_persistent_message_mqput1_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td>Non-persistent message MQPUT1 count</td>
</tr>
<tr>
<td>mq_put_persistent_message_mqput1_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of persistent messages that are put by MQPUT1.</td>
<td>Persistent message MQPUT1 count</td>
</tr>
<tr>
<td>mq_put_failed_mqput1_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQPUT1.</td>
<td>Failed MQPUT1 count</td>
</tr>
<tr>
<td>mq_put_put_non_persistent_messages_byte_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of bytes put in non-persistent messages.</td>
<td>Put non-persistent messages - byte count</td>
</tr>
<tr>
<td>mq_put_put_persistent_messages_byte_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of bytes put in persistent messages.</td>
<td>Put persistent messages - byte count</td>
</tr>
<tr>
<td>mq_put_mqstat_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSTAT.</td>
<td>MQSTAT count</td>
</tr>
</tbody>
</table>

###### MQGET
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
</tr>
<tr>
<td>mq_get_interval_total_destructive_get_count_totalmessages</td>
<td>counter</td>
<td>Number of messages that are removed from queues by MQGET.</td>
<td>Interval total destructive get- count</td>
</tr>
<tr>
<td>mq_get_interval_total_destructive_get_byte_count_totalbytes</td>
<td>counter</td>
<td>Bytes of data that is removed from queues by MQGET.</td>
<td>Interval total destructive get - byte count</td>
</tr>
<tr>
<td>mq_get_non_persistent_message_destructive_get_count_totalmessages</td>
<td>counter</td>
<td>Number of non-persistent messages that are removed from queues by MQGET.</td>
<td>Non-persistent message destructive get - count</td>
</tr>
<tr>
<td>mq_get_persistent_message_destructive_get_count_totalmessages</td>
<td>counter</td>
<td>Number of persistent messages that are removed from queues by MQGET.</td>
<td>Persistent message destructive get - count</td>
</tr>
<tr>
<td>mq_get_failed_mqget_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQGET.</td>
<td>Failed MQGET - count</td>
</tr>
<tr>
<td>mq_get_got_non_persistent_messages_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td>Got non-persistent messages - byte count</td>
</tr>
<tr>
<td>mq_get_got_persistent_messages_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td>Got persistent messages - byte count</td>
</tr>
<tr>
<td>mq_get_non_persistent_message_browse_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of non-persistent messages that have been browsed.</td>
<td>Non-persistent message browse - count</td>
</tr>
<tr>
<td>mq_get_persistent_message_browse_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of persistent messages that have been browsed.</td>
<td>Persistent message browse - count</td>
</tr>
<tr>
<td>mq_get_failed_browse_count_totalbrowses</td>
<td>counter</td>
<td>Shows a count of failed message browses.</td>
<td>Failed browse count</td>
</tr>
<tr>
<td>mq_get_non_persistent_message_browse_byte_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td>Non-persistent message browse - byte count</td>
</tr>
<tr>
<td>mq_get_persistent_message_browse_byte_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of bytes of persistent messages that have been browsed.</td>
<td>Persistent message browse - byte count</td>
</tr>
<tr>
<td>mq_get_expired_message_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of expired messages.</td>
<td>Expired message count</td>
</tr>
<tr>
<td>mq_get_purged_queue_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of queues that have been purged.</td>
<td>Purged queue count</td>
</tr>
<tr>
<td>mq_get_mqcb_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCB.</td>
<td>MQCB count</td>
</tr>
<tr>
<td>mq_get_failed_mqcb_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQCB.</td>
<td>Failed MQCB count</td>
</tr>
<tr>
<td>mq_get_mqctl_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCTL.</td>
<td>MQCTL count</td>
</tr>
</tbody>
</table>

###### Commit and rollback
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
</tr>
<tr>
<td>mq_commit_commit_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCMIT.</td>
<td>Commit count</td>
</tr>
<tr>
<td>mq_rollback_rollback_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQBACK.</td>
<td>Rollback count</td>
</tr>
</tbody>
</table>

###### Subscribe
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
</tr>
<tr>
<td>mq_subscribe_create_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to create durable subscriptions.</td>
<td>Create durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_alter_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to alter durable subscriptions.</td>
<td>Alter durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_resume_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to resume durable subscriptions.</td>
<td>Resume durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_create_non_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to create non-durable subscriptions.</td>
<td>Create non-durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_failed_create_alter_resume_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQSUBRQ to create, alter, or resume subscriptions.</td>
<td>Failed create/alter/resume subscription count</td>
</tr>
<tr>
<td>mq_subscribe_delete_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to delete durable subscriptions.</td>
<td>Delete durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_delete_non_durable_subscription_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to delete non-durable subscriptions.</td>
<td>Delete non-durable subscription count</td>
</tr>
<tr>
<td>mq_subscribe_subscription_delete_failure_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUB to delete subscriptions.</td>
<td>Subscription delete failure count</td>
</tr>
<tr>
<td>mq_subscribe_mqsubrq_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSUBRQ</td>
<td>MQSUBRQ count</td>
</tr>
<tr>
<td>mq_subscribe_failed_mqsubrq_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of failed calls to MQSUBRQ</td>
<td>Failed MQSUBRQ count</td>
</tr>
<tr>
<td>mq_subscribe_durable_subscriber_high_water_mark_subscriptions</td>
<td>counter</td>
<td>Shows the maximum number of durable subscriptions in the current statistics interval.</td>
<td>Durable subscriber - high water mark</td>
</tr>
<tr>
<td>mq_subscribe_durable_subscriber_low_water_mark_subscriptions</td>
<td>counter</td>
<td>Shows the minimum number of durable subscriptions in the current statistics interval.</td>
<td>Durable subscriber - low water mark</td>
</tr>
<tr>
<td>mq_subscribe_non_durable_subscriber_high_water_mark_subscriptions</td>
<td>counter</td>
<td>Shows the maximum number of non-durable subscriptions in the current statistics interval.</td>
<td>Non-durable subscriber - high water mark</td>
</tr>
<tr>
<td>mq_subscribe_non_durable_subscriber_low_water_mark_subscriptions</td>
<td>counter</td>
<td>Shows the minimum number of non-durable subscriptions in the current statistics interval.</td>
<td>Non-durable subscriber - low water mark</td>
</tr>
</tbody>
</table>

###### Publish
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mq_publish_topic_mqput_mqput1_interval_total_totalmessages</td>
<td>counter</td>
<td>The number of messages that are put to topics.</td>
<td>Topic MQPUT/MQPUT1 interval total</td>
</tr>
<tr>
<td>mq_publish_interval_total_topic_bytes_put_totalbytes</td>
<td>counter</td>
<td>The number of message bytes put to topics.</td>
<td>Interval total topic bytes put</td>
</tr>
<tr>
<td>mq_publish_published_to_subscribers_message_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of messages that are published to subscribers.</td>
<td>Published to subscribers - message count</td>
</tr>
<tr>
<td>mq_publish_published_to_subscribers_byte_count_totalmessages</td>
<td>counter</td>
<td>Shows the byte count of messages that are published to subscribers.</td>
<td>Published to subscribers - byte count</td>
</tr>
<tr>
<td>mq_publish_non_persistent_topic_mqput_mqput1_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of non-persistent messages that are put to topics.</td>
<td>Non-persistent - topic MQPUT/MQPUT1 count</td>
</tr>
<tr>
<td>mq_publish_persistent_topic_mqput_mqput1_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of persistent messages that are put to topics.</td>
<td>Persistent - topic MQPUT/MQPUT1 count</td>
</tr>
<tr>
<td>mq_publish_failed_topic_mqput_mqput1_count_totalattempts</td>
<td>counter</td>
<td>Shows the number of failed attempts to put to a topic.</td>
<td>Failed topic MQPUT/MQPUT1 count</td>
</tr>
</tbody>
</table>

#### API per-queue usage statistics
###### MQOPEN and MQCLOSE
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mqobject_mqopen_mqopen_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQOPEN.</td>
<td>MQOPEN count</td>
</tr>
<tr>
<td>mqobject_mqclose_mqclose_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQCLOSE.</td>
<td>MQCLOSE count</td>
</tr>
</tbody>
</table>

###### MQINQ and MQSET
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<td>mqobject_mqinq_mqinq_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQINQ.</td>
<td>MQINQ count</td>
</tr>
<tr>
<td>mqobject_mqset_mqset_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQSET.</td>
<td>MQSET count</td>
</tr>
</tbody>
</table>

###### MQPUT and MQPUT1
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mqobject_put_mqput_mqput1_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQPUT and MQPUT1.</td>
<td>MQPUT/MQPUT1 count</td>
</tr>
<tr>
<td>mqobject_put_mqput_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td>MQPUT byte count</td>
</tr>
<tr>
<td>mqobject_put_mqput_non_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of non-persistent messages that are put by MQPUT.</td>
<td>MQPUT non-persistent message count</td>
</tr>
<tr>
<td>mqobject_put_mqput_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of persistent messages that are put by MQPUT.</td>
<td>MQPUT persistent message count</td>
</tr>
<tr>
<td>mqobject_put_mqput1_non_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td>MQPUT1 non-persistent message count</td>
</tr>
<tr>
<td>mqobject_put_mqput1_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows the number of persistent messages that are put by MQPUT1.</td>
<td>MQPUT1 persistent message count</td>
</tr>
<tr>
<td>mqobject_put_non_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the number of bytes put in non-persistent messages.</td>
<td>non-persistent byte count</td>
</tr>
<tr>
<td>mqobject_put_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the number of bytes put in persistent messages.</td>
<td>persistent byte count</td>
</tr>
<tr>
<td>mqobject_put_queue_avoided_puts_percentage</td>
<td>gauge</td>
<td>Shows the percentage of messages that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</td>
<td>queue avoided puts</td>
</tr>
<tr>
<td>mqobject_put_queue_avoided_bytes_percentage</td>
<td>gauge</td>
<td>Shows the percentage of bytes that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</td>
<td>queue avoided bytes</td>
</tr>
<tr>
<td>mqobject_put_lock_contention_percentage</td>
<td>gauge</td>
<td>Shows the percentage of attempts to lock the queue that resulted in waiting for another process to release the lock first.</td>
<td>lock contention</td>
</tr>
</tbody>
</table>

###### MQGET
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric elemen</strong></td>
</tr>
<tr>
<td>mqobject_get_mqget_count_totalcalls</td>
<td>counter</td>
<td>Shows the number of calls to MQGET.</td>
<td>MQGET count</td>
</tr>
<tr>
<td>mqobject_get_mqget_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the total bytes of data that is got by calls to MQGET.</td>
<td>MQGET byte count</td>
</tr>
<tr>
<td>mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Number of non-persistent messages that are removed from the queue by MQGET.</td>
<td>destructive MQGET non-persistent message count</td>
</tr>
<tr>
<td>mqobject_get_destructive_mqget_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Number of persistent messages that are removed from the queue by MQGET.</td>
<td>destructive MQGET persistent message count</td>
</tr>
<tr>
<td>mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td>destructive MQGET non-persistent byte count</td>
</tr>
<tr>
<td>mqobject_get_destructive_mqget_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td>destructive MQGET persistent byte count</td>
</tr>
<tr>
<td>mqobject_get_mqget_browse_non_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of non-persistent messages that have been browsed.</td>
<td>MQGET browse non-persistent message count</td>
</tr>
<tr>
<td>mqobject_get_mqget_browse_persistent_message_count_totalmessages</td>
<td>counter</td>
<td>Shows a count of persistent messages that have been browsed.</td>
<td>MQGET browse persistent message count</td>
</tr>
<tr>
<td>mqobject_get_mqget_browse_non_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td>MQGET browse non-persistent byte count</td>
</tr>
<tr>
<td>mqobject_get_mqget_browse_persistent_byte_count_totalbytes</td>
<td>counter</td>
<td>Shows the number of bytes of persistent messages that have been browsed.</td>
<td>MQGET browse persistent byte count</td>
</tr>
<tr>
<td>mqobject_get_messages_expired_totalmessages</td>
<td>counter</td>
<td>Shows a count of expired messages.</td>
<td>messages expired</td>
</tr>
<tr>
<td>mqobject_get_queue_purged_count_totalqueues</td>
<td>counter</td>
<td>Shows a count of queues that have been purged.</td>
<td>queue purged count</td>
</tr>
<tr>
<td>mqobject_get_average_queue_time_microseconds</td>
<td>gauge</td>
<td>Shows the average latency of messages that are retrieved from the queue.</td>
<td>average queue time</td>
</tr>
<tr>
<td>mqobject_get_queue_depth_messages</td>
<td>gauge</td>
<td>Shows the number of messages on the queue.</td>
<td>Queue depth</td>
</tr>
</tbody>
</table>

## Issues and Contributions
Feel free to express your thoughts about the exporter, unexpected behaviour and\or issues. New feature suggestions are welcome, use [issue tracker](https://github.com/Cinimex-Informatica/mq-java-exporter/issues). 
Pull requests are always welcome.

## Warning
The exporter is provided as-is with no guarantees. 

## License
The exporter and it's code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
