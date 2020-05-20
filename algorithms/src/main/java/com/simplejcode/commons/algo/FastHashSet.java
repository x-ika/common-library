package com.simplejcode.commons.algo;

import java.util.Arrays;

public class FastHashSet {

    private int size, msk;
    private int[] count;
    private int[][] data;

    public FastHashSet(int numChunks, int chunkSize) {
        msk = numChunks - 1;
        count = new int[numChunks];
        data = new int[numChunks][chunkSize];
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        size = 0;
        Arrays.fill(count, 0);
    }

    public void add(int x) {
        int ind = hash(x) & msk;
        size++;
        int[] c = count;
        int[][] d = data;
        int z = c[ind];
        int[] t = d[ind];
        if (z == t.length) {
            t = d[ind] = Arrays.copyOf(t, z << 1);
        }
        t[c[ind]++] = x;
    }

    public void remove(int x) {
        int ind = hash(x) & msk;
        size--;
        int[] c = count;
        int[][] d = data;
        int z = c[ind];
        int[] t = d[ind];
        for (int i = 0; i < z; i++) {
            if (t[i] == x) {
                System.arraycopy(t, i + 1, t, i, --c[ind] - i);
            }
        }
    }

    private static int hash(int x) {
        x = (x ^ 0xdeadbeef) + (x << 4);
        x = x ^ (x >> 10);
        x = x + (x << 7);
        x = x ^ (x >> 13);
        return x;
    }

}
