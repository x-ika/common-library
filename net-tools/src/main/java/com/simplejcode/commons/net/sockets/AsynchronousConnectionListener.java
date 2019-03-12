package com.simplejcode.commons.net.sockets;

public interface AsynchronousConnectionListener {

    void messageReceived(AsynchronousConnection source, byte[] message) throws Exception;

    void receivingFailed(AsynchronousConnection source, Throwable e) throws Exception;


    void messageSent(AsynchronousConnection source, byte[] message) throws Exception;

    void sendingFailed(AsynchronousConnection source, byte[] message, Throwable e) throws Exception;


    void disconnected(AsynchronousConnection source) throws Exception;

}
