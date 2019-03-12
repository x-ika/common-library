package com.simplejcode.commons.misc;

public final class FastRandom {

    private FastRandom() {
    }

    //-----------------------------------------------------------------------------------

    private static final long mult = 0x5DEECE66DL;
    private static final long add = 0xB;
    private static final long msk = (1L << 48) - 1;

    private static long seed;

    public static void init(long x) {
        seed = (x ^ mult) & msk;
    }

    public static int next(int bits) {
        seed = (seed * mult + add) & msk;
        return (int) (seed >> (48 - bits));
    }

    public static int nextInt(int n) {
        int a, b;
        do {
            a = next(31);
            b = a % n;
        } while (a - b + (n - 1) < 0);
        return b;
    }

    public static double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) / (double) (1L << 53);
    }

    public static int randomElement(int[] a) {
        return randomElement(a, a.length);
    }

    public static int randomElement(int[] a, int n) {
        return a[nextInt(n)];
    }

}
