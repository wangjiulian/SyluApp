package com.dpl.syluapp.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class RoateZAinmation extends Animation
{

	private float  mFromDegrees;  
    private float  mToDegrees;  
	private int mDegree;
	private float  mCenterX;  
    private float  mCenterY;  
	private Camera camera = new Camera();
	private int mDuration;
	private int mDepthZ = 0;
	private boolean mReverse = false;
	
	public RoateZAinmation(int duration, float  fromDegrees,  float  toDegrees)
	{
		mDuration = duration;
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight)
	{
		super.initialize(width, height, parentWidth, parentHeight);
		setDuration(mDuration);
		setFillAfter(true);
		mCenterX = width / 2;
		mCenterY = height / 2;
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{
		final Matrix matrix = t.getMatrix();
	    float degrees =  mFromDegrees + ((mToDegrees -  mFromDegrees) * interpolatedTime);  
		 
		camera.save(); 
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);
		
	}


}
