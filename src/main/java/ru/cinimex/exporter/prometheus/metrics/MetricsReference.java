package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinimex.exporter.mq.MQObject;

import java.util.HashMap;

public class MetricsReference {
    private static final Logger logger = LogManager.getLogger(MetricsReference.class);
    private static HashMap<String, Metric> QUEUE_MANAGER_METRICS_REFERENCE = getQueueManagerMetricsReference();
    private static HashMap<MQObject.MQType, AdditionalMetric> MQ_OBJECT_ADDITIONAL_METRICS_REFERENCE = getMqObjectAdditionalMetricsReference();
    private static HashMap<String, Metric> MQ_OBJECT_METRICS_REFERENCE = getMqObjectMetricsReference();
    private static HashMap<Integer, Double> channelStatus = getChannelStatuses();
    private static HashMap<Integer, Double> listenerStatus = getListenerStatuses();

    private static HashMap<String, Metric> getQueueManagerMetricsReference() {
        HashMap<String, Metric> metrics = new HashMap<>();
        metrics.put("User CPU time percentage", new Metric("system_cpu_user_cpu_time_percentage", Metric.Type.SimpleGauge));
        metrics.put("System CPU time percentage", new Metric("system_cpu_cpu_time_percentage", Metric.Type.SimpleGauge));
        metrics.put("CPU load - one minute average", new Metric("system_cpu_cpu_load_one_minute_average_hundredths", Metric.Type.SimpleGauge));
        metrics.put("CPU load - five minute average", new Metric("system_cpu_cpu_load_five_minute_average_hundredths", Metric.Type.SimpleGauge));
        metrics.put("CPU load - fifteen minute average", new Metric("system_cpu_cpu_load_fifteen_minute_average_hundredths", Metric.Type.SimpleGauge));
        metrics.put("RAM free percentage", new Metric("system_ram_ram_free_percentage", Metric.Type.SimpleGauge));
        metrics.put("RAM total bytes", new Metric("system_ram_ram_total_megabytes", Metric.Type.SimpleGauge));
        metrics.put("User CPU time - percentage estimate for queue manager", new Metric("mq_cpu_user_cpu_time_estimate_percentage", Metric.Type.SimpleGauge));
        metrics.put("System CPU time - percentage estimate for queue manager", new Metric("mq_cpu_system_cpu_time_estimat_percentage", Metric.Type.SimpleGauge));
        metrics.put("RAM total bytes - estimate for queue manager", new Metric("mq_ram_ram_total_estimate_megabytes", Metric.Type.SimpleGauge));
        metrics.put("MQ trace file system - bytes in use", new Metric("system_disk_trace_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
        metrics.put("MQ trace file system - free space", new Metric("system_disk_trace_file_system_free_space_percentage", Metric.Type.SimpleGauge));
        metrics.put("MQ errors file system - bytes in use", new Metric("system_disk_errors_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
        metrics.put("MQ errors file system - free space", new Metric("system_disk_errors_file_system_free_space_percentage", Metric.Type.SimpleGauge));
        metrics.put("MQ FDC file count", new Metric("system_disk_fdc_file_count_files", Metric.Type.SimpleGauge));
        metrics.put("Queue Manager file system - bytes in use", new Metric("mq_disk_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
        metrics.put("Queue Manager file system - free space", new Metric("mq_disk_file_system_free_space_percentage", Metric.Type.SimpleGauge));
        metrics.put("Log - bytes in use", new Metric("mq_rlog_log_bytes_in_use_bytes", Metric.Type.SimpleGauge));
        metrics.put("Log - bytes max", new Metric("mq_rlog_log_bytes_max_bytes", Metric.Type.SimpleGauge));
        metrics.put("Log file system - bytes in use", new Metric("mq_rlog_log_file_system_bytes_in_use_bytes", Metric.Type.SimpleGauge));
        metrics.put("Log file system - bytes max", new Metric("mq_rlog_log_file_system_bytes_max_bytes", Metric.Type.SimpleGauge));
        metrics.put("Log - physical bytes written", new Metric("mq_rlog_log_physical_bytes_written_bytes", Metric.Type.SimpleCounter));
        metrics.put("Log - logical bytes written", new Metric("mq_rlog_log_logical_bytes_written_bytes", Metric.Type.SimpleCounter));
        metrics.put("Log - write latency", new Metric("mq_rlog_log_write_latency_microseconds", Metric.Type.SimpleGauge));
        metrics.put("MQCONN/MQCONNX count", new Metric("mq_mqconn_mqconnx_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQCONN/MQCONNX count", new Metric("mq_mqconn_failed_mqconn_mqconnx_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Concurrent connections - high water mark", new Metric("mq_mqconn_concurrent_connections_high_water_mark_totalconnections", Metric.Type.SimpleGauge));
        metrics.put("MQDISC count", new Metric("mq_mqdisc_mqdisc_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQOPEN count", new Metric("mq_mqopen_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQOPEN count", new Metric("mq_mqopen_failed_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQCLOSE count", new Metric("mq_mqclose_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQCLOSE count", new Metric("mq_mqclose_failed_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQINQ count", new Metric("mq_mqinq_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQINQ count", new Metric("mq_mqinq_failed_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQSET count", new Metric("mq_mqset_mqset_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQSET count", new Metric("mq_mqset_failed_mqset_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Interval total MQPUT/MQPUT1 count", new Metric("mq_put_interval_total_mqput_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Interval total MQPUT/MQPUT1 byte count", new Metric("mq_put_interval_total_mqput_mqput1_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent message MQPUT count", new Metric("mq_put_non_persistent_message_mqput_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent message MQPUT count", new Metric("mq_put_persistent_message_mqput_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Failed MQPUT count", new Metric("mq_put_failed_mqput_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent message MQPUT1 count", new Metric("mq_put_non_persistent_message_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent message MQPUT1 count", new Metric("mq_put_persistent_message_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Failed MQPUT1 count", new Metric("mq_put_failed_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Put non-persistent messages - byte count", new Metric("mq_put_put_non_persistent_messages_byte_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Put persistent messages - byte count", new Metric("mq_put_put_persistent_messages_byte_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQSTAT count", new Metric("mq_put_mqstat_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Interval total destructive get- count", new Metric("mq_get_interval_total_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Interval total destructive get - byte count", new Metric("mq_get_interval_total_destructive_get_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent message destructive get - count", new Metric("mq_get_non_persistent_message_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent message destructive get - count", new Metric("mq_get_persistent_message_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Failed MQGET - count", new Metric("mq_get_failed_mqget_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Got non-persistent messages - byte count", new Metric("mq_get_got_non_persistent_messages_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("Got persistent messages - byte count", new Metric("mq_get_got_persistent_messages_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent message browse - count", new Metric("mq_get_non_persistent_message_browse_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent message browse - count", new Metric("mq_get_persistent_message_browse_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Failed browse count", new Metric("mq_get_failed_browse_count_totalbrowses", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent message browse - byte count", new Metric("mq_get_non_persistent_message_browse_byte_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent message browse - byte count", new Metric("mq_get_persistent_message_browse_byte_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Expired message count", new Metric("mq_get_expired_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Purged queue count", new Metric("mq_get_purged_queue_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQCB count", new Metric("mq_get_mqcb_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQCB count", new Metric("mq_get_failed_mqcb_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQCTL count", new Metric("mq_get_mqctl_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Commit count", new Metric("mq_commit_commit_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Rollback count", new Metric("mq_rollback_rollback_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Create durable subscription count", new Metric("mq_subscribe_create_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Alter durable subscription count", new Metric("mq_subscribe_alter_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Resume durable subscription count", new Metric("mq_subscribe_resume_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Create non-durable subscription count", new Metric("mq_subscribe_create_non_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed create/alter/resume subscription count", new Metric("mq_subscribe_failed_create_alter_resume_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Delete durable subscription count", new Metric("mq_subscribe_delete_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Delete non-durable subscription count", new Metric("mq_subscribe_delete_non_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Subscription delete failure count", new Metric("mq_subscribe_subscription_delete_failure_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQSUBRQ count", new Metric("mq_subscribe_mqsubrq_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Failed MQSUBRQ count", new Metric("mq_subscribe_failed_mqsubrq_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("Durable subscriber - high water mark", new Metric("mq_subscribe_durable_subscriber_high_water_mark_subscriptions", Metric.Type.ExtremeGaugeMax));
        metrics.put("Durable subscriber - low water mark", new Metric("mq_subscribe_durable_subscriber_low_water_mark_subscriptions", Metric.Type.ExtremeGaugeMin));
        metrics.put("Non-durable subscriber - high water mark", new Metric("mq_subscribe_non_durable_subscriber_high_water_mark_subscriptions", Metric.Type.ExtremeGaugeMax));
        metrics.put("Non-durable subscriber - low water mark", new Metric("mq_subscribe_non_durable_subscriber_low_water_mark_subscriptions", Metric.Type.ExtremeGaugeMin));
        metrics.put("Topic MQPUT/MQPUT1 interval total", new Metric("mq_publish_topic_mqput_mqput1_interval_total_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Interval total topic bytes put", new Metric("mq_publish_interval_total_topic_bytes_put_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("Published to subscribers - message count", new Metric("mq_publish_published_to_subscribers_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Published to subscribers - byte count", new Metric("mq_publish_published_to_subscribers_byte_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Non-persistent - topic MQPUT/MQPUT1 count", new Metric("mq_publish_non_persistent_topic_mqput_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Persistent - topic MQPUT/MQPUT1 count", new Metric("mq_publish_persistent_topic_mqput_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("Failed topic MQPUT/MQPUT1 count", new Metric("mq_publish_failed_topic_mqput_mqput1_count_totalattempts", Metric.Type.SimpleCounter));
        return metrics;
    }

    private static HashMap<MQObject.MQType, AdditionalMetric> getMqObjectAdditionalMetricsReference() {
        HashMap<MQObject.MQType, AdditionalMetric> metrics = new HashMap<>();
        metrics.put(MQObject.MQType.QUEUE, new AdditionalMetric("mqobject_queue_queue_max_depth_messages", "The maximum number of messages that are allowed on the queue"));
        metrics.put(MQObject.MQType.CHANNEL, new AdditionalMetric("mqobject_channel_channel_status_code", "The status of the channel"));
        metrics.put(MQObject.MQType.LISTENER, new AdditionalMetric("mqobject_listener_listener_status_code", "The status of the listener"));
        return metrics;
    }

    private static HashMap<String, Metric> getMqObjectMetricsReference() {
        HashMap<String, Metric> metrics = new HashMap<>();
        metrics.put("MQOPEN count", new Metric("mqobject_mqopen_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQCLOSE count", new Metric("mqobject_mqclose_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQINQ count", new Metric("mqobject_mqinq_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQSET count", new Metric("mqobject_mqset_mqset_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQPUT/MQPUT1 count", new Metric("mqobject_put_mqput_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQPUT byte count", new Metric("mqobject_put_mqput_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("MQPUT non-persistent message count", new Metric("mqobject_put_mqput_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQPUT persistent message count", new Metric("mqobject_put_mqput_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQPUT1 non-persistent message count", new Metric("mqobject_put_mqput1_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQPUT1 persistent message count", new Metric("mqobject_put_mqput1_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("non-persistent byte count", new Metric("mqobject_put_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("persistent byte count", new Metric("mqobject_put_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("queue avoided puts", new Metric("mqobject_put_queue_avoided_puts_percentage", Metric.Type.SimpleGauge));
        metrics.put("queue avoided bytes", new Metric("mqobject_put_queue_avoided_bytes_percentage", Metric.Type.SimpleGauge));
        metrics.put("lock contention", new Metric("mqobject_put_lock_contention_percentage", Metric.Type.SimpleGauge));
        metrics.put("MQGET count", new Metric("mqobject_get_mqget_count_totalcalls", Metric.Type.SimpleCounter));
        metrics.put("MQGET byte count", new Metric("mqobject_get_mqget_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("destructive MQGET non-persistent message count", new Metric("mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("destructive MQGET persistent message count", new Metric("mqobject_get_destructive_mqget_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("destructive MQGET non-persistent byte count", new Metric("mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("destructive MQGET persistent byte count", new Metric("mqobject_get_destructive_mqget_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("MQGET browse non-persistent message count", new Metric("mqobject_get_mqget_browse_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQGET browse persistent message count", new Metric("mqobject_get_mqget_browse_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("MQGET browse non-persistent byte count", new Metric("mqobject_get_mqget_browse_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("MQGET browse persistent byte count", new Metric("mqobject_get_mqget_browse_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
        metrics.put("messages expired", new Metric("mqobject_get_messages_expired_totalmessages", Metric.Type.SimpleCounter));
        metrics.put("queue purged count", new Metric("mqobject_queue_queue_purged_count_totalqueues", Metric.Type.SimpleCounter));
        metrics.put("average queue time", new Metric("mqobject_queue_average_queue_time_microseconds", Metric.Type.SimpleGauge));
        metrics.put("Queue depth", new Metric("mqobject_queue_queue_depth_messages", Metric.Type.SimpleGauge));
        return metrics;
    }

    private static HashMap<Integer, Double> getChannelStatuses() {
        HashMap<Integer, Double> statuses = new HashMap<>();
        statuses.put(MQConstants.MQCHS_RUNNING, (double) 1);
        statuses.put(MQConstants.MQCHS_REQUESTING, 0.8);
        statuses.put(MQConstants.MQCHS_PAUSED, 0.7);
        statuses.put(MQConstants.MQCHS_BINDING, 0.6);
        statuses.put(MQConstants.MQCHS_STARTING, 0.5);
        statuses.put(MQConstants.MQCHS_INITIALIZING, 0.4);
        statuses.put(MQConstants.MQCHS_SWITCHING, 0.3);
        statuses.put(MQConstants.MQCHS_STOPPING, 0.2);
        statuses.put(MQConstants.MQCHS_RETRYING, 0.1);
        statuses.put(MQConstants.MQCHS_STOPPED, (double) 0);
        statuses.put(MQConstants.MQCHS_INACTIVE, (double) -1);
        return statuses;
    }

    private static HashMap<Integer, Double> getListenerStatuses() {
        HashMap<Integer, Double> statuses = new HashMap<>();
        statuses.put(MQConstants.MQSVC_STATUS_RUNNING, 1.0);
        statuses.put(MQConstants.MQSVC_STATUS_STARTING, 0.75);
        statuses.put(MQConstants.MQSVC_STATUS_STOPPING, 0.5);
        statuses.put(MQConstants.MQSVC_STATUS_STOPPED, 0.0);
        return statuses;
    }

    public static String getMetricName(String description, boolean requiresObject, int datatype) {
        HashMap<String, Metric> ref = requiresObject ? MQ_OBJECT_METRICS_REFERENCE : QUEUE_MANAGER_METRICS_REFERENCE;
        String metricName;
        Metric metric = ref.get(description);
        if (metric == null) {
            metricName = generateMetricName(description, requiresObject, datatype);
            Metric.Type type = datatype == MQConstants.MQIAMO_MONITOR_DELTA ? Metric.Type.SimpleCounter : Metric.Type.SimpleGauge;
            ref.put(description, new Metric(metricName, type));
        } else {
            metricName = metric.name;
        }
        return metricName;
    }

    public static String getMetricName(MQObject.MQType type) {
        return MQ_OBJECT_ADDITIONAL_METRICS_REFERENCE.get(type).name;
    }

    public static String getMetricHelp(MQObject.MQType type) {
        return MQ_OBJECT_ADDITIONAL_METRICS_REFERENCE.get(type).help;
    }

    private static String generateMetricName(String description, boolean requiresObject, int dataType) {
        String metricName = description.toLowerCase().trim();
        metricName = metricName.replaceAll("[-/ ]+", "_");
        metricName = metricName.replace("for_queue_manager", "");
        metricName = metricName.replace("queue_manager", "");
        if (requiresObject) {
            metricName = "object_" + metricName;
        }
        if (!metricName.startsWith("mq_")) metricName = "mq_" + metricName;
        switch (dataType) {
            case (MQConstants.MQIAMO_MONITOR_HUNDREDTHS): {
                metricName = metricName.concat("_hundredths");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_KB): {
                metricName = metricName.replace("_bytes", "");
                metricName = metricName.replace("_byte", "");
                metricName = metricName.concat("_kilobytes");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_MB): {
                metricName = metricName.replace("_bytes", "");
                metricName = metricName.replace("_byte", "");
                metricName = metricName.concat("_megabytes");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_GB): {
                metricName = metricName.replace("_bytes", "");
                metricName = metricName.replace("_byte", "");
                metricName = metricName.concat("_gigabytes");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_MICROSEC): {
                metricName = metricName.concat("_microseconds");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_UNIT): {
                metricName = metricName.concat("_total");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_PERCENT): {
                metricName = metricName.replace("_percentage", "");
                metricName = metricName.concat("_percentage");
                break;
            }
            case (MQConstants.MQIAMO_MONITOR_DELTA): {
                metricName = metricName.concat("_delta");
                break;
            }
            default: {
                logger.warn("Unknown metric type: {}", MQConstants.lookup(dataType, "MQIAMO_"));
            }
        }
        logger.warn("Unknown metric name! Generated new name '{}' from description '{}'", metricName, description);
        return metricName;
    }

    public static double getMetricValue(MQObject.MQType type, int value) {
        double returnValue = -1;
        switch (type) {
            case QUEUE:
                returnValue = value;
                break;
            case CHANNEL:
                returnValue = channelStatus.get(value);
                break;
            case LISTENER:
                returnValue = listenerStatus.get(value);
                break;
            default:
                logger.warn("Unknown metric type {}.", type.name());
                break;
        }
        return returnValue;
    }

    public static Metric.Type getMetricType(String description, boolean requiresObject) {
        HashMap<String, Metric> ref = requiresObject ? MQ_OBJECT_METRICS_REFERENCE : QUEUE_MANAGER_METRICS_REFERENCE;
        Metric.Type type = ref.get(description).type;
        return type;
    }

    public static class AdditionalMetric {
        public final String name;
        public final String help;

        public AdditionalMetric(String name, String help) {
            this.name = name;
            this.help = help;
        }
    }

    public static class Metric {
        public final String name;
        public final Type type;

        public Metric(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public enum Type {
            SimpleCounter, SimpleGauge, ExtremeGaugeMin, ExtremeGaugeMax
        }
    }
}
