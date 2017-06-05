package com.haotang.pet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeauticianAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TopIndicator_C;
import com.haotang.pet.view.TopIndicator_C.OnTopIndicatorListener;
import com.umeng.analytics.MobclickAgent;
//import com.haotang.pet.view.TopIndicator_B;
//import com.haotang.pet.view.TopIndicator_B.OnTopIndicatorListener;

public class BeauticianListActivity extends SuperActivity implements
		OnTouchListener, OnTopIndicatorListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlList;
	private ArrayList<Beautician> beauticianList = new ArrayList<Beautician>();
	private ArrayList<Beautician> beauticianListMid = new ArrayList<Beautician>();
	private ArrayList<Beautician> beauticianListHIGH = new ArrayList<Beautician>();
	private BeauticianAdapter adapter;
	private Intent fIntent;
	private int serviceid;
	private int shopid;
	private int areaid;
	private double lat;
	private double lng;
	private String time;
	private SharedPreferenceUtil spUtil;
	public static SuperActivity act;
	private static JSONObject localWorkersData;
	private static String localDate;

	private int oldindex = 0;
	private TopIndicator_C tidTopTab;
	private TextView beaulist_price;
	private RelativeLayout rlNull;
	private TextView tv_null_msg1;
	private TextView tv_null_msg2;
	private double pricelevel1;
	private double pricelevel2;
	private double pricelevel3;

	private LinearLayout show;
	private CharSequence[] mLabels = new CharSequence[] { "中级", "高级", "首席" };
	private ArrayList<String> showList = new ArrayList<String>();
	private boolean isFrist = false;
	private int serviceloc;
	private String strp;
	private ImageView ivNull;
	private double lastPrice;
	private double itemPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauticianlist);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		show = (LinearLayout) findViewById(R.id.show);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		beaulist_price = (TextView) findViewById(R.id.beaulist_price);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		ivNull = (ImageView) findViewById(R.id.iv_null_image);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		tv_null_msg2 = (TextView) findViewById(R.id.tv_null_msg2);
		prlList = (PullToRefreshListView) findViewById(R.id.prl_beauticianlist_list);
		tidTopTab = new TopIndicator_C(act);
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tidTopTab.setLayoutParams(topLayoutParams);
		show.addView(tidTopTab);
		tidTopTab.setOnTopIndicatorListener(this);
	}

	private void setView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		ivNull.setBackgroundResource(R.drawable.icon_null);
		fIntent = getIntent();
		serviceid = fIntent.getIntExtra("serviceid", 0);
		serviceloc = fIntent.getIntExtra("serviceloc", 0);
		shopid = fIntent.getIntExtra("shopid", 0);
		areaid = fIntent.getIntExtra("areaid", 0);
		lat = fIntent.getDoubleExtra("lat", 0);
		lastPrice = fIntent.getDoubleExtra("lastPrice", 0);
		lng = fIntent.getDoubleExtra("lng", 0);
		time = fIntent.getStringExtra("time");
		strp = fIntent.getStringExtra("strp");
		tvTitle.setText(fIntent.getStringExtra("areaname") + "美容师");
		int sort = fIntent.getIntExtra("beauticianlevel", 0);
		if (sort == 10)
			oldindex = 0;
		else if (sort == 20)
			oldindex = 1;
		else if (sort == 30)
			oldindex = 2;
		else
			oldindex = 0;
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		adapter = new BeauticianAdapter(this, beauticianList,localDate);
		prlList.setAdapter(adapter);
		prlList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Beautician beautician = (Beautician) adapter
						.getItem(position - 1);
				Intent data = new Intent(BeauticianListActivity.act,
						BeauticianDetailActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				BeauticianListActivity.act.getIntent().putExtra(
						Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("serviceid", serviceid);
				data.putExtra("shopid", shopid);
				data.putExtra("areaid", areaid);
				data.putExtra("serviceloc", serviceloc);
				data.putExtra("isavail", beautician.isAvail);
				data.putExtra("strp", strp);
				data.putExtra("apptime", localDate);
				data.putExtra("beautician_id", beautician.id);
				BeauticianListActivity.act.startActivityForResult(data,
						Global.BEAUTICIAN_TO_TIME);
			}
		});
		adapter.setParams(serviceid, serviceloc, shopid, areaid, strp);
	}

	private void getData(int index) {
		beauticianList.clear();
		beauticianListMid.clear();
		beauticianListHIGH.clear();
		showList.clear();
		proceData();
	}

	private void proceData() {
		if (localWorkersData != null) {
			Utils.mLogError("美容师列表：" + localWorkersData.toString());
			prlList.onRefreshComplete();
			try {
				if (localWorkersData.has("workers")
						&& !localWorkersData.isNull("workers")) {
					JSONObject jWorkers = localWorkersData
							.getJSONObject("workers");
					if (jWorkers.has("10") && !jWorkers.isNull("10")) {
						JSONObject j10 = jWorkers.getJSONObject("10");
						String levelName = "";
						int sort = 0;
						if (j10.has("level") && !j10.isNull("level")) {
							JSONObject objectLevel = j10.getJSONObject("level");
							if (objectLevel.has("price")
									&& !objectLevel.isNull("price")) {
								pricelevel1 = objectLevel.getDouble("price");
							}
							if (objectLevel.has("name")
									&& !objectLevel.isNull("name")) {
								levelName = objectLevel.getString("name");
								showList.add(levelName);
							}
							if (objectLevel.has("sort")
									&& !objectLevel.isNull("sort")) {
								sort = objectLevel.getInt("sort");
							}
						}
						if (j10.has("workers") && !j10.isNull("workers")) {
							JSONArray array = j10.getJSONArray("workers");
							for (int i = 0; i < array.length(); i++) {
								Beautician btc = Beautician.json2Entity(array
										.getJSONObject(i));
								btc.levelname = levelName;
								btc.titlelevel = levelName;
								btc.price = pricelevel1;
								btc.sort = sort;
								beauticianList.add(btc);
							}
						}
					}
					if (jWorkers.has("20") && !jWorkers.isNull("20")) {
						JSONObject j10 = jWorkers.getJSONObject("20");
						String levelName = "";
						int sort = 0;
						if (j10.has("level") && !j10.isNull("level")) {
							JSONObject objectLevel = j10.getJSONObject("level");
							if (objectLevel.has("price")
									&& !objectLevel.isNull("price")) {
								pricelevel2 = objectLevel.getDouble("price");
							}
							if (objectLevel.has("name")
									&& !objectLevel.isNull("name")) {
								levelName = objectLevel.getString("name");
								showList.add(levelName);
							}
							if (objectLevel.has("sort")
									&& !objectLevel.isNull("sort")) {
								sort = objectLevel.getInt("sort");
							}
						}
						if (j10.has("workers") && !j10.isNull("workers")) {
							JSONArray array = j10.getJSONArray("workers");
							for (int i = 0; i < array.length(); i++) {
								Beautician btc = Beautician.json2Entity(array
										.getJSONObject(i));
								btc.levelname = levelName;
								btc.titlelevel = levelName;
								btc.price = pricelevel2;
								btc.sort = sort;
								beauticianListMid.add(btc);
							}
						}
					}
					if (jWorkers.has("30") && !jWorkers.isNull("30")) {
						JSONObject j10 = jWorkers.getJSONObject("30");
						String levelName = "";
						int sort = 0;
						if (j10.has("level") && !j10.isNull("level")) {
							JSONObject objectLevel = j10.getJSONObject("level");
							if (objectLevel.has("price")
									&& !objectLevel.isNull("price")) {
								pricelevel3 = objectLevel.getDouble("price");
							}
							if (objectLevel.has("name")
									&& !objectLevel.isNull("name")) {
								levelName = objectLevel.getString("name");
								showList.add(levelName);
							}
							if (objectLevel.has("sort")
									&& !objectLevel.isNull("sort")) {
								sort = objectLevel.getInt("sort");
							}
						}
						if (j10.has("workers") && !j10.isNull("workers")) {
							JSONArray array = j10.getJSONArray("workers");
							for (int i = 0; i < array.length(); i++) {
								Beautician btc = Beautician.json2Entity(array
										.getJSONObject(i));
								btc.levelname = levelName;
								btc.titlelevel = levelName;
								btc.price = pricelevel3;
								btc.sort = sort;
								beauticianListHIGH.add(btc);
							}
						}
					}
				}
				if (showList.size() > 0) {
					for (int i = 0; i < showList.size(); i++) {
						mLabels[i] = showList.get(i);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (!isFrist) {
				isFrist = true;
				show.removeAllViews();
				tidTopTab.removeAllViews();
				tidTopTab.init(act, mLabels);
				show.addView(tidTopTab);
			}
			if (oldindex == 0) {
				itemPrice = lastPrice  - pricelevel1;
				adapter.setData(beauticianList);
				if (beauticianList.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel1 + itemPrice, 2)
							+ "元");
				}
			} else if (oldindex == 1) {
				itemPrice = lastPrice  - pricelevel2;
				adapter.setData(beauticianListMid);
				if (beauticianListMid.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel2 + itemPrice, 2)
							+ "元");
				}
			} else if (oldindex == 2) {
				itemPrice = lastPrice  - pricelevel3;
				adapter.setData(beauticianListHIGH);
				if (beauticianListHIGH.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel3 + itemPrice, 2)
							+ "元");
				}
			}
			adapter.notifyDataSetChanged();
			tidTopTab.setTabsDisplay(act, oldindex);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("BeauticianListActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长

		tidTopTab.setTabsDisplay(act, oldindex);
		getData(oldindex);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("BeauticianListActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onIndicatorSelected(int index) {
		// TODO Auto-generated method stub
		if (oldindex != index) {
			oldindex = index;
			tidTopTab.setTabsDisplay(act, index);
			if (oldindex == 0) {
				if (beauticianList.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					showMain(true);
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel1 + itemPrice, 2)
							+ "元");
				}
				adapter.setData(beauticianList);
				adapter.notifyDataSetChanged();
			} else if (oldindex == 1) {
				if (beauticianListMid.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					showMain(true);
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel2 + itemPrice, 2)
							+ "元");
				}
				adapter.setData(beauticianListMid);
				adapter.notifyDataSetChanged();
			} else if (oldindex == 2) {
				if (beauticianListHIGH.size() == 0) {
					showMain(false);
					beaulist_price.setText("服务总价：0元");
				} else {
					showMain(true);
					beaulist_price.setText("服务总价："
							+ Utils.formatDouble(pricelevel3 + itemPrice, 2)
							+ "元");
				}
				adapter.setData(beauticianListHIGH);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	private void showMain(boolean ismain) {
		if (ismain) {
			beaulist_price.setVisibility(View.VISIBLE);
			prlList.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
			tv_null_msg2.setVisibility(View.GONE);
		} else {
			beaulist_price.setVisibility(View.GONE);
			prlList.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
			tv_null_msg2.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("这里的美容师已和别家宝贝有约");
			tv_null_msg2.setText("领宝贝认识个新的美容师吧！");

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Global.RESULT_OK
				&& requestCode == Global.BEAUTICIAN_TO_TIME) {
			Intent Intent = new Intent();
			Intent.putExtra("time", data.getStringExtra("time"));
			Intent.putExtra("date", data.getStringExtra("date"));
			Intent.putExtra("strListWokerId",
					data.getStringExtra("strListWokerId"));
			Intent.putExtra("position", data.getIntExtra("position", 0));
			Intent.putExtra("beautician_name",
					data.getStringExtra("beautician_name"));
			Intent.putExtra("beautician_sex",
					data.getIntExtra("beautician_sex", 0));
			Intent.putExtra("beautician_level",
					data.getIntExtra("beautician_level", 0));
			Intent.putExtra("beautician_stars",
					data.getIntExtra("beautician_stars", 0));
			Intent.putExtra("beautician_titlelevel",
					data.getStringExtra("beautician_titlelevel"));
			Intent.putExtra("beautician_image",
					data.getStringExtra("beautician_image"));
			Intent.putExtra("beautician_ordernum",
					data.getIntExtra("beautician_ordernum", 0));
			Intent.putExtra("beautician_storeid",
					data.getIntExtra("beautician_storeid", 0));
			Intent.putExtra("beautician_id",
					data.getIntExtra("beautician_id", 0));
			Intent.putExtra("beautician_sort",
					data.getIntExtra("beautician_sort", 0));
			Intent.putExtra("clicksort",
					data.getIntExtra("beautician_sort", 0));
			Intent.putExtra("beautician_levelname",
					data.getStringExtra("beautician_levelname"));
			Intent.putExtra("beautician_bathmultiple",
					data.getDoubleExtra("beautician_bathmultiple", 1));
			Intent.putExtra("beautician_beautymultiple",
					data.getDoubleExtra("beautician_beautymultiple", 1));
			Intent.putExtra("beautician_factormultiple",
					data.getDoubleExtra("beautician_factormultiple", 1));

			setResult(Global.RESULT_OK, Intent);
			finishWithAnimation();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void setData(JSONObject workersData, String date) {
		localWorkersData = workersData;
		localDate = date;
	}
}
