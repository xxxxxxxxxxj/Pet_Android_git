package com.haotang.pet;

import java.util.Timer;
import java.util.TimerTask;

import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EvaluateOverActivity extends Activity{
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Timer timer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate_over_activity);
		initView();
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		ib_titlebar_back.setVisibility(View.GONE);
		tv_titlebar_title.setText("评价完成");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous", Global.PRE_EVALUATEOVER_BACK_MAIN);
				sendBroadcast(intent);
				try {
					if(OrderDetailFromOrderToConfirmActivity.orderConfirm!=null){
						OrderDetailFromOrderToConfirmActivity.orderConfirm.finish();
					}
					if (OrderFosterDetailActivity.fosterDetailActivity!=null) {
						OrderFosterDetailActivity.fosterDetailActivity.finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
				timer.cancel();
			}
		};
		timer.schedule(task, 2000, 2000);
	}
}
