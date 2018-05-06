package com.dpl.syluapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;
import android.util.Log;

import com.dpl.syluapp.db.EmptyRoomDb;

public class ToTest extends AndroidTestCase {

	public void test() {

		Object[] objects = new Object[3];
		for (int i = 0; i < objects.length; i++) {

			objects[i] = "Room" + i;
		}
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		EmptyRoomDb db = new EmptyRoomDb(getContext(), "room");
	boolean l = db.insert(objects);
		Log.e("TAG", "flag--->" + l);
		list=db.query(null);
		Log.e("TAB", "list-->"+list.get(0).toString());

	}
}
