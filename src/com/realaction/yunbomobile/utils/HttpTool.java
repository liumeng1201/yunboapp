package com.realaction.yunbomobile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class HttpTool {
	// http参数对象
	private static HttpParams httpParams;
	// http客户对象
	private static HttpClient client;

	// post请求方法
	public static InputStream sendDataByPost(String url,
			List<NameValuePair> datas) {
		// 初始化http参数对象
		httpParams = new BasicHttpParams();
		// 设置http响应超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		// 取得http客户对象
		client = new DefaultHttpClient(httpParams);
		// 发送http post请求对象
		HttpPost post = new HttpPost(url);
		// http响应对象
		HttpResponse resp = null;
		// 返回数据流
		InputStream is = null;
		try {
			// 设置post对象的数据和数据格式
			post.setEntity(new UrlEncodedFormEntity(datas, HTTP.UTF_8));
			// 执行post请求，获取响应信息
			resp = client.execute(post);
			// http请求连接成功
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得响应数据实体
				HttpEntity httpEntity = resp.getEntity();
				// 取得数据流
				is = httpEntity.getContent();
			} else {
				// http请求失败
				is = null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	// 将流转换为字符串方法
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			// 添加换行符
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 删除最后一个换行符
				sb.deleteCharAt(sb.lastIndexOf("\n"));
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 网络访问时弹出的包含内容的toast
	public static void showToast(final Context context, Handler handler,
			final String message) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// 将字符串转换为utf-8编码
	public static String stringToUTF8(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		String tmpStr = "";
		String utf8Str = "";
		try {
			tmpStr = new String(sb.toString().getBytes("UTF-8"));
			utf8Str = URLEncoder.encode(tmpStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return utf8Str;
	}
}