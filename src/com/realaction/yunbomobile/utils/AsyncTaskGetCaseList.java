package com.realaction.yunbomobile.utils;

import java.util.List;

import android.os.AsyncTask;

import com.realaction.yunbomobile.moddel.CaseItem;

public class AsyncTaskGetCaseList extends AsyncTask<String, Integer, List<CaseItem>> {
	
	public AsyncTaskGetCaseList() {
		
	}

	@Override
	protected List<CaseItem> doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(List<CaseItem> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
