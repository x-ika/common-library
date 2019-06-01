package com.simplejcode.commons.algo;

public final class FastTimer {

    private FastTimer() {
    }

    //-----------------------------------------------------------------------------------

    private static long[] start, total;

    public static void init(int n) {
        start = new long[n];
        total = new long[n];
    }

    public static void start(int i) {
        start[i] = System.nanoTime();
    }

    public static void end(int i) {
        total[i] += System.nanoTime() - start[i];
    }

    public static long getTotal(int i) {
        return total[i];
    }

    public static void print(int i) {
        System.out.println("Time " + i + ": " + total[i] / 1e9);
    }

    public static void printAll() {
        for (int i = 0; i < total.length; i++) {
            if (total[i] > 0) {
                print(i);
            }
        }
    }

}
