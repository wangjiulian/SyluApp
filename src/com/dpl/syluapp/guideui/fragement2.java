package com.dpl.syluapp.guideui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.dpl.syluapp.R;

public class fragement2 extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_f2, container,false);
		
		
		ImageView f2cloud1 = (ImageView)view.findViewById(R.id.f2cloud1);
		ImageView f2cloud2 = (ImageView)view.findViewById(R.id.f2cloud2);
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation =
	              new TranslateAnimation(
	                  Animation.RELATIVE_TO_SELF,0f,
	                  Animation.RELATIVE_TO_SELF,2f,
	                  Animation.RELATIVE_TO_SELF,0f,
	                  Animation.RELATIVE_TO_SELF,0f);
	           translateAnimation.setDuration(5000);
	           translateAnimation.setRepeatMode(Animation.REVERSE);
	           translateAnimation.setRepeatCount(7);
	           animationSet.addAnimation(translateAnimation);
         f2cloud1.startAnimation(animationSet);
         f2cloud2.startAnimation(animationSet);
		return view;
	}

}
