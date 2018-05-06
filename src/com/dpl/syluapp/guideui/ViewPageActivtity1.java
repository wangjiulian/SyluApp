package com.dpl.syluapp.guideui;

import java.util.ArrayList;
import java.util.List;

import com.dpl.syluapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPageActivtity1 extends FragmentActivity {
	private ViewPager page;
	private FragmentPagerAdapter adapter;
	private fragement1 fragement1;
	private fragement2 fragement2;
	private fragement3 fragement3;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_viewpage);
		page = (ViewPager) findViewById(R.id.page);
		init();
	}

	private void init() {
		fragement1 = new fragement1();
		fragement2 = new fragement2();
		fragement3 = new fragement3();
		fragments.add(fragement1);
		fragments.add(fragement2);
		fragments.add(fragement3);
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragments.get(arg0);
			}
		};
		page.setAdapter(adapter);
    
	}

}
