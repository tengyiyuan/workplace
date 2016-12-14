package com.toplion.cplusschool.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CustTypeSecond;
import com.toplion.cplusschool.Bean.Thirdtype;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.widget.MyGridView;

public class TypeSecondListAdapter extends BaseAdapter {
	private List<CustTypeSecond> list = null;
	private Context mContext;
	private List<Thirdtype> products = new ArrayList<Thirdtype>();

	public TypeSecondListAdapter(Context mContext, List<CustTypeSecond> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void setType(List<CustTypeSecond> list) {
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
		final CustTypeSecond type = list.get(position);

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.typesecond_list_item, null);
			viewHolder.typeName = (TextView) convertView
					.findViewById(R.id.type_name);
			viewHolder.gridview = (MyGridView) convertView
					.findViewById(R.id.mGridView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.typeName.setText(type.getTypename());
		products.clear();
		//if(type.getTypeid().equals("1")) {
		for (int i = 0; i < 10; i++) {
			Thirdtype third = new Thirdtype();
			third.setTypeid(i + "");
				third.setTypename(type.getTypename()+i+"");
			products.add(third);
		}
		typeGridAdapter gridadapter = new typeGridAdapter(mContext, products);
		gridadapter.setProducts(products);
		viewHolder.gridview.setAdapter(gridadapter);

		viewHolder.gridview
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
					}
				});
		//}
		//gridadapter.notifyDataSetChanged();
		// if (position == selectItem) {
		// viewHolder.typeName.setTextColor(Color.RED);
		// } else {
		// viewHolder.typeName.setTextColor(Color.BLACK);
		// }
		return convertView;

	}

	final static class ViewHolder {
		TextView typeName;
		MyGridView gridview;
		typeGridAdapter gridadapter;


	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	private int selectItem = -1;

}