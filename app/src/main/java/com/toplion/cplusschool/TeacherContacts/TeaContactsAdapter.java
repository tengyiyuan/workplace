package com.toplion.cplusschool.TeacherContacts;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.GradeInfoBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CustomDialogListview;

import java.util.ArrayList;
import java.util.Iterator;
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
        final ContactsBean person = persons.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.tea_contacts_list_item, null);
            viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_lv_item_tag);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_lv_item_name);
            viewHolder.iv_tea_contacts_call = (ImageView) convertView.findViewById(R.id.iv_tea_contacts_call);
            viewHolder.tv_tea_contacts_phone = (TextView) convertView.findViewById(R.id.tv_tea_contacts_phone);
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
        viewHolder.rl_tea_contacts_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ContactsTeaDetailActivity.class);
                intent.putExtra("teaNo", person.getXH());
                mcontext.startActivity(intent);
            }
        });
        viewHolder.tv_name.setText(person.getXM());
        viewHolder.tv_tea_contacts_phone.setText(person.getSJH() + "/" + person.getJTDH());
        viewHolder.iv_tea_contacts_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(person.getSJH()) && TextUtils.isEmpty(person.getJTDH())) {
                    ToastManager.getInstance().showToast(mcontext, "暂无电话号码");
                } else {
                    final List<CommonBean> plist = new ArrayList<CommonBean>();
                    if (!TextUtils.isEmpty(person.getJTDH())) {
                        plist.add(new CommonBean("0", person.getJTDH()));
                    }
                    if (!TextUtils.isEmpty(person.getSJH())) {
                        plist.add(new CommonBean("1", person.getSJH()));
                    }
                    final CustomDialogListview dialog_sex = new CustomDialogListview(mcontext, "选择要拨打的电话", plist, "");
                    CustomDialogListview.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CallUtil.CallPhone(mcontext, plist.get(position).getDes());
                            dialog_sex.dismiss();
                        }
                    });
                    dialog_sex.show();
                }
            }
        });
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
        ImageView iv_tea_contacts_call;
        TextView tv_tea_contacts_phone;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ContactsBean> list) {
        this.persons = list;
        notifyDataSetChanged();
    }

}
