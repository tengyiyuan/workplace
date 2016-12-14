package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.Utils.Tools;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wang
 * on 2016/11/10.
 *
 * @修改用户信息
 */

public class ModifyUserInfoActivity extends BaseActivity {
    private ImageView iv_modify_return;//返回键
    private TextView tv_modify_title;//标题
    private TextView tv_modify_save;//保存
    private EditText et_modify_content;
    private LinearLayout ll_modify_sex;//性别选择
    private RelativeLayout rl_modify_boy;//男生
    private ImageView iv_modify_boy;
    private RelativeLayout rl_modify_girl;//女士
    private ImageView iv_modify_girl;
    private int type = 0;
    private String updateKey = "";//要修改的字段
    private String sex = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user_info);
        init();
    }

    @Override
    protected void init() {
        super.init();
        type = getIntent().getIntExtra("type", 0);
        iv_modify_return = (ImageView) findViewById(R.id.iv_modify_return);
        tv_modify_title = (TextView) findViewById(R.id.tv_modify_title);
        et_modify_content = (EditText) findViewById(R.id.et_modify_content);
        tv_modify_save = (TextView) findViewById(R.id.tv_modify_save);
        ll_modify_sex = (LinearLayout) findViewById(R.id.ll_modify_sex);
        rl_modify_boy = (RelativeLayout) findViewById(R.id.rl_modify_boy);
        iv_modify_boy = (ImageView) findViewById(R.id.iv_modify_boy);
        rl_modify_girl = (RelativeLayout) findViewById(R.id.rl_modify_girl);
        iv_modify_girl = (ImageView) findViewById(R.id.iv_modify_girl);
        et_modify_content.setText(getIntent().getStringExtra("content"));
        setModifyTitle(type);
        setListener();
    }

    /**
     * 设置提示内容
     *
     * @param i
     */
    private void setModifyTitle(int i) {
        switch (i) {
            case 1:
                tv_modify_title.setText("修改昵称");
                et_modify_content.setHint("昵称(1-10个字符)");
                et_modify_content.setVisibility(View.VISIBLE);
                et_modify_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                ll_modify_sex.setVisibility(View.GONE);
                updateKey = "nickname";
                break;
            case 2:
                tv_modify_title.setText("修改性别");
                et_modify_content.setVisibility(View.GONE);
                ll_modify_sex.setVisibility(View.VISIBLE);
                updateKey = "sex";
                break;
            case 3:
                tv_modify_title.setText("修改手机号");
                et_modify_content.setHint("手机号");
                et_modify_content.setVisibility(View.VISIBLE);
                ll_modify_sex.setVisibility(View.GONE);
                updateKey = "phone";
                break;
            case 4:
                tv_modify_title.setText("修改故乡");
                et_modify_content.setHint("故乡");
                et_modify_content.setVisibility(View.VISIBLE);
                ll_modify_sex.setVisibility(View.GONE);
                updateKey = "hometown";
                break;
            case 5:
                tv_modify_title.setText("修改所在地");
                et_modify_content.setHint("现居住地");
                et_modify_content.setVisibility(View.VISIBLE);
                ll_modify_sex.setVisibility(View.GONE);
                updateKey = "xzz";
                break;
            case 6:
                updateKey = "personnote";
                tv_modify_title.setText("修改个人说明");
                et_modify_content.setHint("个人说明(1-30个字符)");
                et_modify_content.setVisibility(View.VISIBLE);
                et_modify_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                ll_modify_sex.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 修改信息
     *
     * @param key   要修改的字段
     * @param value 修改的值
     */
    private void updateUserInfo(String key, String value) {
        AbHttpUtil abHttpUtil = AbHttpUtil.getInstance(this);
        final SharePreferenceUtils share = new SharePreferenceUtils(this);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updateUserInfoByInput") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("username", share.getString("ROLE_ID", ""));
        params.put(key, value);
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (jsonArray != null && jsonArray.length() > 0) {
                        jsonObject = jsonArray.getJSONObject(0);
                        if (updateKey.equals("nickname")) {
                            share.put("NICKNAME", Function.getInstance().getString(jsonObject, "NC"));
                        } else if (updateKey.equals("phone")) {
                            share.put("SJH", Function.getInstance().getString(jsonObject, "SJ"));
                        } else if (updateKey.equals("hometown")) {
                            share.put("SJH", Function.getInstance().getString(jsonObject, "SJ"));
                        } else if (updateKey.equals("personnote")) {
                            share.put("GRSM", Function.getInstance().getString(jsonObject, "GRSM"));
                        } else if (updateKey.equals("sex")) {
                            share.put("XB", Function.getInstance().getString(jsonObject, "XBM"));
                        }
                        ToastManager.getInstance().showToast(ModifyUserInfoActivity.this, "修改成功");
                        setResult(RESULT_OK);
                        Tools.HideKeyboard(et_modify_content);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        rl_modify_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSex(1);
            }
        });
        rl_modify_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSex(2);
            }
        });
        tv_modify_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateKey.equals("sex")) {
                    updateUserInfo(updateKey, sex);
                } else {
                    if (!TextUtils.isEmpty(et_modify_content.getText().toString().trim())) {
                        if (updateKey.equals("phone")) {
                            if (!StringUtils.isMobile(et_modify_content.getText().toString().trim())) {
                                ToastManager.getInstance().showToast(ModifyUserInfoActivity.this, "输入的手机号不正确，请重新填写");
                                return;
                            }
                        }
                        updateUserInfo(updateKey, et_modify_content.getText().toString());
                    } else {
                        ToastManager.getInstance().showToast(ModifyUserInfoActivity.this, "输入的内容为空，请重新填写");
                    }
                }
            }
        });
        iv_modify_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.HideKeyboard(et_modify_content);
                finish();
            }
        });
    }

    private void setSex(int i) {
        switch (i) {
            case 1:
                iv_modify_boy.setVisibility(View.VISIBLE);
                iv_modify_girl.setVisibility(View.GONE);
                sex = "1";
                break;
            case 2:
                iv_modify_boy.setVisibility(View.GONE);
                iv_modify_girl.setVisibility(View.VISIBLE);
                sex = "2";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
