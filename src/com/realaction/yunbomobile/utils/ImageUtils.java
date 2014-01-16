package com.realaction.yunbomobile.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ͼƬ��������
 * 
 * @author liumeng
 */
public class ImageUtils {
	/**
	 * ��ȡ����ͼƬ��ת��Ϊbitmap
	 * 
	 * @param url
	 *            ����ͼƬurl
	 * @return ����ͼƬ����Ӧ��bitmap
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
