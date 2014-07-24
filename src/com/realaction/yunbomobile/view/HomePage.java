package com.realaction.yunbomobile.view;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.HomePageAdapter;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.ImageUtils;
import com.realaction.yunbomobile.utils.UserUtils;
import com.realaction.yunbomobile.utils.Utility;
import com.realaction.yunbomobile.view.caseviews.CaseDetailsActivity;

/**
 * 首页界面
 * 
 * @author liumeng
 */
public class HomePage extends Fragment {
	private Context context;
	// 用户头像
	private ImageView user_avatar;
	private String user_avater_url;
	private int user_type;
	// 用户名
	private TextView user_name;
	private String user_name_str;
	// 用户学号/工号
	private TextView user_num;
	private String user_num_str;
	// 用户最常浏览的5条记录
	private ListView user_favorite;
	private HomePageAdapter user_fav_adapter;
	// 用户最后浏览的5条记录
	private ListView user_history;
	private HomePageAdapter user_his_adapter;
	private TextView user_no_favorite;
	private TextView user_no_history;
	
	private DBService dbService;

	public HomePage() { }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		dbService = new DBService(context);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		user_avatar = (ImageView) view.findViewById(R.id.home_avatar);
		user_name = (TextView) view.findViewById(R.id.home_name);
		user_num = (TextView) view.findViewById(R.id.home_num);
		user_favorite = (ListView) view.findViewById(R.id.home_fav_list);
		user_history = (ListView) view.findViewById(R.id.home_his_list);
		user_no_favorite = (TextView) view.findViewById(R.id.home_fav_none);
		user_no_history = (TextView) view.findViewById(R.id.home_his_none);

		setData();
		return view;
	}

	/**
	 * 初始化界面各组件所需的数据
	 */
	private void initData() {
		UserUtils userinfo = new UserUtils(context);
		user_name_str = userinfo.getUserRealName();
		user_type = userinfo.getUserTypeId();
		if (user_type == UserUtils.USER_STUDENT) {
			user_num_str = userinfo.getStuNo();
		} else {
			user_num_str = userinfo.getEmpNo();
		}
		user_avater_url = userinfo.getUserAvatar();

		String[] scoreids = dbService.getUserScoreIds(dbService.findUserByuserName(userinfo.getUserName()).userId);
		if (scoreids != null) {
			List<CaseItem> fav_list = dbService.findCasesOrderByCount(scoreids);
			if (fav_list != null && fav_list.size() > 0) {
				user_fav_adapter = new HomePageAdapter(context, fav_list);
			}
			List<CaseItem> his_list = dbService.findCasesOrderByTime(scoreids);
			if (his_list != null && his_list.size() > 0) {
				user_his_adapter = new HomePageAdapter(context, his_list);
			}
		}
	}

	/**
	 * 为各组件设置数据
	 */
	private void setData() {
		// 使用第三方库为ImageView加载图片
		//Picasso.with(context).load(user_avater_url).into(user_avatar);
		user_avatar.setImageBitmap(ImageUtils.getBitmapFromRes(context, user_avater_url));
		user_name.setText("用户名:" + user_name_str);
		if (user_type == UserUtils.USER_STUDENT) {
			user_num.setText("学号:" + user_num_str);
		} else {
			user_num.setText("工号:" + user_num_str);
		}

		if (user_fav_adapter != null) {
			user_favorite.setVisibility(View.VISIBLE);
			user_no_favorite.setVisibility(View.GONE);
			user_favorite.setAdapter(user_fav_adapter);
			Utility.setListViewHeightBasedOnChildren(user_favorite);
			user_favorite.setOnItemClickListener(new ListClickListener(user_fav_adapter));
		} else {
			user_favorite.setVisibility(View.GONE);
			user_no_favorite.setVisibility(View.VISIBLE);
		}
		if (user_his_adapter != null) {
			user_history.setVisibility(View.VISIBLE);
			user_no_history.setVisibility(View.GONE);
			user_history.setAdapter(user_his_adapter);
			Utility.setListViewHeightBasedOnChildren(user_history);
			user_history.setOnItemClickListener(new ListClickListener(user_his_adapter));
		} else {
			user_history.setVisibility(View.GONE);
			user_no_history.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
	
	private class ListClickListener implements OnItemClickListener {
		private HomePageAdapter adapter;
		
		ListClickListener(HomePageAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long location) {
			CaseItem caseitem = adapter.getItem(position);
			long caseid = caseitem.caseId;
			Intent intent = new Intent(context, CaseDetailsActivity.class);
			intent.putExtra("caseId", caseid);
			startActivity(intent);
		}
		
	}
}
