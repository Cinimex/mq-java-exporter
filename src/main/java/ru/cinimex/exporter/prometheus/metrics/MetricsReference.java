package ru.cinimex.exporter.prometheus.metrics;

import com.ibm.mq.constants.MQConstants;
import ru.cinimex.exporter.mq.MQObject;

import java.util.HashMap;

public class MetricsReference {

    private static HashMap<String, String> QUEUE_MANAGER_METRICS_REFERENCE = new HashMap<String, String>() {
        {
            put("User CPU time percentage", "system_cpu_user_cpu_time_percentage");
            put("System CPU time percentage", "system_cpu_cpu_time_percentage");
            put("CPU load - one minute average", "system_cpu_cpu_load_one_minute_average_hundredths");
            put("CPU load - five minute average", "system_cpu_cpu_load_five_minute_average_hundredths");
            put("CPU load - fifteen minute average", "system_cpu_cpu_load_fifteen_minute_average_hundredths");
            put("RAM free percentage", "system_ram_ram_free_percentage");
            put("RAM total bytes", "system_ram_ram_total_megabytes");
            put("User CPU time - percentage estimate for queue manager", "mq_cpu_user_cpu_time_estimate_percentage");
            put("System CPU time - percentage estimate for queue manager", "mq_cpu_system_cpu_time_estimat_percentage");
            put("RAM total bytes - estimate for queue manager", "mq_cpu_ram_total_estimate_megabytes");
            put("MQ trace file system - bytes in use", "mq_disk_trace_file_system_in_use_megabytes");
            put("MQ trace file system - free space", "mq_disk_trace_file_system_free_space_percentage");
            put("MQ errors file system - bytes in use", "mq_disk_errors_file_system_in_use_megabytes");
            put("MQ errors file system - free space", "mq_disk_errors_file_system_free_space_percentage");
            put("MQ FDC file count", "mq_disk_fdc_file_count_files");
            put("Queue Manager file system - bytes in use", "mq_disk_file_system_in_use_megabytes");
            put("Queue Manager file system - free space", "mq_disk_file_system_free_space_percentage");
            put("Log - bytes in use", "mq_rlog_log_bytes_in_use_bytes");
            put("Log - bytes max", "mq_rlog_log_bytes_max_bytes");
            put("Log file system - bytes in use", "mq_rlog_log_file_system_bytes_in_use_bytes");
            put("Log file system - bytes max", "mq_rlog_log_file_system_bytes_max_bytes");
            put("Log - physical bytes written", "mq_rlog_log_physical_bytes_written_bytes");
            put("Log - logical bytes written", "mq_rlog_log_logical_bytes_written_bytes");
            put("Log - write latency", "mq_rlog_log_write_latency_microseconds");
            put("MQCONN/MQCONNX count", "mq_mqconn_mqconnx_count_totalcalls");
            put("Failed MQCONN/MQCONNX count", "mq_mqconn_failed_mqconn_mqconnx_count_totalcalls");
            put("Concurrent connections - high water mark", "mq_mqconn_concurrent_connections_high_water_mark_totalconnections");
            put("MQDISC count", "mq_mqdisc_mqdisc_count_totalcalls");
            put("MQOPEN count", "mq_mqopen_mqopen_count_totalcalls");
            put("Failed MQOPEN count", "mq_mqopen_failed_mqopen_count_totalcalls");
            put("MQCLOSE count", "mq_mqclose_mqclose_count_totalcalls");
            put("Failed MQCLOSE count", "mq_mqclose_failed_mqclose_count_totalcalls");
            put("MQINQ count", "mq_mqinq_mqinq_count_totalcalls");
            put("Failed MQINQ count", "mq_mqinq_failed_mqinq_count_totalcalls");
            put("MQSET count", "mq_mqset_mqset_count_totalcalls");
            put("Failed MQSET count", "mq_mqset_failed_mqset_count_totalcalls");
            put("Interval total MQPUT/MQPUT1 count", "mq_put_interval_total_mqput_mqput1_count_totalcalls");
            put("Interval total MQPUT/MQPUT1 byte count", "mq_put_interval_total_mqput_mqput1_byte_count_totalbytes");
            put("Non-persistent message MQPUT count", "mq_put_non_persistent_message_mqput_count_totalmessages");
            put("Persistent message MQPUT count", "mq_put_persistent_message_mqput_count_totalmessages");
            put("Failed MQPUT count", "mq_put_failed_mqput_count_totalcalls");
            put("Non-persistent message MQPUT1 count", "mq_put_non_persistent_message_mqput1_count_totalmessages");
            put("Persistent message MQPUT1 count", "mq_put_persistent_message_mqput1_count_totalmessages");
            put("Failed MQPUT1 count", "mq_put_failed_mqput1_count_totalcalls");
            put("Put non-persistent messages - byte count", "mq_put_put_non_persistent_messages_byte_count_totalmessages");
            put("Put persistent messages - byte count", "mq_put_put_persistent_messages_byte_count_totalmessages");
            put("MQSTAT count", "mq_put_mqstat_count_totalcalls");
            put("Interval total destructive get- count", "mq_get_interval_total_destructive_get_count_totalmessages");
            put("Interval total destructive get - byte count", "mq_get_interval_total_destructive_get_byte_count_totalbytes");
            put("Non-persistent message destructive get - count", "mq_get_non_persistent_message_destructive_get_count_totalmessages");
            put("Persistent message destructive get - count", "mq_get_persistent_message_destructive_get_count_totalmessages");
            put("Failed MQGET - count", "mq_get_failed_mqget_count_totalcalls");
            put("Got non-persistent messages - byte count", "mq_get_got_non_persistent_messages_byte_count_totalbytes");
            put("Got persistent messages - byte count", "mq_get_got_persistent_messages_byte_count_totalbytes");
            put("Non-persistent message browse - count", "mq_get_non_persistent_message_browse_count_totalmessages");
            put("Persistent message browse - count", "mq_get_persistent_message_browse_count_totalmessages");
            put("Failed browse count", "mq_get_failed_browse_count_totalbrowses");
            put("Non-persistent message browse - byte count", "mq_get_non_persistent_message_browse_byte_count_totalmessages");
            put("Persistent message browse - byte count", "mq_get_persistent_message_browse_byte_count_totalmessages");
            put("Expired message count", "mq_get_expired_message_count_totalmessages");
            put("Purged queue count", "mq_get_purged_queue_count_totalmessages");
            put("MQCB count", "mq_get_mqcb_count_totalcalls");
            put("Failed MQCB count", "mq_get_failed_mqcb_count_totalcalls");
            put("MQCTL count", "mq_get_mqctl_count_totalcalls");
            put("Commit count", "mq_commit_commit_count_totalcalls");
            put("Rollback count", "mq_rollback_rollback_count_totalcalls");
            put("Create durable subscription count", "mq_subscribe_create_durable_subscription_count_totalcalls");
            put("Alter durable subscription count", "mq_subscribe_alter_durable_subscription_count_totalcalls");
            put("Resume durable subscription count", "mq_subscribe_resume_durable_subscription_count_totalcalls");
            put("Create non-durable subscription count", "mq_subscribe_create_non_durable_subscription_count_totalcalls");
            put("Failed create/alter/resume subscription count", "mq_subscribe_failed_create_alter_resume_subscription_count_totalcalls");
            put("Delete durable subscription count", "mq_subscribe_delete_durable_subscription_count_totalcalls");
            put("Delete non-durable subscription count", "mq_subscribe_delete_non_durable_subscription_count_totalcalls");
            put("Subscription delete failure count", "mq_subscribe_subscription_delete_failure_count_totalcalls");
            put("MQSUBRQ count", "mq_subscribe_mqsubrq_count_totalcalls");
            put("Failed MQSUBRQ count", "mq_subscribe_failed_mqsubrq_count_totalcalls");
            put("Durable subscriber - high water mark", "mq_subscribe_durable_subscriber_high_water_mark_subscriptions");
            put("Durable subscriber - low water mark", "mq_subscribe_durable_subscriber_low_water_mark_subscriptions");
            put("Non-durable subscriber - high water mark", "mq_subscribe_non_durable_subscriber_high_water_mark_subscriptions");
            put("Non-durable subscriber - low water mark", "mq_subscribe_non_durable_subscriber_low_water_mark_subscriptions");
            put("Topic MQPUT/MQPUT1 interval total", "mq_publish_topic_mqput_mqput1_interval_total_totalmessages");
            put("Interval total topic bytes put", "mq_publish_interval_total_topic_bytes_put_totalbytes");
            put("Published to subscribers - message count", "mq_publish_published_to_subscribers_message_count_totalmessages");
            put("Published to subscribers - byte count", "mq_publish_published_to_subscribers_byte_count_totalmessages");
            put("Non-persistent - topic MQPUT/MQPUT1 count", "mq_publish_non_persistent_topic_mqput_mqput1_count_totalmessages");
            put("Persistent - topic MQPUT/MQPUT1 count", "mq_publish_persistent_topic_mqput_mqput1_count_totalmessages");
            put("Failed topic MQPUT/MQPUT1 count", "mq_publish_failed_topic_mqput_mqput1_count_totalattempts");
        }
    };

    private static HashMap<MQObject.MQType, AdditionalMetric> MQ_OBJECT_ADDITIONAL_METRICS_REFERENCE = new HashMap<MQObject.MQType, AdditionalMetric>() {
        {
            put(MQObject.MQType.QUEUE, new AdditionalMetric("mqobject_queue_max_depth_messages", "The maximum number of messages that are allowed on the queue"));
            put(MQObject.MQType.CHANNEL, new AdditionalMetric("mqobject_channel_status_code", "The status of the channel"));
            put(MQObject.MQType.LISTENER, new AdditionalMetric("mqobject_listener_status_code", "The status of the listener"));
        }
    };

    private static HashMap<String, String> MQ_OBJECT_METRICS_REFERENCE = new HashMap<String, String>() {
        {
            put("MQOPEN count", "mqobject_mqopen_mqopen_count_totalcalls");
            put("MQCLOSE count", "mqobject_mqclose_mqclose_count_totalcalls");
            put("MQINQ count", "mqobject_mqinq_mqinq_count_totalcalls");
            put("MQSET count", "mqobject_mqset_mqset_count_totalcalls");
            put("MQPUT/MQPUT1 count", "mqobject_put_mqput_mqput1_count_totalcalls");
            put("MQPUT byte count", "mqobject_put_mqput_byte_count_totalbytes");
            put("MQPUT non-persistent message count", "mqobject_put_mqput_non_persistent_message_count_totalmessages");
            put("MQPUT persistent message count", "mqobject_put_mqput_persistent_message_count_totalmessages");
            put("MQPUT1 non-persistent message count", "mqobject_put_mqput1_non_persistent_message_count_totalmessages");
            put("MQPUT1 persistent message count", "mqobject_put_mqput1_persistent_message_count_totalmessages");
            put("non-persistent byte count", "mqobject_put_non_persistent_byte_count_totalbytes");
            put("persistent byte count", "mqobject_put_persistent_byte_count_totalbytes");
            put("queue avoided puts", "mqobject_put_queue_avoided_puts_percentage");
            put("queue avoided bytes", "mqobject_put_queue_avoided_bytes_percentage");
            put("lock contention", "mqobject_put_lock_contention_percentage");
            put("MQGET count", "mqobject_get_mqget_count_totalcalls");
            put("MQGET byte count", "mqobject_get_mqget_byte_count_totalbytes");
            put("destructive MQGET non-persistent message count", "mqobject_get_destructive_mqget_non_persistent_message_count_totalmessages");
            put("destructive MQGET persistent message count", "mqobject_get_destructive_mqget_persistent_message_count_totalmessages");
            put("destructive MQGET non-persistent byte count", "mqobject_get_destructive_mqget_non_persistent_byte_count_totalbytes");
            put("destructive MQGET persistent byte count", "mqobject_get_destructive_mqget_persistent_byte_count_totalbytes");
            put("MQGET browse non-persistent message count", "mqobject_get_mqget_browse_non_persistent_message_count_totalmessages");
            put("MQGET browse persistent message count", "mqobject_get_mqget_browse_persistent_message_count_totalmessages");
            put("MQGET browse non-persistent byte count", "mqobject_get_mqget_browse_non_persistent_byte_count_totalbytes");
            put("MQGET browse persistent byte count", "mqobject_get_mqget_browse_persistent_byte_count_totalbytes");
            put("messages expired", "mqobject_get_messages_expired_totalmessages");
            put("queue purged count", "mqobject_get_queue_purged_count_totalqueues");
            put("average queue time", "mqobject_get_average_queue_time_microseconds");
            put("Queue depth", "mqobject_get_queue_depth_messages");
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
        HashMap<String, String> ref = requiresObject ? MQ_OBJECT_METRICS_REFERENCE : QUEUE_MANAGER_METRICS_REFERENCE;
        String metricName = ref.get(description);
        if (metricName == null) {
            metricName = generateMetricName(description, requiresObject, datatype);
            ref.put(description, metricName);
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

    public static class AdditionalMetric {
        public final String name;
        public final String help;

        public AdditionalMetric(String name, String help) {
            this.name = name;
            this.help = help;
        }
    }
}
