package com.dpl.syluapp.activity;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.db.TimeTableDb;
import com.dpl.syluapp.db.TimeTableDbOpenHelper;
import com.dpl.syluapp.net.SyllabusNet;
import com.dpl.syluapp.net.SyllabusNet.SyllabusNetCallBack;
import com.dpl.syluapp.widget.CustomToast;

/**
 * 课表登陆
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class SyllabusLogin extends AppActivity {
	private Context mContext;
	private SyllabusNet mSyllabusNet;
	private RelativeLayout pbar_timetablelogin_pr;
	private EditText et_timetablelogin_num;
	private EditText et_timetablelogin_pswd;
	private ProgressDialog mProgressDialog;
	private Button bt_timetablelogin_sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable_login);
		setActivityTitle("课程表");
		mContext=this;
		TimeTableDb db = new TimeTableDb(SyllabusLogin.this);
		final List<Map<String, String>> listTable;
		listTable = db.query(null);
		if (listTable == null || listTable.isEmpty()) {
			mSyllabusNet = new SyllabusNet();
			findView();
		} else {

			startActivity(new Intent(mContext, SyllabusMain.class));
			finish();

		}
		// TimeTableDbOpenHelper dbOpenHelper = new TimeTableDbOpenHelper(
		// SyllabusLogin.this);
		// dbOpenHelper.deleteDatabase(SyllabusLogin.this);

	}

	void findView() {
		mProgressDialog = new ProgressDialog(SyllabusLogin.this);
		mProgressDialog.setTitle("下载提示");
		mProgressDialog.setMessage("正在导入课表，请稍后....");
		pbar_timetablelogin_pr = (RelativeLayout) findViewById(R.id.pbar_timetablelogin_pr);
		et_timetablelogin_num = (EditText) findViewById(R.id.et_timetablelogin_num);
		et_timetablelogin_pswd = (EditText) findViewById(R.id.et_timetablelogin_pswd);
		bt_timetablelogin_sb = (Button) findViewById(R.id.bt_timetablelogin_sb);
		bt_timetablelogin_sb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!et_timetablelogin_num.getText().toString().equals("")
						&& !et_timetablelogin_pswd.getText().toString()
								.equals("")) {
					mProgressDialog.show();
					connectNet(et_timetablelogin_num.getText().toString(),
							et_timetablelogin_pswd.getText().toString());

				} else if (et_timetablelogin_num.getText().toString()
						.equals("")
						&& et_timetablelogin_pswd.getText().toString()
								.equals(""))
					Toast.makeText(SyllabusLogin.this, "用户名,密码不能为空！", 1).show();
				else if (et_timetablelogin_num.getText().toString().equals(""))
					Toast.makeText(SyllabusLogin.this, "用户名不能为空！", 1).show();
				else if (et_timetablelogin_pswd.getText().toString().equals(""))

					Toast.makeText(SyllabusLogin.this, "密码不能为空！", 1).show();

			}

		});

	}

	/**
	 * 
	 * @param user
	 *            学号
	 * @param pswd
	 *            密码
	 */
	private void connectNet(String user, String pswd) {
		mSyllabusNet.login(mContext, user, pswd, new SyllabusNetCallBack() {

			@Override
			public void onSuceess() {
				startActivity(new Intent(mContext, SyllabusMain.class));
				finish();
			}

			@Override
			public void onFailed(int netCode) {

				switch (netCode) {
				case NetConfig.FAILED:

					CustomToast.showShortText(mContext, "登录失败");
					mProgressDialog.dismiss();
					break;

				case NetConfig.NO_NETWORK:
					mProgressDialog.dismiss();

					break;

				case NetConfig.SDCARD_UNUSELESS:
					CustomToast.showShortText(mContext, "存储卡不可用");
					mProgressDialog.dismiss();
					break;
				case NetConfig.NO_DATA:
					CustomToast.showShortText(mContext,
							"亲，不要闹，麻溜去教学网评价！或者，你是大四党！");
					mProgressDialog.dismiss();
					break;
				case NetConfig.SERVER_NO_RESPONES:
					CustomToast.showShortText(mContext, "服务器无响应");
					mProgressDialog.dismiss();
					break;
				case NetConfig.CONNECT_TIME_OUT:
					CustomToast.showShortText(mContext, "连接超时");
					mProgressDialog.dismiss();
					break;
				case NetConfig.REQUEST_TIME_OUT:
					CustomToast.showShortText(mContext, "请求超时，请检查网络");
					mProgressDialog.dismiss();
					break;
				case NetConfig.ERROR:
					CustomToast.showShortText(mContext, "连接异常");
					mProgressDialog.dismiss();
					break;
				}

			}
		});

	}
}
