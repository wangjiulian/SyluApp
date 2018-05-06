package com.dpl.syluapp.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.EmptyRoomAdapter;
import com.dpl.syluapp.config.DPLString;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.EmptyRoomInfo;
import com.dpl.syluapp.net.EmptyRoomDown;
import com.dpl.syluapp.net.EmptyRoomNet;
import com.dpl.syluapp.net.EmptyRoomNet.EmptyRoomCallback;
import com.dpl.syluapp.widget.CustomToast;

/**
 * 空教室查询
 * 
 * @author JUST玖 更新 代码重构
 * @author JUST玖
 * @date 2015-3-17
 * 
 */
public class EmptyRoom extends AppActivity {
	private EmptyRoomNet mEmptyRoomNet;
	private ProgressDialog mOProgressDialog;
	private RelativeLayout mProgress;
	private List<EmptyRoomInfo> emptyRoomInfos = new ArrayList<EmptyRoomInfo>();
	private ListView mListInfo;
	// 获取当前时间
	private Calendar mCalendar = Calendar.getInstance();
	// 空教室适配器
	private EmptyRoomAdapter roomAdapter;
	private int year = mCalendar.get(Calendar.YEAR);
	private int monthOfYear = mCalendar.get(Calendar.MONTH);
	private int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
	private Button mDateChoose;
	private Spinner mNoclass;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emptyroom_main);
		mContext = this;
		mEmptyRoomNet = new EmptyRoomNet();
		findView();
		init();
	}

	/**
	 * 初始化view
	 */
	void findView() {

		mProgress = (RelativeLayout) findViewById(R.id.rlyt_emptyroom_re);

		mListInfo = (ListView) findViewById(R.id.lv_emptyroom_msg);
		mNoclass = (Spinner) findViewById(R.id.spi_emptyroom_numclass);

		mDateChoose = (Button) findViewById(R.id.btn_emptyroom_datechoose);

	}

	/**
	 * 初始化控件
	 */
	void init() {

		if (monthOfYear >= 9 && dayOfMonth >= 10)
			mDateChoose.setText(year + "-" + (monthOfYear + 1) + "-"
					+ dayOfMonth);
		else if (monthOfYear >= 9 && dayOfMonth < 10)
			mDateChoose.setText(year + "-" + (monthOfYear + 1) + "-" + "0"
					+ dayOfMonth);
		else if (monthOfYear < 9 && dayOfMonth >= 10)
			mDateChoose.setText(year + "-" + "0" + (monthOfYear + 1) + "-"
					+ dayOfMonth);
		else if (monthOfYear < 9 && dayOfMonth < 10)
			mDateChoose.setText(year + "-" + "0" + (monthOfYear + 1) + "-"
					+ "0" + dayOfMonth);
		mDateChoose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog date = new DatePickerDialog(EmptyRoom.this,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int day) {
								if (month >= 9 && day >= 10)
									mDateChoose.setText(year + "-"
											+ (month + 1) + "-" + day);
								else if (month >= 9 && day < 10)
									mDateChoose.setText(year + "-"
											+ (month + 1) + "-" + "0" + day);
								else if (month < 9 && day >= 10)
									mDateChoose.setText(year + "-" + "0"
											+ (month + 1) + "-" + day);
								else if (month < 9 && day < 10)
									mDateChoose.setText(year + "-" + "0"
											+ (month + 1) + "-" + "0" + day);
							}
						}, year, (monthOfYear), dayOfMonth);

				date.show();

			}

		});
		findViewById(R.id.btn_emptyroom_search).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mListInfo.setAdapter(null);
						mOProgressDialog = new ProgressDialog(EmptyRoom.this);
						mOProgressDialog.setTitle("空教室查询");
						mOProgressDialog.setMessage("正在查询空教室，请稍后...");
						mOProgressDialog.show();

						String date = mDateChoose.getText().toString();
						String classnum = mNoclass.getSelectedItem().toString();
						mEmptyRoomNet.downRoom(date, classnum,
								new EmptyRoomCallback() {

									@Override
									public void onSuccess(
											List<EmptyRoomInfo> roomInfos) {
										mOProgressDialog.dismiss();

										roomAdapter = new EmptyRoomAdapter(
												EmptyRoom.this);
										roomAdapter.setList(roomInfos);
										roomAdapter.notifyDataSetChanged();
										mListInfo.setAdapter(roomAdapter);
									}

									@Override
									public void onFailed(int netCode) {

										switch (netCode) {
										case NetConfig.SERVER_NO_RESPONES:
											mOProgressDialog.dismiss();
											CustomToast
													.showShortText(
															mContext,
															DPLString.SEVER_NO_RESPONSE);
											break;

										case NetConfig.FAILED:
											mOProgressDialog.dismiss();
											CustomToast
													.showShortText(
															mContext,
															DPLString.EMPTYROOM_SELECT_FAILED);
											break;
										case NetConfig.NO_DATA:
											mOProgressDialog.dismiss();
											CustomToast
													.showShortText(
															mContext,
															DPLString.EMPTYROOM_NO__DATA);
											break;

										}

									}
								});

						// emptyRoomDown = new EmptyRoomDown(date, classnum,
						// handler);
					}
				});
	}
}
