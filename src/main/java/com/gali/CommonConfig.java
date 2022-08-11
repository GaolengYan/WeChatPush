package com.gali;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 一些自定义配置
 *
 * @author 颜伟凡
 * @version 2022-8-9
 */
public class CommonConfig {

    private String appId;

    private String secret;

    private String templateId;

    private Set<String> openIds = new HashSet<>();

    private Map<String, LocalDate> memorialDayMap = new HashMap<>();

    private String weatherKey;

    private String weatherCityName;

    private String weatherCityLocation;

    private String glowingTermsUrl;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Set<String> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(Set<String> openIds) {
        this.openIds = openIds;
    }

    public Map<String, LocalDate> getMemorialDayMap() {
        return memorialDayMap;
    }

    public void setMemorialDayMap(Map<String, LocalDate> memorialDayMap) {
        this.memorialDayMap = memorialDayMap;
    }

    public String getWeatherKey() {
        return weatherKey;
    }

    public void setWeatherKey(String weatherKey) {
        this.weatherKey = weatherKey;
    }

    public String getWeatherCityName() {
        return weatherCityName;
    }

    public void setWeatherCityName(String weatherCityName) {
        this.weatherCityName = weatherCityName;
    }

    public String getWeatherCityLocation() {
        return weatherCityLocation;
    }

    public void setWeatherCityLocation(String weatherCityLocation) {
        this.weatherCityLocation = weatherCityLocation;
    }

    public String getGlowingTermsUrl() {
        return glowingTermsUrl;
    }

    public void setGlowingTermsUrl(String glowingTermsUrl) {
        this.glowingTermsUrl = glowingTermsUrl;
    }
}
