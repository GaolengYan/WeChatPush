package com.gali.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 定制提醒
 *
 * @author 颜伟凡
 * @version 2022-8-17
 */
public class ModifyManager {
    private final static Logger logger = LoggerFactory.getLogger(ModifyManager.class);

    private Map<Integer, Set<String>> modifyMap = new HashMap<>();

    public Set<String> checkModify() {
        Set<String> contents = new HashSet<>();
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        Set<String> strings = modifyMap.get(nowTime);
        if (strings == null) {
            return contents;
        }
        contents.addAll(strings);
        return contents;
    }

    public Map<Integer, Set<String>> getModifyMap() {
        return modifyMap;
    }

    public void setModifyMap(Map<Integer, Set<String>> modifyMap) {
        this.modifyMap = modifyMap;
    }
}
