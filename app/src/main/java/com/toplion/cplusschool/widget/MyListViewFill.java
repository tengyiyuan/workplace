package com.toplion.cplusschool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * @ClassName: MyListViewFill
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author tengyiyuan
 * @date 2015-5-25 下午6:56:07
 * 
 */
public class MyListViewFill extends ListView {

    public MyListViewFill(Context context) {
        super(context);

    }

    public MyListViewFill(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MyListViewFill(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
