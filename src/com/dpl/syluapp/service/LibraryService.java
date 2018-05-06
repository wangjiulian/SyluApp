package com.dpl.syluapp.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dpl.syluapp.preferences.LibrarySign;
import com.dpl.syluapp.receiver.AlarmReceiver;

public class LibraryService extends Service {
	
	private Calendar c1 = Calendar.getInstance();
	private Intent mintent;
	private PendingIntent pi;
	private AlarmManager am;

	@Override
	public void onCreate() {

		LibrarySign note = new LibrarySign(LibraryService.this);
		boolean flag = note.getswitchState();
		if (!flag)
			stopSelf();// 关闭service服务
	
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {

		am.cancel(pi);

		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mintent = new Intent(this, AlarmReceiver.class);
		pi = PendingIntent.getBroadcast(LibraryService.this, 0, mintent, 0);
		// 设置一个PendingIntent对象，发送广播
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 获取AlarmManager对象
		// am.set(AlarmManager.RTC_WAKEUP, c1.getTimeInMillis(), pi);
		am.setRepeating(AlarmManager.RTC, 0, 60000, pi);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}

