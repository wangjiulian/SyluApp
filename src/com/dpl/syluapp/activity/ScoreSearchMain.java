package com.dpl.syluapp.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.ScoreSearchAdapter;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.db.TimeTableDbOpenHelper;
import com.dpl.syluapp.model.ScoreSearchInfo;
import com.dpl.syluapp.net.GradePointNet;
import com.dpl.syluapp.preferences.SchoolUser;

public class ScoreSearchMain extends AppActivity {

	private Button brn_quit;
	private Button btn_grade;
	private Button back;
	private TextView tv_scoremain_name;
	private TextView tv_scoremain_num;

	private TextView tv_scoremain_failnum;

	private TextView tv_socremain_nofail;
	private TextView tv_scoremain_testnum;
	private RelativeLayout rlyt_scoremain_faildetail;
	private RelativeLayout rlyt_scoremain_failtlyt;
	private RelativeLayout rlyt_scoremain_successtlyt;
	private ListView lv_scoremain_faidlist;
	private ListView lv_scoremain_commonlist;
	private List<ScoreSearchInfo> commonInfos;
	private List<ScoreSearchInfo> failScoreInfos;
	private ScoreSearchAdapter commonAdapter;
	private ScoreSearchAdapter failAdapter;
	private Context mContext;
	private ProgressDialog dialog;
	private GradePointNet net;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetConfig.SERVER_NO_RESPONES:
				dialog.dismiss();
				Toast.makeText(ScoreSearchMain.this, "服务器无响应，请稍后再试！", 1).show();
				finish();
				break;
			case NetConfig.SUCCESS:
				dialog.dismiss();

				String obj = (String) msg.obj;
				btn_grade.setText("您当前学位课绩点为：" + paraseGrade(obj));
				break;
			case NetConfig.CONNECT_TIME_OUT:
				dialog.dismiss();

				finish();
				Toast.makeText(ScoreSearchMain.this, "连接超时", 1).show();
			case NetConfig.FAILED:
				dialog.dismiss();

				Toast.makeText(ScoreSearchMain.this, "连接异常", 1).show();
				finish();
			case NetConfig.NO_NETWORK:
				finish();
				Toast.makeText(ScoreSearchMain.this, "当前网络不可用", 1).show();
				dialog.dismiss();
			case NetConfig.REQUEST_TIME_OUT:
				finish();
				Toast.makeText(ScoreSearchMain.this, "请求连接超时，请检查网络", 1).show();
				dialog.dismiss();

				break;
			case NetConfig.NETWORK_ALIEVE:
				net.start();
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoremain);
		setActivityTitle("成绩信息");
		dialog = new ProgressDialog(ScoreSearchMain.this);
		dialog.setTitle("连接提示");
		dialog.setMessage("正在获取绩点成绩，请稍后...");
		mContext = this;

		commonInfos = (List<ScoreSearchInfo>) getIntent().getSerializableExtra(
				"common");
		failScoreInfos = (List<ScoreSearchInfo>) getIntent()
				.getSerializableExtra("fail");

		initView();
		bindData();
		initEvent();

	}

	private void initEvent() {

		brn_quit.setVisibility(View.VISIBLE);
		brn_quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ScoreSearchMain.this);
				builder.setTitle("注销提示");
				builder.setMessage("您确定要注销成绩查询登录信息吗");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SchoolUser.clearScoreUser();
								finish();
								startActivity(new Intent(ScoreSearchMain.this,
										ScoreSearchLogin.class));
								

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				builder.show();

			}
		});
		btn_grade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!"".equals(SchoolUser.getSyllabusUser())
						&& !"".equals(SchoolUser.getSyllabusPswd())) {
					dialog.show();
					net = new GradePointNet(SchoolUser.getSyllabusUser(), SchoolUser.getSyllabusPswd(),
							handler);

				}

			}
		});

	};

	private void bindData() {

		SharedPreferences preferences = getSharedPreferences("scoreUser",
				Context.MODE_PRIVATE);
		tv_scoremain_name.setText(preferences.getString("name", ""));
		tv_scoremain_num.setText(preferences.getString("num", ""));
		commonAdapter = new ScoreSearchAdapter(commonInfos, mContext);
		commonAdapter.setFaideInfo(failScoreInfos);
		failAdapter = new ScoreSearchAdapter(failScoreInfos, mContext);
		tv_scoremain_testnum.setText("" + commonInfos.size());
		if (failScoreInfos.size() == 0) {
			rlyt_scoremain_faildetail.setVisibility(View.GONE);
			rlyt_scoremain_failtlyt.setVisibility(View.GONE);
			rlyt_scoremain_successtlyt.setVisibility(View.VISIBLE);

		}
		rlyt_scoremain_faildetail.setVisibility(View.GONE);
		tv_scoremain_failnum.setText("" + failScoreInfos.size());
		lv_scoremain_commonlist.setAdapter(commonAdapter);
		lv_scoremain_faidlist.setAdapter(failAdapter);
	}

	void initView() {
		brn_quit = (Button) findViewById(R.id.brn_quit);
		btn_grade = (Button) findViewById(R.id.btn_grade);
		tv_scoremain_name = (TextView) findViewById(R.id.tv_scoremain_name);
		tv_scoremain_num = (TextView) findViewById(R.id.tv_scoremain_num);
		tv_scoremain_testnum = (TextView) findViewById(R.id.tv_scoremain_testnum);

		tv_scoremain_failnum = (TextView) findViewById(R.id.tv_scoremain_failnum);
		tv_socremain_nofail = (TextView) findViewById(R.id.tv_socremain_nofail);
		rlyt_scoremain_faildetail = (RelativeLayout) findViewById(R.id.rlyt_scoremain_faildetail);
		rlyt_scoremain_failtlyt = (RelativeLayout) findViewById(R.id.rlyt_scoremain_failtlyt);
		rlyt_scoremain_successtlyt = (RelativeLayout) findViewById(R.id.rlyt_scoremain_successtlyt);
		lv_scoremain_faidlist = (ListView) findViewById(R.id.lv_scoremain_faidlist);
		lv_scoremain_commonlist = (ListView) findViewById(R.id.lv_scoremain_commonlist);

	}
	public String paraseGrade(String str) {
		try {
			JSONObject object = new JSONObject(str);
			String result = object.getString("StringArray");
			String ss = result.substring(2, 3);

			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}