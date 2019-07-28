package com.simplejcode.commons.net.sockets;

import java.io.IOException;
import java.net.*;
import java.util.*;

@SuppressWarnings({"InfiniteLoopStatement"})
public abstract class ConnectionsManager<T> extends ConnectionAdapter<T> {

    protected final int port, pingInterval;
    protected final Collection<SocketConnection<T>> connections;

    protected ServerSocket serverSocket;
    protected boolean running;

    public ConnectionsManager(int port, int pingInterval) {
        this.port = port;
        this.pingInterval = pingInterval;
        connections = new Vector<>();
    }

    public ConnectionsManager(int port) {
        this(port, 0);
    }

    protected abstract SocketConnection<T> create(Socket socket) throws IOException;

    public synchronized void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        if (port > 0) {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (running) {
                    try {
                        addConnection(create(serverSocket.accept()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        if (pingInterval > 0) {
            new Thread(() -> {
                while (running) {
                    try {
                        Thread.sleep(pingInterval);
                    } catch (InterruptedException e) {
                        // empty
                    }
                    synchronized (ConnectionsManager.this) {
                        for (SocketConnection<T> connection : connections) {
                            connection.sendPing();
                        }
                    }
                }
            }).start();
        }
    }

    public synchronized void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (Exception ignore) {
        }
    }

    public synchronized boolean isRunning() {
        return running;
    }


    public synchronized void addConnection(SocketConnection<T> connection) {
        if (connections.contains(connection)) {
            return;
        }
        connection.addConnectionListener(this);
        connections.add(connection);
        connection.start();
    }

    public synchronized void removeConnection(SocketConnection<T> connection) {
        if (connections.remove(connection)) {
            connection.removeConnectionListener(this);
        }
    }

    public synchronized void sendToAll(T message) {
        sendToAll(null, message);
    }

    public synchronized void sendToAll(SocketConnection<T> except, T message) {
        for (SocketConnection<T> connection : connections) {
            if (connection != except) {
                connection.sendMessage(message);
            }
        }
    }

    public synchronized void sendTo(SocketConnection<T> connection, T message) {
        connection.sendMessage(message);
    }

    public void handleMessageReceived(SocketConnection<T> source, T message) {
    }

    //-----------------------------------------------------------------------------------

    public void messageReceived(SocketConnection<T> source, T message) {
        new Thread(() -> handleMessageReceived(source, message)).start();
    }

    public void disconnected(SocketConnection<T> source) {
        removeConnection(source);
    }

}
