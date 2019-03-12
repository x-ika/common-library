package com.simplejcode.commons.net.sockets;

public class ConnectionAdapter<T> implements ConnectionListener<T> {
    public void messageSent(SocketConnection<T> source, T message) throws Exception {

    }

    public void messageReceived(SocketConnection<T> source, T message) throws Exception {

    }

    public void sendingFailed(SocketConnection<T> source, T message, Exception e) throws Exception {

    }

    public void receivingFailed(SocketConnection<T> source, Exception e) throws Exception {

    }

    public void disconnected(SocketConnection<T> source) throws Exception {

    }
}
