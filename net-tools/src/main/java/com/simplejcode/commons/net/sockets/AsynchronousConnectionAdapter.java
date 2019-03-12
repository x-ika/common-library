package com.simplejcode.commons.net.sockets;

public class AsynchronousConnectionAdapter implements AsynchronousConnectionListener {
    public void messageReceived(AsynchronousConnection source, byte[] message) throws Exception {
    }

    public void receivingFailed(AsynchronousConnection source, Throwable e) throws Exception {
    }

    public void messageSent(AsynchronousConnection source, byte[] message) throws Exception {
    }

    public void sendingFailed(AsynchronousConnection source, byte[] message, Throwable e) throws Exception {
    }

    public void disconnected(AsynchronousConnection source) throws Exception {
    }
}
