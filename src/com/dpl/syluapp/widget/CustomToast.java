package com.dpl.syluapp.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * 自定义Toast
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class CustomToast {

	private static int length;

	public static void showShortText(Context mContext, String text) {
		length = Toast.LENGTH_SHORT;
		showText(mContext, text);
	}

	public static void showLongText(Context mContext, String text) {
		length = Toast.LENGTH_LONG;
		showText(mContext, text);
	}

	private static void showText(Context context, String text) {
		Toast.makeText(context, text, length).show();
		// Toast toast = new Toast(context);
		// toast.setText(text);
		// toast.setDuration(length);
		// toast.show();

	}
}
