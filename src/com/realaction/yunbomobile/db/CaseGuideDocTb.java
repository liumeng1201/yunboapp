package com.realaction.yunbomobile.db;

/**
 * 课程案例资源实验指导书表字段及SQL语句
 * 
 * @author liumeng
 */
public class CaseGuideDocTb {
	public static final String CASEGUIDEDOCTB = "t_caseguidedoc";
	
	public static final String ID = "id";
	public static final String GUIDEID = "guideId";
	public static final String GUIDEDOCNAME = "guideDocName";
	public static final String GUIDEDOCDESC = "guideDocDesc";
	public static final String GUIDEDOCPATH = "guideDocPath";
	public static final String MEDIATYPEID = "mediaTypeId";
	public static final String CASEID = "caseId";
	public static final String ISDOWNLOAD = "isDownload";
	public static final String LOCALPATH = "localPath";

	// 创建课程案例资源实验指导书表
	public static final String CREATE_CASEGUIDEDOC_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASEGUIDEDOCTB + " (" + ID + " integer primary key autoincrement, "
			+ GUIDEID + " integer, " + GUIDEDOCNAME + " varchar(128), "
			+ GUIDEDOCDESC + " varchar(256), " + GUIDEDOCPATH + " varchar(1024), "
			+ MEDIATYPEID + " integer, " + CASEID + " integer, "
			+ ISDOWNLOAD + " integer, " + LOCALPATH + " varchar(1024))";
	// 根据caseId查找案例实验指导书
	public static final String FIND_CASEGUIDEDOC_BY_CASEID = "select "
			+ GUIDEID + "," + GUIDEDOCNAME + "," + GUIDEDOCDESC + ","
			+ GUIDEDOCPATH + "," + MEDIATYPEID + "," + CASEID + ","
			+ ISDOWNLOAD + "," + LOCALPATH + " from " + CASEGUIDEDOCTB
			+ " where " + CASEID + "=?";
	// 根据caseId和guideId查找实验指导书
	public static final String FIND_CASEGUIDEDOC_BY_CASEIDANDGUIDEID = "select "
			+ GUIDEID + "," + GUIDEDOCNAME + "," + GUIDEDOCDESC + "," + GUIDEDOCPATH
			+ "," + MEDIATYPEID + "," + CASEID + "," + ISDOWNLOAD + ","
			+ LOCALPATH + " from " + CASEGUIDEDOCTB + " where " + CASEID + "=? and "
			+ GUIDEID + "=?";
	// 删除课程案例资源实验指导书表
	public static final String DROP_CASEGUIDEDOC_TB = "DROP TABLE IF EXISTS " + CASEGUIDEDOCTB;

}
