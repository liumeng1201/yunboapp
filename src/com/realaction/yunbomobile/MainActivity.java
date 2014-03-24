package com.realaction.yunbomobile;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.realaction.yunbomobile.adapter.DrawerListAdapter;
import com.realaction.yunbomobile.service.CleanCacheService;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.MyDialog;
import com.realaction.yunbomobile.view.HomePage;
import com.realaction.yunbomobile.view.MyCoursePage;
import com.realaction.yunbomobile.view.SettingsPage;

public class MainActivity extends Activity {
	private Context context;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mMenuTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = MainActivity.this;
		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new DrawerListAdapter(context, mMenuTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
		
		if (savedInstanceState == null) {
			selectItem(0);
		}
		
		// 新手指导功能
		SharedPreferences ug = context.getSharedPreferences("user_guide", 0);
		boolean firstlogin = ug.getBoolean("user_first_login", false);
		if (!firstlogin) {
			SharedPreferences.Editor uge = ug.edit();
			uge.putBoolean("user_first_login", true);
			uge.commit();
			startActivity(new Intent(context, UserGuideActivity.class));
		}
		
		// 自动更新功能
		Intent intent = getIntent();
		boolean isUpdate = intent.getBooleanExtra("isupdate", false);
		String updatemsg = intent.getStringExtra("updatemsg");
		final String updatefile = intent.getStringExtra("updatefile");
		if (isUpdate) {
			StringBuffer sb = new StringBuffer();
			sb.append("当前版本:");
			try {
				String currentversion = context.getPackageManager()
						.getPackageInfo("com.realaction.yunbomobile", 0).versionName;
				sb.append(currentversion).append("\n");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			sb.append(updatemsg).append("\n");
			sb.append("是否更新?");
			Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新")
					.setMessage(sb.toString())
					.setPositiveButton("更新", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dlAndInstall(updatefile);
						}
					}).setNegativeButton("暂不更新", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create();
			dialog.show();
		}
	}
	
	private void dlAndInstall(String updatefile) {
		final MyDialog dlDialog = new MyDialog(context);
		dlDialog.create();
		dlDialog.show();
		FinalHttp fh = new FinalHttp();
		final String url = AppInfo.base_url + "/update/" + updatefile;
		final String target = AppInfo.base_dir + "/" + updatefile;
		HttpHandler handler = fh.download(url, target,
				new AjaxCallBack<File>() {
					@Override
					public void onStart() {
						super.onStart();
						File file = new File(target);
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						dlDialog.dismiss();
						Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(File t) {
						super.onSuccess(t);
						Log.d("update", "start success");
						dlDialog.dismiss();
						Dialog installDialog = new AlertDialog.Builder(context)
								.setTitle("安装")
								.setMessage("是否安装新的应用")
								.setPositiveButton("确定", new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										intent.setDataAndType(
												Uri.fromFile(new File(target)),
												"application/vnd.android.package-archive");
										startActivity(intent);
										MainActivity.this.finish();
									}
								})
								.setNegativeButton("取消", new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create();
						installDialog.show();
					}
				});
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomePage();
			break;
		case 1:
			fragment = new MyCoursePage();
			break;
		/*	
		case 2:
			fragment = new OpenCoursePage();
			break;
		case 3:
			fragment = new SchedulePage();
			break;
		case 4:
			fragment = new NoticePage();
			break;
		*/	
		case 2:
			fragment = new SettingsPage();
			break;
		default:
			fragment = new HomePage();
			break;
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle(R.string.notice);
			dialog.setMessage(R.string.quit_confirm);
			dialog.setPositiveButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(context, CleanCacheService.class);
					Log.d("lm", "start service");
					startService(intent);
					MainActivity.this.finish();
				}
			});
			dialog.setNegativeButton(R.string.cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.create().show();
		}
		return super.onKeyDown(keyCode, event);
	}

}