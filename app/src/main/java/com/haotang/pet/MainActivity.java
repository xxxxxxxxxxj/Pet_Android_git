package com.haotang.pet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.fragment.MTabContent;
import com.haotang.pet.fragment.MainFragment;
import com.haotang.pet.fragment.MyFragment;
import com.haotang.pet.fragment.OrderFragment;
import com.haotang.pet.fragment.PetCircleOrSelectFragment;
import com.haotang.pet.guideview.Guide;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.InstallDialog;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UpdateUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialog;
import com.haotang.pet.view.MyNotification;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private static final int EXITE_SYSTEM = 200;
	private static int DELAYEDTIME = 3000;
	private MyNotification mNotification;
	private LayoutInflater mInflater;
	public TabHost mTabHost;
	private FragmentManager fm;
	private MainFragment mainFragment;
	private FragmentTransaction ft;
	private OrderFragment orderFragment;
	private MyFragment myFragment;
	public PetCircleOrSelectFragment petCircleOrSelectFragment;
	private MReceiver receiver;
	private SharedPreferenceUtil spUtil;
	public static int[] petServices = new int[4];
	private boolean isFirst = true;
	private String tabTag = "main";
	private double lat;
	private double lng;
	private Guide guide;
	private PopupWindow pWin;
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private TabWidget mTab;
	private View vFlagmain;
	private View vFlagorder;
	private View vFlagmemb;
	private static View vFlagmy;
	private ImageView ivTabMain;
	private ImageView ivTabOrder;
	private ImageView ivTabMemb;
	private ImageView ivTabMy;
	private TextView tvTabMain;
	private TextView tvTabOrder;
	private TextView tvTabMemb;
	private TextView tvTabMy;
	private int selection;

	// START ACTIVITY
	private String activityPic = "";
	private String activityUrl = "";
	private String countType = "";
	private String HomeActivityPageId = "";
	private String updateTime = "";
	private LinearLayout ll_mainactivity_one;
	private ImageView iv_mainactivity_one;
	private TextView tv_mainactivity_one;
	private LinearLayout ll_mainactivity_two;
	private ImageView iv_mainactivity_two;
	private TextView tv_mainactivity_two;
	private LinearLayout ll_mainactivity_three;
	private ImageView iv_mainactivity_three;
	private TextView tv_mainactivity_three;
	private LinearLayout ll_mainactivity_four;
	private ImageView iv_mainactivity_four;
	private TextView tv_mainactivity_four;
	private InstallApkReceiver installApkReceiver;
	private LinearLayout ll_mainactivity_bottom;

	// END ACTIVITY
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Global.APK_DOWNLOAD_OVER:
				mNotification.removeNotification();
				break;
			case Global.APK_DOWNLOADING:
				mNotification.changeProgressStatus(Utils.formatDouble(
						(Double) msg.obj, 1));
				break;
			case Global.APK_DOWNLOAD_FAIL:
				mNotification.changeNotificationText("下载失败");
				mNotification.removeNotification();
				mNotification.showDefaultNotification(R.drawable.logo, "宠物家",
						"下载失败");
				break;
			case Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("order");
				// autoLogin();
				mLocationClient.start();
				commentDialog();
				break;
			case Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("order");
				break;
			case Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("order");
				break;
			case Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT:
				mTabHost.setCurrentTabByTag("circle");
				break;
			case Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("main");

				break;
			case Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("main");
				mLocationClient.start();
				break;
			case Global.PRE_EVALUATEOVER_BACK_MAIN:
				mTabHost.setCurrentTabByTag("main");

				break;
			case Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY:
				// 更新自动登录，从新获取user信息
				// autoLogin();
				mTabHost.setCurrentTabByTag("order");
				mLocationClient.start();
				break;
			// case Global.PRE_LOGOUT_TO_MAINACTIVITY:
			// 更新自动登录，从新获取user信息
			// autoLogin();
			// mLocationClient.start();
			// break;
			case Global.PRE_ORDER_LIST_TO_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("main");
				break;
			case Global.PETADD_BACK_PETINFO_BACK_MY:
				mTabHost.setCurrentTabByTag("my");
				break;
			case Global.DELETEPET_TO_UPDATEUSERINFO:
				autoLogin(lat, lng);
				break;
			case Global.PAYSUCCESS_TO_MYFRAGMENT:
				mTabHost.setCurrentTabByTag("my");
				commentDialog();
				break;
			case Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY:
				mTabHost.setCurrentTabByTag("my");
				commentDialog();
				break;
			case Global.GROWTH_TO_USERMEMBERFRAGMENT:
				mTabHost.setCurrentTabByTag("member");
				break;
			case Global.CARDS_TO_MAINACTIVITY:
				mTabHost.setCurrentTabByTag("main");
				break;
			}

			super.handleMessage(msg);
		}

	};

	private void commentDialog() {
		boolean isComment = spUtil.getBoolean("isComment", false);
		if (!isComment) {
			long refuseTime = spUtil.getLong("refuseTime", 0);
			long localTime = System.currentTimeMillis();
			if (refuseTime > 0) {
				if (localTime > refuseTime) {
					long betweenDays = (long) ((localTime - refuseTime)
							/ (1000 * 60 * 60 * 24) + 0.5);
					if (betweenDays >= 30) {
						tqDialog();
					}
				}
			} else {
				tqDialog();
			}
		}
	}

	private void tqDialog() {
		new AlertDialog(this).builder().setTitle("您对宠物家还满意吗?")
				.setCommentButton("我去评论", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Utils.goMarket(MainActivity.this);
						spUtil.saveBoolean("isComment", true);
					}
				}).setComplaintsButton("我要吐槽", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Utils.goMarket(MainActivity.this);
						spUtil.saveBoolean("isComment", true);
					}
				}).setRefuseButton("残忍拒绝", new OnClickListener() {
					@Override
					public void onClick(View v) {
						spUtil.saveBoolean("isComment", false);
						spUtil.saveLong("refuseTime",
								System.currentTimeMillis());
					}
				}).show();
	}

	private void setBottomVis(String tabId) {
		ll_mainactivity_bottom.bringToFront();
		if (tabId.equals("main")) {
			ll_mainactivity_one.setVisibility(View.VISIBLE);
			ll_mainactivity_two.setVisibility(View.INVISIBLE);
			ll_mainactivity_three.setVisibility(View.INVISIBLE);
			ll_mainactivity_four.setVisibility(View.INVISIBLE);
			if (ivTabMain != null && "main".equals(ivTabMain.getTag())) {
				ivTabMain.setVisibility(View.INVISIBLE);
			}
			if (tvTabMain != null && "main".equals(tvTabMain.getTag())) {
				tvTabMain.setVisibility(View.INVISIBLE);
			}
			if (ivTabOrder != null && "order".equals(ivTabOrder.getTag())) {
				ivTabOrder.setVisibility(View.VISIBLE);
			}
			if (tvTabOrder != null && "order".equals(tvTabOrder.getTag())) {
				tvTabOrder.setVisibility(View.VISIBLE);
			}
			if (ivTabMemb != null && "circle".equals(ivTabMemb.getTag())) {
				ivTabMemb.setVisibility(View.VISIBLE);
			}
			if (tvTabMemb != null && "circle".equals(tvTabMemb.getTag())) {
				tvTabMemb.setVisibility(View.VISIBLE);
			}
			if (ivTabMy != null && "my".equals(ivTabMy.getTag())) {
				ivTabMy.setVisibility(View.VISIBLE);
			}
			if (tvTabMy != null && "my".equals(tvTabMy.getTag())) {
				tvTabMy.setVisibility(View.VISIBLE);
			}
		} else if (tabId.equals("circle")) {
			ll_mainactivity_one.setVisibility(View.INVISIBLE);
			ll_mainactivity_two.setVisibility(View.VISIBLE);
			ll_mainactivity_three.setVisibility(View.INVISIBLE);
			ll_mainactivity_four.setVisibility(View.INVISIBLE);

			if (ivTabMain != null && "main".equals(ivTabMain.getTag())) {
				ivTabMain.setVisibility(View.VISIBLE);
			}
			if (tvTabMain != null && "main".equals(tvTabMain.getTag())) {
				tvTabMain.setVisibility(View.VISIBLE);
			}
			if (ivTabOrder != null && "order".equals(ivTabOrder.getTag())) {
				ivTabOrder.setVisibility(View.VISIBLE);
			}
			if (tvTabOrder != null && "order".equals(tvTabOrder.getTag())) {
				tvTabOrder.setVisibility(View.VISIBLE);
			}
			if (ivTabMemb != null && "circle".equals(ivTabMemb.getTag())) {
				ivTabMemb.setVisibility(View.INVISIBLE);
			}
			if (tvTabMemb != null && "circle".equals(tvTabMemb.getTag())) {
				tvTabMemb.setVisibility(View.INVISIBLE);
			}
			if (ivTabMy != null && "my".equals(ivTabMy.getTag())) {
				ivTabMy.setVisibility(View.VISIBLE);
			}
			if (tvTabMy != null && "my".equals(tvTabMy.getTag())) {
				tvTabMy.setVisibility(View.VISIBLE);
			}
		} else if (tabId.equals("order")) {
			ll_mainactivity_one.setVisibility(View.INVISIBLE);
			ll_mainactivity_two.setVisibility(View.INVISIBLE);
			ll_mainactivity_three.setVisibility(View.VISIBLE);
			ll_mainactivity_four.setVisibility(View.INVISIBLE);

			if (ivTabMain != null && "main".equals(ivTabMain.getTag())) {
				ivTabMain.setVisibility(View.VISIBLE);
			}
			if (tvTabMain != null && "main".equals(tvTabMain.getTag())) {
				tvTabMain.setVisibility(View.VISIBLE);
			}
			if (ivTabOrder != null && "order".equals(ivTabOrder.getTag())) {
				ivTabOrder.setVisibility(View.INVISIBLE);
			}
			if (tvTabOrder != null && "order".equals(tvTabOrder.getTag())) {
				tvTabOrder.setVisibility(View.INVISIBLE);
			}
			if (ivTabMemb != null && "circle".equals(ivTabMemb.getTag())) {
				ivTabMemb.setVisibility(View.VISIBLE);
			}
			if (tvTabMemb != null && "circle".equals(tvTabMemb.getTag())) {
				tvTabMemb.setVisibility(View.VISIBLE);
			}
			if (ivTabMy != null && "my".equals(ivTabMy.getTag())) {
				ivTabMy.setVisibility(View.VISIBLE);
			}
			if (tvTabMy != null && "my".equals(tvTabMy.getTag())) {
				tvTabMy.setVisibility(View.VISIBLE);
			}

		} else if (tabId.equals("my")) {
			ll_mainactivity_one.setVisibility(View.INVISIBLE);
			ll_mainactivity_two.setVisibility(View.INVISIBLE);
			ll_mainactivity_three.setVisibility(View.INVISIBLE);
			ll_mainactivity_four.setVisibility(View.VISIBLE);

			if (ivTabMain != null && "main".equals(ivTabMain.getTag())) {
				ivTabMain.setVisibility(View.VISIBLE);
			}
			if (tvTabMain != null && "main".equals(tvTabMain.getTag())) {
				tvTabMain.setVisibility(View.VISIBLE);
			}
			if (ivTabOrder != null && "order".equals(ivTabOrder.getTag())) {
				ivTabOrder.setVisibility(View.VISIBLE);
			}
			if (tvTabOrder != null && "order".equals(tvTabOrder.getTag())) {
				tvTabOrder.setVisibility(View.VISIBLE);
			}
			if (ivTabMemb != null && "circle".equals(ivTabMemb.getTag())) {
				ivTabMemb.setVisibility(View.VISIBLE);
			}
			if (tvTabMemb != null && "circle".equals(tvTabMemb.getTag())) {
				tvTabMemb.setVisibility(View.VISIBLE);
			}
			if (ivTabMy != null && "my".equals(ivTabMy.getTag())) {
				ivTabMy.setVisibility(View.INVISIBLE);
			}
			if (tvTabMy != null && "my".equals(tvTabMy.getTag())) {
				tvTabMy.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		setContentView(R.layout.main);
		// MApplication.listAppoint.add(this);
		Utils.mLogError("==-->Main Activity");
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		if (Utils.checkNet(this))
			getLatestVersion();
		initBD();
		spUtil = SharedPreferenceUtil.getInstance(this);
		mInflater = LayoutInflater.from(this);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTab = (TabWidget) findViewById(android.R.id.tabs);
		ll_mainactivity_bottom = (LinearLayout) findViewById(R.id.ll_mainactivity_bottom);
		ll_mainactivity_one = (LinearLayout) findViewById(R.id.ll_mainactivity_one);
		iv_mainactivity_one = (ImageView) findViewById(R.id.iv_mainactivity_one);
		tv_mainactivity_one = (TextView) findViewById(R.id.tv_mainactivity_one);
		ll_mainactivity_two = (LinearLayout) findViewById(R.id.ll_mainactivity_two);
		iv_mainactivity_two = (ImageView) findViewById(R.id.iv_mainactivity_two);
		tv_mainactivity_two = (TextView) findViewById(R.id.tv_mainactivity_two);
		ll_mainactivity_three = (LinearLayout) findViewById(R.id.ll_mainactivity_three);
		iv_mainactivity_three = (ImageView) findViewById(R.id.iv_mainactivity_three);
		tv_mainactivity_three = (TextView) findViewById(R.id.tv_mainactivity_three);
		ll_mainactivity_four = (LinearLayout) findViewById(R.id.ll_mainactivity_four);
		iv_mainactivity_four = (ImageView) findViewById(R.id.iv_mainactivity_four);
		tv_mainactivity_four = (TextView) findViewById(R.id.tv_mainactivity_four);
		mTabHost.setup();
		spUtil.saveBoolean("guide", true);
		TabHost.OnTabChangeListener tabChangeListener = new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				getFragment();
				tabTag = tabId;
				// setBottomVis(tabId);
				if (tabId.equals("main")) {
					spUtil.saveBoolean("selectMain", true);
					if (ivTabMain != null && "main".equals(ivTabMain.getTag()))
						if (pressImgList != null && pressImgList.size() > 0) {
							setImg(0, pressImgList.get(0));
						}
					if (noPressImgList != null && noPressImgList.size() > 0) {
						for (int i = 1; i < noPressImgList.size(); i++) {
							setImg(i, noPressImgList.get(i));
						}
					}
					// ivTabMain.setAnimation(shakeAnimation(1));
					addFragment(mainFragment, new MainFragment(lat, lng),
							"main");
				} else if (tabId.equals("circle")) {
					if (ivTabMemb != null
							&& "circle".equals(ivTabMemb.getTag()))
						if (pressImgList != null && pressImgList.size() > 1) {
							setImg(1, pressImgList.get(1));
						}
					if (noPressImgList != null && noPressImgList.size() > 0) {
						for (int i = 0; i < noPressImgList.size(); i++) {
							if (i == 0 || i == 2 || i == 3) {
								setImg(i, noPressImgList.get(i));
							}
						}
					}
					// ivTabMemb.setAnimation(shakeAnimation(1));
					if (spUtil.getInt("hotmember", 0) < 2) {
						vFlagmemb.setVisibility(View.VISIBLE);
						spUtil.saveInt("hotmember",
								spUtil.getInt("hotmember", 0) + 1);
					} else {
						vFlagmemb.setVisibility(View.GONE);
					}
					addFragment(petCircleOrSelectFragment,
							new PetCircleOrSelectFragment(), "circle");
					vFlagmemb.setVisibility(View.GONE);
				} else if (tabId.equals("order")) {
					if (ivTabOrder != null
							&& "order".equals(ivTabOrder.getTag()))
						if (pressImgList != null && pressImgList.size() > 2) {
							setImg(2, pressImgList.get(2));
						}
					if (noPressImgList != null && noPressImgList.size() > 0) {
						for (int i = 0; i < noPressImgList.size(); i++) {
							if (i == 0 || i == 1 || i == 3) {
								setImg(i, noPressImgList.get(i));
							}
						}
					}
					// ivTabOrder.setAnimation(shakeAnimation(1));
					if (vFlagorder != null
							&& "order".equals(vFlagorder.getTag()))
						vFlagorder.setVisibility(View.GONE);
					addFragment(orderFragment, new OrderFragment(), "order");
				} else {
					if (ivTabMy != null && "my".equals(ivTabMy.getTag()))
						if (pressImgList != null && pressImgList.size() > 3) {
							setImg(3, pressImgList.get(3));
						}
					if (noPressImgList != null && noPressImgList.size() > 0) {
						for (int i = 0; i < noPressImgList.size(); i++) {
							if (i == 0 || i == 1 || i == 2) {
								setImg(i, noPressImgList.get(i));
							}
						}
					}
					// ivTabMy.setAnimation(shakeAnimation(1));
					addFragment(myFragment, new MyFragment(), "my");
				}
				// 使用commitAllowingStateLoss才能tab之间跳转
				ft.commitAllowingStateLoss();
			}

		};
		mTabHost.setOnTabChangedListener(tabChangeListener);
		addTab("main", R.drawable.tab_main, "宠物家");
		addTab("circle", R.drawable.tab_knowledge, "宠圈");
		addTab("order", R.drawable.tab_order, "订单");
		addTab("my", R.drawable.tab_my, "我的");
		vFlagmemb.setVisibility(View.VISIBLE);
		mTabHost.setCurrentTab(0);
		// setBottomVis("main");
		initReceiver();

		if (spUtil.getInt("hotmember", 0) < 2) {
			vFlagmemb.setVisibility(View.VISIBLE);
		}

		int previous = getIntent().getIntExtra("previous", 0);
		int orderid = getIntent().getIntExtra("orderid", 0);
		if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_ORDER) {
			Intent intent = new Intent(this, OrderDetailFromOrderActivity.class);
			intent.putExtra("orderid", orderid);
			startActivity(intent);
		} else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_COUPON) {
			Intent intent = new Intent(this, MyCouponActivity.class);
			intent.putExtra("orderid", orderid);
			startActivity(intent);
		}
		mTabHost.setOnTabChangedListener(tabChangeListener);
		getIndex();
		getMainActivity();
		initInstallApkReceiver();
	}

	private void initInstallApkReceiver() {
		installApkReceiver = new InstallApkReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.UpdateServiceInstallApk");
		registerReceiver(installApkReceiver, filter);
	}

	private class InstallApkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			boolean isUpgrade = bundle.getBoolean("isUpgrade");
			String versionHint = bundle.getString("versionHint");
			String apkPath = bundle.getString("apkPath");
			String latestVersion = bundle.getString("latestVersion");
			if (isUpgrade) {
				// 强制升级
				showForceInstallDialog(versionHint, apkPath, latestVersion);
			} else {
				// 非强制升级
				showInstallDialog(versionHint, apkPath, latestVersion);
			}
		}
	}

	private void showForceInstallDialog(String localVersionHint,
			final String localApkPath, String version) {
		InstallDialog mDialog = new InstallDialog.Builder(this)
				.setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_ALERT).setMessage(localVersionHint)
				.setCancelable(false).setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdateUtil.installAPK(MainActivity.this, new File(
								localApkPath));
					}
				}).build();
		mDialog.show();
	}

	private void showInstallDialog(String localVersionHint,
			final String localApkPath, String version) {
		InstallDialog mDialog = new InstallDialog.Builder(this)
				.setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage(localVersionHint).setCancelStr("残忍拒绝")
				.setOKStr("极速升级").positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdateUtil.installAPK(MainActivity.this, new File(
								localApkPath));
					}
				}).build();
		mDialog.show();
	}

	private void getMainActivity() {
		CommUtil.getHomeActivityPage(MainActivity.this, handlerHomeActivity);
	}

	private AsyncHttpResponseHandler handlerHomeActivity = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->首页活动：= " + new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("homeActivityPage")
								&& !objectData.isNull("homeActivityPage")) {
							JSONObject objectPage = objectData
									.getJSONObject("homeActivityPage");
							// if (array.length()>0) {
							// for (int i = 0; i < array.length(); i++) {
							// JSONObject objectPage = array.getJSONObject(0);
							if (objectPage.has("activityPic")
									&& !objectPage.isNull("activityPic")) {
								activityPic = objectPage
										.getString("activityPic");
							}
							if (objectPage.has("activityUrl")
									&& !objectPage.isNull("activityUrl")) {
								activityUrl = objectPage
										.getString("activityUrl");
							}
							if (objectPage.has("countType")
									&& !objectPage.isNull("countType")) {
								countType = objectPage.getString("countType");
							}
							if (objectPage.has("HomeActivityPageId")
									&& !objectPage.isNull("HomeActivityPageId")) {
								HomeActivityPageId = objectPage.getString("HomeActivityPageId");
							}
							if (objectPage.has("updateTime")
									&& !objectPage.isNull("updateTime")) {
								updateTime = objectPage.getString("updateTime");
								if (spUtil.getString("updateTime", "").equals(
										"")) {// 第一次启动
									ActivityPage();
									spUtil.saveString("updateTime", updateTime);
								} else {// 非第一次
									if (spUtil.getString("updateTime", "").equals(updateTime)) {// 本地存的跟服务器返回一致// 这里判断type// 1 或者 0 // 判断跳转
										if (countType.equals("0")) {
											
										} else if (countType.equals("1")) {
											spUtil.saveString("updateTime",updateTime);
											ActivityPage();
										}
									} else {// 这里时间不一致 说明服务器刷新了 弹窗活动
										spUtil.saveString("updateTime",updateTime);
										ActivityPage();
									}
								}

							}
							// }
							// }
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

	private void ActivityPage() {
		Intent intent = new Intent(MainActivity.this, MainAcPage.class);
		intent.putExtra("activityPic", activityPic);
		intent.putExtra("activityUrl", activityUrl);
		startActivity(intent);
	}

	private void getFragment() {
		fm = getSupportFragmentManager();
		mainFragment = (MainFragment) fm.findFragmentByTag("main");
		petCircleOrSelectFragment = (PetCircleOrSelectFragment) fm
				.findFragmentByTag("circle");
		orderFragment = (OrderFragment) fm.findFragmentByTag("order");
		myFragment = (MyFragment) fm.findFragmentByTag("my");
		ft = fm.beginTransaction();
		detachFragment(mainFragment);
		detachFragment(petCircleOrSelectFragment);
		detachFragment(orderFragment);
		detachFragment(myFragment);
	}

	private void detachFragment(Fragment fragment) {
		if (fragment != null)
			ft.detach(fragment);
	}

	private void addFragment(Fragment fragment, Fragment newfragment, String tag) {
		if (fragment == null) {
			ft.add(android.R.id.tabcontent, newfragment, tag);
		} else {
			ft.attach(fragment);
		}
	}

	private void addTab(String tag, int icon, String name) {
		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(getTabView(icon, name, tag));
		tabSpec.setContent(new MTabContent(getBaseContext()));
		mTabHost.addTab(tabSpec);
	}

	// 隐藏还是显示我的tab小红点
	public void setFlagmy(int visibility) {
		if (vFlagmy != null) {
			vFlagmy.setVisibility(visibility);
		}
	}

	private View getTabView(int icon, String name, String tag) {
		View view = mInflater.inflate(R.layout.tab_item, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.tab_icon);
		TextView tvName = (TextView) view.findViewById(R.id.tab_name);
		View vFlag = view.findViewById(R.id.tab_flag);
		if ("order".equals(tag)) {
			ivTabOrder = ivIcon;
			ivTabOrder.setTag(tag);
			tvTabOrder = tvName;
			tvTabOrder.setTag(tag);

			vFlagorder = vFlag;
			vFlagorder.setTag(tag);
		} else if ("main".equals(tag)) {
			ivTabMain = ivIcon;
			ivTabMain.setTag(tag);
			tvTabMain = tvName;
			tvTabMain.setTag(tag);
			vFlagmain = vFlag;
			vFlagmain.setTag(tag);
		} else if ("circle".equals(tag)) {
			ivTabMemb = ivIcon;
			ivTabMemb.setTag(tag);
			tvTabMemb = tvName;
			tvTabMemb.setTag(tag);
			vFlagmemb = vFlag;
			vFlagmemb.setTag(tag);
		} else if ("my".equals(tag)) {
			ivTabMy = ivIcon;
			ivTabMy.setTag(tag);
			tvTabMy = tvName;
			tvTabMy.setTag(tag);
			vFlagmy = vFlag;
			vFlagmy.setTag(tag);
		}
		ivIcon.setImageResource(icon);
		tvName.setText(name);
		return view;
	}

	private void initReceiver() {
		receiver = new MReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.mainactivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}

	private class MReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int previous = intent.getIntExtra("previous", 0);
			Utils.mLogError("MainActitvity收到广播" + previous);
			if (previous == Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY);
			} else if (previous == Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
			} else if (previous == Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY);
			} else if (previous == Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY);
			} else if (previous == Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT) {
				handler.sendEmptyMessage(Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
			} else if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
			} else if (previous == Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY);
			} else if (previous == Global.PRE_EVALUATEOVER_BACK_MAIN) {
				handler.sendEmptyMessage(Global.PRE_EVALUATEOVER_BACK_MAIN);
			} else if (previous == Global.PRE_LOGOUT_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_LOGOUT_TO_MAINACTIVITY);
			} else if (previous == Global.PRE_ORDER_LIST_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.PRE_ORDER_LIST_TO_MAINACTIVITY);
			} else if (previous == Global.PETADD_BACK_PETINFO_BACK_MY) {
				handler.sendEmptyMessage(Global.PETADD_BACK_PETINFO_BACK_MY);
			} else if (previous == Global.DELETEPET_TO_UPDATEUSERINFO) {
				handler.sendEmptyMessage(Global.DELETEPET_TO_UPDATEUSERINFO);
			} else if (previous == Global.PAYSUCCESS_TO_MYFRAGMENT) {
				handler.sendEmptyMessage(Global.PAYSUCCESS_TO_MYFRAGMENT);
			} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
				handler.sendEmptyMessage(Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
			} else if (previous == Global.GROWTH_TO_USERMEMBERFRAGMENT) {
				handler.sendEmptyMessage(Global.GROWTH_TO_USERMEMBERFRAGMENT);
			} else if (previous == Global.CARDS_TO_MAINACTIVITY) {
				handler.sendEmptyMessage(Global.CARDS_TO_MAINACTIVITY);
			}
		}

	}

	private long exitTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			exit();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtil.showToastShortBottom(this, "再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			/*
			 * spUtil.removeData("city"); spUtil.removeData("username");
			 * spUtil.removeData("userimage"); spUtil.removeData("payway");
			 * spUtil.removeData("petid"); spUtil.removeData("petkind");
			 * spUtil.removeData("petname"); spUtil.removeData("petimage");
			 * spUtil.removeData("addressid"); spUtil.removeData("lat");
			 * spUtil.removeData("lng"); spUtil.removeData("address");
			 * spUtil.removeData("serviceloc"); spUtil.removeData("state");
			 * spUtil.removeData("shopid"); spUtil.removeData("newaddr");
			 * spUtil.removeData("newlat"); spUtil.removeData("newlng");
			 * spUtil.removeData("newshopid");
			 */
			MobclickAgent.onKillProcess(this);
			onDestroy();
			this.finish();
			System.exit(0);
		}
	}

	private void initBD() {
		mLocationClient = new LocationClient(this);
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

	private class MLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			StringBuffer sb = new StringBuffer();
			sb.append("lotitude=" + location.getLatitude());
			sb.append("longitude=" + location.getLongitude());
			sb.append("addr=" + location.getAddrStr());
			sb.append("city=" + location.getCity());
			sb.append("citycode=" + location.getCityCode());
			Utils.mLogError("main:" + sb.toString());
			if (isFirst) {
				isFirst = false;
				registGeTuiToService(location.getLatitude(),
						location.getLongitude());
			}
			lat = location.getLatitude();
			lng = location.getLongitude();
			autoLogin(lat, lng);

			mLocationClient.stop();
		}

	}

	private void registGeTuiToService(double lat, double lng) {
		CommUtil.registGeTuitoService(this, spUtil.getString("cellphone", ""),
				Global.getCurrentVersion(this), Global.getIMEI(this),
				spUtil.getInt("userid", 0), Global.getPushID(this), lat, lng,
				getuiHandler);
		Utils.mLogError("==-->tokedid " + Global.getPushID(this));
	}

	private void getIndex() {
		CommUtil.getIndex(this, spUtil.getString("cellphone", ""),
				Global.getCurrentVersion(this), Global.getIMEI(this),
				indexHandler);
	}

	/**
	 * 自动登录获取数据
	 */
	private void autoLogin(double lat, double lng) {
		String cellphone = spUtil.getString("cellphone", "");
		int userid = spUtil.getInt("userid", 0);
		if (!"".equals(cellphone) && 0 != userid) {
			CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
					Global.getIMEI(this), Global.getCurrentVersion(this),
					spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
		}
	}

	private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("自动登录：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						// if(jData.has("serviceLoc")&&!jData.isNull("serviceLoc")){
						// spUtil.saveInt("serviceloc",
						// jData.getInt("serviceLoc"));
						// }
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
							if (jUser.has("inviteCode")
									&& !jUser.isNull("inviteCode")) {
								spUtil.saveString("invitecode",
										jUser.getString("inviteCode"));
							}
							if (jUser.has("cellPhone")
									&& !jUser.isNull("cellPhone")) {
								spUtil.saveString("cellphone",
										jUser.getString("cellPhone"));
							}
							if (jUser.has("payWay") && !jUser.isNull("payWay")) {
								spUtil.saveInt("payway", jUser.getInt("payWay"));
							}
							if (jUser.has("userName")
									&& !jUser.isNull("userName")) {
								spUtil.saveString("username",
										jUser.getString("userName"));
							}

							if (jUser.has("avatarPath")
									&& !jUser.isNull("avatarPath")) {
								spUtil.saveString("userimage",
										jUser.getString("avatarPath"));
							}
							if (jUser.has("UserId") && !jUser.isNull("UserId")) {
								spUtil.saveInt("userid", jUser.getInt("UserId"));
							}
							if (jUser.has("pet") && !jUser.isNull("pet")) {
								JSONObject jPet = jUser.getJSONObject("pet");
								if (jPet.has("isCerti")
										&& !jPet.isNull("isCerti")) {
									spUtil.saveInt("isCerti",
											jPet.getInt("isCerti"));
								}
								if (jPet.has("PetId") && !jPet.isNull("PetId")) {
									spUtil.saveInt("petid",
											jPet.getInt("PetId"));
								}
								if (jPet.has("petKind")
										&& !jPet.isNull("petKind")) {
									spUtil.saveInt("petkind",
											jPet.getInt("petKind"));
								}

								if (jPet.has("petName")
										&& !jPet.isNull("petName")) {
									spUtil.saveString("petname",
											jPet.getString("petName"));
								}
								if (jPet.has("avatarPath")
										&& !jPet.isNull("avatarPath")) {
									spUtil.saveString("petimage",
											jPet.getString("avatarPath"));
									// 保存选择的宠物的头像
									spUtil.saveString(
											"petimg",
											CommUtil.getServiceBaseUrl()
													+ jPet.getString("avatarPath"));
								}
								if (jPet.has("availService")
										&& !jPet.isNull("availService")) {
									JSONArray jarr = jPet
											.getJSONArray("availService");
									if (jarr.length() > 0) {
										petServices = new int[jarr.length()];
										for (int i = 0; i < jarr.length(); i++) {
											petServices[i] = jarr.getInt(i);
										}
									}
								}

							} else {
								spUtil.removeData("isCerti");
								spUtil.removeData("petid");
								spUtil.removeData("petKind");
								spUtil.removeData("petname");
								spUtil.removeData("petimage");
							}
							if (jUser.has("myPet") && !jUser.isNull("myPet")) {
								JSONObject jMyPet = jUser
										.getJSONObject("myPet");
								if (jMyPet.has("CustomerPetId")
										&& !jMyPet.isNull("CustomerPetId")) {
									spUtil.saveInt("customerpetid",
											jMyPet.getInt("CustomerPetId"));
								} else {
									spUtil.removeData("customerpetid");
								}
								if (jMyPet.has("nickName")
										&& !jMyPet.isNull("nickName")) {
									spUtil.saveString("customerpetname",
											jMyPet.getString("nickName"));
								} else {
									spUtil.removeData("customerpetname");
								}
								if (jMyPet.has("avatarPath")
										&& !jMyPet.isNull("avatarPath")) {
									String mypetImage = jMyPet
											.getString("avatarPath");
									if (!mypetImage.startsWith("http://")
											&& !mypetImage
													.startsWith("https://")) {
										spUtil.saveString(
												"mypetImage",
												CommUtil.getServiceNobacklash()
														+ jMyPet.getString("avatarPath"));
									} else {
										spUtil.saveString("mypetImage",
												jMyPet.getString("avatarPath"));
									}
								} else {
									spUtil.removeData("mypetImage");
								}
							} else {
								spUtil.removeData("customerpetname");
								spUtil.removeData("customerpetid");
								spUtil.removeData("mypetImage");

							}
						}
					}
				} else {
					spUtil.removeData("cellphone");
					spUtil.removeData("username");
					spUtil.removeData("userimage");
					spUtil.removeData("userid");
					spUtil.removeData("payway");
					spUtil.removeData("petid");
					spUtil.removeData("petkind");
					spUtil.removeData("petname");
					spUtil.removeData("petimage");
					spUtil.removeData("addressid");
					spUtil.removeData("lat");
					spUtil.removeData("lng");
					spUtil.removeData("address");
					spUtil.removeData("serviceloc");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}

	};
	private AsyncHttpResponseHandler getuiHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("注册个推：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {

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
	private List<String> pressImgList = new ArrayList<String>();
	private List<String> noPressImgList = new ArrayList<String>();
	private AsyncHttpResponseHandler indexHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("首页：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("nToBeComment")
							&& !jdata.isNull("nToBeComment")) {
						if (jdata.getInt("nToBeComment") > 0) {
							vFlagorder.setVisibility(View.VISIBLE);
						} else {
							vFlagorder.setVisibility(View.GONE);
						}
					} else {
						vFlagorder.setVisibility(View.GONE);
					}
					if (jdata.has("index") && !jdata.isNull("index")) {
						JSONObject jindex = jdata.getJSONObject("index");
						if (jindex.has("bottom") && !jindex.isNull("bottom")) {
							JSONObject jbottom = jindex.getJSONObject("bottom");
							if (jbottom.has("list") && !jbottom.isNull("list")) {
								pressImgList.clear();
								noPressImgList.clear();
								JSONArray jlist = jbottom.getJSONArray("list");
								for (int i = 0; i < jlist.length(); i++) {
									JSONObject jitem = jlist.getJSONObject(i);
									if (jitem.has("title")
											&& !jitem.isNull("title"))
										setText(i, jitem.getString("title"));
									if (jitem.has("pic")
											&& !jitem.isNull("pic")) {
										noPressImgList.add(jitem
												.getString("pic"));
										setImg(i, jitem.getString("pic"));
									}
									if (jitem.has("picH")
											&& !jitem.isNull("picH")) {
										String string = jitem.getString("picH");
										if (string != null
												&& !TextUtils.isEmpty(string)) {
											pressImgList.add(string);
										}
									}
								}
								setImg(0, pressImgList.get(0));
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

	/**
	 * 获取最新版本和是否强制升级
	 */
	private void getLatestVersion() {
		CommUtil.getLatestVersion(this,
				Global.getCurrentVersion(MainActivity.this),
				System.currentTimeMillis(), latestHanler);
	}

	private AsyncHttpResponseHandler latestHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("最新版本：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				if (jobj != null) {
					if (jobj.has("code") && !jobj.isNull("code")) {
						int resultCode = jobj.getInt("code");
						if (0 == resultCode) {
							if (jobj.has("data") && !jobj.isNull("data")) {
								String latestVersion = null;
								String downloadPath = null;
								String versionHint = null;
								boolean isUpgrade = false;
								JSONObject jData = jobj.getJSONObject("data");
								if (jData.has("nversion")
										&& !jData.isNull("nversion")) {
									latestVersion = jData.getString("nversion");
								}
								if (jData.has("download")
										&& !jData.isNull("download")) {
									downloadPath = jData.getString("download");
								}
								if (jData.has("text") && !jData.isNull("text")) {
									versionHint = jData.getString("text");
								}
								if (jData.has("mandate")
										&& !jData.isNull("mandate")) {
									isUpgrade = jData.getBoolean("mandate");
								}
								if (latestVersion != null
										&& !TextUtils.isEmpty(latestVersion)) {
									boolean isLatest = UpdateUtil
											.compareVersion(
													latestVersion,
													Global.getCurrentVersion(MainActivity.this));
									if (isLatest) {// 需要下载安装最新版
										if (downloadPath != null
												&& !TextUtils
														.isEmpty(downloadPath)) {
											if (isUpgrade) {
												// 强制升级
												showForceUpgradeDialog(
														versionHint,
														downloadPath,
														latestVersion);
											} else {
												// 非强制升级
												showUpgradeDialog(versionHint,
														downloadPath,
														latestVersion);
											}
										}
									}
								}
							}
						}
					}
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

	private void showForceUpgradeDialog(String msg, final String path,
			String version) {
		InstallDialog mDialog = new InstallDialog.Builder(MainActivity.this)
				.setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_ALERT).setMessage(msg)
				.setCancelable(false).setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoDownload(path);
					}
				}).build();
		mDialog.show();
	}

	private void showUpgradeDialog(String msg, final String path, String version) {
		InstallDialog mDialog = new InstallDialog.Builder(MainActivity.this)
				.setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(msg)
				.setCancelStr("残忍拒绝").setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoDownload(path);
					}
				}).build();
		mDialog.show();
	}

	private void gotoDownload(final String apkurl) {
		mNotification = new MyNotification(this, null, 1);
		mNotification.showCustomizeNotification(R.drawable.logo, "宠物家",
				R.layout.download_notif);
		new Thread() {
			@Override
			public void run() {
				File apkfile = Global.downloadApk(apkurl,
						Utils.getDir(MainActivity.this), "pet.apk", handler);
				if (null != apkfile && 0 < apkfile.length()) {
					installAPK(apkfile);
				}
			}
		}.start();
	}

	// 自动安装一个apk文件
	private void installAPK(File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}

	private void setText(int i, String str) {
		if (str == null || "".equals(str.trim())
				|| "null".equals(str.trim().toLowerCase()))
			return;
		switch (i) {
		case 0:
			tvTabMain.setText(str);
			tv_mainactivity_one.setText(str);
			break;
		case 1:
			tvTabMemb.setText(str);
			tv_mainactivity_two.setText(str);
			break;
		case 2:
			tvTabOrder.setText(str);
			tv_mainactivity_three.setText(str);
			break;
		case 3:
			tvTabMy.setText(str);
			tv_mainactivity_four.setText(str);
			break;
		}
	}

	private void setImg(int i, String str) {
		if (str == null || "".equals(str.trim())
				|| "null".equals(str.trim().toLowerCase()))
			return;
		switch (i) {
		case 0:
			ImageLoaderUtil.loadImg(str, ivTabMain, R.drawable.tab_main, null);
			ImageLoaderUtil.loadImg(str, iv_mainactivity_one,
					R.drawable.tab_home_passed, null);
			break;
		case 1:
			ImageLoaderUtil.loadImg(str, ivTabMemb, R.drawable.tab_knowledge,
					null);
			ImageLoaderUtil.loadImg(str, iv_mainactivity_two,
					R.drawable.tab_knowledge_passed, null);
			break;
		case 2:
			ImageLoaderUtil
					.loadImg(str, ivTabOrder, R.drawable.tab_order, null);
			ImageLoaderUtil.loadImg(str, iv_mainactivity_three,
					R.drawable.tab_order_passed, null);
			break;
		case 3:
			ImageLoaderUtil.loadImg(str, ivTabMy, R.drawable.tab_my, null);
			ImageLoaderUtil.loadImg(str, iv_mainactivity_four,
					R.drawable.tab_my_passed, null);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
		if (installApkReceiver != null)
			unregisterReceiver(installApkReceiver);
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
		}
		super.onDestroy();
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	// 使用sso授权新浪微博客户端时，fragment内部onActivityResult
	// 方法不执行，暂时未找到查询方法，将当前方法在父activity中重写，可以达到效果后期查询具体解决方案
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		Fragment fragment = fm.findFragmentByTag(tabTag);
		fragment.onActivityResult(requestCode, resultCode, data);

	}

	public void setSelect(int selection) {
		this.selection = selection;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}
}
