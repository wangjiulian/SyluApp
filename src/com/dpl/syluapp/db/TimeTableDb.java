package com.dpl.syluapp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimeTableDb {
	private TimeTableDbOpenHelper helper;
	private Context context;
	private SQLiteDatabase database;

	public TimeTableDb(Context context) {
		this.context = context;
		helper = new TimeTableDbOpenHelper(context);

	}

	public boolean addTimeTable(Object[] params) {
		boolean flag = false;
		try {

			String sql = "insert into test(tname,tteacher,tlocat,tweekbegin,tweekend,tviewbegin,tviewend) values(?,?,?,?,?,?,?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return flag;

	}

	public  final List<Map<String, String>> query(String[] selectionArgs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		String sql = "select * from test ";
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int cloums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				map = new HashMap<String, String>();
				for (int i = 0; i < cloums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);

				}
				list.add(map);

			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null)
				database.close();

		}

		return list;
	}

	public boolean deleTabel() {
		boolean flag = false;
		try {
			String sql = "drop table if exists test";
			database = helper.getWritableDatabase();
			database.execSQL(sql);

			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return flag;
	}

	public boolean upDate() {

		boolean flag = false;
		try {
			database = helper.getWritableDatabase();
			helper.onUpgrade(database, 1, 2);
			flag=true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return flag;
	}

}
