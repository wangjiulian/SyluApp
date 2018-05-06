/**
 * 
 */
package com.dpl.syluapp.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import com.dpl.syluapp.widget.CustomToast;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

/**
 * 获取教学网当前周
 * @author JUST玖
 * 
 *         2015-3-13
 */
public class NowWeekService extends IntentService {
	private HttpClient mHttpClient;

	private Context mContext;
	private SharedPreferences mPreferences;
	private HttpResponse mHttpResponse;
	private HttpPost mHttpPost;
	private TextView weekBar;
	private SharedPreferences.Editor mEditor;

	public NowWeekService() {
		super("dsdds");
		System.out.println("week Service");
	}
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = MoApplication.getInstance().context;
		mPreferences = mContext.getSharedPreferences("week",
				Context.MODE_PRIVATE);
		mEditor=mPreferences.edit();
		mHttpClient = new DefaultHttpClient();
		mHttpPost = new HttpPost(DPLUrls.WeekUrl);

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			mHttpResponse = mHttpClient.execute(mHttpPost); // 执行
			// 若状态码为200 ok

			if (mHttpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常

				// InputStream inputStream =
				// mHttpResponse.getEntity().getContent();
				// BufferedReader br = new BufferedReader(new InputStreamReader(
				// inputStream, "utf-8"));
				// StringBuffer sb = new StringBuffer();
				// String data = "";
				// while ((data = br.readLine()) != null) {
				// sb.append(data);
				// }
				// String result = sb.toString();
				String result = EntityUtils.toString(mHttpResponse.getEntity());
				System.out.println("result--" + result);
				Document doc = Jsoup.parse(result);
				Elements r = doc.select("div[id=lead]");
				Elements week = r.select("span");
				String nowWeek = week.text().toString();
				//CustomToast.showLongText(mContext, "--->" + nowWeek);
				
				if(!"".equals(nowWeek)&&nowWeek.length()!=0){
					int W=Integer.valueOf(nowWeek);
					mEditor.putInt("week", W).commit();
					Log.i("TAG", "---week--->" + nowWeek);
					
					
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
