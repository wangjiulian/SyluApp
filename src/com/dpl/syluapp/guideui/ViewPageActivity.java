package com.dpl.syluapp.guideui;
import com.dpl.syluapp.R;
import com.dpl.syluapp.guideui.ViewLayoutA;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewPageActivity extends Activity {
    private VflipperLayout viewfilpper;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
         LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.nullmain, null);  
         viewfilpper=new VflipperLayout(this);   
         setContentView(parent);  
         parent.addView(viewfilpper, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
         
    }
}