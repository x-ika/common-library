package com.simplejcode.commons.misc;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class EventDispatcher {

    private Queue<Object> queue;

    private ScheduledExecutorService executorService;

    private Consumer<Object> consumer;


    public EventDispatcher(Consumer<Object> consumer) {
        this.consumer = consumer;
        queue = new ConcurrentLinkedQueue<>();
    }


    public void setExecutorService(ScheduledExecutorService executorService) {
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
        for (Object event; (event = queue.poll()) != null; ) {
            consumer.accept(event);
        }
    }

}
