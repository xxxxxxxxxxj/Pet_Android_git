package com.haotang.pet.util;

import android.util.Log;

/**
 * <p>
 * Title:Logger
 * </p>
 * <p>
 * Description:日志打印工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-25 下午1:55:55
 */
public final class Logger {
	public static String TAG = "Pet";

	private static boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE);

	/**
	 * Customize the log tag for your application, so that other apps using
	 * Volley don't mix their logs with yours. <br />
	 * Enable the log property for your tag before starting your app: <br />
	 * {@code adb shell setprop log.tag.&lt;tag&gt;}
	 */
	static {
		Log.d(TAG, "LOGGER DEBUG = " + DEBUG);
	}

	// no instance
	private Logger() {
	}

	public static void v(String tag, String msg) {
		if (DEBUG)
			Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG)
			Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (DEBUG)
			Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG)
			Log.e(tag, msg, tr);
	}

}
