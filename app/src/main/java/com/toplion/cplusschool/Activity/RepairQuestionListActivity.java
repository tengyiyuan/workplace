package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.RepairQuestionListAdapter;
import com.toplion.cplusschool.Bean.RepairQuestionBean;
import com.toplion.cplusschool.Bean.RepairQuestionListBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * 报修常见问题及解决方案
 * @author wangshengbo
 * @updatetime 2016-6-30
 */
public class RepairQuestionListActivity extends BaseActivity {
    private SharePreferenceUtils share;
    private ListView question_info;    // 常见问题及解决方案列表
    private TextView tv_go_repair;
    private ImageView iv_return;       // 返回
    private AbHttpUtil abHttpUtil;//网络请求工具
    private RepairQuestionListAdapter repairQuestionListAdapter;//适配器
    private List<RepairQuestionBean> qlist;//问题列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_question_list);
        init();
    }

    //初始化布局
    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_return = (ImageView) this.findViewById(R.id.repair_question_info_return);
        question_info = (ListView) this.findViewById(R.id.repair_question_list);
        tv_go_repair = (TextView) findViewById(R.id.tv_go_repair);
        qlist = new ArrayList<RepairQuestionBean>();
        repairQuestionListAdapter = new RepairQuestionListAdapter(this, qlist);
        question_info.setAdapter(repairQuestionListAdapter);//设置adapter
        getData();
        setListener();
    }
    //获取问题列表数据
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getCommonFaultInfo")+Constants.BASEPARAMS;
        abHttpUtil.get(url, new CallBackParent(RepairQuestionListActivity.this,getResources().getString(R.string.loading),"RepairQuestionListActivity") {
            @Override
            public void Get_Result(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Log.e("content-------", result);
                    //解析服务器返回数据
                    RepairQuestionListBean rqBean = AbJsonUtil.fromJson(result, RepairQuestionListBean.class);
                    if (rqBean.getCode().equals(CacheConstants.LOCAL_SUCCESS) || rqBean.getCode().equals(CacheConstants.SAM_SUCCESS)) {
                        if (rqBean.getData() != null) {
                            qlist = rqBean.getData();
                            if (qlist.size() > 0) {
                                repairQuestionListAdapter.setMlist(qlist);
                                repairQuestionListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
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
        //去报修
        tv_go_repair.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairQuestionListActivity.this, AddRepairActivity.class);
                intent.putExtra("style", 2);
                startActivity(intent);
            }
        });
        //问题列表单个点击事件
        question_info.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                int qId = qlist.get(position).getCF_ID();
                Intent intent = new Intent(RepairQuestionListActivity.this, RepairQuestionDetailActivity.class);
                intent.putExtra("qId", qId+"");
                startActivity(intent);
            }
        });
    }
}
