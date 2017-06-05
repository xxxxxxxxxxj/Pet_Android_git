package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.RegionsAdapter;
import com.haotang.pet.adapter.ShopListAdapter;
import com.haotang.pet.entity.ShopListBean;
import com.haotang.pet.entity.ShopListBean.DataBean.RegionsBean;
import com.haotang.pet.entity.ShopListBean.DataBean.RegionsBean.ShopsBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;

/**
 * <p>
 * Title:ShopListActivity
 * </p>
 * <p>
 * Description:门店列表
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-17 上午11:22:04
 */
public class ShopListActivity extends SuperActivity {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ListView listView_shop_list;
	private double lat = 0;
	private double lng = 0;
	private double addserviceprice = 0;
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private List<ShopsBean> ShopsWithPriceList = new ArrayList<ShopsBean>();
	private int serviceId = 0;
	private ShopListAdapter shopListAdapter;
	private MProgressDialog pDialog;
	private View footer;
	private String strp;
	private int previous;
	private Intent fIntent;
	private int clicksort;
	public static ShopListActivity act;
	private HorizontalScrollView hsv_shoplist_area;
	private GridView gv_shoplist_area;
	public boolean isFirst = true;
	private RegionsAdapter<RegionsBean> regionsAdapter;
	private List<RegionsBean> regions;
	private int regionId;
	private int areaClickPosition;
	private int itemWidth;
	private int typeId;
	private String servicename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list);
		act = this;
		fIntent = getIntent();
		clicksort = fIntent.getIntExtra("clicksort", 0);
		previous = fIntent.getIntExtra("previous", 0);
		typeId = fIntent.getIntExtra("typeId", -1);
		servicename = fIntent.getStringExtra("servicename");
		regionId = spUtil.getInt("regionId", -1);
		if (previous == Global.PRE_SERVICEDETAIL_TO_SHOPLIST) {
			serviceId = fIntent.getIntExtra("serviceid", 0);
			lat = fIntent.getDoubleExtra("lat", 0);
			lng = fIntent.getDoubleExtra("lng", 0);
			addserviceprice = fIntent.getDoubleExtra("addserviceprice", 0);
			strp = fIntent.getStringExtra("strp_long");
		} else {
			strp = fIntent.getIntExtra("petid", 0) + "_"
					+ fIntent.getIntExtra("serviceid", 0) + "_"
					+ fIntent.getIntExtra("customerpetid", 0) + "_"
					+ fIntent.getIntExtra("servicetype", 0) + "_"
					+ fIntent.getIntExtra("petkind", 0) + "_0";
		}
		findView();
		setView();
		initListener();
		if (lat == 0 && lng == 0) {
			initBD();
		} else {
			getData();
		}
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShopsWithPriceList.clear();
				finishWithAnimation();
			}
		});
		gv_shoplist_area.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (regions != null && regions.size() > 0) {
					RegionsBean regionsBean = regions.get(position);
					if (regionsBean != null) {
						regionId = regionsBean.getAreacode();
						areaClickPosition = position;
						regionsAdapter.setClickItem(areaClickPosition);
						ScollTo(areaClickPosition);
						List<ShopsBean> shops = regions.get(areaClickPosition)
								.getShops();
						if (shops != null && shops.size() > 0) {
							ShopsWithPriceList.clear();
							ShopsWithPriceList.addAll(shops);
							shopListAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		});
	}

	private void getData() {
		pDialog.showDialog();
		ShopsWithPriceList.clear();
		shopListAdapter.notifyDataSetChanged();
		CommUtil.queryShopsWithPrice(this, strp, lat, lng, typeId,
				queryShopsWithPrice);
	}

	private AsyncHttpResponseHandler queryShopsWithPrice = new AsyncHttpResponseHandler() {
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

	private void processData(String result) {
		pDialog.closeDialog();
		try {
			Gson gson = new Gson();
			ShopListBean shopListBean = gson.fromJson(result,
					ShopListBean.class);
			if (shopListBean != null) {
				int code = shopListBean.getCode();
				com.haotang.pet.entity.ShopListBean.DataBean data = shopListBean
						.getData();
				String msg = shopListBean.getMsg();
				if (code == 0 && data != null) {
					regions = data.getRegions();
					if (regions != null && regions.size() > 0) {
						// 设置行政区划列表
						int size = regions.size();
						int length = 80;
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float density = dm.density;
						int gridviewWidth = (int) (size * (length + 5) * density);
						itemWidth = (int) (length * density);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								gridviewWidth,
								LinearLayout.LayoutParams.FILL_PARENT);
						gv_shoplist_area.setLayoutParams(params); // 重点
						gv_shoplist_area.setColumnWidth(itemWidth); // 重点
						gv_shoplist_area.setHorizontalSpacing(5); // 间距
						gv_shoplist_area.setStretchMode(GridView.NO_STRETCH);
						gv_shoplist_area.setGravity(Gravity.CENTER);// 位置居中
						gv_shoplist_area.setNumColumns(size); // 重点
						regionsAdapter = new RegionsAdapter<RegionsBean>(
								ShopListActivity.this, regions, 1);
						gv_shoplist_area.setAdapter(regionsAdapter);
						regionsAdapter.setData(regions);
						// 选中行政区划
						if (regionId > 0) {
							for (int i = 0; i < regions.size(); i++) {
								RegionsBean regionsBean = regions.get(i);
								if (regionsBean != null) {
									if (regionId == regionsBean.getAreacode()) {
										areaClickPosition = i;
										regionsAdapter
												.setClickItem(areaClickPosition);
										ScollTo(areaClickPosition);
										List<ShopsBean> shops = regions.get(
												areaClickPosition).getShops();
										if (shops != null && shops.size() > 0) {
											ShopsWithPriceList.clear();
											ShopsWithPriceList.addAll(shops);
											shopListAdapter
													.notifyDataSetChanged();
										}
										break;
									}
								}
							}
						} else {
							areaClickPosition = 0;
							regionsAdapter.setClickItem(areaClickPosition);
							ScollTo(areaClickPosition);
							List<ShopsBean> shops = regions.get(
									areaClickPosition).getShops();
							if (shops != null && shops.size() > 0) {
								ShopsWithPriceList.clear();
								ShopsWithPriceList.addAll(shops);
								shopListAdapter.notifyDataSetChanged();
							}
						}
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(ShopListActivity.this,
								msg);
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
				hsv_shoplist_area.smoothScrollTo(
						(dayposition2 - 2) * itemWidth, 0);
			}
		}, 5);
	}

	private void findView() {
		pDialog = new MProgressDialog(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_shop_list = (ListView) findViewById(R.id.listView_shop_list);
		hsv_shoplist_area = (HorizontalScrollView) findViewById(R.id.hsv_shoplist_area);
		gv_shoplist_area = (GridView) findViewById(R.id.gv_shoplist_area);
		footer = LayoutInflater.from(this).inflate(R.layout.shop_list_footer,
				null);
	}

	private void setView() {
		tv_titlebar_title.setText("门店列表");
		shopListAdapter = new ShopListAdapter(this, serviceId,
				ShopsWithPriceList, addserviceprice, clicksort, servicename);
		shopListAdapter.setPrevious(previous);
		shopListAdapter.setFIntent(fIntent);
		listView_shop_list.addFooterView(footer);
		listView_shop_list.setAdapter(shopListAdapter);
	}

	private void initBD() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(100);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private class MLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			lat = location.getLatitude();// 纬度
			lng = location.getLongitude();// 经度
			if (isFirst) {
				isFirst = false;
				getData();
			}
			mLocationClient.stop();
		}
	}

	@Override
	protected void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
		}
		super.onDestroy();
	}
}
