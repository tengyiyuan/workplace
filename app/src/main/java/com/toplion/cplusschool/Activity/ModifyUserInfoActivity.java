package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toplion.cplusschool.R;

/**
 * Created by wang
 * on 2016/11/10.
 * @修改用户信息
 */

public class ModifyUserInfoActivity extends BaseActivity{
    private ImageView iv_modify_return;
    private TextView tv_modify_title;//
    private EditText et_modify_content;
    private LinearLayout ll_modify_sex;//性别选择
    private RelativeLayout rl_modify_boy;//男
    private ImageView iv_modify_boy;
    private RelativeLayout rl_modify_girl;//女
    private ImageView iv_modify_girl;
    private RelativeLayout rl_modify_baomi;//保密
    private ImageView iv_modify_baomi;
    private int type = 0;
    private int sex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user_info);
        init();
    }

    @Override
    protected void init() {
        super.init();
        type = getIntent().getIntExtra("type",0);
        iv_modify_return = (ImageView) findViewById(R.id.iv_modify_return);
        tv_modify_title = (TextView) findViewById(R.id.tv_modify_title);
        et_modify_content = (EditText) findViewById(R.id.et_modify_content);
        ll_modify_sex = (LinearLayout) findViewById(R.id.ll_modify_sex);
        rl_modify_boy = (RelativeLayout) findViewById(R.id.rl_modify_boy);
        rl_modify_girl = (RelativeLayout) findViewById(R.id.rl_modify_girl);
        rl_modify_baomi = (RelativeLayout) findViewById(R.id.rl_modify_baomi);
        iv_modify_boy = (ImageView) findViewById(R.id.iv_modify_boy);
        iv_modify_girl = (ImageView) findViewById(R.id.iv_modify_girl);
        iv_modify_baomi = (ImageView) findViewById(R.id.iv_modify_baomi);

        if(type == 1){
            tv_modify_title.setText("修改昵称");
            et_modify_content.setHint("昵称");
            et_modify_content.setVisibility(View.VISIBLE);
            ll_modify_sex.setVisibility(View.GONE);
        }else if(type == 2){
            tv_modify_title.setText("修改性别");
            et_modify_content.setVisibility(View.GONE);
            ll_modify_sex.setVisibility(View.VISIBLE);
        }else if(type == 3){
            tv_modify_title.setText("修改手机号");
            et_modify_content.setHint("手机号");
            et_modify_content.setVisibility(View.VISIBLE);
            ll_modify_sex.setVisibility(View.GONE);
        }else if(type == 4){
            tv_modify_title.setText("修改住址");
            et_modify_content.setHint("住址");
            et_modify_content.setVisibility(View.VISIBLE);
            ll_modify_sex.setVisibility(View.GONE);
        }
        setListener();
    }

    @Override
    protected void setListener() {
        super.setListener();
        rl_modify_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(1);
                sex = 1;
            }
        });
        rl_modify_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(2);
                sex = 2;
            }
        });
        rl_modify_baomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(3);
                sex = 3;
            }
        });
        iv_modify_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    private void setSelect(int i){
        switch (i){
            case 1:
                iv_modify_boy.setVisibility(View.VISIBLE);
                iv_modify_girl.setVisibility(View.GONE);
                iv_modify_baomi.setVisibility(View.GONE);
                break;
            case 2:
                iv_modify_boy.setVisibility(View.GONE);
                iv_modify_girl.setVisibility(View.VISIBLE);
                iv_modify_baomi.setVisibility(View.GONE);
                break;
            case 3:
                iv_modify_boy.setVisibility(View.GONE);
                iv_modify_girl.setVisibility(View.GONE);
                iv_modify_baomi.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}
