package com.dpl.syluapp.adapter;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dpl.syluapp.R;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.preferences.LibraryUserInfo;
import com.dpl.syluapp.utils.LibraryParase;

public class LibrarySelectAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<LibrarySelectInfo> listInfo;
	private ViewHolder holder;
	private ProgressDialog progress;

	public LibrarySelectAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public void setList(List<LibrarySelectInfo> listInfo) {
		this.listInfo = listInfo;

		System.out.println("getbName" + listInfo.get(0).getbName());

	}

	@Override
	public int getCount() {

		return listInfo.size();
	}

	@Override
	public Object getItem(int position) {

		return listInfo.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int num = position;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.libraryselect_adapter, null);
			holder = new ViewHolder();// 临时储存器
			holder.bName = (TextView) convertView
					.findViewById(R.id.tv_libraryadapter_bname);
			holder.bBorrowDay = (TextView) convertView
					.findViewById(R.id.tv_libraryadapter_borrow);

			holder.bReturnDay = (TextView) convertView
					.findViewById(R.id.tv_libraryadapter_breturn);
			holder.bRenew = (Button) convertView
					.findViewById(R.id.btn_libraryadapter_brenew);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.bName.setText(listInfo.get(position).getbName());
		holder.bBorrowDay.setText(listInfo.get(position).getbBorrowDay());
		holder.bReturnDay.setText(listInfo.get(position).getbReturnDay());
		final String Bbar_code = listInfo.get(position).getBbar_code();
		final String code = listInfo.get(position).getCode();
		String bRenew = listInfo.get(position).getbRenew();
		if (bRenew.equals("0")) {
			holder.bRenew.setText("续借");
			holder.bRenew.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("图书续借提示");
					builder.setMessage("您确定要续借吗？");
					builder.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Toast.makeText(context, "第几" + num + Bbar_code, 1)
									.show();
							String[] code_params = { Bbar_code, code };
							new renew().execute(code_params);
							progress = new ProgressDialog(context);
							progress.setTitle("联网提醒");
							progress.setMessage("正在续借，请稍后.....");
							progress.show();

						}
					});

					builder.setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					builder.show();

				}
			});
		}

		else {
			holder.bRenew.setText("不可续借");
			holder.bRenew.setClickable(false);
		}
		return convertView;

	}

	private class ViewHolder {
		TextView bName;
		TextView bBorrowDay;
		TextView bReturnDay;
		Button bRenew;

	}

	class renew extends AsyncTask<String, Void, List<LibrarySelectInfo>> {
		@Override
		protected void onPostExecute(List<LibrarySelectInfo> result) {
			listInfo.clear();
			listInfo = result;
			notifyDataSetChanged();
			progress.dismiss();
			Toast.makeText(context, "续借成功！", 1).show();
			super.onPostExecute(result);
		}

		@Override
		protected List<LibrarySelectInfo> doInBackground(String... params) {
			String name = LibraryUserInfo.getUser();
			String pswd = LibraryUserInfo.getPswd();
			List<LibrarySelectInfo> listInfo = null;
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
									+ params[0] + "&&check=" + params[1]);
					try {
						httpResponse2 = client.execute(httpget2);
					} catch (Exception e) {

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

							LibraryParase parase = new LibraryParase();

							listInfo = parase.parase(result1);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("listInfo--->" + listInfo.toString());
			return listInfo;
		}

	}
}
