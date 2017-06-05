package com.haotang.pet.view;

import com.haotang.pet.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;
	private int mBorderWidth = 10;
	public int mBorderColor = Color.parseColor("#f2f2f2");
	private boolean useDefaultStyle = true;
	private Canvas canvas;
	private int width;
	private int height;
	
	static{
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
		mBorderColor = ta.getColor(R.styleable.CircleImageView_border_color, mBorderColor);
		mBorderWidth = ta.getDimensionPixelOffset(R.styleable.CircleImageView_border_width, mBorderWidth);
		ta.recycle();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public void setUseDefaultStyle(boolean useDefaultStyle) {
		this.useDefaultStyle = useDefaultStyle;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		Drawable localDrawable = getDrawable();
		if(localDrawable == null)
			return;
		if(localDrawable instanceof NinePatchDrawable)
			return;
		if(paint == null){
			Paint localPaint = new Paint();
			localPaint.setFilterBitmap(false);
			localPaint.setAntiAlias(true);
			localPaint.setXfermode(MASK_XFERMODE);
			paint = localPaint;
		}
		width = getWidth();
		height = getHeight();
		int layer = canvas.saveLayer(0f, 0f, width, height, null, 31);
		  /** 设置drawable的大小 */
		localDrawable.setBounds(0, 0, width, height);
		  /** 将drawable绑定到bitmap(this.mask)上面（drawable只能通过bitmap显示出来） */
		localDrawable.draw(canvas);
		
		if(null == mask || mask.isRecycled()){
			mask = createOvlBitmap(width, height);
		}
		 /** 将bitmap画到canvas上面 */
		canvas.drawBitmap(mask, 0f, 0f, paint);
		canvas.restoreToCount(layer);
		drawBorder(canvas, width, height);
		
	}
	
	/**
	 * 画外面的边框
	 * @param canvas
	 * @param width
	 * @param height
	 */
	public void drawBorder(int mBorderColor){
		if(mBorderWidth == 0){
			return;
		}
		Paint mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		  /**
         * 坐标x：view宽度的一半  坐标y：view高度的一半 半径r：因为是view的宽度-border的一半
         */
		canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);
		canvas = null;
	}
	
	/**
	 * 画外面的边框
	 * @param canvas
	 * @param width
	 * @param height
	 */
	private void drawBorder(Canvas canvas, int width, int height){
		if(mBorderWidth == 0){
			return;
		}
		Paint mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		  /**
         * 坐标x：view宽度的一半  坐标y：view高度的一半 半径r：因为是view的宽度-border的一半
         */
		canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);
		canvas = null;
	}
	/**
	 * 获取一个bitmap，目的是用来承载drawable;
     * <p>
     * 将这个bitmap放在canvas上面承载，并在其上面画一个椭圆(其实也是一个圆，因为width=height)来固定显示区域
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap createOvlBitmap(int width, int height){
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		int padding = mBorderWidth > 3 ? (mBorderWidth - 3) : 1;
	    /**
         * 设置椭圆的大小(因为椭圆的最外边会和border的最外边重合的，如果图片最外边的颜色很深，有看出有棱边的效果，所以为了让体验更加好，
         * 让其缩进padding px)
         */
		
		RectF rectf = new RectF(padding, padding, width - padding, height - padding);
		localCanvas.drawOval(rectf, localPaint);
		
		
		return localBitmap;
	}
	public void setBorderColor(String colorString){
		mBorderColor = Color.parseColor(colorString);
		postInvalidate();
	}
}
