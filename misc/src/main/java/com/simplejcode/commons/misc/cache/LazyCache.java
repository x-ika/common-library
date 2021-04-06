package com.simplejcode.commons.misc.cache;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class LazyCache implements ICache {

    private static final class CacheEntry {

        Object value;
        long expirationTime;
        boolean isEvaluating;

    }


    private final TimeUnit timeUnit;

    private final ExecutorService executorService;

    private final Map<String, CacheEntry> map;


    public LazyCache(TimeUnit timeUnit, ExecutorService executorService) {
        this.timeUnit = timeUnit;
        this.executorService = executorService;
        map = new HashMap<>();
    }

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    @Override
    public int getMaximumDuration() {
        return Integer.MAX_VALUE;
    }

    @Override
    public synchronized <T> T get(String key) {
        CacheEntry entry = map.get(key);
        if (entry == null || isExpired(entry)) {
            map.remove(key);
            return null;
        }
        return (T) entry.value;
    }

    @Override
    public synchronized void put(String key, Object value, int duration) {
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(duration);
        CacheEntry entry = map.computeIfAbsent(key, k -> new CacheEntry());
        entry.value = value;
        entry.expirationTime = expirationTime;
    }

    @Override
    public synchronized Object remove(String key) {
        CacheEntry entry = map.remove(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized <P, T> T cacheFunction(String name, P param, Function<P, T> function, int duration) {
        String key = ICache.generateCacheKey(name, param);
        CacheEntry entry = map.get(key);
        // first call - need to wait
        if (entry == null) {
            T value = function.apply(param);
            put(key, value, duration);
            return value;
        }
        // lazy evaluation possible
        if (isExpired(entry) && !entry.isEvaluating) {
            entry.isEvaluating = true;
            executorService.execute(() -> {
                put(key, function.apply(param), duration);
                entry.isEvaluating = false;
            });
        }
        return (T) entry.value;
    }

    @Override
    public synchronized String toString() {
        long currentTime = System.currentTimeMillis();
        String PREFIX = "\n    ";
        StringBuilder sb = new StringBuilder();
        sb.append("ContinuousCache {");
        sb.append(PREFIX).append("Cache Size = ").append(map.size());
        for (Map.Entry<String, CacheEntry> entry : map.entrySet()) {
            sb.append(PREFIX).
                    append(entry.getKey()).
                    append(": ").
                    append(entry.getValue()).
                    append("| ").
                    append(isExpired(entry.getValue(), currentTime) ? "expired" : "active");
        }
        sb.append("\n}");
        return sb.toString();
    }

    //-----------------------------------------------------------------------------------
    /*
    Helpers
     */

    private boolean isExpired(CacheEntry entry) {
        return isExpired(entry, System.currentTimeMillis());
    }

    private boolean isExpired(CacheEntry entry, long currentTime) {
        return entry.expirationTime < currentTime;
    }

}
