package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ab.util.AbDateUtil;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 新闻列表适配器
 */
public class NewsListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<NewBean> mlist;
    private ViewHolder viewHolder;

    public List<NewBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<NewBean> mlist) {
        this.mlist = mlist;
    }

    public NewsListAdapter(Context context, List<NewBean> list) {
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewBean newBean = mlist.get(position);
        viewHolder.newtitle.setText(newBean.getNews_title());
        viewHolder.newtime.setText("发布日期:"+ AbDateUtil.getStringByFormat(newBean.getTime(),"yyyy-MM-dd"));
        Date beforeDate = TimeUtils.stringToDate(newBean.getTime(), "yyyy-MM-dd");
        double days = TimeUtils.getDistanceOfTwoDate(beforeDate, new Date());
        if (days == 0) {
            viewHolder.newimg.setVisibility(View.VISIBLE);
        }else{
            viewHolder.newimg.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder {
        private TextView newtitle;
        private TextView newimg;
        private TextView newtime;

    }
}
