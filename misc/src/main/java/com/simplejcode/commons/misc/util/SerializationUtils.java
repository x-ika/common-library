package com.simplejcode.commons.misc.util;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class SerializationUtils {

    private SerializationUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static List<String> stringToStringsList(String string, String delimiter) {
        List<String> result = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return result;
        }
        for (String item : string.split(delimiter)) {
            result.add(decode(item));
        }

        return result;
    }

    public static List<Long> stringToLongsList(String string, String delimiter) {
        List<Long> result = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return result;
        }
        for (String item : string.split(delimiter)) {
            result.add(Long.valueOf(item));
        }
        return result;
    }

    public static String listToString(List<?> list, String delimiter) {
        return StreamUtils.reduce(list, new StringBuilder(), (s, t) -> s.append(delimiter).append(encode(t))).substring(1);
    }

    //-----------------------------------------------------------------------------------

    public static String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map != null) {
            for (String key : map.keySet()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
                String value = map.get(key);
                stringBuilder.append((key != null ? encode(key) : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? encode(value) : "");
            }
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> stringToMap(String input) {
        Map<String, String> map = new HashMap<>();
        if (input != null && !input.isEmpty()) {
            String[] nameValuePairs = input.split("&");
            for (String nameValuePair : nameValuePairs) {
                String[] nameValue = nameValuePair.split("=");
                map.put(decode(nameValue[0]), nameValue.length > 1 ? decode(nameValue[1]) : "");
            }
        }
        return map;
    }

    //-----------------------------------------------------------------------------------

    private static String encode(Object t) {
        return URLEncoder.encode(t.toString(), StandardCharsets.UTF_8);
    }

    private static String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

}
