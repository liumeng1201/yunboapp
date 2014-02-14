package com.realaction.yunbomobile.db;

/**
 * �γ̱����е��ֶμ�SQL���
 * 
 * @author liumeng
 */
public class CourseTb {
	public static final String COURSETB = "t_course";

	public static final String ID = "id";
	public static final String COURSEID = "courseId";
	public static final String COURSENAME = "courseName";
	public static final String TYPE = "type";
	public static final String ICON = "icon";
	public static final String COURSECODE = "courseCode";
	public static final String SCOREID = "scoreId";
	public static final String USERID = "userId";
	public static final String COURSE_TB_UNIQUE_INDEX = "unique_index_courseId";

	// �����γ̱�
	public static final String CREATE_COURSE_TB = "CREATE TABLE IF NOT EXISTS "
			+ COURSETB + " (" + ID + " integer primary key autoincrement, "
			+ COURSEID + " integer, " + COURSENAME + " varchar(50), " + TYPE
			+ " varchar(50), " + ICON + " varchar(50), " + COURSECODE
			+ " varchar(50), " + SCOREID + " varchar(32), " + USERID + " integer)";
	// �����γ̱�courseIdΨһ����
	public static final String CREATE_COURSE_TB_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
			+ COURSE_TB_UNIQUE_INDEX + " ON " + COURSETB + "(" + COURSEID + ")";
	// ����userId���ҿγ�
	public static final String FIND_COURSE_BY_USERID = "select " + COURSEID
			+ "," + COURSENAME + "," + TYPE + "," + ICON + "," + COURSECODE
			+ "," + SCOREID + "," + USERID + " from " + COURSETB + " where "
			+ USERID + "=?";
	// ɾ���γ̱�
	public static final String DROP_COURSE_TB = "DROP TABLE IF EXISTS "
			+ COURSETB;
}
