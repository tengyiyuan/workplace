package com.toplion.cplusschool.TeacherContacts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Activity.ContactsSearcheActivity;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.JobBean;
import com.toplion.cplusschool.Bean.SeniorBean;
import com.toplion.cplusschool.Bean.SeniorListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.ContactsDao;
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
 * Created by wangshengbo
 * on 2016/9/13.
 * @教职工通讯录
 */
public class TeaContactsListActivity extends BaseActivity {
   private  ArrayList<ContactsBean> colplist;
    private ArrayList<ContactsBean> newplist;
    private ImageView iv_tea_contacts_return;//返回
    private ListView lv_tea_contacts_list;//列表
    private RelativeLayout rl_tea_contacts_depart;//部门筛选
    private TextView tv_tea_contacts_dialog;//当前选中的字母
    private SideBar bar_tea_contacts_sidebar;//字母列表
    private ImageView iv_tea_contacts_search;//搜索
    private TeaContactsAdapter tcdapter;
    private List<PersonBean> data;
    private List<SeniorBean> slist;//部门
    private List<JobBean> jlist;//职位
    private List<com.toplion.cplusschool.Bean.PersonBean> personlist;//人员
    private List<String> glist;//年级列表
    private List<String> plist;//专业列表
    private List<String> clist;//班级列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_contacts_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        rl_tea_contacts_depart = (RelativeLayout) findViewById(R.id.rl_tea_contacts_depart);
        iv_tea_contacts_return = (ImageView) findViewById(R.id.iv_tea_contacts_return);
        lv_tea_contacts_list = (ListView) findViewById(R.id.lv_tea_contacts_list);
        iv_tea_contacts_search = (ImageView) findViewById(R.id.iv_tea_contacts_search);
        tv_tea_contacts_dialog = (TextView) findViewById(R.id.tv_tea_contacts_dialog);
        bar_tea_contacts_sidebar = (SideBar) findViewById(R.id.bar_tea_contacts_sidebar);
        bar_tea_contacts_sidebar.setTextView(tv_tea_contacts_dialog);
//        contactsDao = new ContactsDao(this);
        newplist = new ArrayList<ContactsBean>();
        glist = new ArrayList<String>();
        //getData();
        getTeaPhone();
        getTeacherContacts();
        setListener();
    }

//    @Override
//    protected void getData() {
//        super.getData();
//        String [] datas = getResources().getStringArray(R.array.listpersons);
//        data = getTeaData(datas);
//        // 数据在放在adapter之前需要排序
//        Collections.sort(data, new PinyinComparator());
//        tcdapter = new TeaContactsAdapter(this, data);
//        lv_tea_contacts_list.setAdapter(tcdapter);
//    }
    //获取教职工通讯录
    private void getTeacherContacts() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getDepartInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url, new CallBackParent(this, getResources().getString(R.string.loading), "getDepartInfo") {
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
                    } else {
                        ToastManager.getInstance().showToast(TeaContactsListActivity.this, "暂无数据");
                    }
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                bar_tea_contacts_sidebar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void setListener() {
        super.setListener();
        iv_tea_contacts_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_tea_contacts_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SelectPopupWindow selectWindow = new SelectPopupWindow(TeaContactsListActivity.this);
//                selectWindow.showAtLocation(rl_tea_contacts_depart,Gravity.BOTTOM , 0, 0);
                selectWindow.showAsDropDown(rl_tea_contacts_depart);
                selectWindow.gAdapter.setMlist(glist);
                selectWindow.lv_contants_grade.setAdapter(selectWindow.gAdapter);
                selectWindow.lv_contants_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        selectWindow.pAdapter.setMlist(plist);
                        selectWindow.lv_contacts_professional.setAdapter( selectWindow.pAdapter);
                        selectWindow.gAdapter.setSelectItem(position);
                        selectWindow.gAdapter.notifyDataSetChanged();
                        selectWindow.pAdapter.setSelectItem(-1);
                        selectWindow.pAdapter.notifyDataSetChanged();
                    }
                });
                selectWindow.lv_contacts_professional.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        personlist = new ArrayList<com.toplion.cplusschool.Bean.PersonBean>();
                        personlist = jlist.get(position).getPersonname();
                        clist = new ArrayList<String>();
                        for (int i = 0; i < personlist.size(); i++) {
                            clist.add(personlist.get(i).getName());
                        }
                        selectWindow.cAdapter.setMlist(clist);
                        selectWindow.lv_contacts_class.setAdapter(selectWindow.cAdapter);
                        selectWindow.cAdapter.notifyDataSetChanged();
                        selectWindow. pAdapter.setSelectItem(position);
                        selectWindow.pAdapter.notifyDataSetChanged();
                    }
                });
                selectWindow.lv_contacts_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(TeaContactsListActivity.this, ContactsTeaDetailActivity.class);
//                        Intent intent = new Intent(TeaContactsListActivity.this, TeaContactsDetailActivity.class);
                        intent.putExtra("teaNo", personlist.get(position).getNameid());
                        startActivity(intent);
                    }
                });
            }
        });
        iv_tea_contacts_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(colplist.size()>0) {
                    Intent intent = new Intent(TeaContactsListActivity.this, ContactsTeaSearcheActivity.class);
                    intent.putExtra("colplist",colplist);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }
            }
        });
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
                final MenuPopupWindow menuWindow = new MenuPopupWindow(TeaContactsListActivity.this);
                menuWindow.showAtLocation(TeaContactsListActivity.this.findViewById(R.id.ll_pop), Gravity.BOTTOM , 0, 0);
                    menuWindow.setCallOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(colplist.get(position).getSJH())){
                            final List<CommonBean> plist = new ArrayList<CommonBean>();
                            plist.add(new CommonBean("0", colplist.get(position).getJTDH()));
                            if(!TextUtils.isEmpty(colplist.get(position).getSJH())){
                                plist.add(new CommonBean("1",colplist.get(position).getSJH()));
                            }
                            final CustomDialogListview dialog_sex = new CustomDialogListview(TeaContactsListActivity.this, "选择要拨打的电话", plist,"");
                            CustomDialogListview.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CallUtil.CallPhone(TeaContactsListActivity.this, plist.get(position).getDes());
                                    dialog_sex.dismiss();
                                }
                            });
                            dialog_sex.show();
//                            CallUtil.CallPhone(TeaContactsListActivity.this,colplist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(TeaContactsListActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setCopyOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        // 得到剪贴板管理器
                        if(!TextUtils.isEmpty(colplist.get(position).getSJH())) {
                            CallUtil.copyPhone(TeaContactsListActivity.this, colplist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(TeaContactsListActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setsaveOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(colplist.get(position).getSJH())) {
                            CallUtil.toContacts(TeaContactsListActivity.this, colplist.get(position).getXM(), "", colplist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(TeaContactsListActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setMessageOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(colplist.get(position).getSJH())) {
                            CallUtil.sendMessage(TeaContactsListActivity.this, colplist.get(position).getSJH(), "");
                        }else{
                            ToastManager.getInstance().showToast(TeaContactsListActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setDetailOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        Intent intent = new Intent(TeaContactsListActivity.this, ContactsTeaDetailActivity.class);
                        intent.putExtra("teaNo", colplist.get(position).getXH());
                        startActivity(intent);
                    }
                });

            }
        });
    }

    private List<PersonBean> getTeaData(String[] data) {
        List<PersonBean> listarray = new ArrayList<PersonBean>();
        for (int i = 0; i < data.length; i++) {
            String pinyin = PinYinUtils.getPingYin(data[i]);
            String Fpinyin = pinyin.substring(0, 1).toUpperCase();
            PersonBean person = new PersonBean();
            person.setName(data[i]);
            person.setPinYin(pinyin);
            // 正则表达式，判断首字母是否是英文字母
            if (Fpinyin.matches("[A-Z]")) {
                person.setFirstPinYin(Fpinyin);
            } else {
                person.setFirstPinYin("#");
            }
            listarray.add(person);
        }
        return listarray;
    }
    //获取所有教职工信息
    private void getTeaPhone(){
        String   url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAllTeacherInfo") + Constants.BASEPARAMS;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(json.getString("data"));
                    colplist=new ArrayList<ContactsBean>();
                    for(int i= 0;i<jsonArray.length();i++){
                        bar_tea_contacts_sidebar.setVisibility(View.VISIBLE);
//                        ContactsBean cbean = new ContactsBean();
//                        cbean.setXH(jsonArray.getJSONObject(i).getString("num"));
//                        cbean.setXM(jsonArray.getJSONObject(i).getString("name"));
//                        cbean.setXMPY(PinYinUtils.getPinYin(jsonArray.getJSONObject(i).getString("name")));
//                        cbean.setSJH(jsonArray.getJSONObject(i).getString("phonenum"));
//                        cbean.setJTDZ(jsonArray.getJSONObject(i).getString("departments"));
//                        newplist.add(cbean);

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String pinyin = PinYinUtils.getPingYin(Function.getInstance().getString(jsonObject,"name"));
                        String Fpinyin = pinyin.substring(0, 1).toUpperCase();
                        ContactsBean person = new ContactsBean();
                        person.setXM(Function.getInstance().getString(jsonObject,"name"));
                        person.setJTDZ(Function.getInstance().getString(jsonObject,"departments"));
                        person.setSJH(Function.getInstance().getString(jsonObject,"phonenum"));
                        person.setJTDH(Function.getInstance().getString(jsonObject,"gzdh"));
                        person.setXH(Function.getInstance().getString(jsonObject,"num"));
                        person.setXMPY(PinYinUtils.getPinYin(Function.getInstance().getString(jsonObject,"name")));
                        person.setPinYin(pinyin);
                        String hearPinYin = PinYinUtils.getPinYinHeadChar(Function.getInstance().getString(jsonObject,"name"));
                        person.setPinYinHeadChar(hearPinYin);
                        // 正则表达式，判断首字母是否是英文字母
                        if (Fpinyin.matches("[A-Z]")) {
                            person.setFirstPinYin(Fpinyin);
                        } else {
                            person.setFirstPinYin("#");
                        }
                        colplist.add(person);
                    }
//                     colplist=new ArrayList<ContactsBean>();
//                    for (int i = 0; i < newplist.size(); i++) {
//                        String pinyin = PinYinUtils.getPingYin(newplist.get(i).getXM());
//                        String Fpinyin = pinyin.substring(0, 1).toUpperCase();
//                        ContactsBean person = new ContactsBean();
//                        person.setXM(newplist.get(i).getXM());
//                        person.setJTDH(newplist.get(i).getJTDH());
//                        person.setJTDZ(newplist.get(i).getJTDZ());
//                        person.setLXDH(newplist.get(i).getLXDH());
//                        person.setSJH(newplist.get(i).getSJH());
//                        person.setXH(newplist.get(i).getXH());
//                        person.setXMPY(newplist.get(i).getXMPY());
//                        person.setPinYin(pinyin);
//                        // 正则表达式，判断首字母是否是英文字母
//                        if (Fpinyin.matches("[A-Z]")) {
//                            person.setFirstPinYin(Fpinyin);
//                        } else {
//                            person.setFirstPinYin("#");
//                        }
//                        colplist.add(person);
//                    }
                    Collections.sort(colplist, new PinyinComparator());
                    tcdapter = new TeaContactsAdapter(TeaContactsListActivity.this, colplist);
                    lv_tea_contacts_list.setAdapter(tcdapter);
//                    contactsDao.startWritableDatabase(true);
//                    contactsDao.deleteAll();
//                    contactsDao.insertList(colplist);
//                    contactsDao.closeDatabase();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
