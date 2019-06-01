package com.simplejcode.commons.misc;

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
            result.add(URLDecoder.decode(item, StandardCharsets.UTF_8));
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

    public static String listToString(List list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : list) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(URLEncoder.encode(item.toString(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    public static <T> List<List<T>> split(List<T> list, int targetSize) {
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += targetSize) {
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        }
        return lists;
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
                stringBuilder.append((key != null ? URLEncoder.encode(key, StandardCharsets.UTF_8) : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, StandardCharsets.UTF_8) : "");
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
                map.put(URLDecoder.decode(nameValue[0], StandardCharsets.UTF_8), nameValue.length > 1 ? URLDecoder.decode(nameValue[1], StandardCharsets.UTF_8) : "");
            }
        }
        return map;
    }

}
