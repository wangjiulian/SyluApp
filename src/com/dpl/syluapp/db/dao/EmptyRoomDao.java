package com.dpl.syluapp.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EmptyRoomDao {

	public boolean insert(Object[] params);

	public List<HashMap<String, String>> query(String[] selectionArgs);

	public boolean update();

	public boolean delete();


}
