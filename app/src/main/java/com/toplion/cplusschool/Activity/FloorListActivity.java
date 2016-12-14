package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Adapter.FloorListAdapter;
import com.toplion.cplusschool.Bean.FloorBean;
import com.toplion.cplusschool.Bean.FloorListBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wang on 2016/4/20.
 * @des 教学楼列表
 * @version 1.1.1
 * @updatetime 2016-6-12 调试教学楼列表接口
 */
public class FloorListActivity extends BaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener{
    private ImageView iv_floor_return;//返回键
    private AbPullToRefreshView arv_floor_refreshview;//下拉刷新
    private List<FloorBean> flist;//列表
    private AbHttpUtil abHttpUtil;
    private FloorListAdapter floorListAdapter;
    private GridView lv_floorlist;// 列表
    private ImageView iv_leftdate;//减1天
    private ImageView iv_rightdate;//加1天
    private TextView tv_textdate;//当前时间
    private Calendar cld;//日期
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    private Date nowDate;//当前时间
    private String requesttime = "";//当前选择的时间
    private TextView tv_test_title;//提示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floorlist);
        init();
    }

    //初始化布局
    @Override
    protected void init() {
        super.init();
        flist = new ArrayList<FloorBean>();
        tv_test_title = (TextView) findViewById(R.id.tv_test_title);
        abHttpUtil = AbHttpUtil.getInstance(this);
        lv_floorlist = (GridView) findViewById(R.id.lv_floorlist);
        iv_leftdate = (ImageView) findViewById(R.id.iv_leftdate);
        iv_rightdate = (ImageView) findViewById(R.id.iv_rightdate);
        tv_textdate = (TextView) findViewById(R.id.tv_textdate);
        cld = Calendar.getInstance();
        cld.setTime(new Date());
        nowDate = cld.getTime();

        requesttime = cld.getTimeInMillis()+"";
        tv_textdate.setText(sdf.format(cld.getTime()));
        floorListAdapter = new FloorListAdapter(this,flist);
        lv_floorlist.setAdapter(floorListAdapter);
        iv_floor_return = (ImageView) findViewById(R.id.iv_floor_return);
        arv_floor_refreshview = (AbPullToRefreshView) findViewById(R.id.arv_floor_refreshview);
        arv_floor_refreshview.setOnHeaderRefreshListener(this);
        arv_floor_refreshview.setLoadMoreEnable(false);
        arv_floor_refreshview.getFooterView().setVisibility(View.GONE);
        // 设置进度条的样式
        arv_floor_refreshview.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        getData();
        setListener();
        ToastManager.getInstance().getTvTitle(tv_test_title,"");
    }


    //设置点击事件
    @Override
    protected void setListener() {
        super.setListener();
        //左按钮
        iv_leftdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日期减一天
                cld.add(Calendar.DAY_OF_MONTH ,-1);
                tv_textdate.setText(sdf.format(cld.getTime()));
                Date leftDate = cld.getTime();
                requesttime = cld.getTimeInMillis()+"";
                Log.e("leftDate",requesttime+"---"+leftDate.getTime());
                if(leftDate.equals(nowDate)){
                    //小于今天 不显示左边按钮
                    iv_leftdate.setVisibility(View.INVISIBLE);
                }else{
                    iv_leftdate.setVisibility(View.VISIBLE);
                }
                getFloorList(requesttime);
            }
        });
        //右按钮
        iv_rightdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日期加一天
                cld.add(Calendar.DAY_OF_MONTH ,1);
                tv_textdate.setText(sdf.format(cld.getTime()));
                Date rightDate = cld.getTime();
                requesttime = cld.getTimeInMillis()+"";
                Log.e("rightDate",requesttime+"---"+rightDate.getTime());
                if(rightDate.after(nowDate)){
                    //大于今天显示左边按钮
                    iv_leftdate.setVisibility(View.VISIBLE);
                }
                getFloorList(requesttime);
            }
        });
        //返回键
        iv_floor_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //列表点击事件
        lv_floorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fid = flist.get((int)id).getJXLH();
                String fname = flist.get((int)id).getJXLM();
                String fnum = flist.get((int)id).getLCZS();
                Log.e("fid===>",fid+" === "+fname);
                Intent intent = new Intent(FloorListActivity.this,EmptyClassRoomActivity.class);
                intent.putExtra("fid",fid);
                intent.putExtra("fname",fname);
                intent.putExtra("fnumber",fnum);//楼层总数
                intent.putExtra("requesttime",requesttime);//选择的当前时间
                startActivity(intent);
            }
        });
    }
    //初始化数据
    @Override
    protected void getData() {
        super.getData();
        AbTask task = AbTask.newInstance();
        AbTaskItem taskItem = new AbTaskItem();
        taskItem.setListener(new AbTaskListener(){
            @Override
            public void update() {
                super.update();
                getFloorList(requesttime);
            }
        });
        task.execute(taskItem);
    }

    //获取教学楼列表
    private void getFloorList(String timeStr){
        if(!TextUtils.isEmpty(timeStr)){
            timeStr = timeStr.substring(0,10);
        }
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getEmptyClassroomWithCount")
                + Constants.BASEPARAMS + "&requesttime=" + timeStr;
        Log.e("url",url+"");
        abHttpUtil.post(url, new CallBackParent(FloorListActivity.this,getResources().getString(R.string.loading),"FloorListActivity") {
            @Override
            public void Get_Result(String result) {
                FloorListBean fListBean = AbJsonUtil.fromJson(result,FloorListBean.class);
                if(fListBean.getCode().equals(CacheConstants.LOCAL_SUCCESS) || fListBean.getCode().equals(CacheConstants.SAM_SUCCESS)){
                    flist = fListBean.getData();
                    floorListAdapter.setMlist(flist);
                    floorListAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                arv_floor_refreshview.onHeaderRefreshFinish();
            }
        });
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        getFloorList(requesttime);
    }
}
