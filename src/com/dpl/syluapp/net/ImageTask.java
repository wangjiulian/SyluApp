package com.dpl.syluapp.net;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTask extends AsyncTask<String, Void, Bitmap> {
	private Context context;
	private TextView dowmMess;
	private LinearLayout llCcasecade;
	private String path = Environment.getExternalStorageDirectory() + "/mess";
	private static int imageNum = 0;
	private int j;
	private static int i = 0;
	private int casecadeWidth;
	private ImageView imageView;

	public ImageTask(Context context, ImageView image, int casecadeWidth,
			int j, LinearLayout llCcasecade, TextView dowmMess) {
		this.context=context;
		imageView = image;
		this.j = j;
		this.llCcasecade = llCcasecade;
		this.casecadeWidth = casecadeWidth;
		this.dowmMess = dowmMess;
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(params[0]);

		HttpResponse httpResponse = null;
		Bitmap bmp = null;
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("不存在存在");
			file.mkdirs();
		} else
			System.out.println("存在");
		File iamgefile = new File(file + "/" + j + ".jpg");
		if (iamgefile.exists()) {
			System.out.println("图片存在！");
			bmp = ImageTask.decodeSampledBitmapFromResource(
					iamgefile.getPath(), casecadeWidth);

			return bmp;
		} else {
			System.out.println("图片不存在，下载！");

			System.out.println("iamgefile --->" + iamgefile);
			i++;
			try {
				httpResponse = client.execute(httpPost);
				byte data[] = EntityUtils.toByteArray(httpResponse.getEntity());
				FileOutputStream out = new FileOutputStream(iamgefile);
				out.write(data);
				System.out.println("iamgefile --->" + iamgefile);
				bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (bmp != null)
			imageNum++;
		return bmp;
	}

	@Override
	protected void onPostExecute(Bitmap bmp) {

		super.onPostExecute(bmp);
		if (imageView != null && bmp != null) {

			int oldwidth = bmp.getWidth();
			int oldheight = bmp.getHeight();
			LayoutParams lp = imageView.getLayoutParams();
			lp.height = (oldheight * casecadeWidth) / oldwidth;
			imageView.setPadding(0, 2, 0, 0);
			imageView.setLayoutParams(lp);
			imageView.setImageBitmap(bmp);
			if (imageNum == 20) {
				SharedPreferences preferences=context.getSharedPreferences("imageNum", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("down", true).commit();
				dowmMess.setVisibility(View.GONE);
				llCcasecade.setVisibility(View.VISIBLE);
			}

		}

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}
}