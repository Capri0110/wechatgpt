package com.nirvana.wechatgpt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ChatGptReqBody {

    private String model;

    private GptMessageBody[] messages;

    private double temperature;
    // how many chat completion choices to generate for each input message.
    private int n;
    // if set, partial message deltas will be sent, like in ChatGPT.
    // Tokens will be sent as data-only server-sent events as they become available,
    // with the stream terminated by a data: [DONE] message.
    private boolean stream;

    @JSONField(name = "max_tokens")
    private int maxTokens;

    @JSONField(name = "top_p")
    private int topP;

    @JSONField(name = "frequency_penalty")
    private double frequencyPenalty;

    @JSONField(name = "presence_penalty")
    private double presencePenalty;

    List<String> stop;
    // Modify the likelihood of specified tokens appearing in the completion.
    // Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an
    // associated bias value from -100 to 100. Mathematically, the bias is added to the logits generated by
    // the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should
    // decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive
    // selection of the relevant token.
    Map<String, Integer> logit_bias;

    String user;
}
