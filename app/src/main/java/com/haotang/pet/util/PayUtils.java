package com.haotang.pet.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * <p>
 * Title:PayUtils
 * </p>
 * <p>
 * Description:支付工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-4-13 下午8:05:23
 */
public class PayUtils {
	// 微信支付
	public static void weChatPayment(Activity activity, String appId,
			String partnerId, String prepayId, String packageValue,
			String nonceStr, String timeStamp, String sign,
			MProgressDialog pDialog) {
		PayReq payReq = new PayReq();
		payReq.appId = appId;
		payReq.partnerId = partnerId;
		payReq.prepayId = prepayId;
		payReq.packageValue = packageValue;
		payReq.nonceStr = nonceStr;
		payReq.timeStamp = timeStamp;
		payReq.sign = sign;
		IWXAPI createWXAPI = WXAPIFactory.createWXAPI(activity, null);
		createWXAPI.registerApp(appId);
		createWXAPI.sendReq(payReq);
		if (pDialog != null) {
			pDialog.closeDialog();
		}
	}

	// 支付宝支付
	public static void payByAliPay(final Activity activity,
			final String payStr, final Handler handler, MProgressDialog pDialog) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口
				String result = alipay.pay(payStr);
				Message msg = new Message();
				msg.what = Global.ALI_SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
		if (pDialog != null) {
			pDialog.closeDialog();
		}
	}
	
	/**
	 * 判断手机是否装有支付宝APP或者插件
	 * 
	 * @param act
	 * @param handler
	 */
	public static void checkAliPay(final Activity activity,final Handler handler) {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask payTask = new PayTask(activity);
				boolean isExist = payTask.checkAccountIfExist();
				// 之前写的暂时不动。后期如果大幅度出现 您没有安装支付宝，请先安装支付宝!可尝试解禁下方
				// Utils.mLogError("==-->payTask.getVersion()"
				// +payTask.getVersion());
				// if (!isExist) {
				// if (payTask.getVersion().length()>0) {
				// isExist = true;
				// }
				// }
				Message msg = new Message();
				msg.what = Global.ALI_SDK_CHECK_FLAG;
				msg.obj = isExist;
				handler.sendMessage(msg);
			}
		};
		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}
	
}
