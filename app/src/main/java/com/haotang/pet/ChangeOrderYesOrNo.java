package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class ChangeOrderYesOrNo extends SuperActivity implements OnClickListener{
	
	private TextView textview_change_beau_name;
	private TextView textview_change_beau_time;
	private TextView textview_change_notice;
	private Button button_to_cancle;
	private Button button_to_change;
	private int OrderId;
	private int workId;
	private String time;
	private String beauName;
	private String tip="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_order_yes_or_no);
		
		initView();
	}
	private void initView() {
		textview_change_beau_name = (TextView) findViewById(R.id.textview_change_beau_name);
		textview_change_beau_time = (TextView) findViewById(R.id.textview_change_beau_time);
		textview_change_notice = (TextView) findViewById(R.id.textview_change_notice);
		button_to_cancle = (Button) findViewById(R.id.button_to_cancle);
		button_to_change = (Button) findViewById(R.id.button_to_change);
		
		button_to_cancle.setOnClickListener(this);
		button_to_change.setOnClickListener(this);
		
		getIntentData();
	}
	private void getIntentData() {
		OrderId = getIntent().getIntExtra("OrderId", 0);
		workId = getIntent().getIntExtra("workId", 0);
		time = getIntent().getStringExtra("time");
		textview_change_beau_time.setText("服务时间："+time);
		time = time+":00";
		beauName = getIntent().getStringExtra("beauName");
		tip = getIntent().getStringExtra("tip");
		textview_change_beau_name.setText("美容师："+beauName);
		if (!TextUtils.isEmpty(tip)) {
			textview_change_notice.setVisibility(View.VISIBLE);
			textview_change_notice.setText(tip);
		}else {
			textview_change_notice.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_to_cancle:
			finish();
			break;
		case R.id.button_to_change:
			getData();
			break;
		}
	}
	private void getData(){
		mPDialog.showDialog();
		CommUtil.modifyOrder(mContext, workId, time, OrderId,null,null,handler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			mPDialog.closeDialog();
			Utils.mLogError("==-->改单:= "+new String(responseBody));
			try {
				JSONObject Object = new JSONObject(new String(responseBody));
				int code = Object.getInt("code");
				if (code==0) {
					if (Object.has("msg")&&!Object.isNull("msg")) {
						ToastUtil.showToastShortCenter(mContext, Object.getString("msg"));
						Intent intentStatus = new Intent();
						intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
						intentStatus.putExtra("index", 1);
						sendBroadcast(intentStatus);
						try {
							ChangeOrderActivity.activity.finish();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finishWithAnimation();
					}
				}else {
					if (Object.has("msg")&&!Object.isNull("msg")) {
						ToastUtil.showToastShortCenter(mContext, Object.getString("msg"));
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
			mPDialog.closeDialog();
			
		}
	};
}
