package com.haotang.base;

import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class SuperActivity extends Activity {
	public Activity mContext;
	protected int statusBarHeight;
	private View viewStatusBarTop;
	private RelativeLayout rl_superactivity;
	public SharedPreferenceUtil spUtil;
	public MProgressDialog mPDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		spUtil = SharedPreferenceUtil.getInstance(this);
		mContext = this;
		mPDialog = new MProgressDialog(mContext);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * int nDir = getIntent().getIntExtra(Global.ANIM_DIRECTION(), -1); if
		 * (nDir == Global.ANIM_COVER_FROM_LEFT())
		 * overridePendingTransition(R.anim.left_in, R.anim.right_out); else if
		 * (nDir == Global.ANIM_COVER_FROM_RIGHT())
		 * overridePendingTransition(R.anim.right_in, R.anim.left_out); else
		 * overridePendingTransition(0, 0);
		 */
	}

	public void finishWithAnimation() {

		SuperActivity.this.finish();
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected Window setImmerseLayout() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		statusBarHeight = getStatusBarHeight(this.getBaseContext());
		return window;
	}

	public int getStatusBarHeight(Context context) {
		if (statusBarHeight != 0)
			return statusBarHeight;

		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = context.getResources().getDimensionPixelSize(
					resourceId);
		}
		return statusBarHeight;
	}
}
