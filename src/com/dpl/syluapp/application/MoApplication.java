package com.dpl.syluapp.application;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.dpl.syluapp.db.EmptyRoomDbOpenHelper;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.widget.CustomToast;

public class MoApplication extends Application {
	private SharedPreferences mSharedPreferences, fSharedPreferences;
	private SharedPreferences.Editor editor, fEditor;
	private Calendar mCalendar;
	public static Context context;
	private static List<Activity> mActivityList = new LinkedList<Activity>();
	private static MoApplication mInstance = null;
	public Bitmap bitmap;
	public String UrlImage;
	public boolean firstLocaetd;
	private boolean firstUse;
	public int totalItem;
	public static String networkType;
	private Handler handler;
	private HashMap<String, String> filterParams;

	@Override
	public void onCreate() {
		super.onCreate();
		/* SDKInitializer.initialize(getApplicationContext()); */

		context = this;
		LibraryUserInfo.load(context);
	  // init();
		System.out.println("Application start!");
		mInstance = this;
		firstLocaetd = true;

		networkType = getNetworkType();
		SDKInitializer.initialize(this);
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
				//CustomToast.showShortText(context, "删除数据库");
				EmptyRoomDbOpenHelper helper = new EmptyRoomDbOpenHelper(
						context, "test");
				helper.deleteDatabase(context);
				editor.putString("month_day", month + day).commit();

			} else {
				CustomToast.showShortText(context, "时间未更新");
			}
		} else {
			
			fEditor = fSharedPreferences.edit();
			fEditor.putBoolean("first", false).commit();
		}

	}

	public static MoApplication getInstance() {
		return mInstance;
	}

	public void setUrlImage(String url) {
		UrlImage = url;
	}

	public String getUrlImage() {
		return UrlImage;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void located() {
		firstLocaetd = true;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public HashMap<String, String> getFilterParams() {
		return filterParams;
	}

	public void setFilterParams(HashMap<String, String> filterParams) {
		this.filterParams = filterParams;
	}

	public static void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public static void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	public static void exit() {
		for (Activity activity : mActivityList) {
			if (!activity.isFinishing())
				activity.finish();
		}

	}

public	static boolean getNetworStatus() {

		ConnectivityManager manager = (ConnectivityManager) mInstance
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			return false;
		} else
			return true;
	}

	public String getNetworkType() {
		String networkType = "wifi";
		ConnectivityManager manager = (ConnectivityManager) mInstance
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			Toast.makeText(context, "当前网络不可用!", Toast.LENGTH_SHORT).show();
			return "";
		}

		String info = netWrokInfo.getExtraInfo();
		if ((info != null)
				&& ((info.trim().toLowerCase().equals("cmwap"))
						|| (info.trim().toLowerCase().equals("uniwap"))
						|| (info.trim().toLowerCase().equals("3gwap")) || (info
						.trim().toLowerCase().equals("ctwap")))) {
			if (info.trim().toLowerCase().equals("ctwap")) {
				networkType = "ctwap";
			} else {
				networkType = "cmwap";
			}
		}
		return networkType;
	}

}
