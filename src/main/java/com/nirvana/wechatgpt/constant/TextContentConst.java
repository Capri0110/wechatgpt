package com.nirvana.wechatgpt.constant;

public class TextContentConst {
    private TextContentConst() {}

    public static String EXCESS_5_SEC_LIMIT = "你问的问题有点长，让我想想，你可以等下再问哦";

    public static final String HUMAN = "Human:";

    public static final String AI = "Larissa:";

    public static final String multiMessageTokens = ";";

    public static final String DEFAULT_MSG_GREED = String.format("%s: You can ask me whatever you want to know, I will try to response\n" +
            "你可以向我提问任何问题，我会试着解答", AI);

    public static final String DEFAULT_MSG_WAIT = String.format("%s: It seems a good question! Though I have to think harder, ask me after 1 min \n" +
            "这看上去是个很有趣的问题，稍等一分钟再问我下，我会试着组织好语言回答你哦 ", AI);

    public static final String UNEXPECTED_ERR = "发生了一些问题，你可以稍等下或和我的开发者反馈（如果你认识ta）^_^ ";
}
