package com.simplejcode.commons.algo;

public final class FastTimer {

    private FastTimer() {
    }

    //-----------------------------------------------------------------------------------

    private static String[] prefix;
    private static long[] start, total;

    public static void init(int n, String... s) {
        prefix = s;
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

    public static void printAll() {
        for (int i = 0; i < total.length; i++) {
            if (total[i] > 0) {
                print(i);
            }
        }
    }

    public static void print(int i) {
        System.out.printf("%11s Time(%d): %.3f\n", prefix[i], i, total[i] / 1e9);
    }

}
