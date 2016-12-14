package com.toplion.cplusschool.JianKong;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.KongConAdapter;
import com.toplion.cplusschool.Bean.KongConItem;
import com.toplion.cplusschool.Bean.Kongitem;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toplion on 2016/9/12.
 */
public class KongContent extends BaseActivity {
    private ListView mylist;
    private KongConAdapter adapter;
    private ArrayList<KongConItem> kongconitem;
    private String wid;
    private String year;
    private TextView mytitle;
    private  String title="";
    private TextView tv_webview_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kongcontent);
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        ImageView about_iv_return=(ImageView)findViewById(R.id.about_iv_return);
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wid = getIntent().getStringExtra("wid");
        year = getIntent().getStringExtra("year");
        title=getIntent().getStringExtra("title");
        abHttpUtil = AbHttpUtil.getInstance(this);
        mytitle=(TextView)findViewById(R.id.mytitle);
        tv_webview_title=(TextView)findViewById(R.id.tv_webview_title);
        mytitle.setText(title);
        tv_webview_title.setText(year+"年计划内容与进度");
        mylist = (ListView) findViewById(R.id.mylist);
        kongconitem = new ArrayList<KongConItem>();
        adapter = new KongConAdapter(this, kongconitem);
        mylist.setAdapter(adapter);
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("wid", wid);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showZdgzInfo");
        abHttpUtil.post(url, params, new CallBackParent(KongContent.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String str = Function.getInstance().getString(object, "data");
                    if (!str.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            KongConItem conItem = new KongConItem();
                            conItem.setYuefen(year + "年" + Function.getInstance().getString(obj, "JHYF") + "月");
                            conItem.setJihua(Function.getInstance().getString(obj, "JHNR"));
                            conItem.setJindu(Function.getInstance().getString(obj, "JDNR"));
                            conItem.setBaifenbi(Function.getInstance().getString(obj, "JDBFB"));
                            kongconitem.add(conItem);
                        }
                        adapter.setMlist(kongconitem);
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastManager.getInstance().showToast(KongContent.this, "没有相关数据");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
