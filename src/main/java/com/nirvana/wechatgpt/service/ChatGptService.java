package com.nirvana.wechatgpt.service;

/**
 * @author
 */
public interface ChatGptService {
    /**
     * @param messageContent
     * @param userKey
     * @return
     */
    String reply(String messageContent, String userKey);
}
