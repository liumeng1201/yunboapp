package com.realaction.yunbomobile.view.caseviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

		caseName.setText("案例名:" + caseitem.caseName);
		caseDes.setText("包含知识点:" + caseitem.keyWords.replaceAll("#", "、") + "\n开发者角色:"
				+ caseitem.devRoleName + "\n授课老师:" + caseitem.teacherName);

		AsyncTaskGetCaseSources async = new AsyncTaskGetCaseSources(context);
		async.execute(new String[] { String.valueOf(caseId) });
	}

	public void viewCaseGuide(View v) {
		Intent intent = new Intent(context, FileBrowserActivity.class);
		// 实验指导书
		intent.putExtra("opt", 1);
		intent.putExtra("caseId", caseId);
		startActivity(intent);
	}

	public void viewCaseDoc(View v) {
		Intent intent = new Intent(context, FileBrowserActivity.class);
		// 实验答案
		intent.putExtra("opt", 2);
		intent.putExtra("caseId", caseId);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}
