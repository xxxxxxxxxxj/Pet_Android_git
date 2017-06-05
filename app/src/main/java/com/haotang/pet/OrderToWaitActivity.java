package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.VieBeau;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderToWaitActivity extends SuperActivity implements OnClickListener{

	private LinearLayout layout_close;
	private ImageView img_close;
	private LinearLayout layout_img_title_icon;
	private ImageView img_img_title_icon;
	private LinearLayout layout_message;
	private TextView text_message;
	private LinearLayout layout_to_wait;
	private Button button_to_wait;
	private LinearLayout layout_all_close;
	private VieBeau mVieBeau = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_to_wait);
		initView();
		initListener();
		getIntentData();
		setView();
	}
	private void setView() {
		if (mVieBeau!=null) {
			text_message.setText(mVieBeau.title);
		}
	}
	private void getIntentData() {
		mVieBeau = (VieBeau) getIntent().getSerializableExtra("mVieBeau");
	}
	private void initListener() {
		layout_close.setOnClickListener(this);
		img_close.setOnClickListener(this);
		layout_to_wait.setOnClickListener(this);
		button_to_wait.setOnClickListener(this);
		layout_all_close.setOnClickListener(this);
	}
	private void initView() {
		layout_close = (LinearLayout) findViewById(R.id.layout_close);
		img_close = (ImageView) findViewById(R.id.img_close);
		layout_img_title_icon = (LinearLayout) findViewById(R.id.layout_img_title_icon);
		img_img_title_icon = (ImageView) findViewById(R.id.img_img_title_icon);
		layout_message = (LinearLayout) findViewById(R.id.layout_message);
		text_message = (TextView) findViewById(R.id.text_message);
		layout_to_wait = (LinearLayout) findViewById(R.id.layout_to_wait);
		button_to_wait = (Button) findViewById(R.id.button_to_wait);
		layout_all_close = (LinearLayout) findViewById(R.id.layout_all_close);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_close:
		case R.id.img_close:
		case R.id.layout_to_wait:
		case R.id.button_to_wait:
			finish();
			break;
		}
	}
}
