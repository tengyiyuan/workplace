package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ab.util.AbDateUtil;
import com.toplion.cplusschool.Bean.TestBean;
import com.toplion.cplusschool.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by toplion on 2016/4/21.
 * 考试安排列表适配器
 */
public class TestListAdapter extends BaseAdapter{
    private ViewHolder viewHolder;
    private Context mcontext;
    private List<TestBean> mlist;
    private long systemCurrenttime = 0;//当前系统时间
    private Calendar calendar = Calendar.getInstance();

    public List<TestBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<TestBean> mlist) {
        this.mlist = mlist;
    }

    public long getSystemCurrenttime() {
        return systemCurrenttime;
    }

    public void setSystemCurrenttime(long systemCurrenttime) {
        this.systemCurrenttime = systemCurrenttime;
    }

    public TestListAdapter(Context context, List<TestBean> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.testlist_item,null);
            viewHolder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            viewHolder.tv_credit = (TextView) convertView.findViewById(R.id.tv_credit);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_subject.setText(mlist.get(position).getKCM()+"");
        viewHolder.tv_credit.setText("("+mlist.get(position).getXF()+"分)");
        viewHolder.tv_type.setText(mlist.get(position).getKSMC()+"");
        viewHolder.tv_date.setText(mlist.get(position).getKSSJMS()+"");
        viewHolder.tv_address.setText(mlist.get(position).getJSMC()+"");
        try{
            calendar.setTime(new Date(mlist.get(position).getKSRQ()));
            if(systemCurrenttime>calendar.getTimeInMillis()){
                viewHolder.tv_state.setText("已结束");
            }else{
                int day = AbDateUtil.getOffectDay(calendar.getTimeInMillis(),systemCurrenttime);
                viewHolder.tv_state.setText("倒计时: "+day+"天");
            }
        }catch (Exception e){
            e.printStackTrace();
            viewHolder.tv_state.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder{
        private TextView tv_subject;//考试科目
        private TextView tv_credit;//学分
        private TextView tv_type;//类型
        private TextView tv_state;//状态
        private TextView tv_date;//日期
        private TextView tv_time;//时间
        private TextView tv_address;//地点
    }
}
