/**
 * 
 */
package com.dpl.syluapp.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.utils.LibraryParase;

/**
 * 图书续借工具类
 * 
 * @author JUST玖
 * 
 *         2015-3-13
 */
public class LibraryRenew {
	private Handler mHandler;

	public void renew(String Bbar_code, String code,
			final LibraryCallBack callBack) {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case NetConfig.SUCCESS:
					callBack.onSuccess((List<LibrarySelectInfo>) msg.obj);
					break;

				default:
					break;
				}
			}
		};
		new connect(Bbar_code, code).start();

	}

	class connect extends Thread {
		private String Bbar_code;
		private String code;

		public connect(String Bbar_code, String code) {
			this.Bbar_code = Bbar_code;
			this.code = code;
		}

		@Override
		public void run() {
			String name = LibraryUserInfo.getUser();
			String pswd = LibraryUserInfo.getPswd();
			HttpResponse httpResponse;

			String uriAPI = "http://lib.sylu.edu.cn/reader/redr_verify.php";
			HttpPost httpRequest = new HttpPost(uriAPI);

			List<NameValuePair> params1 = new ArrayList<NameValuePair>();

			params1.add(new BasicNameValuePair("number", name));
			params1.add(new BasicNameValuePair("passwd", pswd));
			params1.add(new BasicNameValuePair("returnUrl", ""));// 这是学号

			params1.add(new BasicNameValuePair("select", "cert_no"));
			try {
				HttpClient client = new DefaultHttpClient();
				httpRequest.setEntity(new UrlEncodedFormEntity(params1,
						HTTP.UTF_8));
				// 取得HTTP response
				httpResponse = client.execute(httpRequest); // 执行
				// 若状态码为200 ok

				if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常

					InputStream inputStream = httpResponse.getEntity()
							.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(inputStream, "utf-8"));
					StringBuffer sb = new StringBuffer();
					String data = "";
					while ((data = br.readLine()) != null) {
						sb.append(data);
					}
					String result = sb.toString();

					System.out.println("result--" + result);
					Document doc = Jsoup.parse(result);
					Elements ele = doc.select("font[color=red]");// 提取登录后的网页信息

					HttpResponse httpResponse2;
					HttpGet httpget2 = new HttpGet(
							"http://lib.sylu.edu.cn/reader/ajax_renew.php?bar_code="
									+ Bbar_code + "&&check=" + code);
					try {
						httpResponse2 = client.execute(httpget2);
					} catch (Exception e) {
						// TODO: handle exception
					}

					HttpResponse httpResponse1;

					HttpGet httpRequest1 = new HttpGet(
							"http://lib.sylu.edu.cn/reader/book_lst.php");

					try {

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

							Message message = mHandler.obtainMessage();
							message.what = NetConfig.SUCCESS;
							LibraryParase parase=new LibraryParase();
							message.obj = parase.parase(result1);
							mHandler.sendMessage(message);
							// renewlist = parase.parase(result1);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public interface LibraryCallBack {
		public void onSuccess(List<LibrarySelectInfo> infos);

		public void onFailed(int netCode);
	}
}
