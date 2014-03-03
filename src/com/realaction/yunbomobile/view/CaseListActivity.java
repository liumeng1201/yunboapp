package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
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

/**
 * �γ̰����б����
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
		dbService = new DBService(context);
		userTypeId = (new UserUtils(context)).getUserTypeId();
		// ��ȡ�ӿγ��б������ת������Intent
		Intent intent = getIntent();
		scoreId = intent.getStringExtra("scoreId");
		courseName = intent.getStringExtra("courseName");

		course_name = (TextView) findViewById(R.id.caselist_coursename);
		caselist_list = (ListView) findViewById(R.id.caselist_list);
		caselists = new ArrayList<CaseItem>();
		adapter = new CaseListAdpater(context, caselists);
		course_name.setText("�γ�:" + courseName);
		caselist_list.setAdapter(adapter);
		
		init();
	}
	
	/**
	 * ���ð�����Դ�б��������Ҫ�ĸ�������
	 */
	private void init() {
		dialog.show();
		if (AppInfo.network_avabile) {
			// ������õ�ʱ��ͨ�������ȡ�γ̰������ݲ�֪ͨadapter����
			AsyncTaskGetCaseList async = new AsyncTaskGetCaseList(context, handler, adapter);
			String[] params = new String[] { scoreId, String.valueOf(userTypeId)};
			try {
				caselists = async.execute(params).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// ���粻����ʱͨ�����ݿ��ȡ����İ�������
			caselists = dbService.findCasesByscoreId(scoreId);
			dialog.dismiss();
			if (caselists != null) {
				adapter.refresh(caselists);
			}
		}
		
		caselist_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
				long caseid = caselists.get(position).caseId;
				long scoreid = Long.parseLong(caselists.get(position).scoreId);
				Time time = new Time("GMT+8");
				time.setToNow();
				long currenttime = time.toMillis(false);
				if (AppInfo.network_avabile) {
					dbService.updateCaseCount(caseid, scoreid);
					dbService.updateCaseTime(caseid, scoreid, currenttime);
					Intent intent = new Intent(context, CaseViewActivity.class);
					intent.putExtra("caseId", caseid);
					startActivity(intent);
				} else if (dbService.findCaseGuideDocsBycaseId(String.valueOf(caseid)) != null) {
					dbService.updateCaseCount(caseid, scoreid);
					dbService.updateCaseTime(caseid, scoreid, currenttime);
					Intent intent = new Intent(context, CaseViewActivity.class);
					intent.putExtra("caseId", caseid);
					startActivity(intent);
				} else {
					Toast.makeText(context, R.string.no_res_cache_can_not_open, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}
