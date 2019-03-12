package com.simplejcode.commons.misc;

import java.lang.reflect.Method;

@SuppressWarnings({"unchecked"})
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static Class getCallerClass(int i) {
        Class[] classContext = new SecurityManager() {
            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (classContext != null) {
            for (int j = 0; j < classContext.length; j++) {
                if (classContext[j] == ReflectionUtils.class) {
                    return classContext[i + j];
                }
            }
        } else {
            // SecurityManager.getClassContext() returns null on Android 4.0
            try {
                return Class.forName(getCallingElement(i + 1).getClassName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public static StackTraceElement getCallingElement(int i) {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        for (int j = 0; j < elements.length; j++) {
            if (elements[j].getClassName().endsWith("ReflectionUtils")) {
                return elements[i + j];
            }
        }
        return null;
    }

    public static Method getMethod(String methodName, Class<?>... paramTypes) {
        try {
            return getCallerClass(2).getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
