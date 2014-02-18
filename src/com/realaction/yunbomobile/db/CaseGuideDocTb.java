package com.realaction.yunbomobile.db;

/**
 * �γ̰�����Դʵ��ָ������ֶμ�SQL���
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

	// �����γ̰�����Դʵ��ָ�����
	public static final String CREATE_CASEGUIDEDOC_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASEGUIDEDOCTB + " (" + ID + " integer primary key autoincrement, "
			+ GUIDEID + " integer, " + GUIDEDOCNAME + " varchar(128), "
			+ GUIDEDOCDESC + " varchar(256), " + GUIDEDOCPATH + " varchar(1024), "
			+ MEDIATYPEID + " integer, " + CASEID + " integer, "
			+ ISDOWNLOAD + " integer, " + LOCALPATH + " varchar(1024))";
	// ����caseId���Ұ���ʵ��ָ����
	public static final String FIND_CASEGUIDEDOC_BY_CASEID = "select "
			+ GUIDEID + "," + GUIDEDOCNAME + "," + GUIDEDOCDESC + ","
			+ GUIDEDOCPATH + "," + MEDIATYPEID + "," + CASEID + ","
			+ ISDOWNLOAD + "," + LOCALPATH + " from " + CASEGUIDEDOCTB
			+ " where " + CASEID + "=?";
	// ����caseId��guideId����ʵ��ָ����
	public static final String FIND_CASEGUIDEDOC_BY_CASEIDANDGUIDEID = "select "
			+ GUIDEID + "," + GUIDEDOCNAME + "," + GUIDEDOCDESC + "," + GUIDEDOCPATH
			+ "," + MEDIATYPEID + "," + CASEID + "," + ISDOWNLOAD + ","
			+ LOCALPATH + " from " + CASEGUIDEDOCTB + " where " + CASEID + "=? and "
			+ GUIDEID + "=?";
	// ɾ���γ̰�����Դʵ��ָ�����
	public static final String DROP_CASEGUIDEDOC_TB = "DROP TABLE IF EXISTS " + CASEGUIDEDOCTB;

}
