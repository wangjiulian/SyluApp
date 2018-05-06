package com.dpl.syluapp.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.LibrarySelectAdapter;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;

public class HideLibraryMain extends AppActivity {
	private List<LibrarySelectInfo> listInfo;
	private ListView listView;
	private LibrarySelectAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		listInfo = (List<LibrarySelectInfo>) getIntent().getSerializableExtra(
				"list");
		findView();
	}

	void findView() {
		if (listInfo.isEmpty()) {
			System.out.println("书籍为空！");
			setContentView(R.layout.libraryselect_null);
			findViewById(R.id.tv_librarynull_quit).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									HideLibraryMain.this);
							builder.setTitle("注销提醒");
							builder.setMessage("你确认要注销吗？");
							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LibraryUserInfo.clearInfo();
											Intent intent = new Intent(
													HideLibraryMain.this,
													LibraryLogin.class);
											intent.putExtra("name", "");
											intent.putExtra("pswd", "");

											startActivity(intent);
											finish();

										}

									});

							builder.setNegativeButton("取消",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
							builder.show();

						}
					});

		} else {
			setContentView(R.layout.libraryselect_main);

			listView = (ListView) findViewById(R.id.lv_librarymain_list);

			adapter = new LibrarySelectAdapter(HideLibraryMain.this);
			adapter.setList(listInfo);
			listView.setAdapter(adapter);
			//

			findViewById(R.id.librartLogout).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									HideLibraryMain.this);
							builder.setTitle("注销提醒");
							builder.setMessage("你确认要注销吗？");
							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LibraryUserInfo.clearInfo();
											Intent intent = new Intent(
													HideLibraryMain.this,
													LibraryLogin.class);
											intent.putExtra("name", "");
											intent.putExtra("pswd", "");

											startActivity(intent);
											finish();

										}

									});

							builder.setNegativeButton("取消",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
							builder.show();

						}
					});
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:

			// 在action bar点击app icon; 回到 home

			finish();

			return true;

		default:

			return super.onOptionsItemSelected(item);

		}

	}
}
