package com.simplejcode.commons.misc.util;

import com.google.gson.*;

import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("unchecked")
public final class FlatJsonParser {

    private FlatJsonParser() {
    }

    //-----------------------------------------------------------------------------------

    private static final Gson GSON = new GsonBuilder().create();


    public static Object parse(String json) {
        if (json.charAt(0) == '[') {
            return parse(json, List.class);
        }
        if (json.charAt(0) == '{') {
            return parse(json, Map.class);
        }
        throw generate("JSON type not supported");
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    //-----------------------------------------------------------------------------------

    public static Object extractObject(Object content, Object... keys) {
        for (Object key : keys) {
            if (key instanceof String) {
                content = ((Map) content).get(key);
            }
            if (key instanceof Integer) {
                content = ((List) content).get((Integer) key);
            }
        }
        return content;
    }

    public static String extractString(Object content, Object... keys) {
        Object field = extractObject(content, keys);
        return field == null ? null : field.toString();
    }

    public static List<?> extractList(Object content, Object... keys) {
        return (List) extractObject(content, keys);
    }

    public static List<?> extractListOrEmpty(Object content, Object... keys) {
        List<?> list = extractList(content, keys);
        return list != null ? list : Collections.emptyList();
    }

    public static int extractInt(Object content, Object... keys) {
        return (int) Double.parseDouble(extractString(content, keys));
    }

    public static long extractLong(Object content, Object... keys) {
        return (long) Double.parseDouble(extractString(content, keys));
    }

    public static boolean extractBoolean(Object content, Object... keys) {
        return Boolean.parseBoolean(extractString(content, keys));
    }

    public static BigDecimal extractDecimal(Object content, Object... keys) {
        String val = extractString(content, keys);
        return val == null ? null : new BigDecimal(val);
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException generate(String message) {
        return ExceptionUtils.generate(message);
    }

}
