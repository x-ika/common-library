package com.simplejcode.commons.misc.util;

import java.io.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class DynamicMap<K> {

    private boolean keepSmaller;

    private String fileName;

    private Map<K, Double> map = new HashMap<>();

    public DynamicMap(boolean keepSmaller, String fileName) {
        this.keepSmaller = keepSmaller;
        this.fileName = fileName;
        try {
            Object o = new ObjectInputStream(new FileInputStream(fileName)).readObject();
            if (o instanceof Map) {
                map.putAll((Map) o);
            }
        } catch (Exception e) {
        }
    }

    public void close() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(map);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Double get(K key) {
        Double value = map.get(key);
        return value != null ? value : keepSmaller ? Double.MAX_VALUE : 0;
    }

    public boolean update(K key, Double value) {
        Double prevValue = map.get(key);
        if (prevValue == null || keepSmaller == value.compareTo(prevValue) < 0) {
            map.put(key, value);
            return true;
        }
        return false;
    }

}
