package com.toplion.cplusschool.Reimburse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.FloorBean;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wagnshengbo
 * on 2016/9/14.
 * 选择时间列表
 */
public class SelectTimeListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<TimeBean> mlist;
    private ViewHolder viewHolder;

    public List<TimeBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<TimeBean> mlist) {
        this.mlist = mlist;
    }

    public SelectTimeListAdapter(Context context, List<TimeBean> list){
        this.mcontext = context;
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
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.select_time_list_item,null);
            viewHolder.tvFName = (TextView) convertView.findViewById(R.id.tv_fname);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFName.setText(mlist.get(position).getTimeStr());
        if(position == selectItem){
            viewHolder.tvFName.setTextColor(mcontext.getResources().getColor(R.color.white));
            convertView.setBackgroundResource(R.color.logo_color);
        }else{
            viewHolder.tvFName.setTextColor(mcontext.getResources().getColor(R.color.black));
            convertView.setBackgroundResource(R.color.white);
        }
        return convertView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=-1;
    class ViewHolder{
        private TextView tvFName;//楼层名称
    }
}
