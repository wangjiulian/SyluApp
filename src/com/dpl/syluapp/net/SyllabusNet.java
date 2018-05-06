package com.dpl.syluapp.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.TimeTableInfo;
import com.dpl.syluapp.utils.TimeTableParase;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 课程表工具类
 * 
 * @author JUST玖
 * 
 *         2015-2-2
 */
public class SyllabusNet {
	private Message msg;
	File file = Environment.getExternalStorageDirectory();
	private boolean mLoginFlag;
	private Handler mHandler;

	public void login(Context context, String userName, String pswd,
			final SyllabusNetCallBack callBack) {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case NetConfig.SUCCESS:
					callBack.onSuceess();

					break;

				case NetConfig.SDCARD_UNUSELESS:

					callBack.onFailed(NetConfig.SDCARD_UNUSELESS);
					break;
				case NetConfig.CONNECT_TIME_OUT:
					callBack.onFailed(NetConfig.CONNECT_TIME_OUT);
					break;
				case NetConfig.FAILED:
					callBack.onFailed(NetConfig.FAILED);
					break;
				case NetConfig.NO_DATA:
					callBack.onFailed(NetConfig.NO_DATA);
					break;

				case NetConfig.REQUEST_TIME_OUT:
					callBack.onFailed(NetConfig.REQUEST_TIME_OUT);
					break;
				case NetConfig.SERVER_NO_RESPONES:
					callBack.onFailed(NetConfig.SERVER_NO_RESPONES);
					break;

				case NetConfig.ERROR:
					callBack.onFailed(NetConfig.ERROR);
					break;
				}
			}
		};
		msg = mHandler.obtainMessage();
		String netWork = MoApplication.getInstance().getNetworkType();
		if (netWork.equals("")) {
			callBack.onFailed(NetConfig.NO_NETWORK);
		}

		else {
			new connect(context, userName, pswd, NetConfig.STUDENT_LOGIN_TYPE)
					.start();
		}

	}

	class connect extends Thread {
		Context context;
		String user;
		String pswd;
		String loginType;

		public connect(Context context, String user, String pswd,
				String loginType) {
			this.user = user;
			this.pswd = pswd;
			this.loginType = loginType;
			this.context = context;
		}

		@Override
		public void run() {

			boolean LoginFlag = BaseLogin.login(user, pswd, loginType, null);

			if (!LoginFlag) {
				Message msg = mHandler.obtainMessage();
				msg.what = 0;
				mHandler.sendMessage(msg);
			} else {
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						30000);// 设置连接超时
				HttpConnectionParams.setSoTimeout(client.getParams(), 30000);// 设置请求超时
				StringBuffer buffer = new StringBuffer();

				buffer.append("http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/xskbcx.aspx?xh=");
				buffer.append(user);
				System.out.println("buffer" + buffer.toString());
				HttpResponse httpResponse1;
				String uriAPI1 = buffer.toString();

				HttpGet httpRequest1 = new HttpGet(uriAPI1);

				httpRequest1.setHeader("Referer", buffer.toString());

				try {
					// 发出HTTP request

					httpResponse1 = client.execute(httpRequest1); // 执行
					// 若状态码为200 ok
					if (httpResponse1.getStatusLine().getStatusCode() == NetConfig.NET_STATE_CODE) { // 返回值正常

						InputStream inputStream1 = httpResponse1.getEntity()
								.getContent();
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(inputStream1, "GB2312"));
						StringBuffer sb1 = new StringBuffer();
						String data11 = "";
						while ((data11 = br1.readLine()) != null) {
							sb1.append(data11);
						}
						String result1 = sb1.toString();
						//
						System.out.println("resulttttt--" + result1);
						// 判断sdcard卡是否可用
						if (Environment.MEDIA_MOUNTED.equals(Environment
								.getExternalStorageState())) {
							File dir = new File(file + "/syluapp");
							dir.mkdirs();
							File down = new File(dir, "sourse.txt");
							System.out.println("fiel--" + down);

							BufferedWriter output = new BufferedWriter(
									new FileWriter(down));
							output.write(result1);
							output.close();

							TimeTableParase xmlread = new TimeTableParase(
									context);
							List<TimeTableInfo> list = xmlread.reads();

							if (list.size() == 0) {
								msg.what = NetConfig.NO_DATA;
							} else {
								msg.what = NetConfig.SUCCESS;
							}

							mHandler.sendMessage(msg);

						} else

						{
							msg.what = NetConfig.SDCARD_UNUSELESS;// sdcard不可用
							mHandler.sendMessage(msg);

						}

					} else {
						msg.what = NetConfig.NET_STATE_CODE;
						mHandler.sendMessage(msg);

					}
				} catch (ConnectTimeoutException e) {
					mHandler.sendEmptyMessage(NetConfig.CONNECT_TIME_OUT);
					e.printStackTrace();
				} catch (SocketException e) {
					mHandler.sendEmptyMessage(NetConfig.REQUEST_TIME_OUT);
					e.printStackTrace();
				}

				catch (Exception e) {
					mHandler.sendEmptyMessage(NetConfig.ERROR);
					e.printStackTrace();
				}
			}

			super.run();
		}
	}

	public interface SyllabusNetCallBack {

		public void onSuceess();

		public void onFailed(int netCode);

	}
}
