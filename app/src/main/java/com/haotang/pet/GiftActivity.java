package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GiftActivity extends Activity implements OnClickListener{
	private SharedPreferenceUtil spUtil;
	private View viewBack;
	private ImageButton ibClose;
	private ImageView ivTitle;
	private RelativeLayout rlLevel1;
	private RelativeLayout rlLevel2;
	private RelativeLayout rlLevel3;
	private EditText etPhone;
	private ImageButton ibReceive;
	private TextView tvPhone;
	private ImageButton ibReceiveOk;
	private ImageButton ibReceiveChange;
	private ImageButton ibReceiveGiveup;
	private String code;
	private RelativeLayout rlMain;
	private MProgressDialog pDialog;
	private int level=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gift);
		spUtil = SharedPreferenceUtil.getInstance(this);
		spUtil.saveBoolean("opengift", true);
		pDialog = new MProgressDialog(this);
		findView();
		setView();
	}

	private void findView() {
		viewBack = findViewById(R.id.view_gift_back);
		viewline = findViewById(R.id.view_gift_1);
		ibClose = (ImageButton) findViewById(R.id.ib_gift_close);
		ivTitle = (ImageView) findViewById(R.id.iv_gift_title);
		rlMain = (RelativeLayout) findViewById(R.id.rl_gift_main);
		rlLevel1 = (RelativeLayout) findViewById(R.id.rl_gift_level1);
		rlLevel2 = (RelativeLayout) findViewById(R.id.rl_gift_level2);
		rlLevel3 = (RelativeLayout) findViewById(R.id.rl_gift_level3);
		etPhone = (EditText) findViewById(R.id.et_gift_level1_phone);
		ibReceive = (ImageButton) findViewById(R.id.ib_gift_receive);
		tvPhone = (TextView) findViewById(R.id.tv_gift_level2_phone);
		ibReceiveOk = (ImageButton) findViewById(R.id.ib_gift_receive_ok);
		ibReceiveChange = (ImageButton) findViewById(R.id.ib_gift_receive_change);
		ibReceiveGiveup = (ImageButton) findViewById(R.id.ib_gift_receive_giveup);
		
	}

	private void setView() {
		code = getIntent().getStringExtra("code");
		viewBack.setOnClickListener(this);
		ibClose.setOnClickListener(this);
		rlMain.setOnClickListener(this);
		ibReceive.setOnClickListener(this);
		ibReceiveOk.setOnClickListener(this);
		ibReceiveChange.setOnClickListener(this);
		ibReceiveGiveup.setOnClickListener(this);
		if("".equals(spUtil.getString("giftphone", ""))){
			etPhone.setText(spUtil.getString("cellphone", ""));
		}else{
			etPhone.setText(spUtil.getString("giftphone", ""));
		}
		//把光标放到最后
		CharSequence text = etPhone.getText();
		if(text instanceof Spannable){
			Spannable spantext = (Spannable) text;
			Selection.setSelection(spantext, text.length());
		}
		if(text.toString().length()>0){
			etPhone.setBackgroundResource(R.drawable.bg_gift_phone_white);
		}else{
			etPhone.setBackgroundResource(R.drawable.bg_gift_phone_hint);
		}
		etPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()==0){
					etPhone.setBackgroundResource(R.drawable.bg_gift_phone_hint);
				}else{
					etPhone.setBackgroundResource(R.drawable.bg_gift_phone_white);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_gift_back:
			savePhone();
			finish();
			break;
		case R.id.ib_gift_close:
			savePhone();
			finish();
			break;
		case R.id.ib_gift_receive:
			//兑换
			if(etPhone.getText().toString().trim().length()==11&&
					"1".equals(etPhone.getText().toString().trim().substring(0, 1))){
				sendCode(etPhone.getText().toString().trim());
			}else if(etPhone.getText().toString().trim().length()==0){
				ToastUtil.showToastShortCenter(GiftActivity.this, "号码不能为空，请输入手机号");
			}else{
				ToastUtil.showToastShortCenter(GiftActivity.this, "号码格式不正确，请重新输入");
			}
			break;
		case R.id.ib_gift_receive_ok:
			//兑换完成
			closeNoShow();
			break;
		case R.id.ib_gift_receive_change:
			//更换号码
			level=1;
			ivTitle.setBackgroundResource(R.drawable.icon_gift_title_1);
			rlLevel2.setVisibility(View.GONE);
			rlLevel3.setVisibility(View.GONE);
			rlLevel1.setVisibility(View.VISIBLE);
			break;
		case R.id.ib_gift_receive_giveup:
			//放弃兑换
			closeNoShow();
			break;
		}
		
	}
	private void closeNoShow(){
//		spUtil.saveBoolean("noshowgift", true);
		finish();
	}
	private void savePhone(){
		if(level==1&&etPhone.getText().toString().trim().length()>0&&
				!etPhone.getText().toString().trim().equals(spUtil.getString("cellphone", ""))){
			spUtil.saveString("giftphone", etPhone.getText().toString().trim());
		}else{
			spUtil.removeData("giftphone");
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			savePhone();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	private void sendCode(String phone){
		pDialog.showDialog();
		CommUtil.redeemCouponCode(phone, "chongwujiayonghuduantuiguangzhuanyong", 
				this, code, redeemCouponCode);
	}
	private AsyncHttpResponseHandler redeemCouponCode = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("==-->redeem "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					level++;
					ivTitle.setBackgroundResource(R.drawable.icon_gift_title_2);
					rlLevel1.setVisibility(View.GONE);
					rlLevel3.setVisibility(View.GONE);
					rlLevel2.setVisibility(View.VISIBLE);
					ibClose.setEnabled(false);
					ibClose.setVisibility(View.INVISIBLE);
					viewline.setVisibility(View.INVISIBLE);
					tvPhone.setText(etPhone.getText().toString());
				}else {
					level++;
					ivTitle.setBackgroundResource(R.drawable.icon_gift_title_3);
					rlLevel2.setVisibility(View.GONE);
					rlLevel1.setVisibility(View.GONE);
					rlLevel3.setVisibility(View.VISIBLE);
					etPhone.setText("");
					etPhone.setBackgroundResource(R.drawable.bg_gift_phone_hint);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				level++;
				ivTitle.setBackgroundResource(R.drawable.icon_gift_title_3);
				rlLevel2.setVisibility(View.GONE);
				rlLevel1.setVisibility(View.GONE);
				rlLevel3.setVisibility(View.VISIBLE);
				etPhone.setText("");
				etPhone.setBackgroundResource(R.drawable.bg_gift_phone_hint);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			level++;
			rlLevel2.setVisibility(View.GONE);
			rlLevel1.setVisibility(View.GONE);
			rlLevel3.setVisibility(View.VISIBLE);
			etPhone.setText("");
			etPhone.setBackgroundResource(R.drawable.bg_gift_phone_hint);
		}
		
	};
	private View viewline;
	
}
