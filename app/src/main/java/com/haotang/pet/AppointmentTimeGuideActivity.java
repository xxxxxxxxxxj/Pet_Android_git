package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class AppointmentTimeGuideActivity extends SuperActivity {
	private SharedPreferenceUtil spUtil;
	private RelativeLayout llMain;
	private ImageView ivTop;
	private ImageView ivBottom;
	private int locationy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.appointmenttimeguide);
		
		spUtil = SharedPreferenceUtil.getInstance(this);
		
		llMain = (RelativeLayout) findViewById(R.id.ll_appointmenttimeguide_main);
		ivTop = (ImageView) findViewById(R.id.iv_appointmenttimeguide_top);
		ivBottom = (ImageView) findViewById(R.id.iv_appointmenttimeguide_bottom);
		locationy = getIntent().getIntExtra("locationy", 230);
		ivTop.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		ivTop.setY(locationy+25);
		int height = ivTop.getMeasuredHeight();
		ivBottom.setY(locationy+height+60);
		llMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spUtil.saveBoolean("timeguide", true);
				finishWithAnimation();
			}
		});
	}
}
