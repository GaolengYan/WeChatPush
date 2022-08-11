package com.gali.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * 日期工具类
 *
 * @author 颜伟凡
 * @version 2022-8-8
 */
public class DateUtils {

    private static String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static LocalDate getFirstDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getFirstDayOfMonth(Integer year, Integer month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getLastDayOfMonth(Integer year, int month) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate getLastDayOfMonth(Integer year, Integer month) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate getFirstDayOfYear(Integer year) {
        return LocalDate.of(year, Month.JANUARY, 1);
    }

    public static LocalDate getLastDayOfYear(Integer year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    /**
     * 日期相隔天数
     */
    public static long betweenDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return endDateExclusive.toEpochDay() - startDateInclusive.toEpochDay();
    }

    /**
     * 获取中文日期
     */
    public static String getDate() {
        LocalDate now = LocalDate.now();
        return getDate(now);
    }
    public static String getDate(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonth().getValue();
        int day = localDate.getDayOfMonth();
        int week = localDate.getDayOfWeek().getValue();
        return year + "年" + month + "月" + day + "日  " + weekDays[week];
    }

    public static void main(String[] args) {
        logger.info(getDate());
    }

}
