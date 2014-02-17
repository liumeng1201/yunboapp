package com.realaction.yunbomobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.moddel.CourseItem;
import com.realaction.yunbomobile.moddel.User;

/**
 * 数据库服务类提供增删查改功能
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
	 * 向课程表中插入数据不存在插入存在则更新
	 * 
	 * @param course
	 *            课程
	 * @return 成功返回rowId失败返回-1
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
		Cursor cursor = db.rawQuery(
				CourseTb.FIND_COURSE_BY_COURSEIDANDUSERID,
				new String[] { String.valueOf(course.courseId),
						String.valueOf(course.userId) });
		if (cursor.getCount() > 0) {
			// 已存在记录则返回该记录的Id
			cursor.moveToFirst();
			return cursor.getLong(0);
		} else {
			cursor.close();
			// 不存在记录则插入
			return db.insert(CourseTb.COURSETB, null, cv);
		}
	}
	
	/**
	 * 向案例表中插入数据不存在插入存在则更新
	 * 
	 * @param caseitem
	 *            案例
	 * @return 成功返回rowId失败返回-1
	 */
	public long insertCaseTb(CaseItem caseitem) {
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.CASEID, caseitem.caseId);
		cv.put(CaseTb.CASENAME, caseitem.caseName);
		cv.put(CaseTb.KEYWORDS, caseitem.keyWords);
		cv.put(CaseTb.DEVROLENAME, caseitem.devRoleName);
		cv.put(CaseTb.TEACHERNAME, caseitem.teacherName);
		cv.put(CaseTb.CASEGROUPID, caseitem.caseGroupId);
		cv.put(CaseTb.CASEGROUPNAME, caseitem.caseGroupName);
		cv.put(CaseTb.SCOREID, caseitem.scoreId);
		Cursor cursor = db.rawQuery(
				CaseTb.FIND_CASE_BY_SCOREIDANDCASEID,
				new String[] { caseitem.scoreId,
						String.valueOf(caseitem.caseId) });
		if (cursor.getCount() > 0) {
			// 存在记录则返回该记录的ID
			cursor.moveToFirst();
			return cursor.getLong(0);
		} else {
			// 不存在记录则插入
			cursor.close();
			return db.insert(CaseTb.CASETB, null, cv);
		}
	}

	/**
	 * 根据userId查找user
	 * 
	 * @param userId
	 * @return userId对应的user对象
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
	 * 根据username查找user
	 * 
	 * @param username
	 * @return username对应的user对象
	 */
	public User findUserByuserName(String username) {
		User user = new User();
		Cursor cursor = db.rawQuery(UserTb.FIND_USER_BY_USERNAME,
				new String[] { username });
		if (cursor.getCount() > 0) {
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
		} else {
			return null;
		}
	}

	/**
	 * 根据userId查找该用户的所有课程
	 * 
	 * @param userId
	 * @return userId对应的课程列表
	 */
	public List<CourseItem> findCoursesByuserId(long userId) {
		List<CourseItem> courses = new ArrayList<CourseItem>();
		Cursor cursor = db.rawQuery(CourseTb.FIND_COURSE_BY_USERID,
				new String[] { String.valueOf(userId) });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CourseItem course = new CourseItem();
				course.courseId = cursor.getLong(0);
				course.courseName = cursor.getString(1);
				course.type = cursor.getString(2);
				course.icon = cursor.getString(3);
				course.courseCode = cursor.getString(4);
				course.scoreId = cursor.getString(5);
				course.userId = cursor.getLong(6);
				courses.add(course);
			} while (cursor.moveToNext());
			return courses;
		} else {
			return null;
		}
	}

	/**
	 * 根据scoreId查找用户所有的课程案例
	 * 
	 * @param scoreId
	 * @return scoreId对应的案例列表
	 */
	public List<CaseItem> findCasesByscoreId(String scoredId) {
		List<CaseItem> cases = new ArrayList<CaseItem>();
		Cursor cursor = db.rawQuery(CaseTb.FIND_CASE_BY_SCOREID,
				new String[] { String.valueOf(scoredId) });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseItem caseitem = new CaseItem();
				caseitem.caseId = cursor.getLong(0);
				caseitem.caseName = cursor.getString(1);
				caseitem.keyWords = cursor.getString(2);
				caseitem.devRoleName = cursor.getString(3);
				caseitem.teacherName = cursor.getString(4);
				caseitem.caseGroupId = cursor.getLong(5);
				caseitem.caseGroupName = cursor.getString(6);
				caseitem.scoreId = cursor.getString(7);
				cases.add(caseitem);
			} while (cursor.moveToNext());
			return cases;
		} else {
			return null;
		}
	}

}
