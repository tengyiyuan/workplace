package com.toplion.cplusschool.PhotoWall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Window;

import com.toplion.cplusschool.Activity.PersonInfoActivity;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.SecondMarket.MainMarket;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;


/**
 * Created by toplion on 2016/10/19.
 */
public class PhotoMainActivity extends FragmentActivity {
    private CommDialog dialog;
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);
        share = new SharePreferenceUtils(PhotoMainActivity.this);
        String nickName = share.getString("NICKNAME", "");
        String headIcon = share.getString("HEADIMAGE", "");
        if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(headIcon)) {
            String strmsg = "请先设置昵称和头像后使用！";
            dialog = new CommDialog(PhotoMainActivity.this);
            dialog.CreateDialog("确定", "系统提示", strmsg, PhotoMainActivity.this, new CommDialog.CallBack() {
                @Override
                public void isConfirm(boolean flag) {
                    if (flag) {
                        Intent intent = new Intent(PhotoMainActivity.this, PersonInfoActivity.class);
                        startActivityForResult(intent, 133);
                    } else {
                        finish();
                    }
                }
            });
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CardFragment())
                    .commitAllowingStateLoss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String nickName = share.getString("NICKNAME", "");
            String headIcon = share.getString("HEADIMAGE", "");
            if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(headIcon)) {
                if(dialog!=null){
                    dialog.cancelDialog();
                }
            }
        }
    }
}
