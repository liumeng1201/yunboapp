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
	 * ��γ̱��в������ݲ����ڲ�������򷵻�ID
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
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			// �����ڼ�¼�����
			return db.insert(CourseTb.COURSETB, null, cv);
		}
	}
	
	/**
	 * �������в������ݲ����ڲ�������򷵻�ID
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
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// �����ڼ�¼�����
			cursor.close();
			return db.insert(CaseTb.CASETB, null, cv);
		}
	}
	
	/**
	 * ����caseid��scoreid������case����count��ֵ,��1
	 * 
	 * @param caseId
	 * @param scoreId
	 * @return ��Ӱ�������
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
	 * ����caseid��scoreid������case����time��ֵ
	 * 
	 * @param caseId
	 * @param scoreId
	 * @param time
	 * @return ��Ӱ�������
	 */
	public int updateCaseTime(long caseId, long scoreId, long time) {
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.TIME, time);
		return db.update(CaseTb.CASETB, cv, CaseTb.CASEID + "=?" + " and "
				+ CaseTb.SCOREID + "=?", new String[] { String.valueOf(caseId),
				String.valueOf(scoreId) });
	}
	
	/**
	 * ����caseTb����casedir��ֵ
	 * 
	 * @param caseId
	 *            Ҫ���µ�case��caseId
	 * @param casedir
	 *            Ҫ���µ�casedir��ֵ
	 * @return ��Ӱ�������
	 */
	public int updateCaseDir(long caseId, String casedir) {
		ContentValues cv = new ContentValues();
		cv.put(CaseTb.CASEDIR, casedir);
		return db.update(CaseTb.CASETB, cv, CaseTb.CASEID + "=?", new String[] {String.valueOf(caseId)});
	}
	
	/**
	 * ��γ̰�����Դʵ��ָ�����������ݲ����ڲ�������򷵻�ID
	 * 
	 * @param item
	 *            ʵ��ָ����
	 * @return �ɹ�����rowIdʧ�ܷ���-1
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
			// ���ڼ�¼�򷵻ظü�¼��ID
			cursor.moveToFirst();
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// �����ڼ�¼�����
			cursor.close();
			return db.insert(CaseGuideDocTb.CASEGUIDEDOCTB, null, cv);
		}
	}
	
	/**
	 * ��γ̰�����Դ�𰸱�������ݲ����ڲ�������򷵻�ID
	 * 
	 * @param item
	 *            ��
	 * @return �ɹ�����rowIdʧ�ܷ���-1
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
			// ���ڼ�¼�򷵻ظü�¼��ID
			cursor.moveToFirst();
			long result = cursor.getLong(0);
			cursor.close();
			return result;
		} else {
			// �����ڼ�¼�����
			cursor.close();
			return db.insert(CaseDocTb.CASEDOCTB, null, cv);
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
		cursor.close();
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
			cursor.close();
			return user;
		} else {
			cursor.close();
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
			cursor.close();
			return courses;
		} else {
			cursor.close();
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
	 * ����caseId��ѯ�ð��������е�ʵ��ָ�����б�
	 * 
	 * @param caseId
	 * @return caseId��Ӧ�İ���������ʵ��ָ�����б�
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
	 * ͨ��caseId��guideId���Ҷ�Ӧ��caseguidedoc
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
	 * ����caseId��ѯ�ð��������еĴ��б�
	 * 
	 * @param caseId
	 * @return caseId��Ӧ�İ��������д��б�
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
	 * ���¿γ̰���ʵ��ָ�����
	 * 
	 * @param item
	 * @return ��Ӱ�������
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
	 * ����userid��ȡ���û��µ�����scoreid
	 * 
	 * @param userId
	 * @return scoreId����
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
	 * ��ȡ�û�����ʵ�5��case���б�
	 * 
	 * @param scoreids
	 *            �û����е�scoreid
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
	 * ��ȡ�û������ʵ�5��case���б�
	 * 
	 * @param scoreids
	 *            �û����е�scoreid
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
