package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ServiceAddPetAdapter;
import com.haotang.pet.entity.AddServiceItem;
import com.haotang.pet.entity.MainCharacteristicService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;

public class MulPetAddServiceActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private RelativeLayout rlBath;
	private TextView tvBathPrice;
	private ImageView ivBath;
	private RelativeLayout rlBathInfo;
	private RelativeLayout rlBeauty;
	private TextView tvBeautyPrice;
	private ImageView ivBeauty;
	private RelativeLayout rlBeautyInfo;
	private Button btSubmit;
	private TextView tvMsg1;
	private Button btRefresh;
	private RelativeLayout rlNull;
	private PullToRefreshScrollView prsMain;
	private int petId;
	private int shopId;
	private int servLoc;
	private String time;
	private double lat;
	private double lng;
	private int servicetype;
	private int beautician_sort;
	private int serviceid;
	private boolean isBathSelected;
	private boolean isBeautySelected;
	private int petKind;
	private double addserviceprice;
	private double bathtotalfee;
	private double beautytotalfee;
	private double basefee;
	private double bathPriceLevel1;
	private double bathPriceLevel2;
	private double bathPriceLevel3;
	private double beautyPriceLevel1;
	private double beautyPriceLevel2;
	private double beautyPriceLevel3;
	private double basefeewithbeautician;
	private int previous;
	private Intent fIntent;
	private String servicename;
	private String bathname;
	private String beautyname;
	private boolean isBathAvailable;
	private boolean isBeautyAvailable;
	private int bathserviceid;
	private int beautyserviceid;
	private ServiceAddPetAdapter bathAddServiceAdapter;
	private ServiceAddPetAdapter beautyAddServiceAdapter;
	private ArrayList<AddServiceItem> serviceBathItemsList = new ArrayList<AddServiceItem>();
	private ArrayList<AddServiceItem> serviceBeautyItemsList = new ArrayList<AddServiceItem>();
	private ArrayList<Integer> addServiceIdList = new ArrayList<Integer>();
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private MyGridView gvBathAddService;
	private MyGridView gvBeautyAddService;
	private TextView tvBathTotalFee;
	private TextView tvBeautyTotalFee;
	private LinearLayout llSubmit;
	private LinearLayout llBath;
	private LinearLayout llBeauty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mulpetservice);

		findView();
		setView();
	}

	private void findView() {
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		rlBath = (RelativeLayout) findViewById(R.id.rl_mulpetservice_bath);
		tvBathPrice = (TextView) findViewById(R.id.tv_mulpetservice_bathprice);
		ivBath = (ImageView) findViewById(R.id.iv_mulpetservice_bath);
		rlBathInfo = (RelativeLayout) findViewById(R.id.rl_mulpetservice_bathinfo);

		rlBeauty = (RelativeLayout) findViewById(R.id.rl_mulpetservice_beauty);
		tvBeautyPrice = (TextView) findViewById(R.id.tv_mulpetservice_beautyprice);
		ivBeauty = (ImageView) findViewById(R.id.iv_mulpetservice_beauty);
		rlBeautyInfo = (RelativeLayout) findViewById(R.id.rl_mulpetservice_beautyinfo);

		btSubmit = (Button) findViewById(R.id.bt_mulpetservice_submit);

		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);

		prsMain = (PullToRefreshScrollView) findViewById(R.id.prs_mulpetservice_scrollview);

		gvBathAddService = (MyGridView) findViewById(R.id.gv_mulpetservice_bath_addservice);
		tvBathTotalFee = (TextView) findViewById(R.id.tv_mulpetservice_total_bathprice);
		gvBeautyAddService = (MyGridView) findViewById(R.id.gv_mulpetservice_beauty_addservice);
		tvBeautyTotalFee = (TextView) findViewById(R.id.tv_mulpetservice_total_beautyprice);
		llSubmit = (LinearLayout) findViewById(R.id.ll_mulpetservice_submit);
		llBath = (LinearLayout) findViewById(R.id.ll_mulpetservice_bath);
		llBeauty = (LinearLayout) findViewById(R.id.ll_mulpetservice_beauty);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("选择服务项目");
		tvMsg1.setText("网络异常或连接服务器失败");
		btRefresh.setVisibility(View.VISIBLE);
		fIntent = getIntent();

		previous = fIntent.getIntExtra("previous", 0);
		petId = fIntent.getIntExtra("petid", 0);
		shopId = fIntent.getIntExtra("shopid", 0);
		petKind = fIntent.getIntExtra("petkind", 0);
		servicetype = fIntent.getIntExtra("servicetype", 0);
		servLoc = fIntent.getIntExtra("servloc", 0);
		beautician_sort = fIntent.getIntExtra("beautician_sort", 10);
		time = fIntent.getStringExtra("time");
		lat = fIntent.getDoubleExtra("lat", 0);
		lng = fIntent.getDoubleExtra("lng", 0);

		String serviceids = fIntent.getStringExtra("addserviceids");

		if (lat == 0 && lng == 0) {
			lat = Double.parseDouble(spUtil.getString("lat_home", "0"));
			lng = Double.parseDouble(spUtil.getString("lng_home", "0"));
		}

		ibBack.setOnClickListener(this);
		rlBath.setOnClickListener(this);
		rlBeauty.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		btSubmit.setOnClickListener(this);

		gvBathAddService.setSelector(new ColorDrawable(Color.TRANSPARENT));
		bathAddServiceAdapter = new ServiceAddPetAdapter(this,
				serviceBathItemsList);
		gvBathAddService.setAdapter(bathAddServiceAdapter);

		gvBeautyAddService.setSelector(new ColorDrawable(Color.TRANSPARENT));
		beautyAddServiceAdapter = new ServiceAddPetAdapter(this,
				serviceBeautyItemsList);
		gvBeautyAddService.setAdapter(beautyAddServiceAdapter);

		gvBathAddService.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (isBathSelected) {
					isBathSelected = true;
					isBeautySelected = false;
					servicetype = 1;
					serviceid = bathserviceid;
					int selectedIndex = isItemSelected(serviceBathItemsList
							.get(position));
					if (serviceBathItemsList.get(position).isChecked) {
						serviceBathItemsList.get(position).isChecked = false;

						addserviceprice -= serviceBathItemsList.get(position).price;
						addServiceIdList.remove(selectedIndex);

						bathtotalfee -= serviceBathItemsList.get(position).price;
					} else {
						serviceBathItemsList.get(position).isChecked = true;

						addserviceprice += serviceBathItemsList.get(position).price;
						addServiceIdList.add(serviceBathItemsList.get(position).id);
						bathtotalfee += serviceBathItemsList.get(position).price;
					}
				} else if (isBeautySelected) {
					isBathSelected = true;
					isBeautySelected = false;
					servicetype = 1;
					serviceid = bathserviceid;

					ivBath.setBackgroundResource(R.drawable.icon_pay_selected);
					ivBeauty.setBackgroundResource(R.drawable.icon_pay_normal);
					addserviceprice = 0;
					bathtotalfee = 0;
					beautytotalfee = 0;
					addServiceIdList.clear();
					if (beautician_sort == 10) {
						basefee = bathPriceLevel1;
						basefeewithbeautician = bathPriceLevel1;

					} else if (beautician_sort == 20) {
						basefee = bathPriceLevel2;
						basefeewithbeautician = bathPriceLevel2;
					} else {
						basefee = bathPriceLevel3;
						basefeewithbeautician = bathPriceLevel3;
					}
					bathtotalfee = basefeewithbeautician;
					serviceBathItemsList.get(position).isChecked = true;
					addserviceprice += serviceBathItemsList.get(position).price;
					addServiceIdList.add(serviceBathItemsList.get(position).id);
					bathtotalfee += serviceBathItemsList.get(position).price;
					for (int i = 0; i < serviceBeautyItemsList.size(); i++)
						serviceBeautyItemsList.get(i).isChecked = false;
					beautyAddServiceAdapter.notifyDataSetChanged();
				}

				bathAddServiceAdapter.notifyDataSetChanged();
				String text = "总价：¥" + Utils.formatDouble(bathtotalfee);
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.black)), 0, 3,
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
						4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tvBathTotalFee.setText(ss);
				tvBathTotalFee.setVisibility(View.VISIBLE);
				tvBeautyTotalFee.setVisibility(View.GONE);

			}
		});
		gvBeautyAddService.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (isBeautySelected) {
					isBathSelected = false;
					isBeautySelected = true;
					servicetype = 2;
					serviceid = beautyserviceid;
					int selectedIndex = isItemSelected(serviceBeautyItemsList
							.get(position));
					if (serviceBeautyItemsList.get(position).isChecked) {
						serviceBeautyItemsList.get(position).isChecked = false;

						addserviceprice -= serviceBeautyItemsList.get(position).price;
						addServiceIdList.remove(selectedIndex);

						beautytotalfee -= serviceBeautyItemsList.get(position).price;
					} else {
						serviceBeautyItemsList.get(position).isChecked = true;

						addserviceprice += serviceBeautyItemsList.get(position).price;
						addServiceIdList.add(serviceBeautyItemsList
								.get(position).id);
						beautytotalfee += serviceBeautyItemsList.get(position).price;
					}

				} else if (isBathSelected) {
					isBathSelected = false;
					isBeautySelected = true;
					servicetype = 2;
					serviceid = beautyserviceid;
					ivBeauty.setBackgroundResource(R.drawable.icon_pay_selected);
					ivBath.setBackgroundResource(R.drawable.icon_pay_normal);
					addserviceprice = 0;
					bathtotalfee = 0;
					beautytotalfee = 0;
					addServiceIdList.clear();
					if (beautician_sort == 10) {
						basefee = beautyPriceLevel1;
						basefeewithbeautician = beautyPriceLevel1;

					} else if (beautician_sort == 20) {
						basefee = beautyPriceLevel2;
						basefeewithbeautician = beautyPriceLevel2;
					} else {
						basefee = beautyPriceLevel3;
						basefeewithbeautician = beautyPriceLevel3;
					}
					beautytotalfee = basefeewithbeautician;
					serviceBeautyItemsList.get(position).isChecked = true;
					addserviceprice += serviceBeautyItemsList.get(position).price;
					addServiceIdList.add(serviceBeautyItemsList.get(position).id);
					beautytotalfee += serviceBeautyItemsList.get(position).price;
					for (int i = 0; i < serviceBathItemsList.size(); i++)
						serviceBathItemsList.get(i).isChecked = false;
					bathAddServiceAdapter.notifyDataSetChanged();
				}

				beautyAddServiceAdapter.notifyDataSetChanged();
				String text = "总价：¥" + Utils.formatDouble(beautytotalfee);
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.black)), 0, 3,
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
						4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tvBeautyTotalFee.setText(ss);
				tvBathTotalFee.setVisibility(View.GONE);
				tvBeautyTotalFee.setVisibility(View.VISIBLE);
			}
		});
		prsMain.setMode(Mode.PULL_FROM_START);
		prsMain.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					getData();
				}
			}
		});

		showMain(true);
		if (serviceids != null && !"".equals(serviceids.trim())) {
			String[] addserviceids = serviceids.split(",");
			for (int i = 0; i < addserviceids.length; i++) {
				addServiceIdList.add(Integer.parseInt(addserviceids[i]));
			}
			getData();
		} else {
			getData();
		}

	}

	private void showMain(boolean flag) {
		if (flag) {
			prsMain.setVisibility(View.VISIBLE);
			llSubmit.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			prsMain.setVisibility(View.GONE);
			llSubmit.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * 显示单项
	 * 
	 * @param flag
	 *            是否显示
	 * @param type
	 *            显示类型 1：洗澡 2：美容
	 */
	private void showServiceItems(boolean flag, int type) {
		if (type == 1) {
			if (flag) {
				rlBathInfo.setVisibility(View.VISIBLE);
			} else {
				rlBathInfo.setVisibility(View.GONE);
			}
		} else {
			if (flag) {
				rlBeautyInfo.setVisibility(View.VISIBLE);
			} else {
				rlBeautyInfo.setVisibility(View.GONE);
			}
		}
	}

	private int isItemSelected(AddServiceItem item) {
		for (int i = 0; i < addServiceIdList.size(); i++) {
			if (item.id == addServiceIdList.get(i)) {
				return i;
			}
		}
		return -1;
	}

	private String getAddServiceIdsStr() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < addServiceIdList.size(); i++) {
			sb.append(addServiceIdList.get(i));
			if (i < addServiceIdList.size() - 1)
				sb.append(",");
		}

		return sb.toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			if (ChoosePetActivityNew.act != null)
				ChoosePetActivityNew.act.finish();
			finishWithAnimation();
			break;
		case R.id.rl_mulpetservice_bath:
			if (!isBathSelected) {
				isBathSelected = true;
				isBeautySelected = false;
				ivBath.setBackgroundResource(R.drawable.icon_pay_selected);
				ivBeauty.setBackgroundResource(R.drawable.icon_pay_normal);
				tvBeautyTotalFee.setVisibility(View.GONE);
				tvBathTotalFee.setVisibility(View.VISIBLE);
				bathtotalfee = 0;
				beautytotalfee = 0;
				servicetype = 1;
				serviceid = bathserviceid;
				if (beautician_sort == 10) {
					basefee = bathPriceLevel1;
					basefeewithbeautician = bathPriceLevel1;
				} else if (beautician_sort == 20) {
					basefee = bathPriceLevel2;
					basefeewithbeautician = bathPriceLevel2;
				} else {
					basefee = bathPriceLevel3;
					basefeewithbeautician = bathPriceLevel3;
				}

				bathtotalfee = basefeewithbeautician;
				addserviceprice = 0;
				servicename = bathname;
				addServiceIdList.clear();
				String text = "总价：¥" + Utils.formatDouble(bathtotalfee);
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.black)), 0, 3,
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
						4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tvBathTotalFee.setText(ss);
				for (int i = 0; i < serviceBeautyItemsList.size(); i++)
					serviceBeautyItemsList.get(i).isChecked = false;
				beautyAddServiceAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.rl_mulpetservice_beauty:
			if (!isBeautySelected) {
				isBathSelected = false;
				isBeautySelected = true;
				ivBeauty.setBackgroundResource(R.drawable.icon_pay_selected);
				ivBath.setBackgroundResource(R.drawable.icon_pay_normal);

				tvBeautyTotalFee.setVisibility(View.VISIBLE);
				tvBathTotalFee.setVisibility(View.GONE);
				bathtotalfee = 0;
				beautytotalfee = 0;
				servicetype = 2;
				serviceid = beautyserviceid;
				if (beautician_sort == 10) {
					basefee = beautyPriceLevel1;
					basefeewithbeautician = beautyPriceLevel1;
				} else if (beautician_sort == 20) {
					basefee = beautyPriceLevel2;
					basefeewithbeautician = beautyPriceLevel2;
				} else {
					basefee = beautyPriceLevel3;
					basefeewithbeautician = beautyPriceLevel3;
				}
				beautytotalfee = basefeewithbeautician;
				addserviceprice = 0;
				servicename = beautyname;
				addServiceIdList.clear();
				String text = "总价：¥" + Utils.formatDouble(beautytotalfee);
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.black)), 0, 3,
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
						4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tvBeautyTotalFee.setText(ss);
				for (int i = 0; i < serviceBathItemsList.size(); i++)
					serviceBathItemsList.get(i).isChecked = false;
				bathAddServiceAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.bt_mulpetservice_submit:
			Intent data = new Intent();
			data.putExtra("petid", petId);
			data.putExtra("petkind", petKind);
			data.putExtra("petname", fIntent.getStringExtra("petname"));
			data.putExtra("serviceid", serviceid);
			data.putExtra("servicetype", servicetype);
			data.putExtra("petimage", fIntent.getStringExtra("petimage"));
			data.putExtra("customerpetid",
					fIntent.getIntExtra("customerpetid", 0));
			data.putExtra("customerpetname",
					fIntent.getStringExtra("customerpetname"));
			data.putExtra("addserviceids", getAddServiceIdsStr());
			if (servicetype == 1) {
				data.putExtra("totalfee", bathtotalfee);
				data.putExtra("pricelevel1", bathPriceLevel1);
				data.putExtra("pricelevel2", bathPriceLevel2);
				data.putExtra("pricelevel3", bathPriceLevel3);
			} else if (servicetype == 2) {
				data.putExtra("pricelevel1", beautyPriceLevel1);
				data.putExtra("pricelevel2", beautyPriceLevel2);
				data.putExtra("pricelevel3", beautyPriceLevel3);
				data.putExtra("totalfee", beautytotalfee);
			}
			data.putExtra("addservicefee", addserviceprice);
			data.putExtra("basefee", basefee);
			data.putExtra("servicename", servicename);
			data.putExtra("position", fIntent.getIntExtra("position", -1));
			setResult(Global.RESULT_OK, data);
			finishWithAnimation();
			break;
		case R.id.bt_null_refresh:
			getData();
			break;
		}
	}

	private void getData() {
		pDialog.showDialog();
		serviceBathItemsList.clear();
		serviceBeautyItemsList.clear();
		CommUtil.getPetServiceByAddPet(this, petId, 0, 0, time, lat, lng,
				dataHanler);
	}

	private AsyncHttpResponseHandler dataHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("添加宠物服务：" + new String(responseBody));
			pDialog.closeDialog();
			prsMain.onRefreshComplete();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				if (code == 0 && jobj.has("data") && !jobj.isNull("data")
						&& jobj.getJSONArray("data").length() > 0) {
					JSONArray jarr = jobj.getJSONArray("data");
					showMain(true);
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject jo = jarr.getJSONObject(i);
						if (jo.has("petServicePojo")
								&& !jo.isNull("petServicePojo")) {
							JSONObject jps = jo.getJSONObject("petServicePojo");
							if (jps.has("serviceType")
									&& !jps.isNull("serviceType")) {
								int st = jps.getInt("serviceType");
								if (st == 1) {
									// 洗澡
									isBathAvailable = true;
									if (jps.has("name") && !jps.isNull("name")) {
										bathname = jps.getString("name");
									}
									if (jps.has("strItem")
											&& !jps.isNull("strItem")) {
										// tvBathInfo.setText(jps.getString("strItem"));
									}
									if (jo.has("serviceId")
											&& !jo.isNull("serviceId")) {
										bathserviceid = jo.getInt("serviceId");
									}

									if (servLoc == 1) {
										// 到店
										if (jo.has("shopPrice10")
												&& !jo.isNull("shopPrice10")) {
											bathPriceLevel1 = jo
													.getDouble("shopPrice10");
										}
										if (jo.has("shopPrice20")
												&& !jo.isNull("shopPrice20")) {
											bathPriceLevel2 = jo
													.getDouble("shopPrice20");
										}
										if (jo.has("shopPrice30")
												&& !jo.isNull("shopPrice30")) {
											bathPriceLevel3 = jo
													.getDouble("shopPrice30");
										}
									} else {
										if (jo.has("price10")
												&& !jo.isNull("price10")) {
											bathPriceLevel1 = jo
													.getDouble("price10");
										}
										if (jo.has("price20")
												&& !jo.isNull("price20")) {
											bathPriceLevel2 = jo
													.getDouble("price20");
										}
										if (jo.has("price30")
												&& !jo.isNull("price30")) {
											bathPriceLevel3 = jo
													.getDouble("price30");
										}

									}

									if (jps.has("extraServiceItems")
											&& !jps.isNull("extraServiceItems")
											&& jps.getJSONArray(
													"extraServiceItems")
													.length() > 0) {
										JSONArray je = jps
												.getJSONArray("extraServiceItems");
										showServiceItems(true, 1);
										for (int j = 0; j < je.length(); j++) {
											serviceBathItemsList
													.add(AddServiceItem.json2Entity(je
															.getJSONObject(j)));
										}
										bathAddServiceAdapter
												.notifyDataSetChanged();
									} else {
										showServiceItems(false, 1);
									}
									if (beautician_sort == 10) {
										basefeewithbeautician = bathPriceLevel1;
									} else if (beautician_sort == 20) {
										basefeewithbeautician = bathPriceLevel2;
									} else {
										basefeewithbeautician = bathPriceLevel3;
									}
									bathtotalfee = basefeewithbeautician;
									tvBathPrice
											.setText("¥"
													+ Utils.formatDouble(basefeewithbeautician));
									String text = "总价：¥"
											+ Utils.formatDouble(basefeewithbeautician);
									SpannableString ss = new SpannableString(
											text);
									ss.setSpan(
											new ForegroundColorSpan(mContext
													.getResources().getColor(
															R.color.black)), 0,
											3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
									ss.setSpan(new TextAppearanceSpan(mContext,
											R.style.style3), 0, 4,
											Spanned.SPAN_INCLUSIVE_INCLUSIVE);
									tvBathTotalFee.setText(ss);
								} else if (st == 2) {
									// 美容
									isBeautyAvailable = true;
									if (jps.has("name") && !jps.isNull("name")) {
										beautyname = jps.getString("name");
									}
									if (jps.has("strItem")
											&& !jps.isNull("strItem")) {
										// tvBeautyInfo.setText(jps.getString("strItem"));
									}
									if (jo.has("serviceId")
											&& !jo.isNull("serviceId")) {
										beautyserviceid = jo
												.getInt("serviceId");
									}

									if (servLoc == 1) {
										// 到店
										if (jo.has("shopPrice10")
												&& !jo.isNull("shopPrice10")) {
											beautyPriceLevel1 = jo
													.getDouble("shopPrice10");
										}
										if (jo.has("shopPrice20")
												&& !jo.isNull("shopPrice20")) {
											beautyPriceLevel2 = jo
													.getDouble("shopPrice20");
										}
										if (jo.has("shopPrice30")
												&& !jo.isNull("shopPrice30")) {
											beautyPriceLevel3 = jo
													.getDouble("shopPrice30");
										}
									} else {
										if (jo.has("price10")
												&& !jo.isNull("price10")) {
											beautyPriceLevel1 = jo
													.getDouble("price10");
										}
										if (jo.has("price20")
												&& !jo.isNull("price20")) {
											beautyPriceLevel2 = jo
													.getDouble("price20");
										}
										if (jo.has("price30")
												&& !jo.isNull("price30")) {
											beautyPriceLevel3 = jo
													.getDouble("price30");
										}
									}

									if (jps.has("extraServiceItems")
											&& !jps.isNull("extraServiceItems")
											&& jps.getJSONArray(
													"extraServiceItems")
													.length() > 0) {
										JSONArray je = jps
												.getJSONArray("extraServiceItems");
										showServiceItems(true, 2);
										for (int j = 0; j < je.length(); j++) {
											serviceBeautyItemsList
													.add(AddServiceItem.json2Entity(je
															.getJSONObject(j)));
										}
										beautyAddServiceAdapter
												.notifyDataSetChanged();
									} else {
										showServiceItems(false, 2);
									}
									if (beautician_sort == 10) {
										basefeewithbeautician = beautyPriceLevel1;
									} else if (beautician_sort == 20) {
										basefeewithbeautician = beautyPriceLevel2;
									} else {
										basefeewithbeautician = beautyPriceLevel3;
									}
									beautytotalfee = basefeewithbeautician;
									tvBeautyPrice
											.setText("¥"
													+ Utils.formatDouble(basefeewithbeautician));
									String text = "总价：¥"
											+ Utils.formatDouble(basefeewithbeautician);
									SpannableString ss = new SpannableString(
											text);
									ss.setSpan(
											new ForegroundColorSpan(mContext
													.getResources().getColor(
															R.color.black)), 0,
											3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
									ss.setSpan(new TextAppearanceSpan(mContext,
											R.style.style3), 0, 4,
											Spanned.SPAN_INCLUSIVE_INCLUSIVE);
									tvBeautyTotalFee.setText(ss);
								}
							}

						}
					}

					if (servicetype == 1 && isBathAvailable
							|| (isBathAvailable && !isBeautyAvailable)) {
						isBathSelected = true;
						isBeautySelected = false;
						// rlBath.setBackgroundResource(R.drawable.bg_search_orangeborder);
						ivBath.setBackgroundResource(R.drawable.icon_pay_selected);
						// rlBeauty.setBackgroundResource(R.drawable.bg_white_bordergray);
						ivBeauty.setBackgroundResource(R.drawable.icon_pay_normal);

						// rlBathInfo.setVisibility(View.VISIBLE);
						// rlBeautyInfo.setVisibility(View.GONE);
						servicetype = 1;
						serviceid = bathserviceid;
						if (beautician_sort == 10) {
							basefee = bathPriceLevel1;
							basefeewithbeautician = bathPriceLevel1;
						} else if (beautician_sort == 20) {
							basefee = bathPriceLevel2;
							basefeewithbeautician = bathPriceLevel2;
						} else {
							basefee = bathPriceLevel3;
							basefeewithbeautician = bathPriceLevel3;
						}

						bathtotalfee = basefeewithbeautician;
						tvBathPrice.setText("¥"
								+ Utils.formatDouble(basefeewithbeautician));
						servicename = bathname;
						if (serviceBathItemsList.size() > 0) {
							for (int i = 0; i < addServiceIdList.size(); i++) {
								for (int j = 0; j < serviceBathItemsList.size(); j++) {
									if (addServiceIdList.get(i) == serviceBathItemsList
											.get(j).id) {
										serviceBathItemsList.get(j).isChecked = true;
										addserviceprice += serviceBathItemsList
												.get(j).price;
										break;
									}
								}
							}
							bathAddServiceAdapter.notifyDataSetChanged();
						}
						bathtotalfee = basefeewithbeautician + addserviceprice;
						String text = "总价：¥" + Utils.formatDouble(bathtotalfee);
						SpannableString ss = new SpannableString(text);
						ss.setSpan(new ForegroundColorSpan(mContext
								.getResources().getColor(R.color.black)), 0, 3,
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						ss.setSpan(new TextAppearanceSpan(mContext,
								R.style.style3), 0, 4,
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tvBathTotalFee.setText(ss);
						tvBathTotalFee.setVisibility(View.VISIBLE);
						tvBeautyTotalFee.setVisibility(View.GONE);
					} else if (servicetype == 2 && isBeautyAvailable
							|| (!isBathAvailable && isBeautyAvailable)) {
						isBathSelected = false;
						isBeautySelected = true;
						// rlBeauty.setBackgroundResource(R.drawable.bg_search_orangeborder);
						ivBeauty.setBackgroundResource(R.drawable.icon_pay_selected);
						// rlBath.setBackgroundResource(R.drawable.bg_white_bordergray);
						ivBath.setBackgroundResource(R.drawable.icon_pay_normal);

						// rlBeautyInfo.setVisibility(View.VISIBLE);
						// rlBathInfo.setVisibility(View.GONE);
						servicetype = 2;
						serviceid = beautyserviceid;
						if (beautician_sort == 10) {
							basefee = bathPriceLevel1;
							basefeewithbeautician = beautyPriceLevel1;
						} else if (beautician_sort == 20) {
							basefee = bathPriceLevel2;
							basefeewithbeautician = beautyPriceLevel2;
						} else {
							basefee = bathPriceLevel3;
							basefeewithbeautician = beautyPriceLevel3;
						}
						beautytotalfee = basefeewithbeautician;
						servicename = beautyname;
						tvBeautyPrice.setText("¥"
								+ Utils.formatDouble(basefeewithbeautician));
						if (serviceBeautyItemsList.size() > 0) {
							for (int i = 0; i < addServiceIdList.size(); i++) {
								for (int j = 0; j < serviceBeautyItemsList
										.size(); j++) {
									if (addServiceIdList.get(i) == serviceBeautyItemsList
											.get(j).id) {
										serviceBeautyItemsList.get(j).isChecked = true;
										addserviceprice += serviceBeautyItemsList
												.get(j).price;
										break;
									}
								}
							}
							beautytotalfee = basefeewithbeautician
									+ addserviceprice;
							beautyAddServiceAdapter.notifyDataSetChanged();
						}
						String text = "总价：¥"
								+ Utils.formatDouble(beautytotalfee);
						SpannableString ss = new SpannableString(text);
						ss.setSpan(new ForegroundColorSpan(mContext
								.getResources().getColor(R.color.black)), 0, 3,
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						ss.setSpan(new TextAppearanceSpan(mContext,
								R.style.style3), 0, 4,
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tvBeautyTotalFee.setText(ss);
						tvBathTotalFee.setVisibility(View.GONE);
						tvBeautyTotalFee.setVisibility(View.VISIBLE);
					} else if (!isBathAvailable && !isBeautyAvailable) {
						showMain(false);
					}

					if (isBathAvailable) {
						rlBath.setVisibility(View.VISIBLE);
						llBath.setVisibility(View.VISIBLE);
						if (beautician_sort == 30) {
							if (bathPriceLevel3 > 0) {
								rlBath.setVisibility(View.VISIBLE);
								llBath.setVisibility(View.VISIBLE);
							} else {
								rlBath.setVisibility(View.GONE);
								llBath.setVisibility(View.GONE);
							}
						}
					} else {
						rlBath.setVisibility(View.GONE);
						llBath.setVisibility(View.GONE);
					}
					if (isBeautyAvailable) {
						rlBeauty.setVisibility(View.VISIBLE);
						llBeauty.setVisibility(View.VISIBLE);

					} else {
						rlBeauty.setVisibility(View.GONE);
						llBeauty.setVisibility(View.GONE);

					}

				} else {
					showMain(false);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showMain(false);
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			prsMain.onRefreshComplete();
			showMain(false);

		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ChoosePetActivityNew.act != null)
				ChoosePetActivityNew.act.finish();
			finishWithAnimation();
		}
		return super.onKeyDown(keyCode, event);
	}

}
