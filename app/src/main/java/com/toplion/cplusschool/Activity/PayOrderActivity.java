package com.toplion.cplusschool.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toplion.cplusschool.Bean.BanWidthBean;
import com.toplion.cplusschool.Bean.WxBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Fragment.PlayGroundFragment;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.alipay.Result;
import com.toplion.cplusschool.dao.CallBackParent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wang
 * on 2016/8/24.
 *
 * @支付界面
 */
public class PayOrderActivity extends BaseActivity {
    private ImageView iv_pay_return;
    private TextView tv_pay_banwidth;//宽带套餐
    private LinearLayout ll_pay_time;//
    private TextView tv_pay_time;//时长
    private TextView tv_pay_explanation;//优惠说明
    private TextView tv_meal_tishi;//温馨提示
    private TextView tv_pay_price;//标准价格
    private TextView tv_pay_remark;//备注
    private RelativeLayout ll_pay_tuifei;//退费是否显示
    private View v_pay_explanation;//优惠说明横线
    private LinearLayout ll_pay_explanation;//优惠说明
    private TextView tv_order_price;
    private CheckBox meal_ck_cb;       // 选中
    private TextView meal_agree;        // 购买协议
    private boolean isCheck = true;    // 是否选中

    private LinearLayout ll_pay_confirm;//确认支付
    private View v_meal_tishi;//提示横线
    private RelativeLayout rl_pay_zfb;//支付宝
    private RelativeLayout rl_pay_wx;//微信
    private CheckBox rbt_pay_zfb_dh;//选择支付宝
    private CheckBox rbt_pay_wx_dh;//选择微信
    private AbHttpUtil abHttpUtil;
    private BanWidthBean banWidthBean;//套餐bean
    private int payType = 0;//支付方式 默认 0:支付宝  1:微信
    private PayReq req;
    private IWXAPI msgApi;
    private SharePreferenceUtils share;
    private String tlength;//时长
    private String pkgName;                 // 套餐
    private String orderInfo;                // 订单
    public static final String TAG = "alipay-sdk";
    private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;
    private String orderNum = "";           // 订单编号
    private String orderPayString = null;   //订单支付字符串
    private String canPay = "";
    private String messageStr = null;
    private String tankuangtishi = "";

    private AlertDialog.Builder builder;// Builder
    private AlertDialog dialog;         // dialog
    private DownTask downTask;          // DOWNTASK
    private ImageView imageView;        // 显示图片
    private Button signin_validate_image_see;// 看不清
    private AnimationDrawable animationDrawable; // 等待Loading
    private com.toplion.cplusschool.View.ChangeIconEditText signin_validate_input;      // 输入的验证码
    private JSONObject obj;            // 验证码返回值
    private Button dialogBtn;          // 自定义Dialog的Button

    Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Result result = new Result((String) msg.obj);
            switch (msg.what) {
                case RQF_PAY:
                case RQF_LOGIN: {
                    String trip = "";
                    if (result != null) {
                        if (TextUtils.isEmpty(result.getResult())) {
                            trip = result.getStatus();
                        } else {
                            trip = result.getResult();
                        }
                    } else {
                        trip = "2000";
                    }
                    final CommDialog dialog = new CommDialog(PayOrderActivity.this);
                    if (trip.equals("9000")) {
                        dialog.CreateDialogOnlyOk(Constants.ALIPAY_SUC, "确定", Constants.ALIPAY_SUCCESS, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                // 支付成功后跳转到订单详情界面
                                Intent intent = new Intent(PayOrderActivity.this, MyOrderDetailActivity.class);
                                // 订单编号
                                String no = "out_trade_no=\"";
                                orderInfo = orderInfo.substring(orderInfo.indexOf(no) + no.length());
                                String orderId = orderInfo.substring(0, orderInfo.indexOf("\""));
                                intent.putExtra("orderNum", orderId);
                                intent.putExtra("state", "交易成功");
                                startActivityForResult(intent, 200);
                               // PlayGroundFragment.getPlay().initInfo();
                                dialog.cancelDialog();
                            }
                        });
                    } else {
                        dialog.CreateDialogOnlyOk(Constants.ALIPAY_ERR, "返回", Constants.ALIPAY_ERROR, new CommDialog.CallBack() {

                            @Override
                            public void isConfirm(boolean flag) {
                                // 支付失败后页面不跳转
                                String no = "out_trade_no=\"";
//                                orderInfo = orderInfo.substring(orderInfo.indexOf(no) + no.length());
                                orderInfo = "";
                                dialog.cancelDialog();
                            }
                        });
                    }
                }
                break;
                case 3:
                    try {
                        obj = new JSONObject(obj.getString("result"));
                        String code = obj.getString("code");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            dialogBtn.setEnabled(true);
                            dialogBtn.setBackgroundResource(R.mipmap.btn_orange);
                            signin_validate_input.setClearIconVisible(true, true);
//                            signin_validate_input.setFocusable(false);
                            signin_validate_input.setEnabled(false);
                            dialogBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    if (payType == 0) {//支付宝
                                        payZFB();
                                    } else if (payType == 1) {//微信支付
                                        payWX();
                                    }
                                }
                            });
                        } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                            CommonUtil.intoLogin(PayOrderActivity.this, share, obj.getString("msg"));
                        } else {
                            //Toasts 提示
                            ToastManager.getInstance().showToast(PayOrderActivity.this,obj.getString("msg"));
                            //刷新验证码
                            signin_validate_image_see.performClick();
                            //原输入验证码清空
                            signin_validate_input.setClearIconVisible(true, false);
                            signin_validate_input.setText("");
                            dialogBtn.setEnabled(false);
                            dialogBtn.setBackgroundResource(R.mipmap.btn_gray);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PayOrderActivity.this, CacheConstants.CHKNUM_ERROR, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9001:
                    CommonUtil.intoLogin(PayOrderActivity.this, share, messageStr);
                    break;
                default:
                    break;
            }
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 200:
                PayOrderActivity.this.finish();
                break;

            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_order);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        init();
    }

    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        abHttpUtil = AbHttpUtil.getInstance(this);
        banWidthBean = (BanWidthBean) getIntent().getSerializableExtra("banWidthgBean");
        orderNum = getIntent().getStringExtra("orderNum");
        pkgName = getIntent().getStringExtra("pkgName");
        if (TextUtils.isEmpty(pkgName)) {
            pkgName = banWidthBean.getBanWidthWZ();
        }
        iv_pay_return = (ImageView) findViewById(R.id.iv_pay_return);
        tv_pay_banwidth = (TextView) findViewById(R.id.tv_pay_banwidth);
        ll_pay_time = (LinearLayout) findViewById(R.id.ll_pay_time);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_pay_explanation = (TextView) findViewById(R.id.tv_pay_explanation);
        tv_meal_tishi = (TextView) findViewById(R.id.tv_meal_tishi);
        tv_pay_price = (TextView) findViewById(R.id.tv_pay_price);
        tv_pay_remark = (TextView) findViewById(R.id.tv_pay_remark);
        // 购买协议
        meal_agree = (TextView) this.findViewById(R.id.meal_agree);
        String exchange = getResources().getString(R.string.register_agreement);
        meal_agree.setText(Html.fromHtml(exchange));
        meal_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 跳转到购买协议界面
                Intent intent = new Intent(PayOrderActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });
        // 是否确认购买协议
        meal_ck_cb = (CheckBox) this.findViewById(R.id.meal_ck_cb);
        meal_ck_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
                isCheck = check;
                validateInput();
            }
        });
        v_pay_explanation = findViewById(R.id.v_pay_explanation);
        ll_pay_explanation = (LinearLayout) findViewById(R.id.ll_pay_explanation);
        v_meal_tishi = findViewById(R.id.v_meal_tishi);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        ll_pay_confirm = (LinearLayout) findViewById(R.id.ll_pay_confirm);
        ll_pay_tuifei = (RelativeLayout) findViewById(R.id.ll_pay_tuifei);
        rl_pay_zfb = (RelativeLayout) findViewById(R.id.rl_pay_zfb);
        rl_pay_wx = (RelativeLayout) findViewById(R.id.rl_pay_wx);
        rbt_pay_zfb_dh = (CheckBox) findViewById(R.id.rbt_pay_zfb_dh);
        rbt_pay_wx_dh = (CheckBox) findViewById(R.id.rbt_pay_wx_dh);
        orderPayString = getIntent().getStringExtra("orderPayString");
        if (!TextUtils.isEmpty(orderPayString)) {
            if (orderPayString.contains("Sign=WXPay")) {
                rl_pay_wx.setVisibility(View.VISIBLE);
                rl_pay_zfb.setVisibility(View.GONE);
            } else {
                rl_pay_wx.setVisibility(View.GONE);
                rl_pay_zfb.setVisibility(View.VISIBLE);
            }
        }
        if (pkgName != null) {
            String[] meal = pkgName.split("\\|");
            if (meal.length >= 4) {
                tv_pay_banwidth.setText(meal[0]);
                tv_pay_price.setText(meal[3]+"元");
                tv_order_price.setText("￥" + meal[3]);
            }
        }
        setListener();
        getData();
        if (!TextUtils.isEmpty(orderNum)) {
            getOrderById(orderNum);
        } else {
            tlength = getIntent().getStringExtra("tlength");
        }
    }

    //根据id获取订单详情
    private void getOrderById(String orderId) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getOrderById") + Constants.BASEPARAMS;
        AbRequestParams requestParams = new AbRequestParams();
        requestParams.put("orderId", orderId);
        abHttpUtil.post(url, requestParams, new CallBackParent(PayOrderActivity.this, getResources().getString(R.string.loading), "getOrderById") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    json = new JSONObject(json.getString("data"));
                    canPay = json.getString("canPay");
                    json = new JSONObject(json.getString("userOrderInfo"));
                    String meal = json.getString("orderPackageName");
                    try {
                        String period = json.getString("orderClientPackagePeriod");
                        tlength = period;
                    } catch (Exception e) {
                        e.printStackTrace();
                        tv_pay_time.setText("");
                    }
                    try {
                        orderInfo = json.getString("orderPayString");
                        if (!"".equals(orderInfo) && orderInfo != null) {
                            orderInfo = orderInfo.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\r\n", "").trim();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        //获取套餐提示信息
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getMealDiscountInfo") + Constants.BASEPARAMS;
        AbRequestParams requestParams = new AbRequestParams();
        requestParams.put("meal", pkgName);
        abHttpUtil.post(url, requestParams, new CallBackParent(PayOrderActivity.this, getResources().getString(R.string.loading), "getMealDiscountInfo") {
            @Override
            public void Get_Result(String result) {
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    if (!TextUtils.isEmpty(data)) {
                        jsonObject = new JSONObject(data);
                        tv_pay_banwidth.setText(jsonObject.getString("mealName"));
                        if (!TextUtils.isEmpty(jsonObject.getString("discountExplain"))) {
                            tv_pay_explanation.setText(jsonObject.getString("discountExplain"));
                            tankuangtishi = jsonObject.getString("discountExplain");
                            ll_pay_explanation.setVisibility(View.VISIBLE);
                            v_pay_explanation.setVisibility(View.VISIBLE);
                        } else {
                            ll_pay_explanation.setVisibility(View.GONE);
                            v_pay_explanation.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(jsonObject.getString("timelength"))){
                            tv_pay_time.setText(jsonObject.getString("timelength"));
                            ll_pay_time.setVisibility(View.VISIBLE);
                        }else{
                            ll_pay_time.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(jsonObject.getString("remark"))) {
                            tv_meal_tishi.setText(jsonObject.getString("remark"));
                            ll_pay_tuifei.setVisibility(View.VISIBLE);
                            v_meal_tishi.setVisibility(View.VISIBLE);
                            tv_meal_tishi.setVisibility(View.VISIBLE);
                        } else {
                            ll_pay_tuifei.setVisibility(View.GONE);
                            v_meal_tishi.setVisibility(View.GONE);
                            tv_meal_tishi.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(jsonObject.getString("discountDes"))) {
                            tv_pay_remark.setText(jsonObject.getString("discountDes"));
                            ll_pay_tuifei.setVisibility(View.VISIBLE);
                        } else {
                            ll_pay_tuifei.setVisibility(View.GONE);
                        }
                    } else {
                        ll_pay_explanation.setVisibility(View.GONE);
                        v_pay_explanation.setVisibility(View.GONE);
                        tv_meal_tishi.setVisibility(View.GONE);
                        v_meal_tishi.setVisibility(View.GONE);
                        ll_pay_tuifei.setVisibility(View.GONE);
                        ll_pay_time.setVisibility(View.GONE);
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
        iv_pay_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击叹号 弹框提示
        ll_pay_tuifei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommDialog dialog = new CommDialog(PayOrderActivity.this);
                dialog.CreateDialogOnlyOk("优惠详情", "确定", tankuangtishi, new CommDialog.CallBack() {
                    @Override
                    public void isConfirm(boolean flag) {
                        dialog.cancelDialog();
                    }
                });
            }
        });
        //支付宝
        rl_pay_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 0;
                rbt_pay_zfb_dh.setChecked(true);
                rbt_pay_wx_dh.setChecked(false);
            }
        });
        //微信
        rl_pay_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 1;
                rbt_pay_zfb_dh.setChecked(false);
                rbt_pay_wx_dh.setChecked(true);
            }
        });
        //确认支付
        ll_pay_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
    }

    // 弹窗
    private void initDialog() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_image_info, null);

        imageView = (ImageView) view.findViewById(R.id.dialog_validate_image);
        signin_validate_image_see = (Button) view.findViewById(R.id.dialog_validate_image_see);
        // 图片loading.....
        String exchange = getResources().getString(R.string.see_no);
        signin_validate_image_see.setText(Html.fromHtml(exchange));
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        dialogBtn = (Button) view.findViewById(R.id.tv_dialog_image_btn);
        dialogBtn.setEnabled(false);
        dialogBtn.setBackgroundResource(R.mipmap.btn_gray);

        // 输入的验证码
        signin_validate_input = (com.toplion.cplusschool.View.ChangeIconEditText) view.findViewById(R.id.dialog_validate_input);
        // 请求验证码
        signin_validate_image_see.performClick();
        signin_validate_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String input = signin_validate_input.getText().toString();
                if (input.length() == 4) {
                    // 输入长度等于4时，验证验证码
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("chkNum", ReturnUtils.encode(input));
                    new Thread() {
                        public void run() {
                            obj = new JSONObject();
                            obj = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("validateChkNum") + Constants.BASEPARAMS, params);
                            Message msg = new Message();
                            msg.what = 3;
                            myHandler.sendMessage(msg);
                        }
                    }.start();
                } else {
                    signin_validate_input.setClearIconVisible(true, false);
                    dialogBtn.setEnabled(false);
                    dialogBtn.setBackgroundResource(R.mipmap.btn_gray);
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    // 下载图片验证码使用
    // 验证使用
    // 点击按钮时下载图片
    public void downImage(View v) {

        if ("".equals(Constants.PHPSESSID_VALUE)) {
            new Thread() {
                public void run() {
                    String getChkNum = "getChkNum";
                    String param = "rid=" + ReturnUtils.encode(getChkNum);
                    System.out.println(param);
                    JSONObject jsonobj = HttpUtils.httpClientGet(
                            Constants.BASE_URL, param, PayOrderActivity.this);
                    try {
                        //为常量赋值
                        Constants.PHPSESSID_VALUE = jsonobj
                                .get(Constants.PHPSESSID).toString();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        downTask = new DownTask();
        downTask.execute("");

        signin_validate_input.setClearIconVisible(true, false);
        signin_validate_input.setText("");
        dialogBtn.setEnabled(false);
        signin_validate_input.setEnabled(true);
        dialogBtn.setBackgroundResource(R.mipmap.btn_gray);
    }

    // 定义一个异步任务，实现图片的下载
    // 第二个泛型是进度值的类型
    class DownTask extends AsyncTask<String, Integer, byte[]> {
        // 该方法由主线程执行，在doInBackground方法之前执行
        // doInBackground方法需要接收下载的图片的网址
        @Override
        protected byte[] doInBackground(String... params) {

            return HttpUtils.httpClientImage(Constants.BASE_URL, "rid=" + ReturnUtils.encode("getChkNum"));
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            if (result != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
                        result.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 支付宝支付
     */
    private void payZFB() {
        if (canPay.equals("false")) {

            Toast.makeText(getBaseContext(), "当前计费周期已缴费!", Toast.LENGTH_SHORT).show();
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        orderInfo = "";
                        // 存在订单
                        if (!"".equals(orderPayString) && orderPayString != null) {
//                            AliPay alipay = new AliPay(PayOrderActivity.this, myHandler);
//                            String result = alipay.pay(orderPayString);
                            PayTask alipay = new PayTask(PayOrderActivity.this);
                            String result = alipay.pay(orderPayString,true);

                            Log.i(TAG, "result = " + result);
                            Message msg = new Message();
                            msg.what = RQF_PAY;
                            msg.obj = result;
                            myHandler.sendMessage(msg);
                        } else {
                            // 不存在订单，新建订单
                            Map<String, String> postparams = new HashMap<String, String>();
                            postparams.put("username", share.getString("username", ""));
                            postparams.put("pkgName", pkgName);
                            postparams.put("clientPackagePeriod", tlength);
                            JSONObject json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getPayOrder") + Constants.BASEPARAMS, postparams);
                            json = new JSONObject(json.getString("result"));
                            String code = json.getString("code");
                            messageStr = json.getString("msg");
                            if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                                orderInfo = json.getString("data").trim();
                                orderInfo = orderInfo.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\r\n", "").trim();
//                                AliPay alipay = new AliPay(PayOrderActivity.this, myHandler);
//                                String result = alipay.pay(orderInfo);

                                PayTask alipay = new PayTask(PayOrderActivity.this);
                                String result = alipay.pay(orderInfo,true);

                                Log.i(TAG, "result = " + result);
                                Message msg = new Message();
                                msg.what = RQF_PAY;
                                msg.obj = result;
                                myHandler.sendMessage(msg);
                            } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                                Message msg = new Message();
                                msg.what = 9001;
                                myHandler.sendMessage(msg);
                            } else {
                                // 提示异常
                                Looper.prepare();
                                Toast.makeText(PayOrderActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }

    /**
     * 微信支付 生成订单
     */
    private void payWX() {
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            Toast.makeText(PayOrderActivity.this, "目前你未安装微信或微信版本暂不支持支付，请先安装或升级微信再支付!", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getWxPayOrder") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        String clientIp = ConnectivityUtils.getSysIp(PayOrderActivity.this);

        // 不存在订单，新建订单
        params.put("username", share.getString("username", ""));
        params.put("pkgName", pkgName);
        params.put("clientIp", clientIp);
        params.put("clientPackagePeriod", tlength);
        abHttpUtil.post(url, params, new CallBackParent(PayOrderActivity.this, getResources().getString(R.string.loading), "getWxPayOrder") {
            @Override
            public void Get_Result(String content) {
                try {
                    JSONObject json = new JSONObject(content);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        String orderInfo = json.getString("data").trim().toString();
                        JSONObject jsons = new JSONObject(orderInfo.replace("[", "").replace("]", ""));
                        WxBean wxBean = new WxBean();
                        wxBean.setAppid(jsons.getString("appid"));
                        wxBean.setMch_id(jsons.getString("mch_id"));
                        wxBean.setPrepayid(jsons.getString("prepayid"));
                        wxBean.setNonce_str(jsons.getString("nonce_str"));
                        wxBean.setTimeStamp(jsons.getString("timestamp"));
                        wxBean.setSign(jsons.getString("sign"));
                        sendReq(wxBean);//发起微信支付
                    } else {
                        // 提示异常
                        Toast.makeText(PayOrderActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 提示异常
                    Toast.makeText(PayOrderActivity.this, "服务器异常,请稍后...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //发起微信支付
    private void sendReq(WxBean wxBean) {
        req = new PayReq();//
        req.appId = wxBean.getAppid();// 公众账号ID
        req.partnerId = wxBean.getMch_id();// 商户号
        req.prepayId = wxBean.getPrepayid();// 预支付交易会话ID
        req.packageValue = "Sign=WXPay";// 扩展字段
        req.nonceStr = wxBean.getNonce_str();// 随机字符串
        req.timeStamp = wxBean.getTimeStamp();// 时间戳
        req.sign = wxBean.getSign();
        msgApi.registerApp(com.toplion.cplusschool.wxutils.Constants.APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * 检查是否执行按钮
     */
    public void validateInput() {
        if (isCheck) {
            ll_pay_confirm.setEnabled(true);
        } else {
            ll_pay_confirm.setEnabled(false);
        }
    }
}
