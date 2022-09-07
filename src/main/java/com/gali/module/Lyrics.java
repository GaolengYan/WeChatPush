package com.gali.module;

import com.gali.CommonConfig;
import com.gali.util.HttpUtils;
import com.gali.util.RandomUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chanjar.weixin.common.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 歌词
 *
 * @author 颜伟凡
 * @version 2022-8-11
 */
@Component
public class Lyrics {
    private final static Logger logger = LoggerFactory.getLogger(Lyrics.class);

    @Autowired
    private CommonConfig config;

    /**
     * 获得一条歌词
     */
    public String getSongsLrc() {
        Set<String> lrcSet = config.getLrcSet();
        if (lrcSet.isEmpty()) {
            return "";
        }
        return RandomUtil.selectOne(lrcSet);
    }

//    /**
//     * 获得一句歌词
//     */
//    public String getLyrics() {
//
//    }

    public static void main(String[] args) throws IOException {
        Lyrics lyrics = new Lyrics();

    }
}
