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
import com.realaction.yunbomobile.moddel.CaseItem;

/**
 * 课程案例资源列表Adapter
 * 
 * @author liumeng
 */
public class CaseListAdpater extends BaseAdapter {
	private Context context;
	private List<CaseItem> list;
	
	public CaseListAdpater(Context context, List<CaseItem> list) {
		this.context = context;
		this.list = list;
	}
	
	public void refresh(List<CaseItem> list) {
		this.list = list;
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_caselist, null);
			TextView item = (TextView) convertView.findViewById(R.id.listitem_caselist_text);
			ViewHolder holder = new ViewHolder(item);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.item.setText(list.get(position).caseName);
		return convertView;
	}

	private class ViewHolder {
		TextView item;

		ViewHolder(TextView item) {
			this.item = item;
		}
	}
}
