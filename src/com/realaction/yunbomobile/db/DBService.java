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
	 * ɾ��ָ�����ֵı�
	 * 
	 * @param tablename
	 *            ����
	 */
	public void dropTable(String tablename) {
		db.execSQL("DROP TABLE IF EXISTS " + tablename);
	}

	/**
	 * �ر����ݿ�
	 */
	public void closeDB() {
		db.close();
	}

	/**
	 * �ر�DatabaseOpenHelper
	 */
	public void close() {
		dbHelper.close();
	}

	// TODO �����ݱ����ɾ��Ĳ���

}
