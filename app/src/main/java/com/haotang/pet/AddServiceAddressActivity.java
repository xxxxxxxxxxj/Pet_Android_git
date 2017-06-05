package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class AddServiceAddressActivity extends SuperActivity{
	
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title,textView_icon_addService_area_name;
	private RelativeLayout layout_choose_service_address;
	private EditText editText_icon_addService_detail_name;
	private Button btn_addService_sure;
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	double lat;
	double lng;
	private int previous;
	private SharedPreferenceUtil spUtil;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				String str = (String) msg.obj;
//				Toast.makeText(AddServiceAddressActivity.this, "点击连接网络", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				String str = (String) msg.obj;
				Toast.makeText(AddServiceAddressActivity.this, str, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_service_address);
		spUtil = SharedPreferenceUtil.getInstance(this);
		previous = getIntent().getIntExtra("previous", 0);
		initView();
		initReceiver();
		initListener();
	}
	private void initListener() {
		editText_icon_addService_detail_name.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				editText_icon_addService_detail_name.setCursorVisible(true);
				return false;
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//服务器交互
		btn_addService_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//这里处理获取到的小区名称以及详细地址，发送到服务器
				if (textView_icon_addService_area_name.getText().equals("")||textView_icon_addService_area_name.getText().equals(null)) {
					ToastUtil.showToastShort(AddServiceAddressActivity.this, "请输入您的小区名");
				}else {
					if(!"".equals(spUtil.getString("cellphone", ""))){
						CommUtil.addServiceAddress(
								SharedPreferenceUtil.getInstance(AddServiceAddressActivity.this).getString("cellphone", ""), 
								SharedPreferenceUtil.getInstance(AddServiceAddressActivity.this).getInt("userid", 0),
								Global.getIMEI(AddServiceAddressActivity.this),
								AddServiceAddressActivity.this,
								textView_icon_addService_area_name.getText().toString()+
								editText_icon_addService_detail_name.getText().toString(),
								lat, lng, addService);
						
					}else{
//						CommUtil.searchTradeArea(lat, lng, searchTradeArea);//未登陆状态下判断是否在服务区，已登陆后台处理
						//以下模块迁移至服务区判断模块
						pushDataUnLogin();
						
						
						//以下是给预约模块的
						Intent data = new Intent();
						data.putExtra("addr", textView_icon_addService_area_name.getText().toString()+
								editText_icon_addService_detail_name.getText().toString());
						data.putExtra("addr_lat", lat);
						data.putExtra("addr_lng", lng);
						setResult(Global.RESULT_OK, data);
						finishWithAnimation();
					}

				}
			}


		});
		
		layout_choose_service_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//获取小区名称
				JumpToNext(ServiceAddressSearch.class);
			}
		});
	}
	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.AddServiceAddressActivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		textView_icon_addService_area_name = (TextView) findViewById(R.id.textView_icon_addService_area_name);
		layout_choose_service_address = (RelativeLayout) findViewById(R.id.layout_choose_service_address);
		editText_icon_addService_detail_name = (EditText) findViewById(R.id.editText_icon_addService_detail_name);
		editText_icon_addService_detail_name.setCursorVisible(false);
		btn_addService_sure = (Button) findViewById(R.id.btn_addService_sure);
		
		
		tv_titlebar_title.setText("添加宠物地址");
	}
	private AsyncHttpResponseHandler searchTradeArea = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("result")&&!object.isNull("result")) {
							boolean isOk = object.getBoolean("result");
							if (isOk) {
								Utils.mLogError("==-->isOk");
								Intent data = new Intent();
								data.putExtra("addr", textView_icon_addService_area_name.getText().toString()+
										editText_icon_addService_detail_name.getText().toString());
								data.putExtra("addr_lat", lat);
								data.putExtra("addr_lng", lng);
								setResult(Global.RESULT_OK, data);
								finishWithAnimation();
							}else {
								Utils.mLogError("==-->isNot");
								ToastUtil.showToastShortCenter(AddServiceAddressActivity.this, "很抱歉您选择的地址尚未开通此项服务");
							}
						}
					}
				}
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

	private AsyncHttpResponseHandler addService = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("添加服务地址"+new String(responseBody));
			
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code;
				if (jsonObject.has("code")&&!jsonObject.isNull("code")) {
					code = jsonObject.getInt("code");
					if (code==0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
						
						PushData(jsonObject);
						
						Intent data = new Intent();
						data.putExtra("addr", textView_icon_addService_area_name.getText().toString()+
								editText_icon_addService_detail_name.getText().toString());
						data.putExtra("addr_lat", lat);
						data.putExtra("addr_lng", lng);
						data.putExtra("addr_id", jsonObject.getInt("data"));
						setResult(Global.RESULT_OK, data);
						finishWithAnimation();
					}else {
						String msg;
						if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
							msg = jsonObject.getString("msg");
							ToastUtil.showToastShort(AddServiceAddressActivity.this,msg);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			handler.sendEmptyMessage(0);
//			Message msg = Message.obtain();
//			msg.obj=new String(responseBody);
//			msg.what=1;
//			handler.sendMessage(msg);
		}


		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private void PushData(JSONObject jsonObject) throws JSONException {
		if (previous==Global.SWIM_APPOINMENT_CHOOSEADDRESS_OTHER) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("addr",textView_icon_addService_area_name.getText().toString()+
					editText_icon_addService_detail_name.getText().toString());
			bundle.putDouble("addr_lng", lng);
			bundle.putDouble("addr_lat", lat);
			bundle.putInt("addr_id", jsonObject.getInt("data"));
			bundle.putInt("index", 1);
			intent.setAction("android.intent.action.SwimAppointmentActivity");
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}else {
			//以下是给加急模块的
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("addr",textView_icon_addService_area_name.getText().toString()+
					editText_icon_addService_detail_name.getText().toString());
			bundle.putDouble("addr_lng", lng);
			bundle.putDouble("addr_lat", lat);
			bundle.putInt("addr_id", jsonObject.getInt("data"));
			bundle.putInt("index", 0);
			intent.setAction("android.intent.action.UrgentFragment");
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}
	}

	private void pushDataUnLogin() {
		if (previous==Global.SWIM_APPOINMENT_CHOOSEADDRESS_OTHER) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("addr",textView_icon_addService_area_name.getText().toString()+
					editText_icon_addService_detail_name.getText().toString());
			bundle.putDouble("addr_lng", lng);
			bundle.putDouble("addr_lat", lat);
			bundle.putInt("index", 1);
			intent.setAction("android.intent.action.SwimAppointmentActivity");
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}else {
			//以下是给加急模块的
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("addr",textView_icon_addService_area_name.getText().toString()+
					editText_icon_addService_detail_name.getText().toString());
			bundle.putDouble("addr_lng", lng);
			bundle.putDouble("addr_lat", lat);
			bundle.putInt("index", 0);
			intent.setAction("android.intent.action.UrgentFragment");
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}
	}
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int index =  bundle.getInt("index");
			if (index==0) {
				String selfName = bundle.getString("SelfName");
				textView_icon_addService_area_name.setText(selfName);
				lat = Double.parseDouble(bundle.getString("lat"));
				lng = Double.parseDouble(bundle.getString("lng"));
//				Toast.makeText(context, " lat := "+lat  +"  lng:=  "+lng, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}
	
	private void JumpToNext(Class clazz) {
		Intent intent = new Intent(AddServiceAddressActivity.this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    MobclickAgent.onPageStart("AddServiceAddressActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPageEnd("AddServiceAddressActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		 MobclickAgent.onPause(this);
	}
}
