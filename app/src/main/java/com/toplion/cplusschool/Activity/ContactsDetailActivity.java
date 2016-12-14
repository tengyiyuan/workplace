package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/8/15.
 *
 * @des辅导员查看学生详细信息界面
 */
public class ContactsDetailActivity extends BaseActivity {
    private ImageView iv_contacts_detail_return;//返回键
    private TextView tv_contacts_detail_title;//标题
    private TextView tv_contacts_stunumber;//学号
    private TextView tv_contacts_college;//学院
    private TextView tv_contacts_class;//班级
    private TextView tv_contacts_grade;//年级
    private TextView tv_contacts_political;//政治面貌
    private TextView tv_contacts_address;//地址
    private TextView tv_contacts_family_phone;//紧急电话
    private TextView tv_contacts_phone;//电话
    private LinearLayout ll_contacts_detail_call;//大电话
    private LinearLayout ll_contacts_detail_message;//发短信
    private LinearLayout ll_contacts_detai_copy;//复制
    private LinearLayout ll_contacts_detai_save;//保存
    private AbHttpUtil abHttpUtil;
    private String stuNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        init();
    }
    //初始化布局
    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        stuNo = getIntent().getStringExtra("stuNo");
        iv_contacts_detail_return = (ImageView) findViewById(R.id.iv_contacts_detail_return);
        tv_contacts_detail_title = (TextView) findViewById(R.id.tv_contacts_detail_title);
        tv_contacts_stunumber = (TextView) findViewById(R.id.tv_contacts_stunumber);
        tv_contacts_college = (TextView) findViewById(R.id.tv_contacts_college);
        tv_contacts_class = (TextView) findViewById(R.id.tv_contacts_class);
        tv_contacts_grade = (TextView) findViewById(R.id.tv_contacts_grade);
        tv_contacts_political = (TextView) findViewById(R.id.tv_contacts_political);
        tv_contacts_address = (TextView) findViewById(R.id.tv_contacts_address);
        tv_contacts_family_phone = (TextView) findViewById(R.id.tv_contacts_family_phone);
        tv_contacts_phone = (TextView) findViewById(R.id.tv_contacts_phone);
        ll_contacts_detail_call = (LinearLayout) findViewById(R.id.ll_contacts_detail_call);
        ll_contacts_detail_message = (LinearLayout) findViewById(R.id.ll_contacts_detail_message);
        ll_contacts_detai_copy = (LinearLayout) findViewById(R.id.ll_contacts_detai_copy);
        ll_contacts_detai_save = (LinearLayout) findViewById(R.id.ll_contacts_detai_save);
        getData();
        setListener();
    }

    //请求数据
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("queryStudentInfo")+Constants.BASEPARAMS+"&stuNo="+stuNo;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"queryStudentInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(jsonObject.getString("data"));
                    tv_contacts_detail_title.setText(Function.getInstance().getString(jsonObject,"XM"));
                    tv_contacts_stunumber.setText(Function.getInstance().getString(jsonObject,"XH"));
                    tv_contacts_college.setText(Function.getInstance().getString(jsonObject,"YXMC"));
                    tv_contacts_class.setText(Function.getInstance().getString(jsonObject,"ZYMC"));
                    tv_contacts_grade.setText(Function.getInstance().getString(jsonObject,"NJDM"));
                    tv_contacts_political.setText(Function.getInstance().getString(jsonObject,"ZZMM"));
                    tv_contacts_address.setText(Function.getInstance().getString(jsonObject,"JTDZ"));
                    tv_contacts_family_phone.setText(Function.getInstance().getString(jsonObject,"JTDH"));
                    tv_contacts_phone.setText(Function.getInstance().getString(jsonObject,"SJH"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(ContactsDetailActivity.this,"暂无数据");
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
        tv_contacts_family_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_family_phone.getText().toString())){
                    if(StringUtils.isMobile(tv_contacts_family_phone.getText().toString())){
                        CallUtil.CallPhone(ContactsDetailActivity.this,tv_contacts_family_phone.getText().toString());
                    }else{
                        String [] phones = tv_contacts_family_phone.getText().toString().trim().replace("；",";").split(";");
                        if(phones!=null&&phones.length>0){
                            final List<CommonBean> phonelist = new ArrayList<CommonBean>();
                            for (int i=0;i<phones.length;i++){
                                if(StringUtils.isMobile(phones[i])){
                                    phonelist.add(new CommonBean(i+"",phones[i]));
                                }
                            }
                            if(phonelist!=null&&phonelist.size()>0){
                                final CustomDialogListview dialog_sex = new CustomDialogListview(ContactsDetailActivity.this, "选择要拨打的电话", phonelist,"");
                                dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(StringUtils.isMobile(phonelist.get(position).getDes())){
                                            CallUtil.CallPhone(ContactsDetailActivity.this, phonelist.get(position).getDes());
                                            dialog_sex.dismiss();
                                        }else{
                                            ToastManager.getInstance().showToast(ContactsDetailActivity.this,"电话号码不正确");
                                        }
                                    }
                                });
                                dialog_sex.show();
                            }else{
                                ToastManager.getInstance().showToast(ContactsDetailActivity.this,"电话号码不正确或没有手机号");
                            }
                        }else{
                            ToastManager.getInstance().showToast(ContactsDetailActivity.this,"电话号码不正确");
                        }
                    }
                }
            }
        });
        tv_contacts_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())){
                    if(StringUtils.isMobile(tv_contacts_phone.getText().toString())){
                        CallUtil.CallPhone(ContactsDetailActivity.this,tv_contacts_phone.getText().toString());
                    }else{
                        ToastManager.getInstance().showToast(ContactsDetailActivity.this,"电话号码不正确");
                    }
                }
            }
        });
        //打电话
        ll_contacts_detail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())){
                    final List<CommonBean> plist = new ArrayList<CommonBean>();
                    if(StringUtils.isMobile(tv_contacts_phone.getText().toString())){
                        plist.add(new CommonBean("0", tv_contacts_phone.getText().toString()));
                    }
                    if(!TextUtils.isEmpty( tv_contacts_family_phone.getText().toString())){
                        if(StringUtils.isMobile(tv_contacts_family_phone.getText().toString())){
                            plist.add(new CommonBean("1", tv_contacts_family_phone.getText().toString()));
                        }
                    }
                    final CustomDialogListview dialog_sex = new CustomDialogListview(ContactsDetailActivity.this, "选择要拨打的电话", plist,"");
                    dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(StringUtils.isMobile(plist.get(position).getDes())){
                                CallUtil.CallPhone(ContactsDetailActivity.this, plist.get(position).getDes());
                                dialog_sex.dismiss();
                            }else{
                                ToastManager.getInstance().showToast(ContactsDetailActivity.this,"电话号码不正确");
                            }
                        }
                    });
                    dialog_sex.show();
                }else{
                    ToastManager.getInstance().showToast(ContactsDetailActivity.this,"暂无电话信息");
                }
            }
        });
        //发短信
        ll_contacts_detail_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())) {
                    CallUtil.sendMessage(ContactsDetailActivity.this, tv_contacts_phone.getText().toString(), "");
                }else{
                    ToastManager.getInstance().showToast(ContactsDetailActivity.this,"暂无电话信息");
                }
            }
        });
        //复制
        ll_contacts_detai_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())) {
                    CallUtil.copyPhone(ContactsDetailActivity.this, tv_contacts_phone.getText().toString());
                }else{
                    ToastManager.getInstance().showToast(ContactsDetailActivity.this,"暂无电话信息");
                }
            }
        });
        //保存电话
        ll_contacts_detai_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_contacts_phone.getText().toString())) {
                    CallUtil.toContacts(ContactsDetailActivity.this, tv_contacts_detail_title.getText().toString(), "", tv_contacts_phone.getText().toString());
                }else{
                    ToastManager.getInstance().showToast(ContactsDetailActivity.this,"暂无电话信息");
                }
            }
        });
    }
}
