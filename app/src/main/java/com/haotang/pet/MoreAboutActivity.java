package com.haotang.pet;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.InstallDialog;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UpdateUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyNotification;

public class MoreAboutActivity extends SuperActivity {

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ImageView imageView_noty_img;

	private TextView textView_more_update, textView_more_about_other;
	private RelativeLayout layout_update_check, layout_about;
	private boolean accept = true;

	private LinearLayout layout_login_out;

	private LayoutInflater mInflater;
	private SharedPreferenceUtil spUtil;
	private Short pushNotify = 1;
	private String servicePhone;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Global.APK_DOWNLOAD_OVER) {
				// Utils.mLogError("--已经下载完毕：：："+(double)msg.obj);
				mNotification.removeNotification();
			} else if (msg.what == Global.APK_DOWNLOADING) {
				// Utils.mLogError("--已经下载：：："+(double)msg.obj+"%");
				mNotification.changeProgressStatus(Utils.formatDouble(
						(Double) msg.obj, 1));
			} else if (msg.what == Global.APK_DOWNLOAD_FAIL) {
				mNotification.changeNotificationText("下载失败");
				mNotification.removeNotification();
				mNotification.showDefaultNotification(R.drawable.logo, "宠物家",
						"下载失败");
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_about);
		spUtil = SharedPreferenceUtil.getInstance(this);
		Intent intent = getIntent();
		servicePhone = intent.getStringExtra("servicePhone");
		initView();
		initListener();
		accept = spUtil.getBoolean("accept", true);
		if (accept) {
			// pushNotify = 1;
			imageView_noty_img.setBackgroundResource(R.drawable.noty_yes);
		} else {
			// pushNotify = 0;
			imageView_noty_img.setBackgroundResource(R.drawable.noty_no);
		}
	}

	private void initListener() {
		if (spUtil.getString("cellphone", "").equals("")
				|| spUtil.getInt("userid", 0) <= 0) {
			layout_login_out.setVisibility(View.GONE);
		}
		// 检测是否接受通知
		imageView_noty_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (accept) {
					accept = false;
					spUtil.saveBoolean("accept", accept);
					imageView_noty_img
							.setBackgroundResource(R.drawable.noty_no);
					pushNotify = 0;
					updateUserPushNotify(pushNotify);
				} else {
					accept = true;
					spUtil.saveBoolean("accept", accept);
					imageView_noty_img
							.setBackgroundResource(R.drawable.noty_yes);
					pushNotify = 1;
					updateUserPushNotify(pushNotify);
				}
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		layout_update_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 检测新版本升级
				getLatestVersion();
			}
		});
		layout_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 关于
				Intent intent = new Intent(MoreAboutActivity.this,
						AboutUsMessageActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				intent.putExtra("servicePhone", servicePhone);
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});

		// 退出
		layout_login_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSelectDialog();
			}
		});
	}

	private void initView() {
		mInflater = LayoutInflater.from(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		textView_more_update = (TextView) findViewById(R.id.textView_more_update);
		textView_more_about_other = (TextView) findViewById(R.id.textView_more_about_other);
		imageView_noty_img = (ImageView) findViewById(R.id.imageView_noty_img);
		layout_update_check = (RelativeLayout) findViewById(R.id.layout_update_check);
		layout_about = (RelativeLayout) findViewById(R.id.layout_about);
		layout_login_out = (LinearLayout) findViewById(R.id.layout_login_out);

		tv_titlebar_title.setText("更多");
	}

	/**
	 * 获取最新版本和是否强制升级
	 */
	private void getLatestVersion() {
		CommUtil.getLatestVersion(this, Global.getCurrentVersion(this),
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
													Global.getCurrentVersion(MoreAboutActivity.this));
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

	private void showForceInstallDialog(String localVersionHint,
			final String localApkPath, String version) {
		InstallDialog mDialog = new InstallDialog.Builder(this)
				.setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_ALERT).setMessage(localVersionHint)
				.setCancelable(false).setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdateUtil.installAPK(MoreAboutActivity.this, new File(
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
						UpdateUtil.installAPK(MoreAboutActivity.this, new File(
								localApkPath));
					}
				}).build();
		mDialog.show();
	}

	private void showForceUpgradeDialog(String msg, final String path,
			String version) {
		InstallDialog mDialog = new InstallDialog.Builder(
				MoreAboutActivity.this).setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_ALERT).setMessage(msg)
				.setCancelable(false).setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gotoDownload(path);
					}
				}).build();
		mDialog.show();
	}

	private void showUpgradeDialog(String msg, final String path, String version) {
		InstallDialog mDialog = new InstallDialog.Builder(
				MoreAboutActivity.this).setTitle("升级到版本V." + version)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(msg)
				.setCancelStr("残忍拒绝").setOKStr("极速升级")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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
						Utils.getDir(MoreAboutActivity.this), "pet.apk",
						mHandler);
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

	private boolean compareVersion(String serviceVersion, String localVersion) {
		Utils.mLogError("serviceVersion:" + serviceVersion + " localVersion:"
				+ localVersion);
		boolean result = false;
		serviceVersion = serviceVersion.replace(".", "");
		localVersion = localVersion.replace(".", "");

		int flagLength = 0;
		int versionLength = serviceVersion.length() > localVersion.length() ? localVersion
				.length() : serviceVersion.length();
		for (int i = 0; i < versionLength; i++) {
			if (Integer.parseInt(serviceVersion.charAt(i) + "") > Integer
					.parseInt(localVersion.charAt(i) + "")) {
				result = true;
				break;
			} else if (Integer.parseInt(serviceVersion.charAt(i) + "") < Integer
					.parseInt(localVersion.charAt(i) + "")) {
				break;
			} else {
				flagLength = i;
			}
		}
		if (!result
				&& flagLength + 1 == versionLength
				&& serviceVersion.length() > localVersion.length()
				&& 0 < Integer.parseInt(serviceVersion.charAt(versionLength)
						+ "")) {
			result = true;
		}
		return result;
	}

	private void showSelectDialog() {
		MDialog mDialog = new MDialog.Builder(MoreAboutActivity.this)
				.setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("确定退出?").setCancelStr("否").setOKStr("是")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 退出登录
						removeDataSp();
						Intent intent = new Intent();
						intent.setAction("android.intent.action.mainactivity");
						intent.putExtra("previous",
								Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY);
						sendBroadcast(intent);
						finishWithAnimation();
					}
				}).build();
		mDialog.show();
	}

	public void onDismiss() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1f;
		getWindow().setAttributes(lp);
	}

	private void removeDataSp() {
		spUtil.removeData("upShopName");
		spUtil.removeData("upRegionId");
		spUtil.removeData("isCerti");
		spUtil.removeData("city");
		spUtil.removeData("cellphone");
		spUtil.removeData("userid");
		spUtil.removeData("username");
		spUtil.removeData("userimage");
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
		spUtil.removeData("shopid");
		spUtil.removeData("newshopid");
		spUtil.removeData("newaddr");
		spUtil.removeData("newlat");
		spUtil.removeData("newlng");
		spUtil.removeData("invitecode");
	}

	private void updateUserPushNotify(Short pushNotify) {
		CommUtil.updateUserPushNotify(Global.getIMEI(this), this, pushNotify,
				getuiHandler);
	}

	private AsyncHttpResponseHandler getuiHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						boolean acceptBoolean = jobj.getBoolean("data");
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
	private MyNotification mNotification;

}
