package com.realaction.yunbomobile.db;

/**
 * 案例表及其中的字段及SQL语句
 * 
 * @author liumeng
 */
public class CaseTb {
	public static final String CASETB = "t_case";

	public static final String ID = "id";
	public static final String CASEID = "caseId";
	public static final String CASENAME = "caseName";
	public static final String KEYWORDS = "keyWords";
	public static final String DEVROLENAME = "devRoleName";
	public static final String TEACHERNAME = "teacherName";
	public static final String CASEGROUPID = "caseGroupId";
	public static final String CASEGROUPNAME = "caseGroupName";
	public static final String SCOREID = "scoreId";
	public static final String CASE_TB_UNIQUE_INDEX = "unique_index_caseId";

	// 创建案例表
	public static final String CREATE_CASE_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASETB + " (" + ID + " integer primary key autoincrement, "
			+ CASEID + " integer, " + CASENAME + " varchar(128), " + KEYWORDS
			+ " varchar(200), " + DEVROLENAME + " varchar(50), " + TEACHERNAME
			+ " varchar(50), " + CASEGROUPID + " integer, " + CASEGROUPNAME
			+ " varchar(32), " + SCOREID + " varchar(32))";
	// 创建案例表caseId唯一索引
	public static final String CREATE_CASE_TB_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
			+ CASE_TB_UNIQUE_INDEX + " ON " + CASETB + "(" + CASEID + ")";
	// 根据scoreId查找案例
	public static final String FIND_CASE_BY_SCOREID = "select " + CASEID + ","
			+ CASENAME + "," + KEYWORDS + "," + DEVROLENAME + ","
			+ TEACHERNAME + "," + CASEGROUPID + "," + CASEGROUPNAME + ","
			+ SCOREID + " from " + CASETB + " where " + SCOREID + "=?";
	// 删除课程表
	public static final String DROP_CASE_TB = "DROP TABLE IF EXISTS " + CASETB;
}
