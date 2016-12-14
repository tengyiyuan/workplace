package com.toplion.cplusschool.TeacherContacts;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ImageUtil;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangshengob
 * on 2016/9/19.
 *
 * @des 教工通讯录电话详情页
 */
public class TeaContactsDetailActivity extends BaseActivity {
    private ImageView iv_phone_detail_return;
    private TextView tv_phone;//电话
    private TextView tv_phone_job;//职位
    private TextView tv_phone_address;//部门
    private ImageView iv_phone_c_logo;//学校图标
    private LinearLayout ll_tea_contacts_bg;
    private RelativeLayout rl_call;//打电话
    private RelativeLayout rl_phone_copy;//复制电话号码
    private RelativeLayout rl_phone_save;//保存到本地
    private SharePreferenceUtils share;
    private String teaNo;//职工号

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
        teaNo = getIntent().getStringExtra("teaNo");
        iv_phone_c_logo = (ImageView) findViewById(R.id.iv_phone_c_logo);
        iv_phone_detail_return = (ImageView) findViewById(R.id.iv_phone_detail_return);
        tv_phone_job = (TextView) findViewById(R.id.tv_phone_job);
        ll_tea_contacts_bg = (LinearLayout) findViewById(R.id.ll_tea_contacts_bg);
        tv_phone_address = (TextView) findViewById(R.id.tv_phone_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        try {
            JSONObject jsonObject = new JSONObject(share.getString("schoolInfo",""));
            if(jsonObject!=null){
                String url = jsonObject.getString("SDS_LOGO");
                String bgUrl = jsonObject.getString("SDS_BGIMAGE");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ll_tea_contacts_bg.setBackground(AbImageUtil.bitmapToDrawable(AbImageUtil.getBitmap(bgUrl)));
                }else{
                    ll_tea_contacts_bg.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(AbImageUtil.getBitmap(bgUrl)));
                }
                AbImageLoader.getInstance(this).display(iv_phone_c_logo,url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("showTeacherInfoByzgh")+Constants.BASEPARAMS+"&zgh="+teaNo;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"showTeacherInfoByzgh") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(jsonObject.getString("data"));
                    /**
                     * CSRQ 出生日志
                     DZZWM 职务编号
                     XM 姓名
                     GZDH 工作电话
                     DZXX 电子信箱
                     SJ 手机
                     ZGH 职工号
                     DNAME 职务名称
                     */
                    tv_phone_job.setText(Function.getInstance().getString(jsonObject,"XM"));
                    tv_phone_address.setText(Function.getInstance().getString(jsonObject,"DNAME"));
                    tv_phone.setText(Function.getInstance().getString(jsonObject,"SJ"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                CallUtil.CallPhone(TeaContactsDetailActivity.this, tv_phone.getText().toString());
            }
        });
        //复制
        rl_phone_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.copyPhone(TeaContactsDetailActivity.this,tv_phone.getText().toString());
            }
        });
        //保存电话号码到电话本
        rl_phone_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.toContacts(TeaContactsDetailActivity.this,tv_phone_job.getText().toString(),tv_phone_address.getText().toString(),tv_phone.getText().toString());
            }
        });
    }
}
