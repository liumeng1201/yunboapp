package com.realaction.yunbomobile.db;

/**
 * �û������е��ֶμ�SQL���
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

	// �����û���
	public static final String CREATE_USER_TB = "CREATE TABLE IF NOT EXISTS "
			+ USERTB + " (" + ID + " integer primary key autoincrement, "
			+ USERID + " integer, " + USERNAME + " varchar(20), " + PASSWD
			+ " varchar(100), " + REALNAME + " varchar(100), " + USERTYPEID
			+ " integer, " + PROFILEURL + " varchar(128), " + STUNO
			+ " varchar(32), " + EMPNO + " varchar(32))";
	// �����û���userIdΨһ����
	public static final String CREATE_USER_TB_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
			+ USER_TB_UNIQUE_INDEX + " ON " + USERTB + "(" + USERID + ")";
	// ����userId�����û�
	public static final String FIND_USER_BY_USERID = "select " + USERID + ","
			+ USERNAME + "," + PASSWD + "," + REALNAME + "," + USERTYPEID + ","
			+ PROFILEURL + "," + STUNO + "," + EMPNO + " from " + USERTB
			+ " where " + USERID + "=?";
	// ����userName�����û�
	public static final String FIND_USER_BY_USERNAME = "select " + USERID + ","
			+ USERNAME + "," + PASSWD + "," + REALNAME + "," + USERTYPEID + ","
			+ PROFILEURL + "," + STUNO + "," + EMPNO + " from " + USERTB
			+ " where " + USERNAME + "=?";
}
