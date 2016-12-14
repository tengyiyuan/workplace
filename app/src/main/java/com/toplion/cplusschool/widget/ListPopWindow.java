package com.toplion.cplusschool.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.SecondMarket.ReleaseActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by wang
 * on 2016/10/12.
 *
 * @des listPop
 */

public class ListPopWindow {
    private PopupWindow pop;
    private LinearLayout ll_popup;
    public ListView listView;
    public MyAapter myAapter;
    public TextView tv_pop_confirm;//确定
    private TextView tv_pop_cancle;//取消

    public ListPopWindow(Context context, List<Map<String,String>> list,View ll_parentview) {
        pop = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_select_type, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        tv_pop_confirm = (TextView) view.findViewById(R.id.tv_pop_confirm);
        tv_pop_cancle = (TextView) view.findViewById(R.id.tv_pop_cancle);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
        pop.setBackgroundDrawable(dw);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        tv_pop_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(-1);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        listView = (ListView) view.findViewById(R.id.lv_pop_type);
        myAapter = new MyAapter(list, context);
        listView.setAdapter(myAapter);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.activity_translate_in));
        pop.showAtLocation(ll_parentview, Gravity.BOTTOM, 0, 0);
    }
    public void dismiss(){
        pop.dismiss();
        ll_popup.clearAnimation();
    }
    public void setSelect(int i){
        myAapter.setSelectItem(i);
        myAapter.notifyDataSetChanged();
    }
    private class MyAapter extends BaseAdapter {
        private List<Map<String,String>> data;
        private Context mcontext;

        public MyAapter(List<Map<String,String>> list, Context context) {
            super();
            this.data = list;
            this.mcontext = context;
        }

        @Override
        public int getCount() {

            return data.size();
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
            Holder myholder = null;
            if (convertView == null) {
                myholder = new Holder();
                convertView = LinearLayout.inflate(mcontext, R.layout.single_text, null);
                myholder.txt_content = (TextView) convertView.findViewById(R.id.txt_1);
                myholder.image_ico = (ImageView) convertView.findViewById(R.id.image_ico);
                convertView.setTag(myholder);
            } else {
                myholder = (Holder) convertView.getTag();
            }
            myholder.txt_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            myholder.txt_content.setText(data.get(position).get("des"));
            if(position == selectItem){
                myholder.image_ico.setVisibility(View.VISIBLE);
            }else{
                myholder.image_ico.setVisibility(View.GONE);
            }
            return convertView;
        }
        private int  selectItem = -1;
        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }
        class Holder {
            ImageView image_ico;
            TextView txt_content;
        }
    }
}
