package com.simplejcode.commons.misc.util;

import java.util.function.*;

public final class ObjectUtils {

    private ObjectUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static <T> T nvl(T t, T def) {
        return t != null ? t : def;
    }

    public static <T> T nvl(T t, Supplier<T> def) {
        return t != null ? t : def.get();
    }

    public static <O, T> T nvl(O o, Function<O, T> get) {
        return o != null ? get.apply(o) : null;
    }

    public static <O, T> T nvl(O o, Function<O, T> get, Supplier<T> def) {
        return o != null ? get.apply(o) : def.get();
    }

}
