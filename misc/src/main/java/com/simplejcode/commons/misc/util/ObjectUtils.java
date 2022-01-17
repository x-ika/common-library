package com.simplejcode.commons.misc.util;

import java.util.function.*;

public final class ObjectUtils {

    private ObjectUtils() {
    }

    //-----------------------------------------------------------------------------------

    @SafeVarargs
    public static <T> T nvl(T... ts) {
        for (T t : ts) {
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    public static <T> T nvl(T t, T def) {
        return t != null ? t : def;
    }

    public static <T> T nvl(T t, Supplier<T> def) {
        return t != null ? t : def.get();
    }


    public static <O, T> T nvl(O o, Function<O, T> get) {
        return nvl(o, get, (T) null);
    }

    public static <O, T> T nvl(O o, Function<O, T> get, T def) {
        return o != null ? get.apply(o) : def;
    }

    public static <O, T> T nvl(O o, Function<O, T> get, Supplier<T> def) {
        return o != null ? get.apply(o) : def.get();
    }

    //-----------------------------------------------------------------------------------

    public static <T extends Comparable<T>> T min(T t1, T t2) {
        return t1.compareTo(t2) < 0 ? t1 : t2;
    }

    public static <T extends Comparable<T>> T max(T t1, T t2) {
        return t1.compareTo(t2) > 0 ? t1 : t2;
    }

}
