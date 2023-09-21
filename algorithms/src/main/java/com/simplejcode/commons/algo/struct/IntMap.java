package com.simplejcode.commons.algo.struct;

import java.util.Arrays;

public class IntMap {

    private final static class Entry {
        int key;
        int value;
        Entry next;

        Entry(int key, int value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    private int size;
    private final int[] count;
    private final Entry[] data;


    public IntMap(int size) {
        data = new Entry[size];
        count = new int[size];
    }


    public void add(int i, int k) {
        int ind = i & data.length - 1;
        count[ind] += k;
        for (Entry entry = data[ind]; entry != null; entry = entry.next) {
            if (entry.key == i) {
                entry.value += k;
                return;
            }
        }
        data[ind] = new Entry(i, k, data[ind]);
        size++;
    }

    public void sub(int i, int k) {
        int ind = i & data.length - 1;
        count[ind] -= k;
        Entry prev = null;
        for (Entry entry = data[ind]; entry != null; prev = entry, entry = entry.next) {
            if (entry.key == i) {
                if (entry.value == k) {
                    if (prev == null) {
                        data[ind] = entry.next;
                    } else {
                        prev.next = entry.next;
                    }
                    size--;
                } else {
                    entry.value -= k;
                }
                return;
            }
        }
    }

    public int find(int k) {
        for (int i = 0; i < data.length; i++) {
            if (count[i] > k) {
                for (Entry entry = data[i]; entry != null; entry = entry.next) {
                    if (entry.value > k) {
                        return entry.key;
                    }
                }
            }
        }
        return -1;
    }

    public void put(int i, int e) {
        int ind = i & data.length - 1;
        count[ind] += e;
        data[ind] = new Entry(i, e, data[ind]);
    }

    public int get(int i) {
        int ind = i & data.length - 1;
        for (Entry entry = data[ind]; entry != null; entry = entry.next) {
            if (entry.key == i) {
                return entry.value;
            }
        }
        return -1;
    }

    public void clear() {
        size = 0;
        Arrays.fill(data, null);
        Arrays.fill(count, 0);
    }

    public int sortedKeySet(int[] set) {
        for (int i = 0, z = 0; i < data.length; i++) {
            for (Entry entry = data[i]; entry != null; entry = entry.next) {
                set[z++] = entry.key;
            }
        }
        Arrays.sort(set, 0, size);
        return size;
    }

}
