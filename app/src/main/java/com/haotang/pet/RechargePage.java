package com.haotang.pet;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.Rechar;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.umeng.analytics.MobclickAgent;

/**
 * 充值卡跳转支付界面
 * 
 * @author zhiqiang
 * 
 */
public class RechargePage extends SuperActivity {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView textView_rechar_price;
	private LinearLayout layout_showrech_message;
	private LinearLayout layout_showrech_make_use;
	private ImageView imageView_choose_rech_weixin;
	private ImageView imageView_choose_rech_zhifubao;
	private Button button_rech_to_pay;
	private Rechar rechar;
	private boolean isWx = true;
	private boolean isZf = false;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	public static RechargePage rechargePage;
	private LinearLayout layout_yaoqing;
	private ImageView iv_recharge_yaoqing;
	private EditText et_recharge_yaoqing;
	private TextView textview_recharge_hot;
	private TextView textview_recharge_activity;
	private boolean isFromSpread = false;
	private TextView textview_show_left;
	private TextView textview_surplus;
	private TextView textview_day;
	public static String rechargeAmount = "0";
	private int tempType;
	private TextView tv_recharge_into;
	private LinearLayout ll_recharge_payway;
	private LinearLayout ll_recharge_paywaytwo;
	private RelativeLayout ll_recharge_dhm;
	private RelativeLayout rl_recharge_dhmts;
	private EditText et_recharge_dhm;
	private ImageView iv_recharge_dhm;
	private RelativeLayout rlPayali;
	private RelativeLayout rlPaywx;
	protected String dhm;
	protected int amount;
	// 第三方支付相关字段
	private String orderStr;
	private String appid;
	private String noncestr;
	private String packageValue;
	private String partnerid;
	private String prepayid;
	private String sign;
	private String timestamp;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Utils.mLogError("支付宝返回码：" + msg.what);
			switch (msg.what) {
			case Global.ALI_SDK_PAY_FLAG:
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				Utils.mLogError("支付宝返回码：" + resultStatus);
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// UmengStatistics.UmengEventStatistics(rechargePage,Global.UmengEventID.click_ChargePaySucess);//
					// 充值成功
					sendToMyLastMoney();
					mFinish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.showToastShort(RechargePage.this, "支付结果确认中!");
					} else if (TextUtils.equals(resultStatus, "6001")) {

					} else {
						ToastUtil.showToastShort(RechargePage.this,
								"支付失败,请重新支付!");
					}
				}
				break;
			case Global.ALI_SDK_CHECK_FLAG:
				if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
					// 支付宝支付
					pDialog.showDialog();
					PayUtils.payByAliPay(RechargePage.this, orderStr, mHandler,
							pDialog);
				} else {
					ToastUtil.showToastShortBottom(RechargePage.this, "支付参数错误");
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rechargepage);
		rechargePage = this;
		pDialog = new MProgressDialog(this);
		getIntentFrom();
		findView();
		setView();
		initListener();
	}

	private void getIntentFrom() {
		try {
			rechargeAmount = "0";
			Bundle bundle = getIntent().getExtras();
			rechar = (Rechar) bundle.getSerializable("rechar");
			rechargeAmount = rechar.memberGrowthValue;
			Utils.mLogError("==-->rechargeAmount " + rechargeAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageView_choose_rech_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPayByWX();

			}
		});
		rlPaywx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPayByWX();

			}
		});
		imageView_choose_rech_zhifubao
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setPayByAli();
					}
				});
		rlPayali.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPayByAli();
			}
		});
		// start 2016年11月22日15:02:12
		et_recharge_yaoqing.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 4) {
					CommUtil.checkRechargeInviteCode(spUtil.getString(
							"cellphone", ""), mContext, s.toString().trim(),
							checkRechargeInviteCode);
				} else {
					isFromSpread = false;
					iv_recharge_yaoqing
							.setBackgroundResource(R.drawable.complaint_reason_disable);
				}
			}
		});
		// end 2016年11月22日15:02:38
		button_rech_to_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tempType == 2) {
					pDialog.showDialog();
					CommUtil.rechargeWithCard(
							spUtil.getString("cellphone", ""), dhm,
							RechargePage.this, rechargeWithCardHanler);
				} else {
					// UmengStatistics.UmengEventStatistics(rechargePage,Global.UmengEventID.click_ChargeToPay);
					if (!TextUtils.isEmpty(et_recharge_yaoqing.getText())) {
						if (et_recharge_yaoqing.getText().toString().length() < 4
								|| !isFromSpread) {
							ToastUtil.showToastShortCenter(mContext,
									"请填写正确的邀请码！");
							return;
						}
					}
					if (isWx) {
						if (checklogin()) {
							button_rech_to_pay.setEnabled(false);
							getPayId(rechar.RechargeTemplateId, 1);
						} else {
							JumpToNext(LoginActivity.class,
									Global.PRE_RECHARGEPAGE_WX);
						}
					} else if (isZf) {
						// 支付宝支付
						if (checklogin()) {
							button_rech_to_pay.setEnabled(false);
							getPayId(rechar.RechargeTemplateId, 2);
						} else {
							JumpToNext(LoginActivity.class,
									Global.PRE_RECHARGEPAGE_ZF);
						}
					}
				}
			}
		});
	}

	private AsyncHttpResponseHandler rechargeWithCardHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					sendToMyLastMoney();
					finish();
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(RechargePage.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(RechargePage.this, "网络异常，请重新提交");
		}
	};

	public void mFinish() {
		setResult(Global.RESULT_OK);
		finish();
	}

	private void setPayByAli() {
		if (!isZf) {
			isZf = true;
			imageView_choose_rech_zhifubao
					.setBackgroundResource(R.drawable.complaint_reason);
			isWx = false;
			imageView_choose_rech_weixin
					.setBackgroundResource(R.drawable.complaint_reason_disable);
		}
	}

	private void setPayByWX() {
		if (!isWx) {
			isWx = true;
			imageView_choose_rech_weixin
					.setBackgroundResource(R.drawable.complaint_reason);
			isZf = false;
			imageView_choose_rech_zhifubao
					.setBackgroundResource(R.drawable.complaint_reason_disable);
		}
	}

	private void findView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		textView_rechar_price = (TextView) findViewById(R.id.textView_rechar_price);
		layout_showrech_message = (LinearLayout) findViewById(R.id.layout_showrech_message);
		layout_showrech_make_use = (LinearLayout) findViewById(R.id.layout_showrech_make_use);
		imageView_choose_rech_weixin = (ImageView) findViewById(R.id.imageView_choose_rech_weixin);
		imageView_choose_rech_zhifubao = (ImageView) findViewById(R.id.imageView_choose_rech_zhifubao);
		button_rech_to_pay = (Button) findViewById(R.id.button_rech_to_pay);
		rlPayali = (RelativeLayout) findViewById(R.id.rl_rechargepage_ali);
		rlPaywx = (RelativeLayout) findViewById(R.id.rl_rechargepage_wx);
		tv_recharge_into = (TextView) findViewById(R.id.tv_recharge_into);
		ll_recharge_payway = (LinearLayout) findViewById(R.id.ll_recharge_payway);
		ll_recharge_paywaytwo = (LinearLayout) findViewById(R.id.ll_recharge_paywaytwo);
		ll_recharge_dhm = (RelativeLayout) findViewById(R.id.ll_recharge_dhm);
		rl_recharge_dhmts = (RelativeLayout) findViewById(R.id.rl_recharge_dhmts);
		et_recharge_dhm = (EditText) findViewById(R.id.et_recharge_dhm);
		iv_recharge_dhm = (ImageView) findViewById(R.id.iv_recharge_dhm);
		layout_yaoqing = (LinearLayout) findViewById(R.id.layout_yaoqing);
		iv_recharge_yaoqing = (ImageView) findViewById(R.id.iv_recharge_yaoqing);
		et_recharge_yaoqing = (EditText) findViewById(R.id.et_recharge_yaoqing);
		textview_recharge_hot = (TextView) findViewById(R.id.textview_recharge_hot);
		textview_recharge_activity = (TextView) findViewById(R.id.textview_recharge_activity);
		textview_show_left = (TextView) findViewById(R.id.textview_show_left);
		textview_surplus = (TextView) findViewById(R.id.textview_surplus);
		textview_day = (TextView) findViewById(R.id.textview_day);
		bankCardNumAddSpace(et_recharge_dhm);
		spUtil = SharedPreferenceUtil.getInstance(this);
		tempType = rechar.tempType;
		tv_recharge_into.setText(rechar.caption);
		if (tempType == 3 || tempType == 4) {// 时间段活动 / 个数活动
			textview_recharge_activity.setText(rechar.activityTitle);
			ll_recharge_dhm.setVisibility(View.GONE);
			rl_recharge_dhmts.setVisibility(View.GONE);
			ll_recharge_paywaytwo.setVisibility(View.VISIBLE);
			if (rechar.isHot == 1) {
				textview_recharge_hot.setVisibility(View.VISIBLE);
			} else {
				textview_recharge_hot.setVisibility(View.GONE);
			}
			textview_recharge_hot.setBackgroundDrawable(Utils.getDW("FF0000"));
			if (tempType == 3) {
				if (rechar.surplus.length() > 0) {
					textview_show_left.setVisibility(View.VISIBLE);
					textview_surplus.setVisibility(View.VISIBLE);
					textview_surplus.setText(rechar.surplus.replace("天", ""));
					textview_day.setText("天)");
					textview_day.setVisibility(View.VISIBLE);
				} else {
					textview_show_left.setVisibility(View.GONE);
					textview_surplus.setVisibility(View.GONE);
					textview_day.setVisibility(View.GONE);
				}
			} else if (tempType == 4) {
				if (rechar.activityNum > 0) {
					textview_show_left.setVisibility(View.VISIBLE);
					textview_surplus.setVisibility(View.VISIBLE);
					textview_surplus.setText(rechar.surplus.replace("个", ""));
					textview_day.setText("个)");
					textview_day.setVisibility(View.VISIBLE);
				} else {
					textview_show_left.setVisibility(View.GONE);
					textview_surplus.setVisibility(View.GONE);
					textview_day.setVisibility(View.GONE);
				}
			}
		} else if (tempType == 1) {
			if (rechar.isHot == 1) {
				textview_recharge_hot.setVisibility(View.VISIBLE);
			} else {
				textview_recharge_hot.setVisibility(View.GONE);
			}
			textview_recharge_hot.setBackgroundDrawable(Utils.getDW("FF0000"));
			textview_recharge_activity.setText(rechar.title);
			ll_recharge_dhm.setVisibility(View.GONE);
			rl_recharge_dhmts.setVisibility(View.GONE);
			ll_recharge_paywaytwo.setVisibility(View.VISIBLE);
			textview_show_left.setVisibility(View.GONE);
			textview_surplus.setVisibility(View.GONE);
			textview_day.setVisibility(View.GONE);
		} else if (tempType == 2) {
			if (rechar.isHot == 1) {
				textview_recharge_hot.setVisibility(View.VISIBLE);
			} else {
				textview_recharge_hot.setVisibility(View.GONE);
			}
			textview_recharge_hot.setBackgroundDrawable(Utils.getDW("FF0000"));
			layout_yaoqing.setVisibility(View.GONE);
			ll_recharge_payway.setVisibility(View.GONE);
			textview_recharge_activity.setText(rechar.recharge);
			tv_recharge_into.setText("礼品卡介绍");
			button_rech_to_pay.setText("立即充值");
			textview_show_left.setVisibility(View.GONE);
			textview_surplus.setVisibility(View.GONE);
			textview_day.setVisibility(View.GONE);
		}
	}

	/**
	 * 银行卡四位加空格
	 * 
	 * @param mEditText
	 */
	protected void bankCardNumAddSpace(final EditText mEditText) {
		mEditText.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;
			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 19) {
					dhm = buffer.toString().trim().replaceAll(" ", "");
					checkRedeemCode();
					return;
				} else {
					button_rech_to_pay.setText("立即充值");
					button_rech_to_pay.setEnabled(false);
					iv_recharge_dhm
							.setBackgroundResource(R.drawable.complaint_reason_disable);
				}
				if (isChanged) {
					location = mEditText.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}
					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						if ((index == 4 || index == 9 || index == 14 || index == 19)) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}
					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}
					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}
					mEditText.setText(str);
					Editable etable = mEditText.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}

	private void checkRedeemCode() {
		pDialog.showDialog();
		CommUtil.checkDhmCode(spUtil.getString("cellphone", ""), dhm, this,
				checkDhmCodeHanler);
	}

	private AsyncHttpResponseHandler checkDhmCodeHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("校验兑换码：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					button_rech_to_pay.setEnabled(true);
					iv_recharge_dhm
							.setBackgroundResource(R.drawable.complaint_reason);
					JSONObject jdata = null;
					if (jobj.has("data") && !jobj.isNull("data")) {
						jdata = jobj.getJSONObject("data");
						if (jdata.has("amount") && !jdata.isNull("amount")) {
							amount = jdata.getInt("amount");
							button_rech_to_pay.setText("立即充值    " + amount
									+ "元");
							rechargeAmount = amount + "";
						}
					}
				} else {
					button_rech_to_pay.setText("立即充值");
					button_rech_to_pay.setEnabled(false);
					iv_recharge_dhm
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(RechargePage.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
				button_rech_to_pay.setText("立即充值");
				button_rech_to_pay.setEnabled(false);
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			button_rech_to_pay.setEnabled(false);
			pDialog.closeDialog();
			ToastUtil.showToastShort(RechargePage.this, "网络异常，请重新提交");
		}
	};

	private void setView() {
		tv_titlebar_title.setText("充值详情");
		textView_rechar_price.setText(rechar.recharge);
		List<String> listD = rechar.listDesc;
		for (int i = 0; i < listD.size(); i++) {
			TextView tView = new TextView(this);
			tView.setText(listD.get(i));
			tView.setTextColor(Color.parseColor("#BB996C"));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;
			params.bottomMargin = 10;
			tView.setLayoutParams(params);
			layout_showrech_make_use.addView(tView);
		}
		List<String> listI = rechar.listIntro;
		for (int i = 0; i < listI.size(); i++) {
			TextView tView = new TextView(this);
			tView.setText(listI.get(i));
			tView.setTextColor(Color.parseColor("#666666"));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;
			params.bottomMargin = 10;
			tView.setLayoutParams(params);
			layout_showrech_message.addView(tView);
		}
	}

	/*
	 * check log in or not
	 */
	private boolean checklogin() {
		boolean ifLogin = false;
		String str = SharedPreferenceUtil.getInstance(RechargePage.this)
				.getString("cellphone", "0");
		if (str.equals("0")) {
			ifLogin = false;
		} else {
			ifLogin = true;
		}
		return ifLogin;
	}

	private void JumpToNext(Class clazz, int which) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", which);
		startActivityForResult(intent, which);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.PRE_RECHARGEPAGE_ZF) {
				Utils.mLogError("-------------支付宝--");
				getPayId(rechar.RechargeTemplateId, 2);
			} else if (requestCode == Global.PRE_RECHARGEPAGE_WX) {
				Utils.mLogError("-------------微信--");
				getPayId(rechar.RechargeTemplateId, 1);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		rechargePage = null;
		rechargeAmount = "";
	}

	/**
	 * 支付前调用当前接口生成请求id
	 * 
	 * @param templateId
	 * @param payWay
	 */
	private void getPayId(int templateId, int payWay) {
		if (isFromSpread) {
			CommUtil.recharge(spUtil.getString("cellphone", ""),
					Global.getIMEI(this), this, templateId, payWay,
					et_recharge_yaoqing.getText().toString(), recharge);
		} else {
			CommUtil.recharge(spUtil.getString("cellphone", ""),
					Global.getIMEI(this), this, templateId, payWay, null,
					recharge);
		}
	}

	private AsyncHttpResponseHandler recharge = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			appid = null;
			noncestr = null;
			packageValue = null;
			partnerid = null;
			prepayid = null;
			sign = null;
			timestamp = null;
			orderStr = null;
			button_rech_to_pay.setEnabled(true);
			Utils.mLogError("充值返回：" + new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("payInfo") && !object.isNull("payInfo")) {
							JSONObject jpayInfo = object
									.getJSONObject("payInfo");
							if (jpayInfo.has("appid")
									&& !jpayInfo.isNull("appid")) {
								appid = jpayInfo.getString("appid");
							}
							if (jpayInfo.has("noncestr")
									&& !jpayInfo.isNull("noncestr")) {
								noncestr = jpayInfo.getString("noncestr");
							}
							if (jpayInfo.has("package")
									&& !jpayInfo.isNull("package")) {
								packageValue = jpayInfo.getString("package");
							}
							if (jpayInfo.has("partnerid")
									&& !jpayInfo.isNull("partnerid")) {
								partnerid = jpayInfo.getString("partnerid");
							}
							if (jpayInfo.has("prepayid")
									&& !jpayInfo.isNull("prepayid")) {
								prepayid = jpayInfo.getString("prepayid");
							}
							if (jpayInfo.has("sign")
									&& !jpayInfo.isNull("sign")) {
								sign = jpayInfo.getString("sign");
							}
							if (jpayInfo.has("timestamp")
									&& !jpayInfo.isNull("timestamp")) {
								timestamp = jpayInfo.getString("timestamp");
							}
							if (jpayInfo.has("orderStr")
									&& !jpayInfo.isNull("orderStr")) {
								orderStr = jpayInfo.getString("orderStr");
							}
							if (isWx) {
								if (appid != null && !TextUtils.isEmpty(appid)
										&& noncestr != null
										&& !TextUtils.isEmpty(noncestr)
										&& packageValue != null
										&& !TextUtils.isEmpty(packageValue)
										&& partnerid != null
										&& !TextUtils.isEmpty(partnerid)
										&& prepayid != null
										&& !TextUtils.isEmpty(prepayid)
										&& sign != null
										&& !TextUtils.isEmpty(sign)
										&& timestamp != null
										&& !TextUtils.isEmpty(timestamp)) {
									// 微信支付
									pDialog.showDialog();
									PayUtils.weChatPayment(RechargePage.this,
											appid, partnerid, prepayid,
											packageValue, noncestr, timestamp,
											sign, pDialog);
								} else {
									ToastUtil.showToastShortBottom(
											RechargePage.this, "支付参数错误");
								}
							} else if (isZf) {
								if (orderStr != null
										&& !TextUtils.isEmpty(orderStr)) {
									// 判断是否安装支付宝
									PayUtils.checkAliPay(RechargePage.this,
											mHandler);
								} else {
									ToastUtil.showToastShortBottom(
											RechargePage.this, "支付参数错误");
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
			button_rech_to_pay.setEnabled(true);
			ToastUtil
					.showToastShortCenter(RechargePage.this, "服务器连接失败,请检查您的网络");
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RechargePageActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RechargePageActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private AsyncHttpResponseHandler checkRechargeInviteCode = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->充值前校验邀请码" + new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					isFromSpread = true;
					iv_recharge_yaoqing
							.setBackgroundResource(R.drawable.complaint_reason);
					Utils.goneJP(mContext);
				} else if (code == 10) {
					isFromSpread = false;
					iv_recharge_yaoqing
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					if (object.has("msg") && !object.isNull("msg")) {
						String msg = object.getString("msg");
						ToastUtil.showToastShortCenter(mContext, msg);
					}
				} else {
					isFromSpread = false;
					iv_recharge_yaoqing
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					if (object.has("msg") && !object.isNull("msg")) {
						String msg = object.getString("msg");
						ToastUtil.showToastShortCenter(mContext, msg);
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

	private void sendToMyLastMoney() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MyLastMoney");
		intent.putExtra("money", rechargeAmount);
		Utils.mLogError("==-->rechargeAmount支付宝发送通知：" + rechargeAmount);
		sendBroadcast(intent);
		Utils.mLogError("==-->支付宝充值完成发广播了");
	}
}
