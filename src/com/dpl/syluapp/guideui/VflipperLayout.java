package com.dpl.syluapp.guideui;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
public class VflipperLayout extends RelativeLayout{
	
	private ViewLayoutA mViewLayoutA;
	private ViewLayoutB mViewLayoutB;
	private ViewLayoutC mViewLayoutC;
	private ViewPager   mViewPager;
	private List<View>  mViewArray; 
	private PageItemAdapter adapter;
	public VflipperLayout(Context context) {
		super(context);
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		
		LayoutParams relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		mViewPager=new ViewPager(context);
		this.addView(mViewPager,relativeParams);

		relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mViewLayoutA=new ViewLayoutA(context);
		mViewLayoutB=new ViewLayoutB(context);
		mViewLayoutC=new ViewLayoutC(context);
		mViewArray = new ArrayList<View>();
		mViewArray.add(mViewLayoutA);
		mViewArray.add(mViewLayoutB);
		mViewArray.add(mViewLayoutC);
		adapter=new PageItemAdapter(mViewArray);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new XonPageChangeListener());
		mViewPager.setCurrentItem(0);
	}

		
	
    
    private class XonPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int key) {
			// TODO Auto-generated method stub
				

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
		}
    	
    }
    

}
