package com.realaction.yunbomobile.view;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.lm.filebrowser.FileBrowserActivity;
import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.DrawerCaseViewExpandableAdapter;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.moddel.CaseViewGroupItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseSourceList;
import com.realaction.yunbomobile.utils.MyDialog;
import com.realaction.yunbomobile.view.caseviews.AnswerViewFragment;
import com.realaction.yunbomobile.view.caseviews.CaseViewFragment;
import com.realaction.yunbomobile.view.caseviews.VideoViewActivity;

/**
 * 课程案例资源查看界面
 * 
 * @author liumeng
 */
public class CaseViewActivity extends Activity {
	private static final String TAG = "CaseViewActivity";
	public static final int CANCEL_DIALOG = 1000;
	private Context context;
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
	private DBService dbService;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private long caseId;
	// 用来保存由异步任务获取的课程案例资源列表
	private Map map;
	// 案例资源分类目录列表,如:实验指导书、实验答案等分类目录
	private List<CaseViewGroupItem> groupArray;
	// 案例资源列表,如:实验指导书分类下的各个指导书
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
//				Fragment fragment = new AnswerViewFragment();
//				Bundle bundle = new Bundle();
//				bundle.putLong("caseId", caseId);
//				fragment.setArguments(bundle);
//				FragmentManager fragmentManager = getFragmentManager();
//				fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();
				Intent intent = new Intent(context, FileBrowserActivity.class);
				intent.putExtra("caseId", caseId);
				startActivity(intent);
				setTitle(childArray.get(groupPosition).get(childPosition).guideDocName);
				mDrawerLayout.closeDrawer(mDrawerListExpandable);
			} else {
				String download_url = AppInfo.base_url + "/"
						+ (childArray.get(groupPosition)).get(childPosition).guideDocPath;
				try {
					// 转换下载路径中的空格
					String tmp = URLEncoder.encode(download_url, "UTF-8");
					tmp = tmp.replaceAll("\\+", "%20");
					tmp = tmp.replaceAll("%3A", ":").replaceAll("%2F", "/");
					download_url = tmp;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String target_name = AppInfo.base_dir + "/" + (childArray.get(groupPosition)).get(childPosition).guideDocPath;
				long guideId = (childArray.get(groupPosition)).get(childPosition).guideId;
				Log.d(TAG, "download_url = " + download_url + "\ntarget_name = " + target_name + "\nguide_id = " + guideId);
				
				Bundle bundle = new Bundle();
				bundle.putString("download_url", download_url);
				bundle.putString("target_name", target_name);
				bundle.putLong("guideId", guideId);
				bundle.putLong("caseId", caseId);
				
				if (target_name.toLowerCase().contains(".mp4")
						|| target_name.toLowerCase().contains(".3gp")) {
					// 视频
					Intent intent = new Intent(context, VideoViewActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					// pdf文档资源
					Fragment fragment = new CaseViewFragment();
					fragment.setArguments(bundle);
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();
					setTitle(childArray.get(groupPosition).get(childPosition).guideDocName);
					mDrawerLayout.closeDrawer(mDrawerListExpandable);
				}
			}
			return true;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caseview);
		context = CaseViewActivity.this;
		dialog = new MyDialog(context);
		dialog.create();
		dbService = new DBService(context);
		mTitle = mDrawerTitle = getTitle();
		groupArray = new ArrayList<CaseViewGroupItem>();
		childArray = new ArrayList<List<CaseGuideDocItem>>();
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
	
	// 加载课程案例资源内容列表
	private void loadCaseSourceList() {
		dialog.show();
		if (AppInfo.network_avabile) {
			// 如果网络可用则以异步任务的方式获取资源
			AsyncTaskGetCaseSourceList async = new AsyncTaskGetCaseSourceList(context, handler, expandableAdapter);
			try {
				map = (async.execute(new String[] { String.valueOf(caseId) })).get();
				groupArray = (List<CaseViewGroupItem>) map.get("group");
				childArray = (List<List<CaseGuideDocItem>>) map.get("child");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// 如果网络不可用则从数据库中获取缓存的资源
			List<CaseGuideDocItem> caseguidedoclist = dbService.findCaseGuideDocsBycaseId(String.valueOf(caseId));
			childArray.add(caseguidedoclist);
			CaseViewGroupItem groupguidedoc = new CaseViewGroupItem();
			groupguidedoc.groupname = "实验指导";
			groupArray.add(groupguidedoc);
			
			List<CaseGuideDocItem> answer = new ArrayList<CaseGuideDocItem>();
			answer.add(new CaseGuideDocItem("查看答案"));
			childArray.add(answer);
			CaseViewGroupItem groupanswer = new CaseViewGroupItem();
			groupanswer.groupname = "实验答案";
			groupArray.add(groupanswer);
			
			List<CaseGuideDocItem> video = new ArrayList<CaseGuideDocItem>();
			video.add(new CaseGuideDocItem("查看视频"));
			childArray.add(video);
			CaseViewGroupItem groupvideo = new CaseViewGroupItem();
			groupvideo.groupname = "测试视频";
			groupArray.add(groupvideo);
			
			dialog.dismiss();
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
