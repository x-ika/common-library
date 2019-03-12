package com.simplejcode.commons.net.sockets;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class FastConnection extends SocketConnection<byte[]> {

    private final int inEnd, outEnd, pingCode, exitCode;

    /**
     * Creates new instance
     *
     * @param socket        original socket to be used
     * @param queueCapacity capacity of the send queue
     * @param pingInterval  interval between consecutive pings
     * @param ping          ping code
     * @param exit          exit code
     * @param inEnd         indicator of the input end
     * @param outEnd        indicator of the output end
     * @throws IOException when sopcket output stream can not be retrieved
     */
    public FastConnection(Socket socket, int queueCapacity, int pingInterval, int ping, int exit, int inEnd, int outEnd) throws IOException {
        super(socket, 300 * 1000, queueCapacity, pingInterval, new byte[]{(byte) ping, (byte) outEnd}, new byte[]{(byte) exit, (byte) outEnd});
        this.pingCode = ping;
        this.exitCode = exit;
        this.inEnd = inEnd;
        this.outEnd = outEnd;
    }

    public FastConnection(Socket socket, int queueCapacity, int pingInterval, int ping, int exit) throws IOException {
        this(socket, queueCapacity, pingInterval, ping, exit, 0, 0);
    }

    protected byte[] read() throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] message = new byte[16];
        int x, off = 0;
        while ((x = inputStream.read()) != -1) {
            if (off == message.length) {
                message = Arrays.copyOf(message, 2 * message.length);
            }
            if ((message[off++] = (byte) x) == inEnd) {
                break;
            }
        }
        if (off < 1 || message[off - 1] != inEnd) {
            throw new IOException("Invalid message received");
        }
        if (off == 2 && message[0] == pingCode) {
            return ping;
        }
        if (off == 2 && message[0] == exitCode) {
            return exit;
        }
        return Arrays.copyOf(message, off - 1);
    }

    protected void write(byte[] message) throws IOException {
        outputStream.write(message);
        if (message[message.length - 1] != outEnd) {
            outputStream.write(outEnd);
        }
        outputStream.flush();
    }

}
