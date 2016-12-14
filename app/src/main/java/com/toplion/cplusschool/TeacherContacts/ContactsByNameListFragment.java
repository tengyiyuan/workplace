package com.toplion.cplusschool.TeacherContacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;
import com.toplion.cplusschool.widget.MenuPopupWindow;
import com.toplion.cplusschool.widget.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wang
 * on 2016/11/30.
 * 教工通讯录 -- 部门筛选
 */

public class ContactsByNameListFragment extends Fragment {
    private AbHttpUtil abHttpUtil;
    private ArrayList<ContactsBean> colplist;
    private ListView lv_tea_contacts_list;//列表
    private TextView tv_tea_contacts_dialog;//当前选中的字母
    private SideBar bar_tea_contacts_sidebar;//字母列表
    private TeaContactsAdapter tcdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_by_name_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        abHttpUtil = AbHttpUtil.getInstance(getActivity());
        lv_tea_contacts_list = (ListView) view.findViewById(R.id.lv_tea_contacts_list);
        tv_tea_contacts_dialog = (TextView) view.findViewById(R.id.tv_tea_contacts_dialog);
        bar_tea_contacts_sidebar = (SideBar) view.findViewById(R.id.bar_tea_contacts_sidebar);
        bar_tea_contacts_sidebar.setTextView(tv_tea_contacts_dialog);
        getData();
        setListener();
    }

    private void getData() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAllTeacherInfo") + Constants.BASEPARAMS;
        abHttpUtil.get(url, new CallBackParent(getActivity(), getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(json.getString("data"));
                    colplist = new ArrayList<ContactsBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bar_tea_contacts_sidebar.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String pinyin = PinYinUtils.getPingYin(Function.getInstance().getString(jsonObject, "name"));
                        String Fpinyin = pinyin.substring(0, 1).toUpperCase();
                        ContactsBean person = new ContactsBean();
                        person.setXM(Function.getInstance().getString(jsonObject, "name"));
                        person.setJTDZ(Function.getInstance().getString(jsonObject, "departments"));
                        person.setSJH(Function.getInstance().getString(jsonObject, "phonenum"));
                        person.setJTDH(Function.getInstance().getString(jsonObject, "gzdh"));
                        person.setXH(Function.getInstance().getString(jsonObject, "num"));
                        person.setXMPY(PinYinUtils.getPinYin(Function.getInstance().getString(jsonObject, "name")));
                        person.setPinYin(pinyin);
                        String hearPinYin = PinYinUtils.getPinYinHeadChar(Function.getInstance().getString(jsonObject, "name"));
                        person.setPinYinHeadChar(hearPinYin);
                        // 正则表达式，判断首字母是否是英文字母
                        if (Fpinyin.matches("[A-Z]")) {
                            person.setFirstPinYin(Fpinyin);
                        } else {
                            person.setFirstPinYin("#");
                        }
                        colplist.add(person);
                    }
                    Collections.sort(colplist, new PinyinComparator());
                    tcdapter = new TeaContactsAdapter(getActivity(), colplist);
                    lv_tea_contacts_list.setAdapter(tcdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        // 设置字母导航触摸监听
        bar_tea_contacts_sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // TODO Auto-generated method stub
                // 该字母首次出现的位置
                int position = tcdapter.getPositionForSelection(s.charAt(0));
                if (position != -1) {
                    lv_tea_contacts_list.setSelection(position);
                }
            }
        });
        lv_tea_contacts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(getActivity(), ContactsTeaDetailActivity.class);
                intent.putExtra("teaNo", colplist.get(position).getXH());
                startActivity(intent);
//                final MenuPopupWindow menuWindow = new MenuPopupWindow(getActivity());
//                menuWindow.showAtLocation(getActivity().findViewById(R.id.ll_pop), Gravity.BOTTOM, 0, 0);
//                menuWindow.setCallOnClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuWindow.dismiss();
//                        if (!TextUtils.isEmpty(colplist.get(position).getSJH())) {
//                            final List<CommonBean> plist = new ArrayList<CommonBean>();
//                            plist.add(new CommonBean("0", colplist.get(position).getJTDH()));
//                            if (!TextUtils.isEmpty(colplist.get(position).getSJH())) {
//                                plist.add(new CommonBean("1", colplist.get(position).getSJH()));
//                            }
//                            final CustomDialogListview dialog_sex = new CustomDialogListview(getActivity(), "选择要拨打的电话", plist, "");
//                            dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    CallUtil.CallPhone(getActivity(), plist.get(position).getDes());
//                                    dialog_sex.dismiss();
//                                }
//                            });
//                            dialog_sex.show();
////                            CallUtil.CallPhone(TeaContactsListActivity.this,colplist.get(position).getSJH());
//                        } else {
//                            ToastManager.getInstance().showToast(getActivity(), "暂无电话号码");
//                        }
//                    }
//                });
//                menuWindow.setCopyOnClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuWindow.dismiss();
//                        // 得到剪贴板管理器
//                        if (!TextUtils.isEmpty(colplist.get(position).getSJH())) {
//                            CallUtil.copyPhone(getActivity(), colplist.get(position).getSJH());
//                        } else {
//                            ToastManager.getInstance().showToast(getActivity(), "暂无电话号码");
//                        }
//                    }
//                });
//                menuWindow.setsaveOnClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuWindow.dismiss();
//                        if (!TextUtils.isEmpty(colplist.get(position).getSJH())) {
//                            CallUtil.toContacts(getActivity(), colplist.get(position).getXM(), "", colplist.get(position).getSJH());
//                        } else {
//                            ToastManager.getInstance().showToast(getActivity(), "暂无电话号码");
//                        }
//                    }
//                });
//                menuWindow.setMessageOnClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuWindow.dismiss();
//                        if (!TextUtils.isEmpty(colplist.get(position).getSJH())) {
//                            CallUtil.sendMessage(getActivity(), colplist.get(position).getSJH(), "");
//                        } else {
//                            ToastManager.getInstance().showToast(getActivity(), "暂无电话号码");
//                        }
//                    }
//                });
//                menuWindow.setDetailOnClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuWindow.dismiss();
//                        Intent intent = new Intent(getActivity(), ContactsTeaDetailActivity.class);
//                        intent.putExtra("teaNo", colplist.get(position).getXH());
//                        startActivity(intent);
//                    }
//                });

            }
        });
    }

}
