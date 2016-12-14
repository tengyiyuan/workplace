package com.toplion.cplusschool.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.global.AbActivityManager;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Fragment.MeFragment;
import com.toplion.cplusschool.Fragment.PlayGroundFragment;
import com.toplion.cplusschool.Jpush.NoticeActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Update.UpdateVersion;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TelephoneUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录后的主页面显示
 * 首页 显示用户的基本信息
 * Banner，缴费，报修，一键上网，断开网络，校园广播
 * 设置 显示用户的设置信息
 * 我的订单，我的报修，使用帮助，关于本软件，软件升级，退出登录
 *
 * @author liyb
 * @version 1.0
 */
public class MainFrameActivity extends BaseActivity implements OnClickListener {
    private SharePreferenceUtils share;
    // 底部Tab切换使用
    private RelativeLayout playgroundLayout, meLayout;         // tab切换布局
    private Fragment playgroundFragment, meFragment, currentFragment;// 底部标签切换的Fragment 首页，设置，当前
    private ImageView playgroundImg, meImg;                    // 底部标签图片 首页，设置
    private TextView playgroundTv, meTv;                       // 底部标签的文本 首页，设置
    private String sysVersion = "";
    private String version = "";
    private AbHttpUtil abHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);
        MobclickAgent.openActivityDurationTrack(false);
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        sysVersion = ConnectivityUtils.getAppVersionName(MainFrameActivity.this);
        Constants.SYSVERSION = sysVersion;
        init();         // 初始化界面， 检查是否首次登录和用户名以及token是否为空
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
                                    JSONObject jsonobj = HttpUtils.httpClientGet(Constants.BASE_URL, param, MainFrameActivity.this);
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
                        Toast.makeText(MainFrameActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
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
                                UpdateVersion versionUp = new UpdateVersion(MainFrameActivity.this);
                                versionUp.checkUpdate();
                            } else
                                Toast.makeText(MainFrameActivity.this, "下载文件路径出现异常!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 6:
                    Toast.makeText(MainFrameActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    String str = (String) msg.obj;
                    Toast.makeText(MainFrameActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    // 初始化界面， 检查是否首次登录和用户名以及token是否为空
    protected void init() {
        try {
            // 查看是否存在用户名，密码，以及Token
            String user_name = share.getString("username", "");
            // 如果存在密码，密码要进行AES解密
            String token = share.getString("token", "");
            String deviceId = share.getString("deviceId", "");
            // 是否首次登陆
            boolean isFirst = share.getBoolean("isFirst", true);
            if (isFirst) {
                // 如果是首次登陆，跳转到滚动页面
                Intent intent = new Intent(MainFrameActivity.this, ScrollActivity.class);
                startActivity(intent);
                finish();
                // 用户和Token 是否为空，为空跳转到学校信息选择界面
            } else if ("".equals(user_name) || user_name == null
                    || "".equals(token) || token == null) {
                Intent intent = new Intent(MainFrameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
//                String serverIp = share.getString("serverIp", "");
                Constants.BASE_URL = share.getString("BASE_URL", "");  //默认请求路径
                if ("".equals(Constants.SCHOOL_CODE)) {
                    String school_code = share.getString("schoolCode", "");
                    Constants.SCHOOL_CODE = school_code;
                }
                Constants.BASEPARAMS = "&schoolCode=" + Constants.SCHOOL_CODE + "&clientOSType=android" + "&clientVerNum=" + Constants.SYSVERSION
                        + "&deviceId=" + deviceId +"&role="+share.getInt("ROLE_TYPE",2);
//                        + "&userToken=" + token;
                initUI();       // 初始化UI
                update();       // 更新文件
                checkUserInfo(user_name);//检查用户登录token是否失效
                // 初始化页面信息, 进入界面首先获取Cookie
                new Thread() {
                    public void run() {
                        String getChkNum = "getChkNum";
                        String param = "rid=" + ReturnUtils.encode(getChkNum) + Constants.BASEPARAMS;
                        JSONObject jsonobj = HttpUtils.httpClientGet(Constants.BASE_URL, param, MainFrameActivity.this);
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
        Intent intentbun = getIntent();
        if (null != intentbun.getExtras()) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Intent i = new Intent(this, NoticeActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
    }

    //获取用户信息
    private void checkUserInfo(String userName){
        if (ConnectivityUtils.isNetworkAvailable(this)) {//有网络，进行自动登录
            UploadWif();//提交收集信息数据
            MobclickAgent.onEvent(MainFrameActivity.this, "login");//友盟统计
            String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("queryUserInfo") + Constants.BASEPARAMS;
            AbRequestParams params = new AbRequestParams();
            //信息需要Base64进行加密
            params.put("username", ReturnUtils.encode(userName));  //用户名
            params.put("token", ReturnUtils.encode(share.getString("token", ""))); //Token加密
            abHttpUtil.post(url, params, new CallBackParent(MainFrameActivity.this, "正在加载中...") {
                @Override
                public void Get_Result(String content) {
                    try {
                        JSONObject json = new JSONObject(content);
                        Log.e("content",content+"");
                        String code = json.getString("code");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            json = new JSONObject(json.getString("data"));
                            share.put("samUserInfo", json.getString("samUserInfo") + "");
                            share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                            share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                            share.put("menujson",content);
                            initTab(); //如果已经登录过 加载首页 "操场"和"设置"(初始化底部TAB)
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 重新登录
                        Intent intent = new Intent(MainFrameActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void Get_Result_faile(String errormsg) {
                    super.Get_Result_faile(errormsg);
                    // 重新登录
                    Intent intent = new Intent(MainFrameActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    super.onFailure(statusCode, content, error);
                    // 重新登录
                    Intent intent = new Intent(MainFrameActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            // 重新登录
            Intent intent = new Intent(MainFrameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * 初始化UI ，设置相应的事件
     *
     * @author liyb
     */
    private void initUI() {
        playgroundLayout = (RelativeLayout) findViewById(R.id.rl_playground);
        meLayout = (RelativeLayout) findViewById(R.id.rl_me);
        // 设置点击事件
        playgroundLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);
        // 设置图片
        playgroundImg = (ImageView) findViewById(R.id.iv_playground);
        meImg = (ImageView) findViewById(R.id.iv_me);
        // 设置显示文字
        playgroundTv = (TextView) findViewById(R.id.tv_playground);
        // 设置选中字体颜色
        playgroundTv.setTextColor(getResources().getColor(R.color.logo_color));
        meTv = (TextView) findViewById(R.id.tv_me);
    }

    /**
     * 初始化底部标签，默认为显示首页
     */
    private void initTab() {
        if (playgroundFragment == null) {
            playgroundFragment = new PlayGroundFragment();
        }

        if (!playgroundFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout, playgroundFragment).commit();

            // 记录当前Fragment
            currentFragment = playgroundFragment;
            // 设置选中图片文本的变化
            playgroundImg.setImageResource(R.mipmap.btn_playground_pre);
            // 设置选中字体颜色
            playgroundTv.setTextColor(getResources().getColor(
                    R.color.logo_color));
            // 设置未选中图片
            meImg.setImageResource(R.mipmap.btn_my_nor);
            // 设置未选中字体颜色
            meTv.setTextColor(getResources().getColor(R.color.light_gray));

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_playground:
                // 点击切换显示首页
                clickTab1Layout();
                break;
            case R.id.rl_me:
                // 点击切换显示设置
                clickTab3Layout();
                break;
            default:
                break;
        }
    }

    /**
     * 点击显示首页
     */
    private void clickTab1Layout() {
        if (playgroundFragment == null) {
            playgroundFragment = new PlayGroundFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),
                playgroundFragment);

        // 设置底部选中tab变化
        playgroundImg.setImageResource(R.mipmap.btn_playground_pre);
        playgroundTv.setTextColor(getResources().getColor(
                R.color.logo_color));
        // 设置底部未选中tab变化
        meImg.setImageResource(R.mipmap.btn_my_nor);
        meTv.setTextColor(getResources().getColor(R.color.light_gray));
    }

    /**
     * 点击 显示设置
     */
    private void clickTab3Layout() {
        if (meFragment == null) {
            meFragment = new MeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),meFragment);
        // 设置底部未选中tab变化
        playgroundImg.setImageResource(R.mipmap.btn_playground_nor);
        playgroundTv.setTextColor(getResources().getColor( R.color.light_gray));
        // 设置底部选中tab变化
        meImg.setImageResource(R.mipmap.btn_my_pre);
        meTv.setTextColor(getResources().getColor(R.color.logo_color));
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) {
            // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
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
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

    /**
     * 将wif信道的信息传入后台
     */
    public void UploadWif() {
        if (ConnectivityUtils.isNetworkAvailable(MainFrameActivity.this)) {
            AbRequestParams params = new AbRequestParams();
            params.put("deviceInfo", CommonUtil.getCurrentChannel(MainFrameActivity.this).toString());
            params.put("deviceId", share.getString("deviceId", ""));
            params.put("deviceTeleNum", new TelephoneUtils(MainFrameActivity.this).getPhoneNumber());
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
    }
}

