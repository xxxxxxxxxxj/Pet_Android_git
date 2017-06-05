package com.haotang.pet;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ExampleUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends SuperActivity implements OnClickListener {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private EditText et_userid_num, edt_verifkey;
	private Button bt_get, button_login_sure;
	private ImageView cbAgree;
	private String szVerifyKey = "";
	private static final long nVerifyPeriod = 5 * 60 * 1000; // Verify key is
																// valid for 1
	private long nKeyTimeStamp = 0;
	private TimeCount time;
	private int code = 0;
	private Context context;
	private int previous;
	private SharedPreferenceUtil spUtil;
	// ------ 马总新加需求
	private LocationClient mLocationClient;
	private MLocationListener mLocationListener;
	private double lat = 0;
	private double lng = 0;

	private LinearLayout login_all;
	// private MProgressDialog pDialog;
	private Intent fIntent;
	public static Activity act;
	// ------
	Set<String> tagSet = new LinkedHashSet<String>();
	private int orderid;
	private static final int MSG_SET_ALIASANDTAGS = 1001;
	private LinearLayout ll_login_agree;
	private boolean isSelect = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		// pDialog = new MProgressDialog(this);
		context = LoginActivity.this;
		act = this;
		initView();
		initBD();

	}

	private void initView() {
		spUtil = SharedPreferenceUtil.getInstance(LoginActivity.this);
		fIntent = getIntent();
		previous = fIntent.getIntExtra("previous", -1);
		orderid = fIntent.getIntExtra("orderid", -1);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		et_userid_num = (EditText) findViewById(R.id.et_userid_num);
		edt_verifkey = (EditText) findViewById(R.id.edt_verifkey);
		bt_get = (Button) findViewById(R.id.bt_get);
		login_all = (LinearLayout) findViewById(R.id.login_all);
		tvOO = (TextView) findViewById(R.id.tv_oo);
		button_login_sure = (Button) findViewById(R.id.button_login_sure);
		cbAgree = (ImageView) findViewById(R.id.iv_login_agree);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		ll_login_agree = (LinearLayout) findViewById(R.id.ll_login_agree);
		tv_titlebar_title.setText("登	录");
		bt_get.setOnClickListener(this);
		ib_titlebar_back.setOnClickListener(this);
		button_login_sure.setOnClickListener(this);
		ll_login_agree.setOnClickListener(this);
		tvOO.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO 处理监听
		switch (view.getId()) {
		case R.id.ll_login_agree:
			if (isSelect) {
				cbAgree.setBackgroundResource(R.drawable.complaint_reason_disable);
			} else {
				cbAgree.setBackgroundResource(R.drawable.complaint_reason);
			}
			isSelect = !isSelect;
			break;
		case R.id.ib_titlebar_back:
			// 强制收起键盘
			goneJp();
			if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous",
						Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
				sendBroadcast(intent);
				finishWithAnimation();
			} else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finishWithAnimation();
			} else {
				backToFront();
			}
			act = null;
			break;
		case R.id.button_login_sure:// 登录
			String str = "";
			if (!Utils.checkPhone(this, et_userid_num)) {
			} else if (et_userid_num.getText().toString().equals("")
					|| et_userid_num.getText().toString().equals(null)) {
				str = "请输入您的手机号码";
				showToast(this, str);
			} else if (edt_verifkey.getText().toString().equals("")
					|| edt_verifkey.getText().equals(null)) {
				str = "请输入验证码";
				showToast(this, str);
			} else if (!isSelect) {
				str = "请同意并勾选用户协议";
				showToast(this, str);
			} else {
				str = "开启登录";
				// Utils.mLog("==--> 开启登录 := "+
				// et_userid_num.getText().toString()+"   验证码 ： = "+edt_verifkey.getText().toString());
				// pDialog.showDialog();
				button_login_sure.setEnabled(false);
				button_login_sure
						.setBackgroundResource(R.drawable.bg_button_gray_normal);
				CommUtil.loginAu(context, et_userid_num.getText().toString(),
						Global.getIMEI(this),
						edt_verifkey.getText().toString(), lat, lng,
						loginHandler);
				// }
			}
			break;
		case R.id.bt_get:// 获取验证码
			if (Utils.checkPhone(this, et_userid_num)) {
				onClickVerifyKey();
			}
			break;
		case R.id.tv_oo:// 协议
			Intent intent = new Intent(LoginActivity.this,
					AgreementActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			intent.putExtra("previous", Global.LOGIN_TO_AGREEMENT);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 强制收起键盘
		goneJp();
	}

	private void goneJp() {
		// 强制收起键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private AsyncHttpResponseHandler loginHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->登录 := " + new String(responseBody));
			// pDialog.closeDialog();

			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");

				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jData = jobj.getJSONObject("data");
					// if(jData.has("serviceLoc")&&!jData.isNull("serviceLoc")){
					// spUtil.saveInt("serviceloc", jData.getInt("serviceLoc"));
					// }
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
						if (jUser.has("inviteCode")
								&& !jUser.isNull("inviteCode")) {
							spUtil.saveString("invitecode",
									jUser.getString("inviteCode"));
						}
						if (jUser.has("cellPhone")
								&& !jUser.isNull("cellPhone")) {
							spUtil.saveString("cellphone",
									jUser.getString("cellPhone"));
							if (previous == Global.LOGIN_TO_POSTSELECTIONFRAGMENT) {
								spUtil.saveString(
										"LOGIN_TO_POSTSELECTIONFRAGMENT_FLAG",
										"LOGIN_TO_POSTSELECTIONFRAGMENT");
							}
							// 设置推送别名
							setaliasAndTags();
						}
						if (jUser.has("payWay") && !jUser.isNull("payWay")) {
							spUtil.saveInt("payway", jUser.getInt("payWay"));
						}
						if (jUser.has("userName") && !jUser.isNull("userName")) {
							spUtil.saveString("username",
									jUser.getString("userName"));
						}

						if (jUser.has("avatarPath")
								&& !jUser.isNull("avatarPath")) {
							spUtil.saveString("userimage",
									jUser.getString("avatarPath"));
						}
						if (jUser.has("UserId") && !jUser.isNull("UserId")) {
							spUtil.saveInt("userid", jUser.getInt("UserId"));
						} else {
							spUtil.removeData("userid");
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
						if (jUser.has("pet") && !jUser.isNull("pet")) {
							JSONObject jPet = jUser.getJSONObject("pet");
							if (jPet.has("isCerti") && !jPet.isNull("isCerti")) {
								spUtil.saveInt("isCerti",
										jPet.getInt("isCerti"));
							}
							if (jPet.has("PetId") && !jPet.isNull("PetId")) {
								spUtil.saveInt("petid", jPet.getInt("PetId"));
							}

							if (jPet.has("petKind") && !jPet.isNull("petKind")) {
								spUtil.saveInt("petkind",
										jPet.getInt("petKind"));
							}
							if (jPet.has("petName") && !jPet.isNull("petName")) {
								spUtil.saveString("petname",
										jPet.getString("petName"));
							}
							if (jPet.has("avatarPath")
									&& !jPet.isNull("avatarPath")) {
								spUtil.saveString("petimage",
										jPet.getString("avatarPath"));
							}

							if (jPet.has("availService")
									&& !jPet.isNull("availService")) {
								JSONArray jarr = jPet
										.getJSONArray("availService");
								if (jarr.length() > 0) {
									MainActivity.petServices = new int[jarr
											.length()];
									for (int i = 0; i < jarr.length(); i++) {
										MainActivity.petServices[i] = jarr
												.getInt(i);
									}
								}
							}

						} else {
							spUtil.removeData("isCerti");
							spUtil.removeData("petid");
							spUtil.removeData("petKind");
							spUtil.removeData("petname");
							spUtil.removeData("petimage");
						}
						if (jUser.has("myPet") && !jUser.isNull("myPet")) {
							JSONObject jMyPet = jUser.getJSONObject("myPet");
							if (jMyPet.has("CustomerPetId")
									&& !jMyPet.isNull("CustomerPetId")) {
								spUtil.saveInt("customerpetid",
										jMyPet.getInt("CustomerPetId"));
							} else {
								spUtil.removeData("customerpetid");
							}
							if (jMyPet.has("nickName")
									&& !jMyPet.isNull("nickName")) {
								spUtil.saveString("customerpetname",
										jMyPet.getString("nickName"));
							} else {
								spUtil.removeData("customerpetname");
							}
							if (jMyPet.has("avatarPath")
									&& !jMyPet.isNull("avatarPath")) {
								String mypetImage = jMyPet
										.getString("avatarPath");
								if (!mypetImage.startsWith("http://")
										&& !mypetImage.startsWith("https://")) {
									spUtil.saveString(
											"mypetImage",
											CommUtil.getServiceNobacklash()
													+ jMyPet.getString("avatarPath"));
								} else {
									spUtil.saveString("mypetImage",
											jMyPet.getString("avatarPath"));
								}
							} else {
								spUtil.removeData("mypetImage");
							}
						} else {
							spUtil.removeData("customerpetname");
							spUtil.removeData("customerpetid");
							spUtil.removeData("mypetImage");
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
					}

					if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.mainactivity");
						intent.putExtra(
								"previous",
								Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT);
						sendBroadcast(intent);
					} else if (previous == Global.PRE_BOOKINGSERVICEACTIVITY_TO_LOGINACTIVITY
							|| previous == Global.PRE_SERVICEACTIVITY_TO_LOGINACTIVITY) {
						Intent intent = new Intent(LoginActivity.this,
								OrderDetailActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_RIGHT());
						getIntent().putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_LEFT());
						intent.putExtra("previous", previous);
						intent.putExtra("serviceid",
								fIntent.getIntExtra("serviceid", 0));
						intent.putExtra("servicetype",
								fIntent.getIntExtra("servicetype", 0));
						intent.putExtra("totalfee",
								fIntent.getDoubleExtra("totalfee", 0));
						intent.putParcelableArrayListExtra(
								"mulpetservice",
								fIntent.getParcelableArrayListExtra("mulpetservice"));
						intent.putExtra("ps",
								fIntent.getSerializableExtra("ps"));
						startActivity(intent);
						setResult(Global.RESULT_OK);
					} else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
					} else if (previous == Global.PRE_PUSH_TO_EVALUATE) {
						// 登录去评论
						Intent intent = new Intent(LoginActivity.this,
								EvaluateActivity.class);
						intent.putExtra("orderid",
								getIntent().getIntExtra("orderid", 0));
						startActivity(intent);
						finishWithAnimation();
					} else if (previous == Global.FOSTERCARE_TO_LOGIN) {
						// 登录去寄养订单确认
						Intent intent = new Intent(LoginActivity.this,
								FostercareOrderConfirmActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("fostercare",
								fIntent.getSerializableExtra("fostercare"));
						intent.putExtras(bundle);
						startActivity(intent);
						finishWithAnimation();
					} else if (previous == Global.PRE_RECHARGEPAGE_ZF) {
						// Intent intent = new Intent();
						// intent.setAction("android.intent.action.RechargePage");
						// Bundle bundle = new Bundle();
						// bundle.putInt("previous",
						// Global.PRE_RECHARGEPAGE_TO_LOGIN_SUCCESS_ZF);
						// intent.putExtras(bundle);
						// sendBroadcast(intent);
						setResult(Global.RESULT_OK);
					} else if (previous == Global.PRE_RECHARGEPAGE_WX) {
						// Intent intent = new Intent();
						// intent.setAction("android.intent.action.RechargePage");
						// Bundle bundle = new Bundle();
						// bundle.putInt("previous",
						// Global.PRE_RECHARGEPAGE_TO_LOGIN_SUCCESS_WX);
						// intent.putExtras(bundle);
						// sendBroadcast(intent);
						setResult(Global.RESULT_OK);
					} else if (previous == Global.H5_TO_LOGIN) {
						Intent data = new Intent();
						data.putExtra("loginurl",
								getIntent().getStringExtra("loginurl"));
						setResult(Global.RESULT_OK, data);
					} else if (previous == Global.MY_TO_LOGIN) {

						setResult(Global.RESULT_OK);
					} else if (previous == Global.AD_TO_LOGIN) {

						setResult(Global.RESULT_OK);
					} else if (previous == Global.AD_TO_LOGIN_TO_ORDER) {// 登录去订单详情页
						Intent intent = new Intent(context,
								OrderDetailFromOrderToConfirmActivity.class);
						intent.putExtra("previous", 0);
						intent.putExtra("orderid", orderid);
						intent.putExtra("serviceid", 0);
						intent.putExtra("servicetype", 0);
						startActivity(intent);
					} else if (previous == Global.USERMEMBERFRAGMENT_LOGIN) {
						setResult(Global.RESULT_OK);
					} else if (previous == Global.RequestCode_UserMember) {
						setResult(Global.RESULT_OK);
					} else if (previous == Global.MIPCA_TO_ORDERPAY) {
						startActivity(new Intent(mContext,
								OrderPayActivity.class).putExtra("codeResult",
								getIntent().getStringExtra("codeResult"))
								.putExtra("previous", Global.MIPCA_TO_ORDERPAY));
					}
					finishWithAnimation();
					act = null;
				} else {
					// 登录失败 获取服务器提示信息展示给用户
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						showToast(LoginActivity.this, msg);
					}
					button_login_sure.setEnabled(true);
					button_login_sure
							.setBackgroundResource(R.drawable.bg_button_orange);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				// pDialog.closeDialog();
				showToast(LoginActivity.this, "网络异常，请重新登录");
				button_login_sure.setEnabled(true);
				button_login_sure
						.setBackgroundResource(R.drawable.bg_button_orange);
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			// handler.sendEmptyMessage(3);
			// pDialog.closeDialog();
			showToast(LoginActivity.this, "网络异常，请重新登录");
			button_login_sure.setEnabled(true);
			button_login_sure
					.setBackgroundResource(R.drawable.bg_button_orange);
		}

	};

	private void onClickVerifyKey() {
		// TODO 获取验证码 目前只判断控件，并未请求网络
		String str = "手机号码不能为空";
		if (!Utils.checkNet(this)) {
		} else if (et_userid_num.getText().toString().equals("")) {
			str = "手机号码不能为空";
			showToast(LoginActivity.this, str);
			et_userid_num.requestFocus();
			et_userid_num.selectAll();
			return;
		} else if (et_userid_num.getText().toString().length() != 11
				|| !TextUtils.isDigitsOnly(et_userid_num.getText().toString())) {
			str = "手机号码需要11位的数字";
			showToast(LoginActivity.this, str);
			et_userid_num.requestFocus();
			et_userid_num.selectAll();
			return;
		} else {
			bt_get.setEnabled(false);
			bt_get.setBackgroundResource(R.drawable.btn_noround_gray);
			CommUtil.genVerifyCode(this, et_userid_num.getText().toString(),
					codeHandler);
		}
	}

	private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("验证码：" + new String(responseBody));
			try {
				JSONObject jsonObj = new JSONObject(new String(responseBody));
				code = jsonObj.getInt("code");
				if (code == 0) {
					time.start();// 开始计时,并同时请求网络获取验证码
					edt_verifkey.setFocusable(true);
					edt_verifkey.setFocusableInTouchMode(true);
					edt_verifkey.requestFocus();
				} else {
					bt_get.setBackgroundResource(R.drawable.btn_picked);
					bt_get.setText("重新获取");
					bt_get.setClickable(true);
					bt_get.setEnabled(true);
					// 获取验证码失败 获取服务器提示信息展示给用户
					if (jsonObj.has("msg") && !jsonObj.isNull("msg")) {
						ToastUtil.showToastShort(LoginActivity.this,
								jsonObj.getString("msg"));
					}
				}
			} catch (JSONException e) {
				bt_get.setBackgroundResource(R.drawable.btn_picked);
				bt_get.setText("重新获取");
				bt_get.setClickable(true);
				bt_get.setEnabled(true);
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			bt_get.setBackgroundResource(R.drawable.btn_picked);
			bt_get.setText("重新获取");
			bt_get.setClickable(true);
			bt_get.setEnabled(true);

		}

	};
	private TextView tvOO;
	private InputMethodManager imm;

	private void showToast(Activity cls, String str) {
		ToastUtil.showToastShortCenter(this, str);
	}

	// 返回上层
	private void backToFront() {
		finishWithAnimation();
	}

	protected String validateVerifyKey() {

		String retVal = "";

		String key = edt_verifkey.getText().toString().trim();
		if ("8888".equals(key)) {
			// img_verify_match.setImageResource(R.drawable.newicon_checked);
		} else {
			if (key.equals(szVerifyKey)) {
				long nCurStamp = Calendar.getInstance().getTimeInMillis();
				if (nKeyTimeStamp != 0
						&& nCurStamp - nKeyTimeStamp < nVerifyPeriod) // Input
				{
					// img_verify_match.setImageResource(R.drawable.newicon_checked);
				} else {
					// img_verify_match.setImageResource(R.drawable.newicon_unchecked);
					if (nKeyTimeStamp == 0) {
						retVal = "还没有获取验证码";
					} else {
						retVal = "验证码超时了,重新获取";
					}
				}
			} else {
				// img_verify_match.setImageResource(R.drawable.newicon_unchecked);
				retVal = "验证码超时了,重新获取";
			}
		}

		return retVal;

	}

	// 注册计时器
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			bt_get.setBackgroundResource(R.drawable.btn_picked);
			bt_get.setText("重新获取");
			bt_get.setClickable(true);
			bt_get.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			bt_get.setClickable(false);
			bt_get.setText(millisUntilFinished / 1000 + "秒");
			bt_get.setEnabled(false);
		}
	}

	/**
	 * 秒转换为时间格式
	 * 
	 * @param second
	 * @return 时：分：秒
	 */
	public String SecondToTime(int second) {
		String time = null;
		int s = 00, m = 00, h = 00, d = 00;// 秒，分，时，天；
		/* s = second % 60; */
		m = second / 60 % 60;
		h = second / 3600;
		// d = second/3600/24;
		String S = s + "", M = m + "", H = h + "";
		/*
		 * if (s < 10) { S = "0" + S; }
		 */
		if (m < 10) {
			M = "0" + M;
		}
		if (h < 10) {
			H = "0" + H;
		}

		time = /* H + ":" + */M + ":" + S;

		return time;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous",
						Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
				sendBroadcast(intent);
				finishWithAnimation();
			} else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finishWithAnimation();
			}
		}
		act = null;
		return super.onKeyDown(keyCode, event);
	}

	private void initBD() {
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(this);
		mLocationListener = new MLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(100);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private class MLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			lat = location.getLatitude();// 纬度
			lng = location.getLongitude();// 经度
			mLocationClient.stop();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("LoginActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
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
}
