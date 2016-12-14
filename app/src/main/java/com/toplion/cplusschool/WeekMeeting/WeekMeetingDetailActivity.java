package com.toplion.cplusschool.WeekMeeting;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDateUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.WeekMeetingBean;
import com.toplion.cplusschool.Bean.WeekmeetingListBean;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CalendarUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wang
 * on 2016/9/9.
 * 每周会议
 */
public class WeekMeetingDetailActivity extends BaseActivity {
    private ImageView iv_weekmeeting_back;
    private TextView tv_weekmeeting_detail_title;//标题
    private TextView tv_weelmeeting_bz;//备注
    private ListView lv_meeting_detail_list;//每周会议
    private RelativeLayout rl_nodata;
    private ImageView iv_dis;//没有数据显示的界面
    private AbHttpUtil abHttpUtil;
    private List<WeekMeetingBean> wlist;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekmeeting_detail);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        type = getIntent().getIntExtra("type", 0);
        tv_weekmeeting_detail_title = (TextView) findViewById(R.id.tv_weekmeeting_detail_title);
        tv_weelmeeting_bz = (TextView) findViewById(R.id.tv_weelmeeting_bz);
        String bz = getIntent().getStringExtra("bz");
        if(!TextUtils.isEmpty(bz)){
            tv_weelmeeting_bz.setText(bz);
            tv_weelmeeting_bz.setVisibility(View.VISIBLE);
        }else{
            tv_weelmeeting_bz.setVisibility(View.GONE);
        }
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tv_weekmeeting_detail_title.setText(title);
        }else{
            if(type == 1){
                tv_weekmeeting_detail_title.setText("周会议列表");
            }else if(type == 2){
                tv_weekmeeting_detail_title.setText("报告厅列表");
            }
        }
        iv_weekmeeting_back = (ImageView) findViewById(R.id.iv_weekmeeting_back);
        lv_meeting_detail_list = (ListView) findViewById(R.id.lv_meeting_detail_list);
        iv_dis = (ImageView) findViewById(R.id.iv_dis);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showMeetOrReportInfoById") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("type", type);
        params.put("uid", getIntent().getStringExtra("uid"));
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading), "showMeetOrReportInfoById") {
            @Override
            public void Get_Result(String result) {
                WeekmeetingListBean wlistbean = AbJsonUtil.fromJson(result, WeekmeetingListBean.class);
                if (wlistbean.getData() != null && wlistbean.getData().size() > 0) {
                    lv_meeting_detail_list.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    wlist = new ArrayList<WeekMeetingBean>();
                    wlist = wlistbean.getData();
                    WeekMeetingAdapter wmadapter = new WeekMeetingAdapter(WeekMeetingDetailActivity.this, wlist);
                    lv_meeting_detail_list.setAdapter(wmadapter);
                } else {
                    lv_meeting_detail_list.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                }
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
        iv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private class WeekMeetingAdapter extends BaseAdapter {
        private List<WeekMeetingBean> mlist;
        private Context mcontext;
        private SharePreferenceUtils share;

        public WeekMeetingAdapter(Context context, List<WeekMeetingBean> maps) {
            this.mcontext = context;
            this.mlist = maps;
            share = new SharePreferenceUtils(mcontext);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            final WeekMeetingBean mbean;
            Calendar cld;//日期
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mcontext, R.layout.weekmeeting_detail_item, null);
                viewHolder.tv_wm_detail_title = (TextView) convertView.findViewById(R.id.tv_wm_detail_title);
                viewHolder.tv_wm_detail_time = (TextView) convertView.findViewById(R.id.tv_wm_detail_time);
                viewHolder.tv_wm_detail_location = (TextView) convertView.findViewById(R.id.tv_wm_detail_location);
                viewHolder.tv_wmg_detail_host = (TextView) convertView.findViewById(R.id.tv_wmg_detail_host);
                viewHolder.tv_wm_detail_department = (TextView) convertView.findViewById(R.id.tv_wm_detail_department);
                viewHolder.tv_wm_detail_group = (TextView) convertView.findViewById(R.id.tv_wm_detail_group);
                viewHolder.tv_wm_detail_g = (TextView) convertView.findViewById(R.id.tv_wm_detail_g);
                viewHolder.btn_wm_detail_closeoropen = (ImageButton) convertView.findViewById(R.id.btn_wm_detail_closeoropen);
                viewHolder.rl_weekmeeting_tixing = (RelativeLayout) convertView.findViewById(R.id.rl_weekmeeting_tixing);
                viewHolder.v_weekmeeting_line = convertView.findViewById(R.id.v_weekmeeting_line);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            mbean = mlist.get(position);
            viewHolder.tv_wm_detail_title.setText("周" + mlist.get(position).getLogWeek() + ":" + mlist.get(position).getLogName());
            viewHolder.tv_wm_detail_time.setText("会议时间：" + mlist.get(position).getLogDate() + " " + mlist.get(position).getLogTime());
            viewHolder.tv_wm_detail_location.setText("会议地点：" + mlist.get(position).getLogAddress());
            viewHolder.tv_wmg_detail_host.setText("主持人：" + mlist.get(position).getLogHost());
            viewHolder.tv_wm_detail_department.setText("承办单位：" + mlist.get(position).getLogCompany());
            viewHolder.tv_wm_detail_group.setText(mlist.get(position).getLogJoined());
            String evenId = share.getString(mlist.get(position).getLogId(), "");
            if (!TextUtils.isEmpty(evenId)) {
                if(CalendarUtils.queryCalendarById(mcontext,evenId)){
                    mbean.setRemind(true);
                }else{
                    share.put(mlist.get(position).getLogId(), "");
                    mbean.setRemind(false);
                }
            } else {
                mbean.setRemind(false);
                cld = Calendar.getInstance();
                cld.setTime(new Date());
                Date nowDate = cld.getTime();
                try{
                    String date = mlist.get(position).getLogDate() + " " + mlist.get(position).getLogTime();
                    cld.setTime(AbDateUtil.getDateByFormat(date, "yyyy-MM-dd HH:mm"));
                }catch (Exception e){
                    cld.set(Calendar.HOUR_OF_DAY, 0);
                }
                Date beginTime = cld.getTime();
                if(beginTime.after(nowDate)||beginTime.equals(nowDate)){
                    viewHolder.rl_weekmeeting_tixing.setVisibility(View.VISIBLE);
                    viewHolder.v_weekmeeting_line.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.rl_weekmeeting_tixing.setVisibility(View.GONE);
                    viewHolder.v_weekmeeting_line.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(evenId)&&CalendarUtils.queryCalendarById(mcontext,evenId)){
                        CalendarUtils.deleteCalendar(mcontext,share.getString(mlist.get(position).getLogId(), ""));
                        mbean.setRemind(false);
                        share.put(mlist.get(position).getLogId(), "");
                    }
                }
            }
            viewHolder.btn_wm_detail_closeoropen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mbean.isRemind()) {
                        final CustomDialog dialog = new CustomDialog(mcontext);
                        dialog.setlinecolor();
                        dialog.setTitle("提示");
                        dialog.setContentboolean(true);
                        dialog.setDetial("确认要删除提醒吗?");
                        dialog.setLeftText("确定");
                        dialog.setRightText("取消");
                        dialog.setLeftOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                if(!TextUtils.isEmpty(share.getString(mlist.get(position).getLogId(), ""))){
                                    int rows = CalendarUtils.deleteCalendar(mcontext,share.getString(mlist.get(position).getLogId(), ""));
                                    if(rows!=-1){
                                        mbean.setRemind(false);
                                        share.put(mlist.get(position).getLogId(), "");
                                        showDialog("当前日历提醒已删除");
                                    }else{
                                        showDialog("当前日历提醒删除失败");
                                    }
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.setRightOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        String title = "周" + mlist.get(position).getLogWeek() + ":" + mlist.get(position).getLogName();
                        String des = mlist.get(position).getLogName() + "\n" + "会议时间：" + mlist.get(position).getLogTime()
                                + "\n" + "会议地点：" + mlist.get(position).getLogAddress();
                        String time = mlist.get(position).getLogDate() + " " + mlist.get(position).getLogTime();
                        long evenId = -1;
                        try{
                            evenId = CalendarUtils.addCalendar(mcontext, mlist.get(position).getLogId(), title, time, mlist.get(position).getLogAddress(), des);
                        }catch (Exception e){
                            e.printStackTrace();
                            evenId = -1;
                        }
                        Log.e("evenId", evenId + "");
                        if (evenId != -1) {
                            showDialog("提醒服务已成功加入手机日历");
                            mbean.setRemind(true);
                            share.put(mlist.get(position).getLogId(), evenId + "");
                        } else {
                            mbean.setRemind(false);
                            ToastManager.getInstance().showToast(mcontext,"提醒服务加入手机日历失败,请确认您的手机日历是否可以添加提醒事件");
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            if (mlist.get(position).isRemind()) {
                setRedColor(viewHolder);
            } else {
                setBlackColor(viewHolder);
            }
            return convertView;
        }

        private void showDialog(String msg) {
            final CommDialog dia = new CommDialog(mcontext);
            dia.CreateDialogOnlyOk("系统提示", "确定", msg, new CommDialog.CallBack() {
                @Override
                public void isConfirm(boolean flag) {
                    notifyDataSetChanged();
                    dia.cancelDialog();
                }
            });
        }

        private void setRedColor(ViewHolder viewHolder) {
            viewHolder.btn_wm_detail_closeoropen.setBackgroundResource(R.mipmap.btn_on2);
            viewHolder.tv_wm_detail_title.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wm_detail_time.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wm_detail_location.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wmg_detail_host.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wm_detail_department.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wm_detail_group.setTextColor(getResources().getColor(R.color.anhong));
            viewHolder.tv_wm_detail_g.setTextColor(getResources().getColor(R.color.anhong));
        }

        private void setBlackColor(ViewHolder viewHolder) {
            viewHolder.btn_wm_detail_closeoropen.setBackgroundResource(R.mipmap.btn_off2);
            viewHolder.tv_wm_detail_title.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wm_detail_time.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wm_detail_location.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wmg_detail_host.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wm_detail_department.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wm_detail_group.setTextColor(getResources().getColor(R.color.black));
            viewHolder.tv_wm_detail_g.setTextColor(getResources().getColor(R.color.black));
        }

        class ViewHolder {
            private TextView tv_wm_detail_title;//标题
            private TextView tv_wm_detail_time;//时间
            private TextView tv_wm_detail_location;//地点
            private TextView tv_wmg_detail_host;//主持人
            private TextView tv_wm_detail_department;//部门
            private TextView tv_wm_detail_group;//参加人员
            private TextView tv_wm_detail_g;
            private ImageButton btn_wm_detail_closeoropen;//开关提醒
            private RelativeLayout rl_weekmeeting_tixing;
            private View v_weekmeeting_line;
        }
    }
}
