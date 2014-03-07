package com.realaction.yunbomobile.db;

/**
 * ���������е��ֶμ�SQL���
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

	// ����������
	public static final String CREATE_CASE_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASETB + " (" + ID + " integer primary key autoincrement, "
			+ CASEID + " integer, " + CASENAME + " varchar(128), " + KEYWORDS
			+ " varchar(200), " + DEVROLENAME + " varchar(50), " + TEACHERNAME
			+ " varchar(50), " + CASEGROUPID + " integer, " + CASEGROUPNAME
			+ " varchar(32), " + SCOREID + " varchar(32), " + CASEDIR
			+ " varchar(1024), " + COUNT + " integer, " + TIME + " integer"
			+ ")";
	// ����caseId���Ұ���
	public static final String FIND_CASE_BY_CASEID = "select " + CASEID + ","
			+ CASENAME + "," + KEYWORDS + "," + DEVROLENAME + "," + TEACHERNAME
			+ "," + CASEGROUPID + "," + CASEGROUPNAME + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB + " where " + CASEID + "=?";
	// ����scoreId���Ұ���
	public static final String FIND_CASE_BY_SCOREID = "select " + CASEID + ","
			+ CASENAME + "," + KEYWORDS + "," + DEVROLENAME + "," + TEACHERNAME
			+ "," + CASEGROUPID + "," + CASEGROUPNAME + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB + " where " + SCOREID + "=?";
	// ����scoreId��caseId���Ұ���
	public static final String FIND_CASE_BY_SCOREIDANDCASEID = "select "
			+ CASEID + "," + CASENAME + "," + KEYWORDS + "," + DEVROLENAME
			+ "," + TEACHERNAME + "," + CASEGROUPID + "," + CASEGROUPNAME + ","
			+ SCOREID + "," + CASEDIR + "," + COUNT + " from " + CASETB
			+ " where " + SCOREID + "=? and " + CASEID + "=?";
	// ����count�����г�5�����
	public static final String FIND_CASE_ORDERBY_COUNT = "select " + CASEID
			+ "," + CASENAME + "," + CASEGROUPID + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB;
	// ����time�����г�5�����
	public static final String FIND_CASE_ORDERBY_TIME = "select " + CASEID
			+ "," + CASENAME + "," + CASEGROUPID + "," + SCOREID + ","
			+ CASEDIR + " from " + CASETB;
	// ɾ���γ̱�
	public static final String DROP_CASE_TB = "DROP TABLE IF EXISTS " + CASETB;
}
