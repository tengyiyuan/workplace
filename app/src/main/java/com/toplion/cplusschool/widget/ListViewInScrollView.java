package com.toplion.cplusschool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author wang
 * @date 2016-5-30
 * @des解决SrollView嵌套ListView冲突
 */
public class ListViewInScrollView extends ListView {

	public ListViewInScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ListViewInScrollView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	public ListViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);  
	}
}
