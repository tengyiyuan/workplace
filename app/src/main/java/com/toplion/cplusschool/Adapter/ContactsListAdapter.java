package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/7/14.
 * 通讯录列表适配器
 */
public class ContactsListAdapter extends BaseAdapter{
    private ViewHolder viewHolder;
    private Context mcontext;
    private List<String> mlist;
    public List<String> getMlist() {
        return mlist;
    }

    public void setMlist(List<String> mlist) {
        this.mlist = mlist;
    }
    public ContactsListAdapter(Context context){
        this.mcontext = context;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.contacts_item,null);
            viewHolder.tv_contacts_item_des = (TextView) convertView.findViewById(R.id.tv_contacts_item_des);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_contacts_item_des.setText(mlist.get(position));
        if(position == selectItem){
            convertView.setBackgroundResource(R.color.bg_table);
            viewHolder.tv_contacts_item_des.setTextColor(mcontext.getResources().getColor(R.color.logo_color));
        }else{
            viewHolder.tv_contacts_item_des.setTextColor(mcontext.getResources().getColor(R.color.gray333));
            convertView.setBackgroundResource(R.color.white);
        }
        return convertView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=-1;
    class ViewHolder{
        private TextView tv_contacts_item_des;//年级、专业、班级
    }
}
