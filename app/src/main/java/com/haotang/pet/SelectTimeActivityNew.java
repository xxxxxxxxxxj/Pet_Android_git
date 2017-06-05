package com.haotang.pet;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.MDay;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class SelectTimeActivityNew extends SuperActivity implements OnClickListener{
	private ImageButton ibBack;
	private TextView tvTitle;
	private RelativeLayout rlTime1_1;
	private TextView tvTime1_1;
	private TextView tvTimeHint1_1;
	private RelativeLayout rlTime1_2;
	private TextView tvTime1_2;
	private TextView tvTimeHint1_2;
	private RelativeLayout rlTime1_3;
	private TextView tvTime1_3;
	private TextView tvTimeHint1_3;
	private RelativeLayout rlTime2_1;
	private TextView tvTime2_1;
	private TextView tvTimeHint2_1;
	private RelativeLayout rlTime2_2;
	private TextView tvTime2_2;
	private TextView tvTimeHint2_2;
	private RelativeLayout rlTime2_3;
	private TextView tvTime2_3;
	private TextView tvTimeHint2_3;
	private RelativeLayout rlTime3_1;
	private TextView tvTime3_1;
	private TextView tvTimeHint3_1;
	private RelativeLayout rlTime3_2;
	private TextView tvTime3_2;
	private TextView tvTimeHint3_2;
	private RelativeLayout rlTime3_3;
	private TextView tvTime3_3;
	private TextView tvTimeHint3_3;
	private RelativeLayout rlTime4_1;
	private TextView tvTime4_1;
	private TextView tvTimeHint4_1;
	private RelativeLayout rlTime4_2;
	private TextView tvTime4_2;
	private TextView tvTimeHint4_2;
	private RelativeLayout rlTime4_3;
	private TextView tvTime4_3;
	private TextView tvTimeHint4_3;
	private RelativeLayout rlTime5_1;
	private TextView tvTime5_1;
	private TextView tvTimeHint5_1;
	private RelativeLayout rlTime5_2;
	private TextView tvTime5_2;
	private TextView tvTimeHint5_2;
	private RelativeLayout rlTime5_3;
	private TextView tvTime5_3;
	private TextView tvTimeHint5_3;
	private RelativeLayout rlTime6_1;
	private TextView tvTime6_1;
	private TextView tvTimeHint6_1;
	private RelativeLayout rlTime6_2;
	private TextView tvTime6_2;
	private TextView tvTimeHint6_2;
	private RelativeLayout rlTime6_3;
	private TextView tvTime6_3;
	private TextView tvTimeHint6_3;
	private RelativeLayout rlTime7_1;
	private TextView tvTime7_1;
	private TextView tvTimeHint7_1;
	private RelativeLayout rlTime7_2;
	private TextView tvTime7_2;
	private TextView tvTimeHint7_2;
	private RelativeLayout rlTime7_3;
	private TextView tvTime7_3;
	private TextView tvTimeHint7_3;
	private RelativeLayout rlTime8_1;
	private TextView tvTime8_1;
	private TextView tvTimeHint8_1;
	private RelativeLayout rlTime8_2;
	private TextView tvTime8_2;
	private TextView tvTimeHint8_2;
	private RelativeLayout rlTime8_3;
	private TextView tvTime8_3;
	private TextView tvTimeHint8_3;
	private Calendar calendar;
	
	private String selectedDate="";
	private String selectedTime="";
	private int currentHour;
	private int currentMinute;
	private MProgressDialog pDialog;
	private Intent fIntent;
	private int serviceid;
	private int shopid;
	private int areaid;
//	private double lat;
//	private double lng;
//	private int year;
	private String day1;
	private String day2;
	private String day3;
	private String day4;
	private String day5;
	private String day6;
	private String day7;
	private String date="";
	private boolean isTimeAvailable=true;
	private LayoutInflater mInflater;
	private int[][] times = new int[31][24];
	private ArrayList<MDay> daylist = new ArrayList<MDay>();
	private int[] daystatus = new int[31];
	
	private int screenwidth;
	private int itemwidth;
	private int currentday;
	private int dayposition;
	private int beauticianId;
	private SharedPreferenceUtil spUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecttime_new);
		
		findView();
		setView();
		
	}

	private void findView() {
		pDialog = new MProgressDialog(this);
		calendar = Calendar.getInstance();
		mInflater = LayoutInflater.from(this);
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		tvDayNum = (TextView) findViewById(R.id.tv_selecttime_daynum);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_selecttime_scrollview);
		llDaysContent = (LinearLayout) findViewById(R.id.ll_selecttime_content);
		
		rlTime1_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time1_1);
		tvTime1_1 = (TextView) findViewById(R.id.tv_selecttime_time1_1);
		tvTimeHint1_1 = (TextView) findViewById(R.id.tv_selecttime_timehint1_1);
		
		rlTime1_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time1_2);
		tvTime1_2 = (TextView) findViewById(R.id.tv_selecttime_time1_2);
		tvTimeHint1_2 = (TextView) findViewById(R.id.tv_selecttime_timehint1_2);
		
		rlTime1_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time1_3);
		tvTime1_3 = (TextView) findViewById(R.id.tv_selecttime_time1_3);
		tvTimeHint1_3 = (TextView) findViewById(R.id.tv_selecttime_timehint1_3);
		
		rlTime2_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time2_1);
		tvTime2_1 = (TextView) findViewById(R.id.tv_selecttime_time2_1);
		tvTimeHint2_1 = (TextView) findViewById(R.id.tv_selecttime_timehint2_1);
		
		rlTime2_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time2_2);
		tvTime2_2 = (TextView) findViewById(R.id.tv_selecttime_time2_2);
		tvTimeHint2_2 = (TextView) findViewById(R.id.tv_selecttime_timehint2_2);
		
		rlTime2_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time2_3);
		tvTime2_3 = (TextView) findViewById(R.id.tv_selecttime_time2_3);
		tvTimeHint2_3 = (TextView) findViewById(R.id.tv_selecttime_timehint2_3);
		
		rlTime3_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time3_1);
		tvTime3_1 = (TextView) findViewById(R.id.tv_selecttime_time3_1);
		tvTimeHint3_1 = (TextView) findViewById(R.id.tv_selecttime_timehint3_1);
		
		rlTime3_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time3_2);
		tvTime3_2 = (TextView) findViewById(R.id.tv_selecttime_time3_2);
		tvTimeHint3_2 = (TextView) findViewById(R.id.tv_selecttime_timehint3_2);
		
		rlTime3_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time3_3);
		tvTime3_3 = (TextView) findViewById(R.id.tv_selecttime_time3_3);
		tvTimeHint3_3 = (TextView) findViewById(R.id.tv_selecttime_timehint3_3);
		
		rlTime4_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time4_1);
		tvTime4_1 = (TextView) findViewById(R.id.tv_selecttime_time4_1);
		tvTimeHint4_1 = (TextView) findViewById(R.id.tv_selecttime_timehint4_1);
		
		rlTime4_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time4_2);
		tvTime4_2 = (TextView) findViewById(R.id.tv_selecttime_time4_2);
		tvTimeHint4_2 = (TextView) findViewById(R.id.tv_selecttime_timehint4_2);
		
		rlTime4_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time4_3);
		tvTime4_3 = (TextView) findViewById(R.id.tv_selecttime_time4_3);
		tvTimeHint4_3 = (TextView) findViewById(R.id.tv_selecttime_timehint4_3);
		
		rlTime5_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time5_1);
		tvTime5_1 = (TextView) findViewById(R.id.tv_selecttime_time5_1);
		tvTimeHint5_1 = (TextView) findViewById(R.id.tv_selecttime_timehint5_1);
		
		rlTime5_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time5_2);
		tvTime5_2 = (TextView) findViewById(R.id.tv_selecttime_time5_2);
		tvTimeHint5_2 = (TextView) findViewById(R.id.tv_selecttime_timehint5_2);
		
		rlTime5_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time5_3);
		tvTime5_3 = (TextView) findViewById(R.id.tv_selecttime_time5_3);
		tvTimeHint5_3 = (TextView) findViewById(R.id.tv_selecttime_timehint5_3);
		
		rlTime6_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time6_1);
		tvTime6_1 = (TextView) findViewById(R.id.tv_selecttime_time6_1);
		tvTimeHint6_1 = (TextView) findViewById(R.id.tv_selecttime_timehint6_1);
		
		rlTime6_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time6_2);
		tvTime6_2 = (TextView) findViewById(R.id.tv_selecttime_time6_2);
		tvTimeHint6_2 = (TextView) findViewById(R.id.tv_selecttime_timehint6_2);
		
		rlTime6_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time6_3);
		tvTime6_3 = (TextView) findViewById(R.id.tv_selecttime_time6_3);
		tvTimeHint6_3 = (TextView) findViewById(R.id.tv_selecttime_timehint6_3);
		
		rlTime7_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time7_1);
		tvTime7_1 = (TextView) findViewById(R.id.tv_selecttime_time7_1);
		tvTimeHint7_1 = (TextView) findViewById(R.id.tv_selecttime_timehint7_1);
		
		rlTime7_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time7_2);
		tvTime7_2 = (TextView) findViewById(R.id.tv_selecttime_time7_2);
		tvTimeHint7_2 = (TextView) findViewById(R.id.tv_selecttime_timehint7_2);
		
		rlTime7_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time7_3);
		tvTime7_3 = (TextView) findViewById(R.id.tv_selecttime_time7_3);
		tvTimeHint7_3 = (TextView) findViewById(R.id.tv_selecttime_timehint7_3);
		
		rlTime8_1 = (RelativeLayout) findViewById(R.id.rl_selecttime_time8_1);
		tvTime8_1 = (TextView) findViewById(R.id.tv_selecttime_time8_1);
		tvTimeHint8_1 = (TextView) findViewById(R.id.tv_selecttime_timehint8_1);
		
		rlTime8_2 = (RelativeLayout) findViewById(R.id.rl_selecttime_time8_2);
		tvTime8_2 = (TextView) findViewById(R.id.tv_selecttime_time8_2);
		tvTimeHint8_2 = (TextView) findViewById(R.id.tv_selecttime_timehint8_2);
		
		rlTime8_3 = (RelativeLayout) findViewById(R.id.rl_selecttime_time8_3);
		tvTime8_3 = (TextView) findViewById(R.id.tv_selecttime_time8_3);
		tvTimeHint8_3 = (TextView) findViewById(R.id.tv_selecttime_timehint8_3);
		ivPreDay = (ImageView) findViewById(R.id.iv_selecttime_arrowleft);
		ivNextDay = (ImageView) findViewById(R.id.iv_selecttime_arrowright);
		
	}

	private void setView() {
		tvTitle.setText("预约时间");
		spUtil = SharedPreferenceUtil.getInstance(this);
		fIntent = getIntent();
		serviceid = fIntent.getIntExtra("serviceid", -1);
		serviceloc = fIntent.getIntExtra("serviceloc", 2);
//		petid = fIntent.getIntExtra("petid", -1);
		shopid = fIntent.getIntExtra("shopid", -1);
		areaid = fIntent.getIntExtra("areaid", -1);
		dayposition = fIntent.getIntExtra("position", 0);
		beauticianId = fIntent.getIntExtra("beautician_id", 0);
//		lat = fIntent.getDoubleExtra("lat", -1);
//		lng = fIntent.getDoubleExtra("lng", -1);
		strp = fIntent.getStringExtra("strp");
		
		screenwidth = Utils.getDisplayMetrics(this)[0]-Utils.dip2px(this, 60);
		itemwidth = (int) ((screenwidth/ 5.0 + 0.5f));
		
//		year = calendar.get(Calendar.YEAR);
		currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		currentMinute = calendar.get(Calendar.MINUTE);
		
		
		ibBack.setOnClickListener(this);
		rlTime1_1.setOnClickListener(this);
		rlTime1_2.setOnClickListener(this);
		rlTime1_3.setOnClickListener(this);
		rlTime2_1.setOnClickListener(this);
		rlTime2_2.setOnClickListener(this);
		rlTime2_3.setOnClickListener(this);
		rlTime3_1.setOnClickListener(this);
		rlTime3_2.setOnClickListener(this);
		rlTime3_3.setOnClickListener(this);
		rlTime4_1.setOnClickListener(this);
		rlTime4_2.setOnClickListener(this);
		rlTime4_3.setOnClickListener(this);
		rlTime5_1.setOnClickListener(this);
		rlTime5_2.setOnClickListener(this);
		rlTime5_3.setOnClickListener(this);
		rlTime6_1.setOnClickListener(this);
		rlTime6_2.setOnClickListener(this);
		rlTime6_3.setOnClickListener(this);
		rlTime7_1.setOnClickListener(this);
		rlTime7_2.setOnClickListener(this);
		rlTime7_3.setOnClickListener(this);
		rlTime8_1.setOnClickListener(this);
		rlTime8_2.setOnClickListener(this);
		rlTime8_3.setOnClickListener(this);
		ivNextDay.setOnClickListener(this);
		ivPreDay.setOnClickListener(this);
		
//		date = day1;
		getData(beauticianId);
		
	}
	private boolean isLeapyear(int year){
		return (year % 4 == 0 && year % 100 != 0 || year%400 == 0);
	}
	private String getWeekDay(int day){
		if(1 == day%7){
			return "周日";
		}else if(2 == day%7){
			return "周一";
			
		}else if(3 == day%7){
			return "周二";
			
		}else if(4 == day%7){
			return "周三";
			
		}else if(5 == day%7){
			return "周四";
			
		}else if(6 == day%7){
			return "周五";
			
		}else if(0 == day%7){
			return "周六";
			
		}else{
			return "";
		}
		
	}
	
	private void startGuide(int locationy){
		Intent intent = new Intent(this, AppointmentTimeGuideActivity.class);
		intent.putExtra("locationy", locationy);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		selectday(v);
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回操作
//			getTime();
			finishWithAnimation();
			break;
		case R.id.iv_selecttime_arrowleft:
			//左侧箭头
			if(currentday>0){
				llDaysContent.removeAllViews();
				initNav(daylist,daystatus,currentday-1);
			}
			break;
		case R.id.iv_selecttime_arrowright:
			//右侧箭头
			if(currentday<daylist.size()-1){
				llDaysContent.removeAllViews();
				initNav(daylist,daystatus,currentday+1);
			}
			break;
		case R.id.rl_selecttime_time1_1:
			//
			selectedTime = "0"+tvTime1_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time1_2:
			//
			selectedTime = tvTime1_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time1_3:
			//
			selectedTime = tvTime1_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time2_1:
			//
			selectedTime = tvTime2_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time2_2:
			//
			selectedTime = tvTime2_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time2_3:
			//
			selectedTime = tvTime2_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time3_1:
			//
			selectedTime = tvTime3_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time3_2:
			//
			selectedTime = tvTime3_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time3_3:
			//
			selectedTime = tvTime3_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time4_1:
			//
			selectedTime = tvTime4_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time4_2:
			//
			selectedTime = tvTime4_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time4_3:
			//
			selectedTime = tvTime4_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time5_1:
			//
			selectedTime = tvTime5_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time5_2:
			//
			selectedTime = tvTime5_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time5_3:
			//
			selectedTime = tvTime5_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time6_1:
			//
			selectedTime = tvTime6_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time6_2:
			//
			selectedTime = tvTime6_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time6_3:
			//
			selectedTime = tvTime6_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time7_1:
			//
			selectedTime = tvTime7_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time7_2:
			//
			selectedTime = tvTime7_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time7_3:
			//
			selectedTime = tvTime7_3.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time8_1:
			//
			selectedTime = tvTime8_1.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time8_2:
			//
			selectedTime = tvTime8_2.getText().toString();
			getTime();
			break;
		case R.id.rl_selecttime_time8_3:
			//
			selectedTime = tvTime8_3.getText().toString();
			getTime();
			break;
		}
		
		Utils.mLogError("日期："+selectedDate+selectedTime);
	}
	
	
	private String formatNum(int num){
		if(num<10){
			return "0"+num;
		}else{
			return ""+num;
		}
	}
	
	private void selectday(View v){
		for(int i=0;i<daylist.size();i++){
			if(i==v.getId()){
//				llDaysContent.getChildAt(currentday).setBackgroundResource(R.drawable.icon_selecttime_normal);
//				v.setBackgroundResource(R.drawable.bg_orange_round);
//				if(daystatus[i]==1){
//					((TextView)((LinearLayout)((RelativeLayout)llDaysContent.getChildAt(currentday)).getChildAt(0)).getChildAt(0)).setTextColor(getResources().getColor(R.color.a888888));
//					((TextView)((LinearLayout)((RelativeLayout)llDaysContent.getChildAt(currentday)).getChildAt(0)).getChildAt(1)).setTextColor(getResources().getColor(R.color.a888888));
//				}else{
//					((TextView)((LinearLayout)((RelativeLayout)llDaysContent.getChildAt(currentday)).getChildAt(0)).getChildAt(0)).setTextColor(getResources().getColor(R.color.orange));
//					((TextView)((LinearLayout)((RelativeLayout)llDaysContent.getChildAt(currentday)).getChildAt(0)).getChildAt(1)).setTextColor(getResources().getColor(R.color.orange));
//				}
//				
//				((TextView)((LinearLayout)((RelativeLayout)v).getChildAt(0)).getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
//				((TextView)((LinearLayout)((RelativeLayout)v).getChildAt(0)).getChildAt(1)).setTextColor(getResources().getColor(R.color.white));
//				currentday = i;
//				mHorizontalScrollView.smoothScrollTo((currentday-2) * itemwidth , 0);
//				selectedDate = daylist.get(i).day;
//				StringBuilder sb = new StringBuilder();
////				date = year +"-"+selectedDate;
//				sb.append(daylist.get(i).year);
//				sb.append("-");
//				sb.append(selectedDate);
//				date = sb.toString();
//				if(isTimeAvailable){
//					showTimes(times[currentday]);
//				}else{
//					setTimeAvailable(false);
//				}
				llDaysContent.removeAllViews();
				initNav(daylist, daystatus, i);
				break;
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == event.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
//			getTime();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getTime(){
		
		Intent data = new Intent();
		data.putExtra("position", currentday);
		data.putExtra("time", selectedDate+" "+selectedTime);
		data.putExtra("date", date+" "+selectedTime+":00");
		
		data.putExtra("beautician_name", fIntent.getStringExtra("beautician_name"));
		data.putExtra("beautician_sex", fIntent.getIntExtra("beautician_sex", 0));
		data.putExtra("beautician_level", fIntent.getIntExtra("beautician_level", 0));
		data.putExtra("beautician_stars", fIntent.getIntExtra("beautician_stars", 0));
		data.putExtra("beautician_titlelevel", fIntent.getStringExtra("beautician_titlelevel"));
		data.putExtra("beautician_image", fIntent.getStringExtra("beautician_image"));
		data.putExtra("beautician_ordernum", fIntent.getIntExtra("beautician_ordernum", 0));
		data.putExtra("beautician_storeid", fIntent.getIntExtra("beautician_storeid", 0));
		data.putExtra("beautician_id", fIntent.getIntExtra("beautician_id", 0));
		data.putExtra("beautician_sort", fIntent.getIntExtra("beautician_sort", 0));
		data.putExtra("beautician_levelname", fIntent.getStringExtra("beautician_levelname"));
		data.putExtra("beautician_bathmultiple", fIntent.getDoubleExtra("beautician_bathmultiple", 1));
		data.putExtra("beautician_beautymultiple", fIntent.getDoubleExtra("beautician_beautymultiple", 1));
		data.putExtra("beautician_factormultiple", fIntent.getDoubleExtra("beautician_factormultiple", 1));
		
		setResult(Global.RESULT_OK, data);
		finishWithAnimation();
	}
	private void setTodayDefaultTime(int hour, int minute){
		setTimeAvailable(true);
		if(daylist.size()>0){
			selectedDate = daylist.get(0).day;
			date = daylist.get(0).year +"-"+selectedDate;
		}
		
		showTimes(times[0]);
		boolean isHalf = minute > 30;
		switch (hour) {
		case 9:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
			}
			break;
		case 10:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
			}else{
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
			}
		case 11:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
			}
			break;
		case 12:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
			}
		case 13:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
			}
			break;
		case 14:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
			}
			break;
		case 15:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
			}
			break;
		case 16:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
			}
			break;
		case 17:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
			}else{
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
			}
			break;
		case 18:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
				setSelectTimeItemAvailable7_1(false);
			}else{
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
			}
			break;
		case 19:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
				setSelectTimeItemAvailable7_1(false);
				setSelectTimeItemAvailable7_2(false);
				setSelectTimeItemAvailable7_3(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
				setSelectTimeItemAvailable7_1(false);
				setSelectTimeItemAvailable7_2(false);
			}
			break;
		case 20:
			if(isHalf){
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
				setSelectTimeItemAvailable7_1(false);
				setSelectTimeItemAvailable7_2(false);
				setSelectTimeItemAvailable7_3(false);
				setSelectTimeItemAvailable8_1(false);
				setSelectTimeItemAvailable8_2(false);
			}else{
				setSelectTimeItemAvailable1_3(false);
				setSelectTimeItemAvailable1_2(false);
				setSelectTimeItemAvailable1_1(false);
				setSelectTimeItemAvailable2_1(false);
				setSelectTimeItemAvailable2_2(false);
				setSelectTimeItemAvailable2_3(false);
				setSelectTimeItemAvailable3_1(false);
				setSelectTimeItemAvailable3_2(false);
				setSelectTimeItemAvailable3_3(false);
				setSelectTimeItemAvailable4_1(false);
				setSelectTimeItemAvailable4_2(false);
				setSelectTimeItemAvailable4_3(false);
				setSelectTimeItemAvailable5_1(false);
				setSelectTimeItemAvailable5_2(false);
				setSelectTimeItemAvailable5_3(false);
				setSelectTimeItemAvailable6_1(false);
				setSelectTimeItemAvailable6_2(false);
				setSelectTimeItemAvailable6_3(false);
				setSelectTimeItemAvailable7_1(false);
				setSelectTimeItemAvailable7_2(false);
				setSelectTimeItemAvailable7_3(false);
				setSelectTimeItemAvailable8_1(false);
			}
			break;
		case 21:
		case 22:
		case 23:
			setTimeAvailable(false);
			break;
		}
		
	}
	
	private void setTimeAvailable(boolean available){
		setSelectTimeItemAvailable1_1(available);
		setSelectTimeItemAvailable1_2(available);
		setSelectTimeItemAvailable1_3(available);
		setSelectTimeItemAvailable2_1(available);
		setSelectTimeItemAvailable2_2(available);
		setSelectTimeItemAvailable2_3(available);
		setSelectTimeItemAvailable3_1(available);
		setSelectTimeItemAvailable3_2(available);
		setSelectTimeItemAvailable3_3(available);
		setSelectTimeItemAvailable4_1(available);
		setSelectTimeItemAvailable4_2(available);
		setSelectTimeItemAvailable4_3(available);
		setSelectTimeItemAvailable5_1(available);
		setSelectTimeItemAvailable5_2(available);
		setSelectTimeItemAvailable5_3(available);
		setSelectTimeItemAvailable6_1(available);
		setSelectTimeItemAvailable6_2(available);
		setSelectTimeItemAvailable6_3(available);
		setSelectTimeItemAvailable7_1(available);
		setSelectTimeItemAvailable7_2(available);
		setSelectTimeItemAvailable7_3(available);
		setSelectTimeItemAvailable8_1(available);
		setSelectTimeItemAvailable8_2(available);
		setSelectTimeItemAvailable8_3(available);
	}
	
	private void setSelectTimeItemAvailable1_1(boolean available){
		if(available){
			rlTime1_1.setEnabled(true);
			rlTime1_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime1_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_1.setText("可预约");
		}else{
			rlTime1_1.setEnabled(false);
			rlTime1_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime1_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable1_2(boolean available){
		if(available){
			rlTime1_2.setEnabled(true);
			rlTime1_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime1_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_2.setText("可预约");
		}else{
			rlTime1_2.setEnabled(false);
			rlTime1_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime1_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable1_3(boolean available){
		if(available){
			rlTime1_3.setEnabled(true);
			rlTime1_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime1_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint1_3.setText("可预约");
		}else{
			rlTime1_3.setEnabled(false);
			rlTime1_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime1_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint1_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable2_1(boolean available){
		if(available){
			rlTime2_1.setEnabled(true);
			rlTime2_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime2_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_1.setText("可预约");
		}else{
			rlTime2_1.setEnabled(false);
			rlTime2_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime2_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable2_2(boolean available){
		if(available){
			rlTime2_2.setEnabled(true);
			rlTime2_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime2_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_2.setText("可预约");
		}else{
			rlTime2_2.setEnabled(false);
			rlTime2_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime2_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable2_3(boolean available){
		if(available){
			rlTime2_3.setEnabled(true);
			rlTime2_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime2_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint2_3.setText("可预约");
		}else{
			rlTime2_3.setEnabled(false);
			rlTime2_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime2_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint2_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable3_1(boolean available){
		if(available){
			rlTime3_1.setEnabled(true);
			rlTime3_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime3_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_1.setText("可预约");
		}else{
			rlTime3_1.setEnabled(false);
			rlTime3_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime3_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable3_2(boolean available){
		if(available){
			rlTime3_2.setEnabled(true);
			rlTime3_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime3_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_2.setText("可预约");
		}else{
			rlTime3_2.setEnabled(false);
			rlTime3_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime3_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable3_3(boolean available){
		if(available){
			rlTime3_3.setEnabled(true);
			rlTime3_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime3_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint3_3.setText("可预约");
		}else{
			rlTime3_3.setEnabled(false);
			rlTime3_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime3_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint3_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable4_1(boolean available){
		if(available){
			rlTime4_1.setEnabled(true);
			rlTime4_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime4_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_1.setText("可预约");
		}else{
			rlTime4_1.setEnabled(false);
			rlTime4_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime4_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable4_2(boolean available){
		if(available){
			rlTime4_2.setEnabled(true);
			rlTime4_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime4_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_2.setText("可预约");
		}else{
			rlTime4_2.setEnabled(false);
			rlTime4_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime4_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable4_3(boolean available){
		if(available){
			rlTime4_3.setEnabled(true);
			rlTime4_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime4_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint4_3.setText("可预约");
		}else{
			rlTime4_3.setEnabled(false);
			rlTime4_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime4_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint4_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable5_1(boolean available){
		if(available){
			rlTime5_1.setEnabled(true);
			rlTime5_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime5_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_1.setText("可预约");
		}else{
			rlTime5_1.setEnabled(false);
			rlTime5_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime5_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable5_2(boolean available){
		if(available){
			rlTime5_2.setEnabled(true);
			rlTime5_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime5_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_2.setText("可预约");
		}else{
			rlTime5_2.setEnabled(false);
			rlTime5_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime5_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable5_3(boolean available){
		if(available){
			rlTime5_3.setEnabled(true);
			rlTime5_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime5_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint5_3.setText("可预约");
		}else{
			rlTime5_3.setEnabled(false);
			rlTime5_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime5_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint5_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable6_1(boolean available){
		if(available){
			rlTime6_1.setEnabled(true);
			rlTime6_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime6_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_1.setText("可预约");
		}else{
			rlTime6_1.setEnabled(false);
			rlTime6_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime6_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable6_2(boolean available){
		if(available){
			rlTime6_2.setEnabled(true);
			rlTime6_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime6_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_2.setText("可预约");
		}else{
			rlTime6_2.setEnabled(false);
			rlTime6_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime6_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable6_3(boolean available){
		if(available){
			rlTime6_3.setEnabled(true);
			rlTime6_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime6_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint6_3.setText("可预约");
		}else{
			rlTime6_3.setEnabled(false);
			rlTime6_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime6_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint6_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable7_1(boolean available){
		if(available){
			rlTime7_1.setEnabled(true);
			rlTime7_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime7_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_1.setText("可预约");
		}else{
			rlTime7_1.setEnabled(false);
			rlTime7_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime7_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable7_2(boolean available){
		if(available){
			rlTime7_2.setEnabled(true);
			rlTime7_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime7_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_2.setText("可预约");
		}else{
			rlTime7_2.setEnabled(false);
			rlTime7_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime7_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable7_3(boolean available){
		if(available){
			rlTime7_3.setEnabled(true);
			rlTime7_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime7_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint7_3.setText("可预约");
		}else{
			rlTime7_3.setEnabled(false);
			rlTime7_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime7_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint7_3.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable8_1(boolean available){
		if(available){
			rlTime8_1.setEnabled(true);
			rlTime8_1.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime8_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_1.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_1.setText("可预约");
		}else{
			rlTime8_1.setEnabled(false);
			rlTime8_1.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime8_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_1.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_1.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable8_2(boolean available){
		if(available){
			rlTime8_2.setEnabled(true);
			rlTime8_2.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime8_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_2.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_2.setText("可预约");
		}else{
			rlTime8_2.setEnabled(false);
			rlTime8_2.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime8_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_2.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_2.setText("已约满");
		}
	}
	private void setSelectTimeItemAvailable8_3(boolean available){
		if(available){
			rlTime8_3.setEnabled(true);
			rlTime8_3.setBackgroundResource(R.drawable.bg_selecttime_borderorange);
			tvTime8_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_3.setTextColor(getResources().getColor(R.color.orange));
			tvTimeHint8_3.setText("可预约");
		}else{
			rlTime8_3.setEnabled(false);
			rlTime8_3.setBackgroundResource(R.drawable.bg_selecttime_gray);
			tvTime8_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_3.setTextColor(getResources().getColor(R.color.a888888));
			tvTimeHint8_3.setText("已约满");
		}
	}
	
	private int num;
	private void initNav(ArrayList<MDay> list,int[] status) {
		int position = 0;
		for (int i = 0 ; i < list.size(); i++) {
			RelativeLayout layout = new RelativeLayout(this);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout ll = new LinearLayout(this);
			LayoutParams llp = 
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CLIP_VERTICAL);
			TextView tvWeek = new TextView(this);
			TextView tvDay = new TextView(this);
			tvWeek.setText(list.get(i).daydes);
			if(status[i]==1){
				layout.setBackgroundResource(R.drawable.icon_selecttime_normal);
				tvWeek.setTextColor(getResources().getColor(R.color.a888888));
				tvDay.setTextColor(getResources().getColor(R.color.a888888));
			}else{
				if (num == 0) {
					position = i;
					num++;
					layout.setBackgroundResource(R.drawable.bg_orange_round);
					tvDay.setTextColor(getResources().getColor(R.color.white));
					tvWeek.setTextColor(getResources().getColor(R.color.white));
				}else{
					layout.setBackgroundResource(R.drawable.icon_selecttime_normal);
					tvWeek.setTextColor(getResources().getColor(R.color.orange));
					tvDay.setTextColor(getResources().getColor(R.color.orange));
				}
			}
			tvWeek.setTextSize(18);
			tvWeek.setLayoutParams(llp);
			tvWeek.setGravity(Gravity.CENTER);
			tvDay.setText(list.get(i).day);
			tvDay.setLayoutParams(llp);
			tvDay.setGravity(Gravity.CENTER);
//			tvDay.setTextSize(18);
			ll.addView(tvWeek);
			ll.addView(tvDay);
			layout.setOnClickListener(this);
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			params.topMargin=20;
			layout.addView(ll, params);
			layout.setId(i);
			layout.setOnClickListener(this);
			llDaysContent.addView(layout, (int)(screenwidth/5 + 0.5f), LayoutParams.MATCH_PARENT);
		}
		currentday = position;
		selectedDate = daylist.get(currentday).day;
		StringBuilder sb = new StringBuilder();
		sb.append(daylist.get(currentday).year);
		sb.append("-");
		sb.append(selectedDate);
		date = sb.toString();
		if(isTimeAvailable){
			showTimes(times[currentday]);
		}else{
			setTimeAvailable(false);
		}
		//解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHorizontalScrollView.smoothScrollTo((currentday-2) * itemwidth , 0);
			}
		}, 5);
		
		if(position==0){
			//左侧箭头不可用，置灰
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft);
		}else{
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft_orange);
		}
		if(position==list.size()-1){
			//右侧箭头不可用，置灰
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright);
		}else {
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright_orange);
		}
		
	}
	
	private void initNav(ArrayList<MDay> list,int[] status,int position) {
		for (int i = 0 ; i < list.size(); i++) {
			RelativeLayout layout = new RelativeLayout(this);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout ll = new LinearLayout(this);
			LayoutParams llp = 
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CLIP_VERTICAL);
			TextView tvWeek = new TextView(this);
			TextView tvDay = new TextView(this);
			tvWeek.setText(list.get(i).daydes);
			if(status[i]==1){
				tvWeek.setTextColor(getResources().getColor(R.color.a888888));
				tvDay.setTextColor(getResources().getColor(R.color.a888888));
			}else{
				tvWeek.setTextColor(getResources().getColor(R.color.orange));
				tvDay.setTextColor(getResources().getColor(R.color.orange));
			}
			tvWeek.setTextSize(18);
			tvWeek.setLayoutParams(llp);
			tvWeek.setGravity(Gravity.CENTER);
			tvDay.setText(list.get(i).day);
			tvDay.setLayoutParams(llp);
			tvDay.setGravity(Gravity.CENTER);
//			tvDay.setTextSize(18);
			ll.addView(tvWeek);
			ll.addView(tvDay);
			layout.setOnClickListener(this);
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			params.topMargin=20;
			layout.addView(ll, params);
			layout.setId(i);
			layout.setOnClickListener(this);
			layout.setBackgroundResource(R.drawable.icon_selecttime_normal);
			if(i==position){
				layout.setBackgroundResource(R.drawable.bg_orange_round);
				tvDay.setTextColor(getResources().getColor(R.color.white));
				tvWeek.setTextColor(getResources().getColor(R.color.white));
//				selectedDate = daylist.get(i).day;
//				date = year +"-"+selectedDate;
				
			}
			llDaysContent.addView(layout, (int)(screenwidth/5 + 0.5f), LayoutParams.MATCH_PARENT);
			
		}
		currentday = position;
		selectedDate = daylist.get(currentday).day;
		StringBuilder sb = new StringBuilder();
		sb.append(daylist.get(currentday).year);
		sb.append("-");
		sb.append(selectedDate);
		date = sb.toString();
		if(isTimeAvailable){
			showTimes(times[currentday]);
		}else{
			setTimeAvailable(false);
		}
		//解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHorizontalScrollView.smoothScrollTo((currentday-2) * itemwidth , 0);
			}
		}, 5);
		
		if(position==0){
			//左侧箭头不可用，置灰
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft);
		}else{
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft_orange);
		}
		if(position==list.size()-1){
			//右侧箭头不可用，置灰
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright);
		}else {
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright_orange);
		}
		
	}
	
	private void getData(int beauticianid){
		pDialog.showDialog();
		if(beauticianid==0){
			CommUtil.getBeauticianFreeTime(this,0,0,spUtil.getString("cellphone", ""),
					Global.getIMEI(this),Global.getCurrentVersion(this),
					areaid,serviceloc,shopid,strp,0,null,0,timeHanler);
		}else{
			CommUtil.getBeauticianTimeById(this,0,0,spUtil.getString("cellphone", ""),
					Global.getIMEI(this),Global.getCurrentVersion(this),
					areaid,serviceloc,shopid,beauticianid,strp, timeHanler);
			
		}
	}
	
	private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("时间列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jData = jobj.getJSONObject("data");
					
					if(jData.has("desc")&&!jData.isNull("desc")){
						tvDayNum.setText(jData.getString("desc"));
					}
					
					if(!spUtil.getBoolean("timeguide", false)){
						int[] location = new int[2];
						tvDayNum.getLocationOnScreen(location);
						tvDayNum.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
								View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
						startGuide(location[1]+tvDayNum.getMeasuredHeight());
						
					}
					if(jData.has("calendar")&&!jData.isNull("calendar")){
						isTimeAvailable = true;
						JSONArray jday = jData.getJSONArray("calendar");
						for(int i = 0; i < jday.length(); i++){
							MDay jmditem = MDay.json2Entity(jday.getJSONObject(i));
							daylist.add(jmditem);
						}
					}
					if(jData.has("fullList")&&!jData.isNull("fullList")){
						JSONArray statusarr = jData.getJSONArray("fullList");
						for(int i = 0; i < statusarr.length(); i++){
							daystatus[i] = statusarr.getInt(i);
						}
					}
					if(jData.has("selection")&&!jData.isNull("selection")){
						JSONArray jtime = jData.getJSONArray("selection");
						for(int i = 0; i < jtime.length(); i++){
							JSONArray item = jtime.getJSONArray(i);
							for(int j = 0; j < item.length(); j++){
								times[i][j] = item.getInt(j);
							}
						}
					}
					
//					setTodayDefaultTime(currentHour, currentMinute);
					showTimes(times[0]);
					if (dayposition > 0) {
						initNav(daylist,daystatus,dayposition);
					}else{
						initNav(daylist,daystatus);
					}
				}else{
					isTimeAvailable = false;
					setTimeAvailable(false);
					String msg = jobj.getString("msg");
					ToastUtil.showToastShort(SelectTimeActivityNew.this, msg);
					finishWithAnimation();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isTimeAvailable = false;
				setTimeAvailable(false);
				ToastUtil.showToastShort(SelectTimeActivityNew.this, "网络异常，请重试！");
				finishWithAnimation();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			isTimeAvailable = false;
			setTimeAvailable(false);
			ToastUtil.showToastShort(SelectTimeActivityNew.this, "网络异常，请重试！");
			finishWithAnimation();
		}
		
	};
	private int serviceloc;
	private TextView tvDayNum;
	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout llDaysContent;
	private String strp;
	private ImageView ivPreDay;
	private ImageView ivNextDay;
	private String getDefaultTime(int[] flags){
		for(int i = 0; i < flags.length; i++){
			if(flags[i] == 0){
				switch (i) {
				case 0:
					rlTime1_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint1_1.setText("已选中");
					return tvTime1_1.getText().toString().trim();
				case 1:
					rlTime1_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint1_2.setText("已选中");
					return tvTime1_2.getText().toString().trim();
				case 2:
					rlTime1_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint1_3.setText("已选中");
					return tvTime1_3.getText().toString().trim();
				case 3:
					rlTime2_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint2_1.setText("已选中");
					return tvTime2_1.getText().toString().trim();
				case 4:
					rlTime2_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint2_2.setText("已选中");
					return tvTime2_2.getText().toString().trim();
				case 5:
					rlTime2_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint2_3.setText("已选中");
					return tvTime2_3.getText().toString().trim();
				case 6:
					rlTime3_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint3_1.setText("已选中");
					return tvTime3_1.getText().toString().trim();
				case 7:
					rlTime3_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint3_2.setText("已选中");
					return tvTime3_2.getText().toString().trim();
				case 8:
					rlTime3_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint3_3.setText("已选中");
					return tvTime3_3.getText().toString().trim();
				case 9:
					rlTime4_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint4_1.setText("已选中");
					return tvTime4_1.getText().toString().trim();
				case 10:
					rlTime4_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint4_2.setText("已选中");
					return tvTime4_2.getText().toString().trim();
				case 11:
					rlTime4_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint4_3.setText("已选中");
					return tvTime4_3.getText().toString().trim();
				case 12:
					rlTime5_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint5_1.setText("已选中");
					return tvTime5_1.getText().toString().trim();
				case 13:
					rlTime5_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint5_2.setText("已选中");
					return tvTime5_2.getText().toString().trim();
				case 14:
					rlTime5_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint5_3.setText("已选中");
					return tvTime5_3.getText().toString().trim();
				case 15:
					rlTime6_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTime6_1.setText("已选中");
					return tvTime6_1.getText().toString().trim();
				case 16:
					rlTime6_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint6_2.setText("已选中");
					return tvTime6_2.getText().toString().trim();
				case 17:
					rlTime6_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint6_3.setText("已选中");
					return tvTime6_3.getText().toString().trim();
				case 18:
					rlTime7_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint7_1.setText("已选中");
					return tvTime7_1.getText().toString().trim();
				case 19:
					rlTime7_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint7_2.setText("已选中");
					return tvTime7_2.getText().toString().trim();
				case 20:
					rlTime7_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint7_3.setText("已选中");
					return tvTime7_3.getText().toString().trim();
				case 21:
					rlTime8_1.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint8_1.setText("已选中");
					return tvTime8_1.getText().toString().trim();
				case 22:
					rlTime8_2.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint8_2.setText("已选中");
					return tvTime8_2.getText().toString().trim();
				case 23:
					rlTime8_3.setBackgroundColor(getResources().getColor(R.color.afad1aa));
					tvTimeHint8_3.setText("已选中");
					return tvTime8_3.getText().toString().trim();
				
				}
			}
		}
		return "";
	}
	private void showTimes(int[] flags){
		for(int i = 0; i < flags.length; i++){
			switch (i) {
			case 0:
				setSelectTimeItemAvailable1_1(flags[i]==0);
				break;
			case 1:
				setSelectTimeItemAvailable1_2(flags[i]==0);
				break;
			case 2:
				setSelectTimeItemAvailable1_3(flags[i]==0);
				break;
			case 3:
				setSelectTimeItemAvailable2_1(flags[i]==0);
				break;
			case 4:
				setSelectTimeItemAvailable2_2(flags[i]==0);
				break;
			case 5:
				setSelectTimeItemAvailable2_3(flags[i]==0);
				break;
			case 6:
				setSelectTimeItemAvailable3_1(flags[i]==0);
				break;
			case 7:
				setSelectTimeItemAvailable3_2(flags[i]==0);
				break;
			case 8:
				setSelectTimeItemAvailable3_3(flags[i]==0);
				break;
			case 9:
				setSelectTimeItemAvailable4_1(flags[i]==0);
				break;
			case 10:
				setSelectTimeItemAvailable4_2(flags[i]==0);
				break;
			case 11:
				setSelectTimeItemAvailable4_3(flags[i]==0);
				break;
			case 12:
				setSelectTimeItemAvailable5_1(flags[i]==0);
				break;
			case 13:
				setSelectTimeItemAvailable5_2(flags[i]==0);
				break;
			case 14:
				setSelectTimeItemAvailable5_3(flags[i]==0);
				break;
			case 15:
				setSelectTimeItemAvailable6_1(flags[i]==0);
				break;
			case 16:
				setSelectTimeItemAvailable6_2(flags[i]==0);
				break;
			case 17:
				setSelectTimeItemAvailable6_3(flags[i]==0);
				break;
			case 18:
				setSelectTimeItemAvailable7_1(flags[i]==0);
				break;
			case 19:
				setSelectTimeItemAvailable7_2(flags[i]==0);
				break;
			case 20:
				setSelectTimeItemAvailable7_3(flags[i]==0);
				break;
			case 21:
				setSelectTimeItemAvailable8_1(flags[i]==0);
				break;
			case 22:
				setSelectTimeItemAvailable8_2(flags[i]==0);
				break;
			case 23:
				setSelectTimeItemAvailable8_3(flags[i]==0);
				break;
			}
		}
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    MobclickAgent.onPageStart("SelectTimeActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	    MobclickAgent.onPageEnd("SelectTimeActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
}
