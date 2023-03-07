package com.nirvana.wechatgpt.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GptMessageBody {

    private String role;

    private String content;
}
