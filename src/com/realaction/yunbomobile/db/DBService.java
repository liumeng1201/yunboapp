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
		Cursor cursor = db.rawQuery(
				CourseTb.FIND_COURSE_BY_COURSEIDANDUSERID,
				new String[] { String.valueOf(course.courseId),
						String.valueOf(course.userId) });
		if (cursor.getCount() > 0) {
			// �Ѵ��ڼ�¼�򷵻ظü�¼��Id
			cursor.moveToFirst();
			return cursor.getLong(0);
		} else {
			cursor.close();
			// �����ڼ�¼�����
			return db.insert(CourseTb.COURSETB, null, cv);
		}
	}
	
	/**
	 * �������в������ݲ����ڲ�����������
	 * 
	 * @param caseitem
	 *            ����
	 * @return �ɹ�����rowIdʧ�ܷ���-1
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
			// ���ڼ�¼�򷵻ظü�¼��ID
			cursor.moveToFirst();
			return cursor.getLong(0);
		} else {
			// �����ڼ�¼�����
			cursor.close();
			return db.insert(CaseTb.CASETB, null, cv);
		}
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
	 * ����userId���Ҹ��û������пγ�
	 * 
	 * @param userId
	 * @return userId��Ӧ�Ŀγ��б�
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
	 * ����scoreId�����û����еĿγ̰���
	 * 
	 * @param scoreId
	 * @return scoreId��Ӧ�İ����б�
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
