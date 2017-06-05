package com.haotang.pet;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mapapi.SDKInitializer;
import com.haotang.pet.util.DataCleanManager;
import com.haotang.pet.util.ExampleUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.melink.bqmmsdk.sdk.BQMM;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yixia.weibo.sdk.VCamera;
import com.yixia.weibo.sdk.util.DeviceUtils;

/**
 * <p>
 * Title:MApplication
 * </p>
 * <p>
 * Description:主干
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-15 下午9:02:59
 */
@SuppressLint("NewApi")
public class MApplication extends Application {
	public static ArrayList<Activity> listAppoint;
	private static final int MSG_SET_ALIASANDTAGS = 1001;
	Set<String> tagSet = new LinkedHashSet<String>();
	private SharedPreferenceUtil spUtil;
	private static MApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initImageLoader(getApplicationContext());
		spUtil = SharedPreferenceUtil.getInstance(this);
		SDKInitializer.initialize(getApplicationContext());
		listAppoint = new ArrayList<Activity>();
		JPushInterface.setDebugMode(Utils.isLog); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		setaliasAndTags();
		String cid = JPushInterface.getRegistrationID(getApplicationContext());
		Utils.mLogError("cid = " + cid);
		if (!cid.isEmpty()) {
			Utils.mLogError("cid = " + cid);
			Global.savePushID(getApplicationContext(), cid);
		}
		// 初始化Vcamera
		initVcamera();
		// 初始化BQMM
		initBQMM();
		// 清理缓存
		removeCache();
	}

	private void removeCache() {
		// 判断手机的剩余存储空间，判断是否清楚手机中缓存的视频或裁剪的图片
		File PetTemp = new File(Environment.getExternalStorageDirectory(),
				"PetTemp");
		if (PetTemp.exists()) {
			DataCleanManager.cleanCustomCache(PetTemp.getAbsolutePath());
		}
		File PetCrop = new File(Environment.getExternalStorageDirectory(),
				"PetCrop");
		if (PetCrop.exists()) {
			DataCleanManager.cleanCustomCache(PetCrop.getAbsolutePath());
		}
		File dcim = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (dcim.exists()) {
			File VCameraDemo = new File(dcim + "/Camera/VCameraDemo/");
			if (VCameraDemo.exists()) {
				DataCleanManager
						.cleanCustomCache(VCameraDemo.getAbsolutePath());
			}
		}
		File PetCircleVideoCache = new File(
				Environment.getExternalStorageDirectory(),
				"PetCircleVideoCache");
		if (PetCircleVideoCache.exists()) {
			DataCleanManager.cleanCustomCache(PetCircleVideoCache
					.getAbsolutePath());
		}
	}

	private void initBQMM() {
		/**
		 * BQMM集成 首先从AndroidManifest.xml中取得appId和appSecret，然后对BQMM SDK进行初始化
		 */
		try {
			Bundle bundle = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA).metaData;
			BQMM.getInstance().initConfig(this,
					bundle.getString("bqmm_app_id"),
					bundle.getString("bqmm_app_secret"));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static MApplication getInstance() {
		return instance;
	}

	private void initVcamera() {
		// 设置拍摄视频缓存路径
		File dcim = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
						"/sdcard-ext/")
						+ "/Camera/VCameraDemo/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);
	}

	private void setaliasAndTags() {
		// 获取电话号码
		String cellphone = spUtil.getString("cellphone", "");
		Utils.mLogError("cellphone = " + cellphone);
		if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
			// 检查 tag 的有效性
			if (!ExampleUtil.isValidTagAndAlias(cellphone)) {
				Utils.mLogError("Invalid format");
			} else {
				tagSet.add(cellphone);
			}
			// 设置别名和tag
			JPushInterface.setAliasAndTags(getApplicationContext(), cellphone,
					tagSet, mAliasCallback);
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIASANDTAGS:
				Utils.mLogError("Set aliasandtags in handler.");
				TagAndAlias tagAndAlias = (TagAndAlias) msg.obj;
				JPushInterface.setAliasAndTags(getApplicationContext(),
						tagAndAlias.getAlias(), tagAndAlias.getTags(),
						mAliasCallback);
				break;
			default:
				Utils.mLogError("Unhandled msg - " + msg.what);
			}
		}
	};

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Utils.mLogError("设置别名:" + logs);
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Utils.mLogError("设置别名:" + logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIASANDTAGS,
									new TagAndAlias(alias, tags)), 1000 * 60);
				} else {
					Utils.mLogError("设置别名:" + "No network");
				}
				break;
			default:
				logs = "Failed with errorCode = " + code;
				Utils.mLogError("设置别名:" + logs);
			}
		}
	};

	class TagAndAlias {
		private String alias;
		private Set<String> tags;

		public TagAndAlias(String alias, Set<String> tags) {
			super();
			this.alias = alias;
			this.tags = tags;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public Set<String> getTags() {
			return tags;
		}

		public void setTags(Set<String> tags) {
			this.tags = tags;
		}
	}

	// 初始化ImageLoader
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(720, 1280)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.discCacheExtraOptions(720, 1280, null)
				// Can slow ImageLoader, use it carefully (Better don't use
				// it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				// .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}
}
