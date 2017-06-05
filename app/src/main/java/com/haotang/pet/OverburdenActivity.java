package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.SharedPreferenceUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

public class OverburdenActivity extends SuperActivity{

	private RelativeLayout layout_show_notice;
	private OverburdenActivity overburdenActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_over_burden);
		overburdenActivity = this;
		layout_show_notice = (RelativeLayout) findViewById(R.id.layout_show_notice);
		layout_show_notice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferenceUtil.getInstance(overburdenActivity).saveBoolean("isShowOverBurden", false);
				finishWithAnimation();
			}
		});
	}
}
