package com.realaction.yunbomobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.realaction.yunbomobile.moddel.CourseItem;
import com.realaction.yunbomobile.moddel.User;

/**
 * ���ݿ�������ṩ��ɾ��Ĺ���
 * 
 * @author liumeng
 */
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
		ContentValues cv = new ContentValues();
		cv.put(UserTb.USERID, user.userId);
		cv.put(UserTb.USERNAME, user.userName);
		cv.put(UserTb.PASSWD, user.password);
		cv.put(UserTb.REALNAME, user.realName);
		cv.put(UserTb.USERTYPEID, user.userTypeId);
		cv.put(UserTb.PROFILEURL, user.profileUrl);
		cv.put(UserTb.STUNO, user.stuNo);
		cv.put(UserTb.EMPNO, user.empNo);
		return db.replace(UserTb.USERTB, null, cv);
	}

	/**
	 * ��γ̱��в������ݲ����ڲ�����������
	 * 
	 * @param course
	 *            �γ�
	 * @return �ɹ�����rowIdʧ�ܷ���-1
	 */
	public long insertCourseTb(CourseItem course) {
		ContentValues cv = new ContentValues();
		cv.put(CourseTb.COURSEID, course.courseId);
		cv.put(CourseTb.COURSENAME, course.courseName);
		cv.put(CourseTb.TYPE, course.type);
		cv.put(CourseTb.ICON, course.icon);
		cv.put(CourseTb.COURSECODE, course.courseCode);
		cv.put(CourseTb.SCOREID, course.scoreId);
		cv.put(CourseTb.USERID, course.userId);
		return db.replace(CourseTb.COURSETB, null, cv);
	}

	/**
	 * ����userId����user
	 * 
	 * @param userId
	 * @return userId��Ӧ��user����
	 */
	public User findUserByuserId(long userId) {
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

	/**
	 * ����courseId����course
	 * 
	 * @param courseId
	 * @return courseId��Ӧ��course����
	 */
	public CourseItem findCourseBycourseId(long courseId) {
		CourseItem course = new CourseItem();
		Cursor cursor = db.rawQuery(CourseTb.FIND_COURSE_BY_COURSEID,
				new String[] { String.valueOf(courseId) });
		if (cursor.moveToNext()) {
			course.courseId = cursor.getLong(0);
			course.courseName = cursor.getString(1);
			course.type = cursor.getString(2);
			course.icon = cursor.getString(3);
			course.courseCode = cursor.getString(4);
			course.scoreId = cursor.getString(5);
			course.userId = cursor.getLong(6);
		}
		return course;
	}

}
