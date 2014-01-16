package com.realaction.yunbomobile.adapter;

import com.realaction.yunbomobile.R;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerCaseViewAdapter extends BaseAdapter {

	private String[] arrays;
	private Context context;
	
	public DrawerCaseViewAdapter(Context context, String[] arrays) {
		this.context = context;
		this.arrays = arrays;
	}

	@Override
	public int getCount() {
		return arrays.length;
	}

	@Override
	public Object getItem(int position) {
		return arrays[position];
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
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.drawer_item_img);
		TextView text = (TextView) convertView.findViewById(R.id.drawer_item_name);
		text.setText(arrays[position]);
		// TODO 为不同的菜单项设置不同的图标
		return convertView;
	}

}
