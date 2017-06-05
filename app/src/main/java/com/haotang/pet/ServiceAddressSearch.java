package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ServiceSearchUnChooseAdapter;
import com.haotang.pet.adapter.ServiceSearchUnChooseTopAdapter;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class ServiceAddressSearch extends SuperActivity implements
		OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private ClearEditText editText_service_search_write;
	private ServiceSearchUnChooseTopAdapter chooseTopAdapter;
	private List<ArrayMap<String, String>> addressList = null;
	private List<ArrayMap<String, String>> SearchressList = new ArrayList<ArrayMap<String, String>>();
	private int load_Index = 0;
	private PoiSearch mPoiSearch = null;
	private ListView listView_show_address;
	private ListView listView_service_show_choose_address;
	private RelativeLayout layout_top_show;
	private ServiceSearchUnChooseAdapter ChooseBottomAdapter = null;
	private ImageView imageView_service_address_back;

	private String localName = "";
	private boolean isLocation = true;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	private LocationClientOption option = null;
	private MyLocationData locData = null;
	private BDLocation location;
	private LatLng point;
	private View mPopupView;
	private MProgressDialog pDialog;
	private LatLng latLng = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_service_address_search);
		pDialog = new MProgressDialog(this);

		// if(!isGPSOpen()){
		// showLocationDialog();
		// }
		initView();
		initMap();
		initListener();

	}

	private boolean isGPSOpen() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		return false;
	}

	private void showLocationDialog() {
		MDialog mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("请到【设置】中开启宠物家定位服务，以便您快速选择地址").setCancelStr("取消")
				.setOKStr("去设置")
				.setOKTextColor(this.getResources().getColor(R.color.orange))
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}).build();
		mDialog.show();
	}

	private void displayDriverLocations() {
		mBaiduMap.clear();
		point = new LatLng(ServiceAddressSearch.this.location.getLatitude(),
				ServiceAddressSearch.this.location.getLongitude());
		Projection mProjection = mBaiduMap.getProjection();
		Point pointNew = mProjection.toScreenLocation(point);
		pointNew.y += 60;// 屏幕坐标点偏移60
		LatLng latLngNew = mBaiduMap.getProjection().fromScreenLocation(
				pointNew);
		// 构建Marker图标
		BitmapDescriptor bitmap1 = BitmapDescriptorFactory
				.fromBitmap(getBitmapFromView(mPopupView));
		BitmapDescriptor bitmap2 = BitmapDescriptorFactory
				.fromResource(R.drawable.bk_empty);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, bitmap2));
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latLngNew)
				.zIndex(5).icon(bitmap1);
		mBaiduMap.addOverlay(option);
	}

	private void initListener() {

		imageView_service_address_back
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishWithAnimation();
						;
					}
				});
		// 定位listview
		listView_service_show_choose_address
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String lastChooseName = addressList.get(arg2).get(
								"name");
						String lat = addressList.get(arg2).get("lat");
						String lng = addressList.get(arg2).get("lng");
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putInt("index", 0);
						bundle.putString("SelfName", lastChooseName);
						bundle.putString("lat", lat);
						bundle.putString("lng", lng);
						intent.setAction("android.intent.action.AddServiceAddressActivity");
						intent.putExtras(bundle);
						sendBroadcast(intent);
						finishWithAnimation();
					}
				});
		// 搜索listview
		listView_show_address.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					String lastChooseName = SearchressList.get(arg2)
							.get("name");
					String lat = SearchressList.get(arg2).get("lat");
					String lng = SearchressList.get(arg2).get("lng");
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("index", 0);
					bundle.putString("SelfName", lastChooseName);
					bundle.putString("lat", lat);
					bundle.putString("lng", lng);
					intent.setAction("android.intent.action.AddServiceAddressActivity");
					intent.putExtras(bundle);
					sendBroadcast(intent);
					finishWithAnimation();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		editText_service_search_write.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				try {
					// editText_service_search_write.setBackgroundColor(Color.parseColor("#F3F2F1"));
					layout_top_show.setBackgroundColor(Color
							.parseColor("#F3F2F1"));
					isLocation = false;
					SearchressList = new ArrayList<ArrayMap<String, String>>();
					SearchressList.clear();
					// addressList.clear();
					// listView_service_show_choose_address.setVisibility(View.GONE);
					layout_top_show.setVisibility(View.VISIBLE);
					listView_show_address.setVisibility(View.VISIBLE);
					mPoiSearch.searchInCity(new PoiCitySearchOption()
							.city(SharedPreferenceUtil.getInstance(
									ServiceAddressSearch.this).getString(
									"city", "北京"))
							.keyword(
									editText_service_search_write.getText()
											.toString()).pageNum(0));
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
				if (editText_service_search_write.getText().toString()
						.equals("")
						|| editText_service_search_write.getText().toString()
								.equals(null)) {
					layout_top_show.setVisibility(View.GONE);
					// editText_service_search_write.setBackgroundColor(Color.parseColor("#FFFFFF"));
				}
			}
		});
	}

	private void initView() {

		layout_top_show = (RelativeLayout) findViewById(R.id.layout_top_show);
		imageView_service_address_back = (ImageView) findViewById(R.id.imageView_service_address_back);
		editText_service_search_write = (ClearEditText) findViewById(R.id.editText_service_search_write);
		listView_show_address = (ListView) findViewById(R.id.listView_show_address);
		listView_service_show_choose_address = (ListView) findViewById(R.id.listView_service_show_choose_address);
		listView_service_show_choose_address.setVisibility(View.VISIBLE);
		listView_show_address.setVisibility(View.GONE);
		addressList = new ArrayList<ArrayMap<String, String>>();
	}

	private void initMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mPopupView = LayoutInflater.from(this).inflate(R.layout.newpop_view,
				null);
		mBaiduMap = mMapView.getMap();
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		Utils.mLogError("==-->mLocClient mLocClient.start()");

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			ServiceAddressSearch.this.location = location;
			locData = new MyLocationData.Builder().accuracy(0.0f)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(-1).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			latLng = new LatLng(location.getLatitude(), location.getLongitude());
			Utils.mLogError("==-->mLocClient latLng " + latLng.longitude
					+ "  latitude :=  " + latLng.latitude);
			// 设置地图中心点
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLng(latLng);
			mBaiduMap.animateMapStatus(mapStatusUpdate);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			try {
				localName = location.getAddrStr();
				Utils.mLogError("localName==null?" + (localName == null));
				if (localName == null || "".equals(localName)
						|| "null".equals(localName)) {
					if (latLng != null) {
						// 反Geo搜索
						pDialog.showDialog();
						mSearch.reverseGeoCode(new ReverseGeoCodeOption()
								.location(latLng));
					}
				} else if (localName != null && !"".equals(localName)
						&& !"null".equals(localName)) {
					pDialog.showDialog();
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city(SharedPreferenceUtil.getInstance(
									ServiceAddressSearch.this).getString(
									"city", "北京")).keyword(localName)
							.pageNum(0));

					// Toast.makeText(ServiceAddressSearch.this,localName,
					// Toast.LENGTH_SHORT).show();

					SearchressList.clear();
					mLocClient.stop();
					displayDriverLocations();

					Utils.mLogError("==-->mLocClient mLocClient.stop()");

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			pDialog.closeDialog();
			Toast.makeText(ServiceAddressSearch.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(SharedPreferenceUtil.getInstance(
						ServiceAddressSearch.this).getString("city", "北京"))
				.keyword(result.getAddress()).pageNum(0));
		mLocClient.stop();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		pDialog.closeDialog();
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(ServiceAddressSearch.this, "未找到结果",
					Toast.LENGTH_LONG).show();
			layout_top_show.setVisibility(View.GONE);
			return;
		}
		// 避免出现null异常 捕获处理
		try {
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				Utils.mLogError("==-->search NO_ERROR");
				List<PoiInfo> infos = result.getAllPoi();
				String lstr = "";

				for (int i = 0; i < infos.size(); i++) {
					String name = infos.get(i).name;
					if (i == 0) {
						lstr = infos.get(0).name;
					}
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
						if (addressList != null) {
							addressList.add(map);
							if (i == infos.size() - 1) {
								listView_service_show_choose_address
										.setVisibility(View.VISIBLE);
								ChooseBottomAdapter = new ServiceSearchUnChooseAdapter(
										this, addressList, lstr);
								listView_service_show_choose_address
										.setAdapter(ChooseBottomAdapter);
							}
						}
					} else {
						if (SearchressList != null) {
							SearchressList.add(map);
							if (i == infos.size() - 1) {
								chooseTopAdapter = new ServiceSearchUnChooseTopAdapter(
										this, SearchressList);
								listView_show_address
										.setAdapter(chooseTopAdapter);
								chooseTopAdapter.notifyDataSetChanged();
							}
						}
					}

					// else {
					// layout_top_show.setVisibility(View.GONE);
					// }
				}
				return;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将View转换成Bitmap的方法
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocClient.unRegisterLocationListener(myListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ServiceAddressSearch"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ServiceAddressSearch"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
}
