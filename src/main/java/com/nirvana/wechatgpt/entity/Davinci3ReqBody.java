package com.nirvana.wechatgpt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Davinci3ReqBody {

    private String model;

    private String prompt;

    private double temperature;

    @JSONField(name = "max_tokens")
    private int maxTokens;

    @JSONField(name = "top_p")
    private int topP;

    @JSONField(name = "frequency_penalty")
    private double frequencyPenalty;

    @JSONField(name = "presence_penalty")
    private double presencePenalty;

    List<String> stop;

}
