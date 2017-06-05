package com.haotang.pet;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.umeng.analytics.MobclickAgent;

public class ChangeAccountActivity extends SuperActivity {

	private PopupWindow pWin;
	private LayoutInflater mInflater;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private RelativeLayout layout_change_icon;
	private CircleImageView imageView_change_icon;
	private String TEMPCERTIFICATIONNAME = "pro.jpg";
	private static final int PHOTO_CERTIFICATION = 100;
	private static final int IMAGE_CERTIFICATION = 101;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private String photoPath;
	private String bmpStr = "";
	private Bitmap bitmap;
	private EditText editText_change_username;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_account);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		initView();
		// 监听
		initListener();
		// 访问网络获取用户服务器上的头像以及用户名并设置
		getDate();
	}

	private void getDate() {// 这里估计需要初始化请求头像接口
		String cellphone = spUtil.getString("cellphone", "");
		int userid = spUtil.getInt("userid", 0);
		if (!"".equals(cellphone) && 0 != userid) {
			CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
					Global.getIMEI(this), Global.getCurrentVersion(this),
					spUtil.getInt("userid", 0), 0, 0, autoLoginHandler);
		}
	}

	private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("获取头像：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("user") && !jData.isNull("user")) {
							JSONObject jUser = jData.getJSONObject("user");
							if (jUser.has("areacode") && !jUser.isNull("areacode")) {
								spUtil.saveInt("upRegionId",
										jUser.getInt("areacode"));
							} else {
								spUtil.removeData("upRegionId");
							}
							if (jUser.has("shopName") && !jUser.isNull("shopName")) {
								spUtil.saveString("upShopName",
										jUser.getString("shopName"));
							} else {
								spUtil.removeData("upShopName");
							}
							if (jUser.has("shopId") && !jUser.isNull("shopId")) {
								spUtil.saveInt("shopid", jUser.getInt("shopId"));
							} else {
								spUtil.removeData("shopid");
							}
							if (jUser.has("areaId") && !jUser.isNull("areaId")) {
								spUtil.saveInt("areaid", jUser.getInt("areaId"));
							} else {
								spUtil.removeData("areaid");
							}
							if (jUser.has("areaName") && !jUser.isNull("areaName")) {
								spUtil.saveString("areaname",
										jUser.getString("areaName"));
							} else {
								spUtil.removeData("areaname");
							}
							if (jUser.has("homeAddress")
									&& !jUser.isNull("homeAddress")) {
								JSONObject jAddr = jUser
										.getJSONObject("homeAddress");
								if (jAddr.has("Customer_AddressId")
										&& !jAddr.isNull("Customer_AddressId")) {
									spUtil.saveInt("addressid",
											jAddr.getInt("Customer_AddressId"));
								}
								if (jAddr.has("lat") && !jAddr.isNull("lat")) {
									spUtil.saveString("lat", jAddr.getDouble("lat")
											+ "");
								}
								if (jAddr.has("lng") && !jAddr.isNull("lng")) {
									spUtil.saveString("lng", jAddr.getDouble("lng")
											+ "");
								}
								if (jAddr.has("address")
										&& !jAddr.isNull("address")) {
									spUtil.saveString("address",
											jAddr.getString("address"));
								}
							} else {
								spUtil.removeData("addressid");
								spUtil.removeData("lat");
								spUtil.removeData("lng");
								spUtil.removeData("address");
							}
							if (jUser.has("pet") && !jUser.isNull("pet")) {
								JSONObject jPet = jUser.getJSONObject("pet");
								if (jPet.has("isCerti") && !jPet.isNull("isCerti")) {
									spUtil.saveInt("isCerti",
											jPet.getInt("isCerti"));
								}
							} else {
								spUtil.removeData("isCerti");
							}
							if (jUser.has("userName")
									&& !jUser.isNull("userName")) {
								editText_change_username.setText(jUser
										.getString("userName"));
							}
							if (jUser.has("avatarPath")
									&& !jUser.isNull("avatarPath")) {
								ImageLoaderUtil
										.loadImg(
												CommUtil.getServiceNobacklash()
														+ jUser.getString("avatarPath"),
												imageView_change_icon, 0, null);
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

	private void initListener() {
		layout_change_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSelectDialog();
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		bt_titlebar_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 访问网络，更新头像以及用户名提交至服务器
				if ("".equals(editText_change_username.getText().toString()
						.trim())
						&& "".equals(bmpStr)) {
					ToastUtil.showToastShort(ChangeAccountActivity.this,
							"请选择头像或填写用户名");
					return;
				}
				spUtil.saveString("username", editText_change_username
						.getText().toString());
				spUtil.saveString("userimage", "file://" + photoPath);
				pDialog.showDialog();
				CommUtil.updateUser(spUtil.getString("cellphone", "0"),
						Global.getIMEI(ChangeAccountActivity.this),
						ChangeAccountActivity.this, spUtil.getInt("userid", 0),
						editText_change_username.getText().toString(), bmpStr,
						updateUser);
				Utils.mLogError("==-->更新头像 "
						+ editText_change_username.getText().toString()
						+ "  bmpStr:= " + bmpStr);
			}
		});
	}

	private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				Utils.mLogError("==-->updateUser" + new String(responseBody));
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("userName")
							&& !jsonObject.isNull("userName")) {
						spUtil.saveString("username",
								jsonObject.getString("userName"));
					}
					if (jsonObject.has("avatarPath")
							&& !jsonObject.isNull("avatarPath")) {
						spUtil.saveString("userimage",
								jsonObject.getString("avatarPath"));
					}
					ToastUtil
							.showToastShort(ChangeAccountActivity.this, "更新成功");
					Intent data = new Intent();
					data.putExtra("username", editText_change_username
							.getText().toString());
					data.putExtra("photopath", "file://" + photoPath);
					setResult(Global.RESULT_OK, data);
					finishWithAnimation();
				} else {
					String msg = jsonObject.getString("msg");
					Toast.makeText(ChangeAccountActivity.this, msg,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			Toast.makeText(ChangeAccountActivity.this, "连接服务器失败,请检查您的网络",
					Toast.LENGTH_SHORT).show();
		}
	};

	private void initView() {
		mInflater = LayoutInflater.from(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);

		layout_change_icon = (RelativeLayout) findViewById(R.id.layout_change_icon);
		imageView_change_icon = (CircleImageView) findViewById(R.id.imageView_change_icon);
		editText_change_username = (EditText) findViewById(R.id.editText_change_username);
		tv_titlebar_title.setText("我的账户");

	}

	private void showSelectDialog() {
		try {
			goneJp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
		LinearLayout pop_getIcon_action = (LinearLayout) view
				.findViewById(R.id.pop_getIcon_action);
		LinearLayout pop_getIcon_local = (LinearLayout) view
				.findViewById(R.id.pop_getIcon_local);
		LinearLayout pop_getIcon_cancle = (LinearLayout) view
				.findViewById(R.id.pop_getIcon_cancle);
		// TODO pop
		if (pWin == null) {
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		}
		pWin.setFocusable(true);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		pWin.setWidth(dm.widthPixels/* - 40 */);
		pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// 拍照
		pop_getIcon_action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				TEMPCERTIFICATIONNAME = System.currentTimeMillis()
						+ "_pro.jpg";
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Utils
								.getPetPath(ChangeAccountActivity.this),
								TEMPCERTIFICATIONNAME)));
				startActivityForResult(intent, PHOTO_CERTIFICATION);
				pWin.dismiss();
				pWin = null;
			}
		});
		// 本地获取图片
		pop_getIcon_local.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
				// null);
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, IMAGE_CERTIFICATION);

				pWin.dismiss();
				pWin = null;
			}
		});
		pop_getIcon_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pWin.dismiss();
				pWin = null;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case IMAGE_CERTIFICATION:
				if (data == null) {
					ToastUtil.showToastShortCenter(ChangeAccountActivity.this,
							"您选择的照片不存在，请重新选择");
					return;
				}

				// Uri uri = data.getData();
				// photoPath =
				// RealPathUtil.getRealPathFromURI(ChangeAccountActivity.this,
				// uri);
				// setPicToImageView(imageView_change_icon,photoPath);
				try {
					Uri originalUri = data.getData(); // 获得图片的uri
					if (!TextUtils.isEmpty(originalUri.getAuthority())) {
						// 这里开始的第二部分，获取图片的路径：
						String[] proj = { MediaStore.Images.Media.DATA };
						// Cursor cursor = managedQuery(originalUri, proj, null,
						// null, null);
						Cursor cursor = getContentResolver().query(originalUri,
								proj, null, null, null);
						// 获得用户选择的图片的索引值
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						// 最后根据索引值获取图片路径
						photoPath = cursor.getString(column_index);

						cursor.close();
						// 华为及其他手机系统的图片Url获取
						compressWithLs(new File(photoPath));
					} else {
						// 小米系统走的方法
						compressWithLs(new File(originalUri.getPath()));
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				break;
			case PHOTO_CERTIFICATION:
				File file = new File(
						Utils.getPetPath(ChangeAccountActivity.this),
						TEMPCERTIFICATIONNAME);
				if (file != null && file.length() > 0) {
					photoPath = file.getAbsolutePath();
					Utils.mLogError("==-->photoPath  PHOTO_CERTIFICATION   := "
							+ photoPath);
					compressWithLs(new File(photoPath));
				} else {
					Toast.makeText(ChangeAccountActivity.this,
							"您选择的照片不存在，请重新选择", Toast.LENGTH_SHORT).show();
				}

				break;
			}
		}
	}

	/**
	 * 压缩单张图片 Listener 方式
	 */
	private void compressWithLs(File file) {
		Luban.get(this).load(file).putGear(Luban.THIRD_GEAR)
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(File file) {
						Bitmap getxtsldraw = Utils.getxtsldraw(
								ChangeAccountActivity.this,
								file.getAbsolutePath());
						imageView_change_icon.setImageBitmap(getxtsldraw);
						bmpStr = Global.encodeWithBase64(getxtsldraw);// 这句话将图片转码后发送给服务器
					}

					@Override
					public void onError(Throwable e) {

					}
				}).launch();
	}

	/**
	 * 压缩单张图片 RxJava 方式
	 */
	private void compressWithRx(File file) {
		Luban.get(this)
				.load(file)
				.putGear(Luban.THIRD_GEAR)
				.asObservable()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError(new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						throwable.printStackTrace();
					}
				})
				.onErrorResumeNext(
						new Func1<Throwable, Observable<? extends File>>() {
							@Override
							public Observable<? extends File> call(
									Throwable throwable) {
								return Observable.empty();
							}
						}).subscribe(new Action1<File>() {
					@Override
					public void call(File file) {
					}
				});
	}

	public enum RealPathUtil {
		INSTANCE;

		public static String getRealPathFromURI(Context context, Uri uri) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				return getRealPathFromURI_BelowAPI11(context, uri);
			} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				return getRealPathFromURI_API11to18(context, uri);
			} else {
				return getRealPathFromURI_API19(context, uri);
			}
		}

		@SuppressLint("NewApi")
		public static String getRealPathFromURI_API19(Context context, Uri uri) {
			String filePath = "";
			String wholeID = "";

			try {
				wholeID = DocumentsContract.getDocumentId(uri);
			} catch (Exception ex) {
				ex.printStackTrace(); // Android 4.4.2 can occur this exception.

				return getRealPathFromURI_API11to18(context, uri);
			}

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			String[] column = { MediaColumns.DATA };

			// where id is equal to
			String sel = BaseColumns._ID + "=?";

			Cursor cursor = context.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
					new String[] { id }, null);

			int columnIndex = cursor.getColumnIndex(column[0]);

			if (cursor.moveToFirst()) {
				filePath = cursor.getString(columnIndex);
			}

			cursor.close();
			return filePath;
		}

		@SuppressLint("NewApi")
		public static String getRealPathFromURI_API11to18(Context context,
				Uri contentUri) {
			String[] proj = { MediaColumns.DATA };
			String result = null;

			CursorLoader cursorLoader = new CursorLoader(context, contentUri,
					proj, null, null, null);
			Cursor cursor = cursorLoader.loadInBackground();

			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				result = cursor.getString(column_index);
			}
			return result;
		}

		public static String getRealPathFromURI_BelowAPI11(Context context,
				Uri contentUri) {
			String[] proj = { MediaColumns.DATA };
			Cursor cursor = context.getContentResolver().query(contentUri,
					proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ChangeAccountActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ChangeAccountActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	
	private void goneJp() {
		// 强制收起键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
