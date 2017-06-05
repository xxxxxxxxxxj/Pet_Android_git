package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AddServiceAdapter;
import com.haotang.pet.adapter.AppointmentBeauticianAdapter;
import com.haotang.pet.dao.PetAddressDao;
import com.haotang.pet.entity.AddServiceItem;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.PetAddressInfo;
import com.haotang.pet.entity.PetService;
import com.haotang.pet.entity.SingelAreaBean;
import com.haotang.pet.entity.SingelAreaBean.AreaBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.MyStyleSpan;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.SelectableRoundedImageView;

/**
 * 预约界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class AppointmentActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	public static TextView tvServiceName;
	private TextView textView_appoiment_price;
	private RelativeLayout app_choose_address;
	private RelativeLayout app_choose_time;
	private TextView tvAddr;
	private TextView tvTime;
	private TextView last_price;
	private Button app_submit;
	private CircleImageView srivBeauticianAbout;
	private SelectableRoundedImageView srivBeauticianSex;
	private RelativeLayout rlBeauticianMore;
	private LinearLayout rlBeauticianAbout;
	// private TextView tvBeauticianNull;
	private TextView tvMore;
	public static PetService ps;
	private int petid;
	private SharedPreferenceUtil spUtil;
	private int servicetype;
	private int previous;
	private ArrayMap<String, ArrayList<Beautician>> beauticianMap = new ArrayMap<String, ArrayList<Beautician>>();
	private ArrayList<Beautician> beauticianList = new ArrayList<Beautician>();
	private int addrSize = 0;
	private int addrid = 0;
	public static int addrtype = 0;
	private double lat = 0;
	private double lng = 0;
	private String addr;
	private int serviceid;
	private String time;
	private String date;
	private double pickUpPrice = 0;
	public static double lastPrice = 0;
	public static int shopid;
	private int shoptmpid;
	private String shopName;
	private String shopAddr;
	private String shopImg;
	private String shoptel;
	private String shopbgImg;
	private String areaName;
	private double addserviceprice;
	private ArrayList<Integer> addserviceIdList = new ArrayList<Integer>();
	private TextView tvBeauticianAboutName;
	private TextView tvBeauticianAboutLevel;
	public static AppointmentActivity act;
	private MProgressDialog pDialog;
	private boolean issubmit;// 是否点击提交按钮调用美容师列表判断
	private int areaid;// 上门服务商圈
	private int dayposition = -1;
	private int customerpetid;
	private String avatarPath;
	private MulPetService mpsSingle;
	private boolean isRefreshBeautician = true;// 是否刷新美容师
	private AppointmentBeauticianAdapter adapterBeautician;
	private Beautician beauticianSelected;
	private boolean isChangeShop;
	private boolean isBeauticianAppointment;
	private boolean isInArea = true;
	private String newAreaName;
	private String shopAreaName;
	private int newShopid;
	private int newAreaid;
	private int changeAreaType;
	private PopupWindow pWin;
	private LayoutInflater mInflater;
	private String[] remarks = new String[4];
	public static ArrayList<MulPetService> mulPetServiceList;
	private int OrderType = 3;// 1 到店 派单 2 到店抢单 3 上门 派单 4 上门抢单
	private ImageView iv_appoint_petimg;
	// 单项服务相关view
	private TextView tv_service_addservice;
	private MyGridView gvAddService;
	private ArrayList<AddServiceItem> addserviceList = new ArrayList<AddServiceItem>();
	private PetAddressDao petAddressDao;
	private View vw_servicedetail_beautician1;
	private View vw_servicedetail_beautician2;
	public static int clicksort;
	private AddServiceAdapter asAdapter;
	private MDialog mDialog;
	private String strListWokerId;
	// private double btcPrice;
	private LinearLayout llMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private Button btRefresh;
	private RelativeLayout rlSinglePetInfo;
	private Button rlAddPet;
	private MListview lvPetService;
	private ServicePetAdapter spAdapter;
	private ImageView ivShopbg;
	private TextView btBeauticianLevel1;
	private TextView btBeauticianLevel2;
	private TextView btBeauticianLevel3;
	private HorizontalListView gvBeaulicain;
	private Button btChangeShop;
	private RelativeLayout rl_appointment_beautician;
	private RelativeLayout rl_appointment_switchbau;
	private View vw_appoint_beautician;
	private String extraServiceFeeTag;
	private TextView tv_appointment_extrafee;
	private boolean localIsInArea;
	private int localPosition;
	private TextView tv_appointment_upgradetip;
	private TextView tv_appointment_servicecard;
	private String buyUrl;
	private int enable;
	private String title;
	private Handler tvHintHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				if (pWin != null) {
					pWin.dismiss();
					pWin = null;
				}
			} else if (msg.what == 200) {
				showAddrNoInArea();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appoinment);
		act = this;
		MApplication.listAppoint.add(this);
		mInflater = LayoutInflater.from(this);
		petAddressDao = new PetAddressDao(this);
		initView();
		setView();
	}

	private void setView() {
		String liveFlag = getIntent().getStringExtra("liveFlag");
		if (liveFlag != null && !TextUtils.isEmpty(liveFlag)) {
			if (liveFlag.equals("goneSwitchShop")) {
				btChangeShop.setVisibility(View.GONE);
			}
		}
		tvMsg1.setText("网络异常或连接服务器失败");
		btRefresh.setVisibility(View.VISIBLE);
		clicksort = getIntent().getIntExtra("clicksort", 0);
		servicetype = getIntent().getIntExtra("servicetype", 0);
		previous = getIntent().getIntExtra("previous", -1);
		extraServiceFeeTag = getIntent().getStringExtra("extraServiceFeeTag");
		Utils.setText(tv_appointment_extrafee, extraServiceFeeTag, "",
				View.VISIBLE, View.GONE);
		addrtype = getIntent().getIntExtra("serviceloc", 0);
		beauticianSelected = new Beautician();
		if (getIntent().getIntExtra("beautician_id", 0) > 0) {
			shopid = getIntent().getIntExtra("shopid", 0);
			areaid = getIntent().getIntExtra("areaid", 0);
			Log.e("TAG", "AppointmentActivity areaid = " + areaid);
			Log.e("TAG", "AppointmentActivity shopid = " + shopid);
			tvMore.setVisibility(View.VISIBLE);
			// 从美容师进入预约
			isBeauticianAppointment = true;
			beauticianSelected.id = getIntent().getIntExtra("beautician_id", 0);
			beauticianSelected.gender = getIntent().getIntExtra(
					"beautician_gender", 0);
			beauticianSelected.sort = getIntent().getIntExtra(
					"beautician_sort", 0);
			clicksort = beauticianSelected.sort;
			beauticianSelected.ordernum = getIntent().getIntExtra(
					"beautician_ordernum", 0);
			beauticianSelected.name = getIntent().getStringExtra(
					"beautician_name");
			beauticianSelected.image = getIntent().getStringExtra(
					"beautician_image");
			beauticianSelected.levelname = getIntent().getStringExtra(
					"beautician_levelname");
			beauticianSelected.upgradeTip = getIntent().getStringExtra(
					"upgradeTip");
			ps.upgradeTip = beauticianSelected.upgradeTip;
			areaName = getIntent().getStringExtra("areaname");
			if (areaName != null && !"".equals(areaName))
				if (addrtype == 2)
					tvTitle.setText(areaName + "上门服务");
			ps.beautician_id = beauticianSelected.id;
			ps.beautician_sex = beauticianSelected.gender;
			ps.beautician_sort = beauticianSelected.sort;
			ps.beautician_ordernum = beauticianSelected.ordernum;
			ps.beautician_image = beauticianSelected.image;
			ps.beautician_name = beauticianSelected.name;
			ps.beautician_levelname = beauticianSelected.levelname;
			ps.beautician_storeid = shopid;
			ps.beautician_isavail = beauticianSelected.isAvail;
			if (beauticianSelected.sort == 10) {
				btBeauticianLevel1.setText(beauticianSelected.levelname);
				btBeauticianLevel1.setTextColor(getResources().getColor(
						R.color.aE33A4A));
			} else if (beauticianSelected.sort == 20) {
				btBeauticianLevel2.setText(beauticianSelected.levelname);
				btBeauticianLevel2.setTextColor(getResources().getColor(
						R.color.aE33A4A));
			} else if (beauticianSelected.sort == 30) {
				btBeauticianLevel3.setText(beauticianSelected.levelname);
				btBeauticianLevel3.setTextColor(getResources().getColor(
						R.color.aE33A4A));
			}
			showBeauticianList(1);
			if (ps.beautician_name != null)
				tvBeauticianAboutName.setText(ps.beautician_name);
			tvBeauticianAboutLevel.setText("接单：" + ps.beautician_ordernum);
			Utils.setText(tv_appointment_upgradetip, ps.upgradeTip, "",
					View.VISIBLE, View.GONE);
			ImageLoaderUtil.loadImg(ps.beautician_image, srivBeauticianAbout,
					0, null);
			if (ps.beautician_sex == 1)
				srivBeauticianSex.setImageResource(R.drawable.icon_man);
			else
				srivBeauticianSex.setImageResource(R.drawable.icon_women);
		} else {
			if (spUtil.getInt("tshopid", 0) > 0) {
				shopid = spUtil.getInt("tshopid", -1);
			} else {
				shopid = getIntent().getIntExtra("shopid", 0);
			}
			if (spUtil.getInt("tareaid", 0) == 100) {
				areaid = getIntent().getIntExtra("areaid", 0);
			} else {
				areaid = spUtil.getInt("tareaid", -1);
			}
			if (!"".equals(spUtil.getString("tareaname", "")))
				areaName = spUtil.getString("tareaname", "");
			if (areaName != null && !"".equals(areaName)) {
				if (addrtype == 2)
					tvTitle.setText(areaName + "上门服务");
			}
		}
		if (addrtype == 1) {
			ivShopbg.setBackgroundResource(R.drawable.ddfw);
			btChangeShop.setText("切换门店");
		} else {
			ivShopbg.setBackgroundResource(R.drawable.smfw);
			btChangeShop.setText("切换区域");
		}
		// 设置地址
		String addressName = spUtil.getString("addressName", "");
		/*
		 * if (!"".equals(spUtil.getString("newaddr", "")) &&
		 * !"".equals(spUtil.getString("newlat", "")) &&
		 * !"".equals(spUtil.getString("newlng", ""))) {// 选择地址 addrid =
		 * spUtil.getInt("newaddrid", 0); ps.addr_id = addrid; ps.addr_detail =
		 * spUtil.getString("newaddr", ""); ps.addr_lat =
		 * Double.parseDouble(spUtil.getString("newlat", "0")); ps.addr_lng =
		 * Double.parseDouble(spUtil.getString("newlng", "0")); addr =
		 * ps.addr_detail; lat = ps.addr_lat; lng = ps.addr_lng; } else
		 */if (spUtil.getBoolean("isRestart", false) == false
				&& Utils.isStrNull(addressName)) {// 用户在首页填的地址
			ps.addr_detail = addressName
					+ spUtil.getString("xxAddressName", "");
			addr = ps.addr_detail;
			List<PetAddressInfo> all = petAddressDao.getAll();
			if (all != null) {
				if (all.size() > 0) {
					PetAddressInfo petAddressInfo = all.get(0);
					lat = petAddressInfo.getPet_address_lat();
					lng = petAddressInfo.getPet_address_lng();
					ps.addr_lat = lat;
					ps.addr_lng = lng;
				}
			}
		} else {
			if (spUtil.getInt("addressid", 0) > 0
					&& !"".equals(spUtil.getString("address", ""))) {// 用户上一单的地址
				addrid = spUtil.getInt("addressid", 0);
				ps.addr_id = addrid;
				ps.addr_detail = spUtil.getString("address", "");
				ps.addr_lat = Double.parseDouble(spUtil.getString("lat", "0"));
				ps.addr_lng = Double.parseDouble(spUtil.getString("lng", "0"));
				addr = ps.addr_detail;
				lat = ps.addr_lat;
				lng = ps.addr_lng;
			}
		}
		tvAddr.setText(addr);
		ps.addrtype = addrtype;
		mulPetServiceList = new ArrayList<MulPetService>();
		mpsSingle = new MulPetService();
		serviceid = getIntent().getIntExtra("serviceid", 0);
		customerpetid = getIntent().getIntExtra("customerpetid", 0);
		avatarPath = getIntent().getStringExtra("avatarPath");
		if (spUtil.getInt("charactservice", 0) != 0) {
			// 特色服务
			rlAddPet.setVisibility(View.GONE);
		} else {
			rlAddPet.setVisibility(View.VISIBLE);
		}
		petid = getIntent().getIntExtra("petid", 0);
		mpsSingle.petName = getIntent().getStringExtra("petname");
		mpsSingle.petId = petid;
		mpsSingle.petCustomerId = customerpetid;
		mpsSingle.petKind = getIntent().getIntExtra("petkind", 0);
		mpsSingle.serviceId = serviceid;
		mpsSingle.serviceType = servicetype;
		mpsSingle.petCustomerName = getIntent().getStringExtra(
				"customerpetname");
		mpsSingle.petimage = avatarPath;// 这里这么搞不知道是否会对我的宠物详情页跳过来的数据有影响
		mpsSingle.serviceloc = addrtype;
		mulPetServiceList.add(mpsSingle);
		showSinglePet(true);
		getMulPetServiceData(addrtype, shopid, date, lat, lng);
		app_choose_address.setOnClickListener(this);
		app_choose_time.setOnClickListener(this);
		app_submit.setOnClickListener(this);
		rlBeauticianAbout.setOnClickListener(this);
		rlBeauticianMore.setOnClickListener(this);
		ibBack.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		rlAddPet.setOnClickListener(this);
		ivShopbg.setOnClickListener(this);
		btBeauticianLevel1.setOnClickListener(this);
		btBeauticianLevel2.setOnClickListener(this);
		btBeauticianLevel3.setOnClickListener(this);
		btChangeShop.setOnClickListener(this);
		tv_appointment_servicecard.setOnClickListener(this);
		gvAddService.setSelector(new ColorDrawable(Color.TRANSPARENT));
		asAdapter = new AddServiceAdapter(this, addserviceList);
		gvAddService.setAdapter(asAdapter);
		gvAddService.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pisition, long id) {
				int selectedIndex = isItemSelected(addserviceList.get(pisition));
				if (addserviceList.get(pisition).isChecked) {
					addserviceList.get(pisition).isChecked = false;
					addserviceprice -= addserviceList.get(pisition).price;
					addserviceIdList.remove(selectedIndex);
					lastPrice -= addserviceList.get(pisition).price;
				} else {
					addserviceList.get(pisition).isChecked = true;
					addserviceprice += addserviceList.get(pisition).price;
					addserviceIdList.add(addserviceList.get(pisition).id);
					lastPrice += addserviceList.get(pisition).price;
				}
				asAdapter.notifyDataSetChanged();
				setLastPrice();
				setAppointPrice(Utils.formatDouble(lastPrice));
				mulPetServiceList.get(0).addServiceIds = getAddServiceIds();
				mulPetServiceList.get(0).addservicefee = addserviceprice;
				mulPetServiceList.get(0).fee = lastPrice;
			}
		});
		spAdapter = new ServicePetAdapter();
		lvPetService.setAdapter(spAdapter);
		adapterBeautician = new AppointmentBeauticianAdapter(this,
				beauticianList);
		gvBeaulicain.setAdapter(adapterBeautician);
		gvBeaulicain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Beautician bt = (Beautician) adapterBeautician
						.getItem(position);
				Intent intent = new Intent(act, BeauticianDetailActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				act.getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("beautician_id", bt.id);
				intent.putExtra("isavail", bt.isAvail);
				intent.putExtra("serviceid", serviceid);
				intent.putExtra("shopid", shopid);
				intent.putExtra("areaid", areaid);
				intent.putExtra("serviceloc", addrtype);
				intent.putExtra("position", dayposition);
				intent.putExtra("lng", lng);
				intent.putExtra("lat", lat);
				intent.putExtra("apptime", date);
				intent.putExtra("strp", getPetID_ServiceId_ItemIds());
				act.startActivityForResult(intent,
						Global.SERVICEREQUESTCODE_BEAUTICIAN);
			}
		});
		if (addrtype == 2 && tvAddr != null
				&& !"".equals(tvAddr.getText().toString().trim())) {
			isServiceInTheArea(lat, lng);// 3.第三个请求接口:
		}
	}

	private void initView() {
		pDialog = new MProgressDialog(this);
		ps = new PetService();
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		btChangeShop = (Button) findViewById(R.id.bt_titlebar_other);
		gvAddService = (MyGridView) findViewById(R.id.gv_service_addservice);
		tv_service_addservice = (TextView) findViewById(R.id.tv_service_addservice);
		srivBeauticianAbout = (CircleImageView) findViewById(R.id.civ_servicedetail_beautician_about);
		srivBeauticianSex = (SelectableRoundedImageView) findViewById(R.id.sriv_servicedetail_beautician_sex);
		btBeauticianLevel1 = (TextView) findViewById(R.id.tv_servicedetail_beautician_level1);
		btBeauticianLevel2 = (TextView) findViewById(R.id.tv_servicedetail_beautician_level2);
		btBeauticianLevel3 = (TextView) findViewById(R.id.tv_servicedetail_beautician_level3);
		tvBeauticianAboutName = (TextView) findViewById(R.id.tv_servicedetail_beauticianname_about);
		tvBeauticianAboutLevel = (TextView) findViewById(R.id.tv_servicedetail_beauticianlevel_about);
		tvMore = (TextView) findViewById(R.id.tv_servicedetail_beautician_more);
		rlBeauticianMore = (RelativeLayout) findViewById(R.id.rl_servicedetail_beautician_more);
		rlBeauticianAbout = (LinearLayout) findViewById(R.id.ll_servicedetail_beautician_about);
		gvBeaulicain = (HorizontalListView) findViewById(R.id.hlv_appointment_beautician);
		tvServiceName = (TextView) findViewById(R.id.textView_service_name);
		textView_appoiment_price = (TextView) findViewById(R.id.textView_appoiment_price);
		app_choose_address = (RelativeLayout) findViewById(R.id.app_choose_address);
		app_choose_time = (RelativeLayout) findViewById(R.id.app_choose_time);
		ivShopbg = (ImageView) findViewById(R.id.iv_service_shop_bg);
		iv_appoint_petimg = (ImageView) findViewById(R.id.iv_appoint_petimg);
		ImageLoaderUtil.loadImg(spUtil.getString("petimg", ""),
				iv_appoint_petimg, R.drawable.icon_production_default, null);
		int mWidth = Utils.getDisplayMetrics(this)[0];
		LayoutParams layoutParams = ivShopbg.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = mWidth * 5 / 16;
		ivShopbg.setLayoutParams(layoutParams);
		tvAddr = (TextView) findViewById(R.id.textView_pet_address_name);
		tvTime = (TextView) findViewById(R.id.textView_pet_time_show);
		last_price = (TextView) findViewById(R.id.last_price);
		app_submit = (Button) findViewById(R.id.bt_appoinment_submit);
		llMain = (LinearLayout) findViewById(R.id.ll_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		rlSinglePetInfo = (RelativeLayout) findViewById(R.id.rl_service_singlepetinfo);
		rlAddPet = (Button) findViewById(R.id.btn_service_addpet);
		lvPetService = (MListview) findViewById(R.id.lv_service_pets);
		vw_servicedetail_beautician1 = (View) findViewById(R.id.vw_servicedetail_beautician1);
		vw_servicedetail_beautician2 = (View) findViewById(R.id.vw_servicedetail_beautician2);
		rl_appointment_beautician = (RelativeLayout) findViewById(R.id.rl_appointment_beautician);
		rl_appointment_switchbau = (RelativeLayout) findViewById(R.id.rl_appointment_switchbau);
		vw_appoint_beautician = (View) findViewById(R.id.vw_appoint_beautician);
		tv_appointment_extrafee = (TextView) findViewById(R.id.tv_appointment_extrafee);
		tv_appointment_upgradetip = (TextView) findViewById(R.id.tv_appointment_upgradetip);
		tv_appointment_servicecard = (TextView) findViewById(R.id.tv_appointment_servicecard);
		getServiceAddrList();
	}

	private void showMain(boolean flag) {
		if (flag) {
			llMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			llMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void showSinglePet(boolean flag) {
		if (flag) {
			gvAddService.setVisibility(View.VISIBLE);
			rlSinglePetInfo.setVisibility(View.VISIBLE);
			lvPetService.setVisibility(View.GONE);
		} else {
			tv_service_addservice.setVisibility(View.GONE);
			gvAddService.setVisibility(View.GONE);
			rlSinglePetInfo.setVisibility(View.GONE);
			lvPetService.setVisibility(View.VISIBLE);
		}
	}

	private void showDeletePet(String hint, final int position) {
		mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage(hint)
				.setCancelTextColor(
						getResources().getColor(R.color.orange_light))
				.setCancelStr("取消").setOKStr("确定")
				.positiveListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (position > -1
								&& position < mulPetServiceList.size()) {
							mulPetServiceList.remove(position);
							if (mulPetServiceList.size() <= 1) {
								showSinglePet(true);
								addserviceIdList.clear();
								String addserviceidsstr = mulPetServiceList
										.get(0).addServiceIds;
								lastPrice = mulPetServiceList.get(0).fee;
								if (addserviceidsstr != null
										&& !"".equals(addserviceidsstr.trim())) {
									String[] addserviceids = addserviceidsstr
											.split(",");
									for (int i = 0; i < addserviceids.length; i++) {
										addserviceIdList.add(Integer
												.parseInt(addserviceids[i]));
									}
								}
								ps.ispickup = false;
								addserviceprice = 0;
								if (tvAddr.getText().toString() != null
										&& !"".equals(tvAddr.getText()
												.toString().trim())
										&& tvTime.getText().toString() != null
										&& !"".equals(tvTime.getText()
												.toString().trim())
										&& time != null
										&& !"".equals(time.trim())
										&& date != null
										&& !"".equals(date.trim())) {
									isRefreshBeautician = false;
								}
								getMulPetServiceData(addrtype, shopid, date,
										lat, lng);
							} else {
								if (mulPetServiceList.size() >= 5) {
									spAdapter.setIsFull(true);
									rlAddPet.setVisibility(View.GONE);
								} else {
									rlAddPet.setVisibility(View.VISIBLE);
									spAdapter.setIsFull(false);
								}
								spAdapter.notifyDataSetChanged();
								showSinglePet(false);
								ps.ispickup = false;
								lastPrice = 0;
								for (int i = 0; i < mulPetServiceList.size(); i++) {
									if (ps.beautician_sort == 10) {
										mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
												.get(i).pricelevel1;

									} else if (ps.beautician_sort == 20) {
										mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
												.get(i).pricelevel2;
									} else {
										mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
												.get(i).pricelevel3;
									}
									mulPetServiceList.get(i).fee = mulPetServiceList
											.get(i).basefeewithbeautician
											+ mulPetServiceList.get(i).addservicefee;
									lastPrice += mulPetServiceList.get(i).fee;
								}
								spAdapter.notifyDataSetChanged();
								setLastPrice();
								setAppointPrice(Utils.formatDouble(lastPrice));
							}
						}
					}
				}).build();
		mDialog.show();
	}

	protected void setAppointPrice(int price) {
		String text = "¥ " + price;
		SpannableString ss = new SpannableString(text);
		ss.setSpan(new TextAppearanceSpan(this, R.style.swimdetailshowtext0),
				1, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		textView_appoiment_price.setText(ss);
	}

	private void getMulPetServiceData(int serviceloc, int shopid, String time,
			double lat, double lng) {
		pDialog.showDialog();
		isChangeShop = false;
		lastPrice = 0;
		addserviceprice = 0;
		CommUtil.getPetServicedetailV3(this, shopid, areaid, serviceloc, time,
				lat, lng,
				getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds(),
				mulPetServiceHanler);
	}

	private AsyncHttpResponseHandler mulPetServiceHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			getBeauticianListData();// 1
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
					showMain(true);
					JSONObject jData = jobj.getJSONObject("data");
					if (jData.has("cardInterface")
							&& !jData.isNull("cardInterface")) {
						JSONObject objectcardInterface = jData
								.getJSONObject("cardInterface");
						if (objectcardInterface.has("enable")
								&& !objectcardInterface.isNull("enable")) {
							enable = objectcardInterface.getInt("enable");
						}
						if (objectcardInterface.has("title")
								&& !objectcardInterface.isNull("title")) {
							title = objectcardInterface.getString("title");
						}
						if (objectcardInterface.has("url")
								&& !objectcardInterface.isNull("url")) {
							buyUrl = objectcardInterface.getString("url");
						}
					}
					if (enable == 1) {
						Utils.setText(tv_appointment_servicecard, title, "",
								View.VISIBLE, View.GONE);
						tv_appointment_servicecard.setVisibility(View.VISIBLE);
					} else {
						tv_appointment_servicecard.setVisibility(View.GONE);
					}
					if (jData.has("vieEnabled") && !jData.isNull("vieEnabled")) {
						ps.vieEnabled = jData.getInt("vieEnabled");
					}
					if (jData.has("pickUpPrice")
							&& !jData.isNull("pickUpPrice")) {
						pickUpPrice = Utils.formatDouble(jData
								.getDouble("pickUpPrice"));
						ps.pickupprice = pickUpPrice;
					} else {
						ps.pickupprice = 0;
					}
					if (jData.has("showPickUpOpt2")
							&& !jData.isNull("showPickUpOpt2")) {
						ps.pickupopt = jData.getString("showPickUpOpt2");
					} else {
						ps.pickupopt = "0";
					}
					if (addrtype == 2 && jData.has("homeServiceBanner")
							&& !jData.isNull("homeServiceBanner")) {
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()
								+ jData.getString("homeServiceBanner"),
								ivShopbg, 0, null);
					}
					if (jData.has("shop") && !jData.isNull("shop")) {
						JSONObject object = jData.getJSONObject("shop");
						if (object.has("address") && !object.isNull("address")) {
							shopAddr = object.getString("address");
						}
						if (object.has("shopName")
								&& !object.isNull("shopName")) {
							shopName = object.getString("shopName");
						}
						if (object.has("img") && !object.isNull("img")) {
							shopImg = CommUtil.getServiceNobacklash()
									+ object.getString("img");
						}
						if (object.has("phone") && !object.isNull("phone")) {
							shoptel = object.getString("phone");
						}
						if (addrtype == 1 && object.has("area")
								&& !object.isNull("area")) {
							JSONObject jare = object.getJSONObject("area");
							if (jare.has("name") && !jare.isNull("name")) {
								shopAreaName = jare.getString("name");
							}
						}
						if (addrtype == 1 && object.has("shopBanner")
								&& !object.isNull("shopBanner")) {
							shopbgImg = CommUtil.getServiceNobacklash()
									+ object.getString("shopBanner");
						}
						if (object.has("ShopId") && !object.isNull("ShopId")) {
							shoptmpid = object.getInt("ShopId");
							if (shopid <= 0 || shopid == shoptmpid
									|| addrtype == 2) {
								// shopid = shoptmpid;
								ps.petstoreid = shoptmpid;
								ps.petstoreAddr = shopAddr;
								ps.petstoreName = shopName;
								ps.petstoreImg = shopImg;
								ps.petstorePhone = shoptel;
								if (addrtype == 1)
									tvTitle.setText(shopName);
							} else {
								// 店铺不是推荐店铺
								isChangeShop = true;
								showBackDialog(2, "阿宠发现有距您更近的门店，切换到该门店？");
							}
						}
					} else {
						// 不支持到店
						shopid = 0;
					}
					if (jData.has("prices") && !jData.isNull("prices")) {
						JSONArray jparr = jData.getJSONArray("prices");
						if (jparr.length() == 1) {
							// 单个宠物
							JSONObject jp = jparr.getJSONObject(0);
							if (jp.has("petServicePojo")
									&& !jp.isNull("petServicePojo")) {
								JSONObject js = jp
										.getJSONObject("petServicePojo");
								if (js.has("name") && !js.isNull("name")) {
									mulPetServiceList.get(0).serviceName = js
											.getString("name");
									if (mulPetServiceList.get(0).petCustomerId > 0) {
										tvServiceName.setText(mulPetServiceList
												.get(0).petCustomerName
												+ "专属"
												+ js.getString("name"));
									} else {
										tvServiceName.setText(mulPetServiceList
												.get(0).petName
												+ js.getString("name"));
									}
								}
								if (js.has("extraServiceItems")
										&& !js.isNull("extraServiceItems")) {
									JSONArray jarr = js
											.getJSONArray("extraServiceItems");
									if (addserviceList.size() > 0) {
										addserviceList.clear();
									}
									for (int i = 0; i < jarr.length(); i++) {
										addserviceList.add(AddServiceItem
												.json2Entity(jarr
														.getJSONObject(i)));
									}
									for (int i = 0; i < addserviceIdList.size(); i++) {
										for (int j = 0; i < addserviceList
												.size(); j++) {
											if (addserviceIdList.get(i) == addserviceList
													.get(j).id) {
												addserviceList.get(j).isChecked = true;
												addserviceprice += addserviceList
														.get(j).price;
												break;
											}
										}
									}
									if (addserviceList.size() > 0) {
										gvAddService
												.setVisibility(View.VISIBLE);
										tv_service_addservice
												.setVisibility(View.VISIBLE);
									}
									asAdapter.notifyDataSetChanged();
									mulPetServiceList.get(0).addservicefee = addserviceprice;
									mulPetServiceList.get(0).addServiceIds = getAddServiceIds();
								} else {
									addserviceIdList.clear();
									gvAddService.setVisibility(View.GONE);
									tv_service_addservice
											.setVisibility(View.GONE);
								}
							}
							if (addrtype == 1) {
								if (jp.has("shopPrice10")
										&& !jp.isNull("shopPrice10")) {
									mulPetServiceList.get(0).pricelevel1 = jp
											.getDouble("shopPrice10");
								}
								if (jp.has("shopPrice20")
										&& !jp.isNull("shopPrice20")) {
									mulPetServiceList.get(0).pricelevel2 = jp
											.getDouble("shopPrice20");
								}
								if (jp.has("shopPrice30")
										&& !jp.isNull("shopPrice30")) {
									mulPetServiceList.get(0).pricelevel3 = jp
											.getDouble("shopPrice30");
								}
								mulPetServiceList.get(0).fee = mulPetServiceList
										.get(0).pricelevel1
										+ mulPetServiceList.get(0).addservicefee;
							} else {
								if (jp.has("price10") && !jp.isNull("price10")) {
									mulPetServiceList.get(0).pricelevel1 = jp
											.getDouble("price10");
								}
								if (jp.has("price20") && !jp.isNull("price20")) {
									mulPetServiceList.get(0).pricelevel2 = jp
											.getDouble("price20");
								}
								if (jp.has("price30") && !jp.isNull("price30")) {
									mulPetServiceList.get(0).pricelevel3 = jp
											.getDouble("price30");
								}
								mulPetServiceList.get(0).fee = mulPetServiceList
										.get(0).pricelevel1
										+ mulPetServiceList.get(0).addservicefee;
							}
							lastPrice = mulPetServiceList.get(0).fee;
							setAppointPrice(Utils.formatDouble(lastPrice));
						} else if (jparr.length() > 1) {
							// 多个宠物
							if (jparr.length() == mulPetServiceList.size()) {
								for (int i = 0; i < jparr.length(); i++) {
									JSONObject jp = jparr.getJSONObject(i);
									if (addrtype == 1) {
										if (jp.has("shopPrice10")
												&& !jp.isNull("shopPrice10")) {
											mulPetServiceList.get(i).pricelevel1 = jp
													.getDouble("shopPrice10");
										}
										if (jp.has("shopPrice20")
												&& !jp.isNull("shopPrice20")) {
											mulPetServiceList.get(i).pricelevel2 = jp
													.getDouble("shopPrice20");
										}
										if (jp.has("shopPrice30")
												&& !jp.isNull("shopPrice30")) {
											mulPetServiceList.get(i).pricelevel3 = jp
													.getDouble("shopPrice30");
										}
										mulPetServiceList.get(i).fee = mulPetServiceList
												.get(i).pricelevel1
												+ mulPetServiceList.get(i).addservicefee;
									} else {
										if (jp.has("price10")
												&& !jp.isNull("price10")) {
											mulPetServiceList.get(i).pricelevel1 = jp
													.getDouble("price10");
										}
										if (jp.has("price20")
												&& !jp.isNull("price20")) {
											mulPetServiceList.get(i).pricelevel2 = jp
													.getDouble("price20");
										}
										if (jp.has("price30")
												&& !jp.isNull("price30")) {
											mulPetServiceList.get(i).pricelevel3 = jp
													.getDouble("price30");
										}
										mulPetServiceList.get(i).fee = mulPetServiceList
												.get(i).pricelevel1
												+ mulPetServiceList.get(i).addservicefee;
									}
									lastPrice += mulPetServiceList.get(i).fee;
								}
								spAdapter.notifyDataSetChanged();
							} else {
								showMain(false);
								return;
							}
						} else {
							showMain(false);
							if (jobj.has("msg") && !jobj.isNull("msg"))
								ToastUtil.showToastShort(
										AppointmentActivity.this,
										jobj.getString("msg"));
							return;
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(AppointmentActivity.this,
								jobj.getString("msg"));
				}
				pDialog.closeDialog();
			} catch (Exception e) {
				e.printStackTrace();
				showMain(false);
				pDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			getBeauticianListData();// 1
			showMain(false);
			pDialog.closeDialog();
			ToastUtil.showToastShort(AppointmentActivity.this, "网络异常，请刷新！");
		}
	};

	private boolean isSelect(ArrayList<Beautician> beauticianList) {
		boolean bool = false;
		if (beauticianList != null && beauticianList.size() > 0) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	private void selectData() {
		ArrayList<Beautician> btc = beauticianMap.get("10");
		ArrayList<Beautician> btc1 = beauticianMap.get("20");
		ArrayList<Beautician> btc2 = beauticianMap.get("30");
		if (btc != null) {
			Log.e("TAG", "btc.size() = " + btc.size());
		} else {
			Log.e("TAG", "btc = null");
		}
		if (btc1 != null) {
			Log.e("TAG", "btc1.size() = " + btc1.size());
		} else {
			Log.e("TAG", "btc1 = null");
		}
		if (btc2 != null) {
			Log.e("TAG", "btc2.size() = " + btc2.size());
		} else {
			Log.e("TAG", "btc2 = null");
		}
		if (isSelect(btc)) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel1.setEnabled(true);
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel1.setEnabled(false);
		}
		if (isSelect(btc1)) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel2.setEnabled(true);
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel2.setEnabled(false);
		}
		if (isSelect(btc2)) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel3.setEnabled(true);
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel3.setEnabled(false);
		}
		if (clicksort > 0) {
			switch (clicksort) {
			case 10:
				if (isSelect(btc)) {// 中级可约
					clickLevel1(btc);
				} else {// 中级不可约
					if (isSelect(btc1)) {// 高级可约
						clickLevel2(btc1);
					} else {// 高级不可约
						if (isSelect(btc2)) {// 首席可约
							clickLevel3(btc2);
						}
					}
				}
				break;
			case 20:
				if (isSelect(btc1)) {// 高级可约
					clickLevel2(btc1);
				} else {// 高级不可约
					if (isSelect(btc)) {// 中级可约
						clickLevel1(btc);
					} else {// 中级不可约
						if (isSelect(btc2)) {// 首席可约
							clickLevel3(btc2);
						}
					}
				}
				break;
			case 30:
				if (isSelect(btc2)) {// 首席可约
					clickLevel3(btc2);
				} else {// 首席不可约
					if (isSelect(btc)) {// 中级可约
						clickLevel1(btc);
					} else {// 中级不可约
						if (isSelect(btc1)) {// 高级可约
							clickLevel2(btc1);
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private void getBeauticianListData() {
		if (time != null && !TextUtils.isEmpty(time)) {
			queryAvailableWorkers(areaid, date, ps.addrtype, shopid,
					getPetID_ServiceId_ItemIds());
		} else {
			getBeauticianListNoTimeLimt(areaid, ps.addrtype, shopid,
					getPetID_ServiceId_ItemIds());
		}
	}

	private void setBeautician(Beautician btc) {
		if (btc == null)
			return;
		ps.beautician_id = btc.id;
		ps.beautician_sex = btc.gender;
		ps.beautician_name = btc.name;
		ps.beautician_image = btc.image;
		ps.beautician_levels = btc.levels;
		ps.beautician_ordernum = btc.ordernum;
		ps.beautician_sign = btc.sign;
		ps.beautician_stars = btc.stars;
		ps.beautician_storeid = btc.storeid;
		ps.beautician_titlelevel = btc.titlelevel;
		ps.beautician_levelname = btc.levelname;
		ps.beautician_sort = btc.sort;
		ps.beautician_isavail = btc.isAvail;
		ps.upgradeTip = btc.upgradeTip;
		lastPrice = btc.fee;
		if (ps.beautician_name != null)
			tvBeauticianAboutName.setText(ps.beautician_name);
		tvBeauticianAboutLevel.setText("接单：" + ps.beautician_ordernum);
		Utils.setText(tv_appointment_upgradetip, ps.upgradeTip, "",
				View.VISIBLE, View.GONE);
		ImageLoaderUtil.loadImg(ps.beautician_image, srivBeauticianAbout, 0,
				null);
		if (ps.beautician_sex == 1)
			srivBeauticianSex.setImageResource(R.drawable.icon_man);
		else
			srivBeauticianSex.setImageResource(R.drawable.icon_women);
	}

	/**
	 * 
	 * @param flag
	 *            1：显示单个美容师信息，2：美容师数组，3：没有美容师，显示提示
	 */
	private void showBeauticianList(int flag) {
		if (flag == 1) {
			rlBeauticianAbout.setVisibility(View.VISIBLE);
			tvMore.setVisibility(View.VISIBLE);
			gvBeaulicain.setVisibility(View.GONE);
			rlBeauticianMore.setVisibility(View.VISIBLE);
			tvMore.setText("更换");
		} else if (flag == 2) {
			rlBeauticianAbout.setVisibility(View.GONE);
			tvMore.setVisibility(View.GONE);
			gvBeaulicain.setVisibility(View.VISIBLE);
			rlBeauticianMore.setVisibility(View.VISIBLE);
			tvMore.setText("更多");
		} else {
			rlBeauticianAbout.setVisibility(View.GONE);
			tvMore.setVisibility(View.GONE);
			gvBeaulicain.setVisibility(View.GONE);
			rlBeauticianMore.setVisibility(View.GONE);
			tvMore.setText("更多");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_appointment_servicecard:
			if (buyUrl != null && !TextUtils.isEmpty(buyUrl)) {
				startActivity(new Intent(mContext, ADActivity.class).putExtra(
						"url", buyUrl).putExtra("previous",
						Global.ORDERDETAIL_TO_BUY_CARD));
			}
			break;
		case R.id.ib_titlebar_back:
			goBack();
			break;
		case R.id.bt_null_refresh:
			getServiceAddrList();
			getMulPetServiceData(addrtype, 0, date, lat, lng);
			break;
		case R.id.btn_service_addpet:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_AddPet);
			// 添加宠物
			goNextForData(ChoosePetActivityNew.class,
					Global.APPOINTMENT_TO_ADDPET, Global.APPOINTMENT_TO_ADDPET);
			break;
		case R.id.app_choose_address:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SelectPetAddress);
			// 选择地址
			if (spUtil.getInt("userid", 0) > 0
					&& !"".equals(spUtil.getString("cellphone", ""))
					&& addrSize > 0) {
				goNextForData(CommonAddressActivity.class,
						Global.BOOKINGSERVICEREQUESTCODE_ADDR,
						Global.BOOKINGSERVICEREQUESTCODE_ADDR);
			} else {
				goNextForData(AddServiceAddressActivity.class,
						Global.BOOKINGSERVICEREQUESTCODE_ADDR,
						Global.PRE_SERVICEACTIVITY_TO_ADDSERICEADDRRESSACTIVITY);
			}
			break;
		case R.id.app_choose_time:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SelectTime);
			// 选择时间
			goNextForTimeData(SelectTimeOrUrgentActivity.class,
					Global.BOOKINGSERVICEREQUESTCODE_TIME, serviceid, lat, lng,
					"", addrtype);
			break;
		case R.id.iv_service_shop_bg:
			if (addrtype == 1) {
				// 去店铺详情
				goNext(GoShopDetailActivity.class, serviceid);
			} else {
				goNext(GoHomeDetailActivity.class, serviceid);
			}
			break;
		case R.id.bt_titlebar_other:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SwitchShop);
			// 去店铺列表
			if (addrtype == 1) {
				goNextForAreaData(ShopListActivity.class,
						Global.PRE_SERVICEDETAIL_TO_SHOPLIST, serviceid, lat,
						lng, "", 0);
			} else {
				goNextForAreaData(SelectServiceAreaActivity.class,
						Global.APPOINTMENT_TO_AREA, serviceid, lat, lng, "", 0);
			}
			break;
		case R.id.rl_servicedetail_beautician_more:
			// 选择美容师
			BeauticianListActivity.setData(workersData, date);
			goNextForTimeData(BeauticianListActivity.class,
					Global.SERVICEREQUESTCODE_BEAUTICIAN, serviceid, lat, lng,
					date, ps.addrtype);
			break;
		case R.id.ll_servicedetail_beautician_about:
			// 进入美容师详情
			goNextForTimeData(BeauticianDetailActivity.class,
					Global.SERVICEREQUESTCODE_BEAUTICIAN, serviceid, lat, lng,
					date, ps.addrtype);
			break;
		case R.id.tv_servicedetail_beautician_level1:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SwitchBeautician);
			clickLevel1(beauticianMap.get("10"));
			break;
		case R.id.tv_servicedetail_beautician_level2:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SwitchBeautician);
			clickLevel2(beauticianMap.get("20"));
			break;
		case R.id.tv_servicedetail_beautician_level3:
			UmengStatistics.UmengEventStatistics(mContext,
					Global.UmengEventID.click_SwitchBeautician);
			clickLevel3(beauticianMap.get("30"));
			break;
		case R.id.bt_appoinment_submit:
			// 提交订单
			if (lat <= 0 || lng <= 0
					|| "".equals(tvAddr.getText().toString().trim())) {
				ToastUtil.showToastShort(AppointmentActivity.this, "请填写您的宠物地址");
			} else if (tvTime.getText().toString().trim() == null
					|| "".equals(tvTime.getText().toString().trim())) {
				ToastUtil.showToastShort(AppointmentActivity.this, "请选择您的预约时间");
			} else if (ps.beautician_id <= 0) {
				ToastUtil.showToastShort(AppointmentActivity.this,
						"这个区域没有美容师可选哦");
			} else if (ps.beautician_isavail == 0) {
				ToastUtil.showToastShort(AppointmentActivity.this,
						"该美容师时间已经被约满，换个时间或地址试试吧！");
			} else if (!isInArea) {
				if (changeAreaType == 1) {
					showAddrNoInArea();
				} else if (changeAreaType == 2) {
					showAddrNoInAreaDialog(2, newAreaid, "您的地址属于" + newAreaName
							+ "，是否切换到该区域？");
				}
			} else {
				issubmit = true;
				queryAvailableWorkers(areaid, date, ps.addrtype, shopid,
						getPetID_ServiceId_ItemIds());
			}
			break;
		}
	}

	private void clickLevel3(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 30;
		if (btBeauticianLevel1.isEnabled()) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel2.isEnabled()) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (beauticianSelected != null && beauticianSelected.sort == 30) {
			showBeauticianList(1);
			setBeautician(beauticianSelected);
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.aE33A4A));
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.aE33A4A));
			showBeauticianList(2);
			adapterBeautician.setList(btc);
			setBeautician(btc.get(0));
		}
		lastPrice = 0;
		for (int i = 0; i < mulPetServiceList.size(); i++) {
			mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
					.get(i).pricelevel3;
			mulPetServiceList.get(i).fee = mulPetServiceList.get(i).basefeewithbeautician
					+ mulPetServiceList.get(i).addservicefee;
			lastPrice += mulPetServiceList.get(i).fee;
		}
		spAdapter.notifyDataSetChanged();
		setLastPrice();
		setAppointPrice(Utils.formatDouble(lastPrice));
	}

	private void clickLevel2(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 20;
		if (btBeauticianLevel1.isEnabled()) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel3.isEnabled()) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (beauticianSelected != null && beauticianSelected.sort == 20) {
			showBeauticianList(1);
			setBeautician(beauticianSelected);
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.aE33A4A));
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.aE33A4A));
			showBeauticianList(2);
			adapterBeautician.setList(btc);
			setBeautician(btc.get(0));
		}
		lastPrice = 0;
		for (int i = 0; i < mulPetServiceList.size(); i++) {
			mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
					.get(i).pricelevel2;

			mulPetServiceList.get(i).fee = mulPetServiceList.get(i).basefeewithbeautician
					+ mulPetServiceList.get(i).addservicefee;
			lastPrice += mulPetServiceList.get(i).fee;
		}
		spAdapter.notifyDataSetChanged();
		setLastPrice();
		setAppointPrice(Utils.formatDouble(lastPrice));
	}

	private void clickLevel1(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 10;
		if (btBeauticianLevel2.isEnabled()) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel3.isEnabled()) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (beauticianSelected != null && beauticianSelected.sort == 10) {
			showBeauticianList(1);
			setBeautician(beauticianSelected);
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.aE33A4A));
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.aE33A4A));
			showBeauticianList(2);
			adapterBeautician.setList(btc);
			setBeautician(btc.get(0));
		}
		lastPrice = 0;
		for (int i = 0; i < mulPetServiceList.size(); i++) {
			mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
					.get(i).pricelevel1;

			mulPetServiceList.get(i).fee = mulPetServiceList.get(i).basefeewithbeautician
					+ mulPetServiceList.get(i).addservicefee;
			lastPrice += mulPetServiceList.get(i).fee;
		}
		spAdapter.notifyDataSetChanged();
		setLastPrice();
		setAppointPrice(Utils.formatDouble(lastPrice));
	}

	private String getAddServiceIds() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < addserviceIdList.size(); i++) {
			sb.append(addserviceIdList.get(i) + ",");
		}
		if (sb.toString().length() > 0) {
			return sb.substring(0, sb.toString().length() - 1);
		}
		return null;
	}

	// 获取宠物ID和serviceID及单项ID的组合
	private String getPetID_ServiceId_ItemIds() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mulPetServiceList.size(); i++) {
			MulPetService mps = mulPetServiceList.get(i);
			if (i < mulPetServiceList.size() - 1) {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
				sb.append("_");

			} else {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
			}
		}
		return sb.toString();
	}

	// 获取宠物ID和serviceID客服宠物ID服务类型服务宠物类型和单项ID
	private String getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mulPetServiceList.size(); i++) {
			MulPetService mps = mulPetServiceList.get(i);
			if (i < mulPetServiceList.size() - 1) {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				sb.append(mps.serviceType);
				sb.append("_");
				sb.append(mps.petKind);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
				sb.append("_");

			} else {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				sb.append(mps.serviceType);
				sb.append("_");
				sb.append(mps.petKind);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
			}
		}
		return sb.toString();
	}

	private int isItemSelected(AddServiceItem item) {
		for (int i = 0; i < addserviceIdList.size(); i++) {
			if (item.id == addserviceIdList.get(i)) {
				return i;
			}
		}
		return -1;
	}

	private void goNextForTimeData(Class clazz, int requestcode, int setviceid,
			double lat, double lng, String time, int serviceloc) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("vieEnabled", ps.vieEnabled);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("shopid", shopid);
		intent.putExtra("areaid", areaid);
		intent.putExtra("lastPrice", lastPrice);
		intent.putExtra("serviceloc", serviceloc);
		intent.putExtra("position", dayposition);
		if (addrtype == 1)
			intent.putExtra("areaname", shopAreaName);
		else
			intent.putExtra("areaname", areaName);
		intent.putExtra("beauticianlevel", ps.beautician_sort);
		if (beauticianSelected != null
				&& beauticianSelected.id == ps.beautician_id) {
			intent.putExtra("beautician_id", ps.beautician_id);
		}
		intent.putExtra("time", time);
		intent.putExtra("apptime", date);
		intent.putExtra("strp", getPetID_ServiceId_ItemIds());
		intent.putExtra("strp_long",
				getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds());
		intent.putExtra("lng", lng);
		intent.putExtra("lat", lat);
		if (mulPetServiceList.size() > 1) {
			double addtotalprice = 0;
			for (int i = 0; i < mulPetServiceList.size(); i++)
				addtotalprice += mulPetServiceList.get(i).addservicefee;
			intent.putExtra("addserviceprice", addtotalprice);
		} else {
			intent.putExtra("addserviceprice", addserviceprice);
		}
		intent.putParcelableArrayListExtra("mulpetservice", mulPetServiceList);
		intent.putExtra("ServiceStr", tvServiceName.getText().toString());
		intent.putExtra("ServiceType", addrtype);
		intent.putExtra("clicksort", clicksort);
		if (beauticianSelected != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("localBautician", beauticianSelected);
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestcode);
		if (requestcode == Global.PRE_SERVICEDETAIL_TO_SHOPLIST
				|| requestcode == Global.APPOINTMENT_TO_AREA) {
			overridePendingTransition(R.anim.down_in, R.anim.down_out);
		}
	}

	private void goNextForAreaData(Class clazz, int requestcode, int setviceid,
			double lat, double lng, String time, int serviceloc) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("shopid", shopid);
		intent.putExtra("areaid", areaid);
		intent.putExtra("serviceloc", serviceloc);
		intent.putExtra("position", dayposition);
		intent.putExtra("previous", requestcode);
		intent.putExtra("beauticianlevel", ps.beautician_sort);
		intent.putExtra("time", time);
		intent.putExtra("strp", getPetID_ServiceId_ItemIds());
		intent.putExtra("strp_long",
				getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds());
		intent.putExtra("lng", lng);
		intent.putExtra("lat", lat);

		intent.putExtra("petid", petid);
		intent.putExtra("petname", getIntent().getStringExtra("petname"));
		intent.putExtra("petkind", getIntent().getIntExtra("petkind", 0));
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("customerpetname",
				getIntent().getStringExtra("customerpetname"));
		intent.putExtra("avatarPath", avatarPath);

		startActivityForResult(intent, requestcode);
		overridePendingTransition(R.anim.down_in, R.anim.down_out);
	}

	private void goNextForData(Class clazz, int requestcode, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("addrid", addrid);
		intent.putExtra("lat", lat);
		intent.putExtra("lng", lng);
		intent.putExtra("shopid", shopid);
		intent.putExtra("time", date);
		intent.putExtra("beautician_sort", clicksort);
		intent.putExtra("servloc", addrtype);
		startActivityForResult(intent, requestcode);
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		if (mulPetServiceList != null && mulPetServiceList.size() > 1) {
			showBackDialog(1, "离开页面，已填写的宠物信息将不保存，确定离开？");
			return;
		} else if (mulPetServiceList != null && mulPetServiceList.size() == 1) {
			if (mulPetServiceList.get(0).petId == petid) {
				if (mulPetServiceList.get(0).serviceId == serviceid) {
					if (addserviceIdList.size() > 0) {
						showBackDialog(1, "离开页面，已填写的宠物信息将不保存，确定离开？");
						return;
					}
				} else {
					showBackDialog(1, "离开页面，已填写的宠物信息将不保存，确定离开？");
					return;
				}
			} else {
				showBackDialog(1, "离开页面，已填写的宠物信息将不保存，确定离开？");
				return;
			}
		} else {
			showBackDialog(1, "离开页面，已填写的宠物信息将不保存，确定离开？");
			return;
		}
		finishWithAnimation();
	}

	/**
	 * 
	 * @param type
	 *            对话框类型 1：为返回对话框 2：为切换店铺对话框
	 * @param hint
	 *            提示语
	 */
	private void showBackDialog(final int type, String hint) {
		String okstr = null;
		if (type == 1)
			okstr = "确定";
		else if (type == 2)
			okstr = "切换";
		MDialog mDialog = new MDialog.Builder(this).setTitle("提示")
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(hint)
				.setCancelStr("取消").setOKStr(okstr)
				.positiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (type == 1)
							finishWithAnimation();
						else if (type == 2) {
							// 切换店铺
							clearPSBeautician();
							isBeauticianAppointment = false;
							spUtil.removeData("newshopid");
							shopid = shoptmpid;
							ps.petstoreid = shopid;
							ps.petstoreAddr = shopAddr;
							ps.petstoreName = shopName;
							ps.petstoreImg = shopImg;
							ps.petstorePhone = shoptel;
							if (addrtype == 1)
								tvTitle.setText(shopName);
							getBeauticianListData();
						}
					}
				}).negativeListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (type == 2)
							getBeauticianListData();
					}
				}).build();
		mDialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Global.RESULT_OK && data != null) {
			if (requestCode == Global.BOOKINGSERVICEREQUESTCODE_ADDR) {
				if ((lat != data.getDoubleExtra("addr_lat", 0) || lng != data
						.getDoubleExtra("addr_lng", 0))
						|| (data.getStringExtra("addr") != null && !data
								.getStringExtra("addr").trim().equals(addr))) {
					ps.ispickup = false;
					addr = data.getStringExtra("addr");
					tvAddr.setTextColor(getResources()
							.getColor(R.color.a999999));
					tvAddr.setText(addr);
					lat = data.getDoubleExtra("addr_lat", 0);
					lng = data.getDoubleExtra("addr_lng", 0);
					ps.addr_detail = addr;
					ps.addr_lat = lat;
					ps.addr_lng = lng;
					ps.addr_id = data.getIntExtra("addr_id", 0);
					addrid = data.getIntExtra("addr_id", 0);
					spUtil.saveInt("newaddrid", addrid);
					spUtil.saveString("newaddr", addr);
					spUtil.saveString("newlat", lat + "");
					spUtil.saveString("newlng", lng + "");
					if (!isBeauticianAppointment) {
						clearPSBeautician();
					}
					ps.ispickup = false;
					if (addrtype == 2) {
						isServiceInTheArea(lat, lng);
					}
					getMulPetServiceData(addrtype, 0, date, lat, lng);
					// 清除时间和美容师
					tvTime.setText("");
					strListWokerId = "";
					time = "";
					date = "";
					dayposition = -1;
					beauticianList.clear();
					beauticianMap.clear();
					beauticianSelected = null;
				}
			} else if (requestCode == Global.BOOKINGSERVICEREQUESTCODE_TIME) {
				// 选择时间返回
				strListWokerId = data.getStringExtra("strListWokerId");
				Log.e("TAG", "appact strListWokerId = " + strListWokerId);
				time = data.getStringExtra("time");
				date = data.getStringExtra("date");
				dayposition = data.getIntExtra("position", -1);
				Utils.mLogError("time=" + time);
				Utils.mLogError("date=" + date);
				ps.servicedate = date;
				String datastr = "";
				if (date.length() >= 3) {
					String substring = date.substring(date.length() - 3,
							date.length());
					if (substring.equals(":00")) {
						datastr = date.substring(0, date.length() - 3);
						tvTime.setText(datastr);
					} else {
						tvTime.setText(date);
					}
				}
				ps.servicetime = time;
				beauticianSelected = (Beautician) data
						.getSerializableExtra("localBautician");
				if (beauticianSelected != null) {
					// 选择美容师返回
					showBeauticianList(1);
					if (ps.beautician_id == data
							.getIntExtra("beautician_id", 0)) {
						ps.isDefaultWorker = 0;
					} else {
						ps.isDefaultWorker = 1;
					}
					setBeautician(beauticianSelected);
					ps.beautician_sort = beauticianSelected.sort;
					ps.beautician_id = beauticianSelected.id;
					ps.upgradeTip = beauticianSelected.upgradeTip;
					if (ps.beautician_name != null)
						tvBeauticianAboutName.setText(ps.beautician_name);
					tvBeauticianAboutLevel.setText("接单："
							+ ps.beautician_ordernum);
					Utils.setText(tv_appointment_upgradetip, ps.upgradeTip, "",
							View.VISIBLE, View.GONE);
					if (ps.beautician_sex == 1)
						srivBeauticianSex.setImageResource(R.drawable.icon_man);
					else
						srivBeauticianSex
								.setImageResource(R.drawable.icon_women);
					ImageLoaderUtil.loadImg(ps.beautician_image,
							srivBeauticianAbout, 0, null);
					clicksort = beauticianSelected.sort;
					Log.e("TAG", "clicksort = " + clicksort);
				}
				lastPrice = 0;
				for (int i = 0; i < mulPetServiceList.size(); i++) {
					if (ps.beautician_sort == 10) {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel1;
					} else if (ps.beautician_sort == 20) {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel2;
					} else {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel3;
					}
					mulPetServiceList.get(i).fee = mulPetServiceList.get(i).basefeewithbeautician
							+ mulPetServiceList.get(i).addservicefee;
					lastPrice += mulPetServiceList.get(i).fee;
				}
				spAdapter.notifyDataSetChanged();
				setLastPrice();
				setAppointPrice(Utils.formatDouble(lastPrice));
				getMulPetServiceData(addrtype, shopid, date, lat, lng);
			} else if (requestCode == Global.SERVICEREQUESTCODE_BEAUTICIAN) {
				// 选择美容师返回
				showBeauticianList(1);
				if (ps.beautician_id == data.getIntExtra("beautician_id", 0)) {
					ps.isDefaultWorker = 0;
				} else {
					ps.isDefaultWorker = 1;
				}
				if (data.getStringExtra("time") != null
						&& !"".equals(data.getStringExtra("time"))) {
					time = data.getStringExtra("time");
					date = data.getStringExtra("date");
					ps.servicedate = date;
					String datastr = "";
					if (date.length() >= 3) {
						String substring = date.substring(date.length() - 3,
								date.length());
						if (substring.equals(":00")) {
							datastr = date.substring(0, date.length() - 3);
							tvTime.setText(datastr);
						} else {
							tvTime.setText(date);
						}
					}
					ps.servicetime = time;
				}
				beauticianSelected = new Beautician();
				beauticianSelected.name = data
						.getStringExtra("beautician_name");
				beauticianSelected.gender = data.getIntExtra("beautician_sex",
						0);
				beauticianSelected.levels = data.getIntExtra(
						"beautician_level", 0);
				beauticianSelected.stars = data.getIntExtra("beautician_stars",
						0);
				beauticianSelected.titlelevel = data
						.getStringExtra("beautician_titlelevel");
				beauticianSelected.image = data
						.getStringExtra("beautician_image");
				beauticianSelected.ordernum = data.getIntExtra(
						"beautician_ordernum", 0);
				beauticianSelected.storeid = data.getIntExtra(
						"beautician_storeid", 0);
				beauticianSelected.id = data.getIntExtra("beautician_id", 0);
				beauticianSelected.levelname = data
						.getStringExtra("beautician_levelname");
				beauticianSelected.sort = data
						.getIntExtra("beautician_sort", 0);
				beauticianSelected.upgradeTip = data
						.getStringExtra("upgradeTip");
				ps.upgradeTip = beauticianSelected.upgradeTip;
				setBeautician(beauticianSelected);
				ps.beautician_sort = data.getIntExtra("beautician_sort", 0);
				ps.beautician_id = beauticianSelected.id;
				if (ps.beautician_name != null)
					tvBeauticianAboutName.setText(ps.beautician_name);
				tvBeauticianAboutLevel.setText("接单：" + ps.beautician_ordernum);
				Utils.setText(tv_appointment_upgradetip, ps.upgradeTip, "",
						View.VISIBLE, View.GONE);
				if (ps.beautician_sex == 1)
					srivBeauticianSex.setImageResource(R.drawable.icon_man);
				else
					srivBeauticianSex.setImageResource(R.drawable.icon_women);
				ImageLoaderUtil.loadImg(ps.beautician_image,
						srivBeauticianAbout, 0, null);
				lastPrice = 0;
				for (int i = 0; i < mulPetServiceList.size(); i++) {
					if (ps.beautician_sort == 10) {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel1;
					} else if (ps.beautician_sort == 20) {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel2;
					} else {
						mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
								.get(i).pricelevel3;
					}
					mulPetServiceList.get(i).fee = mulPetServiceList.get(i).basefeewithbeautician
							+ mulPetServiceList.get(i).addservicefee;
					lastPrice += mulPetServiceList.get(i).fee;
				}
				spAdapter.notifyDataSetChanged();
				setLastPrice();
				setAppointPrice(Utils.formatDouble(lastPrice));
				clicksort = beauticianSelected.sort;
				selectData();
			} else if (requestCode == Global.PRE_SERVICEDETAIL_TO_SHOPLIST) {
				// 店铺返回
				if (shopid != data.getIntExtra("shopid", 0)
						&& data.getIntExtra("shopid", 0) != 0) {
					shopid = data.getIntExtra("shopid", 0);
					areaid = data.getIntExtra("areaid", 0);
					ps.petstoreid = shopid;
					ps.petstoreAddr = data.getStringExtra("shopaddr");
					ps.petstoreName = data.getStringExtra("shopname");
					ps.petstoreImg = data.getStringExtra("shopimg");
					ps.petstorePhone = data.getStringExtra("shoptel");
					spUtil.saveInt("newshopid", shopid);
					// 清空地址何经纬度
					tvAddr.setText("");
					lat = 0;
					lng = 0;
					// 清空时间
					date = "";
					time = "";
					ps.servicedate = date;
					ps.servicetime = time;
					clearPSBeautician();
					isBeauticianAppointment = false;
					tvTime.setText("");
					getMulPetServiceData(addrtype, shopid, date, lat, lng);
					// 清除时间和美容师
					tvTime.setText("");
					strListWokerId = "";
					time = "";
					date = "";
					dayposition = -1;
					beauticianList.clear();
					beauticianMap.clear();
					beauticianSelected = null;
				}
			} else if (requestCode == Global.APPOINTMENT_TO_AREA) {
				// 商圈
				if (areaid != data.getIntExtra("areaid", 0)
						&& data.getIntExtra("areaid", 0) != 0) {
					shopid = data.getIntExtra("shopid", 0);
					ps.petstoreid = shopid;
					areaid = data.getIntExtra("areaid", 0);
					tvTitle.setText(data.getStringExtra("areaname") + "上门服务");
					ps.areaid = areaid;
					// 清空地址何经纬度
					tvAddr.setText("");
					lat = 0;
					lng = 0;
					// 清空时间
					date = "";
					time = "";
					ps.servicedate = date;
					ps.servicetime = time;
					clearPSBeautician();
					isBeauticianAppointment = false;
					tvTime.setText("");
					if (addrtype == 2 && areaid > 0 && areaid != 100) {
						if (!"".equals(tvAddr.getText().toString().trim()))
							isServiceInTheArea(lat, lng);
						getMulPetServiceData(addrtype, shopid, date, lat, lng);
					} else {
						getMulPetServiceData(addrtype, shopid, date, lat, lng);
					}
					getBeauticianListData();
					// 清除时间和美容师
					tvTime.setText("");
					strListWokerId = "";
					time = "";
					date = "";
					dayposition = -1;
					beauticianList.clear();
					beauticianMap.clear();
					beauticianSelected = null;
				}
			} else if (requestCode == Global.PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY) {
				// 从订单确认返回,解决未登录时下单登录后返回该界面不显示地址列表中的地址
				getServiceAddrList();
			} else if (requestCode == Global.APPOINTMENT_TO_ADDPET) {
				// 从添加宠物返回
				ps.ispickup = false;
				date = "";
				time = "";
				ps.servicedate = date;
				ps.servicetime = time;
				tvTime.setText("");
				clearPSBeautician();
				isBeauticianAppointment = false;
				MulPetService mps = new MulPetService();
				mps.petId = data.getIntExtra("petid", 0);
				mps.petKind = data.getIntExtra("petkind", 0);
				mps.petCustomerId = data.getIntExtra("customerpetid", 0);
				mps.petName = data.getStringExtra("petname");
				mps.petCustomerName = data.getStringExtra("customerpetname");
				mps.petimage = data.getStringExtra("petimage");
				mps.serviceId = data.getIntExtra("serviceid", 0);
				mps.serviceType = data.getIntExtra("servicetype", 0);
				mps.serviceloc = addrtype;
				mps.serviceName = data.getStringExtra("servicename");
				mps.addServiceIds = data.getStringExtra("addserviceids");
				mps.fee = data.getDoubleExtra("totalfee", 0);
				mps.addservicefee = data.getDoubleExtra("addservicefee", 0);
				mps.basefee = data.getDoubleExtra("basefee", 0);
				mulPetServiceList.add(mps);
				if (mulPetServiceList.size() <= 1) {
					spAdapter.notifyDataSetChanged();
					showSinglePet(true);
				} else {
					if (mulPetServiceList.size() >= 5) {
						spAdapter.setIsFull(true);
						rlAddPet.setVisibility(View.GONE);
					}
					spAdapter.notifyDataSetChanged();
					showSinglePet(false);
				}
				getMulPetServiceData(addrtype, shopid, date, lat, lng);
				// 清除时间和美容师
				tvTime.setText("");
				strListWokerId = "";
				time = "";
				date = "";
				dayposition = -1;
				beauticianList.clear();
				beauticianMap.clear();
				beauticianSelected = null;
			} else if (requestCode == Global.APPOINTMENT_TO_ADDSERVICE) {
				// 从修改宠物服务返回
				int position = data.getIntExtra("position", -1);
				if (position > -1 && position < mulPetServiceList.size()) {
					if (mulPetServiceList.get(position).serviceType != data
							.getIntExtra("servicetype", 0)
							|| (mulPetServiceList.get(position).addServiceIds == null && data
									.getStringExtra("addserviceids") != null)
							|| (mulPetServiceList.get(position).addServiceIds != null && data
									.getStringExtra("addserviceids") == null)
							|| (!mulPetServiceList.get(position).addServiceIds
									.equals(data
											.getStringExtra("addserviceids")))) {
						mulPetServiceList.get(position).serviceType = data
								.getIntExtra("servicetype", 0);
						mulPetServiceList.get(position).serviceId = data
								.getIntExtra("serviceid", 0);
						mulPetServiceList.get(position).serviceName = data
								.getStringExtra("servicename");
						mulPetServiceList.get(position).addServiceIds = data
								.getStringExtra("addserviceids");
						mulPetServiceList.get(position).fee = data
								.getDoubleExtra("totalfee", 0);
						mulPetServiceList.get(position).basefee = data
								.getDoubleExtra("basefee", 0);
						mulPetServiceList.get(position).pricelevel1 = data
								.getDoubleExtra("pricelevel1", 0);
						mulPetServiceList.get(position).pricelevel2 = data
								.getDoubleExtra("pricelevel2", 0);
						mulPetServiceList.get(position).pricelevel3 = data
								.getDoubleExtra("pricelevel3", 0);
						mulPetServiceList.get(position).addservicefee = data
								.getDoubleExtra("addservicefee", 0);
						spAdapter.notifyDataSetChanged();
						if (mulPetServiceList.size() <= 1) {
							showSinglePet(true);
							if (tvAddr.getText().toString() != null
									&& !"".equals(tvAddr.getText().toString()
											.trim())
									&& tvTime.getText().toString() != null
									&& !"".equals(tvTime.getText().toString()
											.trim()) && time != null
									&& !"".equals(time.trim()) && date != null
									&& !"".equals(date.trim())) {
								isRefreshBeautician = false;
							}
							getMulPetServiceData(addrtype, shopid, date, lat,
									lng);
						} else {
							showSinglePet(false);
							lastPrice = 0;
							for (int i = 0; i < mulPetServiceList.size(); i++) {
								if (ps.beautician_sort == 10) {
									mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
											.get(i).pricelevel1;

								} else if (ps.beautician_sort == 20) {
									mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
											.get(i).pricelevel2;
								} else {
									mulPetServiceList.get(i).basefeewithbeautician = mulPetServiceList
											.get(i).pricelevel3;
								}
								mulPetServiceList.get(i).fee = mulPetServiceList
										.get(i).basefeewithbeautician
										+ mulPetServiceList.get(i).addservicefee;
								lastPrice += mulPetServiceList.get(i).fee;
							}
							spAdapter.notifyDataSetChanged();
							setLastPrice();
							setAppointPrice(Utils.formatDouble(lastPrice));
						}
					}
				}
				// 清除时间和美容师
				tvTime.setText("");
				strListWokerId = "";
				time = "";
				date = "";
				dayposition = -1;
				beauticianList.clear();
				beauticianMap.clear();
				beauticianSelected = null;
			}
		}
	}

	private void getServiceAddrList() {
		// TODO 请求服务器判断显示那个布局
		addrSize = 0;
		if (!"".equals(spUtil.getString("cellphone", ""))) {
			CommUtil.queryServiceAddress(spUtil.getString("cellphone", ""),
					spUtil.getInt("userid", 0), Global.getIMEI(this), this,
					queryServiceAddress);
		}
	}

	private void isServiceInTheArea(double lat, double lng) {
		CommUtil.queryTradeAreaByLoc(this, lat, lng, queryTradeAreaHander);
	}

	private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					addrSize = jsonArray.length();
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

	private AsyncHttpResponseHandler queryTradeAreaHander = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			localIsInArea = false;
			localPosition = 0;
			Gson gson = new Gson();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				if (jobj.has("code") && !jobj.isNull("code")) {
					int resultCode = jobj.getInt("code");
					if (0 == resultCode) {
						if (jobj.has("data") && !jobj.isNull("data")) {
							JSONObject jdata = jobj.getJSONObject("data");
							SingelAreaBean singelAreaBean = gson.fromJson(
									jdata.toString(), SingelAreaBean.class);
							pDialog.closeDialog();
							if (singelAreaBean != null) {
								List<AreaBean> area = singelAreaBean.getArea();
								if (area != null && area.size() > 0) {
									for (int i = 0; i < area.size(); i++) {
										AreaBean areaBean = area.get(i);
										if (areaBean != null) {
											if (areaid == areaBean.getAreaId()) {
												localPosition = i;
												// 地址在选中区域
												localIsInArea = true;
												break;
											}
										} else {// 不在任何区域
											isInArea = false;
											changeAreaType = 1;
											tvHintHandler
													.sendEmptyMessageDelayed(
															200, 800);
										}
									}
									if (localIsInArea) {
										isInArea = true;
										AreaBean areaBean = area
												.get(localPosition);
										if (areaBean != null) {
											newAreaid = areaBean.getAreaId();
										}
									} else {
										for (int j = 0; j < area.size(); j++) {
											AreaBean areaBean = area.get(j);
											if (areaBean != null) {
												if (areaBean.getToBeOpen() != 1) {
													newAreaid = areaBean
															.getAreaId();
													// 地址不在选中区域
													isInArea = false;
													if (areaBean.getShopName() != null
															&& !TextUtils
																	.isEmpty(areaBean
																			.getShopName())) {
														newAreaName = areaBean
																.getShopName();
													}
													newShopid = areaBean
															.getShopId();
													showAddrNoInAreaDialog(
															2,
															newAreaid,
															"您的地址属于"
																	+ newAreaName
																	+ "，是否切换到该区域？");
													break;
												}
											}
										}
									}
								} else {// 不在任何区域
									isInArea = false;
									changeAreaType = 1;
									tvHintHandler.sendEmptyMessageDelayed(200,
											800);
								}
							} else {// 不在任何区域
								isInArea = false;
								changeAreaType = 1;
								tvHintHandler.sendEmptyMessageDelayed(200, 800);
							}
						} else {// 不在任何区域
							isInArea = false;
							changeAreaType = 1;
							tvHintHandler.sendEmptyMessageDelayed(200, 800);
						}
					} else {
						if (jobj.has("msg") && !jobj.isNull("msg")) {
							String msg = jobj.getString("msg");
							if (msg != null && !TextUtils.isEmpty(msg)) {
								ToastUtil.showToastShortBottom(
										AppointmentActivity.this, msg);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};
	protected JSONObject workersData;
	private int locServiceloc;
	private String locStrp;

	private void clearPSBeautician() {
		ps.beautician_id = 0;
		ps.beautician_image = null;
		ps.beautician_levels = 0;
		ps.beautician_name = null;
		ps.beautician_ordernum = 0;
		ps.beautician_sign = null;
		ps.beautician_stars = 0;
		ps.beautician_storeid = 0;
		ps.beautician_titlelevel = null;
		ps.beautician_sex = 0;
		ps.beautician_levelname = null;
		ps.beautician_sort = 0;
	}

	private void setLevelButtonStatus(boolean flag) {
		if (flag) {
			vw_servicedetail_beautician1.setVisibility(View.VISIBLE);
			vw_servicedetail_beautician2.setVisibility(View.VISIBLE);
			btBeauticianLevel1.setVisibility(View.VISIBLE);
			btBeauticianLevel2.setVisibility(View.VISIBLE);
			btBeauticianLevel3.setVisibility(View.VISIBLE);
		} else {
			vw_servicedetail_beautician1.setVisibility(View.GONE);
			vw_servicedetail_beautician2.setVisibility(View.GONE);
			btBeauticianLevel1.setVisibility(View.GONE);
			btBeauticianLevel2.setVisibility(View.GONE);
			btBeauticianLevel3.setVisibility(View.GONE);
		}
	}

	private void getBeauticianListNoTimeLimt(int areaid, int serviceloc,
			int shopid, String strp) {
		beauticianList.clear();
		if (!isBeauticianAppointment && !issubmit && isRefreshBeautician
				&& beauticianSelected != null
				&& beauticianSelected.id != ps.beautician_id) {
			beauticianMap.clear();
			clearPSBeautician();
		}
		CommUtil.getBeauticianListNoTimeLimit(this,
				spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this), areaid, serviceloc, shopid,
				lat, lng, strp, BeauticianHanler);
	}

	private void queryAvailableWorkers(int areaid, String date, int serviceloc,
			int shopid, String strp) {
		beauticianList.clear();
		if (!isBeauticianAppointment && !issubmit && isRefreshBeautician
				&& beauticianSelected != null
				&& beauticianSelected.id != ps.beautician_id) {
			beauticianMap.clear();
			clearPSBeautician();
		}
		if (issubmit) {
			CommUtil.queryAvailableWorkers(this, strListWokerId,
					spUtil.getString("cellphone", ""), Global.getIMEI(this),
					Global.getCurrentVersion(this), areaid, date, serviceloc,
					shopid, lat, lng, strp, BeauticianHanler);
		} else {
			if (beauticianSelected != null && beauticianSelected.id > 0) {
				locServiceloc = serviceloc;
				locStrp = strp;
				CommUtil.getBeauticianFreeTime(this, lat, lng,
						spUtil.getString("cellphone", ""),
						Global.getIMEI(this), Global.getCurrentVersion(this),
						areaid, serviceloc, shopid, strp, 0, date, 0,
						timeHanler);
			} else {
				CommUtil.queryAvailableWorkers(this, strListWokerId,
						spUtil.getString("cellphone", ""),
						Global.getIMEI(this), Global.getCurrentVersion(this),
						areaid, date, serviceloc, shopid, lat, lng, strp,
						BeauticianHanler);
			}
		}
	}

	private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				if (code == 0) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("workers") && !jdata.isNull("workers")) {
							JSONArray jarrworkers = jdata
									.getJSONArray("workers");
							if (jarrworkers.length() > 0) {
								String str = "";
								for (int i = 0; i < jarrworkers.length(); i++) {
									str = str + "," + jarrworkers.getInt(i);
								}
								strListWokerId = str.substring(1, str.length());
								Log.e("TAG", "app熬出头 strListWokerId = "
										+ strListWokerId);
								CommUtil.queryAvailableWorkers(
										AppointmentActivity.this,
										strListWokerId,
										spUtil.getString("cellphone", ""),
										Global.getIMEI(AppointmentActivity.this),
										Global.getCurrentVersion(AppointmentActivity.this),
										areaid, date, locServiceloc, shopid,
										lat, lng, locStrp, BeauticianHanler);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private AsyncHttpResponseHandler BeauticianHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				beauticianMap.remove("10");
				beauticianMap.remove("20");
				beauticianMap.remove("30");
				if (!isBeauticianAppointment)
					setLevelButtonStatus(true);
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						workersData = jData;
						if (jData.has("workers") && !jData.isNull("workers")) {
							vw_appoint_beautician.setVisibility(View.VISIBLE);
							rl_appointment_beautician
									.setVisibility(View.VISIBLE);
							rl_appointment_switchbau
									.setVisibility(View.VISIBLE);
							rl_appointment_switchbau
									.setBackgroundColor(getResources()
											.getColor(R.color.white));
							rlBeauticianMore.setVisibility(View.VISIBLE);
							setLevelButtonStatus(true);
							JSONObject jWorkers = jData
									.getJSONObject("workers");
							if (jWorkers.has("10") && !jWorkers.isNull("10")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("10");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel1.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;
										blList.add(btc);
									}
									beauticianMap.put("10", blList);
								}
							}
							if (jWorkers.has("20") && !jWorkers.isNull("20")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("20");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel2.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;

										blList.add(btc);
									}
									beauticianMap.put("20", blList);
								}
							}
							if (jWorkers.has("30") && !jWorkers.isNull("30")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("30");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel3.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;
										blList.add(btc);
									}
									beauticianMap.put("30", blList);
								}
							}
						} else {
							vw_appoint_beautician.setVisibility(View.GONE);
							rl_appointment_beautician.setVisibility(View.GONE);
							rl_appointment_switchbau.setVisibility(View.GONE);
							rl_appointment_switchbau
									.setBackgroundColor(getResources()
											.getColor(R.color.f8f8ff8));
							rlBeauticianMore.setVisibility(View.GONE);
							setLevelButtonStatus(false);
						}
					}
					// 从服务入口
					selectData();
					if (issubmit) {
						issubmit = false;
						if (checkBeautician()) {
							if (spUtil.getInt("userid", 0) > 0
									&& !"".equals(spUtil.getString("cellphone",
											""))) {
								// 洗澡美容提交需判断
								Intent intent = new Intent(
										AppointmentActivity.this,
										OrderDetailActivity.class);
								intent.putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_RIGHT());
								getIntent().putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_LEFT());
								intent.putExtra(
										"previous",
										Global.PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY);
								intent.putExtra("serviceid", serviceid);
								intent.putExtra("servicetype", servicetype);
								intent.putExtra("totalfee", lastPrice);
								intent.putParcelableArrayListExtra(
										"mulpetservice", mulPetServiceList);
								ps.areaid = areaid;
								intent.putExtra("ps", ps);
								startActivityForResult(
										intent,
										Global.PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY);
								if (servicetype == 1) {
									// UmengStatistics.UmengEventStatistics(AppointmentActivity.this,Global.UmengEventID.click_BathSubmit);//
									// 洗澡提交
								} else if (servicetype == 2) {
									// UmengStatistics.UmengEventStatistics(AppointmentActivity.this,Global.UmengEventID.click_BeautySubmit);//
									// 美容提交
								}
							} else {
								Intent intent = new Intent(
										AppointmentActivity.this,
										LoginActivity.class);
								intent.putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_RIGHT());
								getIntent().putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_LEFT());
								intent.putExtra(
										"previous",
										Global.PRE_SERVICEACTIVITY_TO_LOGINACTIVITY);
								intent.putExtra("serviceid", serviceid);
								intent.putExtra("servicetype", servicetype);
								intent.putExtra("totalfee", lastPrice);
								ps.areaid = areaid;
								intent.putParcelableArrayListExtra(
										"mulpetservice", mulPetServiceList);
								intent.putExtra("ps", ps);
								startActivityForResult(
										intent,
										Global.PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY);
							}
							if (beauticianSelected == null
									|| beauticianSelected.id != ps.beautician_id) {
								beauticianSelected = new Beautician();
								beauticianSelected.id = ps.beautician_id;

								beauticianSelected.gender = ps.beautician_sex;
								beauticianSelected.image = ps.beautician_image;
								beauticianSelected.name = ps.beautician_name;
								beauticianSelected.storeid = ps.beautician_storeid;
								beauticianSelected.fee = lastPrice;
								beauticianSelected.sort = ps.beautician_sort;
								beauticianSelected.levelname = ps.beautician_levelname;
								beauticianSelected.ordernum = ps.beautician_ordernum;
								showBeauticianList(1);
								setBeautician(beauticianSelected);
							}
						} else {
							ToastUtil.showToastShortCenter(mContext,
									"该时间段该美容师不可约,换个时间段或美容师试试吧");
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(
									AppointmentActivity.this, msg);
						}
					}
					showBeauticianList(3);
					if (issubmit) {
						issubmit = false;
					}
				}
				if (beauticianMap.size() <= 0) {
					setLevelButtonStatus(false);
				} else {
					setLevelButtonStatus(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showBeauticianList(3);
				if (issubmit) {
					issubmit = false;
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private void setLastPrice() {
		String text = "订单价: ¥ " + Utils.formatDouble(lastPrice);
		SpannableString ss = new SpannableString(text);
		ss.setSpan(
				new ForegroundColorSpan(getResources()
						.getColor(R.color.tabback)), 0, 4,
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		ss.setSpan(new TextAppearanceSpan(this, R.style.style6), 6,
				ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		ss.setSpan(new MyStyleSpan(Typeface.NORMAL), 6, ss.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		last_price.setText(ss);
		last_price.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private boolean checkBeautician() {
		if (beauticianMap != null) {
			ArrayList<Beautician> btc = beauticianMap.get(String
					.valueOf(clicksort));
			if (btc != null && btc.size() > 0) {
				for (int i = 0; i < btc.size(); i++) {
					if (ps.beautician_id > 0
							&& btc.get(i).id == ps.beautician_id
							&& btc.get(i).isAvail == 1)
						return true;
				}
			}
		}
		return false;
	}

	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("shopid", shopid);
		intent.putExtra("petid", petid);
		if (mulPetServiceList != null && mulPetServiceList.size() > 0) {
			MulPetService mulPetService = mulPetServiceList.get(0);
			if (mulPetService != null) {
				if (mulPetService.petCustomerId > 0) {
					intent.putExtra("customerpetid",
							mulPetService.petCustomerId);
					intent.putExtra("customerpetname",
							mulPetService.petCustomerName);
				} else {
					intent.putExtra("petname", mulPetService.petName);
				}
			}
		}
		intent.putExtra("remarks", remarks);
		startActivity(intent);
	}

	/**
	 * 上门服务的地址不在选中区域时提示
	 * 
	 * @param type
	 *            1：不在任何区域 2：不在选择区域
	 * @param hint
	 */
	private void showAddrNoInAreaDialog(final int type, final int id,
			String hint) {
		String okstr = null;
		changeAreaType = type;
		int dialogtype = MDialog.DIALOGTYPE_CONFIRM;
		if (type == 1) {
			okstr = "我知道了";
			dialogtype = MDialog.DIALOGTYPE_ALERT;
		} else if (type == 2) {
			okstr = "是";
			dialogtype = MDialog.DIALOGTYPE_CONFIRM;
		}
		MDialog mDialog = new MDialog.Builder(this).setTitle("提示")
				.setType(dialogtype).setMessage(hint).setCancelStr("否")
				.setOKStr(okstr).positiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (type == 2) {
							spUtil.saveInt("tshopid", newShopid);
							spUtil.saveString("tareaname", newAreaName);
							spUtil.saveInt("tareaid", newAreaid);
							// 切换区域
							areaid = id;
							isInArea = true;
							areaName = newAreaName;
							tvTitle.setText(newAreaName + "上门服务");
							// 清除时间和美容师
							tvTime.setText("");
							strListWokerId = "";
							time = "";
							date = "";
							dayposition = -1;
							beauticianList.clear();
							beauticianMap.clear();
							beauticianSelected = null;

							areaid = newAreaid;
							shopid = newShopid;
							ps.petstoreid = shoptmpid;
							ps.petstoreAddr = shopAddr;
							ps.petstoreName = shopName;
							ps.petstoreImg = shopImg;
							ps.petstorePhone = shoptel;
							ps.servicedate = date;
							ps.servicetime = time;
							tvTime.setText("");
							clearPSBeautician();
							isBeauticianAppointment = false;
							getMulPetServiceData(addrtype, 0, date, lat, lng);
						}
					}
				}).negativeListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				}).build();
		mDialog.show();
	}

	class ServicePetAdapter extends BaseAdapter {
		private boolean isFull;

		@Override
		public int getCount() {
			return mulPetServiceList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mulPetServiceList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		public void setIsFull(boolean isFull) {
			this.isFull = isFull;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.servicepet, null);
				holder.rlLeft = (RelativeLayout) convertView
						.findViewById(R.id.rl_servicepet_left);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_servicepet_icon);
				holder.ivDelete = (ImageView) convertView
						.findViewById(R.id.iv_servicepet_delete);
				holder.tvInfo = (TextView) convertView
						.findViewById(R.id.tv_servicepet_info);
				holder.tvFee = (TextView) convertView
						.findViewById(R.id.tv_servicepet_fee);
				holder.vBottomLine = convertView
						.findViewById(R.id.view_servicepet_bottom);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if (isFull && position == mulPetServiceList.size() - 1)
				holder.vBottomLine.setVisibility(View.GONE);
			else
				holder.vBottomLine.setVisibility(View.VISIBLE);
			final MulPetService mulPetService = mulPetServiceList.get(position);
			if (mulPetService != null) {
				String petimage = mulPetService.petimage;
				if (petimage != null && !TextUtils.isEmpty(petimage)) {
					ImageLoaderUtil.loadImg(mulPetService.petimage,
							holder.ivIcon, R.drawable.icon_production_default,
							null);
				}
				String text = "¥ " + Utils.formatDouble(mulPetService.fee);
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new TextAppearanceSpan(AppointmentActivity.this,
						R.style.style2), 1, ss.length(),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				holder.tvFee.setText(ss);
				if (mulPetService.addServiceIds != null
						&& !"".equals(mulPetService.addServiceIds)) {
					if (mulPetService.petCustomerName != null
							&& !"".equals(mulPetService.petCustomerName))
						holder.tvInfo.setText(mulPetService.petCustomerName
								+ mulPetService.serviceName + "[有单项]");
					else
						holder.tvInfo.setText(mulPetService.petName
								+ mulPetService.serviceName + "[有单项]");
				} else {
					if (mulPetService.petCustomerName != null
							&& !"".equals(mulPetService.petCustomerName))
						holder.tvInfo.setText(mulPetService.petCustomerName
								+ mulPetService.serviceName);
					else
						holder.tvInfo.setText(mulPetService.petName
								+ mulPetService.serviceName);
				}
				holder.rlLeft.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(act,
								MulPetAddServiceActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_RIGHT());
						getIntent().putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_LEFT());
						if (addrtype == 0) {
							shopid = 0;
						}
						intent.putExtra("previous",
								Global.APPOINTMENT_TO_ADDSERVICE);
						intent.putExtra("serviceid", mulPetService.serviceId);
						intent.putExtra("servicetype",
								mulPetService.serviceType);
						intent.putExtra("addrid", addrid);
						intent.putExtra("lat", lat);
						intent.putExtra("lng", lng);
						intent.putExtra("shopid", shopid);
						intent.putExtra("time", date);
						intent.putExtra("servloc", addrtype);
						intent.putExtra("beautician_sort", clicksort);
						intent.putExtra("petid", mulPetService.petId);
						intent.putExtra("petkind", mulPetService.petKind);
						intent.putExtra("addserviceids",
								mulPetService.addServiceIds);
						intent.putExtra("position", position);
						startActivityForResult(intent,
								Global.APPOINTMENT_TO_ADDSERVICE);
					}
				});
				holder.ivDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (mDialog != null && mDialog.isShowing()) {
							ToastUtil.showToastShortBottom(
									AppointmentActivity.this, "请稍后...");
						} else {
							showDeletePet("确认删除该宠物？", position);
						}
					}
				});
			}
			return convertView;
		}
	}

	class Holder {
		RelativeLayout rlLeft;
		ImageView ivIcon;
		ImageView ivDelete;
		TextView tvInfo;
		TextView tvFee;
		View vBottomLine;
	}

	private void showAddrNoInArea() {
		pWin = null;
		if (pWin == null) {
			View view = mInflater.inflate(R.layout.addrhint, null);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			// pWin.setBackgroundDrawable(new BitmapDrawable());
			pWin.setOutsideTouchable(true);
			pWin.setFocusable(true);
			// pWin.showAtLocation(tvAddr, Gravity.RIGHT, 0, -20);
			pWin.showAsDropDown(tvAddr);
			tvAddr.setTextColor(Color.RED);
			tvHintHandler.sendEmptyMessageDelayed(100, 3000);
		}
	}
}
