package com.dpl.syluapp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.select.Collector;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.fragment.EmptyRoomFrag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class EmptyRoom1 extends AppActivity implements OnClickListener,
		OnPageChangeListener {
	private List<TextView> views = new ArrayList<TextView>();

	private TextView view1;
	private TextView view2;
	private TextView view3;
	private TextView view4;
	private TextView view5;
	private TextView view6;
	private TextView view7;

	private ViewPager vp_emptyroom_viewpager;
	private List<EmptyRoomFrag> mFrags = new ArrayList<EmptyRoomFrag>();
	private List<String> titles = Arrays.asList("上午", "下午", "12节", "34节",
			"56节", "78节");
	private List<String> tables = Arrays.asList("room1", "room2", "room3",
			"room4", "room5", "room6");
	private FragmentPagerAdapter mFragmentPagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_emptyroom1_main);
		setActivityTitle("空教室查询");
		ininView();
		initDatas();
		initEvent();

	}

	private void initDatas() {
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		views.add(view6);
		for (int i = 0; i < titles.size(); i++) {
			EmptyRoomFrag frag = new EmptyRoomFrag();
			Bundle bundle = new Bundle();
			bundle.putString(EmptyRoomFrag.TITLE, titles.get(i).toString());
			bundle.putString(EmptyRoomFrag.TABLE, tables.get(i).toString());
			frag.setArguments(bundle);
			mFrags.add(frag);
		}
		mFragmentPagerAdapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFrags.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFrags.get(arg0);
			}
		};
		vp_emptyroom_viewpager.setAdapter(mFragmentPagerAdapter);
		setViewColor(R.id.view1);
	}

	private void ininView() {
		vp_emptyroom_viewpager = (ViewPager) findViewById(R.id.vp_emptyroom_viewpager);
		view1 = (TextView) findViewById(R.id.view1);
		view2 = (TextView) findViewById(R.id.view2);
		view3 = (TextView) findViewById(R.id.view3);
		view4 = (TextView) findViewById(R.id.view4);
		view5 = (TextView) findViewById(R.id.view5);
		view6 = (TextView) findViewById(R.id.view6);

	}

	private void initEvent() {
		view1.setOnClickListener(this);
		view2.setOnClickListener(this);
		view3.setOnClickListener(this);
		view4.setOnClickListener(this);
		view5.setOnClickListener(this);
		view6.setOnClickListener(this);
		vp_emptyroom_viewpager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		clearViewColor();
		switch (v.getId()) {

		case R.id.view1:
			vp_emptyroom_viewpager.setCurrentItem(0, true);
			setViewColor(v.getId());
			break;
		case R.id.view2:
			setViewColor(v.getId());
			vp_emptyroom_viewpager.setCurrentItem(1, true);
			break;
		case R.id.view3:
			vp_emptyroom_viewpager.setCurrentItem(2, true);
			setViewColor(v.getId());
			break;
		case R.id.view4:
			vp_emptyroom_viewpager.setCurrentItem(3, true);
			setViewColor(v.getId());
			break;
		case R.id.view5:
			vp_emptyroom_viewpager.setCurrentItem(4, true);
			setViewColor(v.getId());
			break;
		case R.id.view6:
			vp_emptyroom_viewpager.setCurrentItem(5, true);
			setViewColor(v.getId());
			break;

		}

	}

	private void setViewColor(int id) {
		TextView tv = (TextView) findViewById(id);
		tv.setBackgroundColor(Color.rgb(0x8c, 0xbf, 0x26));
		tv.setTextColor(Color.rgb(0xff, 0xff, 0xff));
	}

	private void clearViewColor() {
		for (TextView v : views) {
			v.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
			v.setTextColor(Color.BLACK);

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		clearViewColor();
		setViewColor(views.get(arg0).getId());

	}

}
