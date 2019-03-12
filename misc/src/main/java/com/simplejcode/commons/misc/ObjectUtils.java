package com.simplejcode.commons.misc;

import java.util.function.Supplier;

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

}
