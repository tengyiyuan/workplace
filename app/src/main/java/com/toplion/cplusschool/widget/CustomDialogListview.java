package com.toplion.cplusschool.widget;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;


/**
 * @Description
 * @author tengyiyuan
 * @version 1.0.0 2016-6-13
 * @reviewer
 */
public class CustomDialogListview extends Dialog {

    public static ListView listView;
    private List<CommonBean> mlist;
    private Context context;


    public CustomDialogListview(Context context, String title, List<CommonBean> list,String ls_select_result) {
        super(context, R.style.edit_AlertDialog_style);
        this.mlist = list;
        this.context = context;
        setContentView(R.layout.select_custom_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (BaseApplication.ScreenWidth * 0.8);
        if(mlist.size()>=6){
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dialogHeight);
        }
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        ((TextView) findViewById(R.id.txt_title)).setText(title);
        listView = (ListView) findViewById(R.id.mylist);
//        list_adapter myadapter = new list_adapter(mlist, ls_select_result);
        listView.setAdapter( new list_adapter(mlist, ls_select_result));
    }

    private class list_adapter extends BaseAdapter {
        private List<CommonBean> data;
        String ls_select_result = "";

        public list_adapter(List<CommonBean> list, String ls_select_result) {
            super();
            this.data = list;
            this.ls_select_result = ls_select_result;
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
                convertView = LinearLayout.inflate(context, R.layout.single_text, null);
                myholder.txt_content = (TextView) convertView .findViewById(R.id.txt_1);
                myholder.image_ico = (ImageView) convertView .findViewById(R.id.image_ico);
                convertView.setTag(myholder);
            } else {
                myholder = (Holder) convertView.getTag();
            }
            myholder.txt_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (ls_select_result.equals(data.get(position).getDes())) {
                myholder.txt_content.setSelected(true);
                myholder.image_ico.setVisibility(View.VISIBLE);
            } else {
                myholder.txt_content.setSelected(false);
                myholder.image_ico.setVisibility(View.INVISIBLE);
            }
            if(!TextUtils.isEmpty(data.get(position).getOther())){
                myholder.txt_content.setText(data.get(position).getDes()+" "+data.get(position).getOther());
            }else{
                myholder.txt_content.setText(data.get(position).getDes());
            }
            return convertView;
        }

        class Holder {
            ImageView image_ico;
            TextView txt_content;
        }
    }
}
