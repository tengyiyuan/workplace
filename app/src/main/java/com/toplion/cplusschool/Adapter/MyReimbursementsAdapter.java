package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/8/2.
 *
 * @Des我的报销adapte
 */
public class MyReimbursementsAdapter extends BaseAdapter {
    private List<String> mlist;
    private Context mcontext;
    private ViewHolder viewHolder;

    public void setMlist(List<String> mlist) {
        this.mlist = mlist;
    }

    public MyReimbursementsAdapter(Context context, List<String> list) {
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.my_reimbursement_list_item, null);
            viewHolder.tv_myreimbursement_number = (TextView) convertView.findViewById(R.id.tv_myreimbursement_number);
            viewHolder.tv_myreimbursement_state = (TextView) convertView.findViewById(R.id.tv_myreimbursement_state);
            viewHolder.tv_myreimbursement_type = (TextView) convertView.findViewById(R.id.tv_myreimbursement_type);
            viewHolder.tv_myreimbursement_time = (TextView) convertView.findViewById(R.id.tv_myreimbursement_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_myreimbursement_number.setText(mlist.get(position));
//        viewHolder.tv_myreimbursement_state.setText(mlist.get(position));
//        viewHolder.tv_myreimbursement_type.setText(mlist.get(position));
//        viewHolder.tv_myreimbursement_time.setText(mlist.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView tv_myreimbursement_number;//报销单号
        private TextView tv_myreimbursement_state;//报销状态
        private TextView tv_myreimbursement_type;//类型
        private TextView tv_myreimbursement_time;//时间
    }
}
