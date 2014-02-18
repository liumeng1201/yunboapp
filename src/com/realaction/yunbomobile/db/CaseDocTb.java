package com.realaction.yunbomobile.db;

/**
 * �γ̰�����Դ����Դ���ֶμ�SQL���
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
	public static final String DOCTYPEID = "docTypeId";
	public static final String CASEID = "caseId";
	public static final String ISDOWNLOAD = "isDownload";
	public static final String LOCALPATH = "localPath";

	// �����γ̰�����Դ����Դ��
	public static final String CREATE_CASEDOC_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASEDOCTB + " (" + ID + " integer primary key autoincrement, "
			+ DOCID + " integer, " + DOCNAME + " varchar(128), " + DOCDESC
			+ " varchar(256), " + DOCPATH + " varchar(1024), " + DOCTYPEID
			+ " integer, " + CASEID + " integer, " + ISDOWNLOAD + " integer, "
			+ LOCALPATH + " varchar(1024))";
	// ����caseId���Ұ�������Դ
	public static final String FIND_CASEDOC_BY_CASEID = "select " + DOCID + ","
			+ DOCNAME + "," + DOCDESC + "," + DOCPATH + "," + DOCTYPEID + ","
			+ CASEID + "," + ISDOWNLOAD + "," + LOCALPATH + " from "
			+ CASEDOCTB + " where " + CASEID + "=?";
	// ����caseId��docId���Ҵ���Դ
	public static final String FIND_CASEDOC_BY_CASEIDANDDOCID = "select "
			+ DOCID + "," + DOCNAME + "," + DOCDESC + "," + DOCPATH + ","
			+ DOCTYPEID + "," + CASEID + "," + ISDOWNLOAD + "," + LOCALPATH
			+ " from " + CASEDOCTB + " where " + CASEID + "=? and " + DOCID
			+ "=?";
	// ɾ���γ̰�������Դ��
	public static final String DROP_CASEDOC_TB = "DROP TABLE IF EXISTS "
			+ CASEDOCTB;

}
