package com.toplion.cplusschool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 学校信息SchoolDatabasseHelper
 * 新增wifiName，serverIp 2016/2/1
 * @author liyb
 *
 */
public class SchoolDatabaseHelper extends SQLiteOpenHelper {
    private final static String database = "schools.db";
    private SchoolDatabaseHelper(Context context, String dbName,
                                 CursorFactory factory, int version) {
        super(context, database, factory, version);
    }

    public SchoolDatabaseHelper(Context context, int version) {
        super(context,database, null, version);
        //onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Init ===");
    }

    public void createTable(SQLiteDatabase db){
        db.execSQL("create table school(_id integer primary key ,name text,des text, serverIpAddress text, wifiName text, schoolCode text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("drop table if exists school ");
        }
    }
}
