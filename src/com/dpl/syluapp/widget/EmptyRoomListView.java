/**
 * 
 */
package com.dpl.syluapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author JUST玖
 * 
 *         2015-3-18
 */
public class EmptyRoomListView extends ListView {
	/*
	 * 自定义控件得使用该构造方法，用于在xml文件中初始化控件属性，否则出错
	 */
	public EmptyRoomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EmptyRoomListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
