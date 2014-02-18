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
 * ��ȡ�γ̰�����Դ�б���֪ͨadapter��������
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

		// TODO ���������Զ���ȡҪ��ʾ�Ĳ˵���,��Ҫ��ʾ�����ݷ�����
		CaseViewGroupItem cvgi1 = new CaseViewGroupItem();
		cvgi1.groupname = "ʵ��ָ����";
		childArray.add(casesourcelist);
		groupArray.add(cvgi1);

		CaseViewGroupItem cvgi4 = new CaseViewGroupItem();
		cvgi4.groupname = "��Ƶ";
		List<CaseGuideDocItem> cvcil4 = new ArrayList<CaseGuideDocItem>();
		for (int i = 0; i < 2; i++) {
			CaseGuideDocItem cvci = new CaseGuideDocItem();
			cvci.guideDocName = "��Ƶ  " + (i + 1);
			cvcil4.add(cvci);
		}
		childArray.add(cvcil4);
		groupArray.add(cvgi4);
		
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