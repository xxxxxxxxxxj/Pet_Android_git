package com.haotang.pet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.entity.VieBeau;
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
import com.haotang.pet.view.CircleImageView;
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
 * 洗护已付款各种状态
 * @author Administrator
 *
 */
@SuppressLint("ResourceAsColor") 
public class OrderDetailFromOrderToConfirmActivity extends SuperActivity implements OnClickListener{
	private ImageButton ibBack;
	private TextView tvTitle;
	private Button bt_titlebar_other;
	private Button btPay;
	private TextView tvServicename;
	private TextView tvPrice;
	private CircleImageView srivBeautician;
	private TextView tvBeauticianName;
	private TextView tvBeauticianOrdernum;
	private TextView tvBeauticianLevel;
	private ImageView ivBeauticianLevel1;
	private ImageView ivBeauticianLevel2;
	private ImageView ivBeauticianLevel3;
	private ImageView ivBeauticianLevel4;
	private ImageView ivBeauticianLevel5;
	private SharedPreferenceUtil spUtil;
	private int OrderId=0;
	
//	-------
	private TextView textView_yuyue_time;
	private TextView tvAddrType;
	private TextView tvAddrTypeDes;
	private TextView textView_people_name;
	private TextView textView_people_phone;
	private TextView textView_address_detail;
	private TextView textView_remark_detail;
	private LayoutInflater mInflater;
	private int PetId;
	private int petService;
	
	private TextView order_status;
	private int state;
	private String statusDescription="";
	
	private RelativeLayout layout_confirm;
	private LinearLayout order_showbase;
	private TextView textView_order_num;
	private TextView textView_order_time;
	
	private TextView textView_over;//需要在适当时间隐藏
	private TextView textView_over_time;//需要在适当时间隐藏
	
	private String petName =  "";
	private String name =   "";
	private double totalPrice =  0;
	
	private RelativeLayout order_detail_show_count;
	private TextView textView_confirm_price_show;
	private TextView textview_old_price_andother;
	private TextView textview_old_price_bottom;
	private TextView textview_price_back;
	
	private TextView textView_pay_other;
	
	
	
//	-------
	boolean statesOrder = true;
	private PopupWindow pWinCancle;
//	------
	private double listPrice = 0;
//	------
//	--start2015年10月26日15:34:07
	private TextView textview_order_show_base;
	private TextView textview_order_show_only;
	private TextView textView_order_GoShopOrGoHome;
//	--end2015年10月26日15:34:13
	public static OrderDetailFromOrderToConfirmActivity orderConfirm;
	private RelativeLayout layout_order_pickup;
	private TextView textView_pickup_price;
	private MProgressDialog pDialog;
	private boolean isShowAll;
	private boolean hasupgrageorder;
	private double upgradefee;
	private double payfee;
	
//	------start 2016年3月2日10:43:27
	private ListView listview_cancle_notice;
//	private CancleAdapter cancleAdapter;
	private PopupWindow pWinChoose;
	private List<Integer> pos = new ArrayList<Integer>();
	
	private String ReasonGit="";
//	------end 2016年3月2日10:43:35
	
	
	/**
	 * 以下几个为多宠物时增加此类标签否则隐藏----能多宠物下单时调试添加
	 */
	private TextView textView_base_petnum;//基础服务宠物个数
	private TextView textView_addservice_petnum;//单项服务宠物个数 
	private TextView textView_upgrade_petnum;//升级服务宠物个数 
	private TextView textView_pickup_petnum;//上门接送宠物个数
	private PopupWindow pWinPacket;
	
	private ImageView ImageView_red_packets;
	private int BackCode = 0;
	private ImageView imageview_status;
	private int serviceId = 0;
	private int shopId = 0;
	private RelativeLayout layout_beautiful_work_to;
	private int beautician_id=0;
	private ImageView imageview_to_service;
	private int serviceloc;
	private String phoneNum = "";
	private LinearLayout layout_addrtype;
	private SelectableRoundedImageView sriv_orderdetail_beautician_sex;
	private ImageView order_beau_level;
	
	
	
	private LinearLayout layout_order_show_base_one;
	private LinearLayout layout_order_show_only_one;
	private LinearLayout layout_order_show_upgradeitems_one;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private String beauPhone="";
	private int type;
	private View petstore_beau_middle_line;
	private String amount ="";
	private  double couponAmount = 0;
	private TextView textView_address;
	private String shopAddress = "";
	private boolean isVie = false;
//	private long lastOverTime = 900000 * 2;
	private long lastOverTime = 0;
	private Timer timer;
	private TimerTask task;
	private int jpushBeauAccpetCode = 0;//推送进来带有这个code弹窗 显示 美容师 后边从推送过来要调下 备注
	private int jpushWaitCode = 0;
	private VieBeau mVieBeau=null;
	private boolean isPopToWait = false;
	private TextView textview_hurryup;
	private boolean isTimeFirst = true;
	private boolean isBeauFirst = true;
	private MReceiver receiver;
	private int payWay=0;
	private String servicePhone;
	private TextView textview_member_show;
	private double lat;
	private double lng;
	private int areaId;
	private StringBuilder serviceIds = null;
	private String avatar ="";
	private String realName = "";
	private String appointment = "";
	private PopupWindow popChangeOrder;
	private RelativeLayout title_order_height;
	private RelativeLayout layout_change_order_parent;
	private LinearLayout layout_cancle_order ;
	private LinearLayout layout_change_order ;
	private LinearLayout layout_people;
	private int modifyEnable=-1;
	private String modifyDisableTip="";
	private String modifyTip="";
	private String levelName ="";
	private String orderTitle ="";
	private String btnTxt ="";
	private int serviceType=-1;
	private String cname;
	private String cphone;
	private ImageView image_change_custome;
	private int tid=-1;
	private RelativeLayout layout_confirm_eva_complaints;
	private RelativeLayout layout_copy_ordernum;
	private Button bt_only_eva;
	private Button bt_complaints;
	private OrdersBean ordersBean = new OrdersBean();
	private LinearLayout layout_compl_parent;
	private TextView textview_compl_title;
	private TextView textview_show_compl_reason;
	private TextView textview_show_compl_status;
	
	private LinearLayout layout_go_home_service_price;
	private TextView textview_go_home_service_tag;
	private TextView textview_go_home_service_price;
	
	private LinearLayout layout_go_home_manjianyouhui;
	private TextView textview_manjianyouhui_left;
	private TextView textview_manjianyouhui_right;
	private String couponCutReductionTag="";
	private String CardTag="";
	private RelativeLayout rl_orderdetail_promoterCode;
	private TextView tv_orderdetail_promoterCode;
	
	private ImageView image_base_show_left;//基础服务是否用次卡支付 用显示  不用隐藏
	private ImageView image_coupon_show_left;//优惠券
	private ImageView image_manjianyouhui_show_left;//满减
	private ImageView image_promoterCode_show_left;//邀请码
	private ImageView image_refund_show_left;//退款
	private RelativeLayout rl_orderdetail_refund;//退款
	private TextView tv_orderdetail_refund;//退款
	private double refund = 0;//退款金额
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Intent intent  = null;
			switch (msg.what) {
			case 0:
				intent  = new Intent();
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
			case 3:
				long lastTimerShowHMS = msg.arg1;
				String time = Utils.formatTimeFM(lastTimerShowHMS);
				String minute = time.substring(0, 2);
				String second = time.substring(3, 5);
				btPay.setText("等待接单"+minute+":"+second);
				isPopToWait = true;
				break;
			case 4:
				btPay.setText("等待美容师接单");
				if (isPopToWait&&jpushWaitCode==1) {
					intent = new Intent(orderConfirm,OrderToWaitActivity.class);
					intent.putExtra("mVieBeau", mVieBeau);
					startActivity(intent);
				}else{
					if (lastOverTime<=0&&jpushWaitCode==1) {
						intent = new Intent(orderConfirm,OrderToWaitActivity.class);
						intent.putExtra("mVieBeau", mVieBeau);
						startActivity(intent);
					}
				}
					
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail_from_order_to_confirm);
		orderConfirm = this;
		pDialog = new MProgressDialog(this);
		OrderId = getIntent().getIntExtra("orderid", 0);
		/**
		 * 等于0 不弹窗 等于1弹窗 
		 * jpushBeauAccpetCode
		 * jpushWaitCode 
		 * 推送带进来的code
		 */
		jpushBeauAccpetCode = getIntent().getIntExtra("jpushBeauAccpetCode", 0);
		jpushWaitCode = getIntent().getIntExtra("jpushWaitCode", 0);
		configPlatforms();
		findVeiw();
		setView();
		initReceiver();
	}


	private void initReceiver() {
		// TODO Auto-generated method stub
		receiver = new MReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}


	private void findVeiw() {
		mInflater = LayoutInflater.from(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		
		tvServicename = (TextView) findViewById(R.id.tv_orderdetail_servicename);
		tvPrice = (TextView) findViewById(R.id.tv_orderdetail_price);
		
		srivBeautician = (CircleImageView) findViewById(R.id.sriv_orderdetail_beautician);
		tvBeauticianName = (TextView) findViewById(R.id.tv_orderdetail_beauticianname);
		tvBeauticianOrdernum = (TextView) findViewById(R.id.tv_orderdetail_beauticianordernum);
		tvBeauticianLevel = (TextView) findViewById(R.id.tv_orderdetail_beauticianlevel);
		ivBeauticianLevel1 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel1);
		ivBeauticianLevel2 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel2);
		ivBeauticianLevel3 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel3);
		ivBeauticianLevel4 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel4);
		ivBeauticianLevel5 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel5);
		
		btPay = (Button) findViewById(R.id.bt_orderdetail_pay);
		layout_confirm = (RelativeLayout) findViewById(R.id.layout_confirm);
		order_showbase = (LinearLayout) findViewById(R.id.order_showbase);
		
		textView_order_num = (TextView) findViewById(R.id.textView_order_num);
		textView_order_time = (TextView) findViewById(R.id.textView_order_time);
		textView_over = (TextView) findViewById(R.id.textView_over);
		textView_over_time = (TextView) findViewById(R.id.textView_over_time);
		textView_confirm_price_show = (TextView) findViewById(R.id.textView_confirm_price_show);
		textview_old_price_andother = (TextView) findViewById(R.id.textview_old_price_andother);
		textview_old_price_bottom = (TextView) findViewById(R.id.textview_old_price_bottom);
		textview_price_back = (TextView) findViewById(R.id.textview_price_back);
		order_detail_show_count = (RelativeLayout) findViewById(R.id.order_detail_show_count);
		textView_pay_other = (TextView) findViewById(R.id.textView_pay_other);
		
		textView_yuyue_time = (TextView) findViewById(R.id.textView_yuyue_time);
		textView_people_name = (TextView) findViewById(R.id.textView_people_name);
		textView_people_phone = (TextView) findViewById(R.id.textView_people_phone);
		textView_address_detail = (TextView) findViewById(R.id.textView_address_detail);
		textView_remark_detail = (TextView) findViewById(R.id.textView_remark_detail);
		order_status = (TextView) findViewById(R.id.order_status);
		
		tvAddrType = (TextView)  findViewById(R.id.textView_addrtype);
		tvAddrTypeDes = (TextView)  findViewById(R.id.textView_addrtype_des);
		srivPetStore = (SelectableRoundedImageView) findViewById(R.id.sriv_petstore_image);
		tvPetStoreName = (TextView) findViewById(R.id.tv_petstore_name);
		tvPetStoreAddr = (TextView) findViewById(R.id.tv_petstore_addr);
		tvPetStorePhone = (TextView) findViewById(R.id.tv_petstore_phone);
		llPetStore = (LinearLayout) findViewById(R.id.ll_petstore);
		
		textview_order_show_base = (TextView) findViewById(R.id.textview_order_show_base);
		textview_order_show_only = (TextView) findViewById(R.id.textview_order_show_only);
		textview_order_upgradeitems = (TextView) findViewById(R.id.textview_order_upgradeitems);
		textView_order_GoShopOrGoHome = (TextView) findViewById(R.id.textView_order_GoShopOrGoHome);
		layout_order_pickup = (RelativeLayout) findViewById(R.id.layout_order_pickup);
		textView_pickup_price = (TextView) findViewById(R.id.textView_pickup_price);
		
		llMain = (LinearLayout) findViewById(R.id.ll_orderdetail_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		
		llHidden = (LinearLayout) findViewById(R.id.ll_orderdetailfromorder_hidden);
		llShowAll = (LinearLayout) findViewById(R.id.ll_orderdetail_showall);
		tvShowAll = (TextView) findViewById(R.id.tv_orderdetail_showall);
		ivShowAll = (ImageView) findViewById(R.id.iv_orderdetail_showall);
		
		tvBaseFee = (TextView) findViewById(R.id.textView_base_price);
		textview_base_show = (TextView) findViewById(R.id.textview_base_show);
		rlAddServiceItems = (RelativeLayout) findViewById(R.id.layout_order_addservice);
		tvAddItemsFee = (TextView) findViewById(R.id.textView_addservice_price);
		rlUpgradeServiceItems = (RelativeLayout) findViewById(R.id.layout_order_upgradeservice);
		tvUpgradeItemsFee = (TextView) findViewById(R.id.textView_upgradeservice_price);
		rlCoupon = (RelativeLayout) findViewById(R.id.rl_orderdetail_coupon);
		tvCouponFee = (TextView) findViewById(R.id.tv_orderdetail_coupon);
		
		
		textView_base_petnum = (TextView) findViewById(R.id.textView_base_petnum);
		textView_addservice_petnum = (TextView) findViewById(R.id.textView_addservice_petnum);
		textView_upgrade_petnum = (TextView) findViewById(R.id.textView_upgrade_petnum);
		textView_pickup_petnum = (TextView) findViewById(R.id.textView_pickup_petnum);
		ImageView_red_packets = (ImageView) findViewById(R.id.ImageView_red_packets);
		imageview_status = (ImageView) findViewById(R.id.imageview_status);
		layout_beautiful_work_to = (RelativeLayout) findViewById(R.id.layout_beautiful_work_to);
		imageview_to_service = (ImageView) findViewById(R.id.imageview_to_service);
		layout_addrtype = (LinearLayout) findViewById(R.id.layout_addrtype);
		sriv_orderdetail_beautician_sex = (SelectableRoundedImageView) findViewById(R.id.sriv_orderdetail_beautician_sex);
		order_beau_level = (ImageView) findViewById(R.id.order_beau_level);
		layout_order_show_base_one = (LinearLayout) findViewById(R.id.layout_order_show_base_one);
		layout_order_show_only_one = (LinearLayout) findViewById(R.id.layout_order_show_only_one);
		layout_order_show_upgradeitems_one = (LinearLayout) findViewById(R.id.layout_order_show_upgradeitems_one);
		layout_order_baseprice = (RelativeLayout) findViewById(R.id.layout_order_baseprice);
		petstore_beau_middle_line = (View) findViewById(R.id.petstore_beau_middle_line);
		textView_address = (TextView) findViewById(R.id.textView_address);
		textview_hurryup = (TextView) findViewById(R.id.textview_hurryup);
		textview_member_show = (TextView) findViewById(R.id.textview_member_show);
		
		layout_change_order_parent = (RelativeLayout) findViewById(R.id.layout_change_order_parent);
		layout_cancle_order = (LinearLayout)findViewById(R.id.layout_cancle_order);
		layout_change_order = (LinearLayout)findViewById(R.id.layout_change_order);
		layout_people = (LinearLayout)findViewById(R.id.layout_people);
		image_change_custome = (ImageView)findViewById(R.id.image_change_custome);
		layout_confirm_eva_complaints = (RelativeLayout)findViewById(R.id.layout_confirm_eva_complaints);
		layout_copy_ordernum = (RelativeLayout)findViewById(R.id.layout_copy_ordernum);
		bt_only_eva = (Button)findViewById(R.id.bt_only_eva);
		bt_complaints = (Button)findViewById(R.id.bt_complaints);
		layout_compl_parent = (LinearLayout)findViewById(R.id.layout_compl_parent);
		textview_compl_title = (TextView)findViewById(R.id.textview_compl_title);
		textview_show_compl_reason = (TextView)findViewById(R.id.textview_show_compl_reason);
		textview_show_compl_status = (TextView)findViewById(R.id.textview_show_compl_status);
		
		layout_go_home_service_price = (LinearLayout)findViewById(R.id.layout_go_home_service_price);
		textview_go_home_service_tag = (TextView)findViewById(R.id.textview_go_home_service_tag);
		textview_go_home_service_price = (TextView)findViewById(R.id.textview_go_home_service_price);

		layout_go_home_manjianyouhui = (LinearLayout)findViewById(R.id.layout_go_home_manjianyouhui);
		textview_manjianyouhui_left = (TextView)findViewById(R.id.textview_manjianyouhui_left);
		textview_manjianyouhui_right = (TextView)findViewById(R.id.textview_manjianyouhui_right);

		
		rl_orderdetail_promoterCode = (RelativeLayout)findViewById(R.id.rl_orderdetail_promoterCode);
		tv_orderdetail_promoterCode = (TextView)findViewById(R.id.tv_orderdetail_promoterCode);
		
		image_base_show_left = (ImageView)findViewById(R.id.image_base_show_left);
		image_coupon_show_left = (ImageView)findViewById(R.id.image_coupon_show_left);
		image_manjianyouhui_show_left = (ImageView)findViewById(R.id.image_manjianyouhui_show_left);
		image_promoterCode_show_left = (ImageView)findViewById(R.id.image_promoterCode_show_left);
		rl_orderdetail_refund = (RelativeLayout)findViewById(R.id.rl_orderdetail_refund);
		image_refund_show_left = (ImageView)findViewById(R.id.image_refund_show_left);
		tv_orderdetail_refund = (TextView)findViewById(R.id.tv_orderdetail_refund);
		
		prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_orderdetail_main);
	}

	private void setView() {
		tvTitle.setText("订单详情");
		bt_titlebar_other.setText("取消订单");
		tvMsg1.setText("网络异常或获取数据出错");
		btRefresh.setVisibility(View.VISIBLE);
		bt_titlebar_other.setVisibility(View.GONE);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		llPetStore.setVisibility(View.GONE);
		
		ibBack.setOnClickListener(this);
		bt_titlebar_other.setOnClickListener(this);
		btPay.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		llShowAll.setOnClickListener(this);
		ImageView_red_packets.setOnClickListener(this);
		llPetStore.setOnClickListener(this);
		layout_beautiful_work_to.setOnClickListener(this);
		imageview_to_service.setOnClickListener(this);
		layout_cancle_order.setOnClickListener(this);
		layout_change_order.setOnClickListener(this);
		layout_people.setOnClickListener(this);
		layout_change_order_parent.setOnClickListener(this);

		layout_change_order_parent.setOnClickListener(this);
		
		bt_only_eva.setOnClickListener(this);
		bt_complaints.setOnClickListener(this);
		layout_copy_ordernum.setOnClickListener(this);
		
		showMain(true);
		
		prsScrollView.setMode(Mode.PULL_FROM_START);
		prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					if (layout_change_order_parent.getVisibility()==View.VISIBLE) {
						layout_change_order_parent.setVisibility(View.GONE);
					}
					getOrderDetails();
				}
			}
		});
		getOrderDetails();
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回
			if (state==4) {
				SharedPreferenceUtil.getInstance(orderConfirm).saveInt("state", state);
			}else{
				SharedPreferenceUtil.getInstance(orderConfirm).saveInt("state", state);
			}
			finishWithAnimation();
			break;
		case R.id.layout_change_order_parent:
//			float CurTrainY = layout_change_order_parent.getTranslationY();
//			ObjectAnimator animator = ObjectAnimator.ofFloat(layout_change_order_parent, "translationY",CurTrainY, -360f,CurTrainY); 
//			animator.setDuration(5000);
//			animator.start(); 
			layout_change_order_parent.setVisibility(View.GONE);
			break;
		case R.id.bt_titlebar_other:

			if (state==2||state==22||state==21||state==23) {//已经付款
				if (type==1) {//洗美改单
					if (layout_change_order_parent.getVisibility() == View.VISIBLE) {
						layout_change_order_parent.setVisibility(View.GONE);
					}else {
						layout_change_order_parent.setVisibility(View.VISIBLE);
					}
				}else {
					UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_CancelOrder);
					layout_change_order_parent.setVisibility(View.GONE);
					//取消订单
					Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
					intentCancle.putExtra("state", state);
					intentCancle.putExtra("type", type);
					intentCancle.putExtra("payWay", payWay);
					intentCancle.putExtra("orderid", OrderId);
					//需要带上各种订单id type 什么
					startActivity(intentCancle);
				}
			}else {
				layout_change_order_parent.setVisibility(View.GONE);
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_CancelOrder);
				//取消订单
				Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
				intentCancle.putExtra("state", state);
				intentCancle.putExtra("type", type);
				intentCancle.putExtra("payWay", payWay);
				intentCancle.putExtra("orderid", OrderId);
				//需要带上各种订单id type 什么
				startActivity(intentCancle);
			}
//			if (type==1) {//洗美改单
//				if (layout_change_order_parent.getVisibility() == View.VISIBLE) {
//					layout_change_order_parent.setVisibility(View.GONE);
//				}else {
//					layout_change_order_parent.setVisibility(View.VISIBLE);
//				}
//			}else { // 其他直接取消订单
//				layout_change_order_parent.setVisibility(View.GONE);
//				//取消订单
//				Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
//				intentCancle.putExtra("state", state);
//				intentCancle.putExtra("type", type);
//				intentCancle.putExtra("payWay", payWay);
//				intentCancle.putExtra("orderid", OrderId);
//				//需要带上各种订单id type 什么
//				startActivity(intentCancle);
//			}
			break;
		case R.id.layout_cancle_order:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_DogCard);
			//取消订单
			Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
			intentCancle.putExtra("state", state);
			intentCancle.putExtra("type", type);
			intentCancle.putExtra("payWay", payWay);
			intentCancle.putExtra("orderid", OrderId);
			//需要带上各种订单id type 什么
			startActivity(intentCancle);
			break;
		case R.id.layout_change_order://改单
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_ModifyOrder);
			if (modifyEnable==0) {//不可以改单
				ToastUtil.showToastShortCenter(orderConfirm, modifyDisableTip);
			}else if (modifyEnable==1) {
				String strp = "";
				//这块调下 特色服务 追加0
				if (serviceType==3) {
					strp = PetId+"_"+serviceId+"_"+0;
				}else {
					if (TextUtils.isEmpty(serviceIds)) {
						strp = PetId+"_"+serviceId+"_"+0;
					}else {
						strp = PetId+"_"+serviceId+"_"+serviceIds;
					}
				}
				Beautician beautician = new Beautician();
				beautician.image = avatar;
				beautician.name = realName;
				beautician.id = beautician_id;
				beautician.appointment = appointment;
				beautician.levelname = levelName;
				beautician.levels=tid;
				Intent intentNext = new Intent(orderConfirm, ChangeOrderActivity.class);
				intentNext.putExtra("lat", lat);
				intentNext.putExtra("lng", lng);
				intentNext.putExtra("areaId", areaId);
				intentNext.putExtra("serviceLoc",serviceloc);
				intentNext.putExtra("shopId",shopId);
				intentNext.putExtra("beautician",beautician);
				intentNext.putExtra("strp",strp);
				intentNext.putExtra("modifyTip",modifyTip);
				intentNext.putExtra("OrderId",OrderId);
				startActivity(intentNext);
			}
			break;
		case R.id.layout_people:
//			ToastUtil.showToastShortCenter(orderConfirm, "更新联系人");
//			ContactActivity
			goNextForData(ContactActivity.class);
			break;
		case R.id.bt_null_refresh:
			//刷新
			getOrderDetails();
			break;
		case R.id.ll_orderdetail_showall:
			//显示全部
			if(!isShowAll){
				//显示全部
				isShowAll = true;
				tvShowAll.setText("点击收起");
				ivShowAll.setBackgroundResource(R.drawable.icon_arrow_up);
				llHidden.setVisibility(View.VISIBLE);

			}else{
				isShowAll = false;
				llHidden.setVisibility(View.GONE);
				tvShowAll.setText("查看全部");
				ivShowAll.setBackgroundResource(R.drawable.icon_arrow_down);
			}
			break;
		case R.id.bt_orderdetail_pay:
			//服务按钮
			if(hasupgrageorder){
//				goUpgradeService(OrderId, UpgradeServiceActivity.class);
				goUpgradeService(OrderId, UpdateOrderConfirmActivity.class);
			}else{
				if (state==3) {//待确认
//					showSelectDialog();
				}else if (state==4) {//待评价
					Intent intent  = new Intent(OrderDetailFromOrderToConfirmActivity.this, EvaluateActivity.class);
					intent.putExtra("orderid", OrderId);
					startActivityForResult(intent, Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW);
				}else if (state==21) {
					CommUtil.getInsertReminderlog(spUtil.getString("cellphone", ""), Global.getIMEI(this), orderConfirm, OrderId, getInsertReminderlog);
					MDialog mDialog = new MDialog.Builder(orderConfirm)
							.setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
							.setMessage("是否拨打电话?").setCancelStr("否")
							.setOKStr("是")
							.positiveListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 确认拨打电话
									Utils.telePhoneBroadcast(orderConfirm,
											beauPhone);
								}
							}).build();
					mDialog.show();
				}
			}
			break;
		case R.id.ImageView_red_packets:
			if (pWinPacket!=null) {
				if (!pWinPacket.isShowing()) {
					getData();
//					showPop();
				}else {
					pWinPacket.dismiss();
					pWinPacket = null;
					Utils.onDismiss(orderConfirm);
				}
			}else {
				getData();
//				showPop();
			}
			break;
		case R.id.ll_petstore:
			toNext(GoShopDetailActivity.class);
			break;
		case R.id.layout_beautiful_work_to:
			toNext(BeauticianDetailActivity.class);
			break;
		case R.id.imageview_to_service:
			// 电话
			if (!phoneNum.equals("")) {
				if (serviceloc == 1) {// 门店服务
					MDialog mDialog = new MDialog.Builder(orderConfirm)
							.setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
							.setMessage("是否拨打电话?").setCancelStr("否")
							.setOKStr("是")
							.positiveListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 确认拨打电话
									Utils.telePhoneBroadcast(orderConfirm,
											phoneNum);
								}
							}).build();
					mDialog.show();
				}
			} else {
				MDialog mDialog = new MDialog.Builder(orderConfirm)
				.setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("是否拨打电话?").setCancelStr("否")
				.setOKStr("是")
				.positiveListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 确认拨打电话
						Utils.telePhoneBroadcast(orderConfirm,servicePhone);
					}
				}).build();
				mDialog.show();
			}
			break;
		case R.id.bt_only_eva://订单已完成 单独显示评价
			Intent intent  = new Intent(OrderDetailFromOrderToConfirmActivity.this, EvaluateActivity.class);
			intent.putExtra("orderid", OrderId);
			
			startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
			break;
		case R.id.bt_complaints://订单已完成 投诉
//			ToastUtil.showToastShortCenter(orderConfirm, "去投诉");
			Intent intentCompl = new Intent(orderConfirm, ComplaintActivity.class);
			intentCompl.putExtra("ordersBean", ordersBean);
			startActivityForResult(intentCompl, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
			break;
		case R.id.layout_copy_ordernum://复制订单号
			ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(textView_order_num.getText());
			ToastUtil.showToastShortCenter(mContext, "复制成功");
			break;
		}
		
	}
	
	private void showMain(boolean flag){
		if(flag){
			llMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		}else{
			llMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}
	private void goUpgradeService(int orderid,Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("orderid", orderid);
		startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
	}
	private void setBottomButtonStatus(int status){
		if (state==2/*||state==22*//*||state==21*/||state==23) {//已经付款  21 已出发 22 开始服务
			order_status.setText(statusDescription);
			layout_confirm.setVisibility(View.VISIBLE);
			if (isVie) {
				if (beautician_id>0) {
					if (TextUtils.isEmpty(btnTxt)) {
						btPay.setText("美容师已接单,坐等服务");
					}else {
						btPay.setText(btnTxt);
					}
				}else {
					if (TextUtils.isEmpty(btnTxt)) {
						btPay.setText("等待美容师接单");
					}else {
						btPay.setText(btnTxt);
					}
				}
			}else {
				if (TextUtils.isEmpty(btnTxt)) {
					btPay.setText("坐等服务");
				}else {
					btPay.setText(btnTxt);
				}
			}
			btPay.setBackgroundColor(Color.parseColor("#bdbdbd"));
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.already_pay);
			bt_titlebar_other.setVisibility(View.VISIBLE);
		}else if (state==21) {
			order_status.setText(statusDescription);
			layout_confirm.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(btnTxt)) {
				btPay.setText("催单");
			}else {
				btPay.setText(btnTxt);
			}
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.already_pay);
			bt_titlebar_other.setVisibility(View.VISIBLE);
		}else if (state==22||state==10) {//开始服务
			order_status.setText(statusDescription);
			layout_confirm.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(btnTxt)) {
				btPay.setText("正在服务");
			}else {
				btPay.setText(btnTxt);
			}
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
			ImageView_red_packets.setVisibility(View.GONE);
			if (state==10) {
				imageview_status.setBackgroundResource(R.drawable.order_update);
			}else {
				imageview_status.setBackgroundResource(R.drawable.already_pay);
			}
			bt_titlebar_other.setVisibility(View.GONE);//开始服务不能取消订单
		}else if (state==6) {//已取消
			order_status.setText(statusDescription);
			layout_confirm.setVisibility(View.GONE);
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			bt_titlebar_other.setVisibility(View.GONE);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.order_already_cancle);
			
			image_change_custome.setVisibility(View.GONE);
			layout_people.setOnClickListener(null);
		}else if (state==5) {//已完成  。。 等于  已关闭
			order_status.setText(statusDescription);
			layout_confirm.setVisibility(View.GONE);
			bt_titlebar_other.setVisibility(View.GONE);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.order_already_over);
			if (BackCode==0) {//初次进入没说是否弹窗预留
				
			}else if(BackCode==Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW){//code
				getData();
//				showPop();
			}
			
			image_change_custome.setVisibility(View.GONE);
			layout_people.setOnClickListener(null);
			layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
			bt_only_eva.setVisibility(View.GONE);
		}else if (state==4) {//待评价
			layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
			layout_confirm.setVisibility(View.GONE);
			order_status.setText(statusDescription);
			bt_titlebar_other.setVisibility(View.GONE);
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			btPay.setText("去评价");
			btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.to_wai_eva);
			
			image_change_custome.setVisibility(View.GONE);
			layout_people.setOnClickListener(null);
		}else if (state==3) {//待确认
			layout_confirm.setVisibility(View.VISIBLE);
			order_status.setText(statusDescription);
			bt_titlebar_other.setVisibility(View.VISIBLE);
			textView_over.setVisibility(View.GONE);
			textView_over_time.setVisibility(View.GONE);
			btPay.setText("确认完成");
			btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
			ImageView_red_packets.setVisibility(View.GONE);
			imageview_status.setBackgroundResource(R.drawable.about_other);
			
			image_change_custome.setVisibility(View.GONE);
			layout_people.setOnClickListener(null);
		}
	}
	private void showStars(int levels, int stars){
		ivBeauticianLevel1.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel2.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel3.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel4.setBackgroundResource(R.drawable.bk_empty);
		ivBeauticianLevel5.setBackgroundResource(R.drawable.bk_empty);
		if(levels == 1){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_1);
				break;
				
			}
		}else if(levels == 2){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_2);
				break;
				
			}
		}else if(levels == 3){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_3);
				break;
				
			}
		}else if(levels == 4){
			switch (stars) {
			case 1:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 2:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 3:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 4:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
				break;
			case 5:
				ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
				ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_4);
				break;
				
			}
		}
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
				prsScrollView.onRefreshComplete();
				pDialog.closeDialog();
				layout_change_order_parent.setVisibility(View.GONE);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			showMain(true);
			Utils.mLogError("订单详情："+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				hasupgrageorder = false;
				if (code==0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
					JSONObject object = jsonObject.getJSONObject("data");
					if (object.has("type")&&!object.isNull("type")) {
						type = object.getInt("type");
						ordersBean.setType(type);//投诉订单对象
					}
					setTypeName();//投诉用
					if (object.has("servicePhone")&&!object.isNull("servicePhone")) {
						servicePhone = object.getString("servicePhone");
					}
					if (object.has("modifyEnable")&&!object.isNull("modifyEnable")) {
						modifyEnable = object.getInt("modifyEnable");
					}
					if (object.has("modifyDisableTip")&&!object.isNull("modifyDisableTip")) {
						modifyDisableTip = object.getString("modifyDisableTip");
					}
					if (object.has("modifyTip")&&!object.isNull("modifyTip")) {
						modifyTip = object.getString("modifyTip");
					}
					if (object.has("orderTitle")&&!object.isNull("orderTitle")) {
						orderTitle = object.getString("orderTitle");
						tvTitle.setText(orderTitle);
					}
					if (object.has("appointment")&&!object.isNull("appointment")) {
						appointment = object.getString("appointment");
						ordersBean.setAppointment(appointment);
						if (type==3) {
							String time [] = appointment.split(" ");
							String AmOrPm="";
							if (time[1].contains("10")) {
								AmOrPm="上午";
							}else if (time[1].contains("14")) {
								AmOrPm="下午";
							}
							textView_yuyue_time.setText(time[0]+" "+AmOrPm);
							
							image_change_custome.setVisibility(View.GONE);
							layout_people.setOnClickListener(null);
						}else {
							textView_yuyue_time.setText(appointment);
						}
						String[] strtime = appointment.split(" ")[0].split("-");
						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH);
						int day = calendar.get(Calendar.DAY_OF_MONTH);
						int length = strtime.length;
					}
					if (object.has("areaId")&&!object.isNull("areaId")) {
						areaId = object.getInt("areaId");
					}
					if (object.has("lat")&&!object.isNull("lat")) {
						lat = object.getDouble("lat");
					}
					if (object.has("lng")&&!object.isNull("lng")) {
						lng = object.getDouble("lng");
					}
					if (object.has("payWay")&&!object.isNull("payWay")) {
						payWay = object.getInt("payWay");
					}
					if (object.has("pet")&&!object.isNull("pet")) {
						JSONObject  object2 = object.getJSONObject("pet");
						
						if (object2.has("avatarPath")&&!object2.isNull("avatarPath")) {
							String avatarPath = object2.getString("avatarPath");
							ordersBean.setAvatar(avatarPath);
						}
						if (object2.has("petName")&&!object2.isNull("petName")) {
							petName = object2.getString("petName");
						}
						if (object2.has("PetId")&&!object2.isNull("PetId")) {
							PetId = object2.getInt("PetId");
						}
					}
					if (object.has("customerName")&&!object.isNull("customerName")) {
						textView_people_name.setText(object.getString("customerName").trim());
						textView_people_name.setVisibility(View.VISIBLE);
						cname = object.getString("customerName").trim();
					}else{
						textView_people_name.setVisibility(View.GONE);
					}
					if (object.has("customerMobile")&&!object.isNull("customerMobile")) {
						textView_people_phone.setText(object.getString("customerMobile").trim());
						cphone = object.getString("customerMobile").trim();
					}
					
					if (object.has("created")&&!object.isNull("created")) {
						String created = object.getString("created");
//						textView_order_time.setText(Utils.getDataString(created));
						textView_order_time.setText(created);
					}
//					if (object.has("endTime")&&!object.isNull("endTime")) {
//						String endTime = object.getString("endTime");
//						textView_over_time.setText(endTime);
//					}
					if (object.has("OrderId")&&!object.isNull("OrderId")) {
						int oId = object.getInt("OrderId");
						ordersBean.setId(oId);
					}
					if (object.has("statusDescription")&&!object.isNull("statusDescription")) {
						statusDescription = object.getString("statusDescription");
					}
					if (object.has("orderSource")&&!object.isNull("orderSource")) {
//						String orderSource = object.getString("orderSource");
						String orderSource = object.getString("orderSource");
						if (orderSource.equals("vie")) {
							textview_hurryup.setVisibility(View.VISIBLE);
							isVie = true;
							textview_hurryup.setText("即时预约");
							textview_hurryup.setBackgroundDrawable(Utils.getDW("FE4D3D"));
						}
					}else {
						isVie  = false;
						textview_hurryup.setVisibility(View.GONE);
					}
					getWorker(object);
					if (object.has("btnTxt")&&!object.isNull("btnTxt")) {
						btnTxt = object.getString("btnTxt");
					}
					if (object.has("status")&&!object.isNull("status")) {
						state = object.getInt("status");
						setBottomButtonStatus(state);
					}
					if (state==2||state==22||state==21||state==23) {//已经付款
						if (type==1) {
							bt_titlebar_other.setText("修改订单");
						}
					}else {
						bt_titlebar_other.setText("取消订单");
					}
					if (object.has("orderNum")&&!object.isNull("orderNum")) {
						String orderNum = object.getString("orderNum");
						textView_order_num.setText(orderNum);
						ordersBean.setOrderNum(orderNum);
					}
					if (object.has("payDesc")&&!object.isNull("payDesc")) {
						String payDesc = object.getString("payDesc");
						textView_pay_other.setText(payDesc);
					}
					
					if (object.has("basicPrice")&&!object.isNull("basicPrice")) {
						if (object.has("timeCardTag")&&!object.isNull("timeCardTag")) {
							JSONObject objectTimeCardTag  = object.getJSONObject("timeCardTag");
							if (objectTimeCardTag.has("title")&&!objectTimeCardTag.isNull("title")) {
								textview_base_show.setText(objectTimeCardTag.getString("title"));
							}
							if (objectTimeCardTag.has("tip")&&!objectTimeCardTag.isNull("tip")) {
								tvBaseFee.setText(objectTimeCardTag.getString("tip").trim());
							}
							image_base_show_left.setVisibility(View.VISIBLE);
						}else {
							tvBaseFee.setText("¥ "+object.getString("basicPrice"));
							image_base_show_left.setVisibility(View.GONE);
						}
						basePrice = object.getInt("basicPrice");
					}
					
					if (object.has("listAddress")&&!object.isNull("listAddress")) {
						try {
							JSONArray array = object.getJSONArray("listAddress");
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2 = array.getJSONObject(0);
								String address = jsonObject2.getString("address");
								textView_address_detail.setText(address);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							//游泳remove address
						}
					}
					
					if (object.has("listPrice")&&!object.isNull("listPrice")) {
						listPrice = object.getDouble("listPrice");
					}
					if (object.has("remark")&&!object.isNull("remark")) {
						String remark = object.getString("remark");
						if("".equals(remark.trim())){
							textView_remark_detail.setText("无");
						}else{
							textView_remark_detail.setText(remark);
						}
					}else {
						textView_remark_detail.setText("无");
					}
//					----
					if (object.has("coupon")&&!object.isNull("coupon")) {
						JSONObject couponObject = object.getJSONObject("coupon");
						String nameCoupon ="";
						rlCoupon.setVisibility(View.VISIBLE);
						image_coupon_show_left.setVisibility(View.VISIBLE);
						if (couponObject.has("amount")&&!couponObject.isNull("amount")) {
							 amount =  couponObject.getString("amount");
							 couponAmount = Double.parseDouble(amount);
							 tvCouponFee.setText("-¥"+Math.abs(Double.parseDouble(amount)));
						}
						if (couponObject.has("name")&&!couponObject.isNull("name")) {
							 nameCoupon = couponObject.getString("name");
							 tvCouponFee.setText("-¥"+Math.abs(Double.parseDouble(amount))+"("+nameCoupon+")");
						}
					}else{
						rlCoupon.setVisibility(View.GONE);
						image_coupon_show_left.setVisibility(View.GONE);
					}
					if (object.has("extraServiceItems")&&!object.isNull("extraServiceItems")) {
						JSONArray extraArray = object.getJSONArray("extraServiceItems");
						StringBuilder sb = new StringBuilder();
						
						for (int i = 0; i < extraArray.length(); i++) {
							JSONObject extraObject = extraArray.getJSONObject(i);
							if (extraObject.has("name")&&!extraObject.isNull("name")) {
								if (i==extraArray.length()-1) {
									sb.append(extraObject.getString("name"));
								}else {
									sb.append(extraObject.getString("name")+"+");
								}
							}
							serviceIds = new StringBuilder();
							if (extraObject.has("ExtraServiceItemId")&&!extraObject.isNull("ExtraServiceItemId")) {
								if (i==extraArray.length()-1) {
									serviceIds.append(extraObject.getString("ExtraServiceItemId"));
								}else {
									serviceIds.append(extraObject.getString("ExtraServiceItemId")+"_");
								}
							}
							
						}
						layout_order_show_only_one.setVisibility(View.VISIBLE);
						textview_order_show_only.setVisibility(View.VISIBLE);
//						SpannableString spans = new SpannableString("单项服务  "+sb.toString().subSequence(0, sb.toString().length()));
//						spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 
//								0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						textview_order_show_only.setText(sb.substring(0, sb.length()));
						
						if (object.has("extraItemPrice")&&!object.isNull("extraItemPrice")) {
							rlAddServiceItems.setVisibility(View.VISIBLE);
							tvAddItemsFee.setText("¥ "+object.getString("extraItemPrice"));
						}
					}else {
						layout_order_show_only_one.setVisibility(View.GONE);
						rlAddServiceItems.setVisibility(View.GONE);
					}
					
					if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
						JSONObject petServicePojo = object.getJSONObject("petServicePojo");
						if (petServicePojo.has("name")&&!petServicePojo.isNull("name")) {
							name = petServicePojo.getString("name");
						}
						if (petServicePojo.has("serviceType")&&!petServicePojo.isNull("serviceType")) {
							serviceType = petServicePojo.getInt("serviceType");
						}
						if (petServicePojo.has("PetServicePojoId")&&!petServicePojo.isNull("PetServicePojoId")) {
							petService = petServicePojo.getInt("PetServicePojoId");
						}
						if (petServicePojo.has("serviceItem")&&!petServicePojo.isNull("serviceItem")) {
							JSONArray jsonArray  =petServicePojo.getJSONArray("serviceItem");
							Map<String,Object> map = new HashMap<String,Object>();
							StringBuilder sb = new StringBuilder();
							for (int j = 0; j < jsonArray.length(); j++) {
								JSONObject serviceItem = jsonArray.getJSONObject(j);
								if (serviceItem.has("itemName")&&!serviceItem.isNull("itemName")) {
									map.put("itemName", serviceItem.getString("itemName"));
									if (j==jsonArray.length()-1) {
										sb.append(serviceItem.getString("itemName"));
									}else {
										sb.append(serviceItem.getString("itemName")+"+");
									}
								}
							}
//							SpannableString spans = new SpannableString("基础服务  "+sb.toString().subSequence(0, sb.toString().length()));
//							spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 
//									0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							textview_order_show_base.setText(sb.substring(0, sb.length()));
							
							
							
						}
					}
//					------
					if (object.has("totalPrice")&&!object.isNull("totalPrice")) {
						 totalPrice = object.getDouble("totalPrice");
						 tvPrice.setText("¥ "+Utils.formatDouble(totalPrice));
						 ordersBean.setPay_price((int)totalPrice);
					}
					
//					----
					
					if (object.has("serviceLoc")&&!object.isNull("serviceLoc")) {
						serviceloc = object.getInt("serviceLoc");
						ordersBean.setServiceLoc(serviceloc);
						if(serviceloc==1){
							tvAddrType.setText("门店服务");
							textView_order_GoShopOrGoHome.setText("到店");
							textView_order_GoShopOrGoHome.setBackgroundDrawable(Utils.getDW("FAA04A"));
							tvAddrTypeDes.setVisibility(View.VISIBLE);
							double pickUpPrice = 0;
							if (object.has("pickupPrice")&&!object.isNull("pickupPrice")) {
								pickUpPrice = object.getDouble("pickupPrice");
								Utils.mLogError("==-->pickupPrice := "+pickUpPrice+ " " +object.getDouble("pickupPrice"));
							}
							layout_order_pickup.setVisibility(View.VISIBLE);
							if(object.has("pickUp")&&!object.isNull("pickUp")){
								int pickup = object.getInt("pickUp");
								if(pickup==1){
//									layout_addrtype.setVisibility(View.VISIBLE);
									tvAddrTypeDes.setText("(需要接送)");
									if (pickUpPrice==0) {
										textView_pickup_price.setText("免费");
									}else {
										textView_pickup_price.setText("¥ "+pickUpPrice);
									}
								}else if(pickup==2){//不再接送范围
//									layout_addrtype.setVisibility(View.GONE);
									tvAddrTypeDes.setText("(不接送)");
									layout_order_pickup.setVisibility(View.GONE);
								}else if (pickup==0) {//不需要接送
									layout_order_pickup.setVisibility(View.GONE);
//									layout_addrtype.setVisibility(View.VISIBLE);
									tvAddrTypeDes.setText("(不接送)");
								}
							}else{
								layout_addrtype.setVisibility(View.GONE);
								layout_order_pickup.setVisibility(View.GONE);
								tvAddrTypeDes.setText("(不接送)");
							}
							
							llPetStore.setVisibility(View.VISIBLE);
							
						}else{
							layout_order_pickup.setVisibility(View.GONE);
							llPetStore.setVisibility(View.GONE);
							tvAddrTypeDes.setVisibility(View.GONE);
							tvAddrType.setText("上门服务");
							textView_order_GoShopOrGoHome.setText("上门");
							textView_order_GoShopOrGoHome.setBackgroundDrawable(Utils.getDW("2FC0F0"));
						}
						
					}
					if (object.has("orderSource")&&!object.isNull("orderSource")) {
						String orderSource = object.getString("orderSource");
						if (serviceloc==1) {//到店
							if (orderSource.equals("vie")) {
								llPetStore.setVisibility(View.VISIBLE);
								layout_beautiful_work_to.setVisibility(View.GONE);//beau
							}
//							else if(orderSource.equals("appointment")) {
//								llPetStore.setVisibility(View.VISIBLE);
//								layout_beautiful_work_to.setVisibility(View.VISIBLE);//beau
//							}
						}else if(serviceloc==2){//上门
							if (orderSource.equals("vie")) {
								llPetStore.setVisibility(View.GONE);
								layout_beautiful_work_to.setVisibility(View.GONE);//beau
							}
//							else if(orderSource.equals("appointment")) {
//								llPetStore.setVisibility(View.VISIBLE);
//								layout_beautiful_work_to.setVisibility(View.VISIBLE);//beau
//							}
						}
						if (orderSource.equals("vie")) {
							if (serviceloc==1) {//到店
								layout_beautiful_work_to.setVisibility(View.GONE);//beau
								llPetStore.setVisibility(View.VISIBLE);
							}else {
								layout_beautiful_work_to.setVisibility(View.GONE);//beau
								llPetStore.setVisibility(View.GONE);
							}
						}
					}
					if (object.has("shop")&&!object.isNull("shop")) {
						JSONObject jshop = object.getJSONObject("shop");
						if(jshop.has("shopName")&&!jshop.isNull("shopName")){
							tvPetStoreName.setText(jshop.getString("shopName"));
						}
						if(jshop.has("address")&&!jshop.isNull("address")){
							shopAddress = jshop.getString("address");
							tvPetStoreAddr.setText(jshop.getString("address"));
						}
						if(jshop.has("phone")&&!jshop.isNull("phone")){
							phoneNum = jshop.getString("phone");
							tvPetStorePhone.setText(jshop.getString("phone"));
						}
						if (jshop.has("ShopId")&&!jshop.isNull("ShopId")) {
							shopId = jshop.getInt("ShopId");
						}
						if(jshop.has("img")&&!jshop.isNull("img")){
							ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+jshop.getString("img"),
									srivPetStore,0, new ImageLoadingListener() {
								
								@Override
								public void onLoadingStarted(String arg0, View view) {
									// TODO Auto-generated method stub
//										((ImageView)view).setImageResource(R.drawable.dog_icon_unnew);
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
					if (object.has("petService")&&!object.isNull("petService")) {
						int petServiceNum = object.getInt("petService");
						if (petServiceNum==300||type==3) {
							textView_address.setText("门店地址：");
							textView_address_detail.setText(shopAddress);
//							petstore_beau_middle_line.setVisibility(View.GONE);
							layout_beautiful_work_to.setVisibility(View.GONE);
							layout_order_baseprice.setVisibility(View.GONE);
//							order_detail_show_count.setVisibility(View.GONE);
//							layout_confirm.setVisibility(View.GONE);
//							order_showbase.setVisibility(View.GONE);
							if (amount.equals("")) {
//								rlCoupon.setBackgroundColor(getResources().getColor(R.color.white));
								order_showbase.setBackgroundColor(getResources().getColor(R.color.white));
//								layout_confirm.setVisibility(View.GONE);
							}else {
								order_showbase.setBackgroundColor(getResources().getColor(R.color.white));
								
							}
							
							if (state==2||state==22||state==21||state==23) {
								layout_confirm.setVisibility(View.GONE);
							}
						}
					}
					String nickName="";
					if (object.has("myPet")&&!object.isNull("myPet")) {
						JSONObject objectMyPet = object.getJSONObject("myPet");
						if (objectMyPet.has("nickName")&&!objectMyPet.isNull("nickName")) {
							nickName = objectMyPet.getString("nickName");
						}
					}
					if (nickName.equals("")) {
						tvServicename.setText(petName.trim()+name.trim());
					}else {
						tvServicename.setText(nickName.trim()+name.trim());
					}
					
					if (type==3) {
						if (nickName.equals("")) {
							tvServicename.setText(petName.trim()+"游泳");
						}else {
							tvServicename.setText(nickName.trim()+"游泳");
						}
						layout_order_show_base_one.setVisibility(View.GONE);
						if (object.has("memberSwimNotice")&&!object.isNull("memberSwimNotice")) {
							textview_member_show.setVisibility(View.VISIBLE);
							textview_member_show.setText(object.getString("memberSwimNotice"));
						}else {
							textview_member_show.setVisibility(View.GONE);
						}
					}
					ordersBean.setService(tvServicename.getText().toString());
					if (object.has("task")&&!object.isNull("task")) {
						JSONObject objectTask = object.getJSONObject("task");
						//完成时间
						if (objectTask.has("actualEndTime")&&!objectTask.isNull("actualEndTime")) {
							textView_over_time.setText(objectTask.getString("actualEndTime"));
						}
						if (objectTask.has("serviceId")&&!objectTask.isNull("serviceId")) {
							serviceId = objectTask.getInt("serviceId");
						}
						if (objectTask.has("shopId")&&!objectTask.isNull("shopId")) {
							shopId = objectTask.getInt("shopId");
						}
					}
					if (type==3) {
						if (object.has("actualEndTime")&&!object.isNull("actualEndTime")) {
							textView_over_time.setText(object.getString("actualEndTime").trim());
						}
					}
					
					
					double extraServicePrice = 0 ;
					double reductionPrice = 0;
					if (object.has("extraServiceFeeTag")&&!object.isNull("extraServiceFeeTag")) {
						String extraServiceFeeTag = object.getString("extraServiceFeeTag");
						textview_go_home_service_tag.setText(extraServiceFeeTag);
					}
					if (object.has("extraServicePrice")&&!object.isNull("extraServicePrice")) {
						layout_go_home_service_price.setVisibility(View.VISIBLE);
						extraServicePrice = object.getDouble("extraServicePrice");
						textview_go_home_service_price.setText("¥"+Utils.formatDouble(extraServicePrice, 2));
					}else {
						layout_go_home_service_price.setVisibility(View.GONE);
					}
					if (extraServicePrice<=0) {
						extraServicePrice = 0;
						layout_go_home_service_price.setVisibility(View.GONE);
					}
					
					if (object.has("extraReductionFeeTag")&&!object.isNull("extraReductionFeeTag")) {
						String reductionPriceTag = object.getString("extraReductionFeeTag");
						textview_manjianyouhui_left.setText(reductionPriceTag);
					}
					if (object.has("reductionPrice")&&!object.isNull("reductionPrice")) {
						layout_go_home_manjianyouhui.setVisibility(View.VISIBLE);
						image_manjianyouhui_show_left.setVisibility(View.VISIBLE);
						reductionPrice = object.getDouble("reductionPrice");
						textview_manjianyouhui_right.setText("-¥"+Utils.formatDouble(reductionPrice, 2)+"");
					}else {
						layout_go_home_manjianyouhui.setVisibility(View.GONE);
						image_manjianyouhui_show_left.setVisibility(View.GONE);
					}
					if (reductionPrice<=0) {
						reductionPrice = 0;
						layout_go_home_manjianyouhui.setVisibility(View.GONE);
						image_manjianyouhui_show_left.setVisibility(View.GONE);
					}
					
					if (object.has("reductionTag")&&!object.isNull("reductionTag")) {
						JSONObject objectReductionTag = object.getJSONObject("reductionTag");
						if (objectReductionTag.has("reductionTag")&&!objectReductionTag.isNull("reductionTag")) {
							couponCutReductionTag = objectReductionTag.getString("reductionTag");
						}
						if (objectReductionTag.has("cardTag")&&!objectReductionTag.isNull("cardTag")) {
							CardTag=objectReductionTag.getString("cardTag");
						}
					}
					if (object.has("refund")&&!object.isNull("refund")) {
						refund = object.getDouble("refund");
						if (refund>0) {
							rl_orderdetail_refund.setVisibility(View.VISIBLE);
							image_refund_show_left.setVisibility(View.VISIBLE);
							tv_orderdetail_refund.setText("¥"+refund);
						}else {
							rl_orderdetail_refund.setVisibility(View.GONE);
							image_refund_show_left.setVisibility(View.GONE);
						}
					}else {
						rl_orderdetail_refund.setVisibility(View.GONE);
					}
					double cutDownMoney = couponAmount+reductionPrice; // 
					//这里不考虑升级订单
					NoUpdateOrder(object, extraServicePrice, cutDownMoney);
					
					if (object.has("updateOrder")&&!object.isNull("updateOrder")) {
						JSONObject jupdateorder = object.getJSONObject("updateOrder");
//						if(jupdateorder.has("OrderId")&&!jupdateorder.isNull("OrderId")){
//							upgradeorderid = jupdateorder.getInt("OrderId");
//						}
						double UpTotalPrice = 0;
						if (jupdateorder.has("totalPrice")&&!jupdateorder.isNull("totalPrice")) {
							UpTotalPrice = jupdateorder.getDouble("totalPrice");
						}
						if (object.has("payPrice")&&!object.isNull("payPrice")) {
							payfee = object.getDouble("payPrice");
							textView_confirm_price_show.setText("合计 ¥ "+(Utils.formatDouble(payfee)/*+extraServicePrice-reductionPrice*/));
						}
						if(jupdateorder.has("status")&&!jupdateorder.isNull("status")){
							int updatestatus = jupdateorder.getInt("status");
							textview_order_upgradeitems.setVisibility(View.GONE);
							layout_order_show_upgradeitems_one.setVisibility(View.GONE);
							rlUpgradeServiceItems.setVisibility(View.GONE);
							if(updatestatus==1){
								hasupgrageorder = true;
								btPay.setText("订单升级-去付款");
								btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
							}else if(updatestatus==2||updatestatus==3){
								if(jupdateorder.has("extraItemPrice")&&!jupdateorder.isNull("extraItemPrice")){
									rlUpgradeServiceItems.setVisibility(View.VISIBLE);
									upgradefee = jupdateorder.getDouble("extraItemPrice");
									tvUpgradeItemsFee.setText("¥ "+Utils.formatDouble(upgradefee));
								}else{
									rlUpgradeServiceItems.setVisibility(View.GONE);
								}
								if(jupdateorder.has("extraServiceItems")&&!jupdateorder.isNull("extraServiceItems")){
									JSONArray jupdatearr = jupdateorder.getJSONArray("extraServiceItems");
									StringBuffer sb = new StringBuffer();
									for(int i=0;i<jupdatearr.length();i++){
										JSONObject sitem = jupdatearr.getJSONObject(i);
										if(sitem.has("name")&&!sitem.isNull("name")){
											sb.append(sitem.getString("name")+"+");
										}
										
									}
									if(jupdatearr.length()>0){
//										SpannableString spans = new SpannableString("升级服务  "+sb.toString().subSequence(0, sb.toString().length()-1));
//										spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 
//												0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
										textview_order_upgradeitems.setVisibility(View.VISIBLE);
										layout_order_show_upgradeitems_one.setVisibility(View.VISIBLE);
										textview_order_upgradeitems.setText(sb.substring(0, sb.length()-1));
										if (object.has("payPrice")&&!object.isNull("payPrice")) {
											payfee = object.getDouble("payPrice");
											textView_confirm_price_show.setText("合计 ¥ "+(Utils.formatDouble(payfee+upgradefee)/*+extraServicePrice-reductionPrice*/));
										}
									}
								}
								//升级订单付完款后这里的总价需要加上update里边的totalprice
								UpdateStatusEqu2_3(object, extraServicePrice,cutDownMoney, UpTotalPrice);
							}
						}
						
					}else{
						textview_order_upgradeitems.setVisibility(View.GONE);
						layout_order_show_upgradeitems_one.setVisibility(View.GONE);
						rlUpgradeServiceItems.setVisibility(View.GONE);
						if (object.has("payPrice")&&!object.isNull("payPrice")) {
							payfee = object.getDouble("payPrice");
							textView_confirm_price_show.setText("合计 ¥ "+(Utils.formatDouble(payfee)/*+extraServicePrice-reductionPrice*/));
						}
						if (object.has("refundTag")&&!object.isNull("refundTag")) {//实付款底下的不再显示挪到左边底下显示
							textview_price_back.setVisibility(View.GONE);
							String refundTag = object.getString("refundTag");
							textview_price_back.setText(refundTag);
						}else {
							textview_price_back.setVisibility(View.GONE);
						}
						
						
//						double cutDownMoney = couponAmount+reductionPrice; // 
						//没有升级订单
						NoUpdateOrder(object, extraServicePrice, cutDownMoney);
					}
					
					if (object.has("payPrice")&&!object.isNull("payPrice")) {
						textView_confirm_price_show.setText(setLastShowStyle("合计 ¥"+object.getDouble("payPrice")));
					}
					if (object.has("remainTime")&&!object.isNull("remainTime")) {
						lastOverTime = object.getLong("remainTime");
//						lastOverTime = 6000;
					}
					mVieBeau = new VieBeau();
					if (object.has("tipDialog")&&!object.isNull("tipDialog")) {
						JSONObject objectDialog = object.getJSONObject("tipDialog");
						if (objectDialog.has("realName")&&!objectDialog.isNull("realName")) {
							mVieBeau.realName = objectDialog.getString("realName");
						}
						if (objectDialog.has("orderAmount")&&!objectDialog.isNull("orderAmount")) {
							mVieBeau.totalOrderAmount = objectDialog.getInt("orderAmount");
						}
						if (objectDialog.has("level")&&!objectDialog.isNull("level")) {
							mVieBeau.levelName = objectDialog.getString("level");
						}
						if (objectDialog.has("avatar")&&!objectDialog.isNull("avatar")) {
							mVieBeau.avatar=CommUtil.getServiceNobacklash()+objectDialog.getString("avatar");
						}
						if (objectDialog.has("title")&&!objectDialog.isNull("title")) {
							mVieBeau.title = objectDialog.getString("title");
						}
					}
					if (state==2&&beautician_id==0&&isVie) {
//						btPay.setText("等待接单");
						btPay.setBackgroundResource(R.drawable.bg_button_service_apponit_ok);
						if (isTimeFirst) {
							SecondTimeDown();
						}
					}
					if (beautician_id!=0&&isVie) {
						layout_beautiful_work_to.setVisibility(View.VISIBLE);
						if (jpushBeauAccpetCode==1) {
							if (isBeauFirst) {
								isBeauFirst = false;
								Intent intent = new Intent(orderConfirm, OrderAcceptBeauVieActivity.class);
								intent.putExtra("mVieBeau", mVieBeau);
								startActivity(intent);
							}
						}
					}
					if (object.has("feedback")&&!object.isNull("feedback")) {
						if (state==5) {
							layout_confirm_eva_complaints.setVisibility(View.GONE);
						}
						layout_compl_parent.setVisibility(View.VISIBLE);
						bt_complaints.setVisibility(View.GONE);
						JSONObject feedback = object.getJSONObject("feedback");
//						if (feedback.has("content")&&!feedback.isNull("content")) {
							textview_compl_title.setText("投诉内容");
//						}
//						if (feedback.has("reason")&&!feedback.isNull("reason")) {
//							String reason = feedback.getString("reason");
//							String [] reasons = reason.split(",");
//							StringBuilder reasonBuilder = new StringBuilder();
//							for (int i = 0; i < reasons.length; i++) {
//								if (i==reasons.length-1) {
//									reasonBuilder.append(reasons[i]);
//									if (feedback.has("content")&&!object.isNull("content")) {
//										reasonBuilder.append("\n\n"+feedback.getString("content"));
//									}
//								}else {
//									reasonBuilder.append(reasons[i]+"\n\n");
//								}
//							}
//							textview_show_compl_reason.setText(reasonBuilder.toString());
//						}
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
					}else {
						layout_compl_parent.setVisibility(View.GONE);
					}
					
					if (bt_complaints.getVisibility()==View.GONE&&bt_only_eva.getVisibility()==View.GONE) {
						layout_confirm_eva_complaints.setVisibility(View.GONE);
					}
					
					
					//统一增加邀请码优惠
					if (object.has("orderFee")&&!object.isNull("orderFee")) {
						double orderFee = object.getDouble("orderFee");
						if (orderFee>0) {
							rl_orderdetail_promoterCode.setVisibility(View.VISIBLE);
							tv_orderdetail_promoterCode.setText("-¥"+orderFee);
							image_promoterCode_show_left.setVisibility(View.VISIBLE);
						}else {
							rl_orderdetail_promoterCode.setVisibility(View.GONE);
							image_promoterCode_show_left.setVisibility(View.GONE);
						}
					}else {
						rl_orderdetail_promoterCode.setVisibility(View.GONE);
						image_promoterCode_show_left.setVisibility(View.GONE);
					}
//					if (object.has("promoterCode")&&!object.isNull("promoterCode")) {
//						if (object.has("orderFee")&&!object.isNull("orderFee")) {
//							double orderFee = object.getDouble("orderFee");
//							if (orderFee>0) {
//								rl_orderdetail_promoterCode.setVisibility(View.VISIBLE);
//								tv_orderdetail_promoterCode.setText("-¥"+orderFee);
//							}else {
//								rl_orderdetail_promoterCode.setVisibility(View.GONE);
//							}
//						}else {
//							rl_orderdetail_promoterCode.setVisibility(View.GONE);
//						}
//					}else {
//						rl_orderdetail_promoterCode.setVisibility(View.GONE);
//					}
				}else{
					if(jsonObject.has("msg")&&!jsonObject.isNull("msg")){
						ToastUtil.showToastShort(OrderDetailFromOrderToConfirmActivity.this,jsonObject.getString("msg"));
					}
					showMain(false);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showMain(false);
			}
		}

		private void NoUpdateOrder(JSONObject object, double extraServicePrice,
				double cutDownMoney) {
			if (cutDownMoney<=0) {//优惠券+满减没哟优惠
				textview_old_price_andother.setText("总价¥"+(Utils.formatDouble(totalPrice+extraServicePrice, 2)));
				if (object.has("cardId")&&!object.isNull("cardId")) {
					int cardId = 0;
					try {
						cardId = object.getInt("cardId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					textview_old_price_bottom.setVisibility(View.VISIBLE);
					textview_old_price_bottom.setText(CardTag+"¥"+basePrice);
					if (refund>0) {
						textview_old_price_bottom.setVisibility(View.VISIBLE);
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"退款¥"+refund);
						}else {
							textview_old_price_bottom.setText("退款¥"+refund);
						}
					}else {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice);
						}else {
							textview_old_price_bottom.setVisibility(View.GONE);
						}
					}
				}else {
					textview_old_price_bottom.setVisibility(View.GONE);
					if (refund>0) {
						textview_old_price_bottom.setVisibility(View.VISIBLE);
						textview_old_price_bottom.setText("退款¥"+refund);
					}else {
						textview_old_price_bottom.setVisibility(View.GONE);
					}
				}
			}else {//优惠券+满减哟优惠
				textview_old_price_bottom.setVisibility(View.VISIBLE);
				textview_old_price_andother.setText("总价¥"+(Utils.formatDouble(totalPrice+extraServicePrice, 2)));
				if (object.has("cardId")&&!object.isNull("cardId")) {
					int cardId=0;
					try {
						cardId = object.getInt("cardId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					if (refund>0) {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
						}else {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
						}
					}else {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
						}else {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
						}
					}
				}else {
					textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					if (refund>0) {
						textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
					}else {
						textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					}
				}
			}
		}

		private void UpdateStatusEqu2_3(JSONObject object,
				double extraServicePrice, double cutDownMoney,
				double UpTotalPrice) {
			//								double cutDownMoney = couponAmount+reductionPrice; // 
			if (cutDownMoney<=0) {//优惠券+满减没哟优惠
				textview_old_price_andother.setText("总价¥"+(Utils.formatDouble(totalPrice+extraServicePrice+UpTotalPrice, 2)));
				if (object.has("cardId")&&!object.isNull("cardId")) {
					int cardId=0;
					try {
						cardId = object.getInt("cardId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					textview_old_price_bottom.setVisibility(View.VISIBLE);
					textview_old_price_bottom.setText(CardTag+"¥"+basePrice);
					if (refund>0) {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"退款¥"+refund);
						}else {
							textview_old_price_bottom.setText("退款¥"+refund);
						}
					}else {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice);
						}else {
							//textview_old_price_bottom.setText(CardTag+"¥"+basePrice);
							textview_old_price_bottom.setVisibility(View.GONE);
						}
					}
				}else {
					textview_old_price_bottom.setVisibility(View.GONE);
					if (refund>0) {
						textview_old_price_bottom.setVisibility(View.VISIBLE);
						textview_old_price_bottom.setText("退款¥"+refund);
					}else {
						textview_old_price_bottom.setVisibility(View.GONE);
					}
				}
			}else {//优惠券+满减哟优惠
				textview_old_price_bottom.setVisibility(View.VISIBLE);
				textview_old_price_andother.setText("总价¥"+(Utils.formatDouble(totalPrice+extraServicePrice+UpTotalPrice, 2)));
				if (object.has("cardId")&&!object.isNull("cardId")) {
					int cardId=0;
					try {
						cardId = object.getInt("cardId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					if (refund>0) {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
						}else {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
						}
					}else {
						if (cardId>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+basePrice+"  "+couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
						}else {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
						}
					}
				}else {
					textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					if (refund>0) {
						textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"退款¥"+refund);
					}else {
						textview_old_price_bottom.setText(couponCutReductionTag+"¥"+Utils.formatDouble(cutDownMoney, 2)+"");
					}
				}
			}
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

		private void getWorker(JSONObject object) throws JSONException {
			if (object.has("worker")&&!object.isNull("worker")) {
				JSONObject  workerObject = object.getJSONObject("worker");
				if (workerObject.has("gender")&&!workerObject.isNull("gender")) {
					if (workerObject.getInt("gender")==1) {
//								sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_man);
						order_beau_level.setImageResource(R.drawable.icon_man);
					}else {
//								sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_women);
						order_beau_level.setImageResource(R.drawable.icon_women);
					}
				}
				if (workerObject.has("tid")&&!workerObject.isNull("tid")) {
					tid = workerObject.getInt("tid");
				}
				if (workerObject.has("user")&&!workerObject.isNull("user")) {
					JSONObject objectUser = workerObject.getJSONObject("user");
					if (objectUser.has("cellPhone")&&!objectUser.isNull("cellPhone")) {
						beauPhone = objectUser.getString("cellPhone");
					}
				}
				if (workerObject.has("level")&&!workerObject.isNull("level")) {
					JSONObject workLever = workerObject.getJSONObject("level");
					if (workLever.has("sort")&&!workLever.isNull("sort")) {
						int sort = workLever.getInt("sort");
						if (sort==10) {
							sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_junior);
						}else if (sort==20) {
							sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_middle);
						}else if (sort==30) {
							sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_top);
						}
					}
					if (workLever.has("name")&&!workLever.isNull("name")) {
						levelName = workLever.getString("name");
						tvBeauticianLevel.setText(levelName);
					}
				}
				if (workerObject.has("avatar")&&!workerObject.isNull("avatar")) {
					avatar = workerObject.getString("avatar");
					ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+avatar,srivBeautician,0, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View view) {
							// TODO Auto-generated method stub
//									((ImageView)view).setImageResource(R.drawable.dog_icon_unnew);
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
				if (workerObject.has("workerGrade")&&!workerObject.isNull("workerGrade")) {
					int workerGrade = workerObject.getInt("workerGrade");
					int levels = workerGrade/10;
					int stars = workerGrade%10;
					showStars(levels,stars);
				}
				if (workerObject.has("totalOrderAmount")&&!workerObject.isNull("totalOrderAmount")) {
					int totalOrderAmount = workerObject.getInt("totalOrderAmount");
					tvBeauticianOrdernum.setText("接单："+totalOrderAmount);
//					tvBeauticianLevel.setText(Utils.getTextAndColorComments("", "接单："+totalOrderAmount));
				}
				if (workerObject.has("title")&&!workerObject.isNull("title")) {
					String title = workerObject.getString("title");
//							tvBeauticianLevel.setText(title);
				}
				if(workerObject.has("realName")&&!workerObject.isNull("realName")){
					realName = workerObject.getString("realName");
					tvBeauticianName.setText(realName);
				}
				if(workerObject.has("WorkerId")&&!workerObject.isNull("WorkerId")){
					beautician_id = workerObject.getInt("WorkerId");
				}
				
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
	
	
	
	private void showSelectDialog() {
		    if (state==3) {//待确认
		    	MDialog mDialog = new MDialog.Builder(OrderDetailFromOrderToConfirmActivity.this)
				.setTitle("提示")
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("完成后将不可退款，是否确认完成?")
				.setCancelStr("否")
				.setOKStr("是")
				.positiveListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//待确认
						//请求服务器,完成订单 并执行跳转
						CommUtil.confirmOrder(spUtil.getString("cellphone", ""), 
								Global.getIMEI(OrderDetailFromOrderToConfirmActivity.this), 
								OrderDetailFromOrderToConfirmActivity.this,
								OrderId, 
								confirmOrder);
					}
				}).build();
				mDialog.show();
			}else if (state==2||state==22||state==21||state==23) {//已经付款 --当前位置状态忘添加了。。
				if (type==1) {
//					CommUtil.orderCareCancelRemindTxt(
//							spUtil.getString("cellphone", ""), 
//							Global.getIMEI(OrderDetailFromOrderToConfirmActivity.this), 
//							OrderDetailFromOrderToConfirmActivity.this,
//							OrderId,
//							getCancleDetail);
				}else if (type==3) {
//					CommUtil.orderSwimCancelRemindTxt(
//							spUtil.getString("cellphone", ""), 
//							Global.getIMEI(OrderDetailFromOrderToConfirmActivity.this), 
//							OrderDetailFromOrderToConfirmActivity.this,
//							OrderId,
//							getCancleDetail);
				}
			}else if (state ==4 ) {//待评价
				//当前位置不做任何操作--按钮已经隐藏
			}
		
	}

	//取消订单弹出原因
	private void IsShowPop() {
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
			LayoutParams lp = new LayoutParams(dm.widthPixels - 80, dm.heightPixels/2);
			llMain.setLayoutParams(lp);
			
			pWinChoose.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
			button_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pos.clear();
					pWinChoose.dismiss();
					pWinChoose = null;
				}
			});
			button_ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (pos.size()<=0) {
						ToastUtil.showToastShortCenter(OrderDetailFromOrderToConfirmActivity.this, "请先选择取消订单的原因哦");
					}else {
						pos.clear();
						Utils.onDismiss(OrderDetailFromOrderToConfirmActivity.this);
						//新的取消加取消原因接口
						CommUtil.ReasonCancelOrder(
								spUtil.getString("cellphone", ""), 
								Global.getIMEI(OrderDetailFromOrderToConfirmActivity.this), 
								OrderDetailFromOrderToConfirmActivity.this, 
								OrderId,ReasonGit,cancelOrder);
						pWinChoose.dismiss();
						pWinChoose = null;
					}
				}
			});
			
			listview_cancle_notice.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					pos.clear();
					pos.add(position);
					ReasonGit = (String) parent.getItemAtPosition(position);
//					cancleAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	//时间到不可取消 --- 取消订单时请求服务器返回 然后执行该方法
	private void alreadyCancleOrder(String msg,final String data){
		MDialog mDialog = new MDialog.Builder(OrderDetailFromOrderToConfirmActivity.this)
		.setTitle("提示")
		.setType(MDialog.DIALOGTYPE_CONFIRM)
		.setMessage(msg)
		.setCancelStr("继续等待")
		.setOKStr("联系客服")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//执行联系客服
				Utils.telePhoneBroadcast(OrderDetailFromOrderToConfirmActivity.this, data);
			}
		}).build();
		mDialog.show();
	};
	
	
//	confirmOrder
	//确认订单
	private AsyncHttpResponseHandler confirmOrder = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->确认订单 "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					Toast.makeText(OrderDetailFromOrderToConfirmActivity.this, "确认订单成功", Toast.LENGTH_SHORT).show();
					//订单确认成功，广播刷新订单
					Intent intent  = new Intent(OrderDetailFromOrderToConfirmActivity.this, EvaluateActivity.class);
					intent.putExtra("orderid", OrderId);
					
					startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
				}else {
					Toast.makeText(OrderDetailFromOrderToConfirmActivity.this, "确认订单失败", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(OrderDetailFromOrderToConfirmActivity.this, "连接服务器失败,请检查您的网络", Toast.LENGTH_SHORT).show();
		}
			
	};
	
	// 取消订单
	private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->取消订单"+new String(responseBody));
			
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {//取消成功
					String msg =jsonObject.getString("msg");
					String data =jsonObject.getString("data");
					showCancleDetail(code,data);
				}else if (code ==120||code==121) {//美容师在路上
					String data ="";
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						data = jsonObject.getString("data");
					}
					String msg = jsonObject.getString("msg");
					alreadyCancleOrder(msg,data);
				}else {//取消失败
					String msg = jsonObject.getString("msg");
					String data =jsonObject.getString("data");
					showCancleDetail(code,data);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(OrderDetailFromOrderToConfirmActivity.this, "网络连接失败 请检查您的网络",Toast.LENGTH_SHORT).show();
		}
	};
	private SelectableRoundedImageView srivPetStore;
	private TextView tvPetStoreName;
	private TextView tvPetStoreAddr;
	private TextView tvPetStorePhone;
	private LinearLayout llPetStore;
	private LinearLayout llMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private Button btRefresh;
	private LinearLayout llHidden;
	private LinearLayout llShowAll;
	private TextView tvShowAll;
	private ImageView ivShowAll;
	private TextView textview_order_upgradeitems;
	private TextView tvBaseFee;
	private TextView textview_base_show;
	private RelativeLayout rlAddServiceItems;
	private TextView tvAddItemsFee;
	private RelativeLayout rlUpgradeServiceItems;
	private TextView tvUpgradeItemsFee;
	private RelativeLayout rlCoupon;
	private TextView tvCouponFee;
	private PullToRefreshScrollView prsScrollView;
	private RelativeLayout layout_order_baseprice;
	private int basePrice = 0;
	
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
					Utils.dip2px(OrderDetailFromOrderToConfirmActivity.this, 150));
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
						}
					}
				});
				
			}

		}
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
			case Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE:
				// 未返回
				getOrderDetails();
				break;
			case Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW:
				Utils.mLogError("==-->评价返回");
				BackCode = Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW;
				// 评价返回
				getOrderDetails();
				break;
			}
		}
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
					IsShowPop();
					String[] showReason = null;
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("txt")&&!objectData.isNull("txt")) {
							JSONArray array = objectData.getJSONArray("txt");
							showReason = new String[array.length()];
							for (int i = 0; i < array.length(); i++) {
								showReason[i] = array.getString(i);
							}
						}
					}
//					cancleAdapter = new CancleAdapter(OrderDetailFromOrderToConfirmActivity.this,showReason,pos);
//					listview_cancle_notice.setAdapter(cancleAdapter);
				}else if (code==120||code==121) {//判断是否能取消不弹出原因了
					String data ="";
					if (object.has("data")&&!object.isNull("data")) {
						data = object.getString("data");
					}
					String msg = object.getString("msg");
					alreadyCancleOrder(msg,data);
				}else {
					if (object.has("msg")&&!object.isNull("msg")) {
						ToastUtil.showToastShortCenter(OrderDetailFromOrderToConfirmActivity.this, object.getString("msg"));
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
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(orderConfirm, appId, appKey);
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
        UMWXHandler wxHandler = new UMWXHandler(orderConfirm, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(orderConfirm, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
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
        circleMedia.setShareImage(new UMImage(orderConfirm, url));
        circleMedia.setTargetUrl(targUrl);
        mController.setShareMedia(circleMedia);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(message);
        weixinContent.setShareContent(message);
        weixinContent.setShareImage(new UMImage(orderConfirm, url));
        weixinContent.setTargetUrl(targUrl);
        mController.setShareMedia(weixinContent);
        

//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setShareContent(message);
//        qqShareContent.setShareImage(new UMImage(mainActivity, url));
//        mController.setShareMedia(qqShareContent);


//        SinaShareContent sinaContent = new SinaShareContent();
//        sinaContent.setTitle(message);
//        sinaContent.setShareContent(message+targUrl);
//        sinaContent.setShareImage( new UMImage( getActivity(), url));
//        sinaContent.setTargetUrl(targUrl);
//        mController.setShareMedia(sinaContent);
        
        // 设置QQ空间分享内容
//        QZoneShareContent qzone = new QZoneShareContent();
//        qzone.setTitle(" ");//不分享title 但是title处不可为null否则显示分享到QQ空间
//        qzone.setShareContent(message);
//        qzone.setShareImage(new UMImage(mainActivity, url));
//        qzone.setTargetUrl(targUrl);
//        mController.setShareMedia(qzone);


    }
//    int i = 0;
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
			pWinPacket.setBackgroundDrawable(new BitmapDrawable());
			pWinPacket.setOutsideTouchable(true);
			pWinPacket.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			LayoutParams lp = new LayoutParams(dm.widthPixels - 80, 
					Utils.dip2px(OrderDetailFromOrderToConfirmActivity.this, 220));
			llMain.setLayoutParams(lp);
			pWinPacket.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//			Utils.setAttribute(orderConfirm, pWinPacket);
//			layout_dlg_all.setBackgroundColor(android.R.color.transparent);
			//关闭弹窗
			layout_dlg_all.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (pWinPacket.isShowing()) {
//					}
					pWinPacket.dismiss();
					pWinPacket = null;
//					Utils.onDismiss(orderConfirm);
				}
			});
			textView_dlg_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (pWinPacket.isShowing()) {
//					}
					pWinPacket.dismiss();
					pWinPacket = null;
//					Utils.onDismiss(orderConfirm);
				}
			});
			textView_dlg_pop_title.setText(title);
			textView_dlg_pop_content.setText(txt);
			button_dlg_pop_press.setText("去分享");
			button_dlg_pop_press.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					ToastUtil.showToastShortCenter(orderConfirm, "点击分享");
					setShareContent(imgUrl, content, urlShow);
					mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN /*,SHARE_MEDIA.SINA,
		                    SHARE_MEDIA.QZONE*/);
					mController.openShare(orderConfirm, false);
//					if (pWinPacket.isShowing()) {
//					}
//					Utils.onDismiss(orderConfirm);
					pWinPacket.dismiss();
					pWinPacket = null;
				}
			});
//			pWinPacket.setOnDismissListener(new OnDismissListener() {
//				
//				@Override
//				public void onDismiss() {
//					// TODO Auto-generated method stub
//					Utils.onDismiss(orderConfirm);
//				}
//			});
		}
	}
	
	
	private void getData() {
		// TODO Auto-generated method stub
		CommUtil.getTipTxtAfterComment(this,OrderId,getTipTxtAfterComment);
	}
	
	//订单分享发红包
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
	
	private void toNext(Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceId);
		intent.putExtra("shopid", shopId);
		intent.putExtra("previous", Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL);
		intent.putExtra("beautician_id", beautician_id);
		intent.putExtra("areaid", areaId);
		startActivity(intent);
	}
	
	
	//订单明细
	private AsyncHttpResponseHandler getInsertReminderlog = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==============="+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					
				}else {
					
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

	private void SecondTimeDown() {
		isTimeFirst = false;
//		lastOverTime = fIntent.getLongExtra("lastOverTime", 0);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				if (lastOverTime > 0) {
					lastOverTime -= 1000;
					Message msg = Message.obtain();
					msg.what = 3;
					msg.arg1 = (int) lastOverTime;
					handler.sendMessage(msg);
				} else {
					if (timer != null) {
						handler.sendEmptyMessage(4);
						timer.cancel();
						timer = null;
					}
				}
			}
		};
		timer.schedule(task, 0, 1000);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void goNextForData(Class clazz){
		Intent intent = new Intent(this, clazz);
		intent.putExtra("previous", Global.ORDER_CHANGE_CUSTOMEORPHONE);
		intent.putExtra("OrderId", OrderId);
		intent.putExtra("cname", cname);
		intent.putExtra("cphone", cphone);
		startActivity(intent);
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
	
	private SpannableString setLastShowStyle(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this,R.style.order_show_left_heji),0, 2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,R.style.service_old_service_two), 2, lastOnePrice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

}
