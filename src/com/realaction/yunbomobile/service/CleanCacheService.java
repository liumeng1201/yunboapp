package com.realaction.yunbomobile.service;

import java.io.File;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
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
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		cleancache();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * ��������ļ����������е�ֵʱ��ɾ�����绺����ļ�
	 */
	private void cleancache() {
		SharedPreferences apppre = getSharedPreferences(getPackageName() + "_preferences", 1);
		String maxsize = apppre.getString("pref_setting_cache_memory", "2");
		long max = 0;
		if (maxsize.equals("1")) {
			max = 100 * 1024 * 1024;
		} else if (maxsize.equals("2")) {
			max = 200 * 1024 * 1024;
		} else if (maxsize.equals("3")) {
			max = 300 * 1024 * 1024;
		} else if (maxsize.equals("5")) {
			max = 500 * 1024 * 1024;
		}
		long current = FileUtils.getFolderSize(new File(AppInfo.base_dir));
		if (current > max) {
			do {
				deleteOldCase();
			} while(FileUtils.getFolderSize(new File(AppInfo.base_dir)) > max);
		} else {
		}
	}
	
	/**
	 * ɾ������ʱ�������һ��case
	 */
	private void deleteOldCase() {
		CaseItem item = dbService.findOldestCase();
		if (item != null) {
			String path = AppInfo.base_dir + "/" + item.casedir;
			FileUtils.delFileAndFolder(path, true);
			updateDatabase(item);
		}
	}
	
	/**
	 * ɾ���ϳ�ʱ��Ļ���ʱͬ���������ݿ�
	 * 
	 * @param item
	 *            ��Ҫ���µ�case
	 */
	private void updateDatabase(CaseItem item) {
		dbService.beginTransaction();
		try {
			String caseid = String.valueOf(item.caseId);
			List<CaseGuideDocItem> guidedoclist = dbService
					.findCaseGuideDocsBycaseId(caseid);
			for (CaseGuideDocItem guidedoc : guidedoclist) {
				guidedoc.isDownload = 0;
				dbService.updateCaseGuideDoc(guidedoc);
			}
			List<CaseDocItem> doclist = dbService.findCaseDocsBycaseId(caseid);
			for (CaseDocItem doc : doclist) {
				doc.isDownload = 0;
				dbService.updateCaseDoc(doc);
			}
			dbService.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbService.endTransaction();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}