package com.dpl.syluapp.activity;

import java.io.Serializable;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;

public class HideLibraryLogin extends AppActivity {
	//private LibraryDown down;
	private EditText bName;
	private EditText bPswd;
	private ProgressDialog dialog;
	private String userName;
	private String userPswd;
	private Button login;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetConfig.FAILED:
				dialog.dismiss();

				Toast.makeText(HideLibraryLogin.this, "对不起，学号或密码错误，请查实！", 1).show();
				break;
			case NetConfig.SUCCESS:
				dialog.dismiss();

				Bundle bundle;
				bundle = (Bundle) msg.obj;
				String bbName = bundle.getString("name");
				String bbPswd = bundle.getString("pswd");
             LibraryUserInfo.setUser(bbName);
             LibraryUserInfo.setPswd(bbPswd);
				List<LibrarySelectInfo> list = (List<LibrarySelectInfo>) bundle
						.getSerializable("list");
				for (LibrarySelectInfo b : list) {
					System.out.println("login list!" + b.toString());

				}
				Intent intent = new Intent(HideLibraryLogin.this, LibraryMain.class);
				intent.putExtra("list", (Serializable) list);
				startActivity(intent);
				finish();
				break;

			case NetConfig.SDCARD_UNUSELESS:
				dialog.dismiss();
				Toast.makeText(HideLibraryLogin.this, "存储卡不可用！请检查后重试！", 1).show();

				break;

			case NetConfig.NO_NETWORK:
				dialog.dismiss();
				break;
			case NetConfig.NETWORK_ALIEVE:
			//	down.start();
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_librarylogin_main);
		userName = getIntent().getStringExtra("name");
		userPswd = getIntent().getStringExtra("pswd");
		findView();
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#1E90FF")));
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		getActionBar().setLogo(R.drawable.slogo);
	}

	void findView() {
		bName = (EditText) findViewById(R.id.et_librarylogin_name);
		bPswd = (EditText) findViewById(R.id.et_librarylogin_pswd);
		login = (Button) findViewById(R.id.btn_librarylogin_login);

		if (!userName.equals("")) {
			bName.setVisibility(View.INVISIBLE);
			bPswd.setVisibility(View.INVISIBLE);
			login.setVisibility(View.INVISIBLE);
			dialog = new ProgressDialog(HideLibraryLogin.this);
			dialog.setTitle("登录提示");
			dialog.setMessage("正在连接图书馆，请稍后....");
			dialog.show();

		//	down = new LibraryDown(userName, userPswd, handler);

		} else {
			dialog = new ProgressDialog(HideLibraryLogin.this);
			dialog.setTitle("登录提示");
			dialog.setMessage("正在连接图书馆，请稍后....");
			dialog.setCancelable(false);
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!bName.getText().toString().equals("")
							&& !bPswd.getText().toString().equals("")) {
						dialog.show();

					//	down = new LibraryDown(bName.getText().toString(),
					//			bPswd.getText().toString(), handler);

					} else if (bName.getText().toString().equals("")
							&& bPswd.getText().toString().equals(""))
						Toast.makeText(HideLibraryLogin.this, "用户名,密码不能为空！", 1)
								.show();
					else if (bName.getText().toString().equals(""))
						Toast.makeText(HideLibraryLogin.this, "用户名不能为空！", 1).show();
					else if (bPswd.getText().toString().equals(""))

						Toast.makeText(HideLibraryLogin.this, "密码不能为空！", 1).show();

				}

			});

		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:

			finish();

			return true;

		default:

			return super.onOptionsItemSelected(item);

		}

	}
}