package com.haotang.pet;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.fragment.AppointmentFragment;
import com.haotang.pet.fragment.UrgentFragment;

@SuppressLint("NewApi")
public class SelectTimeOrUrgentActivity extends SuperActivity implements
		OnClickListener {
	public static SelectTimeOrUrgentActivity act;
	private ImageButton ibBack;
	private TextView tvTitle;
	private RelativeLayout rl_timeorurgent_yy;
	private RelativeLayout rl_timeorurgent_jj;
	private View vw_bottom_yy;
	private View vw_bottom_jj;
	private TextView tv_timeorurgent_yy;
	private TextView tv_timeorurgent_jj;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	/**
	 * 用于展示预约界面的Fragment
	 */
	private AppointmentFragment appointmentFragment;

	/**
	 * 用于展示加急界面的Fragment
	 */
	private UrgentFragment urgentFragment;
	private int vieEnabled;
	private LinearLayout ll_timeorurgent;
	private boolean isBack;
	private double lat;
	private double lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_time_or_urgent);
		act = this;
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		rl_timeorurgent_yy = (RelativeLayout) findViewById(R.id.rl_timeorurgent_yy);
		rl_timeorurgent_jj = (RelativeLayout) findViewById(R.id.rl_timeorurgent_jj);
		vw_bottom_yy = (View) findViewById(R.id.vw_bottom_yy);
		vw_bottom_jj = (View) findViewById(R.id.vw_bottom_jj);
		tv_timeorurgent_yy = (TextView) findViewById(R.id.tv_timeorurgent_yy);
		tv_timeorurgent_jj = (TextView) findViewById(R.id.tv_timeorurgent_jj);
		ll_timeorurgent = (LinearLayout) findViewById(R.id.ll_timeorurgent);
		tvTitle.setText("预约时间");
		fragmentManager = getFragmentManager();
		ibBack.setOnClickListener(this);
		rl_timeorurgent_yy.setOnClickListener(this);
		rl_timeorurgent_jj.setOnClickListener(this);
		Intent intent = getIntent();
		vieEnabled = intent.getIntExtra("vieEnabled", 0);
		lat = intent.getDoubleExtra("lat", 0);
		lng = intent.getDoubleExtra("lng", 0);
		// 第一次启动时选中第0个tab
		setTabSelection(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			isBack();
			break;
		case R.id.rl_timeorurgent_yy:
			setTabSelection(0);
			break;
		case R.id.rl_timeorurgent_jj:
			setTabSelection(1);
			break;
		default:
			break;
		}
	}

	private void isBack() {
		if (appointmentFragment.isBack) {
			goBack();
		} else {
			if(isBack){
				goBack();
			}else{
				setTabSelection(0);
			}
		}
	}

	private void goBack() {
		finishWithAnimation();
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示首页，1表示充电，2表示我的。
	 */
	public void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			isBack = true;
			tv_timeorurgent_yy.setTextColor(getResources().getColor(
					R.color.orange));
			tv_timeorurgent_jj.setTextColor(getResources().getColor(
					R.color.a333333));
			vw_bottom_yy.setVisibility(View.VISIBLE);
			vw_bottom_jj.setVisibility(View.GONE);
			if (appointmentFragment == null) {
				// 如果appointmentFragment为空，则创建一个并添加到界面上
				appointmentFragment = new AppointmentFragment(this, vieEnabled,lat,lng);
				transaction.add(R.id.timeorurgent_content, appointmentFragment);
			} else {
				// 如果appointmentFragment不为空，则直接将它显示出来
				transaction.show(appointmentFragment);
			}
			tvTitle.setText("预约时间");
			break;
		case 1:
			isBack = false;
			tv_timeorurgent_yy.setTextColor(getResources().getColor(
					R.color.a333333));
			tv_timeorurgent_jj.setTextColor(getResources().getColor(
					R.color.orange));
			vw_bottom_jj.setVisibility(View.VISIBLE);
			vw_bottom_yy.setVisibility(View.GONE);
			if (urgentFragment == null) {
				// 如果urgentFragment为空，则创建一个并添加到界面上
				urgentFragment = new UrgentFragment(this);
				transaction.add(R.id.timeorurgent_content, urgentFragment);
			} else {
				// 如果urgentFragment不为空，则直接将它显示出来
				transaction.show(urgentFragment);
			}
			tvTitle.setText("即时预约");
			break;
		}
		transaction.commit();
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (appointmentFragment != null) {
			transaction.hide(appointmentFragment);
		}
		if (urgentFragment != null) {
			transaction.hide(urgentFragment);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			isBack();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}
