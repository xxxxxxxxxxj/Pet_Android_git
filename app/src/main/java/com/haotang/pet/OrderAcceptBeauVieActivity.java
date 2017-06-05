package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.VieBeau;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.SelectableRoundedImageView;

public class OrderAcceptBeauVieActivity extends SuperActivity implements OnClickListener{

	private LinearLayout layout_close;
	private ImageView img_close;
	private TextView text_beau_detail;
	private SelectableRoundedImageView imageView_vie_beau_icon;
	private TextView text_beau_name;
	private TextView textview_beau_level;
	private TextView textview_beau_order_num;
	private VieBeau mVieBeau = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_accept_beau_vie);
		initView();
		getIntentData();
		initListener();
		setView();
	}
	private void setView() {
		if (mVieBeau!=null) {
			text_beau_name.setText(mVieBeau.realName);
			textview_beau_level.setText(mVieBeau.levelName);
			textview_beau_order_num.setText("接单："+mVieBeau.totalOrderAmount);
			text_beau_detail.setText(mVieBeau.title);
			try {
				ImageLoaderUtil.setImage(imageView_vie_beau_icon, mVieBeau.avatar, R.drawable.icon_image_default);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void getIntentData() {
		mVieBeau = (VieBeau) getIntent().getSerializableExtra("mVieBeau");
	}
	private void initListener() {
		layout_close.setOnClickListener(this);
		img_close.setOnClickListener(this);
	}
	private void initView() {
		layout_close = (LinearLayout) findViewById(R.id.layout_close);
		img_close = (ImageView) findViewById(R.id.img_close);
		text_beau_detail = (TextView) findViewById(R.id.text_beau_detail);
		imageView_vie_beau_icon = (SelectableRoundedImageView) findViewById(R.id.imageView_vie_beau_icon);
		text_beau_name = (TextView) findViewById(R.id.text_beau_name);
		textview_beau_level = (TextView) findViewById(R.id.textview_beau_level);
		textview_beau_order_num = (TextView) findViewById(R.id.textview_beau_order_num);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_close:
		case R.id.img_close:
			finish();
			break;
		}
	}
}
