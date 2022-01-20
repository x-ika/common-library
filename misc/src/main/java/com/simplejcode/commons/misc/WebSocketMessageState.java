package com.simplejcode.commons.misc;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class WebSocketMessageState<T> {

    private final Consumer<List<T>> receiveParts;

    private final List<T> parts;

    private CompletableFuture<?> accumulatedMessage;

    public WebSocketMessageState(Consumer<List<T>> receiveParts) {
        this.receiveParts = receiveParts;
        parts = new ArrayList<>();
        accumulatedMessage = new CompletableFuture<>();
    }

    public CompletionStage<?> process(T part, boolean last) {

        CompletableFuture<?> ret = accumulatedMessage;

        parts.add(part);
        if (last) {
            receiveParts.accept(parts);
            parts.clear();
            accumulatedMessage = new CompletableFuture<>();
        }

        return ret;
    }

}
