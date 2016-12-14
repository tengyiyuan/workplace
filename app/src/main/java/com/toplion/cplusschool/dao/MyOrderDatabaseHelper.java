package com.toplion.cplusschool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 我的订单
 * DatabaseHelper
 * @author liyb
 *
 */
public class MyOrderDatabaseHelper extends SQLiteOpenHelper {
    private final static String database = "orders.db";
    private MyOrderDatabaseHelper(Context context, String dbName,
                                  CursorFactory factory, int version) {
        super(context, database, factory, version);
    }

    public MyOrderDatabaseHelper(Context context, int version) {
        super(context,database, null, version);
        //onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // System.out.println("Init ===");
    }

    public void createTable(SQLiteDatabase db){
        db.execSQL("create table orders(_id integer primary key , orderId text, orderState text, orderMoney text, orderType text, orderTime text, pkgName text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("drop table if exists orders ");
        }
    }
}
