package com.simplejcode.commons.misc;

import java.util.Arrays;

@SuppressWarnings({"StatementWithEmptyBody"})
public final class FastBucketHeap {

    private FastBucketHeap() {
    }

    //-----------------------------------------------------------------------------------

    private static final int MAGNIFIER = 64;

    private static int msk, ind, size, hz[], heap[][];

    public static void init(int n, int maxd) {
        int blocks = 1;
        while ((blocks <<= 1) <= maxd) ;
        blocks *= MAGNIFIER;
        msk = blocks - 1;
        hz = new int[blocks];
        heap = new int[blocks][n + 1];
    }

    public static void clear() {
        size = ind = 0;
        Arrays.fill(hz, 0);
    }

    public static int size() {
        return size;
    }

    public static int poll(double[] p) {
        while (hz[ind] == 0) {
            ind++;
            ind &= msk;
        }
        int bst = -1;
        double min = 1e9;
        int[] t = heap[ind];
        for (int i = 0; i < hz[ind]; i++) {
            double cur = p[t[i]];
            if (min > cur) {
                min = cur;
                bst = i;
            }
        }
        int ret = t[bst];
        remove0(ind, bst);
        return ret;
    }

    public static void add(int x, double[] p) {
        int ind = (int) (MAGNIFIER * p[x]) & msk;
        heap[ind][hz[ind]++] = x;
        size++;
    }

    public static void remove(int x, double[] p) {
        int ind = (int) (MAGNIFIER * p[x]) & msk;
        int[] t = heap[ind];
        for (int i = 0; i < hz[ind]; i++) {
            if (t[i] == x) {
                remove0(ind, i);
                return;
            }
        }
    }

    private static void remove0(int ind, int i) {
        hz[ind]--;
        size--;
        int[] t = heap[ind];
        for (int j = i; j < hz[ind]; j++) {
            t[j] = t[j + 1];
        }
    }

}
