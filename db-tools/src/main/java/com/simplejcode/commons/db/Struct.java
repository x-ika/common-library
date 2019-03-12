package com.simplejcode.commons.db;

import java.io.Serializable;
import java.util.*;

public class Struct implements Serializable {

    private static class Item implements Serializable {
        String key;
        Object value;
        Struct.Item next;

        public Item() {
        }

        public Item(String key, Object value, Struct.Item next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private final int mask;
    private Struct.Item[] data;

    public Struct(int size) {
        data = new Struct.Item[Integer.highestOneBit(size)];
        mask = data.length - 1;
    }

    public Struct() {
        this(16);
    }

    public void put(String key, Object value) {
        int ind = key.hashCode() & mask;
        data[ind] = new Struct.Item(key, value, data[ind]);
    }

    public void remove(String key) {
        int ind = key.hashCode() & mask;
        for (Struct.Item t = data[ind], prev = t; t != null; prev = t, t = t.next) {
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
        for (Struct.Item t = data[ind]; t != null; t = t.next) {
            if (t.key.equals(key)) {
                return t.value;
            }
        }
        return null;
    }

    public Object get(String... keys) {
        Object t = this;
        for (String key : keys) {
            t = ((Struct) t).get(key);
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

    public Struct getRecord(String key) {
        return (Struct) get(key);
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

    public Struct getRecord(String... keys) {
        return (Struct) get(keys);
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
        for (Struct.Item item : data) {
            for (Struct.Item t = item; t != null; t = t.next) {
                map.put(t.key, t.value);
            }
        }
        return map;
    }

}
