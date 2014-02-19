package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.DrawerCaseViewExpandableAdapter;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.moddel.CaseViewGroupItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseSourceList;
import com.realaction.yunbomobile.view.caseviews.AnswerViewFragment;
import com.realaction.yunbomobile.view.caseviews.CaseViewFragment;
import com.realaction.yunbomobile.view.caseviews.VideoViewActivity;

/**
 * �γ̰�����Դ�鿴����
 * 
 * @author liumeng
 */
public class CaseViewActivity extends Activity {
	private static final String TAG = "CaseViewActivity";
	private Context context;
	private DBService dbService;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private long caseId;
	// �����������첽�����ȡ�Ŀγ̰�����Դ�б�
	private Map map;
	private List<CaseDocItem> answerList;
	// ������Դ����Ŀ¼�б�,��:ʵ��ָ���顢ʵ��𰸵ȷ���Ŀ¼
	private List<CaseViewGroupItem> groupArray;
	// ������Դ�б�,��:ʵ��ָ��������µĸ���ָ����
	private List<List<CaseGuideDocItem>> childArray;
	private DrawerCaseViewExpandableAdapter expandableAdapter;
	private ExpandableListView mDrawerListExpandable;
	private OnChildClickListener onChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent,
				View v, int groupPosition, int childPosition, long id) {
			if (groupPosition == 2) {
				Intent intent = new Intent(context, VideoViewActivity.class);
				intent.putExtra("filepath", "/sdcard/bbb.mp4");
				startActivity(intent);
			} else if (groupPosition == 1) {
				Fragment fragment = new AnswerViewFragment();
				Bundle bundle = new Bundle();
				// TODO ���ݸ�AnswerFragment����Ĳ���
				bundle.putLong("caseId", caseId);
				fragment.setArguments(bundle);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();
				setTitle(childArray.get(groupPosition).get(childPosition).guideDocName);
				mDrawerLayout.closeDrawer(mDrawerListExpandable);
			} else {
				final String download_url = AppInfo.base_url + "/"
						+ (childArray.get(groupPosition)).get(childPosition).guideTmp;
				final String target_name = "/mnt/sdcard/" + (childArray.get(groupPosition)).get(childPosition).guideDocName;
				Log.d(TAG, "download_url = " + download_url + "\ntarget_name = " + target_name);
				
				Fragment fragment = new CaseViewFragment();
				Bundle bundle = new Bundle();
				bundle.putString("filepath", "/sdcard/aa.pdf");
				bundle.putString("download_url", download_url);
				bundle.putString("target_name", target_name);
				fragment.setArguments(bundle);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();
				setTitle(childArray.get(groupPosition).get(childPosition).guideDocName);
				mDrawerLayout.closeDrawer(mDrawerListExpandable);
			}
			return true;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caseview);
		context = CaseViewActivity.this;
		dbService = new DBService(context);
		mTitle = mDrawerTitle = getTitle();
		groupArray = new ArrayList<CaseViewGroupItem>();
		childArray = new ArrayList<List<CaseGuideDocItem>>();
		answerList = new ArrayList<CaseDocItem>();
		map = new HashMap<String, Object>();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_caseview);
		mDrawerListExpandable = (ExpandableListView) findViewById(R.id.left_drawer_caseview_expandable);
		expandableAdapter = new DrawerCaseViewExpandableAdapter(context, groupArray, childArray);
		mDrawerListExpandable.setAdapter(expandableAdapter);
		Intent intent = getIntent();
		caseId = intent.getLongExtra("caseId", -1);
		
		loadCaseSourceList();
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,	GravityCompat.START);
		mDrawerListExpandable.setOnChildClickListener(onChildClickListener);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	// ���ؿγ̰�����Դ�����б�
	private void loadCaseSourceList() {
		if (AppInfo.network_avabile) {
			// ���������������첽����ķ�ʽ��ȡ��Դ
			AsyncTaskGetCaseSourceList async = new AsyncTaskGetCaseSourceList(context, expandableAdapter);
			try {
				map = (async.execute(new String[] { String.valueOf(caseId) })).get();
				groupArray = (List<CaseViewGroupItem>) map.get("group");
				childArray = (List<List<CaseGuideDocItem>>) map.get("child");
				answerList = (List<CaseDocItem>) map.get("answer");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// ������粻����������ݿ��л�ȡ�������Դ
			List<CaseGuideDocItem> caseguidedoclist = dbService.findCaseGuideDocsBycaseId(String.valueOf(caseId));
			childArray.add(caseguidedoclist);
			CaseViewGroupItem groupitem1 = new CaseViewGroupItem();
			groupitem1.groupname = "ʵ��ָ��";
			groupArray.add(groupitem1);
			
			List<CaseGuideDocItem> answer = new ArrayList<CaseGuideDocItem>();
			answer.add(new CaseGuideDocItem("�鿴��"));
			childArray.add(answer);
			CaseViewGroupItem groupanswer = new CaseViewGroupItem();
			groupanswer.groupname = "ʵ���";
			groupArray.add(groupanswer);
			
			List<CaseGuideDocItem> video = new ArrayList<CaseGuideDocItem>();
			video.add(new CaseGuideDocItem("�鿴��Ƶ"));
			childArray.add(video);
			CaseViewGroupItem groupvideo = new CaseViewGroupItem();
			groupvideo.groupname = "������Ƶ";
			groupArray.add(groupvideo);
			
			expandableAdapter.refresh(groupArray, childArray);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
	
}
