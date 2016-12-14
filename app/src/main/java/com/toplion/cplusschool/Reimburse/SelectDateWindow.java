package com.toplion.cplusschool.Reimburse;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/18.
 * 选择列表弹窗
 */
public class SelectDateWindow extends PopupWindow {
    private Context mcontext;
    private View mMenuView;
    public ListView lv_list;//列表
    public ListAdapter lAdapter;//适配器

    public SelectDateWindow(Context context,List<String> list) {
        super(context);
        this.mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_date_dialog, null);

        lv_list = (ListView) mMenuView.findViewById(R.id.lv_list);
        lAdapter = new ListAdapter(mcontext,list);
        lv_list.setAdapter(lAdapter);
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
                int height = mMenuView.findViewById(R.id.ll_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
    private class ListAdapter extends BaseAdapter {
        private Context mcontext;
        private List<String> mlist;
        public void setMlist(List<String> mlist) {
            this.mlist = mlist;
        }

        public ListAdapter(Context context,List<String> list) {
            this.mcontext = context;
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(mcontext, R.layout.contacts_item,null);
                viewHolder.tv_des = (TextView) convertView.findViewById(R.id.tv_contacts_item_des);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_des.setText(mlist.get(position));
            return convertView;
        }
    }
    class ViewHolder{
        private TextView tv_des;//描述
    }
}
