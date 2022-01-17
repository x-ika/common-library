package com.simplejcode.commons.misc.util;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static Class<?> getCallerClass(int i) {
        Class<?>[] classContext = new SecurityManager() {
            public Class<?>[] getClassContext() {
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
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    public static List<Class<?>> getAllClassesFromPackage(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            ArrayList<Class<?>> result = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                findClasses(packageName, new File(resource.getFile()), result);
            }
            return result;
        } catch (Exception e) {
            throw convert(e);
        }
    }

    private static void findClasses(String packageName, File directory, List<Class<?>> list) {
        if (!directory.exists()) {
            return;
        }
        for (File file : directory.listFiles()) {
            String name = file.getName();
            if (file.isDirectory()) {
                if (!name.contains(".")) {
                    findClasses(packageName + "." + name, file, list);
                }
            } else {
                if (name.endsWith(".class")) {
                    String className = name.substring(0, name.length() - 6);
                    try {
                        list.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                    } catch (ClassNotFoundException ignore) {
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------

    public static <T> T getFieldValue(Object instance, String fieldName) {
        try {
            Field privateField = instance.getClass().getDeclaredField(fieldName);
            privateField.setAccessible(true);
            return (T) privateField.get(instance);
        } catch (Exception e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
