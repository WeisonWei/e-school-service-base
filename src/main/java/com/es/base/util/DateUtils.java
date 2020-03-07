package com.es.base.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


public class DateUtils {

    public static Date today() {
        return new Date();
    }

    public static String todayStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(today());
    }

    public static String thatDayStr(long daysToSubtract) {
        return LocalDate.now().minusDays(daysToSubtract).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String todayFullStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today());
    }

    public static String todayWeekDayStr() {
        return LocalDate.now().getDayOfWeek().toString();
    }

    public static int todayWeekDayInt() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static String dateStrWithPoint(String date) {
        return date.replaceAll("-", ".").substring(5, date.length());
    }

    public static String formattedDate(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : todayStr();
    }

    public static String formattedDate(long times) {
        return formattedDate("yyyy-MM-dd", times);
    }

    public static String formattedDate(String format, long times) {
        return new SimpleDateFormat(format).format(times);
    }

    public static boolean dateIsToday(Long date) {
        String now = todayStr();
        String dateStr = formattedDate(date);
        return now.equals(dateStr);
    }


    public static Long getTodayMill() {
        LocalDateTime today = LocalDateTime.now().withHour(00).withMinute(00).withSecond(00);
        long milli = today.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }

    public static Long getMonthFirstDateMill() {
        LocalDateTime firstDay = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(00).withMinute(00).withSecond(00);
        long milli = firstDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }

    public static Long getMonthLastDateMill() {
        LocalDateTime firstDay = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        long milli = firstDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }


}
