package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toplion.cplusschool.R;


/**
 * @author tengyiyuan
 * @version 1.0.0 2015-7-29
 * @Description提示弹窗
 * @reviewer
 */
public class ToastManager {

    private Toast mToast;
    public static ToastManager myToast;

    private ToastManager() {
    }

    /**
     * 获取MyToast对象。。
     *
     * @return MyToast对象
     */
    public static ToastManager getInstance() {

        if (myToast == null) {

            synchronized (ToastManager.class) {

                if (myToast == null) {

                    myToast = new ToastManager();
                }
            }
        }
        return myToast;
    }

    /**
     * 显示信息弹出框，默认显示Toast.LENGTH_SHORT这么长时间。
     *
     * @param context
     * @param strId   string 资源id
     */
    public void showToast(Context context, int strId) {
        showToast(context, context.getString(strId));
    }

    /**
     * 显示信息弹出框，默认显示Toast.LENGTH_SHORT这么长时间。
     *
     * @param context
     * @param string  显示内容
     */
    public void showToast(Context context, String string) {
        if (mToast == null) {
            mToast = new Toast(context);
        }
        TextView txt_view = new TextView(context);
        txt_view.setText("    " + string + "    ");
        txt_view.setTextColor(Color.WHITE);
        txt_view.setTextSize(16);
        txt_view.setBackgroundResource(R.drawable.toast_bg);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        txt_view.setLayoutParams(lp);
        mToast.setView(txt_view);
        mToast.setDuration(5000);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    /**
     * 根据设定的显示时间值，显示Toast弹出框。
     *
     * @param context
     * @param string  string字符串
     * @param time    设定显示的时间值
     */
    public void showToast(Context context, String string, int time) {

        if (mToast == null) {

            mToast = Toast.makeText(context, string, time);
        } else {

            mToast.setText(string);
        }
        mToast.show();
    }

    /**
     * 根据设定的显示时间值，显示Toast弹出框。
     *
     * @param context
     * @param strId   string资源id
     * @param time    设定显示的时间值
     */
    public void showToast(Context context, int strId, int time) {

        if (mToast == null) {

            mToast = Toast.makeText(context, strId, time);
        } else {

            mToast.setText(strId);
        }
        mToast.show();
    }

    /**
     * 取消Toast
     */
    public void cancelToast() {

        if (mToast != null) {

            mToast.cancel();
        }
    }

    public void getTvTitle(final TextView tv, final String str) {
        if (!str.equals("")) {
            tv.setText(str);
        }
        if (null != tv) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    tv.setVisibility(View.GONE);
                }
            };
            new Handler().postDelayed(runnable, 5000);
        }
    }

}
