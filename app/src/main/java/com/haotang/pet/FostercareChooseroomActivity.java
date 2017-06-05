package com.haotang.pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChooseTransferAdapter;
import com.haotang.pet.adapter.FosPagerAdapter;
import com.haotang.pet.adapter.FostercareXTSAdapter;
import com.haotang.pet.adapter.RoomAdapter;
import com.haotang.pet.dao.PetAddressDao;
import com.haotang.pet.entity.Fostercare;
import com.haotang.pet.entity.PetAddressInfo;
import com.haotang.pet.entity.Room;
import com.haotang.pet.entity.Transfers;
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
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.SelectableRoundedImageView;

public class FostercareChooseroomActivity extends SuperActivity implements
		OnClickListener {
	private final static long DAYTIMEINMILLS = 86400000;
	public static SuperActivity act;
	private ArrayList<View> dots;
	private int oldPostion;
	private int currentItem;
	private PullPushLayout ppl_fostercare_chooseroom;
	private LinearLayout llPoint;
	private ViewPager vpShop;
	private RelativeLayout rlDate;
	private TextView tv_fostercarechooseroom_daterz;
	private TextView tvDaynum;
	private MListview mRoomList;
	private RelativeLayout rlNull;
	private TextView tvNullmsg;
	private Button btRefresh;
	private RoomAdapter rmAdapter;
	private ArrayList<Room> rmList;
	private Intent fIntent;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private String startdate;
	private String enddate;
	private String startdatetxt;
	private String enddatetxt;
	private int daynum;
	private String shoptel;
	private Fostercare fc;
	private int monthposition;
	private String shopName;
	private String shopAddr;
	private double shopLat;
	private double shopLng;
	private MListview mlv_fostercareappointment_zzz;
	private ChooseTransferAdapter chooseTransferAdapter;
	private ArrayList<Transfers> transfersList = new ArrayList<Transfers>();
	private ArrayList<String> jrtips = new ArrayList<String>();
	private RelativeLayout rl_fostercarechooseroom_info;
	private View view1;
	private ImageView iv_fostercarechooseroom_fz;
	private TextView tv_fostercarechooseroom_shoptel;
	private SelectableRoundedImageView sriv_fostercare_chooseroom_petimg;
	private RelativeLayout rl_fostercarechooseroom_jyzn;
	private MListview mlv_fostercareappointment_jyxts;
	private RelativeLayout rl_fostercare_chooseroom_changepet;
	protected String bannerH5Url;
	private PetAddressDao petAddressDao;
	private TextView tv_fostercare_chooseroom_petname;
	private int previous;
	private RelativeLayout rl_fostercare_chooseroom_back;
	private ImageView iv_fostercare_chooseroom_back;
	private Drawable bgNavBarDrawable;
	private int alphaMax = 180;
	private RelativeLayout rlTopBanner;
	private int screenWidth;
	private LayoutParams lp1;
	private int bannerHeight;
	private TextView tv_fostercarechooseroom_dateld;
	private boolean isGoTo;
	private TextView tv_fostercareappointment_to_mylastmoney;
	private String activityCopy;
	private String BuyCardH5Url = null;
	private PopupWindow pWin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercare_chooseroom);
		act = this;
		findView();
		setView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isGoTo = false;
	}

	private void findView() {
		petAddressDao = new PetAddressDao(this);
		vpShop = (ViewPager) findViewById(R.id.iv_servicedetail_pet);
		llPoint = (LinearLayout) findViewById(R.id.point);
		tvShopName = (TextView) findViewById(R.id.tv_fostercarechooseroom_shopname);
		tvShopAddr = (TextView) findViewById(R.id.tv_fostercarechooseroom_shopaddr);
		ibShopNav = (ImageButton) findViewById(R.id.ib_fostercarechooseroom_shopnav);
		rlDate = (RelativeLayout) findViewById(R.id.rl_fostercarechooseroom_date);
		tv_fostercarechooseroom_daterz = (TextView) findViewById(R.id.tv_fostercarechooseroom_daterz);
		tv_fostercarechooseroom_dateld = (TextView) findViewById(R.id.tv_fostercarechooseroom_dateld);
		tvDaynum = (TextView) findViewById(R.id.tv_fostercarechooseroom_daynum);
		mRoomList = (MListview) findViewById(R.id.lv_fostercareappointment_rooms);
		tv_fostercarechooseroom_shoptel = (TextView) findViewById(R.id.tv_fostercarechooseroom_shoptel);
		sriv_fostercare_chooseroom_petimg = (SelectableRoundedImageView) findViewById(R.id.sriv_fostercare_chooseroom_petimg);
		rl_fostercarechooseroom_jyzn = (RelativeLayout) findViewById(R.id.rl_fostercarechooseroom_jyzn);
		rl_fostercare_chooseroom_changepet = (RelativeLayout) findViewById(R.id.rl_fostercare_chooseroom_changepet);
		mlv_fostercareappointment_jyxts = (MListview) findViewById(R.id.mlv_fostercareappointment_jyxts);
		tv_fostercare_chooseroom_petname = (TextView) findViewById(R.id.tv_fostercare_chooseroom_petname);
		rl_fostercarechooseroom_jyzn.setOnClickListener(this);
		rl_fostercare_chooseroom_changepet.setOnClickListener(this);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvNullmsg = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		rl_fostercarechooseroom_info = (RelativeLayout) findViewById(R.id.rl_fostercarechooseroom_info);
		ppl_fostercare_chooseroom = (PullPushLayout) findViewById(R.id.ppl_fostercare_chooseroom);
		mlv_fostercareappointment_zzz = (MListview) findViewById(R.id.mlv_fostercareappointment_zzz);
		iv_fostercarechooseroom_fz = (ImageView) findViewById(R.id.iv_fostercarechooseroom_fz);
		rl_fostercare_chooseroom_back = (RelativeLayout) findViewById(R.id.rl_fostercare_chooseroom_back);
		rl_fostercare_chooseroom_back.bringToFront();
		iv_fostercare_chooseroom_back = (ImageView) findViewById(R.id.iv_fostercare_chooseroom_back);
		rlTopBanner = (RelativeLayout) findViewById(R.id.rl_ppllayout_top);
		tv_fostercareappointment_to_mylastmoney = (TextView) findViewById(R.id.tv_fostercareappointment_to_mylastmoney);
		tv_fostercareappointment_to_mylastmoney.setOnClickListener(this);
		screenWidth = Utils.getDisplayMetrics(this)[0];
		bannerHeight = screenWidth * 243 / 375;
		lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
		rlTopBanner.setLayoutParams(lp1);
		rl_fostercarechooseroom_info.setOnClickListener(this);
		iv_fostercare_chooseroom_back.setOnClickListener(this);
		view1 = (View) findViewById(R.id.view1);
		mlv_fostercareappointment_zzz
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_SwitchStation);
						Transfers transfers = transfersList.get(position);
						chooseTransferAdapter.setClickItem(position);
						setTransferInfo(transfers);
						setFz();
					}
				});
		chooseTransferAdapter = new ChooseTransferAdapter(this);
		mlv_fostercareappointment_zzz.setAdapter(chooseTransferAdapter);
		chooseTransferAdapter.setData(transfersList);

		bgNavBarDrawable = rl_fostercare_chooseroom_back.getBackground();
		bgNavBarDrawable.setAlpha(0);
		ppl_fostercare_chooseroom
				.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {
					@Override
					public void onSlideUp(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlideDwon(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlide(int alpha) {
						int alphaReverse = alphaMax - alpha; 
						if (alphaReverse < 0) {
							alphaReverse = 0;
						}
						bgNavBarDrawable.setAlpha(alpha);
					}
				});

	}

	private void setTransferInfo(Transfers transfers) {
		if (transfers != null) {
			if (fc != null) {
				fc.transferId = transfers.id;
			}
			String name = transfers.name;
			String address = transfers.address;
			shopLat = transfers.lat;
			shopLng = transfers.lng;
			shoptel = transfers.tel;
			shopName = name;
			shopAddr = address;
			Utils.setStringText(tvShopName, name);
			Utils.setStringText(tvShopAddr, address);
			Spanned fromHtml = Html.fromHtml("<u>" + shoptel + "</u>");
			if (fromHtml != null && !TextUtils.isEmpty(fromHtml)) {
				tv_fostercarechooseroom_shoptel.setVisibility(View.VISIBLE);
				tv_fostercarechooseroom_shoptel.setText(fromHtml);
			} else {
				tv_fostercarechooseroom_shoptel.setVisibility(View.INVISIBLE);
			}
			List<String> hotelImg = transfers.hotelImg;
			if (hotelImg != null && hotelImg.size() > 0) {
				dots.clear();
				llPoint.removeAllViews();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						15, 15);
				params.leftMargin = 10;
				for (int i = 0; i < hotelImg.size(); i++) {
					if (i == 0) {
						View view = new View(FostercareChooseroomActivity.this);
						view.setBackgroundResource(R.drawable.dot_focused);
						view.setPadding(10, 0, 10, 0);
						view.setLayoutParams(params);
						llPoint.addView(view);
						dots.add(view);
					} else {
						View view = new View(FostercareChooseroomActivity.this);
						view.setBackgroundResource(R.drawable.dot_normal);
						view.setPadding(10, 0, 10, 0);
						view.setLayoutParams(params);
						llPoint.addView(view);
						dots.add(view);
					}
				}
				vpShop.setAdapter(new FosPagerAdapter(this,hotelImg));
			}
		}
	}

	private void setView() {
		fc = new Fostercare();
		fIntent = getIntent();
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		previous = fIntent.getIntExtra("previous", -1);
		String spPetimage = spUtil.getString("petimage", "");
		String intentPetimage = fIntent.getStringExtra("petimage");
		if (intentPetimage != null && !TextUtils.isEmpty(intentPetimage)) {
			fc.image = intentPetimage;
		} else if (spPetimage != null && !TextUtils.isEmpty(spPetimage)) {
			fc.image = spPetimage;
		}
		ImageLoaderUtil.loadImg(fc.image, sriv_fostercare_chooseroom_petimg,
				R.drawable.user_icon_unnet, null);
		if (previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET) {
			fc.petid = spUtil.getInt("petid", 0);
			fc.petkind = spUtil.getInt("petkind", 0);
			fc.petname = spUtil.getString("petname", "");
			fc.customerpetid = spUtil.getInt("customerpetid", 0);
			fc.customerpetname = spUtil.getString("customerpetname", "");
		} else {
			fc.customerpetid = fIntent.getIntExtra("customerpetid", 0);
			fc.petid = fIntent.getIntExtra("petid", 0);
			fc.petkind = fIntent.getIntExtra("petkind", 0);
			fc.petname = fIntent.getStringExtra("petname");
			fc.customerpetname = fIntent.getStringExtra("customerpetname");
		}
		if (fc.customerpetid > 0)
			Utils.setStringText(tv_fostercare_chooseroom_petname,
					fc.customerpetname + "寄养");
		else
			Utils.setStringText(tv_fostercare_chooseroom_petname, fc.petname
					+ "寄养");
		btRefresh.setVisibility(View.VISIBLE);
		// 设置地址
		String addressName = spUtil.getString("addressName", "");
		if (!"".equals(spUtil.getString("newaddr", ""))
				&& !"".equals(spUtil.getString("newlat", ""))
				&& !"".equals(spUtil.getString("newlng", ""))) {// 选择地址
			fc.addrid = spUtil.getInt("newaddrid", 0);
			fc.addr = spUtil.getString("newaddr", "");
			fc.lat = Double.parseDouble(spUtil.getString("newlat", "0"));
			fc.lng = Double.parseDouble(spUtil.getString("newlng", "0"));
		} else if (spUtil.getBoolean("isRestart", false) == false && Utils.isStrNull(addressName)) {// 用户在首页填的地址
			fc.addr = addressName + spUtil.getString("xxAddressName", "");
			List<PetAddressInfo> all = petAddressDao.getAll();
			if (all != null) {
				if (all.size() > 0) {
					PetAddressInfo petAddressInfo = all.get(0);
					fc.lat = petAddressInfo.getPet_address_lat();
					fc.lng = petAddressInfo.getPet_address_lng();
				}
			}
		} else {
			if (spUtil.getInt("addressid", 0) > 0
					&& !"".equals(spUtil.getString("address", ""))) {
				fc.addrid = spUtil.getInt("addressid", 0);
				fc.addr = spUtil.getString("address", "");
				fc.lat = Double.parseDouble(spUtil.getString("lat", "0"));
				fc.lng = Double.parseDouble(spUtil.getString("lng", "0"));
			}
		}
		dots = new ArrayList<View>();
		rmList = new ArrayList<Room>();
		vpShop.setOnPageChangeListener(new MPageChangeListener());
		rmAdapter = new RoomAdapter(this, rmList, fc);
		mRoomList.setAdapter(rmAdapter);
		rlDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_SelectTime);
				goNextForData(FostercareDateActivity.class,
						Global.FOSTERCARE_APPOINTMENT_SHOPLIST);
			}
		});
		tv_fostercarechooseroom_shoptel
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_CallStation);
						if (shoptel != null && !"".equals(shoptel)) {
							showPhoneDialog(shoptel);
						} else {
							ToastUtil.showToastShortCenter(
									FostercareChooseroomActivity.this,
									"对不起，没有获取到该店铺的手机号码");
						}
					}
				});
		ibShopNav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_StationNavigation);
				Intent intent = new Intent(FostercareChooseroomActivity.this,
						ShopLocationActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("shopname", shopName);
				intent.putExtra("shopaddr", shopAddr);
				intent.putExtra("shoplat", shopLat);
				intent.putExtra("shoplng", shopLng);
				startActivity(intent);
			}
		});
		getTime();
	}

	private void showMain(boolean flag) {
		if (flag) {
			ppl_fostercare_chooseroom.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			// pzlMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
			ppl_fostercare_chooseroom.setVisibility(View.GONE);
		}
	}

	private void showPhoneDialog(final String phone) {
		MDialog mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage("您确定要拨打电话吗？")
				.setCancelStr("否").setOKStr("是")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Global.cellPhone(FostercareChooseroomActivity.this,
								phone);
					}
				}).build();
		mDialog.show();
	}

	private class MPageChangeListener implements OnPageChangeListener {
		/**
		 * 页面状态改变执行的方法
		 * */
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 页面选中以后执行的方法
		 * */
		@Override
		public void onPageSelected(int position) {

			currentItem = position;
			// 圆点更新
			// 更新当前页面为白色的圆点
			dots.get(position % dots.size()).setBackgroundResource(
					R.drawable.dot_focused);
			// 更新上一个页面为灰色的圆点
			dots.get(oldPostion % dots.size()).setBackgroundResource(
					R.drawable.dot_normal);
			// 更新上一个页面的位置
			oldPostion = position;
		}

	}

	private void goNextForData(Class clazz, int requestcode) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("startdate", startdate);
		intent.putExtra("enddate", enddate);
		intent.putExtra("monthposition", monthposition);
		startActivityForResult(intent, requestcode);
	}

	private void getData() {
		dots.clear();
		llPoint.removeAllViews();
		rmList.clear();
		pDialog.showDialog();
		StringBuilder sbstart = new StringBuilder();
		StringBuilder sbend = new StringBuilder();
		sbstart.append(startdate);
		sbstart.append(" 00:00:00");
		sbend.append(enddate);
		sbend.append(" 00:00:00");
		int areaid = spUtil.getInt("tareaid", 0);
		CommUtil.getShopRoom(this, areaid, fc.petid, sbstart.toString(),
				sbend.toString(), fc.lat, fc.lng, dataHandler);
	}

	private void getTime() {
		CommUtil.getReadyReserve(this, defaultDataHandler);
	}

	private AsyncHttpResponseHandler defaultDataHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("寄养预约： " + new String(responseBody));
			pDialog.closeDialog();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						showMain(true);
						JSONObject jdata = jsonObject.getJSONObject("data");

						if (jdata.has("startDateTxt")
								&& !jdata.isNull("startDateTxt")) {
							startdatetxt = jdata.getString("startDateTxt");
						}
						if (jdata.has("endDateTxt")
								&& !jdata.isNull("endDateTxt")) {
							enddatetxt = jdata.getString("endDateTxt");
						}
						Utils.setStringText(tv_fostercarechooseroom_daterz,
								startdatetxt);
						Utils.setStringText(tv_fostercarechooseroom_dateld,
								enddatetxt);
						if (jdata.has("startDate")
								&& !jdata.isNull("startDate")) {
							startdate = jdata.getString("startDate");
							fc.startdate = startdate;
						}
						if (jdata.has("endDate") && !jdata.isNull("endDate")) {
							enddate = jdata.getString("endDate");
							fc.enddate = enddate;
						}
						getData();
						if (jdata.has("bannerH5Url")
								&& !jdata.isNull("bannerH5Url")) {
							bannerH5Url = jdata.getString("bannerH5Url");
						}
						if (jdata.has("totalDay") && !jdata.isNull("totalDay")) {
							daynum = jdata.getInt("totalDay");
							fc.daynum = daynum;
							Utils.setStringText(tvDaynum, "共" + daynum + "天");
						}
					}
				} else {
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(
							FostercareChooseroomActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};
	protected String desc_img;

	private AsyncHttpResponseHandler dataHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("房型： " + new String(responseBody));
			pDialog.closeDialog();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						showMain(true);
						JSONObject jdata = jsonObject.getJSONObject("data");
						if (jdata.has("shop") && !jdata.isNull("shop")) {
							JSONObject jshop = jdata.getJSONObject("shop");
							if (jshop.has("ShopId") && !jshop.isNull("ShopId")) {
								fc.shopid = jshop.getInt("ShopId");
							}
							if (jshop.has("shopName")
									&& !jshop.isNull("shopName")) {
								fc.shopname = jshop.getString("shopName");
								shopName = fc.shopname;
								tvShopName.setText(shopName);
							}
							if (jshop.has("address")
									&& !jshop.isNull("address")) {
								shopAddr = jshop.getString("address");
								tvShopAddr.setText(shopAddr);
								tvShopAddr.post(new Runnable() {
									@Override
									public void run() {
										int twidth = Utils
												.getDisplayMetrics(FostercareChooseroomActivity.this)[0]
												- Utils.dip2px(
														FostercareChooseroomActivity.this,
														100);
										if (tvShopAddr.getWidth() > twidth) {
											tvShopAddr.setWidth(twidth);
										}
									}
								});
							}
							if (jshop.has("lat") && !jshop.isNull("lat")) {
								shopLat = jshop.getDouble("lat");
							}
							if (jshop.has("lng") && !jshop.isNull("lng")) {
								shopLng = jshop.getDouble("lng");
							}
							if (jshop.has("phone") && !jshop.isNull("phone")) {
								shoptel = jshop.getString("phone");
							}
						}
						if (jdata.has("roomTypes")
								&& !jdata.isNull("roomTypes")) {
							JSONArray jroomarr = jdata
									.getJSONArray("roomTypes");
							for (int i = 0; i < jroomarr.length(); i++) {
								rmList.add(Room.json2entity(jroomarr
										.getJSONObject(i)));
							}
						}
						rmAdapter.notifyDataSetChanged();
						// 解析寄养小贴士信息
						if (jdata.has("tips") && !jdata.isNull("tips")) {
							JSONArray JSONArraytips = jdata
									.getJSONArray("tips");
							jrtips.clear();
							for (int i = 0; i < JSONArraytips.length(); i++) {
								jrtips.add(JSONArraytips.getString(i));
							}
							if (jrtips.size() > 0) {
								mlv_fostercareappointment_jyxts
										.setAdapter(new FostercareXTSAdapter<String>(
												FostercareChooseroomActivity.this,
												jrtips,0));
							}
						}
						if (jdata.has("desc_img") && !jdata.isNull("desc_img")) {
							desc_img = jdata.getString("desc_img");
							if (desc_img != null
									&& !TextUtils.isEmpty(desc_img)) {
								if (!isGoTo) {
									isGoTo = true;
									showPopPhoto();
								}
							}
						}
						// 解析中转站信息
						if (jdata.has("transfers")
								&& !jdata.isNull("transfers")) {
							JSONArray jrtransfers = jdata
									.getJSONArray("transfers");
							transfersList.clear();
							for (int i = 0; i < jrtransfers.length(); i++) {
								transfersList.add(Transfers
										.json2entity(jrtransfers
												.getJSONObject(i)));
							}
							if (transfersList.size() > 0) {
								chooseTransferAdapter.setData(transfersList);
								chooseTransferAdapter.setClickItem(0);
								setTransferInfo(transfersList.get(0));
							}
						}
						if (jdata.has("activityCopy")&& !jdata.isNull("activityCopy")) {
//							activityCopy = jdata.getString("activityCopy");
//							if (activityCopy != null&& !TextUtils.isEmpty(activityCopy)) {
//								view1.setVisibility(View.GONE);
//							} else {
//								view1.setVisibility(View.VISIBLE);
//							}
							JSONObject objectActivityCopy = jdata.getJSONObject("activityCopy");
							if (objectActivityCopy.has("txt")&&!objectActivityCopy.isNull("txt")) {
								activityCopy = objectActivityCopy.getString("txt");
								if (activityCopy != null&& !TextUtils.isEmpty(activityCopy)) {
									view1.setVisibility(View.GONE);
								} else {
									view1.setVisibility(View.VISIBLE);
								}
							}
							if (objectActivityCopy.has("url")&&!objectActivityCopy.isNull("url")) {
								BuyCardH5Url = objectActivityCopy.getString("url");
							}
							Utils.setText(tv_fostercareappointment_to_mylastmoney,activityCopy, "", View.VISIBLE, View.GONE);
						} else {
							view1.setVisibility(View.VISIBLE);
							tv_fostercareappointment_to_mylastmoney.setVisibility(View.GONE);
						}
					}
				} else {
					showMain(false);
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(
							FostercareChooseroomActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showMain(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			showMain(false);
		}
	};
	
	private void showPopPhoto() {
		pWin = null;
		if (pWin == null) {
			final View view = View.inflate(this,
					R.layout.pw_main_sendpost, null);
			ImageView iv_pw_main_sendpost = (ImageView) view
					.findViewById(R.id.iv_pw_main_sendpost);
			ImageLoaderUtil.loadImg(desc_img, iv_pw_main_sendpost, 0, null);
			ImageButton ib_pw_main_sendpost_close = (ImageButton) view
					.findViewById(R.id.ib_pw_main_sendpost_close);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(Utils.getDisplayMetrics(this)[0]);
			pWin.showAtLocation(view, Gravity.CENTER, 0, 0);
			iv_pw_main_sendpost.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
			ib_pw_main_sendpost_close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
		}
	}

	private TextView tvShopName;
	private TextView tvShopAddr;
	private ImageButton ibShopNav;
	private boolean isOpen;

	private long getTimeInMills(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.FOSTERCARE_APPOINTMENT_SHOPLIST) {// 选择时间
				startdate = data.getStringExtra("startdate");
				enddate = data.getStringExtra("enddate");
				monthposition = data.getIntExtra("monthposition", 0);
				daynum = (int) ((getTimeInMills(enddate) - getTimeInMills(startdate)) / DAYTIMEINMILLS);
				fc.startdate = startdate;
				fc.enddate = enddate;
				fc.daynum = daynum;
				rmAdapter.setFC(fc);
				String[] arr = startdate.split("-");
				StringBuilder sb = new StringBuilder();
				if (arr.length > 2) {
					sb.append(arr[1]);
					sb.append("月");
					sb.append(arr[2]);
					sb.append("日");
				}
				String[] split = enddate.split("-");
				StringBuilder sb3 = new StringBuilder();
				if (split.length > 2) {
					sb3.append(split[1]);
					sb3.append("月");
					sb3.append(split[2]);
					sb3.append("日");
				}
				StringBuilder sb2 = new StringBuilder();
				sb2.append("共");
				sb2.append(daynum);
				sb2.append("天");
				tv_fostercarechooseroom_daterz.setText(sb.toString());
				tv_fostercarechooseroom_dateld.setText(sb3.toString());
				tvDaynum.setText(sb2.toString());
				getData();
			} else if (requestCode == Global.FOSTERCARE_APPOINTMENT_CHANGEPET) {
				// 选择宠物类型返回
				fc.customerpetid = data.getIntExtra("customerpetid", 0);
				fc.petid = data.getIntExtra("petid", 0);
				fc.petkind = data.getIntExtra("petkind", 0);
				fc.petname = data.getStringExtra("petname");
				fc.customerpetname = data.getStringExtra("customerpetname");
				fc.image = data.getStringExtra("petimage");
				ImageLoaderUtil.loadImg(fc.image,
						sriv_fostercare_chooseroom_petimg,
						R.drawable.user_icon_unnet, null);
				if (fc.customerpetid > 0)
					Utils.setStringText(tv_fostercare_chooseroom_petname,
							fc.customerpetname + "寄养");
				else
					Utils.setStringText(tv_fostercare_chooseroom_petname,
							fc.petname + "寄养");
				getData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void goBack() {
		Intent data = new Intent();
		data.putExtra("startdate", startdate);
		data.putExtra("enddate", enddate);
		data.putExtra("monthposition", monthposition);
		data.putExtra("fromroom", true);
		setResult(Global.RESULT_OK, data);
		finishWithAnimation();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fostercareappointment_to_mylastmoney:
//			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_ToRecharge);
//			Intent intent = new Intent(mContext, MyLastMoney.class);
//			startActivity(intent);
			goAd(mContext, BuyCardH5Url, ADActivity.class);
			break;
		case R.id.iv_fostercare_chooseroom_back:// 返回
			goBack();
			break;
		case R.id.rl_fostercarechooseroom_jyzn:// 寄养指南
			goNext(ADActivity.class, Global.FOSTERCARE_TO_AGREEMENT);
			break;
		case R.id.rl_fostercare_chooseroom_changepet:// 选择宠物
			goNextForData(ChoosePetActivityNew.class,
					Global.FOSTERCARE_APPOINTMENT_CHANGEPET);
			break;
		case R.id.rl_fostercarechooseroom_info:
			setFz();
			break;
		default:
			break;
		}
	}

	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("customerpetid", fc.customerpetid);
		intent.putExtra("url", bannerH5Url);
		startActivity(intent);
	}

	private void setFz() {
		if (isOpen) {
			isOpen = false;
			if (activityCopy != null
					&& !TextUtils.isEmpty(activityCopy)) {
				view1.setVisibility(View.GONE);
			} else {
				view1.setVisibility(View.VISIBLE);
			}
			mlv_fostercareappointment_zzz.setVisibility(View.GONE);
			iv_fostercarechooseroom_fz
					.setBackgroundResource(R.drawable.iv_fostercarechooseroom_fz_down);
		} else {
			isOpen = true;
			view1.setVisibility(View.GONE);
			mlv_fostercareappointment_zzz.setVisibility(View.VISIBLE);
			iv_fostercarechooseroom_fz
					.setBackgroundResource(R.drawable.iv_fostercarechooseroom_fz_up);
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
