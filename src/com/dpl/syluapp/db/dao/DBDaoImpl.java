package com.dpl.syluapp.db.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDaoImpl implements DBDao {
	

    public SQLiteOpenHelper helper;
    private SQLiteDatabase database;
	public DBDaoImpl( SQLiteOpenHelper helper) {
		this.helper = helper;
		database=helper.getWritableDatabase();
	}

	@Override
	public boolean query() {
		return false;
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
	public boolean insert( Object[] params) {
		String sql=null;
		try {
			database.execSQL(sql, params);
			return true;
		} catch (Exception e) {
		}
		   
		
		
		return false;
	}

}
