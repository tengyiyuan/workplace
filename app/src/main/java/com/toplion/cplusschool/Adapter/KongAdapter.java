package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.BanWidthBean;
import com.toplion.cplusschool.Bean.Kongitem;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.R;

import java.util.List;

public class KongAdapter extends BaseAdapter {
    private List<Kongitem> mlist;
    private Context mcontext;

    public void setMlist(List<Kongitem> mlist) {
        this.mlist = mlist;
    }

    public KongAdapter(Context context, List<Kongitem> list) {
        this.mlist = list;
        this.mcontext = context;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.kong_item, null);
            viewHolder.toptitle = (TextView) convertView.findViewById(R.id.newtitle);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.bumen = (TextView) convertView.findViewById(R.id.bumen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.toptitle.setText(mlist.get(position).getToptitle());
        viewHolder.state.setText(" 当前状态:"+mlist.get(position).getState());
        viewHolder.bumen.setText(" 牵头部门:"+mlist.get(position).getBumen());
        return convertView;
    }

    class ViewHolder {
        private TextView toptitle;
        private TextView state;
        private TextView bumen;
    }
}