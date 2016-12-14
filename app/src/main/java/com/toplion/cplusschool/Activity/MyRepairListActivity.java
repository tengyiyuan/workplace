package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Adapter.MyRepairterListAdapter;
import com.toplion.cplusschool.Bean.RepairInfoBean;
import com.toplion.cplusschool.Bean.RepairListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/7/1.
 *
 * @author wang
 * 我的报修列表
 */
public class MyRepairListActivity extends BaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener, AbPullToRefreshView.OnFooterLoadListener {
    private ImageView iv_myrepairter_return;//返回键
    private AbPullToRefreshView arv_myrepair_refreshview;//上拉下拉刷新
    private ListView lv_myrepariter_list;//我的报修列表布局
    private MyRepairterListAdapter rListAdapter;//适配器
    private List<RepairInfoBean> rlist;//列表
    private AbHttpUtil abHttpUtil;//网络请求工具
    private SharePreferenceUtils share;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_repairter_list);
        init();
        setListener();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.isRefresh) {//判断是否需要刷新列表
            Log.e("onResume", "gogogogogogogo");
            getData();
        }
    }

    @Override
    protected void init() {
        super.init();
        rlist = new ArrayList<RepairInfoBean>();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        iv_myrepairter_return = (ImageView) findViewById(R.id.iv_myrepairter_return);
        arv_myrepair_refreshview = (AbPullToRefreshView) findViewById(R.id.arv_myrepair_refreshview);
        lv_myrepariter_list = (ListView) findViewById(R.id.lv_myrepariter_list);
        arv_myrepair_refreshview.setOnHeaderRefreshListener(this);
        arv_myrepair_refreshview.setOnFooterLoadListener(this);
        // 设置进度条的样式
        arv_myrepair_refreshview.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        arv_myrepair_refreshview.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        rListAdapter = new MyRepairterListAdapter(MyRepairListActivity.this, rlist);
        lv_myrepariter_list.setAdapter(rListAdapter);

    }

    @Override
    protected void getData() {
        super.getData();
//        AbTask task = AbTask.newInstance();
//        AbTaskItem taskItem = new AbTaskItem();
//        taskItem.setListener(new AbTaskListener() {
//            @Override
//            public void get() {
//                super.get();
//            }
//
//            @Override
//            public void update() {
//                super.update();
                getRepairList();
//            }
//        });
//        task.execute(taskItem);
    }
    //获取我的报修列表
    private void getRepairList() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getRepairinfoList") + Constants.BASEPARAMS+"&stuNo="+share.getString("ROLE_ID", "");
        abHttpUtil.get(url, new CallBackParent(MyRepairListActivity.this,getResources().getString(R.string.loading),"getRepairinfoList") {
            @Override
            public void Get_Result(String result) {
                Log.e("reapirresult",result);
                try {
                    RepairListBean repairListBean = AbJsonUtil.fromJson(result, RepairListBean.class);
                    Constants.isRefresh = false;//刷新成功后,取消刷新
                    if (repairListBean.getData()!= null && repairListBean.getData().size() > 0) {
                        rlist = repairListBean.getData();
                        rListAdapter.setMlist(rlist);
                        rListAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFinish() {
                arv_myrepair_refreshview.onHeaderRefreshFinish();
                AbDialogUtil.removeDialog(MyRepairListActivity.this);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_myrepairter_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_myrepariter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyRepairListActivity.this, RepairDetailActivity.class);
                int rid  = rlist.get(position).getOIID();
                intent.putExtra("rId", rid);
                startActivityForResult(intent, Constants.REQUESTCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)//我的报修详情修改后返回,重新加载数据
            switch (requestCode) {
                case Constants.REQUESTCODE:
                    getData();
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        arv_myrepair_refreshview.onHeaderRefreshFinish();
    }

    @Override
    public void onFooterLoad(AbPullToRefreshView view) {
        arv_myrepair_refreshview.onFooterLoadFinish();
    }
}
