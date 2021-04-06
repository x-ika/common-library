package com.simplejcode.commons.misc.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class InMemoryCache implements ICache {

    private static final class CacheEntry {

        String key;
        Object value;
        CacheEntry prev;
        CacheEntry next;
        int index;

        CacheEntry(String key, Object value, CacheEntry prev, CacheEntry next, int index) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.prev = prev;
            this.index = index;
        }

    }


    private final long millis;

    private final CacheEntry[] cache;

    private final Map<String, CacheEntry> map;

    private long lastUpdateTime;

    private int currentIndex;


    public InMemoryCache(TimeUnit precisionUnit, int precisionDuration, int maximumStoreDuration) {
        millis = precisionUnit.toMillis(precisionDuration);
        cache = new CacheEntry[maximumStoreDuration + 1];
        map = new HashMap<>();
        currentIndex = 0;
        lastUpdateTime = System.currentTimeMillis() / millis * millis;
    }

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    @Override
    public int getMaximumDuration() {
        return cache.length - 1;
    }

    @Override
    public synchronized <T> T get(String key) {
        update();
        CacheEntry entry = map.get(key);
        return entry == null ? null : (T) entry.value;
    }

    @Override
    public synchronized void put(String key, Object value, int duration) {
        update();
        removeEntry(key);
        putEntry(key, value, duration);
    }

    @Override
    public synchronized <T> T remove(String key) {
        update();
        CacheEntry entry = removeEntry(key);
        return entry == null ? null : (T) entry.value;
    }

    @Override
    public synchronized void clear() {
        clear0();
    }

    @Override
    public <P, T> T cacheFunction(String name, P param, Function<P, T> function, int duration) {
        update();
        String key = ICache.generateCacheKey(name, param);
        T value = get(key);
        if (value == null) {
            synchronized (this) {
                value = get(key);
                if (value == null) {
                    put(key, value = function.apply(param), duration);
                }
            }
        }
        return value;
    }

    @Override
    public synchronized String toString() {
        String PREFIX = "\n    ";
        StringBuilder sb = new StringBuilder();
        sb.append("InMemoryCache {");
        sb.append(PREFIX).append("Cache Size = ").append(map.size());
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] == null) {
                continue;
            }
            sb.append(PREFIX).append(i).append(": ");
            for (CacheEntry entry = cache[i]; entry != null; entry = entry.next) {
                sb.append(" [").append(entry.key).append('=').append(entry.value).append(']');
            }
        }
        sb.append("\n}");
        return sb.toString();
    }

    //-----------------------------------------------------------------------------------
    /*
    Helpers
     */

    private void putEntry(String key, Object value, int duration) {
        int index = getIndex(duration);
        CacheEntry first = cache[index];
        CacheEntry entry = new CacheEntry(key, value, null, first, index);
        if (first != null) {
            first.prev = entry;
        }
        map.put(key, cache[index] = entry);
    }

    private CacheEntry removeEntry(String key) {
        CacheEntry entry = map.remove(key);
        if (entry == null) {
            return null;
        }
        // eliminate entry
        CacheEntry prev = entry.prev;
        CacheEntry next = entry.next;
        if (prev == null) {
            cache[entry.index] = next;
        } else {
            prev.next = next;
            if (next != null) {
                next.prev = prev;
            }
        }
        return entry;
    }

    private synchronized void update() {
        int passed = (int) ((System.currentTimeMillis() - lastUpdateTime) / millis);
        // optimize full cleanup
        if (passed >= cache.length) {
            clear0();
        } else {
            clearAll(passed);
        }
        lastUpdateTime += passed * millis;
        currentIndex = getIndex(passed);
    }

    private void clear0() {
        Arrays.fill(cache, null);
        map.clear();
    }

    private void clearAll(int count) {
        for (int i = 1; i <= count; i++) {
            clear(getIndex(i));
        }
    }

    private void clear(int index) {
        for (CacheEntry entry = cache[index]; entry != null; entry = entry.next) {
            map.remove(entry.key);
        }
        cache[index] = null;
    }

    private int getIndex(int duration) {
        return (currentIndex + duration) % cache.length;
    }

}
