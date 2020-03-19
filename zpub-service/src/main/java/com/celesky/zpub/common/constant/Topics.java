package com.celesky.zpub.common.constant;

/**
 * @desc: kafka主题
 * @author: pan
 * @date: 2018/8/8
 */
public class Topics {

    /**
     * 这个过期了 送进这个topic的数据会被转换后再送入source_export_topic队列
     */
    @Deprecated
    public final static String SOURCE_EVENT_TOPIC = "source_event_topic";


    public final static String SENTRY_MESSAGE_TOPIC = "sentry_message_topic";


    public final static String SOURCE_EXPORT_TOPIC="source_export_topic";

    public final static String GAUGE_EXPORT_TOPIC="gauge_export_topic";


    public final static String AGGRE_STATS_TOPIC ="aggre_stats_topic";

    public final static String ALARM_LOG_TOPIC ="alarm_log_topic";



//    public static String CS_PUSH_TARGET = "cs_push_target";
//
//    public static String CS_SEND_SOURCE = "cs_send_source";

}
