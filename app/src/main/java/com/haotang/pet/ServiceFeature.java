package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.adapter.MPagerAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.ServicesFeature;
import com.haotang.pet.entity.Share;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ServiceFeature extends SuperActivity implements OnClickListener {
	public static ServiceFeature act;
	private RollPagerView rpvBanner;
	private BannerBathLoopAdapter adapterBanner;
	private LinearLayout point;
	private int oldPostion;
	private PullPushLayout pplLayout;
	private int alphaMax = 180;
	private Drawable bgBackDrawable;
	private Drawable bgBackBelowDrawable;
	private Drawable bgShareDrawable;
	private Drawable bgShareBelowDrawable;
	private Drawable bgNavBarDrawable;

	private ImageView service_back;
	private LinearLayout layout_service_back;
	private ImageView service_share;
	private RelativeLayout rlTitle;
	private ImageView service_back_blow;
	private ImageView service_share_below;

	private ArrayList<String> listBanner = new ArrayList<String>();
	private ArrayList<View> dots;
	private MPagerAdapter adapter;

	private TextView textView_service_name_show;
	private LinearLayout llChangepet;
	private TextView textView_service_detail_num_to_be_use;

	// item 1
	private SelectableRoundedImageView imageview_scaling_icon;
	private TextView textview_scaling;
	private TextView textview_scaling_detail;
	private TextView textview_scaling_price;
	private Button button_go_apponit_scaling;
	// item 2
	private SelectableRoundedImageView imageview_scaling_and_wash_icon;
	private TextView textview_scaling_and_wash;
	private TextView textview_scaling_and_wash_detail;
	private TextView textview_scaling_and_wash_price;
	private Button button_go_apponit_scaling_and_wash;
	// item 3
	private SelectableRoundedImageView imageview_scaling_and_beau_icon;
	private TextView textview_scaling_and_beau;
	private TextView textview_scaling_and_beau_detail;
	private TextView textview_scaling_and_beau_price;
	private Button button_go_apponit_scaling_and_beau;

	private View footer;
	private LinearLayout service_layout_eva_one;
	private LinearLayout service_layout_eva_two;
	private ImageView service_eva_is_member_one;
	private ImageView service_eva_is_member_two;
	private LinearLayout item_eva_layout;
	private ImageView service_eva_one;
	private ImageView service_eva_two;
	private TextView textView_service_eva_one;
	private TextView textView_service_eva_two;
	private MListview service_scaling_image;

	private LinearLayout re_layout_scaling_one;
	private LinearLayout re_layout_scaling_two;
	private LinearLayout re_layout_scaling_thr;

	private JoinWorkAdapter joinWorkAdapter;
	private ArrayList<String> Imagelist = new ArrayList<String>();
	private SharedPreferenceUtil spUtil;
	private Intent fIntent;
	private int previous;
	private int customerpetid;
	private String customerpetname;
	private String serviceName;
	private String serviceCurrentName;
	private int petid;
	private int petkind;
	private int beautician_sort;
	private String petname;
	private int currentItem;
	private ScheduledExecutorService scheduledExecutor;
	private StringBuffer sbServiceIds;

	private TextView tv_servicedetail_goodcomments;
	private TextView tv_servicedetail_badcomments;
	private MProgressDialog pDialog;

	private ArrayList<ServicesFeature> featureList = new ArrayList<ServicesFeature>();
	private TextView textview_service_feature_show;

	//特色one 
	//外侧
	private LinearLayout one_service_item;
	private TextView textview_price_one;
	private TextView textview_up_one;
	private ImageView imageview_one;
	private TextView old_one_list_price;
	
	private LinearLayout one_service;
	// one item one
	private LinearLayout first_item_service_one;
	private TextView first_one_service_name;
	private TextView first_one_service_detail;
	private TextView first_one_service_price;
	private TextView first_one_old_price;
	private TextView first_no_old_price_one;
	private Button first_one_button_apponit;
	//one item two
	private LinearLayout first_item_service_two;
	private TextView first_two_service_name;
	private TextView first_two_service_detail;
	private TextView first_two_service_price;
	private TextView first_two_old_price;
	private TextView first_no_old_price_two;
	private Button first_two_button_apponit;
	//one item thr
	private LinearLayout first_item_service_thr;
	private TextView first_thr_service_name;
	private TextView first_thr_service_detail;
	private TextView first_thr_service_price;
	private TextView first_thr_old_price;
	private TextView first_no_old_price_thr;
	private Button first_thr_button_apponit;
	
	//特色two
	//外侧
	private LinearLayout two_service_item;
	private TextView textview_price_two;
	private TextView textview_up_two;
	private ImageView imageview_two;
	private TextView old_two_list_price;

	
	private LinearLayout two_service;
	//two item one
	private LinearLayout Second_item_service_one;
	private TextView Second_one_service_name;
	private TextView Second_one_service_detail;
	private TextView Second_one_service_price;
	private TextView Second_one_old_price;
	private TextView Second_no_old_price_one;
	private Button Second_one_button_apponit;
	//two item two
	private LinearLayout Second_item_service_two;
	private TextView Second_two_service_name;
	private TextView Second_two_service_detail;
	private TextView Second_two_service_price;
	private TextView Second_two_old_price;
	private TextView Second_no_old_price_two;
	private Button Second_two_button_apponit;
	//two item thr
	private LinearLayout Second_item_service_thr;
	private TextView Second_thr_service_name;
	private TextView Second_thr_service_detail;
	private TextView Second_thr_service_price;
	private TextView Second_thr_old_price;
	private TextView Second_no_old_price_thr;
	private Button Second_thr_button_apponit;
	
	//特色thr
	//外侧
	private LinearLayout thr_service_item;
	private TextView textview_price_thr;
	private TextView textview_up_thr;
	private ImageView imageview_thr;
	private TextView old_thr_list_price;
	
	private LinearLayout thr_service;
	//thr item one
	private LinearLayout Thr_item_service_one;
	private TextView Thr_one_service_name;
	private TextView Thr_one_service_detail;
	private TextView Thr_one_service_price;
	private TextView Thr_one_old_price;
	private TextView Thr_no_old_price_one;
	private Button Thr_one_button_apponit;
	//thr item two
	private LinearLayout Thr_item_service_two;
	private TextView Thr_two_service_name;
	private TextView Thr_two_service_detail;
	private TextView Thr_two_service_price;
	private TextView Thr_two_old_price;
	private TextView Thr_no_old_price_two;
	private Button Thr_two_button_apponit;
	//thr item thr
	private LinearLayout Thr_item_service_thr;
	private TextView Thr_thr_service_name;
	private TextView Thr_thr_service_detail;
	private TextView Thr_thr_service_price;
	private TextView Thr_thr_old_price;
	private TextView Thr_no_old_price_thr;
	private Button Thr_thr_button_apponit;
	private boolean isopenOne = false;
	private boolean isopenTwo = false;
	private boolean isopenThr = false;
	
	private Button button_textview_fea_one;
	private Button button_textview_fea_two;
	private Button button_textview_fea_thr;
	
	private RelativeLayout relayout_service_item_one;
	private RelativeLayout layout_fea_one;
	private RelativeLayout relayout_service_item_two;
	private RelativeLayout layout_fea_two;
	private RelativeLayout relayout_service_item_thr;
	private RelativeLayout layout_fea_thr;
	private int clicksort=0;
	
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private List<Share> shareList;
	
	private TextView textview_fea_one_price;
	private TextView textview_fea_two_price;
	private TextView textview_fea_thr_price;

	private TextView no_old_price_one_item;
	private TextView no_old_price_two_item;
	private TextView no_old_price_thr_item;
	
	
	private View service_feature_item;
	private RelativeLayout rl_ppllayout_top;
	private boolean isBeauticianGoShop = true;
	private boolean isBeauticianGoHome = true;
	private String avatarPath="";
	private TextView textview_give_other_money;
	
	private TextView tvChangepet;
	private LinearLayout layout_top_next_eva;
	private TextView textView_title;
	private TextView textview_good_show;
	private TextView textview_good;
	public static ImageView imageView_isplay;
	private TextView textview_notice;
	private int typeId=1;
	private int areaid=0;
	private int shopid;
	private String BuyCardH5Url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_featrue);
		MApplication.listAppoint.add(this);
		act = this;
		shareList = new ArrayList<Share>();
		pDialog = new MProgressDialog(this);
		findView();
		setView();
		
		configPlatforms();
		CommUtil.getShare(this,System.currentTimeMillis(), getShare);
		initListener();
	}

	private void initListener() {
		//等级
//		first_one_button_apponit.setOnClickListener(this);
//		first_two_button_apponit.setOnClickListener(this);
//		first_thr_button_apponit.setOnClickListener(this);
		textview_give_other_money.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext, MyLastMoney.class);
//				startActivity(intent);
				
				goAd(mContext, BuyCardH5Url, ADActivity.class);
			}
		});
		first_item_service_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(0);
			}
		});
		first_one_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(0);
			}
		});
		first_item_service_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(0);
			}
		});
		first_two_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(0);
			}
		});
		first_item_service_thr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(0);
			}
		});
		first_thr_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(0);
			}
		});
//		Second_one_button_apponit.setOnClickListener(this);
//		Second_two_button_apponit.setOnClickListener(this);
//		Second_thr_button_apponit.setOnClickListener(this);
		Second_item_service_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(1);
			}
		});
		Second_one_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(1);
			}
		});
		Second_item_service_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(1);
			}
		});
		Second_two_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(1);
			}
		});
		Second_item_service_thr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(1);
			}
		});
		Second_thr_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(1);
			}
		});
//		Thr_one_button_apponit.setOnClickListener(this);
//		Thr_two_button_apponit.setOnClickListener(this);
//		Thr_thr_button_apponit.setOnClickListener(this);
		Thr_item_service_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(2);
			}
		});
		Thr_one_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=10;
				goAppoinement(2);
			}
		});
		Thr_item_service_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(2);
			}
		});
		Thr_two_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=20;
				goAppoinement(2);
			}
		});
		Thr_item_service_thr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(2);
			}
		});
		Thr_thr_button_apponit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort=30;
				goAppoinement(2);
			}
		});
	}

	private void setView() {
		textView_title.setText("特色服务");
		spUtil.saveInt("charactservice", 1);
		previous = fIntent.getIntExtra("previous", -1);
		typeId = fIntent.getIntExtra("typeId", -1);
		areaid = fIntent.getIntExtra("areaid", 0);
		shopid = fIntent.getIntExtra("shopid", 0);
		serviceName = fIntent.getStringExtra("servicename");
		beautician_sort = fIntent.getIntExtra("beautician_sort", -1);
		isBeauticianGoShop = fIntent.getBooleanExtra("isshop", true);
		isBeauticianGoHome = fIntent.getBooleanExtra("ishome", true);
		if (fIntent.getIntExtra("beautician_sort", 0)>0) {
			if (isBeauticianGoShop) {
				textview_service_feature_show.setText("该美容师仅支持到店服务哦~");
			}else {
				textview_service_feature_show.setText("该美容师暂不支持到店服务哦~");
			}
		}else {
			textview_service_feature_show.setText("该宠物仅支持到店服务哦~");
		}
		
		if (previous == Global.SERVICEFEATURE_TO_PETLIST|| previous == Global.BEAUTICIAN_TO_APPOINTMENT||previous==Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST) {
			customerpetid = fIntent.getIntExtra("customerpetid", 0);
			customerpetname = fIntent.getStringExtra("customerpetname");

			petid = fIntent.getIntExtra("petid", 0);
			petkind = fIntent.getIntExtra("petkind", 0);
			petname = fIntent.getStringExtra("petname");
			avatarPath = fIntent.getStringExtra("petimage");
		} else {
			petid = spUtil.getInt("petid", 0);
			petkind = spUtil.getInt("petkind", 0);
			customerpetid = spUtil.getInt("customerpetid", 0);
			customerpetname = spUtil.getString("customerpetname", "");
			petname = spUtil.getString("petname", "");
			avatarPath = CommUtil.getServiceNobacklash()+spUtil.getString("petimage", "");
			String myPetImage = spUtil.getString("mypetImage", "");	
			if (myPetImage.equals("")) {
				avatarPath=CommUtil.getServiceNobacklash()+spUtil.getString("petimage", "");
			}else {
				avatarPath=myPetImage;
			}
		}

//		pplLayout.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {
//
//			@Override
//			public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSlide(int alpha) {
//				// TODO Auto-generated method stub
//				int alphaReverse = alphaMax - alpha;
//				if (alphaReverse < 0) {
//					alphaReverse = 0;
//				}
//				bgBackDrawable.setAlpha(alphaReverse);
//				bgBackBelowDrawable.setAlpha(alpha);
//				bgShareDrawable.setAlpha(alphaReverse);
//				bgShareBelowDrawable.setAlpha(alpha);
//				bgNavBarDrawable.setAlpha(alpha);
//
//			}
//		});

//		bgBackDrawable = service_back.getBackground();
//		bgBackBelowDrawable = service_back_blow.getBackground();
//		bgShareDrawable = service_share.getBackground();
//		bgShareBelowDrawable = service_share_below.getBackground();
//		bgNavBarDrawable = rlTitle.getBackground();
//		bgBackDrawable.setAlpha(alphaMax);
//		bgShareDrawable.setAlpha(alphaMax);
//		bgNavBarDrawable.setAlpha(0);
//		bgBackBelowDrawable.setAlpha(0);
//		bgShareBelowDrawable.setAlpha(0);


		adapterBanner = new BannerBathLoopAdapter(rpvBanner, listBanner);
		rpvBanner.setAdapter(adapterBanner);
		
		llChangepet.setOnClickListener(this);

		service_share.setOnClickListener(this);
		service_back.setOnClickListener(this);
		service_layout_eva_one.setOnClickListener(this);
		service_layout_eva_two.setOnClickListener(this);
		item_eva_layout.setOnClickListener(this);
//		button_go_apponit_scaling.setOnClickListener(this);
//		button_go_apponit_scaling_and_wash.setOnClickListener(this);
//		button_go_apponit_scaling_and_beau.setOnClickListener(this);
		re_layout_scaling_one.setOnClickListener(this);
		re_layout_scaling_two.setOnClickListener(this);
		re_layout_scaling_thr.setOnClickListener(this);
		layout_service_back.setOnClickListener(this);
		
		button_textview_fea_one.setOnClickListener(this);
		button_textview_fea_two.setOnClickListener(this);
		button_textview_fea_thr.setOnClickListener(this);
		
		layout_top_next_eva.setOnClickListener(this);
		

		getData(petid);
	}

	private void getData(int petid) {
		pDialog.showDialog();
		if (rpvBanner != null) {
			rpvBanner.pause();
		}
		if (Utils.isLog) {
			if (typeId==-1) {//这个测试用，正式需要注掉
				typeId = 1;
			}
		}
		typeId = Integer.parseInt(serviceName);
		if (previous==Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST) {
			CommUtil.getFeaturedService1(spUtil.getString("cellphone", ""),
					Global.getIMEI(this), this, petid,typeId,
					beautician_sort, fIntent.getIntExtra("beautician_id", 0),areaid,getFeaturedService1);
		}else {
			CommUtil.getFeaturedService1(spUtil.getString("cellphone", ""),
					Global.getIMEI(this), this, petid,typeId,
					beautician_sort, fIntent.getIntExtra("beautician_id", 0),spUtil.getInt("tareaid", 0),getFeaturedService1);
		}
	}

	private AsyncHttpResponseHandler getFeaturedService1 = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				Utils.mLogError("特色服务：" + new String(responseBody));
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("activityCopy")&&!objectData.isNull("activityCopy")) {
//							String activityCopy = objectData.getString("activityCopy");
//							textview_give_other_money.setText(activityCopy);
							JSONObject objectActivityCopy = objectData.getJSONObject("activityCopy");
							if (objectActivityCopy.has("txt")&&!objectActivityCopy.isNull("txt")) {
								textview_give_other_money.setText(objectActivityCopy.getString("txt"));
							}
							if (objectActivityCopy.has("url")&&!objectActivityCopy.isNull("url")) {
								BuyCardH5Url = objectActivityCopy.getString("url");
							}
						}else {
							textview_give_other_money.setVisibility(View.GONE);
						}
						if (objectData.has("nearbyDogs") && !objectData.isNull("nearbyDogs")) {
							textView_service_detail_num_to_be_use.setText(objectData.getInt("nearbyDogs")+"");
						}
						if (objectData.has("petServiceType")&&!objectData.isNull("petServiceType")) {
							JSONObject objectType = objectData.getJSONObject("petServiceType");
							if (objectType.has("name")&&!objectType.isNull("name")) {
								serviceCurrentName = objectType.getString("name");
							}
						}
						if (objectData.has("evaluateObj")&&!objectData.isNull("evaluateObj")) {
							JSONObject objectEva = objectData.getJSONObject("evaluateObj");
							if (objectEva.has("score")&&!objectEva.isNull("score")) {
								textview_good.setText(getTextScore(objectEva.getString("score")+"分"));
							}
							if (objectEva.has("percent")&&!objectEva.isNull("percent")) {
								textview_good_show.setText("好评率"+objectEva.getString("percent")+">");
							}
						}
						if (objectData.has("reminders")&&!objectData.isNull("reminders")) {
							JSONArray arrayReminders = objectData.getJSONArray("reminders");
							if (arrayReminders.length()>0) {
								StringBuilder builder = new StringBuilder();
								for (int i = 0; i < arrayReminders.length(); i++) {
									builder.append(arrayReminders.getString(i)+"\n");
								}
								textview_notice.setText(builder.toString());
							}
						}
						if (objectData.has("allServices")&& !objectData.isNull("allServices")) {
							JSONArray array = objectData.getJSONArray("allServices");
							if (array.length() != 0) {
								sbServiceIds = new StringBuffer();
								for (int i = 0; i < array.length(); i++) {
									sbServiceIds.append("_");
									sbServiceIds.append(array.getInt(i));
								}
							}
						}
						if (objectData.has("pet") && !objectData.isNull("pet")) {
							JSONObject objectPet = objectData.getJSONObject("pet");
							Pet pet = Pet.json2Entity(objectPet);
							petid = pet.id;
							if (!"".equals(pet.name)) {
//								if(serviceCurrentName != null && serviceCurrentName.length() >=2){
//									if(serviceCurrentName.substring(serviceCurrentName.length()-2, serviceCurrentName.length()).contains("套餐")){
//										serviceCurrentName = serviceCurrentName.substring(0, serviceCurrentName.length()-2);
//									}
//								}
								if (customerpetid > 0) {
									textView_service_name_show.setText(customerpetname + "专属"+ serviceCurrentName);
									petname = customerpetname;
								} else {
									if (objectPet.has("petName")&& !objectPet.isNull("petName")) {
										textView_service_name_show.setText(pet.name + serviceCurrentName);
										petname = pet.name;
									}
								}
							}
//							if (!TextUtils.isEmpty(pet.image)) {
//								avatarPath = pet.image;
//							}
						}
						
						if (objectData.has("services")&& !objectData.isNull("services")) {
							// sbServiceIds = new StringBuffer();
							featureList.clear();
							JSONArray array = objectData.getJSONArray("services");
							for (int i = 0; i < array.length(); i++) {
								featureList.add(ServicesFeature.json2Entity(array.getJSONObject(i)));
							}
						}else {
							featureList.clear();
						}
						setClickShowNum();
						
						if (objectData.has("picIntro")&& !objectData.isNull("picIntro")) {
							Imagelist.clear();
							JSONArray arrayPic = objectData.getJSONArray("picIntro");
							if (arrayPic.length() != 0) {
								for (int i = 0; i < arrayPic.length(); i++) {
									Imagelist.add(CommUtil.getServiceNobacklash()+ arrayPic.getString(i));
								}
							}
							joinWorkAdapter.notifyDataSetChanged();
						}
						if (objectData.has("comments")&& !objectData.isNull("comments")) {
							service_layout_eva_one.setVisibility(View.GONE);
							service_layout_eva_two.setVisibility(View.GONE);
//							setComments(objectData);
						} else {
							service_layout_eva_one.setVisibility(View.GONE);
							service_layout_eva_two.setVisibility(View.GONE);
						}
						if (objectData.has("positiveComments")&& !objectData.isNull("positiveComments")) {
							if (objectData.getInt("positiveComments")>999) {
								tv_servicedetail_goodcomments.setText(Utils.getTextAndColorComments("好评：","999+条"));
							}else {
								tv_servicedetail_goodcomments.setText(Utils.getTextAndColorComments("好评：",objectData.getInt("positiveComments")+ "条"));
							}
						}
						if (objectData.has("negativeComments")&& !objectData.isNull("negativeComments")) {
							tv_servicedetail_badcomments.setText(Utils.getTextAndColorComments("差评：",objectData.getInt("negativeComments")+ "条"));
						}
						if (objectData.has("levelDesc")&&!objectData.isNull("levelDesc")) {
							JSONObject objectLevelDesc = objectData.getJSONObject("levelDesc");
							if (objectLevelDesc.has("10")&&!objectLevelDesc.isNull("10")) {
								JSONObject object10 = objectLevelDesc.getJSONObject("10");
								if (object10.has("title")&&!object10.isNull("title")) {
									first_one_service_name.setText(object10.getString("title"));
									Second_one_service_name.setText(object10.getString("title"));
									Thr_one_service_name.setText(object10.getString("title"));
								}
								if (object10.has("desc")&&!object10.isNull("desc")) {
									first_one_service_detail.setText(object10.getString("desc"));
									Second_one_service_detail.setText(object10.getString("desc"));
									Thr_one_service_detail.setText(object10.getString("desc"));
								}
							}
							if (objectLevelDesc.has("20")&&!objectLevelDesc.isNull("20")) {
								JSONObject object20 = objectLevelDesc.getJSONObject("20");
								if (object20.has("title")&&!object20.isNull("title")) {
									first_two_service_name.setText(object20.getString("title"));
									Second_two_service_name.setText(object20.getString("title"));
									Thr_two_service_name.setText(object20.getString("title"));
								}
								if (object20.has("desc")&&!object20.isNull("desc")) {
									first_two_service_detail.setText(object20.getString("desc"));
									Second_two_service_detail.setText(object20.getString("desc"));
									Thr_two_service_detail.setText(object20.getString("desc"));
									
								}
							}
							if (objectLevelDesc.has("30")&&!objectLevelDesc.isNull("30")) {
								JSONObject object30 = objectLevelDesc.getJSONObject("30");
								if (object30.has("title")&&!object30.isNull("title")) {
									first_thr_service_name.setText(object30.getString("title"));
									Second_thr_service_name.setText(object30.getString("title"));
									Thr_thr_service_name.setText(object30.getString("title"));
								}
								if (object30.has("desc")&&!object30.isNull("desc")) {
									first_thr_service_detail.setText(object30.getString("desc"));
									Second_thr_service_detail.setText(object30.getString("desc"));
									Thr_thr_service_detail.setText(object30.getString("desc"));
								}
							}
						}
						if (objectData.has("banners")&& !objectData.isNull("banners")&& objectData.getJSONArray("banners").length() > 0) {
							listBanner.clear();
							JSONArray bannersArray = objectData.getJSONArray("banners");
							for (int i = 0; i < bannersArray.length(); i++) {
								if (bannersArray.getString(i).startsWith("video")) {
									listBanner.add(bannersArray.getString(i));
								}else {
									listBanner.add(CommUtil.getServiceNobacklash()+ bannersArray.getString(i));
								}
							}
							if (listBanner.size() > 1) {
								rpvBanner.setHintView(new ColorPointHintView(act, Color.parseColor("#FE8A3F"), Color.parseColor("#FFE2D0")));
							}
							adapterBanner.notifyDataSetChanged();
						}
						if (fIntent.getIntExtra("beautician_id", 0)>0) {
							relayout_service_item_one.setVisibility(View.GONE);
							relayout_service_item_two.setVisibility(View.GONE);
							relayout_service_item_thr.setVisibility(View.GONE);
							
							layout_fea_one.setVisibility(View.VISIBLE);
							layout_fea_two.setVisibility(View.VISIBLE);
							layout_fea_thr.setVisibility(View.VISIBLE);
						}
					}
				}
				pDialog.closeDialog();
			} catch (JSONException e) {
				e.printStackTrace();
				pDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();

		}

	};

	private void setClickShowNum() {
		ServicesFeature featureOne = null;
		ServicesFeature featureTwo = null;
		ServicesFeature featureThr = null;
		switch (featureList.size()) {
		case 0:
//			textview_service_feature_show.setVisibility(View.GONE);
			textview_service_feature_show.setText("该宠物暂不支持此项服务哦~");
			re_layout_scaling_one.setVisibility(View.GONE);
			re_layout_scaling_two.setVisibility(View.GONE);
			re_layout_scaling_thr.setVisibility(View.GONE);
			one_service_item.setVisibility(View.GONE);
			two_service_item.setVisibility(View.GONE);
			thr_service_item.setVisibility(View.GONE);
			break;
		case 1:
			re_layout_scaling_one.setVisibility(View.VISIBLE);
			re_layout_scaling_two.setVisibility(View.GONE);
			re_layout_scaling_thr.setVisibility(View.GONE);
			
			
//			one_service_item.setVisibility(View.VISIBLE);
//			two_service_item.setVisibility(View.GONE);
//			thr_service_item.setVisibility(View.GONE);
			setOneShow();
			break;
		case 2:
			re_layout_scaling_one.setVisibility(View.VISIBLE);
			re_layout_scaling_two.setVisibility(View.VISIBLE);
			re_layout_scaling_thr.setVisibility(View.GONE);
			
//			one_service_item.setVisibility(View.VISIBLE);
//			two_service_item.setVisibility(View.VISIBLE);
//			thr_service_item.setVisibility(View.GONE);
			setOneShow();
			setTwoShow();
			break;
		case 3:
			re_layout_scaling_one.setVisibility(View.VISIBLE);
			re_layout_scaling_two.setVisibility(View.VISIBLE);
			re_layout_scaling_thr.setVisibility(View.VISIBLE);
			
//			one_service_item.setVisibility(View.VISIBLE);
//			two_service_item.setVisibility(View.VISIBLE);
//			thr_service_item.setVisibility(View.VISIBLE);
			setOneShow();
			setTwoShow();
			setThrShow();
			break;
		}
	}

	private void setThrShow() {
		ServicesFeature featureThr;
		featureThr = featureList.get(2);
		int defaultImgId = R.drawable.icon_special_spa;
		if (featureThr.name != null && featureThr.name.contains("洗澡"))
			defaultImgId = R.drawable.icon_special_spabath;
		else if (featureThr.name != null && featureThr.name.contains("美容"))
			defaultImgId = R.drawable.icon_special_spabeauty;
		try {
			ImageLoaderUtil.loadImg( featureThr.spic,imageview_scaling_and_beau_icon,defaultImgId,null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		textview_scaling_and_beau.setText(featureThr.name/*
														 * Utils.getTextAndColor(
														 * featureThr.name,
														 * featureThr.minPrice)
														 */);
		textview_scaling_and_beau_detail.setText(featureThr.description);// 这个改描述

		try {
			String lastThrPrice = "¥" + featureThr.minPrice;
			SpannableString styledText = getTextPrice(lastThrPrice);
			textview_scaling_and_beau_price.setText(styledText,TextView.BufferType.SPANNABLE);
			textview_price_thr.setText(featureThr.minPrice+"");
			textview_fea_thr_price.setText(getTextNewPrice("¥"+featureThr.minPrice));
			no_old_price_thr_item.setText(getTextNewPrice("¥"+featureThr.minPrice));
			
			Thr_one_old_price.setText(featureThr.ServicePrice.shopListPrice);
			Thr_two_old_price.setText(featureThr.ServicePrice.shopListPrice20);
			Thr_thr_old_price.setText(featureThr.ServicePrice.shopListPrice30);
			
			if (beautician_sort==10) {
				old_thr_list_price.setText(featureThr.ServicePrice.shopListPrice);
				Utils.mLogError("==-->featureThr.ServicePrice.shopListPrice "+featureThr.ServicePrice.shopListPrice);
				if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice)) {
					old_thr_list_price.setVisibility(View.GONE);
					textview_fea_thr_price.setVisibility(View.GONE);
					no_old_price_thr_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_thr.setText(featureThr.btnTxtShop10);
			}else if (beautician_sort==20) {
				old_thr_list_price.setText(featureThr.ServicePrice.shopListPrice20);
				if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice20)) {
					old_thr_list_price.setVisibility(View.GONE);
					textview_fea_thr_price.setVisibility(View.GONE);
					no_old_price_thr_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_thr.setText(featureThr.btnTxtShop20);
			}else if (beautician_sort==30) {
				old_thr_list_price.setText(featureThr.ServicePrice.shopListPrice30);
				if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice30)) {
					old_thr_list_price.setVisibility(View.GONE);
					textview_fea_thr_price.setVisibility(View.GONE);
					no_old_price_thr_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_thr.setText(featureThr.btnTxtShop30);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (featureThr.ServicePrice.shopPrice10!=-1) {
			Thr_one_service_price.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice10));
			Thr_no_old_price_one.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice10));
			if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice)) {
				Thr_one_service_price.setVisibility(View.GONE);
				Thr_one_old_price.setVisibility(View.GONE);
				Thr_no_old_price_one.setVisibility(View.VISIBLE);
			}
		}else {
			Thr_item_service_one.setVisibility(View.GONE);
		}
		if (featureThr.ServicePrice.shopPrice20!=-1) {
			Thr_two_service_price.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice20));
			
			Thr_no_old_price_two.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice20));
			if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice20)) {
				Thr_two_service_price.setVisibility(View.GONE);
				Thr_two_old_price.setVisibility(View.GONE);
				Thr_no_old_price_two.setVisibility(View.VISIBLE);
			}
		}else {
			Thr_item_service_two.setVisibility(View.GONE);
		}
		if (featureThr.ServicePrice.shopPrice30!=-1) {
			Thr_thr_service_price.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice30));
			Thr_no_old_price_thr.setText(getTextNewPrice("¥"+featureThr.ServicePrice.shopPrice30));
			if (TextUtils.isEmpty(featureThr.ServicePrice.shopListPrice30)) {
				Thr_thr_service_price.setVisibility(View.GONE);
				Thr_thr_old_price.setVisibility(View.GONE);
				Thr_no_old_price_thr.setVisibility(View.VISIBLE);
			}
		}else {
			Thr_item_service_thr.setVisibility(View.GONE);
		}
		
		Thr_one_service_name.setText(featureThr.title10);
		Thr_one_service_detail.setText(featureThr.desc10);
		Thr_two_service_name.setText(featureThr.title20);
		Thr_two_service_detail.setText(featureThr.desc20);
		Thr_thr_service_name.setText(featureThr.title30);
		Thr_thr_service_detail.setText(featureThr.desc30);
		
		Thr_one_button_apponit.setText(featureThr.btnTxtShop10);
		Thr_two_button_apponit.setText(featureThr.btnTxtShop20);
		Thr_thr_button_apponit.setText(featureThr.btnTxtShop30);

		
		Thr_item_service_one.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		Thr_item_service_two.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		Thr_item_service_thr.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
	}

	private void setTwoShow() {
		ServicesFeature featureTwo;
		featureTwo = featureList.get(1);
		textview_scaling_and_wash.setText(featureTwo.name/*
														 * Utils.getTextAndColor(
														 * featureTwo.name,
														 * featureTwo.minPrice)
														 */);
		textview_scaling_and_wash_detail.setText(featureTwo.description);// 这个改描述
		int defaultImgId = R.drawable.icon_special_spa;
		if (featureTwo.name != null && featureTwo.name.contains("洗澡"))
			defaultImgId = R.drawable.icon_special_spabath;
		else if (featureTwo.name != null && featureTwo.name.contains("美容"))
			defaultImgId = R.drawable.icon_special_spabeauty;
		try {
			ImageLoaderUtil.loadImg(featureTwo.spic,imageview_scaling_and_wash_icon, defaultImgId,null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			String lastTwoPrice = "¥" + featureTwo.minPrice;
			SpannableString styledText = getTextPrice(lastTwoPrice);
			textview_scaling_and_wash_price.setText(styledText,TextView.BufferType.SPANNABLE);
			textview_price_two.setText(featureTwo.minPrice+"");
			textview_fea_two_price.setText(getTextNewPrice("¥"+featureTwo.minPrice));
			no_old_price_two_item.setText(getTextNewPrice("¥"+featureTwo.minPrice));
			Second_one_old_price.setText(featureTwo.ServicePrice.shopListPrice);
			Second_two_old_price.setText(featureTwo.ServicePrice.shopListPrice20);
			Second_thr_old_price.setText(featureTwo.ServicePrice.shopListPrice30);
			
			if (beautician_sort==10) {
				old_two_list_price.setText(featureTwo.ServicePrice.shopListPrice);
				Utils.mLogError("==-->featureTwo.ServicePrice.shopListPrice "+featureTwo.ServicePrice.shopListPrice);
				if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice)) {
					old_two_list_price.setVisibility(View.GONE);
					textview_fea_two_price.setVisibility(View.GONE);
					no_old_price_two_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_two.setText(featureTwo.btnTxtShop10);
			}else if (beautician_sort==20) {
				old_two_list_price.setText(featureTwo.ServicePrice.shopListPrice20);
				if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice20)) {
					old_two_list_price.setVisibility(View.GONE);
					textview_fea_two_price.setVisibility(View.GONE);
					no_old_price_two_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_two.setText(featureTwo.btnTxtShop20);
			}else if (beautician_sort==30) {
				old_two_list_price.setText(featureTwo.ServicePrice.shopListPrice30);
				if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice30)) {
					old_two_list_price.setVisibility(View.GONE);
					textview_fea_two_price.setVisibility(View.GONE);
					no_old_price_two_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_two.setText(featureTwo.btnTxtShop30);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if (featureTwo.ServicePrice.shopPrice10!=-1) {
			Second_one_service_price.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice10));
			Second_no_old_price_one.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice10));
			if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice)) {
				Second_one_service_price.setVisibility(View.GONE);
				Second_one_old_price.setVisibility(View.GONE);
				Second_no_old_price_one.setVisibility(View.VISIBLE);
			}
		}else {
			Second_item_service_one.setVisibility(View.GONE);
		}
		if (featureTwo.ServicePrice.shopPrice20!=-1) {
			Second_two_service_price.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice20));
			Second_no_old_price_two.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice20));
			if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice20)) {
				Second_two_service_price.setVisibility(View.GONE);
				Second_two_old_price.setVisibility(View.GONE);
				Second_no_old_price_two.setVisibility(View.VISIBLE);
			}
		}else {
			Second_item_service_two.setVisibility(View.GONE);
		}
		if (featureTwo.ServicePrice.shopPrice30!=-1) {
			Second_thr_service_price.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice30));
			Second_no_old_price_thr.setText(getTextNewPrice("¥"+featureTwo.ServicePrice.shopPrice30));
			if (TextUtils.isEmpty(featureTwo.ServicePrice.shopListPrice30)) {
				Second_thr_service_price.setVisibility(View.GONE);
				Second_thr_old_price.setVisibility(View.GONE);
				Second_no_old_price_thr.setVisibility(View.VISIBLE);
			}
		}else {
			Second_item_service_thr.setVisibility(View.GONE);
		}
		
		Second_one_service_name.setText(featureTwo.title10);
		Second_one_service_detail.setText(featureTwo.desc10);
		Second_two_service_name.setText(featureTwo.title20);
		Second_two_service_detail.setText(featureTwo.desc20);
		Second_thr_service_name.setText(featureTwo.title30);
		Second_thr_service_detail.setText(featureTwo.desc30);
		
		Second_one_button_apponit.setText(featureTwo.btnTxtShop10);
		Second_two_button_apponit.setText(featureTwo.btnTxtShop20);
		Second_thr_button_apponit.setText(featureTwo.btnTxtShop30);
		
		
		Second_item_service_one.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		Second_item_service_two.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		Second_item_service_thr.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));


	}

	private void setOneShow() {
		ServicesFeature featureOne;
		featureOne = featureList.get(0);
		textview_scaling.setText(featureOne.name/*
												 * Utils.getTextAndColor(featureOne
												 * .name, featureOne.minPrice)
												 */);
		textview_scaling_detail.setText(featureOne.description);
		try {
			String lastOnePrice = "¥" + featureOne.minPrice;
			SpannableString styledText = getTextPrice(lastOnePrice);
			textview_scaling_price.setText(styledText,TextView.BufferType.SPANNABLE);
			textview_price_one.setText(featureOne.minPrice+"");
			textview_fea_one_price.setText(getTextNewPrice("¥"+featureOne.minPrice));
			no_old_price_one_item.setText(getTextNewPrice("¥"+featureOne.minPrice));
			
			first_one_old_price.setText(featureOne.ServicePrice.shopListPrice);
			first_two_old_price.setText(featureOne.ServicePrice.shopListPrice20);
			first_thr_old_price.setText(featureOne.ServicePrice.shopListPrice30);
			if (beautician_sort==10) {
				old_one_list_price.setText(featureOne.ServicePrice.shopListPrice);
				Utils.mLogError("==-->featureOne.ServicePrice.shopListPrice "+featureOne.ServicePrice.shopListPrice);
				if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice)) {
					old_one_list_price.setVisibility(View.GONE);
					textview_fea_one_price.setVisibility(View.GONE);
					no_old_price_one_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_one.setText(featureOne.btnTxtShop10);
			}else if (beautician_sort==20) {
				old_one_list_price.setText(featureOne.ServicePrice.shopListPrice20);
				if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice)) {
					old_one_list_price.setVisibility(View.GONE);
					textview_fea_one_price.setVisibility(View.GONE);
					no_old_price_one_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_one.setText(featureOne.btnTxtShop20);
			}else if (beautician_sort==30) {
				old_one_list_price.setText(featureOne.ServicePrice.shopListPrice30);
				if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice)) {
					old_one_list_price.setVisibility(View.GONE);
					textview_fea_one_price.setVisibility(View.GONE);
					no_old_price_one_item.setVisibility(View.VISIBLE);
				}
				button_textview_fea_one.setText(featureOne.btnTxtShop30);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int defaultImgId = R.drawable.icon_special_spa;
		if (featureOne.name != null && featureOne.name.contains("洗澡"))
			defaultImgId = R.drawable.icon_special_spabath;
		else if (featureOne.name != null && featureOne.name.contains("美容"))
			defaultImgId = R.drawable.icon_special_spabeauty;
		try {
			ImageLoaderUtil.loadImg( featureOne.spic,imageview_scaling_icon, defaultImgId,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (featureOne.ServicePrice.shopPrice10!=-1) {
			first_one_service_price.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice10));
			
			first_no_old_price_one.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice10));
			if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice)) {
				first_one_service_price.setVisibility(View.GONE);
				first_one_old_price.setVisibility(View.GONE);
				first_no_old_price_one.setVisibility(View.VISIBLE);
			}
		}else {
			first_item_service_one.setVisibility(View.GONE);
		}
		if (featureOne.ServicePrice.shopPrice20!=-1) {
			first_two_service_price.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice20));
			first_no_old_price_two.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice20));
			if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice20)) {
				first_two_service_price.setVisibility(View.GONE);
				first_two_old_price.setVisibility(View.GONE);
				first_no_old_price_two.setVisibility(View.VISIBLE);
			}
			
		}else {
			first_item_service_two.setVisibility(View.GONE);
		}
		if (featureOne.ServicePrice.shopPrice30!=-1) {
			first_thr_service_price.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice30));
			first_no_old_price_thr.setText(getTextNewPrice("¥"+featureOne.ServicePrice.shopPrice30));
			if (TextUtils.isEmpty(featureOne.ServicePrice.shopListPrice30)) {
				first_thr_service_price.setVisibility(View.GONE);
				first_thr_old_price.setVisibility(View.GONE);
				first_no_old_price_thr.setVisibility(View.VISIBLE);
			}
		}else {
			first_item_service_thr.setVisibility(View.GONE);
		}
		
		first_one_service_name.setText(featureOne.title10);
		first_one_service_detail.setText(featureOne.desc10);
		first_two_service_name.setText(featureOne.title20);
		first_two_service_detail.setText(featureOne.desc20);
		first_thr_service_name.setText(featureOne.title30);
		first_thr_service_detail.setText(featureOne.desc30);
		
		first_one_button_apponit.setText(featureOne.btnTxtShop10);
		first_two_button_apponit.setText(featureOne.btnTxtShop20);
		first_thr_button_apponit.setText(featureOne.btnTxtShop30);
		
		
		
//		textview_notice.setText(featureOne.noticeShow);
		
		first_item_service_one.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		first_item_service_two.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		first_item_service_thr.setBackgroundColor(getResources().getColor(R.color.f8f8ff8));
		
	}

	private SpannableString getTextPrice(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.style0), 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.style1), 1,lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private SpannableString getTextNewPrice(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.service_old_service_one), 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.service_old_service_two), 1,lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		fIntent = getIntent();
		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);

		service_back = (ImageView) findViewById(R.id.service_back);
		layout_service_back = (LinearLayout) findViewById(R.id.layout_service_back);
		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
		service_share = (ImageView) findViewById(R.id.service_share);
		service_share_below = (ImageView) findViewById(R.id.service_share_below);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);

		rpvBanner = (RollPagerView) findViewById(R.id.rpv_servicedetail_pet);
		point = (LinearLayout) findViewById(R.id.point);
		textView_service_name_show = (TextView) findViewById(R.id.textView_service_name_show);
		llChangepet = (LinearLayout) findViewById(R.id.ll_servicedetail_changepet);
		tvChangepet = (TextView) findViewById(R.id.tv_servicedetail_changepet);
		layout_top_next_eva = (LinearLayout) findViewById(R.id.layout_top_next_eva);
		textview_give_other_money = (TextView) findViewById(R.id.textview_give_other_money);
		textView_service_detail_num_to_be_use = (TextView) findViewById(R.id.textView_service_detail_num_to_be_use);
		service_scaling_image = (MListview) findViewById(R.id.service_scaling_image);

		re_layout_scaling_one = (LinearLayout) findViewById(R.id.re_layout_scaling);
		re_layout_scaling_two = (LinearLayout) findViewById(R.id.re_layout_scaling_and_wash);
		re_layout_scaling_thr = (LinearLayout) findViewById(R.id.re_layout_scaling_and_beau);

		footer = LayoutInflater.from(this).inflate(R.layout.item_eva_service_feature, null);
		service_layout_eva_one = (LinearLayout) footer.findViewById(R.id.service_layout_eva_one);
		service_layout_eva_two = (LinearLayout) footer.findViewById(R.id.service_layout_eva_two);
		service_eva_is_member_one = (ImageView) footer.findViewById(R.id.service_eva_is_member_one);
		service_eva_is_member_two = (ImageView) footer.findViewById(R.id.service_eva_is_member_two);
		item_eva_layout = (LinearLayout) footer.findViewById(R.id.item_eva_layout);

		service_eva_one = (ImageView) footer.findViewById(R.id.service_eva_one);
		service_eva_two = (ImageView) footer.findViewById(R.id.service_eva_two);

		textView_service_eva_one = (TextView) footer.findViewById(R.id.textView_service_eva_one);
		textView_service_eva_two = (TextView) footer.findViewById(R.id.textView_service_eva_two);

		tv_servicedetail_goodcomments = (TextView) footer.findViewById(R.id.tv_servicedetail_goodcomments);
		tv_servicedetail_badcomments = (TextView) footer.findViewById(R.id.tv_servicedetail_badcomments);

		imageview_scaling_icon = (SelectableRoundedImageView) findViewById(R.id.imageview_scaling_icon);
		textview_scaling = (TextView) findViewById(R.id.textview_scaling);
		textview_scaling_detail = (TextView) findViewById(R.id.textview_scaling_detail);
		textview_scaling_price = (TextView) findViewById(R.id.textview_scaling_price);
//		button_go_apponit_scaling = (Button) findViewById(R.id.button_go_apponit_scaling);

		imageview_scaling_and_wash_icon = (SelectableRoundedImageView) findViewById(R.id.imageview_scaling_and_wash_icon);
		textview_scaling_and_wash = (TextView) findViewById(R.id.textview_scaling_and_wash);
		textview_scaling_and_wash_price = (TextView) findViewById(R.id.textview_scaling_and_wash_price);
		textview_scaling_and_wash_detail = (TextView) findViewById(R.id.textview_scaling_and_wash_detail);
//		button_go_apponit_scaling_and_wash = (Button) findViewById(R.id.button_go_apponit_scaling_and_wash);

		imageview_scaling_and_beau_icon = (SelectableRoundedImageView) findViewById(R.id.imageview_scaling_and_beau_icon);
		textview_scaling_and_beau = (TextView) findViewById(R.id.textview_scaling_and_beau);
		textview_scaling_and_beau_price = (TextView) findViewById(R.id.textview_scaling_and_beau_price);
		textview_scaling_and_beau_detail = (TextView) findViewById(R.id.textview_scaling_and_beau_detail);
//		button_go_apponit_scaling_and_beau = (Button) findViewById(R.id.button_go_apponit_scaling_and_beau);
		textview_service_feature_show = (TextView) findViewById(R.id.textview_service_feature_show);

//		service_scaling_image.addFooterView(footer);
		joinWorkAdapter = new JoinWorkAdapter<String>(this, Imagelist);
		service_scaling_image.setAdapter(joinWorkAdapter);
		service_scaling_image.setFocusable(false);
		pplLayout.setFocusable(true);
		pplLayout.scrollTo(0, 0);
		
		//one外侧
		one_service_item = (LinearLayout) findViewById(R.id.one_service_item);
		textview_price_one = (TextView) findViewById(R.id.textview_price_one);
		textview_up_one = (TextView) findViewById(R.id.textview_up_one);
		imageview_one = (ImageView) findViewById(R.id.imageview_one);
		old_one_list_price = (TextView) findViewById(R.id.old_one_list_price);
		//内侧
		one_service = (LinearLayout) findViewById(R.id.one_service);
		//item one
		first_item_service_one = (LinearLayout) one_service.findViewById(R.id.item_service_one);
		first_one_service_name = (TextView) one_service.findViewById(R.id.textview_item_service_name_one);
		first_one_service_detail = (TextView) one_service.findViewById(R.id.textview_item_service_detail_one);
		first_one_service_price = (TextView) one_service.findViewById(R.id.textview_item_service_price_one);
		first_one_old_price = (TextView) one_service.findViewById(R.id.old_one_price);
		first_no_old_price_one = (TextView) one_service.findViewById(R.id.no_old_price_one);
		first_one_button_apponit=(Button) one_service.findViewById(R.id.button_go_apponit_one);
		//item two
		first_item_service_two = (LinearLayout) one_service.findViewById(R.id.item_service_two);
		first_two_service_name = (TextView) one_service.findViewById(R.id.textview_item_service_name_two);
		first_two_service_detail = (TextView) one_service.findViewById(R.id.textview_item_service_detail_two);
		first_two_service_price = (TextView) one_service.findViewById(R.id.textview_item_service_price_two);
		first_two_old_price = (TextView) one_service.findViewById(R.id.old_two_price);
		first_no_old_price_two = (TextView) one_service.findViewById(R.id.no_old_price_two);
		first_two_button_apponit=(Button) one_service.findViewById(R.id.button_go_apponit_two);
		//item thr
		first_item_service_thr = (LinearLayout) one_service.findViewById(R.id.item_service_thr);
		first_thr_service_name = (TextView) one_service.findViewById(R.id.textview_item_service_name_thr);
		first_thr_service_detail = (TextView) one_service.findViewById(R.id.textview_item_service_detail_thr);
		first_thr_service_price = (TextView) one_service.findViewById(R.id.textview_item_service_price_thr);
		first_thr_old_price = (TextView) one_service.findViewById(R.id.old_thr_price);
		first_no_old_price_thr = (TextView) one_service.findViewById(R.id.no_old_price_thr);
		first_thr_button_apponit=(Button) one_service.findViewById(R.id.button_go_apponit_thr);

		
		
		//two外侧
		two_service_item = (LinearLayout) findViewById(R.id.two_service_item);
		textview_price_two = (TextView) findViewById(R.id.textview_price_two);
		textview_up_two = (TextView) findViewById(R.id.textview_up_two);
		imageview_two = (ImageView) findViewById(R.id.imageview_two);
		old_two_list_price = (TextView) findViewById(R.id.old_two_list_price);
		//内侧
		two_service = (LinearLayout) findViewById(R.id.two_service);
		//item one
		Second_item_service_one = (LinearLayout) two_service.findViewById(R.id.item_service_one);
		Second_one_service_name = (TextView) two_service.findViewById(R.id.textview_item_service_name_one);
		Second_one_service_detail = (TextView) two_service.findViewById(R.id.textview_item_service_detail_one);
		Second_one_service_price = (TextView) two_service.findViewById(R.id.textview_item_service_price_one);
		Second_one_old_price = (TextView) two_service.findViewById(R.id.old_one_price);
		Second_no_old_price_one = (TextView) two_service.findViewById(R.id.no_old_price_one);
		Second_one_button_apponit=(Button) two_service.findViewById(R.id.button_go_apponit_one);
		//item two
		Second_item_service_two = (LinearLayout) two_service.findViewById(R.id.item_service_two);
		Second_two_service_name = (TextView) two_service.findViewById(R.id.textview_item_service_name_two);
		Second_two_service_detail = (TextView) two_service.findViewById(R.id.textview_item_service_detail_two);
		Second_two_service_price = (TextView) two_service.findViewById(R.id.textview_item_service_price_two);
		Second_two_old_price = (TextView) two_service.findViewById(R.id.old_two_price);
		Second_no_old_price_two = (TextView) two_service.findViewById(R.id.no_old_price_two);
		Second_two_button_apponit=(Button) two_service.findViewById(R.id.button_go_apponit_two);
		//item thr
		Second_item_service_thr = (LinearLayout) two_service.findViewById(R.id.item_service_thr);
		Second_thr_service_name = (TextView) two_service.findViewById(R.id.textview_item_service_name_thr);
		Second_thr_service_detail = (TextView) two_service.findViewById(R.id.textview_item_service_detail_thr);
		Second_thr_service_price = (TextView) two_service.findViewById(R.id.textview_item_service_price_thr);
		Second_thr_old_price = (TextView) two_service.findViewById(R.id.old_thr_price);
		Second_no_old_price_thr = (TextView) two_service.findViewById(R.id.no_old_price_thr);
		Second_thr_button_apponit=(Button) two_service.findViewById(R.id.button_go_apponit_thr);
		
		//thr外侧
		thr_service_item = (LinearLayout) findViewById(R.id.thr_service_item);
		textview_price_thr = (TextView) findViewById(R.id.textview_price_thr);
		textview_up_thr = (TextView) findViewById(R.id.textview_up_thr);
		imageview_thr = (ImageView) findViewById(R.id.imageview_thr);
		old_thr_list_price = (TextView) findViewById(R.id.old_thr_list_price);
		//内侧
		thr_service = (LinearLayout) findViewById(R.id.thr_service);
		//item one
		Thr_item_service_one = (LinearLayout) thr_service.findViewById(R.id.item_service_one);
		Thr_one_service_name = (TextView) thr_service.findViewById(R.id.textview_item_service_name_one);
		Thr_one_service_detail = (TextView) thr_service.findViewById(R.id.textview_item_service_detail_one);
		Thr_one_service_price = (TextView) thr_service.findViewById(R.id.textview_item_service_price_one);
		Thr_one_old_price = (TextView) thr_service.findViewById(R.id.old_one_price);
		Thr_no_old_price_one = (TextView) thr_service.findViewById(R.id.no_old_price_one);
		Thr_one_button_apponit=(Button) thr_service.findViewById(R.id.button_go_apponit_one);
		//item two
		Thr_item_service_two = (LinearLayout) thr_service.findViewById(R.id.item_service_two);
		Thr_two_service_name = (TextView) thr_service.findViewById(R.id.textview_item_service_name_two);
		Thr_two_service_detail = (TextView) thr_service.findViewById(R.id.textview_item_service_detail_two);
		Thr_two_service_price = (TextView) thr_service.findViewById(R.id.textview_item_service_price_two);
		Thr_two_old_price = (TextView) thr_service.findViewById(R.id.old_two_price);
		Thr_no_old_price_two = (TextView) thr_service.findViewById(R.id.no_old_price_two);
		Thr_two_button_apponit=(Button) thr_service.findViewById(R.id.button_go_apponit_two);
		//item thr
		Thr_item_service_thr = (LinearLayout) thr_service.findViewById(R.id.item_service_thr);
		Thr_thr_service_name = (TextView) thr_service.findViewById(R.id.textview_item_service_name_thr);
		Thr_thr_service_detail = (TextView) thr_service.findViewById(R.id.textview_item_service_detail_thr);
		Thr_thr_service_price = (TextView) thr_service.findViewById(R.id.textview_item_service_price_thr);
		Thr_thr_old_price = (TextView) thr_service.findViewById(R.id.old_thr_price);
		Thr_no_old_price_thr = (TextView) thr_service.findViewById(R.id.no_old_price_thr);
		Thr_thr_button_apponit=(Button) thr_service.findViewById(R.id.button_go_apponit_thr);

		
		
		button_textview_fea_one  = (Button) findViewById(R.id.button_textview_fea_one);
		button_textview_fea_two  = (Button) findViewById(R.id.button_textview_fea_two);
		button_textview_fea_thr  = (Button) findViewById(R.id.button_textview_fea_thr);
		
		relayout_service_item_one = (RelativeLayout) findViewById(R.id.relayout_service_item_one);
		relayout_service_item_two = (RelativeLayout) findViewById(R.id.relayout_service_item_two);
		relayout_service_item_thr = (RelativeLayout) findViewById(R.id.relayout_service_item_thr);
		layout_fea_one = (RelativeLayout) findViewById(R.id.layout_fea_one);
		layout_fea_two = (RelativeLayout) findViewById(R.id.layout_fea_two);
		layout_fea_thr = (RelativeLayout) findViewById(R.id.layout_fea_thr);
		
		textview_fea_one_price = (TextView) findViewById(R.id.textview_fea_one_price);
		textview_fea_two_price = (TextView) findViewById(R.id.textview_fea_two_price);
		textview_fea_thr_price = (TextView) findViewById(R.id.textview_fea_thr_price);
		
		no_old_price_one_item = (TextView) findViewById(R.id.no_old_price_one_item);
		no_old_price_two_item = (TextView) findViewById(R.id.no_old_price_two_item);
		no_old_price_thr_item = (TextView) findViewById(R.id.no_old_price_thr_item);
		
		service_feature_item = findViewById(R.id.service_feature_item);
		
		textView_title = (TextView) findViewById(R.id.textView_title);
		textview_good_show  = (TextView) findViewById(R.id.textview_good_show);
		textview_good  = (TextView) findViewById(R.id.textview_good);
		imageView_isplay  = (ImageView) findViewById(R.id.imageView_isplay);
		textview_notice  = (TextView) findViewById(R.id.textview_notice);
		
		rl_ppllayout_top = (RelativeLayout) findViewById(R.id.rl_ppllayout_top);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (rpvBanner != null){
			rpvBanner.resume();
		}
		MobclickAgent.onPageStart("ServiceFeature"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);// 统计时长
		
		int mWidth = Utils.getDisplayMetrics(this)[0];
		LayoutParams layoutParams = rl_ppllayout_top.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = mWidth * 1 / 2;
		rl_ppllayout_top.setLayoutParams(layoutParams);
		
		tvChangepet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvChangepet.getPaint().setAntiAlias(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (rpvBanner != null){
			rpvBanner.pause();
		}
		MobclickAgent.onPageEnd("ServiceFeature"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.service_back:
			// 返回
			goBack();
			break;
		case R.id.layout_service_back:
			// 返回
			goBack();
			break;
		case R.id.ll_servicedetail_changepet:
			// 跳转到宠物列表
			goNextForData(ChoosePetActivityNew.class,Global.SERVICEFEATURE_TO_PETLIST_1);
			break;
		case R.id.re_layout_scaling:// one
		case R.id.button_textview_fea_one:// one
			if (fIntent.getIntExtra("beautician_id", 0)>0) {
				goAppoinement(0);
			}else {
				if (!isopenOne) {
					isopenOne = true;
					one_service_item.setVisibility(View.VISIBLE);
					imageview_one.setBackgroundResource(R.drawable.service_icon_arrow_down);
					try {
						if (featureList.size()==1) {
							service_feature_item.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					isopenOne = false;
					one_service_item.setVisibility(View.GONE);
					imageview_one.setBackgroundResource(R.drawable.service_icon_arrow_up);
					try {
						if (featureList.size()==1) {
							service_feature_item.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
		case R.id.re_layout_scaling_and_wash:// two
		case R.id.button_textview_fea_two:// two
			if (fIntent.getIntExtra("beautician_id", 0)>0) {
				goAppoinement(1);
			}else {
				if (!isopenTwo) {
					isopenTwo=true;
					two_service_item.setVisibility(View.VISIBLE);
					imageview_two.setBackgroundResource(R.drawable.service_icon_arrow_down);
					try {
						if (featureList.size()==2) {
							service_feature_item.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					isopenTwo = false;
					two_service_item.setVisibility(View.GONE);
					imageview_two.setBackgroundResource(R.drawable.service_icon_arrow_up);
					try {
						if (featureList.size()==2) {
							service_feature_item.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			break;
		case R.id.re_layout_scaling_and_beau:// thr
		case R.id.button_textview_fea_thr:// thr
			if (fIntent.getIntExtra("beautician_id", 0)>0) {
				goAppoinement(2);
			}else {
				if (!isopenThr) {
					isopenThr = true;
					thr_service_item.setVisibility(View.VISIBLE);
					imageview_thr.setBackgroundResource(R.drawable.service_icon_arrow_down);
					service_feature_item.setVisibility(View.GONE);
				}else {
					isopenThr = false;
					thr_service_item.setVisibility(View.GONE);
					imageview_thr.setBackgroundResource(R.drawable.service_icon_arrow_up);
					service_feature_item.setVisibility(View.VISIBLE);
				}
			}
			
			break;
		case R.id.layout_top_next_eva:
		case R.id.service_layout_eva_one:
		case R.id.service_layout_eva_two:
		case R.id.item_eva_layout:// 评价all
			// 跳转美容师评价 同上
			goNextForPetComment(BeauticianCommentsListActivity.class, 0);
			break;
		case R.id.service_share:
			/**
			 * QQ空间分享必须有文字和图片否则会报-10001错误
			 */
			setShareContent(shareList.get(0).img, shareList.get(0).txt,shareList.get(0).url);
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE);
			mController.openShare(act, false);

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || "".equals(data))
			return;
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.SERVICEFEATURE_TO_PETLIST_1) {
				// 选择宠物类型返回
				customerpetid = data.getIntExtra("customerpetid", 0);
				customerpetname = data.getStringExtra("customerpetname");

				petid = data.getIntExtra("petid", 0);
				petkind = data.getIntExtra("petkind", 0);
				petname = data.getStringExtra("petname");
				//保存选择的宠物的头像
				spUtil.saveString("petimg", data.getStringExtra("petimage"));
				avatarPath = data.getStringExtra("petimage");
				getData(petid);
			}
		}
	}

	private void goBack() {
		// MApplication.listAppoint.remove(this);
		// MApplication.listAppoint.clear();
		for (int i = 0; i < MApplication.listAppoint.size(); i++) {
			if (MApplication.listAppoint.get(i).equals(act)) {
				MApplication.listAppoint.get(i).finish();
			}
		}
		clearData();
		finishWithAnimation();
	}

	private void clearData() {
		spUtil.removeData("newpetkindid");
		spUtil.removeData("newpetname");
		spUtil.removeData("newpetimg");
		spUtil.removeData("newtime");
		spUtil.removeData("newdate");
		spUtil.removeData("newaddrtype");
		spUtil.removeData("addserviceids");
		spUtil.removeData("changepet");
	}

	private void goNextForData(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		if (pre==Global.SERVICEFEATURE_TO_PETLIST_1) {
			intent.putExtra("previous", Global.SERVICEFEATURE_TO_PETLIST_1);
		}else {
			intent.putExtra("previous", Global.SERVICEFEATURE_TO_PETLIST);
		}
		intent.putExtra("servicename", serviceName);
		intent.putExtra("beautician_id",fIntent.getIntExtra("beautician_id", 0));
		startActivityForResult(intent, pre);
	}

	private void goAppoinement(int index) {
		if (featureList.size()<=0) {
			return;
		}
		ServicesFeature feature = featureList.get(index);
		Intent intent = null;
//		if ((spUtil.getInt("tareaid", 0) <= 0 || spUtil.getInt("tareaid", 0) == 100)&& fIntent.getIntExtra("beautician_id", 0) <= 0) {
//			intent = new Intent(this, ShopListActivity.class);
//			intent.putExtra("previous", Global.NOAREA_TO_APPOINTMENT);
//		} else {
			intent = new Intent(this, AppointmentActivity.class);
//		}
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", feature.PetServicePojoId);
		intent.putExtra("servicetype", feature.serviceType);
		intent.putExtra("serviceloc", 1);
		intent.putExtra("petid", petid);
		intent.putExtra("petkind", petkind);
		intent.putExtra("petname", petname);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("customerpetname", customerpetname);
		intent.putExtra("clicksort", clicksort);
		intent.putExtra("avatarPath", avatarPath);
		if (fIntent.getIntExtra("beautician_id", 0)>0) {
			intent.putExtra("areaid", areaid);
			intent.putExtra("shopid", shopid);
		}else {
			if (spUtil.getInt("tareaid", 0) == 100) {
				intent.putExtra("areaid", areaid);
			} else {
				intent.putExtra("areaid", spUtil.getInt("tareaid", 0));
			}
			if (spUtil.getInt("tshopid", 0) <= 0) {
				intent.putExtra("shopid", shopid);
			} else {
				intent.putExtra("shopid", spUtil.getInt("tshopid", 0));
			}
		}
		// intent.putExtra("tareaid", fIntent.getIntExtra("tareaid", 0));
		// intent.putExtra("tshopid", fIntent.getIntExtra("tshopid", 0));
		intent.putExtra("areaname", fIntent.getStringExtra("areaname"));
		intent.putExtra("beautician_id",fIntent.getIntExtra("beautician_id", 0));
		intent.putExtra("beautician_gender",fIntent.getIntExtra("beautician_sex", 0));
		intent.putExtra("beautician_sort",fIntent.getIntExtra("beautician_sort", 0));
		intent.putExtra("beautician_ordernum",fIntent.getIntExtra("beautician_ordernum", 0));
		intent.putExtra("beautician_name",fIntent.getStringExtra("beautician_name"));
		intent.putExtra("beautician_image",fIntent.getStringExtra("beautician_image"));
		intent.putExtra("beautician_sortname",fIntent.getStringExtra("beautician_levelname"));
		intent.putExtra("upgradeTip",fIntent.getStringExtra("upgradeTip"));

		startActivity(intent);
	}

	private void setComments(JSONObject jData) throws JSONException {
		try {
			JSONArray array = jData.getJSONArray("comments");
			if (array.length() == 0) {
				service_layout_eva_one.setVisibility(View.GONE);
				service_layout_eva_two.setVisibility(View.GONE);
			} else if (array.length() == 1) {
				service_layout_eva_one.setVisibility(View.VISIBLE);
				service_layout_eva_two.setVisibility(View.GONE);
				JSONObject objectOne = array.getJSONObject(0);
				String realNameOne = "";
				if (objectOne.has("realName") && !objectOne.isNull("realName")) {
					realNameOne = objectOne.getString("realName");
				}
				String contentOne = "";
				if (objectOne.has("content") && !objectOne.isNull("content")) {
					contentOne = objectOne.getString("content");
				}
				textView_service_eva_one.setText(realNameOne + ": " + contentOne);
				if (objectOne.has("avatar") && !objectOne.isNull("avatar")) {
					ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+ objectOne.getString("avatar"),service_eva_one,  0,null);
				}
				if (objectOne.has("user")&&!objectOne.isNull("user")) {
					JSONObject objectUser = objectOne.getJSONObject("user");
					if (objectUser.has("userMemberLevel")&&!objectUser.isNull("userMemberLevel")) {
						JSONObject objectUserLevel = objectUser.getJSONObject("userMemberLevel");
						if (objectUserLevel.has("memberIcon")&&!objectUserLevel.isNull("memberIcon")) {
							service_eva_is_member_one.setVisibility(View.VISIBLE);
							String memberIcon = objectUserLevel.getString("memberIcon");
							if (!memberIcon.startsWith("http")) {
								memberIcon = CommUtil.getServiceNobacklash()+memberIcon;
							}
							ImageLoaderUtil.setImage(service_eva_is_member_one,memberIcon, 0);
						}else {
							service_eva_is_member_one.setVisibility(View.GONE);
						}
					}
				}
			} else if (array.length() == 2) {
				service_layout_eva_one.setVisibility(View.VISIBLE);
				service_layout_eva_two.setVisibility(View.VISIBLE);
				JSONObject objectOne = array.getJSONObject(0);
				String realNameOne = "";
				if (objectOne.has("realName") && !objectOne.isNull("realName")) {
					realNameOne = objectOne.getString("realName");
				}
				String contentOne = "";
				if (objectOne.has("content") && !objectOne.isNull("content")) {
					contentOne = objectOne.getString("content");
				}
				textView_service_eva_one.setText(realNameOne + ": " + contentOne);

				JSONObject objectTwo = array.getJSONObject(1);
				String realNameTwo = "";
				if (objectTwo.has("realName") && !objectTwo.isNull("realName")) {
					realNameTwo = objectTwo.getString("realName");
				}
				String contentTwo = "";
				if (objectTwo.has("content") && !objectTwo.isNull("content")) {
					contentTwo = objectTwo.getString("content");
				}
				textView_service_eva_two.setText(realNameTwo + ": " + contentTwo);
				if (objectOne.has("avatar") && !objectOne.isNull("avatar")) {
					ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+ objectOne.getString("avatar"),service_eva_one,  0,null);
				}
				if (objectTwo.has("avatar") && !objectTwo.isNull("avatar")) {
					ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+ objectTwo.getString("avatar"),service_eva_two,  0,null);
				}
				if (objectOne.has("user")&&!objectOne.isNull("user")) {
					JSONObject objectUser = objectOne.getJSONObject("user");
					if (objectUser.has("userMemberLevel")&&!objectUser.isNull("userMemberLevel")) {
						JSONObject objectUserLevel = objectUser.getJSONObject("userMemberLevel");
						if (objectUserLevel.has("memberIcon")&&!objectUserLevel.isNull("memberIcon")) {
							service_eva_is_member_one.setVisibility(View.VISIBLE);
							String memberIcon = objectUserLevel.getString("memberIcon");
							if (!memberIcon.startsWith("http")) {
								memberIcon = CommUtil.getServiceNobacklash()+memberIcon;
							}
							ImageLoaderUtil.setImage(service_eva_is_member_one,memberIcon, 0);
						}else {
							service_eva_is_member_one.setVisibility(View.GONE);
						}
					}
				}
				if (objectTwo.has("user")&&!objectTwo.isNull("user")) {
					JSONObject objectUser = objectTwo.getJSONObject("user");
					if (objectUser.has("userMemberLevel")&&!objectUser.isNull("userMemberLevel")) {
						JSONObject objectUserLevel = objectUser.getJSONObject("userMemberLevel");
						if (objectUserLevel.has("memberIcon")&&!objectUserLevel.isNull("memberIcon")) {
							service_eva_is_member_two.setVisibility(View.VISIBLE);
							String memberIcon = objectUserLevel.getString("memberIcon");
							if (!memberIcon.startsWith("http")) {
								memberIcon = CommUtil.getServiceNobacklash()+memberIcon;
							}
							ImageLoaderUtil.setImage(service_eva_is_member_two,memberIcon, 0);
						}else {
							service_eva_is_member_two.setVisibility(View.GONE);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void goNextForPetComment(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("serviceids", sbServiceIds.toString().substring(1));
		intent.putExtra("petid", petid);
		if (pre == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
			intent.putExtra("petname", fIntent.getStringExtra("petname"));
		}
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		spUtil.removeData("charactservice");
		super.onDestroy();
	}

	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1104724367";
		String appKey = "gASimi0oEHprSSxe";
		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(act, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx1668e9f200eb89b2";
		String appSecret = "4b1e452b724bc085ac72058dd39adf01";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(act, appId,appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(act,appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	
	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent(String url, String message, String targUrl) {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSinaCallbackUrl("http://www.baidu.com");
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(act, "1104724367", "gASimi0oEHprSSxe");
		qZoneSsoHandler.addToSocialSDK();
		mController.setShareContent(message);
		mController.setShareImage(new UMImage(act, url));

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setTitle(message);
		circleMedia.setShareContent(message);
		circleMedia.setShareImage(new UMImage(act, url));
		circleMedia.setTargetUrl(targUrl);
		mController.setShareMedia(circleMedia);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setTitle(message);
		weixinContent.setShareContent(message);
		weixinContent.setShareImage(new UMImage(act, url));
		weixinContent.setTargetUrl(targUrl);
		mController.setShareMedia(weixinContent);

		// QQShareContent qqShareContent = new QQShareContent();
		// qqShareContent.setShareContent(message);
		// qqShareContent.setShareImage(new UMImage(mainActivity, url));
		// mController.setShareMedia(qqShareContent);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTitle(message);
		sinaContent.setShareContent(message + targUrl);
		sinaContent.setShareImage(new UMImage(act, url));
		sinaContent.setTargetUrl(targUrl);
		mController.setShareMedia(sinaContent);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setTitle(" ");// 不分享title 但是title处不可为null否则显示分享到QQ空间
		qzone.setShareContent(message);
		qzone.setShareImage(new UMImage(act, url));
		qzone.setTargetUrl(targUrl);
		mController.setShareMedia(qzone);

	}
	
	private AsyncHttpResponseHandler getShare = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->str " + new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						shareList.add(Share.json2Entity(object));
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
	private SpannableString getTextScore(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.service_score_left), 0,lastOnePrice.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this, R.style.service_score_right),lastOnePrice.length()-1,lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private void goAd(Context context, String url, Class cls) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", url);
		startActivity(intent);
	}
}
