package com.realaction.yunbomobile.db;

/**
 * 用户表及其中的字段及SQL语句
 * 
 * @author liumeng
 */
public class UserTb {
	public static final String USERTB = "t_user";

	public static final String ID = "id";
	public static final String USERID = "userId";
	public static final String USERNAME = "userName";
	public static final String PASSWD = "password";
	public static final String REALNAME = "realName";
	public static final String USERTYPEID = "userTypeId";
	public static final String PROFILEURL = "profileUrl";
	public static final String STUNO = "stuNo";
	public static final String EMPNO = "empNo";
	public static final String USER_TB_UNIQUE_INDEX = "unique_index_userId";

	// 创建用户表
	public static final String CREATE_USER_TB = "CREATE TABLE IF NOT EXISTS "
			+ USERTB + " (" + ID + " integer primary key autoincrement, "
			+ USERID + " integer, " + USERNAME + " varchar(20), " + PASSWD
			+ " varchar(100), " + REALNAME + " varchar(100), " + USERTYPEID
			+ " integer, " + PROFILEURL + " varchar(128), " + STUNO
			+ " varchar(32), " + EMPNO + " varchar(32))";
	// 创建用户表userId唯一索引
	public static final String CREATE_USER_TB_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
			+ USER_TB_UNIQUE_INDEX + " ON " + USERTB + "(" + USERID + ")";
	// 根据userId查找用户
	public static final String FIND_USER_BY_USERID = "select " + USERID + ","
			+ USERNAME + "," + PASSWD + "," + REALNAME + "," + USERTYPEID + ","
			+ PROFILEURL + "," + STUNO + "," + EMPNO + " from " + USERTB
			+ " where " + USERID + "=?";
	// 根据userName查找用户
	public static final String FIND_USER_BY_USERNAME = "select " + USERID + ","
			+ USERNAME + "," + PASSWD + "," + REALNAME + "," + USERTYPEID + ","
			+ PROFILEURL + "," + STUNO + "," + EMPNO + " from " + USERTB
			+ " where " + USERNAME + "=?";
}
