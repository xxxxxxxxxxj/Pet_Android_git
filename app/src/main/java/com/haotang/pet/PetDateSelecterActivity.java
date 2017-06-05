package com.haotang.pet;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.WheelPicker.ArrayWheelAdapter;
import com.haotang.pet.view.WheelPicker.OnWheelChangedListener;
import com.haotang.pet.view.WheelPicker.WheelView;

public class PetDateSelecterActivity extends SuperActivity {
	private View rlMain;
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wv_day;
	private Button btSave;
	private TextView tvTitle;
	private RelativeLayout rlDate;
	private int startyear;
	private int endyear;
	private int selectedyear;
	private int selectedmonth;
	private ImageButton ibClose;
	private static final String[] months = 
		{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
	private String[] years = new String[20];
	private String [] days = null;
	private String yearStr = null;
	private String monthStr = null;
	private ArrayWheelAdapter<String> dayTimeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.petdateseleter);
		
		findView();
		setView();
	}

	private void findView() {
		rlMain = findViewById(R.id.view_petdateselecter_bg);
		wvYear = (WheelView) findViewById(R.id.wv_year);
		wvMonth = (WheelView) findViewById(R.id.wv_month);
		wv_day = (WheelView) findViewById(R.id.wv_day);
		btSave = (Button) findViewById(R.id.bt_petdateselecter_ok);
		tvTitle = (TextView) findViewById(R.id.tv_petdateselecter_title);
		rlDate = (RelativeLayout) findViewById(R.id.rl_petdateselecter_date);
		ibClose = (ImageButton) findViewById(R.id.ib_petdateselecter_close);
	}

	private void setView() {
		// TODO Auto-generated method stub
		selectedmonth = getIntent().getIntExtra("monthposition", 0);
		selectedyear = getIntent().getIntExtra("yearposition", 0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		endyear = calendar.get(Calendar.YEAR);
		if(selectedmonth==0)
			selectedmonth = calendar.get(Calendar.MONTH);
		if(selectedyear==0)
			selectedyear=19;
		startyear = endyear-19;
		for(int i=startyear;i<=endyear;i++){
			years[i-startyear]=i+"年";
		}
		int defaultsize = (int) tvTitle.getTextSize();
		wvYear.setDefTextSize(defaultsize);
		wvYear.setAdapter(new ArrayWheelAdapter<String>(years));
//		wvYear.setLabel("年");
		wvYear.setCyclic(true);
		wvMonth.setDefTextSize(defaultsize);
//		wvMonth.setAdapter(new NumericWheelAdapter(1, 12));
		wvMonth.setAdapter(new ArrayWheelAdapter<String>(months));
//		wvMonth.setLabel("月");
		wvMonth.setCyclic(true);
		
		wvYear.setCurrentItem(selectedyear);
		wvMonth.setCurrentItem(selectedmonth);
		yearStr = years[selectedyear];
		monthStr = months[selectedmonth];
		int countDay = Utils.getMonthLastDay(Integer.parseInt(yearStr.replace("年", "")), Integer.parseInt(monthStr.replace("月", "")));
		days = new String[countDay];
		for (int i = 0; i < days.length; i++) {
			days[i]=(i+1)+"日";
		}
		
		wv_day.setDefTextSize(defaultsize);
		
//		dayTimeAdapter = new ArrayWheelAdapter<String>(days);
		wv_day.setAdapter(new ArrayWheelAdapter<String>(days));
		wv_day.setCyclic(true);
		wv_day.setCurrentItem(0);
		wvYear.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int yearCurrent = wvYear.getCurrentItem();
				yearStr = years[yearCurrent];
				int countDay = Utils.getMonthLastDay(Integer.parseInt(yearStr.replace("年", "")), Integer.parseInt(monthStr.replace("月", "")));
				days = null;
				days = new String[countDay];
//				dayTimeAdapter = new ArrayWheelAdapter<String>(days);
				for (int i = 0; i < days.length; i++) {
					days[i]=(i+1)+"日";
				}
				wv_day.setAdapter(new ArrayWheelAdapter<String>(days));
				wv_day.setCurrentItem(0);
			}
		});
		wvMonth.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int monthCurrent = wvMonth.getCurrentItem();
				monthStr = months[monthCurrent];
				int countDay = Utils.getMonthLastDay(Integer.parseInt(yearStr.replace("年", "")), Integer.parseInt(monthStr.replace("月", "")));
				days = null;
				days = new String[countDay];
				for (int i = 0; i < days.length; i++) {
					days[i]=(i+1)+"日";
				}
//				dayTimeAdapter = new ArrayWheelAdapter<String>(days);
				wv_day.setAdapter(new ArrayWheelAdapter<String>(days));
				wv_day.setCurrentItem(0);
			}
		});
		rlMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PetDateSelecterActivity.this.finishWithAnimation();
			}
		});
		ibClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PetDateSelecterActivity.this.finishWithAnimation();
			}
		});
		rlDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		btSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				data.putExtra("year", startyear+wvYear.getCurrentItem());
				data.putExtra("month", Integer.parseInt(months[wvMonth.getCurrentItem()].replace("月", "")));
				data.putExtra("day", Integer.parseInt(days[wv_day.getCurrentItem()].replace("日", "")));
				data.putExtra("monthposition", wvMonth.getCurrentItem());
				data.putExtra("yearposition", wvYear.getCurrentItem());
				data.putExtra("dayposition", wv_day.getCurrentItem());
				Utils.mLogError("==--> cccccc "+ startyear+wvYear.getCurrentItem()
						+"  ---->ddd   "+wvMonth.getCurrentItem());
				setResult(RESULT_OK, data);
				PetDateSelecterActivity.this.finishWithAnimation();
			}
		});
		
	}
}
