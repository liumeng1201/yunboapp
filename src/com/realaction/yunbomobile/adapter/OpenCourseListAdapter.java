package com.realaction.yunbomobile.adapter;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.utils.OpenCourseInfo;

/**
 * 公开课ListAdapter
 * 
 * @author liumeng
 */
public class OpenCourseListAdapter extends BaseAdapter {
	private Context context;
	private List<OpenCourseInfo> courselist;
	
	public OpenCourseListAdapter(Context context, List<OpenCourseInfo> courselist) {
		this.context = context;
		this.courselist = courselist;
	}

	@Override
	public int getCount() {
		return courselist.size();
	}

	@Override
	public Object getItem(int position) {
		return courselist.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_opencourse, null);
			TextView name = (TextView) convertView.findViewById(R.id.opencourse_name);
			ImageButton img = (ImageButton) convertView.findViewById(R.id.opencourse_collect);
			ViewHolder holder = new ViewHolder(name, img);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.name.setText(courselist.get(position).courseinfo.coursename);
		if (courselist.get(position).selected) {
			holder.img.setBackgroundResource(R.drawable.star_mark);
		} else {
			holder.img.setBackgroundResource(R.drawable.star_unmark);
		}
		holder.img.setOnClickListener(new SelectedListener(position));
		return convertView;
	}

	private class ViewHolder {
		TextView name;
		ImageButton img;
		ViewHolder(TextView name, ImageButton img) {
			this.name = name;
			this.img = img;
		}
	}
	
	/**
	 * 收藏按钮点击事件监听
	 * 
	 * @author liumeng
	 */
	private class SelectedListener implements OnClickListener {
		private int position;
		SelectedListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (courselist.get(position).selected) {
				courselist.get(position).selected = false;
				v.setBackgroundResource(R.drawable.star_unmark);
			} else {
				courselist.get(position).selected = true;
				v.setBackgroundResource(R.drawable.star_mark);
			}
		}
	}
}
