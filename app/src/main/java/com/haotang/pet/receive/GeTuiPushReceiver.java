package com.haotang.pet.receive;

import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haotang.pet.ADActivity;
import com.haotang.pet.EvaluateActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MyCouponActivity;
import com.haotang.pet.OrderDetailFromOrderActivity;
import com.haotang.pet.OrderDetailFromOrderToConfirmActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MCommonNotification;

public class GeTuiPushReceiver extends BroadcastReceiver {
	private SharedPreferenceUtil spUtil;
	@Override
	public void onReceive(Context context, Intent intent) {/*
		Bundle bundle = intent.getExtras();
		Utils.mLogError("--个推--onReceive() action="+bundle.getInt("action"));
		Utils.mLogError("--个推--onReceive() GET_MSG_DATA="+PushConsts.GET_MSG_DATA);
		Utils.mLogError("--个推--onReceive() GET_CLIENTID="+PushConsts.GET_CLIENTID);
		Utils.mLogError("--个推--onReceive() THIRDPART_FEEDBACK="+PushConsts.THIRDPART_FEEDBACK);
		spUtil = SharedPreferenceUtil.getInstance(context.getApplicationContext());
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
			Utils.mLogError("1--个推--onReceive() taskid="+taskid);
			Utils.mLogError("1--个推--onReceive() messageid="+messageid);
			Utils.mLogError("1--个推--onReceive() result="+result);
			Utils.mLogError("1--个推--onReceive() payload="+new String(payload));
			if (payload != null) {
				String data = new String(payload);
				Utils.mLogError("2--个推--onReceive() payload="+data);
				//根据返回做操作
				donePush(context,data);
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			Utils.mLogError("个推返回："+cid);
			Global.savePushID(context, cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			break;
		default:
			break;
		}
	*/}
	
	private void donePush(Context context,String json){
		try{
			JSONObject jobj = new JSONObject(json);
			if(jobj.has("type")&&!jobj.isNull("type")){
				String type = jobj.getString("type");
				String title = jobj.getString("title");
				String content = jobj.getString("description");
				if(jobj.has("custom_content")&&!jobj.isNull("custom_content")){
					JSONObject jData = jobj.getJSONObject("custom_content");
					if("100".equals(type)&&jData.has("ad_url")&&!jData.isNull("ad_url")){
						//跳转到h5界面
						String url = jData.getString("ad_url");
						if(isAppOpen(context)){
							sendNotification(context, title, content,
									0, 0,url, ADActivity.class);
						}else{
							sendNotification(context, title, content,
									0, 0,url, MainActivity.class);
						}
					}else if(jData.has("orderId")&&!jData.isNull("orderId")){
						int orderId = Integer.parseInt(jData.getString("orderId"));
						
						if("1".equals(type)||"1705".equals(type)){
							//未支付订单
							if(isAppOpen(context)){
								Utils.mLogError("应用已经启动");
								if(isPayOrderOpen(context)){
									if(OrderDetailFromOrderActivity.act!=null){
										OrderDetailFromOrderActivity.act.finishWithAnimation();
									}
									sendNotification(context, title, content,
											Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN, orderId,null, OrderDetailFromOrderActivity.class);
								}else{
									if("".equals(spUtil.getString("cellphone", ""))){
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_LOGIN, orderId,null, LoginActivity.class);
										
									}else{
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_ORDER, orderId,null, OrderDetailFromOrderActivity.class);
									}
								}
							}else{
								Utils.mLogError("应用没有启动");
								Utils.mLogError("11登录："+spUtil.getString("cellphone", ""));
								if("".equals(spUtil.getString("cellphone", ""))){
//									startApp(context,Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN,orderId,LoginActivity.class);
									sendNotification(context, title, content,
											Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN, orderId,null, LoginActivity.class);
								}else{
//									startApp(context,Global.PRE_PUSH_TO_NOSTARTAPP_ORDER,orderId,MainActivity.class);
									sendNotification(context, title, content,
											Global.PRE_PUSH_TO_NOSTARTAPP_ORDER, orderId,null, MainActivity.class);
								}
							}
						}else if("3".equals(type)){
							if(isAppOpen(context)){
								Utils.mLogError("应用已经启动");
									if("".equals(spUtil.getString("cellphone", ""))){
//										goNext(context, Global.PRE_PUSH_TO_LOGIN,orderId, LoginActivity.class);
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_LOGIN, orderId,null, LoginActivity.class);
									}else{
//										goNext(context, Global.PRE_PUSH_TO_ORDER,orderId, MyCouponActivity.class);
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_ORDER, orderId,null, MyCouponActivity.class);
									}
							}else{
								Utils.mLogError("应用没有启动");
								Utils.mLogError("11登录："+spUtil.getString("cellphone", ""));
								if("".equals(spUtil.getString("cellphone", ""))){
//									startApp(context,Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN,orderId,LoginActivity.class);
									sendNotification(context, title, content,
											Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN, orderId,null, LoginActivity.class);
								}else{
//									startApp(context,Global.PRE_PUSH_TO_NOSTARTAPP_COUPON,orderId,MainActivity.class);
									sendNotification(context, title, content,
											Global.PRE_PUSH_TO_NOSTARTAPP_COUPON, orderId,null, MainActivity.class);
								}
							}
						}else if("512".equals(type)){
							//到订单详情
							if(isAppOpen(context)){
								Utils.mLogError("应用已经启动");
									if("".equals(spUtil.getString("cellphone", ""))){
//										goNext(context, 0,orderId, MainActivity.class);
										sendNotification(context, title, content,
												0, orderId,null, MainActivity.class);
									}else{
//										goNext(context, 0,orderId, OrderDetailFromOrderToConfirmActivity.class);
										sendNotification(context, title, content,
												0, orderId,null, OrderDetailFromOrderToConfirmActivity.class);
										
									}
							}else{
								//应用未启动
								sendNotification(context, title, content,
										100, 0,null, MainActivity.class);
							}
						}else if("600".equals(type)||"65".equals(type)){
							//通知用户去评论 65为寄养退房或者结束
							if(isAppOpen(context)){
								Utils.mLogError("应用已经启动");
									if("".equals(spUtil.getString("cellphone", ""))){
//										goNext(context, Global.PRE_PUSH_TO_EVALUATE,orderId, LoginActivity.class);
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_EVALUATE, orderId,null, LoginActivity.class);
									}else{
//										goNext(context, Global.PRE_PUSH_TO_EVALUATE,orderId, EvaluateActivity.class);
										sendNotification(context, title, content,
												Global.PRE_PUSH_TO_EVALUATE, orderId,null, EvaluateActivity.class);
										
									}
							}else{
								Utils.mLogError("应用没有启动");
								Utils.mLogError("11登录："+spUtil.getString("cellphone", ""));
								sendNotification(context, title, content,
										100, 0,null, MainActivity.class);
							}
						}else{
							sendNotification(context, title, content,
									100, 0,null, MainActivity.class);
							
						}
						
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean isPayOrderOpen(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for(RunningTaskInfo info:list){
			if(info.topActivity.getClassName().equals("com.haotang.pet.OrderDetailFromOrderActivity")){
				return true;
			}
		}
		
		return false;
	}
	private boolean isAppOpen(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for(RunningTaskInfo info:list){
			if(info.topActivity.getPackageName().equals("com.haotang.pet")||
					info.baseActivity.getPackageName().equals("com.haotang.pet")){
				return true;
			}
		}
		
		return false;
	}
	private void goNext(Context context,int previous,int orderid,Class clazz){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context.getApplicationContext(), clazz);
		intent.putExtra("previous", previous);
		intent.putExtra("orderid", orderid);
		context.getApplicationContext().startActivity(intent);
	}
	private void startApp(Context context,int previous,int orderid,Class clazz){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName cn = new ComponentName(context.getApplicationContext(), clazz);
		intent.setComponent(cn);
		intent.putExtra("previous", previous);
		intent.putExtra("orderid", orderid);
		context.getApplicationContext().startActivity(intent);
	}
	

	private void sendNotification(Context context,String title,String content,
			int previous,int orderid,String url,Class clazz){
//		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		long when = System.currentTimeMillis();
//		Notification nf = new Notification(R.drawable.logo, title, when);
//		nf.flags = Notification.DEFAULT_SOUND;
//		nf.flags = Notification.DEFAULT_VIBRATE;
//		nf.flags = Notification.FLAG_AUTO_CANCEL;
//		Intent intent = new Intent(context, MainActivity.class);
//		intent.putExtra("previous", previous);
//		intent.putExtra("orderid", orderid);
//		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
//		nf.setLatestEventInfo(context, title, content, pi);
//		nm.notify(1, nf);
		Intent intent = new Intent(context, clazz);
		intent.putExtra("previous", previous);
		intent.putExtra("orderid", orderid);
		if(url!=null&&!"".equals(url))
			intent.putExtra("url", url);
		MCommonNotification mcn = new MCommonNotification(context);
		mcn.showCustomizeNotification(R.drawable.logo, title, content,
				intent, R.layout.mnotification);
	}
	
}
