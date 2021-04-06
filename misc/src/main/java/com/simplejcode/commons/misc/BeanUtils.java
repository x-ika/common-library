package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.struct.TwoWayMap;
import com.simplejcode.commons.misc.util.*;

import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BeanUtils {

    public static final BeanUtils EMPTY_INSTANCE = new BeanUtils();


    private final TwoWayMap<Class> classMappings;


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
        return cloneCollection(c1, null, elementClass, null, null);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class collectionClass,
                                                                Class<T2> elementClass)
    {
        return cloneCollection(c1, collectionClass, elementClass, null, null);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class<T2> elementClass,
                                                                Integer maxDepth,
                                                                PropertyFilter filter)
    {
        return cloneCollection(c1, null, elementClass, maxDepth, filter);
    }

    public <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                Class collectionClass,
                                                                Class<T2> elementClass,
                                                                Integer maxDepth,
                                                                PropertyFilter filter)
    {
        return cloneCollection(c1, collectionClass, elementClass, new HashMap(), maxDepth, filter);
    }

    //-----------------------------------------------------------------------------------
    /*
    Object Cloning
     */

    public <T1, T2> T2 cloneClass(T1 t1) {
        return cloneClass(t1, null, null, null);
    }

    public <T1, T2> T2 cloneClass(T1 t1, PropertyFilter filter) {
        return cloneClass(t1, null, null, filter);
    }

    public <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz) {
        return cloneClass(t1, clazz, null, null);
    }

    public <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, Integer maxDepth, PropertyFilter filter) {
        return cloneClass(t1, clazz, new HashMap(), maxDepth, filter);
    }

    //-----------------------------------------------------------------------------------
    /*
    Object Copying
     */

    public <T1, T2> void copyClass(T1 t1, T2 t2) {
        copyClass(t1, t2, null);
    }

    public <T1, T2> void copyClass(T1 t1, T2 t2, PropertyFilter filter) {
        copyProperties(t1, t2, new HashMap<>(), null, filter);
    }

    //-----------------------------------------------------------------------------------
    /*
    Private
     */

    private <T1, T2, R extends Collection<T2>> R cloneCollection(Collection<T1> c1,
                                                                 Class collectionClass,
                                                                 Class<T2> elementClass,
                                                                 Map memory,
                                                                 Integer maxDepth,
                                                                 PropertyFilter filter)
    {
        if (c1 == null) {
            return null;
        }
        Collection<T2> result = createEmptyCollection(ObjectUtils.nvl(collectionClass, c1.getClass()));
        for (T1 t1 : c1) {
            result.add(cloneClass(t1, elementClass, memory, maxDepth, filter));
        }
        return (R) result;
    }

    private <T1, T2> T2 cloneClass(T1 t1, Class<T2> clazz, Map memory, Integer maxDepth, PropertyFilter filter) {
        // 1. null case
        if (t1 == null) {
            return null;
        }
        // 2. take from memory
        T2 t2 = (T2) memory.get(t1);
        if (t2 != null) {
            return t2;
        }
        // 3. define result class
        if (clazz == null) {
            Class<?> leftClass = t1.getClass();
            clazz = classMappings.get(leftClass);
            // sometimes we need to copy only parent class fields
            if (clazz == null) {
                clazz = classMappings.get(leftClass.getSuperclass());
            }
            if (clazz == null) {
                clazz = (Class<T2>) leftClass;
            }
        }
        if (clazz.isEnum()) {
            return (T2) t1;
        }
        // 4. create and memorize instance
        try {
            t2 = clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw convert(e);
        }
        memory.put(t1, t2);
        // 5. copy
        copyProperties(t1, t2, memory, maxDepth, filter);
        return t2;
    }

    private <T1, T2> void copyProperties(T1 t1, T2 t2, Map memory, Integer maxDepth, PropertyFilter filter) {
        try {
            if (maxDepth != null && maxDepth == 0) {
                return;
            }

            Class<?> c1 = t1.getClass();
            Class<?> c2 = t2.getClass();

            BeanDescriptorInfo[] srcDescriptors = BeanDescriptorsCache.getReadDescriptors(c1);
            Map<String, BeanDescriptorInfo> map = BeanDescriptorsCache.getWriteDescriptors(c2);
            for (BeanDescriptorInfo d1 : srcDescriptors) {

                String name = d1.name;
                Method getter = d1.readMethod;

                BeanDescriptorInfo d2 = map.get(name);
                if (d2 == null || filter != null && filter.filter(t1, t2, c1, c2, d1, d2, name)) {
                    continue;
                }
                Method setter = d2.writeMethod;
                Class<?> leftType = d1.type;
                Class<?> rightType = d2.type;

                // 1. collections: hard case
                if (d1.isCollection && d2.isCollection) {
                    Collection<?> collection = (Collection) getter.invoke(t1);
                    if (d1.genericType == d2.genericType || classMappings.get(d1.genericType) == d2.genericType) {
                        setter.invoke(t2, cloneCollection(collection, rightType, null, memory, maxDepth, filter));
                    }
                    continue;
                }

                // 2. usual case: equal types
                if (leftType == rightType) {
                    setter.invoke(t2, getter.invoke(t1));
                    continue;
                }

                // 3. special case: distinct types but conversion requested
                if (classMappings.get(leftType) == rightType || classMappings.get(leftType.getSuperclass()) == rightType) {
                    setter.invoke(t2, cloneClass(getter.invoke(t1), rightType, memory, maxDepth != null ? maxDepth - 1 : null, filter));
                }

            }

        } catch (Exception e) {
            throw convert(e);
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
        throw generate("Unknown collection type: " + clazz);
    }

    public interface PropertyFilter {

        boolean filter(Object o1, Object o2, Class c1, Class c2, BeanDescriptorInfo d1, BeanDescriptorInfo d2, String name);

    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

    private static RuntimeException generate(String message) {
        return ExceptionUtils.generate(message);
    }

}
