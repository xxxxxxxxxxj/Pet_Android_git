package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionWithParamListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.MProgressDialog;

public class FostercareLiveFullScreen extends SuperActivity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPlayingBufferCacheListener,
		OnCompletionWithParamListener, OnClickListener {

	private static final String AK = "W8hthyKDT42idmitIg1t4D50";
	private static final String SK = "Ub9DKdMfdqIknHKL1uzmlydU1vvFUObU";
	private final String TAG = "FostercareLiveActivity";
	private final Object SYNC_Playing = new Object();
	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "FostercareLiveActivity";
	private int mLastPos;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	private HandlerThread mHandlerThread;
	private EventHandler mEventHandler;
	private static final int UI_EVENT_PLAY = 0;
	private BVideoView bvv_live_fostercarefullscreen;
	private String LiveUrl;
	private MProgressDialog pDialog;
	private ImageView iv_live_fostercarefullscreen_back;
	private RelativeLayout rl_live_fostercarefullscreen;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UI_EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				/**
				 * 设置播放url
				 */
				bvv_live_fostercarefullscreen.setVideoPath(LiveUrl);
				/**
				 * 显示或者隐藏缓冲提示
				 */
				bvv_live_fostercarefullscreen.showCacheInfo(true);
				/**
				 * 开始播放
				 */
				bvv_live_fostercarefullscreen.start();
				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fostercare_live_full_screen);
		init();
		initView();
		initLivePlayer();
	}

	private void initView() {
		iv_live_fostercarefullscreen_back = (ImageView) findViewById(R.id.iv_live_fostercarefullscreen_back);
		bvv_live_fostercarefullscreen = (BVideoView) findViewById(R.id.bvv_live_fostercarefullscreen);
		rl_live_fostercarefullscreen = (RelativeLayout) findViewById(R.id.rl_live_fostercarefullscreen);
		rl_live_fostercarefullscreen.bringToFront();
	}

	private void initLivePlayer() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
		// 设置AK
		BVideoView.setAKSK(AK, SK);
		bvv_live_fostercarefullscreen.setLogLevel(4);
		/**
		 * 注册listener
		 */
		bvv_live_fostercarefullscreen.setOnPreparedListener(this);
		bvv_live_fostercarefullscreen.setOnCompletionListener(this);
		bvv_live_fostercarefullscreen.setOnCompletionWithParamListener(this);
		bvv_live_fostercarefullscreen.setOnErrorListener(this);
		bvv_live_fostercarefullscreen.setOnInfoListener(this);
		/**
		 * 设置解码模式
		 */
		bvv_live_fostercarefullscreen.setDecodeMode(BVideoView.DECODE_SW);
		/**
		 * 开启后台事件处理线程
		 */
		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());
	}

	private void init() {
		Intent intent = getIntent();
		LiveUrl = intent.getStringExtra("LiveUrl");
		pDialog = new MProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (bvv_live_fostercarefullscreen.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercarefullscreen.getCurrentPosition();
			bvv_live_fostercarefullscreen.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		// 发起一次播放任务,当然您不一定要在这发起
		if (!bvv_live_fostercarefullscreen.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			bvv_live_fostercarefullscreen.resume();
		} else {
			mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
		// 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		if (bvv_live_fostercarefullscreen.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercarefullscreen.getCurrentPosition();
			bvv_live_fostercarefullscreen.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ((mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercarefullscreen.getCurrentPosition();
			bvv_live_fostercarefullscreen.stopPlayback();
		}
		/**
		 * 结束后台事件处理线程
		 */
		mHandlerThread.quit();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public void OnCompletionWithParam(int param) {
		Log.v(TAG, "OnCompletionWithParam=" + param);
	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
	}

	@Override
	public boolean onInfo(int what, int extra) {
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
	}
}
