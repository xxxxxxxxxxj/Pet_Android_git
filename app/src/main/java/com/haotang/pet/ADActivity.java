package com.haotang.pet;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AdvancedWebView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

@SuppressLint({ "NewApi", "JavascriptInterface", "ClickableViewAccessibility" })
public class ADActivity extends SuperActivity implements OnClickListener,
		AdvancedWebView.Listener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private String url = "";
	private AdvancedWebView mWebView;
	private SharedPreferenceUtil spUtil;
	private String shareimg = "drawable://" + R.drawable.logo;
	private String sharetitle = "宠物家";
	private String sharetxt = "宠物家";
	private String shareurl = Global.OfficialWebsite;
	private String title;
	private IWXAPI api;
	private LayoutInflater mInflater;
	private PopupWindow pWin;
	private int previous;
	private RelativeLayout rl_titlebar;
	private MProgressDialog mDialog;
	private ImageButton ib_titlebar_other;
	private Bitmap bmp;
	private int flag;
	// private SystemBarTint mtintManager;
	private LinearLayout ll_adactivity;
	private int areaid;
	public static SuperActivity act;
	private UmengShareUtils umengShareUtils;
	private TextView tv_titlebar_other;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bmp = (Bitmap) msg.obj;
			if (flag == 1) {
				showShare();
			}
		}
	};

	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// TODO
			// 在这里进行 http request.网络请求相关操作
			Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(shareimg);
			Message msg = new Message();
			msg.obj = returnBitmap;
			handler.sendMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adbanner);
		act = this;
		MApplication.listAppoint.add(mContext);
		// mtintManager = new SystemBarTint(this);
		// setStatusBarColor(Color.parseColor("#ff0099cc"));
		// 在这里进行 http request.网络请求相关操作
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		Log.e("TAG", "shareimg = "+shareimg);
		Log.e("TAG", "bmp = "+bmp);
		init();
	}

	/*
	 * protected void setStatusBarColor(int colorBurn) { if
	 * (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	 * Window window = setImmerseLayout(); window.setStatusBarColor(colorBurn);
	 * } else if (android.os.Build.VERSION.SDK_INT >=
	 * Build.VERSION_CODES.KITKAT) { if (mtintManager != null) {
	 * mtintManager.setStatusBarTintEnabled(true);
	 * mtintManager.setStatusBarTintColor(colorBurn); } } }
	 */

	private void init() {
		flag = 0;
		api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
		mDialog = new MProgressDialog(this);
		mDialog.showDialog();
		mInflater = LayoutInflater.from(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		areaid = spUtil.getInt("tareaid", 0);
		previous = getIntent().getIntExtra("previous", 0);
		title = getIntent().getStringExtra("title");
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tvTitle.setText("加载中...");
		mWebView = (AdvancedWebView) findViewById(R.id.wv_adbanner);
		rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
		ib_titlebar_other = (ImageButton) findViewById(R.id.ib_titlebar_other);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		LayoutParams layoutParams = ib_titlebar_other.getLayoutParams();
		layoutParams.width = Math.round(36 * 2 / 3 * density);
		layoutParams.height = Math.round(35 * 2 / 3 * density);
		ib_titlebar_other.setLayoutParams(layoutParams);
		ib_titlebar_other.setBackgroundResource(R.drawable.ad_share);
		ll_adactivity = (LinearLayout) findViewById(R.id.ll_adactivity);
		tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
		ib_titlebar_other.setOnClickListener(this);
		tv_titlebar_other.setOnClickListener(this);
		String extraUrl = getIntent().getStringExtra("url");
		if (extraUrl != null && !TextUtils.isEmpty(extraUrl)) {
			if (!extraUrl.startsWith("http://")
					&& !extraUrl.startsWith("https://")) {
				extraUrl = CommUtil.getServiceBaseUrl() + extraUrl;
			}
			if (extraUrl.contains("?")) {
				url = extraUrl
						+ "&system="
						+ CommUtil.getSource()
						+ "_"
						+ Global.getCurrentVersion(this)
						+ "&imei="
						+ Global.getIMEI(this)
						+ "&cellPhone="
						+ SharedPreferenceUtil.getInstance(this).getString(
								"cellphone", "") + "&phoneModel="
						+ android.os.Build.BRAND + " " + android.os.Build.MODEL
						+ "&phoneSystemVersion=" + "Android "
						+ android.os.Build.VERSION.RELEASE + "&time="
						+ System.currentTimeMillis();
			} else {
				url = extraUrl
						+ "?system="
						+ CommUtil.getSource()
						+ "_"
						+ Global.getCurrentVersion(this)
						+ "&imei="
						+ Global.getIMEI(this)
						+ "&cellPhone="
						+ SharedPreferenceUtil.getInstance(this).getString(
								"cellphone", "") + "&phoneModel="
						+ android.os.Build.BRAND + " " + android.os.Build.MODEL
						+ "&phoneSystemVersion=" + "Android "
						+ android.os.Build.VERSION.RELEASE + "&time="
						+ System.currentTimeMillis();
			}
		}
		shareurl = url;
		Utils.mLogError("url===" + url);
		if (url.contains("backaction=1")
				|| previous == Global.FOSTERCARE_TO_AGREEMENT) {
			rl_titlebar.setVisibility(View.GONE);
			ll_adactivity.setFitsSystemWindows(false);
		} else {
			ll_adactivity.setFitsSystemWindows(true);
			rl_titlebar.setVisibility(View.VISIBLE);
		}
		shareurl = url;
		if (url.contains("invite/index.html")) {
			getH5Url();
		}
		if (previous == Global.MYFRAGMENT_INVITESHARE) {
			tv_titlebar_other.setText("我的");
			tv_titlebar_other.setVisibility(View.VISIBLE);
		} else {
			tv_titlebar_other.setVisibility(View.GONE);
		}
		initWebView();
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				ADActivity.this.onReceivedTitle(view, title);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("backaction=1")
						|| previous == Global.FOSTERCARE_TO_AGREEMENT) {
					rl_titlebar.setVisibility(View.GONE);
					ll_adactivity.setFitsSystemWindows(false);
				} else {
					ll_adactivity.setFitsSystemWindows(true);
					rl_titlebar.setVisibility(View.VISIBLE);
				}
				Log.e("TAG", "shouldOverrideUrlLoading = "+url);
				return shouldOverrideUrlBypet(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
						+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				mDialog.closeDialog();
			}
		});
		mWebView.addHttpHeader("X-Requested-With", "");
		mWebView.loadUrl(url);

		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		if (previous != Global.MYFRAGMENT_INVITESHARE) {
			if (url.contains("cwj_bannershare=1")) {
				tv_titlebar_other.setText("分享");
				tv_titlebar_other.setVisibility(View.VISIBLE);
			} else {
				tv_titlebar_other.setVisibility(View.GONE);
			}
		}
	}

	class JsObject {
		@JavascriptInterface
		public String toString() {
			return "fromh5data";
		}
	}

	class JsObject1 {
		@JavascriptInterface
		public String toString() {
			return "fromh5lucy";
		}
	}

	private void H5Js() {
		mWebView.addJavascriptInterface(new JsObject() {
			@JavascriptInterface
			public void LiveFostercare(String orderid) {// 跳转到直播详情页
				Intent intent = new Intent(ADActivity.this,
						FostercareLiveActivity.class);
				intent.putExtra("orderid", orderid);
				startActivity(intent);
			}

			@JavascriptInterface
			public void shareCardInfo(String des, String imgurl, String url) {
				shareimg = imgurl;
				sharetitle = des;
				shareurl = url;
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				new Thread(networkTask).start();
			}

			@JavascriptInterface
			public void shareCardInfo(String des, String imgurl, String url,
					String title) {
				shareimg = imgurl;
				sharetitle = des;
				shareurl = url;
				sharetxt = title;
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				new Thread(networkTask).start();
			}

			@JavascriptInterface
			public void shareCardInfoDidlog(String des, String imgurl,
					String url, String title) {
				flag = 1;
				shareimg = imgurl;
				sharetitle = des;
				shareurl = url;
				sharetxt = title;
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				new Thread(networkTask).start();
			}

			@JavascriptInterface
			public void login() {
				if ("".equals(spUtil.getString("cellphone", ""))) {
					// 去登录
					JumpToNext(LoginActivity.class);
				}
			}

			// 办证支付
			@JavascriptInterface
			public void certipay(String id) {
				Intent intent = new Intent(ADActivity.this,
						OrderPayActivity.class);
				intent.putExtra("previous",
						Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
				intent.putExtra("CertiOrderId", Integer.parseInt(id));
				startActivityForResult(intent,
						Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
			}

			@JavascriptInterface
			public void getShareinfo(String des, String imgurl) {
				shareimg = imgurl;
				sharetitle = des;
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				new Thread(networkTask).start();
			}

			@JavascriptInterface
			public void setData(int type, String data) {
				if ("".equals(spUtil.getString("cellphone", ""))) {
					// 去登录
					JumpToNext(LoginActivity.class);
				} else {
					// 弹出分享
					if (!shareurl.contains("inviteCode")) {
						if (shareurl.contains("?")) {
							shareurl = shareurl + "&inviteCode="
									+ spUtil.getString("invitecode", "");
						} else {
							shareurl = shareurl + "?inviteCode="
									+ spUtil.getString("invitecode", "");
						}
					}
					Utils.mLogError("shareurl" + shareurl);
					showShare();
				}
			}
		}, "fromh5data");
		mWebView.addJavascriptInterface(new JsObject1() {
			@JavascriptInterface
			public void swimming(String num) {// 游泳预约界面
				if (isLogin()) {
					startAct(ADActivity.this, Global.MAIN_TO_SWIM_DETAIL, 0,
							url, 0, 0, 0, 0, SwimDetailActivity.class, 0);
				} else {
					startAct(ADActivity.this,
							Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET, 0,
							url, 0, 0, 0, 0, ChoosePetActivityNew.class, 0);
				}
			}

			@JavascriptInterface
			public void shower(String num) {// 洗澡预约界面
				if (areaid > 0) {
					if (isLogin() && hasPet() && hasService(1)) {
						if (spUtil.getInt("tareaid", 0) == 100) {
							startAct(
									ADActivity.this,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
									0, url, 0, 0, 1, 1, ShopListActivity.class,
									spUtil.getInt("petid", -1));
						} else {
							startAct(ADActivity.this, Global.PRE_MAINFRAGMENT,
									0, url, 0, 0, 1, 1, ServiceActivity.class,
									0);
						}
					} else {
						startAct(ADActivity.this,
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY, 0,
								url, 0, 0, 1, 1, ChoosePetActivityNew.class, 0);
					}
				} else {
					Intent intent = new Intent(ADActivity.this,
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
					ADActivity.this.startActivity(intent);
				}
			}

			@JavascriptInterface
			public void hairdressing(String num) {// 美容预约界面
				if (areaid > 0) {
					if (isLogin() && hasPet() && hasService(2)) {
						if (spUtil.getInt("tareaid", 0) == 100) {
							startAct(
									ADActivity.this,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
									0, url, 0, 0, 2, 2, ShopListActivity.class,
									spUtil.getInt("petid", -1));
						} else {
							startAct(ADActivity.this, Global.PRE_MAINFRAGMENT,
									0, url, 0, 0, 2, 2, ServiceActivity.class,
									0);
						}
					} else {
						startAct(ADActivity.this,
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY, 0,
								url, 0, 0, 2, 2, ChoosePetActivityNew.class, 0);
					}
				} else {
					Intent intent = new Intent(ADActivity.this,
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
					ADActivity.this.startActivity(intent);
				}
			}

			@JavascriptInterface
			public void fosterCare(String num) {// 寄养预约界面
				if (isLogin() && hasPet()) {
					startAct(
							ADActivity.this,
							Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET,
							0, url, 0, 0, 100, 100,
							FostercareChooseroomActivity.class, 0);
				} else {
					startAct(ADActivity.this,
							Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT,
							0, url, 0, 0, 100, 100, ChoosePetActivityNew.class,
							0);
				}

			}

			@JavascriptInterface
			public void worker(String workId) {// 美容师主页
				int workerId = Integer.parseInt(workId);
				startAct(ADActivity.this, Global.MAIN_TO_BEAUTICIANLIST, 0,
						url, workerId, 0, 0, 0, BeauticianDetailActivity.class,
						0);
			}

			@JavascriptInterface
			public void recharge(String num) {// 会员-充值界面
				startAct(ADActivity.this, 0, 0, url, 0, 0, 0, 0,
						MyLastMoney.class, 0);
			}

			@JavascriptInterface
			public void coupon(String num) {// 会员-优惠券
				if ("".equals(spUtil.getString("cellphone", ""))) {
					startAct(ADActivity.this, Global.PRE_PUSH_TO_LOGIN, 0,
							null, 0, 0, 0, 0, LoginActivity.class, 0);
				} else {
					startAct(ADActivity.this, Global.PRE_PUSH_TO_ORDER, 0,
							null, 0, 0, 0, 0, MyCouponActivity.class, 0);
				}
			}

			@JavascriptInterface
			public void order(String orderId) {// 指定订单界面
				int localOrderId = Integer.parseInt(orderId);
				if ("".equals(spUtil.getString("cellphone", ""))) {
					startAct(ADActivity.this, Global.AD_TO_LOGIN_TO_ORDER,
							localOrderId, null, 0, 0, 0, 0,
							LoginActivity.class, 0);
				} else {
					startAct(ADActivity.this, 0, localOrderId, null, 0, 0, 0,
							0, OrderDetailFromOrderToConfirmActivity.class, 0);
				}
			}

			@JavascriptInterface
			public void workerList(String workListId) {// 美容师指定等级列表主页
				int workerLevel = Integer.parseInt(workListId);
				startAct(ADActivity.this, 0, 0, url, 0, workerLevel, 0, 0,
						MainToBeauList.class, 0);
			}

			@JavascriptInterface
			public void card(String url) {// 办狗证
				startAct(ADActivity.this, 0, 0, url, 0, 0, 0, 0,
						ADActivity.class, 0);
			}

			@JavascriptInterface
			public void shareCard(String des, String imgurl, String url) {
				flag = 1;
				shareimg = imgurl;
				sharetitle = des;
				shareurl = url;
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				new Thread(networkTask).start();
			}

			@JavascriptInterface
			public void goback() {
				back();
			}

			@JavascriptInterface
			public void goLogin() {
				if ("".equals(spUtil.getString("cellphone", ""))
						&& LoginActivity.act == null) {
					// 去登录
					Intent intent = new Intent(ADActivity.this,
							LoginActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("previous", Global.AD_TO_LOGIN);
					startActivityForResult(intent, Global.AD_TO_LOGIN);
				}
			}

			@JavascriptInterface
			public void goCoupon() {
				if (!"".equals(spUtil.getString("cellphone", ""))) {
					// 去优惠券列表
					Intent intent = new Intent(ADActivity.this,
							MyCouponActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("previous", Global.AD_TO_LOGIN);
					startActivity(intent);
					finish();
				}
			}

			@JavascriptInterface
			public void dogTraining(String str) {// 训犬主页
				if (Utils.checkLogin(ADActivity.this)) {
					if (spUtil.getInt("petkind", -1) == 1) {
						startActivity(new Intent(ADActivity.this,
								TrainAppointMentActivity.class).putExtra(
								"previous", Global.MAIN_TO_TRAIN_DETAIL));
					} else {// Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
						startActivity(new Intent(ADActivity.this,
								ChoosePetActivityNew.class).putExtra(
								"previous",
								Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET));
					}
				} else {
					startActivity(new Intent(ADActivity.this,
							ChoosePetActivityNew.class).putExtra("previous",
							Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET));
				}
			}

			@JavascriptInterface
			public void hospital(String str) {// 推荐医院列表
				startActivity(new Intent(ADActivity.this, MainToList.class)
						.putExtra("type", 5));
			}

			@JavascriptInterface
			public void unusualServe(String str) {// 特色服务下单页
				if (isLogin() && hasPet()) {
					if (spUtil.getInt("tareaid", 0) == 100) {
						startActivity(new Intent(ADActivity.this,
								ShopListActivity.class)
								.putExtra(
										"previous",
										Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST)
								.putExtra("servicename", str)
								.putExtra("petid", spUtil.getInt("petid", -1))
								.putExtra("typeId", Integer.parseInt(str)));
					} else {
						startActivity(new Intent(ADActivity.this,
								ServiceFeature.class).putExtra("previous",
								Global.PRE_MAINFRAGMENT).putExtra(
								"servicename", str));
					}
				} else {
					startActivity(new Intent(ADActivity.this,
							ChoosePetActivityNew.class)
							.putExtra("previous",
									Global.SERVICEFEATURE_TO_PETLIST)
							.putExtra("servicename", str)
							.putExtra("typeId", Integer.parseInt(str)));
				}
			}

			@JavascriptInterface
			public void serviceCard(String serviceCardId) {// 次卡详情页
				if (serviceCardId != null && !TextUtils.isEmpty(serviceCardId)) {
					if (isLogin() && hasPet()) {
						startActivity(new Intent(mContext,
								CardsDetailActivity.class).putExtra("id",
								Integer.parseInt(serviceCardId)).putExtra("previous",previous));
						Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000--- "+previous);
					} else {
						startActivity(new Intent(mContext,
								ChoosePetActivityNew.class).putExtra("id",
								Integer.parseInt(serviceCardId)).putExtra(
								"previous", Global.CARD_NOTPET_CHOOSE_PET));
						Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 111--- "+previous);
					}
				}
			}
		}, "fromh5lucy");
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

	private void startAct(Context context, int previous, int orderid,
			String url, int workerId, int workerLevel, int serviceid,
			int servicetype, Class clazz, int petid) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("previous", previous);
		intent.putExtra("orderid", orderid);
		intent.putExtra("id", workerId);
		intent.putExtra("workerLevel", workerLevel);
		if (url != null && !TextUtils.isEmpty(url))
			intent.putExtra("url", url);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("petid", petid);
		intent.putExtra("areaid", areaid);
		context.startActivity(intent);
	}

	protected boolean shouldOverrideUrlBypet(WebView view, String url) {
		mDialog.showDialog();
		if (TextUtils.isEmpty(url)) {
			return true;
		}
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			return false;
		} else if (url.startsWith("sms:")) {
			String regex = "sms:([\\d]*?)\\?body=([\\w\\W]*)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(Uri.decode(url).replaceAll(" ", ""));
			if (m.find()) {
				String tel = m.group(1);
				String body = m.group(2);
				Uri smsto = Uri.parse("smsto:" + tel);
				Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsto);
				sendIntent.putExtra("sms_body", body);
				startActivity(sendIntent);
				return true;
			}
		} else if (url.startsWith("tel:")) {
			Intent telIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
			telIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(telIntent);
			return true;
		} else if (url.startsWith("mailto:")) {
			MailTo mailTo = MailTo.parse(url);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mailTo.getTo() });
			intent.putExtra(Intent.EXTRA_CC, mailTo.getCc());
			intent.putExtra(Intent.EXTRA_TEXT, mailTo.getBody());
			intent.putExtra(Intent.EXTRA_SUBJECT, mailTo.getSubject());
			intent.setPackage("com.android.email");
			intent.setType("text/plain");
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			startActivity(intent);
			return true;
		} else if (url.startsWith("intent:")) {
			try {
				Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	// 初始化WebView配置
	protected void initWebView() {
		// mWebView.setDesktopMode(true);
		mWebView.setListener(this, this);
		mWebView.setGeolocationEnabled(false);
		mWebView.setMixedContentAllowed(true);
		mWebView.setCookiesEnabled(true);
		mWebView.setThirdPartyCookiesEnabled(true);
	}

	protected void onReceivedTitle(WebView view, String title) {
		if (!url.contains("invite/index.html")) {
			sharetxt = title;
		}
		tvTitle.setText(title);
		sharetitle = title;
		sharetxt = title;
	}

	// 清除缓存
	private int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// 切换为竖屏
		if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
			if (url != null && url.contains("video.html")) {
				rl_titlebar.setVisibility(View.VISIBLE);
			}
		}
		// 切换为横屏
		else if (newConfig.orientation == this.getResources()
				.getConfiguration().ORIENTATION_LANDSCAPE) {
			if (url != null && url.contains("video.html")) {
				rl_titlebar.setVisibility(View.GONE);
			}
		}
	}

	private void getH5Url() {
		CommUtil.getH5Url(this, h5Handler);
	}

	private void JumpToNext(Class clazz) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.H5_TO_LOGIN);
		intent.putExtra("loginurl", url);
		startActivityForResult(intent, Global.H5_TO_LOGIN);
	}

	private AsyncHttpResponseHandler h5Handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("获取h5url：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("invitationConfig")
							&& !jdata.isNull("invitationConfig")) {
						JSONObject ji = jdata.getJSONObject("invitationConfig");
						if (ji.has("join_url") && !ji.isNull("join_url")) {
							shareurl = ji.getString("join_url");
						}
						if (ji.has("txt") && !ji.isNull("txt")) {
							sharetitle = ji.getString("txt");
						}
						if (ji.has("img") && !ji.isNull("img")) {
							shareimg = ji.getString("img");
							// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
							new Thread(networkTask).start();
						}
						if (ji.has("title") && !ji.isNull("title")) {
							sharetxt = ji.getString("title");
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

	private void goBack() {
		setResult(Global.RESULT_OK);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.AD_TO_LOGIN) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String curl = "javascript:init_roll('"
								+ spUtil.getString("cellphone", "") + "')";
						mWebView.loadUrl(curl);
					}
				});
			} else if (requestCode == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
				mDialog.showDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String curl = data.getStringExtra("payurl");
						String locUrl = null;
						if (curl != null) {
							if (curl.contains("?")) {
								locUrl = curl
										+ "&system="
										+ CommUtil.getSource()
										+ "_"
										+ Global.getCurrentVersion(ADActivity.this)
										+ "&imei="
										+ Global.getIMEI(ADActivity.this)
										+ "&cellPhone="
										+ SharedPreferenceUtil.getInstance(
												ADActivity.this).getString(
												"cellphone", "")
										+ "&phoneModel="
										+ android.os.Build.BRAND + " "
										+ android.os.Build.MODEL
										+ "&phoneSystemVersion=" + "Android "
										+ android.os.Build.VERSION.RELEASE
										+ "&time=" + System.currentTimeMillis();
							} else {
								locUrl = curl
										+ "?system="
										+ CommUtil.getSource()
										+ "_"
										+ Global.getCurrentVersion(ADActivity.this)
										+ "&imei="
										+ Global.getIMEI(ADActivity.this)
										+ "&cellPhone="
										+ SharedPreferenceUtil.getInstance(
												ADActivity.this).getString(
												"cellphone", "")
										+ "&phoneModel="
										+ android.os.Build.BRAND + " "
										+ android.os.Build.MODEL
										+ "&phoneSystemVersion=" + "Android "
										+ android.os.Build.VERSION.RELEASE
										+ "&time=" + System.currentTimeMillis();
							}
						}
						mWebView.loadUrl(locUrl);
					}
				});
			} else if (requestCode == Global.H5_TO_LOGIN) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String curl = "javascript:init_roll('"
								+ spUtil.getString("cellphone", "") + "')";
						mWebView.loadUrl(curl);
					}
				});
			}
		}
		mWebView.onActivityResult(requestCode, resultCode, data);
	}

	private void setWXShareContent(int type) {
		boolean weixinAvilible = Utils.isWeixinAvilible(this);
		if (type == 1 || type == 2) {// 微信
			if (weixinAvilible) {
				WXWebpageObject wxwebpage = new WXWebpageObject();
				wxwebpage.webpageUrl = shareurl;
				WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
				wxmedia.title = sharetxt;
				wxmedia.description = sharetitle;
				Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bmp);
				wxmedia.setThumbImage(createBitmapThumbnail);
				wxmedia.thumbData = Util_WX.bmpToByteArray(
						createBitmapThumbnail, true);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.message = wxmedia;
				if (type == 1) {
					req.scene = SendMessageToWX.Req.WXSceneSession;
				} else {
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
				}
				api.sendReq(req);
			} else {
				ToastUtil.showToastShortBottom(this, "微信不可用");
			}
		} else if (type == 3) {// qq
			umengShareUtils = new UmengShareUtils(this, sharetitle, shareurl,
					sharetitle, shareimg);
			umengShareUtils.mController.getConfig().closeToast();
			umengShareUtils.mController.postShare(this, SHARE_MEDIA.QQ,
					new SnsPostListener() {
						@Override
						public void onStart() {
							/*
							 * ToastUtil.showToastShortCenter(ADActivity.this ,
							 * "开始分享.");
							 */
						}

						@Override
						public void onComplete(SHARE_MEDIA arg0, int eCode,
								SocializeEntity arg2) {
							if (eCode == 200) {
								/*
								 * ToastUtil.showToastShortCenter(
								 * ADActivity.this, "分享成功.");
								 */
							} else {
								String eMsg = "";
								if (eCode == -101) {
									eMsg = "没有授权";
								}
								/*
								 * ToastUtil.showToastShortCenter(
								 * ADActivity.this, "分享失败[" + eCode + "] " +
								 * eMsg);
								 */
							}
						}
					});
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private void showShare() {
		pWin = null;
		if (pWin == null) {
			final View view = mInflater.inflate(R.layout.sharedialog, null);
			RelativeLayout rlParent = (RelativeLayout) view
					.findViewById(R.id.rl_sharedialog_parent);
			LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_wxfriend);
			LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_wxpyq);
			LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_qqfriend);
			Button btn_sharedialog_cancel = (Button) view
					.findViewById(R.id.btn_sharedialog_cancel);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(Utils.getDisplayMetrics(this)[0]);
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			rlParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
			ll_sharedialog_wxfriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 微信好友
					pWin.dismiss();
					pWin = null;
					setWXShareContent(1);
				}
			});
			ll_sharedialog_wxpyq.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 微信朋友圈
					pWin.dismiss();
					pWin = null;
					setWXShareContent(2);
				}
			});
			ll_sharedialog_qqfriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// QQ好友
					pWin.dismiss();
					pWin = null;
					setWXShareContent(3);
				}
			});
			btn_sharedialog_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 取消
					pWin.dismiss();
					pWin = null;
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_other:
			showShare();
			break;
		case R.id.tv_titlebar_other:
			if (url.contains("cwj_bannershare=1")) {
				showShare();
			} else {
				Intent intent = new Intent(this, BalanceRecord.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				Bundle bundle = new Bundle();
				bundle.putInt("index", 1);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * @Override public void onBackPressed() { if (!mWebView.onBackPressed()) {
	 * back(); } else { // Standard back button implementation (for example this
	 * could close the app) super.onBackPressed(); } }
	 */

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		// JS交互
		H5Js();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		mWebView.onPause();
		// ...
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mWebView.onDestroy();
		// ...
		super.onDestroy();
	}

	@Override
	public void onPageStarted(String url, Bitmap favicon) {
		mWebView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPageFinished(String url) {
		mWebView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageError(int errorCode, String description, String failingUrl) {
		Toast.makeText(
				this,
				"onPageError(errorCode = " + errorCode + ",  description = "
						+ description + ",  failingUrl = " + failingUrl + ")",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDownloadRequested(String url, String userAgent,
			String contentDisposition, String mimetype, long contentLength) {
		Toast.makeText(
				this,
				"onDownloadRequested(url = " + url + ",  userAgent = "
						+ userAgent + ",  contentDisposition = "
						+ contentDisposition + ",  mimetype = " + mimetype
						+ ",  contentLength = " + contentLength + ")",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onExternalPageRequest(String url) {
		Toast.makeText(this, "onExternalPageRequest(url = " + url + ")",
				Toast.LENGTH_SHORT).show();
	}

	private void back() {
		goBack();
		mWebView.loadUrl("about:blank");
		mWebView.goBack();
	}
}
