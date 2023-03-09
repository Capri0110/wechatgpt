package com.nirvana.wechatgpt.service.task;

public interface IFutureTask<T> {

    /**
     * execute callable
     * @return return code
     */
    T doTask();
}

