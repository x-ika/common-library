package com.simplejcode.commons.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class EventDispatcher {

    private AtomicLong tasksInQueue;

    private ExecutorService executorService;

    private Consumer<Object> consumer;


    public EventDispatcher(Consumer<Object> consumer) {
        this.consumer = consumer;
        tasksInQueue = new AtomicLong();
    }


    public long getTaskCount() {
        return tasksInQueue.get();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    public void resetTaskCount() {
        tasksInQueue.set(0);
    }

    public void dispatchEvent(Object event) {
        if (executorService == null) {
            handle(event);
            return;
        }
        tasksInQueue.incrementAndGet();
        executorService.submit(() -> handle(event));
    }

    public void handle(Object event) {
        try {
            tasksInQueue.decrementAndGet();
            consumer.accept(event);
        } catch (RuntimeException e) {
            Thread thread = Thread.currentThread();
            thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
        }
    }

    public boolean isServiceAlive(){
        return executorService != null && !executorService.isShutdown();
    }

}
