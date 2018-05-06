package com.dpl.syluapp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.db.TimeTableDb;
import com.dpl.syluapp.db.TimeTableDbOpenHelper;
import com.dpl.syluapp.model.TimeTableName;
import com.dpl.syluapp.model.TimeTableView;
import com.dpl.syluapp.utils.GetWeekNum;
import com.dpl.syluapp.utils.TableColors;
/**
 * 课程格子主页面
 * @author JUST玖
 *
 * 2015-3-13
 */
public class SyllabusMain extends AppActivity {
	private Button brn_quit;
	private boolean select=false;
	private SharedPreferences mPreferences;
	private TimeTableView tableView;
	private TimeTableName tableName;
	private boolean color_flag;
	private int colorNum;
	private List<TimeTableName> tableInfos;
	private  TextView classInfo;
	private HashMap<String, String> map;
	private int weekNow;
	private int netWeek;
	private Spinner weekselect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kebiao);
		mPreferences = getSharedPreferences("week",
				Context.MODE_PRIVATE);
		netWeek=mPreferences.getInt("week", 1);
		
		Log.e("TAG","netWeek--"+netWeek);
		colorNum = 1;
		color_flag = false;
		tableInfos = new ArrayList<TimeTableName>();
		brn_quit=(Button)findViewById(R.id.brn_quit);
		brn_quit.setVisibility(View.VISIBLE);
		brn_quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SyllabusMain.this);
				builder.setTitle("注销提示");
				builder.setMessage("您确定要注销成绩查询登录信息吗");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								TimeTableDbOpenHelper dbOpenHelper = new TimeTableDbOpenHelper(
										SyllabusMain.this);
								dbOpenHelper.deleteDatabase(SyllabusMain.this);
								finish();
								startActivity(new Intent(SyllabusMain.this,SyllabusLogin.class));

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				
				
			
				
			}
		});
		weekselect = (Spinner) findViewById(R.id.spi_timetable_sele);
		weekselect.setVisibility(View.VISIBLE);
		 ArrayAdapter<CharSequence> searchAdapter = ArrayAdapter
	                .createFromResource(SyllabusMain.this, R.array.week,
	                        android.R.layout.simple_spinner_item);
	        searchAdapter
	                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        weekselect.setAdapter(searchAdapter);

		weekselect.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
                 
				if(!select){
					init(netWeek);
					weekselect.setSelection(netWeek-1);
					select=true;
				}
				else{
					weekNow = GetWeekNum.num(parent.getItemAtPosition(position)
							.toString());
					init(weekNow);
				}
				

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
           
			}
		});
	}

	void init(int weeknow) {
        Log.e("TAG","---->posi"+weeknow);
		final List<Map<String, String>> listTable;
		tableView = new TimeTableView();
		tableClear();
		TimeTableDb db = new TimeTableDb(SyllabusMain.this);

		listTable = db.query(null);
		refreshView(listTable, weeknow);

	}

	void tableClear() {
		
		for (int i = 0; i < tableView.view.length; i++) {
			for (int j = 0; j < tableView.view[i].length; j++) {
				TextView tv_clear = (TextView) findViewById(tableView.view[i][j]);
				tv_clear.setBackgroundColor(0x80000000);
				tv_clear.setText("");
				findViewById(tableView.view[i][j]).setClickable(false);
			}

		}
	}

	void refreshView(final List<Map<String, String>> listTable, int weeknow) {

		
		for (int i = 0; i < listTable.size(); i++) {

			map = (HashMap<String, String>) listTable.get(i);
			int weekbegin = Integer.valueOf(map.get("tweekbegin"));
			int weekend = Integer.valueOf(map.get("tweekend"));

			if (weekbegin <= weeknow && weekend >= weeknow) {

				final int flag = i;
				int numf = Integer.valueOf(map.get("tviewbegin"));
				int nums = Integer.valueOf(map.get("tviewend"));
				classInfo = (TextView) findViewById(tableView.view[numf][nums]);
				findViewById(tableView.view[numf][nums]).setClickable(true);
				if (tableInfos.isEmpty()) {

					tableName = new TimeTableName();
					tableName.setClassName(map.get("tname"));
					tableName.setColorNum(colorNum);
					tableInfos.add(tableName);
					classInfo
							.setBackgroundColor(TableColors.colors[colorNum++]);

				} else {
					color_flag = true;

					for (int j = 0; j < tableInfos.size(); j++) {
						if (tableInfos.get(j).getClassName()
								.equals(map.get("tname"))) {
							color_flag = false;
							tableName = new TimeTableName();
							tableName.setClassName(map.get("tname"));

							tableName.setColorNum(tableInfos.get(j)
									.getColorNum());
							// tableInfos.add(tableName);

							classInfo
									.setBackgroundColor(TableColors.colors[tableInfos
											.get(j).getColorNum()]);
							break;

						}

					}
					if (color_flag) {
						tableName = new TimeTableName();
						tableName.setClassName(map.get("tname"));

						tableName.setColorNum(colorNum);
						tableInfos.add(tableName);
						// classInfo.setBackgroundColor(colorNum++);
						classInfo
								.setBackgroundColor(TableColors.colors[colorNum++]);

					}

				}
				classInfo.setText(map.get("tname") + "\n" + "@"
						+ map.get("tlocat"));
                 
				classInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ("".equals(classInfo.getText().toString())) {
							Toast.makeText(SyllabusMain.this, "无数据不跳转", 1)
									.show();

						} else {
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap = (HashMap<String, String>) listTable
									.get(flag);
							Bundle bundle = new Bundle();
							bundle.putSerializable("info", hashMap);
							Intent intent = new Intent(SyllabusMain.this,
									SyllabusDetail.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}

					}
				});
			}

		}
	}

}