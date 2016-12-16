package com.toplion.cplusschool.PhotoWall.SelectPhoto.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.toplion.cplusschool.PhotoWall.SelectPhoto.ImageFloder;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/11/15.
 * 显示图片文件夹列表
 */

public class ListImageDirPopWindow extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private ListView mListDir;
    private View parentView;
    private  List<ImageFloder> mDatas;
    public ListImageDirPopWindow(Context context,int height, List<ImageFloder> datas) {
        super(context);
        this.popupHeight = height;
        this.mDatas = datas;
        initView(context);

    }
    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView = inflater.inflate(R.layout.list_dir, null);
        this.setContentView(parentView);
        mListDir = (ListView) parentView.findViewById(R.id.id_list_dir);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,popupHeight);
        mListDir.setLayoutParams(params);
        setPopConfig();
        initData(context);
        initEvents();
    }
    /**
     *   初始化数据
     * @param context
     */
    private void initData(Context context) {
        mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,R.layout.list_dir_item){
            @Override
            public void convert(ViewHolder helper, int item){
                ImageFloder imageFloder = mDatas.get(item);
                helper.setText(R.id.id_dir_item_name, imageFloder.getName());
                helper.setImageByUrl(R.id.id_dir_item_image,imageFloder.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, imageFloder.getCount() + "张");
            }
        });
    }
    /**
     * 配置弹出框属性
     * @createTime 2015/12/1,12:45
     *
     */
    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(popupHeight);
        ColorDrawable dw = new ColorDrawable(0x33000000);//产生遮罩层
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }
    public ListView getListView() {
        return mListDir;
    }
    /**
     * listView点击事件
     */
    public void initEvents(){
        mListDir.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                if (mImageDirSelected != null){
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }
    private OnImageDirSelected mImageDirSelected;
    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected){
        this.mImageDirSelected = mImageDirSelected;
    }
    public interface OnImageDirSelected
    {
        void selected(ImageFloder floder);
    }
    /**
     * 设置显示在v上方(以v的上边距为开始位置)
     * @param v
     */
    public void showAsDropUp(View v) {
        setAnimationStyle(R.style.AnimBottom);
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2, location[1] - popupHeight);
    }
}
