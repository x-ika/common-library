package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.ExceptionUtils;

import java.util.*;
import java.util.function.Function;

public class BiDirectionalMap<T1, T2> {

    private final Map<T1, T2> map1;
    private final Map<T2, T1> map2;


    public BiDirectionalMap() {
        map1 = new HashMap<>();
        map2 = new HashMap<>();
    }


    public Set<T1> keys1() {
        return map1.keySet();
    }

    public Set<T2> keys2() {
        return map2.keySet();
    }

    public synchronized int size() {
        return map1.size();
    }

    public synchronized T2 computeIfAbsent(T1 t1, Function<T1, T2> mappingFunction) {
        T2 t2 = map1.get(t1);
        if (t2 != null) {
            return t2;
        }
        t2 = mappingFunction.apply(t1);
        put(t1, t2);
        return t2;
    }

    public synchronized void put(T1 t1, T2 t2) {
        map1.put(t1, t2);
        map2.put(t2, t1);
    }

    public synchronized T2 getValue(T1 t) {
        return map1.get(t);
    }

    public synchronized T1 getKey(T2 t) {
        return map2.get(t);
    }

    public synchronized void removeByKey(T1 t1) {
        T2 t2 = map1.remove(t1);
        if (t2 != null) {
            check(map2.remove(t2));
        }
    }

    public synchronized void removeByValue(T2 t2) {
        T1 t1 = map2.remove(t2);
        if (t1 != null) {
            check(map1.remove(t1));
        }
    }

    private void check(Object obj) {
        if (obj == null) {
            throw ExceptionUtils.generate("One of the maps contained no key");
        }
    }

}
