package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.ContactsDetailActivity;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.TeacherContacts.ContactsTeaDetailActivity;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;

import java.util.List;

/**
 * Created by wang
 * on 2016/7/14.
 *
 * @des 通讯录班级列表
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<ContactsBean> clist;
    private SharePreferenceUtils share;
    private int mtype;

    public void setMlist(List<ContactsBean> mlist) {
        this.clist = mlist;
        share = new SharePreferenceUtils(mcontext);

    }

    public MyExpandableListViewAdapter(Context context,int type) {
        this.mcontext = context;
        this.mtype = type;
    }

    /**
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return clist.size();
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition) {
        return clist.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.contacts_detail_list_item, null);
            groupHolder = new GroupHolder();
            groupHolder.tv_contacts_number_str = (TextView) convertView.findViewById(R.id.tv_contacts_number_str);
            groupHolder.tv_contacts_number = (TextView) convertView.findViewById(R.id.tv_contacts_number);
            groupHolder.tv_contacts_phone = (TextView) convertView.findViewById(R.id.tv_contacts_phone);
            groupHolder.tv_contacts_name = (TextView) convertView.findViewById(R.id.tv_contacts_name);
            groupHolder.iv_call_phone = (ImageView) convertView.findViewById(R.id.iv_call_phone);
            groupHolder.iv_call_detail = (ImageView) convertView.findViewById(R.id.iv_call_detail);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if(mtype == 1){
            groupHolder.tv_contacts_number_str.setText("学号:");
        }else if(mtype == 2){
            groupHolder.tv_contacts_number_str.setText("工号:");
        }
        groupHolder.tv_contacts_number.setText(clist.get(groupPosition).getXH());
        groupHolder.tv_contacts_phone.setText(clist.get(groupPosition).getSJH());
        groupHolder.tv_contacts_name.setText(clist.get(groupPosition).getXM());
        if(share.getInt("ROLE_TYPE",0) == 1){
            groupHolder.iv_call_detail.setVisibility(View.VISIBLE);
        }else{
            groupHolder.iv_call_detail.setVisibility(View.GONE);
        }
        final String phoneNumber = clist.get(groupPosition).getSJH();
        groupHolder.iv_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumber)){
                    CallUtil.CallPhone(mcontext,phoneNumber);
                }else{
                    ToastManager.getInstance().showToast(mcontext,"暂无电话号码");
                }
            }
        });
        groupHolder.iv_call_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mtype == 1){
                        Intent intent = new Intent(mcontext, ContactsDetailActivity.class);
                        intent.putExtra("stuNo",clist.get(groupPosition).getXH());
                        mcontext.startActivity(intent);
                    }else if(mtype == 2){
                        Intent intent = new Intent(mcontext, ContactsTeaDetailActivity.class);
                        intent.putExtra("teaNo",clist.get(groupPosition).getXH());
                        mcontext.startActivity(intent);
                    }
            }
        });
        convertView.setPadding(mcontext.getResources().getDimensionPixelOffset( R.dimen.weekItemWidth),0,0,0);
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.contacts_detail_list_child_item, null);
            itemHolder = new ItemHolder();
            itemHolder.ll_contacts_child_message = (LinearLayout) convertView.findViewById(R.id.ll_contacts_child_message);
            itemHolder.ll_contacts_child_copy = (LinearLayout) convertView.findViewById(R.id.ll_contacts_child_copy);
            itemHolder.ll_contacts_child_save = (LinearLayout) convertView.findViewById(R.id.ll_contacts_child_save);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        final String phoneNumber = clist.get(groupPosition).getSJH();
        //发短信
        itemHolder.ll_contacts_child_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumber)) {
                    CallUtil.sendMessage(mcontext, phoneNumber, "");
                }else{
                    ToastManager.getInstance().showToast(mcontext,"暂无电话号码");
                }
            }
        });
        //复制
        itemHolder.ll_contacts_child_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                if(!TextUtils.isEmpty(phoneNumber)) {
                    CallUtil.copyPhone(mcontext, phoneNumber);
                }else{
                    ToastManager.getInstance().showToast(mcontext,"暂无电话号码");
                }
            }
        });
        final String name = clist.get(groupPosition).getXM();
        //保存
        itemHolder.ll_contacts_child_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumber)) {
                    CallUtil.toContacts(mcontext, name, "", phoneNumber);
                }else{
                    ToastManager.getInstance().showToast(mcontext,"暂无电话号码");
                }
            }
        });
        return convertView;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

class GroupHolder {
    TextView tv_contacts_number_str;//
    TextView tv_contacts_number;//编号
    TextView tv_contacts_phone;//电话
    TextView tv_contacts_name;//姓名
    ImageView iv_call_phone;
    ImageView iv_call_detail;//查看详情
}

class ItemHolder {
    LinearLayout ll_contacts_child_message;
    LinearLayout ll_contacts_child_copy;
    LinearLayout ll_contacts_child_save;
}
