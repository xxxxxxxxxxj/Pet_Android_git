package com.haotang.pet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.FostercareDateAdapter;
import com.haotang.pet.entity.MDate;
import com.haotang.pet.entity.MMonth;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.PinnedHeaderListView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FostercareDateActivity extends SuperActivity {
	private ImageButton ibBackButton;
	private TextView tvTitle;
	private PinnedHeaderListView plvListView;
	private TextView tvHint;
	private ArrayList<MMonth> monthList;
	private String startdate;
	private String enddate;
	private String today;
	private boolean isbeforetoday;
	private FostercareDateAdapter dateAdapter;
	private LinearLayout llFooter;
	private int monthpositon;
	private boolean isselectedmiddle;
	private int startmonth;
	private int startday;
	private int endmonth;
	private int endday;
	private MProgressDialog pDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercare_date);
		
		pDialog=new MProgressDialog(this);
		findView();
		setView();
	}

	private void findView() {
		ibBackButton = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		plvListView = (PinnedHeaderListView) findViewById(R.id.plv_fostercare_date);
		
		tvHint = (TextView) findViewById(R.id.tv_fostercaredate_hint);
		llFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fostercaredatefooter, null);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("选择入住离店时间");
		startdate = getIntent().getStringExtra("startdate");
		enddate = getIntent().getStringExtra("enddate");
		monthpositon = getIntent().getIntExtra("monthposition", 0);
		
		
		monthList = new ArrayList<MMonth>();
		dateAdapter = new FostercareDateAdapter(this, monthList,tvHint,
				startmonth,startday,endmonth,endday);
		plvListView.setAdapter(dateAdapter);
		plvListView.addFooterView(llFooter);
		
		ibBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		
		getDate();
		
	}

	
	private int dayofweek(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.get(Calendar.DAY_OF_WEEK)-1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	private void getDate(){
		pDialog.showDialog();
		CommUtil.reserveCalendar(this,dateAddress);
	}
	
	private AsyncHttpResponseHandler dateAddress = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("日期： "+new String(responseBody));
			pDialog.closeDialog();
			try {
				JSONObject json = new JSONObject(new String(responseBody));
				int code = json.getInt("code");
				String msg = json.getString("msg");
				if (code==0&&json.has("data")&&!json.isNull("data")){
					JSONObject jdata = json.getJSONObject("data");
					if(jdata.has("today")&&!jdata.isNull("today")){
						today = jdata.getString("today");
					}
					if(jdata.has("calendar")&&!jdata.isNull("calendar")){
						JSONArray jarr = jdata.getJSONArray("calendar");
						for(int i=0;i<jarr.length();i++){
							MMonth mm = new MMonth();
							JSONArray jdays = jarr.getJSONArray(i);
							mm.daynum = jdays.length();
							for(int j=0;j<jdays.length();j++){
								JSONObject jday = jdays.getJSONObject(j);
								MDate md = new MDate();
								if(jday.has("day")&&!jday.isNull("day")){
									md.date = jday.getString("day");
									md.week=dayofweek(md.date);
									if(!isbeforetoday){
										if(md.date.equals(today)){
											isbeforetoday=true;
											md.valid = true;
										}else{
											md.valid = false;
										}
										
									}else{
										md.valid = true;
									}
									if(md.date.equals(startdate)){
										md.isselected=true;
										md.selectedindex = 1;
										isselectedmiddle = true;
										startmonth=i;
										startday=j;
									}else if(md.date.equals(enddate)){
										md.isselected=true;
										md.selectedindex = 2;
										md.isselectedmiddle = true;
										isselectedmiddle = false;
										endmonth=i;
										endday=j;
									}
									if(isselectedmiddle)
										md.isselectedmiddle = true;
									String[] sarr = md.date.split("-");
									if(sarr.length>2){
										if(j==0){
											StringBuilder sb = new StringBuilder();
											sb.append(sarr[0]);
											sb.append("年");
											sb.append(sarr[1]);
											sb.append("月");
											mm.yearandmonth=sb.toString();
											
										}
										md.day=sarr[2];
									}
									
								}
								if(jday.has("name")&&!jday.isNull("name")){
									md.holiday = jday.getString("name");
								}
								if(j==0){
									for(int k=0;k<md.week;k++){
										MDate md1= new MDate();
										md1.week=k;
										mm.daylist.add(md1);
									}
								}
								mm.daylist.add(md);
							}
							monthList.add(mm);
						}
						dateAdapter.setStartMonth(startmonth);
						dateAdapter.setStartDay(startday);
						dateAdapter.setEndDay(endday);
						dateAdapter.setEndMonth(endmonth);
						dateAdapter.notifyDataSetChanged();
						plvListView.setSelectionFromTop(monthpositon*2, 22);
					}
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
		
	};
	
}
