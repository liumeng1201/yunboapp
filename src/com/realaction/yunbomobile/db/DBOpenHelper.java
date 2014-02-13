package com.realaction.yunbomobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * 
 * @author liumeng
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String name = "yunbo.db";
	private static final int version = 1;

	public DBOpenHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 最常浏览和历史记录数据表创建
		// db.execSQL("CREATE TABLE IF NOT EXISTS favorite (id integer primary key autoincrement, )"caseid,path,count);
		// db.execSQL("CREATE TABLE IF NOT EXISTS history (id integer primary key autoincrement, )"caseid,path,time);
		// 创建user表
		db.execSQL(UserTb.CREATE_USER_TB);
		db.execSQL(UserTb.CREATE_USER_TB_UNIQUE_INDEX);
		// 创建课程表
		db.execSQL(CourseTb.CREATE_COURSE_TB);
		db.execSQL(CourseTb.CREATE_COURSE_TB_UNIQUE_INDEX);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(UserTb.DROP_USER_TB);
		db.execSQL(CourseTb.DROP_COURSE_TB);
		onCreate(db);
	}

}
