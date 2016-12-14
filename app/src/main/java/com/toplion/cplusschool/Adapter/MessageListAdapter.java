package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 新闻列表适配器
 */
public class MessageListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<NewBean> mlist;
    private ViewHolder viewHolder;

    public List<NewBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<NewBean> mlist) {
        this.mlist = mlist;
    }

    public MessageListAdapter(Context context, List<NewBean> list) {
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.news_item, null);
            viewHolder.newtitle = (TextView) convertView.findViewById(R.id.newtitle);
            viewHolder.newimg = (TextView) convertView.findViewById(R.id.newimg);
            viewHolder.newtime = (TextView) convertView.findViewById(R.id.newtime);
            viewHolder.newbumen = (TextView) convertView.findViewById(R.id.newbumen);
            viewHolder.newbumen.setVisibility(View.VISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.newtitle.setText(mlist.get(position).getNews_title());
        viewHolder.newtime.setText("发布时间:" + mlist.get(position).getTime());
        viewHolder.newbumen.setText("发布部门:" + mlist.get(position).getNews_Info());
        String days = formatDateTime(mlist.get(position).getTime());
        if (days.equals("1")) {
            viewHolder.newimg.setVisibility(View.VISIBLE);
            viewHolder.newtime.setTextColor(Color.RED);
        } else if (days.equals("2")) {
            viewHolder.newimg.setVisibility(View.GONE);
            viewHolder.newtime.setTextColor(mcontext.getResources().getColor(R.color.yesdaycol));
        }else if(days.equals("3")){
            viewHolder.newimg.setVisibility(View.GONE);
            viewHolder.newtime.setTextColor(Color.rgb(242,177,1));
        }else{
            viewHolder.newimg.setVisibility(View.GONE);
            viewHolder.newtime.setTextColor(mcontext.getResources().getColor(R.color.yuanshicolor));
        }
        return convertView;
    }


    class ViewHolder {
        private TextView newtitle;
        private TextView newimg;
        private TextView newtime;
        private TextView newbumen;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private static String formatDateTime(String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        Calendar other = Calendar.getInstance();    //前天

        other.set(Calendar.YEAR, current.get(Calendar.YEAR));
        other.set(Calendar.MONTH, current.get(Calendar.MONTH));
        other.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 2);
        other.set(Calendar.HOUR_OF_DAY, 0);
        other.set(Calendar.MINUTE, 0);
        other.set(Calendar.SECOND, 0);


        current.setTime(date);
        if (current.after(today)) {
            return "1";
        } else if (current.after(yesterday)) {
            return "2";
        } else if (current.after(other)) {
            return "3";
        } else {
            return "0";
        }
    }
}
