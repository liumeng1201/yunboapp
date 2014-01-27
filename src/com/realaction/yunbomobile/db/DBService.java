package com.realaction.yunbomobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.realaction.yunbomobile.moddel.User;

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
	 * �ر����ݿ��DatabaseOpenHelper
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * ���û����в������ݲ����ڲ�����������
	 * 
	 * @param user
	 *            �û�
	 * @return �ɹ�����rowIdʧ�ܷ���-1
	 */
	public long insertUserTb(User user) {
		/*
		db.execSQL("insert into t_user (userId, userName, password,"
				+ " realName, userTypeId, profileUrl, stuNo, empNo)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
				user.userId, user.userName, user.password, user.realName,
				user.userTypeId, user.profileUrl, user.stuNo, user.empNo });*/
		ContentValues cv = new ContentValues();
		cv.put(UserTb.USERID, user.userId);
		cv.put(UserTb.USERNAME, user.userName);
		cv.put(UserTb.PASSWD, user.password);
		cv.put(UserTb.REALNAME, user.realName);
		cv.put(UserTb.USERTYPEID, user.userTypeId);
		cv.put(UserTb.PROFILEURL, user.profileUrl);
		cv.put(UserTb.STUNO, user.stuNo);
		cv.put(UserTb.EMPNO, user.empNo);
//		return db.insert(UserTb.USERTB, null, cv);
		return db.replace(UserTb.USERTB, null, cv);
	}

	/**
	 * ����userId����user
	 * 
	 * @param userId
	 * @return userId��Ӧ��user����
	 */
	public User findUserByuserId(int userId) {
		User user = new User();
		Cursor cursor = db.rawQuery(UserTb.FIND_USER_BY_USERID,
				new String[] { String.valueOf(userId) });
		if (cursor.moveToNext()) {
			user.userId = cursor.getLong(0);
			user.userName = cursor.getString(1);
			user.password = cursor.getString(2);
			user.realName = cursor.getString(3);
			user.userTypeId = cursor.getInt(4);
			user.profileUrl = cursor.getString(5);
			user.stuNo = cursor.getString(6);
			user.empNo = cursor.getString(7);
		}
		return user;
	}

	/**
	 * ����username����user
	 * 
	 * @param username
	 * @return username��Ӧ��user����
	 */
	public User findUserByuserName(String username) {
		User user = new User();
		Cursor cursor = db.rawQuery(UserTb.FIND_USER_BY_USERNAME,
				new String[] { username });
		if (cursor.moveToNext()) {
			user.userId = cursor.getLong(0);
			user.userName = cursor.getString(1);
			user.password = cursor.getString(2);
			user.realName = cursor.getString(3);
			user.userTypeId = cursor.getInt(4);
			user.profileUrl = cursor.getString(5);
			user.stuNo = cursor.getString(6);
			user.empNo = cursor.getString(7);
		}
		return user;
	}
}
