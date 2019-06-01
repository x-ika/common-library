package com.simplejcode.commons.misc.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtils {

    private UnsafeUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final int VM_ADD = 12;

    private static final Unsafe unsafe;
    private static final long fieldOffset;
    private static final UnsafeUtils instance = new UnsafeUtils();

    private Object obj;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            fieldOffset = unsafe.objectFieldOffset(UnsafeUtils.class.getDeclaredField("obj"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long ObjectToAddress(Object o) {
        instance.obj = o;
        return unsafe.getLong(instance, fieldOffset);
    }

    public static Object AddressToObject(long address) {
        unsafe.putLong(instance, fieldOffset, address);
        return instance.obj;
    }

    public static long sizeOf(Object object) {
        return unsafe.getAddress(unsafe.getAddress(ObjectToAddress(object) + 4) + VM_ADD);
    }

    public static void copyObjectShallow(Object objectSource, Object objectDest) {
        unsafe.copyMemory(
                ObjectToAddress(objectSource),
                ObjectToAddress(objectDest),
                sizeOf(objectSource) * 4);
    }

}
