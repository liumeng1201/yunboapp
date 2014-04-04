package com.realaction.yunbomobile.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;

public class DrawerListAdapter extends BaseAdapter {
	private String[] arrays;
	private Context context;
	
	public DrawerListAdapter(Context context, String[] arrays) {
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
		switch (position) {
		case 0:
			img.setImageResource(R.drawable.img1home);
			break;
		case 1:
			img.setImageResource(R.drawable.img2mycourse);
			break;
//		case 2:
//			img.setImageResource(R.drawable.img3opencourse);
//			break;
//		case 3:
//			img.setImageResource(R.drawable.img4schdule);
//			break;
//		case 4:
//			img.setImageResource(R.drawable.img5notice);
//			break;
		case 2:
			img.setImageResource(R.drawable.img6settings);
		default:
			break;
		}
		return convertView;
	}

}
