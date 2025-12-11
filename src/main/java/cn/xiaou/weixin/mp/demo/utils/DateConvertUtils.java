package cn.xiaou.weixin.mp.demo.utils;


import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * 时间转换工具类
 * @author xiaou
 * @date 2024/2/3
 */
public final class DateConvertUtils {

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter NONE_SPLIT_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter NONE_SPLIT_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");



    /**
     * 将 毫秒级时间戳 转换成 LocalDateTime
     *
     * @param timestamp 毫秒级时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        return Optional.ofNullable(timestamp).map(item -> {
            Instant instant = Instant.ofEpochMilli(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }).orElse(null);
    }

    /**
     * 将 秒级时间戳 转换成 LocalDateTime
     *
     * @param timestamp 秒级时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime secondToLocalDateTime(Long timestamp) {
        return Optional.ofNullable(timestamp).map(item -> {
            Instant instant = Instant.ofEpochSecond(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }).orElse(null);
    }

    /**
     * 将 毫秒级时间戳 转换成 LocalDate
     *
     * @param timestamp 毫秒级时间戳
     * @return LocalDate
     */
    public static LocalDate timestampToLocalDate(Long timestamp) {
        return Optional.ofNullable(timestamp).map(item -> {
            Instant instant = Instant.ofEpochMilli(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }).orElse(null);
    }

    /**
     * 将 秒级时间戳 转换成 LocalDate
     *
     * @param timestamp 秒级时间戳
     * @return LocalDate
     */
    public static LocalDate secondToLocalDate(Long timestamp) {
        return Optional.ofNullable(timestamp).map(item -> {
            Instant instant = Instant.ofEpochSecond(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }).orElse(null);
    }

    /**
     * 将 LocalDateTime 转换成 毫秒级时间戳
     *
     * @param localDateTime LocalDateTime
     * @return 毫秒级时间戳
     */
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(item -> localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .orElse(null);
    }

    /**
     * 将 LocalDateTime 转换成 秒级时间戳
     *
     * @param localDateTime LocalDateTime
     * @return 秒级时间戳
     */
    public static Long localDateTimeToSecond(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(item -> localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .orElse(null);
    }

    /**
     * 将 LocalDate 转换成 毫秒级时间戳
     *
     * @param localDate LocalDate
     * @return 毫秒级时间戳
     */
    public static Long localDateToTimestamp(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(item -> localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .orElse(null);
    }

/**
     * 将 LocalDate 转换成 秒级时间戳
     *
     * @param localDate LocalDate
     * @return 秒级时间戳
     */
    public static Long localDateToSecond(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(item -> localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .orElse(null);
    }

    /**
     * 将 LocalDateTime 转换成 String
     * @param localDateTime LocalDateTime
     * @param pattern 格式
     * @return
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, String pattern) {
        return localDateTimeToString(localDateTime, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将 LocalDateTime 转换成 String
     * @param localDateTime LocalDateTime
     * @param pattern 格式
     * @return
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, DateTimeFormatter pattern) {
        return Optional.ofNullable(localDateTime)
                .map(item -> localDateTime.format(pattern))
                .orElse("");
    }

    /**
     * 将 LocalDateTime 转换成 String
     * @param localDateTime LocalDateTime
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String localDateToString(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(item -> localDateTime.format(FORMAT_TIME))
                .orElse("");
    }

    /**
     * 将 LocalDate 转换成 String
     * @param localDate LocalDate
     * @param pattern 格式
     * @return yyyy-MM-dd
     */
    public static String localDateToString(LocalDate localDate, DateTimeFormatter pattern) {
        return Optional.ofNullable(localDate)
                .map(item -> localDate.format(pattern))
                .orElse("");
    }

    /**
     * 将 LocalDate 转换成 String
     * @param localDate LocalDate
     * @param pattern 格式
     * @return yyyy-MM-dd
     */
    public static String localDateToString(LocalDate localDate, String pattern) {
        return localDateToString(localDate, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将 LocalDate 转换成 String
     * @param localDate LocalDate
     * @return yyyy-MM-dd
     */
    public static String localDateToString(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(item -> localDate.format(FORMAT_DATE))
                .orElse("");
    }

    /**
     * 将 Date 转换成  LocalDateTime
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return Optional.ofNullable(date)
                .map(item -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .orElse(null);
    }

    /**
     * 将时间转换成当天的最大时间
     * @param time 时间
     * @return 当天的最大时间
     */
    public static LocalDateTime dayMax(LocalDateTime time) {
        return Optional.ofNullable(time)
                .map(item -> time.with(LocalTime.MAX).withNano(0)).orElse(null);
    }

    /**
     * 将时间转换成当天的最小时间
     * @param time 时间
     * @return 当天的最小时间
     */
    public static LocalDateTime dayMin(LocalDateTime time) {
        return Optional.ofNullable(time)
                .map(item -> time.with(LocalTime.MIN)).orElse(null);
    }

    public static LocalDateTime strToLocalDateTime(String str) {
        if (!StringUtils.hasLength(str)) {
            return null;
        }
        return LocalDateTime.parse(str, FORMAT_TIME);
    }
}
