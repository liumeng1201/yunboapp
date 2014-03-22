package com.realaction.yunbomobile.service;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.FileUtils;

public class CleanCacheService extends Service {
	private DBService dbService;
	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = CleanCacheService.this;
		dbService = new DBService(context);
		Log.d("lm", "start service oncreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("lm", "start service onstartcommand");
		cleancache();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * 当缓存的文件大于设置中的值时则删除最早缓存的文件
	 */
	private void cleancache() {
		SharedPreferences apppre = getSharedPreferences(getPackageName() + "_preferences", 1);
		String maxsize = apppre.getString("pref_setting_cache_memory", "2");
		long max = 0;
		if (maxsize.equals("1")) {
			max = 100 * 1024;
		} else if (maxsize.equals("2")) {
			max = 200 * 1024;
		} else if (maxsize.equals("3")) {
			max = 300 * 1024;
		} else if (maxsize.equals("5")) {
			max = 500 * 1024;
		}
		long current = FileUtils.getFolderSize(new File(AppInfo.base_dir));
		if (current > max) {
			Log.d("lm", "current > max");
			do {
				deleteOldCase();
			} while(FileUtils.getFolderSize(new File(AppInfo.base_dir)) > max);
		} else {
			Log.d("lm", "current < max");
		}
	}
	
	/**
	 * 删除插入时间最早的一个case
	 */
	private void deleteOldCase() {
		CaseItem item = dbService.findOldestCase();
		if (item != null) {
			FileUtils.delFileAndFolder(item.casedir, true);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}