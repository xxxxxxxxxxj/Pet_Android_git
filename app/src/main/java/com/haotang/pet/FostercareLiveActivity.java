package com.haotang.pet;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionWithParamListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.LiveServicesItemAdapter;
import com.haotang.pet.entity.LiveServicesItemBean;
import com.haotang.pet.entity.LiveServicesItemBean.DataBean;
import com.haotang.pet.entity.LiveServicesItemBean.DataBean.InfoBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.SelectableRoundedImageView;

/**
 * <p>
 * Title:FostercareLiveActivity
 * </p>
 * <p>
 * Description:寄养直播详情页
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-12 下午3:09:39
 */
public class FostercareLiveActivity extends SuperActivity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPlayingBufferCacheListener,
		OnCompletionWithParamListener, OnClickListener {
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private RelativeLayout rl_live_fostercare;
	private RelativeLayout rl_live_fostercare_screen;
	private RelativeLayout rl_live_fostercare_screenclick;
	private RelativeLayout rl_live_fostercare_info;
	private SelectableRoundedImageView sriv_live_fostercare;
	private ImageView iv_live_fostercare_fz;
	private TextView tv_live_fostercare_tcm;
	private TextView tv_live_fostercare_fjh;
	private TextView tv_live_fostercare_rzsj;
	private TextView tv_live_fostercare_ldsj;
	private TextView tv_live_fostercare_rzts;
	private TextView tv_live_fostercare_fx;
	private TextView tv_live_fostercare_fxfjh;
	private TextView tv_live_fostercare_needxz;
	private TextView tv_live_fostercare_bz;
	private SelectableRoundedImageView sriv_live_fostercare_jysinfo;
	private TextView tv_live_fostercare_jysname;
	private TextView tv_live_fostercare_jystype;
	private TextView tv_live_fostercare_jysjsnum;
	private LinearLayout ll_live_fostercare_xxinfo;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private MyGridView mgv_live_fostercare;
	private boolean isOpen;
	private String Url;
	private int screenWidth;
	private int videoHeight;
	private int orderid;
	private static final String AK = "W8hthyKDT42idmitIg1t4D50";
	private static final String SK = "Ub9DKdMfdqIknHKL1uzmlydU1vvFUObU";
	private final String TAG = "FostercareLiveActivity";
	private final Object SYNC_Playing = new Object();
	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "FostercareLiveActivity";
	private int mLastPos;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	private HandlerThread mHandlerThread;
	private EventHandler mEventHandler;
	private static final int UI_EVENT_PLAY = 0;
	private static final int FULLSCREEN = 1;
	private static final int BANFULLSCREEN = 0;
	private static final int VISIBLEORGONE = 1;
	private static final int TITLEVISIBLEORGONE = 2;
	private static final int PLAYER_IDLE = 3;
	protected static final int SUSPEND_PLAY = 4;
	private static final int ONPREPARED = 5;
	private static final int PLAYER_COMPLETE = 6;
	private BVideoView bvv_live_fostercare;
	private ScrollView sv_live_fostercare;
	private RelativeLayout live_fostercare_title;
	private RelativeLayout rl_live_fostercare_fullscreentitle;
	private RelativeLayout rl_live_fostercare_fullscreenbackclick;
	private TextView tv_live_fostercare_fullscreentitle;
	private RelativeLayout rl_live_fostercare_default;
	private LinearLayout ll_live_fostercare_jxgk;
	private Button btn_live_fostercare_jxgk;
	private TextView tv_live_fostercare_default;
	private boolean isSuspend;
	protected NetworkInfo netInfo;
	private int roomType = 1;// 视频类型
	private LiveServicesItemAdapter liveServicesItemAdapter;

	private List<DataBean> data;
	protected int shopId;
	protected int WorkerId;
	protected int totalOrderAmount;
	protected String avatar;
	protected int gender;
	protected String name;
	protected int sort;
	protected String realName;
	protected String avatarPath;
	protected int PetId;
	protected int petKind;
	protected String petName;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UI_EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				/**
				 * 设置播放url
				 */
				bvv_live_fostercare.setVideoPath(Url);
				/**
				 * 显示或者隐藏缓冲提示
				 */
				bvv_live_fostercare.showCacheInfo(false);
				/**
				 * 开始播放
				 */
				bvv_live_fostercare.start();
				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fostercare_live);
		init();
		initView();
		initLivePlayer();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, intentFilter);
	}

	// 监听网络状态
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			netInfo = mConnectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isAvailable()) {
				if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {// WiFi网络
					Log.v(TAG, "WiFi网络");
				} else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {// 有线网络
					closeDialog();
					Log.v(TAG, "有线网络");
				} else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {// 移动网络
					Log.v(TAG, "移动网络");
					closeDialog();
					if (!isSuspend) {
						mHandler.sendEmptyMessage(SUSPEND_PLAY);
					}
				}
			} else {// 网络断开，无网络
				Log.v(TAG, "网络断开，无网络");
				closeDialog();
				mHandler.sendEmptyMessage(PLAYER_IDLE);
			}
		}
	};

	private void initLivePlayer() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
		// 设置AK
		BVideoView.setAKSK(AK, SK);
		/**
		 * 开启后台事件处理线程
		 */
		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());
		bvv_live_fostercare.setLogLevel(4);
		/**
		 * 注册listener
		 */
		bvv_live_fostercare.setOnPreparedListener(this);
		bvv_live_fostercare.setOnCompletionListener(this);
		bvv_live_fostercare.setOnCompletionWithParamListener(this);
		bvv_live_fostercare.setOnErrorListener(this);
		bvv_live_fostercare.setOnInfoListener(this);
		/**
		 * 设置解码模式
		 */
		bvv_live_fostercare.setDecodeMode(BVideoView.DECODE_SW);
		switchScreen(BANFULLSCREEN);
	}

	private void switchScreen(int flag) {
		if (flag == FULLSCREEN) {// 全屏
			rl_live_fostercare.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			live_fostercare_title.setVisibility(View.GONE);
			rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
			sv_live_fostercare.setVisibility(View.GONE);
			rl_live_fostercare_screen.setVisibility(View.GONE);
		} else if (flag == BANFULLSCREEN) {// 半屏
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, videoHeight);
			layoutParams.addRule(RelativeLayout.BELOW,
					R.id.live_fostercare_title);
			rl_live_fostercare.setLayoutParams(layoutParams);
			live_fostercare_title.setVisibility(View.VISIBLE);
			rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
			sv_live_fostercare.setVisibility(View.VISIBLE);
			rl_live_fostercare_screen.setVisibility(View.GONE);
		}
	}

	// 查询订单明细
	private void getOrderDetails() {
		showDialog();
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderid, getOrderDetails);
	}

	private AsyncHttpResponseHandler queryRoomMenuItemsHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			if (networkType != 1) {
				closeDialog();
			}
			String result = new String(responseBody);
			processData(result);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			closeDialog();
		}
	};

	private void processData(String result) {
		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj.has("code") && !jobj.isNull("code")) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						Gson gson = new Gson();
						LiveServicesItemBean liveServicesItemBean = gson
								.fromJson(jdata.toString(),
										LiveServicesItemBean.class);
						if (liveServicesItemBean != null) {
							data = liveServicesItemBean.getData();
							if (data != null && data.size() > 0) {
								mgv_live_fostercare.setVisibility(View.VISIBLE);
								liveServicesItemAdapter = new LiveServicesItemAdapter(
										this);
								mgv_live_fostercare
										.setAdapter(liveServicesItemAdapter);
								liveServicesItemAdapter.setData(data);
							} else {
								mgv_live_fostercare.setVisibility(View.GONE);
							}
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONObject jdata = jsonObject.getJSONObject("data");
						if (jdata.has("shop") && !jdata.isNull("shop")) {
							JSONObject objectshop = jdata.getJSONObject("shop");
							if (objectshop.has("ShopId")
									&& !objectshop.isNull("ShopId")) {
								shopId = objectshop.getInt("ShopId");
							}
						}
						if (jdata.has("needBathTag")
								&& !jdata.isNull("needBathTag")) {
							tv_live_fostercare_needxz.setText(jdata
									.getString("needBathTag"));
						}
						StringBuilder builder = new StringBuilder();
						if (jdata.has("pet") && !jdata.isNull("pet")) {
							JSONObject objectPet = jdata.getJSONObject("pet");
							if (objectPet.has("avatarPath")
									&& !objectPet.isNull("avatarPath")) {
								avatarPath = objectPet.getString("avatarPath");
								ImageLoaderUtil.loadImg(avatarPath,
										sriv_live_fostercare,
										R.drawable.user_icon_unnet, null);
							}
							if (objectPet.has("petName")
									&& !objectPet.isNull("petName")) {
								builder.append(objectPet.getString("petName")
										.trim());
							}
							if (objectPet.has("PetId")
									&& !objectPet.isNull("PetId")) {
								PetId = objectPet.getInt("PetId");
							}
							if (objectPet.has("petKind")
									&& !objectPet.isNull("petKind")) {
								petKind = objectPet.getInt("petKind");
							}
							if (objectPet.has("petName")
									&& !objectPet.isNull("petName")) {
								petName = objectPet.getString("petName");
							}
						}
						builder.append("寄养套餐");
						String nickName = "";
						if (jdata.has("myPet") && !jdata.isNull("myPet")) {// 寄养类型套餐
							JSONObject objectMyPet = jdata
									.getJSONObject("myPet");
							if (objectMyPet.has("nickName")
									&& !objectMyPet.isNull("nickName")) {
								nickName = objectMyPet.getString("nickName");
							}
						}
						if (nickName.equals("")) {
							tv_live_fostercare_tcm.setText(builder);
						} else {
							tv_live_fostercare_tcm.setText(nickName + "寄养套餐");
						}

						String RoomNum = "";
						if (jdata.has("room") && !jdata.isNull("room")) {
							JSONObject objectRoom = jdata.getJSONObject("room");
							if (objectRoom.has("num")
									&& !objectRoom.isNull("num")) {
								RoomNum = objectRoom.getString("num");
							}
							if (objectRoom.has("liveRoom")
									&& !objectRoom.isNull("liveRoom")) {
								JSONObject objectobjectRoom = objectRoom
										.getJSONObject("liveRoom");
								if (objectobjectRoom.has("liveUrl")
										&& !objectobjectRoom.isNull("liveUrl")) {
									Url = objectobjectRoom.getString("liveUrl");
									// 播放
									isNetWorkPlay();
									roomType = objectobjectRoom
											.getInt("roomType");
									// 请求底部菜单
									CommUtil.queryRoomMenuItems(
											spUtil.getString("cellphone", ""),
											Global.getIMEI(FostercareLiveActivity.this),
											FostercareLiveActivity.this,
											roomType, orderid,
											queryRoomMenuItemsHandler);
								}
							}
						}
						tv_live_fostercare_fjh.setText(RoomNum);
						tv_live_fostercare_fxfjh.setText(RoomNum);
						if (jdata.has("task") && !jdata.isNull("task")) {
							JSONObject objectTask = jdata.getJSONObject("task");
							if (objectTask.has("startTime")
									&& !objectTask.isNull("startTime")) {
								tv_live_fostercare_rzsj.setText(objectTask
										.getString("startTime"));
							}
							if (objectTask.has("stopTime")
									&& !objectTask.isNull("stopTime")) {
								tv_live_fostercare_ldsj.setText(objectTask
										.getString("stopTime"));
							}
						}
						if (jdata.has("ndays") && !jdata.isNull("ndays")) {
							tv_live_fostercare_rzts.setText(" 共"
									+ jdata.getInt("ndays") + "天");
						}
						String RoomName = "";
						if (jdata.has("roomType") && !jdata.isNull("roomType")) {
							JSONObject objectRoom = jdata
									.getJSONObject("roomType");
							if (objectRoom.has("name")
									&& !objectRoom.isNull("name")) {
								String name = objectRoom.getString("name");
								RoomName = name + " ";
							}
						}
						tv_live_fostercare_fx.setText(RoomName);
						if (jdata.has("remark") && !jdata.isNull("remark")) {
							tv_live_fostercare_bz.setText(jdata
									.getString("remark"));
						} else {
							tv_live_fostercare_bz.setText("无");
						}
						if (jdata.has("worker") && !jdata.isNull("worker")) {
							JSONObject objectworker = jdata
									.getJSONObject("worker");

							if (objectworker.has("WorkerId")
									&& !objectworker.isNull("WorkerId")) {
								WorkerId = objectworker.getInt("WorkerId");
							}

							if (objectworker.has("avatar")
									&& !objectworker.isNull("avatar")) {
								avatar = objectworker.getString("avatar");
								ImageLoaderUtil.loadImg(avatar,
										sriv_live_fostercare_jysinfo,
										R.drawable.user_icon_unnet, null);
							}
							if (objectworker.has("realName")
									&& !objectworker.isNull("realName")) {
								realName = objectworker.getString("realName");
								tv_live_fostercare_jysname.setText(realName);
							}
							if (objectworker.has("level")
									&& !objectworker.isNull("level")) {
								JSONObject objectlevel = objectworker
										.getJSONObject("level");
								if (objectlevel.has("name")
										&& !objectlevel.isNull("name")) {
									name = objectlevel.getString("name");
									tv_live_fostercare_jystype.setText(name);
								}
								if (objectlevel.has("sort")
										&& !objectlevel.isNull("sort")) {
									sort = objectlevel.getInt("sort");
								}
							}
							if (objectworker.has("totalOrderAmount")
									&& !objectworker.isNull("totalOrderAmount")) {
								totalOrderAmount = objectworker
										.getInt("totalOrderAmount");
								tv_live_fostercare_jysjsnum.setText("接单:     "
										+ totalOrderAmount);
							}
							if (objectworker.has("gender")
									&& !objectworker.isNull("gender")) {
								gender = objectworker.getInt("gender");
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
			closeDialog();
		}
	};

	private void initView() {
		rl_live_fostercare = (RelativeLayout) findViewById(R.id.rl_live_fostercare);
		rl_live_fostercare_screen = (RelativeLayout) findViewById(R.id.rl_live_fostercare_screen);
		rl_live_fostercare_screenclick = (RelativeLayout) findViewById(R.id.rl_live_fostercare_screenclick);
		rl_live_fostercare_info = (RelativeLayout) findViewById(R.id.rl_live_fostercare_info);
		sriv_live_fostercare = (SelectableRoundedImageView) findViewById(R.id.sriv_live_fostercare);
		iv_live_fostercare_fz = (ImageView) findViewById(R.id.iv_live_fostercare_fz);
		tv_live_fostercare_tcm = (TextView) findViewById(R.id.tv_live_fostercare_tcm);
		tv_live_fostercare_fjh = (TextView) findViewById(R.id.tv_live_fostercare_fjh);
		tv_live_fostercare_rzsj = (TextView) findViewById(R.id.tv_live_fostercare_rzsj);
		tv_live_fostercare_ldsj = (TextView) findViewById(R.id.tv_live_fostercare_ldsj);
		tv_live_fostercare_rzts = (TextView) findViewById(R.id.tv_live_fostercare_rzts);
		tv_live_fostercare_fx = (TextView) findViewById(R.id.tv_live_fostercare_fx);
		tv_live_fostercare_fxfjh = (TextView) findViewById(R.id.tv_live_fostercare_fxfjh);
		tv_live_fostercare_needxz = (TextView) findViewById(R.id.tv_live_fostercare_needxz);
		tv_live_fostercare_bz = (TextView) findViewById(R.id.tv_live_fostercare_bz);
		sriv_live_fostercare_jysinfo = (SelectableRoundedImageView) findViewById(R.id.sriv_live_fostercare_jysinfo);
		tv_live_fostercare_jysname = (TextView) findViewById(R.id.tv_live_fostercare_jysname);
		tv_live_fostercare_jystype = (TextView) findViewById(R.id.tv_live_fostercare_jystype);
		tv_live_fostercare_jysjsnum = (TextView) findViewById(R.id.tv_live_fostercare_jysjsnum);
		ll_live_fostercare_xxinfo = (LinearLayout) findViewById(R.id.ll_live_fostercare_xxinfo);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		mgv_live_fostercare = (MyGridView) findViewById(R.id.mgv_live_fostercare);
		sv_live_fostercare = (ScrollView) findViewById(R.id.sv_live_fostercare);
		live_fostercare_title = (RelativeLayout) findViewById(R.id.live_fostercare_title);
		bvv_live_fostercare = (BVideoView) findViewById(R.id.bvv_live_fostercare);
		rl_live_fostercare_fullscreentitle = (RelativeLayout) findViewById(R.id.rl_live_fostercare_fullscreentitle);
		rl_live_fostercare_fullscreenbackclick = (RelativeLayout) findViewById(R.id.rl_live_fostercare_fullscreenbackclick);
		tv_live_fostercare_fullscreentitle = (TextView) findViewById(R.id.tv_live_fostercare_fullscreentitle);
		rl_live_fostercare_default = (RelativeLayout) findViewById(R.id.rl_live_fostercare_default);
		ll_live_fostercare_jxgk = (LinearLayout) findViewById(R.id.ll_live_fostercare_jxgk);
		btn_live_fostercare_jxgk = (Button) findViewById(R.id.btn_live_fostercare_jxgk);
		tv_live_fostercare_default = (TextView) findViewById(R.id.tv_live_fostercare_default);
		bt_titlebar_other.setVisibility(View.GONE);
		tv_titlebar_title.setText("寄养直播");
		rl_live_fostercare.setOnClickListener(this);
		rl_live_fostercare_screenclick.setOnClickListener(this);
		rl_live_fostercare_info.setOnClickListener(this);
		ib_titlebar_back.setOnClickListener(this);
		rl_live_fostercare_fullscreenbackclick.setOnClickListener(this);
		btn_live_fostercare_jxgk.setOnClickListener(this);
		screenWidth = Utils.getDisplayMetrics(this)[0];
		videoHeight = screenWidth * 11 / 20;
		rl_live_fostercare_screen.bringToFront();
		rl_live_fostercare_fullscreentitle.bringToFront();
		rl_live_fostercare_default.bringToFront();
		mgv_live_fostercare.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (data != null && data.size() > 0) {
					DataBean dataBean = data.get(position);
					if (dataBean != null) {
						InfoBean info = dataBean.getInfo();
						String servicename = dataBean.getName();
						int menuItemId = dataBean.getMenuItemId();
						if (info != null) {// 只有到店
							int serviceType = info.getServiceType();
							int type = info.getType();
							List<Integer> serviceIds = info.getServiceIds();
							int serviceId = 0;
							if (serviceIds != null && serviceIds.size() > 0) {
								serviceId = serviceIds.get(0);
							}
							if (serviceType != 0) {
								if (serviceType == 1) {// 洗澡
									goNext(AppointmentActivity.class,
											serviceId, 1,
											Global.NOAREA_TO_APPOINTMENT,
											servicename);
								} else if (serviceType == 2) {// 美容
									goNext(AppointmentActivity.class,
											serviceId, 2,
											Global.NOAREA_TO_APPOINTMENT,
											servicename);
								} else if (serviceType == 3) {// 特色服务
									goNext(ServiceFeature.class, menuItemId, 3,
											Global.NOAREA_TO_APPOINTMENT,
											servicename);
								}
							} else {
								if (type != 0) {
									if (type == 2) {// 寄养
										goNext(FostercareChooseroomActivity.class,
												100,
												100,
												Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET,
												servicename);
									} else if (type == 3) {// 游泳
										if (petKind == 1) {
											goNext(SwimDetailActivity.class,
													100, 100,
													Global.LIVE_TO_SWIM_DETAIL,
													servicename);
										} else {
											goNext(ChoosePetActivityNew.class,
													100,
													100,
													Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET,
													servicename);
										}
									}
								}
							}
						}
					}
				}
			}
		});
	}

	private void goNext(Class cls, int serviceid, int servicetype,
			int previous, String servicename) {
		Intent intent = new Intent(this, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", previous);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("serviceloc", 1);
		intent.putExtra("petid", PetId);
		intent.putExtra("liveFlag", "goneSwitchShop");
		intent.putExtra("petkind", petKind);
		intent.putExtra("petname", petName);
		intent.putExtra("servicename", servicename);
		intent.putExtra("petimage", avatarPath);
		intent.putExtra("avatarPath", avatarPath);
		intent.putExtra("clicksort", sort);
		intent.putExtra("shopid", shopId);
		startActivity(intent);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// 切换为竖屏
		if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
			switchScreen(BANFULLSCREEN);
		}
		// 切换为横屏
		else if (newConfig.orientation == this.getResources()
				.getConfiguration().ORIENTATION_LANDSCAPE) {
			switchScreen(FULLSCREEN);
		}
	}

	private void init() {
		Intent intent = getIntent();
		String stringExtra = intent.getStringExtra("orderid");
		if (stringExtra != null && !TextUtils.isEmpty(stringExtra)) {
			orderid = Integer.parseInt(stringExtra);
		}
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_live_fostercare_jxgk:// 继续播放
			showDialog();
			isSuspend = true;
			play();
			break;
		case R.id.rl_live_fostercare_fullscreenbackclick:// 恢复半屏
			back();
			break;
		case R.id.rl_live_fostercare:// 点击播放器
			if (rl_live_fostercare_default.getVisibility() == View.GONE) {
				if (getResources().getConfiguration().orientation == getResources()
						.getConfiguration().ORIENTATION_LANDSCAPE) {// 横屏
					if (rl_live_fostercare_fullscreentitle.getVisibility() == View.GONE) {
						rl_live_fostercare_fullscreentitle
								.setVisibility(View.VISIBLE);
						mHandler.removeMessages(TITLEVISIBLEORGONE);
						mHandler.sendEmptyMessageDelayed(TITLEVISIBLEORGONE,
								3000);
					} else {
						rl_live_fostercare_fullscreentitle
								.setVisibility(View.GONE);
					}
				} else {// 竖屏
					if (rl_live_fostercare_screen.getVisibility() == View.GONE) {
						rl_live_fostercare_screen.setVisibility(View.VISIBLE);
						mHandler.removeMessages(VISIBLEORGONE);
						mHandler.sendEmptyMessageDelayed(VISIBLEORGONE, 3000);
					} else {
						rl_live_fostercare_screen.setVisibility(View.GONE);
					}
				}
			}
			break;
		case R.id.rl_live_fostercare_screenclick:// 全屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏
			break;
		case R.id.rl_live_fostercare_info:// 下拉展开合上
			setFz();
			break;
		case R.id.ib_titlebar_back:// 返回
			back();
			break;
		default:
			break;
		}
	}

	private void back() {
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		if (getResources().getConfiguration().orientation == getResources()
				.getConfiguration().ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			finish();
		}
	}

	private void setFz() {
		if (isOpen) {
			isOpen = false;
			iv_live_fostercare_fz
					.setBackgroundResource(R.drawable.iv_fostercarechooseroom_fz_down);
			ll_live_fostercare_xxinfo.setVisibility(View.GONE);
		} else {
			isOpen = true;
			iv_live_fostercare_fz
					.setBackgroundResource(R.drawable.iv_fostercarechooseroom_fz_up);
			ll_live_fostercare_xxinfo.setVisibility(View.VISIBLE);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PLAYER_COMPLETE:// 播放完成
				Log.v(TAG, "case PLAYER_COMPLETE:// 播放完成");
				rl_live_fostercare_default.setVisibility(View.GONE);
				ll_live_fostercare_jxgk.setVisibility(View.GONE);
				rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
				rl_live_fostercare_screen.setVisibility(View.GONE);
				tv_live_fostercare_default.setVisibility(View.GONE);
				break;
			case ONPREPARED:// 隐藏提示
				Log.v(TAG, "// 隐藏提示");
				rl_live_fostercare_default.setVisibility(View.GONE);
				closeDialog();
				break;
			case SUSPEND_PLAY:// 移动网络下
				if (bvv_live_fostercare.isPlaying()) {
					/**
					 * 暂停播放
					 */
					bvv_live_fostercare.pause();
				}
				Log.v(TAG, "case SUSPEND_PLAY:// 移动网络下,暂停播放");
				rl_live_fostercare_default.setVisibility(View.VISIBLE);
				ll_live_fostercare_jxgk.setVisibility(View.VISIBLE);
				rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
				rl_live_fostercare_screen.setVisibility(View.GONE);
				tv_live_fostercare_default.setVisibility(View.GONE);
				break;
			case PLAYER_IDLE:// 播放出错
				closeDialog();
				Log.v(TAG, "case PLAYER_IDLE:// 播放出错");
				rl_live_fostercare_default.setVisibility(View.VISIBLE);
				ll_live_fostercare_jxgk.setVisibility(View.GONE);
				rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
				rl_live_fostercare_screen.setVisibility(View.GONE);
				tv_live_fostercare_default.setVisibility(View.VISIBLE);
				break;
			case VISIBLEORGONE:
				rl_live_fostercare_screen.setVisibility(View.GONE);
				break;
			case TITLEVISIBLEORGONE:
				rl_live_fostercare_fullscreentitle.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
	};

	private int networkType;

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (bvv_live_fostercare.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercare.getCurrentPosition();
			bvv_live_fostercare.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		getOrderDetails();
	}

	private void isNetWorkPlay() {
		networkType = Utils.getNetworkType(this);
		Log.v(TAG, "networkType = " + networkType);
		switch (networkType) {
		case 0:// 无网络

			break;
		case 1:// WIFI网络
			play();
			break;
		case 2:// WAP网络
			if (isSuspend) {
				play();
			}
			break;
		case 3:// NET网络

			break;
		default:
			break;
		}
	}

	private void play() {
		/**
		 * 发起一次新的播放任务
		 */
		if (!bvv_live_fostercare.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			bvv_live_fostercare.resume();
		} else {
			mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
		// 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		if (bvv_live_fostercare.isPlaying()
				&& (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercare.getCurrentPosition();
			bvv_live_fostercare.pause();
		}
	}

	@Override
	protected void onDestroy() {
		isSuspend = false;
		super.onDestroy();
		if ((mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
			mLastPos = (int) bvv_live_fostercare.getCurrentPosition();
			bvv_live_fostercare.stopPlayback();
		}
		if (connectionReceiver != null) {
			unregisterReceiver(connectionReceiver);
		}
		/**
		 * 结束后台事件处理线程
		 */
		mHandlerThread.quit();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public void OnCompletionWithParam(int param) {
		Log.v(TAG, "OnCompletionWithParam=" + param);
	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
	}

	@Override
	public boolean onInfo(int what, int extra) {
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			Log.v(TAG, "开始缓冲");
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			Log.v(TAG, "结束缓冲");
			mHandler.sendEmptyMessage(ONPREPARED);
			break;
		default:
			break;
		}
		return false;
	}

	private void showDialog() {
		if (!pDialog.isShowing()) {
			pDialog.showDialog();
		}
	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mHandler.sendEmptyMessage(PLAYER_IDLE);
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mHandler.sendEmptyMessage(PLAYER_COMPLETE);
	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		closeDialog();
		mHandler.sendEmptyMessage(ONPREPARED);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			closeDialog();
			back();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void closeDialog() {
		if (pDialog.isShowing()) {
			pDialog.closeDialog();
		}
	}
}
