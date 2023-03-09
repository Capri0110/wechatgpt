package com.nirvana.wechatgpt.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrentUtils {

    private ConcurrentUtils() {
    }

    public static ThreadPoolExecutor generateExecuteServiceThreadPool(int maxPoolSize) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat("gpt-service-thread-%d").build();
        return new ThreadPoolExecutor(maxPoolSize, maxPoolSize, 3, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), factory);
    }

}
