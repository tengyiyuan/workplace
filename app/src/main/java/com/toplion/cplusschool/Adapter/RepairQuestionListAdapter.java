package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.RepairQuestionBean;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang on 2016/5/3 9:30.
 * 常见问题及解决办法列表
 */
public class RepairQuestionListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<RepairQuestionBean> mlist;
    private ViewHolder viewHolder;


    public void setMlist(List<RepairQuestionBean> mlist) {
        this.mlist = mlist;
    }

    public RepairQuestionListAdapter(Context context, List<RepairQuestionBean> list){
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
            convertView = View.inflate(mcontext, R.layout.repair_list_item,null);
            viewHolder.qContent = (TextView) convertView.findViewById(R.id.list_repair_tv_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.qContent.setText(mlist.get(position).getCF_TITLE());
        return convertView;
    }


    class ViewHolder{
        private TextView qContent;//问题描述
    }
}
