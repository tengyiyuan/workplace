package com.toplion.cplusschool.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;


/**
 * @author wangshengbo
 * @version 1.0.0 2016-8-2
 * @Description
 * @reviewer
 */
public class CustomEditTextDialog {
    private TextView title;
    private EditText detial;
    private Button cancel;
    private Button ensure;
    private TextView tv_dialog_num;
    private final Dialog mDialog;
    // private final LayoutInflater mInflater;
    private View line;
    private static final double WIDTH = 0.8;
    private static final int COLOR = Color.rgb(238, 120, 12);
    private static final int HEIGHT = 3;

    public CustomEditTextDialog(final Context context) {
        LayoutInflater mInflater;
        mDialog = new Dialog(context, R.style.edit_AlertDialog_style);
        mInflater = LayoutInflater.from(context);
        final View dialogView = mInflater.inflate(R.layout.custom_edittext_dialog, null);
        final Window window = mDialog.getWindow();
        window.setContentView(dialogView);
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (BaseApplication.ScreenWidth * WIDTH);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        findView(window);
    }

    private void findView(Window window) {
        title = (TextView) window.findViewById(R.id.custom_dialog_title);
        tv_dialog_num = (TextView) window.findViewById(R.id.tv_dialog_num);
        detial = (EditText) window.findViewById(R.id.custom_dialog_detial);
        cancel = (Button) window.findViewById(R.id.custom_dialog_cancel);
        ensure = (Button) window.findViewById(R.id.custom_dialog_ensure);
        line = window.findViewById(R.id.line);
    }

    /**
     * 设置点击屏幕Dialog不消失
     */
    public void setDisMiss(boolean flag) {
        mDialog.setCanceledOnTouchOutside(flag);
    }

    /**
     * 割线颜色值变换
     */
    public void setlinecolor() {
        line.setBackgroundColor(COLOR);
        final LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) line
                .getLayoutParams();
        linearParams.height = HEIGHT;
        line.setLayoutParams(linearParams);
    }

    /**
     * 设置输入框右侧提示语是否显示
     *
     * @param flag
     */
    public void setTextNumberBoolean(boolean flag) {
        if (flag) {
            tv_dialog_num.setVisibility(View.VISIBLE);
        } else {
            tv_dialog_num.setVisibility(View.GONE);
        }
    }

    /**
     * 设置输入框右侧提示语显示内容
     *
     * @param text
     */
    public void setEditRightText(String text) {
        tv_dialog_num.setText(text);
    }

    /**
     * 内容面板是否显示
     */
    public void setContentboolean(boolean flag) {
        if (flag) {
            detial.setVisibility(View.VISIBLE);
        } else {
            detial.setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示内容
     *
     * @param hintText
     */
    public void setHintText(String hintText) {
        detial.setHint(hintText);
    }

    /**
     * 设置输入长度
     *
     * @param num
     */
    public void setNumText(int num) {
        detial.setFilters(new InputFilter[]{new InputFilter.LengthFilter(num)});
    }

    /**
     * 设置输入类型
     *
     * @param type
     */
    public void setEditTextType(int type) {
        detial.setInputType(type);
    }

    public void setEditTextLength(int n){
        detial.setFilters(new InputFilter[]{new InputFilter.LengthFilter(n)}); //最大输入长度
    }
    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getEditText() {
        return detial.getText().toString();
    }

    public void setEditTextOnChangeListener(TextWatcher tw){
        detial.addTextChangedListener(tw);
    }
    /**
     * 获取输入的内容
     *
     * @return
     */
    public View getEdit() {
        return detial;
    }

    /**
     * 右侧按钮名字设置
     */
    public void setRightText(String lefttext) {
        cancel.setText(lefttext);
    }

    /**
     * 左边按钮点击事件
     *
     * @param listener
     */
    public void setRightOnClick(View.OnClickListener listener) {
        cancel.setOnClickListener(listener);
    }

    /**
     * 左侧按钮是否显示
     */
    public void setLeftbtnboolean(boolean flag) {
        if (flag) {
            ensure.setVisibility(View.VISIBLE);
        } else {
            ensure.setVisibility(View.GONE);
        }
    }

    /**
     * 左边按钮名字设置
     */
    public void setLeftText(String righttext) {
        ensure.setText(righttext);
    }

    /**
     * 左边按钮点击事件
     *
     * @param listener
     */
    public void setLeftOnClick(View.OnClickListener listener) {
        ensure.setOnClickListener(listener);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * 设置详情
     *
     * @param detial
     */
    public void setDetial(String detial) {
        this.detial.setText(detial);
    }

    /**
     * 设置左边按钮背景
     *
     * @param cancelBg
     */
    public void setCancelBg(int cancelBg) {
        cancel.setBackgroundResource(cancelBg);
    }

    /**
     * 设置右边按钮背景
     */
    public void setEnsureBg(int ensureBg) {
        ensure.setBackgroundResource(ensureBg);
    }

    /**
     * 设置隐藏左边按钮背景
     */
    public void setLeftVisible(int ensureBg) {
        cancel.setVisibility(View.GONE);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
