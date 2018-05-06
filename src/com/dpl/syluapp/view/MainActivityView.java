//package com.dpl.syluapp.view;
//import com.dpl.syluapp.R;
//
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.PorterDuff.Mode;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Looper;
//import android.os.Parcelable;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.View;
///**
// * @author JUST玖
// *
// * 2015-2-21
// */
//public class MainActivityView extends View {
//
//	private static final String INTANCE_STATE = "instance_state";
//	private static final String STATUS_ALPHA = "status_alpha";
//	// 这三个参数首先在内存区绘制mBitmap形成的纯色区域
//	private Canvas mCanvas;// canvas提供的绘制图形方法
//	private Bitmap mBitmap;
//	private Paint mPaint;
//	// 透明度
//	private float mAlpha = 0.0f;// 不断修改透明度
//	// 在CanVas上的位置坐标
//	private Rect mIconRect;
//	private Rect mTextBound;
//	private Paint mTextPaint;
//
//	private int mColor = 0xFF45C01A;// 颜色为绿色
//	private Bitmap mIconBitmap;
//	private String mText = "微信";
//	private int mTextSize = (int) TypedValue.applyDimension(
//			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
//
//	/**
//	 * 初始化自定义属性
//	 * 
//	 * @param context
//	 * @param attrs
//	 * @param defStyleAttr
//	 */
//	public MainActivityView(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//		TypedArray a = context.obtainStyledAttributes(attrs,
//				R.styleable.MainActivityView);
//
//		mColor = a.getColor(R.styleable.MainActivityView_color, 0);
//		mText = a.getString(R.styleable.MainActivityView_text);
//		mTextSize = (int) a.getDimension(
//				R.styleable.MainActivityView_textsize, 0);
//
//		BitmapDrawable bitmapDrawable = (BitmapDrawable) a
//				.getDrawable(R.styleable.MainActivityView_icon);
//		mIconBitmap = bitmapDrawable.getBitmap();
//		a.recycle();
//
//		mTextBound = new Rect();
//		mTextPaint = new Paint();
//		mTextPaint.setTextSize(mTextSize);
//		mTextPaint.setColor(0xff555555);
//		// mTextRect获得文字的长宽高。文字的绘制范围
//		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
//
//	}
//
//	/**
//	 * 获取View本身的大小
//	 */
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// Icon的宽度
//		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
//				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
//				- getPaddingBottom() - mTextBound.height());
//		int left = getMeasuredWidth() / 2 - iconWidth / 2;
//		int top = getMeasuredHeight() / 2 - iconWidth / 2 - mTextBound.height()
//				/ 2;
//		// 绘制Icon的范围
//		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);// 获取Icon绘制区域
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		canvas.drawBitmap(mIconBitmap, null, mIconRect, null); // 在内存中绘制Icon
//
//		// alpha 0-255
//		int alpha = (int) Math.ceil(255 * mAlpha);
//
//		// 内存去准备mBitmap, setAlpha,纯色,xfermode，图标
//		setupTagemetBitmap(alpha);
//		// 1 设置原文本 2 设置变色文本
//		drawSourseText(canvas, alpha);
//		drawTargetText(canvas, alpha);
//
//		canvas.drawBitmap(mBitmap, 0, 0, null);// 绘制在View中
//
//		super.onDraw(canvas);
//	}
//
//	/**
//	 * 绘制变色文本
//	 * 
//	 * @param canvas
//	 * @param alpha
//	 */
//	private void drawTargetText(Canvas canvas, int alpha) {
//		mTextPaint.setColor(mColor);
//		mTextPaint.setAlpha(alpha);
//		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
//				- mTextBound.width() / 2,
//				mIconRect.bottom + mTextBound.height(), mTextPaint);
//	}
//
//	/**
//	 * 绘制原文本
//	 * 
//	 * @param canvas
//	 * @param alpha
//	 */
//	private void drawSourseText(Canvas canvas, int alpha) {
//		mTextPaint.setColor(0xff333333);
//		mTextPaint.setAlpha(255 - alpha);
//		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
//				- mTextBound.width() / 2,
//				mIconRect.bottom + mTextBound.height(), mTextPaint);
//
//	}
//
//	// 在内存中绘制可变色的Icon
//	private void setupTagemetBitmap(int alpha) {
//		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
//				Config.ARGB_8888);
//		mCanvas = new Canvas(mBitmap);
//		mPaint = new Paint();
//		// 画笔颜色
//		mPaint.setColor(mColor);
//		// 设置透明度
//		mPaint.setAlpha(alpha);
//		// 允许锯齿
//		mPaint.setAntiAlias(true);
//		// 允许抖动
//		mPaint.setDither(true);
//		// 在Icon绘制区域绘制纯色
//		mCanvas.drawRect(mIconRect, mPaint);
//		// 设置两张图片相交时的模式
//		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		// 将Icon绘制在背景颜色上，在内存中
//		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
//	}
//
//	public void setIconAlpha(float alpha) {
//		this.mAlpha = alpha;
//		invalidateView();
//	}
//
//	/**
//	 * View重绘
//	 */
//	private void invalidateView() {
//		if (Looper.getMainLooper() == Looper.myLooper()) {
//			// UI线程
//
//			invalidate();
//
//		} else {
//			// 非UI线程
//
//			postInvalidate();
//
//		}
//
//	}
//
//	// Activity回收时保存状态
//	@Override
//	protected Parcelable onSaveInstanceState() {
//
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("save", super.onSaveInstanceState());
//		bundle.putFloat(STATUS_ALPHA, mAlpha);
//		return bundle;
//	}
//
//	// 恢复Activity之前的状态，恢复之前先执行
//	@Override
//	protected void onRestoreInstanceState(Parcelable state) {
//
//		if (state instanceof Bundle) {
//			Bundle bundle = (Bundle) state;
//			mAlpha = bundle.getFloat(STATUS_ALPHA);
//			super.onRestoreInstanceState(bundle.getParcelable(INTANCE_STATE));
//			return;
//		}
//		super.onRestoreInstanceState(state);
//	}
//
//	public MainActivityView(Context context, AttributeSet attrs) {
//		this(context, attrs, 0);
//	}
//
//	public MainActivityView(Context context) {
//		this(context, null);
//	}
//
//}
