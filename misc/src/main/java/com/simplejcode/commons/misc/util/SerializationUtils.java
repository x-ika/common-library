package com.simplejcode.commons.misc.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public final class SerializationUtils {

    private SerializationUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final Gson GSON = new GsonBuilder().create();

    //-----------------------------------------------------------------------------------

    public static String serialize(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T deserialize(String json, Type type) {
        return GSON.fromJson(json, type);
    }

}
