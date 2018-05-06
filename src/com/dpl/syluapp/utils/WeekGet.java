package com.dpl.syluapp.utils;

public class WeekGet {
	public  String[] getWeek(String s) {

		String[] re = s.split("\\{");
		String c = re[1].toString();
		String[] re1 = c.split("\\}");
		String[] re2 = re1[0].toString().split("\\-");
		String k = re2[0].toString().substring(1, re2[0].toString().length());
		String l = re2[1].toString().substring(0,
				re2[1].toString().length() - 1);
		String[] result = new String[2];
		result[0] = k;
		result[1] = l;
		return result;
	}

}
