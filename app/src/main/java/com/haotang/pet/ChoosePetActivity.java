package com.haotang.pet;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.fragment.ChooseCatFragment;
import com.haotang.pet.fragment.ChooseDogFragment;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class ChoosePetActivity extends FragmentActivity implements OnClickListener{
	private ImageView imageView_choose_pet_back;
	private TextView textview_choose_dog,textview_choose_cat;
	private ChoosePetActivity choosePetActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_pet);
		choosePetActivity = this;
		initView();
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_choose_list, ChooseDogFragment.getChooseDogFragment()).commit();
		setDogChooseBack();
		isShowOverBurden();
	}

	private void isShowOverBurden() {
		if (SharedPreferenceUtil.getInstance(choosePetActivity).getBoolean("isShowOverBurden", true)) {
			Intent intent = new Intent(this, OverburdenActivity.class);
			startActivity(intent);
		}
	}

	private void setDogChooseBack() {
		textview_choose_dog.setTextColor(getResources().getColor(R.color.white));
		textview_choose_cat.setTextColor(getResources().getColor(R.color.a555555));
		textview_choose_dog.setBackgroundResource(R.drawable.bg_member_left_passed);
		textview_choose_cat.setBackgroundResource(R.drawable.bg_member_right_normal);
	}
	private void setCatChooseBack() {
		textview_choose_dog.setTextColor(getResources().getColor(R.color.a555555));
		textview_choose_cat.setTextColor(getResources().getColor(R.color.white));
		textview_choose_dog.setBackgroundResource(R.drawable.bg_member_left_normal);
		textview_choose_cat.setBackgroundResource(R.drawable.bg_member_right_passed);
	}

	private void initView() {
		imageView_choose_pet_back = (ImageView) findViewById(R.id.imageView_choose_pet_back);
		textview_choose_dog = (TextView) findViewById(R.id.textview_choose_dog);
		textview_choose_cat = (TextView) findViewById(R.id.textview_choose_cat);
		imageView_choose_pet_back.setOnClickListener(this);
		textview_choose_dog.setOnClickListener(this);
		textview_choose_cat.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO 处理监听
		switch (view.getId()) {
		case R.id.imageView_choose_pet_back://back
			finish();
			break;
		case R.id.textview_choose_dog://choose dog
			getSupportFragmentManager().beginTransaction().replace(R.id.layout_choose_list, ChooseDogFragment.getChooseDogFragment()).commit();
			setDogChooseBack();
			break;
		case R.id.textview_choose_cat://choose cat
			getSupportFragmentManager().beginTransaction().replace(R.id.layout_choose_list, ChooseCatFragment.getChooseCatFragment()).commit();
			setCatChooseBack();
			break;
		default:
			break;
		}
	}
}
