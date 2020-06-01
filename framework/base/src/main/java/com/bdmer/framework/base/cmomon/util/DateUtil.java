package com.bdmer.framework.base.cmomon.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * 日期格式公用类
 *
 * @author GongDeLang
 * @since 2020/6/1 14:00
 */
public class DateUtil {
    /**
     * 私有构造器
     */
    private DateUtil() {
    }

    /**
     * 时间格式
     */
    private static final String STANDARD_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式
     */
    private static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * LocalDateTime时间格式化
     */
    private static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ofPattern(STANDARD_TIME_FORMAT);
    /**
     * LocalDate时间格式化
     */
    private static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT);

    /**
     * 获取当前系统时间转换为自定义格式，返回字符串类型
     *
     * @param formatStr 时间格式（yyyyMMdd）
     * @return 时间字符串
     */
    public static String getNowByFormat(String formatStr) {
        String dateStr = "";

        try {
            dateStr = DateTimeFormatter.ofPattern(formatStr).format(LocalDateTime.now());
        } catch (Exception e) {
            LogUtils.logError("日期字符串转Date - e:", e);
        }

        return dateStr;
    }

    /**
     * 计算date1与date2之间的天数
     * 该天数是calendar计算的天数，与24小时无关
     *
     * @param date1 之前的日期
     * @param date2 之后的日期
     * @return 天数
     */
    public static Long calculateDays(Date date1, Date date2) {
        long days = 0L;
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            return days;
        }

        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());

        // 开始计算天数
        try {
            days = DAYS.between(localDateTime1, localDateTime2);
        } catch (Exception e) {
            LogUtils.logError("计算date1与date2之间的天数 e:", e);
        }

        return days;
    }

    /**
     * 获取每天的开始时间 00:00:00:00
     *
     * @param date 时间
     * @return 开始时间
     */
    public static Date getStartTime(Date date) {
        if (Objects.isNull(date)) {
            return new Date();
        }

        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    /**
     * 获取每天的结束时间 23:59:59:999
     *
     * @param date 时间
     * @return 结束时间
     */
    public static Date getEndTime(Date date) {
        if (Objects.isNull(date)) {
            return new Date();
        }

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date);
        dateEnd.set(Calendar.HOUR_OF_DAY, 23);
        dateEnd.set(Calendar.MINUTE, 59);
        dateEnd.set(Calendar.SECOND, 59);
        return dateEnd.getTime();
    }

    /**
     * 获取日期 2020-05-09 - 字符串
     *
     * @param date 时间
     * @return 日期 - 字符串
     */
    public static String getDateStr(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(LOCAL_DATE_FORMAT);
    }

    /**
     * 获取日期时间 2020-05-09 15:00:22 - 字符串
     *
     * @param date 时间
     * @return 日期 - 字符串
     */
    public static String getDateTimeStr(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(LOCAL_TIME_FORMAT);
    }

    /**
     * 日期字符串转Date
     *
     * @return 时间字符串
     * @author GongDeLang
     * @date 20120-05-09
     */
    public static Date strToDate(String dateStr) {
        Date date = new Date();

        try {
            date = Date.from(LocalDate.parse(dateStr, LOCAL_DATE_FORMAT).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            LogUtils.logError("日期字符串转Date - e:", e);
        }

        return date;
    }

    /**
     * 通过天数获之前的时间字符串
     *
     * @param days 天数
     * @return 时间字符串
     * @author GongDeLang
     * @date 20120-05-09
     */
    public static String getBeforeDateStrByDays(Integer days) {
        if (Objects.isNull(days)) {
            return LocalDate.now().format(LOCAL_DATE_FORMAT);
        }

        return LocalDate.now().minusDays(days).format(LOCAL_DATE_FORMAT);
    }

    /**
     * Date比较相等
     * 描述：主要通过getTime()来比较是否相等
     *
     * @param date0 日期1
     * @param date1 日期2
     * @return 是都相等
     */
    public static Boolean equalDate(Date date0, Date date1) {
        if (Objects.nonNull(date0) && Objects.nonNull(date1)) {
            return date0.getTime() == date1.getTime();
        } else if (Objects.isNull(date0) && Objects.isNull(date1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取两个时间之间的秒数
     *
     * @param dateStart 开始日期
     * @param endDate   结束日期
     * @return 秒数
     */
    public static BigDecimal getSecond(Date dateStart, Date endDate) {
        if (Objects.isNull(dateStart) || Objects.isNull(endDate)) {
            return BigDecimal.ZERO;
        }

        long startTime = dateStart.getTime();
        long endDateTime = endDate.getTime();
        BigDecimal result = BigDecimal.valueOf(Math.abs(endDateTime - startTime));

        return result.divide(BigDecimal.valueOf(1000), RoundingMode.HALF_UP);
    }

    /**
     * 获取两个时间之间的小时数
     *
     * @param dateStart 开始日期
     * @param endDate   结束日期
     * @return 小时数
     */
    public static BigDecimal getHour(Date dateStart, Date endDate) {
        BigDecimal second = DateUtil.getSecond(dateStart, endDate);
        if (Objects.isNull(second)) {
            return BigDecimal.ZERO;
        }

        return second.divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);
    }
}
