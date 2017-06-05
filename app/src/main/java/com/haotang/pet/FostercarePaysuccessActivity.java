package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class FostercarePaysuccessActivity extends SuperActivity {
	private LinearLayout llMsg;
	private Button btSubmit;
	private MProgressDialog pDialog;
	private ArrayList<MulPetService> mpsList;
	private SharedPreferenceUtil spUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercare_paysuccess);
		
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		llMsg = (LinearLayout) findViewById(R.id.ll_fostercarepaysuccess_msg);
		btSubmit = (Button) findViewById(R.id.bt_fostercarepaysuccess_submit);
		llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mpsList = getIntent().getParcelableArrayListExtra("mulpetservice");
		if(mpsList!=null&&mpsList.size()>0){
			for(int i=0;i<mpsList.size();i++)
				mpsList.get(i).serviceName="寄养套餐";
		}
		
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(mpsList!=null&&mpsList.size()>0&&mpsList.get(0).petCustomerId<=0){
//					getPets();
//				}else{
				Intent intent = new Intent(mContext, OrderFosterDetailActivity.class);
				intent.putExtra("orderid", mpsList.get(0).orderid);
				startActivity(intent);
				finishWithAnimation();
//				}
			}
		});
		
		sendToMian();
		getDate();
	}
	
	private void getDate(){
		pDialog.showDialog();
		CommUtil.getMatters(this,matterHandler);
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
					if(jData.has("notice")&&!jData.isNull("notice")&&
							jData.getJSONArray("notice").length()>0){
						JSONArray jarr = jData.getJSONArray("notice");
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
	
	private void getPets(){
		pDialog.showDialog();
		CommUtil.queryCustomerPets(this,spUtil.getString("cellphone", ""), 
				Global.getCurrentVersion(this), 
				Global.getIMEI(this), petHandler);
	}
	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("获取宠物列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("pets")&&!jdata.isNull("pets")){
						JSONArray jpets = jdata.getJSONArray("pets");
						if(jpets.length()<20){
							goAddPetHint();
						}else{
							finishWithAnimation();
						}
					}else{
						goAddPetHint();
					}
				}else{
					finishWithAnimation();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				finishWithAnimation();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			finishWithAnimation();
		}
		
	};
	
	private void goAddPetHint(){
		Intent intent = new Intent(this, OrderListShowAddMyPet.class);
		intent.putParcelableArrayListExtra("mulpetservice", mpsList);
		startActivity(intent);
		finishWithAnimation();
	}
	
	private LayoutParams llp;
	
	private void addMatter(String str){
		TextView tv1 = new TextView(this);
		tv1.setText(str);
		tv1.setTextColor(getResources().getColor(R.color.a888888));
		tv1.setTextSize(18);
		tv1.setLayoutParams(llp);
		llMsg.addView(tv1);
	}
	
	private void setDefaultTxt(){
		llMsg.removeAllViews();
		TextView tv1 = new TextView(this);
		tv1.setText("请注意以下事项：");
		tv1.setTextColor(getResources().getColor(R.color.a888888));
		tv1.setTextSize(18);
		tv1.setLayoutParams(llp);
		TextView tv2 = new TextView(this);
		tv2.setText("1、建议您自带牵引绳和项圈（猫咪不需要）；");
		tv2.setTextColor(getResources().getColor(R.color.a888888));
		tv2.setTextSize(18);
		tv2.setLayoutParams(llp);
		TextView tv3 = new TextView(this);
		tv3.setText("2、如果您选择了自带宠粮，为了爱宠健康，请携带爱宠的的饭盆、水盆等；");
		tv3.setTextColor(getResources().getColor(R.color.a888888));
		tv3.setTextSize(18);
		tv3.setLayoutParams(llp);
		TextView tv4 = new TextView(this);
		tv4.setText("3、请携带您爱宠的防疫证明；");
		tv4.setTextColor(getResources().getColor(R.color.a888888));
		tv4.setTextSize(18);
		tv4.setLayoutParams(llp);
		TextView tv5 = new TextView(this);
		tv5.setText("4、为保证寄养过程中爱宠的健康，请提前或当天给爱宠洗个澡哦！");
		tv5.setTextColor(getResources().getColor(R.color.a888888));
		tv5.setTextSize(18);
		tv5.setLayoutParams(llp);
		llMsg.addView(tv1);
		llMsg.addView(tv2);
		llMsg.addView(tv3);
		llMsg.addView(tv4);
		llMsg.addView(tv5);
	}
	
	private void sendToMian(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY);
		sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
	}
	
}
