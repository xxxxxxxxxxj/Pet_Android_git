package com.haotang.pet;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.StartPageBean;
import com.haotang.pet.entity.StartPageBean.StartPage;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ChannelUtil;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SystemBarTint;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class FlashActivity extends SuperActivity {
	private SharedPreferenceUtil spUtil;
	private String cellphone;
	private boolean guide;
	private SystemBarTint mtintManager;
	protected String img_url;
	protected String jump_url;
	private Timer timer;
	private TimerTask task;
	private static int FLASH_DELAYEDTIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mtintManager = new SystemBarTint(this);
		setStatusBarColor(Color.parseColor("#ff0099cc"));
		Utils.hideBottomUIMenu(mContext);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// 结束你的activity
			finish();
			return;
		}
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.flash);
		MobclickAgent.setDebugMode(true);
		// 禁止默认统计方式
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		cellphone = spUtil.getString("cellphone", "");
		guide = spUtil.getBoolean("guide", false);
		spUtil.saveBoolean("isExit", true);
		spUtil.saveBoolean("isRestart", true);
		spUtil.saveBoolean("PETINFODELAYEDGONE", true);
		/*spUtil.removeData("newaddrid");
		spUtil.removeData("newaddr");
		spUtil.removeData("newlat");
		spUtil.removeData("newlng");
		spUtil.removeData("isAccept");
		spUtil.removeData("shopid");
		spUtil.removeData("newshopid");
		spUtil.removeData("charactservice");
		spUtil.removeData("invitecode");*/
		AnalyticsConfig.setChannel(ChannelUtil.getChannel(this));
		if (guide) {
			getData();
		} else {
			initTimer(0);
		}
	}

	private void initTimer(final int flag) {
		task = new TimerTask() {
			@Override
			public void run() {
				if (guide) {
					if(flag == 1){
						goNext(StartPageActivity.class);
					}else{
						goNext(MainActivity.class);
					}
				} else {
					goNext(GuideActivity.class);
				}
			}
		};
		timer = new Timer();
		timer.schedule(task, FLASH_DELAYEDTIME);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
		if (task != null)
			task.cancel();
	}

	private void getData() {
		CommUtil.startPage(cellphone, mContext, startPageHandler);
	}

	private AsyncHttpResponseHandler startPageHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("启动页：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						Gson gson = new Gson();
						StartPageBean data = gson.fromJson(jData.toString(),
								StartPageBean.class);
						if (data != null) {
							List<StartPage> spcList = data.getSpcList();
							if (spcList != null && spcList.size() > 0) {
								StartPage startPage = spcList.get(0);
								if (startPage != null) {
									img_url = startPage.getImg_url();
									jump_url = startPage.getJump_url();
									if (img_url != null
											&& !TextUtils.isEmpty(img_url)) {
										initTimer(1);
									} else {
										initTimer(0);
									}
								} else {
									initTimer(0);
								}
							} else {
								initTimer(0);
							}
						} else {
							initTimer(0);
						}
					} else {
						initTimer(0);
					}
				} else {
					initTimer(0);
				}
			} catch (Exception e) {
				initTimer(0);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			initTimer(0);
		}
	};

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

	private void goNext(Class clazz) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra("img_url", img_url);
		intent.putExtra("jump_url", jump_url);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("FlashActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FlashActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
}
