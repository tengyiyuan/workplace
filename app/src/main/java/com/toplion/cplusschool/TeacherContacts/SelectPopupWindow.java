package com.toplion.cplusschool.TeacherContacts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.toplion.cplusschool.Adapter.ContactsListAdapter;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

import java.util.List;

/**
 * Created by toplion
 * on 2016/9/13.
 */
public class SelectPopupWindow extends PopupWindow {
    private Context mcontext;
    private View mMenuView;
    public ListView lv_contants_grade;//年级
    public ListView lv_contacts_professional;//专业
    public ListView lv_contacts_class;//班级
    public ContactsListAdapter gAdapter;//适配器
    public ContactsListAdapter pAdapter;//适配器
    public ContactsListAdapter cAdapter;//适配器

    public SelectPopupWindow(Context context) {
        super(context);
        this.mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.tea_contacts_dialog, null);

        lv_contants_grade = (ListView) mMenuView.findViewById(R.id.lv_contants_grade);
        lv_contacts_professional = (ListView) mMenuView.findViewById(R.id.lv_contacts_professional);
        lv_contacts_class = (ListView) mMenuView.findViewById(R.id.lv_contacts_class);
        gAdapter = new ContactsListAdapter(mcontext);
        pAdapter = new ContactsListAdapter(mcontext);
        cAdapter = new ContactsListAdapter(mcontext);

        this.setTouchable(true);
	    /*设置点击menu以外其他地方以及返回键退出*/
        this.setFocusable(true);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PushTopMenu);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
        // ColorDrawable dw = new ColorDrawable(000000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.ll_popu).getBottom();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y>height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
