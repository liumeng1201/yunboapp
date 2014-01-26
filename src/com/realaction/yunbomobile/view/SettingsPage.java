package com.realaction.yunbomobile.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.realaction.yunbomobile.R;

/**
 * 设置界面
 * 
 * @author liumeng
 */
public class SettingsPage extends PreferenceFragment {
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
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
			// 清楚缓存
			AlertDialog.Builder dialog = new Builder(context);
			dialog.setTitle(R.string.notice);
			dialog.setMessage(R.string.clean_cache);
			dialog.setNegativeButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
		} else if (preference.getKey().equals("pref_setting_notice_on")) {
			// 上课提醒
		} else if (preference.getKey().equals("pref_setting_notice_time")) {
			// 提醒时间
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
