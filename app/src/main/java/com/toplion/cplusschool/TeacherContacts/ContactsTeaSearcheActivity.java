package com.toplion.cplusschool.TeacherContacts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.MyExpandableListViewAdapter;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.ContactsListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.ContactsDao;
import com.toplion.cplusschool.widget.CustomDialogListview;
import com.toplion.cplusschool.widget.MenuPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by wangshengbo
 * on 2016/9/14.
 *
 * @des 教工通讯录模糊搜索
 */
public class ContactsTeaSearcheActivity extends BaseActivity {
    private ImageView iv_search_contants_return;//返回键
    private EditText et_search_contants_content;//搜索的内容
    private ListView elv_contacts_search_list;//搜索的结果列表
    private TextView tv_contants_nodata;//没有数据时显示内容
    private TextView tv_search_contants_cancle;//取消按钮
    private TextView tv_search_title;
//    private MyExpandableListViewAdapter expandableListViewAdapter;
    private TeaContactsAdapter teaContactsAdapter;
    private List<ContactsBean> clist;//结果列表
    private AbHttpUtil abHttpUtil;
    private ContactsDao contactsDao;
    private String searcheContet = "";//搜索的关键字
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_contants_searche);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        contactsDao = new ContactsDao(this);
        iv_search_contants_return = (ImageView) findViewById(R.id.iv_search_contants_return);
        et_search_contants_content = (EditText) findViewById(R.id.et_search_contants_content);
        tv_search_title = (TextView) findViewById(R.id.tv_search_title);
        tv_search_title.setText("教工通讯录查询");
        elv_contacts_search_list = (ListView) findViewById(R.id.elv_contacts_search_list);
        tv_contants_nodata = (TextView) findViewById(R.id.tv_contants_nodata);
        tv_search_contants_cancle = (TextView) findViewById(R.id.tv_search_contants_cancle);
        clist = new ArrayList<ContactsBean>();
        getDate();
        setListener();
    }

    //关键字搜索
    private void searchr() {
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                contactsDao.startReadableDatabase();
                String py = PinYinUtils.getPinYin(searcheContet);
                //输入关键字 从缓存中查找
                String[] selectionArgs = {"%" + searcheContet + "%", "%" + py + "%",  "%" + py + "%", "%" + py + "%","%" + searcheContet + "%"};
                String sql = null;
                sql = "SJH like ? or FirstPinYin like ? or PinYinHeadChar like ? or XMPY like ? or JTDZ like ?";
                clist = contactsDao.queryList(sql, selectionArgs);
                contactsDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (clist.size() > 0) {
                    tv_contants_nodata.setVisibility(View.GONE);
                    elv_contacts_search_list.setVisibility(View.VISIBLE);
                    teaContactsAdapter = new TeaContactsAdapter(ContactsTeaSearcheActivity.this, clist);
                    elv_contacts_search_list.setAdapter(teaContactsAdapter);
                    teaContactsAdapter.notifyDataSetChanged();
                } else {
                    tv_contants_nodata.setVisibility(View.VISIBLE);
                    elv_contacts_search_list.setVisibility(View.GONE);
                }
            }
        });
        abTask.execute(abTaskItem);
    }

    private void getDate() {
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                AbDialogUtil.showProgressDialog(ContactsTeaSearcheActivity.this, 0, "正在加载");
                contactsDao.startReadableDatabase();
                clist = contactsDao.queryList();
                contactsDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (clist.size() > 0) {
                    Collections.sort(clist, new PinyinComparator());
                    tv_contants_nodata.setVisibility(View.GONE);
                    elv_contacts_search_list.setVisibility(View.VISIBLE);
                    teaContactsAdapter = new TeaContactsAdapter(ContactsTeaSearcheActivity.this, clist);
                    elv_contacts_search_list.setAdapter(teaContactsAdapter);
                    teaContactsAdapter.notifyDataSetChanged();
                } else {
                    tv_contants_nodata.setVisibility(View.VISIBLE);
                    elv_contacts_search_list.setVisibility(View.GONE);
                }
                AbDialogUtil.removeDialog(ContactsTeaSearcheActivity.this);
            }
        });
        abTask.execute(abTaskItem);
    }

    @Override
    protected void setListener() {
        super.setListener();
        elv_contacts_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final MenuPopupWindow menuWindow = new MenuPopupWindow(ContactsTeaSearcheActivity.this);
                menuWindow.showAtLocation(ContactsTeaSearcheActivity.this.findViewById(R.id.ll_pop),Gravity.BOTTOM , 0, 0);
                menuWindow.setCallOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(clist.get(position).getSJH())){
                            final List<CommonBean> plist = new ArrayList<CommonBean>();
                            plist.add(new CommonBean("0", clist.get(position).getJTDH()));
                            if(!TextUtils.isEmpty(clist.get(position).getSJH())){
                                plist.add(new CommonBean("1",clist.get(position).getSJH()));
                            }
                            final CustomDialogListview dialog_sex = new CustomDialogListview(ContactsTeaSearcheActivity.this, "选择要拨打的电话", plist,"");
                            dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CallUtil.CallPhone(ContactsTeaSearcheActivity.this, plist.get(position).getDes());
                                    dialog_sex.dismiss();
                                }
                            });
                            dialog_sex.show();
//                            CallUtil.CallPhone(TeaContactsListActivity.this,colplist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(ContactsTeaSearcheActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setCopyOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        // 得到剪贴板管理器
                        if(!TextUtils.isEmpty(clist.get(position).getSJH())) {
                            CallUtil.copyPhone(ContactsTeaSearcheActivity.this, clist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(ContactsTeaSearcheActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setsaveOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(clist.get(position).getSJH())) {
                            CallUtil.toContacts(ContactsTeaSearcheActivity.this, clist.get(position).getXM(), "", clist.get(position).getSJH());
                        }else{
                            ToastManager.getInstance().showToast(ContactsTeaSearcheActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setMessageOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        if(!TextUtils.isEmpty(clist.get(position).getSJH())) {
                            CallUtil.sendMessage(ContactsTeaSearcheActivity.this, clist.get(position).getSJH(), "");
                        }else{
                            ToastManager.getInstance().showToast(ContactsTeaSearcheActivity.this,"暂无电话号码");
                        }
                    }
                });
                menuWindow.setDetailOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        Intent intent = new Intent(ContactsTeaSearcheActivity.this, ContactsTeaDetailActivity.class);
                        intent.putExtra("teaNo", clist.get(position).getXH());
                        startActivity(intent);
                    }
                });
            }
        });
        //返回监听
        iv_search_contants_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //取消按钮
        tv_search_contants_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searcheContet = "";
                et_search_contants_content.setText("");
                searchr();
            }
        });
        //搜索框输入监听事件
        et_search_contants_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searcheContet = s.toString();
                searchr();
            }
        });
    }
}
