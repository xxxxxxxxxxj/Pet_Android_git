package com.haotang.pet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SwimAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.SwimIcon;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;

public class SwimAppointmentActivity extends SuperActivity implements OnClickListener{

	public static SwimAppointmentActivity act;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private LinearLayout layout_choose_pet;
	private TextView textview_dog_type;
	private TextView textview_pet_time;
	private LinearLayout layout_pet_address;
	private TextView textview_pet_address;
	private LinearLayout layout_pet_time;
	private CheckBox cb_login_agree;
	private TextView tv_oo;
	private Button button_dog_push_to_service;
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	private int addrSize = 0;
	private SharedPreferenceUtil spUtil;
	private int addrid;
	private double lng;
	private double lat;
	private String address ="";
	private int shopId;
	private String descri="";
	private int swimTime=0;
	private double price=0;
	private double listPrice=0;
	private int SwimPriceId=0;
	private ArrayList<SwimIcon> SwimLists = new ArrayList<SwimIcon>();
	private MyGridView swim_bottom_icon;
	private SwimAdapter<SwimIcon> mSAdapter;
	private String [] months = null;
	private String [] EveryWeek = null;
	private int current = 0;
	private Pet pet;
	private String showMonth ="";
	private String ShowWeek  ="";
	private int previous;
	private ImageView iv_back;
	private String agreement ="";
	private MProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim);
		MApplication.listAppoint.add(this);
		act = this;
		pDialog = new MProgressDialog(act);
		SwimLists.clear();
		getIntentFrom();
		initView();
		setView();
		initReceiver();
		getServiceAddrList();
	}
	private void getIntentFrom() {
		// TODO Auto-generated method stub
		previous = getIntent().getIntExtra("previous", 0);
	}
	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SwimAppointmentActivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}
	private void initView() {
		pet = new Pet();
		spUtil = SharedPreferenceUtil.getInstance(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		textview_pet_time = (TextView) findViewById(R.id.textview_pet_time);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		layout_choose_pet = (LinearLayout) findViewById(R.id.layout_choose_pet);
		textview_dog_type = (TextView) findViewById(R.id.textview_dog_type);
		layout_pet_address = (LinearLayout) findViewById(R.id.layout_pet_address);
		textview_pet_address = (TextView) findViewById(R.id.textview_pet_address);
		layout_pet_time = (LinearLayout) findViewById(R.id.layout_pet_time);
		cb_login_agree = (CheckBox) findViewById(R.id.cb_login_agree);
		tv_oo = (TextView) findViewById(R.id.tv_oo);
		button_dog_push_to_service = (Button) findViewById(R.id.button_dog_push_to_service);
		swim_bottom_icon = (MyGridView) findViewById(R.id.swim_bottom_icon);
		iv_back = (ImageView) findViewById(R.id.iv_fostercareappointment_fostercareimg);
	}
	private void setView() {
		layout_choose_pet.setOnClickListener(this);
		layout_pet_address.setOnClickListener(this);
		layout_pet_time.setOnClickListener(this);
		ib_titlebar_back.setOnClickListener(this);
		button_dog_push_to_service.setOnClickListener(this);
		tv_oo.setOnClickListener(this);
		tv_titlebar_title.setText("游泳");
		//default
		if (spUtil.getInt("petid", -1)<0&&previous==Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
			pet.id= getIntent().getIntExtra("petid", 0);
			pet.kindid = getIntent().getIntExtra("petkind", 0);
			pet.name=getIntent().getStringExtra("petname");
			pet.image = getIntent().getStringExtra("petimage");
			textview_dog_type.setText(pet.name);
		}else if (previous==Global.SWIM_MYPET_TO_SWIMAPPOINTMENT) {
			pet.id= getIntent().getIntExtra("petid", 0);
			pet.kindid = getIntent().getIntExtra("petkind", 0);
			pet.name=getIntent().getStringExtra("petname");
			pet.image = getIntent().getStringExtra("petimage");
			pet.customerpetid=spUtil.getInt("customerpetid", 0);
			pet.nickName=spUtil.getString("customerpetname", "");
			if (pet.nickName.equals("")) {
				textview_dog_type.setText(pet.name);
			}else {
				textview_dog_type.setText(pet.nickName);
			}
		}else if (spUtil.getInt("petid", 0)>0) {
			try {
				if (spUtil.getInt("petkind", 0)!=2) {
					pet.kindid=spUtil.getInt("petkind", 0);
					pet.id=spUtil.getInt("petid", 0);
					pet.name=spUtil.getString("petname", "");
					pet.customerpetid=spUtil.getInt("customerpetid", 0);
					pet.image=CommUtil.getServiceNobacklash()+spUtil.getString("petimage", "");
					pet.youyongPrice=price;
					pet.nickName=spUtil.getString("customerpetname", "");
					if (pet.nickName.equals("")) {
						textview_dog_type.setText(pet.name);
					}else {
						textview_dog_type.setText(pet.nickName);
					}
					addrid = spUtil.getInt("addressid", 0);
					address = spUtil.getString("address", "");
					lng = Double.parseDouble(spUtil.getString("lng", ""));
					lat = Double.parseDouble(spUtil.getString("lat", ""));
					textview_pet_address.setText(address);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mSAdapter = new SwimAdapter<SwimIcon>(this, SwimLists);
		swim_bottom_icon.setAdapter(mSAdapter);
		
		getData();
	}
	private void getData() {
		// TODO Auto-generated method stub
		pDialog.showDialog();
		shopId = 9;
		CommUtil.getSwimTime(mContext,spUtil.getString("cellphone", ""),shopId, handler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->游泳那些东西 responseBody "+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("descri")&&!objectData.isNull("descri")) {
							descri = objectData.getString("descri");
						}
						if (objectData.has("swimTime")&&!objectData.isNull("swimTime")) {
							swimTime = objectData.getInt("swimTime");
							months = new String[swimTime];
							EveryWeek = new String[swimTime];
							long nowDate = System.currentTimeMillis();
							for (int i = 0; i < swimTime; i++) {
								 long beforDate = Utils.beforDateNum(nowDate, i);
								  Date date = new Date(beforDate);
								  @SuppressWarnings("deprecation")
								  String DateTime = date.toLocaleString();
								  months[i]=DateTime;
								  EveryWeek[i]=Utils.getWeekOfDate(date);
							}
						}
						 
						if (objectData.has("openTime")&&!objectData.isNull("openTime")) {
							String openTime = objectData.getString("openTime");
							String openTimeS [] =  openTime.split(" ");
							 String [] monthCom =  months[0].replace("年", "-").replace("月", "-").replace("日", ",").replace("上午", "").split(",");
//							 Utils.mLogError("==-->months openTime 0 "+months[0]);
//							 Utils.mLogError("==-->months openTime 1 "+openTime);
//							 Utils.mLogError("==-->months openTime 2 "+openTimeS[0]);
//							 Utils.mLogError("==-->months openTime 3 "+monthCom[0]);
//							 openTimeS[0] = "2016-07-20";
							 int comResult = Utils.compare_date(openTimeS[0], monthCom[0]);
							 if (comResult==1) {
								ToastUtil.showToastShortCenter(mContext, "敬请期待");
								finishWithAnimation();
							 }
						}
						if (objectData.has("price")&&!objectData.isNull("price")) {
							price = Utils.formatDouble(objectData.getDouble("price"), 2);
							pet.youyongPrice=price;
						}
						if (objectData.has("listPrice")&&!objectData.isNull("listPrice")) {
							listPrice = objectData.getDouble("listPrice");
							pet.listPrice = listPrice;
						}
						if (objectData.has("bannerUrl")&&!objectData.isNull("bannerUrl")) {
							String bannerUrl = objectData.getString("bannerUrl");
							ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+bannerUrl,iv_back,  R.drawable.swimbacktop,null);
						}
						if (objectData.has("shopId")&&!objectData.isNull("shopId")) {
							shopId = objectData.getInt("shopId");
						}
						if (objectData.has("agreement")&&!objectData.isNull("agreement")) {
							agreement = objectData.getString("agreement");
						}
						if (objectData.has("listPrice")&&!objectData.isNull("listPrice")) {
							listPrice = Utils.formatDouble(objectData.getDouble("listPrice"), 2);
						}
//						if (objectData.has("SwimPriceId")&&!objectData.isNull("SwimPriceId")) {
//							SwimPriceId = objectData.getInt("SwimPriceId");
//						}
						if (objectData.has("serviceId")&&!objectData.isNull("serviceId")) {
							SwimPriceId = objectData.getInt("serviceId");
						}
						if (objectData.has("swimIndividual")&&!objectData.isNull("swimIndividual")) {
							JSONArray objectSwim = objectData.getJSONArray("swimIndividual");
							if (objectSwim.length()>0) {
								for (int i = 0; i < objectSwim.length(); i++) {
									SwimIcon swimIcon = new SwimIcon();
									JSONObject objectSwimEvery  = objectSwim.getJSONObject(i);
									if (objectSwimEvery.has("picPath")&&!objectSwimEvery.isNull("picPath")) {
										swimIcon.picPath = CommUtil.getServiceNobacklash()+objectSwimEvery.getString("picPath");
									}
									if (objectSwimEvery.has("name")&&!objectSwimEvery.isNull("name")) {
										swimIcon.name = objectSwimEvery.getString("name");
									}
									if (objectSwimEvery.has("shopId")&&!objectSwimEvery.isNull("shopId")) {
										swimIcon.shopId=objectSwimEvery.getInt("shopId");
									}
									if (objectSwimEvery.has("sort")&&!objectSwimEvery.isNull("sort")) {
										swimIcon.sort=objectSwimEvery.getInt("sort");
									}
									if (objectSwimEvery.has("SwimIndividualId")&&!objectSwimEvery.isNull("SwimIndividualId")) {
										swimIcon.SwimIndividualId=objectSwimEvery.getInt("SwimIndividualId");
									}
									swimIcon.outOrInside=2;
									SwimLists.add(swimIcon);
								}
								if (SwimLists.size()>0) {
									mSAdapter.notifyDataSetChanged();
								}
							}
						}
					}
				}
				pDialog.closeDialog();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					pDialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					pDialog.closeDialog();
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
				pDialog.closeDialog();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.layout_choose_pet:
			goNext(ChoosePetActivityNew.class,Global.SWIM_APPOINMENT);
			break;
		case R.id.layout_pet_address:
			// 选择地址
			if (spUtil.getInt("userid", 0) > 0 && !"".equals(spUtil.getString("cellphone", "")) && addrSize > 0) {
				goNext(CommonAddressActivity.class,Global.SWIM_APPOINMENT_CHOOSEADDRESS);
			} else {
				goNext(AddServiceAddressActivity.class,Global.SWIM_APPOINMENT_CHOOSEADDRESS_OTHER);
			}
			break;
		case R.id.layout_pet_time:
			if (months==null &&EveryWeek==null) {
			}else {
				goNext(PetDataSwimActivity.class, Global.SWIM_APPOINMENT_CHOOSE_TIME);
			}
			break;
		case R.id.button_dog_push_to_service:
			if (textview_dog_type.getText().toString().equals("")||textview_dog_type.getText().equals(null)) {
				ToastUtil.showToastShortCenter(mContext, "请选择您的宠物");
				return;
			}else if (textview_pet_address.getText().toString().equals("")||textview_pet_address.getText().equals(null)) {
				ToastUtil.showToastShortCenter(mContext, "请选择您的宠物地址");
				return;
			}else if (textview_pet_time.getText().toString().equals("")||textview_pet_time.getText().equals(null)) {
				ToastUtil.showToastShortCenter(mContext, "请选择您的预约时间");
				return;
			}else if (!cb_login_agree.isChecked()) {
				ToastUtil.showToastShortCenter(mContext, "请同意并勾选用户协议");
				return;
			}
			goNext(SwimDetailActivity.class, 0);
			break;
		case R.id.tv_oo:
			goNext(ShopH5DetailActivity.class, Global.SWIM_APPOINMENT_TO_WEBVIEW);
//			ToastUtil.showToastShortCenter(mContext, "协议没定");
			break;
			
		}
	}
	private void getServiceAddrList() {
		// TODO 请求服务器判断显示那个布局
		addrSize = 0;
		if (!"".equals(spUtil.getString("cellphone", ""))) {
			CommUtil.queryServiceAddress(spUtil.getString("cellphone", ""),
					spUtil.getInt("userid", 0), Global.getIMEI(this), this,
					queryServiceAddress);
		}
	}
	private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("查询常用地址： " + new String(responseBody));

			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					addrSize = jsonArray.length();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {

		}

	};

	private void goNext(Class cls,int pre){
		Intent intent = new Intent(this,cls);
		if (pre!=Global.SWIM_APPOINMENT_CHOOSE_TIME) {
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		}
		intent.putExtra("previous", pre);
		intent.putExtra("addrid", addrid);
		intent.putExtra("months", months);
		intent.putExtra("WeekShow", EveryWeek);
		intent.putExtra("shopId", shopId);
		intent.putExtra("SwimPriceId",SwimPriceId);
		intent.putExtra("pet",pet);
		intent.putExtra("showMonth",showMonth);
		intent.putExtra("ShowWeek",ShowWeek);
		intent.putExtra("current",current);
		intent.putExtra("addr_lng",lng);
		intent.putExtra("addr_lat",lat);
		intent.putExtra("address",address);
		intent.putExtra("orderDetailH5Url",agreement);
		intent.putParcelableArrayListExtra("icon", SwimLists);
		startActivity(intent);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver!=null) {
			unregisterReceiver(receiver);
		}
	}
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int index = intent.getIntExtra("index", -1);
			if (index==0) {
				String petName = intent.getStringExtra("petname");
				pet.id = intent.getIntExtra("petid", 0);
				pet.kindid = intent.getIntExtra("petkind",0);
				pet.customerpetid = intent.getIntExtra("customerpetid", 0);
				pet.nickName=intent.getStringExtra("customerpetname");
				pet.image =  intent.getStringExtra("petimage");
				pet.youyongPrice = price;
				pet.name = petName;
				if (pet.nickName==null||pet.nickName.equals("")) {
					textview_dog_type.setText(petName);
				}else {
					textview_dog_type.setText(pet.nickName);
				}
			}else if (index==1) {
				address = intent.getStringExtra("addr");
				addrid  = intent.getIntExtra("addr_id", 0);
				lng  = intent.getDoubleExtra("addr_lng", 0);
				lat  = intent.getDoubleExtra("addr_lat", 0);
				Utils.mLogError("==-->lng youyong"+lng+"  lat "+lat +" addrid "+addrid);
				textview_pet_address.setText(address);
			}else if (index==2) {
				showMonth = intent.getStringExtra("monthEvery");
				ShowWeek = intent.getStringExtra("weekEveryShow");
				current = intent.getIntExtra("current", 0);
				textview_pet_time.setText(showMonth+" "+ShowWeek);
			}
		}
	}
}
