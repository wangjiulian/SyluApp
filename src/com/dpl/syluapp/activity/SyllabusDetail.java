 package com.dpl.syluapp.activity;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;

public class SyllabusDetail extends AppActivity {
	private HashMap<String, String> info;
	private TextView classBname;
	private TextView classname;
	private TextView roomName;
	private TextView teacherName;
	private TextView week;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_timetable_detai);

		findView();
		Bundle bundle = getIntent().getExtras();
		info = (HashMap<String, String>) bundle.getSerializable("info");
		
		/*getSupportActionBar().setCustomView(
				LayoutInflater.from(this).inflate(R.layout.title, null));*/
		init();
	}

	void findView() {

		classBname = (TextView) findViewById(R.id.tv_detail_classBname);
		classname = (TextView) findViewById(R.id.tv_detail_classname);
		roomName = (TextView) findViewById(R.id.tv_detail_roomname);
		teacherName = (TextView) findViewById(R.id.tv_detail_teachname);
		week = (TextView) findViewById(R.id.tv_detail_week);

	}

	void init() {
		classBname.setText(info.get("tname"));
		classname.setText("名称:   " + info.get("tname"));
		roomName.setText("教室:   " + info.get("tlocat"));
		teacherName.setText("教师:   " + info.get("tteacher"));
		week.setText("周数:   " + info.get("tweekbegin") + "-"
				+ info.get("tweekend") + "周");

	}

}
