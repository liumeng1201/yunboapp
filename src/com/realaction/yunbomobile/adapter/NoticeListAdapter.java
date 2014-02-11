package com.realaction.yunbomobile.adapter;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.moddel.NoticeItem;

public class NoticeListAdapter extends BaseAdapter {
	private Context context;
	private List<NoticeItem> list;
	
	public NoticeListAdapter(Context context, List<NoticeItem> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_notice, null);
			TextView from = (TextView) view.findViewById(R.id.notice_from);
			TextView message = (TextView) view.findViewById(R.id.notice_message);
			TextView date = (TextView) view.findViewById(R.id.notice_date);
			ViewHolder holder = new ViewHolder(from, message, date);
			view.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.from.setText(list.get(position).noticefrom);
		holder.message.setText(list.get(position).noticemessage);
		holder.date.setText(list.get(position).noticedate);
		return view;
	}

	private class ViewHolder {
		TextView from;
		TextView message;
		TextView date;
		
		ViewHolder(TextView from, TextView message, TextView date) {
			this.from = from;
			this.message = message;
			this.date = date;
		}
	}
}
