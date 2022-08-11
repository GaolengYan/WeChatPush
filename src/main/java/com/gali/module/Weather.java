package com.gali.module;

import com.gali.CommonConfig;
import com.gali.util.HttpUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 天气
 *
 * @author 颜伟凡
 * @version 2022-8-9
 */
@Component
public class Weather {
    private final static Logger logger = LoggerFactory.getLogger(Weather.class);

    @Autowired
    private CommonConfig config;

    public String getWeather() throws IOException {
        StringBuilder weatherStr = new StringBuilder();
        String url = "https://devapi.qweather.com/v7/weather/now?&gzip=n&key=" + config.getWeatherKey() + "&location=" + config.getWeatherCityLocation();
        String json = HttpUtils.sendGet(url);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        if (code != 200) {
            return weatherStr.toString();
        }
        JsonObject now = jsonObject.get("now").getAsJsonObject();
        String weather = now.get("text").getAsString();
        weatherStr.append("天气:").append(weather).append("\n");
        String feelsLike = now.get("feelsLike").getAsString();
        weatherStr.append("体感温度:").append(feelsLike).append("℃");
        return weatherStr.toString();
    }

    public static void main(String[] args) throws IOException {
        String url = "https://devapi.qweather.com/v7/weather/now?&gzip=n&";
        url = url + "key=c443345f2e094987b96388b316e85047" + "&" + "location=113.92,22.52";
        String json = HttpUtils.sendGet(url);
        System.out.println(json);
    }
}
