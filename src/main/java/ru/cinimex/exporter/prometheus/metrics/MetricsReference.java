package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import ru.cinimex.exporter.mq.MQObject;

import java.util.HashMap;

public class MetricsReference {

    private static HashMap<String, Metric> QUEUE_MANAGER_METRICS_REFERENCE = new HashMap<String, Metric>() {
        {
            put("User CPU time percentage", new Metric("system_cpu_user_cpu_time_percentage", Metric.Type.SimpleGauge));
            put("System CPU time percentage", new Metric("system_cpu_cpu_time_percentage", Metric.Type.SimpleGauge));
            put("CPU load - one minute average", new Metric("system_cpu_cpu_load_one_minute_average_hundredths", Metric.Type.SimpleGauge));
            put("CPU load - five minute average", new Metric("system_cpu_cpu_load_five_minute_average_hundredths", Metric.Type.SimpleGauge));
            put("CPU load - fifteen minute average", new Metric("system_cpu_cpu_load_fifteen_minute_average_hundredths", Metric.Type.SimpleGauge));
            put("RAM free percentage", new Metric("system_ram_ram_free_percentage", Metric.Type.SimpleGauge));
            put("RAM total bytes", new Metric("system_ram_ram_total_megabytes", Metric.Type.SimpleGauge));
            put("User CPU time - percentage estimate for queue manager", new Metric("mq_cpu_user_cpu_time_estimate_percentage", Metric.Type.SimpleGauge));
            put("System CPU time - percentage estimate for queue manager", new Metric("mq_cpu_system_cpu_time_estimat_percentage", Metric.Type.SimpleGauge));
            put("RAM total bytes - estimate for queue manager", new Metric("mq_cpu_ram_total_estimate_megabytes", Metric.Type.SimpleGauge));
            put("MQ trace file system - bytes in use", new Metric("mq_disk_trace_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
            put("MQ trace file system - free space", new Metric("mq_disk_trace_file_system_free_space_percentage", Metric.Type.SimpleGauge));
            put("MQ errors file system - bytes in use", new Metric("mq_disk_errors_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
            put("MQ errors file system - free space", new Metric("mq_disk_errors_file_system_free_space_percentage", Metric.Type.SimpleGauge));
            put("MQ FDC file count", new Metric("mq_disk_fdc_file_count_files", Metric.Type.SimpleGauge));
            put("Queue Manager file system - bytes in use", new Metric("mq_disk_file_system_in_use_megabytes", Metric.Type.SimpleGauge));
            put("Queue Manager file system - free space", new Metric("mq_disk_file_system_free_space_percentage", Metric.Type.SimpleGauge));
            put("Log - bytes in use", new Metric("mq_rlog_log_bytes_in_use_bytes", Metric.Type.SimpleGauge));
            put("Log - bytes max", new Metric("mq_rlog_log_bytes_max_bytes", Metric.Type.SimpleGauge));
            put("Log file system - bytes in use", new Metric("mq_rlog_log_file_system_bytes_in_use_bytes", Metric.Type.SimpleGauge));
            put("Log file system - bytes max", new Metric("mq_rlog_log_file_system_bytes_max_bytes", Metric.Type.SimpleGauge));
            put("Log - physical bytes written", new Metric("mq_rlog_log_physical_bytes_written_bytes", Metric.Type.SimpleCounter));
            put("Log - logical bytes written", new Metric("mq_rlog_log_logical_bytes_written_bytes", Metric.Type.SimpleCounter));
            put("Log - write latency", new Metric("mq_rlog_log_write_latency_microseconds", Metric.Type.SimpleGauge));
            put("MQCONN/MQCONNX count", new Metric("mq_mqconn_mqconnx_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQCONN/MQCONNX count", new Metric("mq_mqconn_failed_mqconn_mqconnx_count_totalcalls", Metric.Type.SimpleCounter));
            put("Concurrent connections - high water mark", new Metric("mq_mqconn_concurrent_connections_high_water_mark_totalconnections", Metric.Type.SimpleGauge));
            put("MQDISC count", new Metric("mq_mqdisc_mqdisc_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQOPEN count", new Metric("mq_mqopen_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQOPEN count", new Metric("mq_mqopen_failed_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQCLOSE count", new Metric("mq_mqclose_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQCLOSE count", new Metric("mq_mqclose_failed_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQINQ count", new Metric("mq_mqinq_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQINQ count", new Metric("mq_mqinq_failed_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQSET count", new Metric("mq_mqset_mqset_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQSET count", new Metric("mq_mqset_failed_mqset_count_totalcalls", Metric.Type.SimpleCounter));
            put("Interval total MQPUT/MQPUT1 count", new Metric("mq_put_interval_total_mqput_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
            put("Interval total MQPUT/MQPUT1 byte count", new Metric("mq_put_interval_total_mqput_mqput1_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("Non-persistent message MQPUT count", new Metric("mq_put_non_persistent_message_mqput_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent message MQPUT count", new Metric("mq_put_persistent_message_mqput_count_totalmessages", Metric.Type.SimpleCounter));
            put("Failed MQPUT count", new Metric("mq_put_failed_mqput_count_totalcalls", Metric.Type.SimpleCounter));
            put("Non-persistent message MQPUT1 count", new Metric("mq_put_non_persistent_message_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent message MQPUT1 count", new Metric("mq_put_persistent_message_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
            put("Failed MQPUT1 count", new Metric("mq_put_failed_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
            put("Put non-persistent messages - byte count", new Metric("mq_put_put_non_persistent_messages_byte_count_totalmessages", Metric.Type.SimpleCounter));
            put("Put persistent messages - byte count", new Metric("mq_put_put_persistent_messages_byte_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQSTAT count", new Metric("mq_put_mqstat_count_totalcalls", Metric.Type.SimpleCounter));
            put("Interval total destructive get- count", new Metric("mq_get_interval_total_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
            put("Interval total destructive get - byte count", new Metric("mq_get_interval_total_destructive_get_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("Non-persistent message destructive get - count", new Metric("mq_get_non_persistent_message_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent message destructive get - count", new Metric("mq_get_persistent_message_destructive_get_count_totalmessages", Metric.Type.SimpleCounter));
            put("Failed MQGET - count", new Metric("mq_get_failed_mqget_count_totalcalls", Metric.Type.SimpleCounter));
            put("Got non-persistent messages - byte count", new Metric("mq_get_got_non_persistent_messages_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("Got persistent messages - byte count", new Metric("mq_get_got_persistent_messages_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("Non-persistent message browse - count", new Metric("mq_get_non_persistent_message_browse_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent message browse - count", new Metric("mq_get_persistent_message_browse_count_totalmessages", Metric.Type.SimpleCounter));
            put("Failed browse count", new Metric("mq_get_failed_browse_count_totalbrowses", Metric.Type.SimpleCounter));
            put("Non-persistent message browse - byte count", new Metric("mq_get_non_persistent_message_browse_byte_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent message browse - byte count", new Metric("mq_get_persistent_message_browse_byte_count_totalmessages", Metric.Type.SimpleCounter));
            put("Expired message count", new Metric("mq_get_expired_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("Purged queue count", new Metric("mq_get_purged_queue_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQCB count", new Metric("mq_get_mqcb_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQCB count", new Metric("mq_get_failed_mqcb_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQCTL count", new Metric("mq_get_mqctl_count_totalcalls", Metric.Type.SimpleCounter));
            put("Commit count", new Metric("mq_commit_commit_count_totalcalls", Metric.Type.SimpleCounter));
            put("Rollback count", new Metric("mq_rollback_rollback_count_totalcalls", Metric.Type.SimpleCounter));
            put("Create durable subscription count", new Metric("mq_subscribe_create_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Alter durable subscription count", new Metric("mq_subscribe_alter_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Resume durable subscription count", new Metric("mq_subscribe_resume_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Create non-durable subscription count", new Metric("mq_subscribe_create_non_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed create/alter/resume subscription count", new Metric("mq_subscribe_failed_create_alter_resume_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Delete durable subscription count", new Metric("mq_subscribe_delete_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Delete non-durable subscription count", new Metric("mq_subscribe_delete_non_durable_subscription_count_totalcalls", Metric.Type.SimpleCounter));
            put("Subscription delete failure count", new Metric("mq_subscribe_subscription_delete_failure_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQSUBRQ count", new Metric("mq_subscribe_mqsubrq_count_totalcalls", Metric.Type.SimpleCounter));
            put("Failed MQSUBRQ count", new Metric("mq_subscribe_failed_mqsubrq_count_totalcalls", Metric.Type.SimpleCounter));
            put("Durable subscriber - high water mark", new Metric("mq_subscribe_durable_subscriber_high_water_mark_subscriptions", Metric.Type.SimpleGauge));
            put("Durable subscriber - low water mark", new Metric("mq_subscribe_durable_subscriber_low_water_mark_subscriptions", Metric.Type.SimpleGauge));
            put("Non-durable subscriber - high water mark", new Metric("mq_subscribe_non_durable_subscriber_high_water_mark_subscriptions", Metric.Type.SimpleGauge));
            put("Non-durable subscriber - low water mark", new Metric("mq_subscribe_non_durable_subscriber_low_water_mark_subscriptions", Metric.Type.SimpleGauge));
            put("Topic MQPUT/MQPUT1 interval total", new Metric("mq_publish_topic_mqput_mqput1_interval_total_totalmessages", Metric.Type.SimpleCounter));
            put("Interval total topic bytes put", new Metric("mq_publish_interval_total_topic_bytes_put_totalbytes", Metric.Type.SimpleCounter));
            put("Published to subscribers - message count", new Metric("mq_publish_published_to_subscribers_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("Published to subscribers - byte count", new Metric("mq_publish_published_to_subscribers_byte_count_totalmessages", Metric.Type.SimpleCounter));
            put("Non-persistent - topic MQPUT/MQPUT1 count", new Metric("mq_publish_non_persistent_topic_mqput_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
            put("Persistent - topic MQPUT/MQPUT1 count", new Metric("mq_publish_persistent_topic_mqput_mqput1_count_totalmessages", Metric.Type.SimpleCounter));
            put("Failed topic MQPUT/MQPUT1 count", new Metric("mq_publish_failed_topic_mqput_mqput1_count_totalattempts", Metric.Type.SimpleCounter));
        }
    };

    private static HashMap<MQObject.MQType, AdditionalMetric> MQ_OBJECT_ADDITIONAL_METRICS_REFERENCE = new HashMap<MQObject.MQType, AdditionalMetric>() {
        {
            put(MQObject.MQType.QUEUE, new AdditionalMetric("mqobject_queue_max_depth_messages", "The maximum number of messages that are allowed on the queue"));
            put(MQObject.MQType.CHANNEL, new AdditionalMetric("mqobject_channel_status_code", "The status of the channel"));
            put(MQObject.MQType.LISTENER, new AdditionalMetric("mqobject_listener_status_code", "The status of the listener"));
        }
    };

    private static HashMap<String, Metric> MQ_OBJECT_METRICS_REFERENCE = new HashMap<String, Metric>() {
        {
            put("MQOPEN count", new Metric("mqobject_mqopen_mqopen_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQCLOSE count", new Metric("mqobject_mqclose_mqclose_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQINQ count", new Metric("mqobject_mqinq_mqinq_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQSET count", new Metric("mqobject_mqset_mqset_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQPUT/MQPUT1 count", new Metric("mqobject_put_mqput_mqput1_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQPUT byte count", new Metric("mqobject_put_mqput_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("MQPUT non-persistent message count", new Metric("mqobject_put_mqput_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQPUT persistent message count", new Metric("mqobject_put_mqput_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQPUT1 non-persistent message count", new Metric("mqobject_put_mqput1_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQPUT1 persistent message count", new Metric("mqobject_put_mqput1_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("non-persistent byte count", new Metric("mqobject_put_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("persistent byte count", new Metric("mqobject_put_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("queue avoided puts", new Metric("mqobject_put_queue_avoided_puts_percentage", Metric.Type.SimpleGauge));
            put("queue avoided bytes", new Metric("mqobject_put_queue_avoided_bytes_percentage", Metric.Type.SimpleGauge));
            put("lock contention", new Metric("mqobject_put_lock_contention_percentage", Metric.Type.SimpleGauge));
            put("MQGET count", new Metric("mqobject_get_mqget_count_totalcalls", Metric.Type.SimpleCounter));
            put("MQGET byte count", new Metric("mqobject_get_mqget_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("destructive MQGET non-persistent message count", new Metric("mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("destructive MQGET persistent message count", new Metric("mqobject_get_destructive_mqget_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("destructive MQGET non-persistent byte count", new Metric("mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("destructive MQGET persistent byte count", new Metric("mqobject_get_destructive_mqget_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("MQGET browse non-persistent message count", new Metric("mqobject_get_mqget_browse_non_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQGET browse persistent message count", new Metric("mqobject_get_mqget_browse_persistent_message_count_totalmessages", Metric.Type.SimpleCounter));
            put("MQGET browse non-persistent byte count", new Metric("mqobject_get_mqget_browse_non_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("MQGET browse persistent byte count", new Metric("mqobject_get_mqget_browse_persistent_byte_count_totalbytes", Metric.Type.SimpleCounter));
            put("messages expired", new Metric("mqobject_get_messages_expired_totalmessages", Metric.Type.SimpleCounter));
            put("queue purged count", new Metric("mqobject_get_queue_purged_count_totalqueues", Metric.Type.SimpleCounter));
            put("average queue time", new Metric("mqobject_get_average_queue_time_microseconds", Metric.Type.SimpleGauge));
            put("Queue depth", new Metric("mqobject_get_queue_depth_messages", Metric.Type.SimpleGauge));
        }
    };

    private static HashMap<Integer, Double> channelStatus = new HashMap<Integer, Double>() {
        {
            put(MQConstants.MQCHS_BINDING, (double) 6);
            put(MQConstants.MQCHS_STARTING, (double) 2);
            put(MQConstants.MQCHS_RUNNING, (double) 1);
            put(MQConstants.MQCHS_PAUSED, (double) 3);
            put(MQConstants.MQCHS_STOPPING, (double) 4);
            put(MQConstants.MQCHS_RETRYING, (double) 5);
            put(MQConstants.MQCHS_STOPPED, (double) 0);
            put(MQConstants.MQCHS_REQUESTING, (double) 7);
            put(MQConstants.MQCHS_SWITCHING, (double) 8);
            put(MQConstants.MQCHS_INITIALIZING, (double) 9);
            put(MQConstants.MQCHS_INACTIVE, (double) 10);
        }
    };

    private static HashMap<Integer, Double> listenerStatus = new HashMap<Integer, Double>() {
        {
            put(MQConstants.MQSVC_STATUS_STARTING, (double) 2);
            put(MQConstants.MQSVC_STATUS_RUNNING, (double) 1);
            put(MQConstants.MQSVC_STATUS_STOPPING, (double) 0);
        }
    };

    public static String getMetricName(String description, boolean requiresObject, int datatype) {
        HashMap<String, Metric> ref = requiresObject ? MQ_OBJECT_METRICS_REFERENCE : QUEUE_MANAGER_METRICS_REFERENCE;
        String metricName = ref.get(description).name;
        if (metricName == null) {
            metricName = generateMetricName(description, requiresObject, datatype);
            Metric.Type type = datatype == MQConstants.MQIAMO_MONITOR_DELTA ? Metric.Type.SimpleCounter : Metric.Type.SimpleGauge;
            ref.put(description, new Metric(metricName, type));
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
        }
        //TODO:Add warning!
        System.out.println("Unknown metric name! Generated new name " + metricName + " from description " + description);
        return metricName;
    }

    //TODO: add error processing, think about returnValue possible values.
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
            SimpleCounter, SimpleGauge
        }
    }
}
