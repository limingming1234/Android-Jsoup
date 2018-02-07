package com.example.administrator.Jsoupread.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/2/4.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    // 构造一个SQLiteOpenHelper
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建一个数据库以及表格（ID,NAME,AGE）,其中主键为ID，自增形式
        db.execSQL("create table mytable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库更新
    }
}