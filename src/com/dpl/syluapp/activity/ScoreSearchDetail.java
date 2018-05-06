package com.dpl.syluapp.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.model.ScoreSearchInfo;

public class ScoreSearchDetail extends AppActivity {
	private ScoreSearchInfo info;

	private TextView mTitle;
	private TextView mCredit;
	private TextView mNature;
	private TextView mGPA;
	private TextView mUsusal;
	private TextView mTerminal;
	private TextView mMidterm;
	private TextView mExperiment;
	private TextView mScore;
	private TextView mMakeupinfo;
	private TextView mPapermakeupinfo;
	private TextView mRebuildsocre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scoresearch_detail);
		
		info=(ScoreSearchInfo) getIntent().getSerializableExtra("info");
		findView();
		init();

	}

	private void findView() {

		mTitle = (TextView) findViewById(R.id.tv_scoredetail_title);
		mCredit = (TextView) findViewById(R.id.tv_scoredetail_credit);
		mNature = (TextView) findViewById(R.id.tv_scoredetail_natute);
		mGPA = (TextView) findViewById(R.id.tv_scoredetail_GPA);
		mScore = (TextView) findViewById(R.id.tv_scoredetail_score);
		mUsusal = (TextView) findViewById(R.id.tv_scoredetail_ususal);
		mMidterm = (TextView) findViewById(R.id.tv_scoredetail_midterm);
		mTerminal=(TextView)findViewById(R.id.tv_scoredetail_terminal);
		mExperiment = (TextView) findViewById(R.id.tv_scoredetail_experiment);
		mMakeupinfo = (TextView) findViewById(R.id.tv_scoredetail_makeupinfo);
		mPapermakeupinfo = (TextView) findViewById(R.id.tv_scoredetail_papermakeupinfo);
		mRebuildsocre = (TextView) findViewById(R.id.tv_scoredetail_rebuildsocre);

	}

	void init() {
		if(info!=null){
			
			mTitle.setText(info.getName());
			mCredit.setText("学分:"+info.getCredit());
			mNature.setText(info.getNature());
			mGPA.setText("绩点:"+info.getGPA());
			mScore.setText("成绩:"+info.getResult());
			mUsusal.setText("平常成绩:"+info.getUsusal());
			mMidterm.setText("期中成绩:"+info.getMidterm());
			mExperiment.setText("实验成绩:"+info.getExperiment());
			mTerminal.setText("期末成绩:"+info.getTerminal());
			mMakeupinfo.setText("补考成绩:"+info.getMakeupinfo());
			mPapermakeupinfo.setText("卷面补考成绩:"+info.getPapermakeupinfo());
			mRebuildsocre.setText("重修成绩:"+info.getRebuildsocre());
			
		}

	}

}
