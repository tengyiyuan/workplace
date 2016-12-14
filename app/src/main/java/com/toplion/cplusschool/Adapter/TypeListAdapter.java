package com.toplion.cplusschool.Adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CustType;
import com.toplion.cplusschool.R;


@SuppressLint("ResourceAsColor")
public class TypeListAdapter extends BaseAdapter {
	private List<CustType> list = null;
	private Context mContext;

	public TypeListAdapter(Context mContext, List<CustType> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final CustType type = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.type_list_item, null);
			viewHolder.typeName = (TextView) convertView
					.findViewById(R.id.type_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.typeName.setText(type.getTypename());

		if (position == selectItem) {
			viewHolder.typeName.setTextColor(Color.rgb(0, 195, 197));
			//viewHolder.typeName.setBackgroundResource(R.color.type_color);
		} else {
			viewHolder.typeName.setTextColor(Color.BLACK);
			//viewHolder.typeName.setBackgroundResource(R.color.white);
		}
		return convertView;

	}

	final static class ViewHolder {
		TextView typeName;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	private int selectItem = -1;

}