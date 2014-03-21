package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncTaskGetCaseSources extends AsyncTask<String, Integer, String> {
	private String url = AppInfo.base_url + "/formobile/formobileGetCaseSources.action";
	private Context context;

	public AsyncTaskGetCaseSources(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		long caseId = Long.parseLong(params[0]);
		CaseSourcesUtils csu = new CaseSourcesUtils(context);
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		datas.add(new BasicNameValuePair("caseId", Long.toString(caseId)));
		csu.getCaseSources(url, datas);
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

}
