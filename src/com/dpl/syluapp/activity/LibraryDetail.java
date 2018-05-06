package com.dpl.syluapp.activity;

import java.util.List;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.LibraryItemAdapter;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.preferences.SchoolUser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.*;

/**
 * 图书馆信息主页
 * 
 * @author JUST玖
 * 
 *         2015-3-13
 */
public class LibraryDetail extends AppActivity implements View.OnClickListener {

	private Context context;
	private Button back;
	private List<LibrarySelectInfo> listInfo;
	private LibraryItemAdapter adapter;
	private Button librarydetail_logoff;
	private ListView library_listinfo;
	private ImageView librarydetail_userhead;
	private TextView librarydetail_username;
	private TextView librarydetail_usernumber;
	private TextView librarydetail_userlevel;
	private TextView library_total;
	private TextView library_left;
	private TextView library_arrears;
	private Button brn_quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_librarydetail);
		setActivityTitle("借阅信息");
		context = this;
		listInfo = (List<LibrarySelectInfo>) getIntent().getSerializableExtra(
				"list");
		findView();
		init();
	}

	private void findView() {

		brn_quit = (Button) findViewById(R.id.brn_quit);
		back = (Button) findViewById(R.id.back);

		librarydetail_username = (TextView) findViewById(R.id.librarydetail_username);
		librarydetail_userlevel = (TextView) findViewById(R.id.librarydetail_userlevel);
		library_total = (TextView) findViewById(R.id.library_total);
		library_left = (TextView) findViewById(R.id.library_left);
		library_arrears = (TextView) findViewById(R.id.library_arrears);
		library_listinfo = (ListView) findViewById(R.id.library_listinfo);
	}

	void init() {
		brn_quit.setVisibility(View.VISIBLE);
		brn_quit.setOnClickListener(this);
		String userHead = LibraryUserInfo.getUserStyle();
//		if ("教师".equals(userHead))
//			librarydetail_userhead.setImageResource(R.drawable.teacher);
//		else if ("本科生".equals(userHead))
//			librarydetail_userhead.setImageResource(R.drawable.student);

		librarydetail_username.setText(LibraryUserInfo.getName());
		librarydetail_userlevel.setText(LibraryUserInfo.getUserStyle());
		library_total.setText(LibraryUserInfo.getAccumulateBook());
		int canBorrow = Integer.valueOf(LibraryUserInfo.getCanBorrow())
				- listInfo.size();
		library_left.setText(String.valueOf(canBorrow));
		library_arrears.setText(LibraryUserInfo.getDebtMoney());
		adapter = new LibraryItemAdapter(context, listInfo);
		library_listinfo.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.brn_quit:




			AlertDialog.Builder builder = new AlertDialog.Builder(
					LibraryDetail.this);
			builder.setTitle("注销提示");
			builder.setMessage("您确定要注销图书借阅登录信息吗");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					quit();
					finish();
					startActivity(new Intent(LibraryDetail.this,
							LibraryLogin.class));


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



			break;
		}

	}

	private void quit() {
		LibraryUserInfo.clearInfo();
		Intent intent = new Intent(LibraryDetail.this, LibraryLogin.class);
		String name = LibraryUserInfo.getUser();
		String pswd = LibraryUserInfo.getPswd();
		intent.putExtra("name", name);
		intent.putExtra("pswd", pswd);
		LibraryUserInfo.clearInfo();

		startActivity(intent);
	}
}
