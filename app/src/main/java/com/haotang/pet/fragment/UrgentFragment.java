package com.haotang.pet.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.AddServiceAddressActivity;
import com.haotang.pet.AppointmentActivity;
import com.haotang.pet.CommonAddressActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.OrderDetailActivity;
import com.haotang.pet.PetDataServiceActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.ExpressAdapter;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;

@SuppressLint("NewApi")
public class UrgentFragment extends Fragment implements OnClickListener{
	private Activity context;
	
//	private ImageView imageView_top;
	private TextView 
	textview_dog_name,
	textview_dog_service_time,
	textview_dog_service_content,
	textview_dog_address,
	textview_dog_service_type;
	private Button button_dog_push_to_service;
	private LinearLayout layout_choose_pet,
	layout_choose_time,
	layout_choose_address
	;
	private int addrSize = 0;
	private SharedPreferenceUtil spUtil;
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	private int addrid = 0;
	
	private double lat = 0;
	private double lng = 0;
	
	private MListview listView_show_pet;
	private TextView textview_beau_lever;
	private ArrayList<MulPetService> mulPetServiceList;
	
	private ExpressAdapter<MulPetService> expressAdapter;
	
	private String month ="";
	private String times = "";
	private String address = "";
	private int ServiceType;
	private int OrderType;
	private int clicksort;
	private double lastPrice;
	public UrgentFragment() {
		super();
	}

	public UrgentFragment(Activity context) {
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater, container);
		getIntentFrom();
		initListener();
		getServiceAddrList();
		initReceiver();
		return view;
	}

	private void getIntentFrom() {
		if (!"".equals(spUtil.getString("newaddr", ""))&& !"".equals(spUtil.getString("newlat", ""))
				&& !"".equals(spUtil.getString("newlng", ""))) {
			addrid = spUtil.getInt("newaddrid", 0);
			address=spUtil.getString("newaddr", "");
			textview_dog_address.setText(spUtil.getString("newaddr", ""));
			
			lat = Double.parseDouble(spUtil.getString("newlat", "0"));
			lng = Double.parseDouble(spUtil.getString("newlng", "0"));
		} else if (spUtil.getInt("addressid", 0) > 0
				&& !"".equals(spUtil.getString("address", ""))) {
			addrid = spUtil.getInt("addressid", 0);
			address = spUtil.getString("address", "");
			textview_dog_address.setText(spUtil.getString("address", ""));
			lat = Double.parseDouble(spUtil.getString("lat", "0"));
			lng = Double.parseDouble(spUtil.getString("lng", "0"));
		}
		ServiceType = AppointmentActivity.addrtype/*getActivity().getIntent().getIntExtra("ServiceType",0)*/;
		if (ServiceType==1) {
			textview_dog_service_type.setText("到店服务");
			OrderType = 2;
		}else if (ServiceType==2) {
			textview_dog_service_type.setText("上门服务");
			OrderType = 4;
		}
		mulPetServiceList = AppointmentActivity.mulPetServiceList;
		textview_dog_service_content.setText(AppointmentActivity.tvServiceName.getText().toString());
		if (mulPetServiceList.size()>0) {
			Utils.mLogError("==-->bbbb mulPetServiceList size"+mulPetServiceList.size());
			expressAdapter = new ExpressAdapter<MulPetService>(context,mulPetServiceList);
			listView_show_pet.setAdapter(expressAdapter);
		}
		
		clicksort = AppointmentActivity.clicksort;
		lastPrice = AppointmentActivity.lastPrice;
		Utils.mLogError("==-->clicksort "+clicksort);
		if (clicksort==10) {
			textview_beau_lever.setText("中级");
		}else if (clicksort==20) {
			textview_beau_lever.setText("高级");
		}else if (clicksort==30) {
			textview_beau_lever.setText("首席");
		}
	}

	private void initListener() {
		layout_choose_pet.setOnClickListener(this);
		layout_choose_time.setOnClickListener(this);
		layout_choose_address.setOnClickListener(this);
		button_dog_push_to_service.setOnClickListener(this);
	}

	private View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.urgenfragment_layout,container, false);
//		imageView_top = (ImageView) view.findViewById(R.id.imageView_top);
//		textview_dog_name = (TextView) view.findViewById(R.id.textview_dog_name);
		textview_dog_service_time = (TextView) view.findViewById(R.id.textview_dog_service_time);
		textview_dog_service_content = (TextView) view.findViewById(R.id.textview_dog_service_content);
		textview_dog_address = (TextView) view.findViewById(R.id.textview_dog_address);
		textview_dog_service_type = (TextView) view.findViewById(R.id.textview_dog_service_type);
		button_dog_push_to_service = (Button) view.findViewById(R.id.button_dog_push_to_service);
		layout_choose_pet = (LinearLayout) view.findViewById(R.id.layout_choose_pet);
		layout_choose_time = (LinearLayout) view.findViewById(R.id.layout_choose_time);
		layout_choose_address = (LinearLayout) view.findViewById(R.id.layout_choose_address);
		listView_show_pet = (MListview) view.findViewById(R.id.listView_show_pet);
		textview_beau_lever = (TextView) view.findViewById(R.id.textview_beau_lever);
		spUtil = SharedPreferenceUtil.getInstance(getActivity());
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.layout_choose_pet:
			// 添加宠物
//			goNextForData(ChoosePetActivityNew.class, Global.APPOINTMENT_TO_ADDPET);
//			break;
		case R.id.layout_choose_time:
			goNextChooseData(PetDataServiceActivity.class);
			break;
		case R.id.layout_choose_address:
			// 选择地址
			if (spUtil.getInt("userid", 0) > 0
					&& !"".equals(spUtil.getString("cellphone", ""))
					&& addrSize > 0) {
				goNextForData(CommonAddressActivity.class,Global.BOOKINGSERVICEREQUESTCODE_ADDR);
			} else {
				goNextForData(AddServiceAddressActivity.class,Global.PRE_SERVICEACTIVITY_TO_ADDSERICEADDRRESSACTIVITY);
			}
			break;
		case R.id.button_dog_push_to_service:
			if (month.equals("")) {
				ToastUtil.showToastShortBottom(context, "请选择服务时间");
				return;
			}else if (spUtil.getInt("userid", 0) > 0) {
				//如果是未登录情况下添加的地址是不存在addrid的，已登录情况下选择地址肯定存在，都满足不判断了
//				if (addrid<=0) {
//					ToastUtil.showToastShortBottom(context, "请选择宠物地址");
//					return;
//				}
			}else if (spUtil.getInt("userid", 0) < 0) {
				if (address.equals("")) {
					ToastUtil.showToastShortBottom(context, "请选择宠物地址");
					return;
				}
			}
			if (isLogin()) {
				goNextForData(OrderDetailActivity.class, Global.URGENT_TO_ORDERDETAIL);
			}else {
				goNextForData(LoginActivity.class, 0);
			}
			break;
		}
	}
	private void getServiceAddrList() {
		// TODO 请求服务器判断显示那个布局
		addrSize = 0;
		if (!"".equals(spUtil.getString("cellphone", ""))) {
			CommUtil.queryServiceAddress(spUtil.getString("cellphone", ""),
					spUtil.getInt("userid", 0), Global.getIMEI(getActivity()), getActivity(),
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
	
	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.UrgentFragment");
		// 注册广播接收器
		getActivity().registerReceiver(receiver, filter);
	}
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (!bundle.isEmpty()) {
			   int index = bundle.getInt("index");
				if (index==0) {
					address = bundle.getString("addr");
					addrid = bundle.getInt("addr_id");
					lat  = bundle.getDouble("addr_lat");
					lng  = bundle.getDouble("addr_lng");
					textview_dog_address.setText(address);
				}else if (index==1) {
					month = bundle.getString("month");
					String monthText = bundle.getString("monthText");
					times = bundle.getString("time");
					textview_dog_service_time.setText(monthText+" "+times);
				}
			}
		}
	}
	private void goNextForData(Class clazz,int pre) {
		Intent intent = new Intent(getActivity(), clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("addrid", addrid);
		intent.putExtra("clicksort",clicksort);
		intent.putExtra("OrderType", OrderType);
		intent.putExtra("totalfee", lastPrice);
		intent.putExtra("isEx", true);
		intent.putExtra("month", month);
		intent.putExtra("times", times);
		intent.putParcelableArrayListExtra("mulpetservice", mulPetServiceList);
		
		AppointmentActivity.ps.servicedate=month+" "+times;
		AppointmentActivity.ps.addr_id=addrid;
		AppointmentActivity.ps.addr_detail=address;
		AppointmentActivity.ps.addr_lat = lat;
		AppointmentActivity.ps.addr_lng=lng;
		AppointmentActivity.ps.addrtype=ServiceType;
		AppointmentActivity.ps.addr_detail=address;
		intent.putExtra("ps", AppointmentActivity.ps);
		
		intent.putExtra("shopId", AppointmentActivity.shopid);
		startActivity(intent);
	}

	private void goNextChooseData(Class clazz) {
		Intent intent = new Intent(getActivity(), clazz);
//		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//		getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
//		intent.putExtra("previous", pre);
//		intent.putExtra("serviceid", serviceid);
//		intent.putExtra("servicetype", servicetype);
//		intent.putExtra("addrid", addrid);
//		intent.putExtra("lat", lat);
//		intent.putExtra("lng", lng);
//		intent.putExtra("shopid", shopid);
//		intent.putExtra("time", date);
//		intent.putExtra("beautician_sort", ps.beautician_sort);
//		intent.putExtra("servloc", addrtype);
//		startActivityForResult(intent, requestcode);
		startActivity(intent);
	}
	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

}
