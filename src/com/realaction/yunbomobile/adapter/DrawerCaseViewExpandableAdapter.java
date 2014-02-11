package com.realaction.yunbomobile.adapter;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.moddel.CourseItem;
import com.realaction.yunbomobile.utils.CaseViewChildItem;
import com.realaction.yunbomobile.utils.CaseViewGroupItem;

/**
 * 案例资源查看界面,案例资源二层ListView适配器
 * 
 * @author liumeng
 */
public class DrawerCaseViewExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<CaseViewGroupItem> groupArray;
	private List<List<CaseViewChildItem>> childArray;

	public DrawerCaseViewExpandableAdapter(Context context,
			List<CaseViewGroupItem> groupArray, List<List<CaseViewChildItem>> childArray) {
		this.context = context;
		this.groupArray = groupArray;
		this.childArray = childArray;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_expandable_list_item_child, null);
			TextView itemname = (TextView) convertView.findViewById(R.id.expandable_listitem_child_name);
			ChildViewHolder holder = new ChildViewHolder(itemname);
			convertView.setTag(holder);
		}
		ChildViewHolder holder = (ChildViewHolder) convertView.getTag();
		holder.item.setText(childArray.get(groupPosition).get(childPosition).childname);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childArray.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_expandable_list_item_group, null);
			TextView itemname = (TextView) convertView.findViewById(R.id.expandable_listitem_group_name);
			GroupViewHolder holder = new GroupViewHolder(itemname);
			convertView.setTag(holder);
		}
		GroupViewHolder holder = (GroupViewHolder) convertView.getTag();
		holder.item.setText(groupArray.get(groupPosition).groupname);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class ChildViewHolder {
		private TextView item;
		
		ChildViewHolder(TextView item) {
			this.item = item;
		}
	}
	
	private class GroupViewHolder {
		private TextView item;

		GroupViewHolder(TextView item) {
			this.item = item;
		}
	}
}
