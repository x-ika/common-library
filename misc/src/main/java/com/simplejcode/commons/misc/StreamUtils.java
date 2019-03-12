package com.simplejcode.commons.misc;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class StreamUtils {

    private StreamUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <K, V> List<V> map(K[] array, Function<K, V> mapper) {
        return Arrays.stream(array).map(mapper).collect(Collectors.toList());
    }

    public static <K, V> List<V> map(List<K> list, Function<K, V> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <K, V> List<V> mapDistinct(List<K> list, Function<K, V> mapper) {
        return list.stream().map(mapper).distinct().collect(Collectors.toList());
    }

    //-----------------------------------------------------------------------------------

    public static <T, K> Map<K, T> list2map(List<T> list, Function<T, K> toKey) {
        return list2map(list, toKey, Function.identity());
    }

    public static <T, K, V> Map<K, V> list2map(List<T> list, Function<T, K> toKey, Function<T, V> toValue) {
        return list.stream().collect(Collectors.toMap(toKey, toValue));
    }

    public static <T, K> Map<K, List<T>> list2list(List<T> list, Function<T, K> toKey) {
        return list2list(list, toKey, Function.identity());
    }

    public static <T, K, V> Map<K, List<V>> list2list(List<T> list, Function<T, K> toKey, Function<T, V> toValue) {
        Map<K, List<V>> map = new HashMap<>();
        for (T t : list) {
            K key = toKey.apply(t);
            List<V> sublist = map.computeIfAbsent(key, k -> new ArrayList<>());
            sublist.add(toValue.apply(t));
        }
        return map;
    }

}
