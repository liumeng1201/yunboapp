package com.realaction.yunbomobile.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
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
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.moddel.CaseViewGroupItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseSourceList;
import com.realaction.yunbomobile.view.caseviews.CaseViewFragment;
import com.realaction.yunbomobile.view.caseviews.VideoViewActivity;

/**
 * 课程案例资源查看界面
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
	
	// 用来保存由异步任务获取的课程案例资源列表
	private Map map;
	// 案例资源分类目录列表,如:实验指导书、实验答案等分类目录
	private List<CaseViewGroupItem> groupArray;
	// 案例资源列表,如:实验指导书分类下的各个指导书
	private List<List<CaseGuideDocItem>> childArray;
	private DrawerCaseViewExpandableAdapter expandableAdapter;
	private ExpandableListView mDrawerListExpandable;
	private long caseId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caseview);
		context = CaseViewActivity.this;
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
		mDrawerListExpandable
				.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition, long id) {
						if (groupPosition == 1) {
							Intent intent = new Intent(context, VideoViewActivity.class);
							intent.putExtra("filepath", "/sdcard/bbb.mp4");
							startActivity(intent);
						} else {
							/*-------------------------------------------------------*/
							FinalHttp fh = new FinalHttp();
							final String download_url = AppInfo.base_url + "/"
									+ (childArray.get(groupPosition)).get(childPosition).guideTmp;
							Log.d(TAG, "download_url = " + download_url);
							HttpHandler handler = fh.download(download_url,
									"/mnt/sdcard/" + (childArray.get(groupPosition)).get(childPosition).guideDocName,
									true, new AjaxCallBack<File>() {
										@Override
										public void onStart() {
											super.onStart();
											Log.d(TAG, "start download --> " + download_url);
										}

										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Log.d(TAG, "fail to download --> " + download_url);
										}

										@Override
										public void onLoading(long count, long current) {
											super.onLoading(count, current);
											Log.d(TAG, "下载进度: " + current + "/" + count);
										}

										@Override
										public void onSuccess(File t) {
											super.onSuccess(t);
											Log.d(TAG, "下载完成: " + (t == null ? "null" : t.getAbsoluteFile().toString()));
										}
									});
							/*-------------------------------------------------------*/
							Fragment fragment = new CaseViewFragment();
							Bundle bundle = new Bundle();
							bundle.putString("filepath", "/sdcard/aa.pdf");
							fragment.setArguments(bundle);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();

							// update selected item and title, then close the drawer
							setTitle(childArray.get(groupPosition).get(childPosition).guideDocName);
							mDrawerLayout.closeDrawer(mDrawerListExpandable);
						}
						return true;
					}
				});

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
		if (AppInfo.network_avabile) {
			// 如果网络可用则以异步任务的方式获取资源
			AsyncTaskGetCaseSourceList async = new AsyncTaskGetCaseSourceList(context, expandableAdapter);
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
			CaseViewGroupItem groupitem = new CaseViewGroupItem();
			groupitem.groupname = "实验指导书";
			groupArray.add(groupitem);
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
