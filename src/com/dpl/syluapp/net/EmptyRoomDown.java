package com.dpl.syluapp.net;

import java.io.BufferedReader;
import java.io.File;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.model.EmptyRoomInfo;
import com.dpl.syluapp.utils.DayToNum;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class EmptyRoomDown extends Thread {
	private List<EmptyRoomInfo> roomInfo = new ArrayList<EmptyRoomInfo>();
	private String dateNum;
	private String date;
	private String num;
	private String numCode;
	private File file = Environment.getExternalStorageDirectory();
	private HttpClient client = new DefaultHttpClient();
	private Handler handler;

	public EmptyRoomDown(String date, String num, Handler handler) {
		Message msg = handler.obtainMessage();
		String netWork = MoApplication.getInstance().getNetworkType();
		System.out.println("netWork---->" + netWork);
		if (netWork.equals("")) {
			System.out.println("网络不可用！");

			msg.what = NetConfig.NO_NETWORK;
			handler.sendMessage(msg);

		} else {
			msg.what = NetConfig.NETWORK_ALIEVE;
			handler.sendMessage(msg);
		}
		this.date = date;
		this.num = num;
		this.handler = handler;

		if (num.equals("第1,2节"))
			numCode = "'1'|'1','0','0','0','0','0','0','0','0'";
		else if (num.equals("第3,4节"))
			numCode = "'2'|'0','3','0','0','0','0','0','0','0'";
		else if (num.equals("第5,6节"))
			numCode = "'3'|'0','0','5','0','0','0','0','0','0'";
		else if (num.equals("第7,8节"))
			numCode = "'4'|'0','0','0','7','0','0','0','0','0'";

	}

	public void run() {

		HttpResponse httpResponse;

		String uriAPI = "http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/default2.aspx";
		HttpPost httpRequest = new HttpPost(uriAPI);
		List<NameValuePair> params = createParams();

		try {
			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
			// 取得HTTP response
			httpResponse = client.execute(httpRequest); // 执行
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常
				//
				InputStream inputStream = httpResponse.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream, "GB2312"));
				StringBuffer sb = new StringBuffer();
				String data1 = "";
				while ((data1 = br.readLine()) != null) {
					sb.append(data1);
				}

				String result = sb.toString(); // 此时result中就是我们成绩的HTML的源代码了

				System.out.println("result--" + result);

				HttpResponse httpResponse1;
			
				HttpGet httpRequest1 = new HttpGet(
						"http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/xxjsjy.aspx?");// &gnmkdm=N120303
			
				httpRequest1
						.setHeader("Referer",
								"http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/bm_main.aspx?");// xh=xinxi

				try {
					// 发出HTTP request

					httpResponse1 = client.execute(httpRequest1); // 执行
					// 若状态码为200 ok
					if (httpResponse1.getStatusLine().getStatusCode() == 200) { // 返回值正常

						InputStream inputStream1 = httpResponse1.getEntity()
								.getContent();
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(inputStream1, "GB2312"));
						StringBuffer sb1 = new StringBuffer();
						String data11 = "";
						while ((data11 = br1.readLine()) != null) {
							sb1.append(data11);
						}
						String result1 = sb1.toString(); //
						//
						System.out.println("result1--" + result1);

						Document doc = Jsoup.parse(result1);
						Elements select1 = doc.select("select[name=kssj]");
						System.out.println("select1--->" + select1.text());
						Elements select2 = select1.select("option");
						List<DayToNum> listday = new ArrayList<DayToNum>();
						DayToNum getVaule;
						for (int i = 0; i < select2.size(); i++) {
							getVaule = new DayToNum();
							getVaule.setDay(select2.get(i).text());
							getVaule.setNum(select2.get(i).attr("value"));
							listday.add(getVaule);

						}
						for (int i = 0; i < listday.size(); i++) {

							String n = listday.get(i).dayChange(date);
							if (n != null) {
								dateNum = n;

								System.out.println("dateNum--->" + dateNum);
							}

						}
						if (dateNum != null) {
							HttpPost httpRequest3 = new HttpPost(
									"http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/xxjsjy.aspx?xh=xinxi&xm=信息学院辅导员&gnmkdm=N120303");
							httpRequest3
									.setHeader(
											"Referer",
											"http://218.25.35.27:8080/(o2v02tzfxlbfzt552umrncfz)/xxjsjy.aspx?xh=xinxi&xm=信息学院辅导员&gnmkdm=N120303");

							HttpResponse httpResponse3;
							List<NameValuePair> params1 = new ArrayList<NameValuePair>();
							params1.add(new BasicNameValuePair(
									"__VIEWSTATE",
									"dDw0ODMxMDc2Mjk7dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47aTwzPjtpPDk+O2k8MTE+O2k8MTQ+O2k8MTg+O2k8MjE+O2k8MjQ+O2k8MjY+O2k8MzM+O2k8Mzc+O2k8Mzk+O2k8NDU+O2k8NTE+O2k8NTc+Oz47bDx0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8eHFtYzt4cWRtOz4+Oz47dDxpPDI+O0A8XGU75pys6YOoOz47QDxcZTsxOz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPGpzbGI7anNsYjs+Pjs+O3Q8aTwzMj47QDxcZTvlip7lhazlrqQ75pON5Zy6O+eUteW3peWunumqjOWupDvnlLXlrZDlrp7pqozlrqQ75aSa5aqS5L2TO+i+heWvvOWKnjvnlLvlrqQ76K6h566X5py65oi/O+WBpee+juaTjemmhjvnr67nkIPlnLo75rqc5Yaw5Zy6O+i9rua7keWcujvmjpLnkIPlnLo75pmu6YCaO+WunuS5oOW3peWOgjvlrp7pqozlrqQ76KeG5ZCs5a6kO+aJi+eQg+WcujvkvZPogrLppoY75ZCM5aOw5Lyg6K+RO+e9keeQg+WcujvoiJ7lnLo754mp55CG5a6e6aqM5a6kO+a4uOazs+mmhjvor63pn7PlrqQ757695q+b55CD5Zy6O+ePjeePoOeQg+mmhjvotrPnkIPlnLo75qmE5qaE55CD5Zy6O+avveeQg+WcujtcZTs+O0A8XGU75Yqe5YWs5a6kO+aTjeWcujvnlLXlt6Xlrp7pqozlrqQ755S15a2Q5a6e6aqM5a6kO+WkmuWqkuS9kzvovoXlr7zlip4755S75a6kO+iuoeeul+acuuaIvzvlgaXnvo7mk43ppoY756+u55CD5Zy6O+a6nOWGsOWcujvova7mu5HlnLo75o6S55CD5Zy6O+aZrumAmjvlrp7kuaDlt6XljoI75a6e6aqM5a6kO+inhuWQrOWupDvmiYvnkIPlnLo75L2T6IKy6aaGO+WQjOWjsOS8oOivkTvnvZHnkIPlnLo76Iie5Zy6O+eJqeeQhuWunumqjOWupDvmuLjms7PppoY76K+t6Z+z5a6kO+e+veavm+eQg+Wcujvnj43nj6DnkIPppoY76Laz55CD5Zy6O+aphOamhOeQg+Wcujvmr73nkIPlnLo7XGU7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDIwMTQtMjAxNTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MTs+Pjs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDxueXI7eHE7Pj47Pjt0PGk8MTI1PjtAPDIwMTQtMDktMDk7MjAxNC0wOS0xMDsyMDE0LTA5LTExOzIwMTQtMDktMTI7MjAxNC0wOS0xMzsyMDE0LTA5LTE0OzIwMTQtMDktMTU7MjAxNC0wOS0xNjsyMDE0LTA5LTE3OzIwMTQtMDktMTg7MjAxNC0wOS0xOTsyMDE0LTA5LTIwOzIwMTQtMDktMjE7MjAxNC0wOS0yMjsyMDE0LTA5LTIzOzIwMTQtMDktMjQ7MjAxNC0wOS0yNTsyMDE0LTA5LTI2OzIwMTQtMDktMjc7MjAxNC0wOS0yODsyMDE0LTA5LTI5OzIwMTQtMDktMzA7MjAxNC0xMC0wMTsyMDE0LTEwLTAyOzIwMTQtMTAtMDM7MjAxNC0xMC0wNDsyMDE0LTEwLTA1OzIwMTQtMTAtMDY7MjAxNC0xMC0wNzsyMDE0LTEwLTA4OzIwMTQtMTAtMDk7MjAxNC0xMC0xMDsyMDE0LTEwLTExOzIwMTQtMTAtMTI7MjAxNC0xMC0xMzsyMDE0LTEwLTE0OzIwMTQtMTAtMTU7MjAxNC0xMC0xNjsyMDE0LTEwLTE3OzIwMTQtMTAtMTg7MjAxNC0xMC0xOTsyMDE0LTEwLTIwOzIwMTQtMTAtMjE7MjAxNC0xMC0yMjsyMDE0LTEwLTIzOzIwMTQtMTAtMjQ7MjAxNC0xMC0yNTsyMDE0LTEwLTI2OzIwMTQtMTAtMjc7MjAxNC0xMC0yODsyMDE0LTEwLTI5OzIwMTQtMTAtMzA7MjAxNC0xMC0zMTsyMDE0LTExLTAxOzIwMTQtMTEtMDI7MjAxNC0xMS0wMzsyMDE0LTExLTA0OzIwMTQtMTEtMDU7MjAxNC0xMS0wNjsyMDE0LTExLTA3OzIwMTQtMTEtMDg7MjAxNC0xMS0wOTsyMDE0LTExLTEwOzIwMTQtMTEtMTE7MjAxNC0xMS0xMjsyMDE0LTExLTEzOzIwMTQtMTEtMTQ7MjAxNC0xMS0xNTsyMDE0LTExLTE2OzIwMTQtMTEtMTc7MjAxNC0xMS0xODsyMDE0LTExLTE5OzIwMTQtMTEtMjA7MjAxNC0xMS0yMTsyMDE0LTExLTIyOzIwMTQtMTEtMjM7MjAxNC0xMS0yNDsyMDE0LTExLTI1OzIwMTQtMTEtMjY7MjAxNC0xMS0yNzsyMDE0LTExLTI4OzIwMTQtMTEtMjk7MjAxNC0xMS0zMDsyMDE0LTEyLTAxOzIwMTQtMTItMDI7MjAxNC0xMi0wMzsyMDE0LTEyLTA0OzIwMTQtMTItMDU7MjAxNC0xMi0wNjsyMDE0LTEyLTA3OzIwMTQtMTItMDg7MjAxNC0xMi0wOTsyMDE0LTEyLTEwOzIwMTQtMTItMTE7MjAxNC0xMi0xMjsyMDE0LTEyLTEzOzIwMTQtMTItMTQ7MjAxNC0xMi0xNTsyMDE0LTEyLTE2OzIwMTQtMTItMTc7MjAxNC0xMi0xODsyMDE0LTEyLTE5OzIwMTQtMTItMjA7MjAxNC0xMi0yMTsyMDE0LTEyLTIyOzIwMTQtMTItMjM7MjAxNC0xMi0yNDsyMDE0LTEyLTI1OzIwMTQtMTItMjY7MjAxNC0xMi0yNzsyMDE0LTEyLTI4OzIwMTQtMTItMjk7MjAxNC0xMi0zMDsyMDE0LTEyLTMxOzIwMTUtMDEtMDE7MjAxNS0wMS0wMjsyMDE1LTAxLTAzOzIwMTUtMDEtMDQ7MjAxNS0wMS0wNTsyMDE1LTAxLTA2OzIwMTUtMDEtMDc7MjAxNS0wMS0wODsyMDE1LTAxLTA5OzIwMTUtMDEtMTA7MjAxNS0wMS0xMTs+O0A8MjM7MzM7NDM7NTM7NjM7NzM7MTQ7MjQ7MzQ7NDQ7NTQ7NjQ7NzQ7MTU7MjU7MzU7NDU7NTU7NjU7NzU7MTY7MjY7MzY7NDY7NTY7NjY7NzY7MTc7Mjc7Mzc7NDc7NTc7Njc7Nzc7MTg7Mjg7Mzg7NDg7NTg7Njg7Nzg7MTk7Mjk7Mzk7NDk7NTk7Njk7Nzk7MTEwOzIxMDszMTA7NDEwOzUxMDs2MTA7NzEwOzExMTsyMTE7MzExOzQxMTs1MTE7NjExOzcxMTsxMTI7MjEyOzMxMjs0MTI7NTEyOzYxMjs3MTI7MTEzOzIxMzszMTM7NDEzOzUxMzs2MTM7NzEzOzExNDsyMTQ7MzE0OzQxNDs1MTQ7NjE0OzcxNDsxMTU7MjE1OzMxNTs0MTU7NTE1OzYxNTs3MTU7MTE2OzIxNjszMTY7NDE2OzUxNjs2MTY7NzE2OzExNzsyMTc7MzE3OzQxNzs1MTc7NjE3OzcxNzsxMTg7MjE4OzMxODs0MTg7NTE4OzYxODs3MTg7MTE5OzIxOTszMTk7NDE5OzUxOTs2MTk7NzE5OzEyMDsyMjA7MzIwOzQyMDs1MjA7NjIwOzcyMDs+PjtsPGk8MD47Pj47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPG55cjt4cTs+Pjs+O3Q8aTwxMjU+O0A8MjAxNC0wOS0wOTsyMDE0LTA5LTEwOzIwMTQtMDktMTE7MjAxNC0wOS0xMjsyMDE0LTA5LTEzOzIwMTQtMDktMTQ7MjAxNC0wOS0xNTsyMDE0LTA5LTE2OzIwMTQtMDktMTc7MjAxNC0wOS0xODsyMDE0LTA5LTE5OzIwMTQtMDktMjA7MjAxNC0wOS0yMTsyMDE0LTA5LTIyOzIwMTQtMDktMjM7MjAxNC0wOS0yNDsyMDE0LTA5LTI1OzIwMTQtMDktMjY7MjAxNC0wOS0yNzsyMDE0LTA5LTI4OzIwMTQtMDktMjk7MjAxNC0wOS0zMDsyMDE0LTEwLTAxOzIwMTQtMTAtMDI7MjAxNC0xMC0wMzsyMDE0LTEwLTA0OzIwMTQtMTAtMDU7MjAxNC0xMC0wNjsyMDE0LTEwLTA3OzIwMTQtMTAtMDg7MjAxNC0xMC0wOTsyMDE0LTEwLTEwOzIwMTQtMTAtMTE7MjAxNC0xMC0xMjsyMDE0LTEwLTEzOzIwMTQtMTAtMTQ7MjAxNC0xMC0xNTsyMDE0LTEwLTE2OzIwMTQtMTAtMTc7MjAxNC0xMC0xODsyMDE0LTEwLTE5OzIwMTQtMTAtMjA7MjAxNC0xMC0yMTsyMDE0LTEwLTIyOzIwMTQtMTAtMjM7MjAxNC0xMC0yNDsyMDE0LTEwLTI1OzIwMTQtMTAtMjY7MjAxNC0xMC0yNzsyMDE0LTEwLTI4OzIwMTQtMTAtMjk7MjAxNC0xMC0zMDsyMDE0LTEwLTMxOzIwMTQtMTEtMDE7MjAxNC0xMS0wMjsyMDE0LTExLTAzOzIwMTQtMTEtMDQ7MjAxNC0xMS0wNTsyMDE0LTExLTA2OzIwMTQtMTEtMDc7MjAxNC0xMS0wODsyMDE0LTExLTA5OzIwMTQtMTEtMTA7MjAxNC0xMS0xMTsyMDE0LTExLTEyOzIwMTQtMTEtMTM7MjAxNC0xMS0xNDsyMDE0LTExLTE1OzIwMTQtMTEtMTY7MjAxNC0xMS0xNzsyMDE0LTExLTE4OzIwMTQtMTEtMTk7MjAxNC0xMS0yMDsyMDE0LTExLTIxOzIwMTQtMTEtMjI7MjAxNC0xMS0yMzsyMDE0LTExLTI0OzIwMTQtMTEtMjU7MjAxNC0xMS0yNjsyMDE0LTExLTI3OzIwMTQtMTEtMjg7MjAxNC0xMS0yOTsyMDE0LTExLTMwOzIwMTQtMTItMDE7MjAxNC0xMi0wMjsyMDE0LTEyLTAzOzIwMTQtMTItMDQ7MjAxNC0xMi0wNTsyMDE0LTEyLTA2OzIwMTQtMTItMDc7MjAxNC0xMi0wODsyMDE0LTEyLTA5OzIwMTQtMTItMTA7MjAxNC0xMi0xMTsyMDE0LTEyLTEyOzIwMTQtMTItMTM7MjAxNC0xMi0xNDsyMDE0LTEyLTE1OzIwMTQtMTItMTY7MjAxNC0xMi0xNzsyMDE0LTEyLTE4OzIwMTQtMTItMTk7MjAxNC0xMi0yMDsyMDE0LTEyLTIxOzIwMTQtMTItMjI7MjAxNC0xMi0yMzsyMDE0LTEyLTI0OzIwMTQtMTItMjU7MjAxNC0xMi0yNjsyMDE0LTEyLTI3OzIwMTQtMTItMjg7MjAxNC0xMi0yOTsyMDE0LTEyLTMwOzIwMTQtMTItMzE7MjAxNS0wMS0wMTsyMDE1LTAxLTAyOzIwMTUtMDEtMDM7MjAxNS0wMS0wNDsyMDE1LTAxLTA1OzIwMTUtMDEtMDY7MjAxNS0wMS0wNzsyMDE1LTAxLTA4OzIwMTUtMDEtMDk7MjAxNS0wMS0xMDsyMDE1LTAxLTExOz47QDwyMzszMzs0Mzs1Mzs2Mzs3MzsxNDsyNDszNDs0NDs1NDs2NDs3NDsxNTsyNTszNTs0NTs1NTs2NTs3NTsxNjsyNjszNjs0Njs1Njs2Njs3NjsxNzsyNzszNzs0Nzs1Nzs2Nzs3NzsxODsyODszODs0ODs1ODs2ODs3ODsxOTsyOTszOTs0OTs1OTs2OTs3OTsxMTA7MjEwOzMxMDs0MTA7NTEwOzYxMDs3MTA7MTExOzIxMTszMTE7NDExOzUxMTs2MTE7NzExOzExMjsyMTI7MzEyOzQxMjs1MTI7NjEyOzcxMjsxMTM7MjEzOzMxMzs0MTM7NTEzOzYxMzs3MTM7MTE0OzIxNDszMTQ7NDE0OzUxNDs2MTQ7NzE0OzExNTsyMTU7MzE1OzQxNTs1MTU7NjE1OzcxNTsxMTY7MjE2OzMxNjs0MTY7NTE2OzYxNjs3MTY7MTE3OzIxNzszMTc7NDE3OzUxNzs2MTc7NzE3OzExODsyMTg7MzE4OzQxODs1MTg7NjE4OzcxODsxMTk7MjE5OzMxOTs0MTk7NTE5OzYxOTs3MTk7MTIwOzIyMDszMjA7NDIwOzUyMDs2MjA7NzIwOz4+O2w8aTwwPjs+Pjs7Pjt0PHQ8O3Q8aTwxPjtAPOS6jDs+O0A8Mjs+PjtsPGk8MD47Pj47Oz47dDx0PDt0PGk8MT47QDzljZU7PjtAPOWNlTs+Pjs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx6d3NqZDtzajs+Pjs+O3Q8aTwxMT47QDznrKwxLDLoioI756ysMyw06IqCO+esrDUsNuiKgjvnrKw3LDjoioI756ysOSwxMOiKgjvnrKwxMSwxMuiKgjvkuIrljYg75LiL5Y2IO+aZmuS4ijvnmb3lpKk75pW05aSpOz47QDwnMSd8JzEnLCcwJywnMCcsJzAnLCcwJywnMCcsJzAnLCcwJywnMCc7JzInfCcwJywnMycsJzAnLCcwJywnMCcsJzAnLCcwJywnMCcsJzAnOyczJ3wnMCcsJzAnLCc1JywnMCcsJzAnLCcwJywnMCcsJzAnLCcwJzsnNCd8JzAnLCcwJywnMCcsJzcnLCcwJywnMCcsJzAnLCcwJywnMCc7JzUnfCcwJywnMCcsJzAnLCcwJywnOScsJzAnLCcwJywnMCcsJzAnOyc2J3wnMCcsJzAnLCcwJywnMCcsJzAnLCcxMScsJzAnLCcwJywnMCc7JzcnfCcxJywnMycsJzAnLCcwJywnMCcsJzAnLCcwJywnMCcsJzAnOyc4J3wnMCcsJzAnLCc1JywnNycsJzAnLCcwJywnMCcsJzAnLCcwJzsnOSd8JzAnLCcwJywnMCcsJzAnLCc5JywnMTEnLCcwJywnMCcsJzAnOycxMCd8JzEnLCczJywnNScsJzcnLCcwJywnMCcsJzAnLCcwJywnMCc7JzExJ3wnMScsJzMnLCc1JywnNycsJzknLCcxMScsJzAnLCcwJywnMCc7Pj47Pjs7Pjt0PHA8O3A8bDxkaXNhYmxlZDs+O2w8ZGlzYWJsZWQ7Pj4+Ozs+O3Q8QDA8cDxwPGw8XyFEYXRhU291cmNlSXRlbUNvdW50O18hSXRlbUNvdW50O1BhZ2VDb3VudDtEYXRhS2V5czs+O2w8aTwtMT47aTwtMT47aTwwPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx4bjt4bjs+Pjs+O3Q8aTwxMD47QDwyMDA1LTIwMDY7MjAwNi0yMDA3OzIwMDctMjAwODsyMDA4LTIwMDk7MjAwOS0yMDEwOzIwMTAtMjAxMTsyMDExLTIwMTI7MjAxMi0yMDEzOzIwMTMtMjAxNDsyMDE0LTIwMTU7PjtAPDIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0OzIwMTQtMjAxNTs+Pjs+Ozs+O3Q8QDA8O0AwPDs7Ozs7Ozs7Ozs7Ozs7O0AwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Ozs7Oz47Oz47Ozs7Ozs7Ozs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx4bjt4bjs+Pjs+O3Q8aTwxMD47QDwyMDA1LTIwMDY7MjAwNi0yMDA3OzIwMDctMjAwODsyMDA4LTIwMDk7MjAwOS0yMDEwOzIwMTAtMjAxMTsyMDExLTIwMTI7MjAxMi0yMDEzOzIwMTMtMjAxNDsyMDE0LTIwMTU7PjtAPDIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0OzIwMTQtMjAxNTs+Pjs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47Pnll1RUH1pFK9nuE5JqRHBZJgU54"));
							params1.add(new BasicNameValuePair(
									"__EVENTARGUMENT", ""));
							params1.add(new BasicNameValuePair("__EVENTTARGET",
									""));
							// pJrlqpLknkIPlnLo75q+955CD5Zy6O1xlOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDwyMDE0LTIwMTU7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPDE7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8bnlyO3hxOz4+Oz47dDxpPDEyOT47QDwyMDE0LTA5LTA1OzIwMTQtMDktMDY7MjAxNC0wOS0wNzsyMDE0LTA5LTA4OzIwMTQtMDktMDk7MjAxNC0wOS0xMDsyMDE0LTA5LTExOzIwMTQtMDktMTI7MjAxNC0wOS0xMzsyMDE0LTA5LTE0OzIwMTQtMDktMTU7MjAxNC0wOS0xNjsyMDE0LTA5LTE3OzIwMTQtMDktMTg7MjAxNC0wOS0xOTsyMDE0LTA5LTIwOzIwMTQtMDktMjE7MjAxNC0wOS0yMjsyMDE0LTA5LTIzOzIwMTQtMDktMjQ7MjAxNC0wOS0yNTsyMDE0LTA5LTI2OzIwMTQtMDktMjc7MjAxNC0wOS0yODsyMDE0LTA5LTI5OzIwMTQtMDktMzA7MjAxNC0xMC0wMTsyMDE0LTEwLTAyOzIwMTQtMTAtMDM7MjAxNC0xMC0wNDsyMDE0LTEwLTA1OzIwMTQtMTAtMDY7MjAxNC0xMC0wNzsyMDE0LTEwLTA4OzIwMTQtMTAtMDk7MjAxNC0xMC0xMDsyMDE0LTEwLTExOzIwMTQtMTAtMTI7MjAxNC0xMC0xMzsyMDE0LTEwLTE0OzIwMTQtMTAtMTU7MjAxNC0xMC0xNjsyMDE0LTEwLTE3OzIwMTQtMTAtMTg7MjAxNC0xMC0xOTsyMDE0LTEwLTIwOzIwMTQtMTAtMjE7MjAxNC0xMC0yMjsyMDE0LTEwLTIzOzIwMTQtMTAtMjQ7MjAxNC0xMC0yNTsyMDE0LTEwLTI2OzIwMTQtMTAtMjc7MjAxNC0xMC0yODsyMDE0LTEwLTI5OzIwMTQtMTAtMzA7MjAxNC0xMC0zMTsyMDE0LTExLTAxOzIwMTQtMTEtMDI7MjAxNC0xMS0wMzsyMDE0LTExLTA0OzIwMTQtMTEtMDU7MjAxNC0xMS0wNjsyMDE0LTExLTA3OzIwMTQtMTEtMDg7MjAxNC0xMS0wOTsyMDE0LTExLTEwOzIwMTQtMTEtMTE7MjAxNC0xMS0xMjsyMDE0LTExLTEzOzIwMTQtMTEtMTQ7MjAxNC0xMS0xNTsyMDE0LTExLTE2OzIwMTQtMTEtMTc7MjAxNC0xMS0xODsyMDE0LTExLTE5OzIwMTQtMTEtMjA7MjAxNC0xMS0yMTsyMDE0LTExLTIyOzIwMTQtMTEtMjM7MjAxNC0xMS0yNDsyMDE0LTExLTI1OzIwMTQtMTEtMjY7MjAxNC0xMS0yNzsyMDE0LTExLTI4OzIwMTQtMTEtMjk7MjAxNC0xMS0zMDsyMDE0LTEyLTAxOzIwMTQtMTItMDI7MjAxNC0xMi0wMzsyMDE0LTEyLTA0OzIwMTQtMTItMDU7MjAxNC0xMi0wNjsyMDE0LTEyLTA3OzIwMTQtMTItMDg7MjAxNC0xMi0wOTsyMDE0LTEyLTEwOzIwMTQtMTItMTE7MjAxNC0xMi0xMjsyMDE0LTEyLTEzOzIwMTQtMTItMTQ7MjAxNC0xMi0xNTsyMDE0LTEyLTE2OzIwMTQtMTItMTc7MjAxNC0xMi0xODsyMDE0LTEyLTE5OzIwMTQtMTItMjA7MjAxNC0xMi0yMTsyMDE0LTEyLTIyOzIwMTQtMTItMjM7MjAxNC0xMi0yNDsyMDE0LTEyLTI1OzIwMTQtMTItMjY7MjAxNC0xMi0yNzsyMDE0LTEyLTI4OzIwMTQtMTItMjk7MjAxNC0xMi0zMDsyMDE0LTEyLTMxOzIwMTUtMDEtMDE7MjAxNS0wMS0wMjsyMDE1LTAxLTAzOzIwMTUtMDEtMDQ7MjAxNS0wMS0wNTsyMDE1LTAxLTA2OzIwMTUtMDEtMDc7MjAxNS0wMS0wODsyMDE1LTAxLTA5OzIwMTUtMDEtMTA7MjAxNS0wMS0xMTs+O0A8NTI7NjI7NzI7MTM7MjM7MzM7NDM7NTM7NjM7NzM7MTQ7MjQ7MzQ7NDQ7NTQ7NjQ7NzQ7MTU7MjU7MzU7NDU7NTU7NjU7NzU7MTY7MjY7MzY7NDY7NTY7NjY7NzY7MTc7Mjc7Mzc7NDc7NTc7Njc7Nzc7MTg7Mjg7Mzg7NDg7NTg7Njg7Nzg7MTk7Mjk7Mzk7NDk7NTk7Njk7Nzk7MTEwOzIxMDszMTA7NDEwOzUxMDs2MTA7NzEwOzExMTsyMTE7MzExOzQxMTs1MTE7NjExOzcxMTsxMTI7MjEyOzMxMjs0MTI7NTEyOzYxMjs3MTI7MTEzOzIxMzszMTM7NDEzOzUxMzs2MTM7NzEzOzExNDsyMTQ7MzE0OzQxNDs1MTQ7NjE0OzcxNDsxMTU7MjE1OzMxNTs0MTU7NTE1OzYxNTs3MTU7MTE2OzIxNjszMTY7NDE2OzUxNjs2MTY7NzE2OzExNzsyMTc7MzE3OzQxNzs1MTc7NjE3OzcxNzsxMTg7MjE4OzMxODs0MTg7NTE4OzYxODs3MTg7MTE5OzIxOTszMTk7NDE5OzUxOTs2MTk7NzE5OzEyMDsyMjA7MzIwOzQyMDs1MjA7NjIwOzcyMDs+PjtsPGk8MD47Pj47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPG55cjt4cTs+Pjs+O3Q8aTwxMjk+O0A8MjAxNC0wOS0wNTsyMDE0LTA5LTA2OzIwMTQtMDktMDc7MjAxNC0wOS0wODsyMDE0LTA5LTA5OzIwMTQtMDktMTA7MjAxNC0wOS0xMTsyMDE0LTA5LTEyOzIwMTQtMDktMTM7MjAxNC0wOS0xNDsyMDE0LTA5LTE1OzIwMTQtMDktMTY7MjAxNC0wOS0xNzsyMDE0LTA5LTE4OzIwMTQtMDktMTk7MjAxNC0wOS0yMDsyMDE0LTA5LTIxOzIwMTQtMDktMjI7MjAxNC0wOS0yMzsyMDE0LTA5LTI0OzIwMTQtMDktMjU7MjAxNC0wOS0yNjsyMDE0LTA5LTI3OzIwMTQtMDktMjg7MjAxNC0wOS0yOTsyMDE0LTA5LTMwOzIwMTQtMTAtMDE7MjAxNC0xMC0wMjsyMDE0LTEwLTAzOzIwMTQtMTAtMDQ7MjAxNC0xMC0wNTsyMDE0LTEwLTA2OzIwMTQtMTAtMDc7MjAxNC0xMC0wODsyMDE0LTEwLTA5OzIwMTQtMTAtMTA7MjAxNC0xMC0xMTsyMDE0LTEwLTEyOzIwMTQtMTAtMTM7MjAxNC0xMC0xNDsyMDE0LTEwLTE1OzIwMTQtMTAtMTY7MjAxNC0xMC0xNzsyMDE0LTEwLTE4OzIwMTQtMTAtMTk7MjAxNC0xMC0yMDsyMDE0LTEwLTIxOzIwMTQtMTAtMjI7MjAxNC0xMC0yMzsyMDE0LTEwLTI0OzIwMTQtMTAtMjU7MjAxNC0xMC0yNjsyMDE0LTEwLTI3OzIwMTQtMTAtMjg7MjAxNC0xMC0yOTsyMDE0LTEwLTMwOzIwMTQtMTAtMzE7MjAxNC0xMS0wMTsyMDE0LTExLTAyOzIwMTQtMTEtMDM7MjAxNC0xMS0wNDsyMDE0LTExLTA1OzIwMTQtMTEtMDY7MjAxNC0xMS0wNzsyMDE0LTExLTA4OzIwMTQtMTEtMDk7MjAxNC0xMS0xMDsyMDE0LTExLTExOzIwMTQtMTEtMTI7MjAxNC0xMS0xMzsyMDE0LTExLTE0OzIwMTQtMTEtMTU7MjAxNC0xMS0xNjsyMDE0LTExLTE3OzIwMTQtMTEtMTg7MjAxNC0xMS0xOTsyMDE0LTExLTIwOzIwMTQtMTEtMjE7MjAxNC0xMS0yMjsyMDE0LTExLTIzOzIwMTQtMTEtMjQ7MjAxNC0xMS0yNTsyMDE0LTExLTI2OzIwMTQtMTEtMjc7MjAxNC0xMS0yODsyMDE0LTExLTI5OzIwMTQtMTEtMzA7MjAxNC0xMi0wMTsyMDE0LTEyLTAyOzIwMTQtMTItMDM7MjAxNC0xMi0wNDsyMDE0LTEyLTA1OzIwMTQtMTItMDY7MjAxNC0xMi0wNzsyMDE0LTEyLTA4OzIwMTQtMTItMDk7MjAxNC0xMi0xMDsyMDE0LTEyLTExOzIwMTQtMTItMTI7MjAxNC0xMi0xMzsyMDE0LTEyLTE0OzIwMTQtMTItMTU7MjAxNC0xMi0xNjsyMDE0LTEyLTE3OzIwMTQtMTItMTg7MjAxNC0xMi0xOTsyMDE0LTEyLTIwOzIwMTQtMTItMjE7MjAxNC0xMi0yMjsyMDE0LTEyLTIzOzIwMTQtMTItMjQ7MjAxNC0xMi0yNTsyMDE0LTEyLTI2OzIwMTQtMTItMjc7MjAxNC0xMi0yODsyMDE0LTEyLTI5OzIwMTQtMTItMzA7MjAxNC0xMi0zMTsyMDE1LTAxLTAxOzIwMTUtMDEtMDI7MjAxNS0wMS0wMzsyMDE1LTAxLTA0OzIwMTUtMDEtMDU7MjAxNS0wMS0wNjsyMDE1LTAxLTA3OzIwMTUtMDEtMDg7MjAxNS0wMS0wOTsyMDE1LTAxLTEwOzIwMTUtMDEtMTE7PjtAPDUyOzYyOzcyOzEzOzIzOzMzOzQzOzUzOzYzOzczOzE0OzI0OzM0OzQ0OzU0OzY0Ozc0OzE1OzI1OzM1OzQ1OzU1OzY1Ozc1OzE2OzI2OzM2OzQ2OzU2OzY2Ozc2OzE3OzI3OzM3OzQ3OzU3OzY3Ozc3OzE4OzI4OzM4OzQ4OzU4OzY4Ozc4OzE5OzI5OzM5OzQ5OzU5OzY5Ozc5OzExMDsyMTA7MzEwOzQxMDs1MTA7NjEwOzcxMDsxMTE7MjExOzMxMTs0MTE7NTExOzYxMTs3MTE7MTEyOzIxMjszMTI7NDEyOzUxMjs2MTI7NzEyOzExMzsyMTM7MzEzOzQxMzs1MTM7NjEzOzcxMzsxMTQ7MjE0OzMxNDs0MTQ7NTE0OzYxNDs3MTQ7MTE1OzIxNTszMTU7NDE1OzUxNTs2MTU7NzE1OzExNjsyMTY7MzE2OzQxNjs1MTY7NjE2OzcxNjsxMTc7MjE3OzMxNzs0MTc7NTE3OzYxNzs3MTc7MTE4OzIxODszMTg7NDE4OzUxODs2MTg7NzE4OzExOTsyMTk7MzE5OzQxOTs1MTk7NjE5OzcxOTsxMjA7MjIwOzMyMDs0MjA7NTIwOzYyMDs3MjA7Pj47bDxpPDA+Oz4+Ozs+O3Q8dDw7dDxpPDE+O0A85LqUOz47QDw1Oz4+O2w8aTwwPjs+Pjs7Pjt0PHQ8O3Q8aTwxPjtAPOWPjDs+O0A85Y+MOz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPHp3c2pkO3NqOz4+Oz47dDxpPDExPjtAPOesrDEsMuiKgjvnrKwzLDToioI756ysNSw26IqCO+esrDcsOOiKgjvnrKw5LDEw6IqCO+esrDExLDEy6IqCO+S4iuWNiDvkuIvljYg75pma5LiKO+eZveWkqTvmlbTlpKk7PjtAPCcxJ3wnMScsJzAnLCcwJywnMCcsJzAnLCcwJywnMCcsJzAnLCcwJzsnMid8JzAnLCczJywnMCcsJzAnLCcwJywnMCcsJzAnLCcwJywnMCc7JzMnfCcwJywnMCcsJzUnLCcwJywnMCcsJzAnLCcwJywnMCcsJzAnOyc0J3wnMCcsJzAnLCcwJywnNycsJzAnLCcwJywnMCcsJzAnLCcwJzsnNSd8JzAnLCcwJywnMCcsJzAnLCc5JywnMCcsJzAnLCcwJywnMCc7JzYnfCcwJywnMCcsJzAnLCcwJywnMCcsJzExJywnMCcsJzAnLCcwJzsnNyd8JzEnLCczJywnMCcsJzAnLCcwJywnMCcsJzAnLCcwJywnMCc7JzgnfCcwJywnMCcsJzUnLCc3JywnMCcsJzAnLCcwJywnMCcsJzAnOyc5J3wnMCcsJzAnLCcwJywnMCcsJzknLCcxMScsJzAnLCcwJywnMCc7JzEwJ3wnMScsJzMnLCc1JywnNycsJzAnLCcwJywnMCcsJzAnLCcwJzsnMTEnfCcxJywnMycsJzUnLCc3JywnOScsJzExJywnMCcsJzAnLCcwJzs+Pjs+Ozs+O3Q8cDw7cDxsPGRpc2FibGVkOz47bDxkaXNhYmxlZDs+Pj47Oz47dDxAMDxwPHA8bDxfIURhdGFTb3VyY2VJdGVtQ291bnQ7XyFJdGVtQ291bnQ7UGFnZUNvdW50O0RhdGFLZXlzOz47bDxpPC0xPjtpPC0xPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPHhuO3huOz4+Oz47dDxpPDEwPjtAPDIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0OzIwMTQtMjAxNTs+O0A8MjAwNS0yMDA2OzIwMDYtMjAwNzsyMDA3LTIwMDg7MjAwOC0yMDA5OzIwMDktMjAxMDsyMDEwLTIwMTE7MjAxMS0yMDEyOzIwMTItMjAxMzsyMDEzLTIwMTQ7MjAxNC0yMDE1Oz4+Oz47Oz47dDxAMDw7QDA8Ozs7Ozs7Ozs7Ozs7Ozs7QDA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Ozs7Pjs7Pjs7Ozs7Ozs7Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPHhuO3huOz4+Oz47dDxpPDEwPjtAPDIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0OzIwMTQtMjAxNTs+O0A8MjAwNS0yMDA2OzIwMDYtMjAwNzsyMDA3LTIwMDg7MjAwOC0yMDA5OzIwMDktMjAxMDsyMDEwLTIwMTE7MjAxMS0yMDEyOzIwMTItMjAxMzsyMDEzLTIwMTQ7MjAxNC0yMDE1Oz4+Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjs+Pjs+OQK1/nD+qdmze15jSd8PUW8HY78="));
							params1.add(new BasicNameValuePair("Button2",
									"按时间段查询空教室"));// 这是学号

							params1.add(new BasicNameValuePair("ddlDsz", "双"));
							params1.add(new BasicNameValuePair("ddlSyXn",
									"2014-2015"));
							params1.add(new BasicNameValuePair("ddlSyxq", "1"));
							params1.add(new BasicNameValuePair("jslb", "普通"));
							params1.add(new BasicNameValuePair("jssj", dateNum));//
							params1.add(new BasicNameValuePair("kssj", dateNum));
							params1.add(new BasicNameValuePair("max_zws", ""));
							params1.add(new BasicNameValuePair("min_zws", "0"));
							params1.add(new BasicNameValuePair("sjd", numCode));
							params1.add(new BasicNameValuePair("xiaoq", ""));
							params1.add(new BasicNameValuePair("xn",
									"2014-2015"));
							params1.add(new BasicNameValuePair("xq", "1"));
							params1.add(new BasicNameValuePair("xqj", ""));
							try {
								httpRequest3
										.setEntity(new UrlEncodedFormEntity(
												params1, "GB2312"));
								httpResponse3 = client.execute(httpRequest3);
								if (httpResponse3.getStatusLine()
										.getStatusCode() == 200) {

									InputStream inputStream3 = httpResponse3
											.getEntity().getContent();
									BufferedReader br11 = new BufferedReader(
											new InputStreamReader(inputStream3,
													"GB2312"));
									StringBuffer sb11 = new StringBuffer();
									String data111 = "";
									while ((data111 = br11.readLine()) != null) {
										sb11.append(data111);
									}
									String result11 = sb11.toString(); 
									//
									System.out.println("result--" + result11);
									Document detailist = Jsoup.parse(result11);
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

											roomInfo.add(info);

											flag = 0;

											break;

										}
										int out=j+1;
										
										if(out==emptyroom.size())
											roomInfo.add(info);

									}

									Message msg = handler.obtainMessage();
									msg.obj = roomInfo;
									msg.what = NetConfig.SUCCESS;
									handler.sendMessage(msg);

								}

							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							Message msg = handler.obtainMessage();
							msg.what = NetConfig.NO_DATA;
							handler.sendMessage(msg);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Message msg = handler.obtainMessage();
				msg.what = NetConfig.FAILED;
				handler.sendMessage(msg);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<NameValuePair> createParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	
		params.add(new BasicNameValuePair("RadioButtonList1", "部门"));
		params.add(new BasicNameValuePair("TextBox2", "office326"));
		params.add(new BasicNameValuePair("TextBox1", "xinxi"));// 这是学号
		params.add(new BasicNameValuePair("__VIEWSTATE",
				"dDwxODI0OTM5NjI1Ozs+ErNwwEBfve9YGjMA8xEN6zdawEw="));
		params.add(new BasicNameValuePair("Button1", ""));
		params.add(new BasicNameValuePair("lbLanguage", ""));
		return params;
	}
}

