package com.haotang.pet.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.pet.CharacteristicServiceActivity;
import com.haotang.pet.ChoosePetActivityNew;
import com.haotang.pet.CitysActivity;
import com.haotang.pet.FeedBackActivity;
import com.haotang.pet.FostercareChooseroomActivity;
import com.haotang.pet.GiftActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.adapter.MPagerAdapter;
import com.haotang.pet.entity.City;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MRelativeLayout;
import com.haotang.pet.view.MyNotification;
import com.haotang.pet.view.RoundedImageClickListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 显示所有主框架的Fragment
 * @author Administrator
 *
 */
public class MainFragmentOld extends Fragment implements OnTouchListener{

	private View view;
	private ViewPager mViewPager;
	private MRelativeLayout rivFeature;
	private MRelativeLayout rivBath;
	private MRelativeLayout rivBeauty;
	private MRelativeLayout rivFostercare;
	private MRelativeLayout rivMember;
	
	private ArrayList<ImageView> imageViews= new ArrayList<ImageView>();
	private ArrayList<View> dots = new ArrayList<View>();
	private MPagerAdapter adapter;
	private int oldPostion;
	private int currentItem;
	private ScheduledExecutorService scheduledExecutor;
	
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private SharedPreferenceUtil spUtil;
	private Activity mActivity;
	private ArrayList<City> citysList = new ArrayList<City>();
	private String[] bannerLinks={"http://www.cwjia.cn","http://www.cwjia.cn","http://www.cwjia.cn"};
	private boolean isFirstLoc = true;
	private String giftcode;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==Global.APK_DOWNLOAD_OVER){
//				Utils.mLogError("--已经下载完毕：：："+(double)msg.obj);
				mNotification.removeNotification();
			}else if(msg.what==Global.APK_DOWNLOADING){
//				Utils.mLogError("--已经下载：：："+(double)msg.obj+"%");
//				mNotification.changeProgressStatus((int)msg.obj);
				mNotification.changeProgressStatus(Utils.formatDouble((Double)msg.obj, 1));
			}else if(msg.what==Global.APK_DOWNLOAD_FAIL){
				mNotification.changeNotificationText("下载失败");
				mNotification.removeNotification();
				mNotification.showDefaultNotification(R.drawable.logo, "宠物家", "下载失败");
				
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
		spUtil = SharedPreferenceUtil.getInstance(mActivity);
		
		initBD();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null)
			view = inflater.inflate(R.layout.mainfragment_old, null);
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		return view;
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getLatestVersion();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = getView();
		findView();
		setView();
	}


	private void initBD() {
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(mActivity);
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


	private void findView() {
		mViewPager = (ViewPager) view.findViewById(R.id.vp_home_pager);
		llPagePoint = (LinearLayout) view.findViewById(R.id.ll_home_pagepoint);
		rivFeature = (MRelativeLayout) view.findViewById(R.id.riv_home_feature);
		rivBath = (MRelativeLayout) view.findViewById(R.id.riv_home_bath);
		rivBeauty = (MRelativeLayout) view.findViewById(R.id.riv_home_beauty);
		rivFostercare = (MRelativeLayout) view.findViewById(R.id.riv_home_fostercare);
		rivMember = (MRelativeLayout) view.findViewById(R.id.riv_home_member);
		
		llCity = (LinearLayout) view.findViewById(R.id.ll_home_city);
		tvCity = (TextView) view.findViewById(R.id.tv_home_city);
		btOther = (Button) view.findViewById(R.id.bt_home_other);
		ibGift = (ImageButton) view.findViewById(R.id.ib_main_gift);
		int[] wh = Utils.getDisplayMetrics(mActivity);
		int wborder = Utils.dip2px(mActivity, 15);
		int width = (wh[0]-wborder)/2;
		int hborder = Utils.dip2px(mActivity, 5);
		int height = ((wh[1]- Utils.dip2px(mActivity, 130))*2/3-4*hborder)/8;
		setProductionHeight(rivBath, width, height*5+hborder);
		setProductionHeight(rivBeauty, width, height*3);
		setProductionHeight(rivFostercare, width, height*3);
		setProductionHeight(rivFeature, width, height*3);
		setProductionHeight(rivMember, width, height*2);
		
	}

	private void setProductionHeight(RelativeLayout iv, int width,int height){
		LayoutParams lParams = iv.getLayoutParams();
		lParams.width = width;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}
	
	private void setView() {
		//adapter = new MPagerAdapter(mActivity, imageViews,bannerLinks,0);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new MPageChangeListener());
		
		getBanner();
		
		
		llCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, CitysActivity.class);
				intent.putExtra("lastcity", tvCity.getText().toString());
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivityForResult(intent, Global.CITYREQUESTCODE);
				
			}
		});
		btOther.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(FeedBackActivity.class, 0,0);
			}
		});
		rivFeature.setRoundedImageOnClick(new RoundedImageClickListener() {
			
			@Override
			public void onclick() {
				// TODO Auto-generated method stub
				goNext(CharacteristicServiceActivity.class, 0,0);
			}
		});
		
		
		
		rivBath.setRoundedImageOnClick(new RoundedImageClickListener() {
			
			@Override
			public void onclick() {
				// TODO Auto-generated method stub
				spUtil.removeData("charactservice");
				if(isLogin()&&hasPet()&&hasService(1)){
					goNext(ServiceActivity.class, 1,Global.PRE_MAINFRAGMENT);
				}else{
//					goNext(ChoosePetActivity.class, 1, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					goNext(ChoosePetActivityNew.class, 1, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				}
//				UmengStatistics.UmengEventStatistics(getActivity(),Global.UmengEventID.click_BathSelect);
			}
		});
		rivBeauty.setRoundedImageOnClick(new RoundedImageClickListener() {
			
			@Override
			public void onclick() {
				// TODO Auto-generated method stub
				spUtil.removeData("charactservice");
				if(isLogin()&&hasPet()&&hasService(2)){
					goNext(ServiceActivity.class, 2,Global.PRE_MAINFRAGMENT);
				}else{
//					goNext(ChoosePetActivity.class, 2, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					goNext(ChoosePetActivityNew.class, 2, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				}
//				UmengStatistics.UmengEventStatistics(getActivity(),Global.UmengEventID.click_BeautySelect);
				
			}
		});
		rivFostercare.setRoundedImageOnClick(new RoundedImageClickListener() {
			
			@Override
			public void onclick() {
				// TODO Auto-generated method stub
				if(isLogin()&&hasPet()){
					goNext(FostercareChooseroomActivity.class, 100,Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET);
				}else{
//					goNext(ChoosePetActivity.class, 100, Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT);
					goNext(ChoosePetActivityNew.class, 100, Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT);
				}
				
//				UmengStatistics.UmengEventStatistics(getActivity(),Global.UmengEventID.click_ChargeInMainPage);	
			}
		});
		rivMember.setRoundedImageOnClick(new RoundedImageClickListener() {
			
			@Override
			public void onclick() {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
				mActivity.sendBroadcast(intent);
			}
		});
		ibGift.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goGift(GiftActivity.class);
			}
		});
		getGiftConfig();
	}
	
	private boolean isLogin(){
		if(spUtil.getInt("userid", -1) > 0&&!"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}
	private boolean hasPet(){
		if(spUtil.getInt("petid", -1) > 0)
			return true;
		return false;
	}
	//上个订单的宠物是否有该服务
	private boolean hasService(int serviceid){
		for(int i=0;i<MainActivity.petServices.length;i++){
			if(serviceid == MainActivity.petServices[i]){
				return true;
			}
		}
		return false;
	}
	
	
	
	private void goNext(Class clazz, int type, int previous){
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", type);
		intent.putExtra("servicetype", type);
		intent.putExtra("previous", previous);
		startActivity(intent);
	}
	private void goGift(Class clazz){
		Intent intent = new Intent(mActivity, clazz);
//		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("code", giftcode);
		startActivity(intent);
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
	private class MRunnable implements Runnable {

		@Override
		public void run() {
			// 切换界面
			currentItem = (currentItem + 1) % dots.size();;
			// 更新UI
			handler.sendEmptyMessage(0);
		}

	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 切换到Viewpager 当前的页面
			mViewPager.setCurrentItem(currentItem);
		};
	};
	private LinearLayout llCity;
	private TextView tvCity;

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scheduledExecutor.shutdown();
		
		MobclickAgent.onPageEnd("MainFragment"); 
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutor.scheduleAtFixedRate(new MRunnable(), 3, 3,
				TimeUnit.SECONDS);
		
		MobclickAgent.onPageStart("MainFragment"); //统计页面
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mLocationClient!=null){
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Utils.mLogError("requestCode="+requestCode+" resultCode="+resultCode);
		if(resultCode == Global.RESULT_OK){
			if(requestCode == Global.CITYREQUESTCODE){
				String city = data.getStringExtra("city");
				tvCity.setText(city);
				Utils.mLogError("city="+city);
			}
				
		}
		
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
			spUtil.saveString("lat_home", location.getLatitude()+"");
			spUtil.saveString("lng_home", location.getLongitude()+"");
			if(location.getCity()!=null&&!"".equals(location.getCity())&&isFirstLoc){
				isFirstLoc = false;
				tvCity.setText(location.getCity());
				spUtil.saveString("city", location.getCity());
				getCity();
			}
		}
		
	}
	
	/**
	 * 获取最新版本和是否强制升级
	 */
	private void getLatestVersion(){
		CommUtil.getLatestVersion(mActivity,Global.getCurrentVersion(mActivity),
				System.currentTimeMillis(),latestHanler);
	}
	

	//获取banner
	private void getBanner(){
		CommUtil.getBanner(mActivity,System.currentTimeMillis(),BannerHandler);
	}
	private void getGiftConfig(){
		CommUtil.getGiftConfig(mActivity,GiftHandler);
	}
	private AsyncHttpResponseHandler GiftHandler = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("gift内容："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jData = jobj.getJSONObject("data");
					if(jData.has("hongbaoCode")&&!jData.isNull("hongbaoCode")){
						giftcode = jData.getString("hongbaoCode");
					}
					if(jData.has("showHongbao")&&!jData.isNull("showHongbao")){
						if("1".equals(jData.getString("showHongbao"))){
							ibGift.setVisibility(View.VISIBLE);
							
							//第一次默认启动红包
							if(!spUtil.getBoolean("opengift", false)){
								goGift(GiftActivity.class);
							}
						}else{
							ibGift.setVisibility(View.GONE);
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
	private AsyncHttpResponseHandler BannerHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("banner内容："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode){
					dots.clear();
					llPagePoint.removeAllViews();
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(15,15);
					params.leftMargin = 10;
					JSONArray jData = jobj.getJSONArray("data");
					int length = jData.length()>=bannerLinks.length?bannerLinks.length:jData.length();
					for(int i=0;i<length;i++){
						JSONObject jp = jData.getJSONObject(i);
						String pic = jp.getString("pic");
						bannerLinks[i] = jp.getString("link");
						
						if (i==0) {
			    			View view = new View(mActivity);
			    			view.setBackgroundResource(R.drawable.dot_focused);
			    			view.setPadding(10, 0, 10, 0);
			    			view.setLayoutParams(params);
			    			llPagePoint.addView(view);
			    			dots.add(view);
			    		}else {
			    			View view = new View(mActivity);
			    			view.setBackgroundResource(R.drawable.dot_normal);
			    			view.setPadding(10, 0, 10, 0);
			    			view.setLayoutParams(params);
			    			
			    			llPagePoint.addView(view);
			    			dots.add(view);
			    		}
						if(length<=1)
							llPagePoint.setVisibility(View.GONE);
						else
							llPagePoint.setVisibility(View.VISIBLE);
						ImageView imageView = new ImageView(mActivity);
						imageView.setScaleType(ScaleType.FIT_XY);
						imageViews.add(imageView);
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+pic,imageView,  0,null);
						
					}
					adapter.notifyDataSetChanged();
					
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
	

	private AsyncHttpResponseHandler latestHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("最新版本："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode){
					JSONObject jData = jobj.getJSONObject("data");
					String latestVersion = jData.getString("nversion");
					String downloadPath = jData.getString("download");
					String versionHint = jData.getString("text");
					boolean isUpgrade = jData.getBoolean("mandate");
					
					boolean isLatest = compareVersion(latestVersion, Global.getCurrentVersion(mActivity));
					if(isUpgrade && isLatest){
						//强制升级
						showForceUpgradeDialog(versionHint, downloadPath);
					}else if(!isUpgrade && isLatest){
						//非强制升级
						showUpgradeDialog(versionHint, downloadPath);
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
	
	private boolean compareVersion(String serviceVersion, String localVersion){
		Utils.mLogError("serviceVersion:"+serviceVersion+" localVersion:"+localVersion);
		boolean result = false;
		serviceVersion = serviceVersion.replace(".", "");
		localVersion = localVersion.replace(".", "");
		
		int flagLength = 0;
		int versionLength = serviceVersion.length() > localVersion.length() ? localVersion.length() : serviceVersion.length();
		for(int i = 0; i < versionLength; i++){
			if(Integer.parseInt(serviceVersion.charAt(i)+"") > Integer.parseInt(localVersion.charAt(i)+"")){
				result = true;
				break;
			}else if(Integer.parseInt(serviceVersion.charAt(i)+"") < Integer.parseInt(localVersion.charAt(i)+"")){
				break;
			}else{
				flagLength = i;
			}
		}
		if(!result && flagLength+1 == versionLength&&
				serviceVersion.length() > localVersion.length()&&
				0 < Integer.parseInt(serviceVersion.charAt(versionLength)+"")){
			result = true;
		}	
		return result;
	}
	
	private void showForceUpgradeDialog(String msg, final String path){
		MDialog mDialog = new MDialog.Builder(mActivity)
		.setTitle("升级了")
		.setType(MDialog.DIALOGTYPE_ALERT)
		.setMessage(msg)
		.setCancelable(false)
		.setOKStr("立即升级")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoDownload(path);
			}
		}).build();
		mDialog.show();
	}
	private void showUpgradeDialog(String msg, final String path){
		MDialog mDialog = new MDialog.Builder(mActivity)
		.setTitle("升级了")
		.setType(MDialog.DIALOGTYPE_CONFIRM)
		.setMessage(msg)
		.setCancelStr("暂不升级")
		.setOKStr("立即升级")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoDownload(path);
			}
		}).build();
		mDialog.show();
	}
	
	private void gotoDownload(final String apkurl) {
		mNotification = new MyNotification(mActivity, null, 1);
		mNotification.showCustomizeNotification(R.drawable.logo, "宠物家", 
				R.layout.download_notif);
        new Thread(){
            @Override
            public void run() {
                File apkfile = Global.downloadApk(apkurl, Utils.getDir(mActivity), "pet.apk",
                		mHandler);
                if(null != apkfile && 0 < apkfile.length()){
                    installAPK(apkfile);
                }
            }

        }.start();

    }

    //自动安装一个apk文件
    private void installAPK(File file){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
//        mActivity.finish();
    }
    
    //获取开通城市
    private void getCity(){
		CommUtil.getCitys(mActivity,citysHanler);
	}
	
	private AsyncHttpResponseHandler citysHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("城市列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if(0 == resultCode){
					JSONArray jData = jobj.getJSONArray("data");
					for(int i = 0; i < jData.length(); i++){
						JSONObject jcity = jData.getJSONObject(i);
						citysList.add(City.json2City(jcity));
					}
						//判断定位城市是否开通spUtil.getString("city", "")
						if(!serviceOpen(citysList, spUtil.getString("city", "")))
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
	private Button btOther;
	private MyNotification mNotification;
	private ImageButton ibGift;
	private LinearLayout llPagePoint;
	
	//返回true为开通，false为未开通
		private boolean serviceOpen(ArrayList<City> list, String city){
			boolean flag = false;
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).name.contains(city)||city.contains(list.get(i).name)){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		private void showNoServiceCityDialog(){
			MDialog mDialog = new MDialog.Builder(mActivity)
			.setTitle("提示")
			.setType(MDialog.DIALOGTYPE_ALERT)
			.setMessage("您所在的城市尚未开通服务")
			.setOKStr("确定").positiveListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			}).build();
			mDialog.show();
		}
		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return true;
		}
	
}
