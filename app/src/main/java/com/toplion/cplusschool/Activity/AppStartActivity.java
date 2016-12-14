package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 *
 * @author WangShengbo
 * @created 2016-2-29
 */
public class AppStartActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.start);
        SharePreferenceUtils share = new SharePreferenceUtils(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 屏幕的高度和宽度保存到本地缓存
        BaseApplication.ScreenWidth = screenWidth;
        BaseApplication.ScreenHeight = screenHeight;

        share.put("width", screenWidth);
        share.put("height", screenHeight);
        // 设置描述关闭
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectTo();
            }
        }, 500);
    }

    /**
     * 跳转到主页（已解决6.0系统自动为bundle添加null的问题）
     */
    private void redirectTo() {
        boolean isopen = false;
        Intent intentbun = getIntent();
        if (null != intentbun.getExtras()) {
            Bundle bundle = getIntent().getExtras();
            for (String key : bundle.keySet()) {
                if (bundle.getString(key) == null) {
                    isopen = false;
                } else {
                    isopen = true;
                    break;
                }
            }
            Intent intent = new Intent();
            intent.setClass(this, MainTabActivity.class);
            if (isopen) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainTabActivity.class);
            startActivity(intent);
            finish();
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