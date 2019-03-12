package com.simplejcode.commons.db.ext;

import com.simplejcode.commons.db.Record;

public class SimpleCallback {
    private long id;
    private final String[] colNames;

    public SimpleCallback(String[] colNames) {
        this.colNames = colNames;
    }

    public TableRow convert(Record record) {
        long key = colNames[0] == null ? ++id : Long.parseLong(record.get(colNames[0]).toString());
        TableRow row = new TableRow(key, new String[colNames.length - 1]);
        for (int i = 1; i < colNames.length; i++) {
            row.values[i - 1] = value(record.get(colNames[i]));
        }
        return row;
    }

    public static String value(Object o) {
        return o == null ? "" : o.toString();
    }
}
