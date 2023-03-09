package com.nirvana.wechatgpt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nirvana.wechatgpt.constant.ChatGptUserRoleEnum;
import com.nirvana.wechatgpt.entity.ChatGptReqBody;
import com.nirvana.wechatgpt.entity.GptMessageBody;
import com.nirvana.wechatgpt.entity.MessageResponseBody;
import com.nirvana.wechatgpt.entity.Davinci3ReqBody;
import com.nirvana.wechatgpt.service.domain.UserChatDomain;
import com.nirvana.wechatgpt.service.task.TaskManager;
import com.nirvana.wechatgpt.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static com.nirvana.wechatgpt.constant.LogConst.*;
import static com.nirvana.wechatgpt.constant.TextContentConst.*;

/**
 * @author
 */
@Slf4j
@Service
public class ChatGptServiceImpl implements ChatGptService {

    @Value("${openai.api.key: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}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/completions}")
    private String url;

    @Value("${openai.model:text-davinci-003}")
    private String model;

    @Value("${openai.organization:org-WkgBCSVsTsScrE7eMB8TgCoz}")
    private String organ;

    @Resource
    private RestTemplateUtils httpClient;

    @Resource
    private RedissonClient redissonClient;


    @Override
    public String reply(String messageContent, String userKey) {
        RLock lock = null;
        // 限制wechat多次请求并发
        try {
            lock = redissonClient.getLock(USER_CHAT_LOCK + userKey);
        } catch (Exception e) {
            log.error("runTopicThreads getLock error", e);
        }
        try {
            if (null == lock) {
                return DEFAULT_MSG_GREED;
            }
            boolean lockFlag = lock.tryLock(3, 120, TimeUnit.SECONDS);
            if (!lockFlag) {
                return DEFAULT_MSG_WAIT;
            }
            FutureTask<String> futures = TaskManager.doFutureTask( () -> {
                RMap<String, UserChatDomain> userQueries = redissonClient.getMap(userKey);
                userQueries.expire(Duration.of(20L, ChronoUnit.MINUTES));
                if (userQueries.containsKey(messageContent)) {
                    UserChatDomain chats = userQueries.get(messageContent);
                    log.info("query {} reply with time {} sec", messageContent, chats.getQueryTime());
                    return chats.getQueryReply();
                } else {
                    long startTime = System.currentTimeMillis();
                    // call openapi
                    JSONObject obj = getReplyFromGPT(messageContent, userKey);
                    log.info("reply content is : {}", obj.toJSONString());
                    MessageResponseBody messageResponseBody = JSONObject.toJavaObject(obj,
                            MessageResponseBody.class);
                    // todo: can query with n > 1 save with other data struct
                    userQueries.put(messageContent, UserChatDomain.builder()
                            .queryReply(messageResponseBody.getChoices().get(0).getMessage().getContent())
                            .queryTime(messageResponseBody.getCreated() - startTime).build());
                    return messageResponseBody.getChoices().get(0).getMessage().getContent();
                }
            }, "-->chat service run -->");
//            return (futures.get(4800, TimeUnit.MILLISECONDS) != null) ? futures.get() : EXCESS_5_SEC_LIMIT;
            return (futures.get() != null) ? futures.get() : EXCESS_5_SEC_LIMIT;
        } catch (Throwable e) {
            log.error("query with error occurs ", e);
            return UNEXPECTED_ERR;
        } finally {
            if (lock != null
                    && lock.isLocked()
                    && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("runTopicThreads unlock error", e);
                }
            }
        }

    }

    private JSONObject getReplyFromGPT(String message, String uid) {
        String url = this.url;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + apiKey);
        header.put("OpenAI-Organization", organ);
        header.put("Content-Type", "application/json");
        Object messageSendBody = buildRequestMsgBody(message, uid);
        String body = JSON.toJSONString(messageSendBody, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        log.info("send request body : [{}]", body);
        ResponseEntity<String> data = httpClient.post(url, header, body, String.class);
        return JSON.parseObject(data.getBody());
    }

    private Object buildRequestMsgBody(String msg, String uid) {
        Object preSettings = buildConfig(this.model, uid);
        if (preSettings instanceof Davinci3ReqBody) {
            ((Davinci3ReqBody) preSettings).setPrompt(msg);
        } else if (preSettings instanceof ChatGptReqBody) {
            List<GptMessageBody> chatMsgs = new ArrayList<>();
            Arrays.stream(msg.split(multiMessageTokens))
                    .forEach(subMsg -> chatMsgs
                            .add(GptMessageBody.builder()
                            .role(ChatGptUserRoleEnum.user)
                            .content(subMsg).build())
            );
//            for (String subMsg : msg.split(multiMessageTokens)) {
//                chatMsgs.add(GptMessageBody.builder()
//                        .role(ChatGptUserRoleEnum.user)
//                        .content(subMsg).build());
//            }
            ((ChatGptReqBody) preSettings).setMessages(chatMsgs);
        }
        return preSettings;

    }

    private Object buildConfig(String model, String uid, String... args) {
        Object messageBody;
        log.debug("current reply model : {}", model);
        List<String> stop = new ArrayList<>(8);
        stop.add(AI);
        stop.add(HUMAN);
        if (model.startsWith("text-davinci")) {
            messageBody = Davinci3ReqBody.builder()
                    .model(model)
                    .topP(1)
                    .temperature(0.9)
                    .maxTokens(1000)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.6)
                    .stop(stop).build();
        } else if (model.startsWith("gpt-3.5")) {
            messageBody = ChatGptReqBody.builder()
                    .model(model)
                    .temperature(0.9)
                    .topP(1)
                    .maxTokens(1000)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.6)
                    .logit_bias(new HashMap<>(1))
                    .user(uid)
                    // default only generate 1 response
                    .n(1).stop(stop).build();
        } else {
            throw new InvalidParameterException("not currently support model ");
        }
        return messageBody;
    }

    /**
     * 解决大文章问题超5秒问题，但是目前事个人订阅号，没有客服接口权限，暂时没用
     *
     * @param messageContent
     * @param userKey
     * @return
     */
    public String getArticle(String messageContent, String userKey) {
//        String url = "https://api.openai.com/v1/completions";
//        Map<String, String> header = new HashMap();
//        header.put("Authorization", "Bearer " + apiKey);
//        header.put("OpenAI-Organization", organ);
//        header.put("Content-Type", "application/json");
//
//        Davinci3ReqBody messageSendBody = buildConfig();
//        messageSendBody.setMaxTokens(150);
//        messageSendBody.setPrompt(messageContent);
//        String body = JSON.toJSONString(messageSendBody, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//        String data = httpClient.post(url, body, header);
//        log.info("返回的数据：" + data);
//        JSONObject obj = JSON.parseObject(data);
//        MessageResponseBody messageResponseBody = JSONObject.toJavaObject(obj, MessageResponseBody.class);
//        if (messageResponseBody != null) {
//            if (!CollectionUtils.isEmpty(messageResponseBody.getChoices())) {
//                String replyText = messageResponseBody.getChoices().get(0).getText();
//                return replyText.replace("小小鹏:", "");
//            }
//        }
        return "暂时不明白你说什么!";
    }
}
