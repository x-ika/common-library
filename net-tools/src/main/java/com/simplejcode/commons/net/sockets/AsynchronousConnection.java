package com.simplejcode.commons.net.sockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class AsynchronousConnection {

    private static final int EVENT_MESSAGE_RECEIVED = 1;
    private static final int EVENT_RECEIVING_FAILED = 2;
    private static final int EVENT_MESSAGE_SENT = 3;
    private static final int EVENT_SENDING_FAILED = 4;
    private static final int EVENT_DISCONNECTED = 5;

    protected Object host;

    protected final byte inEnd, outEnd, pingCode, exitCode;

    protected final AsynchronousSocketChannel socket;

    private final List<AsynchronousConnectionListener> listeners;

    protected final ReentrantLock readLock, writeLock;
    protected final ByteBuffer readBuffer, writeBuffer;
    protected final CompletionHandler<Integer, Void> readHandler;
    protected final CompletionHandler<Integer, byte[]> writeHandler;

    protected final byte[] pingMessage, exitMessage;

    protected boolean reading, writting, closed;

    protected final byte[] readQueue;
    protected int index;
    protected final Queue<byte[]> writeQueue;

    public AsynchronousConnection(AsynchronousSocketChannel socket, int bufferCapacity, int maxMessageSize,
                                  int pingCode, int exitCode, int inEnd, int outEnd)
    {
        this.pingCode = (byte) pingCode;
        this.exitCode = (byte) exitCode;
        this.inEnd = (byte) inEnd;
        this.outEnd = (byte) outEnd;

        this.socket = socket;

        listeners = new Vector<>();

        readLock = new ReentrantLock();
        writeLock = new ReentrantLock();
        readBuffer = ByteBuffer.allocateDirect(bufferCapacity);
        writeBuffer = ByteBuffer.allocateDirect(bufferCapacity);

        readHandler = new CompletionHandler<>() {
            public void completed(Integer result, Void attachment) {
                readCompleted(result);
            }

            public void failed(Throwable exc, Void attachment) {
                readFailed(exc);
            }
        };
        writeHandler = new CompletionHandler<>() {
            public void completed(Integer result, byte[] attachment) {
                writeCompleted(result, attachment);
            }

            public void failed(Throwable exc, byte[] attachment) {
                writeFailed(exc, attachment);
            }
        };

        pingMessage = new byte[2];
        pingMessage[0] = (byte) pingCode;
        pingMessage[1] = (byte) outEnd;
        exitMessage = new byte[2];
        exitMessage[0] = (byte) exitCode;
        exitMessage[1] = (byte) outEnd;

        readQueue = new byte[maxMessageSize + bufferCapacity];
        writeQueue = new ConcurrentLinkedQueue<>();
    }

    //-----------------------------------------------------------------------------------

    AsynchronousSocketChannel getSocket() {
        return socket;
    }

    public void addConnectionListener(AsynchronousConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeConnectionListener(AsynchronousConnectionListener listener) {
        listeners.remove(listener);
    }

    public String getRemoteAddress() {
        try {
            return socket.getRemoteAddress().toString();
        } catch (IOException e) {
            return null;
        }
    }

    public void setHost(Object host) {
        this.host = host;
    }

    public Object getHost() {
        return host;
    }

    public byte[] read() {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void write(byte[] message) {
        writeQueue.offer(message);
        tryWrite();
    }

    public void tryRead(long timeout) {
        if (!readLock.tryLock()) {
            return;
        }
        if (!closed && !reading) {
            reading = true;
            try {
                socket.read(readBuffer, timeout, TimeUnit.MILLISECONDS, null, readHandler);
            } catch (ShutdownChannelGroupException e) {
                e.printStackTrace();
            }
        }
        readLock.unlock();
    }

    public void tryWrite() {
        if (!writeLock.tryLock()) {
            return;
        }
        if (!closed && !writting && !writeQueue.isEmpty()) {
            writting = true;
            writeBuffer.clear();
            byte[] message = writeQueue.poll();
            writeBuffer.put(message);
            if (message[message.length - 1] != outEnd) {
                writeBuffer.put(outEnd);
            }
            writeBuffer.flip();
            socket.write(writeBuffer, message, writeHandler);
        }
        writeLock.unlock();
    }

    public void ping() {
        write(pingMessage);
    }

    public void exit() {
        write(exitMessage);
    }

    public void closeConnection() {
        readLock.lock();
        writeLock.lock();
        if (!closed) {
            closed = true;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeLock.unlock();
        readLock.unlock();
        notifyListeners(EVENT_DISCONNECTED, null, null);
    }

    //-----------------------------------------------------------------------------------

    private void readCompleted(Integer result) {
        if (result == -1) {
            closeConnection();
            return;
        }
        readLock.lock();
        readBuffer.flip();
        byte[] queue = readQueue;
        readBuffer.get(queue, index, result);
        int end = index + result;
        for (int i = 0; i < result; i++) {
            if (queue[index++] == inEnd) {
                handleMessageReceive(queue, index - 1);
                System.arraycopy(queue, index, queue, 0, end - index);
                index = 0;
            }
        }
        readBuffer.clear();
        reading = false;
        readLock.unlock();
    }

    private void handleMessageReceive(byte[] queue, int index) {
        if (index == 1) {
            if (queue[0] == pingCode) {
                return;
            } else if (queue[0] == exitCode) {
                closeConnection();
                return;
            }
        }
        notifyListeners(EVENT_MESSAGE_RECEIVED, Arrays.copyOf(readQueue, index), null);
    }

    private void readFailed(Throwable e) {
        readLock.lock();
        reading = false;
        readLock.unlock();
        if (e instanceof IOException) {
            closeConnection();
            return;
        }
        notifyListeners(EVENT_RECEIVING_FAILED, null, e);
    }

    private void writeCompleted(Integer result, byte[] message) {
        writeLock.lock();
        writting = false;
        writeLock.unlock();
        notifyListeners(EVENT_MESSAGE_SENT, message, null);
    }

    private void writeFailed(Throwable e, byte[] message) {
        writeLock.lock();
        writting = false;
        writeLock.unlock();
        notifyListeners(EVENT_SENDING_FAILED, message, e);
    }

    private void notifyListeners(int eventType, byte[] message, Throwable ex) {
        AsynchronousConnectionListener[] array = listeners.toArray(new AsynchronousConnectionListener[0]);
        for (AsynchronousConnectionListener listener : array) {
            try {
                switch (eventType) {
                    case EVENT_MESSAGE_RECEIVED:
                        listener.messageReceived(this, message);
                        break;
                    case EVENT_RECEIVING_FAILED:
                        listener.receivingFailed(this, ex);
                        break;
                    case EVENT_MESSAGE_SENT:
                        listener.messageSent(this, message);
                        break;
                    case EVENT_SENDING_FAILED:
                        listener.sendingFailed(this, message, ex);
                        break;
                    case EVENT_DISCONNECTED:
                        listener.disconnected(this);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
