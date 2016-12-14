package com.toplion.cplusschool.PhotoWall;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.SendFlowerPopup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang
 * on 2016/11/26.
 *
 * @送花pop
 */

public class FlowersPopWindow extends PopupWindow {
    public LinearLayout ll_popup;
    public ImageView iv_jian;
    public ImageView iv_jia;
    public TextView tv_number;
    private TextView tv_now_number;
    private TextView tv_flower_give;// 赠送
    private TextView tv_flower_zuan;
    private ListView mListView;
    private Context mContext;
    private int count = 0;//当前鲜花数量

    public FlowersPopWindow(final Context context, View ll_parentview) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.flowers_pop, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setContentView(view);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        iv_jian = (ImageView) view.findViewById(R.id.iv_jian);
        iv_jia = (ImageView) view.findViewById(R.id.iv_jia);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_now_number = (TextView) view.findViewById(R.id.tv_now_number);
        tv_flower_give = (TextView) view.findViewById(R.id.tv_flower_give);
        tv_flower_zuan = (TextView) view.findViewById(R.id.tv_flower_zuan);
        TextView bt3 = (TextView) view.findViewById(R.id.tv_flower_cancle);
        mListView = (ListView) view.findViewById(R.id.title_list);
        tv_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListView.getVisibility() == View.VISIBLE) {
                    mListView.setVisibility(View.GONE);
                } else {
                    mListView.setVisibility(View.VISIBLE);
                }
//                if (Integer.parseInt(tv_number.getText().toString()) >= count) {
//                    iv_jia.setEnabled(false);
//                } else {
//                    iv_jia.setEnabled(true);
//                }
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = ll_popup.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                ll_popup.clearAnimation();
            }
        });

        ll_popup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.activity_translate_in));
        this.showAtLocation(ll_parentview, Gravity.BOTTOM, 0, 0);
    }

    //减点击事件
    public void setBtnJianOnlickListener(View.OnClickListener clickListener) {
        iv_jian.setOnClickListener(clickListener);
    }

    public void setGiveFlowerListener(View.OnClickListener clickListener) {
        tv_flower_give.setOnClickListener(clickListener);
    }

    public void setZuanFlowerListener(View.OnClickListener clickListener) {
        tv_flower_zuan.setOnClickListener(clickListener);
    }

    /**
     * 现有鲜花数量
     *
     * @param n
     */
    public void setNowFlowersNumber(int n) {
        count = n;
        tv_now_number.setText("X " + n);
    }


    public void setText(String s) {
        tv_number.setText(s);
    }

    public TextView getTv_number() {
        return tv_number;
    }

    //加点击事件
    public void setBtnJiaOnlickListener(View.OnClickListener clickListener) {
        iv_jia.setOnClickListener(clickListener);
    }

    /**
     * 设置弹窗列表子项
     */
    public void setSendFlowerList(final List<Map<String, String>> maps) {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_number.setText(maps.get(position).get("count"));
                mListView.setVisibility(View.GONE);
            }
        });
        //设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.send_flower_item, null);
                    viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
                    viewHolder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tv_count.setText(maps.get(position).get("count"));
                viewHolder.tv_des.setText(maps.get(position).get("des"));
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return maps.get(position);
            }

            @Override
            public int getCount() {
                return maps.size();
            }

            class ViewHolder {
                private TextView tv_count;
                private TextView tv_des;

            }
        });
    }
}
