package com.toplion.cplusschool.TeacherContacts;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbSharedUtil;
import com.toplion.cplusschool.Adapter.ContactsListAdapter;
import com.toplion.cplusschool.Bean.JobBean;
import com.toplion.cplusschool.Bean.SeniorBean;
import com.toplion.cplusschool.Bean.SeniorListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.Bean.PersonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/11/30.
 * 教工通讯录 -- 姓名排序
 */

public class DepartmentListFragment extends Fragment {
    private AbHttpUtil abHttpUtil;
    public ListView lv_contants_grade;//部门
    public ListView lv_contacts_professional;//职位
    public ListView lv_contacts_class;//姓名
    public ContactsListAdapter gAdapter;//适配器
    public ContactsListAdapter pAdapter;//适配器
    public ContactsListAdapter cAdapter;//适配器
    private List<SeniorBean> slist;//部门
    private List<JobBean> jlist;//职位
    private List<PersonBean> personlist;//人员
    private List<String> glist;//部门列表
    private List<String> plist;//职位列表
    private List<String> clist;//人员

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.department_list_contacts, container, false);
        boolean isFirst = AbSharedUtil.getBoolean(getActivity(), "mcIsFirsts", true);
        if (isFirst) {//是不是第一次打开该功能
            View mcView = inflater.inflate(R.layout.mengceng, null);
            addLayer(getActivity(), mcView,view);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        abHttpUtil = AbHttpUtil.getInstance(getActivity());
        lv_contants_grade = (ListView) view.findViewById(R.id.lv_contants_grade);
        lv_contacts_professional = (ListView) view.findViewById(R.id.lv_contacts_professional);
        lv_contacts_class = (ListView) view.findViewById(R.id.lv_contacts_class);
        gAdapter = new ContactsListAdapter(getActivity());
        pAdapter = new ContactsListAdapter(getActivity());
        cAdapter = new ContactsListAdapter(getActivity());
        glist = new ArrayList<String>();
        getData();
        setListener();
    }

    private void getData() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getDepartInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url, new CallBackParent(getActivity(), getString(R.string.loading), "getDepartInfo") {
            @Override
            public void Get_Result(String result) {
                SeniorListBean dataBean = AbJsonUtil.fromJson(result, SeniorListBean.class);
                if (dataBean != null) {
                    if (dataBean.getData() != null && dataBean.getData().size() > 0) {
                        slist = new ArrayList<SeniorBean>();
                        slist = dataBean.getData();
                        for (int i = 0; i < slist.size(); i++) {
                            glist.add(slist.get(i).getSeniorname());
                        }
                        gAdapter.setMlist(glist);
                        lv_contants_grade.setAdapter(gAdapter);
                        initListView();
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "暂无数据");
                    }
                }
            }
        });
    }
    private void initListView(){
        jlist = new ArrayList<JobBean>();
        jlist = slist.get(0).getDepartments();
        plist = new ArrayList<String>();
        for (int i = 0; i < jlist.size(); i++) {
            plist.add(jlist.get(i).getDepartname());
        }
        if (clist != null && clist.size() > 0) {
            clist.clear();
        }
        pAdapter.setMlist(plist);
        lv_contacts_professional.setAdapter(pAdapter);
        gAdapter.setSelectItem(0);
        gAdapter.notifyDataSetInvalidated();
        personlist = new ArrayList<PersonBean>();
        personlist = jlist.get(0).getPersonname();
        clist = new ArrayList<String>();
        for (int i = 0; i < personlist.size(); i++) {
            clist.add(personlist.get(i).getName());
        }
        cAdapter.setMlist(clist);
        lv_contacts_class.setAdapter(cAdapter);
        cAdapter.notifyDataSetInvalidated();
        pAdapter.setSelectItem(0);
        pAdapter.notifyDataSetInvalidated();
    }

    private void setListener() {
        lv_contants_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jlist = new ArrayList<JobBean>();
                jlist = slist.get(position).getDepartments();
                plist = new ArrayList<String>();
                for (int i = 0; i < jlist.size(); i++) {
                    plist.add(jlist.get(i).getDepartname());
                }
                if (clist != null && clist.size() > 0) {
                    clist.clear();
                }
                pAdapter.setMlist(plist);
                lv_contacts_professional.setAdapter(pAdapter);
                gAdapter.setSelectItem(position);
                gAdapter.notifyDataSetInvalidated();
                pAdapter.setSelectItem(-1);
                pAdapter.notifyDataSetInvalidated();
            }
        });
        lv_contacts_professional.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personlist = new ArrayList<PersonBean>();
                personlist = jlist.get(position).getPersonname();
                clist = new ArrayList<String>();
                for (int i = 0; i < personlist.size(); i++) {
                    clist.add(personlist.get(i).getName());
                }
                cAdapter.setMlist(clist);
                lv_contacts_class.setAdapter(cAdapter);
                cAdapter.notifyDataSetInvalidated();
                pAdapter.setSelectItem(position);
                pAdapter.notifyDataSetInvalidated();
            }
        });
        lv_contacts_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContactsTeaDetailActivity.class);
                intent.putExtra("teaNo", personlist.get(position).getNameid());
                startActivity(intent);
            }
        });
    }

    /**
     * 为rootView添加蒙层
     *
     * @return
     */
    public static void addLayer(final Activity mContext, final View layerView,View farentView) {
        if (mContext == null || layerView == null)
            return;
        final ViewGroup contentView = (ViewGroup) mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        contentView.addView(layerView);
        contentView.findViewById(R.id.rl_parent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AbSharedUtil.putBoolean(mContext, "mcIsFirsts", false);
                contentView.removeView(layerView);
                return true;
            }
        });
    }

}
