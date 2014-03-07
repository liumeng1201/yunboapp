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
 * 首页ListView适配器
 * 
 * @author liumeng
 */
public class HomePageAdapter extends BaseAdapter {
	private Context context;
//	private List<String> list;
	private List<CaseItem> list;

	/**
	 * 首页ListView适配器构造函数
	 * 
	 * @param context
	 *            上下文指针
	 * @param list
	 *            数据list
	 */
	public HomePageAdapter(Context context, List<CaseItem> list) {
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
	public View getView(int positon, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_home, null);
			TextView item = (TextView) view
					.findViewById(R.id.listitem_home_text);
			ViewHolder holder = new ViewHolder(item);
			view.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.item.setText(list.get(positon).caseName);
		return view;
	}

	private class ViewHolder {
		TextView item;

		ViewHolder(TextView item) {
			this.item = item;
		}
	}
}
