package com.simplejcode.commons.misc;

import javax.websocket.*;
import java.util.function.BiConsumer;

public class WSEndpoint extends Endpoint {

    private BiConsumer<Session, EndpointConfig> onOpen;
    private BiConsumer<Session, CloseReason> onClose;
    private BiConsumer<Session, Throwable> onError;

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
