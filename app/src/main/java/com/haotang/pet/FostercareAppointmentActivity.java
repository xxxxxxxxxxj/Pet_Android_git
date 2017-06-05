package com.haotang.pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.dao.PetAddressDao;
import com.haotang.pet.entity.Addr;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PetAddressInfo;
import com.haotang.pet.entity.ShopsWithPrice;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FostercareAppointmentActivity extends SuperActivity implements
		OnClickListener {
	private final static long DAYTIMEINMILLS = 86400000;
	public static SuperActivity act;
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshScrollView prsScrollView;
	private ImageView ivImage;
	private RelativeLayout rlChangePet;
	private TextView tvPetName;
	private RelativeLayout rlChangeAddr;
	private TextView tvAddr;
	private RelativeLayout rlChangeDate;
	private TextView tvDayStart;
	private TextView tvDayEnd;
	private TextView tvDayNum;
	private RelativeLayout rlNull;
	private TextView tvNullmsg;
	private Button btRefresh;
	private SharedPreferenceUtil spUtil;
	private Intent fIntent;
	private int servicetype;
	private int serviceid;
	private int previous;
	private Pet pet;
	private Addr addr;
	private String startdate;
	private String enddate;
	private String startdatetxt;
	private String enddatetxt;
	private int daynum;
	private TextView tvAgreement;
	private CheckBox cbAgree;
	private Button btSubmit;
	private int addrSize;
	private MProgressDialog pDialog;
	private int monthposition;
	private int customerpetid;
	private String bannerH5Url;
	private PetAddressDao petAddressDao;
	private ArrayList<ShopsWithPrice> shopList = new ArrayList<ShopsWithPrice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercare_appointment);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		petAddressDao = new PetAddressDao(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		fIntent = getIntent();
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);

		prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_fostercareappointment_main);
		ivImage = (ImageView) findViewById(R.id.iv_fostercareappointment_fostercareimg);
		rlChangePet = (RelativeLayout) findViewById(R.id.rl_fostercareappointment_changepet);
		tvPetName = (TextView) findViewById(R.id.tv_fostercareappointment_petname);
		rlChangeAddr = (RelativeLayout) findViewById(R.id.rl_fostercareappointment_addr);
		tvAddr = (TextView) findViewById(R.id.tv_fostercareappointment_addr);
		rlChangeDate = (RelativeLayout) findViewById(R.id.rl_fostercareappointment_time);
		tvDayStart = (TextView) findViewById(R.id.tv_fostercareappointment_timestart);
		tvDayEnd = (TextView) findViewById(R.id.tv_fostercareappointment_timeend);
		tvDayNum = (TextView) findViewById(R.id.tv_fostercareappointment_timedays);
		tvAgreement = (TextView) findViewById(R.id.tv_fostercareappointment_agreement);
		cbAgree = (CheckBox) findViewById(R.id.cb_fostercareappointment_agree);
		btSubmit = (Button) findViewById(R.id.bt_fostercareappointment_submit);

		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvNullmsg = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
	}

	private void setView() {
		tvTitle.setText("寄养");
		tvNullmsg.setText("网络异常或链接服务器失败");
		btRefresh.setVisibility(View.VISIBLE);
		pet = new Pet();
		addr = new Addr();
		previous = fIntent.getIntExtra("previous", -1);
		servicetype = fIntent.getIntExtra("servicetype", 0);
		serviceid = fIntent.getIntExtra("serviceid", 0);
		customerpetid = fIntent.getIntExtra("customerpetid", 0);
		if (previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET) {
			pet.id = spUtil.getInt("petid", 0);
			pet.kindid = spUtil.getInt("petkind", 0);
			pet.name = spUtil.getString("petname", "");
			customerpetid = spUtil.getInt("customerpetid", 0);
			pet.customerpetid = customerpetid;
			pet.nickName = spUtil.getString("customerpetname", "");
			if (customerpetid > 0)
				tvPetName.setText(pet.nickName);
			else
				tvPetName.setText(pet.name);

		} else if (previous == Global.PRE_CUSTOMERPETINFO_TO_FOSTERCAREAPPOINTMENT_PET) {
			pet.id = fIntent.getIntExtra("petid", 0);
			pet.kindid = fIntent.getIntExtra("petkind", 0);
			pet.name = fIntent.getStringExtra("petname");
			pet.customerpetid = customerpetid;
			pet.nickName = fIntent.getStringExtra("customerpetname");
			if (customerpetid > 0)
				tvPetName.setText(pet.nickName);
			else
				tvPetName.setText(pet.name);
		} else {
			pet.id = fIntent.getIntExtra("petid", 0);
			pet.kindid = fIntent.getIntExtra("petkind", 0);
			pet.name = fIntent.getStringExtra("petname");
			pet.customerpetid = customerpetid;
			pet.nickName = fIntent.getStringExtra("customerpetname");
			if (customerpetid > 0)
				tvPetName.setText(pet.nickName);
			else
				tvPetName.setText(pet.name);

		}
		// 设置地址
		String addressName = spUtil.getString("addressName", "");
		if (!"".equals(spUtil.getString("newaddr", ""))
				&& !"".equals(spUtil.getString("newlat", ""))
				&& !"".equals(spUtil.getString("newlng", ""))) {// 选择地址
			addr.id = spUtil.getInt("newaddrid", 0);
			addr.detail = spUtil.getString("newaddr", "");
			addr.lat = Double.parseDouble(spUtil.getString("newlat", "0"));
			addr.lng = Double.parseDouble(spUtil.getString("newlng", "0"));
		} else if (Utils.isStrNull(addressName)) {// 用户在首页填的地址
			addr.detail = addressName + spUtil.getString("xxAddressName", "");
			List<PetAddressInfo> all = petAddressDao.getAll();
			if (all != null) {
				if (all.size() > 0) {
					PetAddressInfo petAddressInfo = all.get(0);
					addr.lat = petAddressInfo.getPet_address_lat();
					addr.lng = petAddressInfo.getPet_address_lng();
				}
			}
		} else {
			if (spUtil.getInt("addressid", 0) > 0
					&& !"".equals(spUtil.getString("address", ""))) {
				addr.id = spUtil.getInt("addressid", 0);
				addr.detail = spUtil.getString("address", "");
				addr.lat = Double.parseDouble(spUtil.getString("lat", "0"));
				addr.lng = Double.parseDouble(spUtil.getString("lng", "0"));
			}
		}
		tvAddr.setText(addr.detail);
		ibBack.setOnClickListener(this);
		rlChangeAddr.setOnClickListener(this);
		rlChangeDate.setOnClickListener(this);
		rlChangePet.setOnClickListener(this);
		tvAgreement.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		ivImage.setOnClickListener(this);

		prsScrollView.setMode(Mode.PULL_FROM_START);
		prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					getData();
				}
			}
		});

		getServiceAddrList();
		getData();
	}

	private AsyncHttpResponseHandler shoplistHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("店铺列表： " + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
					JSONArray jarr = jobj.getJSONArray("data");
					for (int i = 0; i < jarr.length(); i++) {
						shopList.add(ShopsWithPrice.json2Entity(jarr
								.getJSONObject(i)));
					}
					if (jarr.length() > 0) {
						if (jarr.length() == 1) {
							if (shopList.get(0).status != 2) {
								Intent intent = new Intent(
										FostercareAppointmentActivity.this,
										FostercareChooseroomActivity.class);
								intent.putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_RIGHT());
								getIntent().putExtra(Global.ANIM_DIRECTION(),
										Global.ANIM_COVER_FROM_LEFT());
								intent.putExtra("petid", pet.id);
								intent.putExtra("shopid",
										shopList.get(0).shopId);
								intent.putExtra("petname", pet.name);
								intent.putExtra("petkind", pet.kindid);
								intent.putExtra("customerpetid", customerpetid);
								intent.putExtra("customerpetname", pet.nickName);
								intent.putExtra("addrid", addr.id);
								intent.putExtra("addr", addr.detail);
								intent.putExtra("lat", addr.lat);
								intent.putExtra("lng", addr.lng);
								intent.putExtra("startdate", startdate);
								intent.putExtra("enddate", enddate);
								intent.putExtra("startdatetxt", startdatetxt);
								intent.putExtra("enddatetxt", enddatetxt);
								intent.putExtra("daynum", daynum);
								intent.putExtra("monthposition", monthposition);
								startActivityForResult(intent,
										Global.SHOPLIST_TO_ROOMLIST);
							}
						} else {
							Intent intent = new Intent(
									FostercareAppointmentActivity.this,
									FostercareShopListActivity.class);
							intent.putExtra(Global.ANIM_DIRECTION(),
									Global.ANIM_COVER_FROM_RIGHT());
							getIntent().putExtra(Global.ANIM_DIRECTION(),
									Global.ANIM_COVER_FROM_LEFT());
							intent.putExtra("petid", pet.id);
							intent.putExtra("petname", pet.name);
							intent.putExtra("petkind", pet.kindid);
							intent.putExtra("customerpetid", customerpetid);
							intent.putExtra("customerpetname", pet.nickName);
							intent.putExtra("addrid", addr.id);
							intent.putExtra("addr", addr.detail);
							intent.putExtra("lat", addr.lat);
							intent.putExtra("lng", addr.lng);
							intent.putExtra("startdate", startdate);
							intent.putExtra("enddate", enddate);
							intent.putExtra("startdatetxt", startdatetxt);
							intent.putExtra("enddatetxt", enddatetxt);
							intent.putExtra("daynum", daynum);
							intent.putExtra("monthposition", monthposition);
							startActivityForResult(intent,
									Global.FOSTERCARE_APPOINTMENT_SHOPLIST);
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShortCenter(
								FostercareAppointmentActivity.this,
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
		}
	};

	private void showMain(boolean flag) {
		if (flag) {
			prsScrollView.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			prsScrollView.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			// 返回
			finishWithAnimation();
			break;
		case R.id.rl_fostercareappointment_addr:
			if (pet.id <= 0) {
				ToastUtil.showToastShort(FostercareAppointmentActivity.this,
						"请选择您的宠物");
				return;
			}
			// 地址
			if (spUtil.getInt("userid", 0) > 0
					&& !"".equals(spUtil.getString("cellphone", ""))
					&& addrSize > 0) {
				goNextForData(CommonAddressActivity.class,
						Global.FOSTERCARE_APPOINTMENT_CHANGEADDR,
						Global.BOOKINGSERVICEREQUESTCODE_ADDR);
			} else {
				goNextForData(AddServiceAddressActivity.class,
						Global.FOSTERCARE_APPOINTMENT_CHANGEADDR_LOGOUT,
						Global.PRE_SERVICEACTIVITY_TO_ADDSERICEADDRRESSACTIVITY);
			}
			break;
		case R.id.rl_fostercareappointment_changepet:
			// 切换宠物
			// goNextForData(ChoosePetActivity.class,
			// Global.FOSTERCARE_APPOINTMENT_CHANGEPET,0);
			goNextForData(ChoosePetActivityNew.class,
					Global.FOSTERCARE_APPOINTMENT_CHANGEPET, 0);
			break;
		case R.id.rl_fostercareappointment_time:
			// 日期
			goNextForData(FostercareDateActivity.class,
					Global.FOSTERCARE_APPOINTMENT_SHOPLIST, 0);
			break;
		case R.id.bt_fostercareappointment_submit:
			// 提交预约
			submit();
			break;
		case R.id.bt_null_refresh:
			// 刷新
			getData();
			break;
		case R.id.tv_fostercareappointment_agreement:
			// 协议
			goNext(AgreementActivity.class, Global.FOSTERCARE_TO_AGREEMENT);
			break;
		case R.id.iv_fostercareappointment_fostercareimg:
			// 寄养详情
			goNext(ShopH5DetailActivity.class, Global.FOSTERCARE_TO_AGREEMENT);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.FOSTERCARE_APPOINTMENT_CHANGEPET) {
				// 选择宠物类型返回
				pet.id = data.getIntExtra("petid", 0);
				pet.kindid = data.getIntExtra("petkind", 0);
				pet.customerpetid = data.getIntExtra("customerpetid", 0);
				pet.nickName = data.getStringExtra("customerpetname");
				customerpetid = pet.customerpetid;
				pet.name = data.getStringExtra("petname");
				pet.image = data.getStringExtra("petimage");
				if (customerpetid > 0)
					tvPetName.setText(pet.nickName);
				else
					tvPetName.setText(pet.name);
			} else if (requestCode == Global.FOSTERCARE_APPOINTMENT_CHANGEADDR) {
				if (addr.lat != data.getDoubleExtra("addr_lat", 0)
						|| addr.lng != data.getDoubleExtra("addr_lng", 0)
						|| (data.getStringExtra("addr") != null && !data
								.getStringExtra("addr").trim()
								.equals(addr.detail))) {
					addr.detail = data.getStringExtra("addr");
					tvAddr.setText(addr.detail);
					addr.lat = data.getDoubleExtra("addr_lat", 0);
					addr.lng = data.getDoubleExtra("addr_lng", 0);
					addr.id = data.getIntExtra("addr_id", 0);

					spUtil.saveInt("newaddrid", addr.id);
					spUtil.saveString("newaddr", addr.detail);
					spUtil.saveString("newlat", addr.lat + "");
					spUtil.saveString("newlng", addr.lng + "");

				}
			} else if (requestCode == Global.FOSTERCARE_APPOINTMENT_CHANGEADDR_LOGOUT) {
				if (addr.lat != data.getDoubleExtra("addr_lat", 0)
						|| addr.lng != data.getDoubleExtra("addr_lng", 0)) {
					addr.detail = data.getStringExtra("addr");
					tvAddr.setText(addr.detail);
					addr.lat = data.getDoubleExtra("addr_lat", 0);
					addr.lng = data.getDoubleExtra("addr_lng", 0);
					addr.id = data.getIntExtra("addr_id", 0);

					spUtil.saveInt("newaddrid", addr.id);
					spUtil.saveString("newaddr", addr.detail);
					spUtil.saveString("newlat", addr.lat + "");
					spUtil.saveString("newlng", addr.lng + "");

					getServiceAddrList();
				}
			} else if (requestCode == Global.FOSTERCARE_APPOINTMENT_SHOPLIST) {
				// 日期
				startdate = data.getStringExtra("startdate");
				enddate = data.getStringExtra("enddate");
				monthposition = data.getIntExtra("monthposition", 0);
				daynum = (int) ((getTimeInMills(enddate) - getTimeInMills(startdate)) / DAYTIMEINMILLS);
				String[] arr = startdate.split("-");
				StringBuilder sb = new StringBuilder();
				if (arr.length > 2) {
					sb.append(arr[1]);
					sb.append("月");
					sb.append(arr[2]);
					sb.append("日");
				}
				startdatetxt = sb.toString();
				arr = enddate.split("-");
				StringBuilder sb1 = new StringBuilder();
				if (arr.length > 2) {
					sb1.append(arr[1]);
					sb1.append("月");
					sb1.append(arr[2]);
					sb1.append("日");
				}
				enddatetxt = sb1.toString();

				StringBuilder sb2 = new StringBuilder();
				sb2.append("离店   共");
				sb2.append(daynum);
				sb2.append("天");
				tvDayStart.setText(startdatetxt);
				tvDayEnd.setText(enddatetxt);
				tvDayNum.setText(sb2.toString());

				if (data.getBooleanExtra("fromroom", false))
					getServiceAddrList();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void submit() {
		if (pet.id <= 0) {
			ToastUtil.showToastShortCenter(this, "请选择您的宠物");
			return;
		}
		if (addr.detail == null || "".equals(addr.detail)
				|| (addr.lat == 0 && addr.lng == 0)) {
			ToastUtil.showToastShortCenter(this, "请填写您的宠物地址");
			return;
		}
		if (startdate == null || "".equals(startdate) || enddate == null
				|| "".equals(enddate) || daynum == 0) {
			ToastUtil.showToastShortCenter(this, "请选择您的预约时间");
			return;
		}
		if (!cbAgree.isChecked()) {
			ToastUtil.showToastShortCenter(this, "请同意《宠物家寄养协议》");
			return;
		}

		// 请求店铺列表
		pDialog.showDialog();
		StringBuilder sbstart = new StringBuilder();
		StringBuilder sbend = new StringBuilder();
		sbstart.append(startdate);
		sbstart.append(" 00:00:00");
		sbend.append(enddate);
		sbend.append(" 00:00:00");
		/*CommUtil.getShopList(this, pet.id, sbstart.toString(),
				sbend.toString(), addr.lat, addr.lng, shoplistHandler);*/
	}

	private void goNextForData(Class clazz, int requestcode, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("startdate", startdate);
		intent.putExtra("enddate", enddate);
		intent.putExtra("monthposition", monthposition);
		intent.putExtra("previous", pre);
		intent.putExtra("serviceid", 100);

		intent.putExtra("addrid", addr.id);
		startActivityForResult(intent, requestcode);
	}

	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("orderDetailH5Url", bannerH5Url);
		startActivity(intent);
	}

	private long getTimeInMills(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	private void getServiceAddrList() {
		// TODO 请求服务器判断显示那个布局
		addrSize = 0;
		if (!"".equals(spUtil.getString("cellphone", ""))) {
			CommUtil.queryServiceAddress(spUtil.getString("cellphone", ""),
					spUtil.getInt("userid", 0), Global.getIMEI(this), this,
					queryServiceAddress);
		}
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.getReadyReserve(this, dataHandler);
	}

	private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("查询常用地址： " + new String(responseBody));

			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					addrSize = jsonArray.length();
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
	private AsyncHttpResponseHandler dataHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("寄养预约： " + new String(responseBody));
			pDialog.closeDialog();
			prsScrollView.onRefreshComplete();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0 && jsonObject.has("data")
						&& !jsonObject.isNull("data")) {
					showMain(true);
					JSONObject jdata = jsonObject.getJSONObject("data");

					if (jdata.has("startDateTxt")
							&& !jdata.isNull("startDateTxt")) {
						startdatetxt = jdata.getString("startDateTxt");
						tvDayStart.setText(startdatetxt);
					}
					if (jdata.has("endDateTxt") && !jdata.isNull("endDateTxt")) {
						enddatetxt = jdata.getString("endDateTxt");
						tvDayEnd.setText(enddatetxt);
					}
					if (jdata.has("startDate") && !jdata.isNull("startDate")) {
						startdate = jdata.getString("startDate");
					}
					if (jdata.has("endDate") && !jdata.isNull("endDate")) {
						enddate = jdata.getString("endDate");
					}
					if (jdata.has("bannerH5Url")
							&& !jdata.isNull("bannerH5Url")) {
						bannerH5Url = jdata.getString("bannerH5Url");
					}
					if (jdata.has("totalDay") && !jdata.isNull("totalDay")) {
						daynum = jdata.getInt("totalDay");
						StringBuilder sb = new StringBuilder();
						sb.append("离店   共");
						sb.append(daynum);
						sb.append("天");
						tvDayNum.setText(sb.toString());
					}

					if (jdata.has("pic") && !jdata.isNull("pic")) {
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()
								+ jdata.getString("pic"), ivImage, 0,
								new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0,
											View view) {
										// TODO Auto-generated method stub
										ImageView iv = (ImageView) view;
										iv.setImageResource(R.drawable.bg_fostercare);
									}

									@Override
									public void onLoadingFailed(String arg0,
											View view, FailReason arg2) {
										// TODO Auto-generated method stub
										ImageView iv = (ImageView) view;
										iv.setImageResource(R.drawable.bg_fostercare);
									}

									@Override
									public void onLoadingComplete(String path,
											View view, Bitmap bitmap) {
										// TODO Auto-generated method stub
										ImageView iv = (ImageView) view;
										if (path != null && bitmap != null)
											iv.setImageBitmap(bitmap);
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {
										// TODO Auto-generated method stub

									}
								});
					}
				} else {
					showMain(false);
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(
							FostercareAppointmentActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showMain(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			prsScrollView.onRefreshComplete();
			showMain(false);
		}

	};

}
