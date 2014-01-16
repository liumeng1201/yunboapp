package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.HomePageAdapter;
import com.realaction.yunbomobile.utils.UserInfo;
import com.squareup.picasso.Picasso;

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
	// 用户名
	private TextView user_name;
	private String user_name_str;
	// 用户学号/工号
	private TextView user_num;
	private int user_num_int;
	// 用户班级
	private TextView user_class;
	private String user_class_str;
	// 用户最常浏览的5条记录
	private ListView user_favorite;
	private HomePageAdapter user_fav_adapter;
	private static final int LIST_FAV = 1;
	// 用户最后浏览的5条记录
	private ListView user_history;
	private HomePageAdapter user_his_adapter;
	private static final int LIST_HIS = 2;

	public HomePage() { }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		user_avatar = (ImageView) view.findViewById(R.id.home_avatar);
		user_name = (TextView) view.findViewById(R.id.home_name);
		user_num = (TextView) view.findViewById(R.id.home_num);
		user_class = (TextView) view.findViewById(R.id.home_class);
		user_favorite = (ListView) view.findViewById(R.id.home_fav_list);
		user_history = (ListView) view.findViewById(R.id.home_his_list);

		setData();
		return view;
	}

	/**
	 * 初始化界面各组件所需的数据
	 */
	private void initData() {
		UserInfo userinfo = new UserInfo(context);
		user_name_str = "aaa";// userinfo.getUserName();
		user_num_int = 10001;// userinfo.getUserNumber();
		user_class_str = "abcdefg";// userinfo.getUserClass();
		user_avater_url = "http://image.zcool.com.cn/img2/44/6/m_1313459981693.jpg";// userinfo.getUserAvatar();

		user_fav_adapter = new HomePageAdapter(context, getDataFromDB(LIST_FAV));
		user_his_adapter = new HomePageAdapter(context, getDataFromDB(LIST_HIS));
	}

	/**
	 * 为各组件设置数据
	 */
	private void setData() {
		// 使用第三方库为ImageView加载图片
		Picasso.with(context).load(user_avater_url).into(user_avatar);
		user_name.setText(user_name_str);
		user_num.setText(user_num_int + "");
		user_class.setText(user_class_str);

		user_favorite.setAdapter(user_fav_adapter);
		user_history.setAdapter(user_his_adapter);
	}

	private List<String> getDataFromDB(int flag) {
		List<String> list = new ArrayList<String>();
		switch (flag) {
		case LIST_FAV:
			// TODO 从数据库中查询出最常浏览的5条记录
			for (int i = 0; i < 5; i++) {
				list.add("my favorite : " + i);
			}
			break;
		case LIST_HIS:
			// TODO 从数据库中查询出最后浏览的5条记录
			for (int i = 0; i < 5; i++) {
				list.add("my history : " + i);
			}
			break;
		default:
			break;
		}
		return list;
	}
}
