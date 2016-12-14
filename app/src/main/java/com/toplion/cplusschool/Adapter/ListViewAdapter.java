package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.BanWidthBean;
import com.toplion.cplusschool.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<BanWidthBean> mlist;
    private Context mcontext;

    public ListViewAdapter(Context context, List<BanWidthBean> list) {
        this.mlist = list;
        this.mcontext = context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = android.view.View.inflate(mcontext, R.layout.pay_meal_item, null);
            viewHolder.tv_meal_item_banwidth = (TextView) convertView.findViewById(R.id.tv_meal_item_banwidth);
            viewHolder.iv_meal_item_yh = (ImageView) convertView.findViewById(R.id.iv_meal_item_yh);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_meal_item_banwidth.setText(mlist.get(position).getBanWidth());
        if(mlist.get(position).getIsDiscount().equals("1")){
            viewHolder.iv_meal_item_yh.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_meal_item_yh.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_meal_item_banwidth;
        private ImageView iv_meal_item_yh;
    }
}