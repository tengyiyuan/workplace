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
import com.toplion.cplusschool.Adapter.PhoneBooksAdapter;
import com.toplion.cplusschool.Bean.DepPhoneListBean;
import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/7/11.
 * @des 办公电话
 */
public class PhoneBooksActivity extends BaseActivity{
    private ImageView iv_phone_books_return;//返回键
    private ListView lv_departments_list;//部门列表
    private TextView tv_phone_books_search;//搜索框
    private PhoneBooksAdapter pbAdapter;//适配器
    private List<DepartmentBean> mlist;//通讯录部门列表数据
    private AbHttpUtil abHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_books);
        init();
    }

    //加载布局
    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        mlist = new ArrayList<DepartmentBean>();
        pbAdapter = new PhoneBooksAdapter(this,mlist);
        iv_phone_books_return = (ImageView) findViewById(R.id.iv_phone_books_return);
        lv_departments_list = (ListView) findViewById(R.id.lv_departments_list);
        tv_phone_books_search = (TextView) findViewById(R.id.tv_phone_books_search);
        lv_departments_list.setAdapter(pbAdapter);
        getData();
        setListener();

    }

    //获取部门列表信息
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("showDepartmentsInfo")+Constants.BASEPARAMS;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"showDepartmentsInfo") {
            @Override
            public void Get_Result(String result) {
                DepPhoneListBean depPhoneListBean = AbJsonUtil.fromJson(result,DepPhoneListBean.class);
                if(depPhoneListBean!=null){
                    if(depPhoneListBean.getData()!=null&&depPhoneListBean.getData().size()>0){
                        mlist = depPhoneListBean.getData();
                        pbAdapter.setMlist(mlist);
                        pbAdapter.notifyDataSetChanged();
                    }else{
                        if(mlist!=null)mlist.clear();
                    }
                }else{
                    if(mlist!=null)mlist.clear();
                }
            }
        });
    }
    @Override
    protected void setListener() {
        super.setListener();
        iv_phone_books_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索框点击事件
        tv_phone_books_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneBooksActivity.this,SearchePhoneActivity.class));
            }
        });
        //列表点击事件
        lv_departments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhoneBooksActivity.this,PhoneDetailListActivity.class);
                intent.putExtra("dapId",mlist.get(position).getDD_ID()+"");
                intent.putExtra("dname",mlist.get(position).getDD_NAME()+"");//部门名称
                startActivity(intent);
            }
        });
    }
}
