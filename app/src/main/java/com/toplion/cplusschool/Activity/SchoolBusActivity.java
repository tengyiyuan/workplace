package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Bean.Busbean;
import com.toplion.cplusschool.Bean.ParentBusbean;
import com.toplion.cplusschool.Bean.ParentXiaolibean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改校車校历改为接口调用的方式
 */
public class SchoolBusActivity extends BaseActivity {
    private ListView mylist;
    private List<Busbean> buslist;
    private MyAdapter myAdapter;
    private int style = 2;
    private TextView newtitle;
    private ImageView repair_question_info_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus);
        init();
        getData();
    }
    /**
     * 初始化各个组件
     */
    @Override
    protected void init() {
        super.init();
        repair_question_info_return = (ImageView) findViewById(R.id.repair_question_info_return);
        newtitle = (TextView) findViewById(R.id.newtitle);
        style = getIntent().getIntExtra("style", 2);
        buslist = new ArrayList<Busbean>();
        mylist = (ListView) findViewById(R.id.mylist);
        myAdapter = new MyAdapter();
        mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getOtherData(buslist.get(position).getCIID());
            }
        });
        if (style == 1) {
            newtitle.setText("校历");
        } else {
            newtitle.setText("校车服务");
        }
        repair_question_info_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 网络加载数据接口
     */
    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("type", style);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getSchoolBasicsInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在查询") {
            @Override
            public void Get_Result(String result) {
                ParentBusbean parentBusbean = AbJsonUtil.fromJson(result, ParentBusbean.class);
                buslist = parentBusbean.getData();
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getOtherData(int uid) {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("uid", uid);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showSchoolBasicsInfoById") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在查询") {
            @Override
            public void Get_Result(String result) {
                ParentXiaolibean xiaolibean = AbJsonUtil.fromJson(result, ParentXiaolibean.class);
                Intent intent = new Intent();
                intent.putExtra("title", xiaolibean.getData().getCititle());
                intent.putExtra("data", xiaolibean.getData().getCicontent());
                intent.setClass(SchoolBusActivity.this, CommonWebViewActivity.class);
                SchoolBusActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 列表适配器
     * au:tengyy
     * data:2016/8/17
     */
    private class MyAdapter extends BaseAdapter {
        public int getCount() {
            return buslist.size();
        }

        public Object getItem(int position) {
            return buslist.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SchoolBusActivity.this).inflate(
                        R.layout.busitem, null);
                viewHolder.text_name = (TextView) convertView.findViewById(R.id.busname);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text_name.setText(buslist.get(position).getCITITLE());
            return convertView;
        }
    }

    class ViewHolder {
        private TextView text_name;//名称
        private TextView text_num;//备用

    }
}
