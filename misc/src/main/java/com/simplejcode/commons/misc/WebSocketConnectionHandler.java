package com.simplejcode.commons.misc;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.function.*;

public class WebSocketConnectionHandler implements WebSocket.Listener {

    private final Runnable onOpen;
    private final BiConsumer<Integer, String> onClose;
    private final Consumer<Throwable> onError;

    private WebSocketMessageState<CharSequence> textMessage;
    private WebSocketMessageState<ByteBuffer> binaryMessage;

    public WebSocketConnectionHandler(Runnable onOpen,
                                      BiConsumer<Integer, String> onClose,
                                      Consumer<Throwable> onError,
                                      Consumer<String> onText,
                                      Consumer<byte[]> onBinary)
    {
        this.onOpen = onOpen;
        this.onClose = onClose;
        this.onError = onError;

        textMessage = new WebSocketMessageState<>(list -> {
            StringBuilder sb = new StringBuilder();
            for (CharSequence t : list) {
                sb.append(t);
            }
            onText.accept(sb.toString());
        });

        binaryMessage = new WebSocketMessageState<>(list -> {
            int sum = 0;
            for (ByteBuffer t : list) {
                sum += t.remaining();
            }
            byte[] data = new byte[sum];
            int off = 0;
            for (ByteBuffer t : list) {
                t.get(data, off, t.remaining());
                off += t.remaining();
            }
            onBinary.accept(data);
        });
    }

    //-----------------------------------------------------------------------------------

    @Override
    public void onOpen(WebSocket webSocket) {
        onOpen.run();
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        onClose.accept(statusCode, reason);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        onError.accept(error);
        WebSocket.Listener.super.onError(webSocket, error);
    }

    //-----------------------------------------------------------------------------------

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        webSocket.request(1);
        return textMessage.process(data, last);
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        webSocket.request(1);
        return binaryMessage.process(data, last);
    }

}
