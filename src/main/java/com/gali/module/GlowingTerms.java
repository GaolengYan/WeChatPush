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
 * 彩虹屁
 *
 * @author 颜伟凡
 * @version 2022-8-10
 */
@Component
public class GlowingTerms {
    private final static Logger logger = LoggerFactory.getLogger(GlowingTerms.class);

    @Autowired
    private CommonConfig config;

    /**
     * 获得一条彩虹屁
     */
    public String getGlowingTerms() throws IOException {
        return doGetGlowingTerms(config.getGlowingTermsUrl());
    }

    private String doGetGlowingTerms(String url) throws IOException {
        String glowingTerm = "";
        String result = HttpUtils.sendGet(url);
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        if (code != 200) {
            return glowingTerm;
        }
        JsonArray newslist = jsonObject.get("newslist").getAsJsonArray();
        if (!newslist.isEmpty()) {
            JsonObject glowingTermJson = newslist.get(0).getAsJsonObject();
            glowingTerm = glowingTermJson.get("content").getAsString();
        }
        return glowingTerm;
    }

    public static void main(String[] args) throws IOException {
        GlowingTerms glowingTerms = new GlowingTerms();
        String glowingTermsString = glowingTerms.doGetGlowingTerms("http://api.tianapi.com/caihongpi/index?key=d27dd6e3c068ae4fbe93b8babc9602f1");
        System.out.println(glowingTermsString);
    }
}
