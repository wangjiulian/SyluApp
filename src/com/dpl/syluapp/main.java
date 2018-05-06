package com.dpl.syluapp;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.activity.CampusTnfo;
import com.dpl.syluapp.activity.EmptyRoom1;
import com.dpl.syluapp.activity.HeadSetting;
import com.dpl.syluapp.activity.LibraryLogin;
import com.dpl.syluapp.activity.ScoreSearchLogin;
import com.dpl.syluapp.activity.Setting;
import com.dpl.syluapp.activity.SyllabusLogin;
import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.net.WeekGetNet;
import com.dpl.syluapp.net.WeekGetNet.NetCallBack;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.service.NowWeekService;
import com.dpl.syluapp.utils.WebBrowser;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;

public class main extends AppActivity implements OnClickListener {
	private RelativeLayout rrlt_back;;
	private TextView constanttitlebar_title;
	private RelativeLayout setting;
	private TextView tv_week;
	private Context mContext;
	private Bitmap upbitmap;
	private String iamgePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "syluapp"
			+ File.separator
			+ "head.jpg";
	private Button timeTable;
	private RelativeLayout newstudentzone;
	private RelativeLayout roomsearch;
	// private Button mess;
	private RelativeLayout coursesearch;
	private RelativeLayout booksearch;
	private RelativeLayout schoolcampus;
	private RelativeLayout scoresearch;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		setActivityTitle("主页");
		mContext = this;
		startService(new Intent(main.this, NowWeekService.class));
		Log.i("TAG",
				"用户名-->" + LibraryUserInfo.getUser() + "--名字-->"
						+ LibraryUserInfo.getName() + "--读者类型-->"
						+ LibraryUserInfo.getUserStyle() + "--累计借书-->"
						+ LibraryUserInfo.getAccumulateBook() + "--欠费金额-->"
						+ LibraryUserInfo.getDebtMoney());
		// 自动更新
		UmengUpdateAgent.update(this);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		PushAgent.getInstance(this).onAppStart();
		
		String device_token = UmengRegistrar.getRegistrationId(this);

		findView();
		
		

	}

	void findView() {
		// private Button back;
		// private TextView constanttitlebar_title;
		rrlt_back = (RelativeLayout) findViewById(R.id.rrlt_back);
		constanttitlebar_title = (TextView) findViewById(R.id.constanttitlebar_title);
		rrlt_back.setVisibility(View.GONE);
		rrlt_back.setClickable(false);
		setting = (RelativeLayout) findViewById(R.id.setting);
		tv_week = (TextView) findViewById(R.id.tv_week);
		if (MoApplication.getInstance().getNetworStatus()) {
			WeekGetNet.getWeek(new NetCallBack() {

				@Override
				public void OnSuccess(String week) {
					tv_week.setText("当前为第" + week + "教学周");
				}

				@Override
				public void OnFailed() {
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					  
					  String date=dateFormat.format(new java.util.Date());
					  tv_week.setText(date);

				}
				  
				  
			});
		} else {
			tv_week.setText("当前网络不可用");
		}
		scoresearch = (RelativeLayout) findViewById(R.id.scoresearch);

		newstudentzone = (RelativeLayout) findViewById(R.id.newstudentzone);
		roomsearch = (RelativeLayout) findViewById(R.id.roomsearch);
		// mess = (Button) findViewById(R.id.btn_frg_mess);
		coursesearch = (RelativeLayout) findViewById(R.id.coursesearch);

		booksearch = (RelativeLayout) findViewById(R.id.booksearch);

		schoolcampus = (RelativeLayout) findViewById(R.id.schoolcampus);
		newstudentzone.setOnClickListener(this);
		scoresearch.setOnClickListener(this);
		roomsearch.setOnClickListener(this);
		// mess.setOnClickListener(this);
		booksearch.setOnClickListener(this);
		coursesearch.setOnClickListener(this);
		setting.setOnClickListener(this);
		schoolcampus.setOnClickListener(this);
		imageResId = new int[] { R.drawable.vp_11, R.drawable.vp_22,
				R.drawable.vp_33 };// R.drawable.d,

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(main.this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			/* tv_title.setText(titles[position]); */
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			final int Postision = position;
			View view = imageViews.get(position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (Postision) {
					case 0:
						String url = "http://page.renren.com/601900897";
						Intent intent = new Intent(main.this, WebBrowser.class);
						intent.putExtra("url", url);
						startActivity(intent);

						break;
					case 1:

						String url1 = "http://www.sylu.edu.cn/sylusite/slgyw/";
						Intent intent1 = new Intent(main.this, WebBrowser.class);
						intent1.putExtra("url", url1);
						startActivity(intent1);
						break;
					case 2:
						String url11 = "mailto:syluapp@mail.sylu.edu.cn";
						Intent intent11 = new Intent(main.this,
								WebBrowser.class);
						intent11.putExtra("url", url11);
						startActivity(intent11);

						break;

					}

					System.out.println("点击第" + Postision + "图片");
				}
			});
			ViewPager viewPager = (ViewPager) container;
			viewPager.addView(view);
			return imageViews.get(position);

		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	@Override
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	public void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	@Override
	protected void onDestroy() {


		super.onDestroy();

	};

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {

				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newstudentzone:
			String url = "http://www.baidu.com";
			Intent intent = new Intent(main.this, WebBrowser.class);
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.coursesearch:
			startActivity(new Intent(main.this, SyllabusLogin.class));
			break;
		case R.id.schoolcampus:
			startActivity(new Intent(main.this, CampusTnfo.class));

			break;
		case R.id.roomsearch:
			startActivity(new Intent(main.this, EmptyRoom1.class));

			break;
		case R.id.scoresearch:
			startActivity(new Intent(main.this, ScoreSearchLogin.class));
			break;
		// case R.id.btn_frg_mess:
			
		// startActivity(new Intent(main.this, MessHall.class));
		// break;
		case R.id.booksearch:
			Intent intent1 = new Intent(main.this, LibraryLogin.class);
			String name = LibraryUserInfo.getUser();
			String pswd = LibraryUserInfo.getPswd();
			intent1.putExtra("name", name);
			intent1.putExtra("pswd", pswd);
			String i =intent1.toURI();
			startActivity(intent1);
			break;
		case R.id.head:
			startActivity(new Intent(this, HeadSetting.class));
			break;

		case R.id.tv_menu_quit:
			final Dialog quitDialog = new Dialog(mContext,
					android.R.style.Theme_Translucent_NoTitleBar);
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_quit, null);
			RelativeLayout choice_one_layout = (RelativeLayout) view
					.findViewById(R.id.choice_one_layout);
			choice_one_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					quitDialog.dismiss();
				}
			});
			RelativeLayout choice_two_layout = (RelativeLayout) view
					.findViewById(R.id.choice_two_layout);
			choice_two_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					quitDialog.dismiss();
					MoApplication.exit();

				}
			});

			LinearLayout layout = (LinearLayout) view
					.findViewById(R.id.quit_dialog_layout);
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					quitDialog.dismiss();
				}
			});

			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			quitDialog.setContentView(view, lp);
			quitDialog.show();

			break;
		case R.id.setting:
			startActivity(new Intent(main.this, Setting.class));
			break;
		}

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出校园通", 0).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
