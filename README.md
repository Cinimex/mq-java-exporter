# IBM MQ Exporter

Prometheus exporter for IBM MQ, written in Java. Exposes API of IBM MQ and system metrics of it's host machine.

## Table of contents
1. [Getting Started](#getting-started)
   - [Compatibility](#compatibility)
   - [Prerequisites](#prerequisites)
   - [Dependencies](#dependencies)
   - [Configuration](#configuration)
   - [Build](#build)
   - [Run](#run)
     - [Running exporter as mq service](#running-exporter-as-mq-service)
     - [Running exporter as standalone java application](#running-exporter-as-standalone-java-application)
2. [Metrics](#metrics)
   - [Metrics naming convention](#metrics-naming-convention)
     - [Understanding metrics names](#understanding-metrics-names)
     - [Domains and subdomains](#domains-and-subdomains)
     - [Units](#units)
   - [Metrics list](#metrics-list)
     - [CPU metrics](#cpu-metrics)
       - [CPU performance - platform wide metrics](#cpu-performance---platform-wide-metrics)
       - [CPU performance metrics - running queue manager](#cpu-performance-metrics---running-queue-manager)
     - [Platform persistent data store related metrics](#platform-persistent-data-store-related-metrics)
       - [Disk usage metrics - platform wide](#disk-usage-metrics---platform-wide)
       - [Disk usage metrics - running queue managers](#disk-usage-metrics---running-queue-managers)
       - [Disk usage metrics - queue manager recovery log](#disk-usage-metrics---queue-manager-recovery-log)
     - [API usage metrics](#api-usage-metrics)
       - [MQCONN and MQDISC metrics](#mqconn-and-mqdisc-metrics)
       - [MQOPEN and MQCLOSE metrics](#mqopen-and-mqclose-metrics)
       - [MQINQ and MQSET metrics](#mqinq-and-mqset-metrics)
       - [MQPUT metrics](#mqput-metrics)
       - [MQGET metrics](#mqget-metrics)
       - [Commit and rollback metrics](#commit-and-rollback-metrics)
       - [Subscribe metrics](#subscribe-metrics)
       - [Publish metrics](#publish-metrics)
     - [API per-queue usage statistics](#api-per-queue-usage-statistics)
       - [MQOPEN and MQCLOSE](#mqopen-and-mqclose-1)
       - [MQINQ and MQSET](#mqinq-and-mqset-1)
       - [MQPUT and MQPUT1](#mqput-and-mqput1)
       - [MQGET](#mqget-1)
     - [MQ PCF API specific statistics](#mq-pcf-api-specific-statistics)
       - [PCF requests](#pcf-requests)
       - [MQ constants mapping](#mq-constants-mapping)
         - [Channel status mapping](#channel-status-mapping)
         - [Listener status mapping](#listener-status-mapping)
3. [Issues and Contributions](#issues-and-contributions)
4. [Known issues](#known-issues)
5. [Warning](#warning)
6. [License](#license)

## Getting Started
#### Compatibility
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
Supports [IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm) version 9.0.x.x and above.

Was tested on MQ ver.9.0.x.x and MQ ver. 9.1.x.x.

#### Prerequisites
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
List of prerequisites:
- [IBM JRE 8 or higher](https://developer.ibm.com/javasdk/downloads/sdk8/) \ [Oracle JRE 8 or higher](https://www.oracle.com/technetwork/java/javase/downloads/index.html) \ [OpenJDK JRE 8 or higher](https://jdk.java.net/java-se-ri/8)
-	[Maven](https://maven.apache.org/)

#### Dependencies
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
List of dependencies:
-	[Prometheus](https://prometheus.io)
-	[IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.helphome.v90.doc/WelcomePagev9r0.htm)

#### Configuration
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
All connection and monitoring settings have to be set in exporter_config.yaml file.
Below is an example of a filled configuration file with all possible fields:
```yaml
# MQ connection information -------------------------------
qmgrConnectionParams:
# Queue manager name.
  qmgrName: QM
# Queue manager host.
  qmgrHost: hostname
# Queue manager connection port.
  qmgrPort: 1414
# Queue manager connection channel.
  qmgrChannel: SYSTEM.DEF.SVRCONN
# Username, which will be used for connection (optional).
  user: mqm
# Password, which will be used for connection (optional).
  password: mqm
# Use MQCSP for connection?
  mqscp: false

# Prometheus connection information -------------------------------
prometheusEndpointParams:
# URL and port which will be used to expose metrics for Prometheus.
  url: /metrics
  port: 8080


# Monitoring objects ----------------------------------
# This block refers to collecting of additional metrics.
# If there are any queues, channels or listeners in the config file below,
# these metrics may be useful for you. (More info about additional metrics is located 
# under "MQ PCF API specific statistics" section.   
PCFParameters:
# Collect additional metrics? If false, all settings in this section below are ignored. 
  sendPCFCommands: true
# Use wildcards? If yes, only one PCF command will be send, matching all objects on queue manager. Otherwise, each 
# object will be monitored by separate PCF command.  
  usePCFWildcards: true
# Interval in seconds between sending PCF commands.
  scrapeInterval: 10

# Monitored queues.
queues:
  - QUEUE1
  - QUEUE2

# Monitored listeners.
listeners:
  - LISTENER01
 
# Monitored channels.
channels:
 - MANAGEMENT.CHANNEL
```
#### Build
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
Steps, that need to be taken to build the exporter:
1. Download current repository.
2. Install [Maven](https://maven.apache.org/).
3. Go to mq-java-exporter root folder (where pom.xml is located) and run: 

```shell
mvn package
```

4. After processing is completed, go to mq-java-exporter/target. dependency-jars directory and mq_exporter.jar should appear there.

#### Run
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
To run exporter, dependency-jars directory (and all jars in it) and
mq_exporter.jar should be located in the same folder.

##### Running exporter as mq service
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
It is recommended way of running the exporter. **Note**: all commands
 should be executed via MQ CLI. More info can be found [here](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.ref.adm.doc/q083460_.htm).
 
 Define queue manager service with the following command: 
 
 ```mq
  DEFINE SERVICE(MQEXPORTER) CONTROL(QMGR) SERVTYPE(SERVER) +
  STARTCMD('/opt/mqm/java/jre64/jre/bin/java')              +
  STARTARG('-Dlog4j.configurationFile=/opt/mq_exporter/log4j2.properties -jar /opt/mq_exporter/mq_exporter.jar /opt/mq_exporter/exporter_config.yaml') +
  STOPCMD('/usr/bin/kill ' ) STOPARG(+MQ_SERVER_PID+)       +
  STDOUT('/opt/mq_exporter/mq_prometheus.out')              +
  STDERR('/opt/mq_exporter/mq_prometheus.out')              +
  DESCR('MQ exporter for Prometheus')
 ```
 More information about this command can be found
 [here](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.ref.adm.doc/q085740_.htm).
  
 To start exporter, execute the following command:
 
 ```mq
  START SERVICE(MQEXPORTER)
 ```
 
 To stop exporter, execute the following command:
 
  ```mq
   STOP SERVICE(MQEXPORTER)
  ```
##### Running exporter as standalone java application
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
To run exporter execute the following command:

```shell
 java -jar mq_exporter.jar /opt/mq_exporter/exporter_config.yaml
```
The only input parameter is the path to your configuration file.

## Metrics
### Metrics naming convention
#### Understanding metrics names
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
All metrics have predefined structure: domain, subdomain, name, units:

<img src="/docs/images/metric_naming_example_1.png" data-canonical-src="/docs/images/metric_naming_example_1.png" width="554" height="120" />

- **Domain** - the first single-word prefix that represents a metric type. The examples of domain-level prefixes are: system, mq, mqobject and etc. More information can be found in ["Domains and subdomains" section](#domains-and-subdomains).
- **Subdomain** - second single-word prefix representation of a metric type. It provides more specific information about metric type and helps to differentiate metrics in a single domain. The examples of subdomain-level prefixes are: cpu, ram, put, subscribe, get and etc. More information can be found in ["Domains and subdomains" section](#domains-and-subdomains).
- **Units** - single-word suffix describing the metric's unit, in plural form. Note that an accumulating count has "total" as the first part of a suffix. The examples of unit suffixes are: percentage, hundredths, messages, totalmessages and etc. More information can be found in ["Units" section](#units).
- **Name** - represents metric meaning. The examples of a metric name are: cpu_time, cpu_load_fifteen_minute_average, failed_mqget_count and etc. Note that the amount of words in a metric name can vary:
 
<img src="/docs/images/metric_naming_example_2.png" data-canonical-src="/docs/images/metric_naming_example_2.png" width="899" height="120" />

#### Domains and subdomains
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of domains and subdomains and reflects their relations.
<table>
<tbody>
<tr>
<td><strong>Domain</strong></td>
<td><strong>Domain description</strong></td>
<td><strong>Subdomain</strong></td>
<td><strong>Subdomain description</strong></td>
</tr>
<tr>
<td rowspan="2">system</td>
<td rowspan="2">Platform wide system metrics</td>
<td>cpu</td>
<td>CPU-related performance metrics</td>
</tr>
<tr>
<td>ram</td>
<td>RAM-related performance metrics</td>
</tr>
<tr>
<td rowspan="15">mq</td>
<td rowspan="15">MQ manager wide metrics</td>
<td>cpu</td>
<td>CPU metrics of a running queue manager</td>
</tr>
<tr>
<td>disk</td>
<td>Disk usage metrics, related to a running queue manager</td>
</tr>
<tr>
<td>rlog</td>
<td>Queue manager recovery log metrics</td>
</tr>
<tr>
<td>mqconn</td>
<td>Metrics related to MQCONN calls to a queue manager</td>
</tr>
<tr>
<td>mqdisc</td>
<td>Metrics related to MQDISC calls to a queue manager</td>
</tr>
<tr>
<td>mqopen</td>
<td>Metrics related to MQOPEN calls to a queue manager</td>
</tr>
<tr>
<td>mqclose</td>
<td>Metrics related to MQCLOSE calls to a queue manager</td>
</tr>
<tr>
<td>mqinq</td>
<td>Metrics related to MQINQ calls to a queue manager</td>
</tr>
<tr>
<td>mqset</td>
<td>Metrics related to MQSET calls to a queue manager</td>
</tr>
<tr>
<td>put</td>
<td>Metrics related to MQPUT, MQPUT1 and MQSTAT calls to a queue manager</td>
</tr>
<tr>
<td>get</td>
<td>Metrics related to MQGET, MQCB and MQCTL calls to a queue manager</td>
</tr>
<tr>
<td>commit</td>
<td>Metrics related to MQCMIT calls to a queue manager</td>
</tr>
<tr>
<td>rollback</td>
<td>Metrics related to MQBACK calls to a queue manager</td>
</tr>
<tr>
<td>subscribe</td>
<td>Metrics related to subscriptions of a queue manager</td>
</tr>
<tr>
<td>publish</td>
<td>Metrics related to publications of a queue manager</td>
</tr>   
<tr>
<td rowspan="9">mqobject</td>
<td rowspan="9">Metrics for specific objects of a queue manager: for a queue, for a channel, for a listener</td>
<td>mqopen</td>
<td>Metrics related to MQOPEN calls to a specific queue</td>
</tr>
<tr>
<td>mqclose</td>
<td>Metrics related to MQCLOSE calls to a specific queue</td>
</tr>
<tr>
<td>mqinq</td>
<td>Metrics related to MQINQ calls to a specific queue</td>
</tr>
<tr>
<td>mqset</td>
<td>Metrics related to MQSET calls to a specific queue</td>
</tr>
<tr>
<td>put</td>
<td>Metrics related to MQPUT and MQPUT1 calls to a specific queue</td>
</tr>
<tr>
<td>get</td>
<td>Metrics related to MQGET calls to a specific queue</td>
</tr>   
<tr>
<td>queue</td>
<td>Metrics related to a specific queue</td>
</tr>
<tr>
<td>channel</td>
<td>Metrics related to a specific channel</td>
</tr>
<tr>
<td>listener</td>
<td>Metrics related to a specific listener</td>
</tr>
</tbody>
</table>
   
#### Units
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metric units.<br/>
["Metric and label naming"](https://prometheus.io/docs/practices/naming/#metric-and-label-naming) article by Prometheus states that metrics "...should use base units (e.g. seconds, bytes, meters - not milliseconds, megabytes, kilometers)". But it is not usefull for using IBM MQ exporter. So the exporter has following list of units:

<table>
<tbody>
<tr>
<td><strong>Unit</strong></td>
<td><strong>Unit description</strong></td>
</tr>
<tr>
<td>percentage</td>
<td>Shows %</td>
</tr>
<tr>
<td>hundredths</td>
<td>Shows amount of hundredths. For example, "370 hundredths equal" to "3.70". It is used to reflect system's load average.</td>
</tr>
<tr>
<td>megabytes</td>
<td>Shows amount of megabytes</td>
</tr>
<tr>
<td>files</td>
<td>Shows amount of files</td>
</tr>
<tr>
<td>bytes</td>
<td>Shows amount of bytes</td>
</tr>
<tr>
<td>microseconds</td>
<td>Shows amount of microseconds.</td>
</tr>
<tr>
<td>totalcalls</td>
<td>Shows amount of calls. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>totalconnections</td>
<td>Shows amount of connections. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>totalmessages</td>
<td>Shows amount of messages. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>totalbytes</td>
<td>Shows amount of bytes. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>totalbrowses</td>
<td>Shows amount of browses. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>subscriptions</td>
<td>Shows amount of subscriptions.</td>
</tr>
<tr>
<td>totalattempts</td>
<td>Shows amount of attempts. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>totalqueues</td>
<td>Shows amount of queues. An accumulating count has "total" as the first part of a suffix.</td>
</tr>
<tr>
<td>messages</td>
<td>Shows amount of messages.</td>
</tr>
</tbody>
</table>

### Metrics list
#### CPU metrics
###### CPU performance - platform wide metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of CPU- and RAM-related metrics.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### CPU performance metrics - running queue manager
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of CPU metrics of a running queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

#### Platform persistent data store related metrics
###### Disk usage metrics - platform wide
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of disk usage metrics.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### Disk usage metrics - running queue managers
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of disk usage metrics, related to a running queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### Disk usage metrics - queue manager recovery log
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of disk usage metrics, related to queue manager recovery log.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

#### API usage metrics
###### MQCONN and MQDISC metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQCONN and MQDISC calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQOPEN and MQCLOSE metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQOPEN and MQCLOSE calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQINQ and MQSET metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQINQ and MQSET calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQPUT metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQPUT, MQPUT1 and MQSTAT calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQGET metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQGET, MQCB and MQCTL calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### Commit and rollback metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQCMIT and MQBACK calls to a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### Subscription metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to subscriptions of a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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
<td>gauge</td>
<td>Shows the maximum number of durable subscriptions in the current statistics interval.</td>
<td>Durable subscriber - high water mark</td>
</tr>
<tr>
<td>mq_subscribe_durable_subscriber_low_water_mark_subscriptions</td>
<td>gauge</td>
<td>Shows the minimum number of durable subscriptions in the current statistics interval.</td>
<td>Durable subscriber - low water mark</td>
</tr>
<tr>
<td>mq_subscribe_non_durable_subscriber_high_water_mark_subscriptions</td>
<td>gauge</td>
<td>Shows the maximum number of non-durable subscriptions in the current statistics interval.</td>
<td>Non-durable subscriber - high water mark</td>
</tr>
<tr>
<td>mq_subscribe_non_durable_subscriber_low_water_mark_subscriptions</td>
<td>gauge</td>
<td>Shows the minimum number of non-durable subscriptions in the current statistics interval.</td>
<td>Non-durable subscriber - low water mark</td>
</tr>
</tbody>
</table>

###### Publication metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to publications  of a queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

#### API per-queue usage metrics
###### MQOPEN and MQCLOSE metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQOPEN and MQCLOSE calls to a specific queue.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQINQ and MQSET metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQINQ and MQSET calls to a specific queue.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQPUT and MQPUT1 metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQPUT and MQPUT1 calls to a specific queue.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

###### MQGET metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics related to MQGET calls to a specific queue.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
<td><strong>MQ metric element</strong></td>
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

#### MQ PCF API specific metrics
##### Obtained by PCF commands metrics
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a description of metrics of queues, channels and listeners that are collected via sending direct PCF commands to queue manager.
<table>
<tbody>
<tr>
<td><strong>Prometheus metric name</strong></td>
<td><strong>Metric type</strong></td>
<td><strong>Short description</strong></td>
</tr>
<tr>
<td>mqobject_queue_max_depth_messages</td>
<td>gauge</td>
<td>Shows maximum number of messages that are allowed on the queue.</td>
</tr>
<tr>
<td>mqobject_channel_status_code</td>
<td>gauge</td>
<td>Shows current channel status.</td>
</tr>
<tr>
<td>mqobject_listener_status_code</td>
<td>gauge</td>
<td>Shows current listener status.</td>
</tr>
</tbody>
</table>

##### MQ constants mapping
###### Channel status mapping
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a mapping between MQ channel statuses and metric values, that are sent to Prometheus.
<table>
<tbody>
<tr>
<td><strong>MQ channel status code</strong></td>
<td><strong>Prometheus metric value</strong></td>
</tr>
<tr>
<td>RUNNING</td>
<td>1</td>
</tr>
<tr>
<td>REQUESTING</td>
<td>0.8</td>
</tr>
<tr>
<td>PAUSED</td>
<td>0.7</td>
</tr>
<tr>
<td>BINDING</td>
<td>0.6</td>
</tr>
<tr>
<td>STARTING</td>
<td>0.5</td>
</tr>
<tr>
<td>INITIALIZING</td>
<td>0.4</td>
</tr>
<tr>
<td>SWITCHING</td>
<td>0.3</td>
</tr>
<tr>
<td>STOPPING</td>
<td>0.2</td>
</tr>
<tr>
<td>RETRYING</td>
<td>0.1</td>
</tr>
<tr>
<td>STOPPED</td>
<td>0</td>
</tr>
<tr>
<td>INACTIVE</td>
<td>-1</td>
</tr>
</tbody>
</table>

<b>Note</b>: If channel has status INACTIVE, there is no way to retrieve it's status by PCF command (because technically channel has no status) and MQRCCF_CHL_STATUS_NOT_FOUND will be returned by queue manager. Since INACTIVE status of the channel is the most frequent reason for receiving such an error, the exporter interprets it as INACTIVE status of the channel.

###### Listener status mapping
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
This section provides a mapping between MQ listener statuses and metric values, that are sent to Prometheus.
<table>
<tbody>
<tr>
<td><strong>MQ listener status code</strong></td>
<td><strong>Prometheus metric value</strong></td>
</tr>
<tr>
<td>RUNNING</td>
<td>1</td>
</tr>
<tr>
<td>STARTING</td>
<td>0.5</td>
</tr>
<tr>
<td>STOPPING</td>
<td>0</td>
</tr>
</tbody>
</table>

## Issues and Contributions
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
Feel free to express your thoughts about the exporter, unexpected behaviour and\or issues. New feature suggestions are welcome, use [issue tracker](https://github.com/Cinimex-Informatica/mq-java-exporter/issues). 
Pull requests are always welcome.

## Known issues
The following are known issues and may affect your use of exporter.

* Metric mq_cpu_ram_total_estimate_megabytes may contain negative
  values.
  [#62](https://github.com/Cinimex-Informatica/mq-java-exporter/issues/62)
  
   This problem is related to this IBM
   [APAR](https://www-01.ibm.com/support/docview.wss?uid=swg1IT24336).
   The problem appeared during testing the exporter on MQ ver. 9.0.1.0.
   We could not reproduce this problem on MQ ver. 9.1.0.1 (this version
   includes fix for APAR above). Unfortunately, there is no way to fix
   this problem on the exporter side and the only option is to wait for
   the fix from IBM for MQ ver. 9.0.x.x.

## Warning
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
The exporter is provided as-is with no guarantees. 

## License
<sub><sup> [Back to TOC.](#table-of-contents) </sup></sub><br/>
The exporter and it's code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).
