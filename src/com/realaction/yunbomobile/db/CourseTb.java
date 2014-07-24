package com.realaction.yunbomobile.db;

/**
 * 课程表及其中的字段及SQL语句
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

	// 创建课程表
	public static final String CREATE_COURSE_TB = "CREATE TABLE IF NOT EXISTS "
			+ COURSETB + " (" + ID + " integer primary key autoincrement, "
			+ COURSEID + " integer, " + COURSENAME + " varchar(50), " + TYPE
			+ " varchar(50), " + ICON + " varchar(50), " + COURSECODE
			+ " varchar(50), " + SCOREID + " varchar(32), " + USERID + " integer)";
	// 根据userId查找课程
	public static final String FIND_COURSE_BY_USERID = "select " + COURSEID
			+ "," + COURSENAME + "," + TYPE + "," + ICON + "," + COURSECODE
			+ "," + SCOREID + "," + USERID + " from " + COURSETB + " where "
			+ USERID + "=?";
	// 根据courseId和userId查找课程
	public static final String FIND_COURSE_BY_COURSEIDANDUSERID = "select "
			+ COURSEID + "," + COURSENAME + "," + TYPE + "," + ICON + ","
			+ COURSECODE + "," + SCOREID + "," + USERID + " from " + COURSETB
			+ " where " + COURSEID + "=? and " + USERID + "=?";
	// 删除课程表
	public static final String DROP_COURSE_TB = "DROP TABLE IF EXISTS "
			+ COURSETB;
}
