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

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/Cinimex-Informatica/mq-java-exporter/blob/master/LICENSE).