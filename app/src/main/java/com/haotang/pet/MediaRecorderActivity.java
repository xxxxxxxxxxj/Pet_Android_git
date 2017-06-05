package com.haotang.pet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.CommonIntentExtra;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.ProgressView;
import com.yixia.camera.demo.log.Logger;
import com.yixia.videoeditor.adapter.UtilityAdapter;
import com.yixia.weibo.sdk.MediaRecorderBase;
import com.yixia.weibo.sdk.MediaRecorderNative;
import com.yixia.weibo.sdk.VCamera;
import com.yixia.weibo.sdk.model.MediaObject;
import com.yixia.weibo.sdk.model.MediaObject$MediaPart;
import com.yixia.weibo.sdk.util.ConvertToUtils;
import com.yixia.weibo.sdk.util.DeviceUtils;
import com.yixia.weibo.sdk.util.FileUtils;
import com.yixia.weibo.sdk.util.Log;
import com.yixia.weibo.sdk.util.StringUtils;

/**
 * <p>
 * Title:MediaRecorderActivity
 * </p>
 * <p>
 * Description:录制小视频界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-12 上午10:25:49
 */
public class MediaRecorderActivity extends SuperActivity implements
		com.yixia.weibo.sdk.MediaRecorderBase$OnErrorListener, OnClickListener,
		com.yixia.weibo.sdk.MediaRecorderBase$OnPreparedListener,
		com.yixia.weibo.sdk.MediaRecorderBase$OnEncodeListener {
	/** 录制最长时间 */
	public final static int RECORD_TIME_MAX = 20 * 1000;
	/** 录制最小时间 */
	public final static int RECORD_TIME_MIN = 3 * 1000;
	/** 刷新进度条 */
	private static final int HANDLE_INVALIDATE_PROGRESS = 0;
	/** 延迟拍摄停止 */
	private static final int HANDLE_STOP_RECORD = 1;
	/** 对焦 */
	private static final int HANDLE_HIDE_RECORD_FOCUS = 2;
	/** 拍摄按钮 */
	private ImageView iv_media_recorder_video;
	/** 摄像头数据显示画布 */
	private SurfaceView sfv_media_recorder;
	/** 录制进度 */
	private ProgressView pv_media_recorder;
	/** SDK视频录制对象 */
	private MediaRecorderBase mMediaRecorder;
	/** 视频信息 */
	private MediaObject mMediaObject;
	/** 需要重新编译（拍摄新的或者回删） */
	private boolean mRebuild;
	/** on */
	private boolean mCreated;
	/** 是否是点击状态 */
	private volatile boolean mPressedStatus;
	/** 是否已经释放 */
	private volatile boolean mReleased;
	/** 对焦图片宽度 */
	private int mFocusWidth;
	private int mBackgroundColorNormal, mBackgroundColorPress;
	/** 屏幕宽度 */
	private int mWindowWidth;
	protected ProgressDialog mProgressDialog;
	private RelativeLayout bottom_layout;
	private TextView tv_media_recorder_cancle;
	private TextView tv_media_recorder_num;

	public ProgressDialog showProgress(String title, String message) {
		return showProgress(title, message, -1);
	}

	public ProgressDialog showProgress(String title, String message, int theme) {
		if (mProgressDialog == null) {
			if (theme > 0)
				mProgressDialog = new ProgressDialog(this, theme);
			else
				mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
			mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
		}

		if (!StringUtils.isEmpty(title))
			mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		return mProgressDialog;
	}

	public void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		hideProgress();
		mProgressDialog = null;
	}

	/** 反序列化对象 */
	protected static MediaObject restoneMediaObject(String obj) {
		try {
			String str = FileUtils.readFile(new File(obj));
			Gson gson = new Gson();
			MediaObject result = gson.fromJson(str.toString(),
					MediaObject.class);
			result.getCurrentPart();
			preparedMediaObject(result);
			return result;
		} catch (Exception e) {
			if (e != null)
				Log.e("VCamera", "readFile", e);
		}
		return null;
	}

	/** 预处理数据对象 */
	public static void preparedMediaObject(MediaObject mMediaObject) {
		if (mMediaObject != null && mMediaObject.getMedaParts() != null) {
			int duration = 0;
			for (com.yixia.weibo.sdk.model.MediaObject$MediaPart part : (LinkedList<com.yixia.weibo.sdk.model.MediaObject$MediaPart>) mMediaObject
					.getMedaParts()) {
				part.startTime = duration;
				part.endTime = part.startTime + part.duration;
				duration += part.duration;
			}
		}
	}

	/** 序列号保存视频数据 */
	public static boolean saveMediaObject(MediaObject mMediaObject) {
		if (mMediaObject != null) {
			try {
				if (StringUtils.isNotEmpty(mMediaObject.getObjectFilePath())) {
					FileOutputStream out = new FileOutputStream(
							mMediaObject.getObjectFilePath());
					Gson gson = new Gson();
					out.write(gson.toJson(mMediaObject).getBytes());
					out.flush();
					out.close();
					return true;
				}
			} catch (Exception e) {
				Logger.e(e);
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mCreated = false;
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
		loadIntent();
		loadViews();
		mCreated = true;
	}

	/** 加载传入的参数 */
	private void loadIntent() {
		mWindowWidth = DeviceUtils.getScreenWidth(this);
		mFocusWidth = ConvertToUtils.dipToPX(this, 64);
		mBackgroundColorNormal = getResources().getColor(R.color.black);// camera_bottom_bg
		mBackgroundColorPress = getResources().getColor(
				R.color.camera_bottom_press_bg);
	}

	/** 加载视图 */
	private void loadViews() {
		setContentView(R.layout.activity_media_recorder);
		// ~~~ 绑定控件
		sfv_media_recorder = (SurfaceView) findViewById(R.id.sfv_media_recorder);
		pv_media_recorder = (ProgressView) findViewById(R.id.pv_media_recorder);
		iv_media_recorder_video = (ImageView) findViewById(R.id.iv_media_recorder_video);
		tv_media_recorder_cancle = (TextView) findViewById(R.id.tv_media_recorder_cancle);
		tv_media_recorder_num = (TextView) findViewById(R.id.tv_media_recorder_num);
		tv_media_recorder_cancle.setOnClickListener(this);
		bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
		// ~~~ 绑定事件
		if (DeviceUtils.hasICS()) {
			sfv_media_recorder.setOnTouchListener(mOnSurfaveViewTouchListener);
		}
		iv_media_recorder_video
				.setOnTouchListener(mOnVideoControllerTouchListener);
		pv_media_recorder.setMaxDuration(RECORD_TIME_MAX);
		initSurfaceView();
	}

	/** 初始化画布 */
	private void initSurfaceView() {
		final int width = DeviceUtils.getScreenWidth(this);
		((RelativeLayout.LayoutParams) bottom_layout.getLayoutParams()).topMargin = width;
		int height = width * 4 / 3;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sfv_media_recorder
				.getLayoutParams();
		lp.width = width;
		lp.height = height;
		sfv_media_recorder.setLayoutParams(lp);
	}

	/** 初始化拍摄SDK */
	private void initMediaRecorder() {
		mMediaRecorder = new MediaRecorderNative();
		mRebuild = true;
		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setOnEncodeListener(this);
		File f = new File(VCamera.getVideoCachePath());
		if (!FileUtils.checkFile(f)) {
			f.mkdirs();
		}
		String key = String.valueOf(System.currentTimeMillis());
		mMediaObject = mMediaRecorder.setOutputDirectory(key,
				VCamera.getVideoCachePath() + key);
		mMediaRecorder.setOnSurfaveViewTouchListener(sfv_media_recorder);
		mMediaRecorder.setSurfaceHolder(sfv_media_recorder.getHolder());
		mMediaRecorder.prepare();
	}

	/** 点击屏幕录制 */
	private View.OnTouchListener mOnSurfaveViewTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null || !mCreated) {
				return false;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 检测是否手动对焦
				showFocusImage(event);
				mMediaRecorder.setAutoFocus();
				break;
			}
			return true;
		}
	};

	/** 点击屏幕录制 */
	private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null) {
				return false;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
					return true;
				}
				startRecord();
				break;
			case MotionEvent.ACTION_UP:
				// 暂停
				if (mPressedStatus) {
					stopRecord();
					if (mMediaObject.getDuration() < RECORD_TIME_MIN) {
						ToastUtil.showToastShortCenter(
								MediaRecorderActivity.this, "录制时间太短");
						if (mMediaObject != null
								&& mMediaObject.mediaList != null) {
							int size = mMediaObject.mediaList.size();
							if (size > 0) {
								com.yixia.weibo.sdk.model.MediaObject$MediaPart part = (MediaObject$MediaPart) mMediaObject.mediaList
										.get(size - 1);
								mMediaObject.removePart(part, true);
								if (mMediaObject.mediaList.size() > 0)
									mMediaObject.mCurrentPart = (MediaObject$MediaPart) mMediaObject.mediaList
											.get(mMediaObject.mediaList.size() - 1);
								else
									mMediaObject.mCurrentPart = null;
								return true;
							}
						}
						if (pv_media_recorder != null) {
							pv_media_recorder.invalidate();
							tv_media_recorder_num.setText("0.0s");
							tv_media_recorder_num.setVisibility(View.GONE);
						}
					} else {
						stopRecord();
						mMediaRecorder.startEncoding();
					}
				}
				break;
			}
			return true;
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		UtilityAdapter.freeFilterParser();
		UtilityAdapter.initFilterParser();
		if (mMediaRecorder == null) {
			initMediaRecorder();
		} else {
			mMediaRecorder.prepare();
			pv_media_recorder.setData(mMediaObject);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopRecord();
		UtilityAdapter.freeFilterParser();
		if (!mReleased) {
			if (mMediaRecorder != null)
				mMediaRecorder.release();
		}
		mReleased = false;
	}

	/** 手动对焦 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private boolean checkCameraFocus(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		float touchMajor = event.getTouchMajor();
		float touchMinor = event.getTouchMinor();
		Logger.e("touchMajor = " + touchMajor);
		Logger.e("touchMinor = " + touchMinor);
		Rect touchRect = new Rect((int) (x - touchMajor / 2),
				(int) (y - touchMinor / 2), (int) (x + touchMajor / 2),
				(int) (y + touchMinor / 2));
		Logger.e("touchRect = " + touchRect);
		Logger.e("mWindowWidth = " + mWindowWidth);
		if (touchRect.right > 1000)
			touchRect.right = 1000;
		if (touchRect.bottom > 1000)
			touchRect.bottom = 1000;
		if (touchRect.left < 0)
			touchRect.left = 0;
		if (touchRect.right < 0)
			touchRect.right = 0;
		if (touchRect.left >= touchRect.right
				|| touchRect.top >= touchRect.bottom)
			return false;
		ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
		focusAreas.add(new Camera.Area(touchRect, 1000));
		int left = touchRect.left - (mFocusWidth / 2);
		int top = touchRect.top - (mFocusWidth / 2);
		if (left < 0)
			left = 0;
		else if (left + mFocusWidth > mWindowWidth)
			left = mWindowWidth - mFocusWidth;
		if (top + mFocusWidth > mWindowWidth)
			top = mWindowWidth - mFocusWidth;
		mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);// 最多3.5秒也要消失
		return true;
	}

	private void showFocusImage(MotionEvent e) {
		int x = Math.round(e.getX());
		int y = Math.round(e.getY());
		int focusWidth = 100;
		int focusHeight = 100;
		int previewWidth = sfv_media_recorder.getWidth();
		Rect touchRect = new Rect();
		mMediaRecorder.calculateTapArea(focusWidth, focusHeight, 1f, x, y,
				previewWidth, previewWidth, touchRect);
		int left = touchRect.left - (mFocusWidth / 2);
		int top = touchRect.top - (mFocusWidth / 2);
		if (left < 0)
			left = 0;
		else if (left + mFocusWidth > mWindowWidth)
			left = mWindowWidth - mFocusWidth;
		if (top + mFocusWidth > mWindowWidth)
			top = mWindowWidth - mFocusWidth;
		Logger.e("left =  " + left);
		Logger.e("top =  " + top);
		mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);// 最多3.5秒也要消失
	}

	/** 开始录制 */
	private void startRecord() {
		tv_media_recorder_num.setVisibility(View.VISIBLE);
		if (mMediaRecorder != null) {
			com.yixia.weibo.sdk.model.MediaObject$MediaPart part = mMediaRecorder
					.startRecord();
			if (part == null) {
				return;
			}
			pv_media_recorder.setData(mMediaObject);
		}
		mRebuild = true;
		mPressedStatus = true;
		if (mHandler != null) {
			mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
			mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
			mHandler.removeMessages(HANDLE_STOP_RECORD);
			mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD,
					RECORD_TIME_MAX - mMediaObject.getDuration());
		}
	}

	@Override
	public void onBackPressed() {
		if (mMediaObject != null && mMediaObject.getDuration() > 1) {
			// 未转码
			new AlertDialog.Builder(this)
					.setTitle(R.string.hint)
					.setMessage(R.string.record_camera_exit_dialog_message)
					.setNegativeButton(
							R.string.record_camera_cancel_dialog_yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mMediaObject.delete();
									finish();
								}

							})
					.setPositiveButton(R.string.record_camera_cancel_dialog_no,
							null).setCancelable(false).show();
			return;
		}
		// 停止消息推送轮询
		VCamera.stopPollingService();
		finish();
	}

	/** 停止录制 */
	private void stopRecord() {
		mPressedStatus = false;
		if (mMediaRecorder != null) {
			mMediaRecorder.stopRecord();
		}
		mHandler.removeMessages(HANDLE_STOP_RECORD);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
			mHandler.removeMessages(HANDLE_STOP_RECORD);
		}
		switch (id) {
		case R.id.tv_media_recorder_cancle:
			onBackPressed();
			break;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_STOP_RECORD:
				stopRecord();
				break;
			case HANDLE_INVALIDATE_PROGRESS:
				if (mMediaRecorder != null && !isFinishing()) {
					float num = mMediaObject.getDuration() / 1000;
					tv_media_recorder_num.setText(num + "s");
					if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
						mMediaRecorder.startEncoding();
					}
					if (pv_media_recorder != null)
						pv_media_recorder.invalidate();
					if (mPressedStatus)
						sendEmptyMessageDelayed(0, 30);
				}
				break;
			}
		}
	};

	@Override
	public void onEncodeStart() {
		showProgress("", getString(R.string.record_camera_progress_message));
	}

	@Override
	public void onEncodeProgress(int progress) {
		Logger.e("[MediaRecorderActivity]onEncodeProgress..." + progress);
	}

	/** 转码完成 */
	@Override
	public void onEncodeComplete() {
		hideProgress();
		Intent intent = new Intent(this, SendSelectPostActivity.class);
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			bundle = new Bundle();
		bundle.putSerializable(CommonIntentExtra.EXTRA_MEDIA_OBJECT,
				mMediaObject);
		bundle.putString("output", mMediaObject.getOutputTempVideoPath());
		bundle.putBoolean("Rebuild", mRebuild);
		intent.putExtras(bundle);
		intent.putExtra("flag", "video");
		startActivity(intent);
		finish();
		mRebuild = false;
	}

	/**
	 * 转码失败 检查sdcard是否可用，检查分块是否存在
	 */
	@Override
	public void onEncodeError() {
		hideProgress();
		Toast.makeText(this, R.string.record_video_transcoding_faild,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onVideoError(int what, int extra) {

	}

	@Override
	public void onAudioError(int what, String message) {

	}

	@Override
	public void onPrepared() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
