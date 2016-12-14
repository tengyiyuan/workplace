package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/8/4.
 * @des 选择报销预约时间
 */
public class BaoTimeActivity extends BaseActivity {
    private ListView listView1;
    private TextView newtitle;
    private TextView text1;
    private TextView text2;
    private List<String> list, list1, list2;
    private MyAdapter myAdapter;
    private int type = 1;
    private ImageView back;
    private int intCode=0;
    private AbHttpUtil abHttpUtil;
    private String riid = null;//预约单号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typemain);
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        intCode=getIntent().getIntExtra("code",0);
        riid = getIntent().getStringExtra("riid");
        back = (ImageView) findViewById(R.id.back);
        listView1 = (ListView) findViewById(R.id.listView1);
        newtitle = (TextView) findViewById(R.id.newtitle);
        newtitle.setText("选择时间");
        text1 = (TextView) findViewById(R.id.txt_1);
        text2 = (TextView) findViewById(R.id.txt_2);
        list = new ArrayList<String>();
        myAdapter = new MyAdapter();
        listView1.setAdapter(myAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String obj = list.get(position);
                switch (type) {
                    case 1:
                        getTimeByDay(obj);
                        break;
                    case 2:
                        Intent intent = new Intent();
                        intent.putExtra("datetime",obj+"");
                        intent.putExtra("dateweek",text1.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                }
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list1;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 1;
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list2;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 2;
                text2.setVisibility(View.GONE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1){
                    finish();
                }else{
                    list = list1;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 1;
                    text1.setVisibility(View.GONE);
                    text2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("getTimeRanges")+Constants.BASEPARAMS+"&bookid="+riid;
        abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"getTimeRanges") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    list1 = new ArrayList<String>();
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        list1.add(jsonObject1.getString("DAYS"));
                    }
                    getDays();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //获取日期
    public void getDays() {
        list = list1;
        myAdapter.notifyDataSetChanged();
        listGoTop();
    }
    //获取日期下的时间段
    public void getTimeByDay(final String code) {
        String url = Constants.BASE_URL+"?rid="+ ReturnUtils.encode("getBooktimeWithdate")+Constants.BASEPARAMS+"&bookid="+riid+"&timedate="+code;
       abHttpUtil.get(url, new CallBackParent(this,getResources().getString(R.string.loading),"getBooktimeWithdate") {
           @Override
           public void Get_Result(String result) {
               try {
                   JSONObject jsonObject = new JSONObject(result);
                   list2 = new ArrayList<String>();
                   JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                   for (int i = 0;i<jsonArray.length();i++){
                       JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                       long time = jsonObject1.getLong("S_TIME");
                       String timeStr = TimeUtils.timeStampToDate(time+"","HH:mm");
                       list2.add(timeStr);
                   }
                   if (list2.size() > 0) {
                       text1.setText(code);
                       text1.setVisibility(View.VISIBLE);
                       list = list2;
                       myAdapter.notifyDataSetChanged();
                       listGoTop();
                       type = 2;
                   } else {
                       Intent intent = new Intent();
                       intent.putExtra("datetime",code);
                       intent.putExtra("dateweek",text1.getText().toString());
                       setResult(RESULT_OK,intent);
                       finish();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
    }

    private void listGoTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView1.setSelection(0);
            }
        }, 200);

    }
    @Override
    public void onBackPressed() {
        if(type == 1){
            finish();
        }else{
            list = list1;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 1;
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK)
            switch (requestCode){
                case 0:
                    break;
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class MyAdapter extends BaseAdapter {
        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text_content = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(BaoTimeActivity.this).inflate(
                        R.layout.single_select, null);
                text_content = (TextView) convertView.findViewById(R.id.txt_1);
                convertView.setTag(text_content);
            } else {
                text_content = (TextView) convertView.getTag();
            }
            text_content.setText(list.get(position));
            return convertView;
        }
    }
}
