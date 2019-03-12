package com.simplejcode.commons.net.csbase;

import java.util.*;

public class MapMessage extends Message {

    private static final long serialVersionUID = 6546456465587978923L;

    private Map<String, Object> map = new HashMap<>();

    public MapMessage(Object object) {
        super(object);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object get(String... keys) {
        Object t = this;
        for (String key : keys) {
            t = ((MapMessage) t).get(key);
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

    public MapMessage getRecord(String key) {
        return (MapMessage) get(key);
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

    public MapMessage getRecord(String... keys) {
        return (MapMessage) get(keys);
    }


    public int[] getInts(String... keys) {
        return (int[]) get(keys);
    }

    public String[] getStrings(String... keys) {
        return (String[]) get(keys);
    }

    //-----------------------------------------------------------------------------------

    public Map<String, Object> getMap() {
        return map;
    }

}
