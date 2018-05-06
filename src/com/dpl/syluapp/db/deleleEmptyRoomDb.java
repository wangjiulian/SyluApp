package com.dpl.syluapp.db;

import com.dpl.syluapp.application.MoApplication;

import android.content.Context;

public class deleleEmptyRoomDb {
	private static Context context;
	private static EmptyRoomDbOpenHelper room1, room2, room3, room4;

	public static void deleteDb() {
		context = MoApplication.getInstance().context;
		room1 = new EmptyRoomDbOpenHelper(context, "room1");
		room2 = new EmptyRoomDbOpenHelper(context, "room2");
		room3 = new EmptyRoomDbOpenHelper(context, "room3");
		room4 = new EmptyRoomDbOpenHelper(context, "room4");
		room1.deleteDatabase(context);
		room2.deleteDatabase(context);
		room3.deleteDatabase(context);
		room4.deleteDatabase(context);

	}

}
