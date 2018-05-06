package com.dpl.syluapp.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.DPLUrls;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.ScoreSearchInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 成绩查询工具类
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class ScoreSearchNet {

	private static String TAG = "ScoreSearchNet";

	private Handler mHandler;

	public void login(String mUser, String mPswd,
			final ScoreSearchCallBack callBack) {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NetConfig.SUCCESS:

					Bundle bundle = (Bundle) msg.obj;
					callBack.onSuccess(bundle);

					break;

				case NetConfig.FAILED:
					callBack.onFailed(NetConfig.FAILED);
					break;

				case NetConfig.SERVER_NO_RESPONES:
					callBack.onFailed(NetConfig.SERVER_NO_RESPONES);
					break;
				case NetConfig.CONNECT_TIME_OUT:
					callBack.onFailed(NetConfig.CONNECT_TIME_OUT);
					break;
				case NetConfig.ERROR:
					callBack.onFailed(NetConfig.ERROR);
					break;
				case NetConfig.REQUEST_TIME_OUT:
					callBack.onFailed(NetConfig.REQUEST_TIME_OUT);
					break;
				}

			}
		};

		String netWork = MoApplication.getInstance().getNetworkType();
		if (netWork.equals("")) {
			callBack.onFailed(NetConfig.NO_NETWORK);
		} else {

			new connect(mUser, mPswd).start();

		}

	}

	class connect extends Thread {
		String mUser;
		String mPswd;

		public connect(String mUser, String mPswd) {
			this.mUser = mUser;
			this.mPswd = mPswd;
		}

		@Override
		public void run() {
			Message msg = mHandler.obtainMessage();
			boolean login = BaseLogin.login(mUser, mPswd,
					NetConfig.STUDENT_LOGIN_TYPE, mHandler);

			if (!login) {
				mHandler.sendEmptyMessage(NetConfig.FAILED);

			} else {

				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(DPLUrls.ScoreSearchDetail + mUser);
				httpGet.setHeader("Referer", DPLUrls.ScoreSearchMain + mUser);
				HttpResponse httpGetResponse;

				try {
					httpGetResponse = client.execute(httpGet);
					if (httpGetResponse.getStatusLine().getStatusCode() == 200) {
						String getResult = EntityUtils.toString(httpGetResponse
								.getEntity());

						Document document = Jsoup.parse(getResult);
						String __VIEWSTATE = document
								.select("input[name=__VIEWSTATE]").get(0)
								.attr("value");
						Elements Course = document
								.select("table[class=datelist]");
						Elements CourseTr = Course.select("tr");
						List<ScoreSearchInfo> failsInfos = new ArrayList<ScoreSearchInfo>();
						ScoreSearchInfo failScoreInfo;
						for (int i = 1; i < CourseTr.size(); i++) {
							CourseTr.get(i).select("td").get(1).text();
							failScoreInfo = new ScoreSearchInfo();
							failScoreInfo.setName(CourseTr.get(i).select("td")
									.get(1).text());
							failScoreInfo.setResult(CourseTr.get(i)
									.select("td").get(4).text());

							failsInfos.add(failScoreInfo);
						}

						Log.i("TAG", "fali--->" + failsInfos.size());

						HttpPost httpPost = new HttpPost(
								DPLUrls.ScoreSearchDetail + mUser);
						HttpResponse httpPostResponse;
						httpPost.setHeader("Referer", DPLUrls.ScoreSearchDetail
								+ mUser);
						List<NameValuePair> params = createPostParams(__VIEWSTATE);
						httpPost.setEntity(new UrlEncodedFormEntity(params,
								"GB2312"));
						httpPostResponse = client.execute(httpPost);
						if (httpPostResponse.getStatusLine().getStatusCode() == 200) {
							String scoreInfo = EntityUtils
									.toString(httpPostResponse.getEntity());

							Document document2 = Jsoup.parse(scoreInfo);
							Elements elements = document2
									.select("table[class=datelist]");

							Log.i(TAG, "---" + elements.text());
							Elements elee = elements.select("tr");
							List<ScoreSearchInfo> infos = new ArrayList<ScoreSearchInfo>();
							ScoreSearchInfo info;
							for (int i = 1; i < elee.size(); i++) {
								info = new ScoreSearchInfo();
								Log.i("TAG", "---Result"
										+ elee.get(i).text().toString());

								Elements result = elee.get(i).select("td");
								info.setPapermakeupinfo(result.get(15).text());
								info.setMakeupinfo(result.get(14).text());
								info.setRebuildsocreFlag(result.get(19).text());
								// 长度为2 有补考标记
								Log.i(TAG, result.get(3).text() + "---长度"
										+ info.getMakeupinfo().length());

								info.setRebuildsocre(result.get(19).text());
								// 长度为2 有重修标记
								Log.i(TAG, result.get(3).text() + "---重修标记长度"
										+ info.getRebuildsocreFlag().length());
								info.setName(result.get(3).text());
								info.setNature(result.get(4).text());
								info.setCredit(result.get(6).text());
								info.setGPA(result.get(7).text());
								info.setUsusal(result.get(8).text());
								info.setMidterm(result.get(9).text());
								info.setTerminal(result.get(10).text());
								info.setExperiment(result.get(11).text());
								info.setResult(result.get(12).text());
								infos.add(info);

							}

							// msg.obj = infos;
							Bundle bundle = new Bundle();
							bundle.putSerializable("fail",
									(Serializable) failsInfos);
							bundle.putSerializable("common",
									(Serializable) infos);
							msg.what = NetConfig.SUCCESS;
							msg.obj = bundle;
							mHandler.sendMessage(msg);

						} else {
							msg.what = NetConfig.SERVER_NO_RESPONES;
							mHandler.sendMessage(msg);

						}

					} else {

						msg.what = NetConfig.SERVER_NO_RESPONES;
						mHandler.sendMessage(msg);

					}

				} catch (ConnectTimeoutException e) {
					mHandler.sendEmptyMessage(NetConfig.CONNECT_TIME_OUT);
					e.printStackTrace();
				}catch (SocketTimeoutException e){
					mHandler.sendEmptyMessage(NetConfig.REQUEST_TIME_OUT);
					e.printStackTrace();
				}

				catch (Exception e) {
					mHandler.sendEmptyMessage(NetConfig.ERROR);
					e.printStackTrace();

				}

			}
		}

		private List<NameValuePair> createPostParams(String __VIEWSTATE) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
			params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			params.add(new BasicNameValuePair("__EVENTTARGET", ""));
			params.add(new BasicNameValuePair("ddl_kcxz", ""));

			params.add(new BasicNameValuePair("ddlXN", ""));
			params.add(new BasicNameValuePair("ddlXQ", ""));
			params.add(new BasicNameValuePair("hidLanguage", ""));
			params.add(new BasicNameValuePair("btn_zcj", "历年成绩"));
			return params;
		}

	}

	public interface ScoreSearchCallBack {
		public void onSuccess(Bundle bundle);

		public void onFailed(int netCode);
	}

}
