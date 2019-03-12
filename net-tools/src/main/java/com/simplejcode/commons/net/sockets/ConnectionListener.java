package com.simplejcode.commons.net.sockets;

public interface ConnectionListener<T> {

    void messageSent(SocketConnection<T> source, T message) throws Exception;

    void messageReceived(SocketConnection<T> source, T message) throws Exception;

    void sendingFailed(SocketConnection<T> source, T message, Exception e) throws Exception;

    void receivingFailed(SocketConnection<T> source, Exception e) throws Exception;

    void disconnected(SocketConnection<T> source) throws Exception;

}
