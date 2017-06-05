package com.haotang.pet.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.pet.R;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.ta.utdid2.android.utils.StringUtils;

public class Utils {
	public static boolean isLog = true;
	private static String log_tag = "ht";

	public static void mLogError(String msg) {
		if (isLog)
			Log.e(log_tag, msg);
	}

	public static void mLog(String msg) {
		if (isLog)
			Log.i(log_tag, msg);
	}

	public static void mLog_d(String msg) {
		if (isLog)
			Log.d(log_tag, msg);
	}

	public static void mLog_v(String msg) {
		if (isLog)
			Log.v(log_tag, msg);
	}

	public static void mLog_w(String msg) {
		if (isLog)
			Log.w(log_tag, msg);
	}

	/**
	 * 是否WIFI联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkWIFI(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		if (null != networkInfo) {
			if (ConnectivityManager.TYPE_WIFI == networkInfo.getType())
				return true;
		}
		return false;
		// String type = networkInfo.getTypeName();
		// return "WIFI".equalsIgnoreCase(type);
	}

	/**
	 * 是否网络可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isAvailable()) {
			return true;
		}
		return false;
		// String type = networkInfo.getTypeName();
		// return "WIFI".equalsIgnoreCase(type);
	}

	/**
	 * 获取手机屏幕的宽高
	 * 
	 * @param activity
	 * @return
	 */
	public static int[] getDisplayMetrics(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 把毫秒为单位的时间格式化为yyyyMMddHHmmss
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date(time));
		if (date == null || "".equalsIgnoreCase(date))
			date = "00000000000000";
		return date;
	}

	/**
	 * 把毫秒为单位的时间格式化为0000-00-00 00:00:00
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDateAll(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(time));
		if (date == null || "".equalsIgnoreCase(date))
			date = "0000-00-00 00:00:00";
		return date;
	}

	/**
	 * 把以毫秒为单位的时间格式化为0000-00-00
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDateShort(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(time));
		if (date == null || "".equalsIgnoreCase(date))
			date = "0000-00-00";
		return date;
	}

	/**
	 * 使double类型保留points位小数
	 * 
	 * @param num
	 * @return
	 */
	public static double formatDouble(double num, int points) {
		BigDecimal bigDecimal = new BigDecimal(num);
		return bigDecimal.setScale(points, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/**
	 * @param num
	 * @return
	 */
	public static String formatDouble(double num, String pon) {
		DecimalFormat df = new DecimalFormat(pon);
		return df.format(num);
	}

	/**
	 * double类型转换为int类型
	 * 
	 * @param num
	 * @return
	 */
	public static int formatDouble(double num) {
		return Integer.parseInt(new java.text.DecimalFormat("0").format(num));
	}

	/**
	 * 生成一个小于Max的随机数
	 * 
	 * @param max
	 * @return
	 */
	public static int getRandom(int max) {
		Random rd = new Random();
		return rd.nextInt(max);
	}

	/**
	 * 把数据写入sd卡
	 * 
	 * @param log
	 *            数据
	 * @param filename
	 *            文件名
	 * @param time
	 *            时间，系统 时间，单位毫秒
	 */
	public static void writeToSDCard(String log, String filename, long time) {

		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File dir = new File(path, "haotang");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File guiji = new File(dir, filename);
		try {
			FileWriter fw = new FileWriter(guiji, true);
			fw.append(formatDate(time) + " ::");
			fw.append(log + "\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取应用的文件夹
	 * 
	 * @return
	 */
	public static String getPetPath(Context context) {
		File file = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath(), ".pet");
		} else {
			file = context.getCacheDir();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 计算球面上两个点的距离
	 * 
	 * @param lat1
	 * @param lat2
	 * @param lon1
	 * @param lon2
	 * @return
	 */
	public static double getDistatce(double lat1, double lat2, double lon1,
			double lon2) {
		double R = 6371;
		double distance = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		return distance;
	}

	/**
	 * 格式化double类型
	 * 
	 * @param pattern
	 *            如#0.00为保留两位小数
	 * @param decimal
	 * @return
	 */
	// 格式化里程和费用
	public static String formatDecimal(String pattern, double decimal) {
		DecimalFormat df = new DecimalFormat();
		df.applyPattern(pattern);
		return df.format(decimal);

	}

	/**
	 * 把毫秒为单位的时间格式化为时分秒
	 * 
	 * @param time
	 * @return
	 */
	// 格式化时间格式
	public static String formatTime(long time) {
		long totalss = time / 1000;
		long timess = totalss % 60;
		long totalmm = totalss / 60;
		long timemm = totalmm % 60;
		long totalhh = totalmm / 60;
		long timehh = totalhh % 60;
		StringBuffer sb = new StringBuffer();
		if (timehh < 10) {
			sb.append("0");
		}
		sb.append(timehh);
		sb.append(":");
		if (timemm < 10) {
			sb.append("0");
		}
		sb.append(timemm);
		sb.append(":");
		if (timess < 10) {
			sb.append("0");
		}
		sb.append(timess);
		return sb.toString();
	}

	/**
	 * 把毫秒为单位的时间格式化为时分秒
	 * 
	 * @param time
	 * @return
	 */
	// 格式化时间格式
	public static String formatTimeFM(long time) {
		long totalss = time / 1000;
		long timess = totalss % 60;
		long totalmm = totalss / 60;
		long timemm = totalmm % 60;
		long totalhh = totalmm / 60;
		long timehh = totalhh % 60;
		StringBuffer sb = new StringBuffer();
		if (timehh < 10) {
			// sb.append("0");
		}
		// sb.append(timehh);
		// sb.append(":");
		if (timemm < 10) {
			sb.append("0");
		}
		sb.append(timemm);
		sb.append(":");
		if (timess < 10) {
			sb.append("0");
		}
		sb.append(timess);
		return sb.toString();
	}

	/**
	 * 格式化以分钟为单位的时间，转化为小时：分钟
	 * 
	 * @param minutestr
	 * @return
	 */
	public static String formatMinutesToHour(String minutestr) {
		int minutes = Integer.parseInt(minutestr);
		int minute = minutes % 60;
		int hour = minutes / 60;
		if (0 == hour) {
			return minute + "分钟";
		} else {
			return hour + "小时" + minute + "分钟";
		}
	}

	// 格式化时间格式
	public static long TimeToMinutes(long time) {
		long totalss = time / 1000;
		long totalmm = totalss / 60;
		// if(totalss % 60 > 0){
		// totalmm += 1;
		// }
		return totalmm;
	}

	/**
	 * 转换文件大小格式
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		// if (0l == fileS)
		// return "";
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS <= 0) {
			fileSizeString = "0B";
		} else if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 创建并获取文件夹的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDir(Context context) {
		File file = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile(), "pet");
		} else {
			file = context.getCacheDir();
		}
		if (!file.exists()) {
			file.mkdirs();
		}

		return file.getAbsolutePath();
	}

	/**
	 * 创建精度条对话框
	 * 
	 * @param ctx
	 * @param info
	 * @return
	 */
	public static ProgressDialog createProcessDialog(Context ctx, String info) {
		ProgressDialog processDialog = new ProgressDialog(ctx);
		processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		processDialog.setTitle("");
		processDialog.setMessage(info);
		processDialog.setIcon(android.R.drawable.ic_dialog_map);
		processDialog.setIndeterminate(false);
		processDialog.setCancelable(true);
		processDialog.setCanceledOnTouchOutside(false);
		processDialog.show();
		return processDialog;

	}

	/**
	 * 吐司
	 * 
	 * @param context
	 * @param str
	 */
	public static void showTaost(Context context, String str) {
		// Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		ToastUtil.showToastShort(context, str);
	}

	/**
	 * 电话验证
	 * 
	 * @param
	 * @return
	 */
	public static boolean checkPhone(Context context, EditText phone_et) {
		String telPattern = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(17[0-9]))\\d{8}$";
		String phone = phone_et.getText().toString().trim();
		boolean isAvail = startCheck(telPattern, phone);
		if ("".equals(phone.trim())) {
			showTaost(context, "请输入您的手机号码");
			phone_et.requestFocus();
			phone_et.setFocusableInTouchMode(true);
			return false;
		}
		if (!isAvail) {
			showTaost(context, "请输入正确的手机号码");
			phone_et.requestFocus();
			phone_et.setFocusableInTouchMode(true);
			return false;
		}
		return true;
	}

	/**
	 * 邮箱验证
	 * 
	 * @param context
	 * @param email_et
	 * @return
	 */
	public static boolean checkEmail(Context context, EditText email_et) {
		String emailPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		String email = email_et.getText().toString().trim();
		boolean isAvail = startCheck(emailPattern, email);

		if (!isAvail) {
			showTaost(context, "邮箱格式不正确，请重新输入");
			email_et.requestFocus();
			email_et.setFocusableInTouchMode(true);
			return false;
		}
		if ("".equals(email.trim())) {
			showTaost(context, "邮箱为空，请重新输入");
			email_et.requestFocus();
			email_et.setFocusableInTouchMode(true);
			return false;
		}
		if (email.length() > 50) {
			showTaost(context, "邮箱长度不能大于50，请重新输入");
			email_et.requestFocus();
			email_et.setFocusableInTouchMode(true);
			return false;
		}
		return true;
	}

	/**
	 * 正则规则验证
	 * 
	 * @param reg
	 * @param string
	 * @return
	 */
	public static boolean startCheck(String reg, String string) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	/**
	 * app是否正在运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppAvailable(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskinfos = am.getRunningTasks(20);
		boolean result = false;
		for (int i = 0; i < taskinfos.size(); i++) {
			if (context.getPackageName().equals(
					taskinfos.get(i).topActivity.getPackageName())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static String getIPAddress(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (manager.isWifiEnabled()) {
			return getWifiAddress(context);
		}
		return getLocalIPAddress();

	}

	// 获取wifi地址
	public static String getWifiAddress(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!manager.isWifiEnabled()) {
			manager.setWifiEnabled(true);
		}
		WifiInfo info = manager.getConnectionInfo();
		int ip = info.getIpAddress();
		return ipToString(ip);
	}

	public static String ipToString(int ip) {

		return (ip & 0xFF) + "." +

		((ip >> 8) & 0xFF) + "." +

		((ip >> 16) & 0xFF) + "." +

		(ip >> 24 & 0xFF);
	}

	public static String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces(); networkInterfaces
					.hasMoreElements();) {
				NetworkInterface networkInterface = networkInterfaces
						.nextElement();
				for (Enumeration<InetAddress> addresses = networkInterface
						.getInetAddresses(); addresses.hasMoreElements();) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress()
							&& (address instanceof Inet4Address)) {
						return address.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	public static Bitmap getbitmap(String imageFile, int length) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(imageFile, opts);
		int ins = computeSampleSize(opts, -1, length);
		opts.inSampleSize = ins;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inJustDecodeBounds = false;
		try {
			Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
			return bmp;
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static String creatfile(Context pContext, Bitmap bm, String filename) {
		if (bm == null) {
			return "";
		}
		File f = new File(getSDCardPath(pContext) + "/" + filename + ".jpg");
		try {
			FileOutputStream out = new FileOutputStream(f);
			if (bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.getAbsolutePath();
	}

	public static String getSDCardPath(Context pContext) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.jiuyi160_c";
			File PathDir = new File(path);
			if (!PathDir.exists()) {
				PathDir.mkdirs();
			}
			return path;
		} else {
			return pContext.getCacheDir().getAbsolutePath();
		}
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
	public static Bitmap getxtsldraw(Context c, String file) {
		File f = new File(file);
		Bitmap drawable = null;
		if (f.length() / 1024 < 100) {
			drawable = BitmapFactory.decodeFile(file);
		} else {
			Cursor cursor = c.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Images.Media._ID },
					MediaStore.Images.Media.DATA + " like ?",
					new String[] { "%" + file }, null);
			if (cursor == null || cursor.getCount() == 0) {
				drawable = getbitmap(file, 720 * 1280);
			} else {
				if (cursor.moveToFirst()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					options.inInputShareable = true;
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					String videoId = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media._ID));
					long videoIdLong = Long.parseLong(videoId);
					drawable = MediaStore.Images.Thumbnails.getThumbnail(
							c.getContentResolver(), videoIdLong,
							Thumbnails.MINI_KIND, options);
				} else {
					// drawable = BitmapFactory.decodeResource(c.getResources(),
					// R.drawable.ic_doctor);
				}
			}
		}
		int degree = 0;
		ExifInterface exifInterface;
		try {
			exifInterface = new android.media.ExifInterface(file);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
			if (degree != 0 && drawable != null) {
				Matrix m = new Matrix();
				m.setRotate(degree, (float) drawable.getWidth() / 2,
						(float) drawable.getHeight() / 2);
				drawable = Bitmap.createBitmap(drawable, 0, 0,
						drawable.getWidth(), drawable.getHeight(), m, true);
			}
		} catch (java.lang.OutOfMemoryError e) {
			// Toast.makeText(c, "牌照出错，请重新牌照", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static String getDataString(String data) {
		String n = data.replace("年", "-");
		String y = n.replace("月", "-");
		String r = y.replace("日", " ");
		String s = r.replace("时", ":");
		String f = s.replace("分", ":");
		String overCreateDate = f + "00";
		return overCreateDate;

	}

	// 设置弹窗背景
	public static void setAttribute(Activity activity, PopupWindow pWin) {
		ColorDrawable cd = new ColorDrawable(0x000000);
		pWin.setBackgroundDrawable(cd);
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = 0.4f;
		activity.getWindow().setAttributes(lp);
	}

	// 弹窗背景还原
	public static void onDismiss(Activity activity) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = 1f;
		activity.getWindow().setAttributes(lp);
	}

	// 改变时间样式
	public static String ChangeTime(String dateOld) {
		String n = dateOld.replace("年", "-");
		String y = n.replace("月", "-");
		String r = y.replace("日", " ");
		String s = r.replace("时", ":");
		String f = s.replace("分", "");
		return f;
	}

	public static void telePhoneBroadcast(Activity activity, String phone) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone));
		activity.startActivity(intent);
	}

	// 设置圆角背景
	public static Drawable getDW(String locColor) {
		int strokeWidth = 1; // 3dp 边框宽度
		int roundRadius = 8; // 8dp 圆角半径
		int strokeColor = Color.parseColor("#" + locColor);// 边框颜色
		int fillColor = Color.parseColor("#" + locColor);// 内部填充颜色
		GradientDrawable gd = new GradientDrawable();// 创建drawable
		gd.setColor(fillColor);
		gd.setCornerRadius(roundRadius);
		gd.setStroke(strokeWidth, strokeColor);
		return gd;
	}

	/**
	 * 设置字体大小
	 * 
	 * @param str
	 *            需要设置的字符串。
	 * @param color
	 *            (用getResource的方式获取自定义color。) 颜色。
	 * 
	 *            return SpannableString 设置了颜色的字符串。
	 * */
	public static SpannableString getColorSpan(String str, int color,
			int colorEndIndex) {

		SpannableString spanString = new SpannableString(str);

		ForegroundColorSpan span = new ForegroundColorSpan(color);
		spanString.setSpan(span, 0, colorEndIndex,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanString;

	}

	public static Bitmap reduceBitmapQuality(String sourceBitmap) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 2;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(sourceBitmap, options);
	}

	/**
	 * 图片变色1
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap grey(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		// ColorMatrix colorMatrix = new ColorMatrix(new float[]{
		// 0.33F, 0.59F, 0.11F, 0, 0,
		// 0.33F, 0.59F, 0.11F, 0, 0,
		// 0.33F, 0.59F, 0.11F, 0, 0,
		// 0, 0, 0, 1, 0,
		// });
		ColorMatrix colorMatrix = new ColorMatrix(new float[] { 0.5f, 0, 0, 0,
				50, 0, 0.5f, 0, 0, 50, 0, 0, 0.5f, 0, 50, 0, 0, 0, 1f, 0, 0, 0,
				0, 0, 1

		});
		// colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return faceIconGreyBitmap;
	}

	/** */
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmp) {
		if (bmp != null) {
			int width, height;
			Paint paint = new Paint();
			height = bmp.getHeight();
			width = bmp.getWidth();
			Bitmap bm = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bm);
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0f);
			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(f);
			c.drawBitmap(bmp, 0, 0, paint);
			return bm;
		} else {
			return bmp;
		}
	}

	/**
	 * 图片变色2
	 * 
	 * @param bitmap
	 * @param newColor
	 * @return
	 */
	public static Bitmap changeImageColor(Bitmap bitmap, int newColor) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap grayImg = null;
		try {
			grayImg = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(grayImg);
			Paint paint = new Paint();
			ColorMatrix colorMatrix = new ColorMatrix();
			float[] colorArray = { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0,
					0, 0, 0, 1, 0 };
			colorMatrix.setSaturation(0);
			// colorMatrix.set(colorArray);
			ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
					colorMatrix);
			paint.setColorFilter(colorMatrixFilter);
			// paint.setColorFilter(new LightingColorFilter(Color.BLUE,
			// newColor));//通过color控制图片颜色
			canvas.drawBitmap(bitmap, 0, 0, paint);
			bitmap.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grayImg;
	}

	/** 从给定路径加载图片 */
	public static Bitmap loadBitmap(String imgpath) {
		return BitmapFactory.decodeFile(imgpath);
	}

	/** 从给定的路径加载图片，并指定是否自动旋转方向 */
	public static Bitmap loadBitmap(String path, boolean adjustOritation) {
		if (!adjustOritation) {
			return loadBitmap(path);
		} else {
			Bitmap bm = loadBitmap(path);
			int digree = 0;
			ExifInterface exif = null;
			try {
				exif = new ExifInterface(path);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();

			}
			if (exif != null) {
				// 读取图片中相机方向信息
				int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_UNDEFINED);
				// 计算旋转角度
				switch (ori) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					digree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					digree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					digree = 270;
					break;
				default:
					digree = 0;
					break;
				}
			}
			if (digree != 0) {
				// 旋转图片
				Matrix m = new Matrix();
				m.postRotate(digree);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
			}
			return bm;
		}
	}

	public static enum JSON_TYPE {
		/** JSONObject */
		JSON_TYPE_OBJECT,
		/** JSONArray */
		JSON_TYPE_ARRAY,
		/** 不是JSON格式的字符串 */
		JSON_TYPE_ERROR
	}

	public static JSON_TYPE getJSONType(String str) {
		if (TextUtils.isEmpty(str)) {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}

		final char[] strChar = str.substring(0, 1).toCharArray();
		final char firstChar = strChar[0];

		if (firstChar == '{') {
			return JSON_TYPE.JSON_TYPE_OBJECT;
		} else if (firstChar == '[') {
			return JSON_TYPE.JSON_TYPE_ARRAY;
		} else {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}
	}

	public static Spanned getTextAndColor(String NameBase, String PriceBase) {
		return Html.fromHtml("<font size=\"3\" color=\"#757575\">" + NameBase
				+ "</font><font size=\"3\" color=\"#FF8C00\">" + "<b>"
				+ PriceBase + "</b>" + "</font>");
	}

	public static Spanned getTextAndColorComments(String NameBase,
			String PriceBase) {
		return Html.fromHtml("<font size=\"3\" color=\"#757575\">" + NameBase
				+ "</font><font size=\"3\" color=\"#FF8C00\">" + PriceBase
				+ "</font>");
	}

	public static Spanned getTextAndColorCommentsBeau(String firstColor,
			String NameBase, String secondColor, String PriceBase) {
		return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
				+ NameBase + "</font><font size=\"3\" color=\"" + secondColor
				+ "\">" + PriceBase + "</font>");
	}

	public static Spanned getTextAndColorCommentsBeau(String firstColor,
			String NameBase) {
		return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
				+ NameBase + "</font>");
	}

	public static Spanned getTextAndColorThr(String firstColor,
			String NameBase, String secondColor, String PriceBase,
			String thrColor, String showStr) {
		return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
				+ NameBase + "</font><font size=\"3\" color=\"" + secondColor
				+ "\">" + PriceBase + "</font>" + "<font size=\"3\" color=\""
				+ thrColor + "\">" + showStr + "</font>");
	}

	public static Spanned getTextAndColorFour(String firstColor,
			String NameBase, String secondColor, String PriceBase,
			String thrColor, String showStr, String fourColor, String fourStr) {
		return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
				+ NameBase + "</font><font size=\"3\" color=\"" + secondColor
				+ "\">" + PriceBase + "</font>" + "<font size=\"3\" color=\""
				+ thrColor + "\">" + showStr + "</font>"
				+ "<font size=\"3\" color=\"" + fourColor + "\">" + fourStr
				+ "</font>");

	}

	public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
		Bitmap newBitMap = null;
		if(bitMap != null){
			int width = bitMap.getWidth();
			int height = bitMap.getHeight();
			// 设置想要的大小
			int newWidth = 99;
			int newHeight = 99;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
					matrix, true);
		}
		return newBitMap;
	}

	/**
	 * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
	 * 
	 * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
	 * 
	 * B.本地路径:url="file://mnt/sdcard/photo/image.png";
	 * 
	 * C.支持的图片格式 ,png, jpg,bmp,gif等等
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap GetLocalOrNetBitmap(String imageUrl) {
		if (!imageUrl.startsWith("drawable://")
				&& !imageUrl.startsWith("file://")
				&& !imageUrl.startsWith("http://")
				&& !imageUrl.startsWith("https://")) {
			imageUrl = CommUtil.getServiceNobacklash() + imageUrl;
		}
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(imageUrl).openStream(),
					IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	public final static int IO_BUFFER_SIZE = 1024;

	public static void MyRecycle(Bitmap bmp) {
		if (bmp != null && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
	}

	public static void setStringText(TextView tv, String str) {
		if (str != null && !TextUtils.isEmpty(str)) {
			tv.setText(str);
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.INVISIBLE);
		}
	}

	public static void setStringTextGone(TextView tv, String str) {
		if (str != null && !TextUtils.isEmpty(str)) {
			tv.setText(str);
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 查询当前日期前(后)x天的日期
	 * 
	 * @param millis
	 *            当前日期毫秒数
	 * @param day
	 *            天数（如果day数为负数,说明是此日期前的天数）
	 * @return long 毫秒数只显示到天，时间全为0
	 * @throws ParseException
	 */
	public static long beforDateNum(long millis, int day) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		c.add(Calendar.DAY_OF_YEAR, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(c.getTimeInMillis());
		Date newDate = sdf.parse(sdf.format(date));
		return newDate.getTime();
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 比较两个日期的大小 DATE1 > DATE2 返回1 DATE1 < DATE2 返回-1 DATE1 == DATE2 返回0
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static SpannableString getTextShow(Context mContext, String str,
			int oneStart, int oneEnd, int TwoStart, int TwoEnd) {
		SpannableString styledText = new SpannableString(str);
		// styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style0),
		// oneStart,oneEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style1),
				TwoStart, TwoEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

	// 判断微信是否可用
	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 读取照片exif信息中的旋转角度
	 * 
	 * @param path
	 *            照片路径
	 * @return角度
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			File imageFile = new File(filepath);
			exif = new ExifInterface(imageFile.getAbsolutePath());
		} catch (IOException ex) {
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public static Bitmap toturn(Bitmap img, int rto) {
		Matrix matrix = new Matrix();
		matrix.postRotate(+rto); // 翻转rto度
		int width = img.getWidth();
		int height = img.getHeight();
		img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
		return img;
	}

	public static boolean isStrNull(String str) {
		boolean bool = false;
		if (str != null && !TextUtils.isEmpty(str)) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	public static boolean isObjNull(Object obj) {
		boolean bool = false;
		if (obj != null) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	// 跳转到应用市场评价
	public static void goMarket(Context context) {
		try {
			Uri uri = Uri.parse("market://details?id="
					+ context.getPackageName());
			Intent intentwx = new Intent(Intent.ACTION_VIEW, uri);
			intentwx.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentwx);
		} catch (Exception e) {
			ToastUtil.showToastShortBottom(context, "您没有安装应用市场");
		}
	}

	/**
	 * 将 BD-09 坐标转换成 GCJ-02 坐标 GoogleMap和高德map用的是同一个坐标系GCJ-02
	 * */
	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	public static double[] bd_decrypt(double bd_lat, double bd_lon) {
		double gg_lat = 0.0;
		double gg_lon = 0.0;
		double location[] = new double[2];
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		gg_lon = z * Math.cos(theta);
		gg_lat = z * Math.sin(theta);
		location[0] = gg_lat;
		location[1] = gg_lon;
		return location;
	}

	public static String ToSBC(String input) { // 全角
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/*
	 * 直接\u9999表示的很少，基本都会用转义字符即\\u9999,这样一来打印出来的就不是我们需要的字符，而是Unicode码\u9999
	 * 因此必须解析字符串得到想要的字符。
	 */
	public static String unicode2string(String s) {
		List<String> list = new ArrayList<String>();
		String zz = "\\\\u[0-9,a-z,A-Z]{4}";

		// 正则表达式用法参考API
		Pattern pattern = Pattern.compile(zz);
		Matcher m = pattern.matcher(s);
		while (m.find()) {
			list.add(m.group());
		}
		for (int i = 0, j = 2; i < list.size(); i++) {
			String st = list.get(i).substring(j, j + 4);

			// 将得到的数值按照16进制解析为十进制整数，再強转为字符
			char ch = (char) Integer.parseInt(st, 16);
			// 用得到的字符替换编码表达式
			s = s.replace(list.get(i), String.valueOf(ch));
		}
		return s;
	}

	/*
	 * 将字符转为Unicode码表示
	 */
	public static String string2unicode(String s) {
		int in;
		String st = "";
		for (int i = 0; i < s.length(); i++) {
			in = s.codePointAt(i);
			st = st + "\\u" + Integer.toHexString(in).toUpperCase();
		}
		return st;
	}

	private void setEmojiToTextView(TextView myTextView) {
		int unicodeJoy = 0x1F602;
		String emojiString = getEmojiStringByUnicode(unicodeJoy);
		myTextView.setText(emojiString);
	}

	private String getEmojiStringByUnicode(int unicode) {
		return new String(Character.toChars(unicode));
	}

	// 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	@SuppressLint("NewApi")
	public static String getPathByUri4kitkat(final Context context,
			final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(uri)) {// DownloadsProvider
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(uri)) {// MediaProvider
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
																	// (and
																	// general)
			return getDataColumn(context, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
			return uri.getPath();
		}
		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean checkLogin(Context mContext) {
		boolean bool;
		String cellphone = SharedPreferenceUtil.getInstance(mContext)
				.getString("cellphone", "");
		int userid = SharedPreferenceUtil.getInstance(mContext).getInt(
				"userid", 0);
		if (cellphone != null && !cellphone.isEmpty() && userid > 0) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	public static String insertStringInParticularPosition(String src,
			String dec, int position) {
		StringBuffer stringBuffer = new StringBuffer(src);
		return stringBuffer.insert(position, dec).toString();
	}

	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					context.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	public static File saveImage(Bitmap bmp) {
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"PetTemp");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = "Pet_Temp_camera"
				+ String.valueOf(System.currentTimeMillis() + ".png");
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void goneJP(Context context) {
		try {
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setText(TextView tv, String str, String defaultStr,
			int visibilt, int defaultVisibilt) {
		if (str != null && !TextUtils.isEmpty(str)) {
			tv.setText(str);
			tv.setVisibility(visibilt);
		} else {
			tv.setText(defaultStr);
			tv.setVisibility(defaultVisibilt);
		}
	}

	public static JSONObject getNetData(Context context, byte[] responseBody) {
		JSONObject objectData = null;
		try {
			JSONObject object = new JSONObject(new String(responseBody));
			int code = object.getInt("code");
			if (code == 0) {
				if (object.has("data") && !object.isNull("data")) {
					objectData = object.getJSONObject("data");
				}
			} else {
				if (object.has("msg") && !object.isNull("msg")) {
					String msg = object.getString("msg");
					ToastUtil.showToastShortCenter(context, msg);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objectData;
	}

	public static Drawable loadImageFromNetwork(String imageUrl) {
		if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
			imageUrl = CommUtil.getServiceNobacklash() + imageUrl;
		}
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}
		return drawable;
	}

	/**
	 * RxJava方式保存图片到本地
	 * 
	 * @param context
	 * @param mImageView
	 * @param petCircle
	 * @param fileName
	 */
	public static void savePic(final Context context,
			final ImageView mImageView, final File petCircle,
			final String fileName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(
				new String[] { context.getResources().getString(
						R.string.save_picture) },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						saveImageView(getViewBitmap(mImageView), petCircle,
								context, fileName);
					}
				});
		builder.show();
	}

	private static class SaveObservable implements
			Observable.OnSubscribe<String> {
		private Bitmap drawingCache = null;
		private File petCircle;
		private String fileName;

		public SaveObservable(Bitmap drawingCache, File petCircle,
				String fileName) {
			this.drawingCache = drawingCache;
			this.petCircle = petCircle;
			this.fileName = fileName;
		}

		@Override
		public void call(Subscriber<? super String> subscriber) {
			if (drawingCache == null) {
				subscriber.onError(new NullPointerException(
						"imageview的bitmap获取为null,请确认imageview显示图片了"));
			} else {
				try {
					File imageFile = new File(this.petCircle, this.fileName);
					FileOutputStream outStream;
					outStream = new FileOutputStream(imageFile);
					drawingCache.compress(Bitmap.CompressFormat.JPEG, 100,
							outStream);
					subscriber.onNext(Environment.getExternalStorageDirectory()
							.getPath());
					subscriber.onCompleted();
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					subscriber.onError(e);
				}
			}
		}
	}

	private static class SaveSubscriber extends Subscriber<String> {
		private Context context;

		public SaveSubscriber(Context context) {
			this.context = context;
		}

		@Override
		public void onCompleted() {
			Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(Throwable e) {
			Log.i(getClass().getSimpleName(), e.toString());
			Toast.makeText(context, "保存失败——> " + e.toString(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNext(String s) {/*
									 * Toast.makeText(context, "保存路径为：-->  " +
									 * s, Toast.LENGTH_SHORT) .show();
									 */
		}
	}

	private static void saveImageView(Bitmap drawingCache, File petCircle,
			Context context, String fileName) {
		Observable
				.create(new SaveObservable(drawingCache, petCircle, fileName))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SaveSubscriber(context));
	}

	/**
	 * 某些机型直接获取会为null,在这里处理一下防止国内某些机型返回null
	 */
	private static Bitmap getViewBitmap(View view) {
		if (view == null) {
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	/**
	 * 计算时间差
	 * 
	 * @param oldTime
	 * @param newTime
	 * @return
	 */
	public static int TimeCha(String oldTime, String newTime) {
		int days = 0;
		try {
			SimpleDateFormat simpleFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm");// 如2016-08-10 20:40
			long from = simpleFormat.parse(oldTime).getTime();
			long to = simpleFormat.parse(newTime).getTime();
			days = (int) ((to - from) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			Log.e("TAG", "E = " + e.toString());
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/*
	 * 获取时间差
	 */
	public static String getTimesToNow(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String now = format.format(new Date());
		String returnText = null;
		try {
			long from = format.parse(date).getTime();
			long to = format.parse(now).getTime();
			int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
			if (days == 0) {// 一天以内，以分钟或者小时显示
				int hours = (int) ((to - from) / (1000 * 60 * 60));
				if (hours == 0) {
					int minutes = (int) ((to - from) / (1000 * 60));
					if (minutes == 0) {
						returnText = "刚刚";
					} else {
						returnText = minutes + "分钟前";
					}
				} else {
					returnText = hours + "小时前";
				}
			} else if (days == 1) {
				returnText = "昨天";
			} else {
				returnText = days + "天前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnText;
	}

	public static void imageBrower(Context context, int position, String[] urls) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	/**
	 * 隐藏虚拟按键，并且全屏
	 */
	@SuppressLint("NewApi")
	public static void hideBottomUIMenu(Activity context) {
		// 隐藏虚拟按键，并且全屏
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower
																		// api
			View v = context.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			// for new api versions.
			View decorView = context.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}
}
