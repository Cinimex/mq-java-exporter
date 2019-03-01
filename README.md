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
#### Platform central processing units
###### CPU performance - platform wide
<table class="wrapped confluenceTable">
<tbody>
<tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">user_cpu_time_percentage</td>
<td class="confluenceTd">Shows the percentage of CPU busy in user state.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">system_cpu_time_percentage</td>
<td class="confluenceTd">Shows the percentage of CPU busy in system state</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">cpu_load_one_minute_average</td>
<td class="confluenceTd">Shows the load average over 1 minute.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">hundredths</td>
</tr>
<tr>
<td class="confluenceTd">cpu_load_five_minute_average</td>
<td class="confluenceTd">Shows the load average over 5 minutes.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">hundredths</td>
</tr>
<tr>
<td class="confluenceTd">cpu_load_fifteen_minute_average</td>
<td class="confluenceTd">Shows the load average over fifteen minutes. </td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">hundredths</td>
</tr>
<tr>
<td class="confluenceTd">ram_free_percentage</td>
<td class="confluenceTd">Shows the percentage of free RAM memory.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">ram_total_bytes</td>
<td class="confluenceTd">Shows the total bytes of RAM configured.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">megabytes</td>
</tr>
</tbody>
</table>

###### CPU performance - running queue manager
<table class="wrapped confluenceTable">
<tbody>
<tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">user_cpu_time_percentage_estimate_for_queue_manager</td>
<td class="confluenceTd">Estimates the percentage of CPU use in user state for processes that are related to the queue managers that are being monitored.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">system_cpu_time_percentage_estimate_for_queue_manager</td>
<td class="confluenceTd">Estimates the percentage of CPU use in system state for processes that are related to the queue managers that are being monitored</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">ram_total_bytes_estimate_for_queue_manager</td>
<td class="confluenceTd">Estimates the total bytes of RAM in use by the queue managers that are being monitored.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">megabytes</td>
</tr>
</tbody>
</table>

#### Platform persistent data stores
###### Disk usage - platform wide
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">mq_trace_file_system_bytes_in_use</td>
<td class="confluenceTd">Shows the number of bytes of disk storage that are being used by the trace file system.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">megabytes</td>
</tr>
<tr>
<td class="confluenceTd">mq_trace_file_system_free_space</td>
<td class="confluenceTd">Shows the disk storage that is reserved for the trace file system that is free.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">mq_errors_file_system_bytes_in_use</td>
<td class="confluenceTd">Shows the number of bytes of disk storage that is being used by error data.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">megabytes</td>
</tr>
<tr>
<td class="confluenceTd">mq_errors_file_system_free_space</td>
<td class="confluenceTd">Shows the disk storage that is reserved for error data that is free.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">mq_fdc_file_count</td>
<td class="confluenceTd">Shows the current number of FDC files.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
</tbody>
</table>

###### Disk usage - running queue managers
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">queue_manager_file_system_bytes_in_use</td>
<td class="confluenceTd">Shows the number of bytes of disk storage that is used by queue manager files for the queue managers that you are monitoring.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">megabytes</td>
</tr>
<tr>
<td class="confluenceTd">queue_manager_file_system_free_space</td>
<td class="confluenceTd">Shows the disk storage that is reserved for queue manager files that is free.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
</tbody>
</table>

###### Disk usage - queue manager recovery log
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">log_bytes_in_use</td>
<td class="confluenceTd">Shows the number of bytes of disk storage that is used for the recovery logs of the queue managers that you are monitoring.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
<tr>
<td class="confluenceTd">log_bytes_max</td>
<td class="confluenceTd">Shows the maximum bytes of disk storage that is configured to be used for queue manager recovery logs.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
<tr>
<td class="confluenceTd">log_file_system_bytes_in_use</td>
<td class="confluenceTd">Shows the total number of disk bytes in use for the log file system.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
<tr>
<td class="confluenceTd">log_file_system_bytes_max</td>
<td class="confluenceTd">Shows the number of disk bytes that are configured for the log file system. </td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
<tr>
<td class="confluenceTd">log_physical_bytes_written</td>
<td class="confluenceTd">Shows the number of bytes being written to the recovery logs.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">log_logical_bytes_written</td>
<td class="confluenceTd">Shows the logical number of bytes written to the recovery logs.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">log_write_latency</td>
<td class="confluenceTd">Shows a measure of the latency when writing synchronously to the queue manager recovery log.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">microseconds</td>
</tr>
</tbody>
</table>

#### API usage statistics
###### MQCONN and MQDISC
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">mqconn_mqconnx_count</td>
<td class="confluenceTd">Shows the number of calls to MQCONN and MQCONNX.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqconn_mqconnx_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQCONN and MQCONNX.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">concurrent_connections_high_water_mark</td>
<td class="confluenceTd">Shows the maximum number of concurrent connections in the current statistics interval.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
<tr>
<td class="confluenceTd">mqdisc_count</td>
<td class="confluenceTd">Shows the number of calls to MQDISC.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQOPEN and MQCLOSE
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">mqopen_count</td>
<td class="confluenceTd">Shows the number of calls to MQOPEN.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqopen_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQOPEN.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqclose_count</td>
<td class="confluenceTd">Shows the number of calls to MQCLOSE.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqclose_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQCLOSE.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQINQ and MQSET
<table class="wrapped confluenceTable">
<tbody>
  <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">mqinq_count</td>
<td class="confluenceTd">Shows the number of calls to MQINQ.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqinq_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQINQ.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqset_count</td>
<td class="confluenceTd">Shows the number of calls to MQSET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqset_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQSET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQPUT
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">interval_total_mqput_mqput1_count</td>
<td class="confluenceTd">Shows the number of calls to MQPUT and MQPUT1.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">interval_total_mqput_mqput1_byte_count</td>
<td class="confluenceTd">Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_message_mqput_count</td>
<td class="confluenceTd">Shows the number of non-persistent messages that are put by MQPUT.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_message_mqput_count</td>
<td class="confluenceTd">Shows the number of persistent messages that are put by MQPUT.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqput_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQPUT.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_message_mqput1_count</td>
<td class="confluenceTd">Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_message_mqput1_count</td>
<td class="confluenceTd">Shows the number of persistent messages that are put by MQPUT1.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqput1_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQPUT1.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">put_non_persistent_messages_byte_count</td>
<td class="confluenceTd">Shows the number of bytes put in non-persistent messages.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">put_persistent_messages_byte_count</td>
<td class="confluenceTd">Shows the number of bytes put in persistent messages.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqstat_count</td>
<td class="confluenceTd">Shows the number of calls to MQSTAT.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQGET
<table class="wrapped confluenceTable">
<tbody>
<tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">interval_total_destructive_get_count</td>
<td class="confluenceTd">Number of messages that are removed from queues by MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">interval_total_destructive_get_byte_count</td>
<td class="confluenceTd">Bytes of data that is removed from queues by MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_message_destructive_get_count</td>
<td class="confluenceTd">Number of non-persistent messages that are removed from queues by MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_message_destructive_get_count</td>
<td class="confluenceTd">Number of persistent messages that are removed from queues by MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqget_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">got_non_persistent_messages_byte_count</td>
<td class="confluenceTd">Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">got_persistent_messages_byte_count</td>
<td class="confluenceTd">Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_message_browse_count</td>
<td class="confluenceTd">Shows a count of non-persistent messages that have been browsed.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_message_browse_count</td>
<td class="confluenceTd">Shows a count of persistent messages that have been browsed.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_browse_count</td>
<td class="confluenceTd">Shows a count of failed message browses.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_message_browse_byte_count</td>
<td class="confluenceTd">Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_message_browse_byte_count</td>
<td class="confluenceTd">Shows the number of bytes of persistent messages that have been browsed.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">expired_message_count</td>
<td class="confluenceTd">Shows a count of expired messages.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">purged_queue_count</td>
<td class="confluenceTd">Shows a count of queues that have been purged.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqcb_count</td>
<td class="confluenceTd">Shows the number of calls to MQCB.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqcb_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQCB.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqctl_count</td>
<td class="confluenceTd">Shows the number of calls to MQCTL.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### Commit and rollback
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">commit_count</td>
<td class="confluenceTd">Shows the number of calls to MQCMIT.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">rollback_count</td>
<td class="confluenceTd">Shows the number of calls to MQBACK.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### Subscribe
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">create_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to create durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">alter_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to alter durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">resume_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to resume durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">create_non_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to create non-durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_create_alter_resume_subscription_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQSUBRQ to create, alter, or resume subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">delete_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to delete durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">delete_non_durable_subscription_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to delete non-durable subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">subscription_delete_failure_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUB to delete subscriptions.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">mqsubrq_count</td>
<td class="confluenceTd">Shows the number of calls to MQSUBRQ</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_mqsubrq_count</td>
<td class="confluenceTd">Shows the number of failed calls to MQSUBRQ</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">durable_subscriber_high_water_mark</td>
<td class="confluenceTd">Shows the maximum number of durable subscriptions in the current statistics interval.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">durable_subscriber_low_water_mark</td>
<td class="confluenceTd">Shows the minimum number of durable subscriptions in the current statistics interval.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_durable_subscriber_high_water_mark</td>
<td class="confluenceTd">Shows the maximum number of non-durable subscriptions in the current statistics interval.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_durable_subscriber_low_water_mark</td>
<td class="confluenceTd">Shows the minimum number of non-durable subscriptions in the current statistics interval.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### Publish
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">topic_mqput_mqput1_interval_total</td>
<td class="confluenceTd">The number of messages that are put to topics.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">interval_total_topic_bytes_put</td>
<td class="confluenceTd">The number of message bytes put to topics.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">published_to_subscribers_message_count</td>
<td class="confluenceTd">Shows the number of messages that are published to subscribers.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">published_to_subscribers_byte_count</td>
<td class="confluenceTd">Shows the byte count of messages that are published to subscribers.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">non_persistent_topic_mqput_mqput1_count</td>
<td class="confluenceTd">Shows the number of non-persistent messages that are put to topics.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">persistent_topic_mqput_mqput1_count</td>
<td class="confluenceTd">Shows the number of persistent messages that are put to topics.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">failed_topic_mqput_mqput1_count</td>
<td class="confluenceTd">Shows the number of failed attempts to put to a topic.</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

#### API per-queue usage statistics
###### MQOPEN and MQCLOSE
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">object_mqopen_count</td>
<td class="confluenceTd">Shows the number of calls to MQOPEN.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqclose_count</td>
<td class="confluenceTd">Shows the number of calls to MQCLOSE.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQINQ and MQSET
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">object_mqinq_count</td>
<td class="confluenceTd">Shows the number of calls to MQINQ.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqset_count</td>
<td class="confluenceTd">Shows the number of calls to MQSET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
</tbody>
</table>

###### MQPUT and MQPUT1
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">object_mqput_mqput1_count</td>
<td class="confluenceTd">Shows the number of calls to MQPUT and MQPUT1.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqput_byte_count</td>
<td class="confluenceTd">Shows the total bytes of data that is put by calls to MQPUT and MQPUT1.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqput_non_persistent_message_count</td>
<td class="confluenceTd">Shows the number of non-persistent messages that are put by MQPUT.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqput_persistent_message_count</td>
<td class="confluenceTd">Shows the number of persistent messages that are put by MQPUT.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqput1_non_persistent_message_count</td>
<td class="confluenceTd">Shows the number of non-persistent messages that are put by MQPUT1.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqput1_persistent_message_count</td>
<td class="confluenceTd">Shows the number of persistent messages that are put by MQPUT1.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_non_persistent_byte_count</td>
<td class="confluenceTd">Shows the number of bytes put in non-persistent messages.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_persistent_byte_count</td>
<td class="confluenceTd">Shows the number of bytes put in persistent messages.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_queue_avoided_puts</td>
<td class="confluenceTd">Shows the percentage of messages that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">object_queue_avoided_bytes</td>
<td class="confluenceTd">Shows the percentage of bytes that avoided put - if a message is put to a queue when there is a waiting getter, the message may not need to be queued as it may be possible for it to be passed to the getter immediately.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
<tr>
<td class="confluenceTd">object_lock_contention</td>
<td class="confluenceTd">Shows the percentage of attempts to lock the queue that resulted in waiting for another process to release the lock first.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">percent</td>
</tr>
</tbody>
</table>

###### MQGET
<table class="wrapped confluenceTable">
<tbody>
 <tr>
<td class="confluenceTd"><strong>Metric name</strong></td>
<td class="confluenceTd"><strong>Short description</strong></td>
<td class="confluenceTd"><strong>Requires object</strong></td>
<td class="confluenceTd"><strong>Is a counter</strong></td>
<td class="confluenceTd"><strong>Datatype</strong></td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_count</td>
<td class="confluenceTd">Shows the number of calls to MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_byte_count</td>
<td class="confluenceTd">Shows the total bytes of data that is got by calls to MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_destructive_mqget_non_persistent_message_count</td>
<td class="confluenceTd">Number of non-persistent messages that are removed from the queue by MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_destructive_mqget_persistent_message_count</td>
<td class="confluenceTd">Number of persistent messages that are removed from the queue by MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_destructive_mqget_non_persistent_byte_count</td>
<td class="confluenceTd">Shows a count of bytes of non-persistent messages that are returned to MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_destructive_mqget_persistent_byte_count</td>
<td class="confluenceTd">Shows a count of bytes of persistent messages that are returned to MQGET.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_browse_non_persistent_message_count</td>
<td class="confluenceTd">Shows a count of non-persistent messages that have been browsed.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_browse_persistent_message_count</td>
<td class="confluenceTd">Shows a count of persistent messages that have been browsed.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_browse_non_persistent_byte_count</td>
<td class="confluenceTd">Shows the number of bytes of non-persistent messages that have been browsed.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_mqget_browse_persistent_byte_count</td>
<td class="confluenceTd">Shows the number of bytes of persistent messages that have been browsed.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_messages_expired</td>
<td class="confluenceTd">Shows a count of expired messages.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_queue_purged_count</td>
<td class="confluenceTd">Shows a count of queues that have been purged.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">delta</td>
</tr>
<tr>
<td class="confluenceTd">object_average_queue_time</td>
<td class="confluenceTd">Shows the average latency of messages that are retrieved from the queue.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">microseconds</td>
</tr>
<tr>
<td class="confluenceTd">object_queue_depth</td>
<td class="confluenceTd">Shows&nbsp;the number of messages on the&nbsp;queue.</td>
<td class="confluenceTd">Yes</td>
<td class="confluenceTd">No</td>
<td class="confluenceTd">units</td>
</tr>
</tbody>
</table>

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
