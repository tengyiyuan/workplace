package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.PhoneDetailListAdapter;
import com.toplion.cplusschool.Bean.DepPhoneListBean;
import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.DepartmentPhonesDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangshengbo
 * on 2016/7/11.
 * @des 电话模糊搜索
 */
public class SearchePhoneActivity extends BaseActivity {
    private ImageView iv_search_return;//返回键
    private EditText et_search_content;//搜索的内容
    private ListView lv_search_list;//搜索的结果列表
    private TextView tv_phone_nodata;//没有数据时显示内容
    private PhoneDetailListAdapter phoneDetailListAdapter;//
    private List<DepartmentBean> plist;//结果列表
    private AbHttpUtil abHttpUtil;
    private DepartmentPhonesDao dPhoneDao;
    private String searcheContet = "";//搜索的关键字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searche_phone);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        dPhoneDao = new DepartmentPhonesDao(this);
        iv_search_return = (ImageView) findViewById(R.id.iv_search_return);
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        lv_search_list = (ListView) findViewById(R.id.lv_search_list);
        tv_phone_nodata = (TextView) findViewById(R.id.tv_phone_nodata);
        plist = new ArrayList<DepartmentBean>();
        phoneDetailListAdapter = new PhoneDetailListAdapter(SearchePhoneActivity.this, plist,Constants.PHONECODE);
        lv_search_list.setAdapter(phoneDetailListAdapter);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                //首先加载缓存
                dPhoneDao.startReadableDatabase();
                plist = dPhoneDao.queryList();
                dPhoneDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (plist.size() > 0) {
                    phoneDetailListAdapter.setMlist(plist);
                    phoneDetailListAdapter.notifyDataSetChanged();
                } else {
                    //如果缓存不存在加载网络数据
                    getPhones();
                }
            }
        });
        abTask.execute(abTaskItem);
    }

    //关键字搜索
    private void searchr() {
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                dPhoneDao.startReadableDatabase();
                String fStr = PinYinUtils.getPingYin(searcheContet);
                //输入关键字 从缓存中查找
                String[] selectionArgs = {"%" + searcheContet + "%", "%" + searcheContet + "%","%" + searcheContet + "%","%" + fStr + "%"};
                plist = dPhoneDao.queryList("DP_PHONE like ? or DD_NAME like ? or DD_NAMEPY like ? or FATHER_NAMEPY like ?", selectionArgs);
                dPhoneDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (plist.size() > 0) {
                    tv_phone_nodata.setVisibility(View.GONE);
                    lv_search_list.setVisibility(View.VISIBLE);
                    phoneDetailListAdapter.setMlist(plist);
                    phoneDetailListAdapter.notifyDataSetChanged();
                } else {
                    tv_phone_nodata.setVisibility(View.VISIBLE);
                    lv_search_list.setVisibility(View.GONE);
                }
            }
        });
        abTask.execute(abTaskItem);
    }

    //根据角色查询所有电话
    private void getPhones() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showPhoneInfoByRole") + Constants.BASEPARAMS
                + "&sysRole=2";
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "showPhoneInfoByRole") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        DepartmentBean dbean = new DepartmentBean();
                        dbean.setDD_ID(Function.getInstance().getInteger(jsonObject,"DD_ID"));
                        dbean.setT_COUNT(Function.getInstance().getInteger(jsonObject,"T_COUNT"));
                        dbean.setDD_NAME(Function.getInstance().getString(jsonObject,"DD_NAME"));
                        dbean.setDD_NAMEPY(PinYinUtils.getPinYin(Function.getInstance().getString(jsonObject,"DD_NAME").trim()));
                        dbean.setDP_ID(Function.getInstance().getInteger(jsonObject,"DP_ID"));
                        dbean.setDP_PHONE(Function.getInstance().getString(jsonObject,"DP_PHONE"));
                        dbean.setFATHER_NAME(Function.getInstance().getString(jsonObject,"FATHER_NAME"));
                        dbean.setFATHER_NAMEPY(PinYinUtils.getPinYin(Function.getInstance().getString(jsonObject,"FATHER_NAME").trim()));
                        plist.add(dbean);
                    }
                    phoneDetailListAdapter.setMlist(plist);
                    phoneDetailListAdapter.notifyDataSetChanged();
                    dPhoneDao.startWritableDatabase(true);
                    dPhoneDao.deleteAll();
                    dPhoneDao.insertList(plist);
                    dPhoneDao.closeDatabase();
                } catch (JSONException e) {
                    e.printStackTrace();
                    tv_phone_nodata.setVisibility(View.VISIBLE);
                    lv_search_list.setVisibility(View.GONE);
                    if (plist != null) plist.clear();
                }
//                DepPhoneListBean depPhoneListBean = AbJsonUtil.fromJson(result, DepPhoneListBean.class);
//                if (depPhoneListBean != null) {
//                    if (depPhoneListBean.getData() != null && depPhoneListBean.getData().size() > 0) {
//                        plist = depPhoneListBean.getData();
//                        phoneDetailListAdapter.setMlist(plist);
//                        phoneDetailListAdapter.notifyDataSetChanged();
//                        dPhoneDao.startWritableDatabase(true);
//                        dPhoneDao.deleteAll();
//                        dPhoneDao.insertList(plist);
//                        dPhoneDao.closeDatabase();
//                    } else {
//                        tv_phone_nodata.setVisibility(View.VISIBLE);
//                        lv_search_list.setVisibility(View.GONE);
//                        if (plist != null) plist.clear();
//                    }
//                } else {
//                    if (plist != null) plist.clear();
//                    tv_phone_nodata.setVisibility(View.VISIBLE);
//                    lv_search_list.setVisibility(View.GONE);
//                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                tv_phone_nodata.setVisibility(View.VISIBLE);
                lv_search_list.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchePhoneActivity.this,PhoneDetailActivity.class);
                intent.putExtra("pname",plist.get(position).getFATHER_NAME()+"");
                intent.putExtra("jname",plist.get(position).getDD_NAME());
                intent.putExtra("phoneNum",plist.get(position).getDP_PHONE());
                startActivity(intent);
            }
        });
        //返回监听
        iv_search_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索框输入监听事件
        et_search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searcheContet = s.toString().trim();
//                if (!TextUtils.isEmpty(searcheContet)) {
                    searchr();
//                } else {
//                    if(plist!=null&&plist.size()>0){
//                        plist.clear();
//                    }
//                    phoneDetailListAdapter.setMlist(plist);
//                    phoneDetailListAdapter.notifyDataSetChanged();
//                }
            }
        });
    }
}
