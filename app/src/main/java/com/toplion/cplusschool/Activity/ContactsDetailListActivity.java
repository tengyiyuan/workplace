package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.MyExpandableListViewAdapter;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.ContactsListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.PinYinUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengob
 * on 2016/8/15.
 *
 * @des 班级通讯录列表
 */
public class ContactsDetailListActivity extends BaseActivity {
    private ImageView iv_contacts_detail_return;//返回键
    private TextView tv_contacts_detail_title;//标题
    private RelativeLayout rl_nodata;//没有数据
    private ExpandableListView elv_contacts_detail_list;
    private List<ContactsBean> clist;//
    private MyExpandableListViewAdapter expandableListViewAdapter;
    private AbHttpUtil abHttpUtil;
    private String bId;//班级id
    private String bName;//班级名
    private SharePreferenceUtils share;
    private EditText tv_phone_detail_search;
    private int role = 0;
    private RelativeLayout myserch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        myserch=(RelativeLayout)findViewById(R.id.myserch);
        role = share.getInt("ROLE_TYPE", 0);
        tv_phone_detail_search = (EditText) findViewById(R.id.tv_phone_detail_search);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        iv_contacts_detail_return = (ImageView) findViewById(R.id.iv_contacts_detail_return);
        tv_contacts_detail_title = (TextView) findViewById(R.id.tv_contacts_detail_title);
        elv_contacts_detail_list = (ExpandableListView) findViewById(R.id.elv_contacts_detail_list);
        if (role == 1) {
            bId = getIntent().getStringExtra("bId");
            bName = getIntent().getStringExtra("bName");
            tv_contacts_detail_title.setText(bName);
            myserch.setVisibility(View.GONE);
        } else {
            tv_contacts_detail_title.setText("班级通讯录");
            myserch.setVisibility(View.VISIBLE);
        }
        expandableListViewAdapter = new MyExpandableListViewAdapter(this, 1);
        setListener();
        getData();
        RelativeLayout rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        tv_phone_detail_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getFilter().filter(PinYinUtils.getPingYin(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //获取通讯录信息
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL;
        if (role == 1) {
            url = url + "?rid=" + ReturnUtils.encode("getStuAdressBookByClass") + Constants.BASEPARAMS + "&classNo=" + bId;
        } else if (role == 2) {
            url = url + "?rid=" + ReturnUtils.encode("getStudentAdressBook") + Constants.BASEPARAMS + "&userid=" + share.getString("username", "");
        }
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "getStuAdress") {
            @Override
            public void Get_Result(String result) {
                ContactsListBean contactsListBean = AbJsonUtil.fromJson(result, ContactsListBean.class);
                if (contactsListBean != null && contactsListBean.getData() != null && contactsListBean.getData().size() > 0) {
                    clist = contactsListBean.getData();
                    rl_nodata.setVisibility(View.GONE);
                    elv_contacts_detail_list.setVisibility(View.VISIBLE);
                    expandableListViewAdapter.setMlist(clist);
                    elv_contacts_detail_list.setAdapter(expandableListViewAdapter);
                    expandableListViewAdapter.notifyDataSetChanged();
                } else {
                    rl_nodata.setVisibility(View.VISIBLE);
                    elv_contacts_detail_list.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        //返回键
        iv_contacts_detail_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ContactsDetailListActivity.MyFilter myFilter;

    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new ContactsDetailListActivity.MyFilter();
        }
        return myFilter;
    }

    /**
     * 过滤搜索条件
     */
    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filterResults.values = clist;
            } else {
                List<ContactsBean> mFilteredArrayList = new ArrayList<ContactsBean>();

                for (ContactsBean cbean : clist) {
                    if (constraint.length() > 0) {
                        String zimu = PinYinUtils.getPingYin(cbean.getXM());
                        String tel = "null";
                        String lxtel = "null";
                        if (cbean.getSJH() != null) {
                            tel = cbean.getSJH();
                        }
                        if (cbean.getLXDH() != null) {
                            lxtel = cbean.getLXDH();
                        }
                        if (zimu.contains(constraint) || cbean.getXH().contains(constraint) || tel.contains(constraint) || lxtel.contains(constraint)) {
                            mFilteredArrayList.add(cbean);
                        }
                    } else {
//                        if (cbean.getSJH().contains(constraint) || cbean.getFirstPinYin().contains(constraint)
//                                || cbean.getPinYinHeadChar().contains(constraint)) {
//                            mFilteredArrayList.add(cbean);
//                        }
                    }
                }
                filterResults.values = mFilteredArrayList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ContactsBean> mArrayList = (List<ContactsBean>) results.values;
            expandableListViewAdapter.updateListView(mArrayList);
        }
    }
}
