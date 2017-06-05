package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.pet.ADActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.CharacteristicServiceActivity;
import com.haotang.pet.ChoosePetActivityNew;
import com.haotang.pet.FeedBackActivity;
import com.haotang.pet.FostercareChooseroomActivity;
import com.haotang.pet.JointWorkShopDetail;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MainToBeauList;
import com.haotang.pet.MipcaActivityCapture;
import com.haotang.pet.PetCircleInsideActivity;
import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.SelectServiceAreaActivity;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.ServiceFeature;
import com.haotang.pet.ShopListActivity;
import com.haotang.pet.SwimDetailActivity;
import com.haotang.pet.TrainAppointMentActivity;
import com.haotang.pet.adapter.BannerAdapter;
import com.haotang.pet.adapter.BannerLoopAdapter;
import com.haotang.pet.adapter.HotBeauticianAdapter;
import com.haotang.pet.adapter.MainCharacteristicServiceAdapter;
import com.haotang.pet.adapter.MainHospitalAdapter;
import com.haotang.pet.adapter.MainPetCircleAdapter;
import com.haotang.pet.adapter.MainPetEncyclopediaAdapter;
import com.haotang.pet.adapter.MainServiceAdapter;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.City;
import com.haotang.pet.entity.HomeTopMsg;
import com.haotang.pet.entity.MainCharacteristicService;
import com.haotang.pet.entity.MainHospital;
import com.haotang.pet.entity.MainPetCircle;
import com.haotang.pet.entity.MainPetEncyclopedia;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.entity.PetCircle;
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
import com.haotang.pet.view.AutoTextView;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.ObservableScrollView;
import com.haotang.pet.view.ScrollViewListener;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;

@SuppressLint("NewApi")
public class MainFragment extends Fragment implements OnTouchListener,
		OnClickListener {
	private int screenWidth;
	private Activity mActivity;
	private SharedPreferenceUtil spUtil;
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private View view;
	private MyGridView gvServices;
	private int topMsgCount;
	private ArrayList<MainService> listMainService = new ArrayList<MainService>();
	private ArrayList<Beautician> listHotBeautician = new ArrayList<Beautician>();
	private ArrayList<HomeTopMsg> listTopMsg = new ArrayList<HomeTopMsg>();
	private ArrayList<HomeTopMsg> listHotMsg = new ArrayList<HomeTopMsg>();
	private ArrayList<HomeTopMsg> listMsg = new ArrayList<HomeTopMsg>();
	private ArrayList<Banner> listBanner1 = new ArrayList<Banner>();
	private RelativeLayout rlTopMsg;
	private AutoTextView atvTopMsg;
	private String areaName;
	private ArrayList<City> citysList = new ArrayList<City>();
	private List<MainPetCircle> listPetCircle = new ArrayList<MainPetCircle>();
	private List<MainCharacteristicService> listCharacteristicService = new ArrayList<MainCharacteristicService>();
	private List<MainHospital> listHospital = new ArrayList<MainHospital>();
	private List<MainPetEncyclopedia> listPetEncyclopedia = new ArrayList<MainPetEncyclopedia>();
	private PetCircle circle = new PetCircle();
	private PopupWindow pWin;
	private RelativeLayout rlHotBeautician;
	private BannerLoopAdapter adapterBanner1;
	private MainServiceAdapter adapterMainService;
	private int areaid;
	private BannerAdapter adapterBanner5;
	private ArrayList<Banner> listBanner5 = new ArrayList<Banner>();
	private String addressName;
	private RelativeLayout rl_mainfragment_jyzb;
	private RelativeLayout rl_mainfragment_jyzb_delete;
	private String liveTag;
	private String liveUrl;
	private TextView tv_mainfragment_jyzb_name;
	private double liveLat;
	private double liveLng;
	private RelativeLayout rl_mainfragmentcontent_community;
	private TextView tv_mainfragmentcontent_community_other;
	private ImageView iv_mainfragmentcontent_community_other;
	private View vw_mainfragmentcontent_community_other;
	private ImageView iv_mainfragmentcontent_newred;
	private RollPagerView rvpBanner1;
	private RollPagerView rvpBanner5;
	private TextView tvHotBeauticianTitle;
	private String xxAddressName;
	private MProgressDialog pDialog;
	private ImageView iv_mainfragment_scan;
	private ImageView iv_mainfragment_tousu;
	private TextView tv_mainfragment_areaname;
	private int circleId;
	private int circleType;
	private String rechargeActivityUrl;
	private int shopid;
	private HorizontalListView hlv_mainfragment_beautician;
	private MListview mlv_mainfragment_petcircle;
	private RelativeLayout rl_mainfragment_tsfw;
	private TextView tv_mainfragment_tsfw;
	private View vw_mainfragment_tsfw;
	private RelativeLayout rl_mainfragment_tjyy;
	private TextView tv_mainfragment_tjyy;
	private View vw_mainfragment_tjyy;
	private RelativeLayout rl_mainfragment_cwbk;
	private TextView tv_mainfragment_cwbk;
	private View vw_mainfragment_cwbk;
	private MListview mlv_mainfragment_service;
	private int bottomIndex = 1;
	private ObservableScrollView osv_mainfrag;
	private int scrolledX;
	private int scrolledY;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Global.LIVEDELAYEDGONE:
				spUtil.saveBoolean("isExit", false);
				rl_mainfragment_jyzb.setVisibility(View.GONE);
				break;
			case Global.TOPMSG_SCROLL:
				atvTopMsg.next();
				topMsgCount++;
				if (topMsgCount >= Integer.MAX_VALUE)
					topMsgCount = listMsg.size();
				if (listMsg.size() > 1) {
					String text = listMsg.get(topMsgCount % listMsg.size()).title;
					SpannableString ss = new SpannableString(text);
					ss.setSpan(new ForegroundColorSpan(mActivity.getResources()
							.getColor(R.color.aD1494F)), 0, "【最新】 ".length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					atvTopMsg.setText(ss);
					ImageLoaderUtil.loadImg(
							listMsg.get(topMsgCount % listMsg.size()).pic,
							iv_mainfragmentcontent_newred, 0, View.VISIBLE,
							View.GONE, null);
					mHandler.sendEmptyMessageDelayed(Global.TOPMSG_SCROLL, 3000);
				}
				break;
			case 1000:
				pDialog.closeDialog();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public MainFragment() {
		super();
	}

	public MainFragment(double lat, double lng) {
		this.liveLat = lat;
		this.liveLng = lng;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		initBD();
		pDialog = new MProgressDialog(activity);
		pDialog.showDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mainfragmentnew, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = getView();
		spUtil = SharedPreferenceUtil.getInstance(mActivity);
		screenWidth = Utils.getDisplayMetrics(mActivity)[0];
		findView();
		setView();
	}

	private void findView() {
		osv_mainfrag = (ObservableScrollView) view
				.findViewById(R.id.osv_mainfrag);
		mlv_mainfragment_service = (MListview) view
				.findViewById(R.id.mlv_mainfragment_service);
		rl_mainfragment_tsfw = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragment_tsfw);
		tv_mainfragment_tsfw = (TextView) view
				.findViewById(R.id.tv_mainfragment_tsfw);
		vw_mainfragment_tsfw = (View) view
				.findViewById(R.id.vw_mainfragment_tsfw);
		rl_mainfragment_tjyy = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragment_tjyy);
		tv_mainfragment_tjyy = (TextView) view
				.findViewById(R.id.tv_mainfragment_tjyy);
		vw_mainfragment_tjyy = (View) view
				.findViewById(R.id.vw_mainfragment_tjyy);
		rl_mainfragment_cwbk = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragment_cwbk);
		tv_mainfragment_cwbk = (TextView) view
				.findViewById(R.id.tv_mainfragment_cwbk);
		vw_mainfragment_cwbk = (View) view
				.findViewById(R.id.vw_mainfragment_cwbk);
		mlv_mainfragment_petcircle = (MListview) view
				.findViewById(R.id.mlv_mainfragment_petcircle);
		hlv_mainfragment_beautician = (HorizontalListView) view
				.findViewById(R.id.hlv_mainfragment_beautician);
		iv_mainfragment_scan = (ImageView) view
				.findViewById(R.id.iv_mainfragment_scan);
		iv_mainfragment_tousu = (ImageView) view
				.findViewById(R.id.iv_mainfragment_tousu);
		tv_mainfragment_areaname = (TextView) view
				.findViewById(R.id.tv_mainfragment_areaname);
		rlTopMsg = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragmentcontent_topmsg);
		atvTopMsg = (AutoTextView) view
				.findViewById(R.id.tv_mainfragmentcontent_topmsg_msg);
		rlHotBeautician = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragmentcontent_hotbeautician);
		gvServices = (MyGridView) view
				.findViewById(R.id.gv_mainfragmentcontent_service);
		rvpBanner1 = (RollPagerView) view
				.findViewById(R.id.rvp_mainfragment_banner1);
		rl_mainfragment_jyzb = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragment_jyzb);
		rl_mainfragment_jyzb_delete = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragment_jyzb_delete);
		tv_mainfragment_jyzb_name = (TextView) view
				.findViewById(R.id.tv_mainfragment_jyzb_name);
		rl_mainfragmentcontent_community = (RelativeLayout) view
				.findViewById(R.id.rl_mainfragmentcontent_community);
		tv_mainfragmentcontent_community_other = (TextView) view
				.findViewById(R.id.tv_mainfragmentcontent_community_other);
		iv_mainfragmentcontent_community_other = (ImageView) view
				.findViewById(R.id.iv_mainfragmentcontent_community_other);
		vw_mainfragmentcontent_community_other = (View) view
				.findViewById(R.id.vw_mainfragmentcontent_community_other);
		iv_mainfragmentcontent_newred = (ImageView) view
				.findViewById(R.id.iv_mainfragmentcontent_newred);
		tv_mainfragmentcontent_community_other.setOnClickListener(this);
		iv_mainfragmentcontent_community_other.setOnClickListener(this);
		rl_mainfragmentcontent_community.setOnClickListener(this);
		rl_mainfragment_jyzb.bringToFront();
		rl_mainfragment_jyzb_delete.setOnClickListener(this);
		rl_mainfragment_jyzb.setOnClickListener(this);
		atvTopMsg.setScreenWidth(screenWidth);
		autoLogin(liveLat, liveLng);
		tvHotBeauticianTitle = (TextView) view
				.findViewById(R.id.tv_mainfragmentcontent_hotbeautician);
		rvpBanner5 = (RollPagerView) view.findViewById(R.id.rvpBanner5);
		rl_mainfragment_tsfw.setOnClickListener(this);
		rl_mainfragment_tjyy.setOnClickListener(this);
		rl_mainfragment_cwbk.setOnClickListener(this);
	}

	/**
	 * 自动登录获取数据
	 */
	private void autoLogin(double lat, double lng) {
		String cellphone = spUtil.getString("cellphone", "");
		int userid = spUtil.getInt("userid", 0);
		if (!"".equals(cellphone) && 0 != userid) {
			CommUtil.loginAuto(mActivity, spUtil.getString("cellphone", ""),
					Global.getIMEI(mActivity),
					Global.getCurrentVersion(mActivity),
					spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
		}
	}

	private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("user") && !jData.isNull("user")) {
							JSONObject jUser = jData.getJSONObject("user");

							if (jUser.has("areacode")
									&& !jUser.isNull("areacode")) {
								spUtil.saveInt("upRegionId",
										jUser.getInt("areacode"));
							} else {
								spUtil.removeData("upRegionId");
							}
							if (jUser.has("shopName")
									&& !jUser.isNull("shopName")) {
								spUtil.saveString("upShopName",
										jUser.getString("shopName"));
							} else {
								spUtil.removeData("upShopName");
							}
							if (jUser.has("shopId") && !jUser.isNull("shopId")) {
								spUtil.saveInt("shopid", jUser.getInt("shopId"));
							} else {
								spUtil.removeData("shopid");
							}
							if (jUser.has("areaId") && !jUser.isNull("areaId")) {
								spUtil.saveInt("areaid", jUser.getInt("areaId"));
							} else {
								spUtil.removeData("areaid");
							}
							if (jUser.has("areaName")
									&& !jUser.isNull("areaName")) {
								spUtil.saveString("areaname",
										jUser.getString("areaName"));
							} else {
								spUtil.removeData("areaname");
							}
							if (jUser.has("homeAddress")
									&& !jUser.isNull("homeAddress")) {
								JSONObject jAddr = jUser
										.getJSONObject("homeAddress");
								if (jAddr.has("Customer_AddressId")
										&& !jAddr.isNull("Customer_AddressId")) {
									spUtil.saveInt("addressid",
											jAddr.getInt("Customer_AddressId"));
								}
								if (jAddr.has("lat") && !jAddr.isNull("lat")) {
									spUtil.saveString("lat",
											jAddr.getDouble("lat") + "");
								}
								if (jAddr.has("lng") && !jAddr.isNull("lng")) {
									spUtil.saveString("lng",
											jAddr.getDouble("lng") + "");
								}
								if (jAddr.has("address")
										&& !jAddr.isNull("address")) {
									spUtil.saveString("address",
											jAddr.getString("address"));
								}
							} else {
								spUtil.removeData("addressid");
								spUtil.removeData("lat");
								spUtil.removeData("lng");
								spUtil.removeData("address");
							}

							if (jUser.has("pet") && !jUser.isNull("pet")) {
								JSONObject jPet = jUser.getJSONObject("pet");
								if (jPet.has("isCerti")
										&& !jPet.isNull("isCerti")) {
									spUtil.saveInt("isCerti",
											jPet.getInt("isCerti"));
								}
							} else {
								spUtil.removeData("isCerti");
							}
							if (jUser.has("live") && !jUser.isNull("live")) {
								JSONObject jlive = jUser.getJSONObject("live");
								if (jlive.has("tag") && !jlive.isNull("tag")) {
									liveTag = jlive.getString("tag");
									boolean isExit = spUtil.getBoolean(
											"isExit", false);
									if (!TextUtils.isEmpty(liveTag)) {
										if (isExit) {
											tv_mainfragment_jyzb_name
													.setText(liveTag);
											rl_mainfragment_jyzb
													.setVisibility(View.VISIBLE);
											mHandler.sendEmptyMessageDelayed(
													Global.LIVEDELAYEDGONE,
													5000);
										} else {
											rl_mainfragment_jyzb
													.setVisibility(View.GONE);
										}
									} else {
										rl_mainfragment_jyzb
												.setVisibility(View.GONE);
									}
								}
								if (jlive.has("url") && !jlive.isNull("url")) {
									liveUrl = jlive.getString("url");
								}
							}
						}
					}
				}
			} catch (JSONException e) {
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	private void setView() {
		if (areaName != null && !"".equals(areaName)) {
			tvHotBeauticianTitle.setText("热门美容师" + areaName);
		}
		listMainService.clear();
		adapterMainService = new MainServiceAdapter(mActivity, listMainService);
		gvServices.setAdapter(adapterMainService);
		listBanner1.clear();
		adapterBanner1 = new BannerLoopAdapter(rvpBanner1, listBanner1);
		rvpBanner1.setAdapter(adapterBanner1);
		listBanner5.clear();
		adapterBanner5 = new BannerAdapter(5, listBanner5);
		rvpBanner5.setAdapter(adapterBanner5);
		osv_mainfrag.setScrollViewListener(new ScrollViewListener() {
			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x,
					int y, int oldx, int oldy) {
				scrolledX = x;
				scrolledY = y;
			}
		});

		tv_mainfragment_areaname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,
						SelectServiceAreaActivity.class);
				intent.putExtra("areaid", areaid);
				startActivityForResult(intent, Global.CITYREQUESTCODE);
				UmengStatistics.UmengEventStatistics(mActivity,
						Global.UmengEventID.click_HomePage_AreaSelect);
			}
		});
		iv_mainfragment_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mActivity, MipcaActivityCapture.class));
				UmengStatistics.UmengEventStatistics(mActivity,
						Global.UmengEventID.click_HomePage_Scan);
			}
		});
		iv_mainfragment_tousu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext(FeedBackActivity.class, 0, 0, 0, 0);
				UmengStatistics.UmengEventStatistics(mActivity,
						Global.UmengEventID.click_HomePage_Complaints);
			}
		});
		rlTopMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listTopMsg.size() > 0) {
					UmengStatistics.UmengEventStatistics(mActivity,
							Global.UmengEventID.click_HomePage_Headlines);
					Intent intent = new Intent(mActivity, ADActivity.class);
					intent.putExtra("url",
							listMsg.get(topMsgCount % listMsg.size()).url);
					intent.putExtra("previous", Global.TOPMSG_TO_AD);
					mActivity.startActivity(intent);
				}
			}
		});
		rlHotBeautician.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengStatistics.UmengEventStatistics(mActivity,
						Global.UmengEventID.click_HomePage_PopularList);
				goNext(MainToBeauList.class, 0, 0, 0, 0);
			}
		});
		gvServices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (listMainService != null && listMainService.size() > 0
						&& listMainService.size() > position) {
					MainService mainService = listMainService.get(position);
					if (mainService != null) {
						goService(mainService);
					}
				}
			}
		});
		hlv_mainfragment_beautician
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						UmengStatistics
								.UmengEventStatistics(
										mActivity,
										Global.UmengEventID.click_HomePage_PopularSelect);
						goNext(BeauticianDetailActivity.class,
								listHotBeautician.get(position).id, 0);
					}
				});

		mlv_mainfragment_petcircle
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						UmengStatistics
								.UmengEventStatistics(
										mActivity,
										Global.UmengEventID.click_HomePage_PetRingDetails);
						if (listPetCircle != null && listPetCircle.size() > 0
								&& listPetCircle.size() > position) {
							MainPetCircle mainPetCircle = listPetCircle
									.get(position);
							if (mainPetCircle != null) {
								startActivity(new Intent(mActivity,
										PetCircleInsideDetailActivity.class)
										.putExtra("postId",
												mainPetCircle.postId));
							}
						}
					}
				});
		mlv_mainfragment_service
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (bottomIndex) {
						case 1:// 特色服务
							UmengStatistics
									.UmengEventStatistics(
											mActivity,
											Global.UmengEventID.click_HomePage_CharacteristicDetails);
							if (listCharacteristicService != null
									&& listCharacteristicService.size() > 0
									&& listCharacteristicService.size() > position) {
								MainCharacteristicService mainCharacteristicService = listCharacteristicService
										.get(position);
								if (mainCharacteristicService != null) {
									String name = String
											.valueOf(mainCharacteristicService.PetServiceTypeForListId);
									if (name != null
											&& !TextUtils.isEmpty(name)) {
										if (areaid > 0) {
											if (isLogin() && hasPet()) {
												if (spUtil.getInt("tareaid", 0) == 100) {
													goNext(ShopListActivity.class,
															name,
															Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST,
															spUtil.getInt(
																	"petid", -1),
															mainCharacteristicService.PetServiceTypeForListId);
												} else {
													goNext(ServiceFeature.class,
															name,
															Global.PRE_MAINFRAGMENT,
															0, 0);
												}
											} else {
												goNext(ChoosePetActivityNew.class,
														name,
														Global.SERVICEFEATURE_TO_PETLIST,
														0,
														mainCharacteristicService.PetServiceTypeForListId);
											}
										} else {
											Intent intent = new Intent(
													mActivity,
													SelectServiceAreaActivity.class);
											intent.putExtra("areaid", areaid);
											intent.putExtra("servicename", name);
											if (isLogin() && hasPet()) {
												intent.putExtra("previous",
														Global.PRE_MAINFRAGMENT);
											} else {
												intent.putExtra(
														"previous",
														Global.SERVICEFEATURE_TO_PETLIST);
											}
											startActivityForResult(intent,
													Global.CITYREQUESTCODE);
										}
									}
								}
							}
							break;
						case 2:// 推荐医院
							UmengStatistics
									.UmengEventStatistics(
											mActivity,
											Global.UmengEventID.click_HomePage_Hospital);
							if (listHospital != null && listHospital.size() > 0
									&& listHospital.size() > position) {
								MainHospital mainHospital = listHospital
										.get(position);
								if (mainHospital != null) {
									int HospitalId = mainHospital.HospitalId;
									startActivity(new Intent(mActivity,
											JointWorkShopDetail.class)
											.putExtra("id", HospitalId)
											.putExtra("type", 3));
								}
							}
							break;
						case 3:// 宠物百科
							UmengStatistics
									.UmengEventStatistics(
											mActivity,
											Global.UmengEventID.click_HomePage_Encyclopedia);
							if (listPetEncyclopedia != null
									&& listPetEncyclopedia.size() > 0
									&& listPetEncyclopedia.size() > position) {
								MainPetEncyclopedia mainPetEncyclopedia = listPetEncyclopedia
										.get(position);
								if (mainPetEncyclopedia != null) {
									String url = mainPetEncyclopedia.url;
									if (url != null && !TextUtils.isEmpty(url)) {
										startActivity(new Intent(mActivity,
												ADActivity.class).putExtra(
												"url", url));
									}
								}
							}
							break;

						default:
							break;
						}
					}
				});
		getHomeIndex();
	}

	private void setPetAddress() {
		if (Utils.isStrNull(addressName)) {
			tv_mainfragment_areaname.setText(addressName + xxAddressName);
		} else {
			if (Utils.isStrNull(areaName)) {
				tv_mainfragment_areaname.setText("宠物位置：" + areaName);
			} else {
				tv_mainfragment_areaname.setText("请输入宠物地址");
			}
		}
	}

	private void initBD() {
		mLocationClient = new LocationClient(mActivity);
		mLocationListener = new MLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private void goNext(Class clazz, int id, int type) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		intent.putExtra("areaName", areaName);
		intent.putExtra("areaid", areaid);
		intent.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
		startActivity(intent);
	}

	private void goNextToSwim(Class clazz, int id, int type) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.MAIN_TO_SWIM_DETAIL);
		startActivity(intent);
	}

	private void goNextToTrain(Class clazz, int id, int type) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.MAIN_TO_TRAIN_DETAIL);
		startActivity(intent);
	}

	private void goNext(Class clazz, int serviceid, int servicetype,
			int previous, int petid) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("previous", previous);
		intent.putExtra("petid", petid);
		startActivity(intent);
	}

	private void goNext(Class clazz, String servicename, int previous,
			int petid, int typeId) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("servicename", servicename);
		intent.putExtra("previous", previous);
		intent.putExtra("petid", petid);
		intent.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
		startActivity(intent);
	}

	private void setNewOrHot(int point, int newOrHot) {
		if (newOrHot == 0) {
			spUtil.removeData("hotPoint" + point);
			spUtil.removeData("newPoint" + point);
		} else if (newOrHot == 1) {// 热门
			int hotPoint = spUtil.getInt("hotPoint" + point, 0);
			hotPoint++;
			spUtil.saveInt("hotPoint" + point, hotPoint);
		} else if (newOrHot == 2) {// 最新
			int newPoint = spUtil.getInt("newPoint" + point, 0);
			newPoint++;
			spUtil.saveInt("newPoint" + point, newPoint);
		}
		adapterMainService.notifyDataSetChanged();
	}

	private void goService(MainService mainService) {
		int point = mainService.point;
		String url = mainService.url;
		int newOrHot = mainService.newOrHot;
		setNewOrHot(point, newOrHot);
		if (url != null && !TextUtils.isEmpty(url)) {
			switch (point) {
			case 7:// 犬证
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_DogcardSelect);
				spUtil.removeData("charactservice");
				break;
			default:
				break;
			}
			startActivity(new Intent(mActivity, ADActivity.class).putExtra(
					"url", url));
		} else {
			switch (point) {
			case 1:// 洗澡
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_BathSelect);
				spUtil.removeData("charactservice");
				if (areaid > 0) {
					if (isLogin() && hasPet() && hasService(1)) {
						if (spUtil.getInt("tareaid", 0) == 100) {
							goNext(ShopListActivity.class,
									1,
									1,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
									spUtil.getInt("petid", -1));
						} else {
							goNext(ServiceActivity.class, 1, 1,
									Global.PRE_MAINFRAGMENT, 0);
						}
					} else {
						goNext(ChoosePetActivityNew.class, 1, 1,
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY, 0);
					}
				} else {
					Intent intent = new Intent(mActivity,
							SelectServiceAreaActivity.class);
					intent.putExtra("areaid", areaid);
					intent.putExtra("serviceid", 1);
					intent.putExtra("servicetype", 1);
					if (isLogin() && hasPet() && hasService(1)) {
						intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
					} else {
						intent.putExtra("previous",
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					}
					startActivityForResult(intent, Global.CITYREQUESTCODE);
				}
				break;
			case 2:// 美容
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_BeautySelect);
				spUtil.removeData("charactservice");
				if (areaid > 0) {
					if (isLogin() && hasPet() && hasService(2)) {
						if (spUtil.getInt("tareaid", 0) == 100) {
							goNext(ShopListActivity.class,
									2,
									2,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
									spUtil.getInt("petid", -1));
						} else {
							goNext(ServiceActivity.class, 2, 2,
									Global.PRE_MAINFRAGMENT, 0);
						}
					} else {
						goNext(ChoosePetActivityNew.class, 2, 2,
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY, 0);
					}
				} else {
					Intent intent = new Intent(mActivity,
							SelectServiceAreaActivity.class);
					intent.putExtra("areaid", areaid);
					intent.putExtra("serviceid", 2);
					intent.putExtra("servicetype", 2);
					if (isLogin() && hasPet() && hasService(2)) {
						intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
					} else {
						intent.putExtra("previous",
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					}
					startActivityForResult(intent, Global.CITYREQUESTCODE);
				}
				break;
			case 3:// 寄养
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_FosterCareSelect);
				if (isLogin() && hasPet()) {
					goNext(FostercareChooseroomActivity.class,
							100,
							100,
							Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET,
							0);
				} else {
					goNext(ChoosePetActivityNew.class, 100, 100,
							Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT, 0);
				}
				break;
			case 4:// 游泳
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_SwimmingSelect);
				spUtil.removeData("charactservice");
				if (isLogin()) {
					if (spUtil.getInt("petkind", -1) == 1) {
						goNextToSwim(SwimDetailActivity.class, 0, 0);
					} else {
						// 不仅仅代表未登录
						goNextUnLoginOrCanNotSwim(Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
					}
				} else {
					goNextUnLoginOrCanNotSwim(Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
				}
				break;
			case 5:// 训犬
				UmengStatistics.UmengEventStatistics(getActivity(),
						Global.UmengEventID.click_HomePage_TrainingDogSelect);
				if (isLogin()) {
					if (spUtil.getInt("petkind", -1) == 1) {
						goNextToTrain(TrainAppointMentActivity.class, 0, 0);
					} else {
						// //不仅仅代表未登录
						goNextUnLoginOrCanNotSwim(Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
					}
				} else {
					goNextUnLoginOrCanNotSwim(Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
				}
				break;
			case 6:// 特色服务
				UmengStatistics
						.UmengEventStatistics(
								getActivity(),
								Global.UmengEventID.click_HomePage_CharacteristicSelect);
				if (areaid > 0) {
					goNext(CharacteristicServiceActivity.class, 0, 0, 0, 0);
				} else {
					Intent intent = new Intent(mActivity,
							SelectServiceAreaActivity.class);
					intent.putExtra("areaid", areaid);
					intent.putExtra("previous", Global.MAIN_TO_AREALIST);
					startActivityForResult(intent, Global.CITYREQUESTCODE);
				}
				break;
			default:
				break;
			}
		}
	}

	private void goNextUnLoginOrCanNotSwim(int pre) {
		Intent intent = new Intent(mActivity, ChoosePetActivityNew.class);
		intent.putExtra("previous", pre);
		startActivity(intent);
	}

	private void setLayoutGone() {
		rlHotBeautician.setVisibility(View.GONE);
		rlTopMsg.setVisibility(View.GONE);
		hlv_mainfragment_beautician.setVisibility(View.GONE);
	}

	private void getHomeIndex() {
		if (rvpBanner1 != null) {
			rvpBanner1.pause();
		}
		if (rvpBanner5 != null) {
			rvpBanner5.pause();
		}
		setLayoutGone();
		String HomePageData = spUtil.getString("HomePageData", null);
		if (HomePageData != null && !TextUtils.isEmpty(HomePageData)) {
			processData(HomePageData);
		}
		CommUtil.getHomeData(mActivity, areaid, System.currentTimeMillis(),
				homeIndexHandler);
	}

	private AsyncHttpResponseHandler homeIndexHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			String HomePageData = spUtil.getString("HomePageData", null);
			if (HomePageData != null && !TextUtils.isEmpty(HomePageData)) {
				processData(HomePageData);
			}
			rvpBanner5.setVisibility(View.GONE);
			mHandler.sendEmptyMessageDelayed(1000, 800);
		}
	};

	private void processData(String str) {
		Log.e("TAG", "processData");
		try {
			JSONObject jobj = new JSONObject(str);
			int code = jobj.getInt("code");
			if (code == 0) {
				if (jobj.has("data") && !jobj.isNull("data")) {
					spUtil.saveString("HomePageData", str);
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("hpcBannerList")
							&& !jdata.isNull("hpcBannerList")) {
						JSONArray jarrPetCircle = jdata
								.getJSONArray("hpcBannerList");
						if (jarrPetCircle.length() > 0) {
							listPetCircle.clear();
							for (int i = 0; i < jarrPetCircle.length(); i++) {
								listPetCircle.add(MainPetCircle
										.json2Entity(jarrPetCircle
												.getJSONObject(i)));
							}
							mlv_mainfragment_petcircle
									.setVisibility(View.VISIBLE);
							mlv_mainfragment_petcircle
									.setAdapter(new MainPetCircleAdapter<MainPetCircle>(
											mActivity, listPetCircle));
						}
					}
					if (jdata.has("homePageCopy")
							&& !jdata.isNull("homePageCopy")) {
						JSONObject jobjHomePageCopy = jdata
								.getJSONObject("homePageCopy");
						if (jobjHomePageCopy.has("1")
								&& !jobjHomePageCopy.isNull("1")) {
							String string = jobjHomePageCopy.getString("1");
							Utils.setText(tv_mainfragment_tsfw, string, "",
									View.VISIBLE, View.INVISIBLE);
						}
						if (jobjHomePageCopy.has("2")
								&& !jobjHomePageCopy.isNull("2")) {
							String string = jobjHomePageCopy.getString("2");
							Utils.setText(tv_mainfragment_tjyy, string, "",
									View.VISIBLE, View.INVISIBLE);
						}
						if (jobjHomePageCopy.has("3")
								&& !jobjHomePageCopy.isNull("3")) {
							String string = jobjHomePageCopy.getString("3");
							Utils.setText(tv_mainfragment_cwbk, string, "",
									View.VISIBLE, View.INVISIBLE);
						}
					}
					if (jdata.has("services") && !jdata.isNull("services")) {
						JSONArray jarrCharacteristicService = jdata
								.getJSONArray("services");
						if (jarrCharacteristicService.length() > 0) {
							listCharacteristicService.clear();
							for (int i = 0; i < jarrCharacteristicService
									.length(); i++) {
								listCharacteristicService
										.add(MainCharacteristicService
												.json2Entity(jarrCharacteristicService
														.getJSONObject(i)));
							}
						}
					}
					if (jdata.has("hospitals") && !jdata.isNull("hospitals")) {
						JSONArray jarrHospital = jdata
								.getJSONArray("hospitals");
						if (jarrHospital.length() > 0) {
							listHospital.clear();
							for (int i = 0; i < jarrHospital.length(); i++) {
								listHospital.add(MainHospital
										.json2Entity(jarrHospital
												.getJSONObject(i)));
							}
						}
					}
					if (jdata.has("knowledges") && !jdata.isNull("knowledges")) {
						JSONArray jarrPetEncyclopedia = jdata
								.getJSONArray("knowledges");
						if (jarrPetEncyclopedia.length() > 0) {
							listPetEncyclopedia.clear();
							for (int i = 0; i < jarrPetEncyclopedia.length(); i++) {
								listPetEncyclopedia.add(MainPetEncyclopedia
										.json2Entity(jarrPetEncyclopedia
												.getJSONObject(i)));
							}
						}
					}
					setIndex(bottomIndex);
					listMsg.clear();
					if (jdata.has("newHeadLind")
							&& !jdata.isNull("newHeadLind")) {
						JSONArray jtopmsgarr = jdata
								.getJSONArray("newHeadLind");
						if (jtopmsgarr.length() > 0) {
							listTopMsg.clear();
							for (int i = 0; i < jtopmsgarr.length(); i++) {
								listTopMsg.add(HomeTopMsg
										.json2Entity(jtopmsgarr
												.getJSONObject(i)));
							}
							for (int j = 0; j < listTopMsg.size(); j++) {
								HomeTopMsg homeTopMsg = listTopMsg.get(j);
								if (homeTopMsg != null) {
									homeTopMsg.setTitle("【最新】 "
											+ homeTopMsg.getTitle());
								}
							}
							listMsg.addAll(listTopMsg);
						}
					}
					if (jdata.has("hotHeadLind")
							&& !jdata.isNull("hotHeadLind")) {
						JSONArray jhotmsgarr = jdata
								.getJSONArray("hotHeadLind");
						if (jhotmsgarr.length() > 0) {
							listHotMsg.clear();
							for (int i = 0; i < jhotmsgarr.length(); i++) {
								listHotMsg.add(HomeTopMsg
										.json2Entity(jhotmsgarr
												.getJSONObject(i)));
							}
							for (int j = 0; j < listHotMsg.size(); j++) {
								HomeTopMsg homeTopMsg = listHotMsg.get(j);
								if (homeTopMsg != null) {
									homeTopMsg.setTitle("【最热】 "
											+ homeTopMsg.getTitle());
								}
							}
							listMsg.addAll(listHotMsg);
						}
					}
					if (listMsg.size() > 0) {
						if (mHandler != null)
							mHandler.removeMessages(Global.TOPMSG_SCROLL);
						rlTopMsg.setVisibility(View.VISIBLE);
						topMsgCount = listMsg.size();
						String text = listMsg.get(0).title;
						SpannableString ss = new SpannableString(text);
						ss.setSpan(new ForegroundColorSpan(mActivity
								.getResources().getColor(R.color.aD1494F)), 0,
								"【最新】 ".length(),
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						atvTopMsg.setText(ss);
						ImageLoaderUtil.loadImg(listMsg.get(0).pic,
								iv_mainfragmentcontent_newred, 0, View.VISIBLE,
								View.GONE, null);
						if (listMsg.size() > 1)
							mHandler.sendEmptyMessageDelayed(
									Global.TOPMSG_SCROLL, 3000);
					} else {
						rlTopMsg.setVisibility(View.GONE);
					}
					if (jdata.has("icons") && !jdata.isNull("icons")) {
						JSONArray jiconarr = jdata.getJSONArray("icons");
						if (jiconarr.length() > 0) {
							listMainService.clear();
							for (int i = 0; i < jiconarr.length(); i++) {
								JSONObject jicon = jiconarr.getJSONObject(i);
								MainService ms = new MainService();
								if (jicon.has("pic") && !jicon.isNull("pic")) {
									ms.strimg = jicon.getString("pic");
								}
								if (jicon.has("txt") && !jicon.isNull("txt")) {
									ms.name = jicon.getString("txt");
								}
								if (jicon.has("url") && !jicon.isNull("url")) {
									ms.url = jicon.getString("url");
								}
								if (jicon.has("point")
										&& !jicon.isNull("point")) {
									ms.point = jicon.getInt("point");
								}
								if (jicon.has("newOrHot")
										&& !jicon.isNull("newOrHot")) {
									ms.newOrHot = jicon.getInt("newOrHot");
								}
								listMainService.add(i, ms);
							}
							adapterMainService.notifyDataSetChanged();
						}
					}
					listHotBeautician.clear();
					if (jdata.has("topWorkers")
							&& !jdata.isNull("topWorkers")
							&& Utils.JSON_TYPE.JSON_TYPE_OBJECT
									.equals(Utils.getJSONType(jdata
											.getString("topWorkers")))) {
						JSONObject topWorkersjsonObject = jdata
								.getJSONObject("topWorkers");
						if (topWorkersjsonObject.has("workers")
								&& !topWorkersjsonObject.isNull("workers")) {
							JSONArray workersjservicearr = topWorkersjsonObject
									.getJSONArray("workers");
							if (workersjservicearr.length() > 0) {
								listHotBeautician.clear();
								for (int i = 0; i < workersjservicearr.length(); i++) {
									Beautician btc = Beautician
											.json2Entity(workersjservicearr
													.getJSONObject(i));
									if (i <= 2) {
										btc.colorId = i;
									} else {
										if (listHotBeautician.get(i - 1).colorId == 0) {
											btc.colorId = 1;
										} else if (listHotBeautician.get(i - 1).colorId == 1) {
											btc.colorId = 2;
										} else if (listHotBeautician.get(i - 1).colorId == 2) {
											btc.colorId = 0;
										}
									}
									listHotBeautician.add(btc);
								}
								rlHotBeautician.setVisibility(View.VISIBLE);
								hlv_mainfragment_beautician
										.setVisibility(View.VISIBLE);
								hlv_mainfragment_beautician
										.setAdapter(new HotBeauticianAdapter<Beautician>(
												mActivity, listHotBeautician));
							}
						}
					} else {
						rlHotBeautician.setVisibility(View.GONE);
						hlv_mainfragment_beautician.setVisibility(View.GONE);
					}
					if (jdata.has("banner1") && !jdata.isNull("banner1")) {
						JSONArray jbanner2arr = jdata.getJSONArray("banner1");
						if (jbanner2arr.length() > 0) {
							listBanner1.clear();
							for (int i = 0; i < jbanner2arr.length(); i++) {
								listBanner1.add(Banner.json2Entity(jbanner2arr
										.getJSONObject(i)));
							}
							if (listBanner1.size() > 1) {
								rvpBanner1.setHintView(new ColorPointHintView(
										mActivity, Color.parseColor("#FE8A3F"),
										Color.parseColor("#FFE2D0")));
							}
							adapterBanner1.notifyDataSetChanged();
						}
					}
					if (jdata.has("banner5") && !jdata.isNull("banner5")) {
						JSONArray jbanner5arr = jdata.getJSONArray("banner5");
						if (jbanner5arr.length() > 0) {
							listBanner5.clear();
							rvpBanner5.setVisibility(View.VISIBLE);
							for (int i = 0; i < jbanner5arr.length(); i++) {
								listBanner5.add(Banner.json2Entity(jbanner5arr
										.getJSONObject(i)));
							}
							if (listBanner5.size() > 1) {
								rvpBanner5.setHintView(new ColorPointHintView(
										mActivity, Color.parseColor("#FE8A3F"),
										Color.parseColor("#FFE2D0")));
							}
							adapterBanner5.notifyDataSetChanged();
						} else {
							rvpBanner5.setVisibility(View.GONE);
						}
					} else {
						rvpBanner5.setVisibility(View.GONE);
					}

					rl_mainfragmentcontent_community
							.setVisibility(View.VISIBLE);
					if (jdata.has("homePostCustomInfo")
							&& !jdata.isNull("homePostCustomInfo")) {
						tv_mainfragmentcontent_community_other
								.setVisibility(View.VISIBLE);
						iv_mainfragmentcontent_community_other
								.setVisibility(View.VISIBLE);
						JSONObject homePostCustomInfo = jdata
								.getJSONObject("homePostCustomInfo");
						if (homePostCustomInfo.has("circleId")
								&& !homePostCustomInfo.isNull("circleId")) {
							circleId = homePostCustomInfo.getInt("circleId");
						}
						if (homePostCustomInfo.has("img")
								&& !homePostCustomInfo.isNull("img")) {
							if (homePostCustomInfo.has("title")
									&& !homePostCustomInfo.isNull("title")) {
								android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
										120, 55);
								layoutParams.rightMargin = 20;
								iv_mainfragmentcontent_community_other
										.setLayoutParams(layoutParams);
							} else {
								iv_mainfragmentcontent_community_other
										.setLayoutParams(new LinearLayout.LayoutParams(
												LayoutParams.MATCH_PARENT, 55));
							}
							iv_mainfragmentcontent_community_other
									.setVisibility(View.VISIBLE);
							String img = homePostCustomInfo.getString("img");
							ImageLoaderUtil.loadImg(img,
									iv_mainfragmentcontent_community_other,
									R.drawable.icon_production_default, null);
						} else {
							iv_mainfragmentcontent_community_other
									.setVisibility(View.GONE);
						}
						if (homePostCustomInfo.has("title")
								&& !homePostCustomInfo.isNull("title")) {
							boolean TAG_vw_mainfragmentcontent_community_other_click = spUtil
									.getBoolean(
											"TAG_vw_mainfragmentcontent_community_other_click",
											false);
							if (!TAG_vw_mainfragmentcontent_community_other_click) {
								vw_mainfragmentcontent_community_other
										.setVisibility(View.VISIBLE);
							}
							tv_mainfragmentcontent_community_other
									.setVisibility(View.VISIBLE);
							String title = homePostCustomInfo
									.getString("title");
							Utils.setStringTextGone(
									tv_mainfragmentcontent_community_other,
									title);
						} else {
							tv_mainfragmentcontent_community_other
									.setVisibility(View.GONE);
						}
						if (homePostCustomInfo.has("type")
								&& !homePostCustomInfo.isNull("type")) {
							circleType = homePostCustomInfo.getInt("type");
						}
					} else {
						tv_mainfragmentcontent_community_other
								.setVisibility(View.GONE);
						iv_mainfragmentcontent_community_other
								.setVisibility(View.GONE);
						// rl_mainfragmentcontent_community.setVisibility(View.GONE);
					}
					if (jdata.has("rechargeActivityUrl")
							&& !jdata.isNull("rechargeActivityUrl")) {
						rechargeActivityUrl = jdata
								.getString("rechargeActivityUrl");
						if (rechargeActivityUrl != null
								&& !TextUtils.isEmpty(rechargeActivityUrl)) {
							boolean TAG_MAINGOTOH5 = spUtil.getBoolean(
									"TAG_MAINGOTOH5", false);
							if (!TAG_MAINGOTOH5) {
								// showPopPhoto();//原活动界面
								spUtil.saveBoolean("TAG_MAINGOTOH5", true);
							}
						}
					}
					if (spUtil.getBoolean("selectMain", false) == true) {
						spUtil.saveBoolean("selectMain", false);
						osv_mainfrag.fullScroll(ScrollView.FOCUS_UP);
					} else {
						osv_mainfrag.scrollTo(scrolledX, scrolledY);
					}
				}
			} else {
				if (jobj.has("msg") && !jobj.isNull("msg")) {
					ToastUtil.showToastShortCenter(mActivity,
							jobj.getString("msg"));
				}
			}
			mHandler.sendEmptyMessageDelayed(1000, 800);
		} catch (Exception e) {
			mHandler.sendEmptyMessageDelayed(1000, 800);
			e.printStackTrace();
		}
	}

	private void showPopPhoto() {
		pWin = null;
		if (pWin == null) {
			final View view = View.inflate(mActivity,
					R.layout.pw_main_sendpost, null);
			ImageView iv_pw_main_sendpost = (ImageView) view
					.findViewById(R.id.iv_pw_main_sendpost);
			iv_pw_main_sendpost.setImageResource(R.drawable.mainto_hfive);
			ImageButton ib_pw_main_sendpost_close = (ImageButton) view
					.findViewById(R.id.ib_pw_main_sendpost_close);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
			pWin.showAtLocation(view, Gravity.CENTER, 0, 0);
			iv_pw_main_sendpost.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mActivity, ADActivity.class);
					intent.putExtra("url", rechargeActivityUrl);
					intent.putExtra("previous", 0);
					mActivity.startActivity(intent);
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

	private class MLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			// StringBuffer sb = new StringBuffer();
			// sb.append("lotitude="+location.getLatitude());
			// sb.append("longitude="+location.getLongitude());
			// sb.append("addr="+location.getAddrStr());
			// sb.append("city="+location.getCity());
			// sb.append("citycode="+location.getCityCode());
			// Utils.mLogError(sb.toString());
			spUtil.saveString("lat_home", location.getLatitude() + "");
			spUtil.saveString("lng_home", location.getLongitude() + "");
			// if(location.getCity()!=null&&!"".equals(location.getCity())&&isFirstLoc){
			// isFirstLoc = false;
			// tvCity.setText(location.getCity());
			// spUtil.saveString("city", location.getCity());
			// getCity();
			// }
			if (areaid <= 0)
				/*
				 * queryTradeAreaByLoc(location.getLatitude(),
				 * location.getLongitude());
				 */
				mLocationClient.stop();
		}

	}

	private void getCity() {
		citysList.clear();
		CommUtil.getCitys(mActivity, citysHanler);
	}

	private AsyncHttpResponseHandler citysHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("城市列表：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					JSONArray jData = jobj.getJSONArray("data");
					for (int i = 0; i < jData.length(); i++) {
						JSONObject jcity = jData.getJSONObject(i);
						citysList.add(City.json2City(jcity));
					}
					// 判断定位城市是否开通spUtil.getString("city", "")
					if (!serviceOpen(citysList, spUtil.getString("city", "")))
						showNoServiceCityDialog();
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

	private void queryTradeAreaByLoc(double lat, double lng) {
		CommUtil.queryTradeAreaByLoc(mActivity, lat, lng, tradeHanler);
	}

	private AsyncHttpResponseHandler tradeHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("位置商圈：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("area") && !jdata.isNull("area")) {
						JSONObject jarea = jdata.getJSONObject("area");
						if (jarea.has("shopName") && !jarea.isNull("shopName")) {
							areaName = jarea.getString("shopName");
							spUtil.saveString("tareaname", areaName);
							// showAreaHint(1);
						} else {
							// showAreaHint(2);
						}
						if (jarea.has("shopId") && !jarea.isNull("shopId")) {
							spUtil.saveInt("tshopid", jarea.getInt("shopId"));
						}
						if (jarea.has("areaId") && !jarea.isNull("areaId")) {
							spUtil.saveInt("tareaid", jarea.getInt("areaId"));
						}
					} else {
						// showAreaHint(2);
					}
				} else {
					// showAreaHint(2);
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

	// 返回true为开通，false为未开通
	private boolean serviceOpen(ArrayList<City> list, String city) {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.contains(city)
					|| city.contains(list.get(i).name)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private void showNoServiceCityDialog() {
		MDialog mDialog = new MDialog.Builder(mActivity).setTitle("提示")
				.setType(MDialog.DIALOGTYPE_ALERT).setMessage("您所在的城市尚未开通服务")
				.setOKStr("确定").positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				}).build();
		mDialog.show();
	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

	private boolean hasPet() {
		if (spUtil.getInt("petid", -1) > 0)
			return true;
		return false;
	}

	// 上个订单的宠物是否有该服务
	private boolean hasService(int serviceid) {
		for (int i = 0; i < MainActivity.petServices.length; i++) {
			if (serviceid == MainActivity.petServices[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (rvpBanner1 != null)
			rvpBanner1.pause();
		if (rvpBanner5 != null)
			rvpBanner5.pause();
		if (mHandler != null)
			mHandler.removeMessages(Global.TOPMSG_SCROLL);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (rvpBanner1 != null)
			rvpBanner1.resume();
		if (rvpBanner5 != null)
			rvpBanner5.resume();
		if (mHandler != null && listTopMsg != null && listTopMsg.size() > 1) {
			mHandler.sendEmptyMessageDelayed(Global.TOPMSG_SCROLL, 3000);
		}
		boolean isRestart = spUtil.getBoolean("isRestart", false);
		if (isRestart && Utils.checkLogin(mActivity)) {// 取上一单的地址，门店信息
			spUtil.saveInt("regionId", spUtil.getInt("upRegionId", -1));
			spUtil.saveInt("tareaid", spUtil.getInt("areaid", 0));
			spUtil.saveInt("tshopid", spUtil.getInt("shopid", 0));
			spUtil.saveString("tareaname", spUtil.getString("upShopName", ""));
			spUtil.saveString("addressName", spUtil.getString("address", ""));
			spUtil.saveString("xxAddressName", "");
		} else {// 取用户保存的地址，门店信息
			spUtil.saveInt("regionId", spUtil.getInt("saveregionId", -1));
			spUtil.saveInt("tareaid", spUtil.getInt("savetareaid", 0));
			spUtil.saveInt("tshopid", spUtil.getInt("savetshopid", 0));
			spUtil.saveString("tareaname",
					spUtil.getString("savetareaname", ""));
			spUtil.saveString("addressName",
					spUtil.getString("saveaddressName", ""));
			spUtil.saveString("xxAddressName",
					spUtil.getString("savexxAddressName", ""));
		}
		areaid = spUtil.getInt("tareaid", 0);
		shopid = spUtil.getInt("tshopid", 0);
		if (areaid == 100) {
			tvHotBeauticianTitle.setText("热门美容师");
		} else {
			areaName = spUtil.getString("tareaname", "");
			if (areaName != null && !"".equals(areaName)) {
				tvHotBeauticianTitle.setText("热门美容师" + areaName);
			}
		}
		areaName = spUtil.getString("tareaname", "");
		addressName = spUtil.getString("addressName", "");
		xxAddressName = spUtil.getString("xxAddressName", "");
		if (areaid > 0) {
			getHomeIndex();
		}
		setPetAddress();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return true;
	}

	private void setIndex(int num) {
		switch (num) {
		case 1:
			tv_mainfragment_tsfw.setTextColor(getResources().getColor(
					R.color.aD1494F));
			tv_mainfragment_tjyy.setTextColor(getResources().getColor(
					R.color.black));
			tv_mainfragment_cwbk.setTextColor(getResources().getColor(
					R.color.black));
			vw_mainfragment_tsfw.setVisibility(View.VISIBLE);
			vw_mainfragment_tjyy.setVisibility(View.GONE);
			vw_mainfragment_cwbk.setVisibility(View.GONE);
			mlv_mainfragment_service
					.setAdapter(new MainCharacteristicServiceAdapter<MainCharacteristicService>(
							mActivity, listCharacteristicService));
			break;
		case 2:
			tv_mainfragment_tjyy.setTextColor(getResources().getColor(
					R.color.aD1494F));
			tv_mainfragment_tsfw.setTextColor(getResources().getColor(
					R.color.black));
			tv_mainfragment_cwbk.setTextColor(getResources().getColor(
					R.color.black));
			vw_mainfragment_tjyy.setVisibility(View.VISIBLE);
			vw_mainfragment_tsfw.setVisibility(View.GONE);
			vw_mainfragment_cwbk.setVisibility(View.GONE);
			mlv_mainfragment_service
					.setAdapter(new MainHospitalAdapter<MainHospital>(
							mActivity, listHospital));
			break;
		case 3:
			tv_mainfragment_cwbk.setTextColor(getResources().getColor(
					R.color.aD1494F));
			tv_mainfragment_tjyy.setTextColor(getResources().getColor(
					R.color.black));
			tv_mainfragment_tsfw.setTextColor(getResources().getColor(
					R.color.black));
			vw_mainfragment_cwbk.setVisibility(View.VISIBLE);
			vw_mainfragment_tjyy.setVisibility(View.GONE);
			vw_mainfragment_tsfw.setVisibility(View.GONE);
			mlv_mainfragment_service
					.setAdapter(new MainPetEncyclopediaAdapter<MainPetEncyclopedia>(
							mActivity, listPetEncyclopedia));
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_mainfragment_tsfw:
			bottomIndex = 1;
			setIndex(bottomIndex);
			break;
		case R.id.rl_mainfragment_tjyy:
			bottomIndex = 2;
			setIndex(bottomIndex);
			break;
		case R.id.rl_mainfragment_cwbk:
			bottomIndex = 3;
			setIndex(bottomIndex);
			break;
		case R.id.iv_mainfragmentcontent_community_other:
			goToCircle();
			break;
		case R.id.tv_mainfragmentcontent_community_other:
			spUtil.saveBoolean(
					"TAG_vw_mainfragmentcontent_community_other_click", true);
			vw_mainfragmentcontent_community_other.setVisibility(View.GONE);
			goToCircle();
			break;
		case R.id.rl_mainfragmentcontent_community:// 跳转到社区列表
			UmengStatistics.UmengEventStatistics(mActivity,
					Global.UmengEventID.click_HomePage_PetRingList);
			MainActivity mActivity1 = (MainActivity) getActivity();
			mActivity1.setSelect(0);
			mActivity1.mTabHost.setCurrentTabByTag("circle");
			break;
		case R.id.rl_mainfragment_jyzb:
			// 跳转到直播列表界面
			Intent intent = new Intent(mActivity, ADActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			((Activity) mActivity).getIntent().putExtra(
					Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			intent.putExtra("url", liveUrl);
			mActivity.startActivity(intent);
			break;
		case R.id.rl_mainfragment_jyzb_delete:
			spUtil.saveBoolean("isExit", false);
			// 直播
			rl_mainfragment_jyzb.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void goToCircle() {
		MainActivity mActivity = (MainActivity) getActivity();
		switch (circleType) {
		case 1:
			mActivity.setSelect(1);
			mActivity.mTabHost.setCurrentTabByTag("circle");
			break;
		case 2:
			mActivity.setSelect(2);
			mActivity.mTabHost.setCurrentTabByTag("circle");
			break;
		case 3:
			goToPetCircle(circleId);
			break;
		default:
			break;
		}
	}

	private void goToPetCircle(int areaId) {
		circle.PostGroupId = areaId;
		Intent intent = new Intent(mActivity, PetCircleInsideActivity.class);
		intent.putExtra("petCircle", circle);
		mActivity.startActivity(intent);
	}
}
