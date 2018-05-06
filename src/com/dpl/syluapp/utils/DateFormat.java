package com.dpl.syluapp.utils;

import java.util.Calendar;

import com.dpl.syluapp.preferences.LibrarySign;

import android.content.Context;

public class DateFormat {
	Context context;
	Calendar c1 = Calendar.getInstance();
	Calendar c2 = java.util.Calendar.getInstance();

	public DateFormat(Context context) {
		this.context = context;

	}

	public DateFormat() {

	}

	public Calendar getCalendar() {
		int time[] = new int[2];
		LibrarySign timeNote = new LibrarySign(context);
		// time = timeNote.getTiemNote();
		c1.setTimeInMillis(System.currentTimeMillis());
		c1.set(Calendar.HOUR_OF_DAY, time[0]);
		c1.set(Calendar.MINUTE, time[1]);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1;
	}

	public boolean startNote() {

		boolean flag = false;
		int noteTime = 20;
		int hour = c1.get(Calendar.HOUR_OF_DAY);
		if (noteTime - hour > 0) {
			flag = true;
			return flag;
		}
		return flag;
	}

	public int conTrast(String ruturnDay) {
		int flag;
		int year = Integer.parseInt(ruturnDay.substring(0, 4));
		int month = Integer.parseInt(ruturnDay.substring(5, 7));
		int day = Integer.parseInt(ruturnDay.substring(8, 10));
		c2.set(year, month - 1, day);

		System.out.println("year--->" + c1.get(Calendar.YEAR) + "--->month"
				+ c1.get(Calendar.MONTH) + "day--->"
				+ c1.get(Calendar.DAY_OF_MONTH));
		System.out.println("year--->" + c2.get(Calendar.YEAR) + "--->month"
				+ c2.get(Calendar.MONTH) + "day--->"
				+ c2.get(Calendar.DAY_OF_MONTH));

		flag = (int) ((c2.getTime().getTime() - c1.getTime().getTime()) / (1000 * 60 * 60 * 24));
		System.out.println("i---->" + flag);

		return flag;

	}

}
