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
	// http��������
	private static HttpParams httpParams;
	// http�ͻ�����
	private static HttpClient client;

	// post���󷽷�
	public static InputStream sendDataByPost(String url,
			List<NameValuePair> datas) {
		// ��ʼ��http��������
		httpParams = new BasicHttpParams();
		// ����http��Ӧ��ʱ����
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		// ȡ��http�ͻ�����
		client = new DefaultHttpClient(httpParams);
		// ����http post�������
		HttpPost post = new HttpPost(url);
		// http��Ӧ����
		HttpResponse resp = null;
		// ����������
		InputStream is = null;
		try {
			// ����post��������ݺ����ݸ�ʽ
			post.setEntity(new UrlEncodedFormEntity(datas, HTTP.UTF_8));
			// ִ��post���󣬻�ȡ��Ӧ��Ϣ
			resp = client.execute(post);
			// http�������ӳɹ�
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ����Ӧ����ʵ��
				HttpEntity httpEntity = resp.getEntity();
				// ȡ��������
				is = httpEntity.getContent();
			} else {
				// http����ʧ��
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

	// ����ת��Ϊ�ַ�������
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			// ��ӻ��з�
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// ɾ�����һ�����з�
				sb.deleteCharAt(sb.lastIndexOf("\n"));
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// �������ʱ�����İ������ݵ�toast
	public static void showToast(final Context context, Handler handler,
			final String message) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// ���ַ���ת��Ϊutf-8����
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