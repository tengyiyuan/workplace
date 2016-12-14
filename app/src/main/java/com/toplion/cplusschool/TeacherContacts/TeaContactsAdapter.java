package com.toplion.cplusschool.TeacherContacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.GradeInfoBean;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/13.
 * 教职工通讯录适配器
 */
public class TeaContactsAdapter extends BaseAdapter {
    private List<ContactsBean> persons;
    private Context mcontext;

    public TeaContactsAdapter(Context context, List<ContactsBean> list) {
        this.mcontext = context;
        this.persons = list;
    }
    public void setMlist(List<ContactsBean> persons) {
        this.persons = persons;
    }
    @Override
    public int getCount() {
        return persons.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        ContactsBean person = persons.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.tea_contacts_list_item, null);
            viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_lv_item_tag);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_lv_item_name);
            viewHolder.tv_tea_contacts_phone=(TextView)convertView.findViewById(R.id.tv_tea_contacts_phone);
            viewHolder.rl_tea_contacts_play = (RelativeLayout) convertView.findViewById(R.id.rl_tea_contacts_play);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int selection = person.getFirstPinYin().charAt(0);
        // 通过首字母的assii值来判断是否显示字母
        int positionForSelection = getPositionForSelection(selection);
        if (position == positionForSelection) {// 相等说明需要显示字母
            viewHolder.tv_tag.setVisibility(View.VISIBLE);
            viewHolder.tv_tag.setText(person.getFirstPinYin());
        } else {
            viewHolder.tv_tag.setVisibility(View.GONE);

        }
        viewHolder.tv_name.setText(person.getXM());
        viewHolder.tv_tea_contacts_phone.setText(person.getSJH()+"/"+person.getJTDH());
        return convertView;
    }

    public int getPositionForSelection(int selection) {
        for (int i = 0; i < persons.size(); i++) {
            String Fpinyin = persons.get(i).getFirstPinYin();
            char first = Fpinyin.toUpperCase().charAt(0);
            if (first == selection) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        RelativeLayout rl_tea_contacts_play;
        TextView tv_tag;
        TextView tv_name;
        TextView tv_tea_contacts_phone;
    }
}
