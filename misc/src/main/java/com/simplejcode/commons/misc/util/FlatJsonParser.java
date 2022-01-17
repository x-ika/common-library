package com.simplejcode.commons.misc.util;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

import java.math.BigDecimal;
import java.util.*;

public final class FlatJsonParser {

    private FlatJsonParser() {
    }

    //-----------------------------------------------------------------------------------

    public static Object parse(String json) {
        if (json.charAt(0) == '[') {
            return JsonIterator.deserialize(json, List.class);
        }
        if (json.charAt(0) == '{') {
            return JsonIterator.deserialize(json, Map.class);
        }
        throw generate("JSON type not supported");
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return JsonIterator.deserialize(json, clazz);
    }

    public static String serialize(Object object) {
        return JsonStream.serialize(object);
    }

    //-----------------------------------------------------------------------------------

    public static Object extractObject(Object content, Object... keys) {
        for (Object key : keys) {
            if (key instanceof String) {
                if (!(content instanceof Map)) {
                    return null;
                }
                content = ((Map<?, ?>) content).get(key);
            }
            if (key instanceof Integer) {
                if (!(content instanceof List)) {
                    return null;
                }
                content = ((List<?>) content).get((Integer) key);
            }
        }
        return content;
    }

    public static String extractString(Object content, Object... keys) {
        Object field = extractObject(content, keys);
        return field == null ? null : field.toString();
    }

    public static List<?> extractList(Object content, Object... keys) {
        return (List<?>) extractObject(content, keys);
    }

    public static List<?> extractListOrEmpty(Object content, Object... keys) {
        List<?> list = extractList(content, keys);
        return list != null ? list : Collections.emptyList();
    }

    public static int extractInt(Object content, Object... keys) {
        return Integer.parseInt(extractStringWithCheck(content, keys));
    }

    public static long extractLong(Object content, Object... keys) {
        return Long.parseLong(extractStringWithCheck(content, keys));
    }

    public static boolean extractBoolean(Object content, Object... keys) {
        return Boolean.parseBoolean(extractString(content, keys));
    }

    public static BigDecimal extractDecimal(Object content, Object... keys) {
        String val = extractString(content, keys);
        return val == null ? null : new BigDecimal(val);
    }

    private static String extractStringWithCheck(Object content, Object[] keys) {
        String s = extractString(content, keys);
        if (s == null) {
            throw generate("Not null expected at path: " + Arrays.toString(keys));
        }
        return s;
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException generate(String message) {
        return ExceptionUtils.generate(message);
    }

}
