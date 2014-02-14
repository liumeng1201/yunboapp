package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;

import com.realaction.yunbomobile.adapter.MyCourseListAdapter;
import com.realaction.yunbomobile.moddel.CourseItem;

/**
 * ��ȡ�γ��б�����adapter��������
 * 
 * @author liumeng
 */
public class AsyncTaskGetCourseList extends AsyncTask<String, Integer, List<CourseItem>> {
	private String url_coursestu = AppInfo.base_url + "/formobile/formobileGetStudentCourse.action";
	private String url_coursetea = AppInfo.base_url + "/formobile/formobileGetTeacherCourse.action";

	private Context context;
	private MyCourseListAdapter adapter;

	public AsyncTaskGetCourseList(Context context, MyCourseListAdapter adapter) {
		this.context = context;
		this.adapter = adapter;
	}

	@Override
	protected List<CourseItem> doInBackground(String... params) {
		// params: userTypeId userId
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		int userTypeId = Integer.parseInt(params[0]);
		String url = null;
		switch (userTypeId) {
		case 10:
			// ѧ��
			datas.add(new BasicNameValuePair("stuId", params[1]));
			url = url_coursestu;
			break;
		case 20:
		case 40:
			// ��ʦ
			datas.add(new BasicNameValuePair("teaId", params[1]));
			url = url_coursetea;
			break;
		}
		// ��ȡ�������γ�����
		CourseUtils cu = new CourseUtils(context);
		return cu.getCourseList(url, datas);
	}

	@Override
	protected void onPostExecute(List<CourseItem> result) {
		super.onPostExecute(result);
		// ֪ͨ�γ��б��������
		adapter.refresh(result);
	}

}
