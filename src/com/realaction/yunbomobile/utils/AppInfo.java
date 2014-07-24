package com.realaction.yunbomobile.utils;

import android.os.Environment;

public class AppInfo {
	// 服务器端基础url
//	public static final String base_url = "http://192.168.2.231:8080";
//	public static final String base_url = "http://192.168.2.31:10000";
//	public static final String base_url = "http://192.168.2.50:8080";
	public static final String base_url = "http://www.realboard.cn:10000";
	
	// 网络是否可用标志
	public static boolean network_avabile = true;
	// 软件基础目录
//	public static final String base_dir = "/mnt/sdcard/.yunbo";
	public static final String base_dir = Environment.getExternalStorageDirectory().toString() + "/" + ".yunbo";
}
