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
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), originOffset);
    }

    public static long toTimestamp(LocalDateTime dateTime) {
        return toTimestamp(dateTime, OFFSET_UTC);
    }

    public static long toTimestamp(LocalDateTime dateTime, ZoneOffset offset) {
        return dateTime.toInstant(offset).toEpochMilli();
    }

    public static long toTimestamp(ZonedDateTime dateTime) {
        return dateTime.toInstant().toEpochMilli();
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

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return formatDateTime(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    public static String currentTime() {
        return formatDateTime(fromTimestamp(System.currentTimeMillis()), DEFAULT_DATE_TIME_FORMAT);
    }

    //-----------------------------------------------------------------------------------

    public static long parseDateTimeAsLong(String dateTime, String pattern) {
        return parseDateTimeAsLong(dateTime, pattern, OFFSET_UTC);
    }

    public static long parseDateTimeAsLong(String dateTime, DateTimeFormatter formatter) {
        return parseDateTimeAsLong(dateTime, formatter, OFFSET_UTC);
    }

    public static long parseDateTimeAsLong(String dateTime, String pattern, ZoneOffset offset) {
        return parseDateTimeAsLong(dateTime, DateTimeFormatter.ofPattern(pattern), offset);
    }

    public static long parseDateTimeAsLong(String dateTime, DateTimeFormatter formatter, ZoneOffset offset) {
        return toTimestamp(parseDateTime(dateTime, formatter), offset);
    }

    //-----------------------------------------------------------------------------------

    public static YearMonth parseYearMonth(String yearMonth, String pattern) {
        return parseYearMonth(yearMonth, DateTimeFormatter.ofPattern(pattern));
    }

    public static YearMonth parseYearMonth(String yearMonth, DateTimeFormatter formatter) {
        return YearMonth.parse(yearMonth, formatter);
    }

    public static String formatYearMonth(YearMonth yearMonth, String pattern) {
        return formatYearMonth(yearMonth, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatYearMonth(YearMonth yearMonth, DateTimeFormatter formatter) {
        return yearMonth.format(formatter);
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
