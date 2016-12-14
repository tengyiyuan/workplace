package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Adapter.TestListAdapter;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.TermListBean;
import com.toplion.cplusschool.Bean.TestBean;
import com.toplion.cplusschool.Bean.TestListBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommonPopupWindow;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/4/21.
 * @des 考试安排
 * @updatetime 2016-6-12 调试考试安排接口
 */
public class TestListActivity extends BaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener {
    private AbPullToRefreshView abPullToRefreshView;//下拉刷新
    private LinearLayout tv_title;//提示
    private ListView lv_testlist;//即将开始的考试列表
    private ImageView iv_test_return;//返回键
    private RelativeLayout rl_nodata;//没有数据时显示
//    private LinearLayout ll_test_term;//选择学期布局
    private TestListAdapter testListAdapter;//适配器
    private List<TestBean> mlist; //考试安排列表
    private AbHttpUtil abHttpUtil;
    private TextView tv_select_term;//选择学期
    private List<CommonBean> termList;//学期
    private CommonPopupWindow cPWindow;//
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlist);
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        mlist = new ArrayList<TestBean>();
        abHttpUtil = AbHttpUtil.getInstance(this);
        cPWindow = new CommonPopupWindow(this);
        share = new SharePreferenceUtils(this);
        tv_title = (LinearLayout) findViewById(R.id.tv_title);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tv_title.setVisibility(View.GONE);
            }
        };
        new Handler().postDelayed(runnable, 5000);
        iv_test_return = (ImageView) findViewById(R.id.iv_test_return);
        abPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.abPullToRefreshView);
        lv_testlist = (ListView) findViewById(R.id.lv_testlist);
        tv_select_term = (TextView) findViewById(R.id.tv_select_term);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
//        ll_test_term = (LinearLayout) findViewById(R.id.ll_test_term);
        termList = new ArrayList<CommonBean>();
        abPullToRefreshView.setOnHeaderRefreshListener(this);
        abPullToRefreshView.getFooterView().setVisibility(View.GONE);
        abPullToRefreshView.setLoadMoreEnable(false);
        testListAdapter = new TestListAdapter(this, mlist);
        lv_testlist.setAdapter(testListAdapter);
        setListener();
        RelativeLayout rl_nodata=(RelativeLayout)findViewById(R.id.rl_nodata);
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();

        //返回键
        iv_test_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //选择学期
        tv_select_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialogListview dialog_sex = new CustomDialogListview(TestListActivity.this, "选择学期", termList, tv_select_term.getText().toString());
                dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_select_term.setText(termList.get(position).getDes());
                        getTestList();
                        dialog_sex.dismiss();
                    }
                });
                dialog_sex.show();
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        AbTask abTask = AbTask.newInstance();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void update() {
                super.update();
//                getTermList();
                getTestList();
            }
        });
        abTask.execute(item);
    }
    //获取学期列表
    private void getTermList(){
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("getTermsAboutScore")+Constants.BASEPARAMS+"&stuNo="+share.getString("ROLE_ID","");
        abHttpUtil.get(url, new CallBackParent(TestListActivity.this,getResources().getString(R.string.loading),"termlistTag") {
            @Override
            public void Get_Result(String result) {
                Log.e("termlist",result+"");
                if(!TextUtils.isEmpty(result)){
//                    ll_test_term.setVisibility(View.VISIBLE);
                    TermListBean termListBean = AbJsonUtil.fromJson(result, TermListBean.class);
                    termList = new ArrayList<CommonBean>();
                    termList.add(new CommonBean("","所有学期"));
                    if(termListBean.getData()!=null&&termListBean.getData().size()>0){
                        for (int i=0;i<termListBean.getData().size();i++){
                            termList.add(new CommonBean(termListBean.getData().get(i).getXNXQDM(),termListBean.getData().get(i).getXNXQMC()));
                        }
                    }
                }
            }
        });
    }
    //获取考试安排列表
    private void getTestList() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getStudentExamInformation")
                + Constants.BASEPARAMS + "&stuNo="+share.getString("ROLE_ID","");
        abHttpUtil.post(url, new CallBackParent(TestListActivity.this, "正在加载，请稍后...", "TestListActivity") {
            @Override
            public void Get_Result(String result) {
                Log.e("result",result);
                TestListBean testListBean = AbJsonUtil.fromJson(result, TestListBean.class);
                if (testListBean.getCode().equals(CacheConstants.LOCAL_SUCCESS) || testListBean.getCode().equals(CacheConstants.SAM_SUCCESS)) {
                    if(testListBean.getData()!=null&&testListBean.getData().size()>0){
                        abPullToRefreshView.setVisibility(View.VISIBLE);
//                        ll_test_term.setVisibility(View.VISIBLE);
                        rl_nodata.setVisibility(View.GONE);
                        mlist = testListBean.getData();
                        testListAdapter.setMlist(mlist);
                        if(testListBean.getCurrenttime().length()==10){
                            testListBean.setCurrenttime(testListBean.getCurrenttime()+"000");
                        }
                        testListAdapter.setSystemCurrenttime(Long.parseLong(testListBean.getCurrenttime()));
                        testListAdapter.notifyDataSetChanged();
                    }else{
                        rl_nodata.setVisibility(View.VISIBLE);
                        abPullToRefreshView.setVisibility(View.GONE);
                    }
                } else {

                    rl_nodata.setVisibility(View.VISIBLE);
                    abPullToRefreshView.setVisibility(View.GONE);
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                rl_nodata.setVisibility(View.VISIBLE);
                abPullToRefreshView.setVisibility(View.GONE);
                abPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                abPullToRefreshView.onHeaderRefreshFinish();
            }
        });
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        getTestList();
    }
}
