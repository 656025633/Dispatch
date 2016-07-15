package com.winsion.dispatch.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.utils.LogUtil;

/**
 * Created by yalong on 2016/5/31.
 */
public class RemindersDbHelper extends SQLiteOpenHelper {

    public RemindersDbHelper(Context context) {
        super(context, DbConstants.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + DbConstants.REMINDERS_TABLE_NAME + "(noteid varchar(100) " +
                ",content varchar(500),finished integer,date varchar(100),plandate varchar(100),userid varchar(100))");
        sqLiteDatabase.execSQL("create table " + DbConstants.FAILED_REMINDERS_TABLE_NAME + "(noteid varchar(100) " +
                ",content varchar(500),finished integer,date varchar(100),plandate varchar(100),userid varchar(100),state varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 升级操作
        LogUtil.showLog("Database:", "oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }
}
