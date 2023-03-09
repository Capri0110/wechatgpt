package com.nirvana.wechatgpt.utils;

import com.nirvana.wechatgpt.constant.LogConst;
import org.slf4j.MDC;

import java.util.UUID;

public class MDCUtils {
    private MDCUtils() {}

    public static void setTraceIdIfAbsent() {
        if (MDC.get(LogConst.TRACE_ID) == null) {
            MDC.put(LogConst.TRACE_ID, String.valueOf(UUID.randomUUID()));
        }
    }
}

