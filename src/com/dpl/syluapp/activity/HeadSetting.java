package com.dpl.syluapp.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;
import com.dpl.syluapp.utils.BitmapCompress;
import com.dpl.syluapp.utils.ZoomBitmap;

public class HeadSetting extends AppActivity {
	private Bitmap upbitmap;
	private String Path = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "syluapp"
			+ File.separator
			+ "head.jpg";
	private String iamgePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	private ImageView headPhoto;
	private TextView changeHead;
	private byte[] image_data;// 存储图片的数据;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		createDir();
		setContentView(R.layout.activity_headsetting_main);
		headPhoto = (ImageView) findViewById(R.id.image_headsetting_head);
		changeHead = (TextView) findViewById(R.id.tv_headsetting_change);
		initHead();
		changeHead.setOnClickListener(new headListener());

	}

	class headListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog();

		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("上传头像")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] { "拍照", "从相册选择" }, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// dialog.dismiss();
								if (which == 1) {
									dialog.dismiss();
									Intent intent = new Intent(
											Intent.ACTION_PICK, null);
									intent.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
									startActivityForResult(intent, 0);
								} else {
									dialog.dismiss();
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									/*
									 * filename = "touxiang" +
									 * System.currentTimeMillis() + ".jpg";
									 * System.out.println(filename);
									 */
									// 下面这句指定调用相机拍照后的照片存储的路径
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(Path)));
									startActivityForResult(intent, 1);
								}
							}
						}).setNegativeButton("取消", null).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case 1:
			File temp = new File(Path);
			startPhotoZoom(Uri.fromFile(temp));

			break;
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				ContentResolver resolver = getContentResolver();

				InputStream inputStream;
				try {
					inputStream = resolver.openInputStream(uri);
					FileOutputStream outputStream = new FileOutputStream(Path);
					byte da[] = new byte[1024];
					while (inputStream.read(da) != -1) {
						outputStream.write(da, 0, da.length);

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				startPhotoZoom(data.getData());

			}
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				try {
					setPicToView(data);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 * @throws FileNotFoundException
	 */
	private void setPicToView(Intent picdata) throws FileNotFoundException {

		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			float wight = photo.getWidth();
			float height = photo.getHeight();
			upbitmap = ZoomBitmap.zoomImage(photo, wight, height);


			Uri imageFileUri = picdata.getData();
			int dw = getWindowManager().getDefaultDisplay().getWidth();
			int dh = getWindowManager().getDefaultDisplay().getHeight() / 2;

			BitmapFactory.Options factory = new BitmapFactory.Options();
			factory.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
			Bitmap bmp = extras.getParcelable("data");
			int hRatio = (int) Math.ceil(factory.outHeight / (float) dh); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(factory.outWidth / (float) dw); // 图片是宽度的几倍
			System.out.println("hRatio:" + hRatio + "  wRatio:" + wRatio);
			// 缩小到 1/ratio的尺寸和 1/ratio^2的像素
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					factory.inSampleSize = hRatio;
				} else
					factory.inSampleSize = wRatio;
			}
			factory.inJustDecodeBounds = false;
			bmp = extras.getParcelable("data");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			FileOutputStream outputStream = new FileOutputStream(Path);
			byte data[] = baos.toByteArray();
			try {
				outputStream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			headPhoto.setImageBitmap(bmp);

		}
	}

	void createDir() {
		File file = new File(iamgePath + File.separator + "syluapp");
		if (!file.exists())
			file.mkdirs();

	}

	void initHead() {
		File file = new File(Path);
		Log.i("TAG", file.getPath());

		if (file.exists()) {
			Bitmap photo = BitmapFactory.decodeFile(Path);
			upbitmap = BitmapCompress.compressImage(photo);

			headPhoto.setImageBitmap(upbitmap);

		}
	}

}