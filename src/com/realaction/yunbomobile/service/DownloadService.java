package com.realaction.yunbomobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 缓存课程案例资源的服务
 * 
 * @author liumeng
 */
public class DownloadService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
