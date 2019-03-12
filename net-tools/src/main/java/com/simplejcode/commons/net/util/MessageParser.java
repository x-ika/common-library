package com.simplejcode.commons.net.util;

public interface MessageParser {

    void setDelimiter(int tokenDelimiter);

    void setData(byte[] data);

    int nextInt() throws ParsingException;

    long nextLong() throws ParsingException;

    String nextString() throws ParsingException;

    String nextHexString() throws ParsingException;

    int[] nextInts() throws ParsingException;

    String[] nextStrings() throws ParsingException;

    void verifyAndClose() throws ParsingException;

}
