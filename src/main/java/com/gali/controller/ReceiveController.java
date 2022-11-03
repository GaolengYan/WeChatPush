package com.gali.controller;

import com.gali.util.HttpUtils;
import com.gali.util.MessageUtils;
import com.gali.util.TextMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * 接收信息
 *
 * @author 颜伟凡
 * @version 2022-9-6
 */
@RequestMapping("/wechat")
@ResponseBody
@Controller
public class ReceiveController {
    private final static Logger logger = LoggerFactory.getLogger(ReceiveController.class);

    @RequestMapping(value = "/handle", method = {RequestMethod.GET, RequestMethod.POST})
    public String messageHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        Map<String, String> resultMap = MessageUtils.parseXml(request);
        String fromUserName = resultMap.get("FromUserName");
        String content = resultMap.get("Content");
        logger.info("{} 发来消息: {}", fromUserName, content);
        response.setCharacterEncoding("utf-8");
        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数

        if (!MessageUtils.checkSignature(signature, timestamp, nonce)) {
            logger.info("TOKEN不一致，拒绝请求");
            return "";
        }

        String result = this.doMessageHandler(content, fromUserName);

        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(resultMap.get("ToUserName"));
        Date date = new Date();
        textMessage.setCreateTime(date.getTime());
        textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent(result);
        String str = MessageUtils.textMessageToXml(textMessage);
        logger.info("返回消息:{}", result);
        return str;
    }

    private String doMessageHandler(String content, String openId) {
        String result = "";
        if (content == null) {
            return result;
        }
        switch (content) {
            case "爱你":
                result = "我也爱你";
                break;
            case "测试":
                result = "测试完成";
                break;
            default:
                try {
                    result = chatApi(content);
                } catch (Exception e) {
                    result = "不知道你在说些什么";
                }
        }
        return result;
    }

    private static String chatApi(String content) throws IOException {
        content = URLEncoder.encode(content, "UTF-8");
        String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + content;
        String result = HttpUtils.sendGet(url);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        return asJsonObject.get("content").getAsString();
    }

}
