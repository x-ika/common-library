package com.simplejcode.commons.misc.util;

import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw convert(e);
        }
    }

    public static String dumpAllThreadsStack() {
        StringBuilder sb = new StringBuilder(1 << 10);
        Map<Thread, StackTraceElement[]> m = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> e : m.entrySet()) {
            sb.append(e.getKey().toString()).append('\n');
            for (StackTraceElement s : e.getValue()) {
                sb.append("    ").append(s).append('\n');
            }
        }
        return sb.toString();
    }

    //-----------------------------------------------------------------------------------

    public static Thread createThread(Runnable runnable) {
        return new Thread(runnable);
    }

    public static Thread createThread(String name, Runnable runnable) {
        return new Thread(runnable, name);
    }

    public static ThreadFactory createThreadFactory(String name) {
        return r -> createThread(name, r);
    }

    public static void executeInNewThread(Runnable runnable) {
        createThread(runnable).start();
    }

    public static void executeInNewThread(String name, Runnable runnable) {
        createThread(name, runnable).start();
    }

    //-----------------------------------------------------------------------------------

    public static void wait(Object lock, long timeout) {
        try {
            lock.wait(timeout);
        } catch (InterruptedException e) {
            throw convert(e);
        }
    }

    public static void waitFor(Object lock, Supplier<Boolean> condition) {
        waitFor(lock, condition, 0);
    }

    public static void waitFor(Object lock, Supplier<Boolean> condition, long timeout) {
        long end = timeout == 0 ? 0 : System.currentTimeMillis() + timeout;
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (lock) {
            while (!condition.get()) {
                try {
                    lock.wait(50);
                    if (timeout != 0 && System.currentTimeMillis() > end) {
                        break;
                    }
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
