package com.realaction.yunbomobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String name = "yunbo.db";
	private static final int version = 1;
	
	public DBOpenHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 最常浏览和历史记录数据表创建
		//db.execSQL("CREATE TABLE IF NOT EXISTS favorite (id integer primary key autoincrement, )"caseid,path,count);
		//db.execSQL("CREATE TABLE IF NOT EXISTS history (id integer primary key autoincrement, )"caseid,path,time);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS favorite");
		db.execSQL("DROP TABLE IF EXISTS history");
		onCreate(db);
	}

}
