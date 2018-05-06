package com.dpl.syluapp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dpl.syluapp.db.dao.DBDao;
import com.dpl.syluapp.db.dao.EmptyRoomDao;

public class EmptyRoomDb implements EmptyRoomDao {
	private EmptyRoomDbOpenHelper helper;
	private Context mContext;
	private SQLiteDatabase database;
	private String tableName;

	public EmptyRoomDb(Context mContext, String tableName) {
		super();
		this.mContext = mContext;
		this.tableName = tableName;
		helper = new EmptyRoomDbOpenHelper(mContext, tableName);
	}

	@Override
	public boolean insert( Object[] params) {
	
		boolean flag = false;
		try {
			String sql="insert into "+tableName+"(room1,room2,room3,room4) values (?,?,?,?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return flag;
	}


	@Override
	public boolean update() {
		return false;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public List<HashMap<String, String>> query(String[] selectionArgs) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		String sql = "select * from "+tableName;
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
		} finally {
			if (database != null)
				database.close();

		}

		return list;
	}

}
