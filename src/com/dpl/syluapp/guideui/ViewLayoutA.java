package com.dpl.syluapp.guideui;

import com.dpl.syluapp.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
public class ViewLayoutA extends RelativeLayout{

	public ViewLayoutA(Context context) {
		super(context);
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		RelativeLayout.LayoutParams relativeParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		ImageView widget1=new ImageView(context);
		Resources res=getResources();
		Drawable drawable=res.getDrawable(R.drawable.guide_widget1);
		widget1.setBackgroundDrawable(drawable);
		this.addView(widget1,relativeParams);
	}

}
