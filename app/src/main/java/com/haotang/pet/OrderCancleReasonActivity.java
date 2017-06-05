package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CancleNewAdapter;
import com.haotang.pet.entity.CancleReson;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
/**
 * 新版取消原因
 * @author Administrator
 *
 */
public class OrderCancleReasonActivity extends SuperActivity implements OnClickListener{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ListView listView_show_reason;
	private int type = -1;
	private int orderid = -1;
	private SharedPreferenceUtil spUtil;
	private CancleNewAdapter<CancleReson> mCAdapter;
	ArrayList<CancleReson> showReason = null;
	private Button button_push_reason;
	private String cancleReson="";
	public static OrderCancleReasonActivity orderCancleReasonActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_cancle_reason);
		MApplication.listAppoint.add(this);
		orderCancleReasonActivity = this;
		initView();
		getIntentData();
		setView();
		initListener();
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(this);
		button_push_reason.setOnClickListener(this);
		listView_show_reason.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CancleReson reson = (CancleReson) parent.getItemAtPosition(position);
				cancleReson = reson.txt;
				mCAdapter.setPostition(position);
				mCAdapter.notifyDataSetChanged();
			}
		});
	}
	private void setView() {
		if (type==1) {
			CommUtil.orderCareCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,
					orderid,
					1,
					getInsertReminderlog);
		}else if (type==3) {
			CommUtil.orderSwimCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,
					orderid,
					1,
					getInsertReminderlog);
		}else if (type==4) {
			CommUtil.orderTrainCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,orderid,1,
					getInsertReminderlog);
		}else {
			CommUtil.orderHotelCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,
					orderid,
					1,
					getInsertReminderlog);
		}
		mCAdapter = new CancleNewAdapter<CancleReson>(this, showReason);
		listView_show_reason.setAdapter(mCAdapter);
	}
	private void getIntentData() {
		type = getIntent().getIntExtra("type", -1);
		orderid = getIntent().getIntExtra("orderid", -1);
	}
	private void initView() {
		showReason = new ArrayList<CancleReson>();
		showReason.clear();
		spUtil = SharedPreferenceUtil.getInstance(this);
		listView_show_reason = (ListView) findViewById(R.id.listView_show_reason);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		button_push_reason = (Button) findViewById(R.id.button_push_reason);
		tv_titlebar_title.setText("取消订单原因");
	}
	private AsyncHttpResponseHandler getInsertReminderlog = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("txt")&&!objectData.isNull("txt")) {
							JSONArray array = objectData.getJSONArray("txt");
							for (int i = 0; i < array.length(); i++) {
								CancleReson cancleReson = new CancleReson();
								cancleReson.txt=array.getString(i);
								cancleReson.isChoose = false;
								showReason.add(cancleReson);
							}
						}
						if (showReason.size()>0) {
							mCAdapter.notifyDataSetChanged();
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.button_push_reason:
			CommUtil.saveCancelReason(spUtil.getString("cellphone", ""), Global.getIMEI(this)
					, mContext, orderid, cancleReson, cancleHandle);
			break;
		}
	}
	
	private AsyncHttpResponseHandler cancleHandle = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				Utils.mLogError("==-->保存取消原因  "+new String(responseBody));
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					try {
						OrderCancleActivity.orderCancleActivity.finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent();
					intent.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
					intent.putExtra("index", 1);
					sendBroadcast(intent);
					finishWithAnimation();
				}else {
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

}
