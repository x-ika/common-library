package com.simplejcode.commons.misc;

import javax.xml.datatype.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;

public final class DateUtils {

    public static final String DEFAULT_FORMAT = "dd.MM.yyyy HH:mm:ss";


    private DateUtils() {
    }


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


    public static XMLGregorianCalendar convert(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new SimpleDateFormat(format).parse(date));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static XMLGregorianCalendar long2XmlGregorian(long date) {
        DatatypeFactory dataTypeFactory;
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date);
        return dataTypeFactory.newXMLGregorianCalendar(gc);
    }

    public static XMLGregorianCalendar timestamp2XmlGregorian(Timestamp ts) {
        if (ts == null) {
            return null;
        }

        long date = ts.getTime();
        DatatypeFactory dataTypeFactory;
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date);
        return dataTypeFactory.newXMLGregorianCalendar(gc);
    }

    public static Date xmlGregorian2Date(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    public static Timestamp date2Timestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

}
