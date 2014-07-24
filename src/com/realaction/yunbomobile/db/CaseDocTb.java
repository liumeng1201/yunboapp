package com.realaction.yunbomobile.db;

/**
 * 课程案例资源答案资源表字段及SQL语句
 * 
 * @author liumeng
 */
public class CaseDocTb {
	public static final String CASEDOCTB = "t_casedoc";

	public static final String ID = "id";
	public static final String DOCID = "docId";
	public static final String DOCNAME = "docName";
	public static final String DOCDESC = "docDesc";
	public static final String DOCPATH = "docPath";
	public static final String CASEID = "caseId";
	public static final String ISDOWNLOAD = "isDownload";
	public static final String LOCALPATH = "localPath";

	// 创建课程案例资源答案资源表
	public static final String CREATE_CASEDOC_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASEDOCTB + " (" + ID + " integer primary key autoincrement, "
			+ DOCID + " integer, " + DOCNAME + " varchar(128), "
			+ DOCDESC + " varchar(256), " + DOCPATH + " varchar(1024), "
			+ CASEID + " integer, " + ISDOWNLOAD + " integer, "
			+ LOCALPATH + " varchar(1024))";
	// 根据caseId查找案例答案资源
	public static final String FIND_CASEDOC_BY_CASEID = "select " + DOCID + ","
			+ DOCNAME + "," + DOCDESC + "," + DOCPATH + "," + CASEID + ","
			+ ISDOWNLOAD + "," + LOCALPATH + " from " + CASEDOCTB
			+ " where " + CASEID + "=?";
	// 根据caseId和docId查找答案资源
	public static final String FIND_CASEDOC_BY_CASEIDANDDOCID = "select "
			+ DOCID + "," + DOCNAME + "," + DOCDESC + "," + DOCPATH + ","
			+ CASEID + "," + ISDOWNLOAD + "," + LOCALPATH + " from "
			+ CASEDOCTB + " where " + CASEID + "=? and " + DOCID + "=?";
	// 根据docPath查找答案资源
	public static final String FIND_CASEDOC_BY_DOCPATH = "select " + DOCID + ","
			+ DOCNAME + "," + DOCDESC + "," + DOCPATH + "," + CASEID + ","
			+ ISDOWNLOAD + "," + LOCALPATH + " from " + CASEDOCTB
			+ " where " + DOCPATH + "=?";
	// 删除课程案例答案资源表
	public static final String DROP_CASEDOC_TB = "DROP TABLE IF EXISTS "
			+ CASEDOCTB;

}
