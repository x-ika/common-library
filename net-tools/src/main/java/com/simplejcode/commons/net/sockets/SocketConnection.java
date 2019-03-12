package com.simplejcode.commons.net.sockets;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Fast and flexible socket based connection
 */
@SuppressWarnings({"unchecked"})
public abstract class SocketConnection<T> {

    private static final int EVENT_MESASAGE_SENT = 1;
    private static final int EVENT_MESASAGE_RECEIVED = 2;
    private static final int EVENT_SENDING_FAILED = 3;
    private static final int EVENT_RECEIVING_FAILED = 4;
    private static final int EVENT_DISCONNECTED = 5;

    protected final Socket socket;
    protected final T ping, exit;
    protected OutputStream outputStream;

    private final int queueCapacity, pingIterval;
    private final List<ConnectionListener<T>> listeners;
    private final BlockingQueue<T> queue;
    private ConnectionListener<T>[] listenersClone;
    private Object host;
    private boolean exiting, started, pause;

    /**
     * Creates new intance.
     *
     * @param socket        original socket to be used
     * @param queueCapacity capacity of the send queue
     * @param pingInterval  interval between consecutive pings
     * @param ping          ping message
     * @param exit          exit message
     * @param soTimeout     soTimeout
     * @throws IOException when sopcket output stream can not be retrieved
     */
    public SocketConnection(Socket socket, int soTimeout, int queueCapacity, int pingInterval, T ping, T exit) throws IOException {
        this.socket = socket;
        this.pingIterval = pingInterval;
        this.queueCapacity = queueCapacity;
        this.ping = ping;
        this.exit = exit;
        listeners = new ArrayList<>();
        listenersClone = new ConnectionListener[4];
        queue = queueCapacity > 0 ? new ArrayBlockingQueue<>(queueCapacity) : null;
        outputStream = socket.getOutputStream();
        socket.setSoTimeout(soTimeout);
    }

    //-----------------------------------------------------------------------------------

    public synchronized void start() {
        pause = false;
        if (started) {
            return;
        }
        started = true;
        new Thread() {
            public void run() {
                receiving();
            }
        }.start();
        if (queueCapacity > 0) {
            new Thread() {
                public void run() {
                    sending();
                }
            }.start();
        }
        if (pingIterval > 0) {
            new Thread() {
                public void run() {
                    pinging();
                }
            }.start();
        }
    }

    public synchronized void pause() {
        pause = true;
    }

    public synchronized void addConnectionListener(ConnectionListener<T> listener) {
        listeners.add(listener);
    }

    public synchronized void removeConnectionListener(ConnectionListener<T> listener) {
        listeners.remove(listener);
    }

    public synchronized boolean sendPing() {
        return sendMessage(ping);
    }

    public synchronized boolean sendMessage(T message) {
        if (queueCapacity == 0) {
            return synchronousSend(message);
        } else {
            return queue.offer(message);
        }
    }

    public synchronized void forceSendMessage(T message) throws InterruptedException {
        if (queueCapacity == 0) {
            synchronousSend(message);
        } else {
            queue.put(message);
        }
    }

    public synchronized void closeConnection() {
        exiting = true;
        sendMessage(exit);
    }

    public String getRemoteAddress() {
        return socket.getRemoteSocketAddress().toString();
    }

    public void setHost(Object host) {
        this.host = host;
    }

    public Object getHost() {
        return host;
    }


    protected boolean synchronousSend(T message) {
        try {
            write(message);
            notifyListeners(EVENT_MESASAGE_SENT, message, null);
            return true;
        } catch (IOException e) {
            notifyListeners(EVENT_SENDING_FAILED, message, e);
            return false;
        }
    }

    protected void connectionClosed() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyListeners(EVENT_DISCONNECTED, null, null);
    }

    protected abstract T read() throws Exception;

    protected abstract void write(T message) throws IOException;

    //-----------------------------------------------------------------------------------

    private void receiving() {
        while (!exiting) {
            waitWhilePaused();
            try {
                // receive message
                T message = read();
                // handle exit/ping
                if (message == exit) {
                    sleep(500);
                    exiting = true;
                    continue;
                }
                if (message == ping) {
                    continue;
                }
                // notify listeners
                notifyListeners(EVENT_MESASAGE_RECEIVED, message, null);
            } catch (Exception e) {
                notifyListeners(EVENT_RECEIVING_FAILED, null, e);
                exiting = true;
            }
        }
        connectionClosed();
    }

    private void sending() {
        while (!exiting) {
            waitWhilePaused();
            try {
                synchronousSend(queue.take());
            } catch (InterruptedException e) {
                // message not taken from the queue
            }
        }
    }

    private void pinging() {
        while (!exiting) {
            sleep(pingIterval);
            sendPing();
        }
    }

    private void notifyListeners(int eventType, T message, Exception ex) {
        synchronized (this) {
            listenersClone = listeners.toArray(listenersClone);
        }
        for (ConnectionListener<T> listener : listenersClone) {
            if (listener == null) {
                break;
            }
            try {
                switch (eventType) {
                    case EVENT_MESASAGE_SENT:
                        listener.messageSent(this, message);
                        break;
                    case EVENT_MESASAGE_RECEIVED:
                        listener.messageReceived(this, message);
                        break;
                    case EVENT_SENDING_FAILED:
                        listener.sendingFailed(this, message, ex);
                        break;
                    case EVENT_RECEIVING_FAILED:
                        listener.receivingFailed(this, ex);
                        break;
                    case EVENT_DISCONNECTED:
                        listener.disconnected(this);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error when handling connection event");
            }
        }
    }

    private void waitWhilePaused() {
        while (pause) {
            sleep(100);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // empty
        }
    }

}
