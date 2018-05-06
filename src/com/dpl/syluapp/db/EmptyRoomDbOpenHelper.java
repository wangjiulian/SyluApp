/**
 * 
 */
package com.dpl.syluapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author JUST玖
 * 
 *         2015-3-18
 */
public class EmptyRoomDbOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private final static String DBNAME = "room.db";
	private String tableName;
	private String TABLE_CREATE = "";

	public EmptyRoomDbOpenHelper(Context context, String name) {
		super(context, name, null, VERSION);
		tableName = name;
		TABLE_CREATE = "create table "
				+ tableName
				+ "(id integer primary key autoincrement,room1 varchar(64),room2 varchar(64),room3 varchar(64),room4 varchar(64))";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = TABLE_CREATE;
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 事务处理
		db.beginTransaction();
		String tempTableName = tableName + "_temp"; // 给表重命名
		String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
		db.execSQL(sql); // 创建临时表保存原始数据
		db.execSQL(TABLE_CREATE); // 创建新表
//		String insertSql = "insert into " + tableName + " select *,'','' from "
//				+ tempTableName;
//		db.execSQL(insertSql);
		db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
		
		db.setTransactionSuccessful();
	}

	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(tableName);
	}
}
