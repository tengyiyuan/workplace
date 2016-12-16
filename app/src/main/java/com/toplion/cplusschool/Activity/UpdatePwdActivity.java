package com.toplion.cplusschool.Activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.AESUtils;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustEditTextLRTB;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdatePwdActivity extends BaseActivity {
    private ImageView signin_return;
    private CustEditTextLRTB edit_oldpwd;
    private CustEditTextLRTB edit_pwd;
    private CustEditTextLRTB edit_repwd;
    private Button sumbit;
    private AbHttpUtil mAbHttpUtil = null;
    private SharePreferenceUtils share;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.update_password);
        init();
    }

    /**
     * 初始化组件并优化在没有输入或者两次输入密码不对的情况下按钮变色
     */
    @Override
    protected void init() {
        super.init();
        signin_return = (ImageView) findViewById(R.id.signin_return);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        edit_oldpwd = (CustEditTextLRTB) findViewById(R.id.edit_oldpwd);
        edit_pwd = (CustEditTextLRTB) findViewById(R.id.edit_pwd);
        edit_repwd = (CustEditTextLRTB) findViewById(R.id.edit_repwd);
        sumbit = (Button) findViewById(R.id.sumbit);
        sumbit.setEnabled(false);
        edit_oldpwd.setOnTextChangeListener(new CustEditTextLRTB.OnTextChangeListener() {
            @Override
            public void OnTextChange(CharSequence ss) {
                if (ss.length() > 0
                        && edit_pwd.getText().toString().trim().length() > 0 && edit_repwd.getText().toString().trim().length() > 0)
                    sumbit.setEnabled(true);
                else sumbit.setEnabled(false);
            }
        });
        edit_pwd.setOnTextChangeListener(new CustEditTextLRTB.OnTextChangeListener() {
            @Override
            public void OnTextChange(CharSequence ss) {
                if (ss.length() > 2
                        && edit_oldpwd.getText().toString().trim().length() > 0 && edit_repwd.getText().toString().trim().length() > 0)
                    sumbit.setEnabled(true);
                else sumbit.setEnabled(false);
            }
        });
        edit_repwd.setOnTextChangeListener(new CustEditTextLRTB.OnTextChangeListener() {
            @Override
            public void OnTextChange(CharSequence ss) {
                if (ss.length() > 2
                        && edit_oldpwd.getText().toString().trim().length() > 0 && edit_pwd.getText().toString().trim().length() > 0)
                    sumbit.setEnabled(true);
                else sumbit.setEnabled(false);
            }
        });

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_pwd.getText().toString().trim().equals(edit_repwd.getText().toString().trim())) {
                    ToastManager.getInstance().showToast(UpdatePwdActivity.this, "两次密码输入不一致,请重新输入");
                    edit_pwd.getcontent().setText("");
                    edit_repwd.getcontent().setText("");
                    return;
                }
                if(edit_pwd.getText().toString().trim().equals(edit_oldpwd.getText().toString().trim())){
                    ToastManager.getInstance().showToast(UpdatePwdActivity.this, "与原密码相同,请重新输入");
                    edit_pwd.getcontent().setText("");
                    edit_repwd.getcontent().setText("");
                    return;
                }
                getData();
            }
        });
        signin_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("username", ""));
        params.put("oldpassword", edit_oldpwd.getText().toString().trim());
        params.put("newpassword", edit_repwd.getText().toString().trim());
        params.put("schoolCode", share.getString("schoolCode", ""));
        params.put("clientOSType", "android");
        params.put("clientVerNum", Constants.SYSVERSION + "");
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updateUserPassword") + Constants.BASEPARAMS;
        mAbHttpUtil.post(url, params,new CallBackParent(UpdatePwdActivity.this,"正在修改"){
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result.toString());
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
//                        ToastManager.getInstance().showToast(UpdatePwdActivity.this, msg);
                        CommonUtil.intoLogin(UpdatePwdActivity.this,share,msg);
                    } else {
                        ToastManager.getInstance().showToast(UpdatePwdActivity.this, msg);
                        AbDialogUtil.removeDialog(UpdatePwdActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AbDialogUtil.removeDialog(UpdatePwdActivity.this);
                }
            }
        });
    }
}
