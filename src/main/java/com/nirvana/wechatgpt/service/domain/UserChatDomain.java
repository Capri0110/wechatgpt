package com.nirvana.wechatgpt.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Component
public class UserChatDomain implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * user question reply time : [sec]
     */
    @NotBlank
    private long queryTime;


    /**
     * user question reply content
     */
    @NotBlank
    private String queryReply;

    @NotBlank
    private long replyBitLength;
}