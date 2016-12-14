package com.toplion.cplusschool.PhotoWall;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;

/**
 * Created by wang
 * on 2016/11/11.
 * @照片发布
 */

public class PhotoReleaseActivity extends BaseActivity{
    private ImageView iv_phone_release_return;
    private ImageView iv_photo_release_show;//show
    private Button bt_photo_release_confirm;//确认发布
    private Button bt_photo_release_reselect;//重新选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_release);
        init();
    }

    @Override
    protected void init() {
        super.init();
        iv_phone_release_return = (ImageView) findViewById(R.id.iv_phone_release_return);
        iv_photo_release_show = (ImageView) findViewById(R.id.iv_photo_release_show);
        bt_photo_release_confirm = (Button) findViewById(R.id.bt_photo_release_confirm);
        bt_photo_release_reselect = (Button) findViewById(R.id.bt_photo_release_confirm);
        setListener();
    }

    @Override
    protected void setListener() {
        super.setListener();
        //确认发布
        bt_photo_release_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //重新选择
        bt_photo_release_reselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_phone_release_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
