package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.CaseListAdpater;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.AsyncTaskGetCaseList;
import com.realaction.yunbomobile.utils.CasesUtils;
import com.realaction.yunbomobile.utils.UserUtils;
import com.squareup.picasso.Picasso;

/**
 * 课程案例列表界面
 * 
 * @author liumeng
 */
public class CaseListActivity extends Activity {
	private Context context;
	private int userTypeId;

	private String scoreId;
	private ImageView caselist_img;
	private TextView caselist_info;
	private ListView caselist_list;
	
	private List<CaseItem> caselists;
	private CaseListAdpater adapter;
	
	private String[] img_urls;
	private String[] course_infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caselist);
		context = CaseListActivity.this;
		userTypeId = (new UserUtils(context)).getUserTypeId();
		// 获取从课程列表界面跳转过来的Intent
		Intent intent = getIntent();
		scoreId = intent.getStringExtra("scoreId");

		caselist_img = (ImageView) findViewById(R.id.caselist_img);
		caselist_info = (TextView) findViewById(R.id.caselist_info);
		caselist_list = (ListView) findViewById(R.id.caselist_list);
		caselists = new ArrayList<CaseItem>();
		adapter = new CaseListAdpater(context, caselists);
		caselist_list.setAdapter(adapter);
		
		init();
	}
	
	/**
	 * 设置案例资源列表界面所需要的各种数据
	 */
	private void init() {
		img_urls = new String[] {
				"http://www.sinaimg.cn/edu/157/2012/0426/U5525P352T157D278F5318DT20120426173631.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0227/U6506P352T157D262F5318DT20120227052106.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0217/U5525P352T157D255F5318DT20120217162237.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0426/U5525P352T157D279F5318DT20120426184451.jpg", 
				"http://www.sinaimg.cn/edu/157/2011/0923/U5525P352T157D147F5318DT20110923201942.jpg"};
		course_infos = new String[] {
				"本课程的主讲人为palm公司的副总裁、董事和高级产品经理等主管们，主要讲述在WebOS操作系统上的手机应用开发。学生有机会接触到苹果、Palm这些公司关于应用上传程序的内部信息，并于Palm公司主管团队的不同成员进行合作。",
				"《Sintel》是Blender基金会使用开源免费3D软件Blender制作的一部开源动画片，制作团队一共12人，耗时10个月和40w欧元完成。其主要制作软件均使用的是开源软件，并且制作的素材和技术都是公开的。其中用Blender进行3D建模、视频编辑、合成、剪辑等，用免费的GIMP和Inkscape充当PhotoShop作图和绘画，使用OpenEXR渲染输出，Python编写脚本，在SVN中储存数据等等。同步发行的DVD包含全部制作源文件和大量制作教程。",
				"计算机科学E-75是哈佛大学继续教育学院的一门课程。在这门课程中，学生能够学到如何用DNS建立域名，如何用XHTML和CSS构架页面等相关技术知识。",
				"课程是斯坦福大学关于人与计算机的互动，专门设计的系列讲座。讲座内容广泛涉及到人机互动设计领域的各种前沿话题，如Twitter、语音识别、电玩、机器人等。",
				"这门课程主要介绍化学动力学以及化学热力学，其中包括宏观系统的守恒性质，基本热力学，气体及液体化学反应的平衡性质，等等。讲师会通过一些简单常见的化学反应来传递化学反应的守恒性质，并介绍化学反应速率等知识点。"
		};
		
		if (AppInfo.network_avabile) {
			// 网络可用的时候通过网络获取课程案例数据并通知adapter更新
			/*
			// 方式一
			final Handler mHandler = new Handler();
			final String url_student = AppInfo.base_url + "/formobile/formobileGetStudentCases.action";
			final String url_teacher = AppInfo.base_url + "/formobile/formobileGetTeacherCases.action";
			new Thread() {
				@Override
				public void run() {
					super.run();
					List<NameValuePair> datas = new ArrayList<NameValuePair>();
					datas.add(new BasicNameValuePair("scoreId", scoreId));
					CasesUtils cu = new CasesUtils(context);
					caselists = cu.getCasesList(url_student, datas);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							adapter.refresh(caselists);
						}
					});
				}
			}.start(); */
			// 方式二
			AsyncTaskGetCaseList async = new AsyncTaskGetCaseList(context, adapter);
			String[] params = new String[] { scoreId, String.valueOf(userTypeId)};
			try {
				caselists = async.execute(params).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			// 网络不可用时通过数据库获取缓存的案例数据
		}
	
		Random random = new Random();
		Picasso.with(context).load(img_urls[Math.abs((random.nextInt()) % 5)]).into(caselist_img);
		caselist_info.setText(course_infos[Math.abs((random.nextInt()) % 5)]);
		
		caselist_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long location) {
				Intent intent = new Intent(context, CaseViewActivity.class);
				intent.putExtra("caseId", caselists.get(position).caseId);
				startActivity(intent);
			}
		});
	}

}
