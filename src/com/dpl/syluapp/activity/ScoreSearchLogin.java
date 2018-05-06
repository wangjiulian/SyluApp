package com.dpl.syluapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.DPLString;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.FailScoreInfo;
import com.dpl.syluapp.model.ScoreSearchInfo;
import com.dpl.syluapp.net.ScoreSearchNet;
import com.dpl.syluapp.net.ScoreSearchNet.ScoreSearchCallBack;
import com.dpl.syluapp.preferences.SchoolUser;
import com.dpl.syluapp.widget.CustomToast;

public class ScoreSearchLogin extends AppActivity implements OnClickListener {
	private RelativeLayout layout;
	private ProgressDialog mProgressDialog;
	private ScoreSearchNet mScoreSearchNet;
	private static ArrayList<String> list = new ArrayList<String>();
	private Button back;
	private Button login;
	private List<ScoreSearchInfo> commonInfos;
	private EditText user;
	private Context mContext;
	private EditText pswd;
	private List<FailScoreInfo> failScoreInfos;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoresearch_login);
		setActivityTitle("成绩查询");
		mScoreSearchNet = new ScoreSearchNet();
		layout = (RelativeLayout) findViewById(R.id.layout);
		mContext = this;
		findView();
		if (!"".equals(SchoolUser.getScoreUser())&&!"".equals(SchoolUser.getScorePswd())) {
			connectNet(SchoolUser.getScoreUser(), SchoolUser.getScorePswd());
			layout.setVisibility(View.GONE);
		} else {

			mScoreSearchNet = new ScoreSearchNet();
			
			init();
		}

	}

	void findView() {
		user = (EditText) findViewById(R.id.et_scoresearch_user);
		pswd = (EditText) findViewById(R.id.et_scoresearch_pswd);
		login = (Button) findViewById(R.id.rlyt_socresearch_login);
		back = (Button) findViewById(R.id.back);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			
			finish();
			break;

		case R.id.rlyt_socresearch_login:
			connectNet(user.getText().toString(), pswd.getText().toString());

			break;
		}

	}

	void init() {
		login.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	private void connectNet(String mUser, String mPswd) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("成绩查询");
		mProgressDialog.setMessage("正在连接教学网，请稍后...");
		mProgressDialog.show();
		mScoreSearchNet.login(mUser, mPswd, new ScoreSearchCallBack() {

			@Override
			public void onSuccess(Bundle bundle) {
				commonInfos = (List<ScoreSearchInfo>) bundle
						.getSerializable("common");
				failScoreInfos = (List<FailScoreInfo>) bundle
						.getSerializable("fail");
				if (commonInfos.size() != 1) {
					if("".equals(SchoolUser.getScoreUser())||"".equals(SchoolUser.getScorePswd())){
						SchoolUser.setScoreUser(user.getText().toString());
						SchoolUser.setScorePswd(pswd.getText().toString());
					}
					

					Intent intent = new Intent(ScoreSearchLogin.this,
							ScoreSearchMain.class);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}

				else {

					Toast.makeText(ScoreSearchLogin.this,
							"暂时无法查询到您的成绩，请确认成绩公布后再查询！", 1).show();
				}

			}

			@Override
			public void onFailed(int netCode) {
				switch (netCode) {
				case NetConfig.NO_NETWORK:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext, "当前网络不可用");
					break;
				case NetConfig.FAILED:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext, DPLString.LOGIN_FAILED);
					break;
				case NetConfig.CONNECT_TIME_OUT:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext, "连接超时");
					break;

				case NetConfig.SERVER_NO_RESPONES:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext,
							DPLString.SEVER_NO_RESPONSE);
					break;
				case NetConfig.REQUEST_TIME_OUT:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext,
							"请求连接超时，请检查网络");
					break;
				}
			}
		});
	}

}
