package com.gali;

import com.gali.module.*;
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
import java.util.Set;

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

    @Autowired
    private Constellation constellation;

    @Autowired
    private Lyrics lyrics;

    @Autowired
    private WxMpService wxMpService;

    public void push() {
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
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
        // region彩虹屁 不要了
//        try{
//            String glowingTerms = this.glowingTerms.getGlowingTerms();
//            templateMessage.addData(new WxMpTemplateData("glowingTerms", glowingTerms, "#FFBBA5"));
//        } catch (Exception e) {
//            logger.error("彩虹屁出错啦！快回来修！");
//        }
        // endregion

        // region 歌词
        String songsLrc = lyrics.getSongsLrc();
        templateMessage.addData(new WxMpTemplateData("glowingTerms", songsLrc, "#FFBBA5"));
        // endregion

        // region 幸运色
//        String constellationColor = this.constellation.getConstellationColor();
//        if (!constellationColor.isEmpty()) {
//            templateMessage.addData(new WxMpTemplateData("luckyColor", constellationColor));
//        }
        // endregion

        try {
            logger.info(templateMessage.toJson());
            Set<String> openIds = config.getOpenIds();
            for (String openId : openIds) {
                templateMessage.setToUser(openId);
                this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 晚上下班推送
     */
    public void pushAtNight() {
        String templateId = "zLxckb7gKw8oWWr0fs1c6tkur3kC41wDYGfZU7Q47yM";
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .templateId(templateId)
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息
        templateMessage.addData(new WxMpTemplateData("hesuan", "核酸", "#990000"));

        // region彩虹屁
//        try{
//            String glowingTerms = this.glowingTerms.getGlowingTerms();
//            templateMessage.addData(new WxMpTemplateData("glowingTerms", glowingTerms, "#FFBBA5"));
//        } catch (Exception e) {
//            logger.error("彩虹屁出错啦！快回来修！");
//        }
        // endregion

        // region 歌词
        String songsLrc = lyrics.getSongsLrc();
        templateMessage.addData(new WxMpTemplateData("glowingTerms", songsLrc, "#FFBBA5"));
        // endregion

        try {
            logger.info(templateMessage.toJson());
            for (String openId : config.getOpenIds()) {
                templateMessage.setToUser(openId);
                this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 周四下午推送
     */
    public void pushThursdayAfterNoon() {
        String templateId = "fq2HJAipJqaVu7IjzICuMM4xaLCg1Np-U7D6JAyHp50";
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .templateId(templateId)
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息
        templateMessage.addData(new WxMpTemplateData("crazy", "疯狂", "#990000"));
        templateMessage.addData(new WxMpTemplateData("thursday", "星期四", "#CC9900"));

        // region彩虹屁
//        try{
//            String glowingTerms = this.glowingTerms.getGlowingTerms();
//            templateMessage.addData(new WxMpTemplateData("glowingTerms", glowingTerms, "#FFBBA5"));
//        } catch (Exception e) {
//            logger.error("彩虹屁出错啦！快回来修！");
//        }
        // endregion

        // region 歌词
        String songsLrc = lyrics.getSongsLrc();
        templateMessage.addData(new WxMpTemplateData("glowingTerms", songsLrc, "#FFBBA5"));
        // endregion

        // 幸运色


        try {
            logger.info(templateMessage.toJson());
            for (String openId : config.getOpenIds()) {
                templateMessage.setToUser(openId);
                this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 定制提醒
     */
    public void pushModify(Set<String> strings) {

        //推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .templateId("03W3Vb9TTvE1uUcHj68hG0qDFLv7RqPHmMDUFUkpqK4")
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息
        // 提醒内容

        StringBuilder stringBuilder = new StringBuilder();
        int size = strings.size();
        int i = 1;
        for (String string : strings) {
            stringBuilder.append(string);
            if (i < size) {
                stringBuilder.append("\n\n");
            }
            i++;
        }
        templateMessage.addData(new WxMpTemplateData("data", stringBuilder.toString(), "#FE6C3C"));

        try {
            logger.info(templateMessage.toJson());
            for (String openId : config.getOpenIds()) {
                templateMessage.setToUser(openId);
                this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }


    public void push15ofMonth() {

        //推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .templateId("o1wUV96O2TrcvTQ-IAICjE7bYKVZSIIpF1PyUIdQPW4")
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        //填写变量信息
        // 提醒内容
        LocalDate of1 = LocalDate.of(2020, 11, 15);

        int monthDiff = DateUtils.getMonthDiff(of1, LocalDate.now());

        templateMessage.addData(new WxMpTemplateData("data", String.valueOf(monthDiff), "#FE6C3C"));

        try {
            logger.info(templateMessage.toJson());
            for (String openId : config.getOpenIds()) {
                templateMessage.setToUser(openId);
                this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            logger.error("推送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

}
