package com.gali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定时器
 *
 * @author 颜伟凡
 * @version 2022-8-9
 */
@Component
@EnableScheduling
public class Schedule {
    private final static Logger logger = LoggerFactory.getLogger(Schedule.class);

    @Autowired
    private Pusher pusher;

    @Autowired
    private CommonConfig config;

    /**
     * 定时推送 上午信息
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void scheduledPushMorning() {
        Set<String> openIds = config.getOpenIds();
        for (String openId : openIds) {
            pusher.push(openId);
        }
    }

    /**
     * 定时推送 中午信息
     */
    @Scheduled(cron = "0 20 12 * * ?")
    public void scheduledPushNooning() {

    }

    /**
     * 定时推送 下午信息
     */
    @Scheduled(cron = "0 20 19 * * ?")
    public void scheduledPushAfternoon() {

    }

    /**
     * 定时推送 晚上信息
     */
    @Scheduled(cron = "0 30 20 * * ?")
    public void scheduledPushNight() {

    }

    /**
     * 定时推送 睡觉信息
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledPushSleep() {

    }
}
