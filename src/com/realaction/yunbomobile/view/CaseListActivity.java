package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.CaseListAdpater;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseList;
import com.realaction.yunbomobile.utils.MyDialog;
import com.realaction.yunbomobile.utils.UserUtils;
import com.realaction.yunbomobile.view.caseviews.CaseDetailsActivity;

/**
 * 课程案例列表界面
 * 
 * @author liumeng
 */
public class CaseListActivity extends Activity {
	public static final int CANCEL_DIALOG = 1000;
	
	private Context context;
	private int userTypeId;
	private String scoreId;
	private ListView caselist_list;
	private TextView course_name;
	private String courseName;
	
	private List<CaseItem> caselists;
	private CaseListAdpater adapter;
	private DBService dbService;
	private MyDialog dialog;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == CANCEL_DIALOG) {
				dialog.dismiss();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caselist);
		context = CaseListActivity.this;
		dialog = new MyDialog(context);
		dialog.create();
		dialog.setMessage(getString(R.string.loading_caselist));
		dbService = new DBService(context);
		userTypeId = (new UserUtils(context)).getUserTypeId();
		// 获取从课程列表界面跳转过来的Intent
		Intent intent = getIntent();
		scoreId = intent.getStringExtra("scoreId");
		courseName = intent.getStringExtra("courseName");

		course_name = (TextView) findViewById(R.id.caselist_coursename);
		caselist_list = (ListView) findViewById(R.id.caselist_list);
		caselists = new ArrayList<CaseItem>();
		adapter = new CaseListAdpater(context, caselists);
		course_name.setText(getString(R.string.course) + ":" + courseName);
		caselist_list.setAdapter(adapter);
		
		init();
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		init();
	}

	/**
	 * 设置案例资源列表界面所需要的各种数据
	 */
	private void init() {
		dialog.show();
		if (AppInfo.network_avabile) {
			// 网络可用的时候通过网络获取课程案例数据并通知adapter更新
			AsyncTaskGetCaseList async = new AsyncTaskGetCaseList(context, handler, adapter);
			String[] params = new String[] { scoreId, String.valueOf(userTypeId)};
			async.execute(params);
		} else {
			// 网络不可用时通过数据库获取缓存的案例数据
			caselists = dbService.findCasesByscoreId(scoreId);
			dialog.dismiss();
			if (caselists != null) {
				adapter.refresh(caselists);
			}
		}
		
		caselist_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
				CaseItem item = (CaseItem) adapter.getItem(position);
				long caseid = item.caseId;
				long scoreid = Long.parseLong(item.scoreId);
				Time time = new Time("GMT+8");
				time.setToNow();
				long currenttime = time.toMillis(false);
				if (AppInfo.network_avabile) {
					dbService.updateCaseCount(caseid, scoreid);
					dbService.updateCaseTime(caseid, scoreid, currenttime);
					Intent intent = new Intent(context, CaseDetailsActivity.class);//CaseViewActivity.class);
					intent.putExtra("caseId", caseid);
					startActivity(intent);
				} else if (dbService.findCaseGuideDocsBycaseId(String.valueOf(caseid)) != null) {
					dbService.updateCaseCount(caseid, scoreid);
					dbService.updateCaseTime(caseid, scoreid, currenttime);
					Intent intent = new Intent(context, CaseDetailsActivity.class);//CaseViewActivity.class);
					intent.putExtra("caseId", caseid);
					startActivity(intent);
				} else {
					Toast.makeText(context, R.string.no_res_cache_can_not_open, Toast.LENGTH_SHORT).show();
				}
			}
		});
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
