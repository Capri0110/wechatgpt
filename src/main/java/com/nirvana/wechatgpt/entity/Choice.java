package com.nirvana.wechatgpt.entity;

import lombok.Data;

@Data
public class Choice {

    private String index;

    private GptMessageBody message;

    private String  finish_reason;

}
