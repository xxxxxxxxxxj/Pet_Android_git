package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.UpdateAdapter;
import com.haotang.pet.entity.CardItems;
import com.haotang.pet.entity.CardsConfirmOrder;
import com.haotang.pet.entity.ComplaintReason;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.UpdateEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;

/**
 * <p>
 * Title:UpdateOrderConfirmActivity
 * </p>
 * <p>
 * Description:订单升级确认页
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-4-1 下午2:27:51
 */
public class UpdateOrderConfirmActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private MProgressDialog pDialog;
	private TextView tv_upgradeservice_orderprice;
	private TextView tv_upgradeservice_addrtype;
	private TextView tv_upgradeservice_cash;
	private TextView tv_upgradeservice_firstorder;
	private TextView tv_upgradeservice_servicename;
	private TextView tv_upgradeservice_servicecontent;
	private LinearLayout ll_upgradeservice_addservice_items;
	private TextView tv_upgradeservice_addservice_items;
	private MListview lv_upgradeservice_serviceitems;
	private RelativeLayout rl_upgradeorder_confirm_fwkprice;
	private TextView tv_upgradeorder_confirm_fwkprice;
	private TextView tv_upgradeorder_confirm_sfk;
	private TextView tv_upgradeorder_confirm_sjdd;
	private TextView tv_upgradeorder_confirm_fwkzf;
	private TextView tv_upgradeorder_confirm_sfksubmit;
	private Button btn_upgradeorder_confirm_submit;
	private int orderid;
	
	private ImageView image_card_right;
	private LinearLayout layout_cards;
	private LinearLayout layout_refund;
	private MListview lv_upgradeservice_refund;
	private TextView tv_upgradeorder_confirm_fwk;
	private ArrayList<UpdateEntity> allUpdataPrice = new ArrayList<UpdateEntity>();//使用次卡集合动态差价展示集合
	private ArrayList<UpdateEntity> allNoCardUpdataPrice = new ArrayList<UpdateEntity>();//不使用次卡集合动态差价展示集合
	private ArrayList<UpdateEntity> BackUpdataPrice = new ArrayList<UpdateEntity>();//不使用次卡退款集合
	private ArrayList<UpdateEntity> BackCardUpdataPrice = new ArrayList<UpdateEntity>();//使用次卡退款集合
	private UpdateAdapter updateAdapter;
	private UpdateAdapter updateBackAdapter;
	private TextView textview_show_last_price;
	private TextView textview_show_last_price_bottom;
	
	private int serviceType = -1;
	private int PetId = -1;
	private int WorkerLevelId = -1;
	private int washBeauOrSwim = 1;
	private int serviceLoc = -1;
	private int WorkerId = -1;
	private ArrayList<CardsConfirmOrder> cardsConfirmOrders = new ArrayList<CardsConfirmOrder>();
	private int bathNum=0;
	private int beauNum=0;
	private int feature=0;
	private ArrayList<MulPetService> mpsList = new ArrayList<MulPetService>();
	private double needMoney=0;
	private double totalMoney=0;
	private double cutDownPrice = 0;
	private double CardsCutMoney = 0;
	private String tip = "";
	private int paytype = 0;
	private double updateTotalPrice = 0;
	private double mainTotalPrice = 0;
	private double basicPrice = 0;
	private int orderNo;
	private ArrayList<Integer> listIds = new ArrayList<Integer>();
	public static SuperActivity act;
	private String beauty0useCardPayTag="";
	private double beauty0NeedMoney = 0;
	private String beauty0beautyLastPrice = "";
	private double beauty0refundAmount = 0;
	private String beauty0refundTag = "";
	private String beauty0beautyListPrice = "";
	private String beauty0CardAndRefundTAG="";
	
	private String beauty1useCardPayTag="";
	private double beauty1NeedMoney = 0;
	private String beauty1beautyLastPrice = "";
	private double beauty1refundAmount = 0;
	private String beauty1refundTag = "";
	private String beauty1beautyListPrice = "";
	private String beauty1CardAndRefundTAG="";
	
	private boolean isUsedCard = false;
	
	private View view_last_price;
	private String appointment = "";
	
	private boolean isHasBeau0 = true;
	private boolean isHasBeau1 = true;
	private double lastPrice = 0;//beau0 和 beau1都不存在的时候
	
	private TextView tv_upgradeservice_serviceitemsname;
	private View view_up_line;
	private TextView textview_last_price_name_top;
	private TextView textview_last_price_name_bottom;
	private TextView textview_give_other_money;
	private String buyUrl = "";
	private MyReceiver receiver;
	private int previous_liucheng = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_order_confirm);
		act = this;
		MApplication.listAppoint.add(this);
		allUpdataPrice.clear();
		allNoCardUpdataPrice.clear();
		initData();
		initView();
		setLinster();
		initReceiver();
		setView();
		setAdapter();
		getData();
	}
	private void initReceiver() {
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.UpdateOrderConfirmActivity");
		registerReceiver(receiver, filter);
	}

	private void setAdapter() {
		updateAdapter = new UpdateAdapter<UpdateEntity>(mContext, allUpdataPrice);//待升级
		lv_upgradeservice_serviceitems.setAdapter(updateAdapter);
		
		updateBackAdapter = new UpdateAdapter<UpdateEntity>(mContext, BackUpdataPrice);//相关退款
		lv_upgradeservice_refund.setAdapter(updateBackAdapter);
	}

	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_upgradeservice_orderprice = (TextView) findViewById(R.id.tv_upgradeservice_orderprice);
		tv_upgradeservice_addrtype = (TextView) findViewById(R.id.tv_upgradeservice_addrtype);
		tv_upgradeservice_cash = (TextView) findViewById(R.id.tv_upgradeservice_cash);
		tv_upgradeservice_firstorder = (TextView) findViewById(R.id.tv_upgradeservice_firstorder);
		tv_upgradeservice_servicename = (TextView) findViewById(R.id.tv_upgradeservice_servicename);
		tv_upgradeservice_servicecontent = (TextView) findViewById(R.id.tv_upgradeservice_servicecontent);
		ll_upgradeservice_addservice_items = (LinearLayout) findViewById(R.id.ll_upgradeservice_addservice_items);
		tv_upgradeservice_addservice_items = (TextView) findViewById(R.id.tv_upgradeservice_addservice_items);
		lv_upgradeservice_serviceitems = (MListview) findViewById(R.id.lv_upgradeservice_serviceitems);
		rl_upgradeorder_confirm_fwkprice = (RelativeLayout) findViewById(R.id.rl_upgradeorder_confirm_fwkprice);
		tv_upgradeorder_confirm_fwkprice = (TextView) findViewById(R.id.tv_upgradeorder_confirm_fwkprice);
		tv_upgradeorder_confirm_sfk = (TextView) findViewById(R.id.tv_upgradeorder_confirm_sfk);
		tv_upgradeorder_confirm_sjdd = (TextView) findViewById(R.id.tv_upgradeorder_confirm_sjdd);
		tv_upgradeorder_confirm_fwkzf = (TextView) findViewById(R.id.tv_upgradeorder_confirm_fwkzf);
		tv_upgradeorder_confirm_sfksubmit = (TextView) findViewById(R.id.tv_upgradeorder_confirm_sfksubmit);
		btn_upgradeorder_confirm_submit = (Button) findViewById(R.id.btn_upgradeorder_confirm_submit);
		
		image_card_right = (ImageView) findViewById(R.id.image_card_right);
		layout_cards = (LinearLayout) findViewById(R.id.layout_cards);
		layout_refund = (LinearLayout) findViewById(R.id.layout_refund);
		lv_upgradeservice_refund = (MListview) findViewById(R.id.lv_upgradeservice_refund);
		tv_upgradeorder_confirm_fwk = (TextView) findViewById(R.id.tv_upgradeorder_confirm_fwk);
		textview_show_last_price = (TextView) findViewById(R.id.textview_show_last_price);
		textview_show_last_price_bottom = (TextView) findViewById(R.id.textview_show_last_price_bottom);
		tv_upgradeservice_serviceitemsname = (TextView) findViewById(R.id.tv_upgradeservice_serviceitemsname);
		view_up_line = (View) findViewById(R.id.view_up_line);
		textview_last_price_name_top = (TextView) findViewById(R.id.textview_last_price_name_top);
		textview_last_price_name_bottom = (TextView) findViewById(R.id.textview_last_price_name_bottom);

		view_last_price = (View) findViewById(R.id.view_last_price);
		textview_give_other_money = (TextView) findViewById(R.id.textview_give_other_money);
	}

	private void getData() {
		pDialog.showDialog();
		textview_give_other_money.setVisibility(View.GONE);
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderid, reasonHandler);
	}

	private AsyncHttpResponseHandler reasonHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result) {
		pDialog.closeDialog();
		try {
//			Gson gson = new Gson();
//			ComplaintReason complaintReason = gson.fromJson(result,ComplaintReason.class);
//			if (complaintReason != null) {
//				
//			}
			JSONObject object = new JSONObject(result);
			int code = object.getInt("code");
			if (code==0) {
				if (object.has("data")&&!object.isNull("data")) {
					MulPetService mulPetService = new MulPetService();
					JSONObject objectData = object.getJSONObject("data");
					if (objectData.has("totalPrice")&&!objectData.isNull("totalPrice")) {
						double oldnoUpdateOrderPrice = objectData.getInt("totalPrice");
						tv_upgradeservice_orderprice.setText("¥"+oldnoUpdateOrderPrice);
					}
					if (objectData.has("serviceLoc")&&!objectData.isNull("serviceLoc")) {
						serviceLoc = objectData.getInt("serviceLoc");
					}
					if (objectData.has("locTag")&&!objectData.isNull("locTag")) {
						tv_upgradeservice_addrtype.setVisibility(View.VISIBLE);
						tv_upgradeservice_addrtype.setText(objectData.getString("locTag"));
						if (serviceLoc==1) {//到店 
							tv_upgradeservice_addrtype.setBackgroundDrawable(Utils.getDW("FAA04A"));
						}else if(serviceLoc==2) {//上门
							tv_upgradeservice_addrtype.setBackgroundDrawable(Utils.getDW("2FC0F0"));
						}
					}else{
						tv_upgradeservice_addrtype.setVisibility(View.GONE);
					}
					StringBuilder builderCard = new StringBuilder();
					if (objectData.has("pet")&&!objectData.isNull("pet")) {
						JSONObject objectPet = objectData.getJSONObject("pet");
//						if (objectPet.has("PetId")&&!objectPet.isNull("PetId")) {
//							PetId = objectPet.getInt("PetId");
//							mulPetService.petId = PetId;
//						}
						if (objectPet.has("petName")&&!objectPet.isNull("petName")) {
							String petName = objectPet.getString("petName");
							if (objectData.has("petServicePojo")&&!objectData.isNull("petServicePojo")) {
								JSONObject objectPetService = objectData.getJSONObject("petServicePojo");
								if (objectPetService.has("name")&&!objectPetService.isNull("name")) {
									String ServiceName = objectPetService.getString("name");
									tv_upgradeservice_servicename.setText(petName+ServiceName);
								}
								StringBuilder builder = new StringBuilder();
								if (objectPetService.has("serviceItem")&&!objectPetService.isNull("serviceItem")) {
									JSONArray jsonArray  = objectPetService.getJSONArray("serviceItem");
									if (jsonArray.length()>0) {
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objectEver = jsonArray.getJSONObject(i);
											if (objectEver.has("itemName")&&!objectEver.isNull("itemName")) {
												String itemName = objectEver.getString("itemName");
												if (i==jsonArray.length()-1) {
													builder.append(itemName);
												}else {
													builder.append(itemName+" ");
												}
											}
											
										}
									}
									
								}
								try {
									tv_upgradeservice_servicecontent.setText(builder.toString());
								} catch (Exception e) {
									e.printStackTrace();
									tv_upgradeservice_servicecontent.setVisibility(View.GONE);
								}
							}
						}
					}
					StringBuilder builder = new StringBuilder();
					if (objectData.has("extraServiceItems")&&!objectData.isNull("extraServiceItems")) {
						ll_upgradeservice_addservice_items.setVisibility(View.VISIBLE);
						JSONArray arrayOutItems = objectData.getJSONArray("extraServiceItems");
						if (arrayOutItems.length()>0) {
							for (int i = 0; i < arrayOutItems.length(); i++) {
								JSONObject objectEver = arrayOutItems.getJSONObject(i);
								if (objectEver.has("name")&&!objectEver.isNull("name")) {
									String name = objectEver.getString("name");
									if (i==arrayOutItems.length()-1) {
										builder.append(name);
									}else {
										builder.append(name+" ");
									}
								}
							}
						}
					}else {
						ll_upgradeservice_addservice_items.setVisibility(View.GONE);
					}
					try {
						tv_upgradeservice_addservice_items.setText(builder.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ll_upgradeservice_addservice_items.setVisibility(View.GONE);
						tv_upgradeservice_addservice_items.setVisibility(View.GONE);
					}
					allUpdataPrice.clear();
					allNoCardUpdataPrice.clear();
					if (objectData.has("updateOrder")&&!objectData.isNull("updateOrder")) {
						JSONObject objectUpdata = objectData.getJSONObject("updateOrder");
						if (objectUpdata.has("extraServiceItems")&&!objectUpdata.isNull("extraServiceItems")) {
							JSONArray array = objectUpdata.getJSONArray("extraServiceItems");
							if (array.length()>0) {
								for (int i = 0; i < array.length(); i++) {
									UpdateEntity updateEntity = new UpdateEntity();
									JSONObject objectEver = array.getJSONObject(i);
									if (objectEver.has("name")&&!objectEver.isNull("name")) {
										updateEntity.title = objectEver.getString("name");
									}
									if (objectEver.has("listPrice")&&!objectEver.isNull("listPrice")) {
										updateEntity.name = "¥"+objectEver.getInt("listPrice");
									}
									allUpdataPrice.add(updateEntity);
									allNoCardUpdataPrice.add(updateEntity);
								}
							}
						}
						if (objectUpdata.has("appointment")&&!objectUpdata.isNull("appointment")) {
							appointment = objectUpdata.getString("appointment");
						}
						if (objectUpdata.has("petId")&&!objectUpdata.isNull("petId")) {
							PetId = objectUpdata.getInt("petId");
							mulPetService.petId = PetId;
						}
						if (objectUpdata.has("basicPrice")&&!objectUpdata.isNull("basicPrice")) {
							basicPrice = objectUpdata.getDouble("basicPrice");
							mulPetService.basefee = basicPrice;
						}
						if (objectUpdata.has("petService")&&!objectUpdata.isNull("petService")) {
							serviceType = objectUpdata.getInt("petService");
							mulPetService.serviceType = serviceType;
							mulPetService.serviceId = serviceType;
							if (objectData.has("beautyItem")&&!objectData.isNull("beautyItem")) {
								serviceType = 2;
								mulPetService.serviceType = serviceType;
							}
							builderCard.append(PetId+"_"+serviceType);
						}
						if (objectUpdata.has("OrderId")&&!objectUpdata.isNull("OrderId")) {
							orderNo = objectUpdata.getInt("OrderId");
						}
						if (objectUpdata.has("totalPrice")&&!objectUpdata.isNull("totalPrice")) {
							totalMoney = objectUpdata.getDouble("totalPrice");
						}
						if (objectUpdata.has("payWay")&&!objectUpdata.isNull("payWay")) {
							paytype = objectUpdata.getInt("payWay");
						}
					}
					if (objectData.has("homeItem")&&!objectData.isNull("homeItem")) {
						JSONObject objectHomeItem = objectData.getJSONObject("homeItem");
						UpdateEntity updateEntity = new UpdateEntity();
						if (objectHomeItem.has("name")&&!objectHomeItem.isNull("name")) {
							updateEntity.title = objectHomeItem.getString("name");
						}
						if (objectHomeItem.has("fee")&&!objectHomeItem.isNull("fee")) {
							updateEntity.name = "¥"+objectHomeItem.getInt("fee");
						}
						allUpdataPrice.add(updateEntity);
						allNoCardUpdataPrice.add(updateEntity);
					}
					if (objectData.has("petItem")&&!objectData.isNull("petItem")) {
						JSONObject objectPetItem = objectData.getJSONObject("petItem");
						UpdateEntity updateEntity = new UpdateEntity();
						if (objectPetItem.has("name")&&!objectPetItem.isNull("name")) {
							updateEntity.title = objectPetItem.getString("name");
						}
						if (objectPetItem.has("price")&&!objectPetItem.isNull("price")) {
							updateEntity.name = "¥"+objectPetItem.getInt("price");
						}
						allUpdataPrice.add(updateEntity);
						allNoCardUpdataPrice.add(updateEntity);
					}
					if (objectData.has("worker")&&!objectData.isNull("worker")) {
						JSONObject objectWork = objectData.getJSONObject("worker");
						if (objectWork.has("WorkerId")&&!objectWork.isNull("WorkerId")) {
							WorkerId = objectWork.getInt("WorkerId");
						}
						if (objectWork.has("level")&&!objectWork.isNull("level")) {
							JSONObject objectLevel = objectWork.getJSONObject("level");
							if (objectLevel.has("WorkerLevelId")&&!objectLevel.isNull("WorkerLevelId")) {
								WorkerLevelId = objectLevel.getInt("WorkerLevelId");
								mulPetService.clicksort=WorkerLevelId;
							}
						}
					}
//					if (objectData.has("mainTotalPrice")&&!objectData.isNull("mainTotalPrice")) {
//						mainTotalPrice = objectData.getDouble("mainTotalPrice");
//					}
//					if (objectData.has("updateTotalPrice")&&!objectData.isNull("updateTotalPrice")) {
//						updateTotalPrice = objectData.getDouble("updateTotalPrice");
//						cutDownPrice = updateTotalPrice - mainTotalPrice;
//						totalMoney = cutDownPrice;
//						needMoney = totalMoney;
//						setLastPrice(needMoney);
//					}
					
					if (objectData.has("updateOrderFeeObj")&&!objectData.isNull("updateOrderFeeObj")) {
						JSONObject objectFeeObj = objectData.getJSONObject("updateOrderFeeObj");
						//beauty0 用户不使用次卡支付美容套餐
						if (objectFeeObj.has("card0")&&!objectFeeObj.isNull("card0")) {
							isHasBeau0 = true;
							JSONObject objectBeauty0 = objectFeeObj.getJSONObject("card0");
							if (objectBeauty0.has("useCardPayTag")&&!objectBeauty0.isNull("useCardPayTag")) {
								beauty0useCardPayTag = objectBeauty0.getString("useCardPayTag");
							}
							if (objectBeauty0.has("refundItem")&&!objectBeauty0.isNull("refundItem")) {
								JSONArray array = objectBeauty0.getJSONArray("refundItem");
								if (array.length()>0) {
									for (int i = 0; i < array.length(); i++) {
										UpdateEntity upBackEntity = new UpdateEntity();
										JSONObject objectEva = array.getJSONObject(i);
										if (objectEva.has("title")&&!objectEva.isNull("title")) {
											upBackEntity.title = objectEva.getString("title");
										}
										if (objectEva.has("name")&&!objectEva.isNull("name")) {
											upBackEntity.name = objectEva.getString("name");
										}
										BackUpdataPrice.add(upBackEntity);
									}
								}
							}
							if (objectBeauty0.has("payPrice")&&!objectBeauty0.isNull("payPrice")) {
								beauty0NeedMoney = objectBeauty0.getDouble("payPrice");
							}
							if (objectBeauty0.has("lastPrice")&&!objectBeauty0.isNull("lastPrice")) {
								beauty0beautyLastPrice = objectBeauty0.getString("lastPrice");
							}
							if (objectBeauty0.has("refundAmount")&&!objectBeauty0.isNull("refundAmount")) {
								beauty0refundAmount = objectBeauty0.getDouble("refundAmount");
							}
							if (objectBeauty0.has("refundTag")&&!objectBeauty0.isNull("refundTag")) {
								beauty0refundTag = objectBeauty0.getString("refundTag");
							}
							if (objectBeauty0.has("listPrice")&&!objectBeauty0.isNull("listPrice")) {
								beauty0beautyListPrice = objectBeauty0.getString("listPrice");
							}
							UpdateEntity entity = new UpdateEntity();
							if (objectData.has("beautyItem")&&!objectData.isNull("beautyItem")) {//不使用次卡
								JSONObject objectBeautyItem = objectData.getJSONObject("beautyItem");
								if (objectBeautyItem.has("name")&&!objectBeautyItem.isNull("name")) {
									entity.title = objectBeautyItem.getString("name");
									if (!TextUtils.isEmpty(beauty0beautyLastPrice)) {
										entity.name = beauty0beautyLastPrice+"\n"+beauty0beautyListPrice;
									}else {
										entity.name = beauty0beautyListPrice;
									}
								}
							}
							allNoCardUpdataPrice.add(entity);
							beauty0CardAndRefundTAG = beauty0useCardPayTag+""+beauty0refundTag;
							
						}else {
							isHasBeau0 = false;
						}
						//beauty1 用户使用次卡支付美容套餐
						if (objectFeeObj.has("card1")&&!objectFeeObj.isNull("card1")) {
							isHasBeau1 = true;
							JSONObject objectBeauty1 = objectFeeObj.getJSONObject("card1");
							
							if (objectBeauty1.has("useCardPayTag")&&!objectBeauty1.isNull("useCardPayTag")) {
								beauty1useCardPayTag = objectBeauty1.getString("useCardPayTag");
							}
							if (objectBeauty1.has("refundItem")&&!objectBeauty1.isNull("refundItem")) {
								JSONArray array = objectBeauty1.getJSONArray("refundItem");
								if (array.length()>0) {
									for (int i = 0; i < array.length(); i++) {
										UpdateEntity upBackEntity = new UpdateEntity();
										JSONObject objectEva = array.getJSONObject(i);
										if (objectEva.has("title")&&!objectEva.isNull("title")) {
											upBackEntity.title = objectEva.getString("title");
										}
										if (objectEva.has("name")&&!objectEva.isNull("name")) {
											upBackEntity.name = objectEva.getString("name");
										}
										BackCardUpdataPrice.add(upBackEntity);
									}
								}
							}
							if (objectBeauty1.has("payPrice")&&!objectBeauty1.isNull("payPrice")) {
								beauty1NeedMoney = objectBeauty1.getDouble("payPrice");
							}
							if (objectBeauty1.has("lastPrice")&&!objectBeauty1.isNull("lastPrice")) {
								beauty1beautyLastPrice = objectBeauty1.getString("lastPrice");
							}
							if (objectBeauty1.has("refundAmount")&&!objectBeauty1.isNull("refundAmount")) {
								beauty1refundAmount = objectBeauty1.getDouble("refundAmount");
							}
							if (objectBeauty1.has("refundTag")&&!objectBeauty1.isNull("refundTag")) {
								beauty1refundTag = objectBeauty1.getString("refundTag");
							}
							if (objectBeauty1.has("listPrice")&&!objectBeauty1.isNull("listPrice")) {
								beauty1beautyListPrice = objectBeauty1.getString("listPrice");
							}
							UpdateEntity entity = new UpdateEntity();
							if (objectData.has("beautyItem")&&!objectData.isNull("beautyItem")) {//不使用次卡
								JSONObject objectBeautyItem = objectData.getJSONObject("beautyItem");
								if (objectBeautyItem.has("name")&&!objectBeautyItem.isNull("name")) {
									entity.title = objectBeautyItem.getString("name");
									if (!TextUtils.isEmpty(beauty1beautyLastPrice)) {
										entity.name = beauty1beautyLastPrice+"\n"+beauty1beautyListPrice;
									}else {
										entity.name = beauty1beautyListPrice;
									}
								}
							}
							allUpdataPrice.add(entity);
							beauty1CardAndRefundTAG = beauty1useCardPayTag+""+beauty1refundTag;
							
							getCards(builderCard);
						}else {
							isHasBeau1 = false;
						}
						if (objectFeeObj.has("payPrice")&&!objectFeeObj.isNull("payPrice")) {
							lastPrice = objectFeeObj.getDouble("payPrice");
							needMoney = lastPrice;
							setLastPrice(needMoney);
						}
					}
					
					
					
					if (objectData.has("beautyItem")&&!objectData.isNull("beautyItem")) {
//						UpdateEntity updateEntity = new UpdateEntity();
//						layout_cards.setVisibility(View.VISIBLE);
//						rl_upgradeorder_confirm_fwkprice.setVisibility(View.VISIBLE);
						JSONObject objectBeauty = objectData.getJSONObject("beautyItem");
						if (objectBeauty.has("tip")&&!objectBeauty.isNull("tip")) {
							tip = objectBeauty.getString("tip");
						}
//						if (objectBeauty.has("price")&&!objectBeauty.isNull("price")) {
//							int price = objectBeauty.getInt("price");
////							updateEntity.showPrice = price;
//							updateEntity.name = "¥"+price;
//						}
//						if (objectBeauty.has("name")&&!objectBeauty.isNull("name")) {
//							String beautyName = objectBeauty.getString("name");
//							updateEntity.title = beautyName;
////							tv_upgradeorder_confirm_fwk.setText(beautyName);
//							//这里执行调用获取次卡
//						}
////						updateEntity.upOrderPrice = mainTotalPrice;
//						allUpdataPrice.add(updateEntity);
//						allNoCardUpdataPrice.add(updateEntity);
					}else {
//						layout_cards.setVisibility(View.GONE);
//						rl_upgradeorder_confirm_fwkprice.setVisibility(View.GONE);
					}
					
//					if (allUpdataPrice.size()>0) {
//						updateAdapter.setSource(-1);
//						updateAdapter.notifyDataSetChanged();
//					}
					
//					if (objectData.has("refundItem")&&!objectData.isNull("refundItem")) {
//						layout_refund.setVisibility(View.VISIBLE);
//						JSONObject objectRefundItem = objectData.getJSONObject("refundItem");
//						
//						if (objectRefundItem.has("bath")&&!objectRefundItem.isNull("bath")) {
//							JSONObject objectBath = objectRefundItem.getJSONObject("bath");
//							UpdateEntity upBackEntity = new UpdateEntity();
//							if (objectBath.has("title")&&!objectBath.isNull("title")) {
//								upBackEntity.name = objectBath.getString("title");
//							}
//							if (objectBath.has("name")&&!objectBath.isNull("name")) {
//								upBackEntity.strPrice = objectBath.getString("name");
//							}
//							BackUpdataPrice.add(upBackEntity);
//						}
//						if (objectRefundItem.has("extraItems")&&!objectRefundItem.isNull("extraItems")) {
//							JSONArray array = objectRefundItem.getJSONArray("extraItems");
//							if (array.length()>0) {
//								for (int i = 0; i < array.length(); i++) {
//									UpdateEntity upEntity = new UpdateEntity();
//									JSONObject objectEver = array.getJSONObject(i);
//									if (objectEver.has("name")&&!objectEver.isNull("name")) {
//										upEntity.name = objectEver.getString("name");
//									}
//									if (objectEver.has("price")&&!objectEver.isNull("price")) {
//										upEntity.strPrice = "¥"+objectEver.getInt("price");
//									}
//									BackUpdataPrice.add(upEntity);
//								}
//							}
//						}
//						
//					}else {
//						layout_refund.setVisibility(View.GONE);
//					}
					
//					if (BackUpdataPrice.size()>0) {
//						lv_upgradeservice_refund.setVisibility(View.VISIBLE);
//						updateBackAdapter.setSource(1);
//						updateBackAdapter.notifyDataSetChanged();
//					}else{
//						lv_upgradeservice_refund.setVisibility(View.GONE);
//					}
					
					mpsList.add(mulPetService);
					
					
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		tv_titlebar_title.setText("订单升级确认");
	}

	private void setLinster() {
		ib_titlebar_back.setOnClickListener(this);
		btn_upgradeorder_confirm_submit.setOnClickListener(this);
		image_card_right.setOnClickListener(this);
		tv_upgradeorder_confirm_fwk.setOnClickListener(this);
		layout_cards.setOnClickListener(this);
		rl_upgradeorder_confirm_fwkprice.setOnClickListener(this);
		tv_upgradeorder_confirm_fwk.setOnClickListener(this);
		tv_upgradeorder_confirm_fwkprice.setOnClickListener(this);
		textview_give_other_money.setOnClickListener(this);
	}

	private void initData() {
		pDialog = new MProgressDialog(this);
		Intent intent = getIntent();
		orderid = intent.getIntExtra("orderid", 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			setResult(RESULT_OK);
			finishWithAnimation();
			break;
		case R.id.btn_upgradeorder_confirm_submit:
			if (needMoney>0) {
				Intent intent = new Intent(mContext, OrderPayActivity.class);
				intent.putExtra("previous", Global.UPDATE_TO_ORDERPAY);
				intent.putParcelableArrayListExtra("mpslist", mpsList);
				intent.putExtra("payfee", needMoney);
				intent.putExtra("totalfee", totalMoney);
				intent.putExtra("orderid", orderNo);
				intent.putExtra("serviceloc",serviceLoc);
				intent.putExtra("listIds",getListIds(listIds));
				startActivityForResult(intent, Global.BATH_TO_ORDERPAY);
			}else if(needMoney<=0){
				changepay();
			}
			break;
		case R.id.tv_upgradeorder_confirm_fwkprice:
		case R.id.image_card_right:
		case R.id.tv_upgradeorder_confirm_fwk:
		case R.id.rl_upgradeorder_confirm_fwkprice:
		case R.id.layout_cards:
			if (cardsConfirmOrders.size()<=0) {
				ToastUtil.showToastShortCenter(mContext, "无可用服务卡");
				return;
			}
			Intent intent = new Intent(mContext, OrderConfirmChooseCardActivity.class);
			Bundle bundle = new Bundle();
			intent.putParcelableArrayListExtra("cardsConfirmOrders", cardsConfirmOrders);
			intent.putExtra("petSize",mpsList.size());
			intent.putExtras(bundle);
			startActivityForResult(intent, Global.CARDSORDERDETAIL_TO_CHOOSE_CARDS);
			break;
		case R.id.textview_give_other_money:
			Intent intentCard = new Intent(mContext, ADActivity.class);
			if (!TextUtils.isEmpty(buyUrl)) {
				intentCard.putExtra("url", buyUrl);
				intentCard.putExtra("previous", Global.UPDATEORDERDETAIL_TO_BUY_CARD);
				startActivity(intentCard);
			}else {
				ToastUtil.showToastShortCenter(mContext, "没获取到链接");
			}
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000 --- "+Global.UPDATEORDERDETAIL_TO_BUY_CARD);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Global.RESULT_OK){
			if (requestCode==Global.CARDSORDERDETAIL_TO_CHOOSE_CARDS) {//选择次卡返回
//				CardsCutMoney = 0;
				cardsConfirmOrders = data.getParcelableArrayListExtra("cardsConfirmOrders");
				int index = data.getIntExtra("index", -1);
				switch (index) {
				case 0:
					listIds.clear();
					tv_upgradeorder_confirm_fwkprice.setText("不使用服务卡");
//					totalMoney = cutDownPrice;
//					needMoney = totalMoney;
					isUsedCard = false;
					needMoney = beauty0NeedMoney;
					setLastPrice(needMoney);
					break;
				case 1:
					if (!cardsConfirmOrders.isEmpty()) {
//						totalMoney = cutDownPrice;
						setCardPrice(cardsConfirmOrders);
					}
					break;
				}
			}
		}
	}
	private void getCards(StringBuilder builderCard){
		CommUtil.confirmOrderPrompt(spUtil.getString("cellphone", ""), mContext, washBeauOrSwim,serviceLoc,builderCard.toString(),WorkerId,WorkerLevelId,appointment,null,confirmOrderPrompt);
	}
	private AsyncHttpResponseHandler confirmOrderPrompt = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("cardInterface")&&!objectData.isNull("cardInterface")) {
							JSONObject objectIsBuyCard = objectData.getJSONObject("cardInterface");
							if (objectIsBuyCard.has("enable")&&!objectIsBuyCard.isNull("enable")) {
								int enable = objectIsBuyCard.getInt("enable");
								if (enable==0) {
									textview_give_other_money.setVisibility(View.GONE);
								}else if (enable==1) {
									textview_give_other_money.setVisibility(View.VISIBLE);
									if (objectIsBuyCard.has("title")&&!objectIsBuyCard.isNull("title")) {
										textview_give_other_money.setText(objectIsBuyCard.getString("title"));
									}
									if (objectIsBuyCard.has("url")&&!objectIsBuyCard.isNull("url")) {
										buyUrl = objectIsBuyCard.getString("url");
									}
								}
							}
						}
						if (objectData.has("cards")&&!objectData.isNull("cards")) {
							JSONArray arrayCards = objectData.getJSONArray("cards");
							if (arrayCards.length()>0) {
								for (int i = 0; i < arrayCards.length(); i++) {
									CardsConfirmOrder confirmOrder = new CardsConfirmOrder();
									JSONObject objectEveryCards = arrayCards.getJSONObject(i);
									if (objectEveryCards.has("cardName")&&!objectEveryCards.isNull("cardName")) {
										confirmOrder.cardName = objectEveryCards.getString("cardName");
									}
									if (objectEveryCards.has("cardBgImg")&&!objectEveryCards.isNull("cardBgImg")) {
										confirmOrder.cardBgImg = objectEveryCards.getString("cardBgImg");
									}
									if (objectEveryCards.has("tip")&&!objectEveryCards.isNull("tip")) {
										confirmOrder.tip = objectEveryCards.getString("tip");
									}
									if (objectEveryCards.has("id")&&!objectEveryCards.isNull("id")) {
										confirmOrder.id = objectEveryCards.getInt("id");
									}
									confirmOrder.isChoose=0;
									if (objectEveryCards.has("petId")&&!objectEveryCards.isNull("petId")) {
										confirmOrder.petId = objectEveryCards.getInt("petId");
									}
									if(objectEveryCards.has("items")&&!objectEveryCards.isNull("items")){
										JSONArray arrayItems = objectEveryCards.getJSONArray("items");
										StringBuilder builder = new StringBuilder();
										if (arrayItems.length()>0) {
											for (int j = 0; j < arrayItems.length(); j++) {
												CardItems cardItems = new CardItems();
												JSONObject objectEva = arrayItems.getJSONObject(j);
												cardItems.petId = confirmOrder.petId;
												if (objectEva.has("id")&&!objectEva.isNull("id")) {
													cardItems.id = objectEva.getInt("id");
												}
												if (objectEva.has("count")&&!objectEva.isNull("count")) {
													cardItems.count = objectEva.getInt("count");
													cardItems.oldCount = objectEva.getInt("count");
												}
												if (objectEva.has("level")&&!objectEva.isNull("level")) {
													cardItems.level = objectEva.getInt("level");
//													if (cardItems.level==1) {
//														cardItems.level=10;
//													}else if (cardItems.level==2) {
//														cardItems.level=20;
//													}else if (cardItems.level==3) {
//														cardItems.level=30;
//													}
												}
												if (objectEva.has("title")&&!objectEva.isNull("title")) {
													cardItems.title = objectEva.getString("title");
												}
												if (objectEva.has("countTag")&&!objectEva.isNull("countTag")) {
													cardItems.countTag = objectEva.getString("countTag");
												}
												if (objectEva.has("serviceType")&&!objectEva.isNull("serviceType")) {
													cardItems.serviceType = objectEva.getInt("serviceType");
												}
												confirmOrder.arrayList.add(cardItems);
											}
										}
									}
									
									cardsConfirmOrders.add(confirmOrder);
								}
							}
						}
					}
					if (cardsConfirmOrders.size()>0) {
//						layout_cards.setVisibility(View.VISIBLE);
//						needMoney = beauty1NeedMoney;
						isUsedCard = true;
						setCardPrice(cardsConfirmOrders);
					}else {
						isUsedCard = false;
						needMoney = beauty0NeedMoney;
						setLastPrice(needMoney);
//						tv_orderdetail_card.setTextColor(Color.parseColor("#666666"));
//						layout_cards.setVisibility(View.GONE);
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
	private void setCardPrice(ArrayList<CardsConfirmOrder> cardsConfirmOrders) {
		listIds.clear();

		bathNum = 0;
		beauNum = 0;
		feature = 0;
		boolean isChoose = false;
		for (int i = 0; i < cardsConfirmOrders.size(); i++) {
			if (cardsConfirmOrders.get(i).isChoose==1) {
				isChoose = true;
				break;
			}
		}
		for (int i = 0; i < cardsConfirmOrders.size(); i++) {
			CardsConfirmOrder orderCard = cardsConfirmOrders.get(i);
			for (int j = 0; j < orderCard.arrayList.size(); j++) {
				orderCard.arrayList.get(j).count = orderCard.arrayList.get(j).oldCount;
			}
			cardsConfirmOrders.set(i, orderCard);
		}
		for (int i = 0; i < mpsList.size(); i++) {
			for (int j = 0; j < cardsConfirmOrders.size(); j++) {
				boolean isJump = false;
				for (int j2 = 0; j2 < cardsConfirmOrders.get(j).arrayList.size(); j2++) {
					Utils.mLogError("==-->00000 比熊 1 "+mpsList.get(i).petId+"_"+mpsList.get(i).serviceType+"_"+mpsList.get(i).clicksort/*+"_"+mpsList.get(i).serviceId*/);
					Utils.mLogError("==-->00000 比熊 2 "+cardsConfirmOrders.get(j).arrayList.get(j2).petId+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).serviceType+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).level/*+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).id*/);

					if((mpsList.get(i).petId+"_"+mpsList.get(i).serviceType+"_"+mpsList.get(i).clicksort/*+"_"+mpsList.get(i).serviceId*/).equals(cardsConfirmOrders.get(j).arrayList.get(j2).petId+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).serviceType+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).level/*+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).id*/)){
						Utils.mLogError("==-->22222 "+cardsConfirmOrders.get(j).arrayList.get(j2).count);
						if (cardsConfirmOrders.get(j).arrayList.get(j2).count>0) {
							if (isChoose) {
								if (cardsConfirmOrders.get(j).isChoose==1) {
									cardsConfirmOrders.get(j).isChoose=1;
//									needMoney = totalMoney-=mpsList.get(i).basefee;
//									CardsCutMoney = CardsCutMoney+=mpsList.get(i).basefee;
									if (mpsList.get(i).serviceType==1||mpsList.get(i).serviceType==3) {
										bathNum++;
									}else if (mpsList.get(i).serviceType==2 || mpsList.get(i).serviceType== 4) {
										beauNum++;
									}else if (mpsList.get(i).serviceType==3) {
										feature++;
									}
									listIds.add(cardsConfirmOrders.get(j).id);
									cardsConfirmOrders.get(j).arrayList.get(j2).count--;
									isJump = true;
									break;
								}
							}else {
								cardsConfirmOrders.get(j).isChoose=1;
//								needMoney = totalMoney-=mpsList.get(i).basefee;
//								CardsCutMoney = CardsCutMoney+=mpsList.get(i).basefee;
								if (mpsList.get(i).serviceType==1||mpsList.get(i).serviceType==3) {
									bathNum++;
								}else if (mpsList.get(i).serviceType==2 || mpsList.get(i).serviceType== 4) {
									beauNum++;
								}else if (mpsList.get(i).serviceType==3) {
									feature++;
								}
								listIds.add(cardsConfirmOrders.get(j).id);
								cardsConfirmOrders.get(j).arrayList.get(j2).count--;
								isJump = true;
								break;
							}
							if (isJump) {
								break;
							}
						}
					}
				}
				if (isJump) {
					break;
				}
			}
		}
		
		for (int i = 0; i < cardsConfirmOrders.size(); i++) {
			CardsConfirmOrder orderCard = cardsConfirmOrders.get(i);
			for (int j = 0; j < orderCard.arrayList.size(); j++) {
				orderCard.arrayList.get(j).count = orderCard.arrayList.get(j).oldCount;
			}
			cardsConfirmOrders.set(i, orderCard);
		}

		if (bathNum>0&&beauNum>0) {
			tv_upgradeorder_confirm_fwkprice.setText(tip);
		}else if (bathNum>0&&beauNum<=0) {
			tv_upgradeorder_confirm_fwkprice.setText(tip);
		}else if (bathNum<=0&&beauNum>0) {
			tv_upgradeorder_confirm_fwkprice.setText(tip);
		}else if (feature>0) {
			tv_upgradeorder_confirm_fwkprice.setText(tip);
		}else if (bathNum==0&&beauNum==0) {
			tv_upgradeorder_confirm_fwkprice.setText("未使用服务卡支付");
		}
		isUsedCard = true;
		needMoney = beauty1NeedMoney;
		setLastPrice(needMoney);
		if (listIds.size()>0) {
//			isUsedCard = true;
		}else {
//			isUsedCard = false;
			listIds.clear();
		}
	}
	@SuppressWarnings("unchecked")
	private void setLastPrice(double needMoney) {

		tv_upgradeorder_confirm_sjdd.setText("升级订单¥"+totalMoney);
		if (isUsedCard) {
			needMoney = beauty1NeedMoney;
			if (!TextUtils.isEmpty(beauty1CardAndRefundTAG)) {
				tv_upgradeorder_confirm_fwkzf.setVisibility(View.VISIBLE);
				tv_upgradeorder_confirm_fwkzf.setText(beauty1CardAndRefundTAG);
			}else {
				tv_upgradeorder_confirm_fwkzf.setVisibility(View.GONE);
			}
			if (needMoney<0) {
				paytype = 7;
			}
			updateAdapter.setSource(-1);
			updateAdapter.setData(allUpdataPrice);
			if (BackCardUpdataPrice.size()>0) {
				layout_refund.setVisibility(View.VISIBLE);
				view_last_price.setVisibility(View.VISIBLE);
//				lv_upgradeservice_refund.setVisibility(View.VISIBLE);
				updateBackAdapter.setSource(1);
				updateBackAdapter.setData(BackCardUpdataPrice);
			}else {
				layout_refund.setVisibility(View.GONE);
				view_last_price.setVisibility(View.GONE);
//				lv_upgradeservice_refund.setVisibility(View.GONE);
			}
		}else {
			needMoney = beauty0NeedMoney;
			if (!TextUtils.isEmpty(beauty0CardAndRefundTAG)) {
				tv_upgradeorder_confirm_fwkzf.setVisibility(View.VISIBLE);
				tv_upgradeorder_confirm_fwkzf.setText(beauty0CardAndRefundTAG);
			}else {
				tv_upgradeorder_confirm_fwkzf.setVisibility(View.GONE);
			}
			if (needMoney<0) {
//				paytype = 7;
			}
			updateAdapter.setSource(-1);
			updateAdapter.setData(allNoCardUpdataPrice);
			if (BackUpdataPrice.size()>0) {
				layout_refund.setVisibility(View.VISIBLE);
				view_last_price.setVisibility(View.VISIBLE);
//				lv_upgradeservice_refund.setVisibility(View.VISIBLE);
				updateBackAdapter.setSource(1);
				updateBackAdapter.setData(BackUpdataPrice);
			}else {
				layout_refund.setVisibility(View.GONE);
				view_last_price.setVisibility(View.GONE);
//				lv_upgradeservice_refund.setVisibility(View.GONE);
			}
		}
		if (!isHasBeau0&&!isHasBeau1) {
			needMoney = lastPrice;
			textview_show_last_price.setText(Math.abs(needMoney)+"");
			textview_show_last_price_bottom.setText(Math.abs(needMoney)+"");
		}else {
			if (isUsedCard) {//使用次卡 beauty1
				if (needMoney<0) {
					textview_show_last_price.setText(beauty1refundAmount+"");
					textview_show_last_price_bottom.setText(beauty1refundAmount+"");
				}else {
					textview_show_last_price.setText(Math.abs(needMoney)+"");
					textview_show_last_price_bottom.setText(Math.abs(needMoney)+"");
				}
			}else {//不使用次卡 beauty0
				if (needMoney<0) {
					textview_show_last_price.setText(beauty0refundAmount+"");
					textview_show_last_price_bottom.setText(beauty0refundAmount+"");
				}else {
					textview_show_last_price.setText(Math.abs(needMoney)+"");
					textview_show_last_price_bottom.setText(Math.abs(needMoney)+"");
				}
			}
		}
		if (BackUpdataPrice.size()<=0&&BackCardUpdataPrice.size()<=0) {
			layout_refund.setVisibility(View.GONE);
		}
		if (allUpdataPrice.size()<=0&&allNoCardUpdataPrice.size()<=0) {
			tv_upgradeservice_serviceitemsname.setVisibility(View.GONE);
			view_up_line.setVisibility(View.GONE);
			lv_upgradeservice_serviceitems.setVisibility(View.GONE);
		}
		if (!isHasBeau1) {
			tv_upgradeorder_confirm_fwkprice.setText("无可用服务卡");
		}
		if (needMoney<0) {
			textview_last_price_name_top.setText("退款：");
			textview_last_price_name_bottom.setText("退款：");
			btn_upgradeorder_confirm_submit.setText("确认并退款");
		}else {
			textview_last_price_name_top.setText("实付款：");
			textview_last_price_name_bottom.setText("实付款：");
			btn_upgradeorder_confirm_submit.setText("确认并付款");
		}
		//textview_show_last_price.setText(Math.abs(needMoney)+"");
		//textview_show_last_price_bottom.setText(Math.abs(needMoney)+"");
	}
	private String getListIds(ArrayList<Integer> listIds){
		StringBuilder builder  = new StringBuilder();
		if (listIds.size()>0) {
			for (int i = 0; i < listIds.size(); i++) {
				builder.append(listIds.get(i)+",");
			}
			return builder.substring(0, builder.length()-1);
		}else {
			return null;
		}
	}
	private void changepay(){
		pDialog.showDialog();
//		if(needMoney<0)
//			needMoney=0;
		CommUtil.changeOrderPayMethodV2(null,spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderNo, 
				spUtil.getInt("userid", 0),null,null,null,
				/*pickup*/-1,-1,-1,Utils.formatDouble(needMoney),paytype,
				0,-1,null,false,getListIds(listIds),changeHanler);
	}
	private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("重新支付："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("orderPet")&&!jdata.isNull("orderPet")){
						JSONArray jarr = jdata.getJSONArray("orderPet");
						for(int i=0;i<jarr.length()&&i<mpsList.size();i++){
							mpsList.get(i).orderid=jarr.getInt(i);
						}
					}
					if (jdata.has("orderId") && !jdata.isNull("orderId")) {
						orderNo = jdata.getInt("orderId");
					}
					if (needMoney==0||paytype==7) {
						//直接跳转到支付成功
						goPayResult();
					}
				}else{
					ToastUtil.showToastShort(mContext, msg);
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
	private void goPayResult(){
		Intent intent = new Intent(mContext, PaySuccessActivity.class);
		intent.putParcelableArrayListExtra("mpslist", mpsList);
		intent.putExtra("previous", Global.UPDATE_TO_ORDERPAY);
		intent.putExtra("orderId",orderNo);
		intent.putExtra("type", 1);
		startActivity(intent);
		finishWithAnimation();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			previous_liucheng = intent.getIntExtra("previous_liucheng",0);
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 111 --- "+previous_liucheng);
			if (previous_liucheng>0&&previous_liucheng==Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
				Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 222 --- "+previous_liucheng);
				getData();
			}
		}
		
	}
}
