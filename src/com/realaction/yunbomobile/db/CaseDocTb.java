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
	public static final String CASEID = "caseId";
	public static final String ISDOWNLOAD = "isDownload";
	public static final String LOCALPATH = "localPath";
	public static final String DOCDIR = "docdir";

	// �����γ̰�����Դ����Դ��
	public static final String CREATE_CASEDOC_TB = "CREATE TABLE IF NOT EXISTS "
			+ CASEDOCTB + " (" + ID + " integer primary key autoincrement, "
			+ DOCID + " integer, " + DOCNAME + " varchar(128), "
			+ DOCDESC + " varchar(256), " + DOCPATH + " varchar(1024), "
			+ CASEID + " integer, " + ISDOWNLOAD + " integer, "
			+ LOCALPATH + " varchar(1024)," + DOCDIR + " varchar(1024))";
	// ����caseId���Ұ�������Դ
	public static final String FIND_CASEDOC_BY_CASEID = "select " + DOCID + ","
			+ DOCNAME + "," + DOCDESC + "," + DOCPATH + "," + CASEID + ","
			+ ISDOWNLOAD + "," + LOCALPATH + "," + DOCDIR + " from "
			+ CASEDOCTB + " where " + CASEID + "=?";
	// ����caseId��docId���Ҵ���Դ
	public static final String FIND_CASEDOC_BY_CASEIDANDDOCID = "select "
			+ DOCID + "," + DOCNAME + "," + DOCDESC + "," + DOCPATH + ","
			+ CASEID + "," + ISDOWNLOAD + "," + LOCALPATH + "," + DOCDIR
			+ " from " + CASEDOCTB + " where " + CASEID + "=? and " + DOCID
			+ "=?";
	// ɾ���γ̰�������Դ��
	public static final String DROP_CASEDOC_TB = "DROP TABLE IF EXISTS "
			+ CASEDOCTB;

}
