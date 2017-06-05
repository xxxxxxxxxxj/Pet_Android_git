package com.haotang.pet.receive;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.haotang.pet.ADActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.ChoosePetActivityNew;
import com.haotang.pet.ComplaintOrderHistoryActivity;
import com.haotang.pet.CustomerComplaintsHistoryActivity;
import com.haotang.pet.EvaluateActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MainToBeauList;
import com.haotang.pet.MyCardsActivity;
import com.haotang.pet.MyCouponActivity;
import com.haotang.pet.MyLastMoney;
import com.haotang.pet.OrderDetailFromOrderActivity;
import com.haotang.pet.OrderDetailFromOrderToConfirmActivity;
import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.SelectServiceAreaActivity;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.ServiceFeature;
import com.haotang.pet.ShopListActivity;
import com.haotang.pet.SwimDetailActivity;
import com.haotang.pet.UserListActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:JpushDataReceiver
 * </p>
 * <p>
 * Description:推送消息接收器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-16 上午10:08:44
 */
@SuppressLint("NewApi")
public class JpushDataReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private SharedPreferenceUtil spUtil;
	private Builder mBuilder;
	/** Notification管理 */
	public NotificationManager mNotificationManager;
	/** Notification 的ID */
	int notifyId = 101;
	private String imgUrl = "";
	protected Bitmap bmp;
	private Context context;
	private String title = "";
	private String content = "";
	private String url = "";
	private Class clazz;
	private int areaid;
	private int previous;
	private int orderid;
	private int workerId;
	private int workerLevel;
	private int serviceid;
	private int servicetype;
	private int Identification;
	private String name = "";
	private int postId;
	private int userId;
	private String flag = "";
	private int petid;
	private int typeId;

	@Override
	public void onReceive(Context context, Intent intent) {
		spUtil = SharedPreferenceUtil.getInstance(context
				.getApplicationContext());
		areaid = spUtil.getInt("tareaid", 0);
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive ------------ " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
			if (!regId.isEmpty()) {
				Utils.mLogError("regId = " + regId);
				Global.savePushID(context, regId);
			}
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			// 打开自定义的Activity
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.d(TAG, "extras = " + extras);
			donePush(context, extras);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	private void donePush(Context context, String json) {
		try {
			JSONObject jobj = new JSONObject(json);
			if (jobj.has("type") && !jobj.isNull("type")) {
				Identification = 0;
				String type = jobj.getString("type");
				if ("100".equals(type)) {
					if (jobj.has("ad_url") && !jobj.isNull("ad_url")) {
						// 跳转到h5界面
						String url = jobj.getString("ad_url");
						startAct(context, 0, 0, url, 0, 0, 0, 0, "",
								ADActivity.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2000".equals(type)) {// 进入会员充值界面
					startAct(context, 0, 0, url, 0, 0, 0, 0, "",
							MyLastMoney.class);
				} else if ("2001".equals(type)) {// 进入指定美容师界面
					if (jobj.has("workerId") && !jobj.isNull("workerId")) {
						int workerId = Integer.parseInt(jobj
								.getString("workerId"));
						startAct(context, Global.MAIN_TO_BEAUTICIANLIST, 0,
								url, workerId, 0, 0, 0, "",
								BeauticianDetailActivity.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2002".equals(type)) {// 进入指定等级美容师界面
					if (jobj.has("workerLevel") && !jobj.isNull("workerLevel")) {
						int workerLevel = Integer.parseInt(jobj
								.getString("workerLevel"));
						startAct(context, 0, 0, url, 0, workerLevel, 0, 0, "",
								MainToBeauList.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2003".equals(type)) {// 进入游泳预约界面
					if (isLogin()) {
						if (spUtil.getInt("petkind", -1) == 1) {
							startAct(context, Global.MAIN_TO_SWIM_DETAIL, 0,
									url, 0, 0, 0, 0, "",
									SwimDetailActivity.class);
						} else {
							startAct(
									context,
									Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET,
									0, url, 0, 0, 0, 0, "",
									ChoosePetActivityNew.class);
						}
					} else {
						startAct(context,
								Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET,
								0, url, 0, 0, 0, 0, "",
								ChoosePetActivityNew.class);
					}
				} else if ("2004".equals(type)) {// 进入寄养预约界面
					// 寄养
					startAct(context,
							Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT,
							0, url, 0, 0, 100, 100, "",
							ChoosePetActivityNew.class);
				} else if ("2005".equals(type)) {// 进入洗澡预约界面
					if (areaid > 0) {
						if (isLogin() && hasPet() && hasService(1)) {
							if (spUtil.getInt("tareaid", 0) == 100) {
								petid = spUtil.getInt("petid", -1);
								startAct(
										context,
										Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
										0, url, 0, 0, 1, 1, "",
										ShopListActivity.class);
							} else {
								startAct(context, Global.PRE_MAINFRAGMENT, 0,
										url, 0, 0, 1, 1, "",
										ServiceActivity.class);
							}
						} else {
							startAct(context,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,
									0, url, 0, 0, 1, 1, "",
									ChoosePetActivityNew.class);
						}
					} else {
						Identification = 2005;
						startAct(context, 0, 0, url, 0, 0, 1, 1, "",
								SelectServiceAreaActivity.class);
					}
				} else if ("2006".equals(type)) {// 进入美容预约界面
					if (areaid > 0) {
						if (isLogin() && hasPet() && hasService(2)) {
							if (spUtil.getInt("tareaid", 0) == 100) {
								petid = spUtil.getInt("petid", -1);
								startAct(
										context,
										Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
										0, url, 0, 0, 2, 2, "",
										ShopListActivity.class);
							} else {
								startAct(context, Global.PRE_MAINFRAGMENT, 0,
										url, 0, 0, 2, 2, "",
										ServiceActivity.class);
							}
						} else {
							startAct(context,
									Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,
									0, url, 0, 0, 2, 2, "",
									ChoosePetActivityNew.class);
						}
					} else {
						Identification = 2006;
						startAct(context, 0, 0, url, 0, 0, 2, 2, "",
								SelectServiceAreaActivity.class);
					}
				} else if ("2007".equals(type)) {// 进入办证界面
					if (jobj.has("url") && !jobj.isNull("url")) {
						// 跳转到h5界面
						String url = jobj.getString("url");
						startAct(context, 0, 0, url, 0, 0, 0, 0, "",
								ADActivity.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2008".equals(type) || "2009".equals(type)
						|| "2010".equals(type) || "2011".equals(type)
						|| "2016".equals(type) || "2017".equals(type)
						|| "2018".equals(type) || "2019".equals(type)
						|| "2020".equals(type) || "2021".equals(type)) {// 进入特色服务界面
					if (jobj.has("name") && !jobj.isNull("name")) {
						String name = jobj.getString("name");
						if (areaid > 0) {
							if (isLogin() && hasPet()) {
								if (spUtil.getInt("tareaid", 0) == 100) {
									petid = spUtil.getInt("petid", -1);
									typeId = Integer.parseInt(name);
									startAct(
											context,
											Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST,
											0, url, 0, 0, 0, 0, name,
											ShopListActivity.class);
								} else {
									startAct(context, Global.PRE_MAINFRAGMENT,
											0, url, 0, 0, 0, 0, name,
											ServiceFeature.class);
								}
							} else {
								typeId = Integer.parseInt(name);
								startAct(context,
										Global.SERVICEFEATURE_TO_PETLIST, 0,
										url, 0, 0, 0, 0, name,
										ChoosePetActivityNew.class);
							}
						} else {
							startAct(context, Global.MAIN_TO_AREALIST, 0, url,
									0, 0, 0, 0, "",
									SelectServiceAreaActivity.class);
						}
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2012".equals(type)) {// 进入寄养直播列表页,运营发送
					if (jobj.has("url") && !jobj.isNull("url")) {
						// 跳转到h5界面
						String url = jobj.getString("url");
						startAct(context, 0, 0, url, 0, 0, 0, 0, "",
								ADActivity.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2013".equals(type)) {// 进入寄养直播列表页
					if (jobj.has("tag") && !jobj.isNull("tag")) {
						spUtil.saveBoolean("isExit", true);
					} else {
						spUtil.saveBoolean("isExit", false);
					}
					if (jobj.has("url") && !jobj.isNull("url")) {
						// 跳转到h5界面
						String url = jobj.getString("url");
						startAct(context, 0, 0, url, 0, 0, 0, 0, "",
								ADActivity.class);
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2014".equals(type)) {// 进入帖子详情页
					if (jobj.has("postId") && !jobj.isNull("postId")) {
						postId = jobj.getInt("postId");
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								PetCircleInsideDetailActivity.class);
					} else {
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2015".equals(type)) {// 进入圈子个人主页
					if (jobj.has("userId") && !jobj.isNull("userId")) {
						userId = jobj.getInt("userId");
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								PostUserInfoActivity.class);
					} else {
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2022".equals(type)) {// 进入精选帖子详情界面
					if (jobj.has("postId") && !jobj.isNull("postId")) {
						postId = jobj.getInt("postId");
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								PostSelectionDetailActivity.class);
					} else {
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else if ("2023".equals(type)) {// 进入粉丝列表界面
					if (spUtil.getString("cellphone", "") != null
							&& !TextUtils.isEmpty(spUtil.getString("cellphone",
									"")) && spUtil.getInt("userid", -1) > 0) {
						userId = spUtil.getInt("userid", -1);
						flag = "fans";
						startAct(context, 0, 0, null, 0, 0, 0, 0, "",
								UserListActivity.class);
					} else {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", LoginActivity.class);
					}
				} else if ("2025".equals(type)) {// 进入投诉订单详情界面
					if (Utils.checkLogin(context)) {
						int orderId = jobj.getInt("orderId");
						startAct(context, Global.PRE_PUSH_TO_LOGIN, orderId,
								null, 0, 0, 0, 0, "",
								ComplaintOrderHistoryActivity.class);
					} else {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", LoginActivity.class);
					}
				} else if ("2026".equals(type)) {// 进入投诉客服列表界面
					if (Utils.checkLogin(context)) {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "",
								CustomerComplaintsHistoryActivity.class);
					} else {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", LoginActivity.class);
					}
				} else if ("2027".equals(type)) {// 我的卡包
					if (Utils.checkLogin(context)) {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", MyCardsActivity.class);
					} else {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", LoginActivity.class);
					}
				} else if ("2".equals(type)) {
					if (spUtil.getString("cellphone", "") != null
							&& !TextUtils.isEmpty(spUtil.getString("cellphone",
									""))) {
						startAct(context, Global.PRE_PUSH_TO_ORDER, 0, null, 0,
								0, 0, 0, "", MyCouponActivity.class);
					} else {
						startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
								0, 0, 0, "", LoginActivity.class);
					}
				} else if (jobj.has("orderId") && !jobj.isNull("orderId")) {
					int orderId = Integer.parseInt(jobj.getString("orderId"));
					if ("1".equals(type) || "1705".equals(type)
							|| "2024".equals(type)) {// 试试2024跳详情
						startAct(context,
								Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN,
								orderId, null, 0, 0, 0, 0, "",
								OrderDetailFromOrderActivity.class);
					} else if ("1000".equals(type)) {
						startAct(context,
								Global.PUSH_TO_ORDER_ORDER_BEAU_ACCEPT,
								orderId, null, 0, 0, 0, 0, "",
								OrderDetailFromOrderToConfirmActivity.class);
					} else if ("1001".equals(type)) {
						startAct(context,
								Global.PUSH_TO_ORDER_ORDER_NO_BEAU_ACCEPT,
								orderId, null, 0, 0, 0, 0, "",
								OrderDetailFromOrderToConfirmActivity.class);
					} else if ("512".equals(type)) {
						// 到订单详情
						if (spUtil.getString("cellphone", "") != null
								&& !TextUtils.isEmpty(spUtil.getString(
										"cellphone", ""))) {
							startAct(context, 0, orderId, null, 0, 0, 0, 0, "",
									OrderDetailFromOrderToConfirmActivity.class);
						} else {
							startAct(context, 0, orderId, null, 0, 0, 0, 0, "",
									MainActivity.class);
						}
					}
					// else if ("2024".equals(type)) {
					// // 到训练订单详情
					// if (spUtil.getString("cellphone", "") != null
					// && !TextUtils.isEmpty(spUtil.getString(
					// "cellphone", ""))) {
					// startAct(context, 0, orderId, null, 0, 0, 0, 0, "",
					// OrderFosterDetailActivity.class);
					// } else {
					// startAct(context, 0, orderId, null, 0, 0, 0, 0, "",
					// MainActivity.class);
					// }
					// }
					else if ("600".equals(type) || "65".equals(type)) {
						// 通知用户去评论 65为寄养退房或者结束
						if (spUtil.getString("cellphone", "") != null
								&& !TextUtils.isEmpty(spUtil.getString(
										"cellphone", ""))) {
							startAct(context, Global.PRE_PUSH_TO_EVALUATE,
									orderId, null, 0, 0, 0, 0, "",
									EvaluateActivity.class);
						} else {
							startAct(context, Global.PRE_PUSH_TO_EVALUATE,
									orderId, null, 0, 0, 0, 0, "",
									LoginActivity.class);
						}
					} else {
						startAct(context, 100, 0, null, 0, 0, 0, 0, "",
								MainActivity.class);
					}
				} else {
					startAct(context, 100, 0, null, 0, 0, 0, 0, "",
							MainActivity.class);
				}
			} else {
				startAct(context, 100, 0, null, 0, 0, 0, 0, "",
						MainActivity.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& spUtil.getString("cellphone", "") != null
				&& !TextUtils.isEmpty(spUtil.getString("cellphone", "")))
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

	private boolean isAppOpen(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals("com.haotang.pet")
					|| info.baseActivity.getPackageName().equals(
							"com.haotang.pet")) {
				return true;
			}
		}
		return false;
	}

	private void startAct(Context context, int previous, int orderid,
			String url, int workerId, int workerLevel, int serviceid,
			int servicetype, String servicename, Class clazz) {
		if (!isAppOpen(context)) {
			context.startActivity(new Intent(context, MainActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		Intent intent = new Intent(context, clazz);
		intent.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
		intent.putExtra("petid", petid);
		intent.putExtra("servicename", servicename);
		intent.putExtra("orderid", orderid);
		intent.putExtra("userId", userId);
		intent.putExtra("id", workerId);
		intent.putExtra("workerLevel", workerLevel);
		if (url != null && !TextUtils.isEmpty(url))
			intent.putExtra("url", url);
		intent.putExtra("flag", flag);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("postId", postId);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("areaid", areaid);
		if (previous == Global.PUSH_TO_ORDER_ORDER_BEAU_ACCEPT) {
			intent.putExtra("jpushBeauAccpetCode", 1);
		}
		if (previous == Global.PUSH_TO_ORDER_ORDER_NO_BEAU_ACCEPT) {
			intent.putExtra("jpushWaitCode", 1);
		}
		if (Identification == 2005) {
			if (isLogin() && hasPet() && hasService(1)) {
				intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
			} else {
				intent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			}
		} else if (Identification == 2006) {
			if (isLogin() && hasPet() && hasService(2)) {
				intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
			} else {
				intent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			}
		} else {
			intent.putExtra("previous", previous);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}
				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();
					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (extras != null && !TextUtils.isEmpty(extras)) {
			try {
				JSONObject extraJson = new JSONObject(extras);
				if (null != extraJson && extraJson.length() > 0) {
					String title = "";
					String content = "";
					String url = "";
					String imgUrl = "";
					int workerId = 0;
					int workerLevel = 0;
					String name = "";
					if (extraJson.has("title") && !extraJson.isNull("title")) {
						title = extraJson.getString("title");
					}
					if (extraJson.has("description")
							&& !extraJson.isNull("description")) {
						content = extraJson.getString("description");
					}
					if (extraJson.has("imgUrl") && !extraJson.isNull("imgUrl")) {
						imgUrl = extraJson.getString("imgUrl");
					}
					if (extraJson.has("type") && !extraJson.isNull("type")) {
						String type = extraJson.getString("type");
						if (type != null && !TextUtils.isEmpty(type)
								&& title != null && !TextUtils.isEmpty(title)
								&& content != null
								&& !TextUtils.isEmpty(content)
								&& imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
							Identification = 0;
							if ("100".equals(type)) {
								if (extraJson.has("ad_url")
										&& !extraJson.isNull("ad_url")) {
									// 跳转到h5界面
									url = extraJson.getString("ad_url");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, ADActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2000".equals(type)) {// 进入会员充值界面
								sendNotification(context, title, content, url,
										imgUrl, 0, 0, 0, 0, 0, 0, name,
										MyLastMoney.class);
							} else if ("2001".equals(type)) {// 进入指定美容师界面
								if (extraJson.has("workerId")
										&& !extraJson.isNull("workerId")) {
									workerId = Integer.parseInt(extraJson
											.getString("workerId"));
									sendNotification(context, title, content,
											url, imgUrl,
											Global.MAIN_TO_BEAUTICIANLIST, 0,
											workerId, 0, 0, 0, name,
											BeauticianDetailActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2002".equals(type)) {// 进入指定等级美容师界面
								if (extraJson.has("workerLevel")
										&& !extraJson.isNull("workerLevel")) {
									workerLevel = Integer.parseInt(extraJson
											.getString("workerLevel"));
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, workerLevel,
											0, 0, name, MainToBeauList.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2003".equals(type)) {// 进入游泳预约界面
								if (isLogin()) {
									if (spUtil.getInt("petkind", -1) == 1) {
										sendNotification(context, title,
												content, url, imgUrl,
												Global.MAIN_TO_SWIM_DETAIL, 0,
												0, 0, 0, 0, name,
												SwimDetailActivity.class);
									} else {
										sendNotification(
												context,
												title,
												content,
												url,
												imgUrl,
												Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET,
												0, 0, 0, 0, 0, name,
												ChoosePetActivityNew.class);
									}
								} else {
									sendNotification(
											context,
											title,
											content,
											url,
											imgUrl,
											Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET,
											0, 0, 0, 0, 0, name,
											ChoosePetActivityNew.class);
								}
							} else if ("2004".equals(type)) {// 进入寄养预约界面
								// 寄养
								sendNotification(
										context,
										title,
										content,
										url,
										imgUrl,
										Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT,
										0, 0, 0, 100, 100, name,
										ChoosePetActivityNew.class);
							} else if ("2005".equals(type)) {// 进入洗澡预约界面
								if (areaid > 0) {
									if (isLogin() && hasPet() && hasService(1)) {
										if (spUtil.getInt("tareaid", 0) == 100) {
											petid = spUtil.getInt("petid", -1);
											sendNotification(
													context,
													title,
													content,
													url,
													imgUrl,
													Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
													0, 0, 0, 1, 1, name,
													ShopListActivity.class);
										} else {
											sendNotification(context, title,
													content, url, imgUrl,
													Global.PRE_MAINFRAGMENT, 0,
													0, 0, 1, 1, name,
													ServiceActivity.class);
										}
									} else {
										sendNotification(
												context,
												title,
												content,
												url,
												imgUrl,
												Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,
												0, 0, 0, 1, 1, name,
												ChoosePetActivityNew.class);
									}
								} else {
									Identification = 2005;
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 1, 1,
											name,
											SelectServiceAreaActivity.class);
								}
							} else if ("2006".equals(type)) {// 进入美容预约界面
								if (areaid > 0) {
									if (isLogin() && hasPet() && hasService(2)) {
										if (spUtil.getInt("tareaid", 0) == 100) {
											petid = spUtil.getInt("petid", -1);
											sendNotification(
													context,
													title,
													content,
													url,
													imgUrl,
													Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST,
													0, 0, 0, 2, 2, name,
													ShopListActivity.class);
										} else {
											sendNotification(context, title,
													content, url, imgUrl,
													Global.PRE_MAINFRAGMENT, 0,
													0, 0, 2, 2, name,
													ServiceActivity.class);
										}
									} else {
										sendNotification(
												context,
												title,
												content,
												url,
												imgUrl,
												Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,
												0, 0, 0, 2, 2, name,
												ChoosePetActivityNew.class);
									}
								} else {
									Identification = 2006;
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 2, 2,
											name,
											SelectServiceAreaActivity.class);
								}
							} else if ("2007".equals(type)) {// 进入办证界面
								if (extraJson.has("url")
										&& !extraJson.isNull("url")) {
									// 跳转到h5界面
									url = extraJson.getString("url");
									sendNotification(
											context,
											title,
											content,
											url,
											imgUrl,
											Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,
											0, 0, 0, 0, 0, name,
											ADActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2008".equals(type)
									|| "2009".equals(type)
									|| "2010".equals(type)
									|| "2011".equals(type)
									|| "2016".equals(type)
									|| "2017".equals(type)
									|| "2018".equals(type)
									|| "2019".equals(type)
									|| "2020".equals(type)
									|| "2021".equals(type)) {// 进入特色服务界面
								if (extraJson.has("name")
										&& !extraJson.isNull("name")) {
									name = extraJson.getString("name");
									if (areaid > 0) {
										if (isLogin() && hasPet()) {
											if (spUtil.getInt("tareaid", 0) == 100) {
												typeId = Integer.parseInt(name);
												petid = spUtil.getInt("petid",
														-1);
												sendNotification(
														context,
														title,
														content,
														url,
														imgUrl,
														Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST,
														0, 0, 0, 0, 0, name,
														ShopListActivity.class);
											} else {
												sendNotification(
														context,
														title,
														content,
														url,
														imgUrl,
														Global.PRE_MAINFRAGMENT,
														0, 0, 0, 0, 0, name,
														ServiceFeature.class);
											}
										} else {
											typeId = Integer.parseInt(name);
											sendNotification(
													context,
													title,
													content,
													url,
													imgUrl,
													Global.SERVICEFEATURE_TO_PETLIST,
													0, 0, 0, 0, 0, name,
													ChoosePetActivityNew.class);
										}
									} else {
										sendNotification(context, title,
												content, url, imgUrl,
												Global.MAIN_TO_AREALIST, 0, 0,
												0, 0, 0, name,
												SelectServiceAreaActivity.class);
									}
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2012".equals(type)) {// 进入寄养直播列表页,运营发送
								if (extraJson.has("url")
										&& !extraJson.isNull("url")) {
									// 跳转到h5界面
									url = extraJson.getString("url");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, ADActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2013".equals(type)) {// 进入寄养直播列表页
								if (extraJson.has("tag")
										&& !extraJson.isNull("tag")) {
									spUtil.saveBoolean("isExit", true);
								} else {
									spUtil.saveBoolean("isExit", false);
								}
								if (extraJson.has("url")
										&& !extraJson.isNull("url")) {
									// 跳转到h5界面
									url = extraJson.getString("url");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, ADActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2014".equals(type)) {// 进入帖子详情页
								if (extraJson.has("postId")
										&& !extraJson.isNull("postId")) {
									postId = extraJson.getInt("postId");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name,
											PetCircleInsideDetailActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2015".equals(type)) {// 进入圈子个人主页
								if (extraJson.has("userId")
										&& !extraJson.isNull("userId")) {
									userId = extraJson.getInt("userId");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, PostUserInfoActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2022".equals(type)) {// 进入精选帖子详情页
								if (extraJson.has("postId")
										&& !extraJson.isNull("postId")) {
									postId = extraJson.getInt("postId");
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name,
											PostSelectionDetailActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MainActivity.class);
								}
							} else if ("2023".equals(type)) {// 进入粉丝列表界面
								if (spUtil.getString("cellphone", "") != null
										&& !TextUtils.isEmpty(spUtil.getString(
												"cellphone", ""))
										&& spUtil.getInt("userid", -1) > 0) {
									userId = spUtil.getInt("userid", -1);
									flag = "fans";
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, UserListActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, LoginActivity.class);
								}
							} else if ("2025".equals(type)) {// 进入投诉订单详情界面
								if (Utils.checkLogin(context)) {
									int orderId = extraJson.getInt("orderId");
									sendNotification(context, title, content,
											url, imgUrl, 0, orderId, 0, 0, 0,
											0, name,
											ComplaintOrderHistoryActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, LoginActivity.class);
								}
							} else if ("2026".equals(type)) {// 进入投诉客服列表界面
								if (Utils.checkLogin(context)) {
									sendNotification(
											context,
											title,
											content,
											url,
											imgUrl,
											0,
											0,
											0,
											0,
											0,
											0,
											name,
											CustomerComplaintsHistoryActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, LoginActivity.class);
								}
							} else if ("2027".equals(type)) {// 我的卡包
								if (Utils.checkLogin(context)) {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, MyCardsActivity.class);
								} else {
									sendNotification(context, title, content,
											url, imgUrl, 0, 0, 0, 0, 0, 0,
											name, LoginActivity.class);
								}
							}
						}
					} else {
						sendNotification(context, title, content, url, imgUrl,
								0, 0, 0, 0, 0, 0, name, MainActivity.class);
					}
				}
			} catch (JSONException e) {
			}
		}
	}

	private void sendNotification(Context context, String title,
			String content, String url, String imgUrl, int previous,
			int orderid, int workerId, int workerLevel, int serviceid,
			int servicetype, String name, Class clazz) {
		this.context = context;
		this.title = title;
		this.content = content;
		this.url = url;
		this.imgUrl = imgUrl;
		this.previous = previous;
		this.orderid = orderid;
		this.workerId = workerId;
		this.workerLevel = workerLevel;
		this.serviceid = serviceid;
		this.servicetype = servicetype;
		this.name = name;
		this.clazz = clazz;
		// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
		new Thread(networkTask).start();
	}

	/**
	 * @param context
	 * @param clazz
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
	 *           Notification.FLAG_AUTO_CANCEL
	 */
	public PendingIntent getDefalutIntent() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
		intent.putExtra("petid", petid);
		intent.putExtra("userId", userId);
		intent.putExtra("servicename", name);
		intent.putExtra("orderid", orderid);
		intent.putExtra("id", workerId);
		intent.putExtra("workerLevel", workerLevel);
		if (url != null && !TextUtils.isEmpty(url))
			intent.putExtra("url", url);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("postId", postId);
		intent.putExtra("flag", flag);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("areaid", areaid);
		if (Identification == 2005) {
			if (isLogin() && hasPet() && hasService(1)) {
				intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
			} else {
				intent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			}
		} else if (Identification == 2006) {
			if (isLogin() && hasPet() && hasService(2)) {
				intent.putExtra("previous", Global.PRE_MAINFRAGMENT);
			} else {
				intent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			}
		} else {
			intent.putExtra("previous", previous);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(context, clazz);
		int requestCode = (int) SystemClock.uptimeMillis();
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bmp = (Bitmap) msg.obj;
			if (bmp != null) {
				BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
				bigPictureStyle.bigPicture(bmp).setBigContentTitle(title)
						.setSummaryText(content);
				mNotificationManager = (NotificationManager) context
						.getSystemService(context.NOTIFICATION_SERVICE);
				mBuilder = new Builder(context);
				mBuilder.setContentIntent(getDefalutIntent())
						.setWhen(System.currentTimeMillis())
						// 通知产生的时间，会在通知信息里显示
						.setTicker("有新消息")
						.setPriority(Notification.PRIORITY_DEFAULT)
						// 设置该通知优先级
						.setOngoing(false)
						// 不是正在进行的 true为正在进行 效果和.flag一样
						.setStyle(bigPictureStyle)
						// 设置风格
						.setAutoCancel(true).setSmallIcon(R.drawable.logo)
						.setContentTitle(title).setContentText(content)
						.setDefaults(Notification.DEFAULT_ALL);
				Notification notify = mBuilder.build();
				mNotificationManager.notify(notifyId, notify);
			} else {
			}
		}
	};

	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgUrl);
			Message msg = new Message();
			msg.obj = returnBitmap;
			handler.sendMessage(msg);
		}
	};
}
