package com.nirvana.wechatgpt.entity;

import com.nirvana.wechatgpt.constant.ChatGptUserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GptMessageBody {

    private ChatGptUserRoleEnum role;

    private String content;
}
