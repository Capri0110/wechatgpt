package com.nirvana.wechatgpt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GptReqBody {

    @JsonProperty("user_id")
    private String openId;

    private String content;
}


