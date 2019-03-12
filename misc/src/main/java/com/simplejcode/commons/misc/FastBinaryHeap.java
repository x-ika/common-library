package com.simplejcode.commons.misc;

public final class FastBinaryHeap {

    private FastBinaryHeap() {
    }

    //-----------------------------------------------------------------------------------

    private static int size, heap[], ind[];

    public static void init(int n) {
        heap = new int[n + 1];
        ind = new int[n + 1];
    }

    public static int size() {
        return size;
    }

    public static void clear() {
        size = 0;
    }

    public static int poll(double[] p) {
        int ret = heap[0];
        remove0(0, p);
        return ret;
    }

    public static void add(int x, double[] p) {
        ind[heap[size] = x] = size;
        heapifyUp(++size - 1, p);
    }

    public static void remove(int x, double[] p) {
        remove0(ind[x], p);
    }

    private static void remove0(int i, double[] p) {
        int x = heap[i] = heap[--size];
        if (i == size) {
            return;
        }
        ind[x] = i;
        if (i != 0) {
            double tmp = p[x];
            p[x] = -1;
            heapifyUp(i, p);
            p[x] = tmp;
        }
        heapifyDown(0, p);
    }

    private static void heapifyUp(int i, double[] p) {
        int th = heap[i], j;
        double tp = p[th];
        while (i > 0 && tp < p[heap[j = i - 1 >> 1]]) {
            ind[heap[i] = heap[j]] = i;
            i = j;
        }
        heap[i] = th;
        ind[th] = i;
    }

    private static void heapifyDown(int i, double[] p) {
        int th = heap[i], j, a, b;
        double tp = p[th];
        while ((a = 2 * i + 1) < size && tp > p[heap[j = (b = a + 1) == size || p[heap[a]] < p[heap[b]] ? a : b]]) {
            ind[heap[i] = heap[j]] = i;
            i = j;
        }
        heap[i] = th;
        ind[th] = i;
    }

}
