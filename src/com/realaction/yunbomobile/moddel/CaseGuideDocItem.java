package com.realaction.yunbomobile.moddel;

/**
 * CaseView界面ExpandableListView实验指导child信息
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
	// 是否已经缓存至本地
	public int isDownload;
	// 本地路径
	public String localPath;

	public CaseGuideDocItem() {
	}

	public CaseGuideDocItem(String str) {
		this.guideDocName = str;
	}
}
