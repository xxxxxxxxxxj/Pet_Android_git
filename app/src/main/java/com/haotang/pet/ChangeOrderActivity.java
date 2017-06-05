package com.haotang.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChangeOrderBeau;
import com.haotang.pet.adapter.ContentTimeAdapter;
import com.haotang.pet.adapter.TopTimeDateAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.TimeListCodeBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean.TimesBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;

/**
 * 进度 带进来美容师可以 切换时间 再次点击美容师不出来了。先放着下周来调
 * @author Administrator
 *
 */
public class ChangeOrderActivity extends SuperActivity{

	public static ChangeOrderActivity activity;
	private ImageButton ibBack;
	private TextView tvTitle;
	private HorizontalScrollView hsv_appointfrg_date;
	private GridView gv_appointfrg_top;
	private MyGridView gv_appointfrg_content;
	
//	String[] Times = new String[] { "09:30", "10:00", "10:30", "11:00", "11:30",
//			"12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00",
//			"15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30",
//			"19:00", "19:30", "20:00", "20:30", "21:00" };
	
//	private List<CalendarBean> calendar;
//	private List<Integer> fullList;
	private TopTimeDateAdapter topTimeDateAdapter;
	private ContentTimeAdapter contentTimeAdapter;
//	private List<String> times;
//	private List<List<Integer>> timeList = new ArrayList<List<Integer>>();
	
	protected int currentday = -1;
	protected String selectedDate = "";
	protected String selectedTime = "";
	private String date = "";
	private int itemwidth;
	private double lat;
	private double lng;
	private int areaId;
	private int serviceLoc;
	private int shopId;
	private String strp;
	private String modifyTip;
	
	private int dayposition = -1;
	protected String strListWokerId = "";
	private MyGridView gv_appointfrg_beau;
	private ChangeOrderBeau changeOrderBeau;
	private List<Beautician> allChangeBeau = new ArrayList<Beautician>();
	private Button button_ok;
	private int workId;
	private Beautician beauticianOld;
	private String beauData="";
	private String beauTime="";
	private int currentdayTime=-1;
	private boolean isShowBeau = false;
	private String OnceListWorkId ="";
	private boolean isCanClickChange= false;
	private String oldTime="";
	private TextView tv_appointfrg_time;
	private TextView textview_beau_level;
	private int OrderId;
	private String beauName="";
	private int WorkLevel=-1;
	private List<SelectionBean> selection;
	private String tip="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_order);
		activity = this;
//		times = Arrays.asList(Times);
		allChangeBeau.clear();
		initView();
		setView();
		
	}

	private void setView() {
		tvTitle.setText("修改订单");
		getDataIntent();
		tv_appointfrg_time.setText(modifyTip);
//		textview_beau_level.setText(beauticianOld.levelname);
		setBeauAdapter();
		getData();
		initListener();
	}

	private void setBeauAdapter() {
		changeOrderBeau = new ChangeOrderBeau<Beautician>(mContext, allChangeBeau);
		gv_appointfrg_beau.setAdapter(changeOrderBeau);
	}

	private void initListener() {
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		button_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isCanClickChange) {
					if (workId<=0) {
						ToastUtil.showToastShortCenter(mContext, "请先选择美容师");
						return;
					}
					String appTime = "";
					if (TextUtils.isEmpty(selectedTime)) {
						appTime=date + " " + oldTime+":00";
					}else {
						appTime=date + " " + selectedTime+":00";
					}
					
					getOrderCheck(appTime);//获取接送提示
					
				}else {
//					ToastUtil.showToastShortCenter(mContext, "不可修改");
				}
			}

		});
		gv_appointfrg_beau.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < allChangeBeau.size(); i++) {
					allChangeBeau.get(i).isChoose = false;
				}
				Beautician beautician = (Beautician) parent.getItemAtPosition(position);
				if (!beautician.isChoose) {
					beautician.isChoose = !beautician.isChoose;
				}
				workId = beautician.id;
				beauName = beautician.name;
				allChangeBeau.set(position, beautician);
				changeOrderBeau.notifyDataSetChanged();
				if (TextUtils.isEmpty(selectedTime)) {
					if (beautician.id==beauticianOld.id&&date.equals(beauData)&&beauTime.equals(oldTime)) {
						isCanClickChange = false;
						button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
					}else {
						isCanClickChange = true;
						button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
					}
				}else {
					if (beautician.id==beauticianOld.id&&date.equals(beauData)&&beauTime.equals(selectedTime)) {
						isCanClickChange = false;
						button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
					}else {
						isCanClickChange = true;
						button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
					}
				}
			}
		});
		gv_appointfrg_top.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				allChangeBeau.clear();
				workId = 0;
				beauName = "";
				strListWokerId = "";
				selectedTime ="";
//				isShowBeau = false;
				currentday = position;
//				selectedDate = calendar.get(position).getDate();
//				date = calendar.get(position).getYear() + "-" + selectedDate;
//				if (topTimeDateAdapter != null) {
//					topTimeDateAdapter.setClickItem(position);
//					contentTimeAdapter = new ContentTimeAdapter(mContext, times,selection.get(position));
//					gv_appointfrg_content.setAdapter(contentTimeAdapter);
//					if (date.equals(beauData)) {
//						contentTimeAdapter.setTime(beauTime,0);
//						isShowBeau = true;
//						getChangeBeau(OnceListWorkId);
//					}else {
//						isShowBeau = false;
//					}
//					contentTimeAdapter.notifyDataSetChanged();
//				}
//				ScollTo(currentday);
//				changeOrderBeau.notifyDataSetChanged();
				
				dayposition = position;
				selectedDate = selection.get(position).getDate();
				date = selection.get(position).getYear() + "-" + selectedDate;
				if (topTimeDateAdapter != null) {
					topTimeDateAdapter.setClickItem(position);
//					setTs(position);
					contentTimeAdapter = new ContentTimeAdapter(mContext,selection.get(position).getTimes(), selection.get(position).getIsFull());
					gv_appointfrg_content.setAdapter(contentTimeAdapter);
					if (date.equals(beauData)) {
						contentTimeAdapter.setTime(beauTime, 0);
//						isShowBeau = true;
						getChangeBeau(OnceListWorkId);
					} else {
//						isShowBeau = false;
					}
					contentTimeAdapter.notifyDataSetChanged();
				}
				ScollTo(dayposition);
				changeOrderBeau.notifyDataSetChanged();
			}
		});
		gv_appointfrg_content.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				allChangeBeau.clear();
				workId = 0;
				beauName = "";
//				List<Integer> list = selection.get(currentday).get(position);
//				String str = "";
//				if (list != null && list.size() > 0) {
//					for (int i = 0; i < list.size(); i++) {
//						str = str + "," + list.get(i);
//					}
//				}
//				try {
//					strListWokerId = str.substring(1, str.length());
//				} catch (Exception e) {
//					e.printStackTrace();
//					strListWokerId = "";
//				}
//				selectedTime = Times[position];
//				if (contentTimeAdapter != null) {
//					contentTimeAdapter.setClickItemChangeBeau(position, beauTime,1);
//					Utils.mLogError("==-->strListWokerId "+strListWokerId);
//					//这里请求美容师 根据 strListWokerId
//					changeOrderBeau.notifyDataSetChanged();
//					if (selectedTime.equals(beauTime)&&date.equals(beauData)) {
//						isShowBeau = true;
//						button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
//						isCanClickChange = false; 
//						getChangeBeau(strListWokerId);
//					}else {
//						isShowBeau = false;
//						button_ok.setBackgroundResource(R.drawable.bg_button_orange_noround);
//						isCanClickChange = true;
//						getChangeBeau(strListWokerId);
//					}
//				}
				SelectionBean selectionBean = selection.get(dayposition);
				if (selectionBean != null) {
					List<TimesBean> times = selectionBean.getTimes();
					if (times != null && times.size() > 0&& times.size() > position) {
						TimesBean timesBean = times.get(position);
						if (timesBean != null) {
							selectedTime = timesBean.getTime();
							List<Integer> workers = timesBean.getWorkers();
							if (workers != null && workers.size() > 0) {
								String str = "";
								if (workers != null && workers.size() > 0) {
									for (int i = 0; i < workers.size(); i++) {
										str = str + "," + workers.get(i);
									}
								}
								try {
									strListWokerId = str.substring(1,str.length());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				selectedTime = selectionBean.getTimes().get(position).getTime();
				if (contentTimeAdapter != null) {
					if (selectedTime.equals(beauTime)&&date.equals(beauData)) {
//						isShowBeau = true;
						button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
//						isCanClickChange = false; 
						getChangeBeau(strListWokerId);
						contentTimeAdapter.setTime(beauTime, 0);
						contentTimeAdapter.setClickItem(position);
					}else {
//						isShowBeau = false;
						button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
//						isCanClickChange = true;
						getChangeBeau(strListWokerId);
						contentTimeAdapter.setTime("", 0);
						contentTimeAdapter.setClickItem(position);
					}
				}
			}
		});
	}

	private void getDataIntent() {
		lat = getIntent().getDoubleExtra("lat", 0);
		lng = getIntent().getDoubleExtra("lng",0);
		areaId = getIntent().getIntExtra("areaId", 0);
		serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
		shopId = getIntent().getIntExtra("shopId", 0);
		OrderId = getIntent().getIntExtra("OrderId", 0);
		strp = getIntent().getStringExtra("strp");
		modifyTip = getIntent().getStringExtra("modifyTip");
		beauticianOld = (Beautician) getIntent().getSerializableExtra("beautician");
		beauticianOld.isChoose = true;
		beauData = beauticianOld.appointment.split(" ")[0].trim();
		beauTime = beauticianOld.appointment.split(" ")[1].trim();
		WorkLevel = beauticianOld.levels;
	}

	private void initView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_appointfrg_time = (TextView)findViewById(R.id.tv_appointfrg_time);
		hsv_appointfrg_date = (HorizontalScrollView)findViewById(R.id.hsv_appointfrg_date);
		gv_appointfrg_top = (GridView)findViewById(R.id.gv_appointfrg_top);
		gv_appointfrg_content = (MyGridView)findViewById(R.id.gv_appointfrg_content);
		gv_appointfrg_beau = (MyGridView)findViewById(R.id.gv_appointfrg_beau);
		textview_beau_level = (TextView)findViewById(R.id.textview_beau_level);
		button_ok = (Button)findViewById(R.id.button_ok);
		
	}
	
	private void getChangeBeau(String str){
		mPDialog.showDialog();
		CommUtil.queryWorkersByIds(mContext, str, handler);
	}
	private void getData() {
		mPDialog.showDialog();
		CommUtil.getBeauticianFreeTime(mContext,lat, lng,
				spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
				Global.getCurrentVersion(mContext), areaId, serviceLoc,
				shopId, strp,WorkLevel,null,OrderId,timeHanler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			mPDialog.closeDialog();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
//				beauticianOld.isChoose = true;
				allChangeBeau.clear();
//				if (isShowBeau) {
//					allChangeBeau.add(beauticianOld);
//					isCanClickChange = false;
//					button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
//				}
				if (code == 0) {
					try {
						if (object.has("data")&&!object.isNull("data")) {
							JSONArray array = object.getJSONArray("data");
							if (array.length()>0) {
								for (int i = 0; i < array.length(); i++) {
									allChangeBeau.add(Beautician.json2Entity(array.getJSONObject(i)));
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (allChangeBeau.size()>0) {
					Beautician beautician = allChangeBeau.get(0);
//					if (beautician.id==beauticianOld.id&&date.equals(beauData)&&beauTime.equals(oldTime)) {
//						beautician.isChoose = true;
//						isCanClickChange = false;
//						button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
//					}else {
//						beautician.isChoose = false;
//						isCanClickChange = true;
//						button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
//					}
					if (TextUtils.isEmpty(selectedTime)) {
						if (beautician.id==beauticianOld.id&&date.equals(beauData)&&beauTime.equals(oldTime)) {
							beautician.isChoose = true;
							isCanClickChange = false;
							button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
						}else {
							beautician.isChoose = false;
							isCanClickChange = true;
							button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
						}
					}else {
						if (beautician.id==beauticianOld.id&&date.equals(beauData)&&beauTime.equals(selectedTime)) {
							beautician.isChoose = true;
							isCanClickChange = false;
							button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
						}else {
							beautician.isChoose = false;
							isCanClickChange = true;
							button_ok.setBackgroundColor(Color.parseColor("#bb996c"));
						}
					}
				}
				changeOrderBeau.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mPDialog.closeDialog();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			
		}
	};
	private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			mPDialog.closeDialog();
			String result = new String(responseBody);
			processData(result);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			mPDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result) {
		try {
			Gson gson = new Gson();
			TimeListCodeBean fromJson = gson.fromJson(result.toString(),
					TimeListCodeBean.class);
			if (fromJson != null) {
				int code = fromJson.getCode();
				DataBean data = fromJson.getData();
				String msg = fromJson.getMsg();
				if (code == 0 || code == 141 || code == 140) {
					if (code != 0) {
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(mContext, msg);
						}
					}
					if (data != null) {
						int position = 0;
						String desc = data.getDesc();
						selection = data.getSelection();
//						if (desc != null && !TextUtils.isEmpty(desc)) {
//							tvDayNum.setText(data.getDesc());
//						}
						if (selection != null && selection.size() > 0) {
							int size = selection.size();
							int length = 80;
							DisplayMetrics dm = new DisplayMetrics();
							mContext.getWindowManager().getDefaultDisplay()
									.getMetrics(dm);
							float density = dm.density;
							int gridviewWidth = (int) (size * (length + 5) * density);
							int itemWidth = (int) (length * density);
							itemwidth = itemWidth;
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									gridviewWidth,
									LinearLayout.LayoutParams.FILL_PARENT);
							gv_appointfrg_top.setLayoutParams(params); // 重点
							gv_appointfrg_top.setColumnWidth(itemWidth); // 重点
							gv_appointfrg_top.setHorizontalSpacing(5); // 间距
							gv_appointfrg_top.setStretchMode(GridView.NO_STRETCH);
							gv_appointfrg_top.setGravity(Gravity.CENTER);// 位置居中
							gv_appointfrg_top.setNumColumns(size); // 重点
							topTimeDateAdapter = new TopTimeDateAdapter(mContext, selection);
							gv_appointfrg_top.setAdapter(topTimeDateAdapter);
							for (int i = 0; i < selection.size(); i++) {
								if (selection.get(i).getIsFull() == 0) {
									position = i;
									break;
								}
							}
							for (int i = 0; i < selection.size(); i++) {
								if (beauData.equals(selection.get(i).getYear()+"-"+selection.get(i).getDate())) {
									position = i;
									dayposition = position;
									Utils.mLogError("==-->改单日期 ：= "+position+" 日期：=  "+beauData);
									break;
								}
							}
							if (dayposition >= 0) {
								currentday = dayposition;
								selectedDate = selection.get(dayposition).getDate();
								date = selection.get(dayposition).getYear()+ "-" + selectedDate;
								topTimeDateAdapter.setClickItem(dayposition);
//								setTs(dayposition);
								ScollTo(dayposition);
							} else {
								currentday = dayposition;
								dayposition = position;
								selectedDate = selection.get(position).getDate();
								date = selection.get(position).getYear() + "-"+ selectedDate;
								topTimeDateAdapter.setClickItem(position);
//								setTs(position);
								ScollTo(position);
							}
							// 默认显示可预约的第一天或者上次选中的那一天
							if (dayposition >= 0) {
								contentTimeAdapter = new ContentTimeAdapter(mContext, selection.get(dayposition).getTimes(), selection.get(dayposition).getIsFull());
							} else {
								contentTimeAdapter = new ContentTimeAdapter(mContext, selection.get(dayposition).getTimes(), selection.get(
												dayposition).getIsFull());
							}
							gv_appointfrg_content.setAdapter(contentTimeAdapter);
							
							contentTimeAdapter.setTime(beauTime,0);
							SelectionBean selectionBean = selection.get(currentday);
							for (int i = 0; i < selectionBean.getTimes().size(); i++) {
								if (beauTime.equals(selectionBean.getTimes().get(i).getTime())) {
									oldTime = selection.get(currentday).getTimes().get(i).getTime();
									currentdayTime = i;
								}
							}
							
							try {
								if (selectionBean != null) {
									List<TimesBean> times = selectionBean.getTimes();
									if (times != null && times.size() > 0
											&& times.size() > position) {
										TimesBean timesBean = times.get(currentdayTime);
										if (timesBean != null) {
											selectedTime = timesBean.getTime();
											List<Integer> workers = timesBean.getWorkers();
											if (workers != null && workers.size() > 0) {
												String str = "";
												if (workers != null && workers.size() > 0) {
													for (int i = 0; i < workers.size(); i++) {
														str = str + "," + workers.get(i);
													}
												}
//												isShowBeau = true;
												strListWokerId = str.substring(1,str.length());
												OnceListWorkId = strListWokerId;
											}
										}
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							button_ok.setBackgroundColor(Color.parseColor("#bdbdbd"));
							
						}
						getChangeBeau(strListWokerId);
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortBottom(mContext, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ScollTo(final int dayposition2) {
		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				hsv_appointfrg_date.smoothScrollTo((dayposition2 - 2)* itemwidth, 0);
			}
		}, 5);
	}
	private void getOrderCheck(String appTime) {
		CommUtil.modifyOrderCheck(mContext, workId, appTime,OrderId, shopId, handlerCheck);
	}
	private AsyncHttpResponseHandler handlerCheck = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code =  object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("tip")&&!objectData.isNull("tip")) {
							tip = objectData.getString("tip");
						}else {
//							tip = "";
						}
					}else {
//						tip = "";
					}
				}else {
//					tip = "";
				}
				Intent intent = new Intent(mContext, ChangeOrderYesOrNo.class);
				intent.putExtra("OrderId", OrderId);
				intent.putExtra("workId", workId);
				intent.putExtra("tip", tip);
				if (TextUtils.isEmpty(selectedTime)) {
					intent.putExtra("time", date + " " + oldTime);
				}else {
					intent.putExtra("time", date + " " + selectedTime);
				}
				intent.putExtra("beauName",beauName);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	};
}
