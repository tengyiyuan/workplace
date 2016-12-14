package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Bean.RepairInfoBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;

import java.util.List;

/**
 * Created by wang on 2016/4/20.
 * 教学楼列表
 */
public class MyRepairterListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<RepairInfoBean> mlist;
    private ViewHolder viewHolder;
    private AbHttpUtil abHttpUtil;
    private int width = (int) (BaseApplication.ScreenWidth * 0.8);
    private int height = (int) (BaseApplication.ScreenWidth * 0.5);
    private RepairInfoBean repairBean;//保存position位置的bean
    public List<RepairInfoBean> getMlist() {
        return mlist;
    }
    public void setMlist(List<RepairInfoBean> mlist) {
        this.mlist = mlist;
    }
    public MyRepairterListAdapter(Context context, List<RepairInfoBean> list) {
        this.mcontext = context;
        this.mlist = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.my_repairter_list_item, null);
            abHttpUtil = AbHttpUtil.getInstance(mcontext);
            viewHolder.tv_myrepair_number = (TextView) convertView.findViewById(R.id.tv_myrepair_number);
            viewHolder.tv_myrepair_state = (TextView) convertView.findViewById(R.id.tv_myrepair_state);
            viewHolder.tv_myrepair_reason = (TextView) convertView.findViewById(R.id.tv_myrepair_reason);
            viewHolder.tv_myrepair_time = (TextView) convertView.findViewById(R.id.tv_myrepair_time);

            viewHolder.tv_repair_evaluate = (TextView) convertView.findViewById(R.id.tv_repair_evaluate);
            viewHolder.tv_repair_evaluate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线
            viewHolder.tv_repair_edit = (TextView) convertView.findViewById(R.id.tv_repair_edit);
            viewHolder.tv_repair_edit.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线
            viewHolder.tv_repair_cancle = (TextView) convertView.findViewById(R.id.tv_repair_cancle);
            viewHolder.tv_repair_cancle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_myrepair_number.setText(mlist.get(position).getOINUMBER()+"");
        viewHolder.tv_myrepair_state.setText(getState(mlist.get(position).getOISTATUS()+"",viewHolder));//显示报修状态
        viewHolder.tv_myrepair_reason.setText(mlist.get(position).getDXTITLE()+"");
        viewHolder.tv_myrepair_time.setText(mlist.get(position).getOICREATETIME());
        return convertView;
    }
    /**
     * 显示报修状态
     * @param state 报修状态
     * @param viewHolder 对应显示的控件
     * @return
     */
    private String getState(String state,ViewHolder viewHolder) {
        if (state.equals("1")||state.equals("9")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.logo_color));
            return "待接单";
        }  else if (state.equals("2")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.logo_color));
            return "待预约";
        } else if (state.equals("3")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.logo_color));
            return "待维修";
        } else if (state.equals("4")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            return "待评价";
        } else if (state.equals("5")||state.equals("6")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            return "已完结";
        } else if (state.equals("8")) {
            viewHolder.tv_myrepair_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            return "已撤销";
        }
        return "";
    }

    class ViewHolder {
        private TextView tv_myrepair_number;//订单编号
        private TextView tv_myrepair_state;//报修状态
        private TextView tv_myrepair_reason;//报修原因
        private TextView tv_myrepair_time;//报修时间
        private TextView tv_repair_evaluate;//评价
        private TextView tv_repair_edit;//编辑
        private TextView tv_repair_cancle;//撤销
    }
}
