package com.dpl.syluapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.net.GradePointNet;
import com.dpl.syluapp.preferences.SchoolUser;

public class GradePoint extends AppActivity {
	private TextView tv_grade;
	private GradePointNet net;
	private ProgressDialog dialog;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetConfig.SERVER_NO_RESPONES:
				dialog.dismiss();
				Toast.makeText(GradePoint.this, "服务器无响应，请稍后再试！", 1).show();
				finish();
				break;
			case NetConfig.SUCCESS:
				dialog.dismiss();

				String obj = (String) msg.obj;
				tv_grade.setText("您当前学位课绩点为：" + paraseGrade(obj));
				break;
			case NetConfig.CONNECT_TIME_OUT:
				dialog.dismiss();

				finish();
				Toast.makeText(GradePoint.this, "连接超时", 1).show();
			case NetConfig.FAILED:
				dialog.dismiss();

				Toast.makeText(GradePoint.this, "连接异常", 1).show();
				finish();
			case NetConfig.NO_NETWORK:
				finish();
				Toast.makeText(GradePoint.this, "当前网络不可用", 1).show();
				dialog.dismiss();
			case NetConfig.REQUEST_TIME_OUT:
				finish();
				Toast.makeText(GradePoint.this, "请求连接超时，请检查网络", 1).show();
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
		setContentView(R.layout.activity_grade_point);
		setActivityTitle("绩点信息");
		dialog = new ProgressDialog(GradePoint.this);
		dialog.setTitle("连接提示");
		dialog.setMessage("正在获取绩点成绩，请稍后...");
		initView();
		initData();

	}

	private void initData() {
		dialog.show();

		net = new GradePointNet(SchoolUser.getSyllabusUser(), SchoolUser.getSyllabusPswd(),
				handler);
	}

	private void initView() {

		tv_grade = (TextView) findViewById(R.id.tv_grade);
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
