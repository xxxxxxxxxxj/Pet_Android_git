package com.haotang.pet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.OrderPayScanInfoAdapter;
import com.haotang.pet.adapter.OrderPayTcAdapter;
import com.haotang.pet.entity.CardMakeDog;
import com.haotang.pet.entity.CardsBuy;
import com.haotang.pet.entity.Fostercare;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PetCardInfoCodeBean;
import com.haotang.pet.entity.PetCardInfoCodeBean.CertiCouponsBean;
import com.haotang.pet.entity.PetService;
import com.haotang.pet.entity.TrainServiceEvery;
import com.haotang.pet.entity.TrainTransfer;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.MListview;

public class OrderPayActivity extends SuperActivity implements OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private TextView tvPayFee;
	private RelativeLayout rlBalance;
	private TextView tvBalance;
	private TextView tvBalanceHint;
	private ImageView ivBalance;
	private ImageButton ibHybirdPay;
	private TextView tvHybirdPayHint;
	private TextView tvHybirdPayFee;
	private LinearLayout llHybirdPay;
	private RelativeLayout rlWXPay;
	private ImageView ivWXPay;
	private RelativeLayout rlAliPay;
	private ImageView ivAliPay;
	private Button btPay;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private int paytype = 1;// 1是微信，2为支付宝,3为优惠券，4为余额支付
	private int oldpayway;
	private int couponid;
	private double couponAmount;
	private double totalfee;
	private double payfee;
	private double balance;
	private double needpayfee;
	private boolean isHybirdPay;// 是否开启混合支付
	private boolean isRecharge;
	private boolean isRechargeReturn;// 是否是充值返回刷新余额
	private int previous = 0;
	private int previous_liucheng = 0;
	private Intent fIntent;
	private ArrayList<MulPetService> mpsList;
	private PetService ps;
	private String uuid;
	private int orderNo;
	private int orderNoUpOrder;
	private String servicename = "宠物家";
	private MCloseReceiver wxPayReceiver;
	private String cname;
	private String cphone;
	private String remark;
	private double addservicefee;
	private int type;
	private Fostercare fc;
	public static OrderPayActivity act;
	private long lastOverTime = 900000;
	private Timer timer;
	private TimerTask task;
	private TextView textView_order_thr;// 分钟
	private TextView textView_order_time_two;// 秒
	private int shopId;
	private int addrid;
	private String addr;
	private String dataString;
	private double lat;
	private double lng;
	private ArrayList<Pet> listPets;
	private int clicksort;
	private int serviceloc;
	private int addressid;
	private int pickup;
	private String month = "";
	private String times = "";
	private TextView tv_orderpay_paytitle;
	private LinearLayout ll_orderpay_tc;
	private MListview lv_orderpay_tc;
	private TextView tv_orderpay_des;
	private TextView tv_orderpay_ts;
	private RelativeLayout rl_orderpay_dhm;
	private TextView tv_orderpay_dhm;
	private EditText et_orderpay_dhm;
	private ImageView iv_orderpay_dhm;
	private LinearLayout layout_payway;
	private View vw_line3;
	private int swimPayPrice = 0;
	private String bathPetIds = "";
	private int CertiOrderId;
	private int tcid;
	private String yqm;
	protected double reduce;
	private int fee;
	private int orderFee;
	private int roomType;
	private MListview mlv_orderpay_info;
	private RelativeLayout rl_orderpay_info;
	private TextView tv_orderpay_xfje;
	private TextView tv_orderpay_xfprice;
	private LinearLayout ll_orderpay_djs;
	private String codeResult;
	private List<OrderPayScanInfo> listItems = new ArrayList<OrderPayScanInfo>();
	private OrderPayScanInfoAdapter<OrderPayScanInfo> adapter;
	private String localGoods;
	private int localShopId;
	protected boolean isCheckRedeem;
	protected long scanOrderId;
	protected double totalPayPrice;
	protected String scanRemark;
	private TrainTransfer transfer = null;
	private TrainServiceEvery trainServiceEvery = null;
	private String TrainTime;
	private int petid;
	private int customerpetid;
	private int petServiceId;
	private boolean isVie = false;
	private StringBuilder listpayWays = new StringBuilder();
	private boolean isWeiXin;
	private boolean isZhiFuBao;
	private boolean isYuE;
	protected boolean isCiKa;
	private boolean isHunHePayWays;
	private LinearLayout layout_make_cards;
	private LinearLayout layout_make_cards_dog;
	private TextView textview_card_make_dog;
	private TextView textview_card_make_dog_price;
	private TextView textview_service_card;
	private TextView textview_service_card_price;
	private TextView textview_service_card_cut_down_detail;
	private TextView textview_wash_title;
	private TextView textview_cards_totalmoney;
	private CardMakeDog CardMakeDog = null;
	private CardsBuy cardsBuyEvery;
	private int id;
	private RelativeLayout rlHybirdPay;
	private String listIds = "";
	private TextView textview_service_card_cut_down_old_price_detail;
	private RelativeLayout rl_orderpay_cika;
	private ImageView iv_orderpay_cika;
	private TextView tv_orderpay_cikadesc;
	private TextView tv_orderpay_cika;
	private boolean isManZu;
	// 第三方支付相关字段
	private String orderStr;
	private String appid;
	private String noncestr;
	private String packageValue;
	private String partnerid;
	private String prepayid;
	private String sign;
	private String timestamp;
	private OrderPayTcAdapter orderPayTcAdapter;
	private List<CertiCouponsBean> certiCoupons;
	private double localFee;
	private int certiId;

	private Handler aliHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Global.ALI_SDK_PAY_FLAG:
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// ToastUtil.showToastShort(OrderDetailActivity.this,
					// "支付成功!");
					goPayResult();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.showToastShort(OrderPayActivity.this,
								"支付结果确认中!");
					} else if (TextUtils.equals(resultStatus, "6001")) {
						// 从支付宝返回的状态
					} else {
						ToastUtil.showToastShort(OrderPayActivity.this,
								"支付失败,请重新支付!");
					}
				}
				break;
			case Global.ALI_SDK_CHECK_FLAG:
				if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
					// 支付宝支付
					pDialog.showDialog();
					PayUtils.payByAliPay(OrderPayActivity.this, orderStr,
							aliHandler, pDialog);
				} else {
					ToastUtil.showToastShortBottom(OrderPayActivity.this,
							"支付参数错误");
				}
				break;
			case 0:
				long lastTimerShowHMS = msg.arg1;
				String time = Utils.formatTimeFM(lastTimerShowHMS);
				String minute = time.substring(0, 2);
				String second = time.substring(3, 5);
				textView_order_thr.setText(minute);
				textView_order_time_two.setText(second);
				break;
			case 1:
				finishWithAnimation();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderpay);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		tv_orderpay_cika = (TextView) findViewById(R.id.tv_orderpay_cika);
		tv_orderpay_cikadesc = (TextView) findViewById(R.id.tv_orderpay_cikadesc);
		rl_orderpay_cika = (RelativeLayout) findViewById(R.id.rl_orderpay_cika);
		iv_orderpay_cika = (ImageView) findViewById(R.id.iv_orderpay_cika);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tvPayFee = (TextView) findViewById(R.id.tv_orderpay_payfee);

		rlBalance = (RelativeLayout) findViewById(R.id.rl_orderpay_balance);
		tvBalance = (TextView) findViewById(R.id.tv_orderpay_balance);
		tvBalanceHint = (TextView) findViewById(R.id.tv_orderpay_balance_hint);
		ivBalance = (ImageView) findViewById(R.id.iv_orderpay_balance);

		ibHybirdPay = (ImageButton) findViewById(R.id.ib_orderpay_hybirdpay);
		tvHybirdPayHint = (TextView) findViewById(R.id.tv_orderpay_hybirdpay);
		tvHybirdPayFee = (TextView) findViewById(R.id.tv_orderpay_hybirdpay_fee);
		llHybirdPay = (LinearLayout) findViewById(R.id.ll_orderpay_hybirdpay_fee);

		rlWXPay = (RelativeLayout) findViewById(R.id.rl_orderpay_paywx);
		ivWXPay = (ImageView) findViewById(R.id.iv_orderpay_paywx);
		rlAliPay = (RelativeLayout) findViewById(R.id.rl_orderpay_payali);
		ivAliPay = (ImageView) findViewById(R.id.iv_orderpay_payali);
		rlHybirdPay = (RelativeLayout) findViewById(R.id.rl_orderpay_hybirdpay);
		btPay = (Button) findViewById(R.id.bt_orderpay_pay);
		textView_order_thr = (TextView) findViewById(R.id.textView_order_thr);
		textView_order_time_two = (TextView) findViewById(R.id.textView_order_time_two);
		tv_orderpay_paytitle = (TextView) findViewById(R.id.tv_orderpay_paytitle);

		ll_orderpay_tc = (LinearLayout) findViewById(R.id.ll_orderpay_tc);
		lv_orderpay_tc = (MListview) findViewById(R.id.lv_orderpay_tc);
		tv_orderpay_des = (TextView) findViewById(R.id.tv_orderpay_des);
		tv_orderpay_ts = (TextView) findViewById(R.id.tv_orderpay_ts);

		rl_orderpay_dhm = (RelativeLayout) findViewById(R.id.rl_orderpay_dhm);
		tv_orderpay_dhm = (TextView) findViewById(R.id.tv_orderpay_dhm);
		et_orderpay_dhm = (EditText) findViewById(R.id.et_orderpay_dhm);
		iv_orderpay_dhm = (ImageView) findViewById(R.id.iv_orderpay_dhm);
		layout_payway = (LinearLayout) findViewById(R.id.layout_payway);
		vw_line3 = (View) findViewById(R.id.vw_line3);
		mlv_orderpay_info = (MListview) findViewById(R.id.mlv_orderpay_info);
		rl_orderpay_info = (RelativeLayout) findViewById(R.id.rl_orderpay_info);
		tv_orderpay_xfje = (TextView) findViewById(R.id.tv_orderpay_xfje);
		tv_orderpay_xfprice = (TextView) findViewById(R.id.tv_orderpay_xfprice);
		ll_orderpay_djs = (LinearLayout) findViewById(R.id.ll_orderpay_djs);

		layout_make_cards = (LinearLayout) findViewById(R.id.layout_make_cards);
		layout_make_cards_dog = (LinearLayout) findViewById(R.id.layout_make_cards_dog);
		textview_card_make_dog = (TextView) findViewById(R.id.textview_card_make_dog);
		textview_card_make_dog_price = (TextView) findViewById(R.id.textview_card_make_dog_price);
		textview_service_card = (TextView) findViewById(R.id.textview_service_card);
		textview_service_card_price = (TextView) findViewById(R.id.textview_service_card_price);
		textview_service_card_cut_down_detail = (TextView) findViewById(R.id.textview_service_card_cut_down_detail);
		textview_wash_title = (TextView) findViewById(R.id.textview_wash_title);
		textview_cards_totalmoney = (TextView) findViewById(R.id.textview_cards_totalmoney);
		textview_service_card_cut_down_old_price_detail = (TextView) findViewById(R.id.textview_service_card_cut_down_old_price_detail);

		et_orderpay_dhm.addTextChangedListener(new TextWatcher() {

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
				yqm = s.toString().trim();
				if (s.toString().length() == 4) {
					checkRedeemCode();
				} else {
					if (isCheckRedeem) {
						if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {// 游泳
							setSwimTc(listPets, false);
						} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {// 待付款
							if (type == 2) {// 寄养
								setDHM(false, orderFee, orderFee);
							} else if (type == 3) {// 游泳
								setSwimChangePay(mpsList, false);
							} else if (type == 4) {
								setDHM(false, orderFee, orderFee);
							}
						} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证

						} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {// 寄养
							setDHM(false, orderFee, orderFee);
						} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM) {// 训练
							setDHM(false, orderFee, orderFee); // 啥鬼跟寄养暂时一样看看效果
						}
						payfee = localFee;
						tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
						iv_orderpay_dhm
								.setBackgroundResource(R.drawable.complaint_reason_disable);
						tv_orderpay_dhm.setVisibility(View.GONE);
						isHybird();
						setNeedFee();
					}
				}
			}
		});

		lv_orderpay_tc.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (orderPayTcAdapter != null) {
					orderPayTcAdapter.setOnItemClick(position);
					CertiCouponsBean certiCouponsBean = certiCoupons
							.get(position);
					setTc(certiCouponsBean);
					isHybird();
					setNeedFee();
				}
			}
		});
	}

	private void setNeedFee() {
		if (isHybirdPay) {
			needpayfee = payfee - balance;
		} else {
			if (paytype != 4) {
				needpayfee = payfee;
			} else {
				needpayfee = 0;
			}
		}
	}

	private void setTc(CertiCouponsBean certiCouponsBean) {
		if (certiCouponsBean != null) {
			fee = certiCouponsBean.getFee();
			reduce = certiCouponsBean.getReduce();
			tcid = certiCouponsBean.getCertiCouponId();
			String caption = certiCouponsBean.getCaption();
			Utils.setStringText(tv_orderpay_paytitle, caption);
			setDHM(true, fee / 100, reduce / 100);
		}
	}

	private void setDHM(boolean bool, int fee, double reduce) {
		if (previous != Global.TRAIN_TO_ORDER_CONFIRM || type != 4) {
			if (fee <= 0) {
				rl_orderpay_dhm.setVisibility(View.GONE);
				payfee = localFee;
				tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
				iv_orderpay_dhm
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				tv_orderpay_dhm.setVisibility(View.GONE);
				et_orderpay_dhm.setText("");
			} else {
				rl_orderpay_dhm.setVisibility(View.VISIBLE);
			}
			if (bool) {
				if (reduce > 0) {
					tv_orderpay_ts.setVisibility(View.VISIBLE);
					tv_orderpay_ts.setText("为您节省" + reduce + "元");
				} else {
					tv_orderpay_ts.setVisibility(View.GONE);
				}
			} else {
				tv_orderpay_ts.setVisibility(View.GONE);
			}
			tv_orderpay_dhm.setText("立减" + fee + "元");
		} else {
			rl_orderpay_dhm.setVisibility(View.VISIBLE);
			if (fee > 0) {
				tv_orderpay_dhm.setText("立减" + fee + "元");
			} else {
				tv_orderpay_dhm.setVisibility(View.GONE);
			}
			if (bool) {
				if (reduce > 0) {
					tv_orderpay_ts.setVisibility(View.VISIBLE);
					tv_orderpay_ts.setText("为您节省" + reduce + "元");
				} else {
					tv_orderpay_ts.setVisibility(View.GONE);
				}
			} else {
				tv_orderpay_ts.setVisibility(View.GONE);
			}
		}
	}

	private void setSwimTc(ArrayList<Pet> listPets, boolean isshow) {
		swimPayPrice = 0;
		if (listPets.size() > 0) {
			for (int i = 0; i < listPets.size(); i++) {
				if (listPets.get(i).orderFee > 0) {
					swimPayPrice += listPets.get(i).orderFee;
				}
			}
			if (swimPayPrice > 0) {
				rl_orderpay_dhm.setVisibility(View.VISIBLE);
				tv_orderpay_ts.setVisibility(View.VISIBLE);
				if (isshow) {
					tv_orderpay_ts.setText("为您节省" + swimPayPrice + "元");
					tv_orderpay_dhm.setText("立减" + swimPayPrice + "元");
				} else {
					tv_orderpay_ts.setVisibility(View.GONE);
				}
			} else if (swimPayPrice <= 0) {
				rl_orderpay_dhm.setVisibility(View.GONE);
				payfee = localFee;
				tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
				iv_orderpay_dhm
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				tv_orderpay_dhm.setVisibility(View.GONE);
				et_orderpay_dhm.setText("");
			}

		}
	}

	private void setSwimChangePay(ArrayList<MulPetService> listPets,
			boolean isshow) {
		swimPayPrice = 0;
		if (listPets.size() > 0) {
			for (int i = 0; i < listPets.size(); i++) {
				if (listPets.get(i).orderFee > 0) {
					swimPayPrice += listPets.get(i).orderFee;
				}
			}
			if (swimPayPrice > 0) {
				rl_orderpay_dhm.setVisibility(View.VISIBLE);
				tv_orderpay_ts.setVisibility(View.VISIBLE);
				tv_orderpay_dhm.setText("立减" + swimPayPrice + "元");
				if (isshow) {
					tv_orderpay_ts.setText("为您节省" + swimPayPrice + "元");
					// tv_orderpay_ts.setVisibility(View.GONE);//ios
					// 如果不加这个为您节省，就gone掉，需求是有的
				} else {
					tv_orderpay_ts.setVisibility(View.GONE);
				}
			} else if (swimPayPrice <= 0) {
				rl_orderpay_dhm.setVisibility(View.GONE);
				payfee = localFee;
				tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
				iv_orderpay_dhm
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				tv_orderpay_dhm.setVisibility(View.GONE);
				et_orderpay_dhm.setText("");
			}

		}
	}

	private double setSwinPayPrice(ArrayList<MulPetService> listPets) {
		swimPayPrice = 0;
		if (listPets.size() > 0) {
			for (int i = 0; i < listPets.size(); i++) {
				if (listPets.get(i).orderFee > 0) {
					swimPayPrice += listPets.get(i).orderFee;
				}
			}
		}
		return swimPayPrice;

	}

	private void setTrainChangePay(boolean isshow) {
		if (isshow) {
			rl_orderpay_dhm.setVisibility(View.VISIBLE);
		} else {
			rl_orderpay_dhm.setVisibility(View.GONE);
			payfee = localFee;
			tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
			iv_orderpay_dhm
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			tv_orderpay_dhm.setVisibility(View.GONE);
			et_orderpay_dhm.setText("");
		}
	}

	private void setView() {
		tvTitle.setText("收银台");
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		initWXPayReceiver();
		getBalance();
		fIntent = getIntent();
		previous = fIntent.getIntExtra("previous", 0);
		previous_liucheng = fIntent.getIntExtra("previous_liucheng", 0);
		bathPetIds = fIntent.getStringExtra("bathPetIds");
		shopId = fIntent.getIntExtra("shopId", 0);
		addrid = fIntent.getIntExtra("addrid", 0);
		clicksort = fIntent.getIntExtra("clicksort", 0);
		serviceloc = fIntent.getIntExtra("serviceloc", 0);
		pickup = getIntent().getIntExtra("pickup", 0);
		month = getIntent().getStringExtra("month");
		times = getIntent().getStringExtra("times");
		lat = fIntent.getDoubleExtra("addr_lat", 0);
		lng = fIntent.getDoubleExtra("addr_lng", 0);
		addr = fIntent.getStringExtra("address");
		codeResult = fIntent.getStringExtra("codeResult");
		dataString = fIntent.getStringExtra("date");
		listPets = OrderDetailActivity.listPets;
		listIds = fIntent.getStringExtra("listIds");
		orderNo = fIntent.getIntExtra("orderid", 0);
		layout_payway.setBackgroundResource(R.color.af8f8f8);
		if (previous == Global.BATH_TO_ORDERPAY) {
			uuid = fIntent.getStringExtra("uuid");
			orderNo = fIntent.getIntExtra("orderid", 0);
			ps = (PetService) fIntent.getSerializableExtra("ps");
			mpsList = fIntent.getParcelableArrayListExtra("mpslist");
			couponid = fIntent.getIntExtra("couponid", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			payfee = fIntent.getDoubleExtra("payfee", 0);
			cname = fIntent.getStringExtra("cname");
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			cphone = fIntent.getStringExtra("cphone");
			remark = fIntent.getStringExtra("remark");
			if (mpsList != null && mpsList.size() >= 1) {
				String subString = getServiceName(mpsList);
				if (subString.contains("1") && !subString.contains("2")) {
					servicename = "洗澡套餐金额";
				} else if (!subString.contains("1") && subString.contains("2")) {
					servicename = "美容套餐金额";
				} else if (subString.contains("1") && subString.contains("2")) {
					servicename = "洗美套餐金额";
				}
			}
			if (mpsList != null && mpsList.size() > 0) {
				for (int i = 0; i < mpsList.size(); i++) {
					addservicefee += mpsList.get(i).addservicefee;
					if (mpsList.get(i).serviceType == 3) {
						servicename = "特色服务套餐金额";
					}
				}
			}
			if (orderNo <= 0) {
				timerDown();
			}
			type = 1;
			payWays("care");
		} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {
			orderFee = fIntent.getIntExtra("orderFee", 0);
			setDHM(false, orderFee, orderFee);
			uuid = fIntent.getStringExtra("uuid");
			orderNo = fIntent.getIntExtra("orderid", 0);
			couponid = fIntent.getIntExtra("couponid", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			payfee = fIntent.getDoubleExtra("needfee", 0);
			fc = (Fostercare) fIntent.getSerializableExtra("fc");
			cname = fIntent.getStringExtra("cname");
			cphone = fIntent.getStringExtra("cphone");
			remark = fIntent.getStringExtra("remark");
			servicename = "宠物家寄养-" + fc.roomname;
			mpsList = new ArrayList<MulPetService>();
			MulPetService mps = new MulPetService();
			mps.petId = fc.petid;
			mps.petName = fc.petname;
			mps.petKind = fc.petkind;
			mps.serviceName = fc.roomname;
			mps.serviceType = 100;
			mps.serviceId = fc.roomid;
			mps.petCustomerId = fc.customerpetid;
			mpsList.add(mps);
			if (orderNo <= 0) {
				timerDown();
			}
			servicename = "寄养套餐金额";
			type = 2;
			payWays("hotel");
		} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM) {// 训练过来的
			setTrainChangePay(true);// 只算邀请 不算
			type = fIntent.getIntExtra("type", 0);
			uuid = fIntent.getStringExtra("uuid");
			orderNo = fIntent.getIntExtra("orderid", 0);
			shopId = fIntent.getIntExtra("shopId", 0);
			couponid = fIntent.getIntExtra("couponid", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			payfee = fIntent.getDoubleExtra("needfee", 0);
			transfer = (TrainTransfer) fIntent.getSerializableExtra("transfer");
			trainServiceEvery = (TrainServiceEvery) fIntent
					.getSerializableExtra("trainServiceEvery");
			cname = fIntent.getStringExtra("cname");
			cphone = fIntent.getStringExtra("cphone");
			remark = fIntent.getStringExtra("remark");
			TrainTime = fIntent.getStringExtra("time");
			petid = fIntent.getIntExtra("petid", 0);
			customerpetid = fIntent.getIntExtra("customerpetid", 0);
			servicename = "宠物家训练-" + trainServiceEvery.name;
			rl_orderpay_dhm.setVisibility(View.VISIBLE);
			if (orderNo <= 0) {
				timerDown();
			}
			type = 4;
			payWays("train");
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {// 待付款
			mpsList = fIntent.getParcelableArrayListExtra("mpslist");
			roomType = fIntent.getIntExtra("roomType", 0);
			orderNo = fIntent.getIntExtra("orderid", 0);
			shopId = fIntent.getIntExtra("shopId", 0);
			couponid = fIntent.getIntExtra("couponid", 0);
			payfee = fIntent.getDoubleExtra("needfee", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			type = fIntent.getIntExtra("type", 0);
			petServiceId = fIntent.getIntExtra("petService", 0);
			isVie = fIntent.getBooleanExtra("isVie", false);
			if (mpsList != null && mpsList.size() > 0) {
				for (int i = 0; i < mpsList.size(); i++) {
					addservicefee += mpsList.get(i).addservicefee;
				}
			}
			SecondTimeDown();
			if (type == 2) {
				servicename = "寄养套餐金额";
				orderFee = fIntent.getIntExtra("orderFee", 0);
				setDHM(false, orderFee, orderFee);
				payWays("hotel");
			} else if (type == 3) {
				servicename = "游泳套餐金额";
				setSwimChangePay(mpsList, false);
				payWays("swim");
			} else if (type == 4) {
				servicename = "训练套餐金额";
				rl_orderpay_dhm.setVisibility(View.VISIBLE);
				payWays("train");
			} else if (type == 1) {
				if (mpsList != null && mpsList.size() >= 1) {
					String subString = getServiceName(mpsList);
					if (subString.contains("1") && !subString.contains("2")) {
						servicename = "洗澡套餐金额";
					} else if (!subString.contains("1")
							&& subString.contains("2")) {
						servicename = "美容套餐金额";
					} else if (subString.contains("1")
							&& subString.contains("2")) {
						servicename = "洗美套餐金额";
					}
				}
				for (int i = 0; i < mpsList.size(); i++) {
					if (mpsList.get(i).serviceType == 3) {
						servicename = "特色服务套餐金额";
					}
				}
				payWays("care");
			}
		} else if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			uuid = fIntent.getStringExtra("uuid");
			orderNo = fIntent.getIntExtra("orderid", 0);
			ps = (PetService) fIntent.getSerializableExtra("ps");
			mpsList = fIntent.getParcelableArrayListExtra("mpslist");
			couponid = fIntent.getIntExtra("couponid", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			payfee = fIntent.getDoubleExtra("payfee", 0);
			cname = fIntent.getStringExtra("cname");
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			cphone = fIntent.getStringExtra("cphone");
			remark = fIntent.getStringExtra("remark");
			if (orderNo <= 0) {
				timerDown();
			}
			type = 3;
			setSwimTc(listPets, false);
			Utils.mLogError("==-->swimPayPrice " + swimPayPrice);
			servicename = "游泳套餐金额";
			payWays("swim");
		} else if (previous == Global.URGENT_TO_ORDERDETAIL) {
			isVie = true;
			uuid = fIntent.getStringExtra("uuid");
			orderNo = fIntent.getIntExtra("orderid", 0);
			ps = (PetService) fIntent.getSerializableExtra("ps");
			mpsList = fIntent.getParcelableArrayListExtra("mpslist");
			couponid = fIntent.getIntExtra("couponid", 0);
			couponAmount = fIntent.getDoubleExtra("couponamount", 0);
			payfee = fIntent.getDoubleExtra("payfee", 0);
			cname = fIntent.getStringExtra("cname");
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			cphone = fIntent.getStringExtra("cphone");
			remark = fIntent.getStringExtra("remark");
			addressid = getIntent().getIntExtra("addressid", 0);
			if (orderNo <= 0) {
				timerDown();
			}
			if (mpsList != null && mpsList.size() >= 1) {
				String subString = getServiceName(mpsList);
				if (subString.contains("1") && !subString.contains("2")) {
					servicename = "洗澡套餐金额";
				} else if (!subString.contains("1") && subString.contains("2")) {
					servicename = "美容套餐金额";
				} else if (subString.contains("1") && subString.contains("2")) {
					servicename = "洗美套餐金额";
				}
				for (int i = 0; i < mpsList.size(); i++) {
					if (mpsList.get(i).serviceType == 3) {
						servicename = "特色服务套餐金额";
					}
				}
				payWays("care");
			}
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证支付
			layout_payway.setBackgroundResource(R.color.white);
			CertiOrderId = fIntent.getIntExtra("CertiOrderId", 0);
			orderNo = CertiOrderId;
			type = 12;
			// 获取订单信息
			getOrderInfo();
			servicename = "宠物家办证";
			payWays("certi");
		} else if (previous == Global.MIPCA_TO_ORDERPAY) {// 扫码支付
			mlv_orderpay_info.setVisibility(View.VISIBLE);
			adapter = new OrderPayScanInfoAdapter<OrderPayScanInfo>(mContext,
					listItems);
			mlv_orderpay_info.setAdapter(adapter);
			adapter.setData(listItems);
			rl_orderpay_info.setVisibility(View.GONE);
			tv_orderpay_xfje.setVisibility(View.VISIBLE);
			tv_orderpay_xfprice.setVisibility(View.VISIBLE);
			ll_orderpay_djs.setVisibility(View.GONE);
			tv_orderpay_xfje.setText("消费金额");
			CommUtil.ScanCodePayment(codeResult, mContext,
					spUtil.getString("cellphone", ""), ScanCodePaymentHandler);
			payWays("goods");
		} else if (previous == Global.CARDSDETAIL_TO_ORDERPAY) {
			layout_make_cards.setVisibility(View.VISIBLE);
			rl_orderpay_info.setVisibility(View.GONE);
			petid = fIntent.getIntExtra("petid", 0);
			id = fIntent.getIntExtra("id", -1);
			cardsBuyEvery = (CardsBuy) fIntent.getSerializableExtra("CardsBuy");
			CardMakeDog = (com.haotang.pet.entity.CardMakeDog) fIntent
					.getSerializableExtra("CardMakeDog");
			if (CardMakeDog != null) {
				if (CardMakeDog.isChoose) {
					layout_make_cards_dog.setVisibility(View.VISIBLE);
					textview_card_make_dog.setText(CardMakeDog.name);
					textview_card_make_dog_price.setText("¥"
							+ CardMakeDog.price);

					textview_service_card_price.setText("¥"
							+ cardsBuyEvery.price1);
					payfee = cardsBuyEvery.price1 + CardMakeDog.price;
					textview_service_card_cut_down_detail
							.setText(cardsBuyEvery.petCardDiscount + "折");
				} else {
					layout_make_cards_dog.setVisibility(View.GONE);
					textview_service_card_cut_down_detail
							.setText(cardsBuyEvery.discount + "折");
					textview_service_card_price.setText("¥"
							+ cardsBuyEvery.price);
					payfee = cardsBuyEvery.price;
				}
			} else {
				layout_make_cards_dog.setVisibility(View.GONE);

				textview_service_card_cut_down_detail
						.setText(cardsBuyEvery.discount + "折");
				textview_service_card_price.setText("¥" + cardsBuyEvery.price);
				payfee = cardsBuyEvery.price;
			}
			textview_service_card.setText(fIntent.getStringExtra("petName")
					+ fIntent.getStringExtra("titleName"));
			textview_service_card_cut_down_old_price_detail.setText("原价"
					+ cardsBuyEvery.listPrice);
			StringBuilder builderContent = new StringBuilder();
			for (int i = 0; i < cardsBuyEvery.listStr.size(); i++) {
				if (i == cardsBuyEvery.listStr.size() - 1) {
					builderContent.append(cardsBuyEvery.listStr.get(i));
				} else {
					builderContent
							.append(cardsBuyEvery.listStr.get(i) + "\n\n");
				}
			}
			if (TextUtils.isEmpty(builderContent)) {
				textview_wash_title.setVisibility(View.GONE);
			} else {
				textview_wash_title.setText(builderContent.toString());
			}
			textview_cards_totalmoney.setText("¥" + payfee);
			timerDown();
			payWays("timeCard");
		} else if (previous == Global.UPDATE_TO_ORDERPAY) {// 订单升级确认过来
			orderNo = fIntent.getIntExtra("orderid", 0);
			mpsList = fIntent.getParcelableArrayListExtra("mpslist");
			payfee = fIntent.getDoubleExtra("payfee", 0);
			totalfee = fIntent.getDoubleExtra("totalfee", 0);
			timerDown();
			payWays("care");
		}
		tv_orderpay_paytitle.setText(servicename);
		if (previous == Global.UPDATE_TO_ORDERPAY) {
			tv_orderpay_paytitle.setText("订单升级需付金额");
		}
		if (orderNo > 0) {
			if (!(previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY
					|| previous == Global.MIPCA_TO_ORDERPAY || previous == Global.UPDATE_TO_ORDERPAY)) {
				getTimeDown();
			}
		}
		tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
		oldpayway = spUtil.getInt("payway", 0);
		if (oldpayway == 2) {
			oldpayway = 2;
			paytype = 2;
			needpayfee = payfee;
			ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivBalance
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivAliPay.setBackgroundResource(R.drawable.complaint_reason);
			iv_orderpay_cika
					.setBackgroundResource(R.drawable.complaint_reason_disable);
		} else {
			paytype = 1;
			needpayfee = payfee;
			iv_orderpay_cika
					.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivWXPay.setBackgroundResource(R.drawable.complaint_reason);
			ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
			ivBalance
					.setBackgroundResource(R.drawable.complaint_reason_disable);
		}
		ibBack.setOnClickListener(this);
		ibHybirdPay.setOnClickListener(this);
		rlAliPay.setOnClickListener(this);
		rlBalance.setOnClickListener(this);
		rlWXPay.setOnClickListener(this);
		btPay.setOnClickListener(this);
		rl_orderpay_cika.setOnClickListener(this);
		tv_orderpay_cikadesc.setOnClickListener(this);
		SpannableString ss = new SpannableString("次卡支付更优惠，立即购买");
		// 用下划线标记文本
		ss.setSpan(new UnderlineSpan(), 0, ss.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tv_orderpay_cikadesc.setText(ss);
	}

	private void payWays(String type) {
		CommUtil.payWays(mContext, type, CertiOrderId, payWaysHandler);
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
						if (jData.has("timeCardTag")
								&& !jData.isNull("timeCardTag")) {
							Utils.setText(tv_orderpay_cika,
									jData.getString("timeCardTag"), "",
									View.VISIBLE, View.VISIBLE);
						}
						if (jData.has("payWays") && !jData.isNull("payWays")) {
							JSONArray jsonArray = jData.getJSONArray("payWays");
							if (jsonArray.length() > 0) {
								listpayWays.setLength(0);
								for (int i = 0; i < jsonArray.length(); i++) {
									listpayWays.append(jsonArray.getString(i));
								}
								if (listpayWays.toString().contains("1")) {// 微信
									isWeiXin = true;
									rlWXPay.setVisibility(View.VISIBLE);
								} else {
									isWeiXin = false;
									rlWXPay.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("2")) {// 支付宝
									isZhiFuBao = true;
									rlAliPay.setVisibility(View.VISIBLE);
								} else {
									isZhiFuBao = false;
									rlAliPay.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("3")) {// 混合支付
									isHunHePayWays = true;
									rlHybirdPay.setVisibility(View.VISIBLE);
								} else {
									isHunHePayWays = false;
									rlHybirdPay.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("4")) {// 余额
									isYuE = true;
									rlBalance.setVisibility(View.VISIBLE);
								} else {
									isYuE = false;
									rlBalance.setVisibility(View.GONE);
								}
								if (listpayWays.toString().contains("7")) {// 次卡
									isCiKa = true;
									rl_orderpay_cika
											.setVisibility(View.VISIBLE);
								} else {
									isCiKa = false;
									rl_orderpay_cika.setVisibility(View.GONE);
								}
							}
						}
						getBalance();
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

	private AsyncHttpResponseHandler ScanCodePaymentHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject objectData = Utils
						.getNetData(mContext, responseBody);
				if (objectData != null) {
					if (objectData.has("items") && !objectData.isNull("items")) {
						JSONArray items = objectData.getJSONArray("items");
						if (items.length() > 0) {
							listItems.clear();
							for (int i = 0; i < items.length(); i++) {
								listItems.add(OrderPayScanInfo
										.json2Entity(items.getJSONObject(i)));
							}
							adapter.setData(listItems);
						}
					}
					if (objectData.has("totalPayPrice")
							&& !objectData.isNull("totalPayPrice")) {
						totalPayPrice = objectData.getDouble("totalPayPrice");
						String text = "¥ " + Math.round(totalPayPrice);
						SpannableString ss = new SpannableString(text);
						ss.setSpan(new TextAppearanceSpan(
								OrderPayActivity.this, R.style.style4), 0, 1,
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tv_orderpay_xfprice.setText(ss);
						payfee = Math.round(totalPayPrice);
						oldpayway = 4;
						isHybird();
						setNeedFee();
					}
					if (objectData.has("goods") && !objectData.isNull("goods")) {
						localGoods = objectData.getString("goods");
					}
					if (objectData.has("shopId")
							&& !objectData.isNull("shopId")) {
						localShopId = objectData.getInt("shopId");
					}
					if (objectData.has("remark")
							&& !objectData.isNull("remark")) {
						scanRemark = objectData.getString("remark");
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

	private void SecondTimeDown() {
		lastOverTime = fIntent.getLongExtra("lastOverTime", 0);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				if (lastOverTime > 0) {
					lastOverTime -= 1000;
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = (int) lastOverTime;
					aliHandler.sendMessage(msg);
				} else {
					if (timer != null) {
						aliHandler.sendEmptyMessage(1);
						timer.cancel();
						timer = null;
					}
				}
			}
		};
		timer.schedule(task, 0, 1000);
	}

	private void getTimeDown() {
		CommUtil.getResidualTimeOfPay(mContext,
				spUtil.getString("cellphone", ""), spUtil.getInt("userid", 0),
				orderNo, type, handler);
	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->倒计时  " + new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("residualTime")
								&& !objectData.isNull("residualTime")) {
							lastOverTime = objectData.getLong("residualTime");
							timerDown();
						}
					}
				} else {
					if (object.has("msg") && !object.isNull("msg")) {
						String msg = object.getString("msg");
						ToastUtil.showToastShortCenter(OrderPayActivity.this,
								msg);
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

	private void timerDown() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if (lastOverTime > 0) {
					lastOverTime -= 1000;
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = (int) lastOverTime;
					aliHandler.sendMessage(msg);
				} else {
					if (timer != null) {
						aliHandler.sendEmptyMessage(1);
						timer.cancel();
						timer = null;
					}
				}
			}
		};
		timer.schedule(task, 0, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_orderpay_cikadesc:
			break;
		case R.id.rl_orderpay_cika:// 次卡支付
			if (paytype != 7) {
				isWeiXin = false;
				isZhiFuBao = false;
				isYuE = false;
				isCiKa = true;
				paytype = 7;
				oldpayway = 7;
				iv_orderpay_cika
						.setBackgroundResource(R.drawable.complaint_reason);
				ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBalance
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				needpayfee = 0;
			}
			break;
		case R.id.ib_titlebar_back:
			goBack();
			break;
		case R.id.rl_orderpay_balance:
			// 余额支付
			if (isRecharge) {
				// 余额不足，可以去充值
				goNextForData(MyLastMoney.class,
						Global.PRE_ORDERDETAIL_TO_RECHARGE);
			} else {
				// 余额支付
				if (paytype != 4) {
					paytype = 4;
					oldpayway = 4;
					isWeiXin = false;
					isZhiFuBao = false;
					isYuE = true;
					isCiKa = false;
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason);
					needpayfee = 0;
				}
			}
			break;
		case R.id.ib_orderpay_hybirdpay:
			// 混合支付开关
			if (isHybirdPay) {
				isHybirdPay = false;
				ibHybirdPay.setBackgroundResource(R.drawable.icon_hybird_no);
				tvHybirdPayHint.setTextColor(getResources().getColor(
						R.color.a757575));
				tvHybirdPayHint
						.setBackgroundResource(R.drawable.bg_button_gray_normal);
				llHybirdPay.setVisibility(View.GONE);
				needpayfee = payfee;
			} else {
				isHybirdPay = true;
				ibHybirdPay.setBackgroundResource(R.drawable.icon_hybird_yes);
				tvHybirdPayHint.setTextColor(getResources().getColor(
						R.color.white));
				tvHybirdPayHint
						.setBackgroundResource(R.drawable.bg_button_orange_normal);
				llHybirdPay.setVisibility(View.VISIBLE);
				needpayfee = payfee - balance;
			}
			break;
		case R.id.rl_orderpay_paywx:
			// 微信支付
			if (paytype != 1) {
				paytype = 1;
				oldpayway = 1;
				isWeiXin = true;
				isZhiFuBao = false;
				isYuE = false;
				isCiKa = false;
				ivWXPay.setBackgroundResource(R.drawable.complaint_reason);
				iv_orderpay_cika
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBalance
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				if (isHybirdPay) {
					needpayfee = payfee - balance;
				} else {
					needpayfee = payfee;
				}
			}
			break;
		case R.id.rl_orderpay_payali:
			// 支付宝支付
			if (paytype != 2) {
				paytype = 2;
				oldpayway = 2;
				isWeiXin = false;
				isZhiFuBao = true;
				isYuE = false;
				isCiKa = false;
				ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				iv_orderpay_cika
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAliPay.setBackgroundResource(R.drawable.complaint_reason);
				ivBalance
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				if (isHybirdPay) {
					needpayfee = payfee - balance;
				} else {
					needpayfee = payfee;
				}
			}
			break;
		case R.id.bt_orderpay_pay:
			switch (paytype) {
			case 1:
				if (!isWeiXin) {
					paytype = 0;
				}
				break;
			case 2:
				if (!isZhiFuBao) {
					paytype = 0;
				}
				break;
			case 4:
				if (!isYuE) {
					paytype = 0;
				}
				break;
			case 7:
				if (!isCiKa) {
					paytype = 7;
				}
				break;

			default:
				break;
			}
			if (paytype <= 0) {
				if (isManZu == false && isWeiXin == false
						&& isZhiFuBao == false && isHunHePayWays == false
						&& isCiKa == false && isYuE == true) {
					ToastUtil.showToastShortCenter(mContext, "请先充值再付款哦~");
				} else {
					ToastUtil.showToastShortCenter(mContext, "请选择支付方式");
				}
			} else {
				// 去支付
				if (needpayfee <= 0)
					needpayfee = 0;
				if (previous == Global.BATH_TO_ORDERPAY) {
					if (orderNo > 0) {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							changepay(addservicefee, -1);
						} else {
							changepay(addservicefee, balance);
						}
					} else {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							sendOrder(-1);
						} else {
							sendOrder(balance);
						}
					}
				} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {
					if (orderNo > 0) {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							if (fIntent.getBooleanExtra("isChooseShexiangtou",
									false)) {
								changepay(fIntent.getDoubleExtra(
										"needCameraPrice", -1), -1);
							} else {
								changepay(-1, -1);
							}
						} else {
							if (fIntent.getBooleanExtra("isChooseShexiangtou",
									false)) {
								changepay(fIntent.getDoubleExtra(
										"needCameraPrice", -1), balance);
							} else {
								changepay(-1, balance);
							}
						}

					} else {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							if (fIntent.getBooleanExtra("isChooseShexiangtou",
									false)) {
								newOrder(fIntent.getDoubleExtra(
										"needCameraPrice", 0), -1);
							} else {
								newOrder(-1, -1);
							}
						} else {
							if (fIntent.getBooleanExtra("isChooseShexiangtou",
									false)) {
								newOrder(fIntent.getDoubleExtra(
										"needCameraPrice", 0), balance);
							} else {
								newOrder(-1, balance);
							}
						}

					}
				} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {
					// 待付款支付
					if (balance <= 0 || (paytype == 4 && balance >= payfee)
							|| !isHybirdPay) {
						if (type == 4) {
							TwoTrainPay(-1);
						} else {
							payByOrder(-1);
						}
					} else {
						if (type == 4) {
							TwoTrainPay(balance);
						} else {
							payByOrder(balance);// 不为-1表示是混合支付
						}
					}
				} else if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {// 游泳
					if (orderNo > 0) {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							changepay(0, -1);
						} else {
							changepay(0, balance);
						}
					} else {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							SwimOrder(-1);
						} else {
							SwimOrder(balance);
						}
					}
				} else if (previous == Global.URGENT_TO_ORDERDETAIL) {
					if (orderNo > 0) {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							changepay(0, -1);
						} else {
							changepay(0, balance);
						}
					} else {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							sendIsExOrder(-1);
						} else {
							sendIsExOrder(balance);
						}
					}
				} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
					changePayWay();
				} else if (previous == Global.MIPCA_TO_ORDERPAY) {// 扫码支付
					CommUtil.ScanCodePay(scanRemark, scanOrderId, isHybirdPay,
							totalPayPrice, balance, paytype, localGoods,
							localShopId, mContext,
							spUtil.getString("cellphone", ""),
							ScanCodePayHandler);
				} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM) {// 训练支付
					if (orderNo > 0) {
						if (balance <= 0 || paytype == 4 && balance >= payfee
								|| !isHybirdPay) {
							TwoTrainPay(-1);
						} else {
							TwoTrainPay(balance);
						}
					} else {
						if (balance <= 0 || (paytype == 4 && balance >= payfee)
								|| !isHybirdPay) {
							TrainPay(-1);
						} else {
							TrainPay(balance);
						}
					}
				} else if (previous == Global.CARDSDETAIL_TO_ORDERPAY) {
					if (balance <= 0 || (paytype == 4 && balance >= payfee)
							|| !isHybirdPay) {
						BuyCards(-1);
					} else {
						BuyCards(balance);
					}
				} else if (previous == Global.UPDATE_TO_ORDERPAY) {
					if (balance <= 0 || (paytype == 4 && balance >= payfee)
							|| !isHybirdPay) {
						changeUpdatePay(-1);
					} else {
						changeUpdatePay(balance);
					}
				}
			}
			break;
		}
	}

	private AsyncHttpResponseHandler ScanCodePayHandler = new AsyncHttpResponseHandler() {
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
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("detail")
								&& !objectData.isNull("detail")) {
							servicename = objectData.getString("detail");
						}
						if (objectData.has("orderId")
								&& !objectData.isNull("orderId")) {
							scanOrderId = objectData.getLong("orderId");
						}
						if (objectData.has("payInfo")
								&& !objectData.isNull("payInfo")) {
							JSONObject jpayInfo = objectData
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
						}
					}
					if (needpayfee == 0) {
						ToastUtil.showToastShortBottom(OrderPayActivity.this,
								"支付成功");
						// 直接跳转到支付成功
						goPayResult();
					} else {
						// 第三方支付
						goPay();
					}
				} else {
					if (object.has("msg") && !object.isNull("msg")) {
						String msg = object.getString("msg");
						ToastUtil.showToastShortCenter(OrderPayActivity.this,
								msg);
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

	private void checkRedeemCode() {
		pDialog.showDialog();
		if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {// 游泳
			CommUtil.checkRedeemCode(yqm, 0, swimPayPrice, "SWIM", shopId, 0,
					getPetId_ServiceId_MyPetIdSwim(listPets),
					spUtil.getString("cellphone", ""), this, 0,
					checkRedeemCodeHanler);
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {// 待付款
			if (type == 2) {// 寄养
				CommUtil.checkRedeemCode(yqm, 0, orderFee, "HOTEL", shopId,
						roomType, null, spUtil.getString("cellphone", ""),
						this, 0, checkRedeemCodeHanler);
			} else if (type == 3) {// 游泳
				CommUtil.checkRedeemCode(yqm, 0, swimPayPrice, "SWIM", shopId,
						0, getPetId_ServiceId_MyPetIdFromOrder(mpsList),
						spUtil.getString("cellphone", ""), this, 0,
						checkRedeemCodeHanler);
			} else if (type == 4) {// 训练
				CommUtil.checkRedeemCode(yqm, 0, (int) payfee, "TRAIN", shopId,
						0, petid + "", spUtil.getString("cellphone", ""), this,
						petServiceId, checkRedeemCodeHanler);
			}
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证
			CommUtil.checkRedeemCode(yqm, tcid, fee, "PETCARD", 0, 0, null,
					spUtil.getString("cellphone", ""), this, 0,
					checkRedeemCodeHanler);
		} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {// 寄养
			CommUtil.checkRedeemCode(yqm, 0, orderFee, "HOTEL", fc.shopid,
					fc.roomid, null, spUtil.getString("cellphone", ""), this,
					0, checkRedeemCodeHanler);
		} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM || type == 4) {// 训练
			CommUtil.checkRedeemCode(yqm, 0, (int) payfee, "TRAIN", shopId, 0,
					petid + "", spUtil.getString("cellphone", ""), this,
					trainServiceEvery.id, checkRedeemCodeHanler);
		}
	}

	private AsyncHttpResponseHandler checkRedeemCodeHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("校验邀请码：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					isCheckRedeem = true;
					localFee = payfee;
					if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {// 游泳
						payfee = payfee - swimPayPrice;
						tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
						iv_orderpay_dhm
								.setBackgroundResource(R.drawable.complaint_reason);
						tv_orderpay_dhm.setVisibility(View.VISIBLE);
						setSwimTc(listPets, true);
					} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {// 待付款
						if (type == 2) {// 寄养
							payfee = payfee - orderFee;
							setDHM(true, orderFee, orderFee);
						} else if (type == 3) {// 游泳
							payfee = payfee - setSwinPayPrice(mpsList);
							setSwimChangePay(mpsList, true);
						} else if (type == 4
								|| previous == Global.TRAIN_TO_ORDER_CONFIRM) {
							if (jobj.has("data") && !jobj.isNull("data")) {
								JSONObject object = jobj.getJSONObject("data");
								if (object.has("orderFee")
										&& !object.isNull("orderFee")) {
									orderFee = object.getInt("orderFee");
								} else {
									orderFee = 0;
								}
							}
							payfee = payfee - swimPayPrice;
							setDHM(true, orderFee, orderFee);
						}
					} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证
						payfee = payfee - fee / 100;
					} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {// 寄养
						payfee = payfee - orderFee;
						setDHM(true, orderFee, orderFee);
					} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM) {
						if (jobj.has("data") && !jobj.isNull("data")) {
							JSONObject object = jobj.getJSONObject("data");
							if (object.has("orderFee")
									&& !object.isNull("orderFee")) {
								orderFee = object.getInt("orderFee");
							} else {
								orderFee = 0;
							}
						}
						payfee = payfee - orderFee;
						setDHM(true, orderFee, orderFee);
					}
					tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
					iv_orderpay_dhm
							.setBackgroundResource(R.drawable.complaint_reason);
					tv_orderpay_dhm.setVisibility(View.VISIBLE);
					isHybird();
					setNeedFee();
				} else {
					if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {// 游泳
						setSwimTc(listPets, false);
					} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {// 待付款
						if (type == 2) {// 寄养
							setDHM(false, orderFee, orderFee);
						} else if (type == 3) {// 游泳
							setSwimChangePay(mpsList, false);
						} else if (type == 4
								|| previous == Global.TRAIN_TO_ORDER_CONFIRM) {
							setDHM(false, orderFee, orderFee);
						}
					} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证

					} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {// 寄养
						setDHM(false, orderFee, orderFee);
					} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM
							|| type == 4) {
						setDHM(false, orderFee, orderFee);
					}
					if (previous == Global.TRAIN_TO_ORDER_CONFIRM || type == 4) {
						iv_orderpay_dhm
								.setBackgroundResource(R.drawable.complaint_reason_disable);
						tv_orderpay_dhm.setVisibility(View.GONE);
						yqm = null;
					} else {
						iv_orderpay_dhm
								.setBackgroundResource(R.drawable.complaint_reason_disable);
						tv_orderpay_dhm.setVisibility(View.GONE);
					}
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(OrderPayActivity.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "请求失败");
			btPay.setEnabled(true);
		}
	};

	private void changePayWay() {
		pDialog.showDialog();
		CommUtil.changePayWay(yqm, isHybirdPay, balance * 100, payfee * 100,
				spUtil.getString("cellphone", ""), Global.getIMEI(this), this,
				orderNo, spUtil.getInt("userid", 0), paytype, tcid,
				changePayWayHanler);
	}

	private AsyncHttpResponseHandler changePayWayHanler = new AsyncHttpResponseHandler() {
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
			pDialog.closeDialog();
			btPay.setEnabled(true);
			Utils.mLogError("狗证订单二次支付：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					JSONObject jdata = null;
					if (jobj.has("data") && !jobj.isNull("data")) {
						jdata = jobj.getJSONObject("data");
						if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
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
						}
					}
					if (needpayfee == 0) {
						ToastUtil.showToastShortBottom(OrderPayActivity.this,
								"支付成功");
						// 直接跳转到支付成功
						goPayResult();
					} else {
						// 第三方支付
						goPay();
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(OrderPayActivity.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "请求失败");
			btPay.setEnabled(true);
		}
	};

	private void setBalanceAvaiable(boolean flag) {
		if (flag) {
			isRecharge = false;
			ivBalance.setVisibility(View.VISIBLE);
			tvBalanceHint.setVisibility(View.GONE);
		} else {
			isRecharge = true;
			ivBalance.setVisibility(View.GONE);
			tvBalanceHint.setVisibility(View.VISIBLE);
		}
	}

	private void goNextForData(Class clazz, int requestcode) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous",
				Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY);
		intent.putExtra("remarks", getIntent().getStringArrayExtra("remarks"));
		startActivityForResult(intent, requestcode);
	}

	private void goPayResult() {
		if (previous == Global.BATH_TO_ORDERPAY
				|| previous == Global.SWIM_DETAIL_TO_ORDERDETAIL
				|| previous == Global.URGENT_TO_ORDERDETAIL
				|| previous == Global.MIPCA_TO_ORDERPAY
				|| previous == Global.TRAIN_TO_ORDER_CONFIRM
				|| previous == Global.CARDSDETAIL_TO_ORDERPAY
				|| previous == Global.UPDATE_TO_ORDERPAY) {
			goPaySuccess();
		} else if (previous == Global.FOSTERCARE_TO_ORDERPAY) {
			if (FostercareChooseroomActivity.act != null)
				FostercareChooseroomActivity.act.finishWithAnimation();
			if (FostercareShopListActivity.act != null)
				FostercareShopListActivity.act.finish();
			if (FostercareChooseroomActivity.act != null)
				FostercareChooseroomActivity.act.finish();
			if (FostercareOrderConfirmActivity.act != null)
				FostercareOrderConfirmActivity.act.finish();
			goFostercarePaySuccess(mpsList);
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY) {
			if (OrderDetailFromOrderActivity.act != null) {
				OrderDetailFromOrderActivity.act.finish();
			}
			if (type == 2) {
				goFostercarePaySuccess(mpsList);
			} else {
				goPaySuccess();
			}
		} else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
			if (CustomerPetInfoActivity.act != null) {
				CustomerPetInfoActivity.act.finishWithAnimation();
			}
			if (ADActivity.act != null) {
				ADActivity.act.finishWithAnimation();
			}
			goPaySuccess();
		}
		finishWithAnimation();
	}

	private void goPaySuccess() {
		Intent intent = new Intent(act, PaySuccessActivity.class);
		if (mpsList != null && mpsList.size() > 0) {
			MulPetService mulPetService = mpsList.get(0);
			if (mulPetService != null) {
				intent.putExtra("petCustomerId", mulPetService.petCustomerId);
			}
		}
		intent.putParcelableArrayListExtra("mpslist", mpsList);
		intent.putExtra("previous", previous);
		intent.putExtra("previous_liucheng", previous_liucheng);
		Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000 --- "+previous_liucheng);
		if (previous == Global.UPDATE_TO_ORDERPAY) {
			intent.putExtra("orderId", orderNoUpOrder);
		} else if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {
			intent.putExtra("orderId", listPets.get(0).orderid);
		} else if (previous == Global.TRAIN_TO_ORDER_CONFIRM || type == 4) {
			intent.putExtra("orderId", orderNo);
			intent.putExtra("type", type);
			Utils.mLogError("==-->orderId:= 训练 ：= " + orderNo);
		} else {
			if (previous != Global.MIPCA_TO_ORDERPAY) {
				if (mpsList != null && mpsList.size() > 0) {
					intent.putExtra("orderId", mpsList.get(0).orderid);
				}
			} else {
				intent.putExtra("listItems", (Serializable) listItems);
				intent.putExtra("totalPayPrice", totalPayPrice);
			}
		}
		startActivity(intent);
		finishWithAnimation();
	}

	private void goFostercarePaySuccess(ArrayList<MulPetService> mpstmpList) {
		Intent intent = new Intent(this, FostercarePaysuccessActivity.class);
		intent.putParcelableArrayListExtra("mulpetservice", mpstmpList);
		startActivity(intent);
		finishWithAnimation();
	}

	private String getPetID_ServiceId_MyPetId_ItemIds(
			ArrayList<MulPetService> list) {
		if (list == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			MulPetService mps = list.get(i);
			if (i < list.size() - 1) {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
				sb.append("_");

			} else {
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if (mps.addServiceIds != null && !"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
			}
		}
		return sb.toString();
	}

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
				PayUtils.weChatPayment(OrderPayActivity.this, appid, partnerid,
						prepayid, packageValue, noncestr, timestamp, sign,
						pDialog);
			} else {
				ToastUtil.showToastShortBottom(OrderPayActivity.this, "支付参数错误");
			}
		} else if (paytype == 2) {
			if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
				// 判断是否安装支付宝
				PayUtils.checkAliPay(OrderPayActivity.this, aliHandler);
			} else {
				ToastUtil.showToastShortBottom(OrderPayActivity.this, "支付参数错误");
			}
		}
	}

	private void getBalance() {
		pDialog.showDialog();
		CommUtil.getAccountBalance(this, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				balanceHanler);
	}

	/**
	 * 二次支付
	 * 
	 * @param foodfee
	 *            寄养为宠粮费用，洗美为单项费用 foodfee不为 -1 表示选择了摄像头（以前代表宠粮 后期宠粮 自带 代表意思改变）
	 * @param balance
	 *            混合支付费用
	 */
	private void changepay(double foodfee, double balance) {
		pDialog.showDialog();
		if (type != 2) {
			bathPetIds = null;
		}
		CommUtil.changeOrderPayMethodV2(yqm, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderNo,
				spUtil.getInt("userid", 0), cname, cphone, remark,
				fIntent.getIntExtra("pickup", 0),
				fIntent.getIntExtra("needbath", -1),
				Utils.formatDouble(foodfee, 2), Utils.formatDouble(payfee, 2),
				paytype, couponid, Utils.formatDouble(balance, 2), bathPetIds,
				isVie, listIds, changeHanler);

	}

	// 订单升级确认二次支付
	private void changeUpdatePay(double balance) {
		pDialog.showDialog();
		CommUtil.changeOrderPayMethodV2(null,
				spUtil.getString("cellphone", ""), Global.getIMEI(this), this,
				orderNo, spUtil.getInt("userid", 0), null, null, null,
				/* pickup */-1, -1, -1, Utils.formatDouble(payfee), paytype, 0,
				Utils.formatDouble(balance, 2), null, false, listIds,
				changeHanler);
	}

	/**
	 * 待付款支付
	 */
	private void payByOrder(double balance) {
		pDialog.showDialog();
		CommUtil.changeOrderPayMethodV2(yqm, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), this, orderNo,
				spUtil.getInt("userid", 0), null, null, null, -1,
				fIntent.getIntExtra("needbath", -1),
				fIntent.getIntExtra("cameraPrice", -1),
				Utils.formatDouble(payfee, 2), paytype, couponid,
				Utils.formatDouble(balance, 2), bathPetIds, isVie, listIds,
				changeHanler);
	}

	private void SwimOrder(double balance) {
		pDialog.showDialog();
		CommUtil.getSwimOrder(mContext, spUtil.getString("cellphone", ""),
				shopId, 0, couponid, 0, addrid, cname, cphone, addr, lat, lng,
				dataString, Utils.formatDouble(totalfee, 2),
				Utils.formatDouble(payfee, 2), paytype, remark, 1, 0, 0,
				getPetId_ServiceId_MyPetId(listPets),
				Utils.formatDouble(balance, 2), uuid.toString(), yqm,
				orderHanler);
	}

	private void TwoTrainPay(double balance) {
		pDialog.showDialog();
		CommUtil.TwoTrainPay(mContext, spUtil.getString("cellphone", ""),
				couponid, spUtil.getInt("userid", 0), cphone, cname,
				Utils.formatDouble(balance, 2), orderNo,
				Utils.formatDouble(payfee, 2), paytype, yqm, changeHanler);
	}

	private void TrainPay(double balance) {
		pDialog.showDialog();
		CommUtil.trainPay(mContext, spUtil.getString("cellphone", ""),
				TrainTime, transfer.id, couponid, cphone, cname,
				Utils.formatDouble(balance, 2), remark, transfer.lat,
				transfer.lng, Utils.formatDouble(payfee, 2), paytype, shopId,
				getStrp(), uuid, 4, yqm, newOrderHandler);
	}

	private void sendIsExOrder(double balance) {
		pDialog.showDialog();
		CommUtil.saveOrder(mContext, spUtil.getString("cellphone", ""),
				spUtil.getInt("userid", 0), clicksort, serviceloc, couponid,
				getPetID_ServiceId_MyPetId_ItemIds(mpsList), ps.addr_detail,
				addressid, lng, month, times, lat, spUtil.getInt("tareaid", 0),
				shopId, paytype, Utils.formatDouble(payfee, 2),
				Utils.formatDouble(balance, 2), pickup, cname, cphone, remark,
				listIds, orderHanler);
	}

	private void sendOrder(double balance) {
		pDialog.showDialog();
		CommUtil.newOrder(this, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				ps.petstoreid, ps.areaid, couponid, ps.beautician_id,
				ps.addr_id, cname, cphone, ps.addr_detail, ps.addr_lat,
				ps.addr_lng, ps.servicedate, Utils.formatDouble(totalfee, 2),
				Utils.formatDouble(payfee, 2), paytype, remark, ps.addrtype,
				fIntent.getIntExtra("pickup", 0), ps.isDefaultWorker,
				getPetID_ServiceId_MyPetId_ItemIds(mpsList),
				Utils.formatDouble(balance, 2), uuid.toString(), listIds,
				orderHanler);

	}

	/**
	 * 寄养下单 foodfee不为 -1 表示选择了摄像头（以前代表宠粮 后期宠粮 自带 代表意思改变）
	 */
	private void newOrder(double foodfee, double balance) {
		pDialog.showDialog();
		StringBuilder sb = new StringBuilder();
		sb.append(fc.startdate);
		sb.append(" 00:00:00");
		StringBuilder sb1 = new StringBuilder();
		sb1.append(fc.enddate);
		sb1.append(" 00:00:00");
		CommUtil.newOrderFostercare(this, yqm,
				spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this), fc.roomid, fc.petid, fc.shopid,
				fc.customerpetid, couponid, fc.addrid, cname, cphone, fc.addr,
				fc.lat, fc.lng, sb.toString(), sb1.toString(),
				Utils.formatDouble(totalfee, 2),
				Utils.formatDouble(fc.roomlistprice, 2),
				Utils.formatDouble(payfee, 2), paytype, remark,
				fIntent.getIntExtra("pickup", 0),
				fIntent.getIntExtra("needbath", 0),
				Utils.formatDouble(foodfee, 2), Utils.formatDouble(balance, 2),
				uuid.toString(), fc.transferId, bathPetIds, newOrderHandler);

	}

	private void BuyCards(double balance) {
		mPDialog.showDialog();
		if (CardMakeDog != null) {
			if (CardMakeDog.isChoose) {
				CommUtil.BuyCard(mContext, id, petid, CardMakeDog.petCard,
						payfee, balance, paytype, cardsBuyEvery.CardPackageId,
						BuycardDetail);
			} else {
				CommUtil.BuyCard(mContext, id, petid, -1, payfee, balance,
						paytype, cardsBuyEvery.CardPackageId, BuycardDetail);
			}
		} else {
			CommUtil.BuyCard(mContext, id, petid, -1, payfee, balance, paytype,
					cardsBuyEvery.CardPackageId, BuycardDetail);
		}
	}

	private AsyncHttpResponseHandler BuycardDetail = new AsyncHttpResponseHandler() {

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
			Utils.mLogError("==-->次卡购买 ：= " + new String(responseBody));
			mPDialog.closeDialog();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data") && !object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("detail")
								&& !objectData.isNull("detail")) {
							servicename = objectData.getString("detail");
						}
						if (objectData.has("payInfo")
								&& !objectData.isNull("payInfo")) {
							JSONObject jpayInfo = objectData
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
						}
						if (needpayfee == 0) {
							// 直接跳转到支付成功
							goPayResult();
						} else {
							// 第三方支付
							goPay();
						}
					}
				} else {
					ToastUtil.showToastShortCenter(mContext,
							object.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			mPDialog.closeDialog();
		}
	};
	private AsyncHttpResponseHandler balanceHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("账户余额：" + new String(responseBody));
			pDialog.closeDialog();
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
					tvBalance.setText(Double.toString(Utils.formatDouble(
							balance, 2)));
					isHybird();
					setNeedFee();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private void isHybird() {
		if (balance >= payfee) {
			isManZu = true;
			tvHybirdPayFee.setText("¥0.00");
			setBalanceAvaiable(true);
			rlHybirdPay.setVisibility(View.GONE);
			llHybirdPay.setVisibility(View.GONE);
			if (isRechargeReturn) {
				// 充值返回，默认选余额
				oldpayway = 4;
				paytype = 4;
				needpayfee = 0;
				ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				iv_orderpay_cika
						.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
				ivBalance.setBackgroundResource(R.drawable.complaint_reason);
			} else {
				paytype = oldpayway;
				if (oldpayway == 2) {
					oldpayway = 2;
					paytype = 2;
					needpayfee = payfee;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason);
				} else if (oldpayway == 1) {
					oldpayway = 1;
					paytype = 1;
					needpayfee = payfee;
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason);
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
				} else {
					oldpayway = 4;
					paytype = 4;
					needpayfee = 0;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason);
				}
			}
		} else {
			isManZu = false;
			if (fee > 0) {
				tvHybirdPayFee.setText(Double.toString(Utils.formatDouble(
						payfee - balance, 2)));
				setBalanceAvaiable(false);
				if (balance <= 0) {
					rlHybirdPay.setVisibility(View.GONE);
					llHybirdPay.setVisibility(View.GONE);
				} else {
					if (isHunHePayWays) {
						rlHybirdPay.setVisibility(View.VISIBLE);
					} else {
						rlHybirdPay.setVisibility(View.GONE);
						llHybirdPay.setVisibility(View.GONE);
					}
				}
				paytype = oldpayway;
				if (oldpayway == 2) {
					oldpayway = 2;
					paytype = 2;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason);
				} else {
					oldpayway = 1;
					paytype = 1;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
				}
			} else {
				tvHybirdPayFee.setText(Double.toString(Utils.formatDouble(
						payfee - balance, 2)));
				setBalanceAvaiable(false);
				if (balance <= 0) {
					rlHybirdPay.setVisibility(View.GONE);
					llHybirdPay.setVisibility(View.GONE);
				} else {
					if (isHunHePayWays) {
						rlHybirdPay.setVisibility(View.VISIBLE);
					} else {
						rlHybirdPay.setVisibility(View.GONE);
						llHybirdPay.setVisibility(View.GONE);
					}
				}
				paytype = oldpayway;
				if (oldpayway == 2) {
					oldpayway = 2;
					paytype = 2;
					needpayfee = payfee;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason);
				} else {
					oldpayway = 1;
					paytype = 1;
					needpayfee = payfee;
					iv_orderpay_cika
							.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivWXPay.setBackgroundResource(R.drawable.complaint_reason);
					ivAliPay.setBackgroundResource(R.drawable.complaint_reason_disable);
					ivBalance
							.setBackgroundResource(R.drawable.complaint_reason_disable);
				}
			}
		}
		if (isHybirdPay) {
			llHybirdPay.setVisibility(View.VISIBLE);
		} else {
			llHybirdPay.setVisibility(View.GONE);
		}
	}

	private AsyncHttpResponseHandler orderHanler = new AsyncHttpResponseHandler() {

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
			pDialog.closeDialog();
			Utils.mLogError("生成新订单：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					spUtil.saveInt("payway", paytype);
					JSONObject jData = jobj.getJSONObject("data");
					if (jData.has("orderId") && !jData.isNull("orderId")) {
						orderNo = Integer.parseInt(jData.getString("orderId"));
						try {
							mpsList.get(0).orderid = orderNo;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							listPets.get(0).orderid = orderNo;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (jData.has("orderPet") && !jData.isNull("orderPet")) {
						JSONArray jarr = jData.getJSONArray("orderPet");
						try {
							for (int i = 0; i < jarr.length()
									&& i < mpsList.size(); i++) {
								mpsList.get(i).orderid = jarr.getInt(i);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (jData.has("payInfo") && !jData.isNull("payInfo")) {
						JSONObject jpayInfo = jData.getJSONObject("payInfo");
						if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
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
						if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
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
					}
					if (needpayfee == 0 || (paytype == 4 && balance >= payfee)) {
						// 直接跳转到支付成功
						goPayResult();
					} else {
						Utils.mLogError("用第三方支付" + needpayfee);
						goPay();
					}
				} else if (109 == resultCode) {
					// 重复提交，不做处理
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(OrderPayActivity.this,
								jobj.getString("msg"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "请求失败");
		}

	};

	private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler() {

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
			pDialog.closeDialog();
			Utils.mLogError("重新支付：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("orderPet") && !jdata.isNull("orderPet")) {
						JSONArray jarr = jdata.getJSONArray("orderPet");
						try {
							for (int i = 0; i < jarr.length()
									&& i < mpsList.size(); i++) {
								mpsList.get(i).orderid = jarr.getInt(i);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (jdata.has("detail") && !jdata.isNull("detail")) {
						servicename = jdata.getString("detail");
					}
					if (jdata.has("orderId") && !jdata.isNull("orderId")) {
						if (previous != Global.UPDATE_TO_ORDERPAY) {
							orderNo = jdata.getInt("orderId");
						}else {
							orderNoUpOrder = jdata.getInt("orderId");
						}
					}
					if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
						JSONObject jpayInfo = jdata.getJSONObject("payInfo");
						if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
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
						if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
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
					}
					if (needpayfee == 0) {
						// 直接跳转到支付成功
						goPayResult();
					} else {
						// 第三方支付
						goPay();
					}
				} else {
					String msg = jobj.getString("msg");
					ToastUtil.showToastShort(OrderPayActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "网络异常，请重新提交");
		}
	};

	private AsyncHttpResponseHandler newOrderHandler = new AsyncHttpResponseHandler() {

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
			pDialog.closeDialog();
			btPay.setEnabled(true);
			Utils.mLogError("寄养 / 训练生成新订单：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					spUtil.saveInt("payway", paytype);
					JSONObject jData = jobj.getJSONObject("data");
					if (jData.has("orderId") && !jData.isNull("orderId")) {
						orderNo = Integer.parseInt(jData.getString("orderId"));
					}
					if (jData.has("detail") && !jData.isNull("detail")) {
						servicename = jData.getString("detail");
					}
					if (jData.has("orderPet") && !jData.isNull("orderPet")) {
						JSONArray jarr = jData.getJSONArray("orderPet");
						try {
							for (int i = 0; i < jarr.length()
									&& i < mpsList.size(); i++) {
								mpsList.get(i).orderid = jarr.getInt(i);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (jData.has("payInfo") && !jData.isNull("payInfo")) {
						JSONObject jpayInfo = jData.getJSONObject("payInfo");
						if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
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
						if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
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
					}
					if (needpayfee == 0 || (paytype == 4 && balance >= payfee)) {
						// 直接跳转到支付成功
						goPayResult();
					} else {
						Utils.mLogError("用第三方支付" + needpayfee);
						goPay();
					}
				} else if (109 == resultCode) {
					// 重复提交，不做处理
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(OrderPayActivity.this,
								jobj.getString("msg"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "网络异常，请重新提交");
			btPay.setEnabled(true);
		}

	};

	private void goBack() {
		Intent data = new Intent();
		if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
			data.putExtra("payurl", CommUtil.getWebBaseUrl()
					+ "web/petcerti/register?certi_id=" + certiId);
		} else {
			data.putExtra("orderid", orderNo);
		}
		setResult(Global.RESULT_OK, data);
		finishWithAnimation();
	}

	private void initWXPayReceiver() {
		wxPayReceiver = new MCloseReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.orderpay");
		// 注册广播接收器
		registerReceiver(wxPayReceiver, filter);
	}

	private class MCloseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Utils.mLogError("222开始接收广播");
			goPayResult();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (wxPayReceiver != null)
			unregisterReceiver(wxPayReceiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.PRE_ORDERDETAIL_TO_RECHARGE) {
				isRechargeReturn = true;
				getBalance();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	public String getPetId_ServiceId_MyPetId(ArrayList<Pet> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Pet mps = list.get(i);
			if (i < list.size() - 1) {
				sb.append(mps.id);
				sb.append("_");
				sb.append(mps.serviceid);
				sb.append("_");
				sb.append(mps.customerpetid);
				sb.append("_");
			} else {
				sb.append(mps.id);
				sb.append("_");
				sb.append(mps.serviceid);
				sb.append("_");
				sb.append(mps.customerpetid);
			}
		}
		return sb.toString();

	}

	public String getPetId_ServiceId_MyPetIdSwim(ArrayList<Pet> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Pet mps = list.get(i);
			sb.append(mps.id + ",");
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public String getPetId_ServiceId_MyPetIdFromOrder(
			ArrayList<MulPetService> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			MulPetService mps = list.get(i);
			sb.append(mps.petId + ",");
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public String getServiceName(ArrayList<MulPetService> list) {
		if (list.size() <= 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).serviceId + ",");
		}
		return sb.toString();

	}

	private void getOrderInfo() {
		CommUtil.getOrderInfo(this, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				CertiOrderId, orderInfoHanler);
	}

	private AsyncHttpResponseHandler orderInfoHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String result = new String(responseBody);
			Utils.mLogError("办理狗证订单信息：" + result);
			processData(result);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(OrderPayActivity.this, "网络异常，请重新提交");
		}
	};

	// 解析json数据
	private void processData(String result) {

		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj.has("code") && !jobj.isNull("code")) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						Gson gson = new Gson();
						PetCardInfoCodeBean data = gson.fromJson(
								jdata.toString(), PetCardInfoCodeBean.class);
						if (data != null) {
							certiId = data.getCertiId();
							payfee = data.getTotalPrice() / 100;
							localFee = payfee;
							certiCoupons = data.getCertiCoupons();
							String certiCouponDe = data.getCertiCouponDe();
							if (certiCouponDe != null
									&& !TextUtils.isEmpty(certiCouponDe)) {
								tv_orderpay_des.setText(certiCouponDe);
								vw_line3.setVisibility(View.VISIBLE);
								tv_orderpay_des.setVisibility(View.VISIBLE);
							} else {
								vw_line3.setVisibility(View.GONE);
								tv_orderpay_des.setVisibility(View.GONE);
							}
							tvPayFee.setText("¥"
									+ Utils.formatDouble(payfee, 2));
							if (certiCoupons != null && certiCoupons.size() > 0) {
								ll_orderpay_tc.setVisibility(View.VISIBLE);
								orderPayTcAdapter = new OrderPayTcAdapter(this,
										certiCoupons);
								lv_orderpay_tc.setAdapter(orderPayTcAdapter);
								// 默认选择第一个套餐
								orderPayTcAdapter.setOnItemClick(0);
								CertiCouponsBean certiCouponsBean = certiCoupons
										.get(0);
								setTc(certiCouponsBean);
								isHybird();
								setNeedFee();
								if (certiCoupons.size() == 1) {
									String description = certiCoupons.get(0)
											.getDescription();
									if (description != null
											&& !TextUtils.isEmpty(description)) {
										lv_orderpay_tc
												.setVisibility(View.VISIBLE);
									} else {
										lv_orderpay_tc.setVisibility(View.GONE);
									}
								}
							} else {
								lv_orderpay_tc.setVisibility(View.GONE);
							}
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(this, msg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getStrp() {
		StringBuilder builder = new StringBuilder();
		builder.append(petid + "_");
		builder.append(trainServiceEvery.id + "_");
		builder.append(customerpetid + "");
		return builder.toString();
	}
}
