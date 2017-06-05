package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.UserNameAlertDialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class LanShowActivity extends SuperActivity implements OnClickListener{
	private RelativeLayout layou_all,lLayout_bg;
	private ImageView iv_alertdialog_close;
	private TextView tv_alertdialog_title;
	private EditText et_alertdialog_name;
	private Button btn_alertdialog_refuse;
	private Display display;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lanshou);
		layou_all  = (RelativeLayout) findViewById(R.id.layou_all);
		lLayout_bg  = (RelativeLayout) findViewById(R.id.lLayout_bg);
		iv_alertdialog_close  = (ImageView) findViewById(R.id.iv_alertdialog_close);
		tv_alertdialog_title = (TextView) findViewById(R.id.tv_alertdialog_title);
		et_alertdialog_name = (EditText) findViewById(R.id.et_alertdialog_name);
		et_alertdialog_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() >= 6) {
					ToastUtil.showToastShortCenter(LanShowActivity.this, "你最多只能输入六个字符");
				}
			}
		});
		btn_alertdialog_refuse = (Button) findViewById(R.id.btn_alertdialog_refuse);
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (display.getWidth() * 0.85),
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		lLayout_bg.setLayoutParams(params);
		
		layou_all.setBackgroundColor(Color.parseColor("#99000000"));
		tv_alertdialog_title.setText("没昵称我  \"蓝瘦\"");
		layou_all.setOnClickListener(this);
		lLayout_bg.setOnClickListener(this);
		iv_alertdialog_close.setOnClickListener(this);
		btn_alertdialog_refuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommUtil.updateUser(spUtil.getString("cellphone", "0"),
				Global.getIMEI(mContext), mContext,
				spUtil.getInt("userid", 0),
				et_alertdialog_name.getText().toString(),null,
				updateUser);
			}
		});
		
	}
	
	private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					ToastUtil.showToastShortCenter(mContext, "创建成功");
					spUtil.saveString("username",et_alertdialog_name.getText().toString());
				}else {
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(mContext, msg);
				}
				finish();
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
		case R.id.layou_all:
			setResult(Global.RESULT_OK);
			finish();
			break;
		case R.id.iv_alertdialog_close:
			setResult(Global.RESULT_OK);
			finish();
			break;
		}
	}
}
