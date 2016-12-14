package com.toplion.cplusschool.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.toplion.cplusschool.R;

/**
 * Created by wang
 * on 2016/10/18.
 *
 * @选择照片 or 拍照
 */

public class CameraPopWindow{
    private PopupWindow pop;
    private LinearLayout ll_popup;
    private Button btn_camera;
    private Button btn_picture;
//    private Activity act;

    public CameraPopWindow(Context context,View ll_parentview) {
        pop = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
        pop.setBackgroundDrawable(dw);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        btn_camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
        btn_picture = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        ll_popup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.activity_translate_in));
        pop.showAtLocation(ll_parentview, Gravity.BOTTOM, 0, 0);
    }

    //相机点击事件
    public void setBtnCameraOnlickListener(View.OnClickListener clickListener) {
        btn_camera.setOnClickListener(clickListener);
    }

    public void dismiss(){
        pop.dismiss();
        ll_popup.clearAnimation();
    }
    //相册点击事件
    public void setBtnPictureOnlickListener(View.OnClickListener clickListener) {
        btn_picture.setOnClickListener(clickListener);
    }
}
