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

	/**
	 * 从资源文件中获取Bitmap
	 * 
	 * @param context
	 * @param path
	 *            图片文件在assets目录下的路径
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
			// 如果没有设置用户头像或用户头像为空则使用默认的用户头像
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.default_portrait);
			}
		}
		return bitmap;
	}
}
