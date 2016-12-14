package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbDateUtil;
import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.Bean.Mukebean;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 新闻列表适配器
 */
public class MukeListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Mukebean> mlist;
    private ViewHolder viewHolder;

    public List<Mukebean> getMlist() {
        return mlist;
    }

    public void setMlist(List<Mukebean> mlist) {
        this.mlist = mlist;
    }

    public MukeListAdapter(Context context, List<Mukebean> list) {
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
            convertView = View.inflate(mcontext, R.layout.muke_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.starttime = (TextView) convertView.findViewById(R.id.starttime);
            viewHolder.endtime = (TextView) convertView.findViewById(R.id.endtime);
            viewHolder.mukeimg = (ImageView) convertView.findViewById(R.id.mukeimg);
            viewHolder.people=(TextView)convertView.findViewById(R.id.people);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.people.setText("已有"+mlist.get(position).getEnrollCount()+"人参加");
        viewHolder.name.setText(mlist.get(position).getName());
        viewHolder.starttime.setText("开始时间:"+AbDateUtil.getStringByFormat(mlist.get(position).getCtStartTime(),"yyyy-MM-dd"));
        viewHolder.endtime.setText("结束时间:"+AbDateUtil.getStringByFormat(mlist.get(position).getCtEndTime(),"yyyy-MM-dd"));
        AbImageLoader.getInstance(mcontext).display(viewHolder.mukeimg,mlist.get(position).getCtImgUrl(),420,240);
        return convertView;
    }


    class ViewHolder {
        private TextView name;
        private TextView starttime;
        private TextView endtime;
        private ImageView mukeimg;
        private TextView people;
    }
}
