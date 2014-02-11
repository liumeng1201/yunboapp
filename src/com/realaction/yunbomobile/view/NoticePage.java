package com.realaction.yunbomobile.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.adapter.NoticeListAdapter;
import com.realaction.yunbomobile.moddel.NoticeItem;

/**
 * ֪ͨ����
 * 
 * @author liumeng
 */
public class NoticePage extends Fragment {
	private Context context;
	private ListView noticelist_view;
	private List<NoticeItem> noticelist_message;
	private NoticeListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		noticelist_message = new ArrayList<NoticeItem>();
		for (int i = 1; i < 31; i++) {
			NoticeItem ni = new NoticeItem();
			ni.noticefrom = "��ʦ" + (i / 5 + 1);
			ni.noticemessage = "֪ͨ����  " + i;
			ni.noticedate = "ʱ��  " + i;
			noticelist_message.add(ni);
		}
		
		adapter = new NoticeListAdapter(context, noticelist_message);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notice, null);
		noticelist_view = (ListView) view.findViewById(R.id.notice_list);
		noticelist_view.setAdapter(adapter);
		return view;
	}

}
