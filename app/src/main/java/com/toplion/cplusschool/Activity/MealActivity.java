package com.toplion.cplusschool.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.CommDialog.CallBack;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐页面
 * 显示用户可以选择的套餐
 * 用户选择套餐后，点击下一步跳转到套餐确认界面
 *
 * @author liyb
 * @version 1.0
 */
public class MealActivity extends BaseActivity {
    private ImageView tanimg;
    private ImageButton imgBtn_dialog;
    private Dialog allMsg;
    //Dialog的布局View
    private View allMsgView;
    private GridView gview;                // 带宽选择
    private GridView gview_sign;        // 时长选择
    private SharePreferenceUtils share;   // sharedPreferences

    private ImageView meal_iv_return;   // 返回键
    private Button btn_next;            // 下一步

    private TextView meal_agree;        // 购买协议


    private JSONObject json;            // 返回值


    private AlertDialog.Builder builder;// Builder
    private AlertDialog dialog;         // dialog
    private DownTask downTask;          // DOWNTASK
    private ImageView imageView;        // 显示图片
    private Button signin_validate_image_see;// 看不清
    private AnimationDrawable animationDrawable; // 等待Loading
    private com.toplion.cplusschool.View.ChangeIconEditText signin_validate_input;      // 输入的验证码
    private JSONObject obj;            // 验证码返回值
    private Button dialogBtn;          // 自定义Dialog的Button

    private String[] meals = null;            // 套餐选择数组
    private List<String> wlans;        // 带宽数据
    private List<String> times;        // 时长数据
    private String nextBillingTime;    // 下次绑定时间
    private String online;             // 服务器时间
    private String pkgName = null;            // 套餐
    private int height;                // 屏幕高度
    private CheckBox meal_ck_cb;       // 选中
    private boolean isCheck = true;    // 是否选中
    private TextView tv_meal_tishi;//提示
    private String messageStr = null;//提示
    //图片下载类
    private AbImageLoader mAbImageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals);
        // 获取系统配置文件Config
        share = new SharePreferenceUtils(this);
        height = share.getInt("height", 0);
        try {
            // 获取SAM用户信息
            JSONObject samUserInfo = new JSONObject(share.getString("samUserInfo", ""));
            // 获取下次绑定时间
            nextBillingTime = samUserInfo.getString("nextBillingTime");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MealActivity.this, "账户出现错误......", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        btn_next = (Button) findViewById(R.id.btn_meal_next);
        tv_meal_tishi = (TextView) findViewById(R.id.tv_meal_tishi);
        // 购买协议
        meal_agree = (TextView) this.findViewById(R.id.meal_agree);
        String exchange = getResources().getString(R.string.register_agreement);
        meal_agree.setText(Html.fromHtml(exchange));
        meal_agree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 跳转到购买协议界面
                Intent intent = new Intent(MealActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });
        // 是否确认购买协议
        meal_ck_cb = (CheckBox) this.findViewById(R.id.meal_ck_cb);
        meal_ck_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
                isCheck = check;
                validateInput();
            }
        });

        //初始化
        initView();
        // 带宽选择
        gridView();
       // getcenterImg();
    }

    private void initView() {
        mAbImageLoader = AbImageLoader.getInstance(this);
        // 通过LayoutInflater找到改布局
        allMsgView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tandialog, null);
       //创建Dialog
        allMsg = new AlertDialog.Builder(this).create();
       //设置点击外边缘不消失，2.x的应该是默认不消失的
        allMsg.setCanceledOnTouchOutside(false);
       //findView布局里的控件
        tanimg = (ImageView) allMsgView.findViewById(R.id.tanimg);
        imgBtn_dialog = (ImageButton) allMsgView.findViewById(R.id.dialog_pre_entry_close);
        imgBtn_dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allMsg.dismiss();
            }
        });
        allMsg.show();
        allMsg.getWindow().setContentView((RelativeLayout) allMsgView);
        // 返回上一级
        meal_iv_return = (ImageView) this.findViewById(R.id.meal_iv_return);
        meal_iv_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MealActivity.this.finish();
            }
        });
        gview_sign = (GridView) findViewById(R.id.gview_time);
        // 选择时长的点击事件
        gview_sign.setOnItemClickListener(new ItemClickListenerSign());
        // 下一步

        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 弹出验证码，验证验证码
                // Toast.makeText(MealActivity.this, tmoney + "  " + tlength, Toast.LENGTH_SHORT).show();
                if (!"".equals(tmoney) && !"".equals(tlength)
                        && tmoney != null && tlength != null) {
                    initDialog();
                } else {
                    Toast.makeText(MealActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
private void getcenterImg(){
    mAbImageLoader.display(tanimg,"http://pic14.nipic.com/20110613/7575067_130213594310_2.jpg");
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
        // 请求验证码
        signin_validate_image_see.performClick();

        dialogBtn = (Button) view.findViewById(R.id.tv_dialog_image_btn);
        dialogBtn.setEnabled(false);
        dialogBtn.setBackgroundResource(R.mipmap.btn_gray);

        // 输入的验证码
        signin_validate_input = (com.toplion.cplusschool.View.ChangeIconEditText) view.findViewById(R.id.dialog_validate_input);
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
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                } else {
                    signin_validate_input.setClearIconVisible(true, false);
                    dialogBtn.setEnabled(false);
                    dialogBtn.setBackgroundResource(R.mipmap.btn_gray);
                }
            }
        });
        // 确定
        Button left = (Button) view.findViewById(R.id.tv_dialog_image_btn);
        left.setText("确定");
        left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(MealActivity.this, "pay_detail");//统计
                Intent intent = new Intent(MealActivity.this, PayOrderActivity.class);
                //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                Bundle b = new Bundle();
                b.putString("money", tmoney);
                b.putString("wlan", tlength);
                b.putString("pkgName", pkgName);
                b.putString("tlength", startEnd);
                b.putString("orderInfo", orderInfo);
                //此处使用putExtras，接受方就响应的使用getExtra
                intent.putExtras(b);
                startActivity(intent);
//				finish();
                // 关闭弹窗
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private String orderInfo = ""; // 订单信息

    /**
     * 套餐选择获取数据
     */
    private void gridView() {
        new Thread() {
            public void run() {
                try {
                    json = HttpUtils.httpClientGet(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAllUserPkgInfo") + "&token=" + ReturnUtils.encode(share.getString("token", "")) + "&username=" + ReturnUtils.encode(share.getString("username", "")));
                    // 获取JSON数据
                    System.out.println(json);
                    json = new JSONObject(json.getString("result"));
                    String code = json.getString("code");
                    messageStr = json.getString("msg").toString();
                    Log.e("json--------", json.toString());
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        String result = json.getString("packages");
//                        String result = "[10M无线20M有线|b1|包月|35,本次包月优惠价45|b1|包月|45,本次包月优惠价55|b1|包月|55,本次包月优惠价85|b1|包学期|85,10M无线 20M有线|b1|包学期|1000]";
                        online = TimeUtils.timeStamp2Date(json.getString("serverTime"), "yyyy.MM.dd");
                        result = result.replace("[", "").replace("]", "");
                        Log.e("meal--result-----", result);
                        if (result != null && result != "") {
                            // 获取套餐数据
                            meals = result.split(",");
                            if (meals != null && meals.length > 0) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = messageStr;
                                mHandler.sendMessage(msg);
                            } else {
                                isCheck = false;
                                Message msg = new Message();
                                msg.what = 6;
                                msg.obj = messageStr;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            isCheck = false;
                            Message msg = new Message();
                            msg.what = 6;
                            msg.obj = messageStr;
                            mHandler.sendMessage(msg);
                        }
                    } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                        Message msg = new Message();
                        msg.what = 9001;
                        msg.obj = messageStr;
                        mHandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what = 5;
                        msg.obj = messageStr;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isCheck = false;
                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = messageStr;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    List<String> list = new ArrayList<String>();
                    times = new ArrayList<String>();
                    for (int i = 0; i < meals.length; i++) {
                        if (meals[i] != null && meals[i] != "") {
                            String[] meal = meals[i].replace("\"", "").split("\\|");
                            if (meal != null && meal.length >= 3) {
                                list.add(meal[2]);
                            }
                        }
                        times.add(meals[i]);
                    }
                    // 去重
//                    List<Integer> sorts = new ArrayList<Integer>();
//                    for (String sign : list) {
//                        if (sign.contains("M")) {
//                            Integer m = Integer.parseInt(sign.substring(0, sign.indexOf("M")));
//                            sorts.add(m);
//                        }
//                    }
//                    List<Integer> newSort = new ArrayList<Integer>(new HashSet<Integer>(sorts));
//                    // 排序
//                    Collections.sort(newSort);
//                    list = new ArrayList<String>();
//                    // 重新赋值
//                    for (Integer sign : newSort) {
//                        list.add(sign + "M");
//                    }
                    wlans = list;
                    gview = (GridView) findViewById(R.id.gview);
                    gview_sign = (GridView) findViewById(R.id.gview_time);
                    gview.setAdapter(new ButtonAdapter(MealActivity.this, wlans));
                    // 添加消息处理
                    gview.setOnItemClickListener(new ItemClickListener());
                    break;
                case 2:
                    gview_sign = (GridView) findViewById(R.id.gview_time);

                    // 添加消息处理
                    gview_sign.setOnItemClickListener(new ItemClickListenerSign());
                    break;
                case 3:
                    try {
                        obj = new JSONObject(obj.getString("result"));
                        String code = obj.getString("code");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            dialogBtn.setEnabled(true);
                            dialogBtn.setBackgroundResource(R.mipmap.btn_orange);
                            signin_validate_input.setClearIconVisible(true, true);
                        } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                            // ToastManager.getInstance().showToast(MyOrderDetailActivity.this,R.string.login_timeout);
                        } else {
                            //Toasts 提示
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
                        Toast.makeText(MealActivity.this, CacheConstants.CHKNUM_ERROR, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    btn_next.setEnabled(false);
                    btn_next.setBackgroundResource(R.mipmap.btn_gray);
                    break;
                case 5:
                    String strmsg = (String) msg.obj;
                    final CommDialog dialogs = new CommDialog(MealActivity.this);
                    dialogs.CreateDialog("确定", "系统提示", strmsg + "，请重新登录", MealActivity.this, new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            if (flag) {
                                Intent intent = new Intent(MealActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            dialogs.cancelDialog();
                        }
                    });
                    break;
                case 6:
                    validateInput();
                    String msgStr = (String) msg.obj;
                    if (!TextUtils.isEmpty(msgStr)) {
                        msgStr = "服务器连接失败，请检查网络后重试!";
                    }
                    final CommDialog dialog = new CommDialog(MealActivity.this);
                    dialog.CreateDialogOnlyOk("系统提示", "确定", msgStr, new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            dialog.cancelDialog();
                            finish();
                        }
                    });
                    break;
                case 11:
                    try {
                        Intent intent = new Intent(MealActivity.this, PayOrderActivity.class);
                        //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                        Bundle b = new Bundle();
                        String sub = "subject=\"";
                        sub = orderInfo.substring(orderInfo.indexOf(sub) + sub.length());
                        sub = sub.substring(0, sub.indexOf("\""));

                        String out_trade_no = "out_trade_no=\"";
                        out_trade_no = orderInfo.substring(orderInfo.indexOf(out_trade_no) + out_trade_no.length());
                        out_trade_no = out_trade_no.substring(0, out_trade_no.indexOf("\""));

                        String total_fee = "total_fee=\"";
                        total_fee = orderInfo.substring(orderInfo.indexOf(total_fee) + total_fee.length());
                        total_fee = total_fee.substring(0, total_fee.indexOf("\""));
                        b.putString("orderNum", out_trade_no);
                        b.putString("money", total_fee);
                        b.putString("wlan", sub);
                        b.putString("tlength", startEnd);
                        b.putString("orderInfo", orderInfo);
                        //此处使用putExtras，接受方就响应的使用getExtra
                        intent.putExtras(b);
                        startActivity(intent);
//						finish();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case 9001:
//                    ToastManager.getInstance().showToast(MealActivity.this,R.string.login_timeout);
                    CommonUtil.intoLogin(MealActivity.this, share, messageStr);
                    break;
                default:
                    break;
            }
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                MealActivity.this.finish();
                break;

            default:
                break;
        }
    }

    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
            for (int i = 0; i < parent.getCount(); i++) {
                View v = parent.getChildAt(i);
                if (position == i) {// 当前选中的Item改变背景颜色
                    view.setBackgroundResource(R.mipmap.dashed_orange);
                } else {
                    v.setBackgroundResource(R.mipmap.btn_gray);

                }
            }
        }
    }


    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListenerSign implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
            for (int i = 0; i < parent.getCount(); i++) {
                View v = parent.getChildAt(i);
                if (position == i) {// 当前选中的Item改变背景颜色
                    view.setBackgroundResource(R.mipmap.dashed_orange);
                    TextView tv = (TextView) view.findViewById(R.id.gridview_text_sign);
                    tv.setTextColor(Color.WHITE);
                } else {
                    v.setBackgroundResource(R.mipmap.btn_gray);
                    TextView tv = (TextView) v.findViewById(R.id.gridview_text_sign);
                    tv.setTextColor(Color.BLACK);
                }
            }
        }

    }

    private String tmoney;       // 金额
    private String tlength;      // 时长
    private String startEnd;     // 开始结束时间

    /**
     * 自定义带宽Adapter
     *
     * @author liyb
     */
    private class ButtonAdapter extends BaseAdapter {
        private Context mContext;     // Context
        @SuppressWarnings("unused")
        private JSONObject json;      // 返回数据Json
        private List<String> wlan;    // 数组接收返回数据

        public ButtonAdapter(Context c, List<String> wlan) {
            mContext = c;
            this.wlan = wlan;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private boolean first = false;

        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final Button b;
            if (convertView == null) {
                // 生成Button
                b = new Button(mContext);
                b.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, height / 20));
                b.setPadding(4, 4, 4, 4);
                b.setTextSize(14f);
                b.setBackgroundResource(R.mipmap.dashed_gray);

            } else {
                b = (Button) convertView;
            }
            // 为按钮赋值
            try {
                b.setText(wlan.get(position));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!first) {
                // 默认选中第一个
                if (position == 0) {
                    b.setBackgroundResource(R.mipmap.dashed_orange);
                    b.setTextColor(Color.WHITE);
                    try {
                        if (pkgName.contains("学期")) {
                            startEnd = "本学期";
                        }
                        int tleng = Integer.parseInt(tlength.replace("M", ""));
                        if (tleng >= 10) {
                            tv_meal_tishi.setVisibility(View.VISIBLE);
                        } else {
                            tv_meal_tishi.setVisibility(View.GONE);
                        }
                        // 更新金额 ，带宽，时长数据
                        TextView money = (TextView) MealActivity.this.findViewById(R.id.meal_tv_money);
                        money.setText(tmoney);

                        TextView wlan = (TextView) MealActivity.this.findViewById(R.id.meal_tv_info_wlan);
                        wlan.setText(tlength);

                        TextView start = (TextView) MealActivity.this.findViewById(R.id.meal_tv_tlength);
                        start.setText(startEnd);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // 配置适配器
                    List<String> time = new ArrayList<String>();
                    for (int i = 0; i < times.size(); i++) {
                        String[] meals = times.get(i).replace("\"", "").split("\\|");
                        if (meals[2].equals(b.getText().toString())) {
                            time.add(times.get(i));
                        }
                    }
                    gview_sign.setAdapter(new TimeAdapter(MealActivity.this, time));
                } else {
                    b.setBackgroundResource(R.mipmap.dashed_gray);
                    b.setTextColor(Color.BLACK);
                }
                first = true;
            }


            b.setId(position);
            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置背景色
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View s = parent.getChildAt(i);
                        if (position == i) { // 当前选中的Item改变背景颜色
                            s.setBackgroundResource(R.mipmap.dashed_orange);
                            ((Button) s).setTextColor(Color.WHITE);
                            try {
                                if (pkgName.contains("学期")) {
                                    startEnd = "本学期";
                                }
                                int tleng = Integer.parseInt(tlength.replace("M", ""));
                                if (tleng >= 10) {
                                    tv_meal_tishi.setVisibility(View.VISIBLE);
                                } else {
                                    tv_meal_tishi.setVisibility(View.GONE);
                                }
                                TextView money = (TextView) MealActivity.this.findViewById(R.id.meal_tv_money);
                                money.setText(tmoney);

                                TextView wlan = (TextView) MealActivity.this.findViewById(R.id.meal_tv_info_wlan);
                                wlan.setText(tlength);

                                TextView start = (TextView) MealActivity.this.findViewById(R.id.meal_tv_tlength);
                                start.setText(startEnd);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            s.setBackgroundResource(R.mipmap.dashed_gray);
                            ((Button) s).setTextColor(Color.BLACK);
                        }
                    }
                    // 配置适配器
                    List<String> time = new ArrayList<String>();
                    for (int i = 0; i < times.size(); i++) {
                        String[] meals = times.get(i).replace("\"", "").split("\\|");
                        if (meals[2].equals(b.getText().toString())) {
                            time.add(times.get(i));
                        }

                    }
                    gview_sign.setAdapter(new TimeAdapter(MealActivity.this, time));
                }
            });
            return b;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return wlan.size();
        }

    }

    /**
     * 自定义时长Adapter
     */
    private class TimeAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> time;

        public TimeAdapter(Context c, List<String> times) {
            mContext = c;
            this.time = times;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private boolean first = false;

        public View getView(final int position, final View convertView, final ViewGroup parent) {
            String[] meal = time.get(position).replace("\"", "").split("\\|");
            final Button b;
            if (convertView == null) {
                // 构建新的按钮
                b = new Button(mContext);
                b.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, height / 20));
                b.setPadding(4, 4, 4, 4);
                b.setTextSize(14f);
                b.setBackgroundResource(R.mipmap.dashed_gray);
            } else {
                b = (Button) convertView;
            }
            if (!first) {
                // 默认选中第一个
                if (position == 0) {
                    b.setBackgroundResource(R.mipmap.dashed_orange);
                    b.setTextColor(Color.WHITE);
                    try {
                        String[] me = time.get(0).replace("\"", "").split("\\|");
                        pkgName = time.get(0).replace("\"", "");
                        String buy = me[1];
                        // 获取到期时间
                        String nextBilling = nextBillingTime.substring(0, nextBillingTime.indexOf("T")).replace("-", ".");
                        long next = Long.parseLong(TimeUtils.date2TimeStamp(nextBilling, "yyyy.MM.dd"));
                        long longOnline = Long.parseLong(TimeUtils.date2TimeStamp(online, "yyyy.MM.dd"));
                        long res = next - longOnline;
                        if (res < 0) {
                            // 服务器时间
                            startEnd = TimeUtils.getMealTLength(online, buy);
                        } else {
                            // SAM时间
                            startEnd = TimeUtils.getMealTLength(nextBilling, buy);
                        }
                        if (pkgName.contains("学期")) {
                            startEnd = "本学期";
                        }
                        tlength = me[2];
                        tmoney = me[3];
                        int tleng = Integer.parseInt(tlength.replace("M", ""));
                        if (tleng >= 10) {
                            tv_meal_tishi.setVisibility(View.VISIBLE);
                        } else {
                            tv_meal_tishi.setVisibility(View.GONE);
                        }
                        // 更新金额 ，带宽，时长数据
                        TextView money = (TextView) MealActivity.this.findViewById(R.id.meal_tv_money);
                        money.setText(tmoney);

                        TextView wlan = (TextView) MealActivity.this.findViewById(R.id.meal_tv_info_wlan);
                        wlan.setText(tlength);

                        TextView start = (TextView) MealActivity.this.findViewById(R.id.meal_tv_tlength);
                        start.setText(startEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    b.setBackgroundResource(R.mipmap.dashed_gray);
                    b.setTextColor(Color.BLACK);
                }
                first = true;
            }
            // 按钮显示数据
            try {
                b.setText(meal[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            b.setId(position);
            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置背景色
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View s = parent.getChildAt(i);
                        if (position == i) { // 当前选中的Item改变背景颜色
                            s.setBackgroundResource(R.mipmap.dashed_orange);
                            ((Button) s).setTextColor(Color.WHITE);
                            String[] me = time.get(i).replace("\"", "").split("\\|");
                            pkgName = time.get(i).replace("\"", "");
                            String buy = me[1];
                            // 获取到期时间
                            String nextBilling = nextBillingTime.substring(0, nextBillingTime.indexOf("T")).replace("-", ".");
                            long next = Long.parseLong(TimeUtils.date2TimeStamp(nextBilling, "yyyy.MM.dd"));
                            long longOnline = Long.parseLong(TimeUtils.date2TimeStamp(online, "yyyy.MM.dd"));
                            long res = next - longOnline;
                            if (res < 0) {
                                // 服务器时间
                                startEnd = TimeUtils.getMealTLength(online, buy);
                            } else {
                                // SAM时间
                                startEnd = TimeUtils.getMealTLength(nextBilling, buy);
                            }
                            if (pkgName.contains("学期")) {
                                startEnd = "本学期";
                            }
                            int tleng = Integer.parseInt(tlength.replace("M", ""));
                            if (tleng >= 10) {
                                tv_meal_tishi.setVisibility(View.VISIBLE);
                            } else {
                                tv_meal_tishi.setVisibility(View.GONE);
                            }
                            tlength = me[2];
                            tmoney = me[3];
                            // 更新金额 ，带宽，时长数据
                            TextView money = (TextView) MealActivity.this.findViewById(R.id.meal_tv_money);
                            money.setText(tmoney);

                            TextView wlan = (TextView) MealActivity.this.findViewById(R.id.meal_tv_info_wlan);
                            wlan.setText(tlength);

                            TextView start = (TextView) MealActivity.this.findViewById(R.id.meal_tv_tlength);
                            start.setText(startEnd);
                        } else {
                            s.setBackgroundResource(R.mipmap.dashed_gray);
                            ((Button) s).setTextColor(Color.BLACK);
                        }
                    }
                }
            });
            return b;
        }

        @Override
        public int getCount() {
            return time.size();
        }

    }

    /**
     * 检查是否执行按钮
     */
    public void validateInput() {
        if (isCheck) {
            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.mipmap.btn_orange);
        } else {
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.mipmap.btn_gray);
        }
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
                            Constants.BASE_URL, param, MealActivity.this);
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
    }

    @Override
    protected void onDestroy() {
        // 退出该 activity时，中断异步任务
        super.onDestroy();
        //downTask.cancel(false);
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

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub

    }
}
