package com.dpl.syluapp.view;

import com.dpl.syluapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRoonView extends View {
	private Canvas mCanvas;
	private Rect mTextRect;
	private Paint mPaint;
	private Paint mTextPaint;
	private Bitmap mBitmap;
	private float mAlpha = 0.0f;
	private String title = "上午";
	private float textSize;
	private int backColor;
	private int textColor;

	public EmptyRoonView(Context context) {
		this(context, null);
	}

	public EmptyRoonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EmptyRoonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EmptyView);
		title = a.getString(R.styleable.EmptyView_etitle);
		textSize = a.getDimension(R.styleable.EmptyView_textSize, 0);
		backColor = a.getColor(R.styleable.EmptyView_backcolor, 0);
		textColor = a.getColor(R.styleable.EmptyView_textColor, 0);
		a.recycle();
		mTextPaint = new Paint();
		mTextPaint.setColor(0xff555555);
		mTextPaint.setTextSize(textSize);
		mTextRect = new Rect();
		mTextPaint.getTextBounds(title, 0, title.length(), mTextRect);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int alpha = (int) Math.ceil(255 * mAlpha);
		// drawImageSourse(alpha);
		// setupTagemetBitmap(canvas, alpha);
		drawSourseText(canvas, alpha);
		drawTargetText(canvas, alpha);

	}

	/**
	 * 绘制原文本
	 * 
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourseText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha((int) (255 - alpha));
		canvas.drawText(title, getMeasuredWidth() / 2 - mTextRect.width() / 2,
				getMeasuredHeight() / 2, mTextPaint);

	}

	private void drawImageSourse(int alpha) {

		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);

		mCanvas = new Canvas(mBitmap);

	}

	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(textColor);
		mTextPaint.setAlpha((int) (255 - alpha));
		canvas.drawText(title, getMeasuredWidth() / 2 - mTextRect.width() / 2,
				getMeasuredHeight() / 2, mTextPaint);
	}

	private void setupTagemetBitmap(Canvas canvas, int alpha) {
		mTextPaint.setColor(textColor);
		mTextPaint.setAlpha((int) (255 - alpha));
		canvas.drawText(title, getMeasuredWidth() / 2, getMeasuredHeight() / 2,
				mTextPaint);
	}
}
