package com.lm.filebrowser;

import java.io.File;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.realaction.yunbomobile.R;

/**
 * ÎÄ¼þä¯ÀÀÆ÷adapter
 * 
 * @author liumeng
 */
public class FileBrowserAdapter extends BaseAdapter {
	private File[] mFiles;
	private LayoutInflater mInflater;
	private Context mContext;
	private boolean isTop;

	public FileBrowserAdapter(Context context, File[] files, boolean root) {
		mFiles = files;
		mInflater = LayoutInflater.from(context);
		mContext = context;
		isTop = root;
	}

	public int getCount() {
		return mFiles.length;
	}

	public Object getItem(int position) {
		return mFiles[position];
	}

	public long getItemId(int position) {
		return position;
	}

	private void setRow(ViewHolder holder, int position) {
		File file = mFiles[position];
		holder.text.setText((file.getName()).replaceAll(".pdf", ""));
		if (position == 0 && !isTop) {
			holder.icon.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.ic_menu_back));
		} else if (file.isDirectory()) {
			holder.icon.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.ic_menu_archive));
		} else {
			Drawable d = null;
			d = mContext.getResources().getDrawable(R.drawable.ic_menu_file);
			holder.icon.setImageDrawable(d);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.file_explorer_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.textview_rowtext);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.imageview_rowicon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setRow(holder, position);
		return convertView;
	}

	private static class ViewHolder {
		TextView text;
		ImageView icon;
	}
}