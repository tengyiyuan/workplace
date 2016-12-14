package com.toplion.cplusschool.Adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Update.UpdateVersion;

/**
 * 设置页面自定义ListViewAdapter
 * @author liyb
 *
 */
public class SetListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public SetListViewAdapter(Context context,List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class Setting{
        public ImageView image;
        public TextView title;
        public ImageView view;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Setting set=null;
        if(convertView==null){
            set=new Setting();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.list_setting, null);
            set.image=(ImageView)convertView.findViewById(R.id.list_set_image);
            set.title=(TextView)convertView.findViewById(R.id.list_set_tv);
            set.view=(ImageView)convertView.findViewById(R.id.list_set_btn);
            convertView.setTag(set);
        }else{
            set=(Setting)convertView.getTag();
        }
        //绑定数据
        set.image.setBackgroundResource((Integer)data.get(position).get("image"));
        set.title.setText((String)data.get(position).get("title"));
        if(data.get(position).get("btn").toString().equals("0")){
            set.view.setBackgroundDrawable(convertView.getResources().getDrawable(R.mipmap.jiantou_right));

        }else if(data.get(position).get("btn").toString().equals("1")){
            set.view.setBackgroundDrawable(convertView.getResources().getDrawable(R.mipmap.jiantou_right));
            set.view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Toast.makeText(context, "系统暂无升级!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            set.view.setBackgroundDrawable(convertView.getResources().getDrawable(R.mipmap.btn_up));
            set.view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if(Constants.UPDATE_URL.contains("http:") || Constants.UPDATE_URL.contains("https:")){
                        UpdateVersion version = new UpdateVersion(context);
                        version.checkUpdate();
                    }else
                        Toast.makeText(context, "下载文件路径出现异常!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }
}
