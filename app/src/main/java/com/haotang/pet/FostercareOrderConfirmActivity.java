package com.haotang.pet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.Fostercare;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.TrainServiceEvery;
import com.haotang.pet.entity.TrainTransfer;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;

public class FostercareOrderConfirmActivity extends SuperActivity implements OnClickListener{
	public static SuperActivity act;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private ImageButton ibBack;
	private TextView tvTitle;
	private ScrollView prsScrollView;
	private TextView tvShopName;
	private TextView tvPetName;
	private TextView tvPetAddr;
	private TextView tvStartDate;
	private TextView tvEndDate;
	private TextView tvRoomName;
	private TextView tvTotalRoomFee;
	private RelativeLayout rlContact;
	private TextView tvContact;
	private RelativeLayout rlNote;
	private TextView tvNote;
	private RelativeLayout rlBringFood;
	private ImageView ivBringFood;
	private TextView tvFoodPrice;
	private TextView tvTotalFoodFee;
	private LinearLayout llPickUp;
	private ImageView ivPickUp;
	private RelativeLayout rlCoupon;
	private TextView tvCoupon;
	private TextView tvTotalFee;
	private RelativeLayout rlTextExp;
	private ImageView ivTextExp;
	private TextView tvTextExp;
	private Button btPay;
	private String cphone;
	private String cname;
	private int paytype;
	private int couponId;
	private double couponAmount;
	private double totalMoney;
	private double needMoney;
	private double roomFee;
	private double foodFee;
	private double totalListFee;
	private double pickupFee;
	private double bathFee;
	private double balance;
	private boolean ispickup;
	private boolean isNeedFood;
	private boolean isNeedBath;
	private boolean isTwoNeedBath;
	private int pickup;
	private double lat;
	private double lng;
	private Fostercare fc;
	private int petid;
	private int shopid;
	private UUID uuid;
	private int orderNo;
	private boolean isHintShrink;//是否可收缩
	private boolean isShrink=true;//是否收缩
	private int needbath= -1;//是否洗澡 -1：不支持 0：不洗澡 1：洗澡
	private ImageView ivPickupHint;
	public static ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private String[] remarks=new String[4];
	
	private TextView old_price;
	private TextView textview_cutdown_money;
	private LinearLayout layout_no_cutmoney;
	private TextView textview_no_cutmoney_totalmoney;
	private LinearLayout layout_cutmoney;
	private int basicPrice;//应付
	private int listPrice;//原价
	private boolean couponEnable = true;
	private ImageView conpon_rightarrow;
	private TextView textview_pet_foodtag;
	private int transferId=0;
	private int PetIdNew = 0;
	
	private RelativeLayout rl_fostercareorderconfirm_bath_add_pet;
	private TextView tv_fostercareorderconfirm_bathtag_two;
	private TextView tv_fostercareorderconfirm_totalbathfee_two;
	private TextView tv_fostercareorderconfirm_bathcouponprice_two;
	private ImageView iv_fostercareorderconfirm_bath_two;
	private String PetNameNew ="";
	private String PetNameNickName ="";
	private TextView tv_fostercareorderconfirm_bath;
	private TextView tv_fostercareorderconfirm_bath_two;
	private RelativeLayout rl_fostercareorderconfirm_bath_two;
	private double TwoNeedbath;
	private double TwoBathFee;
	private double totalNeedBath;
	private int bathPetSize;
	private boolean isAddPet = false;
	private boolean isDelPet = false;
	private LinearLayout layout_notice;
	private CheckBox cbAgree;
	private TextView tv_fostercareappointment_agreement;
	//训练
	private int pre;
	private String [] confirmtip;//顶部提示
	private String time;//入训时间
	private int shopId;//门店id
	private String customerpetname;//宠物昵称
	private int customerpetid;//我的宠物id
	private String petname;//宠物名称
	private TrainServiceEvery trainServiceEvery = null;
	private TrainTransfer transfer = null;
	private TextView tv_fostercareorderconfirm_startdatepre;
	private TextView tv_fostercareorderconfirm_enddatepre;
	private TextView tv_fostercareorderconfirm_enddate;
	private TextView tv_fostercareorderconfirm_roompre;
	private TextView textview_choose_food;
	private int type;
	public String agreementPage = null;
	//寄养摄像头
	private LinearLayout layout_foster_shexiangtou;
	private TextView foster_shexiangtou;
	private TextView foster_add_shexiangtou;
	private ImageView foster_choose_or_not_shexiangtou;
	private TextView foster_shexiangtou_price;
	private double sheXiangTouPrice = 0;
	private boolean isChooseShexiangtou;
	private double needCameraPrice=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercare_orderconfirm);
		act = this;
//		showNotice();
		findView();
		setView();
		
	}

	private void showNotice() {
		Intent intent = new Intent(mContext, IsCanPickActivity.class);
		intent.putExtra("index", 2);
		startActivity(intent);
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		prsScrollView = (ScrollView) findViewById(R.id.prs_fostercareorderconfirm_main);
		rlTextExp = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_textexp);
		tvTextExp = (TextView) findViewById(R.id.tv_fostercareorderconfirm_textexp);
		ivTextExp = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_textexp);
		tvShopName = (TextView) findViewById(R.id.tv_fostercareorderconfirm_shopname);
		tvPetName = (TextView) findViewById(R.id.tv_fostercareorderconfirm_petname);
		tvPetAddr = (TextView) findViewById(R.id.tv_fostercareorderconfirm_petaddr);
		tvStartDate = (TextView) findViewById(R.id.tv_fostercareorderconfirm_startdate);
		tvEndDate = (TextView) findViewById(R.id.tv_fostercareorderconfirm_enddate);
		tvRoomName = (TextView) findViewById(R.id.tv_fostercareorderconfirm_room);
		tvTotalRoomFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_totalroomfee);
		
		rlContact = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_contact);
		tvContact = (TextView) findViewById(R.id.tv_fostercareorderconfirm_contact_phone);
		rlNote = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_note);
		tvNote = (TextView) findViewById(R.id.tv_fostercareorderconfirm_note);
		rlBringFood = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_bringfood);
		ivBringFood = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_bringfood);
		tvFoodPrice = (TextView) findViewById(R.id.tv_fostercareorderconfirm_foodprice);
		tvTotalFoodFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_totalfoodfee);
		llPickUp = (LinearLayout) findViewById(R.id.ll_fostercareorderconfirm_pickup);
		ivPickUp = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_pickup);
		tvPickupFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_pickup);
		tvPickupHint = (TextView) findViewById(R.id.tv_fostercareorderconfirm_pickup_hint);
		rlCoupon = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_coupon);
		tvCoupon = (TextView) findViewById(R.id.tv_fostercareorderconfirm_coupon);
		
		tvTotalFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_money);
		btPay = (Button) findViewById(R.id.bt_fostercareorderconfirm_pay);
		
		rlBath = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_bath);
		tvBathTag = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bathtag);
		tvBathFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_totalbathfee);
		tvBathCouponFee = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bathcouponprice);
		ivBath = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_bath);
		ivPickupHint = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_pickup_hint);
		old_price = (TextView) findViewById(R.id.old_price);
		textview_cutdown_money = (TextView) findViewById(R.id.textview_cutdown_money);
		layout_no_cutmoney = (LinearLayout) findViewById(R.id.layout_no_cutmoney);
		textview_no_cutmoney_totalmoney = (TextView) findViewById(R.id.textview_no_cutmoney_totalmoney);
		layout_cutmoney = (LinearLayout) findViewById(R.id.layout_cutmoney);
		conpon_rightarrow = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_conpon_rightarrow);
		textview_pet_foodtag = (TextView) findViewById(R.id.textview_pet_foodtag);
		layout_notice = (LinearLayout) findViewById(R.id.layout_notice);
		
		rl_fostercareorderconfirm_bath_add_pet = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_bath_add_pet);
		tv_fostercareorderconfirm_totalbathfee_two = (TextView) findViewById(R.id.tv_fostercareorderconfirm_totalbathfee_two);
		tv_fostercareorderconfirm_bathtag_two = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bathtag_two);
		tv_fostercareorderconfirm_bathcouponprice_two = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bathcouponprice_two);
		iv_fostercareorderconfirm_bath_two = (ImageView) findViewById(R.id.iv_fostercareorderconfirm_bath_two);
		tv_fostercareorderconfirm_bath = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bath);
		tv_fostercareorderconfirm_bath_two = (TextView) findViewById(R.id.tv_fostercareorderconfirm_bath_two);
		rl_fostercareorderconfirm_bath_two = (RelativeLayout) findViewById(R.id.rl_fostercareorderconfirm_bath_two);
		cbAgree = (CheckBox) findViewById(R.id.cb_fostercareappointment_agree);
		tv_fostercareappointment_agreement = (TextView) findViewById(R.id.tv_fostercareappointment_agreement);
		
		tv_fostercareorderconfirm_startdatepre = (TextView) findViewById(R.id.tv_fostercareorderconfirm_startdatepre);
		tv_fostercareorderconfirm_enddatepre = (TextView) findViewById(R.id.tv_fostercareorderconfirm_enddatepre);
		tv_fostercareorderconfirm_enddate = (TextView) findViewById(R.id.tv_fostercareorderconfirm_enddate);
		tv_fostercareorderconfirm_roompre = (TextView) findViewById(R.id.tv_fostercareorderconfirm_roompre);
		textview_choose_food = (TextView) findViewById(R.id.textview_choose_food);
		
		//寄养摄像头start
		layout_foster_shexiangtou = (LinearLayout) findViewById(R.id.layout_foster_shexiangtou);
		foster_shexiangtou = (TextView) findViewById(R.id.foster_shexiangtou);
		foster_add_shexiangtou = (TextView) findViewById(R.id.foster_add_shexiangtou);
		foster_choose_or_not_shexiangtou = (ImageView) findViewById(R.id.foster_choose_or_not_shexiangtou);
		foster_shexiangtou_price = (TextView) findViewById(R.id.foster_shexiangtou_price);
		//寄养摄像头end
	}

	private void setView() {
		// TODO Auto-generated method stub
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		uuid = UUID.randomUUID();
		
		tvTitle.setText("订单确认");
		pre = getIntent().getIntExtra("pre", 0);
		if (pre==0) {
			layout_foster_shexiangtou.setVisibility(View.VISIBLE);
//			sheXiangTouPrice = 30;
			fc = (Fostercare) getIntent().getSerializableExtra("fostercare");
			petid = fc.petid;
			shopid = fc.shopid;
			transferId = fc.transferId;
			if(fc.customerpetid>0)
				tvPetName.setText(fc.customerpetname);
			else
				tvPetName.setText(fc.petname);
			tvShopName.setText(fc.shopname);
			tvPetAddr.setText(fc.addr);
			StringBuilder sb5 = new StringBuilder();
			sb5.append(fc.startdate);
//			sb5.append(" 12:00");
			tvStartDate.setText(sb5.toString());
			StringBuilder sb = new StringBuilder();
			sb.append(fc.enddate);
//			sb.append(" 12:00");
			sb.append("  共");
			sb.append(fc.daynum);
			sb.append("天");
			tvEndDate.setText(sb.toString());
			StringBuilder sb1 = new StringBuilder();
			sb1.append(fc.roomname);
			sb1.append(" ¥");
			sb1.append(Utils.formatDouble(fc.roomprice, 2));
			sb1.append("/天");
			tvRoomName.setText(sb1.toString());
			setLastPrice(true);
			cname = spUtil.getString("username", "");
			cphone = spUtil.getString("cellphone", "");
			StringBuilder sb4 = new StringBuilder();
			if(!"".equals(cname)){
				sb4.append(cname);
				sb4.append("  ");
			}
			sb4.append(cphone);
			tvContact.setText(sb4.toString());
			
			lat = fc.lat;
			lng = fc.lng;
			StringBuilder sb2 = new StringBuilder();
			sb2.append(fc.startdate);
			sb2.append(" 00:00:00");
			StringBuilder sb3 = new StringBuilder();
			sb3.append(fc.enddate);
			sb3.append(" 00:00:00");
			getData(shopid, sb2.toString(), sb3.toString(), lat, lng);
			getNotice(sb2.toString(), sb3.toString());
		}else if (pre==Global.TRAIN_TO_ORDER_CONFIRM) {
			layout_foster_shexiangtou.setVisibility(View.GONE);
			type = 4;
			ivTextExp.setVisibility(View.GONE);
			tv_fostercareappointment_agreement.setText("《宠物家训练协议》");
			confirmtip = getIntent().getStringArrayExtra("confirmtip");
			trainServiceEvery = (TrainServiceEvery) getIntent().getSerializableExtra("TrainServiceEvery");
			transfer = (TrainTransfer) getIntent().getSerializableExtra("trainTransfer");
			time = getIntent().getStringExtra("time");
			time = time+" 00:00:00";
			shopId = getIntent().getIntExtra("shopId", 0);
			petid = getIntent().getIntExtra("petid", 0);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < confirmtip.length; i++) {
				if (i==confirmtip.length-1) {
					builder.append(confirmtip[i]);
				}else {
					builder.append(confirmtip[i]+"\n");
				}
			}
			tvTextExp.setText(builder.toString());
			tv_fostercareorderconfirm_startdatepre.setText("入训时间    ");
			tvStartDate.setText(time.replace("00", "").replace(":", ""));
			tv_fostercareorderconfirm_enddatepre.setText("训练套餐    ");
			tv_fostercareorderconfirm_enddate.setText(trainServiceEvery.name);
			customerpetname = getIntent().getStringExtra("customerpetname");
			customerpetid = getIntent().getIntExtra("customerpetid", 0);
			petname = getIntent().getStringExtra("petname");
			agreementPage = getIntent().getStringExtra("agreementPage");
//			if (TextUtils.isEmpty(customerpetname)) {
				tvPetName.setText(petname);
//			}else {
//				tvPetName.setText(customerpetname);
//			}
			try {
				Utils.mLogError("==-->transfer 0 "+transfer.name);
				if (transfer!=null) {
					tvShopName.setText(transfer.name);
					Utils.mLogError("==-->transfer 1 "+transfer.name);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv_fostercareorderconfirm_roompre.setText("训练内容    ");
			tvRoomName.setText(trainServiceEvery.info);
			textview_choose_food.setVisibility(View.GONE);
			textview_pet_foodtag.setVisibility(View.GONE);
			textview_no_cutmoney_totalmoney.setText("¥"+trainServiceEvery.price);
			
			cname = spUtil.getString("username", "");
			cphone = spUtil.getString("cellphone", "");
			if(!"".equals(cname)){
				tvContact.setText(cname+"  "+cphone);
			}else{
				tvContact.setText(cphone);
			}
			rlBath.setVisibility(View.GONE);
			totalMoney = trainServiceEvery.price;
			needMoney = totalMoney;
			setLastPrice(true);
			getTrainCoupon();
		}
		cbAgree.setChecked(false);
		
		
		
		
		ibBack.setOnClickListener(this);
		rlContact.setOnClickListener(this);
		rlNote.setOnClickListener(this);
		rlBringFood.setOnClickListener(this);
		llPickUp.setOnClickListener(this);
		rlCoupon.setOnClickListener(this);
		btPay.setOnClickListener(this);
		rlTextExp.setOnClickListener(this);
		rlBath.setOnClickListener(this);
		ivPickupHint.setOnClickListener(this);
		tv_fostercareappointment_agreement.setOnClickListener(this);
		rl_fostercareorderconfirm_bath_two.setOnClickListener(this);
		rl_fostercareorderconfirm_bath_add_pet.setOnClickListener(this);
		layout_foster_shexiangtou.setOnClickListener(this);
	}
	
	private void getTrainCoupon(){
		CommUtil.getAvailableCoupon(this,
				spUtil.getString("cellphone", ""), 
				Global.getIMEI(this), 
				Global.getCurrentVersion(this), 
				time, 
				4, 
				1,
				0, 
				0, 
				cname, 
				cphone, 
				transfer.id, 
				transfer.address, 
				transfer.lat, 
				transfer.lng, 
				petid+"_"+trainServiceEvery.id+"_"+customerpetid, 
				transfer.id, 
				Utils.formatDouble(needMoney), 
				shopId, 
				null, 
				couponHanler);
	}
//	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
//		
//		@Override
//		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//			// TODO Auto-generated method stub
//			Utils.mLogError("==-->训练优惠券："+new String(responseBody));
//		}
//		
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				byte[] responseBody, Throwable error) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
	private void setLastPrice(boolean isMoney) {
		String text =""; 
		if(isMoney){
			text = "实付款: ¥ "+ Utils.formatDouble(needMoney,2);
		}else{
			text = "实付款: ¥ 0.00";
		}
		SpannableString ss = new SpannableString(text); 
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tabback)), 0, 4,Spanned.SPAN_INCLUSIVE_INCLUSIVE);  
		ss.setSpan(new TextAppearanceSpan(this, R.style.style1), 6, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tvTotalFee.setText(ss);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回
			finishWithAnimation();
			break;
		case R.id.rl_fostercareorderconfirm_textexp:
			//展开收起介绍
			if(isHintShrink){
				setHintExt();
			}
			break;
		case R.id.iv_fostercareorderconfirm_pickup_hint:
			//接送提示
			goNext(PickupHintActivity.class);
			break;
		case R.id.rl_fostercareorderconfirm_contact:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Order_ModifyContacts);
			//更换联系人
			goNextForData(ContactActivity.class, Global.ORDERDETAILREQUESTCODE_CONTACT);
			break;
		case R.id.rl_fostercareorderconfirm_note:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Order_AddRemarks);
			//填写备注
			goNextForData(NoteActivity.class, Global.ORDERDETAILREQUESTCODE_NOTE);
			break;
		case R.id.rl_fostercareorderconfirm_bringfood:
			//选择宠粮
//			if(isNeedFood){
//				isNeedFood = false;
//				ivBringFood.setBackgroundResource(R.drawable.icon_pay_normal);
////				rlFoodFee.setVisibility(View.GONE);
//				totalMoney = totalMoney-foodFee;
//				needMoney = needMoney-foodFee;
//				
//			}else{
//				isNeedFood = true;
//				ivBringFood.setBackgroundResource(R.drawable.icon_pay_selected);
////				rlFoodFee.setVisibility(View.VISIBLE);
//				totalMoney = totalMoney+foodFee;
//				needMoney = totalMoney - couponAmount;
//				
//			}
//			if(needMoney<0)
//				setLastPrice(false);
//			else
//				setLastPrice(true);
			break;
		case R.id.rl_fostercareorderconfirm_bath:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Bath_SwitchPet);
			//选择洗澡
			if(isNeedBath){
				rl_fostercareorderconfirm_bath_two.setVisibility(View.GONE);
				isNeedBath = false;
				needbath = 0;
				ivBath.setBackgroundResource(R.drawable.icon_pay_normal);
				totalMoney = totalMoney-bathFee;
				needMoney = needMoney-bathFee;
				rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.GONE);
				if (isAddPet) {
					if (!isDelPet) {
						totalMoney = totalMoney-TwoBathFee;
						needMoney = needMoney-TwoBathFee;
						rl_fostercareorderconfirm_bath_two.setVisibility(View.GONE);
					}
				}
				if (couponEnable) {
					getCoupon();
				}else {
					tvCoupon.setTextColor(Color.parseColor("#666666"));
				}

			}else{
				if (bathPetSize > 1) {
					rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.VISIBLE);
				}else {
					rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.GONE);
				}
				isNeedBath = true;
				needbath = 1;
				ivBath.setBackgroundResource(R.drawable.icon_pay_selected);
				
				if (isAddPet) {
					if (!isDelPet) {
						totalMoney = totalMoney+TwoBathFee;
						needMoney = totalMoney-couponAmount;
						rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.GONE);
						rl_fostercareorderconfirm_bath_two.setVisibility(View.VISIBLE);
					}else {
						rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.VISIBLE);
						rl_fostercareorderconfirm_bath_two.setVisibility(View.GONE);
					}
				}
				//勾选第一条洗澡 总金额等于
				totalMoney = totalMoney+bathFee;
//				needMoney = totalMoney - couponAmount;
				needMoney = totalMoney;
				if (couponEnable) {
					getCoupon();
				}else {
					tvCoupon.setTextColor(Color.parseColor("#666666"));
				}

			}
			if(needMoney<0)
				setLastPrice(false);
			else
				setLastPrice(true);
			break;
		case R.id.ll_fostercareorderconfirm_pickup:
			//选择上门接送
			if(ispickup){
				pickup = 0;
				ispickup=false;
				totalMoney = totalMoney - pickupFee;
				needMoney = needMoney-pickupFee;
				ivPickUp.setBackgroundResource(R.drawable.icon_pay_normal);
				
			}else{
				pickup = 1;
				ispickup=true;
				totalMoney = totalMoney + pickupFee;
				needMoney = totalMoney - couponAmount;
				ivPickUp.setBackgroundResource(R.drawable.icon_pay_selected);
				
			}
			if(needMoney<0)
				setLastPrice(false);
			else
				setLastPrice(true);
			break;
		case R.id.layout_foster_shexiangtou:
			if (foster_choose_or_not_shexiangtou.getVisibility()==View.VISIBLE) {
				//选择摄像头
				if (isChooseShexiangtou) {
					isChooseShexiangtou = false;
					totalMoney = totalMoney - sheXiangTouPrice;
//					needMoney = totalMoney;
					needMoney = needMoney-sheXiangTouPrice;
					needCameraPrice = -1;
					foster_choose_or_not_shexiangtou.setBackgroundResource(R.drawable.icon_pay_normal);
				}else {
					isChooseShexiangtou = true;
					totalMoney = totalMoney + sheXiangTouPrice;
//					needMoney = totalMoney;
					needMoney = needMoney+sheXiangTouPrice;
					needCameraPrice = sheXiangTouPrice;
					foster_choose_or_not_shexiangtou.setBackgroundResource(R.drawable.icon_pay_selected);
				}
				if (couponEnable) {
					getCoupon();
				}else {
					tvCoupon.setTextColor(Color.parseColor("#666666"));
				}
				if(needMoney<0)
					setLastPrice(false);
				else
					setLastPrice(true);
			}
			break;
		case R.id.rl_fostercareorderconfirm_coupon:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Order_SelectCoupon);
			//选择优惠券
			if(couponList.size()>0){
				goNextForData(AvailableCouponActivity.class, Global.ORDERDETAILREQUESTCODE_COUPON);
			}else{
				ToastUtil.showToastShortCenter(FostercareOrderConfirmActivity.this, "无可用优惠券");
			}
			break;
		case R.id.bt_fostercareorderconfirm_pay:
			if (pre==Global.TRAIN_TO_ORDER_CONFIRM) {//宠物家训练协议
				if (!cbAgree.isChecked()) {
					ToastUtil.showToastShortCenter(this, "请同意《宠物家训练协议》");
					return;
				}
			}else {
				if (!cbAgree.isChecked()) {
					ToastUtil.showToastShortCenter(this, "请同意《宠物家寄养协议》");
					return;
				}
			}
			if (!Utils.checkLogin(mContext)) {
				goNext(LoginActivity.class);
				return;
			}
			if (pre==Global.TRAIN_TO_ORDER_CONFIRM||type==4) {//训练新增付款以及二次支付接口
				if (couponAmount>=totalMoney) {//这个界面直接优惠券支付了
					if (orderNo>0) {//二次支付
						TwoTrainPay();
					}else {//首次支付
						trainPay();
					}
				}else {//首次支付
					//去收银台支付
					Intent intent = new Intent(FostercareOrderConfirmActivity.this, OrderPayActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("previous", Global.TRAIN_TO_ORDER_CONFIRM);
					intent.putExtra("transfer", transfer);
					intent.putExtra("trainServiceEvery", trainServiceEvery);
					intent.putExtra("couponid", couponId);
					intent.putExtra("couponamount", couponAmount);
					intent.putExtra("cname", cname);
					intent.putExtra("cphone", cphone);
					intent.putExtra("totalfee", totalMoney);
					intent.putExtra("needfee", needMoney);
					intent.putExtra("time", time);
					intent.putExtra("remark", tvNote.getText().toString().trim());
					intent.putExtra("shopId", shopId);
					intent.putExtra("orderid", orderNo);
					intent.putExtra("strp", getStrp());
					intent.putExtra("petid", petid);
					intent.putExtra("customerpetid", customerpetid);
					intent.putExtra("uuid",uuid.toString());
					intent.putExtra("type",4);
					
					startActivityForResult(intent, Global.TRAIN_TO_ORDER_CONFIRM);
				}
			}else {//寄养付款不动
				//付款
				if(couponAmount>=totalMoney){
					if(orderNo>0){
//						if(isNeedFood)
//							changepay(foodFee);
//						else
//							changepay(0);
						if (isChooseShexiangtou) {
							changepay(sheXiangTouPrice);
						}else {
							changepay(-1);
						}
						
					}else{
//						if(isNeedFood)
//							newOrder(foodFee);
//						else
//							newOrder(0);
						if (isChooseShexiangtou) {
							newOrder(sheXiangTouPrice);
						}else {
							newOrder(-1);
						}
					}
					
				}else{
					//去收银台支付
					Intent intent = new Intent(FostercareOrderConfirmActivity.this, OrderPayActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("previous", Global.FOSTERCARE_TO_ORDERPAY);
					intent.putExtra("uuid", uuid.toString());
					intent.putExtra("fc", fc);
					intent.putExtra("couponid", couponId);
					intent.putExtra("couponamount", couponAmount);
					intent.putExtra("cname", cname);
					intent.putExtra("cphone", cphone);
					intent.putExtra("totalfee", totalMoney);
					intent.putExtra("needfee", needMoney);
					intent.putExtra("foodfee", foodFee);
					intent.putExtra("needCameraPrice", needCameraPrice);
					intent.putExtra("pickup", pickup);
					intent.putExtra("needbath", needbath);
					intent.putExtra("isneefood", isNeedFood);
					intent.putExtra("isChooseShexiangtou", isChooseShexiangtou);
					intent.putExtra("orderid", orderNo);
					intent.putExtra("orderFee", orderFee);
					intent.putExtra("bathPetIds", GetPetIds());
					startActivityForResult(intent, Global.FOSTERCARE_TO_ORDERPAY);
				}
			}
			
				
			break;
		case R.id.rl_fostercareorderconfirm_bath_add_pet:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_LeaveBath);
			Intent intent = new Intent(mContext, ChoosePetActivityNew.class);
			intent.putExtra("previous", Global.FOSTER_TO_CHOOSE_PET);
			startActivityForResult(intent, Global.FOSTER_TO_CHOOSE_PET);
			break;
		case R.id.rl_fostercareorderconfirm_bath_two:
			isDelPet = true;
			if (isDelPet) {
				totalMoney = totalMoney-TwoBathFee;
				needMoney = needMoney-TwoBathFee;
				rl_fostercareorderconfirm_bath_two.setVisibility(View.GONE);
			}
			if (isNeedBath) {
				rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.VISIBLE);
			}
			if(needMoney<0)
				setLastPrice(false);
			else
				setLastPrice(true);
			if (couponEnable) {
				getCoupon();
			}else {
				tvCoupon.setTextColor(Color.parseColor("#666666"));
			}
			
			break;
		case R.id.tv_fostercareappointment_agreement:
			// 协议
			goNext(ADActivity.class, 0);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Global.RESULT_OK){
			if(requestCode == Global.ORDERDETAILREQUESTCODE_CONTACT){
				cphone = data.getStringExtra("contactphone");
				cname = data.getStringExtra("contactname");
				if(!"".equals(cname)){
					tvContact.setText(cname+"  "+cphone);
				}else{
					tvContact.setText(cphone);
				}
			}else if(requestCode == Global.ORDERDETAILREQUESTCODE_COUPON){
				couponId = data.getIntExtra("couponid", 0);
				String couponcontent = data.getStringExtra("content");
				String camount = data.getStringExtra("amount") == null || "".equals(data.getStringExtra("amount"))?"0":data.getStringExtra("amount");
				couponAmount = Utils.formatDouble(Double.parseDouble(camount), 2);
				tvCoupon.setText(couponcontent);
				needMoney = totalMoney - couponAmount;
				if(needMoney<0)
					needMoney = 0;
				setLastPrice(true);
				
			}else if(requestCode == Global.ORDERDETAILREQUESTCODE_NOTE){
				String note = data.getStringExtra("note");
				tvNote.setText(note);
			}else if(requestCode == Global.FOSTERCARE_TO_ORDERPAY||requestCode==Global.TRAIN_TO_ORDER_CONFIRM){
				orderNo = data.getIntExtra("orderid", 0);
			}else if (requestCode==Global.FOSTER_TO_CHOOSE_PET) {
				PetIdNew = data.getIntExtra("petid", 0);
				PetNameNew = data.getStringExtra("petname");
				PetNameNickName = data.getStringExtra("customerpetname");
				StringBuilder sb2 = new StringBuilder();
				sb2.append(fc.startdate);
				sb2.append(" 00:00:00");
				StringBuilder sb3 = new StringBuilder();
				sb3.append(fc.enddate);
				sb3.append(" 00:00:00");
				getTwoData(shopid, sb2.toString(), sb3.toString(), lat, lng);
				if (PetNameNickName==null) {
					tv_fostercareorderconfirm_bath_two.setText("离店洗澡("+PetNameNew+")");
				}else {
					tv_fostercareorderconfirm_bath_two.setText("离店洗澡("+PetNameNickName+")");
				}
				rl_fostercareorderconfirm_bath_add_pet.setVisibility(View.GONE);
				rl_fostercareorderconfirm_bath_two.setVisibility(View.VISIBLE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void goNextForData(Class clazz, int requestcode){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.FOSTERCAREORDER_TO_COUPON);
		intent.putExtra("remarks", remarks);
		intent.putExtra("cname", cname);
		intent.putExtra("cphone", cphone);
		intent.putExtra("remark", tvNote.getText().toString().trim());
		intent.putExtra("couponId", couponId);
		startActivityForResult(intent, requestcode);
	}
	private void goNext(Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra("previous", Global.FOSTERCAREORDER_TO_COUPON);
		startActivity(intent);
		this.overridePendingTransition(R.anim.center_in, R.anim.center_out);
	}
	private void setHintExt(){
		String str = tvTextExp.getText().toString().trim();
		if(isShrink){
			isShrink = false;
			tvTextExp.setEllipsize(null);
			tvTextExp.setSingleLine(false);
			ivTextExp.setBackgroundResource(R.drawable.icon_arrow_up);
		}else{
			isShrink = true;
			ivTextExp.setBackgroundResource(R.drawable.icon_arrow_down);
			tvTextExp.setEllipsize(TruncateAt.END);
			tvTextExp.setLines(2);
		}
	}
	private void setHint(){
		tvTextExp.post(new Runnable() {
			@Override
			public void run() {
				if(tvTextExp.getLineCount() > 2){
					ivTextExp.setVisibility(View.VISIBLE);
					tvTextExp.setEllipsize(TruncateAt.END);
					tvTextExp.setLines(2);
					isHintShrink = true;
				}else{
					isHintShrink = false;
					ivTextExp.setVisibility(View.GONE);
				}
				
			}
			
		});
	}
	private void getNotice(String startdate,String enddate){
		CommUtil.confirmOrderPrompt(spUtil.getString("cellphone", ""), act, 2,0,null,-1,-1,startdate,enddate,confirmOrderPrompt);
	}
	/**
	 * 获取数据
	 */
	private void getData(int shopid,String startdate,String enddate,double lat,double lng){
		CommUtil.prepareConfirmOrder(this,fc.roomid, spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this),petid,shopid,startdate,enddate,lat,lng,dataHanler);
	}
	/**
	 * 获取数据
	 */
	private void getTwoData(int shopid,String startdate,String enddate,double lat,double lng){
		CommUtil.prepareConfirmOrder(this,fc.roomid, spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this),PetIdNew,shopid,startdate,enddate,lat,lng,twoPet);
	}

	/**
	 * 获取可用的优惠券
	 */
	private void getCoupon(){
		couponList.clear();
		couponId = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(fc.startdate);
		sb.append(" 00:00:00");
		StringBuilder sb1 = new StringBuilder();
		sb1.append(petid);
		sb1.append("_");
		sb1.append(fc.roomid);
		sb1.append("_");
		sb1.append("0");
		CommUtil.getAvailableCoupon(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this), sb.toString(), 2,  1,
				0,pickup,cname,cphone,fc.addrid,fc.addr, lat, lng,sb1.toString(),fc.transferId,Utils.formatDouble(needMoney, 2),
				fc.shopid,
				fc.enddate,
				couponHanler);
	}
	
	/**
	 * 下单
	 */
	private void newOrder(double foodfee){
		pDialog.showDialog();
		StringBuilder sb = new StringBuilder();
		sb.append(fc.startdate);
		sb.append(" 00:00:00");
		StringBuilder sb1 = new StringBuilder();
		sb1.append(fc.enddate);
		sb1.append(" 00:00:00");
		if(needMoney<0)
			needMoney = 0;
		CommUtil.newOrderFostercare(this,null,spUtil.getString("cellphone", ""), Global.getIMEI(this), 
				Global.getCurrentVersion(this), fc.roomid, petid,fc.shopid,fc.customerpetid,
				couponId, fc.addrid,
				cname, cphone, fc.addr, lat, lng, sb.toString(), sb1.toString(), 
				Utils.formatDouble(totalMoney, 2), Utils.formatDouble(totalListFee, 2), 
				Utils.formatDouble(needMoney, 2), paytype, tvNote.getText().toString().trim(),
				pickup,needbath, Utils.formatDouble(needCameraPrice, 2),-1, uuid.toString(), 
				transferId,GetPetIds(),
				newOrderHandler);
		
	}
	
	private void changepay(double foodfee){
		pDialog.showDialog();
		if(needMoney<0)
			needMoney = 0;
		
		CommUtil.changeOrderPayMethodV2(null,spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderNo, 
				spUtil.getInt("userid", 0), cname,cphone,tvNote.getText().toString().trim(),
				pickup,needbath,foodfee,Utils.formatDouble(needMoney, 2),paytype,
				couponId,-1, GetPetIds(),false,null,changeHanler);
	}
	
	private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			btPay.setEnabled(true);
			Utils.mLogError("重新支付："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
						if (needMoney==0||paytype==3) {
							//直接跳转到支付成功
							goPayResult();
						}else {
//							if(jdata.has("tradeNo")&&!jdata.isNull("tradeNo"))
//								outorderno = jdata.getString("tradeNo");
						}
				}else{
					ToastUtil.showToastShort(FostercareOrderConfirmActivity.this, msg);
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
			pDialog.closeDialog();
			btPay.setEnabled(true);
		}
		
	};
	protected int orderFee;
	
	/**
	 * 离店洗澡第二条狗 
	 */
	private AsyncHttpResponseHandler twoPet = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->寄养第二条狗洗澡价格："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					isAddPet = true;
					isDelPet = false;
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("servicePrice")&&!jdata.isNull("servicePrice")){
						TwoNeedbath = 0;
						TwoBathFee = jdata.getDouble("servicePrice");
						tv_fostercareorderconfirm_totalbathfee_two.setText("¥"+Utils.formatDouble(TwoBathFee,2));		
						if(jdata.has("discountPrice")&&!jdata.isNull("discountPrice")){
							tv_fostercareorderconfirm_bathcouponprice_two.setText(jdata.getString("discountPrice"));
							tv_fostercareorderconfirm_bathcouponprice_two.setVisibility(View.VISIBLE);
						}else{
							tv_fostercareorderconfirm_bathcouponprice_two.setVisibility(View.GONE);
						}
						if(jdata.has("bathTxt")&&!jdata.isNull("bathTxt")){
							tv_fostercareorderconfirm_bathtag_two.setText(jdata.getString("bathTxt"));
							tv_fostercareorderconfirm_bathtag_two.setVisibility(View.VISIBLE);
						}else{
							tv_fostercareorderconfirm_bathtag_two.setVisibility(View.GONE);
						}
						rl_fostercareorderconfirm_bath_two.setVisibility(View.VISIBLE);
						
						totalMoney = totalMoney+TwoBathFee;
						needMoney = totalMoney;
//						needMoney = totalMoney - couponAmount;
						if(needMoney<0)
							setLastPrice(false);
						else
							setLastPrice(true);
						if (couponEnable) {
							getCoupon();
						}
					}else{
						needbath = -1;
						rl_fostercareorderconfirm_bath_two.setVisibility(View.GONE);
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
	private AsyncHttpResponseHandler dataHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("寄养："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					
					if (jdata.has("cameraPrice")&&!jdata.isNull("cameraPrice")) {
						sheXiangTouPrice = jdata.getDouble("cameraPrice");
						layout_foster_shexiangtou.setVisibility(View.VISIBLE);
						foster_shexiangtou_price.setText(Utils.getTextAndColorCommentsBeau("#dddddd","¥"+Utils.formatDouble(sheXiangTouPrice)+"X"+fc.daynum+"天","#D1494F","　¥"+sheXiangTouPrice*fc.daynum));
						sheXiangTouPrice = sheXiangTouPrice*fc.daynum;
					}else {
						sheXiangTouPrice = -1;
						layout_foster_shexiangtou.setVisibility(View.VISIBLE);
						foster_choose_or_not_shexiangtou.setVisibility(View.INVISIBLE);
						foster_shexiangtou_price.setText("该房型摄像头房间已约满");
					}
					if (jdata.has("cameraTxt")&&!jdata.isNull("cameraTxt")) {
						String cameraTxt = jdata.getString("cameraTxt");
						foster_shexiangtou.setText(cameraTxt);
					}
					if (jdata.has("agreementPage")&&!jdata.isNull("agreementPage")) {
						agreementPage = jdata.getString("agreementPage");
					}
					if(jdata.has("orderFee")&&!jdata.isNull("orderFee")){
						orderFee = jdata.getInt("orderFee");
					}
					if (jdata.has("bathPetSize")&&!jdata.isNull("bathPetSize")) {
						bathPetSize = jdata.getInt("bathPetSize");
					}
					if(jdata.has("servicePrice")&&!jdata.isNull("servicePrice")){
						tv_fostercareorderconfirm_bath.setText("离店洗澡("+fc.petname+")");
						needbath = 0;
						bathFee = jdata.getDouble("servicePrice");
						tvBathFee.setText("¥"+Utils.formatDouble(bathFee,2));		
						if(jdata.has("discountPrice")&&!jdata.isNull("discountPrice")){
							tvBathCouponFee.setText(jdata.getString("discountPrice"));
							tvBathCouponFee.setVisibility(View.VISIBLE);
						}else{
							tvBathCouponFee.setVisibility(View.GONE);
						}
						if(jdata.has("bathTxt")&&!jdata.isNull("bathTxt")){
							tvBathTag.setText(jdata.getString("bathTxt"));
							tvBathTag.setVisibility(View.VISIBLE);
						}else{
							tvBathTag.setVisibility(View.GONE);
						}
						rlBath.setVisibility(View.VISIBLE);
					}else{
						needbath = -1;
						rlBath.setVisibility(View.GONE);
					}

//					if(jdata.has("foodPricePerDay")&&!jdata.isNull("foodPricePerDay")){
//						tvFoodPrice.setText("¥"+Utils.formatDouble(jdata.getDouble("foodPricePerDay"), 2)+"/天");
//					}
					if(jdata.has("foodPrice")&&!jdata.isNull("foodPrice")){
						foodFee = jdata.getDouble("foodPrice");
						tvTotalFoodFee.setText("¥"+Utils.formatDouble(foodFee, 2));
					}
					if(jdata.has("remarkTag")&&!jdata.isNull("remarkTag")){
						JSONArray jRemarks = jdata.getJSONArray("remarkTag");
						int arrlength = jRemarks.length()>4?4:jRemarks.length();
						for(int i=0;i<arrlength;i++){
							remarks[i] = jRemarks.getString(i);
						}
					}
					StringBuilder sb = new StringBuilder();
					int startindex = 0;
					int endindex = 0;
					if(jdata.has("header1")&&!jdata.isNull("header1")){
						startindex = jdata.getString("header1").length();
						sb.append(jdata.getString("header1"));
					}
					if(jdata.has("header2")&&!jdata.isNull("header2")){
						endindex = jdata.getString("header2").length();
						sb.append(jdata.getString("header2"));
					}
					if(jdata.has("header3")&&!jdata.isNull("header3")){
						sb.append(jdata.getString("header3"));
					}
					if (jdata.has("basicPrice")&&!jdata.isNull("basicPrice")) {
						basicPrice = jdata.getInt("basicPrice");
					}
					if (jdata.has("listPrice")&&!jdata.isNull("listPrice")) {
						listPrice = jdata.getInt("listPrice");
					}
					if (jdata.has("discountBasic")&&!jdata.isNull("discountBasic")) {
						int discountBasic = jdata.getInt("discountBasic");
						if (discountBasic>0) {
							layout_no_cutmoney.setVisibility(View.GONE);
							layout_cutmoney.setVisibility(View.VISIBLE);
						}else {
							layout_no_cutmoney.setVisibility(View.VISIBLE);
							layout_cutmoney.setVisibility(View.GONE);
						}
//						textview_no_cutmoney_totalmoney.setText(text)
						roomFee = basicPrice;
						totalMoney = roomFee;
						needMoney = roomFee;
						totalListFee = listPrice;
//						tvTotalRoomFee.setText("¥"+Utils.formatDouble(roomFee, 2));
						textview_no_cutmoney_totalmoney.setText("¥"+basicPrice);
						textview_cutdown_money.setText("已节省"+discountBasic+"元");
						old_price.setText("原价"+listPrice);
						SpannableString str = getTextPrice("¥ "+Utils.formatDouble(roomFee, 2));
						tvTotalRoomFee.setText(str);
						
					}
					if (jdata.has("couponEnable")&&!jdata.isNull("couponEnable")) {
						couponEnable = jdata.getBoolean("couponEnable");
					}
					SpannableString spans = new SpannableString(sb.toString());
					spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 
							startindex, startindex+endindex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					
					tvTextExp.setText(spans);
					setHint();
					if(jdata.has("showPickUpOpt2")&&!jdata.isNull("showPickUpOpt2")){
						if("1".equals(jdata.getString("showPickUpOpt2"))){
							if(jdata.has("pickUpPrice")&&!jdata.isNull("pickUpPrice")){
								pickupFee = jdata.getDouble("pickUpPrice");
								if(pickupFee==0){
									tvPickupFee.setText("免费");
								}else{
									tvPickupFee.setText("¥"+Utils.formatDouble(pickupFee, 2));
								}
								llPickUp.setVisibility(View.VISIBLE);
								tvPickupHint.setVisibility(View.GONE);
								tvPickupFee.setVisibility(View.VISIBLE);
								ivPickUp.setVisibility(View.VISIBLE);
								llPickUp.setEnabled(true);
							}
						}else if("2".equals(jdata.getString("showPickUpOpt2"))){
							llPickUp.setVisibility(View.VISIBLE);
							tvPickupHint.setVisibility(View.VISIBLE);
							tvPickupFee.setVisibility(View.GONE);
							ivPickUp.setVisibility(View.GONE);
							tvPickupHint.setText("接送名额已满");
							llPickUp.setEnabled(false);
							ivPickupHint.setVisibility(View.GONE);
						}else{
							llPickUp.setVisibility(View.VISIBLE);
							tvPickupHint.setVisibility(View.VISIBLE);
							tvPickupFee.setVisibility(View.GONE);
							ivPickUp.setVisibility(View.GONE);
							tvPickupHint.setText("不在接送范围");
							llPickUp.setEnabled(false);
							ivPickupHint.setVisibility(View.GONE);
						}
					}else{
//						llPickUp.setVisibility(View.GONE);
						llPickUp.setVisibility(View.VISIBLE);
						tvPickupHint.setVisibility(View.VISIBLE);
						tvPickupFee.setVisibility(View.GONE);
						ivPickUp.setVisibility(View.GONE);
						tvPickupHint.setText("不在接送范围");
						llPickUp.setEnabled(false);
						ivPickupHint.setVisibility(View.GONE);
					}
					if (jdata.has("petFoodTag")&&!jdata.isNull("petFoodTag")) {
						textview_pet_foodtag.setText(jdata.getString("petFoodTag"));
					}
					setLastPrice(true);
					if (couponEnable) {
						getCoupon();
					}else {
						tvCoupon.setTextColor(Color.parseColor("#666666"));
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
	
	
	private AsyncHttpResponseHandler couponHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("可用的优惠券："+new String(responseBody));
			couponList.clear();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONArray jData = jobj.getJSONArray("data");
					for(int i = 0; i < jData.length(); i++){
						JSONObject jcoupon = jData.getJSONObject(i);
						couponList.add(Coupon.jsonToEntity(jcoupon));
					}
				}
				if(couponList.size() > 0){
					couponId = couponList.get(0).id;
					tvCoupon.setText("-"+couponList.get(0).amount+"("+couponList.get(0).name+")");
					String camount = couponList.get(0).amount == null || "".equals(couponList.get(0).amount)?"0":couponList.get(0).amount;
					couponAmount = Utils.formatDouble(Double.parseDouble(camount), 2);
					if(couponAmount >= totalMoney){
						needMoney = 0;
						paytype = 3;
					}else{
						needMoney = totalMoney - couponAmount;
					}
					setLastPrice(true);
				}else {
					tvCoupon.setTextColor(Color.parseColor("#666666"));
					conpon_rightarrow.setVisibility(View.INVISIBLE);
					needMoney = totalMoney - couponAmount;
					setLastPrice(true);
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
	
	private AsyncHttpResponseHandler newOrderHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			btPay.setEnabled(true);
			Utils.mLogError("生成新订单："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					spUtil.saveInt("payway", paytype);
					JSONObject jData = jobj.getJSONObject("data");
					if(jData.has("orderId")&&!jData.isNull("orderId")){
						orderNo = Integer.parseInt(jData.getString("orderId"));
					}
//					if(jData.has("tradeNo")&&!jData.isNull("tradeNo")){
//						outorderno = jData.getString("tradeNo");
//					}
					
					if(needMoney == 0||(paytype==4&&balance>=needMoney)){
						//直接跳转到支付成功
						goPayResult();
					}else{
						Utils.mLogError("用第三方支付"+needMoney);
					}
				}else if(109 == resultCode){
					//重复提交，不做处理
				}else{
					if(jobj.has("msg")&&!jobj.isNull("msg"))
						ToastUtil.showToastShort(FostercareOrderConfirmActivity.this, jobj.getString("msg"));
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
			btPay.setEnabled(true);
		}
		
	};
	
	
	private void sendBroadcastToMainUpdataUserinfo(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY);
		sendBroadcast(intent);
	}
	
	private void goPayResult(){
		ToastUtil.showToastShortCenter(this, "订单支付成功");
		sendBroadcastToMainUpdataUserinfo();
		if(FostercareChooseroomActivity.act!=null)
			FostercareChooseroomActivity.act.finishWithAnimation();
		if(FostercareShopListActivity.act!=null)
			FostercareShopListActivity.act.finishWithAnimation();
		if(FostercareChooseroomActivity.act!=null)
			FostercareChooseroomActivity.act.finishWithAnimation();
		if (type==4) {
			goPaySuccess();
		}else {
			ArrayList<MulPetService> mpstmpList = new ArrayList<MulPetService>();
			MulPetService mps = new MulPetService();
			mps.petId = fc.petid;
			mps.petName = fc.petname;
			mps.petKind=fc.petkind;
			mps.serviceName=fc.roomname;
			mps.serviceType=100;
			mps.serviceId=fc.roomid;
			mps.orderid=orderNo;
			mps.petCustomerId = fc.customerpetid;
			mpstmpList.add(mps);
			goFostercarePaySuccess(mpstmpList);
		}
		
	}
	private void goPaySuccess() {
		Intent intent = new Intent(act, PaySuccessActivity.class);
		intent.putExtra("orderId",orderNo);
		intent.putExtra("type",type);
		startActivity(intent);
		finishWithAnimation();
	}
	private void goFostercarePaySuccess(ArrayList<MulPetService> mpstmpList){
		Intent intent = new Intent(this, FostercarePaysuccessActivity.class);
		intent.putParcelableArrayListExtra("mulpetservice", mpstmpList);
		startActivity(intent);
		finishWithAnimation();
	}

	private TextView tvPickupFee;
	private RelativeLayout rlBath;
	private TextView tvBathTag;
	private TextView tvBathFee;
	private TextView tvBathCouponFee;
	private ImageView ivBath;
	private TextView tvPickupHint;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TextPaint paint = old_price.getPaint();
		paint.setAntiAlias(true);
		paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
		
		tv_fostercareappointment_agreement.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线;
		tv_fostercareappointment_agreement.getPaint().setAntiAlias(true);//抗锯齿
	}
	
	private SpannableString getTextPrice(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this,R.style.foster_style_y), 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,R.style.foster_style_show), 1,lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private String GetPetIds(){
		StringBuilder builder = new StringBuilder();
		if (isNeedBath) {
			if (isAddPet&&!isDelPet) {
				builder.append(fc.petid+","+PetIdNew);
			}else {
				builder.append(fc.petid);
			}
		}else {
			builder.append("");
		}
		return builder.toString();
		
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
//						String data = object.getString("data");
//						if (data.contains(";")) {
//							String [] everyData = data.split(";");
//							for (int i = 0; i < everyData.length; i++) {
//								TextView textView = new TextView(mContext);
//								textView.setText(everyData[i]);
//								textView.setTextColor(getResources().getColor(R.color.aBB996C));
//								layout_notice.addView(textView);
//							}
//						}else {
//							TextView textView = new TextView(mContext);
//							textView.setText(data);
//							textView.setTextColor(getResources().getColor(R.color.aBB996C));
//							layout_notice.addView(textView);
//						}
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("prompt")&&!objectData.isNull("prompt")) {
							String prompt = objectData.getString("prompt");
							TextView textView = new TextView(mContext);
							textView.setTextColor(getResources().getColor(R.color.aBB996C));
							textView.setText(prompt);
							layout_notice.addView(textView);
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
	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", agreementPage);
		intent.putExtra("previous", pre);
		startActivity(intent);
	}
	private void trainPay(){
		CommUtil.trainPay(
		mContext,
		spUtil.getString("cellphone", ""), 
		time,
		transfer.id,
		couponId,cphone,cname,
		-1,tvNote.getText().toString().trim(), 
		transfer.lat,transfer.lng,Utils.formatDouble(needMoney, 2),paytype,shopId,
		getStrp(),
		uuid.toString(), 
		4,
		null,
		newOrderHandler);
	}
//	private AsyncHttpResponseHandler TrainHandler = new AsyncHttpResponseHandler() {
//		
//		@Override
//		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//			// TODO Auto-generated method stub
//			Utils.mLogError("==-->训练首次次支付：= "+new String(responseBody));
//		}
//		
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				byte[] responseBody, Throwable error) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
	private void TwoTrainPay(){
		CommUtil.TwoTrainPay(mContext, 
				spUtil.getString("cellphone", ""), 
				couponId, 
				spUtil.getInt("userid", 0), 
				cphone, 
				cname, 
				-1, 
				orderNo, 
				Utils.formatDouble(needMoney, 2), 
				paytype, 
				null,
				changeHanler);
	}
//	private AsyncHttpResponseHandler TwoTrainPay = new AsyncHttpResponseHandler() {
//		
//		@Override
//		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//			// TODO Auto-generated method stub
//			Utils.mLogError("==-->训练二次支付：= "+new String(responseBody));
//		}
//		
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				byte[] responseBody, Throwable error) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
	private String getStrp(){
		StringBuilder builder = new StringBuilder();
		builder.append(petid+"_");
		builder.append(trainServiceEvery.id+"_");
		builder.append(customerpetid+"");
		return builder.toString();
		
	}
}
