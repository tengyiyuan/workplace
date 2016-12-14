package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.PhoneDetailListAdapter;
import com.toplion.cplusschool.Bean.DepPhoneListBean;
import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengob
 * on 2016/7/11.
 * @des 部门下的电话列表界面
 */
public class PhoneDetailListActivity extends BaseActivity{
    private ImageView iv_phone_detail_return;//返回键
    private TextView tv_phone_detail_title;//标题
    private TextView tv_phone_detail_search;//搜索按钮
    private ListView lv_phone_detail_list;
    private PhoneDetailListAdapter phoneDetailListAdapter;//部门电话列表适配器
    private List<DepartmentBean> plist;//电话列表信息
    private String dname = null;//部门名称
    private String dId = null;//部门 id
    private AbHttpUtil abHttpUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_detail_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        dname = getIntent().getStringExtra("dname");
        dId = getIntent().getStringExtra("dapId");
        iv_phone_detail_return = (ImageView) findViewById(R.id.iv_phone_detail_return);
        tv_phone_detail_title = (TextView) findViewById(R.id.tv_phone_detail_title);
        tv_phone_detail_search = (TextView) findViewById(R.id.tv_phone_detail_search);
        lv_phone_detail_list = (ListView) findViewById(R.id.lv_phone_detail_list);
        tv_phone_detail_title.setText(dname);
        plist = new ArrayList<DepartmentBean>();
        phoneDetailListAdapter = new PhoneDetailListAdapter(this,plist,Constants.PHONECODE);
        lv_phone_detail_list.setAdapter(phoneDetailListAdapter);
        setListener();
        getData();

    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("showDepartmentInfoById")+Constants.BASEPARAMS
                +"&sysRole=2"+"&depId="+dId;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"showDepartmentInfoById") {
            @Override
            public void Get_Result(String result) {
                DepPhoneListBean depPhoneListBean = AbJsonUtil.fromJson(result,DepPhoneListBean.class);
                if(depPhoneListBean!=null){
                    if(depPhoneListBean.getData()!=null&&depPhoneListBean.getData().size()>0){
                        plist = depPhoneListBean.getData();
                        phoneDetailListAdapter.setMlist(plist);
                        phoneDetailListAdapter.notifyDataSetChanged();
                    }else{
                        if(plist!=null)plist.clear();
                    }

                }else{
                    if(plist!=null)plist.clear();
                }
            }

        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        //返回键
        iv_phone_detail_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索点击事件
        tv_phone_detail_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneDetailListActivity.this,SearchePhoneActivity.class));
            }
        });
        //电话列表点击事件
        lv_phone_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhoneDetailListActivity.this,PhoneDetailActivity.class);
                intent.putExtra("pname",plist.get(position).getFATHER_NAME()+"");
                intent.putExtra("jname",plist.get(position).getDD_NAME());
                intent.putExtra("phoneNum",plist.get(position).getDP_PHONE());
                startActivity(intent);
            }
        });
    }
}
