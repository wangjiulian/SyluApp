package com.dpl.syluapp.guideui;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class ViewLayoutC extends RelativeLayout{

	public ViewLayoutC(Context context) {
		super(context);
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		LayoutParams relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		TextView mTxt=new TextView(context);
		mTxt.setText("ViewLayoutGPS");
		mTxt.setTextSize(28.0f);
		mTxt.setGravity(Gravity.CENTER);
		this.addView(mTxt,relativeParams);
	}
}