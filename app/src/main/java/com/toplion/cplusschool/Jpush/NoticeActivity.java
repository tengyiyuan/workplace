package com.toplion.cplusschool.Jpush;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;

import cn.jpush.android.api.JPushInterface;

public class NoticeActivity extends BaseActivity {
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.notice);
        content = (TextView) findViewById(R.id.content);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String contentstr = bundle.getString(JPushInterface.EXTRA_ALERT);
            content.setText(contentstr);
        }
    }
}
