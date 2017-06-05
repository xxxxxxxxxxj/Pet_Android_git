package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class PickupHintActivity extends Activity {
	private RelativeLayout rlMain;
	private Button btSubmit;
	private LinearLayout llMsg;
	private MProgressDialog pDialog;
	private LayoutParams llp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pickuphint);
		findView();
	}

	@SuppressWarnings("null")
	private void findView() {
		pDialog = new MProgressDialog(this);
		rlMain = (RelativeLayout) findViewById(R.id.rl_pickuphint_main);
		rlContent = (RelativeLayout) findViewById(R.id.rl_pickuphint_content);
		btSubmit = (Button) findViewById(R.id.bt_pickuphint_submit);
		tvTitle = (TextView) findViewById(R.id.tv_pickuphint_title);
		llMsg = (LinearLayout) findViewById(R.id.ll_pickuphint_msg);
		
		llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		rlMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		String [] notices = getIntent().getStringArrayExtra("notices");
		if (notices!=null) {
			for (int i = 0; i < notices.length; i++) {
				addMatter(notices[i]);
			}
		}else {
			if(getIntent().getIntExtra("previous", 0)==Global.FOSTERCAREORDER_TO_COUPON){
				getHotelDate();
			}else if(getIntent().getIntExtra("previous", 0)==Global.AREA_TO_HINT){
				setViewLayout(rlContent, Utils.getDisplayMetrics(this)[1]/3);
				tvTitle.setText("温馨提示");
				TextView tv1 = new TextView(this);
				tv1.setText("服务区外仅支持到店服务哦");
				tv1.setTextColor(getResources().getColor(R.color.a757575));
				tv1.setTextSize(16);
				tv1.setGravity(Gravity.CENTER);
				tv1.setLayoutParams(llp);
				llMsg.addView(tv1);
			}else{
				getCareDate();
			}
		}
	}
	
	private void getHotelDate(){
		pDialog.showDialog();
		CommUtil.getHotelPickUpRemindTxt(this,matterHandler);
	}
	private void getCareDate(){
		pDialog.showDialog();
		CommUtil.getCarePickUpRemindTxt(this,matterHandler);
	}
	
	private void setViewLayout(View iv, int height){
		android.view.ViewGroup.LayoutParams lParams = iv.getLayoutParams();
//		lParams.width = width;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}
	
	private AsyncHttpResponseHandler matterHandler = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("注意事项内容："+new String(responseBody));
			pDialog.closeDialog();
			llMsg.removeAllViews();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jData = jobj.getJSONObject("data");
					if(jData.has("txt")&&!jData.isNull("txt")&&
							jData.getJSONArray("txt").length()>0){
						JSONArray jarr = jData.getJSONArray("txt");
						for(int i=0;i<jarr.length();i++){
							addMatter(jarr.getString(i));
						}
					}else{
						setDefaultTxt();
					}
					
				}else{
					setDefaultTxt();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setDefaultTxt();
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			setDefaultTxt();
		}
		
	};
	private TextView tvTitle;
	private RelativeLayout rlContent;
	
	private void addMatter(String str){
		TextView tv1 = new TextView(this);
		tv1.setText(str);
		tv1.setTextColor(getResources().getColor(R.color.a757575));
		tv1.setTextSize(16);
		tv1.setLayoutParams(llp);
		llMsg.addView(tv1);
	}
	
	private void setDefaultTxt(){
		llMsg.removeAllViews();
		TextView tv1 = new TextView(this);
		tv1.setText("网络好像开小差了，请检查网络设置");
		tv1.setTextColor(getResources().getColor(R.color.a888888));
		tv1.setTextSize(16);
		tv1.setGravity(Gravity.CENTER);
		tv1.setLayoutParams(llp);
		
		llMsg.addView(tv1);
	}

	
}
