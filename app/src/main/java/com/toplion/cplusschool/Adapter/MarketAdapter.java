package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;

import com.toplion.cplusschool.Bean.MarketBean;

import com.toplion.cplusschool.PhotoBrowser.photo.ImagePagerActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;


import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends BaseAdapter {
    private List<MarketBean> mlist;
    private Context mcontext;
    private AbImageLoader mAbImageLoader = null;
    private int style=1;

    public void setMlist(List<MarketBean> mlist) {
        this.mlist = mlist;
    }

    public MarketAdapter(Context context, List<MarketBean> list,int style) {
        this.mlist = list;
        this.mcontext = context;
        //图片的下载
        mAbImageLoader = AbImageLoader.getInstance(mcontext);
        this.style=style;
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
            convertView = View.inflate(mcontext, R.layout.market_item, null);
            viewHolder.imglay = (LinearLayout) convertView.findViewById(R.id.imglay);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.schname = (TextView) convertView.findViewById(R.id.schname);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.biaoqian = (TextView) convertView.findViewById(R.id.biaoqian);
            viewHolder.content = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mlist.get(position).getAUICONTACTNAME().length()>0) {
            viewHolder.name.setText(mlist.get(position).getAUICONTACTNAME().substring(0, 1) + "同学");
        }else{
            viewHolder.name.setText("匿名者");
        }
        viewHolder.schname.setText(mlist.get(position).getAUIADDRESS() + "  " + mlist.get(position).getAUIRELEASETIME());
if(style!=1) {
    viewHolder.money.setVisibility(View.VISIBLE);
    viewHolder.money.setText("¥ " + mlist.get(position).getAUIPRICE() + "");
}else{
    viewHolder.money.setVisibility(View.GONE);
}
        if (!mlist.get(position).getCINAME().equals("")) {
            viewHolder.biaoqian.setVisibility(View.VISIBLE);
            if(mlist.get(position).getCINAME().equals("生活用品")){
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_red);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_shenghuo));
            }else if(mlist.get(position).getCINAME().equals("学习用品")){
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_lan);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_xuexi));
            }else if(mlist.get(position).getCINAME().equals("电子产品")){
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_green);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_dianzi));
            }else{
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_qita));
            }
            viewHolder.biaoqian.setText(mlist.get(position).getCINAME());
        } else {
            viewHolder.biaoqian.setVisibility(View.GONE);
        }
        viewHolder.content.setText(mlist.get(position).getAUITITLE());
        viewHolder.imglay.removeAllViews();
        int lenth = mlist.get(position).getIRIURL().size();
        for (int i = 0; i < lenth; i++) {
            ImageView imageView = new ImageView(mcontext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(BaseApplication.ScreenWidth / 3-12, BaseApplication.ScreenWidth / 3-12));
            imageView.setPadding(0, 0, 20, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mAbImageLoader.display(imageView, mlist.get(position).getIRIURL().get(i));
            final int ids = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    for(int j=0;j<mlist.get(position).getIRIURL().size();j++){
                        urls.add(mlist.get(position).getIRIURL().get(j).replace("thumb/", ""));
                    }
                    imageBrower(ids,urls);
                }
            });
            viewHolder.imglay.addView(imageView);
        }
        return convertView;
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mcontext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mcontext.startActivity(intent);
    }

    class ViewHolder {
        private TextView name;
        private TextView biaoqian;
        private TextView title;
        private TextView money;
        private TextView schname;
        private TextView time;
        private TextView content;
        private LinearLayout imglay;
    }
}