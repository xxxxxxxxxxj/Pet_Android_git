package com.haotang.pet;

import java.util.GregorianCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.WheelPicker.ArrayWheelAdapter;
import com.haotang.pet.view.WheelPicker.OnWheelChangedListener;
import com.haotang.pet.view.WheelPicker.WheelView;

public class PetDataSwimActivity extends SuperActivity{

	private String[] months =null;
	private String[] WeekShow =null;
	private String [] dayTime = {"上午","下午"};
	private String [] dayTodayTime ={"下午"};
	private WheelView wv_month;
	private WheelView wv_time;
	private WheelView wv_day_time;
	private TextView tvTitle,choose_sure;
	private String currentDay = "";
	private int current=0;
	private ArrayWheelAdapter<String> monthAdapter;
	private ArrayWheelAdapter<String> timeAdapter;
	private ArrayWheelAdapter<String> dayTimeAdapter;
	private LinearLayout show_top;
	int key = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.petdateserviceseleter);
		initView();
		getIntentFrom();
		int defaultsize = (int) tvTitle.getTextSize();
		wv_month.setDefTextSize(defaultsize);
//		try {
//			for (int i = 0; i < months.length; i++) {
//				currentDay = months[0];
//				months[0]="今天";
//				if (i!=0) {
//					months[i] = months[i].substring(5, 10);
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		monthAdapter = new ArrayWheelAdapter<String>(months);
		wv_month.setAdapter(monthAdapter);
		wv_time.setDefTextSize(defaultsize);
		timeAdapter = new ArrayWheelAdapter<String>(WeekShow);
		wv_time.setAdapter(timeAdapter);
		
		GregorianCalendar ca = new GregorianCalendar();  
		key = ca.get(GregorianCalendar.AM_PM);
		Utils.mLogError("==-->AM_PM "+key);
		wv_day_time.setDefTextSize(defaultsize);
		if (wv_month.getCurrentItem()==0) {
			if (key==1) {
				dayTimeAdapter = new ArrayWheelAdapter<String>(dayTodayTime);
				wv_day_time.setAdapter(dayTimeAdapter);
			}else if (key==0) {
				dayTimeAdapter = new ArrayWheelAdapter<String>(dayTime);
				wv_day_time.setAdapter(dayTimeAdapter);
			}
		}else {
			dayTimeAdapter = new ArrayWheelAdapter<String>(dayTime);
			wv_day_time.setAdapter(dayTimeAdapter);
		}
		wv_month.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				current = wv_month.getCurrentItem();
				wv_time.setCurrentItem(current);
				if (wv_month.getCurrentItem()==0) {
					if (key==1) {
						dayTimeAdapter = new ArrayWheelAdapter<String>(dayTodayTime);
						wv_day_time.setAdapter(dayTimeAdapter);
					}
				}else {
					dayTimeAdapter = new ArrayWheelAdapter<String>(dayTime);
					wv_day_time.setAdapter(dayTimeAdapter);
				}
			}
		});
		wv_time.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				current = wv_time.getCurrentItem();
				wv_month.setCurrentItem(current);
			}
		});
//		wv_day_time.addChangingListener(new OnWheelChangedListener() {
//			
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				
//			}
//		});
		show_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		choose_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.SwimDetailActivity");
					intent.putExtra("index", 2);
					intent.putExtra("monthEvery", monthAdapter.getItem(wv_month.getCurrentItem()));
					intent.putExtra("weekEveryShow", timeAdapter.getItem(wv_time.getCurrentItem()));
					intent.putExtra("AMORPM", dayTimeAdapter.getItem(wv_day_time.getCurrentItem()));
					intent.putExtra("current", current);
					sendBroadcast(intent);
					finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					ToastUtil.showToastShortCenter(mContext,getResources().getString(R.string.errornotice));
					finish();
				}
			}
		});
	}
	private void getIntentFrom() {
		months = getIntent().getStringArrayExtra("months");
		WeekShow = getIntent().getStringArrayExtra("WeekShow");
	}
	private void initView() {
		wv_month = (WheelView) findViewById(R.id.wv_month);
		wv_time = (WheelView) findViewById(R.id.wv_time);
		wv_day_time = (WheelView) findViewById(R.id.wv_day_time);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		choose_sure = (TextView) findViewById(R.id.choose_sure);
		show_top = (LinearLayout) findViewById(R.id.show_top);
	}
}
