package com.haotang.pet;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Utils;

public class TrainGuideActivity extends SuperActivity{
	private LinearLayout layout_train_guide_top;
	private LinearLayout linearLayout_rain_guide_tel;
	private TextView textview_train_guide_tel;
	private LinearLayout layout_bottom;
	private LinearLayout layout_train_guide_click;
	private String tel ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_train_guide);
		getIntentData();
		initView();
		initListener();
	}

	private void initListener() {
		layout_bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TrainAppointMentActivity.showImg(true);
				spUtil.saveBoolean("isTrainFirst", false);
				finish();
			}
		});
		layout_train_guide_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TrainAppointMentActivity.showImg(true);
				spUtil.saveBoolean("isTrainFirst", false);
				finish();
			}
		});
	}

	private void initView() {
		layout_train_guide_top = (LinearLayout) findViewById(R.id.layout_train_guide_top);
		linearLayout_rain_guide_tel = (LinearLayout) findViewById(R.id.linearLayout_rain_guide_tel);
		textview_train_guide_tel = (TextView) findViewById(R.id.textview_train_guide_tel);
		layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
		layout_train_guide_click = (LinearLayout) findViewById(R.id.layout_train_guide_click);
		textview_train_guide_tel.setText(tel);
	}
	
	private void getIntentData() {
		tel = getIntent().getStringExtra("tel");
	}

	@Override
	protected void onResume() {
		super.onResume();
		textview_train_guide_tel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		textview_train_guide_tel.getPaint().setAntiAlias(true);
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*243/375);
			params.topMargin=Utils.dip2px(mContext, 50);
			layout_train_guide_top.setLayoutParams(params);

		}
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*243/375);
//		Utils.mLogError("==-->Utils.getDisplayMetrics(mContext)[0] 1 "+Utils.getDisplayMetrics(mContext)[0]*243/375);
//		layout_train_guide_top.setLayoutParams(params);
//		Utils.mLogError("==-->height1 "+layout_train_guide_top.getHeight());
//		linearLayout_rain_guide_tel.setX(XY[0]);
//		linearLayout_rain_guide_tel.setY(XY[1]);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			TrainAppointMentActivity.showImg(true);
			spUtil.saveBoolean("isTrainFirst", false);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
