package com.simplejcode.commons.db;

public final class StatementPreparation {

    public static final int TYPE_SELECT = 1;
    public static final int TYPE_INSERT = 2;
    public static final int TYPE_UPDATE = 3;
    public static final int TYPE_SPCALL = 4;

    final int type, id, upd;
    final String tableName;
    final String[] colNames;

    public StatementPreparation(int type, int id, String tableName, int upd, String[] colNames) {
        this.type = type;
        this.id = id;
        this.tableName = tableName;
        this.upd = upd;
        this.colNames = colNames;
    }

    public StatementPreparation(int type, int id, String tableName, String[] colNames) {
        this(type, id, tableName, 0, colNames);
    }

}
