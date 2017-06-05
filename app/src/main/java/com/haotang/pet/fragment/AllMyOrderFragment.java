package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.OrderDetailFromOrderActivity;
import com.haotang.pet.OrderDetailFromOrderToConfirmActivity;
import com.haotang.pet.OrderFosterDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.AllMyOrderAdapter;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.PullListView;
import com.haotang.pet.view.PullListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 全部订单
 * @author 乔彬
 *
 */
public class AllMyOrderFragment extends Fragment{

	private MainActivity mainActivity;
	private PullListView listView_all_my_order;
	private AllMyOrderAdapter allMyOrderAdapter;
//	private List<String> list = null;
	private MProgressDialog pDialog;
	private boolean ifShow = true;
	private boolean isFirst = true;
	private int page=1;
	private List<ArrayMap<String,Object>> orderAllList = new ArrayList<ArrayMap<String,Object>>();
	
	
//	---start 2015年12月15日13:56:25
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private Button bt_null_refresh;
//	---end 2015年12月15日13:56:30
//	static AllMyOrderFragment allMyOrderFragment=null;
//	public static AllMyOrderFragment getAllMyOrderFragment(){
//		if (allMyOrderFragment==null) {
//			allMyOrderFragment = new AllMyOrderFragment();
//		}
//		return allMyOrderFragment;
//	} 
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				List<Map<String,Object>> orderList = (List<Map<String,Object>>) msg.obj;
//				orderAllList.addAll(orderList);
//				Toast.makeText(mainActivity, "添加数据到总集合中", Toast.LENGTH_SHORT).show();
//				for (int i = 0; i < orderAllList.size(); i++) {
//					Utils.mLogError("==-->orderAllList"+orderAllList.get(i).get("totalPrice"));
//				}
//				allMyOrderAdapter.notifyDataSetChanged();
//				listView_all_my_order.onRefreshComplete();
				break;
			case 1:
//				showSelectDialog();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity =(MainActivity) activity;
//		Utils.mLogError("==-->AllMyOrder onAttach");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my_order_all, null);
		listView_all_my_order = (PullListView)view.findViewById(R.id.listView_all_my_order);
		no_has_data = (RelativeLayout) view.findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) view.findViewById(R.id.tv_null_msg1);
		bt_null_refresh = (Button) view.findViewById(R.id.bt_null_refresh);
		pDialog = new MProgressDialog(getActivity());
		
		
		initListener();
		return view;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initListener() {
		listView_all_my_order.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ArrayMap<String,Object> map = (ArrayMap<String, Object>) arg0.getItemAtPosition(arg2);
				int state = (Integer)map.get("status");
				int type =  (Integer) map.get("type");
//				state = 7;
				//当前可以获取状态可判断跳转执行动作
				Intent intent = null;
				if (state==1) {//待付款
					if (type==1) {
						toOrderToPay(map, state,OrderDetailFromOrderActivity.class);
					}else if (type==2) {
						toOrderToPay(map, state,OrderDetailFromOrderActivity.class);
					}
				}else if (state==2||state==22||state==21||state==23) {//已经付款
					if (type==1) {
						toConfirmActivity(map, state,OrderDetailFromOrderToConfirmActivity.class);
					}else if (type==2) {
						toConfirmActivity(map, state,OrderFosterDetailActivity.class);
					}
				}else if (state==3) {//待确认
					if (type==1) {
						toConfirmActivity(map, state,OrderDetailFromOrderToConfirmActivity.class);
					}else if (type==2) {
						toConfirmActivity(map, state,OrderFosterDetailActivity.class);
					}
				}else if (state==4) {//待评价
					if (type==1) {
						toConfirmActivity(map, state,OrderDetailFromOrderToConfirmActivity.class);
					}else if (type==2) {
						toConfirmActivity(map, state,OrderFosterDetailActivity.class);
					}
				}else if (state==5) {//已关闭  == 已完成
					if (type==1) {
						toConfirmActivity(map, state,OrderDetailFromOrderToConfirmActivity.class);
					}else if (type==2) {
						toConfirmActivity(map, state,OrderFosterDetailActivity.class);
					}
				}else if (state==6) {//已取消
					if (type==1) {
						toConfirmActivity(map, state,OrderDetailFromOrderToConfirmActivity.class);
					}else if (type==2) {
						toConfirmActivity(map, state,OrderFosterDetailActivity.class);
					}
				}else {
					/**
					 * state 2 ：目前状态 调试结束；(---产品说需要服务端配合（暂时先不管 等服务器）2015年6月25日14:41:48)
					 * state 3： 目前状态 调试结束；
					 * state 4： 目前状态 调试结束；
					 * state 5： 目前状态 调试结束；
					 * state 6：目前状态 调试结束；
					 */
					//待付款
//					state = 1;
//					toOrderToPay(map, state);
					//付款完成跳转界面测试
//					state = 6;
//					toConfirmActivity(map, state);
				}
			}

			private void toOrderToPay(ArrayMap<String, Object> map, int state,Class clazz) {
				Intent intent;
				intent = new Intent(getActivity(), clazz);
				intent.putExtra("orderid", (Integer)map.get("OrderId"));
				startActivity(intent);
			}

			private void toConfirmActivity(ArrayMap<String, Object> map, int state,Class clazz) {
				Intent intent;
				intent = new Intent(getActivity(),clazz);
				intent.putExtra("orderid", (Integer)map.get("OrderId"));
				startActivity(intent);
			}
		});
		
		listView_all_my_order.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(boolean isTop) {
				if (isTop) {
//					listView_all_my_order.onRefreshComplete();
					page = 1;
					orderAllList.clear();
					getData();
				}else {
					getData();
				}
			}
		});
		
		bt_null_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous", Global.PRE_ORDER_LIST_TO_MAINACTIVITY);
				mainActivity.sendBroadcast(intent);
			}
		});
	}

	private void setData() {
//		allMyOrderAdapter = new AllMyOrderAdapter(getActivity(),orderAllList);
//		listView_all_my_order.setMode(PullToRefreshBase.Mode.BOTH);
//		listView_all_my_order.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//			@Override
//			public void onRefresh(PullToRefreshBase refreshView) {
//				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
//				if(mode == Mode.PULL_FROM_START){
//					page = 1;
//					orderAllList.clear();
//					allMyOrderAdapter.notifyDataSetChanged();
//					getData();
//				}else{
//					getData();
//					for (int i = 0; i < orderAllList.size(); i++) {
//						Utils.mLogError("==-->orderAllList12 "+orderAllList.get(i).get("OrderId"));
//					}
//				}
//			}
//		});
//		listView_all_my_order.setAdapter(allMyOrderAdapter);
		allMyOrderAdapter = new AllMyOrderAdapter(getActivity(),orderAllList);
		listView_all_my_order.setAdapter(allMyOrderAdapter);
	}
	
	
	
	private AsyncHttpResponseHandler queryMyOrders = new AsyncHttpResponseHandler(){
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				if (ifShow) {
					pDialog.closeDialog();
					ifShow = false;
				}
				listView_all_my_order.onRefreshComplete();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject objectData = jsonObject.getJSONObject("data");
						if (objectData.has("nToBeComment")&&!objectData.isNull("nToBeComment")) {
							int nToBeComment = objectData.getInt("nToBeComment");
						}
						if (objectData.has("orders")&&!objectData.isNull("orders")) {
							JSONArray jsonArray = objectData.getJSONArray("orders");
							List<ArrayMap<String,Object>> orderList = new ArrayList<ArrayMap<String,Object>>();
							for (int i = 0; i < jsonArray.length(); i++) {
								ArrayMap<String,Object> map = new ArrayMap<String,Object>();
								JSONObject object = jsonArray.getJSONObject(i);
								if (object.has("appointment")&&!object.isNull("appointment")) {
									String appointment = object.getString("appointment");
									map.put("appointment", appointment);
//								Utils.mLogError("==-->AllMyFragment appointment:= "+appointment);
								}
								if (object.has("assessment")&&!object.isNull("assessment")) {
									String assessment = object.getString("assessment");
//								Utils.mLogError("==-->AllMyFragment assessment:= "+assessment);
								}
								if (object.has("coupon")&&!object.isNull("coupon")) {
									String coupon = object.getString("coupon");
//								Utils.mLogError("==-->AllMyFragment coupon:= "+coupon);
								}
								if (object.has("created")&&!object.isNull("created")) {
									String created = object.getString("created");
									String n = created.replace("年", "-");
									String y = n.replace("月", "-");
									String r = y.replace("日", " ");
									String s = r.replace("时", ":");
									String f = s.replace("分", ":");
									String m = f+"00";
									map.put("created", m);
//								Utils.mLogError("==-->AllMyFragment created:= "+created);
								}
								if (object.has("customerId")&&!object.isNull("customerId")) {
									int customerId = object.getInt("customerId");
//								Utils.mLogError("==-->AllMyFragment customerId:= "+customerId);
								}
								if (object.has("grade")&&!object.isNull("grade")) {
									int grade = object.getInt("grade");
//								Utils.mLogError("==-->AllMyFragment grade:= "+grade);
								}
								if (object.has("OrderId")&&!object.isNull("OrderId")) {
									int OrderId = object.getInt("OrderId");
									map.put("OrderId", OrderId);
//								Utils.mLogError("==-->AllMyFragment OrderId:= "+OrderId);
								}
								//服务地址暂时不取----
								if (object.has("listAddress")&&!object.isNull("listAddress")) {
									JSONArray array = object.getJSONArray("listAddress");
									for (int j = 0; j < array.length(); j++) {
										if (j==0) {
											JSONObject object2 =  array.getJSONObject(0);
											if (object2.has("address")&&!object2.isNull("address")) {
												String address  = object2.getString("address");
												map.put("address",address);
//											Utils.mLogError("==-->AllMyFragment address:= "+address);
											}
										}
										
									}
								}
								if (object.has("listPrice")&&!object.isNull("listPrice")) {
									double listPrice = object.getDouble("listPrice");
									map.put("listPrice",listPrice);
//								Utils.mLogError("==-->AllMyFragment listPrice:= "+listPrice);
								}
								if (object.has("serviceLoc")&&!object.isNull("serviceLoc")) {
									map.put("serviceLoc",object.getInt("serviceLoc"));
								}else{
									map.put("serviceLoc",2);
								}
								if (object.has("pickUp")&&!object.isNull("pickUp")) {
									map.put("pickUp",object.getInt("pickUp"));
								}else{
									map.put("pickUp",0);
								}
								if (object.has("pet")&&!object.isNull("pet")) {
									JSONObject petObject = object.getJSONObject("pet");
									if (petObject.has("avatarPath")&&!petObject.isNull("avatarPath")) {
										String avatarPath = petObject.getString("avatarPath");	
										map.put("avatarPath", avatarPath);
//									Utils.mLogError("==-->AllMyFragment avatarPath:= "+avatarPath);
									}
									if (petObject.has("description")&&!petObject.isNull("description")) {
										String description = petObject.getString("description");
//									Utils.mLogError("==-->AllMyFragment description:= "+description);
									}
									if (petObject.has("PetId")&&!petObject.isNull("PetId")) {
										int PetId = petObject.getInt("PetId");
										map.put("PetId", PetId);
//									Utils.mLogError("==-->AllMyFragment PetId:= "+PetId);
									}
									if (petObject.has("petKind")&&!petObject.isNull("petKind")) {
										String petKind = petObject.getString("petKind");
									}
									if (petObject.has("petName")&&!petObject.isNull("petName")) {
										String petName = petObject.getString("petName");
										map.put("petName", petName);
//									Utils.mLogError("==-->AllMyFragment petName:= "+petName);
									}
								}
								
								if (object.has("petId")&&!object.isNull("petId")) {
//							int petId = 
								}
								if (object.has("petService")&&!object.isNull("petService")) {
									int petService = object.getInt("petService");
									map.put("petService", petService);
								}
//								if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
//									String petServicePojo = object.getString("petServicePojo");
////								Utils.mLogError("==-->AllMyFragment petServicePojo:= "+petServicePojo);
//								}
								if (object.has("type")&&!object.isNull("type")) {
									int type = object.getInt("type"); //type = 1 是洗澡和美容 type = 2 是寄养
									map.put("type", type);
								}
								if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
									JSONObject petServicePojoObject = object.getJSONObject("petServicePojo");
									if (petServicePojoObject.has("name")&&!petServicePojoObject.isNull("name")) {
										String name = petServicePojoObject.getString("name");
//										Utils.mLogError("==-->AllMyFragment name:= "+name);
										map.put("name", name);
									}
									if (petServicePojoObject.has("serviceType")&&!petServicePojoObject.isNull("serviceType")) {
										int serviceType= petServicePojoObject.getInt("serviceType");
										map.put("serviceType", serviceType);
									}
								}
								if (object.has("status")&&!object.isNull("status")) {
									int status = object.getInt("status");
									map.put("status", status);
//								Utils.mLogError("==-->AllMyFragment status:= "+status);
								}
								if (object.has("statusDescription")&&!object.isNull("statusDescription")) {
									String statusDescription = object.getString("statusDescription");
									map.put("statusDescription", statusDescription);
//								Utils.mLogError("==-->AllMyFragment statusDescription:= "+statusDescription);
								}
								if (object.has("totalPrice")&&!object.isNull("totalPrice")) {
									double totalPrice = object.getDouble("totalPrice");
									map.put("totalPrice", totalPrice);
								}
								if (object.has("worker")&&!object.isNull("worker")) {
									String worker = object.getString("worker");
//								Utils.mLogError("==-->AllMyFragment worker:= "+worker);
								}
								if (object.has("workerId")&&!object.isNull("workerId")) {
									int workerId = object.getInt("workerId");
//								Utils.mLogError("==-->AllMyFragment workerId:= "+workerId);
								}
								if (object.has("task")&&!object.isNull("task")) {
									JSONObject objectTask = object.getJSONObject("task");
									if (objectTask.has("startTime")&&!objectTask.isNull("startTime")) {
										map.put("startTime", objectTask.getString("startTime"));
									}
									if (objectTask.has("stopTime")&&!objectTask.isNull("stopTime")) {
										map.put("stopTime", objectTask.getString("stopTime"));
									}
								}
								if (object.has("mypetId")&&!object.isNull("mypetId")) {
									map.put("mypetId", object.getInt("mypetId"));
								}
								if (object.has("myPet")&&!object.isNull("myPet")) {
									JSONObject objectMyPet = object.getJSONObject("myPet");
									if (objectMyPet.has("nickName")&&!objectMyPet.isNull("nickName")) {
										map.put("nickName", objectMyPet.getString("nickName"));
									}
									if (objectMyPet.has("avatarPath")&&!objectMyPet.isNull("avatarPath")) {
										map.put("nickavatarPath", objectMyPet.getString("avatarPath"));
									}
								}
								orderList.add(map);
							}
							orderAllList.addAll(orderList);
							if (jsonArray.length()>0) {
								page++;
							}
						}
					}
					if (orderAllList.size()<=0) {
						showMain(false);
					}
					allMyOrderAdapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listView_all_my_order.onRefreshComplete();
				if (ifShow) {
					pDialog.closeDialog();
					ifShow = false;
				}
			}
		}

		

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			listView_all_my_order.onRefreshComplete();
			if (ifShow) {
				pDialog.closeDialog();
				ifShow = false;
			}
		}
		
	};
	private void getData() {
		if (ifShow) {
			pDialog.showDialog();
		}
		CommUtil.queryMyOrders(SharedPreferenceUtil.getInstance(getActivity()).getString("cellphone", "0"), 
				Global.getIMEI(getActivity()),
				getActivity(),page,
				queryMyOrders);
	}
	
	@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			orderAllList.clear();
			page = 1;
//			Utils.mLogError("==-->AllMyOrder : page = "+page);
//			int wh[] = Utils.getDisplayMetrics(mainActivity);
//			LayoutParams params = listView_all_my_order.getLayoutParams();
//			params.width=wh[0];
//			params.height=wh[1]-100;
//			listView_all_my_order.setLayoutParams(params);
			getData();
			setData();
			
			MobclickAgent.onPageStart("AllMyOrderFragment"); //统计页面
		}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPageEnd("AllMyOrderFragment"); 
	}
	
	private void showMain(boolean ismain){
		if(ismain){
			listView_all_my_order.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		}else{
			listView_all_my_order.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			bt_null_refresh.setVisibility(View.VISIBLE);
			bt_null_refresh.setBackgroundResource(R.drawable.null_button_back);
			bt_null_refresh.setText("立即预约");
			tv_null_msg1.setText("您还没有订单,赶紧去约一单吧~");
		}
	}
}
