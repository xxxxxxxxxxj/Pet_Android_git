package com.haotang.pet;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

public class ShopLocationActivity extends SuperActivity {
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private double lat;
	private double lng;
	private String city;
	private String startAddr;
	private String endAddr;
	private String shopName;
	private double shoplat;
	private double shoplng;
	private LayoutInflater mInflater;
	private ImageButton ibBack;
	private TextView tvTitle;
	private LatLng cp;
	/*private double shopGaodelat=0;
	private double shopGaodelng=0;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoplocation);
		
		initBD();
		findView();
		setView();
	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.mv_shoplocation);
		mBaiduMap = mapView.getMap();
		mInflater = LayoutInflater.from(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("位置及周边");
		shoplat = getIntent().getDoubleExtra("shoplat", 0);
		shoplng = getIntent().getDoubleExtra("shoplng", 0);
		endAddr = getIntent().getStringExtra("shopaddr");
		shopName = getIntent().getStringExtra("shopname");
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMaxAndMinZoomLevel(18, 3);
		cp = new LatLng(shoplat, shoplng);
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_geo);  
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(cp)  
		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
		mBaiduMap.addOverlay(option);
		
		MapStatus ms = new MapStatus.Builder().target(cp).zoom(15).build();
		MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.setMapStatus(msu);
		
		InfoWindow iw = new InfoWindow(getLocationView(), cp, -62);
		mBaiduMap.showInfoWindow(iw);
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		Utils.mLogError("==-->swim 4 shoplat  "+shoplat+" shoplng "+shoplng );
		//CommUtil.ConverBaiduToGaoDe(shoplng+"", shoplat+"", handler);
	}
	/*private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->输出了  "+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int status = object.getInt("status");
				if (status==1) {
					if (object.has("info")&&!object.isNull("info")) {
						String info = object.getString("info");
						if (info=="ok"||info.equals("ok")) {
							if (object.has("locations")&&!object.isNull("locations")) {
								String locations = object.getString("locations");
								String [] zuobiao = locations.split(",");
								shopGaodelat = Double.parseDouble(zuobiao[1]);
								shopGaodelng = Double.parseDouble(zuobiao[0]);
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
		
	};*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	
	private void initBD() {
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(this);
		mLocationListener = new MLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(100);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	private class MLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
//			StringBuffer sb = new StringBuffer();
//			sb.append("lotitude="+location.getLatitude());
//			sb.append("longitude="+location.getLongitude());
//			sb.append("addr="+location.getAddrStr());
//			sb.append("city="+location.getCity());
//			sb.append("citycode="+location.getCityCode());
//			Utils.mLogError(sb.toString());
			lat = location.getLatitude();
			lng = location.getLongitude();
			city = location.getCity();
			startAddr = location.getAddrStr();
			mLocationClient.stop();
		}
		
	}
	private View getLocationView(){
		View view = mInflater.inflate(R.layout.locationview, null);
		TextView tvName = (TextView) view.findViewById(R.id.tv_location_name);
		TextView tvAddr = (TextView) view.findViewById(R.id.tv_location_addr);
		tvName.setText(shopName);
		tvAddr.setText(endAddr);
		LinearLayout ibNav = (LinearLayout) view.findViewById(R.id.ll_location_nav);
		ibNav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goNavigation(lat, lng, "我的位置", endAddr, city);
			}
		});
		
		return view;
	}
	
	private void goNavigation(double lat,double lng,String saddr,
			String daddr,String city){
		if(isInstallByread("com.baidu.BaiduMap")){
			try {
				Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:"+lat+","+lng+"|name:"+saddr+"&destination="+daddr+"&mode=driving&region="+city+"&src=宠物家#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
				startActivity(intent); //启动调用
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//当特定手机调用百度出现 no activity found to handle 时 内部异常尝试调用高德或网页调用
				Utils.mLogError("==-->swim 2  "+e.getMessage());
				if (isInstallByread("com.autonavi.minimap")) {
					try{  
						Utils.mLogError("==-->swim 3 shoplat  "+shoplat+" shoplng "+shoplng );
						//double[] gglatlng = bd_decrypt(shoplat, shoplng);
						double[] bd_decrypt = Utils.bd_decrypt(shoplat, shoplng);
//					 	double[] gglatlng = bd_decrypt(lat, lng);
//			            Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=宠物家&poiname="+daddr+"&lat="+shoplat/*+gglatlng[0]*/+"&lon="+shoplng/*gglatlng[1]*/+"&dev=0&style=2");  
//			            startActivity(intent);   
						/*if (shopGaodelng!=0||shopGaodelat!=0) {
							goToNaviActivity(mContext, "宠物家","",shopGaodelat+"", shopGaodelng+"", "1", "2");
						}else {
							goToNaviActivity(mContext, "宠物家","", gglatlng[0]+"", gglatlng[1]+"", "1", "2");
						}*/
						goToNaviActivity(mContext, "宠物家","", bd_decrypt[0]+"", bd_decrypt[1]+"", "1", "2");
			        } catch (Exception e2){  
			            e2.printStackTrace();  
			        }  
				}else{
					String url = "http://api.map.baidu.com/direction?origin=latlng:"+lat+","+lng+
							"|name:"+saddr+"&destination="+daddr+"&mode=driving&region="+city+
							"&output=html&src=宠物家";
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					startActivity(intent);
				}
			}
		}else if(isInstallByread("com.autonavi.minimap")){
			 try{  
				 double[] bd_decrypt = Utils.bd_decrypt(shoplat, shoplng);
					//double[] gglatlng = bd_decrypt(shoplat, shoplng);
//				 	double[] gglatlng = bd_decrypt(lat, lng);
//		            Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=宠物家&poiname="+daddr+"&lat="+shoplat/*+gglatlng[0]*/+"&lon="+shoplng/*gglatlng[1]*/+"&dev=0&style=2");  
//		            startActivity(intent);   
					/*if (shopGaodelng!=0||shopGaodelat!=0) {
						goToNaviActivity(mContext, "宠物家","",shopGaodelat+"", shopGaodelng+"", "1", "2");
					}else {
						goToNaviActivity(mContext, "宠物家","", gglatlng[0]+"", gglatlng[1]+"", "1", "2");
					}*/
				 goToNaviActivity(mContext, "宠物家","", bd_decrypt[0]+"", bd_decrypt[1]+"", "1", "2");
		        } catch (Exception e){  
		            e.printStackTrace();  
		        }  
		}else{
			String url = "http://api.map.baidu.com/direction?origin=latlng:"+lat+","+lng+
					"|name:"+saddr+"&destination="+daddr+"&mode=driving&region="+city+
					"&output=html&src=宠物家";
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivity(intent);
		}
		
		
	}
	
	 private boolean isInstallByread(String packageName) {    
	    	return new File("/data/data/" + packageName).exists();    
	    }
	 
	 private double[] bd_decrypt(double bd_lat, double bd_lon)
	 {
		 double x_pi = 3.14159265358979324 * 3000.0 / 180.0; 
	     double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	     double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	     double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//	     gg_lon = z * Math.cos(theta);
//	     gg_lat = z * Math.sin(theta);
	     return new double[]{z * Math.sin(theta),z * Math.cos(theta)};
	 }
	 
	 public void goToNaviActivity(Context context,String sourceApplication , String poiname , String lat , String lon , String dev , String style){
	        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
	                .append(sourceApplication);
	        if (!TextUtils.isEmpty(poiname)){
	            stringBuffer.append("&poiname=").append(poiname);
	        }
	        stringBuffer.append("&lat=").append(lat)
	                .append("&lon=").append(lon)
	                .append("&dev=").append(dev)
	                .append("&style=").append(style);

	        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
	        intent.setPackage("com.autonavi.minimap");
	        context.startActivity(intent);
	    }
}
