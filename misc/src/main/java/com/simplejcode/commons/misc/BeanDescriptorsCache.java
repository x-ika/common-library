package com.simplejcode.commons.misc;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.*;

public final class BeanDescriptorsCache {

    private BeanDescriptorsCache() {
    }

    //-----------------------------------------------------------------------------------

    private static Map<Class, PropertyDescriptor[]> descriptorArray = new HashMap<>();
    private static Map<Class, Map<String, PropertyDescriptor>> descriptorMap = new HashMap<>();

    public static PropertyDescriptor[] getDescriptorsArray(Class clazz) {
        cacheDescriptors(clazz);
        return descriptorArray.get(clazz);
    }

    public static Map<String, PropertyDescriptor> getDescriptorsMap(Class clazz) {
        cacheDescriptors(clazz);
        return descriptorMap.get(clazz);
    }

    private static void cacheDescriptors(Class<?> clazz) {
        if (descriptorArray.containsKey(clazz)) {
            return;
        }
        try {

            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();

            List<PropertyDescriptor> list = new ArrayList<>();
            for (PropertyDescriptor descriptor : descriptors) {
                Method getter = descriptor.getReadMethod();
                if (getter != null) {
                    list.add(descriptor);
                }
            }
            descriptorArray.put(clazz, list.toArray(new PropertyDescriptor[0]));

            Map<String, PropertyDescriptor> map = new HashMap<>();
            for (PropertyDescriptor d : descriptors) {
                if (d.getWriteMethod() != null) {
                    map.put(d.getName(), d);
                }
            }
            descriptorMap.put(clazz, map);

        } catch (IntrospectionException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
