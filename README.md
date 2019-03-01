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

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
