package com.haotang.pet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.haotang.pet.R;

/**
 * 垂直翻滚
 * 
 * @author zhanyue
 * 
 */

public class AutoTextView extends TextSwitcher implements
		ViewSwitcher.ViewFactory {

	private float mHeight;
	private Context mContext;
	// mInUp,mOutUp分别构成向下翻页的进出动画
	private Rotate3dAnimation mInUp;
	private Rotate3dAnimation mOutUp;

	// mInDown,mOutDown分别构成向下翻页的进出动画
	private Rotate3dAnimation mInDown;
	private Rotate3dAnimation mOutDown;
	private int textColor = Color.parseColor("#666666");
	private int textSize = 14;
	private int gavity;
	private int paddingleft = 2;
	private int paddingtop;
	private int paddingright = 2;
	private int paddingbottom;
	private int background;
	private TextView t;
	private boolean isMaxLength;
	private double screenWidth;

	public double getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}

	public boolean isMaxLength() {
		return isMaxLength;
	}

	public void setMaxLength(boolean isMaxLength) {
		this.isMaxLength = isMaxLength;
	}

	public AutoTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public AutoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.auto3d);
		// mHeight = a.getDimension(R.styleable.auto3d_textSize, 36);
		textColor = a.getColor(R.styleable.auto3d_textColor, textColor);
		textSize = a.getDimensionPixelSize(R.styleable.auto3d_textSize,
				textSize);
		gavity = a.getInteger(R.styleable.auto3d_textgravity, 0);
		background = a.getResourceId(R.styleable.auto3d_at_background, 0);
		paddingleft = a
				.getDimensionPixelSize(R.styleable.auto3d_paddingLeft, 2);
		paddingtop = a.getDimensionPixelSize(R.styleable.auto3d_paddingTop, 0);
		paddingright = a.getDimensionPixelSize(R.styleable.auto3d_paddingRight,
				2);
		paddingbottom = a.getDimensionPixelSize(
				R.styleable.auto3d_paddingBottom, 0);
		mHeight = 20;
		a.recycle();
		mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setFactory(this);
		mInUp = createAnim(-90, 0, true, true);
		mOutUp = createAnim(0, 90, false, true);
		mInDown = createAnim(90, 0, true, false);
		mOutDown = createAnim(0, -90, false, false);
		// TextSwitcher主要用于文件切换，比如 从文字A 切换到 文字 B，
		// setInAnimation()后，A将执行inAnimation，
		// setOutAnimation()后，B将执行OutAnimation
		setInAnimation(mInUp);
		setOutAnimation(mOutUp);
	}

	private Rotate3dAnimation createAnim(float start, float end,
			boolean turnIn, boolean turnUp) {
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				turnIn, turnUp);
		// 动画持续时间
		rotation.setDuration(300);
		rotation.setFillAfter(false);
		rotation.setInterpolator(new AccelerateInterpolator());
		return rotation;
	}

	public void setData() {

	}

	// 这里返回的TextView，就是我们看到的View
	@Override
	public TextView makeView() {
		t = new TextView(mContext);
		t.setGravity(gavity);
		// t.setTextSize(mHeight);
		t.setMaxLines(1);
		t.setSingleLine(true);
		t.setEllipsize(TruncateAt.END);
		t.setPadding(paddingleft, paddingtop, paddingright, paddingbottom);
		if (background > 0)
			t.setBackgroundResource(background);
		// 设置文字颜色
		t.setTextColor(textColor);
		t.setTextSize(textSize);
		return t;
	}

	@Override
	public void setText(CharSequence text) {
		super.setText(text);
		if (isMaxLength()) {
			t.setMaxWidth((int) (340.00 / 480.00 * getScreenWidth()));
		}
	}

	public void setDrawableRight(String imgUrl, int num) {
		/*
		 * if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) { Log.e("TAG",
		 * "imgUrl = " + imgUrl); Bitmap getLocalOrNetBitmap =
		 * Utils.GetLocalOrNetBitmap(imgUrl); Log.e("TAG",
		 * "getLocalOrNetBitmap = " + getLocalOrNetBitmap); ImageSpan imgSpan =
		 * new ImageSpan(mContext, getLocalOrNetBitmap); SpannableString
		 * spanString = new SpannableString("icon"); spanString.setSpan(imgSpan,
		 * num, num + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * t.setText(spanString); }
		 */
	}

	// 定义动作，向下滚动翻页
	public void previous() {
		if (getInAnimation() != mInDown) {
			setInAnimation(mInDown);
		}
		if (getOutAnimation() != mOutDown) {
			setOutAnimation(mOutDown);
		}
	}

	// 定义动作，向上滚动翻页
	public void next() {
		if (getInAnimation() != mInUp) {
			setInAnimation(mInUp);
		}
		if (getOutAnimation() != mOutUp) {
			setOutAnimation(mOutUp);
		}
	}

	class Rotate3dAnimation extends Animation {
		private final float mFromDegrees;
		private final float mToDegrees;
		private float mCenterX;
		private float mCenterY;
		private final boolean mTurnIn;
		private final boolean mTurnUp;
		private Camera mCamera;

		public Rotate3dAnimation(float fromDegrees, float toDegrees,
				boolean turnIn, boolean turnUp) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mTurnIn = turnIn;
			mTurnUp = turnUp;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			mCenterY = getHeight() / 2;
			mCenterX = getWidth() / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees
					+ ((mToDegrees - fromDegrees) * interpolatedTime);

			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final int derection = mTurnUp ? 1 : -1;

			final Matrix matrix = t.getMatrix();

			camera.save();
			if (mTurnIn) {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime - 1.0f), 0.0f);
			} else {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime), 0.0f);
			}
			camera.rotateX(degrees);
			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
}
