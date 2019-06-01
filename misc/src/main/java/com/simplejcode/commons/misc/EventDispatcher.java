package com.simplejcode.commons.misc;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class EventDispatcher {

    private Queue<Object> queue;

    private ExecutorService executorService;

    private Consumer<Object> consumer;


    public EventDispatcher(Consumer<Object> consumer) {
        this.consumer = consumer;
        queue = new ConcurrentLinkedQueue<>();
    }


    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    public void clear() {
        queue.clear();
    }

    public void dispatchEvent(Object event) {
        if (executorService == null) {
            consumer.accept(event);
            return;
        }
        queue.add(event);
        executorService.submit(this::handle);
    }

    public void handle() {
        try {
            for (Object event; (event = queue.poll()) != null; ) {
                consumer.accept(event);
            }
        } catch (RuntimeException e) {
            Thread thread = Thread.currentThread();
            thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
        }
    }

}
