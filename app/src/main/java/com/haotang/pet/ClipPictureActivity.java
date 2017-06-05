package com.haotang.pet;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClipView;
import com.haotang.pet.view.ClipView.OnDrawListenerComplete;

/**
 * <p>
 * Title:ClipPictureActivity
 * </p>
 * <p>
 * Description:裁剪图片界面:裁剪框不动,图片可放大缩小
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-15 上午10:33:08
 */
public class ClipPictureActivity extends Activity implements OnTouchListener,
		OnClickListener {
	private ImageView srcPic;
	private View sure;
	private ClipView clipview;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	/** 动作标志：无 */
	private static final int NONE = 0;
	/** 动作标志：拖动 */
	private static final int DRAG = 1;
	/** 动作标志：缩放 */
	private static final int ZOOM = 2;
	/** 初始化动作标志 */
	private int mode = NONE;
	/** 记录起始坐标 */
	private PointF start = new PointF();
	/** 记录缩放时两指中间点坐标 */
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private Bitmap bitmap;
	private View btn_cancel;
	private Uri source;
	private float scale;
	private int flag;
	private int lastX;
	private int lastY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_picture);
		Intent intent = getIntent();
		source = intent.getParcelableExtra("source");
		flag = intent.getIntExtra("flag", 0);
		srcPic = (ImageView) this.findViewById(R.id.src_pic);
		srcPic.setOnTouchListener(this);
		ViewTreeObserver observer = srcPic.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			public void onGlobalLayout() {
				srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				initClipView(srcPic.getTop());
			}
		});
		sure = (View) this.findViewById(R.id.sure);
		btn_cancel = (View) this.findViewById(R.id.btn_cancel);
		sure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	/**
	 * 初始化截图区域，并将源图按裁剪框比例缩放
	 * 
	 * @param top
	 */
	private void initClipView(int top) {
		String realFilePath = Utils.getPathByUri4kitkat(this, source);
		bitmap = Utils.getxtsldraw(this, realFilePath);
		clipview = (ClipView) this.findViewById(R.id.clipview);
		clipview.bringToFront();
		// clipview.setCustomTopBarHeight(top);
		clipview.addOnDrawCompleteListener(new OnDrawListenerComplete() {
			public void onDrawCompelete() {
				clipview.removeOnDrawCompleteListener();
				int clipHeight = clipview.getClipHeight();
				int clipWidth = clipview.getClipWidth();
				int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
				int midY = clipview.getClipTopMargin() + (clipHeight / 2);
				int imageWidth = bitmap.getWidth();
				int imageHeight = bitmap.getHeight();
				// 按裁剪框求缩放比例
				float scale = (clipWidth * 1.0f) / imageWidth;
				if (imageWidth > imageHeight) {
					scale = (clipHeight * 1.0f) / imageHeight;
				}
				// 起始中心点
				float imageMidX = imageWidth * scale / 2;
				float imageMidY = clipview.getCustomTopBarHeight()
						+ imageHeight * scale / 2;
				srcPic.setScaleType(ScaleType.MATRIX);
				// 缩放
				matrix.postScale(scale, scale);
				// 平移
				matrix.postTranslate(midX - imageMidX, midY - imageMidY);
				srcPic.setImageMatrix(matrix);
				// srcPic.setImageBitmap(bitmap);
				srcPic.setImageURI(source);
			}
		});
		/*
		 * this.addContentView(clipview, new LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		 */
	}

	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			savedMatrix.set(matrix);
			// 设置开始点位置
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mode == DRAG) {/*
								 * int movex = (int) event.getRawX(); int moveY
								 * = (int) event.getRawY(); int distanceX =
								 * movex-lastX; int distanceY = moveY-lastY;
								 * Log.e("TAG", "lastX = " + lastX);
								 * Log.e("TAG", "lastY = " + lastY);
								 * Log.e("TAG", "movex = " + movex);
								 * Log.e("TAG", "moveY = " + moveY);
								 * Log.e("TAG", "distanceX = " + distanceX);
								 * Log.e("TAG", "distanceY = " + distanceY);
								 * //得到新的坐标 int left =
								 * srcPic.getLeft()+distanceX; int top =
								 * srcPic.getTop()+distanceY; int right =
								 * srcPic.getRight()+distanceX; int bottom =
								 * srcPic.getBottom()+distanceY; Log.e("TAG",
								 * "left = " + left); Log.e("TAG", "top = " +
								 * top); Log.e("TAG", "right = " + right);
								 * Log.e("TAG", "bottom = " + bottom);
								 * Log.e("TAG", "event.getX() = " +
								 * event.getX()); if(left > 0){
								 * matrix.postTranslate(-left,0); } //更新last
								 * lastX = movex; lastY = moveY;
								 */
			}
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true;
	}

	/**
	 * 多点触控时，计算最先放下的两指距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * 多点触控时，计算最先放下的两指中心坐标
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure:
			Bitmap clipBitmap = getBitmap();
			/*
			 * Uri parse = Uri.parse(MediaStore.Images.Media.insertImage(
			 * getContentResolver(), clipBitmap, null, null)); Log.e("TAG",
			 * "clipBitmap = " + clipBitmap.toString()); Log.e("TAG", "parse = "
			 * + parse.toString());
			 */
			File saveImage = Utils.saveImage(clipBitmap);
			Uri uri = Uri.fromFile(saveImage);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					SendSelectPostActivity.class);
			intent.putExtra("picOutput", uri);
			intent.putExtra("flag", "pic");
			startActivity(intent);
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取裁剪框内截图
	 * 
	 * @return
	 */
	private Bitmap getBitmap() {
		// 获取截屏
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
				clipview.getClipLeftMargin(), clipview.getClipTopMargin()
						+ statusBarHeight, clipview.getClipWidth(),
				clipview.getClipHeight());
		// 释放资源
		view.destroyDrawingCache();
		return finalBitmap;
	}
}
