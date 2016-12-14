package com.toplion.cplusschool.PhotoWall;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;


import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;
/**
 * Created by toplion on 2016/11/2.
 */
public class PhotoContent extends BaseActivity {
    private ImageView myphoto;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photocontent);
        init();
        getData();
    }
    @Override
    protected void init() {
        super.init();
        myphoto = (ImageView) findViewById(R.id.myphoto);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = AbImageUtil.getBitmap(getIntent().getStringExtra("imagePath"));
                PhotoContent.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null)
                            myphoto.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }
}
