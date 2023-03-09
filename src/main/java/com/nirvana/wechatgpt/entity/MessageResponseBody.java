package com.nirvana.wechatgpt.entity;
import lombok.Data;

import java.util.List;

/**
 * @author honghu
 */
@Data
public class MessageResponseBody {

    private String id;

    private String object;

    private int created;

    private String model;

    private List<Choice> choices;

}
