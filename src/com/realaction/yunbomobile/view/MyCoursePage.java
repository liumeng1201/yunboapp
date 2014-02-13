package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.MyCourseListAdapter;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CourseItem;
import com.realaction.yunbomobile.moddel.User;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.CourseUtils;
import com.realaction.yunbomobile.utils.UserUtils;

/**
 * 我的课程界面
 * 
 * @author liumeng
 */
public class MyCoursePage extends Fragment {
	private Context context;
	private Handler mHandler = new Handler();
	private UserUtils userUtils;
	private DBService dbService;
	private User currentUser;
	
	private String url_coursestu = AppInfo.base_url + "/formobile/formobileGetStudentCourse.action";
	private String url_coursetea = AppInfo.base_url + "/formobile/formobileGetTeacherCourse.action";
	
	private MyCourseListAdapter adapter_bixiu;
	private MyCourseListAdapter adapter_xuanxiu;
	private List<CourseItem> list_bixiu;
	private List<CourseItem> list_xuanxiu;
	private OnItemClickListener listener_bixiu = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
			CourseItem ci = list_bixiu.get(position);
			Intent intent = new Intent(context, CaseListActivity.class);
			intent.putExtra("scoreId", ci.scoreId);
			startActivity(intent);
		}
	};
	private OnItemClickListener listener_xuanxiu = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
			Intent intent = new Intent(context, CaseListActivity.class);
			startActivity(intent);
		}
	};

	public MyCoursePage() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		userUtils = new UserUtils(context);
		dbService = new DBService(context);
		currentUser = new User();
		currentUser = dbService.findUserByuserName(userUtils.getUserName());
		list_bixiu = new ArrayList<CourseItem>();
		list_xuanxiu = new ArrayList<CourseItem>();
		
		/*----------------------------------------------*/
		for (int i = 1; i < 51; i++) {
			CourseItem ci1 = new CourseItem();
			ci1.courseName = "必修课程  " + i;
			list_bixiu.add(ci1);
			CourseItem ci2 = new CourseItem();
			ci2.courseName = "选修课程  " + i;
			list_xuanxiu.add(ci2);
		}
		/*----------------------------------------------*/
		
		adapter_xuanxiu = new MyCourseListAdapter(context, list_xuanxiu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mycourse, null);
		final ListView course_bixiu = (ListView) view.findViewById(R.id.mycourse_list1);
		ListView course_xuanxiu = (ListView) view.findViewById(R.id.mycourse_list2);
		
		TabHost tabhost = (TabHost) view.findViewById(R.id.tabhost);
		tabhost.setup();
		tabhost.addTab(tabhost.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.course_bixiu))
				.setContent(R.id.mycourse_list1));
		tabhost.addTab(tabhost
				.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.course_xuanxiu))
				.setContent(R.id.mycourse_list2));
		
		/****************************/
		new Thread() {
			@Override
			public void run() {
				super.run();
				String url = null;
				List<NameValuePair> datas = new ArrayList<NameValuePair>();
				switch (currentUser.userTypeId) {
				case 10:
					// 学生
					datas.add(new BasicNameValuePair("stuId", Long.toString(currentUser.userId)));
					url = url_coursestu;
					break;
				case 20:
				case 40:
					// 老师
					datas.add(new BasicNameValuePair("teaId", Long.toString(currentUser.userId)));
					url = url_coursetea;
					break;
				}
				CourseUtils cu = new CourseUtils(context);
				list_bixiu = cu.getCourseList(url, datas);
				
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						adapter_bixiu = new MyCourseListAdapter(context, list_bixiu);
						course_bixiu.setAdapter(adapter_bixiu);
					}
				});
			}
		}.start();
		/****************************/
		
		course_xuanxiu.setAdapter(adapter_xuanxiu);
		
		course_bixiu.setOnItemClickListener(listener_bixiu);
		course_xuanxiu.setOnItemClickListener(listener_xuanxiu);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
}
