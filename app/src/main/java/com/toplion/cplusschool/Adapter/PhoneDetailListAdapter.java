package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;

import java.util.List;

/**
 * 自定义部门通讯录列表adapter
 * @date 2016-6-29
 * @author wangshengbo
 */
public class PhoneDetailListAdapter extends BaseAdapter {
    private List<DepartmentBean> mlist;
    private Context mcontext;
    private int tag = 0;

    public void setMlist(List<DepartmentBean> mlist) {
        this.mlist = mlist;
    }

    public PhoneDetailListAdapter(Context context, List<DepartmentBean> list,int code) {
        this.mcontext = context;
        this.mlist = list;
        this.tag = code;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(tag == Constants.PHONECODE){
            if (convertView == null) {
                viewHolder = new ViewHolder();
                //获得组件，实例化组件
                convertView = View.inflate(mcontext,R.layout.phone_detail_list_item, null);
                viewHolder.tv_departments_name = (TextView) convertView.findViewById(R.id.tv_departments_name);
                viewHolder.tv_departments_jiancheng = (TextView) convertView.findViewById(R.id.tv_departments_jiancheng);
                viewHolder.tv_departments_phone = (TextView) convertView.findViewById(R.id.tv_departments_phone);
                viewHolder.iv_call_phone = (ImageView) convertView.findViewById(R.id.iv_call_phone);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_departments_name.setText(mlist.get(position).getDD_NAME()+"");
            viewHolder.tv_departments_jiancheng.setText(mlist.get(position).getFATHER_NAME()+"");
            viewHolder.tv_departments_phone.setText(mlist.get(position).getDP_PHONE()+"");
            final String phone = mlist.get(position).getDP_PHONE();
            viewHolder.iv_call_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallUtil.CallPhone(mcontext,phone);
                }
            });
        }else if(tag == Constants.CONTACTSCODE){
            if (convertView == null) {
                viewHolder = new ViewHolder();
                //获得组件，实例化组件
                convertView = View.inflate(mcontext,R.layout.contacts_detail_list_item, null);
                viewHolder.tv_departments_name = (TextView) convertView.findViewById(R.id.tv_departments_name);
                viewHolder.tv_departments_phone = (TextView) convertView.findViewById(R.id.tv_departments_phone);
                viewHolder.iv_call_phone = (ImageView) convertView.findViewById(R.id.iv_call_phone);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_departments_name.setText(mlist.get(position).getDD_NAME()+"");
            viewHolder.tv_departments_phone.setText(mlist.get(position).getDP_PHONE()+"");
            final String phone = mlist.get(position).getDP_PHONE();
            viewHolder.iv_call_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallUtil.CallPhone(mcontext,phone);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_departments_name;//部门名称
        private TextView tv_departments_jiancheng;//部门简称
        private TextView tv_departments_phone;//部门电话
        private ImageView iv_call_phone;//打电话
    }
}
