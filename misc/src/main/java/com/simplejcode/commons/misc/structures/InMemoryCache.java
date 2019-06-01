package com.simplejcode.commons.misc.structures;

import com.simplejcode.commons.misc.ObjectUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class InMemoryCache {

    private final class CacheEntry {

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


    private final TimeUnit timeUnit;

    private final long millis;

    private final CacheEntry[] cache;

    private final Map<String, CacheEntry> map;

    private long lastUpdateTime;

    private int currentIndex;


    public InMemoryCache(TimeUnit timeUnit, int maximumStoreDuration) {
        this.timeUnit = timeUnit;
        millis = timeUnit.toMillis(1);
        cache = new CacheEntry[maximumStoreDuration];
        map = new HashMap<>();
        currentIndex = 0;
        lastUpdateTime = System.currentTimeMillis();
    }

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public synchronized <T> T get(String key) {
        update();
        CacheEntry entry = map.get(key);
        return entry == null ? null : (T) entry.value;
    }

    public synchronized void put(String key, Object value) {
        put(key, value, cache.length - 1);
    }

    public synchronized void put(String key, Object value, int duration) {
        update();
        removeEntry(key);
        putEntry(key, value, duration);
    }

    public synchronized Object remove(String key) {
        update();
        return removeEntry(key);
    }

    public synchronized <P, T> T getCached(String name, P param, Function<P, T> getter) {
        return getCached(name, param, getter, cache.length - 1);
    }

    public synchronized <P, T> T getCached(String name, P param, Function<P, T> getter, int duration) {
        update();
        String key = generateCacheKey(name, param);
        T value = get(key);
        if (value == null) {
            put(key, value = getter.apply(param), duration);
        }
        return value;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(map.size());
        for (int i = 0; i < cache.length; i++) {
            sb.append('\n').append(i).append(": ");
            for (CacheEntry entry = cache[i]; entry != null; entry = entry.next) {
                sb.append(" {").append(entry.key).append(':').append(entry.value).append('}');
            }
        }
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

    private Object removeEntry(String key) {
        CacheEntry entry = map.get(key);
        if (entry == null) {
            return null;
        }
        // eliminate entry
        CacheEntry prev = ObjectUtils.nvl(entry.prev, cache[entry.index]);
        prev.next = entry.next;
        return entry.value;
    }

    private void update() {
        int passed = (int) ((System.currentTimeMillis() - lastUpdateTime) / millis);
        // optimize full cleanup
        if (passed >= cache.length) {
            clear();
        } else {
            clearAll(passed);
        }
        lastUpdateTime += passed * millis;
        currentIndex = getIndex(passed);
    }

    private void clear() {
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

    //-----------------------------------------------------------------------------------
    /*
    Static API
     */

    public static String generateCacheKey(Object... s) {
        StringBuilder sb = new StringBuilder();
        for (Object p : s) {
            sb.append('#').append(p);
        }
        return sb.toString();
    }

}
