package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.OrderPayScanInfoAdapter;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * <p>
 * Title:PaySuccessActivity
 * </p>
 * <p>
 * Description:支付成功界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-5-8 上午9:47:42
 */
public class PaySuccessActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private SharedPreferenceUtil spUtil;
	private int previous = 0;
	private int previous_liucheng = 0;
	public static PaySuccessActivity act;
	private TextView content;
	private Button button_to_myfragment;
	private int orderId;
	private int type;
	private RelativeLayout rl_paysuccess_xfje;
	private TextView tv_paysuccess_xfje;
	private TextView tv_paysuccess_xfprice;
	private View vw_paysuccess_xfje;
	private ListView mlv_paysuccess_info;
	/**
	 * start 次卡
	 */
	private LinearLayout layout_card_pay_success;
	private Button button_look_cards;
	private Button button_back_main;
	/**
	 * end 次卡
	 */
	private LinearLayout layout_ximei_pay_success;
	private Button button_ddxq;
	private Button button_wsxx;
	private int sex;
	private int petCustomerId;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();
	private Button button_back_old;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		intiView();
		setView();
		initLinsetr();
	}

	private void initLinsetr() {
		ibBack.setOnClickListener(this);
		button_look_cards.setOnClickListener(this);
		button_back_main.setOnClickListener(this);
		button_ddxq.setOnClickListener(this);
		button_wsxx.setOnClickListener(this);
		button_to_myfragment.setOnClickListener(this);
		button_back_old.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	private void setView() {
		tvTitle.setText("支付成功");
		if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
			content.setVisibility(View.VISIBLE);
			content.setText("办证详情,请到个人中心查看");
		} else if (previous == Global.CARDSDETAIL_TO_ORDERPAY) {// 买次卡
			layout_card_pay_success.setVisibility(View.VISIBLE);
			button_back_old.setVisibility(View.GONE);
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 111 --- "+previous_liucheng);
			if (previous_liucheng>0/*&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD*/) {
				button_back_old.setVisibility(View.VISIBLE);
				layout_card_pay_success.setVisibility(View.GONE);
			}
		} else if (previous == Global.MIPCA_TO_ORDERPAY) {// 商品支付
			rl_paysuccess_xfje.setVisibility(View.VISIBLE);
			tv_paysuccess_xfje.setVisibility(View.VISIBLE);
			tv_paysuccess_xfprice.setVisibility(View.VISIBLE);
			vw_paysuccess_xfje.setVisibility(View.VISIBLE);
			mlv_paysuccess_info.setVisibility(View.VISIBLE);
			double totalPayPrice = getIntent().getDoubleExtra("totalPayPrice",
					0.00);
			ArrayList<OrderPayScanInfo> listItems = (ArrayList<OrderPayScanInfo>) getIntent()
					.getSerializableExtra("listItems");
			String text = "¥ " + Math.round(totalPayPrice);
			SpannableString ss = new SpannableString(text);
			ss.setSpan(new TextAppearanceSpan(this, R.style.style4), 0, 1,
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_paysuccess_xfprice.setText(ss);
			new OrderPayScanInfoAdapter<OrderPayScanInfo>(this, listItems);
			mlv_paysuccess_info
					.setAdapter(new OrderPayScanInfoAdapter<OrderPayScanInfo>(
							this, listItems));
		} else {
			// 洗美，特色服务，寄养，游泳，训犬
			layout_ximei_pay_success.setVisibility(View.VISIBLE);
			Log.e("TAG", "petCustomerId = " + petCustomerId);
			if (petCustomerId > 0) {
				// 获取我的宠物信息
				CommUtil.queryCustomerPets(PaySuccessActivity.this,
						spUtil.getString("cellphone", ""),
						Global.getCurrentVersion(PaySuccessActivity.this),
						Global.getIMEI(PaySuccessActivity.this), petHandler);
			} else {
				LinearLayout.LayoutParams layoutParams = (LayoutParams) button_ddxq
						.getLayoutParams();
				layoutParams.setMargins(0, 0, 0, 0);// 4个参数按顺序分别是左上右下
				button_ddxq.setLayoutParams(layoutParams);
				button_wsxx.setVisibility(View.GONE);
			}
		}
	}

	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("pets") && !jdata.isNull("pets")) {
							petlist.clear();
							JSONArray jpets = jdata.getJSONArray("pets");
							if (jpets.length() > 0) {
								sex = 0;
								for (int i = 0; i < jpets.length(); i++) {
									petlist.add(Pet.json2Entity(jpets
											.getJSONObject(i)));
								}
								for (int j = 0; j < petlist.size(); j++) {
									Pet pet = petlist.get(j);
									if (pet != null) {
										if (petCustomerId == pet.customerpetid) {
											if (pet.sex == -1) {
												sex = pet.sex;
											}
											break;
										}
									}
								}
								if (sex == -1) {
									button_wsxx.setVisibility(View.VISIBLE);
								} else {
									LinearLayout.LayoutParams layoutParams = (LayoutParams) button_ddxq
											.getLayoutParams();
									layoutParams.setMargins(0, 0, 0, 0);// 4个参数按顺序分别是左上右下
									button_ddxq.setLayoutParams(layoutParams);
									button_wsxx.setVisibility(View.GONE);
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	private void initData() {
		act = this;
		spUtil = SharedPreferenceUtil.getInstance(this);
		orderId = getIntent().getIntExtra("orderId", 0);
		type = getIntent().getIntExtra("type", 0);
		previous = getIntent().getIntExtra("previous", 0);
		previous_liucheng = getIntent().getIntExtra("previous_liucheng", 0);
		Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000 --- "+previous_liucheng);
		petCustomerId = getIntent().getIntExtra("petCustomerId", 0);
	}

	private void intiView() {
		setContentView(R.layout.paysuccess);
		content = (TextView) findViewById(R.id.content);
		button_to_myfragment = (Button) findViewById(R.id.button_to_myfragment);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		rl_paysuccess_xfje = (RelativeLayout) findViewById(R.id.rl_paysuccess_xfje);
		tv_paysuccess_xfje = (TextView) findViewById(R.id.tv_paysuccess_xfje);
		tv_paysuccess_xfprice = (TextView) findViewById(R.id.tv_paysuccess_xfprice);
		vw_paysuccess_xfje = (View) findViewById(R.id.vw_paysuccess_xfje);
		mlv_paysuccess_info = (ListView) findViewById(R.id.mlv_paysuccess_info);
		layout_card_pay_success = (LinearLayout) findViewById(R.id.layout_card_pay_success);
		button_look_cards = (Button) findViewById(R.id.button_look_cards);
		button_back_main = (Button) findViewById(R.id.button_back_main);
		layout_ximei_pay_success = (LinearLayout) findViewById(R.id.layout_ximei_pay_success);
		button_ddxq = (Button) findViewById(R.id.button_ddxq);
		button_wsxx = (Button) findViewById(R.id.button_wsxx);
		button_back_old = (Button) findViewById(R.id.button_back_old);
	}

	private void sendToMian() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		if (previous == Global.CARDSDETAIL_TO_ORDERPAY) {
			intent.putExtra("previous", Global.CARDS_TO_MAINACTIVITY);
		} else {
			intent.putExtra("previous",
					Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY);
		}
		sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
		finishWithAnimation();
	}

	private void sendToMyFragment() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PAYSUCCESS_TO_MYFRAGMENT);
		sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
		finishWithAnimation();
	}

	private void sendToMyCardFragment() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
		sendBroadcast(intent);
		Utils.mLogError("sendToMyCardFragment 开始发送广播");
		if (PetInfoDeatilActivity.act != null)
			PetInfoDeatilActivity.act.finishWithAnimation();
		finishWithAnimation();
	}
	private void sendToBroad(String action,int previous_liucheng){
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra("previous_liucheng",previous_liucheng);
		sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
		finishWithAnimation();
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PaySuccessActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
		if (previous == Global.BATH_TO_ORDERPAY
				|| previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY
				|| previous == Global.SWIM_DETAIL_TO_ORDERDETAIL
				|| previous == Global.URGENT_TO_ORDERDETAIL) {
			if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {
				content.setVisibility(View.VISIBLE);
				content.setText("您的订单已支付成功");
			}
			removeTopActivity();
			clearData();
		}else if(previous==Global.CARDSDETAIL_TO_ORDERPAY){
			if (previous_liucheng<=0/*&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD*/) {
				removeActivity();
			}
		} 
		else {
			removeActivity();
			clearData();
		}
	}

	private void removeTopActivity() {
		for (int i = 0; i < MApplication.listAppoint.size(); i++) {
			MApplication.listAppoint.get(i).finish();
		}
		MApplication.listAppoint.clear();
		try {
			if (UpdateOrderConfirmActivity.act != null)
				UpdateOrderConfirmActivity.act.finish();
			if (OrderDetailActivity.act != null)
				OrderDetailActivity.act.finish();
			if (AppointmentActivity.act != null)
				AppointmentActivity.act.finish();
			if (ServiceActivity.act != null)
				ServiceActivity.act.finish();
			if (ServiceFeature.act != null)
				ServiceFeature.act.finishWithAnimation();
			if (CharacteristicServiceActivity.act != null)
				CharacteristicServiceActivity.act.finish();
			if (PetInfoActivity.act != null)
				PetInfoActivity.act.finish();
			if (MainToBeauList.act != null)
				MainToBeauList.act.finish();
			if (BeauticianDetailActivity.act != null)
				BeauticianDetailActivity.act.finish();
			if (OrderDetailFromOrderActivity.act != null)
				OrderDetailFromOrderActivity.act.finish();

			if (SwimAppointmentActivity.act != null) {
				SwimAppointmentActivity.act.finish();
			}
			if (SwimDetailActivity.act != null) {
				SwimDetailActivity.act.finish();
			}
			if (SelectTimeOrUrgentActivity.act != null) {
				SelectTimeOrUrgentActivity.act.finish();
			}
			if (TrainAppointMentActivity.trainAppointMentActivity != null) {
				TrainAppointMentActivity.trainAppointMentActivity.finish();
			}
			if (FostercareOrderConfirmActivity.act != null) {
				FostercareOrderConfirmActivity.act.finish();
			}
			if (UpdateOrderConfirmActivity.act != null) {
				UpdateOrderConfirmActivity.act.finish();
			}
			if (OrderDetailFromOrderToConfirmActivity.orderConfirm != null) {
				OrderDetailFromOrderToConfirmActivity.orderConfirm.finish();
			}
			if (ADActivity.act != null) {
				ADActivity.act.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeActivity() {
		for (int i = 0; i < MApplication.listAppoint.size(); i++) {
			MApplication.listAppoint.get(i).finish();
		}
		MApplication.listAppoint.clear();
		try {
			if (UpdateOrderConfirmActivity.act != null)
				UpdateOrderConfirmActivity.act.finish();
			if (ServiceActivity.act != null)
				ServiceActivity.act.finishWithAnimation();
			if (ServiceFeature.act != null)
				ServiceFeature.act.finishWithAnimation();
			if (AppointmentActivity.act != null)
				AppointmentActivity.act.finishWithAnimation();
			if (CharacteristicServiceActivity.act != null)
				CharacteristicServiceActivity.act.finish();
			if (PetInfoActivity.act != null)
				PetInfoActivity.act.finish();
			if (MainToBeauList.act != null)
				MainToBeauList.act.finish();
			if (BeauticianDetailActivity.act != null)
				BeauticianDetailActivity.act.finish();
			if (OrderDetailFromOrderActivity.act != null)
				OrderDetailFromOrderActivity.act.finish();
			if (SwimAppointmentActivity.act != null) {
				SwimAppointmentActivity.act.finish();
			}
			if (SwimDetailActivity.act != null) {
				SwimDetailActivity.act.finish();
			}
			if (SelectTimeOrUrgentActivity.act != null) {
				SelectTimeOrUrgentActivity.act.finish();
			}
			if (TrainAppointMentActivity.trainAppointMentActivity != null) {
				TrainAppointMentActivity.trainAppointMentActivity.finish();
			}
			if (FostercareOrderConfirmActivity.act != null) {
				FostercareOrderConfirmActivity.act.finish();
			}
			if (CardsDetailActivity.detailActivity != null) {
				CardsDetailActivity.detailActivity.finish();
			}
			if (UpdateOrderConfirmActivity.act != null) {
				UpdateOrderConfirmActivity.act.finish();
			}
			if (OrderDetailFromOrderToConfirmActivity.orderConfirm != null) {
				OrderDetailFromOrderToConfirmActivity.orderConfirm.finish();
			}
			if (ADActivity.act != null) {
				ADActivity.act.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PaySuccessActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
														// onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void clearData() {
		spUtil.removeData("newaddrid");
		spUtil.removeData("newaddr");
		spUtil.removeData("newlat");
		spUtil.removeData("newlng");
		spUtil.removeData("isAccept");
		spUtil.removeData("charactservice");
		spUtil.removeData("changepet");
		spUtil.removeData("newpetkindid");
		spUtil.removeData("newpetname");
		spUtil.removeData("newpetimg");
		spUtil.removeData("newaddrid");
		spUtil.removeData("newaddr");
		spUtil.removeData("newlat");
		spUtil.removeData("newlng");
		spUtil.removeData("isAccept");
		spUtil.removeData("charactservice");
		spUtil.removeData("changepet");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			if (previous_liucheng>0&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.OrderDetailActivity");
			}else if (previous_liucheng>0&&previous_liucheng==Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.UpdateOrderConfirmActivity");
			}
			finishWithAnimation();
			break;
		case R.id.button_back_old:
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 222 --- "+previous_liucheng);
			if (previous_liucheng>0&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.OrderDetailActivity");
			}else if (previous_liucheng>0&&previous_liucheng==Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.UpdateOrderConfirmActivity");
			}
			finishWithAnimation();
			break;
		case R.id.button_look_cards:
			sendToMian();
			Intent intent = new Intent(mContext, MyCardsActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.button_back_main:
			sendToMian();
			finish();
			break;
		case R.id.button_ddxq:
			if (previous != Global.MIPCA_TO_ORDERPAY) {
				if (previous != Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
					sendToMian();
				}
				Intent intent1 = null;
				if (type == 4 || previous == Global.MAIN_TO_TRAIN_DETAIL) {// 从训练支付成功的进入复用寄养详情
					intent1 = new Intent(mContext,
							OrderFosterDetailActivity.class);
					intent1.putExtra("orderid", orderId);
					startActivity(intent1);
					finishWithAnimation();
				} else if (previous == Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE) {
					finishWithAnimation();
				} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
					sendToMyCardFragment();
				} else {// 其他的复用洗美详情
					intent1 = new Intent(mContext,
							OrderDetailFromOrderToConfirmActivity.class);
					intent1.putExtra("orderid", orderId);
					startActivity(intent1);
					finishWithAnimation();
				}
			}
			break;
		case R.id.button_wsxx:
			startActivity(new Intent(this, PetAddActivity.class).putExtra(
					"customerpetid", petCustomerId).putExtra("previous",
					Global.PETINFO_TO_EDITPET));
			break;
		case R.id.button_to_myfragment:
			sendToMyFragment();
			break;
		}
	}
	private void SuccessClickSendBroadCast(String str){
		try {
			if (ADActivity.act != null) {
				ADActivity.act.finish();
			}
			if (CardsDetailActivity.detailActivity!=null) {
				CardsDetailActivity.detailActivity.finish();
			}
			if (OrderPayActivity.act!=null) {
				OrderPayActivity.act.finish();
			}
			sendToBroad(str,previous_liucheng);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (previous_liucheng>0&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.OrderDetailActivity");
			}else if (previous_liucheng>0&&previous_liucheng==Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
				SuccessClickSendBroadCast("android.intent.action.UpdateOrderConfirmActivity");
			}
			finishWithAnimation();
		}
		return super.onKeyDown(keyCode, event);
	}

}
