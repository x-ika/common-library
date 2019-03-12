package com.simplejcode.commons.net.sockets;

import java.io.IOException;
import java.net.Socket;

public class FlashConnection extends FastConnection {

    private boolean checked = false;

    public FlashConnection(Socket socket, int queueCapacity, int pingInterval, int ping, int exit, int inEnd, int outEnd) throws IOException {
        super(socket, queueCapacity, pingInterval, ping, exit, inEnd, outEnd);
        socket.setTcpNoDelay(true);
    }

    protected byte[] read() throws IOException {
        byte[] message = super.read();
        if (!checked && new String(message).contains("policy-file-request")) {
            outputStream.write(buildPolicy().getBytes());
            message = exit;
        }
        checked = true;
        return message;
    }

    private static String buildPolicy() {
        StringBuilder policyBuffer = new StringBuilder();
        policyBuffer.append("<?xml version=\"1.0\"?><cross-domain-policy>");
        policyBuffer.append("<allow-access-from domain=\"*\" to-ports=\"*\" />");
        policyBuffer.append("</cross-domain-policy>");
        return policyBuffer.toString();
    }

}
