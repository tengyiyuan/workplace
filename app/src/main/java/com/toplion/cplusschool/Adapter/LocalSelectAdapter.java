package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CityBean;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/11/18.
 *
 */

public class LocalSelectAdapter extends BaseAdapter{
    private List<CityBean> mlist;
    private Context mContext;

    public void setMlist(List<CityBean> mlist) {
        this.mlist = mlist;
    }

    public LocalSelectAdapter(Context context, List<CityBean> list){
        this.mlist = list;
        this.mContext = context;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.city_list_item, null);
            viewHolder.tv_cName = (TextView) convertView.findViewById(R.id.tv_city_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_cName.setText(mlist.get(position).getName()+"");
        return convertView;
    }

    class ViewHolder{
       private TextView tv_cName;
    }
}
