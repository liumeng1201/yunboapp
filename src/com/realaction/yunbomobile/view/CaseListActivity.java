package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.realaction.yunbomobile.utils.CasesUtils;
import com.squareup.picasso.Picasso;

/**
 * �γ̰����б����
 * 
 * @author liumeng
 */
public class CaseListActivity extends Activity {
	private Context context;
	private Handler mHandler;
	private String url_student = "http://192.168.2.231:8080/formobile/formobileGetStudentCases.action";
	private String url_teacher = "http://192.168.2.231:8080/formobile/formobileGetTeacherCases.action";
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
		mHandler = new Handler();
		// ��ȡ�ӿγ��б������ת������Intent
		Intent intent = getIntent();
		scoreId = intent.getStringExtra("scoreId");

		caselist_img = (ImageView) findViewById(R.id.caselist_img);
		caselist_info = (TextView) findViewById(R.id.caselist_info);
		caselist_list = (ListView) findViewById(R.id.caselist_list);
		initData();
		setData();
	}
	
	/**
	 * ��ʼ��������Դ�б��������Ҫ�ĸ�������
	 */
	private void initData() {
		img_urls = new String[] {
				"http://www.sinaimg.cn/edu/157/2012/0426/U5525P352T157D278F5318DT20120426173631.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0227/U6506P352T157D262F5318DT20120227052106.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0217/U5525P352T157D255F5318DT20120217162237.jpg", 
				"http://www.sinaimg.cn/edu/157/2012/0426/U5525P352T157D279F5318DT20120426184451.jpg", 
				"http://www.sinaimg.cn/edu/157/2011/0923/U5525P352T157D147F5318DT20110923201942.jpg"};
		course_infos = new String[] {
				"���γ̵�������Ϊpalm��˾�ĸ��ܲá����º͸߼���Ʒ����������ǣ���Ҫ������WebOS����ϵͳ�ϵ��ֻ�Ӧ�ÿ�����ѧ���л���Ӵ���ƻ����Palm��Щ��˾����Ӧ���ϴ�������ڲ���Ϣ������Palm��˾�����ŶӵĲ�ͬ��Ա���к�����",
				"��Sintel����Blender�����ʹ�ÿ�Դ���3D���Blender������һ����Դ����Ƭ�������Ŷ�һ��12�ˣ���ʱ10���º�40wŷԪ��ɡ�����Ҫ���������ʹ�õ��ǿ�Դ����������������زĺͼ������ǹ����ġ�������Blender����3D��ģ����Ƶ�༭���ϳɡ������ȣ�����ѵ�GIMP��Inkscape�䵱PhotoShop��ͼ�ͻ滭��ʹ��OpenEXR��Ⱦ�����Python��д�ű�����SVN�д������ݵȵȡ�ͬ�����е�DVD����ȫ������Դ�ļ��ʹ��������̡̳�",
				"�������ѧE-75�ǹ����ѧ��������ѧԺ��һ�ſγ̡������ſγ��У�ѧ���ܹ�ѧ�������DNS���������������XHTML��CSS����ҳ�����ؼ���֪ʶ��",
				"�γ���˹̹����ѧ�������������Ļ�����ר����Ƶ�ϵ�н������������ݹ㷺�漰���˻������������ĸ���ǰ�ػ��⣬��Twitter������ʶ�𡢵��桢�����˵ȡ�",
				"���ſγ���Ҫ���ܻ�ѧ����ѧ�Լ���ѧ����ѧ�����а������ϵͳ���غ����ʣ���������ѧ�����弰Һ�廯ѧ��Ӧ��ƽ�����ʣ��ȵȡ���ʦ��ͨ��һЩ�򵥳����Ļ�ѧ��Ӧ�����ݻ�ѧ��Ӧ���غ����ʣ������ܻ�ѧ��Ӧ���ʵ�֪ʶ�㡣"
		};
		
		// TODO ��Ҫ�Ż�
		// ��ȡ�γ̰�����Դ���밸���б�ListView��
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
						adapter = new CaseListAdpater(context, caselists);
						caselist_list.setAdapter(adapter);
					}
				});
			}
		}.start();
	}
	
	/**
	 * Ϊ������������������
	 */
	private void setData() {
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
