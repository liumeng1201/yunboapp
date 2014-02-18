package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;

import com.realaction.yunbomobile.adapter.DrawerCaseViewExpandableAdapter;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.moddel.CaseViewGroupItem;

/**
 * 获取课程案例资源列表并通知adapter更新数据
 * 
 * @author liumeng
 */
public class AsyncTaskGetCaseSourceList extends AsyncTask<String, Integer, Map> {
	private String url = AppInfo.base_url + "/formobile/formobileGetCaseSources.action";
	private Context context;
	private DrawerCaseViewExpandableAdapter adapter;
	
	public AsyncTaskGetCaseSourceList(Context context, DrawerCaseViewExpandableAdapter adapter) {
		this.context = context;
		this.adapter = adapter;
	}

	@Override
	protected Map doInBackground(String... params) {
		// params: caseId
		Map map = new HashMap<String, Object>();
		List<CaseViewGroupItem> groupArray = new ArrayList<CaseViewGroupItem>();
		List<List<CaseGuideDocItem>> childArray = new ArrayList<List<CaseGuideDocItem>>();
		long caseId = Long.parseLong(params[0]);
		
		CaseSourcesUtils csu = new CaseSourcesUtils(context);
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		datas.add(new BasicNameValuePair("caseId", Long.toString(caseId)));
		List<CaseGuideDocItem> casesourcelist = csu.getCaseSourcesList(url, datas);

		// TODO 根据内容自动获取要显示的菜单项,将要显示的内容分类存放
		CaseViewGroupItem cvgi1 = new CaseViewGroupItem();
		cvgi1.groupname = "实验指导";
		childArray.add(casesourcelist);
		groupArray.add(cvgi1);

		CaseViewGroupItem cvgi2 = new CaseViewGroupItem();
		cvgi2.groupname = "实验答案";
		List<CaseGuideDocItem> cvcil2 = new ArrayList<CaseGuideDocItem>();
		CaseGuideDocItem cvci2 = new CaseGuideDocItem();
		cvci2.guideDocName = "查看答案";
		cvcil2.add(cvci2);
		childArray.add(cvcil2);
		groupArray.add(cvgi2);
		
		CaseViewGroupItem cvgi3 = new CaseViewGroupItem();
		cvgi3.groupname = "测试视频";
		List<CaseGuideDocItem> cvcil3 = new ArrayList<CaseGuideDocItem>();
		for (int i = 0; i < 2; i++) {
			CaseGuideDocItem cvci = new CaseGuideDocItem();
			cvci.guideDocName = "视频  " + (i + 1);
			cvcil3.add(cvci);
		}
		childArray.add(cvcil3);
		groupArray.add(cvgi3);
		
		map.put("group", groupArray);
		map.put("child", childArray);
		return map;
	}

	@Override
	protected void onPostExecute(Map result) {
		super.onPostExecute(result);
		adapter.refresh((List<CaseViewGroupItem>) (result.get("group")),
				(List<List<CaseGuideDocItem>>) (result.get("child")));
	}
}
