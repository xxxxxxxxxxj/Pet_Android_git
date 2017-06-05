package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

public class GrowthActivity extends SuperActivity{

	private LinearLayout layout_growth_setback;
	private TextView textView_title;
	private TextView textView_zengsong_grwoth;
	private Button button_next;
	private ImageView imageView_close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_growth);
		layout_growth_setback = (LinearLayout) findViewById(R.id.layout_growth_setback);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_zengsong_grwoth = (TextView) findViewById(R.id.textView_zengsong_grwoth);
		button_next = (Button) findViewById(R.id.button_next);
		imageView_close = (ImageView) findViewById(R.id.imageView_close);
		
		String money = getIntent().getStringExtra("money");
		Utils.mLogError("==-->赠送的成长值"+money);
		textView_zengsong_grwoth.setText(money);
		button_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendToMainActivity();
			}
		});
		imageView_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void sendToMainActivity(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.GROWTH_TO_USERMEMBERFRAGMENT);
		sendBroadcast(intent);
		try {
			MyLastMoney.lastMoney.finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
}
