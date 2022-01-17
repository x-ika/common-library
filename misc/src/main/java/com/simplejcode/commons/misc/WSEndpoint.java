package com.simplejcode.commons.misc;

import javax.websocket.*;
import java.io.IOException;
import java.util.function.BiConsumer;

public class WSEndpoint extends Endpoint {

    private final BiConsumer<Session, EndpointConfig> onOpen;
    private final BiConsumer<Session, CloseReason> onClose;
    private final BiConsumer<Session, Throwable> onError;

    private WSSessionWrapper sessionWrapper;


    public WSEndpoint(BiConsumer<Session, EndpointConfig> onOpen,
                      BiConsumer<Session, CloseReason> onClose,
                      BiConsumer<Session, Throwable> onError)
    {
        this.onOpen = onOpen;
        this.onClose = onClose;
        this.onError = onError;
    }


    public void setSessionWrapper(WSSessionWrapper sessionWrapper) {
        this.sessionWrapper = sessionWrapper;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        MessageHandler.Whole<PongMessage> pongMessageWhole = new MessageHandler.Whole<PongMessage>() {
            @Override
            public void onMessage(PongMessage message) {
                try {
                    session.getAsyncRemote().sendPong(message.getApplicationData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        session.addMessageHandler(pongMessageWhole);
        sessionWrapper.setSession(session);
        onOpen.accept(session, config);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        onClose.accept(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        onError.accept(session, throwable);
    }

}
