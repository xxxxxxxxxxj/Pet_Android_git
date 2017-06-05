package com.haotang.pet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CancleAdapter;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 寄养详情页,支持取消订单和已完成订单展示
 * --START 2016年1月22日15:24:18
 * 	  新增支持订单评价展示
 * --END 2016年1月22日15:24:22
 * @author Administrator
 *
 */
@SuppressLint("ResourceAsColor") public class OrderFosterDetailActivity extends SuperActivity{

	private ImageButton ibBack;
	private TextView tvTitle;
	private Button bt_titlebar_other;
	private TextView order_status;
	private TextView tv_orderdetail_servicename;
	private TextView textview_foster_come_in_time;
	private TextView textview_foster_go_out_time;
	private TextView textview_foster_home_style;
	private TextView textview_pet_food_detail;
	private LinearLayout layout_foster_is_pickup;
	private TextView textview_foster_pickup_detail;
	private TextView textview_foster_pet_address;
	private TextView textview_foster_phone_people;
	private TextView textView_foster_Remarks;
	private TextView textview_foster_price_num;
	private TextView textview_foster_price_detail;
	private LinearLayout layout_foster_pet_food_detail;
	private TextView textview_foster_pet_food_num_detail;
	private TextView textview_foster_pet_food_price_detail;
	private LinearLayout layout_foster_ispickup_detail;
	private TextView textview_foster_pickUp_price_detail;
	private TextView textview_foster_coupon_dis;
	private TextView textview_foster_last_price;
	private TextView textview_foster_order_num;
	private TextView textview_foster_order_git;
	private TextView textview_foster_order_over;
	
	private LinearLayout layout_foster_pet_foodprice;
	private LinearLayout layout_foster_coupon_show;
	private PullToRefreshScrollView prs_orderdetail_main;
	
	private SelectableRoundedImageView sriv_petstore_image;
	private TextView tv_petstore_name;
	private TextView tv_petstore_addr;
	private TextView tv_petstore_phone;
	
	private SharedPreferenceUtil spUtil;
	private int OrderId=0;
	private int status =0;
	private int ndays = 0 ;
	private PopupWindow pWinCancle;
	private LayoutInflater mInflater;
	private MProgressDialog pDialog ;
	
//	---start 2015年12月30日16:16:27 新增寄养退款
	private LinearLayout layout_foster_money_addOrReduce;
	private TextView textview_foster_addOrReduce_title;
	private TextView textview_foster_addOrReduce_days;
	private TextView textview_foster_addOrReduce_price;
	
//	private LinearLayout layout_foster_food_addOrReduce;
//	private TextView textview_foster_food_addOrReduce_title;
//	private TextView textview_foster_food_addOrReduce_days;
//	private TextView textview_foster_food_addOrReduce_price;
	
//	---end  2015年12月30日16:16:35
	
//	---start 2016年1月19日10:59:51
	private RelativeLayout layout_confirm;
	private Button bt_orderdetail_pay;
	
	public static OrderFosterDetailActivity fosterDetailActivity;
//	---end 2016年1月19日10:59:54
//	------start 2016年3月2日10:43:27
	private ListView listview_cancle_notice;
	private CancleAdapter cancleAdapter;
	private PopupWindow pWinChoose;
	private List<Integer> pos = new ArrayList<Integer>();
	private LinearLayout layout_foster_is_wash;
	private TextView textview_foster_wash_detail;
	
	private String ReasonGit="";
	
	private LinearLayout layout_foster_pet_wash_detail;
	private TextView textview_foster_pet_wash_price_detail;
	
	private PopupWindow pWinPacket;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private ImageView ImageView_red_packets;
	private int BackCode = 0;
	private LinearLayout foster_petstore;
	private int serviceId = 0;
	private int shopId = 0;
	private ImageView imageview_status;
	private ImageView imageview_to_service;
	private String phoneNum = "";
	private int payWay;
	private TextView textview_foster_home_address;
	private TextView textview_foster_home_address_title;
	private int type;
	private TextView textview_foster_order_pay_type;
	private MReceiver receiver;
	private TextView textview_show_pet_type;
	private TextView textview_show_train_content;
	private TextView textview_train_time;
	private TextView textview_train_address;
	private LinearLayout layout_pet_address;
	private View view_line;
	private TextView textview_train_name_price;
	private LinearLayout layout_middle_address_left;
	
//	------end 2016年3月2日10:43:35
	//寄养摄像头start
	private LinearLayout layout_foster_shexiangtou;
	private TextView textview_foster_price_shexiangtou;
	private TextView textview_foster_price_shexiangtou_num;
	//寄养摄像头end
	private String orderTitle="";
	
	private LinearLayout layout_compl_parent;
	private TextView textview_compl_title;
	private TextView textview_show_compl_reason;
	private TextView textview_show_compl_status;
	
	
	private RelativeLayout layout_confirm_eva_complaints;
	private LinearLayout layout_evaandmore;
	private Button bt_only_eva;
	private Button bt_complaints;
	private OrdersBean ordersBean = new OrdersBean();
	private LinearLayout layout_copy_ordernum;
	private LinearLayout layout_foster_promoterCode_show;
	private TextView textview_foster_promoterCode_dis;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Intent intent  = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous", Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY);
				sendBroadcast(intent);
				Utils.mLogError("发送广播");
				break;
			case 2:
				try {
					if (pWinCancle.isShowing()) {
						pWinCancle.dismiss();
						pWinCancle = null;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int codeNum = msg.arg1;
				if (codeNum==0) {
					//订单取消成功，广播刷新订单
					intent = new Intent();
					intent.setAction("android.intent.action.mainactivity");
					intent.putExtra("previous", Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
					sendBroadcast(intent);
					finishWithAnimation();
				}
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_foster);
		fosterDetailActivity  = this;
		OrderId = getIntent().getIntExtra("orderid", 0);
		findView();
		setView();
		configPlatforms();
		initListener();
		initReceiver();
		
	}
	private void initReceiver() {
		receiver = new MReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		imageview_to_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!phoneNum.equals("")) {
					MDialog mDialog = new MDialog.Builder(fosterDetailActivity)
							.setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
							.setMessage("是否拨打电话?").setCancelStr("否")
							.setOKStr("是")
							.positiveListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 确认拨打电话
									Utils.telePhoneBroadcast(
											fosterDetailActivity, phoneNum);
								}
							}).build();
					mDialog.show();
				}
			}
		});
		foster_petstore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toNext(GoShopDetailActivity.class);
			}
		});
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferenceUtil.getInstance(OrderFosterDetailActivity.this).saveInt("state", status);
				finishWithAnimation();
			}
		});
		ImageView_red_packets.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pWinPacket!=null) {
					if (!pWinPacket.isShowing()) {
						getData();
//						showPop();
					}else {
						pWinPacket.dismiss();
						pWinPacket = null;
						Utils.onDismiss(fosterDetailActivity);
					}
				}else {
					getData();
//					showPop();
				}
			}
		});
		
		bt_titlebar_other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_CancelOrder);
				Intent intentCancle = new Intent(fosterDetailActivity, OrderCancleActivity.class);
//				intentCancle.putExtra("state", state);
				intentCancle.putExtra("type", type);
				intentCancle.putExtra("payWay", payWay);
				intentCancle.putExtra("orderid", OrderId);
				//需要带上各种订单id type 什么
				startActivity(intentCancle);

				//取错了接口 更新为寄养接口 原因
/*				CommUtil.orderHotelCancelRemindTxt(
						spUtil.getString("cellphone", ""), 
						Global.getIMEI(fosterDetailActivity), 
						fosterDetailActivity
						,getCancleDetail);
				pWinChoose = null;
				if(pWinChoose==null){
					View view = mInflater.inflate(R.layout.dlg_cancle_order_new, null);
					listview_cancle_notice = (ListView) view.findViewById(R.id.listview_cancle_notice);
					Button button_cancle = (Button) view.findViewById(R.id.button_cancle);
					Button button_ok = (Button) view.findViewById(R.id.button_ok);
					LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_dlg_cancle_main);
					
					pWinChoose = new PopupWindow(view,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
					pWinChoose.setBackgroundDrawable(new BitmapDrawable());
					pWinChoose.setOutsideTouchable(true);
					pWinChoose.setFocusable(true);
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
//					pWinChoose.setWidth(dm.widthPixels - 80);
//					pWinChoose.setHeight(dm.heightPixels - dm.heightPixels/2);
					LayoutParams lp = new LayoutParams(dm.widthPixels - 80, dm.heightPixels/2);
					llMain.setLayoutParams(lp);
					pWinChoose.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//					Utils.setAttribute(fosterDetailActivity, pWinChoose);
					button_cancle.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							pos.clear();
//							if (pWinChoose.isShowing()) {
//							}
//							Utils.onDismiss(fosterDetailActivity);
							pWinChoose.dismiss();
							pWinChoose = null;
						}
					});
					button_ok.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (pos.size()<=0) {
								ToastUtil.showToastShortCenter(fosterDetailActivity, "请先选择取消订单的原因哦");
							}else {
								pos.clear();
								// TODO Auto-generated method stub
//								if (pWinChoose.isShowing()) {
//								}
//								Utils.onDismiss(fosterDetailActivity);
								CommUtil.prepCancelOrder(spUtil.getString("cellphone", ""), Global.getIMEI(OrderFosterDetailActivity.this), OrderFosterDetailActivity.this, OrderId, prepCancelOrder);
								pWinChoose.dismiss();
								pWinChoose = null;
							}
						}
					});
					
					listview_cancle_notice.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							try {
								pos.clear();
								pos.add(position);
								ReasonGit = (String) parent.getItemAtPosition(position);
								cancleAdapter.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
//					pWinChoose.setOnDismissListener(new OnDismissListener() {
//						
//						@Override
//						public void onDismiss() {
//							// TODO Auto-generated method stub
//							Utils.onDismiss(fosterDetailActivity);
//						}
//					});
				}*/
			}
		});
		bt_orderdetail_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(EvaluateActivity.class, Global.ORDERFOSTERDETAILACTIVITY_TO_EVA);
			}
		});
		
		bt_only_eva.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(EvaluateActivity.class, Global.ORDERFOSTERDETAILACTIVITY_TO_EVA);
			}
		});
		bt_complaints.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentCompl = new Intent(fosterDetailActivity, ComplaintActivity.class);
				intentCompl.putExtra("ordersBean", ordersBean);
				startActivityForResult(intentCompl, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
			}
		});
		layout_copy_ordernum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(textview_foster_order_num.getText());
				ToastUtil.showToastShortCenter(mContext, "复制成功");
			}
		});
	}
	// 取消订单
	private AsyncHttpResponseHandler prepCancelOrder = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					if (object.has("data")&&!object.isNull("data")) {
						//取消订单
						showSelectDialog(code,object.getString("data"));
					}
				}else if (code == 618) {
					if (object.has("msg")&&!object.isNull("msg")) {
						//取消订单
						showSelectDialog(code,object.getString("msg"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};

	private void showSelectDialog(int code ,String str) {
	   if (status==2||status==22||status==21||status==23) {//已经付款 --当前位置状态忘添加了。。
		   if (code==0) {
			   CommUtil.ReasonCancelOrder(
						spUtil.getString("cellphone", ""), 
						Global.getIMEI(OrderFosterDetailActivity.this), 
						OrderFosterDetailActivity.this, 
						OrderId,ReasonGit,cancelOrder);
		   }else if (code ==618) {
			   MDialog mDialog = new MDialog.Builder(OrderFosterDetailActivity.this)
				.setTitle("提示")
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage(str)
				.setCancelStr("否")
				.setOKStr("是")
				.positiveListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CommUtil.ReasonCancelOrder(
								spUtil.getString("cellphone", ""), 
								Global.getIMEI(OrderFosterDetailActivity.this), 
								OrderFosterDetailActivity.this, 
								OrderId,ReasonGit,cancelOrder);
					}
				}).build();
				mDialog.show();
		   }
		}
	
	}
	
	
	//时间到不可取消 --- 取消订单时请求服务器返回 然后执行该方法
	private void alreadyCancleOrder(String msg,final String data){
		MDialog mDialog = new MDialog.Builder(OrderFosterDetailActivity.this)
		.setTitle("提示")
		.setType(MDialog.DIALOGTYPE_CONFIRM)
		.setMessage(msg)
		.setCancelStr("继续等待")
		.setOKStr("联系客服")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//执行联系客服
				Utils.telePhoneBroadcast(OrderFosterDetailActivity.this, data);
			}
		}).build();
		mDialog.show();
	};
	private void showWindowBackGround() {
		ColorDrawable cd = new ColorDrawable(0x000000);
		pWinCancle.setBackgroundDrawable(cd); 
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
	}
	// 取消订单
	private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->取消订单"+new String(responseBody));
			
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					String msg =jsonObject.getString("msg");
					String data =jsonObject.getString("data");
					showCancleDetail(code,data);
					showWindowBackGround();
				}else if (code ==120) {
					String data ="";
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						data = jsonObject.getString("data");
					}
					String msg = jsonObject.getString("msg");
					alreadyCancleOrder(msg,data);
				}else {
					String msg = jsonObject.getString("msg");
					String data =jsonObject.getString("data");
					showCancleDetail(code,data);
					showWindowBackGround();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(OrderFosterDetailActivity.this, "网络连接失败 请检查您的网络",Toast.LENGTH_SHORT).show();
		}
	};
	
	public void onDismiss(){
	    WindowManager.LayoutParams lp=getWindow().getAttributes();
	    lp.alpha = 1f;
	    getWindow().setAttributes(lp);	
	}
	private void showCancleDetail(final int code,final String msg) {
		pWinCancle = null;
		if (pWinCancle == null) {
			View view = mInflater.inflate(R.layout.dlg_cancle_order_success_or_fail, null);
			TextView textView_title=	(TextView) view.findViewById(R.id.textView_title);
			TextView textView_bottom=	(TextView) view.findViewById(R.id.textView_bottom);
			TextView textView_message_last=	(TextView) view.findViewById(R.id.textView_message_last);
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_dlg_cancle_main);
			pWinCancle = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWinCancle.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			pWinCancle.setWidth(dm.widthPixels - 80);
			LayoutParams lp = new LayoutParams(dm.widthPixels - 80, 
					Utils.dip2px(OrderFosterDetailActivity.this, 150));
			llMain.setLayoutParams(lp);
			pWinCancle.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//			
			if (code == 0 ) {
				textView_title.setText("订单取消成功!");
				textView_message_last.setText(msg);
				textView_bottom.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Message message = Message.obtain();
						message.arg1=code;
						message.what=2;
						handler.sendMessage(message);
						onDismiss();
					}
				});
			}else {
				textView_title.setText("订单取消失败!");
				textView_message_last.setText("  "+msg);
				textView_bottom.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (pWinCancle.isShowing()) {
							pWinCancle.dismiss();
							pWinCancle = null;
							onDismiss();
						}
					}
				});
				
			}

		}
	}
	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("订单详情");
		bt_titlebar_other.setVisibility(View.GONE);
		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		prs_orderdetail_main.setMode(Mode.PULL_FROM_START);
		prs_orderdetail_main.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					getOrderDetails();
				}
			}
		});
		getOrderDetails();
	}
	//查询订单明细
	private void getOrderDetails() {
		pDialog.showDialog();
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),  
				Global.getIMEI(this), this,OrderId, getOrderDetails);
	}

	//订单明细
	private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				prs_orderdetail_main.onRefreshComplete();
				pDialog.closeDialog();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						
						if (object.has("status")&&!object.isNull("status")) {
							status = object.getInt("status");
//							status = 4;
							if (status==2) {//已经付款
								order_status.setText("已付款");
								bt_titlebar_other.setVisibility(View.VISIBLE);
								bt_titlebar_other.setText("取消订单");
								layout_confirm.setVisibility(View.GONE);
								ImageView_red_packets.setVisibility(View.GONE);
								
							}else {
								bt_titlebar_other.setVisibility(View.GONE);
								ImageView_red_packets.setVisibility(View.GONE);
								if (status==4) {
									layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
									layout_confirm.setVisibility(View.GONE);
								}else {
									layout_confirm.setVisibility(View.GONE);
								}
							}
							//新增已完成状态
							if (status==5) {
								layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
								bt_only_eva.setVisibility(View.GONE);
								
								
								textview_foster_order_over.setVisibility(View.VISIBLE);
								ImageView_red_packets.setVisibility(View.GONE);
								if (BackCode==0) {//初次进入没说是否弹窗预留
//									ImageView_red_packets.setVisibility(View.GONE);
								}else if(BackCode==Global.ORDERFOSTERDETAILACTIVITY_TO_EVA){//code
									getData();//评价回来显示弹窗与否后台控制初次进入是否显示标签
								}
							}else {
								textview_foster_order_over.setVisibility(View.GONE);
								ImageView_red_packets.setVisibility(View.GONE);
							}
							
							if (status==2) {
								imageview_status.setBackgroundResource(R.drawable.already_pay);
							}else if (status==5) {
								imageview_status.setBackgroundResource(R.drawable.order_already_over);
							}else if (status==4) {
								imageview_status.setBackgroundResource(R.drawable.to_wai_eva);
							}else if (status==6) {
								imageview_status.setBackgroundResource(R.drawable.order_already_cancle);
							}else if (status==22) {
								imageview_status.setBackgroundResource(R.drawable.foster_doing);
							}
						}
						if (object.has("feedback")&&!object.isNull("feedback")) {
							
							layout_compl_parent.setVisibility(View.VISIBLE);
							bt_complaints.setVisibility(View.GONE);
							JSONObject feedback = object.getJSONObject("feedback");
//							if (feedback.has("content")&&!feedback.isNull("content")) {
								textview_compl_title.setText("投诉内容");
//							}
//							if (feedback.has("reason")&&!feedback.isNull("reason")) {
//								String reason = feedback.getString("reason");
//								String [] reasons = reason.split(",");
//								StringBuilder reasonBuilder = new StringBuilder();
//								for (int i = 0; i < reasons.length; i++) {
//									if (i==reasons.length-1) {
//										reasonBuilder.append(reasons[i]);
//									}else {
//										reasonBuilder.append(reasons[i]+"\n\n");
//									}
//								}
//								textview_show_compl_reason.setText(reasonBuilder.toString());
//							}
							if (feedback.has("reason")&&!feedback.isNull("reason")) {
								JSONArray arrayReason = feedback.getJSONArray("reason");
								StringBuilder reasonBuilder = new StringBuilder();
								if (arrayReason.length()>0) {
									for (int i = 0; i < arrayReason.length(); i++) {
										if (i==arrayReason.length()-1) {
											reasonBuilder.append(arrayReason.getString(i));
											if (feedback.has("content")&&!feedback.isNull("content")) {
												reasonBuilder.append("\n\n"+feedback.getString("content"));
											}
										}else {
											reasonBuilder.append(arrayReason.getString(i)+"\n\n");
										}
									}
								}else {
									reasonBuilder.append(feedback.getString("content"));
								}
								textview_show_compl_reason.setText(reasonBuilder.toString());
							}
							if (feedback.has("managerStatus")&&!feedback.isNull("managerStatus")) {
								int managerStatus = feedback.getInt("managerStatus");
								if (managerStatus==0) {
									textview_show_compl_status.setText("正在处理中,请您耐心等候");
									
								}else if (managerStatus==1) {
									textview_show_compl_status.setText("处理完成");
								}
							}
							if (feedback.has("managerStatusName")&&!feedback.isNull("managerStatusName")) {
								textview_show_compl_status.setText(feedback.getString("managerStatusName"));
							}
							if (status==5) {
								layout_confirm_eva_complaints.setVisibility(View.GONE);
							}
						}else {//没有反馈 状态4 我要投诉 评价均显示  状态5 我要投诉显示 评价隐藏
							layout_compl_parent.setVisibility(View.GONE);
							if (status == 4) {
								layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
							}else if (status == 5) {
								layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
								bt_complaints.setVisibility(View.VISIBLE);
								bt_only_eva.setVisibility(View.GONE);
							}
						}
						if (object.has("orderTitle")&&!object.isNull("orderTitle")) {
							orderTitle = object.getString("orderTitle");
							tvTitle.setText(orderTitle);
						}
						if (object.has("type")&&!object.isNull("type")) {
							type = object.getInt("type");
							ordersBean.setType(type);//投诉订单对象
						}
						if (object.has("payWay")&&!object.isNull("payWay")) {
							payWay = object.getInt("payWay");
						}
						if (object.has("payDesc")&&!object.isNull("payDesc")) {
							String payDesc = object.getString("payDesc");
							textview_foster_order_pay_type.setText("支付方式："+payDesc);
						}
						if (object.has("ndays")&&!object.isNull("ndays")) {
							ndays = object.getInt("ndays");
//							textview_foster_price_num.setText("X"+ndays);
//							textview_foster_pet_food_num_detail.setText("X"+ndays);
//							textview_foster_price_num.setText("X"+ndays);
						}
						if (object.has("statusDescription")&&!object.isNull("statusDescription")) {
							order_status.setText(object.getString("statusDescription"));
						}
						if (object.has("created")&&!object.isNull("created")) {
							textview_foster_order_git.setText("提交时间："+object.getString("created"));
						}
						if (object.has("orderNum")&&!object.isNull("orderNum")) {
							textview_foster_order_num.setText("订单编号："+object.getString("orderNum"));
							ordersBean.setOrderNum(object.getString("orderNum"));
						}
						StringBuilder builder = new StringBuilder();
						if (object.has("pet")&&!object.isNull("pet")) {
							JSONObject objectPet = object.getJSONObject("pet");
							if (objectPet.has("petName")&&!objectPet.isNull("petName")) {
								builder.append(objectPet.getString("petName").trim());
							}
							if (objectPet.has("avatarPath")&&!objectPet.isNull("avatarPath")) {
								String avatarPath = objectPet.getString("avatarPath");
								ordersBean.setAvatar(avatarPath);
							}
						}
						ordersBean.setId(OrderId);
						StringBuilder roomSet = new StringBuilder();
						String RoomName ="";
						if (object.has("roomType")&&!object.isNull("roomType")) {
							JSONObject objectRoom = object.getJSONObject("roomType");
							if (objectRoom.has("name")&&!objectRoom.isNull("name")) {
								String name = objectRoom.getString("name");
								RoomName = name+" ";
								roomSet.append(name+" ");
//								builder.append(name);
//								textview_foster_home_style.setText(name);
							}
//							textview_foster_order_num.setText("订单编号："+object.getString("roomType"));
						}
						builder.append("寄养套餐");
						String nickName ="";
						if (object.has("myPet")&&!object.isNull("myPet")) {//寄养类型套餐
							JSONObject objectMyPet = object.getJSONObject("myPet");
							if (objectMyPet.has("nickName")&&!objectMyPet.isNull("nickName")) {
								nickName = objectMyPet.getString("nickName");
							}
						}
						if (nickName.equals("")) {
							tv_orderdetail_servicename.setText(builder);
						}else {
							tv_orderdetail_servicename.setText(nickName+"寄养套餐");
						}
						String RoomNum="";
						if (object.has("room")&&!object.isNull("room")) {
							JSONObject objectRoom = object.getJSONObject("room");
							if (objectRoom.has("num")&&!objectRoom.isNull("num")) {
								roomSet.append(objectRoom.getString("num"));
								RoomNum = objectRoom.getString("num");
							}
						}
						String RoomPrice ="";
						if (object.has("roomPrice")&&!object.isNull("roomPrice")) {
							JSONObject objectPrice = object.getJSONObject("roomPrice");
							if (objectPrice.has("price")&&!objectPrice.isNull("price")) {
								double roomPrice = objectPrice.getDouble("price");
								roomSet.append(" ¥"+roomPrice+"/天");
								RoomPrice = " ¥"+roomPrice+"/天";
								textview_foster_price_detail.setText(Utils.getTextAndColorCommentsBeau("#666666", "¥ "+Utils.formatDouble(roomPrice), "#666666", " X "+ndays+"天"));
							}
							if (objectPrice.has("cameraPrice")&&!objectPrice.isNull("cameraPrice")) {
								layout_foster_shexiangtou.setVisibility(View.VISIBLE);
								double cameraPrice = objectPrice.getDouble("cameraPrice");
								textview_foster_price_shexiangtou.setText(Utils.getTextAndColorCommentsBeau("#666666", "¥ "+Utils.formatDouble(cameraPrice), "#666666", " X "+ndays+"天"));
							}else {
								layout_foster_shexiangtou.setVisibility(View.GONE);
							}
						}
						textview_foster_home_style.setText(Utils.getTextAndColorThr("#7E7E7E", RoomName, "#BB996C", RoomNum, "#7E7E7E", RoomPrice));
						
//						if (object.has("pickUp")&&!object.isNull("pickUp")) {
//							int pickUp = object.getInt("pickUp");
//							if (pickUp==1) {
//								layout_foster_ispickup_detail.setVisibility(View.VISIBLE);
//								textview_foster_pickup_detail.setText("需要接送");
//								if (object.has("pickupPrice")&&!object.isNull("pickupPrice")) {
//									textview_foster_pickUp_price_detail.setText("¥ "+Utils.formatDouble(object.getDouble("pickupPrice")));
//								}
//							}else if (pickUp==2) {//不再接送范围内
//								textview_foster_pickup_detail.setText("不接送");
////								layout_foster_is_pickup.setVisibility(View.GONE);//入住洗澡上方条目
//								layout_foster_ispickup_detail.setVisibility(View.GONE);
//							}else {
//								textview_foster_pickup_detail.setText("不接送");
//								layout_foster_ispickup_detail.setVisibility(View.GONE);
//							}
//						}else {
//							layout_foster_is_pickup.setVisibility(View.GONE);
//							layout_foster_ispickup_detail.setVisibility(View.GONE);
//						}
						
						if (object.has("address")&&!object.isNull("address")) {
							textview_foster_pet_address.setText(object.getString("address"));
						}
						
						StringBuilder stringBuilder = new StringBuilder();
						if (object.has("customerName")&&!object.isNull("customerName")) {
							stringBuilder.append(object.getString("customerName")+"  ");
						}
						if (object.has("customerMobile")&&!object.isNull("customerMobile")) {
							stringBuilder.append(object.getString("customerMobile"));
						}
						textview_foster_phone_people.setText(stringBuilder);
						
						if (object.has("remark")&&!object.isNull("remark")) {
							textView_foster_Remarks.setText(object.getString("remark"));
						}else {
							textView_foster_Remarks.setText("无");
						}
						
						if (object.has("basicPrice")&&!object.isNull("basicPrice")) {//寄养基础费用
//							textview_foster_price_detail.setText("¥ "+Utils.formatDouble(object.getDouble("basicPrice"))+"X"+ndays+"天");
							
						}
						if (object.has("foodTotalPrice")&&!object.isNull("foodTotalPrice")) {
							double foodTotalPrice = object.getDouble("foodTotalPrice");
//							textview_foster_pet_food_price_detail.setText("¥ "+Utils.formatDouble(foodTotalPrice)+"X"+ndays+"天");
							
							if (foodTotalPrice>0) {
								if (object.has("foodPrice")&&!object.isNull("foodPrice")) {
									double foodPrice = object.getDouble("foodPrice");
//									textview_pet_food_detail.setText("宠物家提供 "+"¥"+foodPrice+"/天");
									textview_foster_pet_food_price_detail.setText(Utils.getTextAndColorCommentsBeau("#666666", "¥ "+Utils.formatDouble(foodPrice), "#666666", " X "+ndays+"天"));
								}
							}else {
//								textview_pet_food_detail.setText("自带宠粮或到店购买");
								layout_foster_pet_food_detail.setVisibility(View.GONE);
//								layout_foster_pet_foodprice.setVisibility(View.VISIBLE);
							}
						}else {
//							textview_pet_food_detail.setText("自带宠粮或到店购买");
							layout_foster_pet_food_detail.setVisibility(View.GONE);
//							layout_foster_pet_foodprice.setVisibility(View.VISIBLE);
						}
						/**
						 * 新增入住洗澡
						 * start 2016年3月3日13:55:55 
						 */
						int bathRequired = -1;
						if (object.has("bathRequired")&&!object.isNull("bathRequired")) {
							bathRequired = object.getInt("bathRequired");
						}
						if (object.has("needBathTag")&&!object.isNull("needBathTag")) {
							String needBathTag = object.getString("needBathTag");
							bathRequired = 1;
						}else {
							bathRequired = -1;
						}
						if (object.has("bathPrice")&&!object.isNull("bathPrice")) {
							layout_foster_is_wash.setVisibility(View.GONE);
							layout_foster_pet_wash_detail.setVisibility(View.VISIBLE);
							double bathPrice = Utils.formatDouble(object.getDouble("bathPrice"));
							if (bathRequired!=-1) {//支持入住洗澡 
								if (bathPrice>=0) {
									textview_foster_wash_detail.setText("需要洗澡");
									textview_foster_pet_wash_price_detail.setText("¥ "+bathPrice);
								}else {
									textview_foster_wash_detail.setText("不需要洗澡");
									layout_foster_pet_wash_detail.setVisibility(View.GONE);
								}
							}else if(bathRequired==-1){//不支持洗澡
								layout_foster_is_wash.setVisibility(View.GONE);
								layout_foster_pet_wash_detail.setVisibility(View.GONE);
							}
						}else {
							if (bathRequired!=-1){
								layout_foster_is_wash.setVisibility(View.GONE);
								textview_foster_wash_detail.setText("不需要洗澡");
								layout_foster_pet_wash_detail.setVisibility(View.GONE);
							}else if (bathRequired==-1) {
								layout_foster_is_wash.setVisibility(View.GONE);
								layout_foster_pet_wash_detail.setVisibility(View.GONE);
							}
						}
						if (object.has("coupon")&&!object.isNull("coupon")) {
							JSONObject objectCoupon=object.getJSONObject("coupon");
							if (objectCoupon.has("amount")&&!objectCoupon.isNull("amount")) {
								textview_foster_coupon_dis.setText("- ¥"+objectCoupon.getDouble("amount"));
							}else {
								layout_foster_coupon_show.setVisibility(View.GONE);
							}
						}else {
							layout_foster_coupon_show.setVisibility(View.GONE);
						}
						if (object.has("payPrice")&&!object.isNull("payPrice")) {
							textview_foster_last_price.setText("¥ "+Utils.formatDouble(object.getDouble("payPrice")));
							ordersBean.setPay_price((int)Utils.formatDouble(object.getDouble("payPrice")));
						}
						ordersBean.setServiceLoc(1);
						ordersBean.setService(tv_orderdetail_servicename.getText().toString());
						setTypeName();
						if (object.has("task")&&!object.isNull("task")) {
							JSONObject objectTask = object.getJSONObject("task");
							if (objectTask.has("startTime")&&!objectTask.isNull("startTime")) {
								textview_foster_come_in_time.setText(objectTask.getString("startTime"));
								ordersBean.setAppointment(objectTask.getString("startTime"));
							}
							if (objectTask.has("stopTime")&&!objectTask.isNull("stopTime")) {
								String stoptime = objectTask.getString("stopTime");
								textview_foster_go_out_time.setText(stoptime+" 共"+ndays+"天");
								String[] strstoptime = stoptime.split(" ")[0].split("-");
								int year = calendar.get(Calendar.YEAR);
								int month = calendar.get(Calendar.MONTH);
								int day = calendar.get(Calendar.DAY_OF_MONTH);
								int length = strstoptime.length;
//								if(length>0&&year>Integer.parseInt(strstoptime[0])){
//									bt_titlebar_other.setVisibility(View.GONE);
//								}else if(length>1&&month>Integer.parseInt(strstoptime[1])+1){
//									bt_titlebar_other.setVisibility(View.GONE);
//								}else if(length>2&&day>Integer.parseInt(strstoptime[2])){
//									bt_titlebar_other.setVisibility(View.GONE);
//								}else{
//									bt_titlebar_other.setVisibility(View.VISIBLE);
//								}
							}
							//完成时间
							if (objectTask.has("actualEndTime")&&!objectTask.isNull("actualEndTime")) {
								textview_foster_order_over.setText("完成时间："+objectTask.getString("actualEndTime"));
							}
							if (objectTask.has("serviceId")&&!objectTask.isNull("serviceId")) {
								serviceId = objectTask.getInt("serviceId");
							}
							if (objectTask.has("shopId")&&!objectTask.isNull("shopId")) {
								shopId = objectTask.getInt("shopId");
							}
						}
						if (object.has("refund")&&!object.isNull("refund")) {
							double refund = object.getDouble("refund");
							if (refund>0) {
								layout_foster_money_addOrReduce.setVisibility(View.VISIBLE);//处理本界面刷新显示退款。gone掉没重新显示
								textview_foster_addOrReduce_price.setText("-¥ "+Utils.formatDouble(refund));
							}else {
								layout_foster_money_addOrReduce.setVisibility(View.GONE);
							}
						}else {
							layout_foster_money_addOrReduce.setVisibility(View.GONE);
						}
						if (object.has("petFoodTag")&&!object.isNull("petFoodTag")) {
							textview_pet_food_detail.setText(object.getString("petFoodTag"));
						}
						if (object.has("shop")&&!object.isNull("shop")) {
							JSONObject objectShop = object.getJSONObject("shop");
							if (objectShop.has("shopName")&&!objectShop.isNull("shopName")) {
								tv_petstore_name.setText(objectShop.getString("shopName"));
								textview_foster_home_address.setText(objectShop.getString("shopName"));
							}
							if (objectShop.has("address")&&!objectShop.isNull("address")) {
								tv_petstore_addr.setText(objectShop.getString("address"));
							}
							if (objectShop.has("phone")&&!objectShop.isNull("phone")) {
								phoneNum = objectShop.getString("phone");
								tv_petstore_phone.setText(objectShop.getString("phone"));
							}
							if (objectShop.has("hotelName")&&!objectShop.isNull("hotelName")) {
								textview_foster_home_address_title.setText("寄养中心     ");
								textview_foster_home_address.setText(objectShop.getString("hotelName"));
							}
							if (objectShop.has("transferName")&&!objectShop.isNull("transferName")) {
								textview_foster_home_address_title.setText("中  转  站     ");
								textview_foster_home_address.setText(objectShop.getString("transferName"));
							}
							if (objectShop.has("img")&&!objectShop.isNull("img")) {
								String imgUrl = CommUtil.getServiceNobacklash()+objectShop.getString("img");
								ImageLoaderUtil.loadImg(imgUrl, sriv_petstore_image,0, new ImageLoadingListener() {
									
									@Override
									public void onLoadingStarted(String arg0, View arg1) {
										
									}
									
									@Override
									public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onLoadingComplete(String path, View view, Bitmap bitmap) {
										// TODO Auto-generated method stub
										if(view!=null&&bitmap!=null){
											ImageView iv = (ImageView) view;
											iv.setImageBitmap(bitmap);
										}
									}
									
									@Override
									public void onLoadingCancelled(String arg0, View arg1) {
										// TODO Auto-generated method stub
										
									}
								});
							}
						}
						
						if (type==4) {
							layout_foster_shexiangtou.setVisibility(View.GONE);
							//start 训练
							textview_show_pet_type.setText("宠物类型    ");
							textview_show_train_content.setText("训练内容    ");
							textview_train_time.setText("入训时间    ");
							textview_foster_home_address_title.setText("中  转  站     ");
							textview_train_address.setText("训练基地    ");
							layout_pet_address.setVisibility(View.GONE);
							view_line.setVisibility(View.VISIBLE);
							textview_train_name_price.setText("训练费用");
							String petName="";
							if (object.has("pet")&&!object.isNull("pet")) {
								JSONObject objectPet = object.getJSONObject("pet");
								
								if (objectPet.has("petName")&&!objectPet.isNull("petName")) {
									petName = objectPet.getString("petName");
									textview_foster_come_in_time.setText(petName);
								}
							}
							if (object.has("myPet")&&!object.isNull("myPet")) {//寄养类型套餐
								JSONObject objectMyPet = object.getJSONObject("myPet");
								if (objectMyPet.has("nickName")&&!objectMyPet.isNull("nickName")) {
									nickName = objectMyPet.getString("nickName");
								}
							}
							String ServiceName="";
							if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
								JSONObject objectPetService = object.getJSONObject("petServicePojo");
								if (objectPetService.has("name")&&!objectPetService.isNull("name")) {
									ServiceName = objectPetService.getString("name");
								}
								if (objectPetService.has("description")&&!objectPetService.isNull("description")) {
									String description = objectPetService.getString("description");
									textview_foster_go_out_time.setText(description);
								}
							}
							if (nickName.equals("")) {
								tv_orderdetail_servicename.setText(petName+ServiceName);
							}else {
								tv_orderdetail_servicename.setText(nickName+ServiceName);
							}
							if (object.has("appointment")&&!object.isNull("appointment")) {
								textview_foster_home_style.setText(object.getString("appointment").replace("00", "").replace(":", "").trim());
							}
							if (object.has("coupon")&&!object.isNull("coupon")) {
								JSONObject objectCoupon=object.getJSONObject("coupon");
								if (objectCoupon.has("amount")&&!objectCoupon.isNull("amount")) {
									textview_foster_coupon_dis.setText("- ¥"+objectCoupon.getDouble("amount"));
								}else {
									layout_foster_coupon_show.setVisibility(View.GONE);
								}
							}else {
								layout_foster_coupon_show.setVisibility(View.GONE);
							}
							if (object.has("basicPrice")&&!object.isNull("basicPrice")) {
								textview_foster_price_detail.setText("¥"+object.getInt("basicPrice"));
							}
							if (object.has("shop")&&!object.isNull("shop")) {
								JSONObject objectShop = object.getJSONObject("shop");
								if (objectShop.has("transferName")&&!objectShop.isNull("transferName")) {
									layout_middle_address_left.setVisibility(View.VISIBLE);
									String transferName = objectShop.getString("transferName");
									textview_foster_home_address.setText(transferName);
								}else {
									layout_middle_address_left.setVisibility(View.GONE);
								}
								if (objectShop.has("shopName")&&!objectShop.isNull("shopName")) {
									layout_foster_pet_foodprice.setVisibility(View.VISIBLE);
									String shopName = objectShop.getString("shopName");
									textview_pet_food_detail.setText(shopName);
								}else {
									layout_foster_pet_foodprice.setVisibility(View.GONE);
								}
							}
							if (object.has("appointEnd")&&!object.isNull("appointEnd")) {
								String appointEnd = object.getString("appointEnd");
								textview_foster_order_over.setText("完成时间："+appointEnd);
							}
							if (object.has("refund")&&!object.isNull("refund")) {
								double refund = object.getDouble("refund");
								if (refund>0) {
									layout_foster_money_addOrReduce.setVisibility(View.VISIBLE);//处理本界面刷新显示退款。gone掉没重新显示
									textview_foster_addOrReduce_price.setText("-¥ "+Utils.formatDouble(refund));
								}else {
									layout_foster_money_addOrReduce.setVisibility(View.GONE);
								}
							}else {
								layout_foster_money_addOrReduce.setVisibility(View.GONE);
							}
							if (bt_complaints.getVisibility()==View.GONE&&bt_only_eva.getVisibility()==View.GONE) {
								layout_confirm_eva_complaints.setVisibility(View.GONE);
							}
							//end 训练
						}
						
						//统一增加邀请码优惠
						if (object.has("orderFee")&&!object.isNull("orderFee")) {
							double orderFee = object.getDouble("orderFee");
							if (orderFee>0) {
								layout_foster_promoterCode_show.setVisibility(View.VISIBLE);
								textview_foster_promoterCode_dis.setText("-¥"+orderFee);
							}else {
								layout_foster_promoterCode_show.setVisibility(View.GONE);
							}
						}else {
							layout_foster_promoterCode_show.setVisibility(View.GONE);
						}
//						if (object.has("promoterCode")&&!object.isNull("promoterCode")) {
//							if (object.has("orderFee")&&!object.isNull("orderFee")) {
//								double orderFee = object.getDouble("orderFee");
//								if (orderFee>0) {
//									layout_foster_promoterCode_show.setVisibility(View.VISIBLE);
//									textview_foster_promoterCode_dis.setText("-¥"+orderFee);
//								}else {
//									layout_foster_promoterCode_show.setVisibility(View.GONE);
//								}
//							}else {
//								layout_foster_promoterCode_show.setVisibility(View.GONE);
//							}
//						}else {
//							layout_foster_promoterCode_show.setVisibility(View.GONE);
//						}
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				prs_orderdetail_main.onRefreshComplete();
				pDialog.closeDialog();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};

	
	private void findView() {
		// TODO Auto-generated method stub
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		mInflater = LayoutInflater.from(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		prs_orderdetail_main = (PullToRefreshScrollView) findViewById(R.id.prs_orderdetail_main);
		order_status = (TextView) findViewById(R.id.order_status);
		tv_orderdetail_servicename = (TextView) findViewById(R.id.tv_orderdetail_servicename);
		textview_foster_come_in_time = (TextView) findViewById(R.id.textview_foster_come_in_time);
		textview_foster_go_out_time = (TextView) findViewById(R.id.textview_foster_go_out_time);
		textview_foster_home_style = (TextView) findViewById(R.id.textview_foster_home_style);
		textview_pet_food_detail = (TextView) findViewById(R.id.textview_pet_food_detail);
		layout_foster_is_pickup = (LinearLayout) findViewById(R.id.layout_foster_is_pickup);
		textview_foster_pickup_detail = (TextView) findViewById(R.id.textview_foster_pickup_detail);
		textview_foster_pet_address = (TextView) findViewById(R.id.textview_foster_pet_address);
		textview_foster_phone_people = (TextView) findViewById(R.id.textview_foster_phone_people);
		textView_foster_Remarks = (TextView) findViewById(R.id.textView_foster_Remarks);
		textview_foster_price_num = (TextView) findViewById(R.id.textview_foster_price_num);
		textview_foster_price_detail = (TextView) findViewById(R.id.textview_foster_price_detail);
		layout_foster_pet_food_detail = (LinearLayout) findViewById(R.id.layout_foster_pet_food_detail);
		textview_foster_pet_food_num_detail = (TextView) findViewById(R.id.textview_foster_pet_food_num_detail);
		textview_foster_pet_food_price_detail = (TextView) findViewById(R.id.textview_foster_pet_food_price_detail);
		layout_foster_ispickup_detail = (LinearLayout) findViewById(R.id.layout_foster_ispickup_detail);
		textview_foster_pickUp_price_detail = (TextView) findViewById(R.id.textview_foster_pickUp_price_detail);
		textview_foster_coupon_dis = (TextView) findViewById(R.id.textview_foster_coupon_dis);
		textview_foster_last_price = (TextView) findViewById(R.id.textview_foster_last_price);
		textview_foster_order_num = (TextView) findViewById(R.id.textview_foster_order_num);
		textview_foster_order_git = (TextView) findViewById(R.id.textview_foster_order_git);
		textview_foster_order_over = (TextView) findViewById(R.id.textview_foster_order_over);
		sriv_petstore_image = (SelectableRoundedImageView) findViewById(R.id.sriv_petstore_image);
		tv_petstore_name = (TextView) findViewById(R.id.tv_petstore_name);
		tv_petstore_addr = (TextView) findViewById(R.id.tv_petstore_addr);
		tv_petstore_phone = (TextView) findViewById(R.id.tv_petstore_phone);
		layout_foster_coupon_show = (LinearLayout) findViewById(R.id.layout_foster_coupon_show);
		layout_foster_pet_foodprice = (LinearLayout) findViewById(R.id.layout_foster_pet_foodprice);
		
		layout_foster_money_addOrReduce = (LinearLayout) findViewById(R.id.layout_foster_money_addOrReduce);
		textview_foster_addOrReduce_title = (TextView) findViewById(R.id.textview_foster_addOrReduce_title);
		textview_foster_addOrReduce_days = (TextView) findViewById(R.id.textview_foster_addOrReduce_days);
		textview_foster_addOrReduce_price = (TextView) findViewById(R.id.textview_foster_addOrReduce_price);
		layout_confirm = (RelativeLayout) findViewById(R.id.layout_confirm);
		bt_orderdetail_pay = (Button) findViewById(R.id.bt_orderdetail_pay);
		
//		-------------start 2016年3月2日15:26:37
		layout_foster_is_wash = (LinearLayout) findViewById(R.id.layout_foster_is_wash);
		textview_foster_wash_detail = (TextView) findViewById(R.id.textview_foster_wash_detail);
		
		layout_foster_pet_wash_detail = (LinearLayout) findViewById(R.id.layout_foster_pet_wash_detail);
		textview_foster_pet_wash_price_detail = (TextView) findViewById(R.id.textview_foster_pet_wash_price_detail);
		
		ImageView_red_packets = (ImageView) findViewById(R.id.ImageView_red_packets);
		foster_petstore = (LinearLayout) findViewById(R.id.foster_petstore);
		imageview_status = (ImageView) findViewById(R.id.imageview_status);
		imageview_to_service = (ImageView) findViewById(R.id.imageview_to_service);
		textview_foster_home_address = (TextView) findViewById(R.id.textview_foster_home_address);
		textview_foster_home_address_title = (TextView) findViewById(R.id.textview_foster_home_address_title);
		textview_foster_order_pay_type = (TextView) findViewById(R.id.textview_foster_order_pay_type);
		//训练增加
		textview_show_pet_type = (TextView) findViewById(R.id.textview_show_pet_type);
		textview_show_train_content = (TextView) findViewById(R.id.textview_show_train_content);
		textview_train_time = (TextView) findViewById(R.id.textview_train_time);
		textview_train_address = (TextView) findViewById(R.id.textview_train_address);
		layout_pet_address = (LinearLayout) findViewById(R.id.layout_pet_address);
		view_line = (View) findViewById(R.id.view_line);
		textview_train_name_price = (TextView) findViewById(R.id.textview_train_name_price);
		layout_middle_address_left = (LinearLayout) findViewById(R.id.layout_middle_address_left);
//		-------------end 2016年3月2日15:26:44

//		layout_foster_food_addOrReduce = (LinearLayout) findViewById(R.id.layout_foster_food_addOrReduce);
//		textview_foster_food_addOrReduce_title = (TextView) findViewById(R.id.textview_foster_food_addOrReduce_title);
//		textview_foster_food_addOrReduce_days = (TextView) findViewById(R.id.textview_foster_food_addOrReduce_days);
//		textview_foster_food_addOrReduce_price = (TextView) findViewById(R.id.textview_foster_food_addOrReduce_price);
//		寄养摄像头start
		layout_foster_shexiangtou = (LinearLayout) findViewById(R.id.layout_foster_shexiangtou);
		textview_foster_price_shexiangtou = (TextView) findViewById(R.id.textview_foster_price_shexiangtou);
		textview_foster_price_shexiangtou_num = (TextView) findViewById(R.id.textview_foster_price_shexiangtou_num);
//		寄养摄像头end
		
		//投诉
		layout_compl_parent = (LinearLayout)findViewById(R.id.layout_compl_parent);
		textview_compl_title = (TextView)findViewById(R.id.textview_compl_title);
		textview_show_compl_reason = (TextView)findViewById(R.id.textview_show_compl_reason);
		textview_show_compl_status = (TextView)findViewById(R.id.textview_show_compl_status);
		
		layout_confirm_eva_complaints = (RelativeLayout)findViewById(R.id.layout_confirm_eva_complaints);
		bt_only_eva = (Button)findViewById(R.id.bt_only_eva);
		bt_complaints = (Button)findViewById(R.id.bt_complaints);
		layout_copy_ordernum = (LinearLayout)findViewById(R.id.layout_copy_ordernum);
		layout_evaandmore = (LinearLayout)findViewById(R.id.layout_evaandmore);

		
		layout_foster_promoterCode_show = (LinearLayout)findViewById(R.id.layout_foster_promoterCode_show);
		textview_foster_promoterCode_dis = (TextView)findViewById(R.id.textview_foster_promoterCode_dis);

	}
	private void goNext(Class clazz,int pre){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("orderid", OrderId);
		startActivityForResult(intent, pre);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

	// TODO Auto-generated method stub
		
		if (pWinChoose != null && pWinChoose.isShowing()) {
			
			pWinChoose.dismiss();
	
			pWinChoose = null;
			return true;
	
		}

	return super.onTouchEvent(event);

	}
	
	//取消订单后台获取原因
	private AsyncHttpResponseHandler getCancleDetail = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->orderCareCancelRemindTxt "+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					String[] showReason = null;
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("txt")&&!objectData.isNull("txt")) {
							JSONArray array = objectData.getJSONArray("txt");
							showReason = new String[array.length()];
							for (int i = 0; i < array.length(); i++) {
								showReason[i] = array.getString(i);
							}
							try {
								cancleAdapter = new CancleAdapter(fosterDetailActivity,showReason,pos);
								listview_cancle_notice.setAdapter(cancleAdapter);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}else {
					if (object.has("msg")&&!object.isNull("msg")) {
						ToastUtil.showToastShortCenter(fosterDetailActivity, object.getString("msg"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	//订单分享红包
	private AsyncHttpResponseHandler getTipTxtAfterComment = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->getAddPetTipAfterOrder "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						String title ="";
						if (object.has("title")&&!object.isNull("title")) {
							title = object.getString("title");
						}
						String txt = "";
						if (object.has("txt")&&!object.isNull("txt")) {
							txt = object.getString("txt");
						}
						String url="";
						if (object.has("url")&&!object.isNull("url")) {
							url = object.getString("url");
						}
						String imgUrl="";
						if (object.has("img")&&!object.isNull("img")) {
							imgUrl = object.getString("img");
						}
						String content="";
						if (object.has("content")&&!object.isNull("content")) {
							content = object.getString("content");
						}
						if (!url.equals("")) {
							ImageView_red_packets.setVisibility(View.VISIBLE);
							showPop(imgUrl,content,title, txt, url);
						}else {
							ImageView_red_packets.setVisibility(View.GONE);
							ToastUtil.showToastShortCenter(fosterDetailActivity, "感谢您的评价！");
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private Calendar calendar;
	private void showPop(final String imgUrl,final String content,String title,String txt,final String urlShow){
		pWinPacket = null;
		if (pWinPacket==null) {
			View view = mInflater.inflate(R.layout.dlg_order_all_style, null);
			ImageView textView_dlg_cancle = (ImageView) view.findViewById(R.id.imageView_dlg_cancle);
			TextView textView_dlg_pop_title = (TextView) view.findViewById(R.id.textView_dlg_pop_title);
			TextView textView_dlg_pop_content = (TextView) view.findViewById(R.id.textView_dlg_pop_content);
			TextView button_dlg_pop_press = (TextView) view.findViewById(R.id.button_dlg_pop_press);
			LinearLayout layout_dlg_all = (LinearLayout) view.findViewById(R.id.layout_dlg_all);
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.layout_low_all);
			pWinPacket = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
//			pWinPacket.setBackgroundDrawable(new BitmapDrawable());
//			pWinPacket.setOutsideTouchable(true);
			pWinPacket.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			pWin.setWidth(dm.widthPixels - 80);
//			pWin.setHeight(dm.heightPixels - dm.heightPixels/2);
			LayoutParams lp = new LayoutParams(dm.widthPixels - 80, 
					Utils.dip2px(OrderFosterDetailActivity.this, 220));
			llMain.setLayoutParams(lp);
//			layout_dlg_all.setBackgroundColor(android.R.color.transparent);
//			layout_dlg_all.setBackgroundColor(getResources().getColor(R.color.a33000000));
			pWinPacket.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//			Utils.setAttribute(fosterDetailActivity, pWinPacket);
			//关闭弹窗
			layout_dlg_all.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (pWinPacket.isShowing()) {
//					}
//					Utils.onDismiss(fosterDetailActivity);
					pWinPacket.dismiss();
					pWinPacket = null;
				}
			});
			textView_dlg_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (pWinPacket.isShowing()) {
//					}
//					Utils.onDismiss(fosterDetailActivity);
					pWinPacket.dismiss();
					pWinPacket = null;
				}
			});
			textView_dlg_pop_title.setText(title);
			textView_dlg_pop_content.setText(txt);
			button_dlg_pop_press.setText("去分享");
			button_dlg_pop_press.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setShareContent(imgUrl, content, urlShow);
					mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN /*,SHARE_MEDIA.SINA,
		                    SHARE_MEDIA.QZONE*/);
					mController.openShare(fosterDetailActivity, false);
//					if (pWinPacket.isShowing()) {
//					}
//					Utils.onDismiss(fosterDetailActivity);
					pWinPacket.dismiss();
					pWinPacket = null;
				}
			});
//			pWinPacket.setOnDismissListener(new OnDismissListener() {
//				
//				@Override
//				public void onDismiss() {
//					// TODO Auto-generated method stub
//					Utils.onDismiss(fosterDetailActivity);
//				}
//			});
		}
	}
	
	
    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent(String url ,String message,String targUrl) {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSinaCallbackUrl("http://www.baidu.com");
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),"1104724367", "gASimi0oEHprSSxe");
//        qZoneSsoHandler.addToSocialSDK();
//        mController.setShareContent(message);
//        mController.setShareImage(new UMImage(getActivity(), url));

        
        
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle(message);
        circleMedia.setShareContent(message);
        circleMedia.setShareImage(new UMImage(fosterDetailActivity, url));
        circleMedia.setTargetUrl(targUrl);
        mController.setShareMedia(circleMedia);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(message);
        weixinContent.setShareContent(message);
        weixinContent.setShareImage(new UMImage(fosterDetailActivity, url));
        weixinContent.setTargetUrl(targUrl);
        mController.setShareMedia(weixinContent);
        


    }
    
	private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();
	}
    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        String appId = "1104724367";
        String appKey = "gASimi0oEHprSSxe";
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(fosterDetailActivity, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx1668e9f200eb89b2";
        String appSecret = "4b1e452b724bc085ac72058dd39adf01";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(fosterDetailActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(fosterDetailActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
    
	private void getData() {
		// TODO Auto-generated method stub
		CommUtil.getTipTxtAfterComment(this,OrderId,getTipTxtAfterComment);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**分享需要使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (resultCode==RESULT_OK) {
			switch (requestCode) {
			case Global.ORDERFOSTERDETAILACTIVITY_TO_EVA:
				Utils.mLogError("==-->评价返回");
				BackCode = Global.ORDERFOSTERDETAILACTIVITY_TO_EVA;
				// 评价返回
				getOrderDetails();
				break;
			case Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE:
				getOrderDetails();
				break;
			}
		}
	}
	
	private void toNext(Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceId);
		intent.putExtra("shopid", shopId);
		intent.putExtra("previous", Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL);
		startActivity(intent);
	}
	private void setTypeName() {
		if (type==1) {
			ordersBean.setTypeName("洗护");
		}else if (type==2) {
			ordersBean.setTypeName("寄养");
		}else if (type==3) {
			ordersBean.setTypeName("游泳");
		}else if (type==4) {
			ordersBean.setTypeName("训练");
		}
	}
	private class MReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int index = intent.getIntExtra("index", 0);
			if (index==1) {
				getOrderDetails();
			}
		}
	
	}
}
