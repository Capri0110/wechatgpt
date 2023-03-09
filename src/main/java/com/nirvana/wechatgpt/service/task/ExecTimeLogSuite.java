package com.nirvana.wechatgpt.service.task;

import com.nirvana.wechatgpt.utils.MDCUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.function.Supplier;

/**
 * print execution timestamp
 * @author nirvana.xu
 * @Date 2021-05-05
 */
@Slf4j
public class ExecTimeLogSuite {

    private ExecTimeLogSuite() {
    }

    /**
     * @param supplier  execution method
     * @param title  to be executed
     * @param <T>    return type
     * @return
     */
    public static <T> T wrap(Supplier<T> supplier, String title, Map<String,String> ctx) {
        if (ctx == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(ctx);
        }
        MDCUtils.setTraceIdIfAbsent();
        log.info("SPEED_TIME_LOG:[{}], start to run", title);
        StopWatch watch = new StopWatch();
        watch.start();
        T result = supplier.get();
        watch.stop();
        log.info("SPEED_TIME_LOG:[{}] run completed in {} ms", title, watch.getTotalTimeMillis());
        return result;
    }
}

