package com.toplion.cplusschool.Wage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toplion
 * on 2016/8/11.
 */
public class GongActivity extends BaseActivity {
    private ImageView iv_one_card_return;//返回键
    private TextView tv_gjj_dqye;//当前余额
    private TextView tv_gjj_zh;//公积金账号
    private TextView tv_gjj_ye;//本月余额
    private TextView tv_gjj_byjc;//本月缴存
    private TextView tv_gjj_nd;//年度
    private AbHttpUtil abHttpUtil;
    private SharePreferenceUtils share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongjijin);
        init();
    }
    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        iv_one_card_return = (ImageView) findViewById(R.id.iv_one_card_return);
        tv_gjj_dqye = (TextView) findViewById(R.id.tv_gjj_dqye);

        tv_gjj_zh = (TextView) findViewById(R.id.tv_gjj_zh);
        tv_gjj_ye = (TextView) findViewById(R.id.tv_gjj_ye);
        tv_gjj_byjc = (TextView) findViewById(R.id.tv_gjj_byjc);
        tv_gjj_nd = (TextView) findViewById(R.id.tv_gjj_nd);
        setListener();
        getData();
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("getFundInfo")+Constants.BASEPARAMS
                +"&userid="+share.getString("wAccount","")+"&password="+share.getString("wPwd","");
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"getFundInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(jsonObject.getString("data"));
                    if(jsonObject!=null){
                        tv_gjj_dqye.setText(jsonObject.getString("当前余额"));
                        tv_gjj_zh.setText(jsonObject.getString("公积金帐号"));
                        tv_gjj_ye.setText(jsonObject.getString("月初余额"));
                        tv_gjj_byjc.setText(jsonObject.getString("本月缴存"));
                        tv_gjj_nd.setText(jsonObject.getString("年度"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(GongActivity.this,"暂无公积金信息");
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_one_card_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
