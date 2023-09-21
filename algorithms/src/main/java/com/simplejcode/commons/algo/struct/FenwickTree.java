package com.simplejcode.commons.algo.struct;

public class FenwickTree {

    private final long[] t;


    public FenwickTree(int n) {
        t = new long[n];
    }


    // Adds value to the i-th element
    public void add(int i, long value) {
        for (; i < t.length; i += (i + 1) & -(i + 1)) t[i] += value;
    }

    // Returns sum of elements in range [0, i]
    public long sum(int i) {
        long res = 0;
        for (; i >= 0; i -= (i + 1) & -(i + 1)) res += t[i];
        return res;
    }

    // Returns sum of elements in range [a, b]
    public long sum(int a, int b) {
        return sum(b) - sum(a - 1);
    }

    public long get(int i) {
        return sum(i) - sum(i - 1);
    }

    // Returns the largest b such that sum(0,b) >= sum and has minimal value
    public int search(int sum) {
        int highestOneBit = Integer.highestOneBit(t.length);
        int pos = -1;
        for (int blockSize = highestOneBit; blockSize != 0; blockSize >>= 1) {
            int nextPos = pos + blockSize;
            if (nextPos < t.length && sum >= t[nextPos]) {
                pos = nextPos;
                sum -= t[nextPos];
            }
        }
        return pos;
    }

    // number of free places in [0, x]
    public int getFree(int x) {
        return (x < 0) ? 0 : x + 1 - (int) sum(x);
    }

    // number of free places in [x1, x2], cyclically
    public long getFree(int x1, int x2) {
        long s = getFree(x2) - getFree(x1 - 1);
        return (x1 <= x2) ? s : s + getFree(t.length - 1);
    }

    public int getFreeKthElement(int x1, int k) {
        int totFree = getFree(t.length - 1);
        k %= totFree;
        if (k == 0) {
            k = totFree;
        }
        int lo = 0;
        int hi = t.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            long midVal = getFree(x1, (x1 + mid) % t.length);
            if (midVal < k) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return (x1 + lo) % t.length;
    }

}
