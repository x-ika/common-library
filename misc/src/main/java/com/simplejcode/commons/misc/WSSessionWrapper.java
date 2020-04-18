package com.simplejcode.commons.misc;

import javax.websocket.*;
import java.net.URI;

public class WSSessionWrapper {

    /**
     * Session object
     */
    private Session session;

    /**
     * The associated Endpoint instance
     */
    private Endpoint endpoint;


    public WSSessionWrapper(Endpoint endpoint) {
        this.endpoint = endpoint;
    }


    public void connect(String uri) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
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
