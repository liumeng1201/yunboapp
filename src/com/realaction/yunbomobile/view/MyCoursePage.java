package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.MyCourseListAdapter;
import com.realaction.yunbomobile.utils.CourseInfo;

/**
 * 我的课程界面
 * 
 * @author liumeng
 */
public class MyCoursePage extends Fragment {
	private Context context;
	private MyCourseListAdapter adapter_bixiu;
	private MyCourseListAdapter adapter_xuanxiu;
	private List<CourseInfo> list_bixiu;
	private List<CourseInfo> list_xuanxiu;
	private OnItemClickListener listener_bixiu = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
			Intent intent = new Intent(context, CaseListActivity.class);
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
		list_bixiu = new ArrayList<CourseInfo>();
		list_xuanxiu = new ArrayList<CourseInfo>();
		
		for (int i = 1; i < 51; i++) {
			CourseInfo ci = new CourseInfo();
			ci.coursename = "必修课程  " + i;
			list_bixiu.add(ci);
			ci.coursename = "选修课程  " + i;
			list_xuanxiu.add(ci);
		}
		adapter_bixiu = new MyCourseListAdapter(context, list_bixiu);
		adapter_xuanxiu = new MyCourseListAdapter(context, list_xuanxiu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mycourse, null);
		ListView course_bixiu = (ListView) view.findViewById(R.id.mycourse_list1);
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
		
		course_bixiu.setAdapter(adapter_bixiu);
		course_xuanxiu.setAdapter(adapter_xuanxiu);
		
		course_bixiu.setOnItemClickListener(listener_bixiu);
		course_xuanxiu.setOnItemClickListener(listener_xuanxiu);
		return view;
	}

}
