package com.toplion.cplusschool.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.global.AbActivityManager;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Fragment.MeFragment;
import com.toplion.cplusschool.Fragment.MeFragmentNew;
import com.toplion.cplusschool.Fragment.PlayGroundFragment;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Update.UpdateVersion;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TelephoneUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toplion on 2016/9/21.
 */
public class MainTabActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager my_viewpage;
    private SharePreferenceUtils share;
    // 底部Tab切换使用
    private RelativeLayout rl_playground, rl_me;         // tab切换布局
    private ImageView playgroundImg, meImg;                    // 底部标签图片 首页，设置
    private TextView playgroundTv, meTv;                       // 底部标签的文本 首页，设置
    private String sysVersion = "";
    private String version = "";
    private AbHttpUtil abHttpUtil;
    private ArrayList<Fragment> fragmentlist = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);
        AbActivityManager.getInstance().addActivity(this);
        MobclickAgent.openActivityDurationTrack(false);
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        sysVersion = ConnectivityUtils.getAppVersionName(this);
        Constants.SYSVERSION = sysVersion;
        my_viewpage=(ViewPager)findViewById(R.id.my_viewpage);
        rl_playground=(RelativeLayout)findViewById(R.id.rl_playground);
        rl_me=(RelativeLayout)findViewById(R.id.rl_me);
        playgroundImg = (ImageView) findViewById(R.id.iv_playground);
        meImg = (ImageView) findViewById(R.id.iv_me);
        // 设置显示文字
        playgroundTv = (TextView) findViewById(R.id.tv_playground);
        meTv = (TextView) findViewById(R.id.tv_me);
        rl_playground.setOnClickListener(this);
        rl_me.setOnClickListener(this);
        playgroundImg.setImageResource(R.mipmap.btn_playground_pre);
        playgroundTv.setTextColor(getResources().getColor(R.color.logo_color));
        meTv.setTextColor(getResources().getColor(R.color.light_gray));
        init();
        UploadWif();//提交收集信息数据
    }
    private void ini_fragment() {
        PlayGroundFragment f1 = new PlayGroundFragment();
        fragmentlist.add(f1);
//        MeFragment f2 = new MeFragment();
        MeFragmentNew f2 = new MeFragmentNew();
        fragmentlist.add(f2);
        my_viewpage.setAdapter(new my_fragment_adpter(
                getSupportFragmentManager()));
        my_viewpage.setCurrentItem(0);
        my_viewpage.setOnPageChangeListener(my_OnPageChangeListener);
        my_viewpage.setOffscreenPageLimit(1);
    }
    // 初始化界面， 检查是否首次登录和用户名以及token是否为空
    protected void init() {
        try {
            // 查看是否存在用户名，密码，以及Token
            String user_name = share.getString("username", "");
            // 如果存在密码，密码要进行AES解密
            String token = share.getString("token", "");
            String deviceId = share.getString("deviceId", "");
            AbSharedUtil abSharedUtil = new AbSharedUtil();
            // 是否首次登陆
//            boolean isFirst = share.getBoolean("isFirst", true);
            boolean isFirst = AbSharedUtil.getBoolean(MainTabActivity.this,"isFirst",true);
            if (isFirst) {
                // 如果是首次登陆，跳转到滚动页面
                Intent intent = new Intent(MainTabActivity.this, ScrollActivity.class);
                startActivity(intent);
                finish();
                // 用户和Token 是否为空，为空跳转到学校信息选择界面
            } else if ("".equals(user_name) || user_name == null
                    || "".equals(token) || token == null) {
                Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                String islogo=getIntent().getStringExtra("islogo");
//                String serverIp = share.getString("serverIp", "");
                Constants.BASE_URL = share.getString("BASE_URL", "");  //默认请求路径
                if ("".equals(Constants.SCHOOL_CODE)) {
                    String school_code = share.getString("schoolCode", "");
                    Constants.SCHOOL_CODE = school_code;
                }
                Constants.BASEPARAMS = "&schoolCode=" + Constants.SCHOOL_CODE + "&clientOSType=android" + "&clientVerNum=" + Constants.SYSVERSION
                        + "&deviceId=" + deviceId +"&role="+share.getInt("ROLE_TYPE",2);
//                        + "&userToken=" + token;
                update();       // 更新文件
                if(islogo==null||islogo.equals("")) {
                    checkUserInfo(user_name);//检查用户登录token是否失效
                }else {
                    ini_fragment();
                }
                // 初始化页面信息, 进入界面首先获取Cookie
                new Thread() {
                    public void run() {
                        String getChkNum = "getChkNum";
                        String param = "rid=" + ReturnUtils.encode(getChkNum) + Constants.BASEPARAMS;
                        JSONObject jsonobj = HttpUtils.httpClientGet(Constants.BASE_URL, param, MainTabActivity.this);
                        try {
                            //为常量赋值
                            Constants.PHPSESSID_VALUE = jsonobj.get(Constants.PHPSESSID).toString();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Intent intentbun = getIntent();
//        if (null != intentbun.getExtras()) {
//            Bundle bundle = getIntent().getExtras();
//            if (bundle != null) {
//                Intent i = new Intent(this, com.toplion.cplusschool.Jpush.NoticeActivity.class);
//                i.putExtras(bundle);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//            }
//        }
    }
    // 查看系统是否需要更新app
    private void update() {
        new Thread() {
            public void run() {
                Map<String, String> postparams = new HashMap<String, String>();
                postparams.put("osType", "android");
                // 获取服务器系统版本号
                JSONObject json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAppUpdateInfo") + Constants.BASEPARAMS, postparams);
                try {
                    json = new JSONObject(json.getString("result"));
                    Log.e("json",json+"");
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        version = json.getString("versionNum");
                        share.put("version",version);
                        Constants.ISBIND = json.getString("isBind");        // 是否强制更新
                        Constants.UPDATE_URL = json.getString("url");       // 获取系统更新路径
                        String noteStr = json.getString("note").toString();
                        if (!noteStr.trim().equals("") && !"".equals(noteStr.trim()) && noteStr.length() > 0) {
                            Constants.UPDATE_CONTENT = json.getString("note");   //更新内容
                        } else {
                            if (Constants.ISBIND.equals("true")) {
                                Constants.UPDATE_CONTENT = "系统版本需强制更新，请升级!";
                            } else {
                                Constants.UPDATE_CONTENT = "系统版本已升级，请选择是否升级!";
                            }
                        }
                        Message msg = new Message();
                        msg.what = 5;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 7;
                        msg.obj = json.getString("msg");
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    // 获取系统版本号失败，出现异常
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 6;
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }
    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        // 检查Cookie是否为空，为空时重新获取Cookie
                        if ("".equals(Constants.PHPSESSID_VALUE) || Constants.PHPSESSID_VALUE == null) {
                            new Thread() {
                                @Override
                                public void run() {
                                    String getChkNum = "getChkNum";
                                    String param = "rid=" + ReturnUtils.encode(getChkNum) + Constants.BASEPARAMS;
                                    JSONObject jsonobj = HttpUtils.httpClientGet(Constants.BASE_URL, param, MainTabActivity.this);
                                    try {
                                        // 为常量赋值
                                        Constants.PHPSESSID_VALUE = jsonobj.get(Constants.PHPSESSID).toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Message msg = new Message();
                                        msg.what = 6;
                                        handler.sendMessage(msg);
                                    }
                                }
                            }.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastManager.getInstance().showToast(MainTabActivity.this, Constants.NETWORK_ERROR);
                    }
                    break;
                case 5:
                    Log.d("sysVersion========", sysVersion);
                    Log.d("version========", version);
                    if (!version.isEmpty()) {
                        if (Integer.parseInt(version.replace(".", "")) > Integer.parseInt(sysVersion.replace(".", ""))) {
                            // 更新
                            if (Constants.UPDATE_URL.contains("http:")
                                    || Constants.UPDATE_URL.contains("https:")) {
                                UpdateVersion versionUp = new UpdateVersion(MainTabActivity.this);
                                versionUp.checkUpdate();
                            } else
                                ToastManager.getInstance().showToast(MainTabActivity.this, "下载文件路径出现异常!");
                        }
                    }
                    break;
                case 6:
                    ToastManager.getInstance().showToast(MainTabActivity.this, Constants.NETWORK_ERROR);
                    break;
                case 7:
                    String str = (String) msg.obj;
                    ToastManager.getInstance().showToast(MainTabActivity.this, str);
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_playground:
                playgroundImg.setImageResource(R.mipmap.btn_playground_pre);
                playgroundTv.setTextColor(getResources().getColor(R.color.logo_color));
                // 设置未选中图片
                meImg.setImageResource(R.mipmap.btn_my_nor);
                // 设置未选中字体颜色
                meTv.setTextColor(getResources().getColor(R.color.light_gray));
                my_viewpage.setCurrentItem(0, true);
                break;
            case R.id.rl_me:
                // 设置底部未选中tab变化
                playgroundImg.setImageResource(R.mipmap.btn_playground_nor);
                playgroundTv.setTextColor(getResources().getColor( R.color.light_gray));
                // 设置底部选中tab变化
                meImg.setImageResource(R.mipmap.btn_my_pre);
                meTv.setTextColor(getResources().getColor(R.color.logo_color));
                my_viewpage.setCurrentItem(1, true);
                break;
            default:
                break;
        }

    }

    private class my_fragment_adpter extends FragmentPagerAdapter {

        public my_fragment_adpter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentlist.get(arg0);
        }

        @Override
        public int getCount() {

            return fragmentlist.size();
        }
    }

    ViewPager.OnPageChangeListener my_OnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    rl_playground.performClick();
                    break;
                case 1:
                    rl_me.performClick();
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private void checkUserInfo(String userName){
        if (ConnectivityUtils.isNetworkAvailable(this)) {//有网络，进行自动登录
            UploadWif();//提交收集信息数据
            MobclickAgent.onEvent(MainTabActivity.this, "login");//友盟统计
            String url = Constants.NEWBASE_URL + "?rid=" + ReturnUtils.encode("queryUserInfo") + Constants.BASEPARAMS;
            AbRequestParams params = new AbRequestParams();
            //信息需要Base64进行加密
            params.put("username", ReturnUtils.encode(userName));  //用户名
            params.put("token", ReturnUtils.encode(share.getString("token", ""))); //Token加密
            abHttpUtil.post(url, params, new CallBackParent(MainTabActivity.this, "菜单加载中") {
                @Override
                public void Get_Result(String content) {
                    try {
                        JSONObject json = new JSONObject(content);
                        String code = json.getString("code");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            JSONArray roleInfo = new JSONArray(Function.getInstance().getString(json,"roleInfo"));
                            if(roleInfo!=null&&roleInfo.length()>0){//保存用户个人信息（签到等。。）
                                JSONObject joRoleInfo = roleInfo.getJSONObject(0);
                                share.put("ROLE_USERNAME",Function.getInstance().getString(joRoleInfo,"ROLE_USERNAME"));//姓名
                                share.put("XB",Function.getInstance().getString(joRoleInfo,"XB"));//性别
                                share.put("NICKNAME",Function.getInstance().getString(joRoleInfo,"NICKNAME"));//昵称
                                share.put("ROLE_ID",Function.getInstance().getString(joRoleInfo,"ROLE_ID"));//学号或者工号
                                share.put("CSRQ",Function.getInstance().getString(joRoleInfo,"CSRQ"));//出生年月
                                share.put("SJH",Function.getInstance().getString(joRoleInfo,"SJH"));//手机号
                                share.put("HEADIMAGE",Function.getInstance().getString(joRoleInfo,"HEADIMAGE"));//头像
                                share.put("SBIFLOWERSNUMBER",Function.getInstance().getInteger(joRoleInfo,"SBIFLOWERSNUMBER"));//鲜花数量
                                share.put("SBICONSECUTIVEDAYS",Function.getInstance().getInteger(joRoleInfo,"SBICONSECUTIVEDAYS"));//签到日期
                                share.put("STATUS",Function.getInstance().getInteger(joRoleInfo,"STATUS"));//今天签到状态 0表示没有签到 1表示已签到
                                share.put("ZYMC",Function.getInstance().getString(joRoleInfo,"ZYMC"));//专业名

                                share.put("ADDRESS",Function.getInstance().getString(joRoleInfo,"ADDRESS"));//住址
                                share.put("ZYJSZWM",Function.getInstance().getString(joRoleInfo,"ZYJSZWM"));//技术职称

                                share.put("SENIORNAME",Function.getInstance().getString(joRoleInfo,"SENIORNAME"));//部门
                                share.put("DEPARTNAME",Function.getInstance().getString(joRoleInfo,"DEPARTNAME"));//职位名

                            }
                            json = new JSONObject(json.getString("data"));
                            share.put("samUserInfo", json.getString("samUserInfo") + "");
                            share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                            share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                            share.put("menujson",content);
                            ini_fragment();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 重新登录
                        Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void Get_Result_faile(String errormsg) {
                    super.Get_Result_faile(errormsg);
                    // 重新登录
                    Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    super.onFailure(statusCode, content, error);
                    // 重新登录
                    Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            // 重新登录
            Intent intent = new Intent(MainTabActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * 将wif信道的信息传入后台
     */
    public void UploadWif() {
            AbRequestParams params = new AbRequestParams();
            params.put("deviceInfo", CommonUtil.getCurrentChannel(MainTabActivity.this).toString());
            params.put("deviceId", share.getString("deviceId", ""));
            params.put("deviceTeleNum", new TelephoneUtils(MainTabActivity.this).getPhoneNumber());
            String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("saveDeviceInfo") + Constants.BASEPARAMS;
            abHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(int statusCode, String content) {
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {

                }

                @Override
                public void onFinish() {
                }
            });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    long waitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - waitTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                ToastManager.getInstance().showToast(this, "再按一次退出程序");
                waitTime = secondTime;//更新firstTime
                return true;
            } else {                                                    //两次按键小于2秒时，退出应用
                AbActivityManager.getInstance().finishAllActivity(null);
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
