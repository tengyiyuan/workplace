package com.toplion.cplusschool.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.Thirdtype;
import com.toplion.cplusschool.R;


public class typeGridAdapter extends BaseAdapter {
	private Context context;
	private List<Thirdtype> products; // 图片下载类

	public typeGridAdapter(Context context, List<Thirdtype> products) {
		this.context = context;
		this.products=products;
	}

	public void setProducts(List<Thirdtype> products) {
		this.products = products;
	}

	@Override
	public int getCount() {
		return products != null ? products.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return products != null ? products.get(position) : 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView myholder = null;
		if (convertView == null) {
			myholder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.typegirditem, null);
			myholder.title=(TextView)convertView.findViewById(R.id.gird_shopname);
			convertView.setTag(myholder);
		} else {
			myholder = (HolderView) convertView.getTag();
		}
		myholder.title.setText(products.get(position).getTypename());
		return convertView;
	}

	class HolderView {
		// 图片
		ImageView image;
		// 简介
		TextView title;
		//剩余
		TextView shengyu;
		//价格
		TextView priceRaw;
		//状态
		ImageView state;

	}

}