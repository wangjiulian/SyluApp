package com.dpl.syluapp;

import java.util.ArrayList;
import java.util.List;

import com.dpl.syluapp.model.TimeTableInfo;
import com.dpl.syluapp.model.TimeTableView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class test extends Activity{
    private int colors[] = {  
            Color.rgb(0xee,0xff,0xff),  
            Color.rgb(0xf0,0x96,0x09),  
            Color.rgb(0x8c,0xbf,0x26),  
            Color.rgb(0x00,0xab,0xa9),  
            Color.rgb(0x99,0x6c,0x33),  
            Color.rgb(0x3b,0x92,0xbc),  
            Color.rgb(0xd5,0x4d,0x34),  
            Color.rgb(0xcc,0xcc,0xcc)   };  
	private List<TimeTableInfo>list=new ArrayList<TimeTableInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kebiao);
	/*	get();*/
		TimeTableView v = new TimeTableView();
		/*for (int i = 0; i < 6; i++) {
			
			//final TextView k = (TextView)findViewById(v.view[i]);
		//	k.setText("教室"+i);
		//	k.setBackgroundColor(colors[i]);
		//	k.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(test.this, k.getText().toString(), 1).show();
					
				}
			});*/
			
		
		
	}
	
	
 	List<TimeTableInfo> get(){
	
	TimeTableInfo info;
	for (int i = 0; i < 20; i++) {
		info=new TimeTableInfo();
		info.setAddress("教室"+i);
		list.add(info);
		
	}
	
	return list;
	
}

}
