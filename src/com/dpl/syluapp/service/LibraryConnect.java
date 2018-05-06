package com.dpl.syluapp.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.dpl.syluapp.R;
import com.dpl.syluapp.activity.HideLibraryLogin;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibrarySign;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.utils.DateFormat;
import com.dpl.syluapp.utils.LibraryParase;

public class LibraryConnect extends IntentService {
private	int notification_id = 19172439;
private	NotificationManager nm;
private	String name;
private	String pswd;

	public LibraryConnect() {
		super("dsadd");

	}

	@Override
	public void onCreate() {

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		name = LibraryUserInfo.getUser();
		pswd =LibraryUserInfo.getPswd();
		System.out.println("staert name-->" + name);
		System.out.println("staert pswd-->" + pswd);
		if (!name.equals("")) {

			HttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse;

			String uriAPI = "http://lib.sylu.edu.cn/reader/redr_verify.php";
			HttpPost httpRequest = new HttpPost(uriAPI);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("number", name));
			params.add(new BasicNameValuePair("passwd", pswd));
			params.add(new BasicNameValuePair("returnUrl", ""));// 这是学号

			params.add(new BasicNameValuePair("select", "cert_no"));

			try {

				httpRequest.setEntity(new UrlEncodedFormEntity(params,
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

							LibraryParase parase = new LibraryParase();

							List<LibrarySelectInfo> list = parase
									.parase(result1);
							String returnDay = list.get(list.size() - 1)
									.getbReturnDay();

							DateFormat con = new DateFormat();
							int flag = con.conTrast(returnDay);

							int daybefore = new LibrarySign(LibraryConnect.this)
									.getdaybeforeNote();
							System.out.println("daybefore--->" + daybefore);
							System.out.println("flag--->" + flag);
							/*
							 * Toast.makeText(LibraryConnect.this, "daybefore" +
							 * daybefore, 1).show();
							 */
							if ((0 <= flag && flag <= 3) || flag <= daybefore)// 0<=flag&&flag<=3
							// flag<=daybefore
							{
								showNotification(R.drawable.ic_launcher, null,
										"您有一本书即将到期！", null);

							}

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

	public void showNotification(int icon, String tickertext, String title,
			String content) {
		// 设置一个唯一的ID，随便设置

		// Notification管理器
		Notification notification = new Notification(icon, tickertext,
				System.currentTimeMillis());
		// System.currentTimeMillis()
		// 后面的参数分别是显示在顶部通知栏的小图标，小图标旁的文字（短暂显示，自动消失）系统当前时间（不明白这个有什么用）
		notification.defaults = Notification.DEFAULT_ALL;
		// 这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND
		// 振动为Notification.DEFAULT_VIBRATE;
		// Light为Notification.DEFAULT_LIGHTS，在我的Milestone上好像没什么反应
		// 全部为Notification.DEFAULT_ALL
		// 如果是振动或者全部，必须在AndroidManifest.xml加入振动权限

		Intent intent = new Intent();
		intent.putExtra("userName", name);
		intent.putExtra("userPswd", pswd);
		intent.setClass(LibraryConnect.this, HideLibraryLogin.class);

		PendingIntent pt = PendingIntent.getActivity(this, 0, intent, 0);
		// 点击通知后的动作，这里是转回main 这个Acticity
		// new Intent(this,LibraryConnect.class)
		notification.setLatestEventInfo(this, title, content, pt);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后图标消失
		nm.notify(notification_id, notification);

	}
}
