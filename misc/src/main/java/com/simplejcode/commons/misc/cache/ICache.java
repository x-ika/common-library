package com.simplejcode.commons.misc.cache;

import java.util.function.*;

public interface ICache {

    //-----------------------------------------------------------------------------------
    /*
    API
     */

    /**
     * @return Maximum possible storing duration
     */
    int getMaximumDuration();

    /**
     * @param key cache key
     * @param <T> Type of the value
     * @return Value associated to that key or {@code null}
     */
    <T> T get(String key);

    /**
     * Puts the new value into the cache
     * @param key cache key
     * @param value new cache value
     * @param duration during which the value needs to be stored
     */
    void put(String key, Object value, int duration);

    /**
     * Removes the specified key from the cache
     * @param key cache key
     * @return the old value associated with the given key
     */
    <T> T remove(String key);

    /**
     * Clears the cache
     */
    void clear();

    /**
     * @param name part of the cache key
     * @param param parameter of the compute function
     * @param function value computation function to be used when cache expires
     * @param duration during which the value needs to be stored
     * @param <P> Function parameter type
     * @param <T> Type of the value
     * @return Value associated to that key or computed by the given function
     */
    <P, T> T cacheFunction(String name, P param, Function<P, T> function, int duration);

    //-----------------------------------------------------------------------------------
    /*
    Default Methods
     */

    default void put(String key, Object value) {
        put(key, value, getMaximumDuration());
    }

    default <P, T> T cacheFunction(String name, Supplier<T> supplier) {
        return cacheFunction(name, null, t -> supplier.get(), getMaximumDuration());
    }

    default <P, T> T cacheFunction(String name, P param, Function<P, T> function) {
        return cacheFunction(name, param, function, getMaximumDuration());
    }

    //-----------------------------------------------------------------------------------
    /*
    Static
     */

    static String generateCacheKey(Object... s) {
        StringBuilder sb = new StringBuilder();
        for (Object p : s) {
            sb.append('#').append(p);
        }
        return sb.toString();
    }

}
