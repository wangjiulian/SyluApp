package com.dpl.syluapp.preferences;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class LibraryUserInfo {
	private Context context;
	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor editor;

	public static void load(Context context) {
		sharedPreferences = context.getSharedPreferences("LibraryUser",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public static void setUser(String user) {
		editor.putString("user", user);
		save();

	}

	public static String getUser() {
		return sharedPreferences.getString("user", "");
	}

	public static void setPswd(String pswd) {
		editor.putString("pswd", pswd);
		save();

	}

	public static String getPswd() {
		return sharedPreferences.getString("pswd", "");
	}

	public static void setName(String name) {
		editor.putString("name", name);
		save();

	}

	public static String getName() {
		return sharedPreferences.getString("name", "");
	}

	public static void setUserStyle(String userStyle) {
		editor.putString("userStyle", userStyle);
		save();

	}

	public static String getUserStyle() {
		return sharedPreferences.getString("userStyle", "");
	}

	public static void setAccumulateBook(String accumlateBook) {
		editor.putString("accumlateBook", accumlateBook);
		save();

	}

	public static String getAccumulateBook() {
		return sharedPreferences.getString("accumlateBook", "");
	}

	public static void setDebtMoney(String debtMoney) {
		editor.putString("debtMoney", debtMoney);
		save();

	}

	public static String getDebtMoney() {
		return sharedPreferences.getString("debtMoney", "");
	}

	public static void setCanBorrow(String canBorrow) {
		editor.putString("canBorrow", canBorrow);
		save();

	}

	public static String getCanBorrow() {
		return sharedPreferences.getString("canBorrow", "");
	}

	public static void save() {
		editor.commit();
	}

	public static void clearInfo() {
		editor.clear().commit();
	}
}
