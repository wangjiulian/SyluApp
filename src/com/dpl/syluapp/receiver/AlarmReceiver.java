package com.dpl.syluapp.receiver;

import java.util.Calendar;

import com.dpl.syluapp.preferences.LibrarySign;
import com.dpl.syluapp.service.LibraryConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String hour = String.valueOf(Calendar.getInstance().get(
				Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(Calendar.getInstance().get(
				Calendar.MINUTE));
		String nowTime = hour + ":" + minute;
		LibrarySign info = new LibrarySign(context);
		String time = info.getTiemNote(hour, minute);
		System.out.println("time" + time);
		if (nowTime.equals(time))

		{
			System.out.println("到点了！！！");

			Intent it = new Intent(context, LibraryConnect.class);

			context.startService(it);

		}

	}
}
