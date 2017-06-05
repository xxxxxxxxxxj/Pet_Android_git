package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AllServiceAreaAdapter;
import com.haotang.pet.adapter.RegionsAdapter;
import com.haotang.pet.adapter.ServiceSearchAddressAdapter;
import com.haotang.pet.dao.PetAddressDao;
import com.haotang.pet.dao.PetXxAddressDao;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.PetAddressInfo;
import com.haotang.pet.entity.PetXxAddressInfo;
import com.haotang.pet.entity.ServiceAreaCodeBean;
import com.haotang.pet.entity.ServiceAreaCodeBean.AreasBean;
import com.haotang.pet.entity.ServiceAreaCodeBean.AreasBean.ShopsBean;
import com.haotang.pet.entity.SingelAreaBean;
import com.haotang.pet.entity.SingelAreaBean.AreaBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.ClearEditText;
import com.haotang.pet.view.SelectAddressPopupWindow;
import com.haotang.pet.view.SelectPicPopupWindow;

/**
 * <p>
 * Title:SelectServiceAreaActivity
 * </p>
 * <p>
 * Description:选择服务区域界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-15 下午8:59:36
 */
public class SelectServiceAreaActivity extends SuperActivity implements
		OnClickListener, OnGetPoiSearchResultListener, OnLayoutChangeListener {
	private static final int SINGEL_AREA = 2;
	private static final int ALL_AREA = 1;
	private ImageButton ibBack;
	private TextView tvTitle;
	private MProgressDialog pDialog;
	private String name = "";
	private int shopid;
	private int selectareaid;
	private Button btSave;
	private MapView mapView;
	private TextView tv_selectarea;
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	private AllServiceAreaAdapter adapter;
	private SelectPicPopupWindow menuWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.arealocal);
	private Overlay addOverlay;
	private int previous;
	private SharedPreferenceUtil spUtil;
	private int toBeOpen;
	private String toBeOpenTxt;
	private ClearEditText cet_selectarea;
	private List<ArrayMap<String, String>> SearchressList = new ArrayList<ArrayMap<String, String>>();
	private PoiSearch mPoiSearch = null;
	private boolean isLocation = true;
	private ServiceSearchAddressAdapter serviceSearchAddressAdapter;
	private SelectAddressPopupWindow selectAddressPopupWindow;
	private List<CommAddr> totalList = new ArrayList<CommAddr>();
	private boolean isDj;
	private static final int GV_SELECTAREA = 1;
	private static final int MBAIDUMAP = 2;
	private ClearEditText cet_selectarea_xxdz;
	private View vw_line1;
	private boolean isHavePetAdd;
	private double lng;
	private double lat;
	private PetAddressDao petAddressDao;
	private PetXxAddressDao petXxAddressDao;
	private List<OverlayOptions> listOverlayOptions = new ArrayList<OverlayOptions>();
	private ServiceAreaCodeBean data;
	private List<PetXxAddressInfo> petXxAddressInfoList = new ArrayList<PetXxAddressInfo>();
	private String pet_xxaddress_add;
	private boolean flag;
	private HorizontalScrollView hsv_selectservicearea_area;
	private GridView gv_selectservicearea_area;
	private List<AreasBean> listAreas;
	private RegionsAdapter<AreasBean> regionsAdapter;
	private View vw_line2;
	private RelativeLayout include1;
	private View vw_line;
	private int areaClickPosition;
	private int itemWidth;
	private int regionId;
	private boolean isDrop;
	private LinearLayout ll_selectarea_add;
	private RelativeLayout rl_selectservice_root;
	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;
	private int localSelectareaid;
	private String adressStr;
	private String xxAdressStr;
	private boolean isSerch;
	private boolean isClick;
	private boolean isFirSerch;
	private StringBuilder localAreaIds = new StringBuilder();
	private boolean isContain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		setView();
		setLinister();
		CommUtil.getServiceArea(this, homeIndexHandler);
	}

	private void setView() {
		adressStr = spUtil.getString("addressName", "")
				+ spUtil.getString("xxAddressName", "");
		xxAdressStr = spUtil.getString("xxAddressName", "");
		cet_selectarea.setText(adressStr);
		cet_selectarea_xxdz.setText(xxAdressStr);
		include1.bringToFront();
		ll_selectarea_add.bringToFront();
		vw_line.bringToFront();
		cet_selectarea_xxdz.bringToFront();
		vw_line1.bringToFront();
		hsv_selectservicearea_area.bringToFront();
		vw_line2.bringToFront();
		mBaiduMap = mapView.getMap();
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mapView.removeViewAt(1);
		mapView.showZoomControls(false);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// mBaiduMap.setMaxAndMinZoomLevel(18, 3);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12.0f);
		mBaiduMap.setMapStatus(msu);
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setCompassEnabled(false);
		tvTitle.setText("宠物所在区域");
		btSave.setVisibility(View.VISIBLE);
		if (previous == Global.PRE_MAINFRAGMENT
				|| previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY
				|| previous == Global.SERVICEFEATURE_TO_PETLIST
				|| previous == Global.MAIN_TO_AREALIST) {
			btSave.setText("下一步");
		} else {
			btSave.setText("保存");
		}
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
	}

	private void setLinister() {
		// 添加layout大小发生改变监听器
		rl_selectservice_root.addOnLayoutChangeListener(this);
		ibBack.setOnClickListener(this);
		btSave.setOnClickListener(this);
		cet_selectarea.setOnClickListener(this);
		cet_selectarea_xxdz.setOnClickListener(this);
		cet_selectarea.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				dismissPopAddress();
				dismissPopArea();
				try {
					if (!isDj) {
						String string = cet_selectarea.getText().toString();
						if (string != null && !TextUtils.isEmpty(string)
								&& string.length() > 0) {
							isFirSerch = true;
							isLocation = false;
							mPoiSearch.searchInCity(new PoiCitySearchOption()
									.city(SharedPreferenceUtil.getInstance(
											SelectServiceAreaActivity.this)
											.getString("city", "北京"))
									.keyword(string).pageNum(0));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (cet_selectarea.getText().toString().equals("")
						|| cet_selectarea.getText().toString().equals(null)) {
					dismissPopAddress();
					dismissPopArea();
				}
			}
		});
		cet_selectarea_xxdz.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				dismissPopAddress();
				dismissPopArea();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (cet_selectarea_xxdz.getText().toString().equals("")
						|| cet_selectarea_xxdz.getText().toString()
								.equals(null)) {
					dismissPopAddress();
					dismissPopArea();
				}
			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker arg0) {
				String title = arg0.getTitle();
				final int position = Integer.parseInt(title);
				ShopsBean shopsBean = listAreas.get(areaClickPosition)
						.getShops().get(position);
				String string = cet_selectarea.getText().toString();
				if (Utils.isStrNull(string)) {
					if (shopsBean != null) {
						if (localAreaIds.length() > 0
								&& !TextUtils.isEmpty(localAreaIds.toString())) {
							isContain = false;
							String[] split = localAreaIds.toString().split("&");
							if (split != null && split.length > 0) {
								for (int i = 0; i < split.length; i++) {
									if (Integer.parseInt(split[i]) == shopsBean
											.getAreaId()) {
										isContain = true;
										break;
									}
								}
							}
							if (isContain) {
								setItemClick(arg0, position, shopsBean);
							} else {
								setDialog(arg0, position, shopsBean, MBAIDUMAP);
							}
						} else {
							if (selectareaid != shopsBean.getAreaId()) {
								setDialog(arg0, position, shopsBean, MBAIDUMAP);
							} else {
								setItemClick(arg0, position, shopsBean);
							}
						}
					}
					return false;
				} else {
					if (shopsBean != null) {
						setItemClick(arg0, position, shopsBean);
					}
					return true;
				}
			}
		});
		gv_selectservicearea_area
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						if (listAreas != null && listAreas.size() > 0
								&& listAreas.size() > position) {
							final AreasBean areasBean = listAreas.get(position);
							if (areasBean != null) {
								String string = cet_selectarea.getText()
										.toString();
								if (position == listAreas.size() - 1) {
									selectareaid = 100;
								}
								if (Utils.isStrNull(string)
										&& position == listAreas.size() - 1
										&& localSelectareaid != selectareaid) {
									new AlertDialogNavAndPost(
											SelectServiceAreaActivity.this)
											.builder()
											.setTitle("切换服务区域")
											.setMsg("您所选服务区域与填写的地址不匹配,确定切换么?")
											.setPositiveButton("确定",
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															// 清空输入框
															cet_selectarea
																	.setText("");
															cet_selectarea_xxdz
																	.setText("");
															regionId = areasBean
																	.getAreacode();
															selectRegion(
																	areasBean
																			.getShops(),
																	position);
															// 添加店铺大头标
															setMaker(areasBean
																	.getShops());
														}
													})
											.setNegativeButton("取消",
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															selectareaid = localSelectareaid;
														}
													}).show();

								} else {
									regionId = areasBean.getAreacode();
									selectRegion(areasBean.getShops(), position);
									// 添加店铺大头标
									setMaker(areasBean.getShops());
								}
							}
						}
					}
				});
	}

	private void initView() {
		setContentView(R.layout.activity_select_service_area);
		vw_line = (View) findViewById(R.id.vw_line);
		include1 = (RelativeLayout) findViewById(R.id.include1);
		vw_line2 = (View) findViewById(R.id.vw_line2);
		hsv_selectservicearea_area = (HorizontalScrollView) findViewById(R.id.hsv_selectservicearea_area);
		gv_selectservicearea_area = (GridView) findViewById(R.id.gv_selectservicearea_area);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		btSave = (Button) findViewById(R.id.bt_titlebar_other);
		mapView = (MapView) findViewById(R.id.mv_select_servicearea);
		tv_selectarea = (TextView) findViewById(R.id.tv_selectarea);
		cet_selectarea = (ClearEditText) findViewById(R.id.cet_selectarea);
		cet_selectarea_xxdz = (ClearEditText) findViewById(R.id.cet_selectarea_xxdz);
		vw_line1 = (View) findViewById(R.id.vw_line1);
		ll_selectarea_add = (LinearLayout) findViewById(R.id.ll_selectarea_add);
		rl_selectservice_root = (RelativeLayout) findViewById(R.id.rl_selectservice_root);
	}

	private void initData() {
		isSerch = false;
		isClick = false;
		isFirSerch = false;
		spUtil = SharedPreferenceUtil.getInstance(this);
		String localAreaIdsStr = spUtil.getString("localAreaIdsStr", "");
		if (localAreaIdsStr != null && !TextUtils.isEmpty(localAreaIdsStr)) {
			localAreaIds.setLength(0);
			localAreaIds.append(localAreaIdsStr);
		}
		lat = Double.parseDouble(spUtil.getString("lat", "0"));
		lng = Double.parseDouble(spUtil.getString("lng", "0"));
		petAddressDao = new PetAddressDao(this);
		petXxAddressDao = new PetXxAddressDao(this);
		Intent intent = getIntent();
		previous = intent.getIntExtra("previous", 0);
		selectareaid = spUtil.getInt("tareaid", -1);
		localSelectareaid = selectareaid;
		regionId = spUtil.getInt("regionId", -1);
		pDialog = new MProgressDialog(this);
	}

	private void setDialog(final Marker arg0, final int position,
			final ShopsBean shopsBean, final int flag) {
		new AlertDialogNavAndPost(SelectServiceAreaActivity.this).builder()
				.setTitle("切换服务区域").setMsg("您所选服务区域与填写的地址不匹配,确定切换么?")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 清空数据
						localAreaIds.setLength(0);
						// 清空输入框
						cet_selectarea.setText("");
						cet_selectarea_xxdz.setText("");
						switch (flag) {
						case GV_SELECTAREA:
							selectArea(position, shopsBean);
							break;
						case MBAIDUMAP:
							setItemClick(arg0, position, shopsBean);
							break;
						default:
							break;
						}
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	private void setItemClick(Marker arg0, int position, ShopsBean shopsBean) {
		name = shopsBean.getShopName() != null ? shopsBean.getShopName() : "";
		shopid = shopsBean.getShopId();
		selectareaid = shopsBean.getAreaId();
		adapter.setCheckItem(position);
		// 绘制区域
		showArea(shopsBean.getPoints() != null ? shopsBean.getPoints() : "");
		// 移动地图
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(arg0.getPosition()).zoom(13.0f);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
		tv_selectarea.setText(shopsBean.getDesc1());
	}

	private void selectArea(int position, ShopsBean shopsBean) {
		adapter.setCheckItem(position);
		if (shopsBean != null) {
			toBeOpen = shopsBean.getToBeOpen();
			toBeOpenTxt = shopsBean.getToBeOpenTxt();
			name = shopsBean.getShopName() != null ? shopsBean.getShopName()
					: "";
			shopid = shopsBean.getShopId();
			selectareaid = shopsBean.getAreaId();
			if (!isDrop) {
				// dismissPopArea();
			} else {
				isDrop = false;
			}
			initDropMenu(shopsBean);
		}
	}

	private void dismissPopAddress() {
		if (selectAddressPopupWindow != null) {
			selectAddressPopupWindow.dismiss();
		}
	}

	protected void showDropAddress(boolean bool) {
		dismissPopAddress();
		selectAddressPopupWindow = new SelectAddressPopupWindow(this,
				onItemsOnClick);
		if (bool) {
			isHavePetAdd = true;
			selectAddressPopupWindow.tv_dropmenu_address
					.setVisibility(View.VISIBLE);
		} else {
			isHavePetAdd = false;
			if (flag) {
				selectAddressPopupWindow.tv_dropmenu_address
						.setVisibility(View.VISIBLE);
			} else {
				selectAddressPopupWindow.tv_dropmenu_address
						.setVisibility(View.GONE);
			}
		}
		selectAddressPopupWindow.showAsDropDown(cet_selectarea, 0, 0);
		serviceSearchAddressAdapter = new ServiceSearchAddressAdapter(this);
		selectAddressPopupWindow.lv_dropmenu_address
				.setAdapter(serviceSearchAddressAdapter);
	}

	private OnItemClickListener onItemsOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			isClick = true;
			selectAddressPopupWindow.setFocusable(true);
			cet_selectarea.setText("");
			cet_selectarea_xxdz.setText("");
			ArrayMap<String, String> mapSearchressList = SearchressList
					.get(position);
			String address = mapSearchressList.get("name");
			pet_xxaddress_add = mapSearchressList.get("address");
			String xxaddress = mapSearchressList.get("xxaddress");
			String strLat = mapSearchressList.get("lat");
			String strLng = mapSearchressList.get("lng");
			if (address != null && !TextUtils.isEmpty(address)) {
				boolean strLatNull = Utils.isStrNull(strLat);
				boolean strLngNull = Utils.isStrNull(strLng);
				if (strLatNull && strLngNull) {
					pDialog.showDialog();
					lat = Double.parseDouble(strLat);
					lng = Double.parseDouble(strLng);
					CommUtil.queryTradeAreaByLoc(
							SelectServiceAreaActivity.this, lat, lng,
							queryTradeAreaByLoc);
					if (isHavePetAdd) {
						cet_selectarea_xxdz.setVisibility(View.GONE);
						vw_line1.setVisibility(View.GONE);
					} else {
						cet_selectarea_xxdz.setVisibility(View.VISIBLE);
						if (xxaddress != null && !TextUtils.isEmpty(xxaddress)) {
							cet_selectarea_xxdz.setText(xxaddress);
						}
						vw_line1.setVisibility(View.VISIBLE);
					}
				}
				isDj = true;
				cet_selectarea.setText(address);
				dismissPopAddress();
				cet_selectarea_xxdz.setFocusable(true);
				cet_selectarea_xxdz.setFocusableInTouchMode(true);
				cet_selectarea_xxdz.requestFocus();
			}
		}
	};

	private AsyncHttpResponseHandler queryTradeAreaByLoc = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String result = new String(responseBody);
			Utils.mLogError("==-->获取距离用户最近的商圈 " + result);
			processData(result, SINGEL_AREA);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dismissPopArea();
		dismissPopAddress();
		return super.onTouchEvent(event);
	}

	private void dismissPopArea() {
		if (menuWindow != null) {
			if (menuWindow.isShowing()) {
				menuWindow.dismiss();
			}
		}
	}

	private void showArea(String points) {
		// 清除区域绘制的颜色
		if (addOverlay != null) {
			addOverlay.remove();
		}
		// 绘制区域
		List<LatLng> pts = new ArrayList<LatLng>();
		List<String> lat = new ArrayList<String>();
		List<String> lon = new ArrayList<String>();
		if (points != null && !TextUtils.isEmpty(points)) {
			Utils.mLogError("获取商圈信息：points = " + points);
			if (points.contains(",")) {
				String[] split = points.split(",");
				Utils.mLogError("获取商圈信息：points.length=" + split);
				if (split != null && split.length > 0 && split.length % 2 == 0) {
					for (int j = 0; j < split.length; j++) {
						if (j % 2 == 0) {
							lat.add(split[j]);
						} else {
							lon.add(split[j]);
						}
					}
					for (int k = 0; k < lon.size(); k++) {
						Utils.mLogError("获取商圈信息：lon=" + lon.get(k) + " : lat="
								+ lat.get(k));
						LatLng pt = new LatLng(Double.parseDouble(lon.get(k)),
								Double.parseDouble(lat.get(k)));
						pts.add(pt);
					}
					OverlayOptions ooPolygon = new PolygonOptions()
							.points(pts)
							.stroke(new Stroke(5, 0x000000FF))
							.fillColor(
									getResources().getColor(R.color.areacolor));
					addOverlay = mBaiduMap.addOverlay(ooPolygon);
				}
			}
		}
	}

	private void initDropMenu(ShopsBean shopsBean) {
		// 绘制区域
		showArea(shopsBean.getPoints() != null ? shopsBean.getPoints() : "");
		if (shopsBean.getDesc1() != null
				&& !TextUtils.isEmpty(shopsBean.getDesc1())) {
			tv_selectarea.setText(shopsBean.getDesc1());
		}
		// 移动地图
		LatLng llA = new LatLng(shopsBean.getLat() != 0.0 ? shopsBean.getLat()
				: 0.0, shopsBean.getLng() != 0.0 ? shopsBean.getLng() : 0.0);
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(llA).zoom(13.0f);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
	}

	private void showShare() {
		dismissPopArea();
		// 强制收起键盘
		goneJp();
		menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
		// 显示窗口
		menuWindow.showAsDropDown(vw_line2, 0, 0);
		menuWindow.gv_dropmenu
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// dismissPopArea();
						// 强制收起键盘
						goneJp();
						String string = cet_selectarea.getText().toString();
						ShopsBean shopsBean = listAreas.get(areaClickPosition)
								.getShops().get(position);
						if (Utils.isStrNull(string)) {
							if (shopsBean != null) {
								if (localAreaIds.length() > 0
										&& !TextUtils.isEmpty(localAreaIds
												.toString())) {
									isContain = false;
									String[] split = localAreaIds.toString()
											.split("&");
									if (split != null && split.length > 0) {
										for (int i = 0; i < split.length; i++) {
											if (Integer.parseInt(split[i]) == shopsBean
													.getAreaId()) {
												isContain = true;
												break;
											}
										}
									}
									if (isContain) {
										selectArea(position, shopsBean);
									} else {
										setDialog(null, position, shopsBean,
												GV_SELECTAREA);
									}
								} else {
									if (selectareaid != shopsBean.getAreaId()) {
										setDialog(null, position, shopsBean,
												GV_SELECTAREA);
									} else {
										selectArea(position, shopsBean);
									}
								}
							}
						} else {
							selectArea(position, shopsBean);
						}
					}
				});
	}

	private void goneJp() { // 强制收起键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_dropmenu_sq:
				dismissPopArea();
				break;

			default:
				break;
			}
		}
	};

	private AsyncHttpResponseHandler homeIndexHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String result = new String(responseBody);
			processData(result, ALL_AREA);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result, int flag) {
		Gson gson = new Gson();
		switch (flag) {
		case ALL_AREA:
			allAreaData(result, gson);
			break;
		case SINGEL_AREA:
			singelAreaData(result, gson);
			break;
		default:
			break;
		}
	}

	private void setArea() {
		isDrop = true;
		if (listAreas != null && listAreas.size() > 0) {
			List<ShopsBean> shops = null;
			// 选中行政区划
			for (int i = 0; i < listAreas.size(); i++) {
				AreasBean areasBean = listAreas.get(i);
				if (areasBean != null) {
					shops = areasBean.getShops();
					if (regionId == areasBean.getAreacode()) {
						selectRegion(shops, i);
						break;
					}
				}
			}
			// 添加店铺大头标
			setMaker(shops);
			// 选中店铺
			selectShop(shops);
		}
	}

	private void selectShop(List<ShopsBean> shops) {
		if (shops != null && shops.size() > 0) {
			// 选中店铺
			for (int j = 0; j < shops.size(); j++) {
				ShopsBean shopsBean = shops.get(j);
				if (shopsBean != null) {
					if (selectareaid == shopsBean.getAreaId()) {
						selectArea(j, shopsBean);
						break;
					}
				}
			}
		}
	}

	private void setMaker(List<ShopsBean> shops) {
		mBaiduMap.clear();
		if (shops != null && shops.size() > 0) {
			for (int j = 0; j < shops.size(); j++) {
				ShopsBean shopsBean = shops.get(j);
				if (shopsBean != null) {
					// 添加店铺大头标
					LatLng llA = new LatLng(
							shopsBean.getLat() != 0.0 ? shopsBean.getLat()
									: 0.0,
							shopsBean.getLng() != 0.0 ? shopsBean.getLng()
									: 0.0);
					View inflate = View
							.inflate(this, R.layout.infowindow, null);
					inflate.bringToFront();
					TextView tv_infowindow = (TextView) inflate
							.findViewById(R.id.tv_infowindow);
					if (shopsBean.getShopName() != null
							&& !TextUtils.isEmpty(shopsBean.getShopName())) {
						tv_infowindow.setText(shopsBean.getShopName());
					}
					MarkerOptions ooA = new MarkerOptions().position(llA)
							.icon(BitmapDescriptorFactory.fromView(inflate))
							.zIndex(9).draggable(true);
					String title = String.valueOf(j);
					ooA.title(title);
					mBaiduMap.addOverlay(ooA);
				}
			}
		}
	}

	private void selectRegion(List<ShopsBean> shops, int i) {
		areaClickPosition = i;
		regionsAdapter.setClickItem(areaClickPosition);
		ScollTo(areaClickPosition);
		showShare();
		if (shops != null && shops.size() > 0) {
			adapter = new AllServiceAreaAdapter(SelectServiceAreaActivity.this,
					shops);
			menuWindow.gv_dropmenu.setAdapter(adapter);
		}
	}

	private void singelAreaData(String result, Gson gson) {
		try {
			JSONObject jobj = new JSONObject(result);
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
								localAreaIds.setLength(0);
								for (int i = 0; i < area.size(); i++) {
									AreaBean areaBean = area.get(i);
									if (areaBean != null) {
										int areaId = areaBean.getAreaId();
										localAreaIds.append(areaId + "&");
									}
								}
								AreaBean areaBean = area.get(0);
								if (areaBean != null) {
									regionId = areaBean.getAreacode();
									selectareaid = areaBean.getAreaId();
									setArea();
								} else {
									if (listAreas != null
											&& listAreas.size() > 0) {
										regionId = listAreas.get(
												listAreas.size() - 1)
												.getAreacode();
										selectareaid = 100;
										setArea();
									}
								}
							} else {
								if (listAreas != null && listAreas.size() > 0) {
									regionId = listAreas.get(
											listAreas.size() - 1).getAreacode();
									selectareaid = 100;
									setArea();
								}
							}
						} else {
							if (listAreas != null && listAreas.size() > 0) {
								regionId = listAreas.get(listAreas.size() - 1)
										.getAreacode();
								selectareaid = 100;
								setArea();
							}
						}
					} else {
						if (listAreas != null && listAreas.size() > 0) {
							regionId = listAreas.get(listAreas.size() - 1)
									.getAreacode();
							selectareaid = 100;
							setArea();
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(this, msg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void allAreaData(String result, Gson gson) {
		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj.has("code") && !jobj.isNull("code")) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						data = gson.fromJson(jdata.toString(),
								ServiceAreaCodeBean.class);
						if (data != null) {
							tv_selectarea
									.setText(data.getIntroOtherArea() != null ? data
											.getIntroOtherArea()
											: "所有服务区围绕体验馆三公里范围");
							listAreas = data.getAreas();
							if (listAreas != null && listAreas.size() > 0) {
								// 设置行政区划列表
								int size = listAreas.size();
								int length = 80;
								DisplayMetrics dm = new DisplayMetrics();
								getWindowManager().getDefaultDisplay()
										.getMetrics(dm);
								float density = dm.density;
								int gridviewWidth = (int) (size * (length + 5) * density);
								itemWidth = (int) (length * density);
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										gridviewWidth,
										LinearLayout.LayoutParams.FILL_PARENT);
								gv_selectservicearea_area
										.setLayoutParams(params); // 重点
								gv_selectservicearea_area
										.setColumnWidth(itemWidth); // 重点
								gv_selectservicearea_area
										.setHorizontalSpacing(5); // 间距
								gv_selectservicearea_area
										.setStretchMode(GridView.NO_STRETCH);
								gv_selectservicearea_area
										.setGravity(Gravity.CENTER);// 位置居中
								gv_selectservicearea_area.setNumColumns(size); // 重点
								regionsAdapter = new RegionsAdapter<AreasBean>(
										SelectServiceAreaActivity.this,
										listAreas, 0);
								gv_selectservicearea_area
										.setAdapter(regionsAdapter);
								regionsAdapter.setData(listAreas);
								if (regionId >= 0) {
									setArea();
								} else {
									// 选中第一个区域
									selectRegion(listAreas.get(0).getShops(), 0);
								}
							}
							pDialog.closeDialog();
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(this, msg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ScollTo(final int dayposition2) {
		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				hsv_selectservicearea_area.smoothScrollTo((dayposition2 - 2)
						* itemWidth, 0);
			}
		}, 5);
	}

	public void addTasksToBaiduMapAsMarker() {
		mBaiduMap.clear();
		final OverlayManager overlayManager = new OverlayManager(mBaiduMap) {
			@Override
			public List getOverlayOptions() {
				return listOverlayOptions;
			}

			@Override
			public boolean onMarkerClick(Marker marker) {
				return true;
			}
		};
		overlayManager.addToMap();
		overlayManager.zoomToSpan();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cet_selectarea:
			cet_selectarea.setFocusable(true);
			isDj = false;
			dismissPopArea();
			String cellphone = spUtil.getString("cellphone", "");
			boolean strNull = Utils.isStrNull(cellphone);
			if (strNull) {// 用户已登录
				CommUtil.queryMyHotAddress(
						SharedPreferenceUtil.getInstance(this).getString(
								"cellphone", ""), Global.getIMEI(this), this,
						queryServiceAddress);
			} else {// 未登录
				setLocalAddress();
			}
			break;
		case R.id.cet_selectarea_xxdz:
			dismissPopArea();
			break;
		case R.id.ib_titlebar_back:// 返回
			finish();
			break;
		case R.id.bt_titlebar_other:// 完成
			if (toBeOpen == 1) {
				if (toBeOpenTxt != null && !TextUtils.isEmpty(toBeOpenTxt)) {
					ToastUtil.showToastShortCenter(this, toBeOpenTxt);
				}
			} else {
				if (selectareaid <= 0) {
					ToastUtil.showToastShortCenter(this, "请选择店铺");
				} else {
					String cet_selectarea_str = cet_selectarea.getText()
							.toString();
					String cet_selectarea_xxdz_str = cet_selectarea_xxdz
							.getText().toString();
					if (isHavePetAdd) {
						saveDataBack(cet_selectarea_str,
								cet_selectarea_xxdz_str);
					} else {
						boolean isDis = false;
						// 保存用户输入的地址为常用地址
						List<PetXxAddressInfo> all = petXxAddressDao.getAll();
						if (all != null) {
							if (all.size() > 0) {
								for (int i = 0; i < all.size(); i++) {
									PetXxAddressInfo petXxAddressInfo = all
											.get(i);
									double pet_xxaddress_lat = petXxAddressInfo
											.getPet_xxaddress_lat();
									double pet_xxaddress_lng = petXxAddressInfo
											.getPet_xxaddress_lng();
									if (lat == pet_xxaddress_lat
											&& lng == pet_xxaddress_lng) {
										isDis = true;
										break;
									}
								}
							}
						}
						if (!isDis && isDj) {
							petXxAddressDao.add(new PetXxAddressInfo(
									cet_selectarea_str, pet_xxaddress_add,
									cet_selectarea_xxdz_str, lat, lng, ""));
						}
						saveDataBack(cet_selectarea_str,
								cet_selectarea_xxdz_str);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private void setLocalAddress() {
		petXxAddressInfoList.clear();
		SearchressList.clear();
		petXxAddressInfoList.addAll(petXxAddressDao.getAll());
		if (petXxAddressInfoList.size() > 0) {
			for (int i = 0; i < petXxAddressInfoList.size(); i++) {
				ArrayMap<String, String> map = new ArrayMap<String, String>();
				PetXxAddressInfo petXxAddressInfo = petXxAddressInfoList.get(i);
				String pet_xxaddress_name = petXxAddressInfo
						.getPet_xxaddress_name();
				String pet_xxaddress_add = petXxAddressInfo
						.getPet_xxaddress_add();
				String pet_xxaddress_xxadd = petXxAddressInfo
						.getPet_xxaddress_xxadd();
				double pet_xxaddress_lat = petXxAddressInfo
						.getPet_xxaddress_lat();
				double pet_xxaddress_lng = petXxAddressInfo
						.getPet_xxaddress_lng();
				map.put("name", pet_xxaddress_name);
				map.put("address", pet_xxaddress_add);
				map.put("xxaddress", pet_xxaddress_xxadd);
				map.put("lat", pet_xxaddress_lat + "");
				map.put("lng", pet_xxaddress_lng + "");
				SearchressList.add(map);
			}
			flag = true;
			adapterSetData(false);
		}
	}

	/*
	 * private void setSaveDialog() { new
	 * AlertDialogNavAndPost(SelectServiceAreaActivity.this).builder()
	 * .setMsg("请填写您的详细地址哟~") .setPositiveButton("确定", new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * } }).show(); }
	 */

	private void saveDataBack(String cet_selectarea_str,
			String cet_selectarea_xxdz_str) {
		if (localAreaIds.length() > 0
				&& !TextUtils.isEmpty(localAreaIds.toString())) {
			spUtil.saveString("localAreaIdsStr", localAreaIds.toString());
		}
		if ((isFirSerch == true || isSerch == true) && isClick == false) {
			spUtil.saveString("saveaddressName", adressStr);
			spUtil.saveString("savexxAddressName", xxAdressStr);
		} else {
			spUtil.saveString("saveaddressName", cet_selectarea_str);
			spUtil.saveString("savexxAddressName", cet_selectarea_xxdz_str);
		}
		if (regionId == listAreas.get(listAreas.size() - 1).getAreacode()) {
			spUtil.saveInt("savetshopid", 0);
			spUtil.saveInt("savetareaid", 100);
			spUtil.saveInt("saveregionId", 0);
			spUtil.saveString("savetareaname", "其他区域");
		} else {
			spUtil.saveInt("savetshopid", shopid);
			spUtil.saveString("savetareaname", name);
			spUtil.saveInt("savetareaid", selectareaid);
			if (listAreas != null && listAreas.size() > 0) {
				boolean bool = false;
				for (int i = 0; i < listAreas.size(); i++) {
					if (bool) {
						break;
					}
					AreasBean areasBean = listAreas.get(i);
					if (areasBean != null) {
						int areacode = areasBean.getAreacode();
						List<ShopsBean> shops = areasBean.getShops();
						if (shops != null && shops.size() > 0) {
							for (int j = 0; j < shops.size(); j++) {
								ShopsBean shopsBean = shops.get(j);
								if (shopsBean != null) {
									if (selectareaid == shopsBean.getAreaId()) {
										regionId = areacode;
										spUtil.saveInt("saveregionId", regionId);
										bool = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		// 查询数据库看是否保存的有宠物地址信息
		PetAddressInfo petAddressInfo = new PetAddressInfo(1, "", lat, lng, "");
		List<PetAddressInfo> all = petAddressDao.getAll();
		if (all != null) {
			if (all.size() > 0) {
				petAddressDao.update(petAddressInfo);
			} else {
				petAddressDao.add(petAddressInfo);
			}
		} else {
			petAddressDao.add(petAddressInfo);
		}
		spUtil.saveBoolean("isRestart", false);
		spUtil.saveInt("regionId", spUtil.getInt("saveregionId", -1));
		spUtil.saveInt("tareaid", spUtil.getInt("savetareaid", 0));
		spUtil.saveInt("tshopid", spUtil.getInt("savetshopid", 0));
		spUtil.saveString("tareaname", spUtil.getString("savetareaname", ""));
		spUtil.saveString("addressName",
				spUtil.getString("saveaddressName", ""));
		spUtil.saveString("xxAddressName",
				spUtil.getString("savexxAddressName", ""));
		back();
	}

	private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->query address " + new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						totalList.clear();
						SearchressList.clear();
						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								totalList.add(CommAddr.json2Entity(jsonArray
										.getJSONObject(i)));
							}
							for (int i = 0; i < totalList.size(); i++) {
								ArrayMap<String, String> map = new ArrayMap<String, String>();
								CommAddr commAddr = totalList.get(i);
								String address = commAddr.address;
								double lat = commAddr.lat;
								double lng = commAddr.lng;
								map.put("lat", lat + "");
								map.put("lng", lng + "");
								if (Utils.isStrNull(address)) {
									map.put("name", address);
								}
								SearchressList.add(map);
							}
							adapterSetData(true);
						}
					} else {
						setLocalAddress();
					}
				} else {
					setLocalAddress();
					if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
						String msg = jsonObject.getString("msg");
						ToastUtil.showToastShort(
								SelectServiceAreaActivity.this, msg);
					}
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

	private void back() {
		if (previous == Global.PRE_MAINFRAGMENT) {
			if (getIntent().getStringExtra("servicename") != null
					&& !"".equals(getIntent().getStringExtra("servicename"))) {
				Intent intent = new Intent(this, ServiceFeature.class);
				intent.putExtra("servicename",
						getIntent().getStringExtra("servicename"));
				intent.putExtra("previous", previous);
				intent.putExtra("areaname", name);
				intent.putExtra("areaid", selectareaid);
				intent.putExtra("shopid", shopid);
				startActivity(intent);
				// setResult(Global.RESULT_OK, intent);
				finishWithAnimation();
			} else {
				Intent intent = new Intent(this, ServiceActivity.class);
				intent.putExtra("previous", previous);
				intent.putExtra("serviceid",
						getIntent().getIntExtra("serviceid", 0));
				intent.putExtra("servicetype",
						getIntent().getIntExtra("servicetype", 0));
				intent.putExtra("areaname", name);
				intent.putExtra("areaid", selectareaid);
				intent.putExtra("shopid", shopid);
				startActivity(intent);
				// setResult(Global.RESULT_OK, intent);
				finishWithAnimation();
			}
		} else if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY
				|| previous == Global.SERVICEFEATURE_TO_PETLIST) {
			Intent intent = new Intent(this, ChoosePetActivityNew.class);
			intent.putExtra("servicename",
					getIntent().getStringExtra("servicename"));
			intent.putExtra("serviceid", getIntent()
					.getIntExtra("serviceid", 0));
			intent.putExtra("servicetype",
					getIntent().getIntExtra("servicetype", 0));
			intent.putExtra("previous", previous);
			intent.putExtra("areaname", name);
			intent.putExtra("areaid", selectareaid);
			intent.putExtra("shopid", shopid);
			startActivity(intent);
			// setResult(Global.RESULT_OK, intent);
			finishWithAnimation();
		} else if (previous == Global.MAIN_TO_AREALIST) {
			Intent intent = new Intent(this,
					CharacteristicServiceActivity.class);
			intent.putExtra("previous", previous);
			intent.putExtra("areaname", name);
			intent.putExtra("areaid", selectareaid);
			intent.putExtra("shopid", shopid);
			startActivity(intent);
			// setResult(Global.RESULT_OK, intent);
			finishWithAnimation();
		} else {
			// 获取意图intent
			Intent intent = getIntent();
			// 携带经纬度和商户地址
			intent.putExtra("areaname", name);
			intent.putExtra("areaid", selectareaid);
			intent.putExtra("shopid", shopid);
			// 设置结果
			setResult(Global.RESULT_OK, intent);
			finishWithAnimation();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(SelectServiceAreaActivity.this, "未找到结果",
					Toast.LENGTH_LONG).show();
			dismissPopAddress();
			return;
		}
		// 避免出现null异常 捕获处理
		try {
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				Utils.mLogError("==-->search NO_ERROR");
				List<PoiInfo> infos = result.getAllPoi();
				SearchressList.clear();
				for (int i = 0; i < infos.size(); i++) {
					String name = infos.get(i).name;
					String address = infos.get(i).address;
					LatLng latLng = infos.get(i).location;
					Utils.mLogError("==-->mLocClient address:= " + address
							+ " latLng:= " + latLng);
					double lat = latLng.latitude;
					double lng = latLng.longitude;
					ArrayMap<String, String> map = new ArrayMap<String, String>();
					map.put("name", name);
					map.put("address", address);
					map.put("lat", lat + "");
					map.put("lng", lng + "");
					if (isLocation) {
					} else {
						SearchressList.add(map);
						if (i == infos.size() - 1) {
							isSerch = true;
							flag = false;
							adapterSetData(false);
						}
					}
				}
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void adapterSetData(boolean bool) {
		showDropAddress(bool);
		serviceSearchAddressAdapter.setData(SearchressList, bool);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {// 弹起
			dismissPopArea();
		} else if (oldBottom != 0 && bottom != 0
				&& (bottom - oldBottom > keyHeight)) {// 关闭
		}
	}

}
