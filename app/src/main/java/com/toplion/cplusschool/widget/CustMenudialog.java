package com.toplion.cplusschool.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.toplion.cplusschool.R;


/**
 * @author tengyiyuan
 * @version 1.0.0 2015-7-29
 * @Description
 * @reviewer
 */
public class CustMenudialog extends Dialog {
    public static Button button1;
    public static RatingBar pjsu_start;
    public static RatingBar fwtd_start;
    public static RatingBar fwjg_start;
    public static RatingBar ywnl_start;
    public static EditText et_star_content;
    public CustMenudialog(Context context, int width, int height) {
        super(context, R.style.edit_AlertDialog_style);
        setContentView(R.layout.raingstart);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        //this.setCanceledOnTouchOutside(false);
        button1 = (Button) findViewById(R.id.button1);
        pjsu_start=(RatingBar)findViewById(R.id.pjsu_start);
        fwtd_start=(RatingBar)findViewById(R.id.fwtd_start);
        fwjg_start=(RatingBar)findViewById(R.id.fwjg_start);
        ywnl_start = (RatingBar) findViewById(R.id.ywnl_start);
        et_star_content = (EditText) findViewById(R.id.et_star_content);
    }

}
