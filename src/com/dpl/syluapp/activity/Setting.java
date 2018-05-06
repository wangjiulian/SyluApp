/**
 * 
 */
package com.dpl.syluapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.AboutUsActivity;
import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.db.TimeTableDbOpenHelper;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;

/**
 * @author JUST玖
 * 
 *         2015-6-12
 */
public class Setting extends AppActivity implements OnClickListener {
	private RelativeLayout tv_about;
	private RelativeLayout tv_delete_syllabus;
	private RelativeLayout tv_delete_schooluser;
	private RelativeLayout tv_feedback;
	private RelativeLayout tv_delete_library;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setActivityTitle("设置");
		initView();
		initEvent();
	}

	private void initEvent() {
		tv_delete_syllabus.setOnClickListener(this);
		tv_delete_schooluser.setOnClickListener(this);
		tv_about.setOnClickListener(this);
		tv_delete_library.setOnClickListener(this);
		tv_feedback.setOnClickListener(this);
	}

	private void initView() {
		tv_delete_syllabus = (RelativeLayout) findViewById(R.id.tv_delete_syllabus);
		tv_delete_schooluser = (RelativeLayout) findViewById(R.id.tv_delete_schooluser);
		tv_about = (RelativeLayout) findViewById(R.id.tv_about);
		tv_feedback= (RelativeLayout) findViewById(R.id.tv_feedback);
		tv_delete_library = (RelativeLayout) findViewById(R.id.tv_delete_library);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_delete_syllabus:
			AlertDialog.Builder alBuilder = new AlertDialog.Builder(Setting.this);
			alBuilder.setTitle("清除提示");
			alBuilder.setMessage("您确定要清楚课程表吗");
			alBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					TimeTableDbOpenHelper dbOpenHelper = new TimeTableDbOpenHelper(Setting.this);
					dbOpenHelper.deleteDatabase(Setting.this);
					Toast.makeText(Setting.this, "清除成功", 1).show();
				}
			});
			alBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alBuilder.show();
			break;

		case R.id.tv_delete_schooluser:
			// AlertDialog.Builder alBuilder1 = new AlertDialog.Builder(
			// Setting.this);
			// alBuilder1.setTitle("清除提示");
			// alBuilder1.setMessage("您确定注销教学网账号吗");
			// alBuilder1.setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			//// SchoolUser.clearSyllabusUser();
			//// Toast.makeText(Setting.this, "清除成功", 1).show();
			// }
			// });
			// alBuilder1.setNegativeButton("取消",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			//
			// }
			// });
			// alBuilder1.show();
			break;

		case R.id.tv_delete_library:
			AlertDialog.Builder alBuilder1 = new AlertDialog.Builder(Setting.this);
			alBuilder1.setTitle("清除提示");
			alBuilder1.setMessage("您确定注销图书馆账号吗");
			alBuilder1.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					LibraryUserInfo.clearInfo();
					// SchoolUser.clearSyllabusUser();
					Toast.makeText(Setting.this, "清除成功", 1).show();
				}
			});
			alBuilder1.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alBuilder1.show();
			break;

		case R.id.tv_about:
			startActivity(new Intent(Setting.this, AboutUsActivity.class));
			break;
		case R.id.tv_feedback:

			Intent intent = new Intent();
			intent.setClass(Setting.this, ConversationDetailActivity.class);
			String id = new FeedbackAgent(Setting.this).getDefaultConversation().getId();
			intent.putExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, id);
			startActivity(intent);
			break;
		}
	}

}
