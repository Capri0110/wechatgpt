package com.honghu.wxmp_chat.entity;

import lombok.Data;

@Data
public class Choices {

    private String text;

    private String index;

    private String logprobs;

    private String  finish_reason;

}
