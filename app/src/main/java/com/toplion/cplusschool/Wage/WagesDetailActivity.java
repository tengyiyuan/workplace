package com.toplion.cplusschool.Wage;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.global.AbActivityManager;
import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang
 * on 2016/8/10.
 *
 * @des 工资详情
 */
public class WagesDetailActivity extends BaseActivity {
    private ImageView iv_wages_back;//返回
    private TextView tv_wages_month;//选择的月份
    private RelativeLayout rl_wages_month;
    private ListView lv_wages_month;//选择月份列表
    private LinearLayout ll_wages_detail;//显示详细界面
    private TextView tv_wages_gw;//岗位工资
    private TextView tv_wages_xj;//薪级工资
    private TextView tv_wages_jcjx;//基础绩效
    private TextView tv_wages_zfbt;//住房补贴
    private TextView tv_wages_wybt;//物业补贴
    private TextView tv_wages_jljx;//奖励绩效
    private TextView tv_wages_qtbt1;//其他补助1
    private TextView tv_wages_qtbt2;//补助2
    private TextView tv_wages_yf;//应发
    private TextView tv_wages_gjj;//公积金
    private TextView tv_wages_ylbx;//养老保险
    private TextView tv_wages_yilbx;//医疗保险
    private TextView tv_wages_sqkk;//税前扣款
    private TextView tv_wages_jsgz;//计税工资
    private TextView tv_wages_gsjm;//个税减免系数
    private TextView tv_wages_gssd;//个人所得税
    private TextView tv_wages_qtk;//其他扣
    private TextView tv_wages_sf;//实发
    private MyAdapter myAdapter;
    private List<Map<String, String>> list;
    private AbHttpUtil abHttpUtil;
    private SharePreferenceUtils share;
    private JSONArray jsonArray;//工资列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wages_detail);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        iv_wages_back = (ImageView) findViewById(R.id.iv_wages_back);
        tv_wages_month = (TextView) findViewById(R.id.tv_wages_month);
        rl_wages_month = (RelativeLayout) findViewById(R.id.rl_wages_month);
        lv_wages_month = (ListView) findViewById(R.id.lv_wages_month);
        ll_wages_detail = (LinearLayout) findViewById(R.id.ll_wages_detail);
        tv_wages_gw = (TextView) findViewById(R.id.tv_wages_gw);
        tv_wages_xj = (TextView) findViewById(R.id.tv_wages_xj);
        tv_wages_jcjx = (TextView) findViewById(R.id.tv_wages_jcjx);
        tv_wages_zfbt = (TextView) findViewById(R.id.tv_wages_zfbt);
        tv_wages_wybt = (TextView) findViewById(R.id.tv_wages_wybt);
        tv_wages_jljx = (TextView) findViewById(R.id.tv_wages_jljx);
        tv_wages_qtbt1 = (TextView) findViewById(R.id.tv_wages_qtbt1);
        tv_wages_qtbt2 = (TextView) findViewById(R.id.tv_wages_qtbt2);
        tv_wages_yf = (TextView) findViewById(R.id.tv_wages_yf);
        tv_wages_gjj = (TextView) findViewById(R.id.tv_wages_gjj);
        tv_wages_ylbx = (TextView) findViewById(R.id.tv_wages_ylbx);
        tv_wages_yilbx = (TextView) findViewById(R.id.tv_wages_yilbx);
        tv_wages_sqkk = (TextView) findViewById(R.id.tv_wages_sqkk);
        tv_wages_jsgz = (TextView) findViewById(R.id.tv_wages_jsgz);
        tv_wages_gsjm = (TextView) findViewById(R.id.tv_wages_gsjm);
        tv_wages_gssd = (TextView) findViewById(R.id.tv_wages_gssd);
        tv_wages_qtk = (TextView) findViewById(R.id.tv_wages_qtk);
        tv_wages_sf = (TextView) findViewById(R.id.tv_wages_sf);
        list = new ArrayList<Map<String, String>>();
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAccountInfo") + Constants.BASEPARAMS
                + "&userid=" + share.getString("wAccount", "") + "&password=" + share.getString("wPwd", "");
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "getAccountInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String month = jsonArray.getJSONObject(i).getString("月份");
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", i + "");
                            if (!month.contains("合计")) {
                                map.put("month", month + "月");
                            } else {
                                map.put("month", month);
                            }
                            list.add(map);
                        }
                        myAdapter = new MyAdapter();
                        lv_wages_month.setAdapter(myAdapter);
                    } else {
                        ToastManager.getInstance().showToast(WagesDetailActivity.this, "暂无工资信息");
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
        iv_wages_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_wages_month.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    lv_wages_month.setVisibility(View.VISIBLE);
                    rl_wages_month.setVisibility(View.GONE);
                    ll_wages_detail.setVisibility(View.GONE);
                }
            }
        });
        rl_wages_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_wages_month.setVisibility(View.VISIBLE);
                rl_wages_month.setVisibility(View.GONE);
                ll_wages_detail.setVisibility(View.GONE);
            }
        });
        lv_wages_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> map = list.get(position);
                if (jsonArray != null && jsonArray.length() > 0) {
                    try {
//                        if(text.contains("月")){
//                            index = jsonArray.length() - Integer.parseInt(text.replace("月",""))-1;
//                        }else if(text.contains("合计")){
//                            text = "合计";
//                            index = jsonArray.length() - 1;
//                        }

                        setTextViewData(map.get("month"), jsonArray.getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastManager.getInstance().showToast(WagesDetailActivity.this, "查询工资出错,请稍后重试");
                    }
                } else {
                    ToastManager.getInstance().showToast(WagesDetailActivity.this, "暂无工资信息");
                }
            }
        });
    }

    //设置控件值
    private void setTextViewData(String month, JSONObject json) {
        tv_wages_month.setText(month);
        lv_wages_month.setVisibility(View.GONE);
        rl_wages_month.setVisibility(View.VISIBLE);
        ll_wages_detail.setVisibility(View.VISIBLE);
        try {
            tv_wages_gw.setText(Function.getInstance().getString(json,"岗位工资"));
            tv_wages_xj.setText(Function.getInstance().getString(json,"薪级工资"));
            tv_wages_jcjx.setText(Function.getInstance().getString(json,"基础绩效"));
            tv_wages_zfbt.setText(Function.getInstance().getString(json,"住房补贴"));
            tv_wages_wybt.setText(Function.getInstance().getString(json,"物业补贴"));
            tv_wages_jljx.setText(Function.getInstance().getString(json,"奖励绩效1"));
            tv_wages_qtbt1.setText(Function.getInstance().getString(json,"其他补1"));
            tv_wages_qtbt2.setText(Function.getInstance().getString(json,"其他补2"));
            tv_wages_yf.setText(Function.getInstance().getString(json,"应发"));
            tv_wages_gjj.setText(Function.getInstance().getString(json,"公积金"));
            tv_wages_ylbx.setText(Function.getInstance().getString(json,"养老保险"));
            tv_wages_yilbx.setText(Function.getInstance().getString(json,"医疗保险"));
            tv_wages_sqkk.setText(Function.getInstance().getString(json,"税前扣款"));
            tv_wages_jsgz.setText(Function.getInstance().getString(json,"计税工资"));
            tv_wages_gsjm.setText(Function.getInstance().getString(json,"个税减免系数"));
            tv_wages_gssd.setText(Function.getInstance().getString(json,"个人所得税"));
            tv_wages_qtk.setText(Function.getInstance().getString(json,"其它扣"));
            tv_wages_sf.setText(Function.getInstance().getString(json,"实发"));
//            ToastManager.getInstance().showToast(WagesDetailActivity.this,json.getString("月份"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (lv_wages_month.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            lv_wages_month.setVisibility(View.VISIBLE);
            rl_wages_month.setVisibility(View.GONE);
            ll_wages_detail.setVisibility(View.GONE);
        }
    }


    private class MyAdapter extends BaseAdapter {
        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text_content = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(WagesDetailActivity.this).inflate(R.layout.single_select, null);
                text_content = (TextView) convertView.findViewById(R.id.txt_1);
                convertView.setTag(text_content);
            } else {
                text_content = (TextView) convertView.getTag();
            }
            text_content.setText(list.get(position).get("month"));
            return convertView;
        }
    }
}
