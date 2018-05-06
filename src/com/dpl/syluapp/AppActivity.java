package com.dpl.syluapp;

import com.dpl.syluapp.application.MoApplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author JUST玖 名称：AppActivity 类型：类 父类：Activity 功能：所有行为页面类的父类 负责：
 */
public class AppActivity extends FragmentActivity {
	
	private RelativeLayout rrlt_back;
	private FrameLayout layout;
	private TextView constanttitlebar_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MoApplication.addActivity(this);
	}

	/**
	 * 
	 * @param layoutResID
	 * @param flag
	 *            flag为true 表示使用通用标题栏 否则不使用
	 */
	public void setContentView(int layoutResID, boolean flag) {
		if (flag) {
			setContentView(layoutResID);
		} else {
			super.setContentView(layoutResID);
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.activity_app_activity);
		layout = (FrameLayout) findViewById(R.id.frame);
		rrlt_back = (RelativeLayout) findViewById(R.id.rrlt_back);
		constanttitlebar_title = (TextView) findViewById(R.id.constanttitlebar_title);
		rrlt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppActivity.this.finish();
			}
		});
		View view = LayoutInflater.from(AppActivity.this).inflate(layoutResID,
				null);
		layout.addView(view);

	}
/**
 * 设置标题栏名字
 * @param str
 */
	public void setActivityTitle(String str) {
		
		
		constanttitlebar_title.setText(str);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MoApplication.removeActivity(this);
	}

}
