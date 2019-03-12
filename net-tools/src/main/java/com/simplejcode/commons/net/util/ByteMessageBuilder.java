package com.simplejcode.commons.net.util;

import java.util.Arrays;

public class ByteMessageBuilder implements MessageBuilder {

    private static final byte[] DIGIT_TENS = {
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
    };

    private static final byte[] DIGIT_ONES = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    private final static byte[] HEX = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };

    private final byte[] data;
    private byte delim;
    private int off;

    public ByteMessageBuilder(int size, int tokenDelimiter) {
        data = new byte[size];
        setDelimiter(tokenDelimiter);
    }


    public void setDelimiter(int tokenDelimiter) {
        delim = (byte) tokenDelimiter;
    }

    public void clear() {
        off = 0;
    }

    public int size() {
        return off;
    }

    public void removeLastDelimiter() {
        off--;
    }

    public void setLast(int code) {
        data[off - 1] = (byte) code;
    }

    public byte[] getMessage() {
        return Arrays.copyOf(data, off - 1);
    }


    public void writeByte(byte x) {
        data[off++] = x;
        data[off++] = delim;
    }

    public void writeChar(char x) {
        data[off++] = (byte) x;
        data[off++] = delim;
    }

    public void writeInt(long x) {
        if (x < 0) {
            data[off++] = '-';
            x = -x;
        }
        long p = 1;
        int i = 0;
        while (++i < 19) {
            if (x < (p *= 10)) break;
        }
        off += i;
        i = off;
        while (x >= 65536) {
            long t = x / 100;
            int r = (int) (x - 100 * t);
            x = t;
            data[--i] = DIGIT_ONES[r];
            data[--i] = DIGIT_TENS[r];
        }
        int x0 = (int) x;
        while (true) {
            int t = 52429 * x0 >>> 19;
            int r = x0 - 10 * t;
            x0 = t;
            data[--i] = DIGIT_ONES[r];
            if (x0 == 0) {
                break;
            }
        }
        data[off++] = delim;
    }

    public void writeDouble(double x) {
        writeString(Double.toString(x));
    }

    public void writeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            data[off++] = (byte) s.charAt(i);
        }
        data[off++] = delim;
    }

    public void writeHexString(String s) {
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            data[off++] = HEX[c >>> 4];
            data[off++] = HEX[c & 15];
        }
        data[off++] = delim;
    }

    public void writeObject(Writeable object) {
        byte old = delim;
        object.write(this);
        data[off] = delim = old;
    }


    public void writeBytes(byte[] x) {
        System.arraycopy(x, 0, data, off, x.length);
        off += x.length;
        data[off++] = delim;
    }

    public void writeAscii(char[] x) {
        for (char c : x) {
            data[off++] = (byte) c;
        }
        data[off++] = delim;
    }

    public void writeInts(int[] x) {
        for (int i : x) {
            writeInt(i);
        }
    }

    public void writeLongs(long[] x) {
        for (long i : x) {
            writeInt(i);
        }
    }

    public void writeDoubles(double[] x) {
        for (double d : x) {
            writeDouble(d);
        }
    }

    public void writeStrings(String[] s) {
        for (String p : s) {
            writeString(p);
        }
    }

    public void writeHexStrings(String[] s) {
        for (String p : s) {
            writeHexString(p);
        }
    }

    public void writeObjects(Writeable[] object) {
        for (Writeable o : object) {
            writeObject(o);
        }
    }

    public String toString() {
        return new String(data, 0, off);
    }

}
