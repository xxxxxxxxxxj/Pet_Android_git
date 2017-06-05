package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.ButtonType;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
/**
 * 取消订单新版
 * @author Administrator
 *
 */
public class OrderCancleActivity extends SuperActivity implements OnClickListener{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ImageView img_cancle_icon;
	private TextView textView_cancle_one;
	private TextView textView_cancle_two;
	private TextView textView_cancle_thr;
	private TextView textView_cancle_four;
	private LinearLayout layout_cancle_tel;
	private Button button_un_cancle;
	private Button button_cancle;
	private LinearLayout layout_cancle_rule;
	private ImageView img_rule_show;
	private Button button_rule_click;
	private String str = "{'code':0,'data':{'topImg':'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1869402809,2463293159&fm=116&gp=0.jpg','content':['美容师已经为您准备好服务了','取消订单请联系客服哦...','#提前24小时取消订单，可免费取消#'],'buttons':[{'type':'tel','text':'4008700111'}]},'msg':'操作成功'}";
//	private int state = -1;
	private int type = -1;
	private int payWay = -1;
	private int orderid = -1;
	private SharedPreferenceUtil spUtil;
	private Button button_tel;
	private String telPhone="";
	private String ReasonGit="";
	private String rulePage="";
	public static OrderCancleActivity orderCancleActivity;
//	private LinearLayout service_give_resaon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_cancle);
		MApplication.listAppoint.add(this);
		orderCancleActivity = this;
		initView();
		getIntentData();
		initListener();
		setView();
	}
	private void setView() {
		if (type==1) {
			CommUtil.orderCareCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,orderid,0,
					getInsertReminderlog);
		}else if (type==2) {//寄养取消订单原因 先暂时用洗美的--后边需要改成寄养专用的提示
			CommUtil.orderHotelCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,orderid,0,
					getInsertReminderlog);
		}else if (type==4) {
			CommUtil.orderTrainCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,orderid,0,
					getInsertReminderlog);
		}else if (type==3) {
			CommUtil.orderSwimCancelRemindTxt(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(this), 
					this,orderid,0,
					getInsertReminderlog);
		}
		tv_titlebar_title.setText("取消订单");
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(this);
		layout_cancle_tel.setOnClickListener(this);
		button_un_cancle.setOnClickListener(this);
		button_cancle.setOnClickListener(this);
		layout_cancle_rule.setOnClickListener(this);
		img_rule_show.setOnClickListener(this);
		button_rule_click.setOnClickListener(this);
		button_tel.setOnClickListener(this);
	}
	private void getIntentData() {
//		state = getIntent().getIntExtra("state", -1);
		type = getIntent().getIntExtra("type", -1);
		payWay = getIntent().getIntExtra("payWay", -1);
		orderid = getIntent().getIntExtra("orderid", -1);
	}
	private void initView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
//		service_give_resaon = (LinearLayout) findViewById(R.id.service_give_resaon);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		img_cancle_icon = (ImageView) findViewById(R.id.img_cancle_icon);
		textView_cancle_one = (TextView) findViewById(R.id.textView_cancle_one);
		textView_cancle_two = (TextView) findViewById(R.id.textView_cancle_two);
		textView_cancle_thr = (TextView) findViewById(R.id.textView_cancle_thr);
		textView_cancle_four = (TextView) findViewById(R.id.textView_cancle_four);
		layout_cancle_tel = (LinearLayout) findViewById(R.id.layout_cancle_tel);
		button_un_cancle = (Button) findViewById(R.id.button_un_cancle);
		button_cancle = (Button) findViewById(R.id.button_cancle);
		layout_cancle_rule = (LinearLayout) findViewById(R.id.layout_cancle_rule);
		img_rule_show = (ImageView) findViewById(R.id.img_rule_show);
		button_rule_click = (Button) findViewById(R.id.button_rule_click);
		button_tel = (Button) findViewById(R.id.button_tel);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_un_cancle:
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.button_tel:
		case R.id.layout_cancle_tel:
			MDialog mDialog = new MDialog.Builder(mContext).setTitle("提示")
					.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage("是否拨打电话?")
					.setCancelStr("否").setOKStr("是")
					.positiveListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 确认拨打电话
							Utils.telePhoneBroadcast((Activity)mContext,telPhone);
						}
					}).build();
			mDialog.show();
			break;
		case R.id.button_cancle:
//			if (type==2) {//寄养取消订单
//				CommUtil.prepCancelOrder(spUtil.getString("cellphone", ""), 
//						Global.getIMEI(mContext), mContext, orderid, cancelOrder);
//			}else {//洗美特色服务取消订单
				CommUtil.ReasonCancelOrder(
						spUtil.getString("cellphone", ""), 
						Global.getIMEI(mContext), 
						mContext, 
						orderid,ReasonGit,cancelOrder);
//			}
			break;
		case R.id.img_rule_show:
		case R.id.button_rule_click:
		case R.id.layout_cancle_rule:
			Intent intent = new Intent(mContext, ShopH5DetailActivity.class);
			intent.putExtra("previous", 20145);
			intent.putExtra("rulePage", rulePage);
			startActivity(intent);
			break;
		}
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
						if (objectData.has("topImg")&&!objectData.isNull("topImg")) {
							String topImg = /*CommUtil.getServiceNobacklash()+*/objectData.getString("topImg");
							ImageLoaderUtil.setImage(img_cancle_icon, topImg, R.drawable.icon_image_default);
						}
						if (objectData.has("rulePage")&&!objectData.isNull("rulePage")) {
							rulePage = objectData.getString("rulePage");
						}
						if (objectData.has("content")&&!objectData.isNull("content")) {
							JSONArray array = objectData.getJSONArray("content");
							ArrayList<String> list = new ArrayList<String>();
							for (int i = 0; i < array.length(); i++) {
								list.add(array.getString(i));
							}
							setTextDetail(list);
//							for (int j = 0; j < list.size(); j++) {
//								TextView textView = new TextView(mContext);
//								if (list.get(j).contains("#")) {
//									textView.setTextColor(getResources().getColor(R.color.orange));
//								}else {
//									textView.setTextColor(getResources().getColor(R.color.black));
//								}
//								textView.setText(list.get(j).replace("#", ""));
//								service_give_resaon.addView(textView);
//							}
							
						}
						if (objectData.has("buttons")&&!objectData.isNull("buttons")) {
							JSONArray array = objectData.getJSONArray("buttons");
							ArrayList<ButtonType> buttonList = new ArrayList<ButtonType>();
							for (int i = 0; i < array.length(); i++) {
								ButtonType buttonType = new ButtonType();
								JSONObject objectButtonType = array.getJSONObject(i);
								if (objectButtonType.has("type")&&!objectButtonType.isNull("type")) {
									buttonType.type= objectButtonType.getString("type");
								}
								if (objectButtonType.has("text")&&!objectButtonType.isNull("text")) {
									buttonType.text = objectButtonType.getString("text");
								}
								buttonList.add(buttonType);
							}
							for (int j = 0; j < buttonList.size(); j++) {
								ButtonType Bttype = buttonList.get(j);
								if (Bttype.type.equals("tel")) {
									layout_cancle_tel.setVisibility(View.VISIBLE);
									button_tel.setText(Bttype.text);
									telPhone = Bttype.text;
									button_un_cancle.setVisibility(View.INVISIBLE);
									button_cancle.setVisibility(View.INVISIBLE);
								}else {
									layout_cancle_tel.setVisibility(View.INVISIBLE);
									button_un_cancle.setVisibility(View.VISIBLE);
									button_cancle.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

		private void setTextDetail(ArrayList<String> list) {
			for (int i = 0; i < list.size(); i++) {
				switch (i) {
				case 0:
					textView_cancle_one.setText(list.get(0));
					break;
				case 1:
					textView_cancle_two.setText(list.get(1));
					break;
				case 2:
					textView_cancle_thr.setText(list.get(2).replace("#", ""));
					textView_cancle_thr.setTextColor(getResources().getColor(R.color.aBB996C));
					textView_cancle_four.setVisibility(View.GONE);
					break;
				case 3:
					textView_cancle_four.setVisibility(View.VISIBLE);
					textView_cancle_four.setText(list.get(3).replace("#", ""));
					textView_cancle_thr.setTextColor(getResources().getColor(R.color.black));
					textView_cancle_four.setTextColor(getResources().getColor(R.color.aBB996C));
					break;
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	
	};
	
	// 取消订单
	private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->取消订单信息 : "+new String(responseBody));
			String msg ="";
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						msg =jsonObject.getString("msg");
					}
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						String data = jsonObject.getString("data");
						ToastUtil.showToastShortCenter(mContext, data);
					}
//					if (payWay==1) {
//						ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.weixinpay));
//					}else if (payWay==2) {
//						ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.zhifubaopay));
//					}else if (payWay==3) {
//						ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.coupons));
//					}else if (payWay==4) {
//						ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.yu_ezhifu));
//					}else {
//						ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.hunhezhifu));
//					}
					Intent intentStatus = new Intent();
					intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
					intentStatus.putExtra("index", 1);
					sendBroadcast(intentStatus);
					
					Intent intent = new Intent(mContext, OrderCancleReasonActivity.class);
					intent.putExtra("type", type);
					intent.putExtra("orderid", orderid);
					startActivity(intent);
					finishWithAnimation();
				}else {
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						msg =jsonObject.getString("msg");
						ToastUtil.showToastShortCenter(mContext, msg);
					}
//					showCancleDetail(code,msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(mContext, "连接服务器失败,请检查网络", Toast.LENGTH_SHORT).show();
		}

	};

}
