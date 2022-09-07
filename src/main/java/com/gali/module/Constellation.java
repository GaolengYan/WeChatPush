package com.gali.module;

import com.gali.CommonConfig;
import com.gali.util.HttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 星座
 *
 * @author 颜伟凡
 * @version 2022-8-11
 */
@Component
public class Constellation {
    private final static Logger logger = LoggerFactory.getLogger(Constellation.class);

    @Autowired
    private CommonConfig config;

    public String getConstellationColor() throws IOException {
        String constellation = "";
        String result = HttpUtils.sendGet(config.getConstellationUrl());
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        if (code != 200) {
            return constellation;
        }
        JsonArray newslist = jsonObject.get("newslist").getAsJsonArray();
        for (JsonElement jsonElement : newslist) {
            String type = jsonElement.getAsJsonObject().get("type").getAsString();
            if ("幸运颜色".equals(type)) {
                return jsonElement.getAsJsonObject().get("content").getAsString();
            }
        }
        return constellation;
    }



}
