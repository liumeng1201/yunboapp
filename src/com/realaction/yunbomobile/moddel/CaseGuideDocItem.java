package com.realaction.yunbomobile.moddel;

/**
 * CaseView����ExpandableListViewʵ��ָ��child��Ϣ
 * 
 * @author liumeng
 */
public class CaseGuideDocItem {
	public long guideId;
	public String guideDocName;
	public String guideDocDesc;
	public String guideDocPath;
	public String casedir;
	public long caseId;
	// �Ƿ��Ѿ�����������
	public int isDownload;
	// ����·��
	public String localPath;

	public CaseGuideDocItem() {
	}

	public CaseGuideDocItem(String str) {
		this.guideDocName = str;
	}
}
