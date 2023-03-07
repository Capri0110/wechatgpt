package com.nirvana.wechatgpt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nirvana.wechatgpt.constant.ChatGptUserRoleEnum;
import com.nirvana.wechatgpt.entity.ChatGptReqBody;
import com.nirvana.wechatgpt.entity.GptMessageBody;
import com.nirvana.wechatgpt.entity.MessageResponseBody;
import com.nirvana.wechatgpt.entity.Davinci3ReqBody;
import com.nirvana.wechatgpt.utils.RedisUtils;
import com.nirvana.wechatgpt.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Value("${openai.model:text-davinci-003")
    private String model;

    @Value("${openai.organization:org-WkgBCSVsTsScrE7eMB8TgCoz}")
    private String organ;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RestTemplateUtils httpClient;

    private final String HUMAN = "Human:";

    private final String AI = "Larissa:";

    private final String multiMessageTokens = ";";

    @Override
    public String reply(String messageContent, String userKey) {
        // default
        String message = String.format("%s: You can ask me whatever you want to know, I will try to response\n" +
                "你可以向我提问任何问题，我会试着解答", AI);
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        if (redisUtils.hasKey(userKey)) {
            message = redisUtils.get(userKey);
        }
        // set redis cache
        message = message + HUMAN + messageContent + "\n";
        redisUtils.setEx(userKey, message, 60, TimeUnit.SECONDS);
        // call openapi
        JSONObject obj = getReplyFromGPT(message);
        MessageResponseBody messageResponseBody = JSONObject.toJavaObject(obj, MessageResponseBody.class);
        // cache reply when necessary
        if (messageResponseBody != null) {
            if (!CollectionUtils.isEmpty(messageResponseBody.getChoices())) {
                String replyText = messageResponseBody.getChoices().get(0).getText();
                new Thread(() -> {
                    String msg = redisUtils.get(userKey);
                    msg = msg + AI + replyText + "\n";
                    redisUtils.setEx(userKey, msg, 60, TimeUnit.SECONDS);
                }).start();
                return replyText.replace(AI, "");
            }
        }
        return "发生了一些问题，你可以稍等下或和我的开发者反馈（如果你认识ta）^_^ ";
    }

    private JSONObject getReplyFromGPT(String message) {
        String url = this.url;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + apiKey);
        header.put("OpenAI-Organization", organ);
        header.put("Content-Type", "application/json");
        Object messageSendBody = buildRequestMsgBody(message);
        String body = JSON.toJSONString(messageSendBody, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        log.info("send request body : [{}]", body);
        ResponseEntity<String> data = httpClient.post(url, header, body, String.class);
        return JSON.parseObject(data.getBody());
    }

    private Object buildRequestMsgBody(String msg) {
        Object preSettings = buildConfig(msg);
        if (preSettings instanceof Davinci3ReqBody) {
            ((Davinci3ReqBody) preSettings).setPrompt(msg);
        } else if (preSettings instanceof ChatGptReqBody) {
            GptMessageBody[] chatMsgs = new GptMessageBody[16];
            int idx = 0;
            for (String subMsg : msg.split(multiMessageTokens)) {
                chatMsgs[idx++] = GptMessageBody.builder()
                        .role(ChatGptUserRoleEnum.user.name())
                        .content(subMsg).build();
            }
            ((ChatGptReqBody) preSettings).setMessages(chatMsgs);
        }
        return preSettings;

    }

    private Object buildConfig(String model, String... args) {
        Object messageBody;
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
//                    .maxTokens(1000)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.6)
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
