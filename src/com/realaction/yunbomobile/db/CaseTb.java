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
	public static final String CASEDIR = "casedir";
	public static final String COUNT = "count";
	public static final String TIME = "time";
	public static final String DOWNLOAD = "download";
	public static final String CASEDESC = "caseDesc";

	// 创建案例表
	public static final String CREATE_CASE_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASETB + " (" + ID + " integer primary key autoincrement, "
			+ CASEID + " integer, " + CASENAME + " varchar(128), " + KEYWORDS
			+ " varchar(200), " + DEVROLENAME + " varchar(50), " + TEACHERNAME
			+ " varchar(50), " + CASEGROUPID + " integer, " + CASEGROUPNAME
			+ " varchar(32), " + SCOREID + " varchar(32), " + CASEDIR
			+ " varchar(1024), " + COUNT + " integer, " + TIME + " integer, "
			+ DOWNLOAD + " integer DEFAULT 0, " + CASEDESC + " varchar(1024)"
			+ ")";
	// 根据caseId查找案例
	public static final String FIND_CASE_BY_CASEID = "select " + CASEID + ","
			+ CASENAME + "," + KEYWORDS + "," + DEVROLENAME + "," + TEACHERNAME
			+ "," + CASEGROUPID + "," + CASEGROUPNAME + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB + " where " + CASEID + "=?";
	// 根据scoreId查找案例
	public static final String FIND_CASE_BY_SCOREID = "select " + CASEID + ","
			+ CASENAME + "," + KEYWORDS + "," + DEVROLENAME + "," + TEACHERNAME
			+ "," + CASEGROUPID + "," + CASEGROUPNAME + "," + SCOREID + ","
			+ CASEDIR + "," + DOWNLOAD + " from " + CASETB + " where "
			+ SCOREID + "=?";
	// 根据scoreId和caseId查找案例
	public static final String FIND_CASE_BY_SCOREIDANDCASEID = "select "
			+ CASEID + "," + CASENAME + "," + KEYWORDS + "," + DEVROLENAME
			+ "," + TEACHERNAME + "," + CASEGROUPID + "," + CASEGROUPNAME + ","
			+ SCOREID + "," + CASEDIR + "," + COUNT + " from " + CASETB
			+ " where " + SCOREID + "=? and " + CASEID + "=?";
	public static final String FIND_CASE_ORDERBY_COUNTORTIME = "select " + CASEID
			+ "," + CASENAME + "," + CASEGROUPID + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB;
	// 查找最老的case
	public static final String FIND_OLDEST_CASE = "select " + CASEID
			+ "," + CASEDIR + " from " + CASETB + " where " + CaseTb.TIME
			+ " > 0" + " order by " + CaseTb.TIME + " limit 1";
	// 删除课程表
	public static final String DROP_CASE_TB = "DROP TABLE IF EXISTS " + CASETB;
}
