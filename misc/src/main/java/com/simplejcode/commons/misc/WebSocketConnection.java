package com.simplejcode.commons.misc;

import java.net.URI;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class WebSocketConnection {

    private static final int DEFAULT_WEB_SOCKET_TIMEOUT_SECONDS = 15;


    private final WebSocketConnectionHandler handler;

    private WebSocket webSocket;


    public WebSocketConnection(WebSocketConnectionHandler handler) {
        this.handler = handler;
    }


    public void connect(String uri) {
        connect(uri, DEFAULT_WEB_SOCKET_TIMEOUT_SECONDS);
    }

    public void connect(String uri, int timeout) {
        webSocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .connectTimeout(Duration.ofSeconds(timeout))
                .buildAsync(URI.create(uri), handler)
                .join();
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
        }
    }

    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
        return webSocket.sendText(data, last);
    }

    public CompletableFuture<WebSocket> sendPing(ByteBuffer message) {
        return webSocket.sendPing(message);
    }

}
