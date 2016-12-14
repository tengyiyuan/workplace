package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.OrderBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wangshengbo on 2016/5/10.
 * 订单列表适配器
 */
public class MyOrderListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<OrderBean> mlist;
    private ViewHolder viewHolder;
    private String mOrderState;
    public List<OrderBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<OrderBean> mlist) {
        this.mlist = mlist;
    }

    public MyOrderListAdapter(Context context, List<OrderBean> list,String orderState){
        this.mcontext = context;
        this.mlist = list;
        this.mOrderState = orderState;
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
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.list_my_order_item,null);
            viewHolder.list_my_order_num = (TextView) convertView.findViewById(R.id.list_my_order_num);
            viewHolder.list_my_order_state = (TextView) convertView.findViewById(R.id.list_my_order_state);
            viewHolder.list_my_order_money = (TextView) convertView.findViewById(R.id.list_my_order_money);
            viewHolder.list_my_order_type = (TextView) convertView.findViewById(R.id.list_my_order_type);
            viewHolder.list_my_order_time = (TextView) convertView.findViewById(R.id.list_my_order_time);
            viewHolder.list_my_order_btn = (TextView) convertView.findViewById(R.id.list_my_order_btn);
            viewHolder.list_my_order_pkgName= (TextView) convertView.findViewById(R.id.list_my_order_pkgName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.list_my_order_num.setText(mlist.get(position).getOrderId());
        String meal = mlist.get(position).getOrderPackageName();  // 套餐
        String[] meals = meal.split("\\|");                // 拆分套餐
        viewHolder.list_my_order_money.setText(meals[3]);// 金额
        viewHolder.list_my_order_type.setText(meals[2]); // 带宽
        String period = "";
        try{
            period = mlist.get(position).getOrderClientPackagePeriod();// 时长
        }catch(Exception e){
            e.printStackTrace();
            if(period==null) period = "";
        }
        viewHolder.list_my_order_time.setText(period);  // 时长
        viewHolder.list_my_order_pkgName.setText(mlist.get(position).getOrderPackageName());
        String state = mlist.get(position).getOrderState();
        if(TextUtils.isEmpty(state)){
            state = mOrderState;
        }
        if(TextUtils.isEmpty(state)||state.equals(Constants.not_pay)){
            // 待支付
            String exchange = convertView.getResources().getString(R.string.waitforpay);
            viewHolder.list_my_order_state.setText(Html.fromHtml(exchange));
            viewHolder.list_my_order_btn.setVisibility(View.VISIBLE);
            exchange = convertView.getResources().getString(R.string.start_pay);
            viewHolder.list_my_order_btn.setText(Html.fromHtml(exchange));
        }else if(state.equals(Constants.pay_closed)){
            // 交易关闭
            state = Constants.ORDER_CLOSE;
            viewHolder.list_my_order_state.setText(state);
            viewHolder.list_my_order_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            viewHolder.list_my_order_btn.setVisibility(View.GONE);
        }else if(state.equals(Constants.ali_not_pay) || state.equals(Constants.ali_payed_not_in_sam)
                || state.equals(Constants.ali_payed_sam_feed) || state.equals(Constants.return_success_to_ali)
                || state.equals(Constants.ali_payed_sam_pkged) || state.equals(Constants.ali_trade_finished)){
            // 交易成功
            state = Constants.ORDER_SUCEESS;
            viewHolder.list_my_order_state.setText(state);
            viewHolder.list_my_order_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            viewHolder.list_my_order_btn.setVisibility(View.GONE);
        }else {
            state = Constants.ORDER_CLOSE;
            viewHolder.list_my_order_state.setText(state);
            viewHolder.list_my_order_state.setTextColor(mcontext.getResources().getColor(R.color.black));
            viewHolder.list_my_order_btn.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        private TextView list_my_order_num;//订单编号
        private TextView list_my_order_state;//状态
        private TextView list_my_order_money;//价格
        private TextView list_my_order_type;//类型
        private TextView list_my_order_time;//时间
        private TextView list_my_order_btn;//支付按钮
        private TextView list_my_order_pkgName;
    }
}
