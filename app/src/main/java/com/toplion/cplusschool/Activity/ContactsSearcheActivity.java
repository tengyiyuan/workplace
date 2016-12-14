package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.MyExpandableListViewAdapter;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.ContactsListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.ContactsDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangshengbo
 * on 2016/8/15.
 *
 * @des 通讯录模糊搜索
 */
public class ContactsSearcheActivity extends BaseActivity {
    private ImageView iv_search_contants_return;//返回键
    private EditText et_search_contants_content;//搜索的内容
    private ExpandableListView elv_contacts_search_list;//搜索的结果列表
    private TextView tv_contants_nodata;//没有数据时显示内容
    private TextView tv_search_title;
    private MyExpandableListViewAdapter expandableListViewAdapter;
    private List<ContactsBean> plist;//结果列表
    private AbHttpUtil abHttpUtil;
    private ContactsDao contactsDao;
    private String searcheContet = "";//搜索的关键字
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contants_searche);
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
        tv_search_title.setText("学生通讯录查询");
        elv_contacts_search_list = (ExpandableListView) findViewById(R.id.elv_contacts_search_list);
        tv_contants_nodata = (TextView) findViewById(R.id.tv_contants_nodata);
        plist = new ArrayList<ContactsBean>();
        expandableListViewAdapter = new MyExpandableListViewAdapter(this, 1);
        expandableListViewAdapter.setMlist(plist);
        elv_contacts_search_list.setAdapter(expandableListViewAdapter);
        getPhones();
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
                String[] selectionArgs = {"%" + searcheContet + "%", "%" + py + "%","%" + py + "%", "%" + searcheContet + "%"};
                String sql = null;
                sql = "SJH like ? or XMPY like ? or PinYinHeadChar like ? or XH like ?";
                plist = contactsDao.queryList(sql, selectionArgs);
                contactsDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (plist.size() > 0) {
                    tv_contants_nodata.setVisibility(View.GONE);
                    elv_contacts_search_list.setVisibility(View.VISIBLE);
                    expandableListViewAdapter.setMlist(plist);
                    expandableListViewAdapter.notifyDataSetChanged();
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
                AbDialogUtil.showProgressDialog(ContactsSearcheActivity.this, 0, "正在加载");
                contactsDao.startReadableDatabase();
                plist = contactsDao.queryList();
                contactsDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (plist.size() > 0) {
                    tv_contants_nodata.setVisibility(View.GONE);
                    elv_contacts_search_list.setVisibility(View.VISIBLE);
                    expandableListViewAdapter.setMlist(plist);
                    expandableListViewAdapter.notifyDataSetChanged();
                } else {
                    tv_contants_nodata.setVisibility(View.VISIBLE);
                    elv_contacts_search_list.setVisibility(View.GONE);
                }
                AbDialogUtil.removeDialog(ContactsSearcheActivity.this);
            }
        });
        abTask.execute(abTaskItem);
    }

    //根据角色查询所有电话
    private void getPhones() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getStudentAdressBook") + Constants.BASEPARAMS + "&userid="
                + share.getString("ROLE_ID", "0");
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "getStudentAdressBook") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(json.getString("data"));
                    plist = new ArrayList<ContactsBean>();
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ContactsBean cbean = new ContactsBean();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            cbean.setXH(Function.getInstance().getString(jsonObject, "XH"));
                            cbean.setXM(Function.getInstance().getString(jsonObject, "XM"));
                            cbean.setXMPY(PinYinUtils.getPinYin(jsonObject.getString("XM").trim()));
                            cbean.setLXDH(Function.getInstance().getString(jsonObject, "LXDH"));
                            cbean.setSJH(Function.getInstance().getString(jsonObject, "SJH"));
                            cbean.setJTDH(Function.getInstance().getString(jsonObject, "JTDH"));
                            cbean.setJTDZ(Function.getInstance().getString(jsonObject, "JTDZ"));
                            String hearPinYin = PinYinUtils.getPinYinHeadChar(Function.getInstance().getString(jsonObject,"XM"));
                            cbean.setPinYinHeadChar(hearPinYin);
                            plist.add(cbean);
                        }
                        expandableListViewAdapter.setMlist(plist);
                        expandableListViewAdapter.notifyDataSetChanged();
                        contactsDao.startWritableDatabase(true);
                        contactsDao.deleteAll();
                        contactsDao.insertList(plist);
                        contactsDao.closeDatabase();
                    } else {
                        tv_contants_nodata.setVisibility(View.VISIBLE);
                        elv_contacts_search_list.setVisibility(View.GONE);
                        if (plist != null) plist.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tv_contants_nodata.setVisibility(View.VISIBLE);
                    elv_contacts_search_list.setVisibility(View.GONE);
                    if (plist != null) plist.clear();
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                tv_contants_nodata.setVisibility(View.VISIBLE);
                elv_contacts_search_list.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void setListener() {
        super.setListener();
        //返回监听
        iv_search_contants_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
