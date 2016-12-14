package com.toplion.cplusschool.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.toplion.cplusschool.R;

/**
 * 通用底部弹出框的设置
 */
public class MenuPopupWindow extends PopupWindow {

    private TextView btn_bottom_call, btn_bottom_message, btn_bottom_copy, btn_bottom_save, btn_cancel;
    private LinearLayout lay_chnel,lay_call,lay_message,lay_copy,lay_save,lay_detail;
    private Context ctx;
    private View mMenuView;
    private TextView title;

    public MenuPopupWindow(Activity context) {
        super(context);
        ctx = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_menu, null);
        btn_bottom_call = (TextView) mMenuView.findViewById(R.id.btn_bottom_call);
        btn_bottom_message = (TextView) mMenuView.findViewById(R.id.btn_bottom_message);
        btn_bottom_copy = (TextView) mMenuView.findViewById(R.id.btn_bottom_copy);
        btn_bottom_save = (TextView) mMenuView.findViewById(R.id.btn_bottom_save);
        title=(TextView) mMenuView.findViewById(R.id.title);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_bottom_chnel);
        lay_chnel=(LinearLayout) mMenuView.findViewById(R.id.lay_chnel);
        lay_call=(LinearLayout) mMenuView.findViewById(R.id.lay_call);
        lay_message=(LinearLayout) mMenuView.findViewById(R.id.lay_message);
        lay_copy=(LinearLayout) mMenuView.findViewById(R.id.lay_copy);
        lay_save=(LinearLayout) mMenuView.findViewById(R.id.lay_save);
        lay_detail = (LinearLayout) mMenuView.findViewById(R.id.lay_detail);
        this.setTouchable(true);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        	ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
       // ColorDrawable dw = new ColorDrawable(000000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        lay_chnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 是否显示呼叫按钮
     *
     * @param
     */
    public void setCallboolean(boolean flag) {
        if (flag) {
            lay_call.setVisibility(View.VISIBLE);
        } else {
            lay_call.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示发短信按钮
     *
     * @param
     */
    public void setMessageboolean(boolean flag) {
        if (flag) {
            lay_message.setVisibility(View.VISIBLE);
        } else {
            lay_message.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示复制按钮
     *
     * @param
     */
    public void setCopyboolean(boolean flag) {
        if (flag) {
            lay_copy.setVisibility(View.VISIBLE);
        } else {
            lay_copy.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示保存按钮
     *
     * @param
     */
    public void setSaveboolean(boolean flag) {
        if (flag) {
            lay_save.setVisibility(View.VISIBLE);
        } else {
            lay_save.setVisibility(View.GONE);
        }
    }
    /**
     * 是否查看详情
     *
     * @param
     */
    public void setInfo(boolean flag) {
        if (flag) {
            lay_detail.setVisibility(View.VISIBLE);
        } else {
            lay_detail.setVisibility(View.GONE);
        }
    }
    /**
     * 设置标题文字
     */
    public void setTitleText(String text) {
        title.setText(text);
    }

    /**
     * 呼叫名字设置
     */
    public void setCallText(String text) {
        btn_bottom_call.setText(text);
    }

    /**
     * 发短信名字设置
     */
    public void setMessageText(String text) {
        btn_bottom_message.setText(text);
    }

    /**
     * 复制名字设置
     */
    public void setCopyText(String text) {
        btn_bottom_copy.setText(text);
    }

    /**
     * 保存名字设置
     */
    public void setSaveText(String text) {
        btn_bottom_save.setText(text);
    }

    /**
     * 呼叫添加点击事件
     *
     * @param listener
     */
    public void setCallOnClick(View.OnClickListener listener) {
        lay_call.setOnClickListener(listener);
    }

    /**
     * 发短信点击事件
     *
     * @param listener
     */
    public void setMessageOnClick(View.OnClickListener listener) {
        lay_message.setOnClickListener(listener);
    }

    /**
     * 复制点击事件
     *
     * @param listener
     */
    public void setCopyOnClick(View.OnClickListener listener) {
        lay_copy.setOnClickListener(listener);
    }

    /**
     * 保存点击事件
     *
     * @param listener
     */
    public void setsaveOnClick(View.OnClickListener listener) {
        lay_save.setOnClickListener(listener);
    }

    /**
     * 查看详情
     * @param listener
     */

    public void setDetailOnClick(View.OnClickListener listener){
        lay_detail.setOnClickListener(listener);
    }

}
