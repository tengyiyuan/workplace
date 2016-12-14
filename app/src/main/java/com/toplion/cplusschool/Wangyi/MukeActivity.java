package com.toplion.cplusschool.Wangyi;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Activity.CommonWebViewActivity;
import com.toplion.cplusschool.Adapter.MukeListAdapter;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.Mukebean;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CustomDialogListview;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toplion on 2016/11/4.
 */
public class MukeActivity extends BaseActivity {
    int lastItem;
    private RelativeLayout loadmoremain;
    private ListView mukelist;
    private ArrayList<Mukebean> mukebeens;
    private MukeListAdapter mukeListAdapter;
    private View footView;
    private int page = 1;
    private boolean isla = true;
    private LayoutInflater inflater;
    private AbHttpUtil abHttpUtil;//网络请求工具
    private TextView state;
    private int kestate=20;
    private List<CommonBean> plist;//是否通过
    private ImageView repair_question_info_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mukemain);
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 屏幕的高度和宽度保存到本地缓存
        BaseApplication.ScreenWidth = screenWidth;
        BaseApplication.ScreenHeight = screenHeight;
        inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.load_more, null);
        loadmoremain = (RelativeLayout) footView.findViewById(R.id.loadmoremain);
        repair_question_info_return=(ImageView)findViewById(R.id.repair_question_info_return);
        state=(TextView)findViewById(R.id.state);
        abHttpUtil = AbHttpUtil.getInstance(this);
        mukelist = (ListView) findViewById(R.id.muke_list);
        mukebeens = new ArrayList<Mukebean>();
        mukeListAdapter = new MukeListAdapter(this, mukebeens);
        mukelist.addFooterView(footView);
        mukelist.setAdapter(mukeListAdapter);
        state.setVisibility(View.GONE);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plist = new ArrayList<CommonBean>();
                plist.add(new CommonBean("20", "正在进行"));
                plist.add(new CommonBean("10", "已经结束"));
                plist.add(new CommonBean("30", "尚未开课"));
                final CustomDialogListview dialog_sex = new CustomDialogListview(MukeActivity.this, "是否通过", plist, state.getText().toString());
                dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        state.setText(plist.get(position).getDes());
                        kestate=Integer.parseInt(plist.get(position).getId());
                        getData();
                        dialog_sex.dismiss();
                    }
                });
                dialog_sex.show();
            }
        });
        mukelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent();
//                intent.putExtra("url",mukebeens.get(position).getCtUrl());
//                intent.putExtra("title","课程详情");
//                intent.setClass(MukeActivity.this,MukeWebview.class);
//                MukeActivity.this.startActivity(intent);

                Uri uri = Uri.parse(mukebeens.get(position).getCtUrl());
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                MukeActivity.this.startActivity(viewIntent);


            }
        });
        repair_question_info_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addonclicklistenter() {
        mukelist.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void getMoreData() {
        loadmoremain.setVisibility(View.VISIBLE);
        page++;
        long time = System.currentTimeMillis();
        String signature = getSha1("2c6ade3103944ef54e194fcee01b251416546135431" + time);
        String url = "http://www.icourse163.org/open/courses/mooc?appId=27b3e069286bd775b3336da3e880c8f9&signature=" + signature + "&format=json&nonce=16546135431&pageIndex=" + page + "&pageSize=15&publishStatus=2&progress="+kestate+"&timestamp=" + time;
        abHttpUtil.get(url, new AbStringHttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    JSONObject object = new JSONObject(content);
                    int code = Function.getInstance().getInteger(object, "code");
                    if (code == 200) {
                        String results = Function.getInstance().getString(object, "result");
                        JSONObject objresult = new JSONObject(results);
                        JSONArray resultlist = objresult.getJSONArray("list");
                        ArrayList<Mukebean> otherlist = new ArrayList<Mukebean>();
                        for (int i = 0; i < resultlist.length(); i++) {
                            JSONObject obj = (JSONObject) resultlist.get(i);
                            Mukebean bean = new Mukebean();
                            bean.setCtUrl(Function.getInstance().getString(obj, "ctUrl"));
                            bean.setCtImgUrl(Function.getInstance().getString(obj, "ctImgUrl"));
                            bean.setName(Function.getInstance().getString(obj, "name"));
                            bean.setCtStartTime(Function.getInstance().getLong(obj, "ctStartTime"));
                            bean.setCtEndTime(Function.getInstance().getLong(obj, "ctEndTime"));
                            bean.setEnrollCount(Function.getInstance().getInteger(obj,"enrollCount"));
                            otherlist.add(bean);
                        }
                        if (otherlist.size() > 0) {
                            if (otherlist.size() < 15) {
                                isla = false;
                                mukelist.removeFooterView(footView);
                            }
                            mukebeens.addAll(otherlist);
                        }
                        mukeListAdapter.setMlist(mukebeens);
                        loadmoremain.setVisibility(View.GONE);
                        mukeListAdapter.notifyDataSetChanged();
                    } else {
                        mukelist.removeFooterView(footView);
                        ToastManager.getInstance().showToast(MukeActivity.this, "课程出错");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void getData() {
        super.getData();
        page = 1;
        long time = System.currentTimeMillis();
        String signature = getSha1("2c6ade3103944ef54e194fcee01b251416546135431" + time);
        String url = "http://www.icourse163.org/open/courses/mooc?appId=27b3e069286bd775b3336da3e880c8f9&signature=" + signature + "&format=json&nonce=16546135431&pageIndex=" + page + "&pageSize=15&publishStatus=2&progress="+kestate+"&timestamp=" + time;
        abHttpUtil.get(url, new AbStringHttpResponseListener() {
            @Override
            public void onStart() {
                AbDialogUtil.showProgressDialog(MukeActivity.this, 0, "正在加载");
            }

            @Override
            public void onFinish() {
                AbDialogUtil.removeDialog(MukeActivity.this);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbDialogUtil.removeDialog(MukeActivity.this);
                ToastManager.getInstance().showToast(MukeActivity.this, "服务器异常!");
            }
            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    JSONObject object = new JSONObject(content);
                    int code = Function.getInstance().getInteger(object, "code");
                    if (code == 200) {
                        mukebeens.clear();
                        String results = Function.getInstance().getString(object, "result");
                        JSONObject objresult = new JSONObject(results);
                        JSONArray resultlist = objresult.getJSONArray("list");
                        for (int i = 0; i < resultlist.length(); i++) {
                            JSONObject obj = (JSONObject) resultlist.get(i);
                            Mukebean bean = new Mukebean();
                            bean.setCtUrl(Function.getInstance().getString(obj, "ctUrl"));
                            bean.setCtImgUrl(Function.getInstance().getString(obj, "ctImgUrl"));
                            bean.setName(Function.getInstance().getString(obj, "name"));
                            bean.setCtStartTime(Function.getInstance().getLong(obj, "ctStartTime"));
                            bean.setCtEndTime(Function.getInstance().getLong(obj, "ctEndTime"));
                            bean.setEnrollCount(Function.getInstance().getInteger(obj,"enrollCount"));
                            mukebeens.add(bean);
                        }
                        if (mukebeens.size() < 15) {
                            mukelist.removeFooterView(footView);
                        } else {
                            addonclicklistenter();
                        }
                        mukeListAdapter.setMlist(mukebeens);
                        mukeListAdapter.notifyDataSetChanged();
                    } else {
                        mukelist.removeFooterView(footView);
                        ToastManager.getInstance().showToast(MukeActivity.this, "课程出错");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
