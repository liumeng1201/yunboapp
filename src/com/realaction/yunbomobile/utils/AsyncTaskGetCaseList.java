package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;

import com.realaction.yunbomobile.adapter.CaseListAdpater;
import com.realaction.yunbomobile.moddel.CaseItem;

public class AsyncTaskGetCaseList extends AsyncTask<String, Integer, List<CaseItem>> {
	private String url_student = AppInfo.base_url + "/formobile/formobileGetStudentCases.action";
	private String url_teacher = AppInfo.base_url + "/formobile/formobileGetTeacherCases.action";
	
	private Context context;
	private CaseListAdpater adapter;
	
	public AsyncTaskGetCaseList(Context context, CaseListAdpater adapter) {
		this.context = context;
		this.adapter = adapter;
	}

	@Override
	protected List<CaseItem> doInBackground(String... params) {
		// params: scoreId userTypeId
		String scoreId = params[0];
		String url = null;
		int userTypeId = Integer.parseInt(params[1]);
		switch (userTypeId) {
		case 10:
			// 学生
			url = url_student;
			break;
		case 20:
		case 40:
			// 老师
			url = url_teacher;
			break;
		}
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		datas.add(new BasicNameValuePair("scoreId", scoreId));
		CasesUtils cu = new CasesUtils(context, scoreId);
		return cu.getCasesList(url_student, datas);
	}

	@Override
	protected void onPostExecute(List<CaseItem> result) {
		super.onPostExecute(result);
		// 通知案例列表更新数据
		adapter.refresh(result);
	}

}
