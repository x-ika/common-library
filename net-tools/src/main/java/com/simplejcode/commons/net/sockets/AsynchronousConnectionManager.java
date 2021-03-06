package com.simplejcode.commons.net.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;

public class AsynchronousConnectionManager implements AsynchronousConnectionListener {

    private static final int IO_DELAY = 50;

    protected final int port, pingInterval, soTimout;
    protected boolean running;

    private final Object readerThreadLock, writerThreadLock;

    protected final Collection<AsynchronousConnection> connections;
    protected AsynchronousServerSocketChannel serverChannel;

    public AsynchronousConnectionManager(int port, int pingInterval, int soTimout) {
        this.port = port;
        this.pingInterval = 1000 * pingInterval;
        this.soTimout = 1000 * soTimout;
        readerThreadLock = new Object();
        writerThreadLock = new Object();
        connections = new Vector<>();
    }

    protected AsynchronousConnection create(AsynchronousSocketChannel socket) throws IOException {
        return new AsynchronousConnection(socket, 1 << 9, 1 << 9, '1', '2', '@', '@');
    }

    public synchronized void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        if (port > 0) {
            startListening();
        }
        if (pingInterval > 0) {
            startPinging();
        }
        startReading();
        startWriting();
    }

    public synchronized void stop() {
        running = false;
        try {
            serverChannel.close();
        } catch (Exception ignore) {
        }
    }

    public synchronized boolean isRunning() {
        return running;
    }

    //-----------------------------------------------------------------------------------

    public void addConnection(AsynchronousConnection connection) {
        if (connections.add(connection)) {
            connection.addConnectionListener(this);
        }
        notifyThread(readerThreadLock);
    }

    public void removeConnection(AsynchronousConnection connection) {
        if (connections.remove(connection)) {
            connection.removeConnectionListener(this);
        }
    }

    public void sendToAll(byte[] message) {
        sendToAll(null, message);
    }

    public void sendToAll(AsynchronousConnection except, byte[] message) {
        AsynchronousConnection[] array = connections.toArray(new AsynchronousConnection[0]);
        for (AsynchronousConnection connection : array) {
            if (connection != except) {
                sendTo(connection, message);
            }
        }
    }

    public void sendTo(AsynchronousConnection connection, byte[] message) {
        connection.write(message);
    }

    //-----------------------------------------------------------------------------------

    public void messageReceived(AsynchronousConnection source, byte[] message) {
        notifyThread(readerThreadLock);
    }

    public void receivingFailed(AsynchronousConnection source, Throwable e) throws Exception {
        notifyThread(readerThreadLock);
    }

    public void messageSent(AsynchronousConnection source, byte[] message) throws Exception {
        notifyThread(writerThreadLock);
    }

    public void sendingFailed(AsynchronousConnection source, byte[] message, Throwable e) throws Exception {
        notifyThread(writerThreadLock);
    }

    public void disconnected(AsynchronousConnection source) {
        removeConnection(source);
    }

    //-----------------------------------------------------------------------------------

    private void startListening() throws IOException {
        serverChannel = AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        new Thread(() -> {
            while (running) {
                try {
                    addConnection(create(serverChannel.accept().get()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startReading() {
        new Thread(() -> {
            while (running) {
                synchronized (connections) {
                    for (AsynchronousConnection connection : connections) {
                        connection.tryRead(soTimout);
                    }
                }
                synchronized (readerThreadLock) {
                    try {
                        readerThreadLock.wait(IO_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void startWriting() {
        new Thread(() -> {
            while (running) {
                synchronized (connections) {
                    for (AsynchronousConnection connection : connections) {
                        connection.tryWrite();
                    }
                }
                synchronized (writerThreadLock) {
                    try {
                        writerThreadLock.wait(IO_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void startPinging() {
        new Thread(() -> {
            while (running) {
                synchronized (connections) {
                    for (AsynchronousConnection connection : connections) {
                        connection.ping();
                    }
                }
                try {
                    Thread.sleep(pingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void notifyThread(Object threadLockObject) {
        synchronized (threadLockObject) {
            threadLockObject.notify();
        }
    }

}
