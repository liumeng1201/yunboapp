package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.CaseListAdpater;
import com.realaction.yunbomobile.utils.CaseInfo;
import com.squareup.picasso.Picasso;

/**
 * 课程案例列表界面
 * 
 * @author liumeng
 */
public class CaseListActivity extends Activity {
	private Context context;
	
	private ImageView caselist_img;
	private TextView caselist_info;
	private ListView caselist_list;
	
	private String img_url;
	private String course_info;
	private List<CaseInfo> caselists;
	private CaseListAdpater adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caselist);
		context = CaseListActivity.this;
		// 获取从课程列表界面跳转过来的Intent
		Intent intent = getIntent();
		initData();

		caselist_img = (ImageView) findViewById(R.id.caselist_img);
		caselist_info = (TextView) findViewById(R.id.caselist_info);
		caselist_list = (ListView) findViewById(R.id.caselist_list);
		setData();
	}
	
	/**
	 * 初始化案例资源列表界面所需要的各种数据
	 */
	private void initData() {
		img_url = "http://e.hiphotos.baidu.com/image/w%3D2048/sign=afda38f8d762853592e0d521a4d777c6/6d81800a19d8bc3e85bf44c0808ba61ea8d34500.jpg";
		course_info = "测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据测试数据";
		caselists = getCaseList();
		adapter = new CaseListAdpater(context, caselists);
	}
	
	/**
	 * 为界面各个组件设置数据
	 */
	private void setData() {
		Picasso.with(context).load(img_url).into(caselist_img);
		caselist_info.setText(course_info);
		caselist_list.setAdapter(adapter);
		
		caselist_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
				Intent intent = new Intent(context, CaseViewActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取课程中案例资源列表信息
	 * 
	 * @return 案例资源列表
	 */
	private List<CaseInfo> getCaseList() {
		List<CaseInfo> caselist = new ArrayList<CaseInfo>();
		for (int i = 0; i < 50; i++) {
			CaseInfo ci = new CaseInfo();
			ci.casename = "案例  " + (i + 1);
			caselist.add(ci);
		}
		return caselist;
	}
}
