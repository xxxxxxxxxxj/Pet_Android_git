package com.haotang.pet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContactActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private Button btSave;
	private ClearEditText cetName;
	private ClearEditText cetPhone;
	private SharedPreferenceUtil spUtil;
	private int previous;
	private int OrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		findView();
		setView();
	}

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		btSave = (Button) findViewById(R.id.bt_titlebar_other);
		
		cetName = (ClearEditText) findViewById(R.id.cet_contact_name);
		cetPhone = (ClearEditText) findViewById(R.id.cet_contact_phone);
		
	}

	private void setView() {
		tvTitle.setText("联系人信息");
		
//		cetName.setText(spUtil.getString("username", ""));
//		cetPhone.setText(spUtil.getString("cellphone", ""));
		cetName.setText(getIntent().getStringExtra("cname"));
		cetPhone.setText(getIntent().getStringExtra("cphone"));
		previous = getIntent().getIntExtra("previous", 0);
		OrderId = getIntent().getIntExtra("OrderId", 0);
		//把光标放在最后
		CharSequence text = cetName.getText();
		if(text instanceof Spannable){
			Spannable spantext = (Spannable) text;
			Selection.setSelection(spantext, text.length());
		}
		CharSequence textphone = cetPhone.getText();
		if(textphone instanceof Spannable){
			Spannable spantext = (Spannable) textphone;
			Selection.setSelection(spantext, textphone.length());
		}
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		
		btSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkPhone(ContactActivity.this, cetPhone)){
					if (previous==Global.ORDER_CHANGE_CUSTOMEORPHONE||previous==Global.ORDER_WAIT_TO_PAY_DETAIL_CHANGE_CUSTOMER) {
						getData();
					}else {
						Intent data = new Intent();
						data.putExtra("contactname", cetName.getText().toString().trim());
						data.putExtra("contactphone", cetPhone.getText().toString().trim());
						setResult(Global.RESULT_OK, data);
						finishWithAnimation();
					}
				}else{
					ToastUtil.showToastShort(ContactActivity.this, "请输入正确的手机号码");
				}
			}
		});
		
		
		
	}
	
	public static boolean checkPhone(Context context, ClearEditText phone_et) {
		String telPattern = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(17[0-9]))\\d{8}$";
		String phone = phone_et.getText().toString().trim();
		boolean isAvail = startCheck(telPattern, phone);
		if("".equals(phone.trim())){
			ToastUtil.showToastShort(context, "请输入您的手机号码");
			phone_et.requestFocus();
			phone_et.setFocusableInTouchMode(true);
			return false;
		}
		if (!isAvail) {
			ToastUtil.showToastShort(context, "请输入正确的手机号码");
			phone_et.requestFocus();
			phone_et.setFocusableInTouchMode(true);
			return false;
		}
		return true;
	}
	/**
	 * 正则规则验证
	 * @param reg
	 * @param string
	 * @return
	 */
	public static boolean startCheck(String reg, String string) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);		
		return matcher.matches();
	}
	private void getData(){
		mPDialog.showDialog();
		CommUtil.modifyOrder(mContext,0, null, OrderId,cetName.getText().toString().trim(),cetPhone.getText().toString().trim(),handler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			mPDialog.closeDialog();
			Utils.mLogError("==-->修改联系人"+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("msg")&&!object.isNull("msg")) {
						if (previous==Global.ORDER_WAIT_TO_PAY_DETAIL_CHANGE_CUSTOMER) {
							Intent data = new Intent();
//							data.putExtra("contactname", cetName.getText().toString().trim());
//							data.putExtra("contactphone", cetPhone.getText().toString().trim());
							setResult(Global.RESULT_OK, data);
							finishWithAnimation();
						}else if (previous==Global.ORDER_CHANGE_CUSTOMEORPHONE) {
							ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
							Intent intentStatus = new Intent();
							intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
							intentStatus.putExtra("index", 1);
							sendBroadcast(intentStatus);
							finishWithAnimation();
						}
					}
				}else {
					if (object.has("msg")&&!object.isNull("msg")) {
						ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
					}
				}
			} catch (JSONException e) {
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
