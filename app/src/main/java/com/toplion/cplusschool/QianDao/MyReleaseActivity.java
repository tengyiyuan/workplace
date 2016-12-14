package com.toplion.cplusschool.QianDao;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/10/31.
 * @des   我的发布
 */

public class MyReleaseActivity extends BaseActivity{
    private ImageView iv_my_release_back;
    private GridView gv_my_release_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_flower_release_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        iv_my_release_back = (ImageView) findViewById(R.id.iv_my_release_back);
        gv_my_release_list = (GridView) findViewById(R.id.gv_my_release_list);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        List<String> list = new ArrayList<String>();
        for (int i=0;i<20;i++){
            list.add(""+i);
        }
        gv_my_release_list.setAdapter(new MyAdapter(list));
    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_my_release_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
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
                convertView = View.inflate(MyReleaseActivity.this,R.layout.my_flower_release_list_item,null);
                viewHolder.iv_fr_icon = (ImageView) convertView.findViewById(R.id.iv_fr_icon);
                viewHolder.tv_my_fr_number = (TextView) convertView.findViewById(R.id.tv_my_fr_number);
                viewHolder.tv_my_fr_lnumber = (TextView) convertView.findViewById(R.id.tv_my_fr_lnumber);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            viewHolder.tv_my_fr_number.setText(mlist.get(position));
            return convertView;
        }

        class ViewHolder{
            private ImageView iv_fr_icon;
            private TextView tv_my_fr_number;
            private TextView tv_my_fr_lnumber;
        }
    }
}
