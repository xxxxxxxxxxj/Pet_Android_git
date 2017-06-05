package com.haotang.pet;

import java.util.ArrayList;
import java.util.UUID;

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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MulOrderPetAdapter;
import com.haotang.pet.adapter.SwimMulPet;
import com.haotang.pet.entity.CardItems;
import com.haotang.pet.entity.CardsConfirmOrder;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PetService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.MListview;
import com.umeng.analytics.MobclickAgent;

public class OrderDetailActivity extends SuperActivity implements OnClickListener{
	private ImageButton ibBack;
	private TextView tvTitle;
	private TextView tvContactPhone;
	private TextView user_phone_num;
	private LinearLayout rlContact;
	private TextView tvTime;
//	private TextView tvAddrType;
	private TextView tvAddr;
	private CircleImageView srivBeautician;
	private ImageView srivBeauticianSex;
	private TextView tvBeauticianName;
	private TextView tvBeauticianOrdernum;
	private ImageView ivBeauticianLevel1;
	private ImageView ivBeauticianLevel2;
	private ImageView ivBeauticianLevel3;
	private ImageView ivBeauticianLevel4;
	private ImageView ivBeauticianLevel5;
	private RelativeLayout rlCoupon;
	private RelativeLayout rlNote;
	private TextView tvCoupon;
	private TextView tvNote;
	private TextView tvMoney;
	private TextView tv_orderdetail_money_price;
	private Button btPay;
	private int paytype = 1;//1是微信，2为支付宝,3为优惠券，4为余额支付
	private SharedPreferenceUtil spUtil;
	public static ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private PetService ps=null;
	private int couponId = 0;
	private double couponAmount = 0;
	private double needMoney = 0;
	private double totalMoney = 0;
	private String date;
	private int workerid;
	private int addressid;
	private String address;
	private double lat;
	private double lng;
	private MProgressDialog pDialog;
	private UUID uuid;
	private int orderNo = 0;
	private String cphone;
	private String cname;
	
	private LinearLayout llPetStore;
	private ImageView srivPetStore;
	private TextView tvPetStoreName;
	private TextView tvPetStoreAddr;
	private TextView tvPetStorePhone;
	private int petSize = 1;

	private boolean ispickup = false;//是否需要接送
	private int pickup = 0;//是否接送 0：不接送 1：接送
	private int serviceloc = 2; //服务方式 1：门店服务 2：上门服务
	private ArrayList<MulPetService> mpsList;
	public static SuperActivity act;
	public int OrderType=0;//1 到店 派单 2 到店抢单  3 上门 派单 4 上门抢单 
	public int clicksort=0;
	private View show_petstore_and_beau;
	private View show_pickup_line;
	private boolean isEx = false;
	private String month="";
	private String times="";
	private int previous=0;
	private SwimMulPet<Pet> sAMulPet;
	public static ArrayList<Pet> listPets;
	private int shopId;
	private int current;
	private String [] months;
	private String dataString ="";
	private int addrid;
	private String addr="";
	private String shopAddress;
	private String shopImg;
	private String shopName;
	private TextView text_lev;
	private LinearLayout ungent_order;
	private TextView text_all_order_notice;
	private TextView textview_urgnet_time;
	private TextView textviwe_urgent_notice;
	private int washBeauOrSwim = -1;
	private LinearLayout layout_notice;
	private boolean isVie = false;
	private TextView textview_member_cutdown;
	private String memberSwimNotice = null;
	private LinearLayout layout_go_home_notice;
	private TextView textview_go_home_extraFeeTag;
	private TextView textview_go_home_extraFee;
	private LinearLayout rl_orderdetail_go_home_service_price;
	private TextView tv_orderdetail_go_home_left;
	private TextView tv_orderdetail_go_home_service_price;
	private double extraFee = 0;
	private double reductionFee = 0;
	private String [] notices = null;
	private TextView top_youhui_left;
	private TextView top_lastprice_right;
	private double topLeftMoney = 0;// 仅仅新增加的左侧带优惠的位置使用
	private LinearLayout layout_serviceloc;
	private TextView tv_orderdetail_serviceloc;
	private ImageView iv_orderdetail_card_rightarrow;
	private TextView tv_orderdetail_card;
	private StringBuilder builderStrp = null;
	private StringBuilder builderServiceType=null;
	private ArrayList<CardsConfirmOrder> cardsConfirmOrders = new ArrayList<CardsConfirmOrder>();
	private ArrayList<CardsConfirmOrder> OldCardsConfirmOrders = new ArrayList<CardsConfirmOrder>();
	private LinearLayout layout_cards_choose;
	private TextView tv_orderdetail_card_pre;
	private int bathNum=0;
	private int beauNum=0;
	private int feature=0;
	private boolean isFirst = true;
	private ArrayList<Integer> listIds = new ArrayList<Integer>();
	
//	private ArrayList<CardsConfirmOrder> cardsConfirmOrdersBack = null;
//	private boolean isUsedCard = false;
	
	private TextView top_youhui_bottom;
	private double CardsCutMoney = 0;
	private String couponCutReductionTag="";
	private String CardTag="";
	private String appointment="";
	private int index = -1;
	private double getCouponNeedMoney=0;
	private TextView textview_isgo_update;
	private TextView textview_give_other_money;
	private String buyUrl = "https://demo.cwjia.cn/static/content/html5/201703/cardList.html";
	private MyReceiver receiver;
	private int previous_liucheng = 0;
	private String AMORPM = "";
	private ScrollView scroll_view_top;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.orderdetail);
		setContentView(R.layout.orderdetail_new);
		act = this;
		MApplication.listAppoint.add(this);
		findVeiw();
		initReceiver();
		getIntentData();
		setView();
	}
	
	private void getIntentData() {
		totalMoney = getIntent().getDoubleExtra("totalfee", 0);
		OrderType = getIntent().getIntExtra("OrderType", 0);
		previous = getIntent().getIntExtra("previous", 0);
		shopId = getIntent().getIntExtra("shopId", 0);
		current = getIntent().getIntExtra("current", 0);
		clicksort = getIntent().getIntExtra("clicksort", 0);
		month = getIntent().getStringExtra("month");
		try {
			times = getIntent().getStringExtra("times").replace("点", "");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		months = getIntent().getStringArrayExtra("months");
		isEx = getIntent().getBooleanExtra("isEx", false);
		ps = (PetService) getIntent().getSerializableExtra("ps");
		mpsList = getIntent().getParcelableArrayListExtra("mulpetservice");
		
		
		addrid = getIntent().getIntExtra("addrid", 0);
		addr = getIntent().getStringExtra("address");
		shopAddress = getIntent().getStringExtra("shopAddress");
		shopImg = getIntent().getStringExtra("shopImg");
		shopName = getIntent().getStringExtra("shopName");
		memberSwimNotice = getIntent().getStringExtra("memberSwimNotice");
		
		
		Utils.mLogError("==-->washBeauOrSwim:= "+washBeauOrSwim);
		AMORPM = getIntent().getStringExtra("AMORPM");
		
		if (ps==null) {
			lng = getIntent().getDoubleExtra("addr_lng", 0);
			lat = getIntent().getDoubleExtra("addr_lat", 0);
			addressid = getIntent().getIntExtra("addrid", 0);
		}
		
	}
	private void initReceiver() {
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.OrderDetailActivity");
		registerReceiver(receiver, filter);
	}

	private void findVeiw() {
		uuid = UUID.randomUUID();
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		spUtil.removeData("upgradeservice");
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		lvPets = (MListview) findViewById(R.id.lv_orderdetail_pets);
		
		tvContactPhone = (TextView) findViewById(R.id.tv_orderdetail_contact_phone);
		user_phone_num = (TextView) findViewById(R.id.user_phone_num);
		rlContact = (LinearLayout) findViewById(R.id.rl_orderdetail_contact);
		tvTime = (TextView) findViewById(R.id.tv_orderdetail_time);
		
		tvAddr = (TextView) findViewById(R.id.tv_orderdetail_addr);
		
		srivBeautician = (CircleImageView) findViewById(R.id.sriv_orderdetail_beautician);
		srivBeauticianSex = (ImageView) findViewById(R.id.sriv_orderdetail_beautician_sex);
//		bt_orderdetail_beautician_level = (Button) findViewById(R.id.bt_orderdetail_beautician_level);
		tvBeauticianName = (TextView) findViewById(R.id.tv_orderdetail_beauticianname);
		tvBeauticianOrdernum = (TextView) findViewById(R.id.tv_orderdetail_beauticianordernum);
		ivBeauticianLevel1 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel1);
		ivBeauticianLevel2 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel2);
		ivBeauticianLevel3 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel3);
		ivBeauticianLevel4 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel4);
		ivBeauticianLevel5 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel5);
		
		rlCoupon = (RelativeLayout) findViewById(R.id.rl_orderdetail_coupon);
		rlNote = (RelativeLayout) findViewById(R.id.rl_orderdetail_note);
		tvCoupon = (TextView) findViewById(R.id.tv_orderdetail_coupon);
		tvNote = (TextView) findViewById(R.id.tv_orderdetail_note);
		
		tvMoney = (TextView) findViewById(R.id.tv_orderdetail_money);
		tv_orderdetail_money_price = (TextView) findViewById(R.id.tv_orderdetail_money_price);
		btPay = (Button) findViewById(R.id.bt_orderdetail_pay);
		
//		tvAddrType = (TextView) findViewById(R.id.tv_orderdetail_addrtype);
		llPetStore = (LinearLayout) findViewById(R.id.ll_petstore);
		srivPetStore = (ImageView) findViewById(R.id.sriv_petstore_image);
		tvPetStoreName = (TextView) findViewById(R.id.tv_petstore_name);
		tvPetStoreAddr = (TextView) findViewById(R.id.tv_petstore_addr);
		tvPetStorePhone = (TextView) findViewById(R.id.tv_petstore_phone);
		
		rlBeauticianinfo = (LinearLayout) findViewById(R.id.rl_beauticianinfo);
		
//		llShowall = (LinearLayout) findViewById(R.id.ll_orderdetail_showall);
//		tvShowall = (TextView) findViewById(R.id.tv_orderdetail_showall);
//		ivShowall = (ImageView) findViewById(R.id.iv_orderdetail_showall);
		
		rlPickup = (RelativeLayout) findViewById(R.id.rl_orderdetail_pickup);
		tvPickupHot = (TextView) findViewById(R.id.tv_orderdetail_pickup_hot);
		ivPickupHint = (ImageView) findViewById(R.id.iv_orderdetail_pickup_pickup_hint);
		tvPickupPrice = (TextView) findViewById(R.id.tv_orderdetail_pickup);
		ivPickup = (ImageView) findViewById(R.id.iv_orderdetail_pickup);
		show_petstore_and_beau = (View) findViewById(R.id.show_petstore_and_beau);
		show_pickup_line = (View) findViewById(R.id.show_pickup_line);
		text_lev = (TextView) findViewById(R.id.text_lev);
		ungent_order = (LinearLayout) findViewById(R.id.ungent_order);
		text_all_order_notice = (TextView) findViewById(R.id.text_all_order_notice);
		textview_urgnet_time = (TextView) findViewById(R.id.textview_urgnet_time);
		textviwe_urgent_notice = (TextView) findViewById(R.id.textviwe_urgent_notice);
		layout_notice = (LinearLayout) findViewById(R.id.layout_notice);
		textview_member_cutdown = (TextView) findViewById(R.id.textview_member_cutdown);
		layout_go_home_notice = (LinearLayout) findViewById(R.id.layout_go_home_notice);
		textview_go_home_extraFeeTag = (TextView) findViewById(R.id.textview_go_home_extraFeeTag);
		textview_go_home_extraFee = (TextView) findViewById(R.id.textview_go_home_extraFee);
		rl_orderdetail_go_home_service_price = (LinearLayout) findViewById(R.id.rl_orderdetail_go_home_service_price);
		tv_orderdetail_go_home_left = (TextView) findViewById(R.id.tv_orderdetail_go_home_left);
		tv_orderdetail_go_home_service_price = (TextView) findViewById(R.id.tv_orderdetail_go_home_service_price);
		top_youhui_left = (TextView) findViewById(R.id.top_youhui_left);
		top_lastprice_right = (TextView) findViewById(R.id.top_lastprice_right);
		layout_serviceloc = (LinearLayout) findViewById(R.id.layout_serviceloc);
		tv_orderdetail_serviceloc = (TextView) findViewById(R.id.tv_orderdetail_serviceloc);
		iv_orderdetail_card_rightarrow = (ImageView) findViewById(R.id.iv_orderdetail_card_rightarrow);
		tv_orderdetail_card = (TextView) findViewById(R.id.tv_orderdetail_card);
		layout_cards_choose = (LinearLayout) findViewById(R.id.layout_cards_choose);
		tv_orderdetail_card_pre = (TextView) findViewById(R.id.tv_orderdetail_card_pre);
		top_youhui_bottom = (TextView) findViewById(R.id.top_youhui_bottom);

		textview_isgo_update = (TextView) findViewById(R.id.textview_isgo_update);
		textview_give_other_money = (TextView) findViewById(R.id.textview_give_other_money);
		scroll_view_top = (ScrollView) findViewById(R.id.scroll_view_top);
		
//		vPickupLine = findViewById(R.id.view_orderdetail_8);
	}

	@SuppressWarnings("unchecked")
	private void setView() {
		tvTitle.setText("订单确认");
		showPetStore(false);
		/**
		totalMoney = getIntent().getDoubleExtra("totalfee", 0);
		OrderType = getIntent().getIntExtra("OrderType", 0);
		previous = getIntent().getIntExtra("previous", 0);
		shopId = getIntent().getIntExtra("shopId", 0);
		current = getIntent().getIntExtra("current", 0);
		clicksort = getIntent().getIntExtra("clicksort", 0);
		month = getIntent().getStringExtra("month");
		try {
			times = getIntent().getStringExtra("times").replace("点", "");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		months = getIntent().getStringArrayExtra("months");
		isEx = getIntent().getBooleanExtra("isEx", false);
		*/
		needMoney = totalMoney;
		topLeftMoney = totalMoney;
		cname = spUtil.getString("username", "");
		cphone = spUtil.getString("cellphone", "");
		tvContactPhone.setText(cname);
		if (cname.equals("")) {
			user_phone_num.setText(cphone);
		}else {
			user_phone_num.setText("  "+cphone);
		}
		/**
		ps = (PetService) getIntent().getSerializableExtra("ps");
		mpsList = getIntent().getParcelableArrayListExtra("mulpetservice");
		*/
		if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			ungent_order.setVisibility(View.GONE);
			rlPickup.setVisibility(View.GONE);
			listPets = SwimDetailActivity.listPets;
			dataString = months[current];
			Utils.mLogError("==-->dataString 0 "+dataString);
			/**
			addrid = getIntent().getIntExtra("addrid", 0);
			addr = getIntent().getStringExtra("address");
			shopAddress = getIntent().getStringExtra("shopAddress");
			shopImg = getIntent().getStringExtra("shopImg");
			shopName = getIntent().getStringExtra("shopName");
			memberSwimNotice = getIntent().getStringExtra("memberSwimNotice");
			
			
			Utils.mLogError("==-->washBeauOrSwim:= "+washBeauOrSwim);
			AMORPM = getIntent().getStringExtra("AMORPM");
			*/
			washBeauOrSwim = 3;
			try {
//				String dataShow = dataString.replace("年", "-").replace("月", "-").replace("日", ",").replace("上午", "");
//				String [] timeLast = dataShow.split(",");
//				String strTime = timeLast[1].trim();
				String strTime="";
				if (AMORPM.equals("上午")) {
					String am =Global.SwimDayTime.AM;
					strTime=am;
				}else if (AMORPM.equals("下午")) {
					String pm = Global.SwimDayTime.PM;
					strTime=pm;
				}
				tvTime.setText("预约时间："+dataString+" "+AMORPM);
				dataString = dataString+" "+strTime;
				appointment = dataString;
				Utils.mLogError("==-->data dataString 1 "+dataString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvAddr.setText("宠物地址："+addr);
			if (!TextUtils.isEmpty(memberSwimNotice)) {
				totalMoney = totalMoney / 2;
				needMoney  = needMoney / 2;
				textview_member_cutdown.setVisibility(View.VISIBLE);
				textview_member_cutdown.setText(memberSwimNotice);
			}else {
				textview_member_cutdown.setVisibility(View.GONE);
			}
			setLastPrice(true);
			if (listPets.size()>0) {
				sAMulPet = new SwimMulPet<Pet>(this, listPets);
				sAMulPet.setData(1);
				lvPets.setAdapter(sAMulPet);
			}
			ImageLoaderUtil.loadImg(shopImg,srivPetStore, R.drawable.icon_production_default,null);
			tvPetStoreName.setText(shopName);
			tvPetStoreAddr.setText(shopAddress);
			tvAddr.setText("门店地址："+shopAddress);
			tv_orderdetail_serviceloc.setText("服务方式：到店服务");
		}else {
			if(mpsList!=null&&mpsList.size()>0){
				Utils.mLogError("==-->serviceType:= "+mpsList.get(0).serviceType);
				if (mpsList.get(0).serviceType==3) {
					layout_serviceloc.setVisibility(View.GONE);
				}else {
					layout_serviceloc.setVisibility(View.VISIBLE);
				}
				if (mpsList.get(0).serviceloc==1) {
					tv_orderdetail_serviceloc.setText("服务方式：到店服务");
				}else if (mpsList.get(0).serviceloc==2) {
					tv_orderdetail_serviceloc.setText("服务方式：上门服务");
				}
				builderStrp = new StringBuilder();
				builderServiceType = new StringBuilder();
				for (int i = 0; i < mpsList.size(); i++) {
					MulPetService mulPetService = mpsList.get(i);
					mulPetService.basefee=0;
					if (mulPetService.basefee<=0) {
						mulPetService.basefee = mulPetService.fee-mulPetService.addservicefee;
						mpsList.set(i, mulPetService);
					}
//					mpsList.get(i).clicksort = clicksort;
					
					if (ps.beautician_sort==10) {
						mpsList.get(i).clicksort=10;
					}else if (ps.beautician_sort==20) {
						mpsList.get(i).clicksort=20;
					}else if (ps.beautician_sort==30) {
						mpsList.get(i).clicksort=30;
					}
					
					if (i==mpsList.size()) {
						builderStrp.append(mpsList.get(i).petId+"_"+mpsList.get(i).serviceId);
					}else {
						builderStrp.append(mpsList.get(i).petId+"_"+mpsList.get(i).serviceId+",");
					}
					builderServiceType.append(mpsList.get(i).serviceType+",");
				}
				petSize = mpsList.size();
				if (previous==Global.URGENT_TO_ORDERDETAIL) {
					isVie = true;
				}
				@SuppressWarnings("rawtypes")
				MulOrderPetAdapter mopAdapter = new MulOrderPetAdapter(this, mpsList);
				lvPets.setAdapter(mopAdapter);
				mopAdapter.setIsVie(isVie);
				mopAdapter.notifyDataSetChanged();
			}
			washBeauOrSwim = 1;
			Utils.mLogError("==-->washBeauOrSwim:= "+washBeauOrSwim);
		}
		if (ps!=null) {//流程与加急都走这里
			setData(ps);
			Utils.mLogError("==-->data 正常 "+ps.servicedate);
			Utils.mLogError("==-->data 加急 "+month+" "+times);
			if (previous==Global.URGENT_TO_ORDERDETAIL) {
				isVie = true;
				appointment = month+" "+times+":00:00";
				getIsCanPick(ps.addr_id);//加急接送流程走这个
			}else {
				appointment = ps.servicedate;
				getIsCanPickNormal(ps.addr_id);//正常的流程接送取这个
			}
		}else {
			/*lng = getIntent().getDoubleExtra("addr_lng", 0);
			lat = getIntent().getDoubleExtra("addr_lat", 0);
			addressid = getIntent().getIntExtra("addrid", 0);*/
		}
		if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			llPetStore.setVisibility(View.GONE);
			rlBeauticianinfo.setVisibility(View.GONE);
			show_petstore_and_beau.setVisibility(View.GONE);
		}
		
		ibBack.setOnClickListener(this);
		rlContact.setOnClickListener(this);
		rlCoupon.setOnClickListener(this);
		rlNote.setOnClickListener(this);
		btPay.setOnClickListener(this);
		rlPickup.setOnClickListener(this);
		ivPickupHint.setOnClickListener(this);
		layout_go_home_notice.setOnClickListener(this);
		layout_cards_choose.setOnClickListener(this);
		textview_give_other_money.setOnClickListener(this);

		if (TextUtils.isEmpty(builderServiceType)) {
			getCouponNeedMoney = needMoney;
			getCoupon();
			getNotice();
		}else {
			if (builderServiceType.toString().contains("1")||builderServiceType.toString().contains("2")||builderServiceType.toString().contains("3")) {
				getNotice();
			}else {
				getCouponNeedMoney = needMoney;
				getCoupon();
				getNotice();
			}
		}
			
	}
	private void getNotice(){//后边两个null前边那个必须要传递 appoinment
		cardsConfirmOrders.clear();
		if (TextUtils.isEmpty(builderStrp)) {
			CommUtil.confirmOrderPrompt(spUtil.getString("cellphone", ""), act, washBeauOrSwim,serviceloc,null,workerid,clicksort,appointment,null,confirmOrderPrompt);
		}else {
			CommUtil.confirmOrderPrompt(spUtil.getString("cellphone", ""), act, washBeauOrSwim,serviceloc,builderStrp.toString(),workerid,clicksort,appointment,null,confirmOrderPrompt);
		}
	}
	private void setLastPrice(boolean isMoney) {
		
		String text =""; 
		String bottomShowPrice = "";
		if(isMoney){
			text = "实付款: ¥ "+ Utils.formatDouble(needMoney);
			bottomShowPrice = Utils.formatDouble(needMoney)+"";
		}else{
			text = "实付款: ¥ 0";
			bottomShowPrice = 0+"";
		}
		SpannableString ss = new SpannableString(text); 
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tabback)), 0, 4,Spanned.SPAN_INCLUSIVE_INCLUSIVE);  
		ss.setSpan(new TextAppearanceSpan(this, R.style.style1), 6, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//		tvMoney.setText(ss);
		tv_orderdetail_money_price.setText(bottomShowPrice);
		
		top_lastprice_right.setText(ss);
		double cutDownMoney = couponAmount+reductionFee;
		if (cutDownMoney<=0) {//无优惠 
			top_youhui_left.setText("订单¥"+(topLeftMoney+extraFee));
			if (CardsCutMoney>0) {//有次卡
				top_youhui_bottom.setVisibility(View.VISIBLE);
				top_youhui_bottom.setText(CardTag+"¥"+CardsCutMoney);
			}else {//无次卡
				top_youhui_bottom.setVisibility(View.GONE);
			}
		}else {//有优惠
			top_youhui_left.setText("总价¥"+(topLeftMoney+extraFee));
			top_youhui_bottom.setVisibility(View.VISIBLE);
			if (getCouponNeedMoney>couponAmount) {
				if (CardsCutMoney>0) {//有次卡
					top_youhui_bottom.setText(CardTag+"¥"+CardsCutMoney+"  "+couponCutReductionTag+"¥"+cutDownMoney);
				}else {//无次卡
					top_youhui_bottom.setText(couponCutReductionTag+"¥"+cutDownMoney);
				}
			}else {
				if (CardsCutMoney>0) {//有次卡
					top_youhui_bottom.setText(CardTag+"¥"+CardsCutMoney+"  "+couponCutReductionTag+"¥"+(getCouponNeedMoney+reductionFee));
				}else {//无次卡
					top_youhui_bottom.setText(couponCutReductionTag+"¥"+(getCouponNeedMoney+reductionFee));
				}
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回
			goBack();
			break;
		case R.id.iv_orderdetail_pickup_pickup_hint:
			//接送提示
			goNext(PickupHintActivity.class);
			break;
		case R.id.layout_go_home_notice:
			//接送提示
			goNext(PickupHintActivity.class);
			break;
		case R.id.rl_orderdetail_pickup:
			//选择接送服务
			if(ispickup){
				pickup = 0;
				ispickup=false;
				totalMoney = totalMoney - ps.pickupprice/**petSize*/;
				needMoney = needMoney-ps.pickupprice/**petSize*/;
				ivPickup.setBackgroundResource(R.drawable.icon_pay_normal);
				getCouponNeedMoney = needMoney;
				getCoupon();
				/**
				 * 不选接送请求优惠券计算最后价格
				 */
			}else{
				pickup = 1;
				ispickup=true;
				totalMoney = totalMoney + ps.pickupprice/**petSize*/;
//				needMoney = totalMoney-couponAmount;
				needMoney = totalMoney;//后边每次请求优惠券可以放开
				ivPickup.setBackgroundResource(R.drawable.icon_pay_selected);
				getCouponNeedMoney = needMoney;
				getCoupon();
				Intent intent  = new Intent(mContext, IsCanPickActivity.class);
				intent.putExtra("index", 1);
				startActivity(intent);
				/**
				 * 选择接送请求优惠券最后计算
				 */
			}
//			if(needMoney>0){
//				setLastPrice(true);
//			}else{
//				setLastPrice(false);
//			}
			break;
		case R.id.rl_orderdetail_contact:
			//修改联系人
			goNextForData(ContactActivity.class, Global.ORDERDETAILREQUESTCODE_CONTACT);
			break;
		case R.id.rl_orderdetail_coupon:
			//选择优惠券
			if(couponList.size()>0){
				goNextForData(AvailableCouponActivity.class, Global.ORDERDETAILREQUESTCODE_COUPON);
			}else{
				ToastUtil.showToastShortCenter(OrderDetailActivity.this, "无可用优惠券");
			}
			break;
		case R.id.rl_orderdetail_note:
			//填写留言
			goNextForData(NoteActivity.class, Global.ORDERDETAILREQUESTCODE_NOTE);
			break;
		case R.id.bt_orderdetail_pay:
			//开始支付
			if(couponAmount>=totalMoney){
				//优惠券金额够该订单使用
				if(orderNo>0){
					changepay();
				}else{
					if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
						SwimOrder();
					}else if (previous==Global.URGENT_TO_ORDERDETAIL||isEx) {
						sendIsExOrder();
					}else {
						sendOrder();
					}
				}
				
			}else{
				Intent intent = new Intent(OrderDetailActivity.this, OrderPayActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
					intent.putExtra("previous", Global.SWIM_DETAIL_TO_ORDERDETAIL);
				}else if(previous==Global.URGENT_TO_ORDERDETAIL){
					intent.putExtra("previous", Global.URGENT_TO_ORDERDETAIL);
				}else {
					intent.putExtra("previous", Global.BATH_TO_ORDERPAY);
				}
				intent.putParcelableArrayListExtra("mpslist", mpsList);
				intent.putExtra("ps", ps);
				intent.putExtra("couponid", couponId);
				intent.putExtra("pickup", pickup);
				intent.putExtra("couponamount", couponAmount);
				intent.putExtra("payfee", needMoney);
				intent.putExtra("totalfee", totalMoney);
				intent.putExtra("cname", cname);
				intent.putExtra("cphone", cphone);
				intent.putExtra("orderid", orderNo);
				intent.putExtra("uuid", uuid.toString());
				intent.putExtra("remark", tvNote.getText().toString().trim());
				intent.putExtra("isEx",isEx);
				intent.putExtra("clicksort",clicksort);
				intent.putExtra("serviceloc",serviceloc);
				intent.putExtra("addr_lat",lat);
				intent.putExtra("addr_lng",lng);
				intent.putExtra("addrid",addrid);
				intent.putExtra("addressid",addressid);
				intent.putExtra("address",addr);
				intent.putExtra("shopId",shopId);
				intent.putExtra("date",dataString);
				intent.putExtra("month",month);
				intent.putExtra("times",times);
				intent.putExtra("pickup",pickup);
				intent.putExtra("listIds",getListIds(listIds));
				
				startActivityForResult(intent, Global.BATH_TO_ORDERPAY);
			}
			
			break;
		case R.id.layout_cards_choose:
			if (cardsConfirmOrders.size()<=0) {
				ToastUtil.showToastShortCenter(OrderDetailActivity.this, "无可用服务卡");
				return;
			}
			Intent intent = new Intent(mContext, OrderConfirmChooseCardActivity.class);
//			Bundle bundle = new Bundle();
			intent.putParcelableArrayListExtra("cardsConfirmOrders", cardsConfirmOrders);
			intent.putExtra("petSize",mpsList.size());
//			bundle.putIntegerArrayList("listIds",listIds);
//			intent.putExtras(bundle);
			startActivityForResult(intent, Global.CARDSORDERDETAIL_TO_CHOOSE_CARDS);
			break;
		case R.id.textview_give_other_money:
			Intent intentCard = new Intent(mContext, ADActivity.class);
			if (!TextUtils.isEmpty(buyUrl)) {
				intentCard.putExtra("url", buyUrl);
				intentCard.putExtra("previous", Global.ORDERDETAIL_TO_BUY_CARD);
				startActivity(intentCard);
			}else {
				ToastUtil.showToastShortCenter(mContext, "没获取到链接");
			}
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000 --- "+Global.ORDERDETAIL_TO_BUY_CARD);
			break;
		}
		
	}
	
	private void goBack(){
		MApplication.listAppoint.remove(this);
		setResult(Global.RESULT_OK);
		finishWithAnimation();
	}
	
	private void setData(PetService ps){
		if(ps == null)
			return;
		date = ps.servicedate;
		Utils.mLogError("====---->date "+date);
		workerid = ps.beautician_id;
		addressid = ps.addr_id;
		address = ps.addr_detail;
		lat = ps.addr_lat;
		lng = ps.addr_lng;
		
		Utils.mLogError("预约时间："+date);
		if(ps.servicedate!=null&&!"".equals(ps.servicedate)){
			tvTime.setText("预约时间："+ps.servicedate);
		}
		if(!"".equals(ps.addr_detail)){
			tvAddr.setText("宠物地址："+ps.addr_detail);
		}
		
		if(ps.addrtype==1){
			showPetStore(true);//默认展开
			serviceloc = 1;
//			tvAddrType.setText("到店");
//			tvAddrType.setBackgroundResource(R.drawable.bg_red_round);
			ImageLoaderUtil.loadImg(ps.petstoreImg,srivPetStore,  0,null);
			
			tvPetStoreAddr.setText(ps.petstoreAddr);
			tvPetStoreName.setText(ps.petstoreName);
			tvPetStorePhone.setText(ps.petstorePhone);
			show_petstore_and_beau.setVisibility(View.VISIBLE);
			setLastPrice(true);
//			-----------下边加急
			if (OrderType==2) {
				rlBeauticianinfo.setVisibility(View.GONE);
				show_petstore_and_beau.setVisibility(View.GONE);
				ungent_order.setVisibility(View.VISIBLE);
			}else {
				llPetStore.setVisibility(View.VISIBLE);
				show_petstore_and_beau.setVisibility(View.VISIBLE);
				rlBeauticianinfo.setVisibility(View.VISIBLE);
				ungent_order.setVisibility(View.GONE);
			}
			
		}else if(ps.addrtype==2){
			serviceloc = 2;
//			tvAddrType.setText("上门");
//			tvAddrType.setBackgroundResource(R.drawable.bg_blue_round);
			showPetStore(false);
			setLastPrice(true);
			ungent_order.setVisibility(View.GONE);
			show_petstore_and_beau.setVisibility(View.GONE);
			if (OrderType==4) {
				llPetStore.setVisibility(View.GONE);
				rlBeauticianinfo.setVisibility(View.GONE);
				show_petstore_and_beau.setVisibility(View.GONE);
				ungent_order.setVisibility(View.VISIBLE);
			}
			
			rlPickup.setVisibility(View.GONE);//上门
			
		}
		if (previous==Global.URGENT_TO_ORDERDETAIL) {
			tvTime.setText("预约时间："+month+" "+times+":00:00");
		}
		//流程接送
//		setPickShow(ps); //此接送由接口控制
		
		if(ps.beautician_id>0){
			Utils.mLogError("===----->ps.beautfician_image "+ps.beautician_image);
			ImageLoaderUtil.loadImg(ps.beautician_image,srivBeautician,  0,null);
			
			if(ps.beautician_name!=null&&!"".equals(ps.beautician_name))
				tvBeauticianName.setText(ps.beautician_name);
			tvBeauticianOrdernum.setText("接单："+ps.beautician_ordernum);
//			showStars(ps.beautician_levels,ps.beautician_stars);
//			showStars(0,ps.beautician_stars);
			if(ps.beautician_sex==1)
				srivBeauticianSex.setImageResource(R.drawable.icon_man);
			else
				srivBeauticianSex.setImageResource(R.drawable.icon_women);
//			if(ps.beautician_sort==10)
//				bt_orderdetail_beautician_level.setText("中级");
//			else if(ps.beautician_sort==20)
//				bt_orderdetail_beautician_level.setText("高级");
//			else if(ps.beautician_sort==30)
//				bt_orderdetail_beautician_level.setText("首席");
			if (TextUtils.isEmpty(ps.upgradeTip)) {
				textview_isgo_update.setVisibility(View.GONE);
			}else {
				textview_isgo_update.setVisibility(View.VISIBLE);
				textview_isgo_update.setText(ps.upgradeTip);
			}
		}
		setBeauLeve(ps);
	}

	private void setBeauLeve(PetService ps) {
		Utils.mLogError("==-->sort  "+ps.beautician_sort+" level "+ps.beautician_levels);
		switch (ps.beautician_sort) {
		case 10:
			text_lev.setText("中级美容师");
			break;
		case 20:
			text_lev.setText("高级美容师");
			break;
		case 30:
			text_lev.setText("首席美容师");
			break;
		}
	}

	private void setPickShow(PetService ps) {
		/*
		 * 接送结果pickupResult返回值定义：
		 * 0或是无：不支持接送，提示文案取pickupTip
		 * 1:可接送，价格字段：pickupPrice（0表示免费） 
		 * 2:接送已满，提示文案取pickupTip
		 * 3:不再接送范围，提示文案取pickupTip
		 */
//		if("1".equals(ps.pickupopt)&&ps.addrtype==1){
//			//显示接送服务
//			if(ps.pickupprice==0){
//				tvPickupPrice.setText("免费");
//			}else{
//				
//				tvPickupPrice.setText("¥"+Utils.formatDouble(ps.pickupprice*petSize));
//			}
//
//			showPickup(1);
//		}else if("2".equals(ps.pickupopt)&&ps.addrtype==1){
//			//接送名额已满
//			showPickup(2);
//		}else{
//			if(ps.addrtype==1)
//				showPickup(3);
//			else
//				showPickup(0);
//		}
		if("1".equals(ps.pickupopt)&&ps.addrtype==1){
			
			//显示接送服务
			if(ps.pickupprice==0){
//				tvPickupPrice.setText("免费");
				tvPickupPrice.setText(Utils.getTextAndColorFour(
						"#BB996C", "免费", 
						"#333333", "(剩余:", 
						"#BB996C", ps.restAmount, 
						"#333333", "个)"));
			}else{
//				tvPickupPrice.setText("¥"+Utils.formatDouble(ps.pickupprice*petSize));
				tvPickupPrice.setText(Utils.getTextAndColorFour(
						"#BB996C", "¥"+Utils.formatDouble(ps.pickupprice/**petSize*/), 
						"#333333", "(剩余:", 
						"#BB996C", ps.restAmount, 
						"#333333", "个)"));
			}

			showPickup(1,ps.pickupTip);
		}
//		else if ("2".equals(ps.pickupopt)&&ps.addrtype==1) {
//			//接送名额已满
//			showPickup(2,ps.pickupTip);
//		}else if ("3".equals(ps.pickupopt)&&ps.addrtype==1) {
//			//不再接送范围，提示文案取pickupTip
//			showPickup(3,ps.pickupTip);
//		}
		else if ("0".equals(ps.pickupopt)&&ps.addrtype==1) {
			//0或是无：不支持接送，提示文案取pickupTip
			showPickup(0,ps.pickupTip);
		}else {
			if(ps.addrtype==2){
				showPickup(3,ps.pickupTip);
			}
			else{
				showPickup(0,ps.pickupTip);
			}
		}
	}
	
	private void getIsCanPick(int addressId) {
		// TODO Auto-generated method stub
		if (previous!=Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			Utils.mLogError(
					"==-->isCanPickUp 参数  "+
							spUtil.getString("cellphone", "")
							+" shopId "+shopId 
							+" addressId "+addressId
							+" month "+month
							+" times "+times
							+" serviceLoc "+1);
			CommUtil.canBePickup(mContext, spUtil.getString("cellphone", ""),spUtil.getInt("tareaid", 0),shopId,addressId,month,times, 1,ps.addr_lat,ps.addr_lng,handler);
		}
	}
	private void getIsCanPickNormal(int addressId) {
		// TODO Auto-generated method stub
		if (previous!=Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			Utils.mLogError(
					"==-->isCanPickUp 参数  "+
			spUtil.getString("cellphone", "")
					+" shopId "+shopId 
					+" addressId "+addressId
					+" month "+month
					+" times "+times
					+" serviceLoc "+1);
			CommUtil.canBePickup(mContext, spUtil.getString("cellphone", ""),spUtil.getInt("tareaid", 0),ps.petstoreid,addressId,null,ps.servicedate, 1,ps.addr_lat,ps.addr_lng,handler);
		}
	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData  = object.getJSONObject("data");
						if (objectData.has("pickupResult")&&!objectData.isNull("pickupResult")) {
							int pickupResult = objectData.getInt("pickupResult");
							ps.pickupopt = pickupResult+"";
						}
						if (objectData.has("pickupTip")&&!objectData.isNull("pickupTip")) {
							String pickupTip = objectData.getString("pickupTip");
							ps.pickupTip = pickupTip+"";
						}
						if (objectData.has("restAmount")&&!objectData.isNull("restAmount")) {
							String restAmount = objectData.getString("restAmount");
							ps.restAmount = restAmount+"";
						}
						if (objectData.has("pickupPrice")&&!objectData.isNull("pickupPrice")) {
							double pickupPrice = Utils.formatDouble(objectData.getDouble("pickupPrice"));
							ps.pickupprice=pickupPrice;
						}
						if (objectData.has("topTips")&&!objectData.isNull("topTips")) {
							try {
								JSONArray array = objectData.getJSONArray("topTips");
								ArrayList<String> list = new ArrayList<String>();
								for (int i = 0; i < array.length(); i++) {
									list.add(array.getString(i));
								}
								textview_urgnet_time.setText(list.get(0).replace("#", ""));
								textviwe_urgent_notice.setText(list.get(1));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (objectData.has("bottomTips")&&!objectData.isNull("bottomTips")) {
							text_all_order_notice.setVisibility(View.VISIBLE);
							try {
								JSONArray objectTip = objectData.getJSONArray("bottomTips");
								ArrayList<String> list = new ArrayList<String>();
								for (int i = 0; i < objectTip.length(); i++) {
									list.add(objectTip.getString(i));
								}
								text_all_order_notice.setText(list.get(0));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							text_all_order_notice.setVisibility(View.GONE);
						}
						setPickShow(ps);
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
	private void showPetStore(boolean show){
		if(show){
			llPetStore.setVisibility(View.VISIBLE);
			rlBeauticianinfo.setVisibility(View.GONE);
			show_petstore_and_beau.setVisibility(View.GONE);
		}else{
			llPetStore.setVisibility(View.GONE);
			rlBeauticianinfo.setVisibility(View.VISIBLE);
			show_petstore_and_beau.setVisibility(View.VISIBLE);
			
		}
	}
	private void showPickup(int show,String tips){
		if(show==1){
			//可以接送
			rlPickup.setVisibility(View.VISIBLE);
//			vPickupLine.setVisibility(View.VISIBLE);
			tvPickupHot.setVisibility(View.GONE);
			tvPickupPrice.setVisibility(View.VISIBLE);
			ivPickup.setVisibility(View.VISIBLE);
			rlPickup.setEnabled(true);
		}
		else if (show==0) {
			//不支持接送
			rlPickup.setVisibility(View.VISIBLE);
//			vPickupLine.setVisibility(View.VISIBLE);
			tvPickupHot.setVisibility(View.VISIBLE);
			tvPickupPrice.setVisibility(View.GONE);
			ivPickup.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(tips)) {
				tvPickupHot.setText(tips);
			}else {
				tvPickupHot.setText("暂不支持接送");
			}
			rlPickup.setEnabled(false);
		}else if(show==3){
			//不显示
			rlPickup.setVisibility(View.GONE);
			show_pickup_line.setVisibility(View.GONE);
		}
		else{
			//不显示
//			rlPickup.setVisibility(View.GONE);
//			show_pickup_line.setVisibility(View.GONE);
//			vPickupLine.setVisibility(View.GONE);
			
			rlPickup.setVisibility(View.VISIBLE);
//			vPickupLine.setVisibility(View.VISIBLE);
			tvPickupHot.setVisibility(View.VISIBLE);
			tvPickupPrice.setVisibility(View.GONE);
			ivPickup.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(tips)) {
				tvPickupHot.setText(tips);
			}else {
				tvPickupHot.setText("暂不支持接送");
			}
			rlPickup.setEnabled(false);
		}
	}
	

	private void showStars(int levels, int stars){
		ivBeauticianLevel1.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel2.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel3.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel4.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel5.setBackgroundResource(R.drawable.bk_empty);
		if(levels==0)
			return;
		if(levels == 1){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_1);
				break;
				
			}
		}else if(levels == 2){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_2);
				break;
				
			}
		}else if(levels == 3){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_3);
				break;
				
			}
		}else if(levels == 4){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_4);
				break;
				
			}
		}
	}
	private void goNextForData(Class clazz, int requestcode){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY);
		intent.putExtra("remarks", getIntent().getStringArrayExtra("remarks"));
		intent.putExtra("cname", cname);
		intent.putExtra("cphone", cphone);
		intent.putExtra("remark", tvNote.getText().toString().trim());
		intent.putExtra("couponId", couponId);
		startActivityForResult(intent, requestcode);
	}
	
	private void goNext(Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra("previous", Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY);
		intent.putExtra("notices", notices);
		startActivity(intent);
		this.overridePendingTransition(R.anim.center_in, R.anim.center_out);
	}
	public String getPetId_ServiceId_MyPetId(ArrayList<Pet> list){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Pet mps = list.get(i);
			if (i<list.size()-1) {
				sb.append(mps.id);
				sb.append("_");
				sb.append(mps.serviceid);
				sb.append("_");
				sb.append(mps.customerpetid);
				sb.append("_");
			}else {
				sb.append(mps.id);
				sb.append("_");
				sb.append(mps.serviceid);
				sb.append("_");
				sb.append(mps.customerpetid);
			}
		}
		return sb.toString();
		
	}
	
	private String getPetID_ServiceId_MypetId_ItemIds(ArrayList<MulPetService> list){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<list.size();i++){
			MulPetService mps = list.get(i);
			if(i<list.size()-1){
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if(mps.addServiceIds!=null&&!"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
				sb.append("_");
				
			}else{
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if(mps.addServiceIds!=null&&!"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
			}
		}
		return sb.toString();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Global.RESULT_OK){
			if(requestCode == Global.ORDERDETAILREQUESTCODE_CONTACT){
				cphone = data.getStringExtra("contactphone");
				cname = data.getStringExtra("contactname");
//				tvContactPhone.setText(cname+" "+cphone);
				tvContactPhone.setText(cname);
				user_phone_num.setText(cphone);
			}else if(requestCode == Global.ORDERDETAILREQUESTCODE_COUPON){
				couponId = data.getIntExtra("couponid", 0);
				String couponcontent = data.getStringExtra("content");
				String camount = data.getStringExtra("amount") == null || "".equals(data.getStringExtra("amount"))?"0":data.getStringExtra("amount");
				couponAmount = Utils.formatDouble(Double.parseDouble(camount));
				tvCoupon.setText(couponcontent);
				if(couponAmount >= totalMoney){
					needMoney = 0;
					paytype = 3;
					setLastPrice(false);
				}else{
					needMoney = totalMoney - couponAmount;
					setLastPrice(true);
				}
			}else if(requestCode == Global.ORDERDETAILREQUESTCODE_NOTE){
				String note = data.getStringExtra("note");
				tvNote.setText(note);
			}else if(requestCode == Global.BATH_TO_ORDERPAY){
				orderNo = data.getIntExtra("orderid", 0);
			}else if (requestCode==Global.CARDSORDERDETAIL_TO_CHOOSE_CARDS) {//选择次卡返回
				CardsCutMoney = 0;
				cardsConfirmOrders = data.getParcelableArrayListExtra("cardsConfirmOrders");
				index = data.getIntExtra("index", -1);
				switch (index) {
				case 0:
					tv_orderdetail_card.setText("不使用服务卡");
					totalMoney = topLeftMoney;
					needMoney = totalMoney;
					listIds.clear();
					getCouponNeedMoney = needMoney;
					getCoupon();
					break;
				case 1:
					if (!cardsConfirmOrders.isEmpty()) {
						totalMoney = topLeftMoney;
						setCardPrice(cardsConfirmOrders);
					}
//					getCoupon();
					break;

				}
				
				
			}
		}
	}
	/**
	 * 获取可用的优惠券
	 */
	private void getCoupon(){
		pDialog.showDialog();
		couponList.clear();
		couponId = 0;
		if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			Utils.mLogError("==-->游泳请求优惠券");
			serviceloc = 1;
			pickup = 0;
			CommUtil.mycouponsForSwim(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
					Global.getCurrentVersion(this), dataString, 3, serviceloc,
					workerid,pickup,cname,cphone,addressid,address, lat, lng,
					getPetId_ServiceId_MyPetId(listPets),spUtil.getInt("tareaid", 0),
					Utils.formatDouble(needMoney),shopId,
					couponHanler);
		}else {
			CommUtil.getAvailableCoupon(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
					Global.getCurrentVersion(this), date, 1, serviceloc,
					workerid,pickup,cname,cphone,addressid,address, lat, lng,
					getPetID_ServiceId_MypetId_ItemIds(mpsList),spUtil.getInt("tareaid", 0),Utils.formatDouble(needMoney),ps.petstoreid,
					null,couponHanler);
		}
	}
	private void sendIsExOrder(){
		pDialog.showDialog();
		if(needMoney<0)
			needMoney=0;
		CommUtil.saveOrder(
				mContext, 
				spUtil.getString("cellphone", ""),
				spUtil.getInt("userid", 0), 
				clicksort, 
				serviceloc, 
				couponId,
				getPetID_ServiceId_MypetId_ItemIds(mpsList), 
				address,
				addressid, 
				lng, 
				month, 
				times, 
				lat, 
				spUtil.getInt("tareaid", 0),
				shopId,
				paytype, 
				Utils.formatDouble(needMoney), 
				-1,
				pickup,
				cname,
				cphone,
				tvNote.getText().toString().trim(),
				getListIds(listIds),
				orderHanler
				);
	}
	private void SwimOrder(){
		pDialog.showDialog();
		if(needMoney<0)
			needMoney=0;
		/**
		 * 地址经纬度跟后台协商传随意值，后台不保存
		 */
		addr="地址经纬度跟后台协商传随意值，后台不保存";
		lat = 1;
		lng = 1;
//		addrid=1;
		CommUtil.getSwimOrder(mContext,spUtil.getString("cellphone", ""), 
				shopId,0,couponId,workerid,addrid,cname,cphone,addr,lat, 
				lng,dataString,Utils.formatDouble(totalMoney),
				Utils.formatDouble(needMoney),paytype, 
				tvNote.getText().toString().trim(),1,0,0, 
				getPetId_ServiceId_MyPetId(listPets), -1, uuid.toString(),null,orderHanler);
	}
	private void sendOrder(){
		pDialog.showDialog();
		if(needMoney<0)
			needMoney=0;
		
		CommUtil.newOrder(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this), 
				ps.petstoreid,ps.areaid, couponId, workerid, addressid, cname, 
				cphone, address, lat, lng, date, Utils.formatDouble(totalMoney),
				Utils.formatDouble(needMoney),
				paytype,tvNote.getText().toString().trim(),
				serviceloc,pickup,ps.isDefaultWorker,
				getPetID_ServiceId_MypetId_ItemIds(mpsList),-1,
				uuid.toString(),getListIds(listIds),orderHanler);
	}
	
	private void changepay(){
		pDialog.showDialog();
		if(needMoney<0)
			needMoney=0;
		CommUtil.changeOrderPayMethodV2(null,spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderNo, 
				spUtil.getInt("userid", 0),cname,cphone,tvNote.getText().toString().trim(),
				pickup,-1,-1,Utils.formatDouble(needMoney),paytype,
				couponId,-1,null,isVie,getListIds(listIds),changeHanler);
	}
	
	private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
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
						if (needMoney==0||paytype==3) {
							//直接跳转到支付成功
							goPayResult();
						}
				}else{
					ToastUtil.showToastShort(OrderDetailActivity.this, msg);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
		
	};
	
	
	private AsyncHttpResponseHandler couponHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("可用的优惠券："+new String(responseBody));
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONArray jData = jobj.getJSONArray("data");
					for(int i = 0; i < jData.length(); i++){
						JSONObject jcoupon = jData.getJSONObject(i);
						couponList.add(Coupon.jsonToEntity(jcoupon));
					}
					if (TextUtils.isEmpty(builderServiceType)) {
						totalMoney = totalMoney+extraFee-reductionFee;
					}else {
						if (builderServiceType.toString().contains("1")||builderServiceType.toString().contains("2")||builderServiceType.toString().contains("3")) {
							
						}else {
							totalMoney = totalMoney+extraFee-reductionFee;
						}
					}
					if(couponList.size() > 0){
						couponId = couponList.get(0).id;
						tvCoupon.setText("-"+couponList.get(0).amount+"("+couponList.get(0).name+")");
						String camount = couponList.get(0).amount == null || "".equals(couponList.get(0).amount)?"0":couponList.get(0).amount;
						couponAmount = Utils.formatDouble(Double.parseDouble(camount));
//						getCouponNeedMoney = needMoney;
						if(couponAmount >= totalMoney){
							needMoney = 0;
//							getCouponNeedMoney = needMoney;
							paytype = 3;
							setLastPrice(false);
						}else{
							needMoney = totalMoney - couponAmount;
//							getCouponNeedMoney = needMoney;
							setLastPrice(true);
						}
						tvCoupon.setTextColor(Color.parseColor("#D1494F"));
					}else {
						tvCoupon.setTextColor(Color.parseColor("#666666"));
						needMoney = totalMoney - couponAmount;
//						getCouponNeedMoney = needMoney;
						setLastPrice(true);
					}
				}else {
//					if (couponList.size()<=0) {
//						totalMoney = totalMoney+extraFee-reductionFee;
//					}
					if (couponList.size()<=0) {
						if (TextUtils.isEmpty(builderServiceType)) {
							totalMoney = totalMoney+extraFee-reductionFee;
						}else {
							if (builderServiceType.toString().contains("1")||builderServiceType.toString().contains("2")||builderServiceType.toString().contains("3")) {
								
							}else {
								totalMoney = totalMoney+extraFee-reductionFee;
							}
						}
					}
					tvCoupon.setTextColor(Color.parseColor("#666666"));
					needMoney = totalMoney - couponAmount;
					getCouponNeedMoney = needMoney;
					setLastPrice(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				pDialog.closeDialog();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
		
	};
	private AsyncHttpResponseHandler orderHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("生成新订单："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					spUtil.saveInt("payway", paytype);
					JSONObject jData = jobj.getJSONObject("data");
					if(jData.has("orderId")&&!jData.isNull("orderId")){
						orderNo = Integer.parseInt(jData.getString("orderId"));
						Utils.mLogError("==-->orderNo "+orderNo);
						try {
							mpsList.get(0).orderid=orderNo;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							listPets.get(0).orderid=orderNo;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(jData.has("orderPet")&&!jData.isNull("orderPet")){
						JSONArray jarr = jData.getJSONArray("orderPet");
						try {
							for(int i=0;i<jarr.length()&&i<mpsList.size();i++){
								mpsList.get(i).orderid=jarr.getInt(i);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					sendBroadcastToMainUpdataUserinfo();
					if(needMoney == 0||paytype==3){
						//直接跳转到支付成功
						goPayResult();
					}else{
						Utils.mLogError("用第三方支付"+needMoney);
						
					}
				}else if(109 == resultCode){
					//重复提交，不做处理
				}else{
					if(jobj.has("msg")&&!jobj.isNull("msg"))
						ToastUtil.showToastShort(OrderDetailActivity.this, jobj.getString("msg"));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pDialog.closeDialog();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
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
	private LinearLayout rlBeauticianinfo;
	private RelativeLayout rlPickup;
	private TextView tvPickupHot;
	private TextView tvPickupPrice;
	private ImageView ivPickup;
//	private View vPickupLine;
	private ImageView ivPickupHint;
	private MListview lvPets;
//	private Button bt_orderdetail_beautician_level;
	private void goAddPetHint(){
		ArrayList<MulPetService> mpstmpList = new ArrayList<MulPetService>();
		for(int i=0;i<mpsList.size();i++){
			if(mpsList.get(i).petCustomerId<=0)
				mpstmpList.add(mpsList.get(i));
		}
		if(mpstmpList.size()>0){
			//到添加宠物界面
			Intent intent = new Intent(OrderDetailActivity.this, OrderListShowAddMyPet.class);
			intent.putParcelableArrayListExtra("mulpetservice", mpstmpList);
			startActivity(intent);
		}
		finishWithAnimation();
		
	}
	private void sendBroadcastToMainUpdataUserinfo(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY);
		sendBroadcast(intent);
	}
	
	private void clearData(){
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
	
	
	private void goPayResult(){
		Intent intent = new Intent(act, PaySuccessActivity.class);
		if (previous!=Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			intent.putParcelableArrayListExtra("mpslist", mpsList);
			intent.putExtra("previous", Global.SWIM_DETAIL_TO_ORDERDETAIL);
		}
		if (previous==Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			intent.putExtra("orderId", listPets.get(0).orderid);
		}else {
			intent.putExtra("orderId", mpsList.get(0).orderid);
		}
		intent.putExtra("type", 1);
		startActivity(intent);
		finishWithAnimation();

//		ToastUtil.showToastShortCenter(this, "订单支付成功");
//		for(int i=0;i<MApplication.listAppoint.size();i++){
//			MApplication.listAppoint.remove(i);
//		}
//		if(ServiceActivity.act!=null)
//			ServiceActivity.act.finishWithAnimation();
//		if(ServiceFeature.act!=null)
//			ServiceFeature.act.finishWithAnimation();
//		if(AppointmentActivity.act!=null)
//			AppointmentActivity.act.finishWithAnimation();
//		if(CharacteristicServiceActivity.act!=null)
//			CharacteristicServiceActivity.act.finish();
//		if(PetInfoActivity.act!=null)
//			PetInfoActivity.act.finish();
//		if(MainToBeauList.act!=null)
//			MainToBeauList.act.finish();
//		if(BeauticianDetailActivity.act!=null)
//			BeauticianDetailActivity.act.finish();
//		clearData();
//		//去添加宠物
//		if(mpsList!=null&&mpsList.size()>0){
//			getPets();
//		}else{
//			finishWithAnimation();
//		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
		
		
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    MobclickAgent.onPageStart("OrderDetailActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	    MobclickAgent.onPageEnd("OrderDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
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
	private SpannableString getTextShow(String str) {
		SpannableString styledText = new SpannableString(str);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.style1), 1,str.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private AsyncHttpResponseHandler confirmOrderPrompt = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			Utils.mLogError("==-->新增免责声明：="+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						layout_notice.removeAllViews();
						if (objectData.has("prompt")&&!objectData.isNull("prompt")) {
							String prompt = objectData.getString("prompt");
							TextView textView = new TextView(mContext);
							textView.setTextColor(getResources().getColor(R.color.a666666));
							textView.setText(prompt);
							layout_notice.addView(textView);
						}
						if (objectData.has("reductionTag")&&!objectData.isNull("reductionTag")) {
							JSONObject objectReductionTag = objectData.getJSONObject("reductionTag");
							if (objectReductionTag.has("reductionTag")&&!objectReductionTag.isNull("reductionTag")) {
								couponCutReductionTag = objectReductionTag.getString("reductionTag");
							}
							if (objectReductionTag.has("cardTag")&&!objectReductionTag.isNull("cardTag")) {
								CardTag=objectReductionTag.getString("cardTag");
							}
						}
						if (serviceloc==2) {
							layout_go_home_notice.setVisibility(View.VISIBLE);
							rl_orderdetail_go_home_service_price.setVisibility(View.VISIBLE);
							if (objectData.has("extraFeeTag")&&!objectData.isNull("extraFeeTag")) {
								String extraFeeTag = objectData.getString("extraFeeTag");
								textview_go_home_extraFeeTag.setText(extraFeeTag);
							}
//							textview_go_home_extraFee
							if (objectData.has("extraFee")&&!objectData.isNull("extraFee")) {
								extraFee = objectData.getDouble("extraFee");
								textview_go_home_extraFee.setText("¥"+extraFee);
								if (extraFee<=0) {
									extraFee = 0;
									layout_go_home_notice.setVisibility(View.GONE);
								}
							}else {
								layout_go_home_notice.setVisibility(View.GONE);
							}
							if (objectData.has("reductionFeeTag")&&!objectData.isNull("reductionFeeTag")) {
								String reductionFeeTag = objectData.getString("reductionFeeTag");
								tv_orderdetail_go_home_left.setText(reductionFeeTag);
							}
							if (objectData.has("reductionFee")&&!objectData.isNull("reductionFee")) {
								reductionFee = objectData.getDouble("reductionFee");
								tv_orderdetail_go_home_service_price.setText("-¥"+reductionFee);
								if (reductionFee<=0) {
									reductionFee = 0;
									rl_orderdetail_go_home_service_price.setVisibility(View.GONE);
								}
							}else {
								rl_orderdetail_go_home_service_price.setVisibility(View.GONE);
							}
							if (objectData.has("fee_tips")&&!objectData.isNull("fee_tips")) {
								JSONArray array = objectData.getJSONArray("fee_tips");
								if (array.length()>0) {
									notices = new String[array.length()];
									for (int i = 0; i < array.length(); i++) {
										notices[i] = array.getString(i);
									}
								}
							}
							Utils.mLogError("==-->extraFee0:= "+extraFee+" reductionFee:= "+reductionFee);
							totalMoney = totalMoney+extraFee-reductionFee-couponAmount;
							needMoney = totalMoney;
							getCouponNeedMoney = needMoney;
							if (needMoney>0) {
								getCouponNeedMoney = needMoney;
								setLastPrice(true);	
							}else {
								needMoney = 0;
								paytype = 3;
								setLastPrice(false);	
							}
						}else if (serviceloc ==1 ) {
							layout_go_home_notice.setVisibility(View.GONE);
							rl_orderdetail_go_home_service_price.setVisibility(View.GONE);
							extraFee = 0;
							reductionFee = 0;
							Utils.mLogError("==-->extraFee1:= "+extraFee+" reductionFee:= "+reductionFee);
						}
						if (objectData.has("timeCardTag")&&!objectData.isNull("timeCardTag")) {
							JSONObject objectTimeCardTag  = objectData.getJSONObject("timeCardTag");
							if (objectTimeCardTag.has("title")&&!objectTimeCardTag.isNull("title")) {
								tv_orderdetail_card_pre.setText(objectTimeCardTag.getString("title"));
							}
						}
						textview_give_other_money.setVisibility(View.GONE);
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
//									if (objectEveryCards.has("bathTitleTag")&&!objectEveryCards.isNull("bathTitleTag")) {
//										confirmOrder.bathTitleTag = objectEveryCards.getString("bathTitleTag");
//									}
//									if (objectEveryCards.has("bathCountTag")&&!objectEveryCards.isNull("bathCountTag")) {
//										confirmOrder.bathCountTag = objectEveryCards.getString("bathCountTag");
//									}
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
//									if (objectEveryCards.has("beautyTitleTag")&&!objectEveryCards.isNull("beautyTitleTag")) {
//										confirmOrder.beautyTitleTag = objectEveryCards.getString("beautyTitleTag");
//									}
//									if (objectEveryCards.has("beautyCountTag")&&!objectEveryCards.isNull("beautyCountTag")) {
//										confirmOrder.beautyCountTag = objectEveryCards.getString("beautyCountTag");
//									}
//									if (objectEveryCards.has("beautyCount")&&!objectEveryCards.isNull("beautyCount")) {
//										confirmOrder.beautyCount = objectEveryCards.getInt("beautyCount");
//										confirmOrder.beautyCountOld = objectEveryCards.getInt("beautyCount");
//										confirmOrder.serviceTypeBeau=2;
//										
//									}
//									if (objectEveryCards.has("bathCount")&&!objectEveryCards.isNull("bathCount")) {
//										confirmOrder.bathCount = objectEveryCards.getInt("bathCount");
//										confirmOrder.bathCountOld = objectEveryCards.getInt("bathCount");
//										confirmOrder.serviceTypeBath=1;
//									}
//									if (objectEveryCards.has("beautyLevel")&&!objectEveryCards.isNull("beautyLevel")) {
//										int beautyLevel = objectEveryCards.getInt("beautyLevel");
//										if (beautyLevel==1) {
//											confirmOrder.clicksortBeautyLevel=10;
//										}else if (beautyLevel==2) {
//											confirmOrder.clicksortBeautyLevel=20;
//										}else if (beautyLevel==3) {
//											confirmOrder.clicksortBeautyLevel=30;
//										}
//									}
//									if (objectEveryCards.has("bathLevel")&&!objectEveryCards.isNull("bathLevel")) {
//										int bathLevel = objectEveryCards.getInt("bathLevel");
//										if (bathLevel==1) {
//											confirmOrder.clicksortbathLevel=10;
//										}else if (bathLevel==2) {
//											confirmOrder.clicksortbathLevel=20;
//										}else if (bathLevel==3) {
//											confirmOrder.clicksortbathLevel=30;
//										}
//									}
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
//													builder.append(cardItems.id+",");
												}
												if (objectEva.has("count")&&!objectEva.isNull("count")) {
													cardItems.count = objectEva.getInt("count");
													cardItems.oldCount = objectEva.getInt("count");
//													builder.append(cardItems.count+",");
//													builder.append(cardItems.oldCount+",");
												}
												if (objectEva.has("level")&&!objectEva.isNull("level")) {
													cardItems.level = objectEva.getInt("level");
													if (cardItems.level==1) {
														cardItems.level=10;
													}else if (cardItems.level==2) {
														cardItems.level=20;
													}else if (cardItems.level==3) {
														cardItems.level=30;
													}
//													builder.append(cardItems.count+",");
												}
												if (objectEva.has("title")&&!objectEva.isNull("title")) {
													cardItems.title = objectEva.getString("title");
//													builder.append(cardItems.title+",");
												}
												if (objectEva.has("countTag")&&!objectEva.isNull("countTag")) {
													cardItems.countTag = objectEva.getString("countTag");
//													builder.append(cardItems.countTag+",");
												}
												if (objectEva.has("serviceType")&&!objectEva.isNull("serviceType")) {
													cardItems.serviceType = objectEva.getInt("serviceType");
//													builder.append(cardItems.serviceType);
												}
												confirmOrder.arrayList.add(cardItems);
//												confirmOrder.cardItems = builder.toString();
											}
										}
									}
									
									OldCardsConfirmOrders.add(confirmOrder);
									cardsConfirmOrders.add(confirmOrder);
								}
							}
						}
					}
					if (cardsConfirmOrders.size()>0) {
//						layout_cards_choose.setVisibility(View.VISIBLE);
						setCardPrice(cardsConfirmOrders);
					}else if(cardsConfirmOrders.size()<=0) {
						tv_orderdetail_card.setTextColor(Color.parseColor("#666666"));
//						layout_cards_choose.setVisibility(View.VISIBLE);
					}
				}
				if (!TextUtils.isEmpty(builderServiceType)) {
					if (builderServiceType.toString().contains("1")||builderServiceType.toString().contains("2")||builderServiceType.toString().contains("3")) {
						layout_cards_choose.setVisibility(View.VISIBLE);
						if (needMoney>0) {
							getCouponNeedMoney = needMoney;
							getCoupon();
						}else {
							paytype = 7;
							tvCoupon.setText("无需使用优惠券");
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
					Utils.mLogError("==-->00000 比熊 1 "+mpsList.get(i).petId+"_"+mpsList.get(i).serviceType+"_"+mpsList.get(i).clicksort+"_"+mpsList.get(i).serviceId);
					Utils.mLogError("==-->00000 比熊 2 "+cardsConfirmOrders.get(j).arrayList.get(j2).petId+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).serviceType+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).level+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).id);

					if((mpsList.get(i).petId+"_"+mpsList.get(i).serviceType+"_"+mpsList.get(i).clicksort+"_"+mpsList.get(i).serviceId).equals(cardsConfirmOrders.get(j).arrayList.get(j2).petId+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).serviceType+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).level+"_"+cardsConfirmOrders.get(j).arrayList.get(j2).id)){
						Utils.mLogError("==-->22222 "+cardsConfirmOrders.get(j).arrayList.get(j2).count);
						if (cardsConfirmOrders.get(j).arrayList.get(j2).count>0) {
							if (isChoose) {
								if (cardsConfirmOrders.get(j).isChoose==1) {
									cardsConfirmOrders.get(j).isChoose=1;
									needMoney = totalMoney-=mpsList.get(i).basefee;
									CardsCutMoney = CardsCutMoney+=mpsList.get(i).basefee;
									if (mpsList.get(i).serviceType==1) {
										bathNum++;
									}else if (mpsList.get(i).serviceType==2) {
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
								needMoney = totalMoney-=mpsList.get(i).basefee;
								CardsCutMoney = CardsCutMoney+=mpsList.get(i).basefee;
								if (mpsList.get(i).serviceType==1) {
									bathNum++;
								}else if (mpsList.get(i).serviceType==2) {
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
//			orderCard.bathCount = orderCard.bathCountOld;
//			orderCard.beautyCount = orderCard.beautyCountOld;
			cardsConfirmOrders.set(i, orderCard);
		}

		if (bathNum>0&&beauNum>0) {
			tv_orderdetail_card.setText(CardTag+bathNum+"次洗澡"+beauNum+"美容");
		}else if (bathNum>0&&beauNum<=0) {
			tv_orderdetail_card.setText(CardTag+bathNum+"次洗澡");
		}else if (bathNum<=0&&beauNum>0) {
			tv_orderdetail_card.setText(CardTag+beauNum+"次美容");
		}else if (bathNum<=0&&beauNum<=0&&feature>0) {
			tv_orderdetail_card.setText("服务卡支付"+feature+"次特色服务");
		}else if (bathNum==0&&beauNum==0) {
			tv_orderdetail_card.setText("未使用"+CardTag);
		}
		if (needMoney>0) {//第一次调用是在初始进入流程里调用的 选择次卡返回如果选择了次卡在这里并且如果支付价格>0那么在这里需要调用下优惠券
			setLastPrice(true);
			if (index==1) {
				getCouponNeedMoney = needMoney;
				getCoupon();
			}
		}else {
			needMoney = 0;
			paytype=7;
//			paytype = 7;
			tvCoupon.setText("无需使用优惠券");
			couponAmount = 0;
			couponId=0;
//			getCouponNeedMoney = needMoney;
			setLastPrice(false);
		}
		if (listIds.size()>0) {
//			isUsedCard = true;
		}else {
//			isUsedCard = false;
			listIds.clear();
		}
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
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			previous_liucheng = intent.getIntExtra("previous_liucheng",0);
			Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 111 --- "+previous_liucheng);
			if (previous_liucheng>0&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD) {
				scroll_view_top.scrollTo(0, 0);
				totalMoney = topLeftMoney;
				Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 222 --- "+previous_liucheng+"  "+totalMoney);
				ispickup=false;
				ivPickup.setBackgroundResource(R.drawable.icon_pay_normal);
				setView();
			}
		}
		
	}

}
