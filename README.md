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
<td class="confluenceTd">Shows the load average over fifteen minutes.&nbsp;</td>
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
<td class="confluenceTd">Shows the number of disk bytes that are configured for the log file system.&nbsp;</td>
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

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
