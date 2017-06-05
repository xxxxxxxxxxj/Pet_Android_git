package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 下单成功后订单列表悬浮窗界面
 * @author Administrator
 *
 */
public class OrderListShowAddMyPet extends SuperActivity{

	private ImageView textView_dlg_cancle;
	private TextView textView_dlg_pop_title;
	private TextView textView_dlg_pop_content;
	private Button button_dlg_pop_press;
	private LinearLayout layout_dlg_all;
	private OrderListShowAddMyPet oAddMyPet;
	
	private ArrayList<MulPetService> mulpsList;
	private int petsize;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dlg_order_all_style);
		oAddMyPet = this;
		findView();
		setView();
		initListener();
		
		mulpsList  =  getIntent().getParcelableArrayListExtra("mulpetservice");
		for (int i = 0; i < mulpsList.size(); i++) {
			Utils.mLogError("==-->mulpsList:=  "+mulpsList.get(i).petName);
		}
	}
	private void getData() {
		// TODO Auto-generated method stub
		CommUtil.getAddPetTipAfterOrder(this,getAddPetTipAfterOrder);
	}
	//订单列表弹窗
	private AsyncHttpResponseHandler getAddPetTipAfterOrder = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->getAddPetTipAfterOrder "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("title")&&!object.isNull("title")) {
							String title = object.getString("title");
							textView_dlg_pop_title.setText(title);
						}
						if (object.has("txt")&&!object.isNull("txt")) {
							String txt = object.getString("txt");
							textView_dlg_pop_content.setText(txt);
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
	private void initListener() {
		// TODO Auto-generated method stub
		textView_dlg_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		layout_dlg_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		button_dlg_pop_press.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (petsize<20) {
					JumpToNext(PetAddActivity.class,Global.ORDERLIST_TO_ADD_PET,mulpsList);
				}
				finishWithAnimation();
			}
		});
		
	}
	private void setView() {
		textView_dlg_pop_title.setText("下单成功了!~");
		textView_dlg_pop_content.setText("将宝贝添加到 “我的宠物” 美容师会更好的服务哦~");
		button_dlg_pop_press.setText("去添加");
		getPets();
		
	}
	private void findView() {
		// TODO Auto-generated method stub
		pDialog = new MProgressDialog(oAddMyPet);
		textView_dlg_cancle = (ImageView) findViewById(R.id.imageView_dlg_cancle);
		textView_dlg_pop_title = (TextView) findViewById(R.id.textView_dlg_pop_title);
		textView_dlg_pop_content = (TextView) findViewById(R.id.textView_dlg_pop_content);
		button_dlg_pop_press = (Button) findViewById(R.id.button_dlg_pop_press);
		layout_dlg_all = (LinearLayout) findViewById(R.id.layout_dlg_all);
		spUtil = SharedPreferenceUtil.getInstance(oAddMyPet);
//		getData();
	}
	
	private void JumpToNext(Class clazz,int requestcode,
			ArrayList<MulPetService> mulpsList) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		intent.putExtra("customerpetid", mulpsList.get(0).petCustomerId);
		Utils.mLogError("==-->");
		Bundle bundle = new Bundle();
//		bundle.putParcelable("mulpsList", mulpsList.get(0));
		bundle.putParcelableArrayList("mulpsList", mulpsList);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestcode);
	}
	private void getPets(){
		pDialog.showDialog();
		String cellphone = spUtil.getString("cellphone", "");
		int userid = spUtil.getInt("userid", 0);
		if(!"".equals(cellphone)&&0<userid){
			petsize = 0;
			CommUtil.queryCustomerPets(this,spUtil.getString("cellphone", ""), 
					Global.getCurrentVersion(oAddMyPet), 
					Global.getIMEI(oAddMyPet), petHandler);
		}
	}
	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
//			Utils.mLogError("获取宠物："+new String(responseBody));
			try {
				pDialog.closeDialog();
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("pets")&&!jdata.isNull("pets")){
						JSONArray jpets = jdata.getJSONArray("pets");
						petsize = jpets.length();
						if (petsize>=20) {
							finishWithAnimation();
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
//			addPetListFooter();
			pDialog.closeDialog();
		}
		
	};
}
