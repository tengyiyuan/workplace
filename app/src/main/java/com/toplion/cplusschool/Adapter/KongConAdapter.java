package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.KongConItem;
import com.toplion.cplusschool.Bean.Kongitem;
import com.toplion.cplusschool.R;

import java.util.List;
import java.util.regex.Pattern;

public class KongConAdapter extends BaseAdapter {
    private List<KongConItem> mlist;
    private Context mcontext;

    public void setMlist(List<KongConItem> mlist) {
        this.mlist = mlist;
    }

    public KongConAdapter(Context context, List<KongConItem> list) {
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
            convertView = View.inflate(mcontext, R.layout.kongcon_item, null);
            viewHolder.yuefen = (TextView) convertView.findViewById(R.id.yuefen);
            viewHolder.jihua = (TextView) convertView.findViewById(R.id.jihua);
            viewHolder.jindu = (TextView) convertView.findViewById(R.id.jindu);
            viewHolder.baifenbi = (TextView) convertView.findViewById(R.id.baifenbi);
            viewHolder.bar = (ProgressBar) convertView.findViewById(R.id.bar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.yuefen.setText(mlist.get(position).getYuefen());
        viewHolder.jihua.setText(mlist.get(position).getJihua());
        viewHolder.jindu.setText(mlist.get(position).getJindu());
        if (!mlist.get(position).getBaifenbi().equals("")) {
            String str=mlist.get(position).getBaifenbi();
            if(str.trim().equals("lt50")){
                viewHolder.baifenbi.setText("50%以下");
                viewHolder.bar.setProgress(30);
            }else if(str.trim().equals("gt50")){
                viewHolder.baifenbi.setText("50%以上,但尚未完成");
                viewHolder.bar.setProgress(70);
            }else if(str.trim().equals("100")){
                viewHolder.baifenbi.setText("100%完成");
                viewHolder.bar.setProgress(100);
            }else{
                viewHolder.baifenbi.setText("0%");
                viewHolder.bar.setProgress(0);
            }
        } else {
            viewHolder.baifenbi.setText("0%");
            viewHolder.bar.setProgress(0);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView yuefen;
        private TextView jihua;
        private TextView jindu;
        private TextView baifenbi;
        private ProgressBar bar;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}