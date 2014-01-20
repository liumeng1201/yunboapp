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

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.OpenCourseListAdapter;
import com.realaction.yunbomobile.utils.OpenCourseInfo;

/**
 * 公开课界面
 * 
 * @author liumeng
 */
public class OpenCoursePage extends Fragment {
	private Context context;
	private ListView opencourse_list;
	private List<OpenCourseInfo> courselist;
	private OpenCourseListAdapter adapter;

	public OpenCoursePage() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		
		courselist = new ArrayList<OpenCourseInfo>();
		for (int i = 1; i < 51; i++) {
			OpenCourseInfo ci = new OpenCourseInfo();
			ci.courseinfo.coursename = "公开课  " + i;
			ci.selected = false;
			courselist.add(ci);
		}
		
		adapter = new OpenCourseListAdapter(context, courselist);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_opencourse, null);
		opencourse_list = (ListView) view.findViewById(R.id.opencourse_list);
		opencourse_list.setAdapter(adapter);
		opencourse_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long location) {
				Intent intent = new Intent(context, CaseListActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

}