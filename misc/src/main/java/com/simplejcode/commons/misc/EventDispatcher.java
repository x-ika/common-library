package com.simplejcode.commons.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class EventDispatcher {

    private final AtomicLong tasksInQueue;

    private final ExecutorService executorService;

    private final Consumer<Object> consumer;

    public EventDispatcher(ExecutorService executorService, Consumer<Object> consumer) {
        this.executorService = executorService;
        this.consumer = consumer;
        tasksInQueue = new AtomicLong();
    }

    //-----------------------------------------------------------------------------------

    public long getTaskCount() {
        return tasksInQueue.get();
    }

    public void resetTaskCount() {
        tasksInQueue.set(0);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public boolean isServiceAlive() {
        return executorService != null && !executorService.isShutdown();
    }

    //-----------------------------------------------------------------------------------

    public void dispatchEvent(Object event) {
        if (executorService == null) {
            handle(event);
            return;
        }
        tasksInQueue.incrementAndGet();
        executorService.submit(() -> handle(event));
    }

    private void handle(Object event) {
        tasksInQueue.decrementAndGet();
        consumer.accept(event);
    }

}
