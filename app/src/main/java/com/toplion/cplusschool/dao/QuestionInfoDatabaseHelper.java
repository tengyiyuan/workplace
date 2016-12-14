package com.toplion.cplusschool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 常见问题及解决方案列表
 * QuestionInfoDatabaseHelper
 * @author liyb
 *
 */
public class QuestionInfoDatabaseHelper extends SQLiteOpenHelper {
    private final static String database = "questioninfos.db";
    private QuestionInfoDatabaseHelper(Context context, String dbName,
                                       CursorFactory factory, int version) {
        super(context, database, factory, version);
    }

    public QuestionInfoDatabaseHelper(Context context, int version) {
        super(context,database, null, version);
        //onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Init ===");

    }

    public void createTable(SQLiteDatabase db){
        db.execSQL("create table questioninfos(_id text primary key ,name text,des text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("drop table if exists questioninfos ");
        }
    }
}
