package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.R;
import java.util.List;

/**
 * 自定义通讯录adapter
 * @date 2016-6-29
 * @author wangshengbo
 */
public class PhoneBooksAdapter extends BaseAdapter {
    private List<DepartmentBean> mlist;
    private Context context;

    public void setMlist(List<DepartmentBean> mlist) {
        this.mlist = mlist;
    }

    public PhoneBooksAdapter(Context context, List<DepartmentBean> list) {
        this.context = context;
        this.mlist = list;
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //获得组件，实例化组件
            convertView = View.inflate(context,R.layout.list_setting, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_set_tv);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.list_set_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //绑定数据
        viewHolder.title.setText(mlist.get(position).getDD_NAME()+"");
        viewHolder.iv_icon.setImageResource(R.mipmap.function_set);
        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public ImageView iv_icon;
    }
}
