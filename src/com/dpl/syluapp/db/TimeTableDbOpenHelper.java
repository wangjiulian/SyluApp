package com.dpl.syluapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeTableDbOpenHelper extends SQLiteOpenHelper {
	static TimeTableDbOpenHelper mInstance = null;  

	private static final String DBNAME = "timetable.db";
	private static final int VERSION = 1;

	public TimeTableDbOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table test(id integer primary key autoincrement,tname varchar(64),tteacher varchar(64),tlocat varchar(64),tweekbegin varchar(64),tweekend varchar(64), tviewbegin varchar(64),tviewend varchar(64))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "create table test(id integer primary key autoincrement,tname varchar(64),tteacher varchar(64),tlocat varchar(64),tweekbegin varchar(64),tweekend varchar(64), tviewbegin varchar(64),tviewend varchar(64))";
		db.execSQL(sql);

	}
	synchronized TimeTableDbOpenHelper getInstance(Context context) {  
	    if (mInstance == null) {  
	        mInstance = new TimeTableDbOpenHelper(context);  
	    }  
	    return mInstance;  
	    }  


	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(DBNAME);
	}

}
