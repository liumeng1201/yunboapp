package com.realaction.yunbomobile.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片处理工具类
 * 
 * @author liumeng
 */
public class ImageUtils {
	/**
	 * 获取网络图片并转化为bitmap
	 * 
	 * @param url
	 *            网络图片url
	 * @return 网络图片所对应的bitmap
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myurl;
		Bitmap bitmap = null;
		try {
			myurl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(3000);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
