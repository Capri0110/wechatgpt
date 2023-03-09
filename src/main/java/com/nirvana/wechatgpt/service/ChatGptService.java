package com.nirvana.wechatgpt.service;

/**
 * @author
 */
public interface ChatGptService {
    /**
     * @param messageContent chat content prompted by user
     * @param userKey user openid in wxmp context
     * @return chat reply
     */
    String reply(String messageContent, String userKey);
}
