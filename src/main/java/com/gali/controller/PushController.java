package com.gali.controller;

import com.gali.CommonConfig;
import com.gali.Pusher;
import com.gali.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Set;

@ResponseBody
@Controller
public class PushController {

    @Autowired
    private Pusher pusher;

    @Autowired
    private CommonConfig config;

    @Autowired
    private Schedule schedule;

    /**
     * 微信测试账号推送
     */
    @GetMapping("/push")
    public void push() {
        pusher.push();
    }

    /**
     * 微信测试账号推送
     */
    @GetMapping("/pushThursdayAfterNoon")
    public void pushThursdayAfterNoon() {
        pusher.pushThursdayAfterNoon();
    }

    /**
     * 微信测试账号推送
     */
    @GetMapping("/pushAtNight")
    public void pushAtNight() {
        pusher.pushAtNight();
    }

    /**
     * 微信测试账号推送
     */
    @GetMapping("/test")
    public void test() {
        schedule.scheduledModify();
    }
}
