package com.toplion.cplusschool.Wage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.ParentWaya;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

/**
 * 绑定工资账号的页面
 */

public class Bound extends BaseActivity {
    private ImageView back;
    private EditText wayaname;
    private EditText wayapassword;
    private TextView bound;
    private String wName;//用户名
    private String wJob;//职位
    private String wDepart;//部门
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bound);
        init();
    }
    /**
     * 初始化布局
     */
    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        back = (ImageView) findViewById(R.id.back);
        wayaname = (EditText) findViewById(R.id.wayaname);
        wayapassword = (EditText) findViewById(R.id.wayapassword);
        bound = (TextView) findViewById(R.id.bound);
        bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = wayaname.getText().toString().trim();
                String password = wayapassword.getText().toString().trim();
                if (userId.equals("") || password.equals("")) {
                    ToastManager.getInstance().showToast(Bound.this, "工资号和密码都不能为空");
                    return;
                }
                getData(userId, password);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 数据加载
     */
    protected void getData(String userId, String password) {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("userid", userId);
        params.put("password", password);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("validateUserInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在绑定账号") {
            @Override
            public void Get_Result(String result) {
                ParentWaya wayalist = AbJsonUtil.fromJson(result, ParentWaya.class);
                share.put("wAccount",wayaname.getText().toString().trim());
                share.put("wPwd",wayapassword.getText().toString().trim());
                for (int i = 0; i < wayalist.getData().size(); i++) {
                    if (wayalist.getData().get(i).getName().contains("用户")) {
                        wName = wayalist.getData().get(i).getValue();
                        ToastManager.getInstance().showToast(Bound.this, wayalist.getData().get(i).getValue() + "绑定成功");
                    }else if(wayalist.getData().get(i).getName().contains("权限")){
                        wJob = wayalist.getData().get(i).getValue();
                    }else if(wayalist.getData().get(i).getName().contains("部门")){
                        wDepart = wayalist.getData().get(i).getValue();
                    }
                }
                share.put("wName",wName);
                share.put("wJob",wJob);
                share.put("wDepart",wDepart);
                Intent intent = new Intent(Bound.this,SelectTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
