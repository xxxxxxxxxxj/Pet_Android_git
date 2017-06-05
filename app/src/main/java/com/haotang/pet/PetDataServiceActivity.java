package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.petDate;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.WheelPicker.ArrayWheelAdapter;
import com.haotang.pet.view.WheelPicker.OnWheelChangedListener;
import com.haotang.pet.view.WheelPicker.WheelView;

public class PetDataServiceActivity extends SuperActivity{

	private WheelView wv_month;
	private WheelView wv_time;
	private WheelView wv_day_time;
//	private static final String[] months ={"2017-6-9","2017-6-10"};
//	private static final String[] monthsText ={"今天","明天"};
	private static String[] months =null;
	private static String[] monthsText =null;
	private String onetimes [];
	private String twotimes [];
	
	private TextView tvTitle,choose_sure;
	private ArrayWheelAdapter<String> monthAdapter;
	private ArrayWheelAdapter<String> timeAdapter;
	private ArrayList<petDate> listdatas = new ArrayList<petDate>();
	private int current=0;
	private MProgressDialog mDialog;	
	private LinearLayout show_top;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.petdateserviceseleter);
		mDialog = new MProgressDialog(mContext);
		mDialog.showDialog();
		wv_month = (WheelView) findViewById(R.id.wv_month);
		wv_time = (WheelView) findViewById(R.id.wv_time);
		wv_day_time = (WheelView) findViewById(R.id.wv_day_time);
		wv_day_time.setVisibility(View.GONE);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		choose_sure = (TextView) findViewById(R.id.choose_sure);
		show_top = (LinearLayout) findViewById(R.id.show_top);
		
		int defaultsize = (int) tvTitle.getTextSize();
		wv_month.setDefTextSize(defaultsize);
		wv_time.setDefTextSize(defaultsize);
		wv_month.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				Utils.mLogError("==-->"+monthAdapter.getItem(wv_month.getCurrentItem()));
				current = wv_month.getCurrentItem();
				if (current==0) {
					timeAdapter = new ArrayWheelAdapter<String>(listdatas.get(current).times);
					wv_time.setAdapter(timeAdapter);
					wv_time.setCurrentItem(0);
				}else if (current==1) {
					timeAdapter = new ArrayWheelAdapter<String>(listdatas.get(current).times);
					wv_time.setAdapter(timeAdapter);
					wv_time.setCurrentItem(0);
				}
			}
		});
		CommUtil.getLoadAppointments(this,LoadAppointments);
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
					Bundle bundle = new Bundle();
					bundle.putInt("index", 1);
					bundle.putString("month",months[wv_month.getCurrentItem()]);
					bundle.putString("monthText",monthsText[wv_month.getCurrentItem()]);
					bundle.putString("time", timeAdapter.getItem(wv_time.getCurrentItem()));
					intent.setAction("android.intent.action.UrgentFragment");
					intent.putExtras(bundle);
					sendBroadcast(intent);
					finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.errornotice));
					finish();
				}
			}
		});
	}
	
	private AsyncHttpResponseHandler LoadAppointments = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->获取加急时间列表 ： "+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONArray objectData = object.getJSONArray("data");
						if (objectData.length()>0) {
							for (int i = 0; i < objectData.length(); i++) {
								petDate petDate = new petDate();
								JSONObject objectEvery = objectData.getJSONObject(i);
								if (objectEvery.has("date")&&!objectEvery.isNull("date")) {
									petDate.date=objectEvery.getString("date");
//									months[i]=objectEvery.getString("date");
								}
								if (objectEvery.has("date_tx")&&!objectEvery.isNull("date_tx")) {
									petDate.date_tx =  objectEvery.getString("date_tx");
//									monthsText[i] = objectEvery.getString("date_tx");
								}
								if (objectEvery.has("times")&&!objectEvery.isNull("times")) {
									JSONArray array = objectEvery.getJSONArray("times");
									if (array.length()>0) {
										petDate.times = new String[array.length()];
										for (int j = 0; j < array.length(); j++) {
											petDate.times[j]=array.getString(j)+"点";
										}
									}
								}
								listdatas.add(petDate);
							}
							
						}
//						listdatas.clear();
//						petDate date = new petDate();
//						date.date="2016-07-27";
//						date.date_tx="明天";
//						date.times = new String[11];
//						for (int i = 0; i < date.times.length; i++) {
//							date.times[i]=(i+9)+"";
//						}
//						listdatas.add(date);
						if (listdatas.size()>0) {
							months = new String[listdatas.size()];
							monthsText = new String[listdatas.size()];
							for (int i = 0; i < listdatas.size(); i++) {
								months [i] =  listdatas.get(i).date;
								monthsText [i] = listdatas.get(i).date_tx;
							}
						}
						monthAdapter = new ArrayWheelAdapter<String>(monthsText);
						wv_month.setAdapter(monthAdapter);
						current = wv_month.getCurrentItem();
						timeAdapter = new ArrayWheelAdapter<String>(listdatas.get(0).times);
						wv_time.setAdapter(timeAdapter);
					}
				}
				try {
					mDialog.closeDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					mDialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			try {
				mDialog.closeDialog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
}
