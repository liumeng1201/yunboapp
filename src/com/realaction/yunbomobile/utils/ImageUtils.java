package com.realaction.yunbomobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.realaction.yunbomobile.R;

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

	/**
	 * ����Դ�ļ��л�ȡBitmap
	 * 
	 * @param context
	 * @param path
	 *            ͼƬ�ļ���assetsĿ¼�µ�·��
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromRes(Context context, String path) {
		Bitmap bitmap = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(path);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// ���û�������û�ͷ����û�ͷ��Ϊ����ʹ��Ĭ�ϵ��û�ͷ��
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.default_portrait);
			}
		}
		return bitmap;
	}
}
