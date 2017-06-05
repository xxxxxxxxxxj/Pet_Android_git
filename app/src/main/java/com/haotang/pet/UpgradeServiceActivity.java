package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.UpgradeServiceAdapter;
import com.haotang.pet.entity.UpgradeItem;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.MListview;

/**
 * <p>
 * Title:UpgradeServiceActivity
 * </p>
 * <p>
 * Description:订单升级收银台
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-3-31 下午5:31:37
 */
public class UpgradeServiceActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private MListview lvList;
	private TextView tvTotalFee;
	private TextView tvBlance;
	private RelativeLayout rlBlancePay;
	private RelativeLayout rlWXPay;
	private RelativeLayout rlAliPay;
	private ImageView ivBlancepayIcon;
	private ImageView ivWXpayIcon;
	private ImageView ivAlipayIcon;
	private Button btPay;
	private ArrayList<UpgradeItem> upgradeItemsList = new ArrayList<UpgradeItem>();
	private PullToRefreshScrollView prsScrollView;
	private LinearLayout llMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private Button btRefresh;
	private UpgradeServiceAdapter adapter;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private int orderid;
	private double totalfee;
	private double needfee;
	private double balance;
	private int paytype;
	private long customerId;
	private int upgradeorderid;
	private boolean isRecharge;// 是否可以充值
	private boolean isRechargeReturn;// 是否是充值返回刷新余额
	public static UpgradeServiceActivity act;
	private StringBuilder listpayWays = new StringBuilder();
	private TextView tvBalanceHint;
	// 第三方支付相关字段
	private String orderStr;
	private String appid;
	private String noncestr;
	private String packageValue;
	private String partnerid;
	private String prepayid;
	private String sign;
	private String timestamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgradeservice);
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_upgradeservice_main);
		lvList = (MListview) findViewById(R.id.lv_upgradeservice_serviceitems);
		tvTotalFee = (TextView) findViewById(R.id.tv_upgradeservice_totalfee);
		tvBlance = (TextView) findViewById(R.id.tv_upgradeservice_balance);
		rlBlancePay = (RelativeLayout) findViewById(R.id.rl_upgradeservice_balance);
		rlWXPay = (RelativeLayout) findViewById(R.id.rl_upgradeservice_paywx);
		rlAliPay = (RelativeLayout) findViewById(R.id.rl_upgradeservice_payali);
		ivBlancepayIcon = (ImageView) findViewById(R.id.iv_upgradeservice_balance);
		ivWXpayIcon = (ImageView) findViewById(R.id.iv_upgradeservice_paywx);
		ivAlipayIcon = (ImageView) findViewById(R.id.iv_upgradeservice_payali);
		tvBalanceHint = (TextView) findViewById(R.id.tv_orderdetail_balance_hint);
		btPay = (Button) findViewById(R.id.bt_upgradeservice_pay);
		llMain = (LinearLayout) findViewById(R.id.ll_upgradeservice_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);

	}

	private void setView() {
		tvTitle.setText("订单升级");
		tvMsg1.setText("网络异常或获取数据失败");
		orderid = getIntent().getIntExtra("orderid", 0);
		btRefresh.setVisibility(View.VISIBLE);
		rlBlancePay.setOnClickListener(this);
		ibBack.setOnClickListener(this);
		rlAliPay.setOnClickListener(this);
		rlWXPay.setOnClickListener(this);
		btPay.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		prsScrollView.setMode(Mode.PULL_FROM_START);
		prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					getData();
				}
			}
		});
		adapter = new UpgradeServiceAdapter(this, upgradeItemsList);
		lvList.setAdapter(adapter);
		getBalance();
		getData();
		CommUtil.payWays(this, "update", 0, payWaysHandler);
	}

	private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("payWays") && !jData.isNull("payWays")) {
							JSONArray jsonArray = jData.getJSONArray("payWays");
							if (jsonArray.length() > 0) {
								listpayWays.setLength(0);
								for (int i = 0; i < jsonArray.length(); i++) {
									listpayWays.append(jsonArray.getString(i));
								}
								if (listpayWays.toString().contains("1")) {// 微信
									rlWXPay.setVisibility(View.VISIBLE);
								} else {
									rlWXPay.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("2")) {// 支付宝
									rlAliPay.setVisibility(View.VISIBLE);
								} else {
									rlAliPay.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("4")) {// 余额
									rlBlancePay.setVisibility(View.VISIBLE);
								} else {
									rlBlancePay.setVisibility(View.GONE);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {

		}
	};

	private void showMain(boolean flag) {
		if (flag) {
			llMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			llMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void setLastPrice(double lastPrice) {
		String text = "合计: ¥ " + Utils.formatDouble(lastPrice, 2);
		SpannableString ss = new SpannableString(text);
		ss.setSpan(
				new ForegroundColorSpan(getResources()
						.getColor(R.color.a666666)), 0, "合计: ".length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		ss.setSpan(new TextAppearanceSpan(this, R.style.foster_style_y), 0,
				"合计: ".length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tvTotalFee.setText(ss);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.bt_null_refresh:
			getBalance();
			getData();
			break;
		case R.id.rl_upgradeservice_balance:
			if (isRecharge) {
				// 余额不足，可以去充值
				goNextForData(
						MyLastMoney.class,
						Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY);
			} else {
				// 余额支付
				if (paytype != 4) {
					paytype = 4;
					ivWXpayIcon
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAlipayIcon
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBlancepayIcon
							.setBackgroundResource(R.drawable.complaint_reason);
					needfee = totalfee;
					setLastPrice(needfee);
				}
			}
			break;
		case R.id.rl_upgradeservice_payali:
			// 选择支付宝支付
			if (paytype != 2) {
				paytype = 2;
				ivWXpayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAlipayIcon.setBackgroundResource(R.drawable.complaint_reason);
				ivBlancepayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				needfee = totalfee;
				setLastPrice(needfee);
			}
			break;
		case R.id.rl_upgradeservice_paywx:
			// 选择微信支付
			if (paytype != 1) {
				paytype = 1;
				ivWXpayIcon.setBackgroundResource(R.drawable.complaint_reason);
				ivAlipayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBlancepayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				needfee = totalfee;
				setLastPrice(needfee);
			}
			break;
		case R.id.bt_upgradeservice_pay:
			sendMsgToPay();
			break;
		}
	}

	private void goNextForData(Class clazz, int requestcode) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		startActivityForResult(intent, requestcode);
	}

	private void showNeedFee() {
		if (paytype == 2) {
			paytype = 2;
			ivWXpayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivBlancepayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivAlipayIcon.setBackgroundResource(R.drawable.complaint_reason);
		} else if (paytype == 1) {
			paytype = 1;
			ivWXpayIcon.setBackgroundResource(R.drawable.complaint_reason);
			ivAlipayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivBlancepayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
		} else {
			paytype = 4;
			ivWXpayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivAlipayIcon
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivBlancepayIcon.setBackgroundResource(R.drawable.complaint_reason);
		}
		if (balance >= totalfee) {
			needfee = totalfee;
			setBalanceAvaiable(true);
			if (isRechargeReturn) {
				paytype = 4;
				ivWXpayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAlipayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBlancepayIcon
						.setBackgroundResource(R.drawable.complaint_reason);
			}
		} else {
			setBalanceAvaiable(false);
			needfee = totalfee;
			if (paytype == 4) {
				paytype = 1;
				ivWXpayIcon.setBackgroundResource(R.drawable.complaint_reason);
				ivAlipayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBlancepayIcon
						.setBackgroundResource(R.drawable.complaint_reason_disable);
			}
		}
		setLastPrice(needfee);
	}

	private void setBalanceAvaiable(boolean flag) {
		if (flag) {
			isRecharge = false;
			ivBlancepayIcon.setVisibility(View.VISIBLE);
			tvBalanceHint.setVisibility(View.GONE);
		} else {
			isRecharge = true;
			ivBlancepayIcon.setVisibility(View.GONE);
			tvBalanceHint.setVisibility(View.VISIBLE);
		}
	}

	private void getData() {
		upgradeItemsList.clear();
		totalfee = 0;
		needfee = 0;
		adapter.notifyDataSetChanged();
		pDialog.showDialog();
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderid, orderdetailHanlder);
	}

	/**
	 * 获取账户余额
	 */
	private void getBalance() {
		CommUtil.getAccountBalance(this, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				balanceHanler);
	}

	private void sendMsgToPay() {
		pDialog.showDialog();
		CommUtil.changeOrderPayMethodV2(null,
				spUtil.getString("cellphone", ""), Global.getIMEI(this), this,
				upgradeorderid, customerId, null, null, null, -1, -1, -1,
				Utils.formatDouble(needfee, 2), paytype, 0, -1, null, false,
				null, payHanler);
	}

	private AsyncHttpResponseHandler orderdetailHanlder = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("升级订单 " + new String(responseBody));
			prsScrollView.onRefreshComplete();
			pDialog.closeDialog();
			showMain(true);
			try {
				JSONObject obj = new JSONObject(new String(responseBody));
				int code = obj.getInt("code");
				if (code == 0 && obj.has("data") && !obj.isNull("data")) {
					JSONObject jobj = obj.getJSONObject("data");
					if (jobj.has("petServicePojo")
							&& !jobj.isNull("petServicePojo")) {
						JSONObject jservice = jobj
								.getJSONObject("petServicePojo");
					}
					if (jobj.has("customerId") && !jobj.isNull("customerId")) {
						customerId = jobj.getLong("customerId");
					}
					if (jobj.has("payWay") && !jobj.isNull("payWay")) {
						paytype = jobj.getInt("payWay");
					}
					if (jobj.has("updateOrder") && !jobj.isNull("updateOrder")) {
						JSONObject jdata = jobj.getJSONObject("updateOrder");
						if (jdata.has("OrderId") && !jdata.isNull("OrderId")) {
							upgradeorderid = jdata.getInt("OrderId");
						}
						if (jdata.has("extraServiceItems")
								&& !jdata.isNull("extraServiceItems")) {
							JSONArray jarr = jdata
									.getJSONArray("extraServiceItems");
							for (int i = 0; i < jarr.length(); i++) {
								UpgradeItem item = UpgradeItem.json2Entity(jarr
										.getJSONObject(i));
								upgradeItemsList.add(item);
								totalfee += item.price;
							}
							adapter.notifyDataSetChanged();
							tvTotalFee.setText(Utils.formatDouble(totalfee, 2)
									+ "");
							showNeedFee();
						}

					} else {
						setResultForOrderDetail();
					}

				} else {
					if (obj.has("msg") && !obj.isNull("msg"))
						ToastUtil.showToastShortCenter(
								UpgradeServiceActivity.this,
								obj.getString("msg"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
				showMain(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			prsScrollView.onRefreshComplete();
			pDialog.closeDialog();
			showMain(false);
			ToastUtil.showToastShortCenter(UpgradeServiceActivity.this, "请求失败");
		}

	};

	private AsyncHttpResponseHandler balanceHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("账户余额：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("balance") && !jdata.isNull("balance")) {
						balance = jdata.getDouble("balance");
					}
					if (jdata.has("rechargeReminder")
							&& !jdata.isNull("rechargeReminder")) {
						tvBalanceHint.setText(jdata
								.getString("rechargeReminder"));
					}
					tvBlance.setText("" + Utils.formatDouble(balance, 2));
					showNeedFee();
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

	private AsyncHttpResponseHandler payHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("重新支付订单：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (paytype == 4 && balance >= needfee) {
						// 直接跳转到支付成功
						goPayResult();
					} else {
						if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
							appid = null;
							noncestr = null;
							packageValue = null;
							partnerid = null;
							prepayid = null;
							sign = null;
							timestamp = null;
							orderStr = null;
							JSONObject jpayInfo = jdata
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
							goPay();
						}
					}
				} else if (100 == resultCode) {
					// 操作失败，刷新订单
					ToastUtil.showToastShort(UpgradeServiceActivity.this, msg);
					getData();
				} else if (566 == resultCode) {
					// 美容师取消了订单，刷新订单
					ToastUtil.showToastShort(UpgradeServiceActivity.this, msg);
					getData();
				} else if (568 == resultCode) {
					// 美容师取消了订单又重新升级了订单，刷新订单
					ToastUtil.showToastShort(UpgradeServiceActivity.this, msg);
					getData();
				} else if (570 == resultCode) {
					// 整个订单被取消，刷新订单
					ToastUtil.showToastShort(UpgradeServiceActivity.this, msg);
					setResultForOrderDetail();
				} else {
					ToastUtil.showToastShort(UpgradeServiceActivity.this, msg);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(UpgradeServiceActivity.this, "请求失败");
		}
	};

	private void goPay() {
		if (paytype == 1) {
			if (appid != null && !TextUtils.isEmpty(appid) && noncestr != null
					&& !TextUtils.isEmpty(noncestr) && packageValue != null
					&& !TextUtils.isEmpty(packageValue) && partnerid != null
					&& !TextUtils.isEmpty(partnerid) && prepayid != null
					&& !TextUtils.isEmpty(prepayid) && sign != null
					&& !TextUtils.isEmpty(sign) && timestamp != null
					&& !TextUtils.isEmpty(timestamp)) {
				// 微信支付
				pDialog.showDialog();
				PayUtils.weChatPayment(UpgradeServiceActivity.this, appid,
						partnerid, prepayid, packageValue, noncestr, timestamp,
						sign, pDialog);
			} else {
				ToastUtil.showToastShortBottom(UpgradeServiceActivity.this,
						"支付参数错误");
			}
		} else if (paytype == 2) {
			if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
				// 判断是否安装支付宝
				PayUtils.checkAliPay(UpgradeServiceActivity.this, mHandler);
			} else {
				ToastUtil.showToastShortBottom(UpgradeServiceActivity.this,
						"支付参数错误");
			}
		}
	}

	public void setResultForOrderDetail() {
		setResult(RESULT_OK);
		finishWithAnimation();
	}

	private void goPayResult() {
		Intent intent = new Intent(this, PaySuccessActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
		startActivity(intent);
		setResultForOrderDetail();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Global.RESULT_OK
				&& requestCode == Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY) {
			isRechargeReturn = true;
			getBalance();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 支付宝支付begin
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Utils.mLogError("支付宝返回码：" + msg.what);
			switch (msg.what) {
			case Global.ALI_SDK_PAY_FLAG:
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				Utils.mLogError("支付宝返回码：" + resultStatus);
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					goPayResult();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.showToastShort(UpgradeServiceActivity.this,
								"支付结果确认中!");
					} else if (TextUtils.equals(resultStatus, "6001")) {
						// 从支付宝返回的状态
					} else {
						ToastUtil.showToastShort(UpgradeServiceActivity.this,
								"支付失败,请重新支付!");
					}
				}
				break;
			case Global.ALI_SDK_CHECK_FLAG:
				if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
					// 支付宝支付
					pDialog.showDialog();
					PayUtils.payByAliPay(UpgradeServiceActivity.this, orderStr,
							mHandler, pDialog);
				} else {
					ToastUtil.showToastShortBottom(UpgradeServiceActivity.this,
							"支付参数错误");
				}
				break;
			}
		};
	};
}
