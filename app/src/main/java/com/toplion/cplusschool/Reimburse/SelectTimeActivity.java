package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDateUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;
import com.toplion.cplusschool.widget.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/9/13.
 *
 * @预约报销选择时间
 */
public class SelectTimeActivity extends BaseActivity {
    private ImageView iv_select_time_back;//返回
    private TextView tv_select_time_title;//标题
    private GridView gv_select_time;//时间选择列表
    private TextView tv_select_datetime;//选中的日期
    private TextView tv_select_confirm_time;//确认
    private RelativeLayout rl_data;//有数据
    private RelativeLayout rl_nodata;//无数据
    private ImageView iv_dis;
    private AbHttpUtil abHttpUtil;
    private String riid = null;//预约单号
    private List<CommonBean> list1;//学期
    private List<TimeBean> tList;//可用时间列表
    private TextView tv_select_date;//
    private SelectTimeListAdapter timeAdapter;
    private int totalTime = 0;//时间总和
    private String selectDate = null;//当前选中的日期 如：2016-09-18
    private String selectTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxiaoyuyue_select_time);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        riid = getIntent().getStringExtra("riid");
        totalTime = getIntent().getIntExtra("totaltime", 0);
        rl_data = (RelativeLayout) findViewById(R.id.rl_data);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        iv_dis = (ImageView) findViewById(R.id.iv_dis);
        iv_select_time_back = (ImageView) findViewById(R.id.iv_select_time_back);
        tv_select_time_title = (TextView) findViewById(R.id.tv_select_time_title);
        tv_select_datetime = (TextView) findViewById(R.id.tv_select_datetime);
        tv_select_date = (TextView) findViewById(R.id.tv_select_date);
        gv_select_time = (GridView) findViewById(R.id.gv_select_time);
        tv_select_confirm_time = (TextView) findViewById(R.id.tv_select_confirm_time);
        tv_select_confirm_time.setEnabled(false);
        tList = new ArrayList<TimeBean>();
        timeAdapter = new SelectTimeListAdapter(this, tList);
        gv_select_time.setAdapter(timeAdapter);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getTimeRanges") + Constants.BASEPARAMS + "&bookid=" + riid;
        abHttpUtil.get(url, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    list1 = new ArrayList<CommonBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        list1.add(new CommonBean(i + "", AbDateUtil.getWeekNumber(jsonObject1.getString("DAYS"), "yyyy-MM-dd"), jsonObject1.getString("DAYS")));
                    }
                    if (list1 != null && list1.size() > 0) {
                        selectDate = list1.get(0).getDes();
                        tv_select_time_title.setText(selectDate + "  " + list1.get(0).getOther());
                        getIsAvailableTime(selectDate);
                        rl_data.setVisibility(View.VISIBLE);
                        rl_nodata.setVisibility(View.GONE);
                    } else {
                        rl_data.setVisibility(View.GONE);
                        rl_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rl_data.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                rl_data.setVisibility(View.GONE);
                rl_nodata.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                super.onFailure(statusCode, content, error);
                rl_data.setVisibility(View.GONE);
                rl_nodata.setVisibility(View.VISIBLE);
            }
        });
    }

    //根据日期获取可用时间范围
    private void getIsAvailableTime(String timedate) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getBooktimeWithdate") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("timedate", timedate);
        params.put("bookid", riid);
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading), "getBooktimeWithdate") {
            @Override
            public void Get_Result(String result) {
                Log.e("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (tList != null && tList.size() > 0) {
                        tList.clear();
                    }
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            Date start = TimeUtils.secondsToDate(jsonObject.getLong("S_TIME") + "");
                            Date end = TimeUtils.secondsToDate(jsonObject.getLong("E_TIME") + "");
                            Calendar sCalendar = Calendar.getInstance();
                            sCalendar.setTime(start);
                            Calendar eCalendar = Calendar.getInstance();
                            eCalendar.setTime(end);
                            while (sCalendar.getTime().before(eCalendar.getTime())) {
                                Log.e("startTime", sCalendar.getTime() + "---- end:" + eCalendar.getTime());
                                int hour = sCalendar.get(Calendar.HOUR_OF_DAY);// 当前时
                                int minute = sCalendar.get(Calendar.MINUTE); // 当前分
                                String stimeStr = hour + ":" + (minute == 0 ? "00" : minute);
                                TimeBean tbean = new TimeBean();
                                tbean.setTimeStr(stimeStr);
                                tbean.setChecked(false);
                                tList.add(tbean);
                                sCalendar.add(Calendar.MINUTE, 10);
                            }
                            int ehour = eCalendar.get(Calendar.HOUR_OF_DAY);// 当前时
                            int eminute = eCalendar.get(Calendar.MINUTE); // 当前分
                            String etimeStr = ehour + ":" + (eminute == 0 ? "00" : eminute);
                            TimeBean etbean = new TimeBean();
                            etbean.setTimeStr(etimeStr);
                            etbean.setChecked(false);
                            tList.add(etbean);
                        }
                    }
                    timeAdapter.setMlist(tList);
                    timeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置开始和结束时间
     */
    private void setStartAndEndTime() {
        String datetime = selectDate + " " + selectTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtils.stringToDate(datetime, "yyyy-MM-dd HH:mm"));
        calendar.add(Calendar.MINUTE, totalTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);// 当前时
        int minute = calendar.get(Calendar.MINUTE); // 当前分
        String time = hour + ":" + (minute == 0 ? "00" : minute);
        tv_select_datetime.setText("您已经选择了" + datetime + "-" + time + "作为您的报销时间");
        tv_select_confirm_time.setEnabled(true);
        tv_select_confirm_time.setText("就选它了");
    }

    @Override
    protected void setListener() {
        super.setListener();
        gv_select_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectTime = tList.get(position).getTimeStr();
                timeAdapter.setSelectItem(position);
                timeAdapter.notifyDataSetChanged();
                setStartAndEndTime();
            }
        });
        //选择日期
        tv_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list1 != null && list1.size() > 0) {
                    final CustomDialogListview dialog_sex = new CustomDialogListview(SelectTimeActivity.this, "选择学期", list1, tv_select_time_title.getText().toString());
                    CustomDialogListview.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_select_datetime.setText("您还未选择预约报销时间，赶紧点击选择吧!");
                            tv_select_confirm_time.setEnabled(false);
                            tv_select_confirm_time.setText("请选择时间");
                            timeAdapter.setSelectItem(-1);
                            timeAdapter.notifyDataSetChanged();
                            tv_select_time_title.setText(list1.get(position).getDes() + "  " + list1.get(position).getOther());
                            selectDate = list1.get(position).getDes();
                            getIsAvailableTime(selectDate);
                            dialog_sex.dismiss();
                        }
                    });
                    dialog_sex.show();
                }

            }
        });

        //确认选择时间
        tv_select_confirm_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("datetime", selectTime);
                intent.putExtra("dateweek", selectDate);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        iv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_select_time_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
