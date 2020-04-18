package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.ObjectUtils;

public class TwoWayMap<T> extends BiDirectionalMap<T, T> {

    public T get(T t) {
        return ObjectUtils.nvl(getKey(t), getValue(t));
    }

}
