package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangshengob
 * on 2016/7/11.
 *
 * @des 部门下的电话详情页
 */
public class PhoneDetailActivity extends BaseActivity {
    private ImageView iv_phone_detail_return;
    private TextView tv_phone;//电话
    private TextView tv_phone_job;//职位
    private TextView tv_phone_address;//部门
    private ImageView iv_phone_c_logo;//学校图标
    private RelativeLayout rl_call;//打电话
    private RelativeLayout rl_phone_copy;//复制电话号码
    private RelativeLayout rl_phone_save;//保存到本地
    private String pName = null;//部门名称
    private String jname = null;//职位
    private String phoneNumber = null;//电话号码
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_detail);
        init();
    }

    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        pName = getIntent().getStringExtra("pname");
        phoneNumber = getIntent().getStringExtra("phoneNum");
        jname = getIntent().getStringExtra("jname");
        iv_phone_c_logo = (ImageView) findViewById(R.id.iv_phone_c_logo);
        iv_phone_detail_return = (ImageView) findViewById(R.id.iv_phone_detail_return);
        tv_phone_job = (TextView) findViewById(R.id.tv_phone_job);

        tv_phone_address = (TextView) findViewById(R.id.tv_phone_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        try {
            JSONObject jsonObject = new JSONObject(share.getString("schoolInfo",""));
            if(jsonObject!=null){
                String url = jsonObject.getString("SDS_LOGO");
                AbImageLoader.getInstance(this).display(iv_phone_c_logo,url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_phone_job.setText(jname);
        tv_phone_address.setText(pName);
        tv_phone.setText(phoneNumber);
        rl_call = (RelativeLayout) findViewById(R.id.rl_call);
        rl_phone_copy = (RelativeLayout) findViewById(R.id.rl_phone_copy);
        rl_phone_save = (RelativeLayout) findViewById(R.id.rl_phone_save);
        setListener();
        getData();
    }

    //获取数据
    @Override
    protected void getData() {
        super.getData();
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
            }

            @Override
            public void update() {
                super.update();
            }
        });
        abTask.execute(abTaskItem);
    }


    //点击事件
    @Override
    protected void setListener() {
        super.setListener();
        iv_phone_detail_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //打电话
        rl_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.CallPhone(PhoneDetailActivity.this, phoneNumber);
            }
        });
        //复制
        rl_phone_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.copyPhone(PhoneDetailActivity.this,tv_phone.getText().toString());
            }
        });
        //保存电话号码到电话本
        rl_phone_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.toContacts(PhoneDetailActivity.this,jname,pName,tv_phone.getText().toString());
            }
        });
    }
}
