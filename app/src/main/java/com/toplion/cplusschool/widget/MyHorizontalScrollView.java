package com.toplion.cplusschool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 自定义view用来重新计算高度，解决各种嵌套问题
 */
public class MyHorizontalScrollView extends HorizontalScrollView{
    private View mView;
    public MyHorizontalScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mView!=null){
            mView.scrollTo(l, t);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setHorScrollView(View view){
        mView = view;
    }
}
