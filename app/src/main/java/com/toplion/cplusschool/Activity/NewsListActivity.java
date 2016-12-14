package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Adapter.NewsListAdapter;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 保修常见问题及解决方案
 *
 * @author liyb
 */
public class NewsListActivity extends BaseActivity {
    private ListView listview;    // 新闻列表
    private ImageView iv_return;       // 返回
    private AbHttpUtil abHttpUtil;//网络请求工具
    private NewsListAdapter newsListAdapter;//适配器
    private List<NewBean> newlist;//新闻列表
    private int style = 0;
    private TextView newtitle;

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
        style = getIntent().getIntExtra("style", 0);
        newtitle = (TextView) findViewById(R.id.newtitle);
        if (style == 0) {
            newtitle.setText("建大新闻");
        } else {
            newtitle.setText("最新讲座");
        }
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_return = (ImageView) this.findViewById(R.id.repair_question_info_return);
        listview = (ListView) this.findViewById(R.id.news_list);
        newlist = new ArrayList<NewBean>();
        newsListAdapter = new NewsListAdapter(this, newlist);
        listview.setAdapter(newsListAdapter);//设置adapter
    }

    //获取问题列表数据
    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("type", style);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showNews");
        abHttpUtil.post(url, params, new CallBackParent(NewsListActivity.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray array = obj.getJSONArray("data");
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject newjson = (JSONObject) array.get(i);
                            NewBean news = new NewBean();
                            news.setNewsID(Function.getInstance().getString(newjson, "NEWSID"));
                            news.setNews_title(Function.getInstance().getString(newjson, "NEWS_TITLE"));
                            news.setTime(Function.getInstance().getString(newjson, "TIME"));
                            newlist.add(news);
                        }
                        newsListAdapter.setMlist(newlist);
                        newsListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        //问题列表单个点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                String newsid = newlist.get(position).getNewsID();
                Intent intent = new Intent(NewsListActivity.this, WebActivity.class);
                intent.putExtra("bean",newlist.get(position));
                intent.putExtra("style", style);
                startActivity(intent);
            }
        });
    }
}
