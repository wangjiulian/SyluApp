package com.dpl.syluapp.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.DPLUrls;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.utils.LibraryParase;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 图书馆登录工具类
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class LibraryNet {

	private Context mContext;
	private Handler mHandler;

	public void login(Context mContext, String user, String pswd,
			final LibraryCallBack callBack) {
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
				case NetConfig.CONNECT_TIME_OUT:
					callBack.onFailed(NetConfig.CONNECT_TIME_OUT);
					break;

				case NetConfig.SERVER_NO_RESPONES:
					callBack.onFailed(NetConfig.SERVER_NO_RESPONES);
					break;
				case NetConfig.NO_NETWORK:
					callBack.onFailed(NetConfig.NO_NETWORK);
					break;
				case NetConfig.REQUEST_TIME_OUT:
					callBack.onFailed(NetConfig.REQUEST_TIME_OUT);
					break;
				}

			}

		};

		String netWork = MoApplication.getInstance().getNetworkType();
		if (netWork.equals("")) {
			mHandler.sendEmptyMessage(NetConfig.NO_NETWORK);
		} else {
			new connect(user, pswd).start();
		}

	}

	class connect extends Thread {
		private String mUser;
		private String mPswd;

		public connect(String mUser, String mPswd) {
			this.mUser = mUser;
			this.mPswd = mPswd;
		}

		@Override
		public void run() {

			Message message = mHandler.obtainMessage();
			HttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse;
			HttpPost httpRequest = new HttpPost(DPLUrls.LibraryLogin);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair(NetConfig.NUMBER, mUser));
			params.add(new BasicNameValuePair(NetConfig.PASSWD, mPswd));
			params.add(new BasicNameValuePair(NetConfig.RETURNURL, ""));// 这是学号
			params.add(new BasicNameValuePair(NetConfig.SELECT,
					NetConfig.CERT_NO));
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 30000);

			try {

				httpRequest.setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
				// 取得HTTP response
				httpResponse = client.execute(httpRequest); // 执行
				// 若状态码为200 ok

				if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常

					String result = EntityUtils.toString(
							httpResponse.getEntity(), "utf-8");
					Log.i("TAG", result.toString());
					Document doc = Jsoup.parse(result);
					Elements ele = doc.select("font[color=red]");// 提取登录后的网页信息

					if (ele.text().equals("对不起，密码错误，请查实！")) {
						message.what = NetConfig.FAILED;
						mHandler.sendMessage(message);
					}

					else {

						Elements userInfo = doc.select("div[id=mylib_info]");
						Log.i("TAG", userInfo.text());
						Elements detaiInfo = userInfo.select("TR");
						Log.i("TAG", "----->" + detaiInfo.get(0).text());
						Elements info1 = detaiInfo.get(0).select("TD");
						Log.i("TAG",
								"-----info1>"
										+ info1.get(1).text().split("：")[1]);
						LibraryUserInfo
								.setName(info1.get(1).text().split("：")[1]);
						Log.i("TAG",
								"-----info1>"
										+ info1.get(2).text().split("：")[1]
												.trim());
						LibraryUserInfo
								.setUser(info1.get(2).text().split("：")[1]
										.trim());
						Elements info2 = detaiInfo.get(2).select("TD");
						LibraryUserInfo.setCanBorrow(info2.get(0).text()
								.split("：")[1]);
						Log.i("TAG", "borow--"
								+ info2.get(0).text().split("：")[1]);
						Elements info3 = detaiInfo.get(3).select("TD");
						LibraryUserInfo.setUserStyle(info3.get(0).text()
								.split("：")[1]);

						String libraryTotal = info3.get(2).text().split("：")[1];

						String accumulateBook = libraryTotal.substring(0,
								libraryTotal.length() - 2);
						LibraryUserInfo.setAccumulateBook(accumulateBook);

						Elements info4 = detaiInfo.get(4).select("TD");
						Log.i("TAG",
								"-----info3>"
										+ info4.get(1).text().split("：")[1]);
						LibraryUserInfo.setDebtMoney(info4.get(1).text()
								.split("：")[1]);
						HttpResponse httpResponse1;

						HttpGet httpRequest1 = new HttpGet(
								DPLUrls.LibraryDetail);

						httpResponse1 = client.execute(httpRequest1); // 执行
						// 若状态码为200 ok
						if (httpResponse1.getStatusLine().getStatusCode() == 200) {
							// 返回值正常

							InputStream inputStream1 = httpResponse1
									.getEntity().getContent();
							BufferedReader br1 = new BufferedReader(
									new InputStreamReader(inputStream1, "utf-8"));
							StringBuffer sb1 = new StringBuffer();
							String data11 = "";
							while ((data11 = br1.readLine()) != null) {
								sb1.append(data11);
							}
							String result1 = sb1.toString();

							System.out.println("result--" + result1);

							LibraryParase parase = new LibraryParase();

							List<LibrarySelectInfo> list = parase
									.parase(result1);

							message.what = NetConfig.SUCCESS;
							Bundle bunble = new Bundle();
							bunble.putString("name", mUser);
							bunble.putString("pswd", mPswd);
							bunble.putSerializable("list", (Serializable) list);
							message.obj = bunble;
							mHandler.sendMessage(message);

						} else {

							message.what = NetConfig.SERVER_NO_RESPONES;
							mHandler.sendMessage(message);

						}

					}
				} else {

					mHandler.sendEmptyMessage(NetConfig.SERVER_NO_RESPONES);
				}
			} catch (ConnectTimeoutException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(NetConfig.CONNECT_TIME_OUT);
			} 
			
			catch(SocketTimeoutException e){
				e.printStackTrace();
				mHandler.sendEmptyMessage(NetConfig.REQUEST_TIME_OUT);
			}
			catch (Exception e) {
				mHandler.sendEmptyMessage(NetConfig.FAILED);
				e.printStackTrace();
			}

			super.run();
		}

	}

	public interface LibraryCallBack {
		public void onSuccess(Bundle bundle);

		public void onFailed(int netCode);
	}
}
