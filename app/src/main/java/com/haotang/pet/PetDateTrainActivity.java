package com.haotang.pet;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.TrainData;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.WheelPicker.ArrayWheelAdapter;
import com.haotang.pet.view.WheelPicker.OnWheelChangedListener;
import com.haotang.pet.view.WheelPicker.WheelView;

public class PetDateTrainActivity extends SuperActivity{
	private TextView tvTitle;
	private TextView choose_sure;
	private WheelView v_data;
	private WheelView v_week;
	private WheelView wv_day_time;
	private String [] times = null;
	private String [] Weeks = null;
	private ArrayList<TrainData> arrayList;
	private ArrayWheelAdapter<String> Adapter;
	private ArrayWheelAdapter<String> WeekAdapter;
	private LinearLayout show_top;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.petdateserviceseleter);
		getIntentData();
		initView();
		setView();
	}
	private void getIntentData() {
		// TODO Auto-generated method stub
//		arrayList = getIntent().getStringArrayExtra("times");
		arrayList = getIntent().getParcelableArrayListExtra("times");
		times = new String[arrayList.size()];
		Weeks = new String[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			times[i]=arrayList.get(i).date.trim();
			Weeks[i] = arrayList.get(i).week;
		}
		try {
			times[0]=arrayList.get(0).date;
			times[1]=arrayList.get(1).date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setView() {
//		mPDialog.showDialog();
		
		int defaultsize = (int) tvTitle.getTextSize();
		v_data.setDefTextSize(defaultsize);
		v_week.setDefTextSize(defaultsize);
		
		Adapter = new ArrayWheelAdapter<String>(times);
		v_data.setAdapter(Adapter);
		v_data.setCurrentItem(0);
		WeekAdapter = new ArrayWheelAdapter<String>(Weeks);
		v_week.setAdapter(WeekAdapter);
		v_week.setCurrentItem(0);
		
		v_data.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int current = v_data.getCurrentItem();
				v_week.setCurrentItem(current);
			}
		});
		v_week.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int current = v_week.getCurrentItem();
				v_data.setCurrentItem(current);
			}
		});
		choose_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("apptime", arrayList.get(v_data.getCurrentItem()));
				intent.putExtra("current", v_data.getCurrentItem());
				Utils.mLogError("==-->apptime0:= "+arrayList.get(v_data.getCurrentItem()).appointment);
				setResult(Global.RESULT_OK, intent);
				finish();
			}
		});
		show_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(Global.RESULT_OK);
				finish();
			}
		});
	}
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		choose_sure = (TextView) findViewById(R.id.choose_sure);
		v_data = (WheelView) findViewById(R.id.wv_month);
		v_week = (WheelView) findViewById(R.id.wv_time);
		wv_day_time = (WheelView) findViewById(R.id.wv_day_time);
		show_top = (LinearLayout) findViewById(R.id.show_top);
		
//		wv_time.setVisibility(View.GONE);
		wv_day_time.setVisibility(View.GONE);
	}
}
