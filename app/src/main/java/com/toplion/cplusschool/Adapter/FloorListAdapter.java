package com.toplion.cplusschool.Adapter;

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
 * on 2016/4/20.
 * 教学楼列表
 * @updatetime 2016-6-12 调试接口，显示真是数据
 */
public class FloorListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<FloorBean> mlist;
    private ViewHolder viewHolder;

    public List<FloorBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<FloorBean> mlist) {
        this.mlist = mlist;
    }

    public FloorListAdapter(Context context, List<FloorBean> list){
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
            convertView = View.inflate(mcontext, R.layout.floor_list_item,null);
            viewHolder.tvFName = (TextView) convertView.findViewById(R.id.tv_fname);
            viewHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_floor_num);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFName.setText(mlist.get(position).getJXLM()+"");
        viewHolder.tvNum.setText("("+mlist.get(position).getKJSSL()+")");
        return convertView;
    }


    class ViewHolder{
        private TextView tvFName;//楼层名称
        private TextView tvNum;//空教室是数量
    }
}
