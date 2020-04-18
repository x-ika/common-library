package com.simplejcode.commons.misc.util;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.*;

public final class BeanDescriptorsCache {

    private BeanDescriptorsCache() {
    }

    //-----------------------------------------------------------------------------------

    private static Map<Class, BeanDescriptorInfo[]> readDescriptors = new HashMap<>();
    private static Map<Class, Map<String, BeanDescriptorInfo>> writeDescriptors = new HashMap<>();

    public static BeanDescriptorInfo[] getReadDescriptors(Class clazz) {
        cacheDescriptors(clazz);
        return readDescriptors.get(clazz);
    }

    public static Map<String, BeanDescriptorInfo> getWriteDescriptors(Class clazz) {
        cacheDescriptors(clazz);
        return writeDescriptors.get(clazz);
    }

    private static void cacheDescriptors(Class<?> clazz) {
        if (writeDescriptors.containsKey(clazz)) {
            return;
        }
        synchronized (BeanDescriptorsCache.class) {
            if (writeDescriptors.containsKey(clazz)) {
                return;
            }
            try {

                PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();

                List<BeanDescriptorInfo> readersList = new ArrayList<>();
                Map<String, BeanDescriptorInfo> writersMap = new HashMap<>();

                for (PropertyDescriptor d : descriptors) {
                    if (d.getReadMethod() != null || d.getWriteMethod() != null) {
                        BeanDescriptorInfo info = convert(clazz, d);
                        if (d.getReadMethod() != null) {
                            readersList.add(info);
                        }
                        if (d.getWriteMethod() != null) {
                            writersMap.put(d.getName(), info);
                        }
                    }
                }

                readDescriptors.put(clazz, readersList.toArray(new BeanDescriptorInfo[0]));
                writeDescriptors.put(clazz, writersMap);

            } catch (IntrospectionException e) {
                throw convert(e);
            }
        }
    }

    private static BeanDescriptorInfo convert(Class<?> clazz, PropertyDescriptor descriptor) {
        String name = descriptor.getName();
        Class<?> type = descriptor.getPropertyType();
        Method readMethod = descriptor.getReadMethod();
        Method writeMethod = descriptor.getWriteMethod();
        boolean isCollection = Collection.class.isAssignableFrom(type);
        Class<?> genericType = isCollection ? parseFrom(clazz, name) : null;
        return new BeanDescriptorInfo(name, type, readMethod, writeMethod, isCollection, genericType);
    }

    private static Class<?> parseFrom(Class<?> clazz, String fieldName) {
        try {
            String typeName = clazz.getDeclaredField(fieldName).getGenericType().getTypeName();
            return Class.forName(typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>')));
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return parseFrom(superclass, fieldName);
            }
            throw convert(e);
        } catch (ClassNotFoundException e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
