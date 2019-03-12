package com.simplejcode.commons.db.ext;

import java.io.Serializable;

public class TableRow implements Serializable {

    public long id;
    public String[] values;

    public TableRow() {
    }

    public TableRow(long id, String[] values) {
        this.id = id;
        this.values = values;
    }

}
