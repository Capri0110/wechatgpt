package com.nirvana.wechatgpt.service.task;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import static com.nirvana.wechatgpt.service.task.ExecTimeLogSuite.wrap;

@Slf4j
public class TaskManager {

    private TaskManager() {
    }

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static Map<String,String> ctx = MDC.getCopyOfContextMap();

    /**
     * do task async
     *
     * @param task  validation task
     * @param title  job title
     */
    public static void doTask(final ITask task, String title) {
        executorService.execute(() -> wrap((Supplier<Void>) () -> {
            try {
                task.doTask();
            } catch (Exception e) {
                log.error("TaskManager doTask execute error.", e);
            } finally {
                MDC.clear();
            }
            return null;
        }, title, ctx));
    }

    /**
     * task with return
     *
     * @param task  validation task
     * @param title  job title
     */
    public static <T> FutureTask<T> doFutureTask(final IFutureTask<T> task, String title) {
        FutureTask<T> future = new FutureTask<>(() -> wrap((task::doTask), title, ctx));
        executorService.execute(future);
        MDC.clear();
        return future;
    }
}
