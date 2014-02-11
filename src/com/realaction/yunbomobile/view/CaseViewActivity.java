package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.DrawerCaseViewExpandableAdapter;
import com.realaction.yunbomobile.utils.CaseViewChildItem;
import com.realaction.yunbomobile.utils.CaseViewGroupItem;
import com.realaction.yunbomobile.view.caseviews.CaseViewFragment;
import com.realaction.yunbomobile.view.caseviews.VideoViewActivity;

public class CaseViewActivity extends Activity {
	private Context context;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private List<CaseViewGroupItem> groupArray;
	private List<List<CaseViewChildItem>> childArray;
	private DrawerCaseViewExpandableAdapter expandableAdapter;
	private ExpandableListView mDrawerListExpandable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caseview);

		context = CaseViewActivity.this;
		mTitle = mDrawerTitle = getTitle();

		// TODO 根据内容自动获取要显示的菜单项
		groupArray = new ArrayList<CaseViewGroupItem>();
		childArray = new ArrayList<List<CaseViewChildItem>>();
		CaseViewGroupItem cvgi1 = new CaseViewGroupItem();
		cvgi1.groupname = "实验指导书";
		List<CaseViewChildItem> cvcil1 = new ArrayList<CaseViewChildItem>();
		for (int i = 0; i < 3; i++) {
			CaseViewChildItem cvci = new CaseViewChildItem();
			cvci.childname = "实验指导书  " + (i + 1);
			cvcil1.add(cvci);
		}
		childArray.add(cvcil1);
		groupArray.add(cvgi1);
		CaseViewGroupItem cvgi2 = new CaseViewGroupItem();
		cvgi2.groupname = "实验报告";
		List<CaseViewChildItem> cvcil2 = new ArrayList<CaseViewChildItem>();
		for (int i = 0; i < 3; i++) {
			CaseViewChildItem cvci = new CaseViewChildItem();
			cvci.childname = "实验报告  " + (i + 1);
			cvcil2.add(cvci);
		}
		childArray.add(cvcil2);
		groupArray.add(cvgi2);
		CaseViewGroupItem cvgi3 = new CaseViewGroupItem();
		cvgi3.groupname = "答案";
		List<CaseViewChildItem> cvcil3 = new ArrayList<CaseViewChildItem>();
		for (int i = 0; i < 3; i++) {
			CaseViewChildItem cvci = new CaseViewChildItem();
			cvci.childname = "答案  " + (i + 1);
			cvcil3.add(cvci);
		}
		childArray.add(cvcil3);
		groupArray.add(cvgi3);
		CaseViewGroupItem cvgi4 = new CaseViewGroupItem();
		cvgi4.groupname = "视频";
		List<CaseViewChildItem> cvcil4 = new ArrayList<CaseViewChildItem>();
		for (int i = 0; i < 2; i++) {
			CaseViewChildItem cvci = new CaseViewChildItem();
			cvci.childname = "视频  " + (i + 1);
			cvcil4.add(cvci);
		}
		childArray.add(cvcil4);
		groupArray.add(cvgi4);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_caseview);
		mDrawerListExpandable = (ExpandableListView) findViewById(R.id.left_drawer_caseview_expandable);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerListExpandable.setAdapter(new DrawerCaseViewExpandableAdapter(
				context, groupArray, childArray));
		mDrawerListExpandable
				.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						if (groupPosition == 3) {
							Intent intent = new Intent(context, VideoViewActivity.class);
							intent.putExtra("filepath", "/sdcard/bbb.mp4");
							startActivity(intent);
						} else {
							Fragment fragment = new CaseViewFragment();
							Bundle bundle = new Bundle();
							bundle.putString("filepath", "/sdcard/aa.pdf");
							fragment.setArguments(bundle);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.content_frame_caseview, fragment).commit();

							// update selected item and title, then close the drawer
							setTitle(childArray.get(groupPosition).get(childPosition).childname);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
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
