package com.simplejcode.commons.db.ext;

import com.simplejcode.commons.db.Record;

import java.util.*;

@SuppressWarnings({"unchecked"})
public class DBStruct<Key> {

    public final int id;
    private Record record;
    private final Map<Key, String> settings;

    public DBStruct(Record record, int id) {
        setRecord(record);
        this.id = id;
        settings = new HashMap<>();
    }

    public DBStruct(Record record, String cid) {
        this(record, Integer.parseInt(record.get(cid).toString()));
    }

    public DBStruct(Record record) {
        this(record, 0);
    }


    public void setRecord(Record record) {
        this.record = record;
    }

    public Object get(String key) {
        return record.get(key);
    }

    public void put(String key, Object value) {
        record.put(key, value);
    }

    public String getString(String key) {
        return record.getString(key);
    }


    public String getSetting(Key key) {
        return settings.get(key);
    }

    public void setSetting(Key key, String value) {
        settings.put(key, value);
    }

    public int parseInt(Key key) {
        return Integer.parseInt(getSetting(key));
    }

    public long parseLong(Key key) {
        return Long.parseLong(getSetting(key));
    }

    public double parseDouble(Key key) {
        return Double.parseDouble(getSetting(key));
    }

    public int[] parseInts(Key key, String delim) {
        String[] s = getSetting(key).split(delim);
        int[] a = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            a[i] = Integer.parseInt(s[i]);
        }
        return a;
    }

    public long[] parseLongs(Key key, String delim) {
        String[] s = getSetting(key).split(delim);
        long[] a = new long[s.length];
        for (int i = 0; i < s.length; i++) {
            a[i] = Long.parseLong(s[i]);
        }
        return a;
    }

    public double[] parseDoubles(Key key, String delim) {
        String[] s = getSetting(key).split(delim);
        double[] a = new double[s.length];
        for (int i = 0; i < s.length; i++) {
            a[i] = Double.parseDouble(s[i]);
        }
        return a;
    }


    public void loadSettings(List<Record> list, String key, String value) {
        for (Record rec : list) {
            setSetting((Key) rec.get(key), rec.getString(value));
        }
    }

    public Map<Key, String> getSettings() {
        return settings;
    }

}
