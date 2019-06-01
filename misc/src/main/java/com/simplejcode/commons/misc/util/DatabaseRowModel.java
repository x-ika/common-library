package com.simplejcode.commons.misc.util;

import java.io.Serializable;
import java.util.*;

public class DatabaseRowModel implements Serializable {

    private static class Item implements Serializable {
        String key;
        Object value;
        Item next;

        public Item() {
        }

        public Item(String key, Object value, Item next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private final int mask;
    private Item[] data;

    public DatabaseRowModel(int size) {
        data = new Item[Integer.highestOneBit(size)];
        mask = data.length - 1;
    }

    public DatabaseRowModel() {
        this(16);
    }

    public void put(String key, Object value) {
        int ind = key.hashCode() & mask;
        data[ind] = new Item(key, value, data[ind]);
    }

    public void remove(String key) {
        int ind = key.hashCode() & mask;
        for (Item t = data[ind], prev = t; t != null; prev = t, t = t.next) {
            if (t.key.equals(key)) {
                if (prev == t) {
                    data[ind] = t.next;
                } else {
                    prev.next = t.next;
                }
                return;
            }
        }
    }

    public Object get(String key) {
        int ind = key.hashCode() & mask;
        for (Item t = data[ind]; t != null; t = t.next) {
            if (t.key.equals(key)) {
                return t.value;
            }
        }
        return null;
    }

    public Object get(String... keys) {
        Object t = this;
        for (String key : keys) {
            t = ((DatabaseRowModel) t).get(key);
            if (t == null) {
                return null;
            }
        }
        return t;
    }

    //-----------------------------------------------------------------------------------

    public int getInt(String key) {
        return (Integer) get(key);
    }

    public long getLong(String key) {
        return (Long) get(key);
    }

    public double getDouble(String key) {
        return (Double) get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public DatabaseRowModel getRecord(String key) {
        return (DatabaseRowModel) get(key);
    }


    public int[] getInts(String key) {
        return (int[]) get(key);
    }

    public long[] getLongs(String key) {
        return (long[]) get(key);
    }

    public double[] getDoubles(String key) {
        return (double[]) get(key);
    }

    public String[] getStrings(String key) {
        return (String[]) get(key);
    }


    public int getInt(String... keys) {
        return (Integer) get(keys);
    }

    public long getLong(String... keys) {
        return (Long) get(keys);
    }

    public double getDouble(String... keys) {
        return (Double) get(keys);
    }

    public String getString(String... keys) {
        return (String) get(keys);
    }

    public DatabaseRowModel getRecord(String... keys) {
        return (DatabaseRowModel) get(keys);
    }


    public int[] getInts(String... keys) {
        return (int[]) get(keys);
    }

    public long[] getLongs(String... keys) {
        return (long[]) get(keys);
    }

    public double[] getDoubles(String... keys) {
        return (double[]) get(keys);
    }

    public String[] getStrings(String... keys) {
        return (String[]) get(keys);
    }

    //-----------------------------------------------------------------------------------

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        for (Item item : data) {
            for (Item t = item; t != null; t = t.next) {
                map.put(t.key, t.value);
            }
        }
        return map;
    }

}
