package com.haotang.pet.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * <p>
 * Title:UpdateService
 * </p>
 * <p>
 * Description:更新版本的service
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-11 下午4:18:53
 */
public class UpdateService extends Service {
	private String down_url;// 下载链接
	private boolean isUpgrade;
	private String versionHint;
	private String latestVersion;
	private static final int TIMEOUT = 10 * 1000;// 超时
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private String apkPath;

	/********* update UI ******/
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_OK:
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setAction("android.intent.action.UpdateServiceInstallApk");
				bundle.putBoolean("isUpgrade", isUpgrade);
				bundle.putString("versionHint", versionHint);
				bundle.putString("apkPath", apkPath);
				bundle.putString("latestVersion", latestVersion);
				intent.putExtras(bundle);
				sendBroadcast(intent);
				stopSelf();
				break;
			case DOWN_ERROR:
				stopSelf();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 方法描述：onStartCommand方法
	 * 
	 * @param Intent
	 *            intent, int flags, int startId
	 * @return int
	 * @see UpdateService
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		down_url = intent.getStringExtra("down_url");
		isUpgrade = intent.getBooleanExtra("isUpgrade", false);
		versionHint = intent.getStringExtra("versionHint");
		latestVersion = intent.getStringExtra("latestVersion");
		apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/PetApkCache/" + latestVersion + ".apk";
		// 开启分线程下载
		createThread();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 方法描述：createThread方法, 开线程下载
	 * 
	 * @param
	 * @return
	 * @see UpdateService
	 */
	public void createThread() {
		new DownLoadThread().start();
	}

	private class DownLoadThread extends Thread {
		@Override
		public void run() {
			Message message = new Message();
			try {
				// 获取应用的大小
				long downloadSize = downloadUpdateFile();
				if (downloadSize > 0) {
					// down success
					message.what = DOWN_OK;
					// 下载成功，就发送消息，进行安装
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = DOWN_ERROR;
				handler.sendMessage(message);
			}
		}

		/***
		 * down file
		 * 
		 * @return
		 * @throws MalformedURLException
		 */
		public long downloadUpdateFile() throws Exception {
			long readSize;
			int mediaLength = 0;
			FileOutputStream out = null;
			InputStream is = null;
			URL url = new URL(down_url);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			File cacheFile = new File(apkPath);
			if (!cacheFile.exists()) {
				cacheFile.getParentFile().mkdirs();
				cacheFile.createNewFile();
			}
			readSize = cacheFile.length();
			out = new FileOutputStream(cacheFile, true);
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			httpConnection.setRequestProperty("RANGE", "bytes=" + readSize
					+ "-");
			is = httpConnection.getInputStream();
			mediaLength = httpConnection.getContentLength();
			if (mediaLength == -1) {
				return -1;
			}
			mediaLength += readSize;
			byte buf[] = new byte[4 * 1024];
			int size = 0;
			while ((size = is.read(buf)) != -1) {
				try {
					out.write(buf, 0, size);
					readSize += size;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			out.close();
			is.close();
			return mediaLength;
		}
	}
}
