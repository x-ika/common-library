package com.simplejcode.commons.net.util;

public class ByteMessageParser implements MessageParser {

    private final static byte[] HEX = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };

    private static final int[] HEX1 = new int[256];

    static {
        for (int i = 0; i < HEX.length; i++) {
            HEX1[HEX[i]] = i;
        }
    }

    private byte[] data;
    private byte delim;
    private int off;

    public ByteMessageParser(byte[] data, int tokenDelimiter) {
        setData(data);
        setDelimiter(tokenDelimiter);
    }

    public ByteMessageParser(byte[] data) {
        this(data, 0);
    }

    public ByteMessageParser(int tokenDelimiter) {
        this(null, tokenDelimiter);
    }


    public void setDelimiter(int tokenDelimiter) {
        delim = (byte) tokenDelimiter;
    }

    public void setData(byte[] data) {
        this.data = data;
        off = 0;
    }

    public int nextInt() throws ParsingException {
        return (int) nextLong();
    }

    public long nextLong() throws ParsingException {
        check();
        int next = next();
        if (next == off) {
            throw new ParsingException("Parsing error at index " + off + ". Excpected digit or '-' got " + (char) data[off]);
        }
        int sign = 1;
        if (data[off] == '-') {
            sign = -1;
            off++;
        }
        long val = 0;
        for (int i = off; i < next; i++) {
            byte b = data[i];
            if (b < '0' || '9' < b) {
                throw new ParsingException("Parsing error at index " + i + ". Excpected digit got " + (char) b + " (" + b + ")");
            }
            val *= 10;
            val += b - '0';
        }
        off = next + 1;
        return sign * val;
    }

    public String nextString() throws ParsingException {
        check();
        int length = next() - off;
        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            buf[i] = (char) data[off++];
        }
        off++;
        return new String(buf);
    }

    public String nextHexString() throws ParsingException {
        int length = next() - off >> 1;
        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            byte x = data[off++], y = data[off++];
            buf[i] = (char) (HEX1[x] << 4 | HEX1[y]);
        }
        off++;
        return new String(buf);
    }

    public int[] nextInts() throws ParsingException {
        throw new RuntimeException("Not Implemented");
    }

    public String[] nextStrings() throws ParsingException {
        throw new RuntimeException("Not Implemented");
    }

    public void verifyAndClose() throws ParsingException {
        if (off < data.length) {
            throw new ParsingException("When closing reader...");
        }
        data = null;
    }


    private void check() throws ParsingException {
        if (off >= data.length) {
            throw new ParsingException("Not enough data");
        }
    }

    private int next() throws ParsingException {
        byte[] a = data;
        for (int x = off; ; x++) {
            if (x == a.length || a[x] == delim) {
                return x;
            }
        }
    }

}
