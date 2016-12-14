package com.toplion.cplusschool.SendMessage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;

import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.MessageListAdapter;
import com.toplion.cplusschool.Adapter.NewsListAdapter;
import com.toplion.cplusschool.Bean.NewBean;
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
 * 信息发布及公文发布页面
 *
 * @author tengyy
 */
public class MessageActivity extends BaseActivity {
    int lastItem;
    private RelativeLayout loadmoremain;
    private LayoutInflater inflater;
    private ListView listview;    // 新闻列表
    private ImageView iv_return;       // 返回
    private AbHttpUtil abHttpUtil;//网络请求工具
    private MessageListAdapter newsListAdapter;//适配器
    private List<NewBean> newlist;//新闻列表
    private int style = 0;
    private TextView newtitle;
    private View footView;
    private int page = 1;
    private boolean isla=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        init();
        getData();
        setListener();
    }

    //初始化布局
    @Override
    protected void init() {
        super.init();
        style = getIntent().getIntExtra("style", 1);
        newtitle = (TextView) findViewById(R.id.newtitle);
        if (style == 1) {
            newtitle.setText("信息发布");
        } else if (style == 2) {
            newtitle.setText("公文发布");
        }
        inflater = LayoutInflater.from(this);
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_return = (ImageView) this.findViewById(R.id.repair_question_info_return);
        listview = (ListView) this.findViewById(R.id.news_list);
        newlist = new ArrayList<NewBean>();
        newsListAdapter = new MessageListAdapter(this, newlist);
        footView = inflater.inflate(R.layout.load_more, null);
        loadmoremain = (RelativeLayout) footView.findViewById(R.id.loadmoremain);
        listview.addFooterView(footView);
        listview.setAdapter(newsListAdapter);//设置adapter
                //问题列表单个点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                Intent intent = new Intent(MessageActivity.this, MessageContent.class);
                intent.putExtra("bean",newlist.get(position));
                intent.putExtra("style", style);
                startActivity(intent);
            }
        });
    }

    private void addonclicklistenter() {
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            //AbsListView view 这个view对象就是listview
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        if(isla) {
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
    //获取问题列表数据
    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("type", style);
        params.put("page", page);
        params.put("pageCount", 15);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showInfomationOrDocumentByInput");
        abHttpUtil.post(url, params, new CallBackParent(MessageActivity.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String str=obj.getString("data");
                    if(!str.equals("[]")) {
                        JSONArray array = obj.getJSONArray("data");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject newjson = (JSONObject) array.get(i);
                                NewBean news = new NewBean();
                                news.setNewsID(Function.getInstance().getString(newjson, "id"));
                                news.setNews_title(Function.getInstance().getString(newjson, "title"));
                                news.setTime(Function.getInstance().getString(newjson, "releaseTime"));
                                news.setNews_Info(Function.getInstance().getString(newjson, "releaseDepart"));
                                newlist.add(news);
                            }
                            if (newlist.size() < 15) {
                                listview.removeFooterView(footView);
                            } else {
                                addonclicklistenter();
                            }
                            newsListAdapter.setMlist(newlist);
                            newsListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        listview.removeFooterView(footView);
                        ToastManager.getInstance().showToast(MessageActivity.this,"没有相关数据");
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
        params.put("type", style);
        params.put("page", page);
        params.put("pageCount", 15);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showInfomationOrDocumentByInput");
        abHttpUtil.post(url, params, new CallBackParent(MessageActivity.this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String str=obj.getString("data");
                    if(!str.equals("[]")) {
                        JSONArray array = obj.getJSONArray("data");
                        if (array.length() > 0) {
                            ArrayList<NewBean> otherlist = new ArrayList<NewBean>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject newjson = (JSONObject) array.get(i);
                                NewBean news = new NewBean();
                                news.setNewsID(Function.getInstance().getString(newjson, "id"));
                                news.setNews_title(Function.getInstance().getString(newjson, "title"));
                                news.setTime(Function.getInstance().getString(newjson, "releaseTime"));
                                news.setNews_Info(Function.getInstance().getString(newjson, "releaseDepart"));
                                otherlist.add(news);
                            }
                            if (otherlist.size() > 0) {
                                if(otherlist.size()<15){
                                    isla=false;
                                    listview.removeFooterView(footView);
                                }
                                newlist.addAll(otherlist);
                            }
                            newsListAdapter.setMlist(newlist);
                            loadmoremain.setVisibility(View.GONE);
                            newsListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        listview.removeFooterView(footView);
                        ToastManager.getInstance().showToast(MessageActivity.this,"没有更多数据");
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

    //点击事件
    @Override
    protected void setListener() {
        super.setListener();
        //返回键
        iv_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 关闭当前页面
                finish();
            }
        });
    }
}
