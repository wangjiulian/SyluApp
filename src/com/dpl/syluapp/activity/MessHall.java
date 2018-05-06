package com.dpl.syluapp.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.ksoap2.serialization.SoapObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.ImageUrl;
import com.dpl.syluapp.model.MessHallInfo;
import com.dpl.syluapp.net.ImageTask;
import com.dpl.syluapp.widget.MyGridView;

public class MessHall extends AppActivity implements OnClickListener {
	private Button back;
	private Context context;;
	private static final String TAG = "MessHall";
	private TextView dowmMess;
	private String path = Environment.getExternalStorageDirectory() + "/mess";
	private LinearLayout llCcasecade;
	private LinearLayout lvCasecade1;
	private LinearLayout lvCasecade2;
	private LinearLayout lvCasecade3;
	private Display display;
	private int casecadeWidth;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点

	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	private ProgressDialog dialog;
	private List<MessHallInfo> list = new ArrayList<MessHallInfo>();
	private SoapObject resultObj;;
	private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private MyGridView mGridView;
	private int[] imageRes = { R.drawable.f_taocan, R.drawable.f_noodles,
			R.drawable.f_tese, R.drawable.f_xiaochao, R.drawable.f_bre,
			R.drawable.f_drink, R.drawable.f_takeout, R.drawable.f_menu };
	private String[] itemName = { "经典套餐", "最喜面食", "特色美味", "精致小炒", "早餐午茶",
			"饮品大全" };
	private Handler Ihandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				dialog.dismiss();
				resultObj = (SoapObject) msg.obj;
				System.out.println("ss" + resultObj.toString());
				if (!resultObj.toString().equals("anyType{}")) {
					list.clear();
					SoapObject resultobj1 = (SoapObject) resultObj
							.getProperty(0);
					int len = resultobj1.getPropertyCount();
					for (int i = 0; i < len; i++) {
						MessHallInfo info = new MessHallInfo();
						info.setShopName(resultobj1.getProperty(i).toString());
						list.add(info);

					}
					for (int j = 0; j < list.size(); j++) {
						System.out.println("list--->"
								+ list.get(j).getShopName().toString());

					}
					Intent intent = new Intent();
					/* intent.setClass(MessHall.this, messClassicfy.class); */
					intent.putExtra("list", (Serializable) list);
					startActivity(intent);
				}

			}

			if (msg.what == 9) {

				if (dialog != null)
					dialog.dismiss();

			}
		};

	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		display = getWindowManager().getDefaultDisplay();
		casecadeWidth = display.getWidth() / 3;
		context = MessHall.this;
		setContentView(R.layout.activity_messhall_main);
		init();

	}

	void init() {
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		dowmMess = (TextView) findViewById(R.id.down);
		llCcasecade = (LinearLayout) findViewById(R.id.llyt_messhall_llCcasecade);
		lvCasecade1 = (LinearLayout) findViewById(R.id.llyt_messhall_casecade1);
		lvCasecade2 = (LinearLayout) findViewById(R.id.llyt_messhall_casecade2);
		lvCasecade3 = (LinearLayout) findViewById(R.id.llyt_messhall_casecade3);
		imageResId = new int[] { R.drawable.vp_11, R.drawable.vp_22,
				R.drawable.vp_33 };// R.drawable.d,

		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(MessHall.this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_messhall_dot0));
		dots.add(findViewById(R.id.v_messhall_dot1));
		dots.add(findViewById(R.id.v_messhall_dot2));

		viewPager = (ViewPager) findViewById(R.id.vp_messhall_vp);

		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		mGridView = (MyGridView) findViewById(R.id.mgv_messhall_mv);
		int length = itemName.length;
		for (int i = 0; i < length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImageView", imageRes[i]);
			map.put("ItemTextView", itemName[i]);
			data.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(MessHall.this, data,
				R.layout.messhall_item, new String[] { "ItemImageView",
						"ItemTextView" }, new int[] { R.id.ItemImageView,
						R.id.ItemTextView });
		mGridView.setAdapter(simpleAdapter);
		mGridView.setOnItemClickListener(new GridViewItemOnClick());
		findView();
		SharedPreferences preferences = context.getSharedPreferences(
				"imageNum", Context.MODE_PRIVATE);
		boolean down_flag = preferences.getBoolean("down", false);
		/*
		 * if(down_flag){
		 * 
		 * dowmMess.setVisibility(View.GONE);
		 * llCcasecade.setVisibility(View.VISIBLE); }
		 */
	}

	public class GridViewItemOnClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			dialog = new ProgressDialog(MessHall.this);
			dialog.setTitle("下载提醒");
			dialog.setMessage("正在获取档口信息，请稍后.....");

			dialog.show();
			String t = data.get(position).get("ItemTextView").toString();
			System.out.println("---->" + t);
			Map<Object, Object> params = new HashMap<Object, Object>();
			params.put("shopkind", t);
			/*
			 * new SoapCommonTools("Shitang_OneKindShops", params, handler, 1)
			 * .startSoap();
			 */

		}
	}

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
				Ihandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

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
						Uri u = Uri.parse("http://page.renren.com/601900897");
						Intent it = new Intent();
						it.setData(u);
						it.setAction(Intent.ACTION_VIEW);
						it.setClassName("com.android.browser",
								"com.android.browser.BrowserActivity");
						MessHall.this.startActivity(it);
						break;
					case 1:// http://www.sylu.edu.cn/sylusite/slgyw/
						Uri u1 = Uri
								.parse("http://www.sylu.edu.cn/sylusite/slgyw/");
						Intent it1 = new Intent();
						it1.setData(u1);
						it1.setAction(Intent.ACTION_VIEW);
						it1.setClassName("com.android.browser",
								"com.android.browser.BrowserActivity");
						MessHall.this.startActivity(it1);

						break;
					case 2:

						Uri emailUri = Uri
								.parse("mailto:syluapp@mail.sylu.edu.cn");
						Uri u11 = Uri
								.parse("http://218.25.35.27:8080/(3vz5wk55qpx5px45zjepgz3x)/default2.aspx");
						Intent it11 = new Intent(Intent.ACTION_SENDTO, emailUri);
						/* it11.setData(emailUri); */
						/* it11.setAction(Intent.ACTION_SENDTO); */
						/*
						 * it11.setClassName("com.android.browser",
						 * "com.android.browser.BrowserActivity");
						 */
						MessHall.this.startActivity(it11);

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

	private void findView() {

		LayoutParams lp1 = lvCasecade1.getLayoutParams();
		lp1.width = casecadeWidth;
		lvCasecade1.setLayoutParams(lp1);

		LayoutParams lp2 = lvCasecade2.getLayoutParams();
		lp2.width = casecadeWidth;
		lvCasecade2.setLayoutParams(lp2);

		LayoutParams lp3 = lvCasecade3.getLayoutParams();
		lp3.width = casecadeWidth;
		lvCasecade3.setLayoutParams(lp3);
		int j = 0;
		for (int i = 0; i < ImageUrl.imageUrls.length; i++) {

			addImage(ImageUrl.imageUrls[i], i, j);
			j++;
			if (j >= 3)
				j = 0;

		}

	}

	private void addImage(String url, final int i, int j) {

		ImageView iv = (ImageView) LayoutInflater.from(MessHall.this).inflate(
				R.layout.item, null);
		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MessHall.this, "第  ——>" + i + "张图片", 1).show();

			}
		});
		if (j == 0) {
			lvCasecade1.addView(iv);
		} else if (j == 1) {
			lvCasecade2.addView(iv);
		} else {
			lvCasecade3.addView(iv);
		}

		ImageTask task = new ImageTask(context, iv, casecadeWidth, i,
				llCcasecade, dowmMess);
		task.execute(url);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		}
	}
}
