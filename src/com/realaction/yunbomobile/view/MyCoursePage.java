package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
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
import com.realaction.yunbomobile.utils.AsyncTaskGetCourseList;
import com.realaction.yunbomobile.utils.MyDialog;
import com.realaction.yunbomobile.utils.UserUtils;

/**
 * 我的课程界面
 * 
 * @author liumeng
 */
public class MyCoursePage extends Fragment {
	public static final int CANCEL_DIALOG = 1000;
	public static final int SHOW_DIALOG = 1001;
	
	private Context context;
	private UserUtils userUtils;
	private DBService dbService;
	private User currentUser;
	
	private MyDialog dialog;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == CANCEL_DIALOG) {
				dialog.dismiss();
			}
			if (msg.what == SHOW_DIALOG) {
				dialog.show();
			}
		}
	};

	private MyCourseListAdapter adapter_bixiu;
//	private MyCourseListAdapter adapter_xuanxiu;
	private List<CourseItem> list_bixiu;
//	private List<CourseItem> list_xuanxiu;
	private OnItemClickListener listener_bixiu = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
			CourseItem ci = list_bixiu.get(position);
			Intent intent = new Intent(context, CaseListActivity.class);
			intent.putExtra("courseName", ci.courseName);
			intent.putExtra("scoreId", ci.scoreId);
			startActivity(intent);
		}
	};
//	private OnItemClickListener listener_xuanxiu = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
//			Intent intent = new Intent(context, CaseListActivity.class);
//			startActivity(intent);
//		}
//	};

	public MyCoursePage() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		userUtils = new UserUtils(context);
		dialog = new MyDialog(getActivity());
		dialog.create();
		dbService = new DBService(context);
		currentUser = dbService.findUserByuserName(userUtils.getUserName());
		list_bixiu = new ArrayList<CourseItem>();
//		list_xuanxiu = new ArrayList<CourseItem>();

		/*--------------选修课测试数据------------
		for (int i = 1; i < 51; i++) {
			CourseItem ci2 = new CourseItem();
			ci2.courseName = "选修课程  " + i;
			list_xuanxiu.add(ci2);
		}
		-----------------------------------*/

		adapter_bixiu = new MyCourseListAdapter(context, list_bixiu);
//		adapter_xuanxiu = new MyCourseListAdapter(context, list_xuanxiu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mycourse, null);
		final ListView course_bixiu = (ListView) view.findViewById(R.id.mycourse_list1);
//		ListView course_xuanxiu = (ListView) view.findViewById(R.id.mycourse_list2);

//		TabHost tabhost = (TabHost) view.findViewById(R.id.tabhost);
//		tabhost.setup();
//		tabhost.addTab(tabhost.newTabSpec("tab1")
//				.setIndicator(getResources().getString(R.string.course_bixiu))
//				.setContent(R.id.mycourse_list1));
//		tabhost.addTab(tabhost.newTabSpec("tab2")
//				.setIndicator(getResources().getString(R.string.course_xuanxiu))
//				.setContent(R.id.mycourse_list2));

		course_bixiu.setAdapter(adapter_bixiu);
//		course_xuanxiu.setAdapter(adapter_xuanxiu);

		course_bixiu.setOnItemClickListener(listener_bixiu);
//		course_xuanxiu.setOnItemClickListener(listener_xuanxiu);

		refreshCoursesList();

		return view;
	}

	/**
	 * 更新课程列表的内容
	 */
	private void refreshCoursesList() {
		handler.sendEmptyMessage(SHOW_DIALOG);
		if (AppInfo.network_avabile) {
			// 网络可用的时候通过网络获取要显示的数据
			// 以异步任务方式获取课程数据并更新列表
			AsyncTaskGetCourseList async = new AsyncTaskGetCourseList(context, handler, adapter_bixiu);
			String[] params = { String.valueOf(currentUser.userTypeId),
					String.valueOf(currentUser.userId) };
			try {
				list_bixiu = async.execute(params).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// 网络不可用的时候通过访问数据库中缓存的数据来获取要显示的数据
			list_bixiu = dbService.findCoursesByuserId(currentUser.userId);
			handler.sendEmptyMessage(CANCEL_DIALOG);
			if (list_bixiu != null) {
				adapter_bixiu.refresh(list_bixiu);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
}
