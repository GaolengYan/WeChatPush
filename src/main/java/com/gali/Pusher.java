package com.gali;

import com.gali.module.GlowingTerms;
import com.gali.module.MemorialDay;
import com.gali.module.Weather;
import com.gali.util.DateUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Component
public class Pusher {
    private final static Logger logger = LoggerFactory.getLogger(Pusher.class);

    @Autowired
    private CommonConfig config;

    @Autowired
    private MemorialDay memorialDay;

    @Autowired
    private Weather weather;

    @Autowired
    private GlowingTerms glowingTerms;

    public void push(String openId) {
        //1,配置
        WxMpDefaultConfigImpl wxStorage = new WxMpDefaultConfigImpl();
        wxStorage.setAppId(this.config.getAppId());
        wxStorage.setSecret(this.config.getSecret());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(this.config.getTemplateId())
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息
        LocalDate now = LocalDate.now();
        // region 日期
        templateMessage.addData(new WxMpTemplateData("dateTime", DateUtils.getDate(now), "#B5C7D2"));
        // endregion

        // region 纪念日
        Map<String, LocalDate> memorialDay = this.config.getMemorialDayMap();
        StringBuilder memorialDayStringBuilder = new StringBuilder();
        for (Map.Entry<String, LocalDate> entry : memorialDay.entrySet()) {
            String name = entry.getKey();
            LocalDate date = entry.getValue();
            long betweenDays = DateUtils.betweenDays(date, now);
            memorialDayStringBuilder.append("今天是").append(name).append("的第").append(betweenDays).append("天").append("\n");
        }
        templateMessage.addData(new WxMpTemplateData("memorialDay", memorialDayStringBuilder.toString(), "#FE6C3C"));
        // endregion
        // region天气
        try {
            String weather = this.weather.getWeather();
            templateMessage.addData(new WxMpTemplateData("weather", weather, "#C6E564"));
        } catch (IOException e) {
            logger.error("天气出错啦！快回来修！");
            e.printStackTrace();
        }
        // endregion
        // region彩虹屁
        try{
            String glowingTerms = this.glowingTerms.getGlowingTerms();
            templateMessage.addData(new WxMpTemplateData("glowingTerms", glowingTerms, "#FFBBA5"));
        } catch (Exception e) {
            logger.error("彩虹屁出错啦！快回来修！");
        }
        // endregion

        try {
            System.out.println(templateMessage.toJson());
            System.out.println(wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

}
