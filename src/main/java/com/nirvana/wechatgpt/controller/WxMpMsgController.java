package com.nirvana.wechatgpt.controller;

import com.nirvana.wechatgpt.entity.GptMessageBody;
import com.nirvana.wechatgpt.entity.GptReqBody;
import com.nirvana.wechatgpt.service.ChatGptServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * wechat event & msg reply
 *
 * @author
 */
@Slf4j
@RestController
@RequestMapping("/wx/mp")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WxMpMsgController {

    private final WxMpService wxMpService;

    private final ChatGptServiceImpl chatGptService;

    /**
     * Message validator
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws Exception
     */
    @GetMapping("")
    public Object get(@RequestParam("signature") String signature,
                      @RequestParam("timestamp") String timestamp,
                      @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        // validate signature
        log.info("signature is {}, ts {}, nonce {}", signature, timestamp, nonce);
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            return null;
        }
        return ResponseEntity.ok(echostr);
    }

    /**
     * passively reply
     *
     * @param reqBody
     * @return
     * @throws Exception
     */
    @PostMapping("")
    @ResponseBody
    public Object passiveMsg(@RequestBody String reqBody) throws IOException, WxErrorException {

        log.info("msg is {}", reqBody);
        //获取消息流,并解析xml
        WxMpXmlMessage message = WxMpXmlMessage.fromXml(reqBody);
        log.info("msg is {}", message.toString());
        //消息类型
        String messageType = message.getMsgType();
        log.info("消息类型:" + messageType);
        //发送者帐号
        String fromUser = message.getFromUser();
        log.info("发送者账号：" + fromUser);
        //开发者微信号
        String touser = message.getToUser();
        log.info("开发者微信：" + touser);
        //文本消息  文本内容
        String text = message.getContent();
        log.info("文本消息：" + text);
        // 事件推送
        if (messageType.equals("event")) {
            log.info("event：" + message.getEvent());
            // 关注
            if (message.getEvent().equals("subscribe")) {
                log.info("用户关注：{}", fromUser);
                WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                        .TEXT()
                        .toUser(fromUser)
                        .fromUser(touser)
                        .content("谢谢你长的这么好看还关注我哦～ 我是Larissa（a.k.a. 🇧🇷超模）, 我会试着回答你呢-")
                        .build();

                String result = texts.toXml();

                log.info("响应给用户的消息：" + result);

                return result;
            }
            // 取消关注
            if (message.getEvent().equals("unsubscribe")) {
                log.info("用户取消关注：{}", fromUser);
            }
            // 点击菜单
            if (message.getEvent().equals("CLICK")) {
                log.info("用户点击菜单：{}", message.getEventKey());
            }
            // 点击菜单
            if (message.getEvent().equals("VIEW")) {
                log.info("用户点击菜单：{}", message.getEventKey());
            }
            // 已关注用户扫描带参数二维码
            if (message.getEvent().equals("scancode_waitmsg")) {
                log.info("用户扫描二维码：{}", fromUser);
            }
            // 获取位置信息
            if (message.getEvent().equals("LOCATION")) {
                log.info("用户发送位置信息：经度：{}，纬度：{}", message.getLatitude(), message.getLongitude());
            }
            return null;
        }
        //文本消息
        if (messageType.equals("text")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content( chatGptService.reply(text, fromUser))
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        //图片消息
        if (messageType.equals("image")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的图片信息")
                    .build();
            String result = texts.toXml();
            result.replace("你发送的消息为： ", "");
            log.info("响应给用户的消息：" + result);
            return result;
        }
        /**
         * 语音消息
         */
        if (messageType.equals("voice")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的语音信息")
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        /**
         * 视频
         */
        if (messageType.equals("video")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的视频信息")
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        /**
         * 小视频
         */
        if (messageType.equals("shortvideo")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的小视频信息")
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        /**
         * 地理位置信息
         */
        if (messageType.equals("location")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的地理位置信息")
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        /**
         * 链接信息
         */
        if (messageType.equals("link")) {
            WxMpXmlOutTextMessage texts = WxMpXmlOutTextMessage
                    .TEXT()
                    .toUser(fromUser)
                    .fromUser(touser)
                    .content("已接收到您发的链接信息")
                    .build();
            String result = texts.toXml();
            log.info("响应给用户的消息：" + result);
            return result;
        }
        return null;
    }

//    public void kefuMessage(String toUser, String content) throws WxErrorException {
//        WxMpKefuMessage message = new WxMpKefuMessage();
//        message.setToUser(toUser);
//        message.setMsgType("text");
//        message.setContent(content);
//
//        wxMpService.getKefuService().sendKefuMessage(message);
//    }

//    @PostMapping("/chat")
//    public Object chatWithGpt(@RequestBody GptReqBody content) {
//        return chatGptService.reply(content.getContent(), content.getOpenId());
//    }

}
