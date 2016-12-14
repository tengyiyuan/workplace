package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbDateUtil;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.Bean.baoitem;
import com.toplion.cplusschool.Bean.type;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Reimburse.ReimbursementDataActivity;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyy
 * on 2016/8/1.
 * 报销适配器
 */
public class BaomainAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mcontext;
    private List<baoitem> mlist;
    private Callback callback;


    public void setMlist(List<baoitem> mlist) {
        this.mlist = mlist;
    }

    public interface Callback {
        void click(View v);
    }

    public BaomainAdapter(Context context, List<baoitem> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    public BaomainAdapter(Context context, List<baoitem> list, Callback callback) {
        this.mcontext = context;
        this.mlist = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.baoitem, null);
            viewHolder.flowcity=(FlowLayout)convertView.findViewById(R.id.flowa);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.yuyuenum = (TextView) convertView.findViewById(R.id.dannum);
            viewHolder.windownum = (TextView) convertView.findViewById(R.id.windownum);
            viewHolder.quxiao = (TextView) convertView.findViewById(R.id.quxiao);
            viewHolder.state=(TextView)convertView.findViewById(R.id.state);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.time.setText(gettime(mlist.get(position).getRISTARTTIME()));

        final ArrayList<StandardInfo> standardInfo = mlist.get(position).getStandardInfo();
        String content = "";
        int num = 0;
        viewHolder.flowcity.removeAllViews();
        for (int i = 0; i < standardInfo.size(); i++) {
            if (i == standardInfo.size()-1) {
                content = content + standardInfo.get(i).getRTNAME();
            } else {
                content = content + standardInfo.get(i).getRTNAME() + "丶";
            }
            num = num + standardInfo.get(i).getRRNUMBER();

            final TextView tvb = new TextView(
                    mcontext);
            tvb.setBackgroundResource(R.drawable.button_chepai_bg);
            tvb.setPadding(50, 5, 50, 5);
            tvb.setTextColor(Color.WHITE);
            tvb.setTextSize(16);
            tvb.setText(standardInfo.get(i).getFNAME()+"("+standardInfo.get(i).getRTID()+")");
            viewHolder.flowcity.addView(tvb);
            final int finalI = i;
            tvb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type type=new type();
                    type.setRTID(standardInfo.get(finalI).getRTID());
                    type.setRTNAME(standardInfo.get(finalI).getRTNAME());
                    type.setRTPROCESSINGTIME(standardInfo.get(finalI).getRTPROCESSINGTIME());
                    Intent intent =new Intent();
                    intent.putExtra("TYPE",type);
                    intent.putExtra("style",1);
                    intent.setClass(mcontext,ReimbursementDataActivity.class);
                    mcontext.startActivity(intent);
                }
            });
        }
        viewHolder.content.setText(content);
        viewHolder.yuyuenum.setText(num + "");
        viewHolder.windownum.setText(mlist.get(position).getWINAME());
        if(mlist.get(position).getRISTATUS() == 1){
            viewHolder.state.setText("处理中");
        }else if(mlist.get(position).getRISTATUS() == 4){
            viewHolder.state.setText("已取消");
        }else if(mlist.get(position).getRISTATUS() == 5){
            viewHolder.state.setText("报销逾期");
        }
        if (mlist.get(position).getRISTATUS() == 6) {
            viewHolder.quxiao.setVisibility(View.VISIBLE);
            viewHolder.quxiao.setTextColor(Color.BLACK);
            viewHolder.quxiao.setText("取消预约");
            viewHolder.state.setText("待处理");
        } else if (mlist.get(position).getRISTATUS() == 5 || mlist.get(position).getRISTATUS() == 4) {
            viewHolder.quxiao.setVisibility(View.VISIBLE);
            viewHolder.quxiao.setTextColor(mcontext.getResources().getColor(R.color.logo_color));
            viewHolder.quxiao.setText("重新预约");
        }else if(mlist.get(position).getRISTATUS() == 2 ){
            viewHolder.quxiao.setVisibility(View.VISIBLE);
            viewHolder.quxiao.setTextColor(mcontext.getResources().getColor(R.color.gray666));
            viewHolder.quxiao.setText("报销完毕");
            viewHolder.state.setText("处理完毕");
        }else if(mlist.get(position).getRISTATUS() == 3 ){
            viewHolder.quxiao.setVisibility(View.VISIBLE);
            viewHolder.quxiao.setTextColor(Color.RED);
            viewHolder.quxiao.setText("无效报销");
            viewHolder.state.setText("无效报销单");
        }else {
            viewHolder.quxiao.setVisibility(View.GONE);
        }
        viewHolder.quxiao.setOnClickListener(this);
        viewHolder.quxiao.setTag(position);
        return convertView;
    }


    class ViewHolder {
        private TextView time;//预约时间
        private TextView content;//预约内容
        private TextView yuyuenum;//单据数量
        private TextView windownum;//预约窗口
        private TextView quxiao;
        private TextView state;
        private FlowLayout flowcity;//流布局
    }

    private String gettime(long lon) {
        String time;
        String week;
        time = TimeUtils.timeStampToDate(lon+"", "yyyy-MM-dd HH:mm:ss");
        week = AbDateUtil.getWeekNumber(time, "yyyy-MM-dd HH:mm:ss");
        return time + " " + week;
    }

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        callback.click(v);
    }
}
