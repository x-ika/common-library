package com.simplejcode.commons.misc.util;

import com.simplejcode.commons.misc.BeanDescriptorsCache;
import com.simplejcode.commons.misc.ExceptionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"unchecked"})
public class BeanUtils {

    private static final String NO_IGNORE = "";


    private TwoWayMap<Class> classMappings;


    public BeanUtils(TwoWayMap<Class> classMappings) {
        this.classMappings = classMappings;
    }

    public BeanUtils() {
        this(new TwoWayMap<>());
    }

    //-----------------------------------------------------------------------------------
    /*
    Collection Cloning
     */

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class<T2> elementClass)
    {
        return cloneCollection(c1, elementClass, NO_IGNORE, null);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class collectionClass,
                                                                Class<T2> elementClass)
    {
        return cloneCollection(c1, collectionClass, elementClass, NO_IGNORE, null);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class<T2> elementClass,
                                                                String ignoreProperties,
                                                                Integer depth)
    {
        return cloneCollection(c1, c1.getClass(), elementClass, ignoreProperties, depth);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class collectionClass,
                                                                Class<T2> elementClass,
                                                                String ignoreProperties,
                                                                Integer depth)
    {
        if (c1 == null) {
            return null;
        }
        Collection<T2> result = createEmptyCollection(collectionClass);
        for (T1 t1 : c1) {
            result.add(cloneClass(t1, elementClass, ignoreProperties, depth));
        }
        return (R) result;
    }

    //-----------------------------------------------------------------------------------
    /*
    Object Cloning
     */

    public <T1, T2> T2 cloneClass(T1 t1) {
        return cloneClass(t1, NO_IGNORE);
    }

    public <T1, T2> T2 cloneClass(T1 t1, String ignoreProperties) {
        if (t1 == null) {
            return null;
        }
        Class<?> leftClass = t1.getClass();
        Class rightClass = classMappings.get(leftClass);
        // sometimes we need to copy only parent class fields
        if (rightClass == null) {
            rightClass = classMappings.get(leftClass.getSuperclass());
        }
        return cloneClass(t1, (Class<T2>) rightClass, ignoreProperties, null);
    }

    public <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz) {
        return cloneClass(t1, clazz, new HashMap<>(), NO_IGNORE, null);
    }

    public <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, String ignoreProperties, Integer depth) {
        return cloneClass(t1, clazz, new HashMap<>(), ignoreProperties, depth);
    }

    public <T1, T2> void copyClass(T1 t1, T2 t2) {
        copyClass(t1, t2, NO_IGNORE);
    }

    public <T1, T2> void copyClass(T1 t1, T2 t2, String ignoreProperties) {
        copyProperties(t1, t2, new HashMap<>(), ignoreProperties, null);
    }

    //-----------------------------------------------------------------------------------
    /*
    Private
     */

    private <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, Map memory, String ignoreProperties, Integer depth) {
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
        copyProperties(t1, t2, memory, ignoreProperties, depth);
        return t2;
    }

    private <T1, T2> void copyProperties(T1 t1, T2 t2, Map memory, String ignoreProperties, Integer depth) {
        try {
            if (depth != null && depth == 0) {
                return;
            }
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
                if (Collection.class.isAssignableFrom(leftType) && Collection.class.isAssignableFrom(rightType)) {
                    Collection<?> collection = (Collection) getter.invoke(t1);
                    // 1.1 collection is null
                    if (collection == null) {
                        continue;
                    }
                    // 1.2 collection is empty
                    if (collection.isEmpty()) {
                        setter.invoke(t2, createEmptyCollection(rightType));
                        continue;
                    }
                    // 1.3 need to clone
                    Class<?> elementClass = collection.iterator().next().getClass();
                    Class<?> toElementClass = classMappings.get(elementClass);
                    if (toElementClass != null) {
                        setter.invoke(t2, cloneCollection(collection, rightType, toElementClass));
                    }
                    continue;
                }

                // 2. usual case: equal types
                if (leftType == rightType) {
                    setter.invoke(t2, getter.invoke(t1));
                    continue;
                }

                // 3. distinct types but conversion possible
                if (classMappings.get(leftType) == rightType || classMappings.get(leftType.getSuperclass()) == rightType) {
                    setter.invoke(t2, cloneClass(getter.invoke(t1), rightType, memory, NO_IGNORE, depth != null ? depth - 1 : null));
                }

                // 4. custom converters (localDate <-> sqlDate)
                Object getterValue = getter.invoke(t1);
                if (getterValue != null) {
                    if (rightType.equals(java.sql.Date.class) && leftType.equals(java.time.LocalDate.class)) {
                        setter.invoke(t2, java.sql.Date.valueOf((java.time.LocalDate) getterValue));
                    } else if (leftType.equals(java.sql.Date.class) && rightType.equals(java.time.LocalDate.class)) {
                        setter.invoke(t2, ((java.sql.Date) getterValue).toLocalDate());
                    }
                }
            }

        } catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private static <T> Collection<T> createEmptyCollection(Class clazz) {
        if (List.class.isAssignableFrom(clazz) || Collection.class == clazz) {
            return new ArrayList<>();
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet<>();
        }
        if (Queue.class.isAssignableFrom(clazz)) {
            return new ArrayDeque<>();
        }
        throw new RuntimeException("Unknown collection type: " + clazz);
    }

}
