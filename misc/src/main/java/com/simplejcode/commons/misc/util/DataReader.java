package com.simplejcode.commons.misc.util;

import java.io.*;
import java.math.BigInteger;
import java.util.StringTokenizer;

@SuppressWarnings({"EmptyCatchBlock"})
public class DataReader {

    private BufferedReader in;

    private StringTokenizer tokenizer = new StringTokenizer("");


    public DataReader(String fileName) {
        try {
            in = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
        }
    }

    public DataReader(InputStream stream) {
        in = new BufferedReader(new InputStreamReader(stream));
    }


    public String nextLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public String nextToken() {
        while (!tokenizer.hasMoreTokens()) {
            tokenizer = new StringTokenizer(nextLine());
        }
        return tokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(nextToken());
    }

    public long nextLong() {
        return Long.parseLong(nextToken());
    }

    public double nextDouble() {
        return Double.parseDouble(nextToken());
    }

    public BigInteger nextBigInteger() {
        return new BigInteger(nextToken());
    }

    public void close() {
        try {
            in.close();
        } catch (Exception e) {
        }
    }

}
