package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ImageLoaderUtil;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class IsCanPickActivity extends SuperActivity implements OnClickListener{

	private ImageView imageView_show_img;
	private ImageView imageView_close;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_is_show_pick);
		initView();
	}
	private void initView() {
		imageView_show_img = (ImageView) findViewById(R.id.imageView_show_img);
		imageView_close = (ImageView) findViewById(R.id.imageView_close);
		
		imageView_show_img.setOnClickListener(this);
		imageView_close.setOnClickListener(this);
		
		int index = getIntent().getIntExtra("index", 1);
		url = getIntent().getStringExtra("url");
		if (index==1) {
			imageView_show_img.setBackgroundResource(R.drawable.normal_notice);
		}else if (index==2) {
			imageView_show_img.setBackgroundResource(R.drawable.foster_notice);
		}else if (index==3) {
			ImageLoaderUtil.setImage(imageView_show_img, url, R.drawable.train_desc_img);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView_close:
		case R.id.imageView_show_img:
			finish();
			break;
		}
	}
}
