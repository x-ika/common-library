package com.simplejcode.commons.db.ext;

import com.simplejcode.commons.db.Record;

public interface SerializationCallback {
    TableRow convert(Record record);
}
