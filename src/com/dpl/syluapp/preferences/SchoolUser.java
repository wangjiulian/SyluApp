/**
 * 
 */
package com.dpl.syluapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.dpl.syluapp.application.MoApplication;

/**
 * @author JUST玖
 * 
 *         2015-6-12
 */
public class SchoolUser {

	public static SharedPreferences scorePre;
	public static SharedPreferences syllabusPre;
	public static SharedPreferences.Editor syllabusEd;
	public static SharedPreferences.Editor scoreEd;

	

	public static void loadSyllabus() {
		syllabusPre = MoApplication.getInstance().context.getSharedPreferences(
				"syllabusPre", Context.MODE_PRIVATE);
		syllabusEd = syllabusPre.edit();
	}

	public static void clearSyllabusUser() {
		loadSyllabus();
		syllabusEd.clear().commit();
	}

	

	public static void setSyllabusUser(String str) {
		loadSyllabus();
		syllabusEd.putString("user", str).commit();
	}

	public static void setSyllabusPswd(String str) {
		loadSyllabus();
		syllabusEd.putString("pswd", str).commit();
	}
	public static String getSyllabusPswd() {
		loadSyllabus();
		return syllabusPre.getString("pswd", "");
	}
	public static String getSyllabusUser() {
		loadSyllabus();
		return syllabusPre.getString("user", "");
	}

	public static void loadScore() {
		scorePre = MoApplication.getInstance().context.getSharedPreferences(
				"scorePre", Context.MODE_PRIVATE);
		scoreEd = scorePre.edit();
	}
	public static void setScoreUser(String str) {
		loadScore();
		scoreEd.putString("user", str).commit();
	}

	

	public static void setScorePswd(String str) {
		loadScore();
		scoreEd.putString("pswd", str).commit();
	}

	public static String getScoreUser() {
		loadScore();
		return scorePre.getString("user", "");
	}

	public static String getScorePswd() {
		loadScore();
		return scorePre.getString("pswd", "");
	}
	public static void clearScoreUser() {
		loadScore();
		scoreEd.clear().commit();
	}
}
