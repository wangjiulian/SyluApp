package com.dpl.syluapp;

import java.util.Calendar;

import com.dpl.syluapp.activity.EmptyRoom1;
import com.dpl.syluapp.db.EmptyRoomDbOpenHelper;
import com.dpl.syluapp.db.deleleEmptyRoomDb;
import com.dpl.syluapp.guideui.ViewPageActivity;
import com.dpl.syluapp.guideui.ViewPageActivtity1;
import com.dpl.syluapp.widget.CustomToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity implements AnimationListener {

	private SharedPreferences mSharedPreferences, fSharedPreferences;
	private SharedPreferences.Editor editor, fEditor;
	private boolean firstUse;
	private Calendar mCalendar;
	public static Context context;
	private ImageView imageView = null;
	private Animation alphaAnimation = null;
	private SharedPreferences preferences;

	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		context = this;
		init();
		imageView = (ImageView) findViewById(R.id.welcome_image_view);
		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true);
		alphaAnimation.setFillAfter(true);
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this);

		preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
		int count = preferences.getInt("count", 0);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		if (count == 0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ViewPageActivtity1.class);
			startActivity(intent);
			this.finish();
		}
		Editor editor = preferences.edit();
		// 存入数据
		editor.putInt("count", ++count);
		// 提交修改
		editor.commit();
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(this, main.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}

	private void init() {

		fSharedPreferences = context.getSharedPreferences("first",
				Context.MODE_PRIVATE);
		firstUse = fSharedPreferences.getBoolean("first", true);
		mSharedPreferences = context.getSharedPreferences("emptyroom",
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
		mCalendar = Calendar.getInstance();
		String month = String.valueOf(mCalendar.get(Calendar.MONTH));
		String day = String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH));
		String month_day = mSharedPreferences.getString("month_day", "");
		if (!firstUse) {
			if (!(month_day).equals(month + day)) {
				deleleEmptyRoomDb.deleteDb();
				editor.putString("month_day", month + day).commit();

			}
		} else {

			fEditor = fSharedPreferences.edit();
			fEditor.putBoolean("first", false).commit();
		}

	}
}
