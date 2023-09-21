package com.simplejcode.commons.misc;

import jakarta.websocket.*;
import java.net.URI;

public class WSSessionWrapper {

    private static final String TIMEOUT_PROPERTY = "io.undertow.websocket.CONNECT_TIMEOUT";

    private static final int DEFAULT_WEB_SOCKET_TIMEOUT_SECONDS = 15;

    /**
     * Session object
     */
    private Session session;

    /**
     * The associated Endpoint instance
     */
    private final Endpoint endpoint;


    public WSSessionWrapper(Endpoint endpoint) {
        this.endpoint = endpoint;
    }


    public void connect(String uri) throws Exception {
        connect(uri, DEFAULT_WEB_SOCKET_TIMEOUT_SECONDS);
    }

    public void connect(String uri, int timeOut) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
        config.getUserProperties().put(TIMEOUT_PROPERTY, timeOut);
        container.connectToServer(endpoint, config, new URI(uri));
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void disconnect() throws Exception {
        if (session != null) {
            session.close();
        }
    }

    public void sendText(String text) {
        this.session.getAsyncRemote().sendText(text);
    }

}
