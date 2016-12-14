package com.toplion.cplusschool.WeekMeeting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshengbo
 * on 2016/9/8.
 * @周会议
 */
public class WeekMeetingListActivity extends BaseActivity{
    private ImageView iv_weekmeeting_back;//返回键
    private ListView lv_meeting_list;//列表
    private TextView tv_weekmeeting_title;//标题
    private ImageView iv_dis;
    private RelativeLayout rl_nodata;//没有数据时显示界面
    private AbHttpUtil abHttpUtil;
    private List<Map<String,String>> list;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekmeeting_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        type = getIntent().getIntExtra("type",0);
        iv_weekmeeting_back = (ImageView) findViewById(R.id.iv_weekmeeting_back);
        lv_meeting_list = (ListView) findViewById(R.id.lv_meeting_list);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        iv_dis = (ImageView) findViewById(R.id.iv_dis);
        rl_nodata.setVisibility(View.GONE);

        tv_weekmeeting_title = (TextView) findViewById(R.id.tv_weekmeeting_title);
        if(type == 1){
            tv_weekmeeting_title.setText("周会议列表");
        }else if(type == 2){
            tv_weekmeeting_title.setText("报告厅列表");
        }
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("showMeetOrReportByInput")+Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("type",type);
        abHttpUtil.post(url,params, new CallBackParent(this,getResources().getString(R.string.loading),"showMeetOrReportByInput") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(json.getString("data"));
                    list = new ArrayList<Map<String, String>>();
                    if(jsonArray!=null&&jsonArray.length()>0){
                        for (int i= 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("id",jsonObject.getString("id"));
                            String title = jsonObject.getString("title");
                            title = title.replace("（",",").replace("）",",");
                            String [] titles = title.split(",");
                            if(titles.length>0){
                                if(titles.length>2){
                                    map.put("title",titles[0]+titles[2]);
                                }else{
                                    map.put("title",titles[0]);
                                }
                                if(titles.length>1){
                                    map.put("time",titles[1]);
                                }
                            }
                            map.put("bz",jsonObject.getString("bz"));
                            list.add(map);
                        }
                        WeekMeetingAdapter wadapter = new WeekMeetingAdapter(WeekMeetingListActivity.this,list);
                        lv_meeting_list.setAdapter(wadapter);
                    }else{
                        rl_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rl_nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                rl_nodata.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                super.onFailure(statusCode, content, error);
                rl_nodata.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_weekmeeting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_meeting_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeekMeetingListActivity.this,WeekMeetingDetailActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("uid",list.get(position).get("id"));
                intent.putExtra("title",list.get(position).get("title"));
                intent.putExtra("bz",list.get(position).get("bz"));
                startActivity(intent);
            }
        });
        iv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }


    private class WeekMeetingAdapter extends BaseAdapter{
        private List<Map<String,String>> mlist;
        private Context mcontext;
        public WeekMeetingAdapter(Context context,List<Map<String,String>> maps){
            this.mcontext = context;
            this.mlist = maps;
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
             ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(mcontext, R.layout.weekmeeting_list_item,null);
                viewHolder.tv_weekmeeting_title = (TextView) convertView.findViewById(R.id.tv_weekmeeting_title);
                viewHolder.tv_weekmeeting_time = (TextView) convertView.findViewById(R.id.tv_weekmeeting_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_weekmeeting_title.setText(mlist.get(position).get("title"));
            viewHolder.tv_weekmeeting_time.setText(mlist.get(position).get("time"));
            return convertView;
        }
        class ViewHolder{
            private TextView tv_weekmeeting_title;//标题
            private TextView tv_weekmeeting_time;//时间
        }
    }
}
