package com.realaction.yunbomobile.view;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.CaseDocTb;
import com.realaction.yunbomobile.db.CaseGuideDocTb;
import com.realaction.yunbomobile.db.CaseTb;
import com.realaction.yunbomobile.db.CourseTb;
import com.realaction.yunbomobile.db.DBOpenHelper;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.FileUtils;
import com.realaction.yunbomobile.utils.MyDialog;

/**
 * 设置界面
 * 
 * @author liumeng
 */
public class SettingsPage extends PreferenceFragment {
	private Context context;
	private MyDialog mydialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		mydialog = new MyDialog(context);
		mydialog.create();
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals("pref_setting_offline")) {
			// 离线模式
		} else if (preference.getKey().equals("pref_setting_cache_memory")) {
			// 设置缓存大小
		} else if (preference.getKey().equals("pref_setting_clean_cache")) {
			// 清除缓存
			AlertDialog.Builder dialog = new Builder(context);
			dialog.setTitle(R.string.notice);
			long size = FileUtils.getFolderSize(new File(AppInfo.base_dir));
			String msg = getString(R.string.cache_size) + size / 1024 + "KB"
					+ "\n" + getString(R.string.clean_cache);
			dialog.setMessage(msg);
			dialog.setNegativeButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mydialog.show();
					clearTable(CourseTb.COURSETB);
					clearTable(CaseTb.CASETB);
					clearTable(CaseGuideDocTb.CASEGUIDEDOCTB);
					clearTable(CaseDocTb.CASEDOCTB);
					FileUtils.delFileAndFolder(AppInfo.base_dir, true);
					mydialog.dismiss();
					Toast.makeText(context,
							getString(R.string.clean_cache_success),
							Toast.LENGTH_SHORT).show();
				}
			});
			dialog.setPositiveButton(R.string.cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.create();
			dialog.show();
		} 
//		else if (preference.getKey().equals("pref_setting_notice_on")) {
//			// 上课提醒
//		} else if (preference.getKey().equals("pref_setting_notice_time")) {
//			// 提醒时间
//		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private void clearTable(String tablename) {
		String sql = "DELETE FROM " + tablename + ";";
		DBOpenHelper dbHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		revertSeq(tablename);
		dbHelper.close();
	}
	
	private void revertSeq(String tablename) {
		String sql = "update sqlite_sequence set seq=0 where name='" + tablename + "';";
		DBOpenHelper dbHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		dbHelper.close();
	}
}
