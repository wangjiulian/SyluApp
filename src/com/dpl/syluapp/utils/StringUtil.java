package com.dpl.syluapp.utils;

import android.util.Log;

public class StringUtil {

	public static String FilterAuthor(String author) {
		StringBuffer buffer = new StringBuffer();
		char sb[] = author.toCharArray();

		for (int i = 0; i < author.length(); i++) {
			if ('.' != author.charAt(i) && ' ' != author.charAt(i)) {
				buffer.append(author.charAt(i));
			}
		}

		return buffer.toString();
	}
	public static boolean isStrEquals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null || str2 == null) {
			return false;
		}
		if (str1.equalsIgnoreCase(str2)) {
			return true;
		}
		return false;
	}

	public static String notNull(String str) {
		if (str == null || str.equalsIgnoreCase("")) {
			return "null";
		} else {
			return str;
		}
	}

	public static boolean isNotTab(String str) {
		char ch;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);

			if (Character.isWhitespace(ch))
				return false;

		}
		return true;

	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String InsertEscChar(String str, char escapeChar) {
		if (escapeChar == '%') {
			if (str.indexOf(String.valueOf('%')) != -1) {
				str = str.replaceAll("%", "\n%");
			}
		} else if (escapeChar == '_') {
			if (str.indexOf(String.valueOf('_')) != -1) {
				str = str.replaceAll("_", "\n_");
			}
		} else if (escapeChar == '\'') {
			if (str.indexOf(String.valueOf('\'')) != -1) {
				str = str.replaceAll("\'", "\''");
			}
		}
		return str;
	}

	public static String convertEscapeSequence(String orig) {
		orig = orig.replaceAll("&ldquo;", "\u201c");
		orig = orig.replaceAll("&rdquo;", "\u201d");
		orig = orig.replaceAll("&hellip;", "\u2026");
		orig = orig.replaceAll("&mdash;", "\u2014");
		orig = orig.replaceAll("&lsquo;", "\u2018");
		orig = orig.replaceAll("&rsquo;", "\u2019");
		orig = orig.replaceAll("&middot;", "\u00b7");
		return orig;
	}

	public static String cutAWord(String ori, int maxEms) {
		if (ori != null) {
			StringBuffer nowStr = new StringBuffer();
			int len = ori.length();
			if (len > maxEms) {
				nowStr.append(ori.substring(0, maxEms));
				nowStr.append("\u2026");
				String last = nowStr.toString();
				return last;
			}
		}
		return ori;
	}

	public static boolean hasChinese(String input) {
		return input.getBytes().length != input.length();
	}

	public static String[] splitString(String spacing, String src) {
		if (isNullOrEmpty(src)) {
			return null;
		}
		return src.split(spacing);
	}

	public static boolean isAllNumber(String str) {
		if (str == null || str.equalsIgnoreCase("")) {
			return false;
		}
		return str.matches("[0-9]+");
	}

	public static boolean isThreeNumber(String str) {
		return str.matches("^\\d{3}$");
	}

}
