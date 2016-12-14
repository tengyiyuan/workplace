package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wagnshengbo
 * on 2016/6/28.
 * 操场首页功能列表
 */
public class FunctionListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Map<String, Object>> fdata;//功能列表
    private ViewHolder viewHolder;

    public FunctionListAdapter(Context context, List<Map<String, Object>> list) {
        this.mcontext = context;
        this.fdata = list;
        //添加更多功能
//        Map<String, Object> functionMap = new HashMap<String, Object>();
//        functionMap.put("funImage", R.mipmap.btn_repair);
//        functionMap.put("funDes", Constants.GENGDUO);
//        fdata.add(functionMap);
    }

    @Override
    public int getCount() {
        return fdata.size();
    }

    @Override
    public Object getItem(int position) {
        return fdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.function_list_item, null);
            viewHolder.ground_iv_pay = (ImageView) convertView.findViewById(R.id.ground_iv_pay);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title_des);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ground_iv_pay.setImageResource((Integer) fdata.get(position).get("funImage"));
        viewHolder.tv_title.setText(fdata.get(position).get("funDes") + "");
        return convertView;
    }

    class ViewHolder {
        private ImageView ground_iv_pay;//功能图片显示
        private TextView tv_title;//功能描述
    }
}
