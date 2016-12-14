package com.toplion.cplusschool.JianKong;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.KongAdapter;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.Kongitem;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toplion on 2016/9/12.
 */
public class KongMainActivity extends BaseActivity {
    int lastItem;
    private RelativeLayout loadmoremain;
    private LayoutInflater inflater;
    private ListView mylist;
    private TextView title;
    private TextView years;
    private KongAdapter adapter;
    private ArrayList<Kongitem> mlist;
    private List<CommonBean> termList;//年份
    private int page = 1;
    private View footView;
    private String year = "2015";
    private boolean isla = true;
    private RelativeLayout rl_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kongmain);
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        ImageView about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        abHttpUtil = AbHttpUtil.getInstance(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 屏幕的高度和宽度保存到本地缓存
        BaseApplication.ScreenWidth = screenWidth;
        BaseApplication.ScreenHeight = screenHeight;
        rl_nodata=(RelativeLayout)findViewById(R.id.rl_nodata);
        rl_nodata.setVisibility(View.GONE);
        inflater = LayoutInflater.from(this);
        mylist = (ListView) findViewById(R.id.mylist);
        title = (TextView) findViewById(R.id.about_iv_Title);
        years = (TextView) findViewById(R.id.years);
        mlist = new ArrayList<Kongitem>();
        termList = new ArrayList<CommonBean>();
        footView = inflater.inflate(R.layout.load_more, null);
        loadmoremain = (RelativeLayout) footView.findViewById(R.id.loadmoremain);
        adapter = new KongAdapter(this, mlist);
        mylist.setAdapter(adapter);
        years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termList.size() > 0) {
                    final CustomDialogListview dialog_sex = new CustomDialogListview(KongMainActivity.this, "选择年份", termList, years.getText().toString());
                    dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            title.setText(termList.get(position).getId() + "年重点工作监控");
                            years.setText(termList.get(position).getId() + " ");
                            year = termList.get(position).getId();
                            page = 1;
                            getData();
                            dialog_sex.dismiss();
                        }
                    });
                    dialog_sex.show();
                } else {
                    getYears();
                }
            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("year", year);
                intent.putExtra("wid", mlist.get(position).getKongid());
                intent.putExtra("title", mlist.get(position).getToptitle());
                intent.setClass(KongMainActivity.this, KongContent.class);
                KongMainActivity.this.startActivity(intent);
            }
        });
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void addonclicklistenter() {
        mylist.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    protected void getData() {
        mlist.clear();
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("page", page);
        params.put("year", year);
        params.put("pageCount", 15);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showZdgzList");
        abHttpUtil.post(url, params, new CallBackParent(KongMainActivity.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String str = Function.getInstance().getString(object, "data");
                    year=Function.getInstance().getString(object,"currentyear");
                    if(!year.equals("")){
                        title.setText(year + "年重点工作监控");
                        years.setText(year+ " ");
                    }
                    if (str.equals("")) {
                        ToastManager.getInstance().showToast(KongMainActivity.this, "服务器异常");
                        return;
                    }
                    if (!str.equals("[]")) {
                        rl_nodata.setVisibility(View.GONE);
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = (JSONObject) array.get(i);
                                Kongitem item = new Kongitem();
                                item.setKongid(Function.getInstance().getString(obj, "WID"));
                                item.setToptitle(Function.getInstance().getString(obj, "GZMC"));
                                item.setState(Function.getInstance().getString(obj, "GZZTDMMS"));
                                item.setBumen(Function.getInstance().getString(obj, "DWBZMC"));
                                mlist.add(item);
                            }
                            if (mlist.size() < 15) {
                                mylist.removeFooterView(footView);
                            } else {
                                mylist.addFooterView(footView);
                                isla = true;
                                addonclicklistenter();
                            }
                            adapter.setMlist(mlist);
                            adapter.notifyDataSetChanged();
                            mylist.setSelection(0);
                        }
                    } else {
                        mylist.removeFooterView(footView);
                        ToastManager.getInstance().showToast(KongMainActivity.this, "没有相关数据");
                        rl_nodata.setVisibility(View.VISIBLE);
                        adapter.setMlist(mlist);
                        adapter.notifyDataSetChanged();
                        mylist.setSelection(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    protected void getMoreData() {
        loadmoremain.setVisibility(View.VISIBLE);
        page++;
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("year", year);
        params.put("page", page);
        params.put("pageCount", 15);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showZdgzList");
        abHttpUtil.post(url, params, new CallBackParent(KongMainActivity.this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String str = object.getString("data");
                    if (!str.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            ArrayList<Kongitem> otherlist = new ArrayList<Kongitem>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = (JSONObject) array.get(i);
                                Kongitem item = new Kongitem();
                                item.setKongid(Function.getInstance().getString(obj, "WID"));
                                item.setToptitle(Function.getInstance().getString(obj, "GZMC"));
                                item.setState(Function.getInstance().getString(obj, "GZZTDMMS"));
                                item.setBumen(Function.getInstance().getString(obj, "DWBZMC"));
                                otherlist.add(item);
                            }
                            if (otherlist.size() > 0) {
                                if (otherlist.size() < 15) {
                                    isla = false;
                                    mylist.removeFooterView(footView);
                                }
                                mlist.addAll(otherlist);
                            }
                            adapter.setMlist(mlist);
                            loadmoremain.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        mylist.removeFooterView(footView);
                        ToastManager.getInstance().showToast(KongMainActivity.this, "没有相关数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadmoremain.setVisibility(View.GONE);
            }
        });
    }

    private void getYears() {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getArrayYear");
        abHttpUtil.post(url, params, new CallBackParent(KongMainActivity.this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String str = Function.getInstance().getString(object, "data");
                    if (str.equals("")) {
                        ToastManager.getInstance().showToast(KongMainActivity.this, "服务器异常");
                        return;
                    }
                    if (!str.equals("[]")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = (JSONObject) array.get(i);
                                termList.add(new CommonBean(Function.getInstance().getInteger(obj, "ID") + "", Function.getInstance().getString(obj, "VALUE")));
                            }
                            final CustomDialogListview dialog_sex = new CustomDialogListview(KongMainActivity.this, "选择年份", termList, years.getText().toString());
                            dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    title.setText(termList.get(position).getId() + "年重点工作监控");
                                    years.setText(termList.get(position).getId() + " ");
                                    year = termList.get(position).getId();
                                    page = 1;
                                    getData();
                                    dialog_sex.dismiss();
                                }
                            });

                            dialog_sex.show();
                        }
                    } else {
                        ToastManager.getInstance().showToast(KongMainActivity.this, "服务器异常");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
