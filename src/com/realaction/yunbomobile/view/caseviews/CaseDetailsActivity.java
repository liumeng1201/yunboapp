package com.realaction.yunbomobile.view.caseviews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lm.filebrowser.FileBrowserActivity;
import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseSources;

public class CaseDetailsActivity extends Activity {
	private Context context;
	private long caseId;
	private TextView caseName;
	private TextView caseDes;
	private DBService dbService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_casedetails);
		context = CaseDetailsActivity.this;
		dbService = new DBService(context);
		caseName = (TextView) findViewById(R.id.casedetail_casename);
		caseDes = (TextView) findViewById(R.id.casedetail_casedes);

		Intent intent = getIntent();
		caseId = intent.getLongExtra("caseId", -1);

		CaseItem caseitem = dbService.findCaseByCaseId(caseId);

		caseName.setText("������:" + caseitem.caseName);
		caseDes.setText("����֪ʶ��:" + caseitem.keyWords.replaceAll("#", "��") + "\n�����߽�ɫ:"
				+ caseitem.devRoleName + "\n�ڿ���ʦ:" + caseitem.teacherName);

		AsyncTaskGetCaseSources async = new AsyncTaskGetCaseSources(context);
		async.execute(new String[] { String.valueOf(caseId) });
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}

	public void viewCaseGuide(View v) {
		Intent intent = new Intent(context, FileBrowserActivity.class);
		// ʵ��ָ����
		intent.putExtra("opt", 1);
		intent.putExtra("caseId", caseId);
		startActivity(intent);
	}

	public void viewCaseDoc(View v) {
		Intent intent = new Intent(context, FileBrowserActivity.class);
		// ʵ���
		intent.putExtra("opt", 2);
		intent.putExtra("caseId", caseId);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}
