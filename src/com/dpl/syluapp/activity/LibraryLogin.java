package com.dpl.syluapp.activity;

import java.io.Serializable;
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
import android.widget.Toast;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.net.LibraryNet;
import com.dpl.syluapp.net.LibraryNet.LibraryCallBack;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.widget.CustomToast;

/**
 * 图书馆登录主页面
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class LibraryLogin extends AppActivity {
	private Button back;
	private Context mContext;
	private LibraryNet mLibraryNet;
	// private LibraryDown libraryDown;
	private EditText mUserEditText;
	private EditText mPswdEditText;
	private ProgressDialog mProgressDialog;
	private String mUserName;
	private String mUserPswd;
	private Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_librarylogin_main);
		setActivityTitle("图书借阅");
		mContext = this;
		mLibraryNet = new LibraryNet();
		mUserName = LibraryUserInfo.getUser();
		mUserPswd = LibraryUserInfo.getPswd();
		findView();
	}

	/**
	 * 初始化view
	 */
	void findView() {

		mUserEditText = (EditText) findViewById(R.id.et_librarylogin_name);
		mPswdEditText = (EditText) findViewById(R.id.et_librarylogin_pswd);
		login = (Button) findViewById(R.id.btn_librarylogin_login);
		if (!"".equals(mUserName) && !"".equals(mUserPswd)) {
			
			connectNet(mUserName, mUserPswd);
			mUserEditText.setVisibility(View.GONE);
			mPswdEditText.setVisibility(View.GONE);
			login.setVisibility(View.GONE);
		} else {
			
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!mUserEditText.getText().toString().equals("")
							&& !mPswdEditText.getText().toString().equals("")) {
						connectNet(mUserEditText.getText().toString(),
								mPswdEditText.getText().toString());

					} else if (mUserEditText.getText().toString().equals("")
							&& mPswdEditText.getText().toString().equals(""))
						Toast.makeText(LibraryLogin.this, "用户名,密码不能为空！", 1)
								.show();
					else if (mUserEditText.getText().toString().equals(""))
						Toast.makeText(LibraryLogin.this, "用户名不能为空！", 1).show();
					else if (mPswdEditText.getText().toString().equals(""))

						Toast.makeText(LibraryLogin.this, "密码不能为空！", 1).show();

				}

			});

		}

	}

	private void connectNet(String mUser, String mPswd) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("登录提示");
		mProgressDialog.setMessage("正在连接图书馆，请稍后....");
		mProgressDialog.show();
		mLibraryNet.login(mContext, mUser, mPswd, new LibraryCallBack() {

			@Override
			public void onSuccess(Bundle bundle) {
				String bbName = bundle.getString("name");
				String bbPswd = bundle.getString("pswd");
				LibraryUserInfo.setUser(bbName);
				LibraryUserInfo.setPswd(bbPswd);
				List<LibrarySelectInfo> list = (List<LibrarySelectInfo>) bundle
						.getSerializable("list");
				for (LibrarySelectInfo b : list) {
					System.out.println("login list!" + b.toString());

				}
				Intent intent = new Intent(LibraryLogin.this,
						LibraryDetail.class);
				intent.putExtra("list", (Serializable) list);
				startActivity(intent);
				finish();

			}

			@Override
			public void onFailed(int netCode) {
				switch (netCode) {
				case NetConfig.CONNECT_TIME_OUT:
					CustomToast.showShortText(mContext, "连接超时");
					mProgressDialog.dismiss();
					break;
				case NetConfig.FAILED:
					CustomToast.showShortText(mContext, "用户名或密码错误");
					mProgressDialog.dismiss();
					break;

				case NetConfig.SERVER_NO_RESPONES:
					mProgressDialog.dismiss();
					CustomToast.showShortText(mContext, "服务器无响应");
					break;

				case NetConfig.NO_NETWORK:
					mProgressDialog.dismiss();

					break;
				case NetConfig.REQUEST_TIME_OUT:
					CustomToast.showShortText(mContext, "请求连接超时，请检查网络");
					mProgressDialog.dismiss();
					
					break;
				}
			}
		});

	}
}
