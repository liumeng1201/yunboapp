package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.CaseListAdpater;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseList;
import com.realaction.yunbomobile.utils.UserUtils;

/**
 * 课程案例列表界面
 * 
 * @author liumeng
 */
public class CaseListActivity extends Activity {
	private Context context;
	private int userTypeId;
	private String scoreId;
	private ListView caselist_list;
	private TextView course_name;
	private String courseName;
	
	private List<CaseItem> caselists;
	private CaseListAdpater adapter;
	private DBService dbService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caselist);
		context = CaseListActivity.this;
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
		course_name.setText("课程:" + courseName);
		caselist_list.setAdapter(adapter);
		
		init();
	}
	
	/**
	 * 设置案例资源列表界面所需要的各种数据
	 */
	private void init() {
		if (AppInfo.network_avabile) {
			// 网络可用的时候通过网络获取课程案例数据并通知adapter更新
			/*
			// 方式一
			final Handler mHandler = new Handler();
			final String url_student = AppInfo.base_url + "/formobile/formobileGetStudentCases.action";
			final String url_teacher = AppInfo.base_url + "/formobile/formobileGetTeacherCases.action";
			new Thread() {
				@Override
				public void run() {
					super.run();
					List<NameValuePair> datas = new ArrayList<NameValuePair>();
					datas.add(new BasicNameValuePair("scoreId", scoreId));
					CasesUtils cu = new CasesUtils(context, scoreId);
					caselists = cu.getCasesList(url_student, datas);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							adapter.refresh(caselists);
						}
					});
				}
			}.start(); */
			// 方式二
			AsyncTaskGetCaseList async = new AsyncTaskGetCaseList(context, adapter);
			String[] params = new String[] { scoreId, String.valueOf(userTypeId)};
			try {
				caselists = async.execute(params).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// 网络不可用时通过数据库获取缓存的案例数据
			caselists = dbService.findCasesByscoreId(scoreId);
			if (caselists != null) {
				adapter.refresh(caselists);
			}
		}
		
		caselist_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
				Intent intent = new Intent(context, CaseViewActivity.class);
				intent.putExtra("caseId", caselists.get(position).caseId);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}
