package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Utils;

public class ServiceAreaActivity extends SuperActivity implements
		OnClickListener {
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private ImageButton ibBack;
	private TextView tvTitle;
	private String points;
	private double arealat;
	private double arealng;
	private String shopname;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.jiaoya);
	private UiSettings mUiSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_area);
		findView();
		Intent intent = getIntent();
		shopname = intent.getStringExtra("shopname");
		points = intent.getStringExtra("points");
		arealat = intent.getDoubleExtra("lat", 0.0);
		arealng = intent.getDoubleExtra("lng", 0.0);
		showArea();
	}

	private void showArea() {
		// 绘制区域
		List<LatLng> pts = new ArrayList<LatLng>();
		List<String> lat = new ArrayList<String>();
		List<String> lon = new ArrayList<String>();
		if (points != null) {
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
						// 添加区域（宠物家生活馆（朝青店））
						LatLng pt = new LatLng(Double.parseDouble(lon.get(k)),
								Double.parseDouble(lat.get(k)));
						pts.add(pt);
					}
				}
			}
		}
		OverlayOptions ooPolygon = new PolygonOptions().points(pts)
				.stroke(new Stroke(5, 0x000000FF))
				.fillColor(getResources().getColor(R.color.areacolor));
		mBaiduMap.addOverlay(ooPolygon);
		// 添加店铺大头标
		LatLng llA = new LatLng(arealat, arealng);
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooA);
		// 移动地图
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(llA).zoom(14.0f);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
		// 显示店名
		View inflate = View.inflate(this, R.layout.infowindow, null);
		Button btn_infowindow = null;
		if (shopname != null && !TextUtils.isEmpty(shopname)) {
			btn_infowindow.setText(shopname);
		}
		mBaiduMap.showInfoWindow(new InfoWindow(BitmapDescriptorFactory
				.fromView(inflate), llA, -70, null));
	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.mv_servicearea);
		mBaiduMap = mapView.getMap();
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tvTitle.setText("服务区范围");
		mapView.removeViewAt(1);
		mapView.showZoomControls(false);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMaxAndMinZoomLevel(18, 3);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setCompassEnabled(false);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				return true;
			}
		});
		ibBack.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:// 返回
			finishWithAnimation();
			break;
		default:
			break;
		}
	}
}
