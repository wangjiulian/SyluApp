package com.dpl.syluapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LibrarySign {
	private  static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;

	private Context context;
   public static boolean save(){
	   return editor.commit();
   }
	public void lode(Context context) {
		preferences = context.getSharedPreferences("dayBefroreInfo",
				Context.MODE_PRIVATE);
		editor = preferences.edit();

	}
     
	public static void  setDayBeforeNote(int day){
		editor.putInt("hour", day);
		save();
		
	};
	
	
	public LibrarySign(Context context) {

		this.context = context;
	}

	public void setdaybeforeNote(int day) {

		SharedPreferences shareNote = context.getSharedPreferences(
				"dayBefroreInfo", Context.MODE_PRIVATE);// 存储在userinfo.xml
		// 对数据进行编辑
		SharedPreferences.Editor editor = shareNote.edit();
		editor.putInt("hour", day);
		editor.commit();

	}
   public static int getDayBeforeNote(){
	   
	   return preferences.getInt("hour", 3);
   }
	public int getdaybeforeNote() {
		int day;
		SharedPreferences shareNote = context.getSharedPreferences(
				"dayBefroreInfo", Context.MODE_PRIVATE);
		day = shareNote.getInt("hour", 3);

		return day;

	}
      public static void setTimeNot(int hour, int minute){
    	  String timeStr = hour + ":" + minute;
    	  editor.putString(timeStr, timeStr);
    	  save();
      }
	public void setTimeNote(int hour, int minute) {

		SharedPreferences shareNote = context.getSharedPreferences("timeInfo",
				Context.MODE_PRIVATE);// 存储在userinfo.xml
		// 对数据进行编辑
		SharedPreferences.Editor editor = shareNote.edit();
		String timeStr = hour + ":" + minute;
		editor.putString(timeStr, timeStr).commit();

	}
public static String getTimeNot(String hour, String minute){
	return null;
}
	public String getTiemNote(String hour, String minute) {

		SharedPreferences shareNote = context.getSharedPreferences("timeInfo",
				Context.MODE_PRIVATE);
		SharedPreferences shareNote1 = context.getSharedPreferences("timeset",
				Context.MODE_PRIVATE);
		boolean tiem_flag = shareNote1.getBoolean("flag", false);
		String time;
		if (tiem_flag)

			time = shareNote.getString(hour + ":" + minute, null);
		else
			time = shareNote.getString(hour + ":" + minute, "12:0");

		return time;

	}

	public void openSwitch() {

		SharedPreferences shareNote = context.getSharedPreferences("noteInfo",
				Context.MODE_PRIVATE);// 存储在userinfo.xml
		// 对数据进行编辑
		SharedPreferences.Editor editor = shareNote.edit();
		editor.putBoolean("swichState", true);

		editor.commit();// 将数据持久化到存储介质中
		boolean flag = getswitchState();
		System.out.println("openSwitch--->" + flag);

	}

	public void closeSwitch() {

		SharedPreferences shareNote = context.getSharedPreferences("noteInfo",
				Context.MODE_PRIVATE);// 存储在userinfo.xml
		// 对数据进行编辑
		SharedPreferences.Editor editor = shareNote.edit();
		editor.putBoolean("swichState", false);

		editor.commit();// 将数据持久化到存储介质中
		boolean flag = getswitchState();
		System.out.println("closeSwitch--->" + flag);
	}

	public boolean getswitchState() {
		SharedPreferences shareNote = context.getSharedPreferences("noteInfo",
				Context.MODE_PRIVATE);// 存储在userinfo.xml
		boolean flag = shareNote.getBoolean("swichState", true);
		return flag;

	}

}
