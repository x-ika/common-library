package com.simplejcode.commons.algo;

import java.util.Arrays;

@SuppressWarnings({"StatementWithEmptyBody"})
public final class FastHeapG {

    private FastHeapG() {
    }

    //-----------------------------------------------------------------------------------

    private static final int MAGNIFIER = 16;

    private static int msk, ind, size, hid[], hz[], heap[][];

    public static void init(int n, int maxd) {
        int blocks = 1;
        while ((blocks <<= 1) <= maxd) ;
        blocks *= MAGNIFIER;
        msk = blocks - 1;
        hid = new int[n + 1];
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
        size--;
        while (hz[ind] == 0) {
            ind++;
            ind &= msk;
        }
        return poll(heap[ind], hid, hz[ind]--, p);
    }

    public static void add(int x, double[] p) {
        size++;
        int ind = (int) (MAGNIFIER * p[x]) & msk;
        add(heap[ind], hid, hz[ind]++, x, p);
    }

    public static void remove(int x, double[] p) {
        size--;
        int ind = (int) (MAGNIFIER * p[x]) & msk;
        remove(heap[ind], hid, hz[ind]--, x, p);
    }

    //-----------------------------------------------------------------------------------

    private static int poll(int[] t, int[] id, int z, double[] p) {
        int ret = t[0];
        remove0(t, id, z, 0, p);
        return ret;
    }

    private static void add(int[] t, int[] id, int z, int x, double[] p) {
        id[t[z] = x] = z;
        heapifyUp(t, id, z, z, p);
    }

    private static void remove(int[] t, int[] id, int z, int x, double[] p) {
        remove0(t, id, z, id[x], p);
    }

    private static void remove0(int[] t, int[] id, int z, int i, double[] p) {
        int x = t[i] = t[--z];
        if (i == z) {
            return;
        }
        id[x] = i;
        if (i != 0) {
            double tmp = p[x];
            p[x] = -1;
            heapifyUp(t, id, z, i, p);
            p[x] = tmp;
        }
        heapifyDown(t, id, z, 0, p);
    }

    private static void heapifyUp(int[] t, int[] id, int z, int i, double[] p) {
        int th = t[i], j;
        double tp = p[th];
        while (i > 0 && tp < p[t[j = i - 1 >> 1]]) {
            id[t[i] = t[j]] = i;
            i = j;
        }
        t[i] = th;
        id[th] = i;
    }

    private static void heapifyDown(int[] t, int[] id, int z, int i, double[] p) {
        int th = t[i], j, a, b;
        double tp = p[th];
        while ((a = 2 * i + 1) < z && tp > p[t[j = (b = a + 1) == z || p[t[a]] < p[t[b]] ? a : b]]) {
            id[t[i] = t[j]] = i;
            i = j;
        }
        t[i] = th;
        id[th] = i;
    }

}
