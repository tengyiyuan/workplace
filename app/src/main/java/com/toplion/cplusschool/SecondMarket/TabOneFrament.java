package com.toplion.cplusschool.SecondMarket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Adapter.MarketAdapter;
import com.toplion.cplusschool.Bean.MarketBean;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.MyAnimations;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toplion on 2016/10/10.
 */
@SuppressLint("ValidFragment")
public class TabOneFrament extends Fragment implements AbPullToRefreshView.OnHeaderRefreshListener {
    private AbPullToRefreshView mAbPullToRefreshView = null;
    private ListView mListView;
    private View footView;
    private MarketAdapter adapter;
    private List<MarketBean> mlist;
    private AbHttpUtil abHttpUtil;//网络请求工具
    private int style = 1;
    private int page = 1;
    private boolean isla = true;
    private RelativeLayout loadmoremain;
    private LayoutInflater inflater;
    int lastItem;
    private int type=1;
    private TextView noresult;
    public TabOneFrament(int style,int type) {
        this.style=style;
        this.type=type;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketcontent, container, false);
        mListView = (ListView) view.findViewById(R.id.mylist);
        noresult=(TextView)view.findViewById(R.id.noresult);
        inflater = LayoutInflater.from(getActivity());
        footView = inflater.inflate(R.layout.load_more, null);
        loadmoremain = (RelativeLayout) footView.findViewById(R.id.loadmoremain);
        mAbPullToRefreshView = (AbPullToRefreshView) view
                .findViewById(R.id.mPullRefreshView);
        // 设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        // 设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.setLoadMoreEnable(false);
        mlist = new ArrayList<MarketBean>();
        abHttpUtil = AbHttpUtil.getInstance(getActivity());//初始化请求工具
        adapter = new MarketAdapter(getActivity(), mlist,style);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MarketContent.class);
                intent.putExtra("style",style);
                intent.putExtra("infocontent", mlist.get(position));
                getActivity().startActivity(intent);
            }
        });
        getData();
        return view;
    }

    private void addonclicklistenter() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    /**
     * 获取发布列表
     */
    public void getData() {
        isla = true;
        page = 1;
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("module", style);
        params.put("isdivide", 0);
        params.put("reltype",type);
        params.put("page", page);
        params.put("pageCount", 10);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showReleaseInfoByModule") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), "正在加载数据...") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String result_obj = Function.getInstance().getString(object, "data");
                    if (!result_obj.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            noresult.setVisibility(View.GONE);
                            mlist.clear();
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
                                marketBean.setAUIPRICE(Function.getInstance().getDouble(arr_obj, "AUIPRICE"));
                                marketBean.setAUIID(Function.getInstance().getString(arr_obj, "AUIID"));
                                marketBean.setAUIRELEASETIME(Function.getInstance().getString(arr_obj, "AUIRELEASETIME"));
                                marketBean.setAUISTATUS(Function.getInstance().getInteger(arr_obj, "AUISTATUS"));
                                marketBean.setCINAME(Function.getInstance().getString(arr_obj, "CINAME"));
                                marketBean.setAUICONTACTNAME(Function.getInstance().getString(arr_obj, "AUICONTACTNAME"));
                                marketBean.setNC(Function.getInstance().getString(arr_obj,"NC"));
                                marketBean.setTXDZ(Function.getInstance().getString(arr_obj,"TXDZ"));
                                marketBean.setUINAME(Function.getInstance().getString(arr_obj,"UINAME"));
                                marketBean.setUIID(Function.getInstance().getInteger(arr_obj, "UIID"));
                                mlist.add(marketBean);
                            }
                            if (mlist.size() < 10) {
                                mListView.removeFooterView(footView);
                                loadmoremain.setVisibility(View.GONE);
                            } else {
                                mListView.addFooterView(footView);
                                addonclicklistenter();
                            }
                        } else {
                            mListView.removeFooterView(footView);
                            loadmoremain.setVisibility(View.GONE);
                            noresult.setVisibility(View.VISIBLE);
                            mlist.clear();
                            adapter.notifyDataSetChanged();
                            //ToastManager.getInstance().showToast(getActivity(), "暂无数据");
                        }
                    } else {
                        mListView.removeFooterView(footView);
                        loadmoremain.setVisibility(View.GONE);
                        noresult.setVisibility(View.VISIBLE);
                        mlist.clear();
                        adapter.notifyDataSetChanged();
                        //ToastManager.getInstance().showToast(getActivity(), "暂无数据");
                    }
                } catch (JSONException e) {
                    mListView.removeFooterView(footView);
                    loadmoremain.setVisibility(View.GONE);
                    ToastManager.getInstance().showToast(getActivity(), "服务器异常");
                    e.printStackTrace();
                }
                adapter.setMlist(mlist);
                adapter.notifyDataSetChanged();
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }
        });
    }

    private void getMoreData() {
        loadmoremain.setVisibility(View.VISIBLE);
        page++;
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("module", style);
        params.put("isdivide", 0);
        params.put("reltype",type);
        params.put("page", page);
        params.put("pageCount", 10);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showReleaseInfoByModule") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), false) {
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
                                marketBean.setAUIPRICE(Function.getInstance().getDouble(arr_obj, "AUIPRICE"));
                                marketBean.setAUIID(Function.getInstance().getString(arr_obj, "AUIID"));
                                marketBean.setAUIRELEASETIME(Function.getInstance().getString(arr_obj, "AUIRELEASETIME"));
                                marketBean.setAUISTATUS(Function.getInstance().getInteger(arr_obj, "AUISTATUS"));
                                marketBean.setCINAME(Function.getInstance().getString(arr_obj, "CINAME"));
                                marketBean.setAUICONTACTNAME(Function.getInstance().getString(arr_obj, "AUICONTACTNAME"));
                                marketBean.setNC(Function.getInstance().getString(arr_obj,"NC"));
                                marketBean.setTXDZ(Function.getInstance().getString(arr_obj,"TXDZ"));
                                marketBean.setUINAME(Function.getInstance().getString(arr_obj,"UINAME"));
                                marketBean.setUIID(Function.getInstance().getInteger(arr_obj, "UIID"));
                                otherlist.add(marketBean);
                            }
                            if (otherlist.size() > 0) {
                                if (otherlist.size() < 10) {
                                    isla = false;
                                    mListView.removeFooterView(footView);
                                    loadmoremain.setVisibility(View.GONE);
                                }
                                mlist.addAll(otherlist);
                            }
                        } else {
                            isla = false;
                            mListView.removeFooterView(footView);
                            loadmoremain.setVisibility(View.GONE);
                            //ToastManager.getInstance().showToast(getActivity(), "暂无更多数据");
                        }
                    } else {
                        mListView.removeFooterView(footView);
                        loadmoremain.setVisibility(View.GONE);
                       // ToastManager.getInstance().showToast(getActivity(), "暂无更多数据");
                    }
                } catch (JSONException e) {
                    mListView.removeFooterView(footView);
                    loadmoremain.setVisibility(View.GONE);
                    ToastManager.getInstance().showToast(getActivity(), "服务器异常");
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

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        getData();
    }
}
