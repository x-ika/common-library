package com.simplejcode.commons.misc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"unchecked"})
public final class BeanUtils {

    private BeanUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final String NO_IGNORE = "";

    // TODO change BeanUtils entities by per module impl
    private static Map<Class, Class> forwardMap = new HashMap<>();
    private static Map<Class, Class> backwardMap = new HashMap<>();

    public static void init(Class[] canCopyFrom, Class[] canCopyTo) {
        int n = canCopyFrom.length;
        for (int i = 0; i < n; i++) {
            Class<?> from = canCopyFrom[i];
            Class<?> to = canCopyTo[i];
            forwardMap.put(from, to);
            backwardMap.put(to, from);
        }
    }


    public static <T1, T2> List<T2> cloneList(Collection<T1> t1List, Class<T2> clazz) {
        return cloneList(t1List, clazz, NO_IGNORE);
    }

    public static <T1, T2> List<T2> cloneList(Collection<T1> t1List, Class<T2> clazz, String ignoreProperties) {
        if (t1List == null) {
            return null;
        }
        List<T2> result = new ArrayList<>();
        for (T1 t1 : t1List) {
            result.add(cloneClass(t1, clazz, ignoreProperties));
        }
        return result;
    }

    public static <T1, T2> T2 cloneClass(T1 t1) {
        return cloneClass(t1, NO_IGNORE);
    }

    public static <T1, T2> T2 cloneClass(T1 t1, String ignoreProperties) {
        if (t1 == null) {
            return null;
        }
        Class<?> leftClass = t1.getClass();
        Class rightClass = forwardMap.getOrDefault(leftClass, backwardMap.get(leftClass));
        if (rightClass == null) {
            rightClass = forwardMap.getOrDefault(leftClass.getSuperclass(), backwardMap.get(leftClass));
        }
        return cloneClass(t1, (Class<T2>) rightClass, ignoreProperties);
    }

    public static <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz) {
        return cloneClass(t1, clazz, new HashMap<>(), NO_IGNORE);
    }

    public static <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, String ignoreProperties) {
        return cloneClass(t1, clazz, new HashMap<>(), ignoreProperties);
    }

    public static <T1, T2> void copyClass(T1 t1, T2 t2) {
        copyClass(t1, t2, NO_IGNORE);
    }

    public static <T1, T2> void copyClass(T1 t1, T2 t2, String ignoreProperties) {
        copyProperties(t1, t2, new HashMap<>(), ignoreProperties);
    }

    //-----------------------------------------------------------------------------------
    /*
    Private
     */

    private static <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, Map memory, String ignoreProperties) {
        if (t1 == null) {
            return null;
        }
        T2 t2 = (T2) memory.get(t1);
        if (t2 != null) {
            return t2;
        }
        try {
            t2 = clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw ExceptionUtils.wrap(e);
        }
        memory.put(t1, t2);
        copyProperties(t1, t2, memory, ignoreProperties);
        return t2;
    }

    private static <T1, T2> void copyProperties(T1 t1, T2 t2, Map memory, String ignoreProperties) {
        try {

            Map<String, PropertyDescriptor> map = BeanDescriptorsCache.getDescriptorsMap(t2.getClass());
            PropertyDescriptor[] srcDescriptors = BeanDescriptorsCache.getDescriptorsArray(t1.getClass());
            for (PropertyDescriptor d1 : srcDescriptors) {

                String name = d1.getName();
                Method getter = d1.getReadMethod();

                PropertyDescriptor d2 = map.get(name);
                if (d2 == null || ignoreProperties.contains(name)) {
                    continue;
                }
                Method setter = d2.getWriteMethod();
                Class<?> leftType = d1.getPropertyType();
                Class<?> rightType = d2.getPropertyType();

                // 1. collections: hard case
                if (Collection.class.isAssignableFrom(leftType) && rightType.isAssignableFrom(List.class)) {
                    Collection collection = (Collection) getter.invoke(t1);
                    // 1.1 collection is null
                    if (collection == null) {
                        continue;
                    }
                    // 1.2 collection is empty
                    if (collection.isEmpty()) {
                        setter.invoke(t2, new ArrayList<>());
                        continue;
                    }
                    // 1.3 copy to list
                    Class<?> elementClass = collection.iterator().next().getClass();
                    Class<?> toElementClass = forwardMap.computeIfAbsent(elementClass, k -> backwardMap.get(k));
                    setter.invoke(t2, cloneList(collection, toElementClass));
                    continue;
                }
                // 2. usual case: equal types
                if (leftType == rightType) {
                    setter.invoke(t2, getter.invoke(t1));
                    continue;
                }
                // 3. distinct types but conversion possible
                if (forwardMap.get(leftType) == rightType || backwardMap.get(leftType) == rightType) {
                    setter.invoke(t2, cloneClass(getter.invoke(t1), rightType, memory, NO_IGNORE));
                }
                Class<?> leftSuper = leftType.getSuperclass();
                if (forwardMap.get(leftSuper) == rightType || backwardMap.get(leftSuper) == rightType) {
                    setter.invoke(t2, cloneClass(getter.invoke(t1), rightType, memory, NO_IGNORE));
                }

            }

        } catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
