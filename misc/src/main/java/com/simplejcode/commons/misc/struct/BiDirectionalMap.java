package com.simplejcode.commons.misc.struct;

import java.util.*;

public class BiDirectionalMap<T1, T2> {

    private final Map<T1, T2> map1;
    private final Map<T2, T1> map2;

    public BiDirectionalMap() {
        map1 = new HashMap<>();
        map2 = new HashMap<>();
    }

    public void put(T1 t1, T2 t2) {
        map1.put(t1, t2);
        map2.put(t2, t1);
    }

    public T2 getValue(T1 t) {
        return map1.get(t);
    }

    public T1 getKey(T2 t) {
        return map2.get(t);
    }

}
