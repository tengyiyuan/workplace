package com.toplion.cplusschool.Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FeekBackActivity
 * <p/>
 * author: wangshengbo
 * <p/>
 * date :2016-9-7
 */

public class FeekBackActivity extends BaseActivity {
    private ImageView iv_feek_back_return;//返回键
    private EditText et_add_suggest;
    private EditText et_feekback_phone;//电话
    private Button tv_suggestion_confirm;
    private AbHttpUtil abHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feek_back_activity);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_feek_back_return = (ImageView) findViewById(R.id.iv_feek_back_return);
        et_add_suggest = (EditText) findViewById(R.id.et_add_suggest);
        et_feekback_phone = (EditText) findViewById(R.id.et_feekback_phone);
        tv_suggestion_confirm = (Button) findViewById(R.id.tv_suggestion_confirm);
        setListener();
    }

    //提交意见
    private void confirm(String con, String phone) {
        SharePreferenceUtils share = new SharePreferenceUtils(this);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("addFeedbackInfo") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("content", con);
        params.put("phone", phone);
        params.put("account", share.getString("username", ""));
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading), "addFeedbackInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    ToastManager.getInstance().showToast(FeekBackActivity.this, jsonObject.getString("msg"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setListener() {
        iv_feek_back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_suggestion_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_add_suggest.getText().toString())) {
                    confirm(et_add_suggest.getText().toString(), et_feekback_phone.getText().toString());
                } else {
                    ToastManager.getInstance().showToast(FeekBackActivity.this, "请您留下宝贵的意见");
                }
            }
        });
    }
}
