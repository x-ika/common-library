package com.simplejcode.commons.misc.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class StreamUtils {

    private StreamUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static <T> T find(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <K, V> List<V> map(K[] array, Function<K, V> mapper) {
        return Arrays.stream(array).map(mapper).collect(Collectors.toList());
    }

    public static <K, V> List<V> map(Collection<K> collection, Function<K, V> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    public static <K, V> List<V> mapDistinct(Collection<K> collection, Function<K, V> mapper) {
        return collection.stream().map(mapper).distinct().collect(Collectors.toList());
    }

    //-----------------------------------------------------------------------------------

    public static <K, V> V reduce(Collection<K> collection, V initial, BiFunction<V, K, V> reducer) {
        for (K k : collection) {
            initial = reducer.apply(initial, k);
        }
        return initial;
    }

    public static <K, V> V mapReduce(Collection<K> collection, Function<K, V> mapper, BinaryOperator<V> reducer) {
        return mapReduce(collection, mapper, reducer, null);
    }

    public static <K, V> V mapReduce(Collection<K> collection, Function<K, V> mapper, BinaryOperator<V> reducer, V def) {
        return collection.stream().map(mapper).reduce(reducer).orElse(def);
    }

    //-----------------------------------------------------------------------------------

    public static <T, K> Map<K, T> list2map(Collection<T> collection, Function<T, K> toKey) {
        return list2map(collection, toKey, Function.identity());
    }

    public static <T, K, V> Map<K, V> list2map(Collection<T> collection, Function<T, K> toKey, Function<T, V> toValue) {
        return collection.stream().collect(Collectors.toMap(toKey, toValue, (a, b) -> a));
    }

    public static <T, K> Map<K, List<T>> list2list(Collection<T> collection, Function<T, K> toKey) {
        return list2list(collection, toKey, Function.identity());
    }

    public static <T, K, V> Map<K, List<V>> list2list(Collection<T> collection, Function<T, K> toKey, Function<T, V> toValue) {
        Map<K, List<V>> map = new HashMap<>();
        for (T t : collection) {
            K key = toKey.apply(t);
            List<V> sublist = map.computeIfAbsent(key, k -> new ArrayList<>());
            sublist.add(toValue.apply(t));
        }
        return map;
    }

}
