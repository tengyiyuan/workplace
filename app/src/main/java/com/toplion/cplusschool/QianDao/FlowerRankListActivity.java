package com.toplion.cplusschool.QianDao;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/10/31.
 *
 * @des 鲜花排行榜
 */

public class FlowerRankListActivity extends BaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener, AbPullToRefreshView.OnFooterLoadListener {
    private ImageView iv_flist_back;//返回键
    private AbPullToRefreshView abPullToRefreshView;
    private ListView lv_flower_rank_list;//列表控件
    private AbHttpUtil abHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flower_rank_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_flist_back = (ImageView) findViewById(R.id.iv_flist_back);
        abPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.abPullToRefreshView);
        abPullToRefreshView.setOnHeaderRefreshListener(this);
        abPullToRefreshView.setOnFooterLoadListener(this);
        // 设置进度条的样式
        abPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        abPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        lv_flower_rank_list = (ListView) findViewById(R.id.lv_flower_rank_list);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        List<String> list = new ArrayList<String>();
        for(int i=0;i<200;i++){
            list.add("山东建筑大学"+i);
        }
        lv_flower_rank_list.setAdapter(new MyAdapter(list));
    }


    @Override
    protected void setListener() {
        super.setListener();
        iv_flist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onFooterLoad(AbPullToRefreshView view) {
        abPullToRefreshView.onFooterLoadFinish();

    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        abPullToRefreshView.onHeaderRefreshFinish();
    }



    private class MyAdapter extends BaseAdapter{
        private List<String> mlist;

        public MyAdapter(List<String> list){
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(FlowerRankListActivity.this,R.layout.flower_rank_list_item,null);
                viewHolder.iv_flower_icon = (ImageView) convertView.findViewById(R.id.iv_flower_icon);
                viewHolder.tv_flower_nickname = (TextView) convertView.findViewById(R.id.tv_flower_nickname);
                viewHolder.tv_flower_school = (TextView) convertView.findViewById(R.id.tv_flower_school);
                viewHolder.iv_flower_mc = (ImageView) convertView.findViewById(R.id.iv_flower_mc);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_flower_school.setText(mlist.get(position));
            viewHolder.iv_flower_mc.setImageResource(getResource(position));
            return convertView;
        }

        class ViewHolder{
            private ImageView iv_flower_icon;
            private TextView tv_flower_nickname;
            private TextView tv_flower_school;
            private TextView tv_flower_number;
            private ImageView iv_flower_mc;
        }

        private int getResource(int i){
            int resId = 0;
            switch (i){
                case 0:
                    resId = R.mipmap.flower_one;
                    break;
                case 1:
                    resId = R.mipmap.flower_two;
                    break;
                case 2:
                    resId = R.mipmap.flower_three;
                    break;
                case 3:
                    resId = R.mipmap.flower_four;
                    break;
                case 4:
                    resId = R.mipmap.flower_five;
                    break;
                case 5:
                    resId = R.mipmap.flower_six;
                    break;
                case 6:
                    resId = R.mipmap.flower_seven;
                    break;
                case 7:
                    resId = R.mipmap.flower_eight;
                    break;
                case 8:
                    resId = R.mipmap.flower_nine;
                    break;
                case 9:
                    resId = R.mipmap.flower_ten;
                    break;
            }
            return resId;
        }
    }
}
