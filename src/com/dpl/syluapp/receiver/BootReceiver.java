package com.dpl.syluapp.receiver;

import com.dpl.syluapp.preferences.LibrarySign;
import com.dpl.syluapp.service.LibraryService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver  extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		LibrarySign note = new LibrarySign(context);
		boolean flag = note.getswitchState();

		if (flag) {

			Intent mintent = new Intent(context, LibraryService.class);

			// mintent.setAction("com.just.servicetest.ServiceMain");
			context.startService(mintent);
		} else {

		}

	}
}
