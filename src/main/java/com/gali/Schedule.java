package com.gali;

import com.gali.module.ModifyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static java.time.DayOfWeek.*;

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

    @Autowired
    private ModifyManager modifyManager;

    /**
     * 定时推送 上午信息
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void scheduledPushMorning() {
        pusher.push();
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
    @Scheduled(cron = "0 0 19 * * ?")
    public void scheduledPushAfternoon() {
        if (LocalDate.now().getDayOfWeek() == THURSDAY) {
            pusher.pushThursdayAfterNoon();
        }
    }

    /**
     * 定时推送 晚上信息
     */
    @Scheduled(cron = "0 30 20 * * ?")
    public void scheduledPushNight() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        if (dayOfWeek != SATURDAY && dayOfWeek != SUNDAY) {
            pusher.pushAtNight();
        }
    }

    /**
     * 定时推送 睡觉信息
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledPushSleep() {

    }

    /**
     * 定制提醒
     * 每分钟检测一次，有提醒则发送
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduledModify() {
        Set<String> strings = modifyManager.checkModify();
        if (strings.isEmpty()) {
            return;
        }
        pusher.pushModify(strings);
    }

    /**
     * 定制提醒
     * 15号0点检测
     */
    @Scheduled(cron = "1 0 0 15 * ?")
    public void scheduled15() {
        pusher.push15ofMonth();
    }


}
