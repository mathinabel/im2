package com.quyuanjin.im2.netty.xutilsDB.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "citys";


    //从ShareData中取得想要新建的表名
    ShareData sData = new ShareData();
    //建表语句
    private Context context;

    /**
     * 构造方法
     * (Context context, String name, CursorFactory factory,int version)
     * 数据库创建的构造方法  数据库名称  sql_table.db ，版本号为1
     *
     * @param context 上下文对象
     * @param name    数据库名称 secb.db
     * @param factory 游标工厂
     * @param version 数据库版本
     */
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        if (!sData.get().isEmpty()) {
            String CREATE_BOOK = "create table " + sData.get() + "(" + "id integer primary key autoincrement,"
                    + "fromuuid text," + "touuid text," + "msg text)";
            sqLiteDatabase.execSQL(CREATE_BOOK);
            Log.i("建表界面", "表名是" + sData.get());
            Toast.makeText(context, "建表成功" + sData.get(), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "获取表名失败", Toast.LENGTH_SHORT).show();


    }

    /**
     * 数据库版本升级时调用
     * 数据库版本发生改变时才会被调用,数据库在升级时才会被调用;
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + sData.get());
            Log.i("更新界面", "表名是" + sData.get());
            onCreate(db);
        }
    }


    /**
     * 变更列名
     *
     * @param db
     * @param oldColumn
     * @param newColumn
     * @param typeColumn
     */
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
        try {
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " " + newColumn +
                    " " + typeColumn
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(SQLiteDatabase db, String table) {
        if (table != null && db != null) {
            db.execSQL("CREATE TABLE IF NOT EXISTS" +
                    table + "(");
        }
    }

    public void instertDate(SQLiteDatabase db,
                            String table, String fromuuid, String touuid, String msg) {
        //   db.execSQL("");
        if (table != null && db != null) {
            ContentValues initialValues = new ContentValues();

            initialValues.put("fromuuid", fromuuid);
            initialValues.put("touuid", touuid);
            initialValues.put("msg", msg);
            db.insert(table, null, initialValues);
        }
    }

    public List queryDateFromTable(SQLiteDatabase db, String table) {
        if (table != null && db != null) {
            String CREATESQL = "SELECT * FROM " + table;
            Cursor cursor = db.rawQuery(CREATESQL, null);
           List<String> mList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String NUMBER = cursor.getString(cursor.getColumnIndex("fromuuid"));
                String NUMBER2 = cursor.getString(cursor.getColumnIndex("touuid"));
                String NUMBER3 = cursor.getString(cursor.getColumnIndex("msg"));
               mList.add(NUMBER);
                mList.add(NUMBER2);
                mList.add(NUMBER3);
            }
            cursor.close();
            return mList;
        } return null;
    }

    public void delete(SQLiteDatabase db, String table) {
        if (table != null && db != null) {
            String DELETESQL = "DELETE FROM " + table + " WHERE ID = 1";
            db.execSQL(DELETESQL);

        }
    }
}
