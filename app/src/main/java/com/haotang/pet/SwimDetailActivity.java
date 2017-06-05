package com.haotang.pet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.SwimAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.SwimIcon;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.ViewHolder;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;

@SuppressLint("NewApi")
public class SwimDetailActivity extends SuperActivity implements
		OnClickListener {

	public static SwimDetailActivity act;
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

	private SharedPreferenceUtil spUtil;

	private TextView tv_orderdetail_money;
	private Button bt_orderdetail_pay;

	private RollPagerView rpvBanner;
	private BannerBathLoopAdapter adapterBanner;

	private LinearLayout layout_show_title;
	private TextView show_title;
	private TextView textview_swim_safe;
	private TextView textview_swim_time;
	private TextView textview_swim_address;
	private ImageView ImageView_go_map;
	private TextView textview_swim_score;
	private TextView textview_swim_score_detail;
	private LinearLayout layout_add_pet;
	private MListview swim_pets;
	// private MListview swim_scaling_image;
	private LinearLayout layout_safe;
	private int shopId = 9;
	private Pet pet;
	public static ArrayList<Pet> listPets = new ArrayList<Pet>();
	private SwimMulPetCurrent<Pet> sAMulPet;
	private int addrid;
	private int current = -1;
	private String[] months = null;
	private String[] EveryWeek = null;
	private int SwimPriceId;

	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	private double price;
	private int listPrice;
	private double lastPrice;
	private double totalfee;
	private ArrayList<String> listBanner = new ArrayList<String>();
	private ArrayList<SwimIcon> SwimLists = new ArrayList<SwimIcon>();
	private MyGridView swim_bottom_icon;
	private SwimAdapter<SwimIcon> mSAdapter;
	private ArrayList<String> Imagelist = new ArrayList<String>();
	private double lng;
	private double lat;
	private String address = "";
	private String shopImg = "";
	private String shopAddress = "";
	private String shopName = "";
	private double ShopLng = 0;
	private double ShopLat = 0;
	private boolean isFirst = true;
	private boolean isBanner = true;
	private boolean isImg = true;
	private Pet petAccpet = null;
	private String guaranteeUrl = "";
	private String guarantee = "";
	private int index = -1;
	private TextView text_swim_show_feature;
	private LinearLayout swim_feature;
	private TextView textview_swim_notice;
	private LinearLayout swim_dog_notice;
	private MProgressDialog dialog;

	private int previous;
	private int swimTime = 0;
	private LinearLayout layout_pet_time;

	private String showMonth = "";
	private String ShowWeek = "";
	private String AMORPM = "";
	private TextView textView_choose_time;
	private LinearLayout layout_add_detail_img;
	private LinearLayout right;
	private int imgHeightEvery = 0;
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private ArrayList<Bitmap> imgBitMap = new ArrayList<Bitmap>();
	private String openTimeS[] = null;
	private int orderFee = 0;
	private View address_line;
	private TextView textview_give_other_money;
	private TextView textview_member_cutdown;
	private String memberSwimNotice = null;
	private String BuyCardH5Url = null;
	private TextView textView_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_detai);
		MApplication.listAppoint.add(this);
		listPets.clear();
		act = this;
		listBanner.clear();
		images.clear();
		Imagelist.clear();
		dialog = new MProgressDialog(mContext);
		findView();
		setView();
		initReceiver();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		textview_give_other_money.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_ToRecharge);
//				Intent intent = new Intent(mContext, MyLastMoney.class);
//				startActivity(intent);
				goAd(mContext, BuyCardH5Url, ADActivity.class);
			}
		});
		swim_pets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_SwitchPet);
					goNext(ChoosePetActivityNew.class,Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET);
				}
			}
		});
	}

	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SwimDetailActivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}

	private void setView() {
		textView_title.setText("游泳");
//		topStyleShow();
		getIntentFrom();
		listPets.add(pet);
		if (listPets.size() > 0) {
			swim_pets.setVisibility(View.VISIBLE);
		}
		sAMulPet = new SwimMulPetCurrent<Pet>(this, listPets);
		swim_pets.setAdapter(sAMulPet);
		adapterBanner = new BannerBathLoopAdapter(rpvBanner, listBanner);
		rpvBanner.setAdapter(adapterBanner);

		mSAdapter = new SwimAdapter<SwimIcon>(this, SwimLists);
		swim_bottom_icon.setAdapter(mSAdapter);

		getData(pet.id);
		getAppoinmentData();
	}

	private void topStyleShow() {
		pplLayout.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {

			@Override
			public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSlide(int alpha) {
				// TODO Auto-generated method stub
				int alphaReverse = alphaMax - alpha;
				if (alphaReverse < 0) {
					alphaReverse = 0;
				}
				bgBackDrawable.setAlpha(alphaReverse);
				bgBackBelowDrawable.setAlpha(alpha);
				bgShareDrawable.setAlpha(alphaReverse);
				bgShareBelowDrawable.setAlpha(alpha);
				bgNavBarDrawable.setAlpha(alpha);

			}
		});

		bgBackDrawable = service_back.getBackground();
		bgBackBelowDrawable = service_back_blow.getBackground();
		bgShareDrawable = service_share.getBackground();
		bgShareBelowDrawable = service_share_below.getBackground();
		bgNavBarDrawable = rlTitle.getBackground();
		bgBackDrawable.setAlpha(alphaMax);
		bgShareDrawable.setAlpha(alphaMax);
		bgNavBarDrawable.setAlpha(0);
		bgBackBelowDrawable.setAlpha(0);
		bgShareBelowDrawable.setAlpha(0);
	}

	private void getIntentFrom() {
		pet = new Pet();
		/*
		 * address = getIntent().getStringExtra("address"); shopId =
		 * getIntent().getIntExtra("shopId", 9); SwimPriceId =
		 * getIntent().getIntExtra("SwimPriceId", 300); addrid =
		 * getIntent().getIntExtra("addrid",0); current =
		 * getIntent().getIntExtra("current",0); lng =
		 * getIntent().getDoubleExtra("addr_lng", 0); lat =
		 * getIntent().getDoubleExtra("addr_lat", 0); months =
		 * getIntent().getStringArrayExtra("months"); EveryWeek =
		 * getIntent().getStringArrayExtra("WeekShow"); pet = (Pet)
		 * getIntent().getSerializableExtra("pet"); pet.serviceid=SwimPriceId;
		 * SwimLists = getIntent().getParcelableArrayListExtra("icon");
		 */
		// for (int i = 0; i < SwimLists.size(); i++) {
		// SwimLists.get(i).outOrInside=1;
		// }
		// lastPrice = pet.youyongPrice;
		// totalfee = lastPrice;
		// String str = "¥"+Utils.formatDouble(lastPrice);
		// tv_orderdetail_money.setText(Utils.getTextShow(mContext, str, 0, 1,
		// 1, str.length()));
		// tv_orderdetail_money.setText("¥"+Utils.formatDouble(lastPrice));
		previous = getIntent().getIntExtra("previous", -1);

		if (previous == Global.LIVE_TO_SWIM_DETAIL) {
			pet.image = getIntent().getStringExtra("avatarPath");
			pet.kindid = getIntent().getIntExtra("petkind", 0);
			pet.id = getIntent().getIntExtra("petid", 0);
			pet.name = getIntent().getStringExtra("petname");
		} else if (previous == Global.MAIN_TO_SWIM_DETAIL) {
			if (spUtil.getInt("customerpetid", 0) > 0) {
				pet.nickName = spUtil.getString("customerpetname", "");
				pet.customerpetid = spUtil.getInt("customerpetid", 0);
			}
			String myPetImage = spUtil.getString("mypetImage", "");
			if (myPetImage.equals("")) {
				pet.image = CommUtil.getServiceNobacklash()
						+ spUtil.getString("petimage", "");
			} else {
				pet.image = myPetImage;
			}
			pet.kindid = spUtil.getInt("petkind", 0);
			pet.id = spUtil.getInt("petid", 0);
			pet.name = spUtil.getString("petname", "");
			pet.customerpetid = spUtil.getInt("customerpetid", 0);
			pet.youyongPrice = price;
			pet.nickName = spUtil.getString("customerpetname", "");
		} else if (previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
			pet.id = getIntent().getIntExtra("petid", 0);
			pet.kindid = getIntent().getIntExtra("petkind", 0);
			pet.name = getIntent().getStringExtra("petname");
			pet.image = getIntent().getStringExtra("petimage");
			pet.nickName = getIntent().getStringExtra("customerpetname");
			pet.customerpetid = getIntent().getIntExtra("customerpetid", 0);
		} else if (previous == Global.SWIM_MYPET_TO_SWIMAPPOINTMENT) {
			pet.id = getIntent().getIntExtra("petid", 0);
			pet.kindid = getIntent().getIntExtra("petkind", 0);
			pet.name = getIntent().getStringExtra("petname");
			pet.image = getIntent().getStringExtra("petimage");
			pet.customerpetid = getIntent().getIntExtra("customerpetid", 0);
			pet.nickName = spUtil.getString("customerpetname", "");
		}
	}

	private void getAppoinmentData() {
		SwimLists.clear();
		shopId = 9;
//		CommUtil.getSwimTime(mContext, spUtil.getString("cellphone", ""),
//				shopId, Apphandler);
		CommUtil.getSwimTimeNew(mContext, spUtil.getString("cellphone", ""), shopId, AppNewTimehandler);
	}

	private void getData(int petid) {
		dialog.showDialog();
		CommUtil.getSwimDetail(mContext, spUtil.getString("cellphone", ""),
				shopId, petid, handler);
	}

	private void setLastPrice() {
		if (!TextUtils.isEmpty(memberSwimNotice)) {
			lastPrice = lastPrice / 2;
		}
		String text = "实付款: ¥ " + Utils.formatDouble(lastPrice);
		SpannableString ss = new SpannableString(text);
		ss.setSpan(
				new ForegroundColorSpan(getResources()
						.getColor(R.color.tabback)), 0, 4,
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		ss.setSpan(new TextAppearanceSpan(this, R.style.style1), 6,
				ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tv_orderdetail_money.setText(ss);
	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->游泳详情   " + new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("swimPrice")&& !objectData.isNull("swimPrice")) {
							JSONObject objectSP = objectData.getJSONObject("swimPrice");
							if (objectSP.has("descri")&& !objectSP.isNull("descri")) {
								String descri = objectSP.getString("descri");
								layout_show_title.setVisibility(View.VISIBLE);
								show_title.setText(descri);
							} else {
								layout_show_title.setVisibility(View.GONE);
							}
							if (objectSP.has("memberSwimNotice")&&!objectSP.isNull("memberSwimNotice")) {
								memberSwimNotice = objectSP.getString("memberSwimNotice");
								textview_member_cutdown.setVisibility(View.VISIBLE);
								textview_member_cutdown.setText(memberSwimNotice);
							}else {
								memberSwimNotice = null;
								textview_member_cutdown.setVisibility(View.GONE);
							}
							if (objectSP.has("price")&& !objectSP.isNull("price")) {
								price = Utils.formatDouble(objectSP.getDouble("price"), 2);
								if (objectSP.has("listPrice")&& !objectSP.isNull("listPrice")) {
									listPrice = (int) objectSP.getDouble("listPrice");
								}
								if (objectSP.has("orderFee")&& !objectSP.isNull("orderFee")) {
									orderFee = objectSP.getInt("orderFee");
								}
								if (index == 1) {
									listPets.get(0).youyongPrice = price;
									listPets.get(0).listPrice = listPrice;
									listPets.get(0).orderFee = orderFee;
								} else {
									try {
										if (!isFirst) {
											petAccpet.youyongPrice = price;
											petAccpet.listPrice = listPrice;
											petAccpet.orderFee = orderFee;
											listPets.add(petAccpet);
										}
										if (isFirst) {
											isFirst = false;
											pet.youyongPrice = price;
											pet.listPrice = listPrice;
											pet.orderFee = orderFee;
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								sAMulPet.notifyDataSetChanged();
								lastPrice = 0;
								totalfee = 0;
								for (int i = 0; i < listPets.size(); i++) {
									lastPrice += listPets.get(i).youyongPrice;
								}
								totalfee = lastPrice;
								setLastPrice();
								if (listPets.size() >= 5) {
									layout_add_pet.setVisibility(View.GONE);
								}
							}

							if (objectSP.has("guarantee")
									&& !objectSP.isNull("guarantee")) {
								guarantee = objectSP.getString("guarantee");
								textview_swim_safe.setText(objectSP
										.getString("guarantee"));
							}
							if (objectSP.has("guaranteeUrl")
									&& !objectSP.isNull("guaranteeUrl")) {
								guaranteeUrl = objectSP
										.getString("guaranteeUrl");
								Utils.mLogError("==-->swim  guaranteeUrl  "
										+ guaranteeUrl);
							}
							if (isImg) {
								if (objectSP.has("img")&& !objectSP.isNull("img")) {
									String img = objectSP.getString("img");
									String[] showDetailImage = img.split(",");
									for (int i = 0; i < showDetailImage.length; i++) {
										Imagelist.add(CommUtil.getServiceNobacklash()+ showDetailImage[i]);
										Utils.mLogError("==-->Imagelist "+ Imagelist.get(i));
									}
									isImg = false;
									for (int j = 0; j < Imagelist.size(); j++) {
										ImageView imageView = new ImageView(mContext);
										imageView.setTag(j);
										images.add(imageView);
									}
									for (int j = 0; j < Imagelist.size(); j++) {
										new MyTask(images.get(j)).execute(Imagelist.get(j));
									}
								}
							}
							if (objectSP.has("ts") && !objectSP.isNull("ts")) {
								text_swim_show_feature.setText(objectSP.getString("ts"));
							}
							if (objectSP.has("tsContent")&& !objectSP.isNull("tsContent")) {
								swim_feature.removeAllViews();
								swim_feature.setVisibility(View.VISIBLE);
								String tsContent = objectSP.getString("tsContent");
								String[] tsContentShow = tsContent.split(";");
								for (int i = 0; i < tsContentShow.length; i++) {
									TextView textView = new TextView(mContext);
									textView.setText(tsContentShow[i]);
									textView.setTextColor(getResources().getColor(R.color.a676767));
									textView.setPadding(0, 10, 0, 10);
									swim_feature.addView(textView);
								}
							}
							if (objectSP.has("tieshi")&& !objectSP.isNull("tieshi")) {
								textview_swim_notice.setText(objectSP
										.getString("tieshi"));
							}
							if (objectSP.has("tieshiContent")
									&& !objectSP.isNull("tieshiContent")) {
								swim_dog_notice.removeAllViews();
								swim_dog_notice.setVisibility(View.VISIBLE);
								String tieshiContent = objectSP
										.getString("tieshiContent");
								String[] tieshiContentShow = tieshiContent
										.split(";");
								for (int i = 0; i < tieshiContentShow.length; i++) {
									TextView textView = new TextView(mContext);
									textView.setText(tieshiContentShow[i]);
									textView.setTextColor(getResources()
											.getColor(R.color.a676767));
									textView.setPadding(0, 10, 0, 10);
									swim_dog_notice.addView(textView);
								}
							}

						}
						if (objectData.has("lng") && !objectData.isNull("lng")) {
							ShopLng = objectData.getDouble("lng");
						}
						if (objectData.has("lat") && !objectData.isNull("lat")) {
							ShopLat = objectData.getDouble("lat");
						}
						if (objectData.has("address")
								&& !objectData.isNull("address")) {
							shopAddress = objectData.getString("address");
							textview_swim_address
									.setText("乐园地址：" + shopAddress);
						}
						if (objectData.has("shopName")
								&& !objectData.isNull("shopName")) {
							shopName = objectData.getString("shopName");
						}
						if (objectData.has("grade")
								&& !objectData.isNull("grade")) {
							String str = objectData.getInt("grade") + "分";
							textview_swim_score.setText(getGradle(str));
						}
						if (objectData.has("good")
								&& !objectData.isNull("good")) {
							textview_swim_score_detail.setText(objectData
									.getString("good"));
						}
						if (objectData.has("operTime")
								&& !objectData.isNull("operTime")) {
							textview_swim_time.setText("营业时间："
									+ objectData.getString("operTime"));
						}
						// if (isBanner) {
						listBanner.clear();
						if (objectData.has("bannerImg")
								&& !objectData.isNull("bannerImg")) {
							JSONArray array = objectData
									.getJSONArray("bannerImg");
							for (int i = 0; i < array.length(); i++) {
								listBanner.add(CommUtil.getServiceBaseUrl()
										+ array.getString(i));
								// listBanner.add("http://img4.imgtn.bdimg.com/it/u=2460609582,1359771844&fm=21&gp=0.jpg");
								Utils.mLogError("==-->swim listBanner "
										+ listBanner.get(i));
							}

							// isBanner = false;
							if (listBanner.size() <= 1) {
								rpvBanner
										.setHintView(new ColorPointHintView(
												act,
												Color.parseColor("#00000000"/* "#FE8A3F" */),
												Color.parseColor("#00000000")));
							} else if (listBanner.size() > 1) {
								rpvBanner.setHintAlpha(0);
							}
						}
						// }
						if (listBanner.size() > 0) {
							adapterBanner.setIsSwim(1);
							adapterBanner.notifyDataSetChanged();
						}
						if (objectData.has("img") && !objectData.isNull("img")) {
							shopImg = CommUtil.getServiceNobacklash()
									+ objectData.getString("img");
							Utils.mLogError("==-->swim shopImg  " + shopImg);
						}
					}
				}
				try {
					dialog.closeDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				try {
					dialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				dialog.closeDialog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	public MDialog mDialog;

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
		right = (LinearLayout) findViewById(R.id.right);
		service_back = (ImageView) findViewById(R.id.service_back);
		layout_service_back = (LinearLayout) findViewById(R.id.layout_service_back);
		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
		service_share = (ImageView) findViewById(R.id.service_share);
		service_share_below = (ImageView) findViewById(R.id.service_share_below);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);

		tv_orderdetail_money = (TextView) findViewById(R.id.tv_orderdetail_money);
		bt_orderdetail_pay = (Button) findViewById(R.id.bt_orderdetail_pay);

		rpvBanner = (RollPagerView) findViewById(R.id.rpv_servicedetail_pet);
		layout_show_title = (LinearLayout) findViewById(R.id.layout_show_title);
		show_title = (TextView) findViewById(R.id.show_title);
		textview_swim_time = (TextView) findViewById(R.id.textview_swim_time);
		textview_swim_address = (TextView) findViewById(R.id.textview_swim_address);
		ImageView_go_map = (ImageView) findViewById(R.id.ImageView_go_map);
		textview_swim_score = (TextView) findViewById(R.id.textview_swim_score);
		textview_swim_score_detail = (TextView) findViewById(R.id.textview_swim_score_detail);
		layout_add_pet = (LinearLayout) findViewById(R.id.layout_add_pet);
		swim_pets = (MListview) findViewById(R.id.swim_pets);
		// swim_scaling_image = (MListview)
		// findViewById(R.id.swim_scaling_image);
		layout_safe = (LinearLayout) findViewById(R.id.layout_safe);
		swim_bottom_icon = (MyGridView) findViewById(R.id.swim_bottom_icon);
		textview_swim_safe = (TextView) findViewById(R.id.textview_swim_safe);
		text_swim_show_feature = (TextView) findViewById(R.id.text_swim_show_feature);
		textview_swim_notice = (TextView) findViewById(R.id.textview_swim_notice);
		swim_feature = (LinearLayout) findViewById(R.id.swim_feature);
		swim_dog_notice = (LinearLayout) findViewById(R.id.swim_dog_notice);
		layout_pet_time = (LinearLayout) findViewById(R.id.layout_pet_time);
		textView_choose_time = (TextView) findViewById(R.id.textView_choose_time);
		layout_add_detail_img = (LinearLayout) findViewById(R.id.layout_add_detail_img);
		address_line = (View) findViewById(R.id.address_line);
		textview_give_other_money = (TextView) findViewById(R.id.textview_give_other_money);
		textview_member_cutdown = (TextView) findViewById(R.id.textview_member_cutdown);
		textView_title = (TextView) findViewById(R.id.textView_title);

		layout_add_pet.setOnClickListener(this);
		service_back.setOnClickListener(this);
		bt_orderdetail_pay.setOnClickListener(this);
		ImageView_go_map.setOnClickListener(this);
		layout_safe.setOnClickListener(this);
		textview_swim_score.setOnClickListener(this);
		right.setOnClickListener(this);
		layout_pet_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.service_back:
			finishWithAnimation();
			break;
		case R.id.layout_add_pet:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_AddPet);
			goNext(ChoosePetActivityNew.class, Global.SWIM_DETAIL_ADD_PET);
			break;
		case R.id.bt_orderdetail_pay:
			if (current == -1) {
				ToastUtil.showToastShortCenter(mContext, "请选择预约时间");
				return;
			}
			if (!Utils.checkNet(mContext)) {
				return;
			}
			if (isLogin()) {
				goNext(OrderDetailActivity.class,Global.SWIM_DETAIL_TO_ORDERDETAIL);
			} else {
//				goNext(LoginActivity.class, 0);
				goLogin(LoginActivity.class, 7001, Global.PRE_RECHARGEPAGE_ZF);
			}
			break;
		case R.id.ImageView_go_map:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_Navigation);
			goNext(ShopLocationActivity.class,Global.SWIM_DETAIL_TO_LOCALACTIVITY);
			break;
		case R.id.layout_safe:
			// goNext(ShopH5DetailActivity.class,
			// Global.SWIM_DETAIL_TO_WEBVIEW);
			break;
		case R.id.right:
		case R.id.textview_swim_score:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_Evaluation);
			goNext(BeauticianCommentsListActivity.class, 0);
			break;
		case R.id.layout_pet_time:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Swimming_SelectTime);
			if (months == null && EveryWeek == null) {
			} else {
				goNext(PetDataSwimActivity.class,Global.SWIM_APPOINMENT_CHOOSE_TIME);
			}
			break;
		}
	}

	private void goLogin(Class cls ,int requestCode,int pre){
		Intent intent = new Intent(this, cls);
		intent.putExtra("previous", pre);
		startActivityForResult(intent, requestCode);
	}
	private void goNext(Class cls, int pre) {
		Intent intent = new Intent(this, cls);
		if (pre != Global.SWIM_APPOINMENT_CHOOSE_TIME) {
			intent.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
		}
		intent.putExtra("previous", pre);
		intent.putExtra("months", months);
		intent.putExtra("WeekShow", EveryWeek);
		intent.putExtra("shopId", shopId);
		intent.putExtra("SwimPriceId", SwimPriceId);
		intent.putExtra("pet", pet);
		intent.putExtra("current", current);
		intent.putExtra("totalfee", totalfee);
		intent.putExtra("addr_lng", lng);
		intent.putExtra("addr_lat", lat);
		intent.putExtra("addrid", addrid);
		intent.putExtra("address", address);
		intent.putExtra("shopImg", shopImg);
		intent.putExtra("shopAddress", shopAddress);
		intent.putExtra("shopName", shopName);
		intent.putExtra("orderDetailH5Url", guaranteeUrl);
		intent.putExtra("guarantee", guarantee);
		intent.putExtra("serviceids", "300");
		intent.putExtra("AMORPM", AMORPM);
		intent.putExtra("memberSwimNotice", memberSwimNotice);
		if (pre == Global.SWIM_DETAIL_TO_LOCALACTIVITY) {
			intent.putExtra("shopname", shopName);
			intent.putExtra("shopaddr", shopAddress);
			intent.putExtra("shoplat", ShopLat);
			intent.putExtra("shoplng", ShopLng);
		}
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			for (int i = 0; i < listBanner.size(); i++) {
				boolean isRemove = ImageLoaderUtil
						.removeDiskCacheFile(listBanner.get(i));
				if (isRemove) {
					Utils.mLogError("==-->移除磁盘缓存成功");
				} else {
					Utils.mLogError("==-->移除磁盘缓存失败");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Global.RESULT_OK) {
			if (requestCode==7001) {
				isFirst = true;
				getData(listPets.get(0).id);
			}
		}
	}
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			index = intent.getIntExtra("index", -1);
			if (index == 0) {
				petAccpet = new Pet();
				String petName = intent.getStringExtra("petname");
				petAccpet.id = intent.getIntExtra("petid", 0);
				petAccpet.kindid = intent.getIntExtra("petkind", 0);
				petAccpet.customerpetid = intent
						.getIntExtra("customerpetid", 0);
				petAccpet.image = intent.getStringExtra("petimage");
				petAccpet.youyongPrice = price;
				petAccpet.name = petName;
				petAccpet.serviceid = SwimPriceId;
				petAccpet.nickName = intent.getStringExtra("customerpetname");
				getData(petAccpet.id);
			} else if (index == 1) {
				listPets.get(0).id = intent.getIntExtra("petid", 0);
				listPets.get(0).kindid = intent.getIntExtra("petkind", 0);
				listPets.get(0).customerpetid = intent.getIntExtra(
						"customerpetid", 0);
				listPets.get(0).image = intent.getStringExtra("petimage");
				listPets.get(0).name = intent.getStringExtra("petname");
				listPets.get(0).serviceid = SwimPriceId;
				listPets.get(0).nickName = intent
						.getStringExtra("customerpetname");
				getData(listPets.get(0).id);
			} else if (index == 2) {
				showMonth = intent.getStringExtra("monthEvery");
				ShowWeek = intent.getStringExtra("weekEveryShow");
				AMORPM = intent.getStringExtra("AMORPM");
				current = intent.getIntExtra("current", -1);
				textView_choose_time.setText(showMonth + " " + ShowWeek + " "
						+ AMORPM);
				Utils.mLogError("==-->showMonth " + showMonth);
				String[] monthCom = months[current].replace("年", "-")
						.replace("月", "-").replace("日", ",").replace("上午", "")
						.split(",");
//				int comResult = Utils.compare_date(openTimeS[0], monthCom[0]);
//				if (comResult == 1) {
//					bt_orderdetail_pay.setOnClickListener(null);
//					bt_orderdetail_pay.setText("敬请期待");
//					bt_orderdetail_pay.setBackgroundColor(Color.parseColor("#bdbdbd"));
//					// ToastUtil.showToastShortCenter(mContext, "敬请期待");
//					// finishWithAnimation();
//				} else {
					bt_orderdetail_pay.setText("下一步");
					bt_orderdetail_pay.setBackgroundColor(getResources().getColor(R.color.orange));
					bt_orderdetail_pay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (current == -1) {
										ToastUtil.showToastShortCenter(mContext, "请选择预约时间");
										return;
									}
									if (!Utils.checkNet(mContext)) {
										return;
									}
									if (isLogin()) {
										goNext(OrderDetailActivity.class,Global.SWIM_DETAIL_TO_ORDERDETAIL);
									} else {
										goLogin(LoginActivity.class, 7001, Global.PRE_RECHARGEPAGE_ZF);
									}
								}
							});
//				}
			}
		}
	}

	private SpannableString getGradle(String str) {
		SpannableString styledText = new SpannableString(str);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.swimdetailshowtext0), 0, str.length() - 1,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.swimdetailshowtext1), str.length() - 1, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (rpvBanner != null)
			rpvBanner.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (rpvBanner != null) {
			rpvBanner.pause();
		}
	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

	public class SwimMulPetCurrent<T> extends CommonAdapter<T> {

		public SwimMulPetCurrent(Activity mContext, List<T> mDatas) {
			super(mContext, mDatas);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
					parent, R.layout.item_swim_detail_pet, position);
			Pet pet = (Pet) mDatas.get(position);
			viewHolder.setBackgroundCircle(R.id.mul_petimg, pet.image,
					R.drawable.dog_icon_unnew);
			if (pet.nickName == null || pet.nickName.equals("")) {
				viewHolder.setText(R.id.textView_swim_title, pet.name.trim()
						+ "游泳");
			} else {
				viewHolder.setText(R.id.textView_swim_title,
						pet.nickName.trim() + "游泳");
			}
			viewHolder.setText(R.id.textview_swim_price,
					Utils.formatDouble(pet.youyongPrice) + "");

			TextView textView_old_price = viewHolder
					.getView(R.id.textView_old_price);
			textView_old_price.setText("原价:¥"
					+ Utils.formatDouble(pet.listPrice));
			textView_old_price.setVisibility(View.VISIBLE);
//			TextPaint paint = textView_old_price.getPaint();
//			paint.setAntiAlias(true);
//			paint.setColor(Color.RED);
//			paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
			if (position != 0) {
				viewHolder.setImageResource(R.id.imageView_arrow,
						R.drawable.icon_swim_close);
				viewHolder.getView(R.id.imageView_arrow).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mDialog != null && mDialog.isShowing()) {
									ToastUtil.showToastShortBottom(
											SwimDetailActivity.this, "请稍后...");
								} else {
									showDeletePet("确认删除该宠物？", position);
								}
							}
						});
			}
			return viewHolder.getConvertView();
		}

		private void showDeletePet(String hint, final int position) {
			mDialog = new MDialog.Builder(SwimDetailActivity.this)
					.setType(MDialog.DIALOGTYPE_CONFIRM)
					.setMessage(hint)
					.setCancelTextColor(
							getResources().getColor(R.color.orange_light))
					.setCancelStr("取消").setOKStr("确定")
					.positiveListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							mDatas.remove(position);
							lastPrice = 0;
							totalfee = 0;
							for (int i = 0; i < listPets.size(); i++) {
								lastPrice += listPets.get(i).youyongPrice;
							}
							totalfee = lastPrice;
							setLastPrice();
							if (listPets.size() >= 5) {
								layout_add_pet.setVisibility(View.GONE);
							} else {
								layout_add_pet.setVisibility(View.VISIBLE);
							}
							sAMulPet.notifyDataSetChanged();
						}
					}).build();
			mDialog.show();
		}

	}
	//new time
	private AsyncHttpResponseHandler AppNewTimehandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("swimIndividual")&&!objectData.isNull("swimIndividual")) {
							JSONArray objectSwim = objectData.getJSONArray("swimIndividual");
							if (objectSwim.length() > 0) {
								for (int i = 0; i < objectSwim.length(); i++) {
									SwimIcon swimIcon = new SwimIcon();
									JSONObject objectSwimEvery = objectSwim.getJSONObject(i);
									if (objectSwimEvery.has("picPath")&& !objectSwimEvery.isNull("picPath")) {
										swimIcon.picPath = CommUtil.getServiceNobacklash()+ objectSwimEvery.getString("picPath");
									}
									if (objectSwimEvery.has("name")&& !objectSwimEvery.isNull("name")) {
										swimIcon.name = objectSwimEvery.getString("name");
									}
									if (objectSwimEvery.has("shopId")&& !objectSwimEvery.isNull("shopId")) {
										swimIcon.shopId = objectSwimEvery.getInt("shopId");
									}
									if (objectSwimEvery.has("sort")&& !objectSwimEvery.isNull("sort")) {
										swimIcon.sort = objectSwimEvery.getInt("sort");
									}
									if (objectSwimEvery.has("SwimIndividualId")&& !objectSwimEvery.isNull("SwimIndividualId")) {
										swimIcon.SwimIndividualId = objectSwimEvery.getInt("SwimIndividualId");
									}
									SwimLists.add(swimIcon);
								}
								if (SwimLists.size() > 0) {
									mSAdapter.notifyDataSetChanged();
								}
							}
						}
						if (objectData.has("activityCopy")&&!objectData.isNull("activityCopy")) {
							JSONObject objectActivityCopy = objectData.getJSONObject("activityCopy");
							if (objectActivityCopy.has("txt")&&!objectActivityCopy.isNull("txt")) {
								textview_give_other_money.setText(objectActivityCopy.getString("txt"));
								textview_give_other_money.setVisibility(View.VISIBLE);
								address_line.setVisibility(View.GONE);
							}
							if (objectActivityCopy.has("url")&&!objectActivityCopy.isNull("url")) {
								BuyCardH5Url = objectActivityCopy.getString("url");
							}
						}else {
							textview_give_other_money.setVisibility(View.GONE);
							address_line.setVisibility(View.VISIBLE);
						}
						if (objectData.has("time")&&!objectData.isNull("time")) {
							JSONArray arrayTime = objectData.getJSONArray("time");
							if (arrayTime.length()>0) {
								months = new String[arrayTime.length()];
								EveryWeek = new String[arrayTime.length()];
								for (int i = 0; i < arrayTime.length(); i++) {
									JSONObject objectTime = arrayTime.getJSONObject(i);
									if (objectTime.has("date")&&!objectTime.isNull("date")) {
										months[i] = objectTime.getString("date").replace("00:00","").trim();
									}
									if (objectTime.has("week")&&!objectTime.isNull("week")) {
										EveryWeek[i] = objectTime.getString("week");
									}
								}
							}
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
	//old time
	private AsyncHttpResponseHandler Apphandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("swimTime")
								&& !objectData.isNull("swimTime")) {
							swimTime = objectData.getInt("swimTime");
							months = new String[swimTime];
							EveryWeek = new String[swimTime];
							long nowDate = System.currentTimeMillis();
							for (int i = 0; i < swimTime; i++) {
								long beforDate = Utils.beforDateNum(nowDate, i);
								Date date = new Date(beforDate);
								@SuppressWarnings("deprecation")
								String DateTime = date.toLocaleString();
								months[i] = DateTime;
								EveryWeek[i] = Utils.getWeekOfDate(date);
							}
						}

						if (objectData.has("openTime")
								&& !objectData.isNull("openTime")) {
							String openTime = objectData.getString("openTime");
							openTimeS = openTime.split(" ");
							String[] monthCom = months[0].replace("年", "-")
									.replace("月", "-").replace("日", ",")
									.replace("上午", "").split(",");
							int comResult = Utils.compare_date(openTimeS[0],
									monthCom[0]);
							Utils.mLogError("==-->===openTimeS " + openTimeS[0]
									+ " monthCom  " + monthCom[0]);
							if (comResult == 1) {
								bt_orderdetail_pay.setOnClickListener(null);
								bt_orderdetail_pay.setText("敬请期待");
								bt_orderdetail_pay.setBackgroundColor(Color.parseColor("#bdbdbd"));
							}
						}
						if (objectData.has("activityCopy")&&!objectData.isNull("activityCopy")) {
//							String activityCopy = objectData.getString("activityCopy");
//							textview_give_other_money.setText(activityCopy);
//							textview_give_other_money.setVisibility(View.VISIBLE);
//							address_line.setVisibility(View.GONE);
							
							JSONObject objectActivityCopy = objectData.getJSONObject("activityCopy");
							if (objectActivityCopy.has("txt")&&!objectActivityCopy.isNull("txt")) {
								textview_give_other_money.setText(objectActivityCopy.getString("txt"));
								textview_give_other_money.setVisibility(View.VISIBLE);
								address_line.setVisibility(View.GONE);
							}
							if (objectActivityCopy.has("url")&&!objectActivityCopy.isNull("url")) {
								BuyCardH5Url = objectActivityCopy.getString("url");
							}
						}else {
							textview_give_other_money.setVisibility(View.GONE);
							address_line.setVisibility(View.VISIBLE);
						}
						if (objectData.has("swimIndividual")
								&& !objectData.isNull("swimIndividual")) {
							JSONArray objectSwim = objectData
									.getJSONArray("swimIndividual");
							if (objectSwim.length() > 0) {
								for (int i = 0; i < objectSwim.length(); i++) {
									SwimIcon swimIcon = new SwimIcon();
									JSONObject objectSwimEvery = objectSwim
											.getJSONObject(i);
									if (objectSwimEvery.has("picPath")
											&& !objectSwimEvery
													.isNull("picPath")) {
										swimIcon.picPath = CommUtil
												.getServiceNobacklash()
												+ objectSwimEvery
														.getString("picPath");
									}
									if (objectSwimEvery.has("name")
											&& !objectSwimEvery.isNull("name")) {
										swimIcon.name = objectSwimEvery
												.getString("name");
									}
									if (objectSwimEvery.has("shopId")
											&& !objectSwimEvery
													.isNull("shopId")) {
										swimIcon.shopId = objectSwimEvery
												.getInt("shopId");
									}
									if (objectSwimEvery.has("sort")
											&& !objectSwimEvery.isNull("sort")) {
										swimIcon.sort = objectSwimEvery
												.getInt("sort");
									}
									if (objectSwimEvery.has("SwimIndividualId")
											&& !objectSwimEvery
													.isNull("SwimIndividualId")) {
										swimIcon.SwimIndividualId = objectSwimEvery
												.getInt("SwimIndividualId");
									}
									SwimLists.add(swimIcon);
								}
								if (SwimLists.size() > 0) {
									mSAdapter.notifyDataSetChanged();
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
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

	class MyTask extends AsyncTask<String, Void, byte[]> {
		private ImageView vi;

		private MyTask(ImageView vi) {
			this.vi = vi;
		}

		@Override
		protected byte[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			BufferedInputStream bis = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				if (connection.getResponseCode() == 200) {
					bis = new BufferedInputStream(connection.getInputStream());
					byte[] buffer = new byte[1024 * 8];
					int c = 0;
					while ((c = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, c);
						baos.flush();
					}
					return baos.toByteArray();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setLayOutAddView(result);
		}

		private void setLayOutAddView(byte[] result) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
					result.length);
			imgBitMap.add(bitmap);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.width = Utils.getDisplayMetrics(act)[0];
			params.topMargin = Utils.dip2px(mContext, 10);
			int imgWidth = 0;
			int imgHeight = 0;
			if (bitmap != null) {
				imgWidth = bitmap.getWidth();
				imgHeight = bitmap.getHeight();
				params.height = ((params.width * imgHeight) / imgWidth);
				// imgHeightEvery = params.height;
				// LinearLayout.LayoutParams paramsTwo = new
				// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
				// paramsTwo.width=Utils.getDisplayMetrics(act)[0];
				// paramsTwo.height=imgHeightEvery;
				// paramsTwo.topMargin=Utils.dip2px(mContext, 10);
				vi.setLayoutParams(params);
				vi.setScaleType(ScaleType.FIT_XY);
				vi.setImageBitmap(bitmap);
				layout_add_detail_img.addView(vi);
			}
		}
	}
	private void goAd(Context context, String url, Class cls) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", url);
		startActivity(intent);
	}
}
