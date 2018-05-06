/**
 * 
 */
package com.dpl.syluapp.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.DPLUrls;
import com.dpl.syluapp.config.NetConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * 获取当前教学周
 * 
 * @author JUST玖
 * 
 *         2015-6-12
 */
public class WeekGetNet {

	public static void getWeek(final NetCallBack callBack) {
	final	Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

				switch (msg.what) {
				case NetConfig.SUCCESS:
					callBack.OnSuccess((String) msg.obj);
					break;

				case NetConfig.FAILED:
					callBack.OnFailed();
					break;
				case NetConfig.ERROR:
					callBack.OnFailed();
					break;

				}
			};
		};

		new Thread() {
			public void run() {

				Context mContext = MoApplication.getInstance().context;
				SharedPreferences mPreferences = mContext.getSharedPreferences(
						"week", Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mPreferences.edit();
				HttpClient mHttpClient = new DefaultHttpClient();
				HttpPost mHttpPost = new HttpPost(DPLUrls.WeekUrl);
				try {

					HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost); // 执行
					// 若状态码为200 ok

					if (mHttpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常

						String result = EntityUtils.toString(mHttpResponse
								.getEntity());
						System.out.println("result--" + result);
						Document doc = Jsoup.parse(result);
						Elements r = doc.select("div[id=lead]");
						Elements week = r.select("span");
						String nowWeek = week.text().toString();

						if (!"".equals(nowWeek) && nowWeek.length() != 0) {
							int W = Integer.valueOf(nowWeek);
							mEditor.putInt("week", W).commit();
							Message message = handler.obtainMessage();
							message.what = NetConfig.SUCCESS;

							message.obj = nowWeek;
							handler.sendMessage(message);

						}else {
							
							handler.sendEmptyMessage(NetConfig.ERROR);
						}

					} else {
						handler.sendEmptyMessage(NetConfig.ERROR);
					}
				} catch (Exception e) {

					handler.sendEmptyMessage(NetConfig.ERROR);
					e.printStackTrace();
				}

			};
		}.start();

	}

	public interface NetCallBack {
		public void OnSuccess(String week);

		public void OnFailed();

	}
}
