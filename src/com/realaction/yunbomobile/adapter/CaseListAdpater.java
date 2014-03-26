package com.realaction.yunbomobile.adapter;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
			Button btn = (Button) convertView.findViewById(R.id.listitem_caselist_btn);
			ViewHolder holder = new ViewHolder(item, btn);
			convertView.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.item.setText(list.get(position).caseName);
		final int num = position;
		switch (list.get(position).download) {
		case 0:
			// 未下载
			holder.btn.setBackgroundResource(R.drawable.btn_dl_do_selector);
			break;
		case 1:
			// 已下载
			holder.btn.setBackgroundResource(R.drawable.btn_dl_done_selector);
			break;
		case 2:
			// 正在下载
			holder.btn.setBackgroundResource(R.drawable.btn_dl_doing_selector);
			break;
		default:
			holder.btn.setBackgroundResource(R.drawable.btn_dl_do_selector);
			break;
		}
		holder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.btn.setBackgroundResource(R.drawable.btn_dl_doing_selector);
				list.get(num).download = 1;
				Toast.makeText(context, "Button click " + num, Toast.LENGTH_SHORT).show();				
			}
		});
		return convertView;
	}

	private class ViewHolder {
		TextView item;
		Button btn;

		ViewHolder(TextView item, Button btn) {
			this.item = item;
			this.btn = btn;
		}
	}
}
