package com.dpl.syluapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;

	static void load(Context context) {
		preferences = context
				.getSharedPreferences("User", Context.MODE_PRIVATE);
		editor = preferences.edit();

	}

	static void setPhone(String phone) {
		editor.putString("phone", phone);
		save();

	}

	static String getPhone() {

		return preferences.getString("phone", "");
	}
	static void setAccount(String account) {
		editor.putString("account", account);
		save();

	}

	static String getAccount() {

		return preferences.getString("account", "");
	}
	static void setPswd(String pswd) {
		editor.putString("pswd", pswd);
		save();

	}

	static String getPswd() {

		return preferences.getString("pswd", "");
	}
	static void setNickName(String nickname) {
		editor.putString("nickname", nickname);
		save();

	}

	static String getNickName() {

		return preferences.getString("nickname", "");
	}
	
	static void setGander(String gander) {
		editor.putString("gander", gander);
		save();

	}

	static String getGander() {

		return preferences.getString("gander", "");
	}
	static void setAge(String age) {
		editor.putString("age", age);
		save();

	}

	static String getAge() {

		return preferences.getString("age", "");
	}
	
	static void setHomeTown(String hometown) {
		editor.putString("hometown", hometown);
		save();

	}

	static String getHomeTown() {

		return preferences.getString("hometown", "");
	}
	static void setStudentNumber(String studentNumber) {
		editor.putString("studentNumber", studentNumber);
		save();

	}

	static String getStudentNumber() {

		return preferences.getString("studentNumber", "");
	}
	
	static void setUserSign(String usersign) {
		editor.putString("usersign", usersign);
		save();

	}

	static String getUserSign() {

		return preferences.getString("usersign", "");
	}	
	
	static void setHeadPortrait(String headPortrait) {
		editor.putString("headPortrait", headPortrait);
		save();

	}

	static String getHeadPortrait() {

		return preferences.getString("headPortrait", "");
	}	
	
	static void save() {
		editor.commit();

	}
}
