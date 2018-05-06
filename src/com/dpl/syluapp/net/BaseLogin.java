package com.dpl.syluapp.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.DPLString;
import com.dpl.syluapp.config.DPLUrls;
import com.dpl.syluapp.config.NetConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 教学网登录类
 * 
 * @author JUST玖
 * 
 *         2015-2-4
 */
public class BaseLogin {

	public static String TAG = "BaseLogin";

	/**
	 * 
	 * @param user
	 *            用户名
	 * @param pswd
	 *            密码
	 * @param loginType
	 *            登陆类型
	 * @param handler
	 * @return 成功返回true
	 */
	public static boolean login(String user, String pswd, String loginType,
			Handler handler) {
		boolean flag = false;
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(DPLUrls.Login);
		HttpResponse Responselogin;
		List<NameValuePair> params = creareParams(user, pswd, loginType);

		try {
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			httpPost.setEntity(new UrlEncodedFormEntity(params,
					NetConfig.CHARSET_GB));
			Responselogin = client.execute(httpPost);
			if (Responselogin.getStatusLine().getStatusCode() == NetConfig.NET_STATE_CODE) {
				String loginResult = EntityUtils.toString(
						(HttpEntity) Responselogin.getEntity(),
						NetConfig.CHARSET_GB);

				Log.i("TAG", "loginResult-->" + loginResult.toString());
				Document logindoc = Jsoup.parse(loginResult);
				Elements loginele = logindoc.select("title");

				if (loginele.text().equals("登录")) {

					flag = false;
				} else {
					SharedPreferences preferences = MoApplication.getInstance().context
							.getSharedPreferences("scoreUser",
									Context.MODE_PRIVATE);

					Elements USR = logindoc.select("span[id=xhxm]");
					String userStr[] = USR.text().split(" ");
					preferences.edit().putString("num", user).commit();
					preferences
							.edit()
							.putString(
									"name",
									userStr[1].toString().substring(0,
											userStr[1].length() - 2)).commit();
					Log.i(TAG,
							userStr[1].toString().substring(0,
									userStr[1].length() - 2));
					Log.i(TAG, USR.text());
					flag = true;
				}
			}

			else {

				handler.sendEmptyMessage(NetConfig.SERVER_NO_RESPONES);
				return false;

			}
		} catch (ConnectTimeoutException e) {
			handler.sendEmptyMessage(NetConfig.CONNECT_TIME_OUT);
			e.printStackTrace();
		} catch (Exception e) {
			handler.sendEmptyMessage(NetConfig.ERROR);
			e.printStackTrace();
		}

		return flag;

	}

	/**
	 * POST请求参数
	 * 
	 * @param user
	 * @param pswd
	 * @param loginType
	 * @return
	 */
	private static List<NameValuePair> creareParams(String user, String pswd,
			String loginType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(NetConfig.LOGINTYPE, loginType));
		params.add(new BasicNameValuePair(NetConfig.PSWD, pswd));
		params.add(new BasicNameValuePair(NetConfig.USER, user));
		params.add(new BasicNameValuePair(NetConfig.VIEWSTATE,
				NetConfig.LOGIN__VIEWSTATE));
		params.add(new BasicNameValuePair(NetConfig.BUTTON, ""));
		params.add(new BasicNameValuePair(NetConfig.LBLANGUAGE, ""));
		return params;
	}
}
