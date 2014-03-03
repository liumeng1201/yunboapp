package com.realaction.yunbomobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
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
	
	public void beginTransaction() {
		db.beginTransaction();
	}
	
	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}
	
	public void endTransaction() {
		db.endTransaction();
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
	 * 向课程表中插入数据不存在插入存在则返回ID
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
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			// 不存在记录则插入
			return db.insert(CourseTb.COURSETB, null, cv);
		}
	}
	
	/**
	 * 向案例表中插入数据不存在插入存在则返回ID
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
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// 不存在记录则插入
			cursor.close();
			return db.insert(CaseTb.CASETB, null, cv);
		}
	}
	
	/**
	 * 根据caseid和scoreid来更新case表中count的值,加1
	 * 
	 * @param caseId
	 * @param scoreId
	 * @return 受影响的行数
	 */
	public int updateCaseCount(long caseId, long scoreId) {
		int count = 0;
		Cursor cursor = db.rawQuery(CaseTb.FIND_CASE_BY_SCOREIDANDCASEID, new String[] {
						String.valueOf(scoreId), String.valueOf(caseId) });
		try {
			cursor.moveToFirst();
			count = cursor.getInt(9);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			count += 1;
		}
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.COUNT, count);
		return db.update(CaseTb.CASETB, cv, CaseTb.CASEID + "=?" + " and "
				+ CaseTb.SCOREID + "=?", new String[] { String.valueOf(caseId),
				String.valueOf(scoreId) });
	}
	
	/**
	 * 根据caseid和scoreid来更新case表中time的值
	 * 
	 * @param caseId
	 * @param scoreId
	 * @param time
	 * @return 受影响的行数
	 */
	public int updateCaseTime(long caseId, long scoreId, long time) {
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.TIME, time);
		return db.update(CaseTb.CASETB, cv, CaseTb.CASEID + "=?" + " and "
				+ CaseTb.SCOREID + "=?", new String[] { String.valueOf(caseId),
				String.valueOf(scoreId) });
	}
	
	/**
	 * 更新caseTb表中casedir的值
	 * 
	 * @param caseId
	 *            要更新的case的caseId
	 * @param casedir
	 *            要更新的casedir的值
	 * @return 受影响的行数
	 */
	public int updateCaseDir(long caseId, String casedir) {
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.CASEDIR, casedir);
		return db.update(CaseTb.CASETB, cv, CaseTb.CASEID + "=?", new String[] {String.valueOf(caseId)});
	}
	
	/**
	 * 向课程案例资源实验指导书表插入数据不存在插入存在则返回ID
	 * 
	 * @param item
	 *            实验指导书
	 * @return 成功返回rowId失败返回-1
	 */
	public long insertCaseGuideDocTb(CaseGuideDocItem item) {
		ContentValues cv = new ContentValues();
		cv.put(CaseGuideDocTb.GUIDEID, item.guideId);
		cv.put(CaseGuideDocTb.GUIDEDOCNAME, item.guideDocName);
		cv.put(CaseGuideDocTb.GUIDEDOCDESC, item.guideDocDesc);
		cv.put(CaseGuideDocTb.GUIDEDOCPATH, item.guideDocPath);
		cv.put(CaseGuideDocTb.CASEID, item.caseId);
		cv.put(CaseGuideDocTb.ISDOWNLOAD, item.isDownload);
		cv.put(CaseGuideDocTb.LOCALPATH, item.localPath);
		Cursor cursor = db.rawQuery(
				CaseGuideDocTb.FIND_CASEGUIDEDOC_BY_CASEIDANDGUIDEID,
				new String[] { String.valueOf(item.caseId),
						String.valueOf(item.guideId) });
		if (cursor.getCount() > 0) {
			// 存在记录则返回该记录的ID
			cursor.moveToFirst();
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// 不存在记录则插入
			cursor.close();
			return db.insert(CaseGuideDocTb.CASEGUIDEDOCTB, null, cv);
		}
	}
	
	/**
	 * 向课程案例资源答案表插入数据不存在插入存在则返回ID
	 * 
	 * @param item
	 *            答案
	 * @return 成功返回rowId失败返回-1
	 */
	public long insertCaseDocTb(CaseDocItem item) {
		ContentValues cv = new ContentValues();
		cv.put(CaseDocTb.DOCID, item.docId);
		cv.put(CaseDocTb.DOCNAME, item.docName);
		cv.put(CaseDocTb.DOCDESC, item.docDesc);
		cv.put(CaseDocTb.DOCPATH, item.docPath);
		cv.put(CaseDocTb.CASEID, item.caseId);
		cv.put(CaseDocTb.ISDOWNLOAD, item.isDownload);
		cv.put(CaseDocTb.LOCALPATH, item.localPath);
		Cursor cursor = db.rawQuery(
				CaseDocTb.FIND_CASEDOC_BY_CASEIDANDDOCID,
				new String[] { String.valueOf(item.caseId),
						String.valueOf(item.docId) });
		if (cursor.getCount() > 0) {
			// 存在记录则返回该记录的ID
			cursor.moveToFirst();
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// 不存在记录则插入
			cursor.close();
			return db.insert(CaseDocTb.CASEDOCTB, null, cv);
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
		cursor.close();
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
			cursor.close();
			return user;
		} else {
			cursor.close();
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
			cursor.close();
			return courses;
		} else {
			cursor.close();
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
				caseitem.casedir = cursor.getString(8);
				cases.add(caseitem);
			} while (cursor.moveToNext());
			cursor.close();
			return cases;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 根据caseId查询该案例下所有的实验指导书列表
	 * 
	 * @param caseId
	 * @return caseId对应的案例下所有实验指导书列表
	 */
	public List<CaseGuideDocItem> findCaseGuideDocsBycaseId(String caseId) {
		List<CaseGuideDocItem> guideDocs = new ArrayList<CaseGuideDocItem>();
		Cursor cursor = db.rawQuery(CaseGuideDocTb.FIND_CASEGUIDEDOC_BY_CASEID,
				new String[] { caseId });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseGuideDocItem item = new CaseGuideDocItem();
				item.guideId = cursor.getLong(0);
				item.guideDocName = cursor.getString(1);
				item.guideDocDesc = cursor.getString(2);
				item.guideDocPath = cursor.getString(3);
				item.caseId = cursor.getLong(4);
				item.isDownload = cursor.getInt(5);
				item.localPath = cursor.getString(6);
				guideDocs.add(item);
			} while (cursor.moveToNext());
			cursor.close();
			return guideDocs;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 通过caseId和guideId查找对应的caseguidedoc
	 * 
	 * @param caseId
	 * @param guideId
	 * @return caseguidedoc
	 */
	public CaseGuideDocItem findCaseGuideDocBycaseIdAndguideId(String caseId, String guideId) {
		CaseGuideDocItem guideItem = new CaseGuideDocItem();
		Cursor cursor = db.rawQuery(
				CaseGuideDocTb.FIND_CASEGUIDEDOC_BY_CASEIDANDGUIDEID,
				new String[] { caseId, guideId });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				guideItem.guideId = cursor.getLong(0);
				guideItem.guideDocName = cursor.getString(1);
				guideItem.guideDocDesc = cursor.getString(2);
				guideItem.guideDocPath = cursor.getString(3);
				guideItem.caseId = cursor.getLong(4);
				guideItem.isDownload = cursor.getInt(5);
				guideItem.localPath = cursor.getString(6);
			} while (cursor.moveToNext());
			cursor.close();
			return guideItem;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 根据caseId查询该案例下所有的答案列表
	 * 
	 * @param caseId
	 * @return caseId对应的案例下所有答案列表
	 */
	public List<CaseDocItem> findCaseDocsBycaseId(String caseId) {
		List<CaseDocItem> caseDocs = new ArrayList<CaseDocItem>();
		Cursor cursor = db.rawQuery(CaseDocTb.FIND_CASEDOC_BY_CASEID,
				new String[] { caseId });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseDocItem item = new CaseDocItem();
				item.docId = cursor.getLong(0);
				item.docName = cursor.getString(1);
				item.docDesc = cursor.getString(2);
				item.docPath = cursor.getString(3);
				item.caseId = cursor.getLong(4);
				item.isDownload = cursor.getInt(5);
				item.localPath = cursor.getString(6);
				caseDocs.add(item);
			} while (cursor.moveToNext());
			cursor.close();
			return caseDocs;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 更新课程案例实验指导书表
	 * 
	 * @param item
	 * @return 受影响的行数
	 */
	public int updateCaseGuideDoc(CaseGuideDocItem item) {
		ContentValues cv = new ContentValues();
		cv.put(CaseGuideDocTb.GUIDEID, item.guideId);
		cv.put(CaseGuideDocTb.GUIDEDOCNAME, item.guideDocName);
		cv.put(CaseGuideDocTb.GUIDEDOCDESC, item.guideDocDesc);
		cv.put(CaseGuideDocTb.GUIDEDOCPATH, item.guideDocPath);
		cv.put(CaseGuideDocTb.CASEID, item.caseId);
		cv.put(CaseGuideDocTb.ISDOWNLOAD, item.isDownload);
		cv.put(CaseGuideDocTb.LOCALPATH, item.localPath);
		return db.update(CaseGuideDocTb.CASEGUIDEDOCTB, cv, CaseGuideDocTb.GUIDEID + "=" + item.guideId, null);
	}
	
	/**
	 * 根据userid获取该用户下的所有scoreid
	 * 
	 * @param userId
	 * @return scoreId数组
	 */
	public String[] getUserScoreIds(long userId) {
		StringBuffer sb = new StringBuffer();
		Cursor cursor = db.rawQuery("select " + CourseTb.SCOREID + " from "
				+ CourseTb.COURSETB + " where " + CourseTb.USERID + "=?",
				new String[] { String.valueOf(userId) });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < cursor.getCount(); i++) {
				list.add(String.valueOf(cursor.getLong(0)));
			}
			String[] scoreids = new String[list.size()];
			list.toArray(scoreids);
			cursor.close();
			return scoreids;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 获取用户最常访问的5个case的列表
	 * 
	 * @param scoreids
	 *            用户所有的scoreid
	 * @return
	 */
	public List<CaseItem> findCasesOrderByCount(String[] scoreids) {
		List<CaseItem> caselist = new ArrayList<CaseItem>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < scoreids.length; i++) {
			sb.append('?');
			if ((i - 1) > 0) {
				sb.append(',');
			}
		}
		Cursor cursor = db.rawQuery(CaseTb.FIND_CASE_ORDERBY_COUNT
				+ " where scoreId in(" + sb.toString() + ")" + " order by "
				+ CaseTb.COUNT + " limit 5", scoreids);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseItem caseitem = new CaseItem();
				caseitem.caseId = cursor.getLong(0);
				caseitem.caseName = cursor.getString(1);
				caseitem.caseGroupId = cursor.getLong(2);
				caseitem.scoreId = cursor.getString(3);
				caseitem.casedir = cursor.getString(4);
				caselist.add(caseitem);
			} while (cursor.moveToNext());
			cursor.close();
			return caselist;
		} else {
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 获取用户最后访问的5个case的列表
	 * 
	 * @param scoreids
	 *            用户所有的scoreid
	 * @return
	 */
	public List<CaseItem> findCasesOrderByTime(String[] scoreids) {
		List<CaseItem> caselist = new ArrayList<CaseItem>();
		StringBuffer sb = new StringBuffer();
		for (String scoreid : scoreids) {
			sb.append('?').append(',');
		}
		Cursor cursor = db.rawQuery(CaseTb.FIND_CASE_ORDERBY_COUNT
				+ " where scoreId in(" + sb.toString() + ")" + " order by "
				+ CaseTb.TIME + " limit 5", scoreids);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseItem caseitem = new CaseItem();
				caseitem.caseId = cursor.getLong(0);
				caseitem.caseName = cursor.getString(1);
				caseitem.caseGroupId = cursor.getLong(2);
				caseitem.scoreId = cursor.getString(3);
				caseitem.casedir = cursor.getString(4);
				caselist.add(caseitem);
			} while (cursor.moveToNext());
			cursor.close();
			return caselist;
		} else {
			cursor.close();
			return null;
		}
	}
}
