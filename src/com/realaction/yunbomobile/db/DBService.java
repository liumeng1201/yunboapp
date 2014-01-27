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
	 * 删除指定名字的表
	 * 
	 * @param tablename
	 *            表名
	 */
	public void dropTable(String tablename) {
		db.execSQL("DROP TABLE IF EXISTS " + tablename);
	}

	/**
	 * 关闭数据库和DatabaseOpenHelper
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * 向用户表中插入数据不存在插入存在则更新
	 * 
	 * @param user
	 *            用户
	 * @return 成功返回rowId失败返回-1
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
	 * 根据userId查找user
	 * 
	 * @param userId
	 * @return userId对应的user对象
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
	 * 根据username查找user
	 * 
	 * @param username
	 * @return username对应的user对象
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
