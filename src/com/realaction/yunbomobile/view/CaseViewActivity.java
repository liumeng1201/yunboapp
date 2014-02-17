package com.realaction.yunbomobile.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import com.realaction.yunbomobile.moddel.CaseSourcesItem;
import com.realaction.yunbomobile.moddel.CaseViewGroupItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.CaseSourcesUtils;
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
	private Handler mHandler;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	private List<CaseSourcesItem> cvcil1;

	// ������Դ����Ŀ¼�б�,��:ʵ��ָ���顢ʵ��𰸵ȷ���Ŀ¼
	private List<CaseViewGroupItem> groupArray;
	// ������Դ�б�,��:ʵ��ָ��������µĸ���ָ����
	private List<List<CaseSourcesItem>> childArray;
	private DrawerCaseViewExpandableAdapter expandableAdapter;
	private ExpandableListView mDrawerListExpandable;
	
	private long caseId;
	
	private String url = AppInfo.base_url + "/formobile/formobileGetCaseSources.action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caseview);
		context = CaseViewActivity.this;
		mHandler = new Handler();
		mTitle = mDrawerTitle = getTitle();
		groupArray = new ArrayList<CaseViewGroupItem>();
		childArray = new ArrayList<List<CaseSourcesItem>>();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_caseview);
		mDrawerListExpandable = (ExpandableListView) findViewById(R.id.left_drawer_caseview_expandable);
		Intent intent = getIntent();
		caseId = intent.getLongExtra("caseId", -1);
		
		loadCaseSourceList();
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,	GravityCompat.START);
		mDrawerListExpandable
				.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						if (groupPosition == 1) {
							Intent intent = new Intent(context, VideoViewActivity.class);
							intent.putExtra("filepath", "/sdcard/bbb.mp4");
							startActivity(intent);
						} else {
							/*-------------------------------------------------------*/
							FinalHttp fh = new FinalHttp();
							final String download_url = AppInfo.base_url + "/" + cvcil1.get(childPosition).guideTmp;
							Log.d(TAG, "download_url = " + download_url);
							HttpHandler handler = fh.download(download_url,
									"/mnt/sdcard/" + cvcil1.get(childPosition).guideDocName,
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
											Log.d(TAG, "���ؽ���: " + current + "/" + count);
										}

										@Override
										public void onSuccess(File t) {
											super.onSuccess(t);
											Log.d(TAG, "�������: " + (t == null ? "null" : t.getAbsoluteFile().toString()));
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
	
	// ���ؿγ̰�����Դ�����б�
	private void loadCaseSourceList() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				CaseSourcesUtils csu = new CaseSourcesUtils(context);
				List<NameValuePair> datas = new ArrayList<NameValuePair>();
				datas.add(new BasicNameValuePair("caseId", Long.toString(caseId)));
				cvcil1 = csu.getCaseSourcesList(url, datas);

				// TODO ���������Զ���ȡҪ��ʾ�Ĳ˵���
				CaseViewGroupItem cvgi1 = new CaseViewGroupItem();
				cvgi1.groupname = "ʵ��ָ����";
				childArray.add(cvcil1);
				groupArray.add(cvgi1);

				CaseViewGroupItem cvgi4 = new CaseViewGroupItem();
				cvgi4.groupname = "��Ƶ";
				List<CaseSourcesItem> cvcil4 = new ArrayList<CaseSourcesItem>();
				for (int i = 0; i < 2; i++) {
					CaseSourcesItem cvci = new CaseSourcesItem();
					cvci.guideDocName = "��Ƶ  " + (i + 1);
					cvcil4.add(cvci);
				}
				childArray.add(cvcil4);
				groupArray.add(cvgi4);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mDrawerListExpandable.setAdapter(new DrawerCaseViewExpandableAdapter(context, groupArray, childArray));
					}
				});
			}
		}.start();
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
}
