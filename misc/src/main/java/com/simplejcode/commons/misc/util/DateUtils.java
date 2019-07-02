package com.simplejcode.commons.misc.util;

import java.text.*;
import java.time.*;
import java.util.*;

public final class DateUtils {

    private DateUtils() {
    }

    //-----------------------------------------------------------------------------------
    /*
    Java 1.5
     */

    public static final String DEFAULT_FORMAT = "dd.MM.yyyy HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";


    public static SimpleDateFormat create(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static SimpleDateFormat create(String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat create(String pattern, TimeZone timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(timeZone);
        return df;
    }

    public static SimpleDateFormat create(String pattern, Locale locale, TimeZone timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(timeZone);
        return df;
    }

    public static SimpleDateFormat create(String pattern, String zoneId) {
        return create(pattern, TimeZone.getTimeZone(zoneId));
    }


    public static String formatTime(long time, String pattern, String zoneId) {
        return create(pattern, zoneId).format(new Date(time));
    }

    public static String formatTime(long time, String pattern) {
        return create(pattern).format(new Date(time));
    }

    public static String formatTime(long time) {
        return formatTime(time, DEFAULT_FORMAT);
    }

    public static String formatDate(long time) {
        return formatTime(time, DEFAULT_DATE_FORMAT);
    }

    public static String currentTime(String pattern, String zone) {
        return formatTime(System.currentTimeMillis(), pattern, zone);
    }

    public static String currentTime(String pattern) {
        return formatTime(System.currentTimeMillis(), pattern);
    }

    public static String currentTime() {
        return formatTime(System.currentTimeMillis());
    }


    public static long parseDate(String date, String pattern, String zoneId) {
        try {
            return create(pattern, zoneId).parse(date).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long parseDate(String date, String pattern) {
        try {
            return create(pattern).parse(date).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long parseDate(String date) {
        return parseDate(date, DEFAULT_FORMAT);
    }

    //-----------------------------------------------------------------------------------
    /*
    Java 1.8
     */

    public static final ZoneOffset OFFSET = OffsetDateTime.now().getOffset();

    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

    public static LocalDateTime fromTimestamp(long millis) {
        return LocalDateTime.ofEpochSecond(millis / 1000, (int) (millis % 1000 * 1000_000), ZoneOffset.UTC);
//        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public static long toTimestamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDateTime convertFrom(LocalDateTime dateTime, ZoneOffset offset) {
        return dateTime.atOffset(offset).withOffsetSameInstant(OFFSET).toLocalDateTime();
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
