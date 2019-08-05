package com.simplejcode.commons.misc.util;

import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class DateUtils {

    private DateUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

    //-----------------------------------------------------------------------------------
    /*
    Java 1.5
     */

    public static SimpleDateFormat createFormatter(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static SimpleDateFormat createFormatter(String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat createFormatter(String pattern, TimeZone timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(timeZone);
        return df;
    }

    public static SimpleDateFormat createFormatter(String pattern, Locale locale, TimeZone timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(timeZone);
        return df;
    }

    public static SimpleDateFormat createFormatter(String pattern, String zoneId) {
        return createFormatter(pattern, TimeZone.getTimeZone(zoneId));
    }


    public static String formatDate(long time) {
        return formatTime(time, DEFAULT_DATE_FORMAT);
    }

    public static String formatTime(long time) {
        return formatTime(time, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String formatTime(long time, String pattern) {
        return createFormatter(pattern).format(new Date(time));
    }

    public static String formatTime(long time, String pattern, String zoneId) {
        return createFormatter(pattern, zoneId).format(new Date(time));
    }


    public static String currentTime() {
        return formatTime(System.currentTimeMillis());
    }

    public static String currentTime(String pattern) {
        return formatTime(System.currentTimeMillis(), pattern);
    }

    public static String currentTime(String pattern, String zone) {
        return formatTime(System.currentTimeMillis(), pattern, zone);
    }


    public static long parseDate(String date) {
        return parseDate(date, DEFAULT_DATE_TIME_FORMAT);
    }

    public static long parseDate(String date, String pattern) {
        try {
            return createFormatter(pattern).parse(date).getTime();
        } catch (ParseException e) {
            throw convert(e);
        }
    }

    public static long parseDate(String date, String pattern, String zoneId) {
        try {
            return createFormatter(pattern, zoneId).parse(date).getTime();
        } catch (ParseException e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Java 1.8
     */

    public static final ZoneOffset OFFSET_UTC = ZoneOffset.UTC;


    public static LocalDateTime fromTimestamp(long millis) {
        return fromTimestamp(millis, OFFSET_UTC);
    }

    public static LocalDateTime fromTimestamp(long millis, ZoneOffset originOffset) {
        return LocalDateTime.ofEpochSecond(millis / 1000, (int) (millis % 1000 * 1000_000), originOffset);
//        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public static long toTimestamp(LocalDateTime time) {
        return toTimestamp(time, OFFSET_UTC);
    }

    public static long toTimestamp(LocalDateTime time, ZoneOffset offset) {
        return time.toInstant(offset).toEpochMilli();
    }

    public static LocalDateTime convertOffset(LocalDateTime dateTime, ZoneOffset fromOffset, ZoneOffset toOffset) {
        return dateTime.atOffset(fromOffset).withOffsetSameInstant(toOffset).toLocalDateTime();
    }


    public static LocalDateTime parseDateTimeDefault(String dateTime) {
        return LocalDateTime.parse(dateTime);
    }

    public static LocalDateTime parseDateTime(String dateTime, String pattern) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDateTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
