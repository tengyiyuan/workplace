package com.toplion.cplusschool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author wang
 * @date 2016-5-30
 * @des解决SrollView嵌套ListView冲突
 */
public class GridViewInScrollView extends GridView {
	public GridViewInScrollView(Context context) {
		super(context);
	}

	public GridViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
