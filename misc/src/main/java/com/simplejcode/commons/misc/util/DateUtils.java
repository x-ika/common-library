package com.simplejcode.commons.misc.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

    public static final ZoneOffset OFFSET_UTC = ZoneOffset.UTC;

    //-----------------------------------------------------------------------------------

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

    public static long toTimestamp(ZonedDateTime time) {
        return time.toInstant().toEpochMilli();
    }

    public static LocalDateTime convertOffset(LocalDateTime dateTime, ZoneOffset fromOffset, ZoneOffset toOffset) {
        return dateTime.atOffset(fromOffset).withOffsetSameInstant(toOffset).toLocalDateTime();
    }

    //-----------------------------------------------------------------------------------

    public static LocalDateTime parseDateTime(String dateTime, String pattern) {
        return parseDateTime(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String formatDateTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String currentTime() {
        return formatDateTime(fromTimestamp(System.currentTimeMillis()), DEFAULT_DATE_TIME_FORMAT);
    }

    //-----------------------------------------------------------------------------------

    public static long parseDateTimeAsLong(String dateTime, String pattern) {
        return parseDateTimeAsLong(dateTime, pattern, OFFSET_UTC);
    }

    public static long parseDateTimeAsLong(String dateTime, String pattern, ZoneOffset offset) {
        return toTimestamp(parseDateTime(dateTime, DateTimeFormatter.ofPattern(pattern)), offset);
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
