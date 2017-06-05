package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;

public class MainAcPage extends SuperActivity{

	private ImageView show_main_img;
	private ImageView imageView_close;
	
	private String activityPic="";
	private String activityUrl="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity);
		show_main_img = (ImageView) findViewById(R.id.show_main_img);
		imageView_close = (ImageView) findViewById(R.id.imageView_close);
		
		activityPic = getIntent().getStringExtra("activityPic");
		activityUrl = getIntent().getStringExtra("activityUrl");
		ImageLoaderUtil.setImage(show_main_img, activityPic, 0);
		show_main_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(ADActivity.class, 0);
			}
		});
		imageView_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", activityUrl);
		intent.putExtra("previous", pre);
		startActivity(intent);
	}
}
