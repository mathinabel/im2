package com.quyuanjin.im2.netty.xutilsDB.sqlite;

public class ShareData {

    private static String table="defaultTable",tableFromInsert;
    //private Context context;
    public ShareData(){
        //this.context=context;
    }
    public static void setTable(String table) {
        tableFromInsert=table;
    }
    public static String getTable() {
        return tableFromInsert;
    }
    public void set(String tableName) {
        table=tableName;
    }
    public String get() {
        return table;
    }

}
