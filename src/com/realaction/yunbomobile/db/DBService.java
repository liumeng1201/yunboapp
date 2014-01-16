package com.realaction.yunbomobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private DBOpenHelper dbHelper;
	private SQLiteDatabase db;

	public DBService(Context context) {
		dbHelper = new DBOpenHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 删除指定名字的表
	 * 
	 * @param tablename
	 *            表名
	 */
	public void dropTable(String tablename) {
		db.execSQL("DROP TABLE IF EXISTS " + tablename);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		db.close();
	}

	/**
	 * 关闭DatabaseOpenHelper
	 */
	public void close() {
		dbHelper.close();
	}

	// TODO 对数据表的增删查改操作

}
