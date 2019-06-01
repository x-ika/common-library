package com.simplejcode.commons.misc.util;

import java.util.*;

public class MultiSet<T> {

    private Map<T, Integer> map;

    public MultiSet() {
        map = new HashMap<>();
    }

    public boolean contains(T t) {
        return count(t) > 0;
    }

    public int count(T t) {
        Integer c = map.get(t);
        return c == null ? 0 : c;
    }

    public void add(T t) {
        setCount(t, count(t) + 1);
    }

    public void remove(T t) {
        setCount(t, count(t) - 1);
    }

    public void removeAll(T t) {
        setCount(t, 0);
    }

    public void setCount(T t, int c) {
        if (c <= 0) {
            map.remove(t);
        } else {
            map.put(t, c);
        }
    }

    public Map<T, Integer> getMap() {
        return new HashMap<>(map);
    }

    public void clear() {
        map.clear();
    }

}
