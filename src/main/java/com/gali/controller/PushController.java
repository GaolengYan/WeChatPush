package com.gali.controller;

import com.gali.CommonConfig;
import com.gali.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@ResponseBody
@Controller
public class PushController {

    @Autowired
    private Pusher pusher;

    @Autowired
    private CommonConfig config;

    /**
     * 微信测试账号推送
     */
    @GetMapping("/push")
    public void push() {
        Set<String> openIds = config.getOpenIds();
        for (String openId : openIds) {
            pusher.push(openId);
        }
    }

    /**
     * 微信测试账号推送
     */
    @GetMapping("/test")
    public void test() {
        System.out.println("测试");
    }
}
