package com.toplion.cplusschool.TeacherContacts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.EmailUtil;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TelephoneUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;
import com.toplion.cplusschool.widget.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/9/8.
 *
 * @des教职工详细信息界面
 */
public class ContactsTeaDetailActivity extends BaseActivity {
    private ImageView iv_contacts_detail_return;//返回键
    private TextView tv_contacts_detail_title;//标题
    private TextView tv_contacts_job;//职位
    private TextView tv_contacts_depart;//部门
    private LinearLayout ll_tea_contacts_bg;
    private ImageView iv_contacts_logo;
    private TextView tv_contacts_qq;//qq
    private TextView tv_contacts_wx;//微信
    private TextView tv_contacts_email;//邮箱
    private TextView tv_contacts_zgphone;//办公电话
    private TextView tv_contacts_phone;//电话
    private LinearLayout ll_contacts_detail_call;//大电话
    private LinearLayout ll_contacts_detail_message;//发短信
    private LinearLayout ll_contacts_detai_copy;//复制
    private LinearLayout ll_contacts_detai_save;//保存
    private AbHttpUtil abHttpUtil;
    private String teaNo;
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_tea_detail);
        init();
    }
    //初始化布局
    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        teaNo = getIntent().getStringExtra("teaNo");
        iv_contacts_detail_return = (ImageView) findViewById(R.id.iv_contacts_detail_return);
        tv_contacts_detail_title = (TextView) findViewById(R.id.tv_contacts_detail_title);
        tv_contacts_job = (TextView) findViewById(R.id.tv_contacts_job);
        tv_contacts_qq = (TextView) findViewById(R.id.tv_contacts_qq);
        tv_contacts_wx = (TextView) findViewById(R.id.tv_contacts_wx);
        ll_tea_contacts_bg = (LinearLayout) findViewById(R.id.ll_tea_contacts_bg);
        tv_contacts_email = (TextView) findViewById(R.id.tv_contacts_email);
        tv_contacts_depart= (TextView) findViewById(R.id.tv_contacts_depart);
        iv_contacts_logo = (ImageView) findViewById(R.id.iv_contacts_logo);
        tv_contacts_zgphone = (TextView) findViewById(R.id.tv_contacts_zgphone);
        tv_contacts_phone = (TextView) findViewById(R.id.tv_contacts_phone);
        ll_contacts_detail_call = (LinearLayout) findViewById(R.id.ll_contacts_detail_call);
        ll_contacts_detail_message = (LinearLayout) findViewById(R.id.ll_contacts_detail_message);
        ll_contacts_detai_copy = (LinearLayout) findViewById(R.id.ll_contacts_detai_copy);
        ll_contacts_detai_save = (LinearLayout) findViewById(R.id.ll_contacts_detai_save);
        try {
            JSONObject jsonObject = new JSONObject(share.getString("schoolInfo",""));
            if(jsonObject!=null){
                String url = jsonObject.getString("SDS_LOGO");
                AbImageLoader.getInstance(this).display(iv_contacts_logo,url);
//                String bgUrl = jsonObject.getString("SDS_BGIMAGE");
//                abHttpUtil.get(bgUrl, new AbBinaryHttpResponseListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String content, Throwable error) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, byte[] content) {
//                        Bitmap bitmap = AbImageUtil.bytes2Bimap(content);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            ll_tea_contacts_bg.setBackground(AbImageUtil.bitmapToDrawable(bitmap));
//                        }else{
//                            ll_tea_contacts_bg.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(bitmap));
//                        }
//                    }
//                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getData();
        setListener();
    }

    //请求数据
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
                    tv_contacts_detail_title.setText(Function.getInstance().getString(jsonObject,"XM"));
                    tv_contacts_depart.setText(Function.getInstance().getString(jsonObject,"DEPARTMENTNAME"));
                    tv_contacts_job.setText(Function.getInstance().getString(jsonObject,"DNAME"));
                    tv_contacts_qq.setText((jsonObject.getLong("QQ") == 0?"无":jsonObject.getLong("QQ"))+"");
                    tv_contacts_wx.setText(Function.getInstance().getString(jsonObject,"WXH").equals("")?"无":Function.getInstance().getString(jsonObject,"WXH"));
                    tv_contacts_email.setText(Function.getInstance().getString(jsonObject,"DZXX"));
                    tv_contacts_zgphone.setText(Function.getInstance().getString(jsonObject,"GZDH"));
                    tv_contacts_phone.setText(Function.getInstance().getString(jsonObject,"SJ"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //初始化点击事件
    @Override
    protected void setListener() {
        super.setListener();
        iv_contacts_detail_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_contacts_zgphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_zgphone.getText().toString())){
                    CallUtil.CallPhone(ContactsTeaDetailActivity.this, tv_contacts_zgphone.getText().toString());
                }
            }
        });
        tv_contacts_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())){
                    CallUtil.CallPhone(ContactsTeaDetailActivity.this, tv_contacts_phone.getText().toString());
                }
            }
        });
        //发邮件
        tv_contacts_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_email.getText().toString())){
                    EmailUtil.sendEmail(ContactsTeaDetailActivity.this, tv_contacts_email.getText().toString(),"","");
                }
            }
        });
        //打电话
        ll_contacts_detail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<CommonBean> plist = new ArrayList<CommonBean>();
                plist.add(new CommonBean("0", tv_contacts_zgphone.getText().toString()));
                plist.add(new CommonBean("1", tv_contacts_phone.getText().toString()));
                final CustomDialogListview dialog_sex = new CustomDialogListview(ContactsTeaDetailActivity.this, "选择要拨打的电话", plist,"");
                CustomDialogListview.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CallUtil.CallPhone(ContactsTeaDetailActivity.this, plist.get(position).getDes());
                    }
                });
                dialog_sex.show();
            }
        });
        //发短信
        ll_contacts_detail_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.sendMessage(ContactsTeaDetailActivity.this, tv_contacts_phone.getText().toString(), "");
            }
        });
        //复制
        ll_contacts_detai_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                CallUtil.copyPhone(ContactsTeaDetailActivity.this, tv_contacts_phone.getText().toString());
            }
        });
        //保存电话
        ll_contacts_detai_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.toContacts(ContactsTeaDetailActivity.this,tv_contacts_detail_title.getText().toString(),"", tv_contacts_phone.getText().toString());
            }
        });
    }
}
