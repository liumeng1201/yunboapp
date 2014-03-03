package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
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

/**
 * ��ҳ����
 * 
 * @author liumeng
 */
public class HomePage extends Fragment {
	private String TAG = "lm";
	private Context context;
	// �û�ͷ��
	private ImageView user_avatar;
	private String user_avater_url;
	// �û���
	private TextView user_name;
	private String user_name_str;
	// �û�ѧ��/����
	private TextView user_num;
	private String user_num_str;
	// �û��༶
	private TextView user_class;
	private String user_class_str;
	// �û�������5����¼
	private ListView user_favorite;
	private HomePageAdapter user_fav_adapter;
	private static final int LIST_FAV = 1;
	// �û���������5����¼
	private ListView user_history;
	private HomePageAdapter user_his_adapter;
	private OnItemClickListener listener;
	private static final int LIST_HIS = 2;
	
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
		user_class = (TextView) view.findViewById(R.id.home_class);
		user_favorite = (ListView) view.findViewById(R.id.home_fav_list);
		user_history = (ListView) view.findViewById(R.id.home_his_list);
		user_favorite.setOnItemClickListener(listener);
		user_history.setOnItemClickListener(listener);

		setData();
		return view;
	}

	/**
	 * ��ʼ�������������������
	 */
	private void initData() {
		UserUtils userinfo = new UserUtils(context);
		user_name_str = userinfo.getUserRealName();
		if (userinfo.getUserTypeId() == UserUtils.USER_STUDENT) {
			user_num_str = userinfo.getStuNo();
		} else {
			user_num_str = userinfo.getEmpNo();
		}
		user_class_str = userinfo.getUserTypeId() + "";
		user_avater_url = userinfo.getUserAvatar();//"http://www.realaction.cn/template/images/logo.jpg";
		Log.d(TAG, "user_avater_url = " + user_avater_url);

		user_fav_adapter = new HomePageAdapter(context, getDataFromDB(LIST_FAV));
		user_his_adapter = new HomePageAdapter(context, getDataFromDB(LIST_HIS));
		listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long location) {
//				Intent intent = new Intent(context, CaseViewActivity.class);
//				startActivity(intent);
			}
		};
		
		String[] scoreids = dbService.getUserScoreIds(dbService.findUserByuserName(userinfo.getUserName()).userId);
		for (String scoreid : scoreids) {
			Log.d("time", scoreid.toString());
		}
		List<CaseItem> list = dbService.findCasesOrderByCount(scoreids);
		if (list.size() > 0) {
			
		}
	}

	/**
	 * Ϊ�������������
	 */
	private void setData() {
		// ʹ�õ�������ΪImageView����ͼƬ
		//Picasso.with(context).load(user_avater_url).into(user_avatar);
		user_avatar.setImageBitmap(ImageUtils.getBitmapFromRes(context, user_avater_url));
		user_name.setText(user_name_str);
		user_num.setText(user_num_str);
		user_class.setText(user_class_str);

		user_favorite.setAdapter(user_fav_adapter);
		user_history.setAdapter(user_his_adapter);
	}

	private List<String> getDataFromDB(int flag) {
		List<String> list = new ArrayList<String>();
		switch (flag) {
		case LIST_FAV:
			// TODO �����ݿ��в�ѯ��������5����¼
			for (int i = 0; i < 5; i++) {
				list.add("���� : " + (i + 1));
			}
			break;
		case LIST_HIS:
			// TODO �����ݿ��в�ѯ����������5����¼
			for (int i = 0; i < 5; i++) {
				list.add("��ʷ��¼ : " + (i + 1));
			}
			break;
		default:
			break;
		}
		return list;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
}
