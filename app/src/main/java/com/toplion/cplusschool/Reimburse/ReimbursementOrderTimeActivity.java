package com.toplion.cplusschool.Reimburse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDateUtil;
import com.ab.view.sliding.AbSlidingButton;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CalendarUtils;
import com.toplion.cplusschool.Utils.EportalUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/8/3.
 *
 * @des报销预约时间
 */
public class ReimbursementOrderTimeActivity extends BaseActivity {
    private ImageView iv_reimburse_order_time_back;//返回键
    private TextView tv_reimburse_change_time;//换个时间
    private TextView tv_order_time;//时间段
    private TextView tv_order_week;//星期
    private TextView tv_chuangkou;//窗口
    private LinearLayout ll_lxfs;//联系方式
    private View v_lxfs_line;//线
    private EditText et_phone;//联系方式
    private AbSlidingButton btn_closeoropen;//开关按钮
    private RelativeLayout rl_daiban;//确定预约
    private EditText et_dbrxm;//代办人姓名
    private EditText et_dbrdh;//代办人电话
    private Button btn_confirm_order;//确定预约
    private int REQUESTCODE = 100;
    private String riid = null;//报销单号
    private AbHttpUtil abHttpUtil;
    private List<StandardInfo> standardInfo;
    private SharePreferenceUtils share;
    private int totalTime = 0;//时间总和
    private String datetime = null;
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reimbursement_order_time);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        standardInfo = new ArrayList<StandardInfo>();
        standardInfo = (List<StandardInfo>) getIntent().getSerializableExtra("infolist");
        totalTime = getIntent().getIntExtra("maxtime", 0);
        iv_reimburse_order_time_back = (ImageView) findViewById(R.id.iv_reimburse_order_time_back);
        tv_reimburse_change_time = (TextView) findViewById(R.id.tv_reimburse_change_time);
        ll_lxfs = (LinearLayout) findViewById(R.id.ll_lxfs);
        v_lxfs_line = findViewById(R.id.v_lxfs_line);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_order_week = (TextView) findViewById(R.id.tv_order_week);
        tv_chuangkou = (TextView) findViewById(R.id.tv_chuangkou);
        btn_closeoropen = (AbSlidingButton) findViewById(R.id.btn_closeoropen);
        rl_daiban = (RelativeLayout) findViewById(R.id.rl_daiban);
        et_dbrxm = (EditText) findViewById(R.id.et_dbrxm);
        et_dbrdh = (EditText) findViewById(R.id.et_dbrdh);
        btn_closeoropen.setImageResource(R.mipmap.btn_bottom, R.mipmap.btn_frame, R.mipmap.btn_mask, R.mipmap.btn_unpressed, R.mipmap.btn_pressed);
        btn_confirm_order = (Button) findViewById(R.id.btn_confirm_order);
        startBook();
        setListener();
    }

    //开始预约
    private void startBook() {
        StringBuffer standardInfoStr = new StringBuffer();
        for (int i = 0; i < standardInfo.size(); i++) {
            standardInfoStr.append(standardInfo.get(i).getRTID() + "," + standardInfo.get(i).getRRNUMBER());
            standardInfoStr.append(";");
        }
        standardInfoStr.deleteCharAt(standardInfoStr.length() - 1);
        Log.e("standardInfoStr", standardInfoStr.toString());
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("saveAccount") + Constants.BASEPARAMS;
        AbRequestParams requestParams = new AbRequestParams();
        requestParams.put("userid", share.getString("username", ""));//预约人账号
        requestParams.put("totaltime", totalTime);//所需总时间和
        requestParams.put("standardInfo", standardInfoStr.toString());//类型信息格式要求:类型ID,单据数量（中间用逗号分开）多个按；分开，删除最后一条数据的;
        abHttpUtil.post(url, requestParams, new CallBackParent(this, getResources().getString(R.string.loading), "saveAccount") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (jsonArray != null && jsonArray.length() > 0) {
                        jsonObject = jsonArray.getJSONObject(0);
                        riid = jsonObject.getString("RIID");
                        tv_chuangkou.setText(jsonObject.getString("WIID") + "号窗口");
                        datetime = jsonObject.getString("RISTARTTIME");
                        String time = TimeUtils.timeStampToDate(jsonObject.getString("RISTARTTIME"), "yyyy-MM-dd");
                        tv_order_week.setText(time + " " + AbDateUtil.getWeekNumber(time, "yyyy-MM-dd"));
                        tv_order_time.setText(TimeUtils.timeStampToDate(jsonObject.getString("RISTARTTIME") + "", "HH:mm"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        //返回键
        iv_reimburse_order_time_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(et_phone);
                finish();
            }
        });
        //换个时间
        tv_reimburse_change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ReimbursementOrderTimeActivity.this,BaoTimeActivity.class);
                Intent intent = new Intent(ReimbursementOrderTimeActivity.this, SelectTimeActivity.class);
                intent.putExtra("riid", riid);
                intent.putExtra("totaltime", totalTime);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
        //是否显示填写代办人信息
        btn_closeoropen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpen = isChecked;
                if (isChecked) {
                    rl_daiban.setVisibility(View.VISIBLE);
                    ll_lxfs.setVisibility(View.GONE);
                    v_lxfs_line.setVisibility(View.GONE);
                } else {
                    rl_daiban.setVisibility(View.GONE);
                    ll_lxfs.setVisibility(View.VISIBLE);
                    v_lxfs_line.setVisibility(View.VISIBLE);
                }
            }
        });
        //确定预约
        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    if (!TextUtils.isEmpty(et_dbrdh.getText().toString().trim()) && !TextUtils.isEmpty(et_dbrxm.getText().toString().trim())) {
                        if (StringUtils.isMobile(et_dbrdh.getText().toString())) {
                            confirmBook();
                        } else {
                            ToastManager.getInstance().showToast(ReimbursementOrderTimeActivity.this, "手机号格式不正确");
                        }
                    } else {
                        ToastManager.getInstance().showToast(ReimbursementOrderTimeActivity.this, "代办人姓名或电话不能为空");
                    }
                } else {
                    if (!TextUtils.isEmpty(et_phone.getText().toString())) {
                        if (StringUtils.isMobile(et_phone.getText().toString())) {
                            confirmBook();
                        } else {
                            ToastManager.getInstance().showToast(ReimbursementOrderTimeActivity.this, "手机号格式不正确");
                        }
                    } else {
                        ToastManager.getInstance().showToast(ReimbursementOrderTimeActivity.this, "联系方式不能为空");
                    }
                }
            }
        });
    }

    //确定预约
    private void confirmBook() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updBookInfoWithAccountAndtime") + Constants.BASEPARAMS;
        AbRequestParams requestParams = new AbRequestParams();
        requestParams.put("bookid", riid);
        requestParams.put("starttime", datetime);
        requestParams.put("senseOptusr", et_dbrxm.getText().toString());
        requestParams.put("senseOptphone", et_dbrdh.getText().toString());
        Log.e("requestParams", requestParams + "");
        Log.e("datetime", datetime);
        abHttpUtil.post(url, requestParams, new CallBackParent(this, getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    json = new JSONObject(json.getString("data"));
                    final String id = json.getString("RIID");
                    final String evenId = json.getString("RISTATUS");
                    final String time = TimeUtils.timeStampToDate(json.getString("RISTARTTIME"),"yyyy-MM-dd HH:mm");
                    String week = AbDateUtil.getWeekNumber(time,"yyyy-MM-dd HH:mm");
                    final String wiid = json.getString("WIID")+"号窗口";
                    final String content = "您有预约报销事件,预约时间为"+time+" "+week+",预约窗口号是"+wiid+",预约单号是"+id+",请您提前准备好资料,不要迟到哦.";
                    final CommDialog dia = new CommDialog(ReimbursementOrderTimeActivity.this);
                    dia.CreateDialogOnlyOkForUp("系统提示", "确定", "预约成功!", new CommDialog.CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            dia.cancelDialog();
                            showAddCalendar(id,evenId,time,content,wiid);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加入闹钟提醒
    private void showAddCalendar(final String id,final String eId, final String datetime,final String content, final String wiid) {
        final CustomDialog dialog = new CustomDialog(ReimbursementOrderTimeActivity.this);
        dialog.setlinecolor();
        dialog.setTitle("提示");
        dialog.setContentboolean(true);
        dialog.setDetial("是否加入手机日历提醒事件");
        dialog.setLeftText("确定");
        dialog.setRightText("取消");
        dialog.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                long evenId = CalendarUtils.addCalendar(ReimbursementOrderTimeActivity.this,eId ,content, datetime, wiid, content);
                String msg;
                if(evenId != -1){
                    msg = "提醒服务已成功加入手机日历";
                    share.put(id,evenId+"");
                }else{
                    msg = "提醒服务加入手机日历失败,请确认您的手机日历是否可以添加提醒事件";
                }
                final CommDialog dia = new CommDialog(ReimbursementOrderTimeActivity.this);
                dia.CreateDialogOnlyOkForUp("系统提示", "确定", msg, new CommDialog.CallBack() {
                    @Override
                    public void isConfirm(boolean flag) {
                        dialog.dismiss();
                        dia.cancelDialog();
                        Intent intent = new Intent(ReimbursementOrderTimeActivity.this, BaomainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        dialog.setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ReimbursementOrderTimeActivity.this, BaomainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE) {
                String dateTime = data.getStringExtra("datetime");
                String dateWeek = data.getStringExtra("dateweek");
                datetime = TimeUtils.date2TimeStamp(dateWeek + " " + dateTime, "yyyy-MM-dd HH:mm");
                Log.e("datetime", datetime);
                String week = AbDateUtil.getWeekNumber(dateWeek, "yyyy-MM-dd");
                tv_order_time.setText(dateTime);
                tv_order_week.setText(dateWeek + " " + week);
            }
        }
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
