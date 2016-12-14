package com.toplion.cplusschool.PhotoWall;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.toplion.cplusschool.R;


/**
 * Created by toplion on 2016/10/19.
 */
public class PhotoMainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CardFragment())
                    .commitAllowingStateLoss();
        }
    }


}
