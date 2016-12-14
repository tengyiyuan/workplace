package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.FujianBean;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 新闻列表适配器
 */
public class FujianListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<FujianBean> mlist;
    private ViewHolder viewHolder;

    public List<FujianBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<FujianBean> mlist) {
        this.mlist = mlist;
    }

    public FujianListAdapter(Context context, List<FujianBean> list) {
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
            convertView = View.inflate(mcontext, R.layout.fujian_item, null);
            viewHolder.newtitle = (TextView) convertView.findViewById(R.id.newtitle);
            viewHolder.topimg = (ImageView) convertView.findViewById(R.id.topimg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.newtitle.setText(mlist.get(position).getFujianname());
        return convertView;
    }


    class ViewHolder {
        private TextView newtitle;
        private ImageView topimg;

    }
}
