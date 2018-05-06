package com.dpl.syluapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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

import com.dpl.syluapp.activity.CampusTnfo;
import com.dpl.syluapp.activity.EmptyRoom1;
import com.dpl.syluapp.activity.HeadSetting;
import com.dpl.syluapp.activity.LibraryLogin;
import com.dpl.syluapp.activity.MessHall;
import com.dpl.syluapp.activity.ScoreSearchLogin;
import com.dpl.syluapp.activity.SyllabusLogin;
import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.service.NowWeekService;
import com.dpl.syluapp.utils.BitmapCompress;
import com.dpl.syluapp.utils.WebBrowser1;
import com.dpl.syluapp.widget.SlidingMenu;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
//pushservice

public class MainActivity extends Activity implements OnClickListener {
	private Context mContext;
	private Bitmap upbitmap;
	private String iamgePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "syluapp"
			+ File.separator
			+ "head.jpg";
	private ImageView headIView;
	private TextView tv_menu_quit;
	private RelativeLayout headRLayout;
	private Button timeTable;
	private Button emptyRoom;
	private Button mess;
	private Button btn_frg_socre;
	private Button librarySelect;
	private Button campusInfo;
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

	private SlidingMenu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		startService(new Intent(MainActivity.this, NowWeekService.class));
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
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		initHead();

	}

	public void toggleMenu(View view) {
		mMenu.toggle();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_frg_timetable:
			startActivity(new Intent(MainActivity.this, SyllabusLogin.class));
			break;
		case R.id.btn_frg_campusinfo:
			startActivity(new Intent(MainActivity.this, CampusTnfo.class));

			break;
		case R.id.btn_frg_emptyroom:
			startActivity(new Intent(MainActivity.this, EmptyRoom1.class));

			break;
		case R.id.btn_frg_socre:
			startActivity(new Intent(MainActivity.this, ScoreSearchLogin.class));
			break;
		case R.id.btn_frg_mess:
			startActivity(new Intent(MainActivity.this, MessHall.class));
			break;
		case R.id.btn_frg_libraryselect:
			Intent intent = new Intent(MainActivity.this, LibraryLogin.class);
			String name = LibraryUserInfo.getUser();
			String pswd = LibraryUserInfo.getPswd();
			intent.putExtra("name", name);
			intent.putExtra("pswd", pswd);

			startActivity(intent);
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
		}

	}

	void findView() {
		tv_menu_quit = (TextView) findViewById(R.id.tv_menu_quit);

		headIView = (ImageView) findViewById(R.id.lv_menu_tou);
		headRLayout = (RelativeLayout) findViewById(R.id.head);
		timeTable = (Button) findViewById(R.id.btn_frg_timetable);
		emptyRoom = (Button) findViewById(R.id.btn_frg_emptyroom);
		mess = (Button) findViewById(R.id.btn_frg_mess);
		btn_frg_socre = (Button) findViewById(R.id.btn_frg_socre);

		librarySelect = (Button) findViewById(R.id.btn_frg_libraryselect);

		campusInfo = (Button) findViewById(R.id.btn_frg_campusinfo);
		tv_menu_quit.setOnClickListener(this);
		headRLayout.setOnClickListener(this);
		timeTable.setOnClickListener(this);
		emptyRoom.setOnClickListener(this);
		mess.setOnClickListener(this);
		librarySelect.setOnClickListener(this);
		btn_frg_socre.setOnClickListener(this);
		campusInfo.setOnClickListener(this);
		imageResId = new int[] { R.drawable.vp_11, R.drawable.vp_22,
				R.drawable.vp_33 };// R.drawable.d,

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(MainActivity.this);
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
						Intent intent = new Intent(MainActivity.this,
								WebBrowser1.class);
						intent.putExtra("url", url);
						startActivity(intent);

						break;
					case 1:

						String url1 = "http://www.sylu.edu.cn/sylusite/slgyw/";
						Intent intent1 = new Intent(MainActivity.this,
								WebBrowser1.class);
						intent1.putExtra("url", url1);
						startActivity(intent1);
						break;
					case 2:
						String url11 = "mailto:syluapp@mail.sylu.edu.cn";
						Intent intent11 = new Intent(MainActivity.this,
								WebBrowser1.class);
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
		initHead();
		// TODO Auto-generated method stub
		super.onResume();
	}

	void initHead() {
		File file = new File(iamgePath);
		Log.i("TAG", file.getPath());

		if (file.exists()) {
			Bitmap photo = BitmapFactory.decodeFile(iamgePath);
			upbitmap = BitmapCompress.compressImage(photo);

			headIView.setImageBitmap(upbitmap);

		}

	}
}