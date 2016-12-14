package com.toplion.cplusschool.SecondMarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.MarketAdapter;
import com.toplion.cplusschool.Bean.MarketBean;
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
import java.util.List;

/**
 * Created by wang
 * on 2016/10/13.
 *
 * @des 条件搜索界面
 */

public class MarketSearchActivity extends BaseActivity {
    private TextView btn_sou;
    private ImageView iv_search_market_return;//返回键
    private ListView lv_search_market;//列表
    private EditText et_search_content;//搜索框
    private List<MarketBean> mlist;
    private MarketAdapter adapter;
    private int page = 1;
    private boolean isla = true;
    private int module=1;
    private int reltype=1;
    private String keyword="";
    private View footView;
    private RelativeLayout loadmoremain;
    private LayoutInflater inflater;
    int lastItem;
    private TextView noresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_search);
        init();
    }

    @Override
    protected void init() {
        super.init();
        module=getIntent().getIntExtra("module",1);
        reltype=getIntent().getIntExtra("reltype",1);
        inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.load_more, null);
        loadmoremain = (RelativeLayout) footView.findViewById(R.id.loadmoremain);
        iv_search_market_return = (ImageView) findViewById(R.id.iv_search_market_return);
        lv_search_market = (ListView) findViewById(R.id.lv_search_market);
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        noresult=(TextView)findViewById(R.id.noresult);
        btn_sou=(TextView)findViewById(R.id.sousuo);
        mlist=new ArrayList<MarketBean>();
        adapter = new MarketAdapter(this, mlist,module);
        lv_search_market.setAdapter(adapter);
        btn_sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword=et_search_content.getText().toString().trim();
                HideKeyboard(et_search_content);
                if(keyword.equals("")){
                    ToastManager.getInstance().showToast(MarketSearchActivity.this,"请您正确输入搜索信息");
                    return;
                }else{
                    getData();
                }
            }
        });
        lv_search_market.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MarketSearchActivity.this, MarketContent.class);
                intent.putExtra("infocontent", mlist.get(position));
                startActivity(intent);
            }
        });
        iv_search_market_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(et_search_content);
                finish();
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        isla = true;
        mlist.clear();
        page = 1;
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("module", module);
        params.put("keyword",keyword);
        params.put("auitype",reltype);
        params.put("page", page);
        params.put("pageCount", 10);
        String url = Constants.NEWBASE_URL + "?rid=" + ReturnUtils.encode("searchReleaseInfoByInput") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在查询数据...") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String result_obj = Function.getInstance().getString(object, "data");
                    if (!result_obj.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            noresult.setVisibility(View.GONE);
                            for (int i = 0; i < array.length(); i++) {
                                MarketBean marketBean = new MarketBean();
                                JSONObject arr_obj = (JSONObject) array.get(i);
                                marketBean.setAUITITLE(Function.getInstance().getString(arr_obj, "AUITITLE"));
                                marketBean.setAUITYPE(Function.getInstance().getInteger(arr_obj, "AUITYPE"));
                                marketBean.setAUICONTENT(Function.getInstance().getString(arr_obj, "AUICONTENT"));
                                marketBean.setAUICATEGORY(Function.getInstance().getInteger(arr_obj, "AUICATEGORY"));
                                if (!Function.getInstance().getString(arr_obj, "IRIURL").equals("")) {
                                    JSONArray urlarry = arr_obj.getJSONArray("IRIURL");
                                    ArrayList<String> url = new ArrayList<String>();
                                    for (int j = 0; j < urlarry.length(); j++) {
                                        JSONObject url_obj = (JSONObject) urlarry.get(j);
                                        url.add(Function.getInstance().getString(url_obj, "URL"));
                                    }
                                    marketBean.setIRIURL(url);
                                } else {
                                    marketBean.setIRIURL(new ArrayList<String>());
                                }
                                marketBean.setAUIPHONE(Function.getInstance().getString(arr_obj, "AUIPHONE"));
                                marketBean.setAUIQQ(Function.getInstance().getString(arr_obj, "AUIQQ"));
                                marketBean.setAUIWEIXIN(Function.getInstance().getString(arr_obj, "AUIWEIXIN"));
                                marketBean.setAUIADDRESS(Function.getInstance().getString(arr_obj, "AUIADDRESS"));
                                marketBean.setAUIPRICE(Function.getInstance().getInteger(arr_obj, "AUIPRICE"));
                                marketBean.setAUIID(Function.getInstance().getString(arr_obj, "AUIID"));
                                marketBean.setAUIRELEASETIME(Function.getInstance().getString(arr_obj, "AUIRELEASETIME"));
                                marketBean.setAUISTATUS(Function.getInstance().getInteger(arr_obj, "AUISTATUS"));
                                marketBean.setCINAME(Function.getInstance().getString(arr_obj, "CINAME"));
                                marketBean.setAUICONTACTNAME(Function.getInstance().getString(arr_obj, "AUICONTACTNAME"));
                                mlist.add(marketBean);
                            }
                            if (mlist.size() < 10) {
                                lv_search_market.removeFooterView(footView);
                                loadmoremain.setVisibility(View.GONE);
                            } else {
                                lv_search_market.addFooterView(footView);
                                addonclicklistenter();
                            }
                        } else {
                            lv_search_market.removeFooterView(footView);
                            loadmoremain.setVisibility(View.GONE);
                            noresult.setVisibility(View.VISIBLE);
                            //ToastManager.getInstance().showToast(MarketSearchActivity.this, "暂无数据");
                        }
                    } else {
                        lv_search_market.removeFooterView(footView);
                        loadmoremain.setVisibility(View.GONE);
                        noresult.setVisibility(View.VISIBLE);
                        //ToastManager.getInstance().showToast(MarketSearchActivity.this, "暂无数据");
                    }
                } catch (JSONException e) {
                    lv_search_market.removeFooterView(footView);
                    loadmoremain.setVisibility(View.GONE);
                    ToastManager.getInstance().showToast(MarketSearchActivity.this, "服务器异常");
                    e.printStackTrace();
                }
                adapter.setMlist(mlist);
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void getMoreData() {
        loadmoremain.setVisibility(View.VISIBLE);
        page++;
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("module", module);
        params.put("keyword",keyword);
        params.put("auitype",reltype);
        params.put("page", page);
        params.put("pageCount", 10);
        String url = Constants.NEWBASE_URL + "?rid=" + ReturnUtils.encode("searchReleaseInfoByInput") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String result_obj = Function.getInstance().getString(object, "data");
                    if (!result_obj.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            ArrayList<MarketBean> otherlist = new ArrayList<MarketBean>();
                            for (int i = 0; i < array.length(); i++) {
                                MarketBean marketBean = new MarketBean();
                                JSONObject arr_obj = (JSONObject) array.get(i);
                                marketBean.setAUITITLE(Function.getInstance().getString(arr_obj, "AUITITLE"));
                                marketBean.setAUITYPE(Function.getInstance().getInteger(arr_obj, "AUITYPE"));
                                marketBean.setAUICONTENT(Function.getInstance().getString(arr_obj, "AUICONTENT"));
                                marketBean.setAUICATEGORY(Function.getInstance().getInteger(arr_obj, "AUICATEGORY"));
                                if (!Function.getInstance().getString(arr_obj, "IRIURL").equals("")) {
                                    JSONArray urlarry = arr_obj.getJSONArray("IRIURL");
                                    ArrayList<String> url = new ArrayList<String>();
                                    for (int j = 0; j < urlarry.length(); j++) {
                                        JSONObject url_obj = (JSONObject) urlarry.get(j);
                                        url.add(Function.getInstance().getString(url_obj, "URL"));
                                    }
                                    marketBean.setIRIURL(url);
                                } else {
                                    marketBean.setIRIURL(new ArrayList<String>());
                                }
                                marketBean.setAUIPHONE(Function.getInstance().getString(arr_obj, "AUIPHONE"));
                                marketBean.setAUIQQ(Function.getInstance().getString(arr_obj, "AUIQQ"));
                                marketBean.setAUIWEIXIN(Function.getInstance().getString(arr_obj, "AUIWEIXIN"));
                                marketBean.setAUIADDRESS(Function.getInstance().getString(arr_obj, "AUIADDRESS"));
                                marketBean.setAUIPRICE(Function.getInstance().getInteger(arr_obj, "AUIPRICE"));
                                marketBean.setAUIID(Function.getInstance().getString(arr_obj, "AUIID"));
                                marketBean.setAUIRELEASETIME(Function.getInstance().getString(arr_obj, "AUIRELEASETIME"));
                                marketBean.setAUISTATUS(Function.getInstance().getInteger(arr_obj, "AUISTATUS"));
                                marketBean.setCINAME(Function.getInstance().getString(arr_obj, "CINAME"));
                                marketBean.setAUICONTACTNAME(Function.getInstance().getString(arr_obj, "AUICONTACTNAME"));
                                otherlist.add(marketBean);
                            }
                            if (otherlist.size() > 0) {
                                if (otherlist.size() < 10) {
                                    isla = false;
                                    lv_search_market.removeFooterView(footView);
                                    loadmoremain.setVisibility(View.GONE);
                                }
                                mlist.addAll(otherlist);
                            }
                        } else {
                            isla = false;
                            lv_search_market.removeFooterView(footView);
                            loadmoremain.setVisibility(View.GONE);
                            //ToastManager.getInstance().showToast(getActivity(), "暂无更多数据");
                        }
                    } else {
                        lv_search_market.removeFooterView(footView);
                        loadmoremain.setVisibility(View.GONE);
                        // ToastManager.getInstance().showToast(getActivity(), "暂无更多数据");
                    }
                } catch (JSONException e) {
                    lv_search_market.removeFooterView(footView);
                    loadmoremain.setVisibility(View.GONE);
                    ToastManager.getInstance().showToast(MarketSearchActivity.this, "服务器异常");
                    e.printStackTrace();
                }
                adapter.setMlist(mlist);
                loadmoremain.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadmoremain.setVisibility(View.GONE);
            }
        });
    }
    private void addonclicklistenter() {
        lv_search_market.setOnScrollListener(new AbsListView.OnScrollListener() {
            //AbsListView view 这个view对象就是listview
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        if (isla) {
                            getMoreData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }
    @Override
    protected void setListener() {
        super.setListener();
        iv_search_market_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_search_market.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MarketSearchActivity.this, MarketContent.class);
                intent.putExtra("infocontent", mlist.get(position));
                startActivity(intent);
            }
        });
    }
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
