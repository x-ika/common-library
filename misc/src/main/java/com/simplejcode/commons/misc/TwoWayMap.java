package com.simplejcode.commons.misc;

import java.util.*;

public class TwoWayMap<T> {

    private Map<T, T> map;

    public TwoWayMap() {
        map = new HashMap<>();
    }

    public void put(T t1, T t2) {
        map.put(t1, t2);
        map.put(t2, t1);
    }

    public T get(T t) {
        return map.get(t);
    }

}
