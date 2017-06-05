package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SystemBarTint;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class StartPageActivity extends SuperActivity implements OnClickListener {
	// 引导界面的图片
	private ImageView iv_landingpage;
	private Button btn_landing_tg;
	private String img_url;
	private String jump_url;
	private TimeCount time;
	private SystemBarTint mtintManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page);
		mtintManager = new SystemBarTint(this);
		setStatusBarColor(Color.parseColor("#ff0099cc"));
		Utils.hideBottomUIMenu(mContext);
		time = new TimeCount(3000, 1000);// 构造CountDownTimer对象
		Intent intent = getIntent();
		img_url = intent.getStringExtra("img_url");
		jump_url = intent.getStringExtra("jump_url");
		iv_landingpage = (ImageView) findViewById(R.id.iv_landingpage);
		btn_landing_tg = (Button) findViewById(R.id.btn_landing_tg);
		btn_landing_tg.setOnClickListener(this);
		iv_landingpage.setOnClickListener(this);
		ImageLoaderUtil.loadImg(img_url, iv_landingpage, 0,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						btn_landing_tg.setVisibility(View.VISIBLE);
						time.start();
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
	}

	protected void setStatusBarColor(int colorBurn) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = setImmerseLayout();
			window.setStatusBarColor(colorBurn);
		} else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (mtintManager != null) {
				mtintManager.setStatusBarTintEnabled(true);
				mtintManager.setStatusBarTintColor(colorBurn);
			}
		}
	}

	// 注册计时器
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			goNext(MainActivity.class, 0);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_landing_tg.setText("跳过  " + millisUntilFinished / 1000);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (time != null)
			time.cancel();
	}

	private void goNext(Class clazz, int previous) {
		time.cancel();
		Intent intent = new Intent(this, clazz);
		intent.putExtra("url", jump_url);
		intent.putExtra("previous", previous);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_landing_tg:
			goNext(MainActivity.class, 0);
			break;
		case R.id.iv_landingpage:
			if (jump_url != null && !TextUtils.isEmpty(jump_url)) {
				startActivity(new Intent(this, MainActivity.class));
				goNext(ADActivity.class, 0);
			}
			break;
		default:
			break;
		}
	}
}
