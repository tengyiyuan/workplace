package com.toplion.cplusschool.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Common.CommonPopupWindow;
import com.toplion.cplusschool.R;

import java.util.List;


/**
 * @author tengyiyuan
 * @version 1.0.0 2016-6-12
 * @Description
 * @reviewer
 */
public class CustMenuSelect extends Dialog {
    public static  Button button1;
    public static TextView tv_yuanyin;
    private  RelativeLayout relativeLayout;
    private CommonPopupWindow cPWindow;

    public CustMenuSelect(final Context context, final List<CommonBean> list, int width, int height) {
        super(context, R.style.edit_AlertDialog_style);
        setContentView(R.layout.select);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        //this.setCanceledOnTouchOutside(false);
        button1 = (Button) findViewById(R.id.button1);
        tv_yuanyin = (TextView) findViewById(R.id.tv_yuanyin);
        relativeLayout = (RelativeLayout) findViewById(R.id.selectyuanyin);
        cPWindow = new CommonPopupWindow(context);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cPWindow.showPopUp(tv_yuanyin, list, new CommonPopupWindow.PopWinCallBack() {
                    @Override
                    public void onPopItemClick(int postion) {
                        tv_yuanyin.setText(list.get(postion).getDes());
                    }
                });
            }
        });


    }

}
