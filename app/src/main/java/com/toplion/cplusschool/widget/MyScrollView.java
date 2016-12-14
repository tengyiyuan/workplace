package com.toplion.cplusschool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by toplion on 2016/4/18.
 */
public class MyScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //此句代码是为了通知他的父控件现在进行的是本控件的操作，不要对我的操作进行干扰
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.onTouchEvent(ev);
    }

    public interface ScrollViewListener {

        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
