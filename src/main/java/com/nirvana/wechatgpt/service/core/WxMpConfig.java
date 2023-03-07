package com.nirvana.wechatgpt.service.core;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class WxMpConfig {

    @Value("${wx.mp.app-id}")
    private String appId;

    @Value("${wx.mp.secret}")
    private String secretKey;

    @Value("${wx.mp.token}")
    private String token;

    @Value("${wx.mp.aes-key}")
    private String aesKey;

    @Bean
    public WxMpDefaultConfigImpl wxMpDefaultConfigImpl() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(secretKey); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
        return config;
    }
    @Bean
    @Primary
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfigImpl());
        return wxMpService;
    }
}
