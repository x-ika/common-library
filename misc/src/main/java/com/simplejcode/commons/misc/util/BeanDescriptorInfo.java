package com.simplejcode.commons.misc.util;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
public final class BeanDescriptorInfo {

    public final String name;
    public final Class<?> type;

    public final Method readMethod;
    public final Method writeMethod;

    public final boolean isCollection;
    public final Class<?> genericType;

}
