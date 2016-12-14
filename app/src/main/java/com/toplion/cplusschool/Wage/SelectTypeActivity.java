package com.toplion.cplusschool.Wage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

/**
 * Created by wang
 * on 2016/8/11.
 * @工资  公积金查询选择界面
 */
public class SelectTypeActivity extends BaseActivity{
    private ImageView iv_select_back;
    private TextView tv_select_replace;//切换账号
    private TextView tv_select_xm;//姓名
    private TextView tv_select_zc;//职称
    private TextView tv_select_bm;//校办
    private TextView tv_select_zh;//账号
    private TextView tv_select_gjj;//公积金
    private TextView tv_select_gz;//工资
    private SharePreferenceUtils share;
    private AbHttpUtil abHttpUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectgzgjj);
        init();
    }


    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_select_back = (ImageView) findViewById(R.id.iv_select_back);
        tv_select_replace = (TextView) findViewById(R.id.tv_select_replace);
        tv_select_xm = (TextView) findViewById(R.id.tv_select_xm);
        tv_select_zc = (TextView) findViewById(R.id.tv_select_zc);
        tv_select_bm = (TextView) findViewById(R.id.tv_select_bm);
        tv_select_zh = (TextView) findViewById(R.id.tv_select_zh);
        tv_select_gjj = (TextView) findViewById(R.id.tv_select_gjj);
        tv_select_gz = (TextView) findViewById(R.id.tv_select_gz);
        setListener();
        getData();
    }

    @Override
    protected void getData() {
        super.getData();
        String wAccount = share.getString("wAccount","");
        String wPwd = share.getString("wPwd","");
        String wName = share.getString("wName","");
        String wJob = share.getString("wJob","");
        String wDepart = share.getString("wDepart","");
        if(!TextUtils.isEmpty(wAccount)&&!TextUtils.isEmpty(wPwd)){
            tv_select_xm.setText(wName);
            tv_select_zc.setText(wJob);
            tv_select_bm.setText(wDepart);
            tv_select_zh.setText(wAccount);
        }else{
            Intent intent = new Intent(SelectTypeActivity.this,Bound.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void setListener() {
        super.setListener();
        //返回
        iv_select_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //切换账号
        tv_select_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share.put("wAccount","");
                share.put("wPwd","");
                Intent intent = new Intent(SelectTypeActivity.this,Bound.class);
                startActivity(intent);
                finish();
            }
        });
        //公积金
        tv_select_gjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTypeActivity.this,GongActivity.class);
                startActivity(intent);
            }
        });
        //工资查询
        tv_select_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTypeActivity.this,WagesDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
