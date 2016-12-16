package com.toplion.cplusschool.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.CommDialog.CallBack;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Common.CustomDialog;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Update.UpdateVersion;
import com.toplion.cplusschool.Utils.AESUtils;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TelephoneUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.View.ChangeIconEditText;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.UserInsideDao;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录界面
 * 用户填写登录信息，
 * 输入验证码验证，验证通过后登录到主页面
 *
 * @author wang
 * @version 3.0
 */
public class LoginActivity extends BaseActivity {
    //    private String SCHOOLCODE = "sdjzu"; //建大：sdjzu
//    private String SCHOOLCODE = "qlnu"; //齐鲁师范:qlnu
//	private String SCHOOLCODE = "sdmu"; //管理学院:sdmu
    //   private String SCHOOLCODE = "toplion";//test
    private RelativeLayout signin_validate;
    private JSONObject schoolJson;//学校

    private TextView sch_name;                       // 学校名称
    private String sch_id;                           // 学校Id

    private ImageView iv_return;                     // 返回上一级
    private Button btn_login;                        // 登录
    private EditText account;                        // 学号
    private EditText pwd;                            // 密码
    private CheckBox signin_cb;                      // 确定注册

    private boolean isCheck = true;                  // 是否选中
    private boolean isRight = false;                 // 验证码是否正确

    private SharePreferenceUtils share;
    //验证码使用
    private ImageView imageView;                     // 显示图片
    private DownTask downTask;                       // DownTask
    private Button signin_validate_image_see;        // 看不清

    private AnimationDrawable animationDrawable;     // 等待Loading

    private com.toplion.cplusschool.View.ChangeIconEditText signin_validate_input;// 自定义输入框，输入的验证码

    private JSONObject obj;                           // 验证码返回值
    private JSONObject updResult;                     // 登录返回值

    private Dialog dialog;                       // dialog

    private boolean connection = false;               // 一键登录结果

    private TextView signin_agree;                    // 同意
    private int width = 0;                            // 屏幕宽度

    private String wifi;                              // WIFI
    private String TAG = "LoginActivity";
    private String sysVersion = "";
    private String version = "";
    private String schoolName = null;//
    private int codenum = 0;//验证码计数器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        share = new SharePreferenceUtils(this);
        // 获取屏幕高/宽
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // 屏幕的高度和宽度保存到本地缓存
        share.put("width", screenWidth);
        share.put("height", screenHeight);
        // 获取屏幕宽度和WifiName
        width = screenWidth;
//        Constants.SCHOOL_CODE = SCHOOLCODE;
        Log.e(" Constants.SCHOOL_CODE", Constants.SCHOOL_CODE);
        wifi = share.getString("wifiName", "");
        signin_validate = (RelativeLayout) findViewById(R.id.signin_validate);
        sch_name = (TextView) this.findViewById(R.id.signin_sch_des);
        schoolName = getIntent().getStringExtra("sch_name");
        sch_id = getIntent().getStringExtra("sch_id");
        sch_name.setText(schoolName);
        update();
        initView();        // 初始化信息
//        getSchoolData();
        // 首先获取Cookie
        new Thread() {
            public void run() {
                String getChkNum = "getChkNum";
                String param = "rid=" + ReturnUtils.encode(getChkNum);
                JSONObject jsonobj = HttpUtils.httpClientGet(Constants.BASE_URL, param, LoginActivity.this);
                try {
                    //为常量赋值
                    if ("".equals(Constants.PHPSESSID_VALUE)) {
                        Constants.PHPSESSID_VALUE = jsonobj.get(Constants.PHPSESSID).toString();
                    }
                    Message msg = new Message();
                    msg.what = 5;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "获取验证码失败");
                }
            }
        }.start();
        String deviceId = share.getString("deviceId", "");
        Constants.BASEPARAMS = "&schoolCode=" + Constants.SCHOOL_CODE + "&clientOSType=android" + "&clientVerNum=" + Constants.SYSVERSION
                + "&deviceId=" + deviceId + "&role=" + share.getInt("ROLE_TYPE", 2);
    }

    // 查看系统是否需要更新app
    private void update() {
        new Thread() {
            public void run() {
                Map<String, String> postparams = new HashMap<String, String>();
                postparams.put("osType", "android");
                // 获取服务器系统版本号
                JSONObject json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAppUpdateInfo"), postparams);
                try {
                    Log.e("result------>", json.getString("result"));
                    json = new JSONObject(json.getString("result"));
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        version = json.getString("versionNum");
                        Constants.ISBIND = json.getString("isBind");        // 是否强制更新
                        Constants.UPDATE_URL = json.getString("url");       // 获取系统更新路径
                        String noteStr = json.getString("note").toString();
                        if (!TextUtils.isEmpty(noteStr) && !noteStr.equals("null")) {
                            Constants.UPDATE_CONTENT = json.getString("note");   //更新内容
                        } else {
                            if (Constants.ISBIND.equals("true")) {
                                Constants.UPDATE_CONTENT = "系统版本需强制更新，请升级!";
                            } else {
                                Constants.UPDATE_CONTENT = "系统版本已升级，请选择是否升级!";
                            }
                        }
                        Message msg = new Message();
                        msg.what = 7;
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 8;
                        msg.obj = json.getString("msg");
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    // 获取系统版本号失败，出现异常
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 8;
                    mHandler.sendMessage(msg);
                }

            }
        }.start();
    }

    /**
     * @date 2016-3-30 13:03
     * @author WangShengBo
     */
    private void getSchoolData() {
        dialog = new CustomDialog(LoginActivity.this).initDialog("数据加载中，请稍后......");
        new Thread() {
            @Override
            public void run() {
                // 获取学校信息
                String getSchoolInfo = "getSchoolInfo";
                JSONObject json = HttpUtils.httpClientGet(Constants.BASE_URL + "?rid=" + ReturnUtils.encode(getSchoolInfo) + "&schVerNum=" + ReturnUtils.encode("0"));
                // 初始化数据
                String result;
                try {
                    result = json.getString("result");
                    result = ReturnUtils.decode(result);
                    System.out.println(result + "   === ");
                    JSONObject version = new JSONObject(result);
                    String code = version.getString("code");
                    // 判断CODE是否正确
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        version = new JSONObject(version.getString("data"));
                        code = version.getString("schoolVersion");
                        JSONArray array = version.getJSONArray("schoolInfo");
                        // 为学校list赋值
                        for (int i = 0; i < array.length(); i++) {
                            if (array.getJSONObject(i).getString("schoolCode").equals("")) {
                                schoolJson = array.getJSONObject(i);
                                Message msg = new Message();
                                msg.what = 6;
                                mHandler.sendMessage(msg);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }.start();
    }

    // 加载验证码
    private void initLoading() {
        imageView = (ImageView) this.findViewById(R.id.signin_validate_image); // 验证码
        // 图片loading.....
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        signin_validate_image_see.performClick(); // 请求验证码
    }

    // 初始化页面
    private void initView() {
        // 学号  ，密码
        account = (EditText) this.findViewById(R.id.signin_account_des);
        pwd = (EditText) this.findViewById(R.id.signin_pwd_des);
        // 登录禁用，颜色为灰色
        btn_login = (Button) this.findViewById(R.id.sign_btn_login);
        btn_login.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn_login.setBackground(getResources().getDrawable(R.mipmap.btn_gray));
        } else {
            btn_login.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_gray));
        }
        signin_validate_image_see = (Button) this.findViewById(R.id.signin_validate_image_see); // 看不清按钮
        signin_validate_image_see.setWidth(width / 5);
        String exchange2 = getResources().getString(R.string.see_no);
        signin_validate_image_see.setText(Html.fromHtml(exchange2));

        // 用户协议
        signin_agree = (TextView) this.findViewById(R.id.signin_agree);
        String exchange = getResources().getString(R.string.register_agreement);
        signin_agree.setText(Html.fromHtml(exchange));
        signin_agree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 跳转到购买协议界面
                Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });

        // 登录点击事件
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (codenum >= 3) {
                    validateNum();
                } else {
                    Login();
                }

            }

        });

        // 返回上一级
        iv_return = (ImageView) this.findViewById(R.id.signin_return);
        iv_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 关闭当前Activity
                LoginActivity.this.finish();
            }
        });

        // 购买协议是否选中
        signin_cb = (CheckBox) this.findViewById(R.id.signin_cb);
        signin_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
                // 验证
                validateInput();
            }
        });

        // 学号检测
        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // 验证输入
                validateInput();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() == 0) {
                    pwd.setText("");
                }
            }
        });
        // 密码监测
        pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // 验证输入
                validateInput();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        // 输入的验证码检测
        signin_validate_input = (ChangeIconEditText) this.findViewById(R.id.signin_validate_input);

        signin_validate_input.setWidth(width / 3);
        signin_validate_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 获取输入的验证码
                String input = signin_validate_input.getText().toString();
                // 验证码长度等于4的时候，开始验证输入的验证码
                if (input.length() == 4) {
                    isRight = true;
                } else {
                    isRight = false;
                    signin_validate_input.setClearIconVisible(true, false);
                }
                validateInput();
            }
        });

    }

    /**
     * 登录验证
     */
    private void Login() {
        // 判断是否是WIFI登录
        // 弹出等待框
        dialog = new CustomDialog(LoginActivity.this).initDialog("登录验证中，请稍后......");
        connection = true;
        MobclickAgent.onEvent(LoginActivity.this, "login");//友盟统计
        if (connection) {
            //保存初始化信息
            final Map<String, String> map = new HashMap<String, String>();
            //信息需要Base64进行加密
            map.put("username", ReturnUtils.encode(account.getText().toString()));  //用户名
            map.put("password", ReturnUtils.encode(pwd.getText().toString()));      //密码加密
            map.put("schoolId", ReturnUtils.encode(sch_id));                        //学校Id
            map.put("deviceMode", ReturnUtils.encode(TelephoneUtils.getDeviceMode()));//获取设备制式
            map.put("deviceNum", ReturnUtils.encode(TelephoneUtils.getDeviceNum()));   //获取设备唯一编号
            map.put("simNum", ReturnUtils.encode(TelephoneUtils.getSimNum()));         //获取Sim卡序列号，如无Sim卡则置0
            map.put("subscriberNum", ReturnUtils.encode(TelephoneUtils.getSubscriberNum())); //获取用户ID
            map.put("imsiNum", ReturnUtils.encode(TelephoneUtils.getSubscriberNum())); //获取用户ID
            map.put("osType", "android" + ConnectivityUtils.getSdkVersion());

            new Thread() {
                public void run() {
                    updResult = new JSONObject();
                    updResult = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updateUser") + Constants.BASEPARAMS, map);
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
            }.start();

        }
    }

    // 验证码和登录使用Handler
    private Handler mHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @SuppressWarnings("static-access")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final CommDialog d = new CommDialog(LoginActivity.this);
            switch (msg.what) {
                case 1:
                    // 验证验证码
                    try {
                        obj = new JSONObject(obj.getString("result"));
                        String code = obj.getString("code");
                        // 验证码成功匹配
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            signin_validate_input.setClearIconVisible(true, true);
                            Login();
                        } else {
                            // 验证码匹配错误
                            String trip = obj.getString("msg");
                            d.CreateDialogOnlyOk("系统提示", "确定", trip, new CallBack() {
                                @Override
                                public void isConfirm(boolean flag) {
                                    // 判断点击按钮
                                    if (flag) {
                                        // 刷新验证码
                                        signin_validate_image_see.performClick();
                                        // 原输入验证码清空
                                        signin_validate_input.setClearIconVisible(true, false);
                                        signin_validate_input.setText("");
                                        isRight = false;
                                        d.cancelDialog();
                                    }
                                }
                            });
                        }
                        validateInput();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        // 判断用户登录结果
                        String result = updResult.getString("result");
                        result = ReturnUtils.decode(result);
                        Log.e("result", result + "");
                        updResult = new JSONObject(result);
                        String code = updResult.getString("code");
                        // 判断状态码是否一致
                        JSONArray jsonArray = null;
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            if (updResult.getString("roleInfo").equals("[]")) {

                            } else {
                                jsonArray = new JSONArray(updResult.getString("roleInfo"));//获取用户角色信息
                            }
                            updResult = new JSONObject(updResult.getString("data"));
                            code = updResult.getString("code");
                            if (code.equals(CacheConstants.USR_SUCESS)) {
                                // 登录成功，执行操作
                                String token = updResult.getString("token");   // 获取token
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    JSONObject obj = (JSONObject) jsonArray.get(0);
                                    if (account.getText().toString().equals("wzw")) {
                                        share.put("ROLE_TYPE", 1);
                                    } else {
                                        share.put("ROLE_TYPE", Function.getInstance().getInteger(obj,"ROLE_TYPE"));
                                    }
                                    share.put("ROLE_ID",  Function.getInstance().getString(obj,"ROLE_ID"));
                                    Log.e("ROLE_ID", obj.getString("ROLE_ID"));
                                    share.put("ROLE_USERNAME", Function.getInstance().getString(obj,"ROLE_USERNAME"));
                                } else {
                                    if (account.getText().toString().equals("wzw")) {
                                        share.put("ROLE_TYPE", 1);
                                    } else {
                                        share.put("ROLE_TYPE", 2);
                                    }
                                }
//								Long tokenTim = Long.parseLong(updResult.getString("tokenExpTime").toString());
//                                String tokenTim = updResult.getString("tokenExpTime").toString();.
                                // 更换值,保存数据到本地，密码进行加密
                                share.put("username", account.getText().toString());          // 用户名
                                share.put("pwd", AESUtils.encode(pwd.getText().toString()));  // 密码
                                share.put("token", token);                                    // token
                                Log.e("token", token);
//                                share.put("tokenTim", tokenTim + "");                       // token有效期
                                share.put("deviceMode", new TelephoneUtils(LoginActivity.this).getDeviceMode());// 获取设备制式
                                share.put("deviceNum", new TelephoneUtils(LoginActivity.this).getDeviceNum());   // 获取设备唯一编号
                                share.put("simNum", new TelephoneUtils(LoginActivity.this).getSimNum());         // 获取Sim卡序列号，如无Sim卡则置0
                                share.put("subscriberNum", new TelephoneUtils(LoginActivity.this).getSubscriberNum());
                                share.put("imsiNum", new TelephoneUtils(LoginActivity.this).getSubscriberNum());
                                share.put("sch_name", sch_name.getText().toString()); // 保存学校名字进常量
                                share.put("schoolId", sch_id);                        // 学校Id
                                share.put("deviceId", updResult.getString("deviceId") + "");
                                share.put("loginId", updResult.getString("loginId"));
                                UserInsideDao userDao = new UserInsideDao(LoginActivity.this);
                                userDao.startWritableDatabase(true);
                                userDao.deleteAll();
                                userDao.closeDatabase();
                                // 关闭弹窗
//                                dialog.dismiss();
                                String deviceId = share.getString("deviceId", "");
                                Constants.BASEPARAMS = "&schoolCode=" + Constants.SCHOOL_CODE + "&clientOSType=android" + "&clientVerNum=" + Constants.SYSVERSION
                                        + "&deviceId=" + deviceId + "&role=" + share.getInt("ROLE_TYPE", 2);
//                                Intent intent = new Intent(LoginActivity.this, MainFrameActivity.class);
//                                startActivity(intent);
//                                setResult(1);
//                                finish();
                                getMenu();
                            } else {
                                // 提示错误信息
                                if (dialog != null) dialog.dismiss();
                                String trip = updResult.getString("msg");
                                d.CreateDialogOnlyOk("系统提示", "确定", trip, new CallBack() {
                                    @Override
                                    public void isConfirm(boolean flag) {
                                        if (flag) {
                                            signin_validate_image_see.performClick();
                                            // 原输入验证码清空
                                            signin_validate_input.setClearIconVisible(true, false);
                                            signin_validate_input.setText("");
                                            isRight = false;
                                            d.cancelDialog();
                                            validateInput();
                                        }
                                    }
                                });
                            }
                        } else {
                            codenum++;
                            if (codenum >= 3) {
                                signin_validate.setVisibility(View.VISIBLE);
                                // validateNum();
                            }
                            // 关闭弹窗
                            dialog.dismiss();
                            // 提示错误信息
                            d.CreateDialogOnlyOk("系统提示", "确定", updResult.getString("msg"), new CallBack() {
                                @Override
                                public void isConfirm(boolean flag) {
                                    if (flag) {
                                        signin_validate_image_see.performClick();
                                        // 原输入验证码清空
                                        signin_validate_input.setClearIconVisible(true, false);
                                        signin_validate_input.setText("");
                                        isRight = false;
                                        d.cancelDialog();
                                        validateInput();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        codenum++;
                        if (codenum >= 3) {
                            signin_validate.setVisibility(View.VISIBLE);
                            // validateNum();
                        }
                        e.printStackTrace();
                        // 提示错误信息
                        d.CreateDialogOnlyOk("系统提示", "确定", Constants.NETWORK_ERROR, new CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                if (flag) {
                                    signin_validate_image_see.performClick();
                                    // 原输入验证码清空
                                    signin_validate_input.setClearIconVisible(true, false);
                                    signin_validate_input.setText("");
                                    isRight = false;
                                    d.cancelDialog();
                                    validateInput();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                    break;
                case 5:
                    // initView();        // 初始化信息
                    initLoading();     // 加载验证码
                    break;
                case 6:
                    try {
                        String title = schoolJson.getString("schoolName");
                        String id = schoolJson.getString("schoolId");
                        // 新加WifiName, serverIpAddress 2016/2/1
                        String wifiName = schoolJson.getString("wifiName");
                        String serverIpAddress = schoolJson.getString("serverIpAddress");
                        String schoolCode = schoolJson.getString("schoolCode");
                        String schoolId = schoolJson.getString("schoolId");
                        sch_name.setText(title);  // 学校名称
                        sch_id = id;             // 学校id
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        // 保存wifi 和 Ip 信息到本地缓存
                        share.put("wifiName", wifiName);
                        Log.e("serverIp", serverIpAddress);
                        share.put("serverIp", serverIpAddress);
                        share.put("schoolCode", schoolCode);
                        if (!TextUtils.isEmpty(schoolId)) {
                            share.put("schoolId", schoolId);
                        }
                        share.put("BASE_URL", Constants.BASE_URL);
//						Constants.BASE_URL = "http://"+serverIpAddress+"/index.php";  //默认请求路径
                        Constants.SCHOOL_CODE = schoolCode;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    Log.d("sysVersion========", sysVersion);
                    Log.d("version========", version);
                    if (!version.isEmpty()) {
                        if (Integer.parseInt(version.replace(".", "")) > Integer.parseInt(Constants.SYSVERSION.replace(".", ""))) {
                            // 更新
                            if (Constants.UPDATE_URL.contains("http:")
                                    || Constants.UPDATE_URL.contains("https:")) {
                                UpdateVersion versionUp = new UpdateVersion(LoginActivity.this);
                                versionUp.checkUpdate();
                            } else
                                Toast.makeText(LoginActivity.this, "下载文件路径出现异常!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 8:
                    String str = (String) msg.obj;
                    if (TextUtils.isEmpty(str)) {
                        str = Constants.NETWORK_ERROR;
                    }
                    ToastManager.getInstance().showToast(LoginActivity.this, str);
                    break;
                case 200:
                    if (codenum >= 3) {
                        validateNum();
                    } else {
                        getMenu();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //首次登录获取菜单并保存本地
    private void getMenu() {
        if (ConnectivityUtils.isNetworkAvailable(this)) {//有网络，进行自动登录
            MobclickAgent.onEvent(LoginActivity.this, "login");//友盟统计
            String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("queryUserInfo") + Constants.BASEPARAMS;
            AbRequestParams params = new AbRequestParams();
            //信息需要Base64进行加密
            params.put("username", ReturnUtils.encode(account.getText().toString()));  //用户名
            params.put("token", ReturnUtils.encode(share.getString("token", ""))); //Token加密
            abHttpUtil.post(url, params, new CallBackParent(LoginActivity.this, "正在加载菜单") {
                @Override
                public void Get_Result(String content) {
                    try {
                        JSONObject json = new JSONObject(content);
                        Log.e("content", content + "");
                        String code = json.getString("code");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            String uploadUrl = Function.getInstance().getString(json, "uploadUrl");
                            String personUrl = Function.getInstance().getString(json,"personUrl");
                            String photowallUrl = Function.getInstance().getString(json,"photowallUrl");
                            share.put("uploadUrl", uploadUrl);//上传照片路径
                            share.put("personUrl",personUrl);//照片墙
                            share.put("photowallUrl",photowallUrl);//照片墙上传照片路径
                            JSONArray roleInfo = new JSONArray(Function.getInstance().getString(json, "roleInfo"));
                            if (roleInfo != null && roleInfo.length() > 0) {//保存用户个人信息（签到等。。）
                                JSONObject joRoleInfo = roleInfo.getJSONObject(0);
                                share.put("ROLE_TYPE",  Function.getInstance().getInteger(joRoleInfo, "ROLE_TYPE"));
                                share.put("ROLE_ID", Function.getInstance().getString(joRoleInfo, "ROLE_ID"));
                                share.put("ROLE_USERNAME", Function.getInstance().getString(joRoleInfo, "ROLE_USERNAME"));//姓名
                                share.put("XB", Function.getInstance().getString(joRoleInfo, "XB"));//性别
                                share.put("NICKNAME", Function.getInstance().getString(joRoleInfo, "NICKNAME"));//昵称
                                share.put("ROLE_ID", Function.getInstance().getString(joRoleInfo, "ROLE_ID"));//学号或者工号
                                share.put("CSRQ", Function.getInstance().getString(joRoleInfo, "CSRQ"));//出生年月
                                share.put("CSDM",Function.getInstance().getString(joRoleInfo,"CSDM"));//城市代码
                                share.put("SJH", Function.getInstance().getString(joRoleInfo, "SJH"));//手机号
                                share.put("HEADIMAGE", Function.getInstance().getString(joRoleInfo, "HEADIMAGE").replace("thumb/", ""));//头像
                                share.put("SBIFLOWERSNUMBER", Function.getInstance().getInteger(joRoleInfo, "SBIFLOWERSNUMBER"));//鲜花数量
                                share.put("SBICONSECUTIVEDAYS", Function.getInstance().getInteger(joRoleInfo, "SBICONSECUTIVEDAYS"));//签到日期
                                share.put("STATUS", Function.getInstance().getInteger(joRoleInfo, "STATUS"));//今天签到状态 0表示没有签到 1表示已签到
                                share.put("ZYMC", Function.getInstance().getString(joRoleInfo, "ZYMC"));//专业名

                                share.put("CSDM", Function.getInstance().getString(joRoleInfo, "CSDM"));//住址
                                share.put("XZZ", Function.getInstance().getString(joRoleInfo, "XZZ"));//现住址
                                share.put("GRSM",Function.getInstance().getString(joRoleInfo,"GRSM"));//个人说明
                                share.put("ZYJSZWM", Function.getInstance().getString(joRoleInfo, "ZYJSZWM"));//技术职称

                                share.put("SENIORNAME", Function.getInstance().getString(joRoleInfo, "SENIORNAME"));//部门
                                share.put("DEPARTNAME", Function.getInstance().getString(joRoleInfo, "DEPARTNAME"));//职位名

                            }
                            json = new JSONObject(json.getString("data"));
                            share.put("samUserInfo", json.getString("samUserInfo") + "");
                            share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                            share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                            share.put("menujson", content);
                            share.put("islogo", "true");
                            setResult(1);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ToastManager.getInstance().showToast(this, "网络异常,请稍后再试！");
            return;
        }
    }

    /**
     * 验证验证码
     */
    private void validateNum() {
        String numner = signin_validate_input.getText().toString();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("chkNum", ReturnUtils.encode(numner));
        new Thread() {
            public void run() {
                obj = new JSONObject();
                obj = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("validateChkNum") + Constants.BASEPARAMS, params);
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    // 判断登录按钮是否可用
    private void validateInput() {
        if (codenum < 3) {
            isRight = true;
        }
        if (isCheck && isRight && account.getText().toString().trim().length() > 0 && pwd.getText().toString().trim().length() > 0) {
            btn_login.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_login.setBackground(getResources().getDrawable(R.mipmap.btn_orange));
            } else {
                btn_login.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_orange));
            }
        } else {
            btn_login.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_login.setBackground(getResources().getDrawable(R.mipmap.btn_gray));
            } else {
                btn_login.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_gray));
            }
        }
    }

    // 验证码使用
    // 点击按钮时下载图片
    public void downImage(View v) {
        downTask = new DownTask();
        downTask.execute("");
        signin_validate_input.setClearIconVisible(true, false);
        signin_validate_input.setText("");
        isRight = false;
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
            return HttpUtils.httpClientImage(Constants.BASE_URL, "rid=" + ReturnUtils.encode("getChkNum") + Constants.BASEPARAMS);
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            if (result != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                if(bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void setListener() {
    }


}
