package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.R;

import java.util.List;

public class PopupWindowAdapter extends BaseAdapter{
	private Context mContext;
	private List<CommonBean> mlist;
	
	private ViewHolder viewHolder;

	public List<CommonBean> getMlist() {
		return mlist;
	}

	public void setMlist(List<CommonBean> mlist) {
		this.mlist = mlist;
	}

	public PopupWindowAdapter(Context context, List<CommonBean> list) {
		this.mContext = context;
		this.mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.popupwindow_item, null);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_content.setText(mlist.get(position).getDes().toString());
		
		return convertView;
	}
	
	class ViewHolder{
		private TextView tv_content;
	}

}
