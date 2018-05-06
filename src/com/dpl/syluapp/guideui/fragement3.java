package com.dpl.syluapp.guideui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.dpl.syluapp.R;
import com.dpl.syluapp.main;

public class fragement3 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_f3, container,false);


		ImageView ball = (ImageView)view.findViewById(R.id.ballgroup);
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation =
				new TranslateAnimation(
						Animation.RELATIVE_TO_SELF,0f,
						Animation.RELATIVE_TO_SELF,0f,
						Animation.RELATIVE_TO_SELF,0f,
						Animation.RELATIVE_TO_SELF,-0.05f);
		translateAnimation.setDuration(2000);
		translateAnimation.setRepeatMode(Animation.REVERSE);
		translateAnimation.setRepeatCount(10);
		animationSet.addAnimation(translateAnimation);
		ball.startAnimation(animationSet);
//		ImageView hand = (ImageView)view.findViewById(R.id.hand);

		Button enterbutton = (Button)view.findViewById(R.id.enterbutton);
		enterbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),main.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		
		AnimationSet animationSet2 = new AnimationSet(true);
		//创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
		AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
		//设置动画执行的时间
		alphaAnimation2.setDuration(5000);
		//将alphaAnimation对象添加到AnimationSet当中
		animationSet2.addAnimation(alphaAnimation2);
		//使用ImageView的startAnimation方法执行动画
		enterbutton.startAnimation(animationSet2);    
		
		return view;
		
		
		
	}

}
