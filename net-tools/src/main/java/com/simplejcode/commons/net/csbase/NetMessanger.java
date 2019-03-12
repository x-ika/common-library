package com.simplejcode.commons.net.csbase;

import com.simplejcode.commons.net.sockets.SocketConnection;

import java.io.*;
import java.net.Socket;

public class NetMessanger extends SocketConnection<Message> {

    private static final int PING = 1;
    private static final int EXIT = 2;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public NetMessanger(Socket socket, int queueCapacity, int pingInterval) throws IOException {
        super(socket, 0, queueCapacity, pingInterval, new Message(null, PING), new Message(null, EXIT));
    }

    protected Message read() throws Exception {
        if (ois == null) {
            ois = new ObjectInputStream(socket.getInputStream());
        }
        Message msg = (Message) ois.readObject();
        return msg.getType() == PING ? ping : msg.getType() == EXIT ? exit : msg;
    }

    protected void write(Message message) throws IOException {
        if (oos == null) {
            oos = new ObjectOutputStream(outputStream);
        }
        oos.writeObject(message);
        oos.reset();
    }

    protected void connectionClosed() {
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.connectionClosed();
    }

}
