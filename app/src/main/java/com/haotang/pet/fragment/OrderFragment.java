package com.haotang.pet.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.OrderDetailFromOrderActivity;
import com.haotang.pet.OrderDetailFromOrderToConfirmActivity;
import com.haotang.pet.OrderFosterDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.OrderAdapter;
import com.haotang.pet.entity.Order;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TopIndicator_T;
import com.haotang.pet.view.TopIndicator_T.OnTopIndicatorListener;
import com.umeng.analytics.MobclickAgent;

public class OrderFragment extends Fragment implements OnTouchListener,
		OnTopIndicatorListener {
	private Activity mActivity;
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlList;
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private OrderAdapter adapter;
	private int page = 1;
	private SharedPreferenceUtil spUtil;
	private TopIndicator_T tidTopTab;
	private int oldindex = 0;
	// private MProgressDialog pDialog;
	private RelativeLayout rlNull;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1000) {
				llMain.setVisibility(View.VISIBLE);
				rlLoading.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.mActivity = activity;
		super.onAttach(activity);
		spUtil = SharedPreferenceUtil.getInstance(mActivity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.orderfragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		oldindex = 0;
		findView(view);
		setView();
	}

	private void findView(View view) {
		// pDialog = new MProgressDialog(mActivity);
		rlLoading = (RelativeLayout) view
				.findViewById(R.id.rl_orderfragmentloading);
		llMain = (LinearLayout) view.findViewById(R.id.ll_orderfragment);
		ibBack = (ImageButton) view.findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) view.findViewById(R.id.tv_titlebar_title);
		prlList = (PullToRefreshListView) view
				.findViewById(R.id.prl_order_list);
		tidTopTab = (TopIndicator_T) view
				.findViewById(R.id.tid_order_topindicator);
		tidTopTab.setOnTopIndicatorListener(this);
		rlNull = (RelativeLayout) view.findViewById(R.id.rl_null);
		tvMsg1 = (TextView) view.findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) view.findViewById(R.id.bt_null_refresh);

	}

	private void setView() {
		ibBack.setVisibility(View.GONE);
		tvTitle.setText("我的订单");
		tvMsg1.setText("您还没有订单,赶紧去约一单吧~");
		btRefresh.setText("立即预约");
		btRefresh.setVisibility(View.VISIBLE);
		btRefresh.setBackgroundResource(R.drawable.null_button_back);

		adapter = new OrderAdapter(mActivity, orderlist);

		prlList.setMode(Mode.BOTH);
		prlList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					orderlist.clear();
					adapter.notifyDataSetChanged();
					getData(oldindex);

				} else {
					getData(oldindex);
				}
			}
		});

		prlList.setAdapter(adapter);

		prlList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int orderid = orderlist.get(position - 1).orderid;
				int type = orderlist.get(position - 1).type;
				int status = orderlist.get(position - 1).status;

				if (status == 1) {
					// 待付款
					goOrderDetail(orderid, OrderDetailFromOrderActivity.class);
				} else {
					// 其他状态
					if (type == 2|| type == 4) {
						// 寄养
						goOrderDetail(orderid, OrderFosterDetailActivity.class);
					} else {
						// 洗美
						goOrderDetail(orderid,
								OrderDetailFromOrderToConfirmActivity.class);
					}
				}
			}
		});

		btRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous",
						Global.PRE_ORDER_LIST_TO_MAINACTIVITY);
				mActivity.sendBroadcast(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("OrderFragment");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("OrderFragment"); // 统计页面
		if (spUtil.getString("cellphone", "0").equals("0")) {
			Intent intent = new Intent(mActivity, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			intent.putExtra("previous",
					Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT);
			startActivity(intent);
		} else {
			page = 1;
			orderlist.clear();
			adapter.notifyDataSetChanged();
			tidTopTab.setTabsDisplay(mActivity, oldindex);
			getData(oldindex);
		}
	}

	private void goOrderDetail(int orderid, Class cls) {
		Intent intent = new Intent(mActivity, cls);
		intent.putExtra("orderid", orderid);
		startActivity(intent);
	}

	private void getData(int index) {
		// pDialog.showDialog();
		if (index == 0) {//进行中
			CommUtil.queryMyOrders(spUtil.getString("cellphone", ""),
					Global.getIMEI(mActivity), mActivity, page, ingWorkerOrders);
		} else if (index == 1) {//待评价
			CommUtil.queryProcessMyOrders(spUtil.getString("cellphone", ""),
					Global.getIMEI(mActivity), mActivity, page, ingWorkerOrders);
		}else if (index == 2) {//查询已取消的订单
			CommUtil.queryEvaluateMyOrders(spUtil.getString("cellphone", ""),
					Global.getIMEI(mActivity), mActivity, page, ingWorkerOrders);
		}else if (index == 3) {//全部
			CommUtil.queryCanceledOrders(spUtil.getString("cellphone", ""), 
					Global.getIMEI(mActivity), mActivity, page, ingWorkerOrders);
		}else {
			CommUtil.queryMyOrders(spUtil.getString("cellphone", ""),
					Global.getIMEI(mActivity), mActivity, page, ingWorkerOrders);
		}
	}

	private AsyncHttpResponseHandler ingWorkerOrders = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("订单列表：" + new String(responseBody));
			prlList.onRefreshComplete();
			// pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");

						if (jdata.has("nToBeComment")
								&& !jdata.isNull("nToBeComment")) {
							int nToBeComment = jdata.getInt("nToBeComment");
							tidTopTab.setFlagDisplay(nToBeComment);
						}
						if (jdata.has("orders") && !jdata.isNull("orders")) {
							JSONArray jorders = jdata.getJSONArray("orders");
							if (jorders.length() > 0) {
								page++;
								for (int i = 0; i < jorders.length(); i++) {
									Order order = new Order();
									JSONObject jo = jorders.getJSONObject(i);
									if (jo.has("appointment")&& !jo.isNull("appointment")) {
										order.starttime = jo.getString("appointment");
										String [] timeMonthAndYear = order.starttime.split(" ")[0].split("-");
										order.EverytYearAndMonth = timeMonthAndYear[0]+"-"+timeMonthAndYear[1];
										order.EveryDay = order.starttime.split(" ")[0].split("-")[2];
										order.isShow=false;
									}
									if (jo.has("OrderId")&& !jo.isNull("OrderId")) {
										order.orderid = jo.getInt("OrderId");
									}
									if (jo.has("serviceLoc")&& !jo.isNull("serviceLoc")) {
										order.addrtype = jo
												.getInt("serviceLoc");
									}
									if (jo.has("pickUp")
											&& !jo.isNull("pickUp")) {
										order.pickup = jo.getInt("pickUp");
									}
									if (jo.has("petService")
											&& !jo.isNull("petService")) {
										order.serviceid = jo
												.getInt("petService");
									}
									if (jo.has("type") && !jo.isNull("type")) {
										order.type = jo.getInt("type");
									}
									if (jo.has("status")
											&& !jo.isNull("status")) {
										order.status = jo.getInt("status");
									}
									if (jo.has("statusDescription")
											&& !jo.isNull("statusDescription")) {
										order.statusstr = jo
												.getString("statusDescription");
									}
									if (jo.has("totalPrice")
											&& !jo.isNull("totalPrice")) {
										order.fee = jo.getDouble("totalPrice");
									}
									if (jo.has("petServicePojo")
											&& !jo.isNull("petServicePojo")) {
										JSONObject jpp = jo
												.getJSONObject("petServicePojo");
										if (jpp.has("name")
												&& !jpp.isNull("name")) {
											order.servicename = jpp
													.getString("name");
										}
									}
									if (jo.has("task") && !jo.isNull("task")) {
										JSONObject jt = jo
												.getJSONObject("task");
										if (jt.has("startTime")
												&& !jt.isNull("startTime")) {
											order.starttime = jt
													.getString("startTime");
										}
										if (jt.has("stopTime")
												&& !jt.isNull("stopTime")) {
											order.endtime = jt
													.getString("stopTime");
										}
									}
									if (jo.has("pet") && !jo.isNull("pet")) {
										JSONObject jpet = jo
												.getJSONObject("pet");
										if (jpet.has("avatarPath")
												&& !jpet.isNull("avatarPath")) {
											order.petimg = CommUtil
													.getServiceNobacklash()
													+ jpet.getString("avatarPath");
										}
										if (jpet.has("petName")
												&& !jpet.isNull("petName")) {
											order.petname = jpet
													.getString("petName");
										}
										if (jpet.has("petKind")
												&& !jpet.isNull("petKind")) {
											order.petkind = jpet
													.getInt("petKind");
										}
									}
									if (jo.has("myPet") && !jo.isNull("myPet")) {
										JSONObject jmp = jo
												.getJSONObject("myPet");
										if (jmp.has("nickName")
												&& !jmp.isNull("nickName")) {
											order.customerpetname = jmp
													.getString("nickName");
										}
										if (jmp.has("avatarPath")
												&& !jmp.isNull("avatarPath")) {
											order.petimg = CommUtil
													.getServiceNobacklash()
													+ jmp.getString("avatarPath");
										}
									}
									if (jo.has("orderSource")&&!jo.isNull("orderSource")) {
										order.orderSource = jo.getString("orderSource");
									}
									orderlist.add(order);
								}
							}
							showMain(true);
						}

					}
					for (int i = 0; i < orderlist.size(); i++) {
//						orderlist.get(i).isShow=true;
						if (i==0) {
							orderlist.get(i).isShow=true;
						}else {
							String [] YearAndMonthOne = orderlist.get(i-1).starttime.split(" ")[0].split("-");
							String dataOne = YearAndMonthOne[0]+"-"+YearAndMonthOne[1];
							String [] YearAndMonthTwo = orderlist.get(i).starttime.split(" ")[0].split("-");
							String dataTwo = YearAndMonthTwo[0]+"-"+YearAndMonthTwo[1];
							if (dataOne.equals(dataTwo)) {
								orderlist.get(i).isShow=false;
							}else {
								orderlist.get(i).isShow=true;
							}
						}
					}
					adapter.notifyDataSetChanged();
					if (page == 1) {
						showMain(false);
					}
				} else {
					ToastUtil.showToastShortCenter(mActivity, msg);
					if (page == 1) {
						showMain(false);
					}
				}
				mHandler.sendEmptyMessageDelayed(1000, 800);
			} catch (JSONException e) {
				e.printStackTrace();
				if (page == 1) {
					showMain(false);
				}
				mHandler.sendEmptyMessageDelayed(1000, 800);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			prlList.onRefreshComplete();
			// pDialog.closeDialog();
			if (page == 1) {
				showMain(false);
			}
			mHandler.sendEmptyMessageDelayed(1000, 800);
		}

	};
	private TextView tvMsg1;
	private Button btRefresh;
	private RelativeLayout rlLoading;
	private LinearLayout llMain;

	private void showMain(boolean ismain) {
		if (ismain) {
			prlList.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			prlList.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onIndicatorSelected(int index) {
		// TODO Auto-generated method stub
		Utils.mLogError("index===" + index);
		Utils.mLogError("oldindex===" + oldindex);
		if (oldindex != index) {
			oldindex = index;
			page = 1;
			tidTopTab.setTabsDisplay(mActivity, index);
			orderlist.clear();
			adapter.notifyDataSetChanged();
			getData(index);
		}
	}

}
