package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.AmountUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangshengbo
 * on 2016/7/2.
 *
 * @des 一卡通
 */
public class OneCardPassActivity extends BaseActivity {
    private ImageView iv_one_card_return;//
    private ImageView iv_one_card_slogo;//
    private TextView tv_one_card_name;
    private TextView tv_one_card_money;
    private TextView tv_one_card_wx;//
    private TextView tv_one_card_zfb;//
    private AbHttpUtil abHttpUtil;
    private TextView tv_test_title;
    private TextView weixin_sm, zhifubao_sm;
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_card_pass);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        iv_one_card_return = (ImageView) findViewById(R.id.iv_one_card_return);
        iv_one_card_slogo = (ImageView) findViewById(R.id.iv_one_card_slogo);
        tv_one_card_name = (TextView) findViewById(R.id.tv_one_card_name);
        tv_one_card_money = (TextView) findViewById(R.id.tv_one_card_money);
        tv_one_card_wx = (TextView) findViewById(R.id.tv_one_card_wx);
        tv_one_card_zfb = (TextView) findViewById(R.id.tv_one_card_zfb);
        tv_test_title = (TextView) findViewById(R.id.tv_test_title);
        tv_test_title.setText("本数据仅供参考,实际余额请查询官方数据");
        weixin_sm = (TextView) findViewById(R.id.weixin_sm);
        zhifubao_sm = (TextView) findViewById(R.id.zhifubao_sm);
        final String serverIp = share.getString("serverIp","");
        weixin_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OneCardPassActivity.this,CommonWebViewActivity.class)
                        .putExtra("title","微信一卡通充值说明")
                        .putExtra("url","http://"+serverIp+"/help/wxpayhelp.html");
                OneCardPassActivity.this.startActivity(intent);
            }
        });
        zhifubao_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OneCardPassActivity.this,CommonWebViewActivity.class)
                        .putExtra("title","支付宝一卡通充值说明")
                        .putExtra("url","http://"+serverIp+"/help/alipayhelp.html");
                OneCardPassActivity.this.startActivity(intent);

            }
        });
        iv_one_card_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("oneCardPass") + Constants.BASEPARAMS + "&stuNo="
                +(share.getString("ROLE_ID",""));
        abHttpUtil.post(url, new CallBackParent(OneCardPassActivity.this, getResources().getString(R.string.loading), "oneCardPass") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject json = jsonArray.getJSONObject(0);

                    if (json != null) {
                        tv_one_card_name.setText(json.getString("XM").trim());
                        String money =json.getString("KYE").trim();
                        if(!TextUtils.isEmpty(money)){
                            try {
                                String m = AmountUtils.changeF2Y(money);
                                Log.e("money", m + "");
                                tv_one_card_money.setText(m + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
        iv_one_card_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_one_card_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_one_card_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
