package com.example.vliascrm.util;

import com.example.vliascrm.common.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 将字符串转换为LocalDateTime
     * @param dateTimeString 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
    }

    /**
     * 将字符串转换为LocalDate
     * @param dateString 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

    /**
     * 将LocalDateTime转换为字符串
     * @param dateTime LocalDateTime对象
     * @return 日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
    }

    /**
     * 将LocalDate转换为字符串
     * @param date LocalDate对象
     * @return 日期字符串
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

    /**
     * 获取当前日期时间
     * @return 当前日期时间字符串
     */
    public static String now() {
        return formatDateTime(LocalDateTime.now());
    }

    /**
     * 获取当前日期
     * @return 当前日期字符串
     */
    public static String today() {
        return formatDate(LocalDate.now());
    }
} 