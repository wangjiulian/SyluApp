/**
 * 
 */
package com.dpl.syluapp.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.NetConfig;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 绩点计算Net
 * 
 * @author JUST玖
 * 
 *         2015-6-4
 */
public class GradePointNet extends Thread {
	private String name;
	private String pswd;
	private HttpClient client = new DefaultHttpClient();
	private Handler handler;

	public GradePointNet(String name, String pswd, Handler handler) {
		this.name = name;
		this.pswd = pswd;
		this.client = client;
		String netWork = MoApplication.getInstance().getNetworkType();
		if (netWork.equals("")) {

			handler.sendEmptyMessage(NetConfig.NO_NETWORK);
		} else {

			handler.sendEmptyMessage(NetConfig.NETWORK_ALIEVE);
		}
		this.name = name;
		this.pswd = pswd;
		this.handler = handler;
	}

	@Override
	public void run() {
		super.run();

		super.run();
		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(
				"http://byw20481.my3w.com/syluapp.svc/getvipscore/" + name
						+ "/" + pswd);
		try {
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 300000);
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				String str = EntityUtils.toString(response.getEntity());
				Message msg = handler.obtainMessage();
				msg.what = NetConfig.SUCCESS;
				msg.obj = str;
				handler.sendMessage(msg);

			} else {

				handler.sendEmptyMessage(NetConfig.SERVER_NO_RESPONES);
			}

		} catch (ConnectTimeoutException e) {
			handler.sendEmptyMessage(NetConfig.CONNECT_TIME_OUT);
			e.printStackTrace();
		}
		
		catch(SocketTimeoutException e){
			handler.sendEmptyMessage(NetConfig.REQUEST_TIME_OUT);
			e.printStackTrace();
		}
		
		catch (Exception e) {
			handler.sendEmptyMessage(NetConfig.FAILED);
			e.printStackTrace();
		}

	}
}
