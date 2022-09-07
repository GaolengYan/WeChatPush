package com.gali;

import com.gali.module.ModifyManager;
import com.gali.util.DateUtils;
import com.gali.util.FileUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configurable
@ComponentScan(basePackages = {"com.gali"})
@EnableAsync
public class ApplicationConfig {

    /**
     * 加载配置
     */
    @Bean(name = "commonConfig")
    public CommonConfig getCommonConfig() {
        CommonConfig commonConfig = new CommonConfig();

        // region 微信推送配置pusher.conf
        File file = new File("src/main/resources/pusher.conf");
        if (!file.exists()){
            System.out.println("文件读取失败");
            System.exit(0);
        }
        Config config = ConfigFactory.parseFile(file);

        String templateId = config.getString("templateId");
        commonConfig.setTemplateId(templateId);

        String openIds = config.getString("wx.openID");
        String[] split = openIds.split(",");
        Set<String> openIdSet = commonConfig.getOpenIds();
        openIdSet.addAll(Arrays.asList(split));
        // endregion

        //region天气API
        commonConfig.setWeatherKey(config.getString("weather.key"));
        commonConfig.setWeatherCityName(config.getString("weather.cityName"));
        commonConfig.setWeatherCityLocation(config.getString("weather.cityLocation"));
        //endregion

        // region 纪念日配置
        file = new File("src/main/resources/memorial.json");
        if (!file.exists()){
            System.out.println("纪念日文件读取失败");
            System.exit(0);
        }

        Map<String, LocalDate> memorialDayMap = commonConfig.getMemorialDayMap();

        String json = FileUtils.readFile(file);
        if (json == null) {
            json = "[]";
        }
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();
            String name = object.get("纪念日").getAsString();
            String date = object.get("日期").getAsString();
            split = date.split("/");
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int day = Integer.parseInt(split[2]);
            LocalDate localDate = LocalDate.of(year, month, day);
            memorialDayMap.put(name, localDate);
        }

        // endregion

        //region 彩虹屁配置
        commonConfig.setGlowingTermsUrl(config.getString("glowingTerms.url"));
        //endregion

        // region星座配置

        commonConfig.setConstellationUrl(config.getString("constellation.url"));

        // endregion

        // region 歌词
        file = new File("src/main/resources/lrc.json");
        if (!file.exists()){
            System.out.println("歌词文件读取失败");
            System.exit(0);
        }

        Set<String> lrcSet = commonConfig.getLrcSet();
        lrcSet.clear();

        json = FileUtils.readFile(file);
        if (json == null) {
            json = "[]";
        }
        jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            String lrc = jsonElement.getAsString();
            lrcSet.add(lrc);
        }
        // endregion

        return commonConfig;
    }

    @Bean(name = "wxMpService")
    public WxMpService getWxMapService() {
        File file = new File("src/main/resources/pusher.conf");
        if (!file.exists()){
            System.out.println("文件读取失败");
            System.exit(0);
        }
        Config config = ConfigFactory.parseFile(file);

        String appID = config.getString("appID");

        String appSecret = config.getString("appSecret");

        WxMpDefaultConfigImpl wxStorage = new WxMpDefaultConfigImpl();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);

        return wxMpService;
    }

    @Bean(name = "modifyManager")
    public ModifyManager getModifyManager() {
        File file = new File("src/main/resources/custom_modify.json");
        if (!file.exists()){
            System.out.println("定制提醒文件读取失败");
            System.exit(0);
        }

        ModifyManager modifyManager = new ModifyManager();
        Map<Integer, Set<String>> modifyMap = modifyManager.getModifyMap();

        String json = FileUtils.readFile(file);
        if (json == null) {
            json = "[]";
        }
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String dateTime = jsonObject.get("dateTime").getAsString();
            int time;
            try {
                time = DateUtils.getDate(dateTime);
            } catch (ParseException e) {
                System.out.println("定制提醒文件读取失败, 时间格式有误");
                System.exit(0);
                return modifyManager;
            }
            String content = jsonObject.get("content").getAsString();
            Set<String> strings = modifyMap.computeIfAbsent(time, k -> new HashSet<>());
            strings.add(content);
        }
        return modifyManager;

    }

}
