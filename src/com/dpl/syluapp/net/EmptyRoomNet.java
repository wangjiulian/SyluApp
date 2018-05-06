package com.dpl.syluapp.net;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.DPLString;
import com.dpl.syluapp.config.DPLUrls;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.EmptyRoomInfo;
import com.dpl.syluapp.utils.EmptyRoomDayChangeToNum;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 空教室工具类
 * 
 * @author JUST玖
 * 
 *         2015-3-17
 */
public class EmptyRoomNet {
	private static final String TAG = "EmptyRoomNet";
	private List<EmptyRoomInfo> roomInfos = new ArrayList<EmptyRoomInfo>();
	private Handler mHandler;
	private HttpClient mHttpClient;
	private HttpResponse mHttpResponseGet;
	private HttpResponse mHttpResponsePost;
	private HttpPost mHttpPost;
	private HttpGet mHttpGet;

	public void downRoom(String date, String num,
			final EmptyRoomCallback callBack) {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NetConfig.SUCCESS:
					callBack.onSuccess((List<EmptyRoomInfo>) msg.obj);
					break;
				case NetConfig.SERVER_NO_RESPONES:
					callBack.onFailed(NetConfig.SERVER_NO_RESPONES);
					break;
				case NetConfig.NO_DATA:
					callBack.onFailed(NetConfig.NO_DATA);

					break;
				case NetConfig.FAILED:
					callBack.onFailed(NetConfig.FAILED);
					break;
				}

				super.handleMessage(msg);
			}
		};

		String netWork = MoApplication.getInstance().getNetworkType();
		if (netWork.equals("")) {
			callBack.onFailed(NetConfig.NO_NETWORK);
		}

		else {
			new connect(date, num).start();
		}

	}

	class connect extends Thread {

		String date, num, numcode, dateNum;

		public connect(String date, String num) {
			this.date = date;
			this.num = num;
			if (num.equals("第1,2节"))
				numcode = "'1'|'1','0','0','0','0','0','0','0','0'";
			else if (num.equals("第3,4节"))
				numcode = "'2'|'0','3','0','0','0','0','0','0','0'";
			else if (num.equals("第5,6节"))
				numcode = "'3'|'0','0','5','0','0','0','0','0','0'";
			else if (num.equals("第7,8节"))
				numcode = "'4'|'0','0','0','7','0','0','0','0','0'";
			else if (num.equals("上午"))
				numcode = "'7'|'1','3','0','0','0','0','0','0','0'";
			else if (num.equals("下午"))
				numcode = "'8'|'0','0','5','7','0','0','0','0','0'";

		}

		@Override
		public void run() {
			// 教学网登陆
			boolean loginFlag = BaseLogin.login(NetConfig.EMPTY_USER,
					NetConfig.EMPTY_PSWD, NetConfig.EMPTY_LOGIN_TYPE, mHandler);
			if (loginFlag) {
				Log.i(TAG, "Empty Login Sueccess");
				Message msg = mHandler.obtainMessage();
				// 获取可查询日期并转换
				mHttpClient = new DefaultHttpClient();
				mHttpGet = new HttpGet(DPLUrls.EmptyRoomGet);
				mHttpGet.setHeader("Referer", DPLUrls.EmptyRoomGetReferer);// xh=xinxi
				try {
					mHttpResponseGet = mHttpClient.execute(mHttpGet);
					if (mHttpResponseGet.getStatusLine().getStatusCode() == 200) {
						String getString = EntityUtils.toString(
								mHttpResponseGet.getEntity(), "GB2312");
						Document doc = Jsoup.parse(getString);
						Elements getViewState = doc
								.select("input[name=__VIEWSTATE]");
						String view_State = getViewState.attr("value");
						Elements select1 = doc.select("select[name=kssj]");
						System.out.println("select1--->" + select1.text());
						Elements select2 = select1.select("option");
						List<EmptyRoomDayChangeToNum> listday = new ArrayList<EmptyRoomDayChangeToNum>();
						EmptyRoomDayChangeToNum getVaule;
						for (int i = 0; i < select2.size(); i++) {
							getVaule = new EmptyRoomDayChangeToNum();
							getVaule.setDay(select2.get(i).text());
							getVaule.setNum(select2.get(i).attr("value"));
							listday.add(getVaule);

						}
						for (int i = 0; i < listday.size(); i++) {

							String n = listday.get(i).dayChange(date);
							if (n != null) {
								dateNum = n;

								System.out.println("datenum--->" + dateNum);
							}

						}
						// 客服端所请求的日期合法则查询
						if (dateNum != null) {
							mHttpPost = new HttpPost(DPLUrls.EmptyRoomPost);
							mHttpPost.setHeader("Referer",
									DPLUrls.EmptyRoomPost);

							HttpResponse httpResponse3;
							List<NameValuePair> postParams = createPostParams(view_State);
							try {
								mHttpPost.setEntity(new UrlEncodedFormEntity(
										postParams, "GB2312"));
								mHttpResponsePost = mHttpClient
										.execute(mHttpPost);
								if (mHttpResponsePost.getStatusLine()
										.getStatusCode() == 200) {
									String postResult = EntityUtils.toString(
											mHttpResponsePost.getEntity(),
											"GB2312");
									Document detailist = Jsoup
											.parse(postResult);
									Elements table = detailist
											.select("table[class=datelist]");
									System.out.println("table--->"
											+ table.text());
									Elements emptyroom = table.select("tr");
									int flag = 0;
									EmptyRoomInfo info = null;
									for (int j = 1; j < emptyroom.size(); j++) {
										if (flag == 0)
											info = new EmptyRoomInfo();

										Elements room = emptyroom.get(j)
												.select("td");
										switch (flag++) {
										case 0:

											info.setRoom1(room.get(0).text());
											System.out.println("room1"
													+ flag
													+ info.getRoom1()
															.toString());

											break;
										case 1:
											info.setRoom2(room.get(0).text());
											System.out.println("room1"
													+ flag
													+ info.getRoom2()
															.toString());
											break;
										case 2:
											info.setRoom3(room.get(0).text());
											System.out.println("room1"
													+ flag
													+ info.getRoom3()
															.toString());

											roomInfos.add(info);

											flag = 0;

											break;

										}
										int out = j + 1;

										if (out == emptyroom.size())
											roomInfos.add(info);

										for (EmptyRoomInfo in : roomInfos) {
											Log.e("TAG",
													"---->"
															+ roomInfos
																	.toString());
										}

									}
									msg.what = NetConfig.SUCCESS;
									msg.obj = roomInfos;
									mHandler.sendMessage(msg);

								} else {
									msg.what = NetConfig.SERVER_NO_RESPONES;
									mHandler.sendMessage(msg);
								}

							} catch (Exception e) {
							}

						}

						else {
							msg.what = NetConfig.NO_DATA;
							mHandler.sendMessage(msg);

						}

					}

					else {
						msg.what = NetConfig.SERVER_NO_RESPONES;
						mHandler.sendMessage(msg);

					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}

		private List<NameValuePair> createPostParams(String view_State) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(NetConfig.VIEWSTATE, view_State));
			params.add(new BasicNameValuePair(
					NetConfig.EMPTYROOM__EVENTARGUMENT,
					DPLString.EMPTYROOM_EVENTARGUMENT));
			params.add(new BasicNameValuePair(NetConfig.EMPTYROOM__EVENTTARGET,
					DPLString.EMPTYROOM_EVENTTARGET));
			params.add(new BasicNameValuePair(NetConfig.Button2,
					DPLString.EMPTYROOM_SELECT));

			params.add(new BasicNameValuePair(NetConfig.DDLDSZ,
					DPLString.EMPTYROOM_DDLDSZ));
			params.add(new BasicNameValuePair(NetConfig.DDLSYXN,
					DPLString.EMPTYROOM_DDLSYXN));
			params.add(new BasicNameValuePair(NetConfig.DDLSYXQ,
					DPLString.EMPTYROOM_DDLSYXQ));
			params.add(new BasicNameValuePair(NetConfig.JSLB,
					DPLString.EMPTYROOM_JSLB));
			params.add(new BasicNameValuePair(NetConfig.JSSJ, dateNum));//
			params.add(new BasicNameValuePair(NetConfig.KSSJ, dateNum));
			params.add(new BasicNameValuePair(NetConfig.MAX_ZWS,
					DPLString.EMPTYROOM_MAX_ZWS));
			params.add(new BasicNameValuePair(NetConfig.MIN_ZWS,
					DPLString.EMPTYROOM_MIN_ZWS));
			params.add(new BasicNameValuePair(NetConfig.SJD, numcode));
			params.add(new BasicNameValuePair(NetConfig.XIAOQ,
					DPLString.EMPTYROOM_XIAOQ));
			params.add(new BasicNameValuePair(NetConfig.XN,
					DPLString.EMPTYROOM_XN));
			params.add(new BasicNameValuePair(NetConfig.XQ,
					DPLString.EMPTYROOM_XQ));
			params.add(new BasicNameValuePair(NetConfig.XQJ,
					DPLString.EMPTYROOM_XQJ));
			return params;
		}
	}

	public interface EmptyRoomCallback {

		public void onSuccess(List<EmptyRoomInfo> roomInfos);

		public void onFailed(int netCode);

	}

}
