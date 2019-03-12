package com.simplejcode.commons.net.util;

public interface MessageBuilder {

    void setDelimiter(int tokenDelimiter);

    void clear();

    int size();

    void removeLastDelimiter();

    void setLast(int code);

    byte[] getMessage();


    void writeByte(byte x);

    void writeChar(char x);

    void writeInt(long x);

    void writeDouble(double x);

    void writeString(String s);

    void writeHexString(String s);

    void writeObject(Writeable object);


    void writeBytes(byte[] x);

    void writeAscii(char[] x);

    void writeInts(int[] x);

    void writeLongs(long[] x);

    void writeDoubles(double[] x);

    void writeStrings(String[] s);

    void writeHexStrings(String[] s);

    void writeObjects(Writeable[] object);

}
