package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CancleAdapter;
import com.haotang.pet.adapter.WaitToPayShowItem;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.MulPetService;
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
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 待付款
 * @author Administrator
 *
 */
public class OrderDetailFromOrderActivity extends SuperActivity implements OnClickListener{
	
	public static OrderDetailFromOrderActivity act;
	private ImageButton ibBack;
	private TextView tvTitle;
	private Button bt_titlebar_other;
	private CircleImageView srivBeautician;
	private TextView tvBeauticianName;
	private TextView tvBeauticianOrdernum;
	private TextView tvBeauticianLevel;
	private ImageView ivBeauticianLevel1;
	private ImageView ivBeauticianLevel2;
	private ImageView ivBeauticianLevel3;
	private ImageView ivBeauticianLevel4;
	private ImageView ivBeauticianLevel5;
	private RelativeLayout rlCoupon;
	private TextView tvCoupon;
	private TextView tvMoney;
	private Button btPay;
	private int paytype = 1;//1是微信，2为支付宝
	private SharedPreferenceUtil spUtil;
	public static ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private int previous = 0;
	private int couponId = 0;
	private double couponAmount = 0;
	private int OrderId=0;
	private int serviceloc;
	private String customerMobile;
	private int pickup;
	private int addressid;
	private double lat;
	private double lng;
	
//	-------
	private TextView textView_yuyue_time;
	private TextView textViewAddrType;
	private TextView textViewAddrTypeDes;
	private TextView textView_people_name;
	private TextView textView_people_phone;
	private TextView textView_address_detail;
	private TextView textView_remark_detail;
	private PopupWindow pWinCancle;
	private LayoutInflater mInflater;
	private int PetId;
	private int petService;
	
	private Timer timer;
//	------
	private long lastOverTime = 0;
//	------
	
	
	
	private TextView textView_order_thr;//分钟
	private TextView textView_order_time_two;//秒
	
//	-------
	private double needMoney = 0;
	private double totalMoney = 0;
	private double AllTotalMoney = 0;
	private String address;
	private MProgressDialog pDialog;
	private String customerName;
	
	private int customerId;
	private int workerid;
	private TimerTask task;
	private String outorderno;
	
	
	private String name ="";
	private String petName = "";
	private String date;
	private String endTime;
	
	private TextView textView_order_num;
	private TextView textView_order_time;
	private double balance;
	private double baseprice;
	private double addserviceprice;
	private double pickupprice;
	private boolean isShowAll;
	private boolean gotoOrderDetail;//是否跳转到取消订单界面
	
	
	
//	------start 2015年12月22日11:31:12 新增寄养功能
	private RelativeLayout layout_foster_detail_show_price;//寄养宠物套餐价格模块
	private TextView textView_foster_name;//寄养套餐名
	private TextView textView_foster_name_price;//寄养套餐价格
	
	private LinearLayout layout_foster_detail;//寄养入住离店时间等详情布局 
	
	private LinearLayout layout_foster_come_in;//寄养入住时间
	private TextView textview_foster_come_in_time;
	
	private LinearLayout layout_foster_go_out;//寄养离店时间
	private TextView textview_foster_go_out_time;
	
	private TextView textview_foster_home_style;//房型
	
	private TextView textview_pet_food_detail;//宠粮价格信息等
	
	private LinearLayout layout_foster_is_pickup;//是否支持接送 ，不支持隐藏
	private TextView textview_foster_pickup_detail;//有接送信息展示
	private TextView textview_foster_pet_address;//宠物寄养--宠物地址
	private TextView textview_foster_phone_people;//宠物寄养--联系人
	private TextView textView_foster_Remarks;//宠物寄养--备注
	private RelativeLayout layout_foster_look_all_or_no;//宠物寄养-是否显示全部信息
	
	private RelativeLayout layout_order_foster;//如果仅有寄养费用无其他附加费用则整个模块隐藏
	
	//分别针对寄养 和 洗澡美容的门店
	private LinearLayout washandotherpetstore;//洗澡美容门店
	private LinearLayout newfosterpetstore;//寄养门店
	
	private LinearLayout layout_foster_is_gone;
	private TextView textview_look_all_or_no;
	private ImageView imageView_look_all_or_no;
//	寄养
	private SelectableRoundedImageView sriv_petstore_image;
	private TextView tv_petstore_name;
	private TextView tv_petstore_addr;
	private TextView tv_petstore_phone;
	
	
	private boolean isShowFoster = false;
	
	private RelativeLayout layout_bath_and_beauty;
	private LinearLayout layout_foster;
	
	private int type = 0;
//	------end 2015年12月22日11:31:20
	
//	------start 2016年3月2日10:43:27
	private LinearLayout layout_foster_is_wash;
	private TextView textview_foster_wash_detail;
	private ListView listview_cancle_notice;
	private CancleAdapter cancleAdapter;
	private PopupWindow pWinChoose;
	private List<Integer> pos = new ArrayList<Integer>();
	private String ReasonGit="";
	
	private MyGridView gridView_foster_show;
	private List<String> fosterList = new ArrayList<String>();
	private WaitToPayShowItem payShowItem;//寄养展示
	private int bathRequired = -1;
	
	//宠物1
	private LinearLayout layout_more_pet_one;
	private TextView more_pet_name_one;
	private TextView more_pet_price_one;
	private MyGridView gridView_more_pet_one;
	private WaitToPayShowItem WaitToPayShowItemOne;
	private List<String> oneList = new ArrayList<String>();
	private View more_pet_one_view;
	//宠物2
	private LinearLayout layout_more_pet_two;
	private TextView more_pet_name_two;
	private TextView more_pet_price_two;
	private MyGridView gridView_more_pet_two;
	private WaitToPayShowItem WaitToPayShowItemTwo;
	private List<String> twoList = new ArrayList<String>();
	private View more_pet_two_view;
	//宠物3
	private LinearLayout layout_more_pet_thr;
	private TextView more_pet_name_thr;
	private TextView more_pet_price_thr;
	private MyGridView gridView_more_pet_thr;
	private WaitToPayShowItem WaitToPayShowItemThr;
	private List<String> thrList = new ArrayList<String>();
	private View more_pet_thr_view;
	//宠物4
	private LinearLayout layout_more_pet_four;
	private TextView more_pet_name_four;
	private TextView more_pet_price_four;
	private MyGridView gridView_more_pet_four;
	private WaitToPayShowItem WaitToPayShowItemFour;
	private List<String> fourList = new ArrayList<String>();
	private View more_pet_four_view;
	//宠物5
	private LinearLayout layout_more_pet_five;
	private TextView more_pet_name_five;
	private TextView more_pet_price_five;
	private MyGridView gridView_more_pet_five;
	private WaitToPayShowItem WaitToPayShowItemFive;
	private List<String> fiveList = new ArrayList<String>();
	
	private RelativeLayout rlOrderPikcupPriceShow;//接送价格优惠卷上展示位置
	private View view_pick_down_line;//如接送需要隐藏 则附带
	private TextView textview_more_pet_pickprice;
	private ImageView more_pet_show_other;
	
	
	private ArrayList<MulPetService> mulPetServiceList = new ArrayList<MulPetService>();
	private MulPetService mulPetService = null;
	private LinearLayout layout_addrtype;
	
	private SelectableRoundedImageView  sriv_orderdetail_beautician_sex;
	private ImageView order_beau_level;
	
	private RelativeLayout Rlayout_show_beau;
	private TextView textView_address;
	private String ShopAddress="";
	private int shopId = 9;
	private int areaId;
	protected int orderFee;
	private int status;
	private boolean couponEnable;
	private TextView textview_foster_home_address;//寄养中心
	private TextView textview_foster_home_address_title;//title 显示中转站 或者 寄养中心
//	------end 2016年3月2日10:43:35
	
	//start 2016年12月7日18:45:10
	private TextView textview_show_pet_type;
	private TextView textview_show_train_content;
	private TextView textview_train_time;
	private TextView textview_train_address;
	private LinearLayout layout_pet_address;
	private View view_line;
	private LinearLayout layout_tarnser;
	//end 2016年12月7日18:45:14
//	摄像头start 2016年12月23日14:57:24
	private RelativeLayout layout_foster_shexaingtou;
	private TextView textview_shexiangtou_price_and_days;
//	摄像头end 2016年12月23日14:57:29
	private boolean isVie = false;
	
	private double onePetTotalMoney = 0;
	private double twoPetTotalMoney = 0;
	private double thrPetTotalMoney = 0;
	private double fourPetTotalMoney = 0;
	private double fivePetTotalMoney = 0;
	private TextView textview_member_cutdown;
	private LinearLayout layout_people;
	private ImageView image_change_custome;
	
	
	private LinearLayout layout_go_home_service_price;
	private TextView textview_go_home_service_tag;
	private TextView textview_go_home_service_price;
	
	private LinearLayout layout_go_home_manjianyouhui;
	private TextView textview_manjianyouhui_left;
	private TextView textview_manjianyouhui_right;
	
	private RelativeLayout order_detail_show_count;
	private TextView textView_confirm_price_show;
	private TextView textview_old_price_andother;
	private double extraServicePrice = 0;
	private	double reductionPrice = 0;
	private RelativeLayout layout_copy_ordernum;
	
	private LinearLayout layout_card_title;
	private TextView textview_card_title;
	private TextView textview_card_detail;
	
	private TextView textview_old_price_bottom;
	private boolean isHasCard = false;
	private double CutCardMoney = 0;
	private String couponCutReductionTag="";
	private String CardTag="";
	private double getCouponNeedMoney=0;
	private double cameraPrice = 0;
	private String bathPetIds="";
	private ArrayList<Integer> listIds = new ArrayList<Integer>();
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Intent intent = null;
			switch (msg.what) {
			case 0:
				long lastTimerShowHMS= msg.arg1;
				String time = Utils.formatTimeFM(lastTimerShowHMS);
				String minute = time.substring(0, 2);
				String second = time.substring(3, 5);
				textView_order_thr.setText(minute);
				textView_order_time_two.setText(second);
				break;
			case 1:
				//订单到时间，广播刷新订单
				intent = new Intent();
				intent.setAction("android.intent.action.mainactivity");
				intent.putExtra("previous", Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
				sendBroadcast(intent);
				if(task!=null){
					task.cancel();
					task = null;
				}
				if(timer!=null){
					timer.cancel();
					timer = null;
				}
				if(gotoOrderDetailNow()){
					goOrderDetail();
				}else{
					gotoOrderDetail = true;
				}
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
				try {
					int codeNum = msg.arg1;
					if (codeNum==0) {
						//订单取消成功，广播刷新订单
						intent = new Intent();
						intent.setAction("android.intent.action.mainactivity");
						intent.putExtra("previous", Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
						sendBroadcast(intent);
						finishWithAnimation();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};
	protected int roomType;
//	-------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail_from_order);
		act = this;
		OrderId = getIntent().getIntExtra("orderid", 0);
		initReceiver();
		findVeiw();
		setView();
		
	}


	private void findVeiw() {
		pDialog = new MProgressDialog(this);
		mInflater = LayoutInflater.from(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		
		washandotherpetstore = (LinearLayout) findViewById(R.id.washandotherpetstore);//洗澡美容
		newfosterpetstore = (LinearLayout) findViewById(R.id.newfosterpetstore);//寄养
		
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		
		more_pet_name_one = (TextView) findViewById(R.id.more_pet_name_one);
		more_pet_price_one = (TextView) findViewById(R.id.more_pet_price_one);
		
		tvAddrtype = (TextView) findViewById(R.id.tv_orderdetail_addrtype);
		
		srivBeautician = (CircleImageView) findViewById(R.id.sriv_orderdetail_beautician);
		tvBeauticianName = (TextView) findViewById(R.id.tv_orderdetail_beauticianname);
		tvBeauticianOrdernum = (TextView) findViewById(R.id.tv_orderdetail_beauticianordernum);
		tvBeauticianLevel = (TextView) findViewById(R.id.tv_orderdetail_beauticianlevel);
		ivBeauticianLevel1 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel1);
		ivBeauticianLevel2 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel2);
		ivBeauticianLevel3 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel3);
		ivBeauticianLevel4 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel4);
		ivBeauticianLevel5 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel5);
		
		rlCoupon = (RelativeLayout) findViewById(R.id.rl_orderdetail_coupon);
		tvCoupon = (TextView) findViewById(R.id.tv_orderdetail_coupon);
		
		tvMoney = (TextView) findViewById(R.id.tv_orderdetail_money);
		btPay = (Button) findViewById(R.id.bt_orderdetail_pay);
		
		
		textView_yuyue_time = (TextView) findViewById(R.id.textView_yuyue_time);
		textView_people_name = (TextView) findViewById(R.id.textView_people_name);
		textView_people_phone = (TextView) findViewById(R.id.textView_people_phone);
		textView_address_detail = (TextView) findViewById(R.id.textView_address_detail);
		textView_remark_detail = (TextView) findViewById(R.id.textView_remark_detail);
		textView_order_thr = (TextView) findViewById(R.id.textView_order_thr);
		textView_order_time_two = (TextView) findViewById(R.id.textView_order_time_two);
		
		
		textView_order_num = (TextView) findViewById(R.id.textView_order_num);
		textView_order_time = (TextView) findViewById(R.id.textView_order_time);
		
		llHidden = (LinearLayout) findViewById(R.id.ll_orderdetailfromorder_hidden);
		llShowAll = (LinearLayout) findViewById(R.id.ll_orderdetail_showall);
		tvShowAll = (TextView) findViewById(R.id.tv_orderdetail_showall);
		ivShowAll = (ImageView) findViewById(R.id.iv_orderdetail_showall);
		
		llMain = (LinearLayout) findViewById(R.id.ll_orderdetail_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		
//		-------------start 2015年12月22日19:05:16
		
		layout_foster_detail_show_price = (RelativeLayout) findViewById(R.id.layout_foster_detail_show_price);
		textView_foster_name = (TextView) findViewById(R.id.textView_foster_name);
		textView_foster_name_price = (TextView) findViewById(R.id.textView_foster_name_price);
		layout_foster_detail = (LinearLayout) findViewById(R.id.layout_foster_detail);
		layout_foster_come_in = (LinearLayout) findViewById(R.id.layout_foster_come_in);
		textview_foster_come_in_time = (TextView) findViewById(R.id.textview_foster_come_in_time);
		layout_foster_go_out = (LinearLayout) findViewById(R.id.layout_foster_go_out);
		textview_foster_go_out_time = (TextView) findViewById(R.id.textview_foster_go_out_time);
		textview_foster_home_style = (TextView) findViewById(R.id.textview_foster_home_style);
		textview_pet_food_detail = (TextView) findViewById(R.id.textview_pet_food_detail);
		layout_foster_is_pickup = (LinearLayout) findViewById(R.id.layout_foster_is_pickup);
		textview_foster_pickup_detail = (TextView) findViewById(R.id.textview_foster_pickup_detail);
		textview_foster_pet_address = (TextView) findViewById(R.id.textview_foster_pet_address);
		textview_foster_phone_people = (TextView) findViewById(R.id.textview_foster_phone_people);
		textView_foster_Remarks = (TextView) findViewById(R.id.textView_foster_Remarks);
		layout_foster_look_all_or_no = (RelativeLayout) findViewById(R.id.layout_foster_look_all_or_no);
		layout_order_foster = (RelativeLayout) findViewById(R.id.layout_order_foster);
		layout_foster_is_gone = (LinearLayout) findViewById(R.id.layout_foster_is_gone);
		textview_look_all_or_no = (TextView) findViewById(R.id.textview_look_all_or_no);
		imageView_look_all_or_no = (ImageView) findViewById(R.id.imageView_look_all_or_no);
		gridView_foster_show = (MyGridView) findViewById(R.id.gridView_foster_show);
//		-------------end 2015年12月22日19:05:23
		prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_orderdetail_main);
		
		
		textViewAddrType = (TextView) findViewById(R.id.textView_addrtype);
		textViewAddrTypeDes = (TextView) findViewById(R.id.textView_addrtype_des);
		
//		--------start 2015年12月23日10:37:08
		//洗澡美容门店更改
		srivPetStore = (SelectableRoundedImageView) washandotherpetstore.findViewById(R.id.sriv_petstore_image);
		tvPetStoreName = (TextView) washandotherpetstore.findViewById(R.id.tv_petstore_name);
		tvPetStoreAddr = (TextView) washandotherpetstore.findViewById(R.id.tv_petstore_addr);
		tvPetStorePhone = (TextView) washandotherpetstore.findViewById(R.id.tv_petstore_phone);
		//寄养门店
		sriv_petstore_image = (SelectableRoundedImageView) newfosterpetstore.findViewById(R.id.sriv_petstore_image);
		tv_petstore_name = (TextView) newfosterpetstore.findViewById(R.id.tv_petstore_name);
		tv_petstore_addr = (TextView) newfosterpetstore.findViewById(R.id.tv_petstore_addr);
		tv_petstore_phone = (TextView) newfosterpetstore.findViewById(R.id.tv_petstore_phone);
		
		
		layout_bath_and_beauty = (RelativeLayout) findViewById(R.id.layout_bath_and_beauty);
		layout_foster = (LinearLayout) findViewById(R.id.layout_foster);
//		--------end 2015年12月23日10:37:18
		
		
		llPetStore = (LinearLayout) findViewById(R.id.ll_petstore);
		
//		---start  2016年3月4日10:31:50 新增入住洗澡
		layout_foster_is_wash = (LinearLayout) findViewById(R.id.layout_foster_is_wash);
		textview_foster_wash_detail = (TextView) findViewById(R.id.textview_foster_wash_detail);
//		---end 2016年3月4日10:31:58
		
		
//		宠物1
		layout_more_pet_one = (LinearLayout) findViewById(R.id.layout_more_pet_one);
		more_pet_name_one = (TextView) findViewById(R.id.more_pet_name_one);
		more_pet_price_one = (TextView) findViewById(R.id.more_pet_price_one);
		gridView_more_pet_one = (MyGridView) findViewById(R.id.gridView_more_pet_one);
		more_pet_one_view = (View) findViewById(R.id.more_pet_one_view);
//		宠物2
		layout_more_pet_two = (LinearLayout) findViewById(R.id.layout_more_pet_two);
		more_pet_name_two = (TextView) findViewById(R.id.more_pet_name_two);
		more_pet_price_two = (TextView) findViewById(R.id.more_pet_price_two);
		gridView_more_pet_two = (MyGridView) findViewById(R.id.gridView_more_pet_two);
		more_pet_two_view = (View) findViewById(R.id.more_pet_two_view);
//		宠物3
		layout_more_pet_thr = (LinearLayout) findViewById(R.id.layout_more_pet_thr);
		more_pet_name_thr = (TextView) findViewById(R.id.more_pet_name_thr);
		more_pet_price_thr = (TextView) findViewById(R.id.more_pet_price_thr);
		gridView_more_pet_thr = (MyGridView) findViewById(R.id.gridView_more_pet_thr);
		more_pet_thr_view = (View) findViewById(R.id.more_pet_thr_view);

//		宠物4
		layout_more_pet_four = (LinearLayout) findViewById(R.id.layout_more_pet_four);
		more_pet_name_four = (TextView) findViewById(R.id.more_pet_name_four);
		more_pet_price_four = (TextView) findViewById(R.id.more_pet_price_four);
		gridView_more_pet_four = (MyGridView) findViewById(R.id.gridView_more_pet_four);
		more_pet_four_view = (View) findViewById(R.id.more_pet_four_view);
//		宠物5
		layout_more_pet_five = (LinearLayout) findViewById(R.id.layout_more_pet_five);
		more_pet_name_five = (TextView) findViewById(R.id.more_pet_name_five);
		more_pet_price_five = (TextView) findViewById(R.id.more_pet_price_five);
		gridView_more_pet_five = (MyGridView) findViewById(R.id.gridView_more_pet_five);
		rlOrderPikcupPriceShow = (RelativeLayout) findViewById(R.id.rl_orderdetail_pikcup_price_show);
		view_pick_down_line = (View) findViewById(R.id.view_pick_down_line);
		textview_more_pet_pickprice = (TextView) findViewById(R.id.textview_more_pet_pickprice);
		more_pet_show_other = (ImageView) findViewById(R.id.more_pet_show_other);
		layout_addrtype = (LinearLayout) findViewById(R.id.layout_addrtype);
		order_beau_level = (ImageView) findViewById(R.id.order_beau_level);
		sriv_orderdetail_beautician_sex = (SelectableRoundedImageView) findViewById(R.id.sriv_orderdetail_beautician_sex);
		Rlayout_show_beau = (RelativeLayout) findViewById(R.id.Rlayout_show_beau);
		textView_address = (TextView) findViewById(R.id.textView_address);
		textview_foster_home_address = (TextView) findViewById(R.id.textview_foster_home_address);
		textview_foster_home_address_title = (TextView) findViewById(R.id.textview_foster_home_address_title);
		
		//训练增加
		textview_show_pet_type = (TextView) findViewById(R.id.textview_show_pet_type);
		textview_show_train_content = (TextView) findViewById(R.id.textview_show_train_content);
		textview_train_time = (TextView) findViewById(R.id.textview_train_time);
		textview_train_address = (TextView) findViewById(R.id.textview_train_address);
		layout_pet_address = (LinearLayout) findViewById(R.id.layout_pet_address);
		view_line = (View) findViewById(R.id.view_line);
		layout_tarnser = (LinearLayout) findViewById(R.id.layout_tarnser);
		layout_foster_shexaingtou = (RelativeLayout) findViewById(R.id.layout_foster_shexaingtou);
		textview_shexiangtou_price_and_days = (TextView) findViewById(R.id.textview_shexiangtou_price_and_days);
//		textview_train_name_price = (TextView) findViewById(R.id.textview_train_name_price);
		
		
		//游泳会员打折新增
		textview_member_cutdown = (TextView) findViewById(R.id.textview_member_cutdown);
		layout_people = (LinearLayout) findViewById(R.id.layout_people);
		image_change_custome = (ImageView) findViewById(R.id.image_change_custome);
		
		layout_go_home_service_price = (LinearLayout)findViewById(R.id.layout_go_home_service_price);
		textview_go_home_service_tag = (TextView)findViewById(R.id.textview_go_home_service_tag);
		textview_go_home_service_price = (TextView)findViewById(R.id.textview_go_home_service_price);

		layout_go_home_manjianyouhui = (LinearLayout)findViewById(R.id.layout_go_home_manjianyouhui);
		textview_manjianyouhui_left = (TextView)findViewById(R.id.textview_manjianyouhui_left);
		textview_manjianyouhui_right = (TextView)findViewById(R.id.textview_manjianyouhui_right);

		order_detail_show_count = (RelativeLayout)findViewById(R.id.order_detail_show_count);
		textView_confirm_price_show = (TextView)findViewById(R.id.textView_confirm_price_show);
		textview_old_price_andother = (TextView)findViewById(R.id.textview_old_price_andother);
		layout_copy_ordernum = (RelativeLayout)findViewById(R.id.layout_copy_ordernum);

		layout_card_title = (LinearLayout)findViewById(R.id.layout_card_title);
		textview_card_title = (TextView)findViewById(R.id.textview_card_title);
		textview_card_detail = (TextView)findViewById(R.id.textview_card_detail);
		textview_old_price_bottom = (TextView) findViewById(R.id.textview_old_price_bottom);
	}

	private void setView() {
		tvTitle.setText("订单详情");
		bt_titlebar_other.setText("取消订单");
		tvMsg1.setText("网络异常链接服务器失败");
		btRefresh.setVisibility(View.VISIBLE);
		previous = getIntent().getIntExtra("previous", 0);
		
		llPetStore.setVisibility(View.GONE);
		ibBack.setOnClickListener(this);
		rlCoupon.setOnClickListener(this);
		btPay.setOnClickListener(this);
		bt_titlebar_other.setOnClickListener(this);
		llShowAll.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		layout_foster_look_all_or_no.setOnClickListener(this);
		more_pet_show_other.setOnClickListener(this);
		layout_people.setOnClickListener(this);
		layout_copy_ordernum.setOnClickListener(this);
		
		prsScrollView.setMode(Mode.PULL_FROM_START);
		prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					if (timer!=null) {
						timer.cancel();
						timer = null;
					}
					if(task!=null){
						task.cancel();
						task=null;
					}
					getOrderDetails();
				}
			}
		});
		
		showMain(true);
		getOrderDetails();
	}

	private boolean gotoOrderDetailNow(){
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		//是否在前台运行
		if(!list.isEmpty()){
			  ComponentName topActivity = list.get(0).topActivity;
	            if (topActivity.getPackageName().equals(getPackageName())&&
	            		topActivity.getClassName().equals("com.haotang.pet.OrderDetailFromOrderActivity")) {
	                return true;
	            }
		}
		return false;
	}
	
	private void goOrderDetail(){
		ToastUtil.showToastShort(OrderDetailFromOrderActivity.this, "抱歉,当前订单已过时");
		if (type==1) {
			goNext(OrderDetailFromOrderToConfirmActivity.class, OrderId);
		}else if (type==2||type==4) {
			goNext(OrderFosterDetailActivity.class, OrderId);
		}
		finishWithAnimation();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回
			finishWithAnimation();
			break;
		case R.id.bt_null_refresh:
			//刷新
			getOrderDetails();
			break;
		case R.id.bt_titlebar_other:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_CancelOrder);
			//取消
			showSelectDialog();
//			Intent intentCancle = new Intent(OrderDetailFromOrderActivity.this, OrderCancleActivity.class);
//			intentCancle.putExtra("state", status);
//			intentCancle.putExtra("type", type);
//			intentCancle.putExtra("orderid", OrderId);
//			//需要带上各种订单id type 什么
//			startActivity(intentCancle);

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
		case R.id.rl_orderdetail_coupon:
			//选择优惠券
			if(couponList.size()>0){
				goNextForData(AvailableCouponActivity.class, Global.PRE_ORDERDETAILFROMORDERACTIVITY_TO_AVAILABLECOUPONACTIVITY);
			}else{
				ToastUtil.showToastShortCenter(OrderDetailFromOrderActivity.this, "您当前没有可用的优惠券");
			}
			break;
		

		case R.id.bt_orderdetail_pay:
			//开始支付
			if(couponAmount>=totalMoney){
				needMoney = 0;
				if (type==4) {//训练二次支付
					TwoTrainPay();
				}else {
					sendOrder();
				}
			}else{
				Intent intent = new Intent(act, OrderPayActivity.class);
				intent.putParcelableArrayListExtra("mpslist", mulPetServiceList);
				intent.putExtra("previous",Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY);
				intent.putExtra("orderid",OrderId);
				intent.putExtra("couponid",couponId);
				intent.putExtra("conponamount",couponAmount);
				intent.putExtra("needbath",bathRequired);
				intent.putExtra("needfee",needMoney);
				intent.putExtra("type",type);
				intent.putExtra("shopId",shopId);
				intent.putExtra("orderFee",orderFee);
				intent.putExtra("roomType",roomType);
				intent.putExtra("lastOverTime", lastOverTime);
				intent.putExtra("bathPetIds",bathPetIds);
				intent.putExtra("petService", petService);
				intent.putExtra("cameraPrice",addserviceprice);
				intent.putExtra("isVie", isVie);
				intent.putExtra("listIds",getListIds(listIds));
				startActivity(intent);
			}
//			
			break;
			
		case R.id.layout_foster_look_all_or_no:
			if (type==2) {
				layout_foster_is_gone.setVisibility(View.VISIBLE);
			}else {
				if (isShowFoster) {
					isShowFoster = false;
					layout_foster_is_gone.setVisibility(View.GONE);
					textview_look_all_or_no.setText("查看全部");
					imageView_look_all_or_no.setBackgroundResource(R.drawable.icon_arrow_down);
				}else {
					isShowFoster = true;
					layout_foster_is_gone.setVisibility(View.VISIBLE);
					textview_look_all_or_no.setText("点击收起");
					imageView_look_all_or_no.setBackgroundResource(R.drawable.icon_arrow_up);
				}
			}
			break;
		case R.id.more_pet_show_other:
			//接送提示
			if (type==1) {
				goNextForData(PickupHintActivity.class, 0);
			}else if (type==2) {
				goNextForData(PickupHintActivity.class, Global.FOSTERCAREORDER_TO_COUPON);
			}
			break;
		case R.id.layout_people:
//			ToastUtil.showToastShortCenter(mContext, "更新联系人");
			goNextChangeCustomer(ContactActivity.class, Global.ORDER_WAIT_TO_CHANGE_CUSTOME_REQUEST_CODE);
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
	
	private void goNextForData(Class clazz, int requestcode){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		intent.putExtra("couponId", couponId);
		startActivityForResult(intent, requestcode);
	}
	private void goNext(Class clazz, int orderid){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("orderid", orderid);
		startActivity(intent);
	}
	
	private String getPetID_ServiceId_MypetId_ItemIds(ArrayList<MulPetService> list){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<list.size();i++){
			MulPetService mps = list.get(i);
			if(i<list.size()-1){
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if(mps.addServiceIds!=null&&!"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
				sb.append("_");
				
			}else{
				sb.append(mps.petId);
				sb.append("_");
				sb.append(mps.serviceId);
				sb.append("_");
				sb.append(mps.petCustomerId);
				sb.append("_");
				if(mps.addServiceIds!=null&&!"".equals(mps.addServiceIds))
					sb.append(mps.addServiceIds);
				else
					sb.append("0");
			}
		}
		return sb.toString();
	}
	
	private String getTrainStrp(){
		StringBuffer sb = new StringBuffer();
		sb.append(PetId+"_");
		sb.append(petService+"_");
		sb.append(customerId);
		return sb.toString();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Global.RESULT_OK){
			if(requestCode == Global.PRE_ORDERDETAILFROMORDERACTIVITY_TO_AVAILABLECOUPONACTIVITY){
				couponId = data.getIntExtra("couponid", 0);
				String couponcontent = data.getStringExtra("content");
				String camount = data.getStringExtra("amount") == null || "".equals(data.getStringExtra("amount"))?"0":data.getStringExtra("amount");
				couponAmount = Utils.formatDouble(Double.parseDouble(camount),2);
				tvCoupon.setText(couponcontent);
				if (couponAmount+Math.abs(reductionPrice)>=totalMoney+extraServicePrice) {
					paytype = 3;
					needMoney = 0;
				}else {
					needMoney = totalMoney+extraServicePrice - couponAmount-Math.abs(reductionPrice);
				}
				if (isHasCard) {
					textview_old_price_bottom.setVisibility(View.VISIBLE);
					double lastShowPrice = couponAmount+Math.abs(reductionPrice);
					if (getCouponNeedMoney>couponAmount) {
						if (lastShowPrice>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney+"  "+couponCutReductionTag+"¥"+lastShowPrice);
						}else {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney);
						}
					}else {
						if (lastShowPrice>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney+"  "+couponCutReductionTag+"¥"+(getCouponNeedMoney+Math.abs(reductionPrice)));
						}else {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney);
						}
					}
					
				}else {
					double lastShowPrice = couponAmount+Math.abs(reductionPrice);
					if (getCouponNeedMoney>couponAmount) {
						if (lastShowPrice>0) {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+lastShowPrice);
							textview_old_price_bottom.setVisibility(View.VISIBLE);
						}else {
							textview_old_price_bottom.setVisibility(View.GONE);
						}
					}else {
						if (lastShowPrice>0) {
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+(getCouponNeedMoney+Math.abs(reductionPrice)));
							textview_old_price_bottom.setVisibility(View.VISIBLE);
						}else {
							textview_old_price_bottom.setVisibility(View.GONE);
						}
					}
					
				}
				
				setLastPrice();
			}
			if (requestCode==Global.ORDER_WAIT_TO_CHANGE_CUSTOME_REQUEST_CODE) {
				getOrderDetails();
			}
		}
	}
	
	private void setLastPrice() {
		String text = "实付款: ¥ "+ Utils.formatDouble(needMoney,2);
		SpannableString ss = new SpannableString(text); 
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tabback)), 0, 4,Spanned.SPAN_INCLUSIVE_INCLUSIVE);  
		ss.setSpan(new TextAppearanceSpan(this, R.style.style1), 6, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tvMoney.setText(ss);
		
		textView_confirm_price_show.setText(ss);
	}
	
	/**
	 * 获取可用的优惠券
	 */
	private void getCoupon(){
		couponList.clear();
		if (type==3) {
			Utils.mLogError("==-->游泳请求优惠券");
			serviceloc=1;
			pickup = 0;
			CommUtil.mycouponsForSwim(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
					Global.getCurrentVersion(this), date, type, serviceloc,
					workerid,pickup,customerName,customerMobile,addressid,address, lat, lng,
					getPetId_ServiceId_MyPetId(mulPetServiceList),areaId,Utils.formatDouble(needMoney,2),shopId,
					couponHanler);
		}else {
			if (type==2) {
				if (couponEnable) {
					CommUtil.getAvailableCoupon(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
							Global.getCurrentVersion(this), date, type, serviceloc,
							workerid,pickup,customerName,customerMobile,addressid,address, lat, lng,
							getPetID_ServiceId_MypetId_ItemIds(mulPetServiceList),areaId,Utils.formatDouble(needMoney,2),shopId,
							endTime,couponHanler);
				}else {
					tvCoupon.setTextColor(Color.parseColor("#666666"));
				}
			}else if (type==4) {
				CommUtil.getAvailableCoupon(this,
						spUtil.getString("cellphone", ""), 
						Global.getIMEI(this),
						Global.getCurrentVersion(this),
						date, 
						type, 
						serviceloc,
						workerid,
						pickup,
						customerName,
						customerMobile,
						addressid,
						address, 
						lat, 
						lng,
						getTrainStrp(),
						areaId,
						Utils.formatDouble(needMoney,2),
						shopId,
						null,
						couponHanler);
			}else {
				CommUtil.getAvailableCoupon(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
						Global.getCurrentVersion(this), date, type, serviceloc,
						workerid,pickup,customerName,customerMobile,addressid,address, lat, lng,
						getPetID_ServiceId_MypetId_ItemIds(mulPetServiceList),areaId,Utils.formatDouble(needMoney,2),shopId,
						null,couponHanler);
			}
		}
	}
	//查询订单明细
	private void getOrderDetails() {
		pDialog.showDialog();
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),  
				Global.getIMEI(this), this, OrderId, getOrderDetails);
	}
	
	
	//订单明细
	private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler(){

		

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("订单详情："+new String(responseBody));
			prsScrollView.onRefreshComplete();
			pDialog.closeDialog();
			CutCardMoney = 0;
			showMain(true);
			try {
				mulPetServiceList.clear();
				fosterList.clear();
				oneList.clear();
				twoList.clear();
				thrList.clear();
				fourList.clear();
				fiveList.clear();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
					JSONObject object = jsonObject.getJSONObject("data");
					MulPetService mulPetService = new MulPetService();
					//判断洗澡美容或者寄养
					if (object.has("type")&&!object.isNull("type")) {
						type = object.getInt("type");
						if (type==1) {//洗澡美容
							layout_bath_and_beauty.setVisibility(View.VISIBLE);
							layout_foster.setVisibility(View.GONE);
							rlOrderPikcupPriceShow.setVisibility(View.VISIBLE);
							view_pick_down_line.setVisibility(View.VISIBLE);
						}else if (type==2||type==4) {//寄养
							layout_bath_and_beauty.setVisibility(View.GONE);
							layout_foster.setVisibility(View.VISIBLE);
							rlOrderPikcupPriceShow.setVisibility(View.GONE);
							view_pick_down_line.setVisibility(View.GONE);
							layout_foster_look_all_or_no.setVisibility(View.GONE);//寄养的话隐藏查看全部
							layout_foster_is_gone.setVisibility(View.VISIBLE);//直接显示
						}
					}
					if (object.has("bathPetIds")&&!object.isNull("bathPetIds")) {
						bathPetIds = object.getString("bathPetIds");
					}
					if (object.has("timeCardTag")&&!object.isNull("timeCardTag")) {
						layout_card_title.setVisibility(View.VISIBLE);
						JSONObject objectTimeCardTag  = object.getJSONObject("timeCardTag");
						if (objectTimeCardTag.has("title")&&!objectTimeCardTag.isNull("title")) {
							textview_card_title.setText(objectTimeCardTag.getString("title"));
						}
						if (objectTimeCardTag.has("tip")&&!objectTimeCardTag.isNull("tip")) {
							textview_card_detail.setText(objectTimeCardTag.getString("tip"));
						}
					}else {
						layout_card_title.setVisibility(View.GONE);
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
					if (object.has("status")&&!object.isNull("status")) {
						status = object.getInt("status");
					}
					if (object.has("couponEnable")&&!object.isNull("couponEnable")) {
						couponEnable = object.getBoolean("couponEnable");
					}
					if (object.has("areaId")&&!object.isNull("areaId")) {
						areaId = object.getInt("areaId");
					}
					if (object.has("shopId")&&!object.isNull("shopId")) {
						shopId = object.getInt("shopId");
					}
					if (object.has("mypetId")&&!object.isNull("mypetId")) {
						mulPetService.petCustomerId=object.getInt("mypetId");
					}
					int isUnitedPay = -1;
					if (object.has("isUnitedPay")&&!object.isNull("isUnitedPay")) {
						isUnitedPay = object.getInt("isUnitedPay");
					}
					if (object.has("appointment")&&!object.isNull("appointment")) {
						String appointment = object.getString("appointment");
						date = Utils.ChangeTime(appointment);
						if (type==3) {
							String time [] = appointment.split(" ");
							String AmOrPm="";
							if (time[1].contains("10")) {
								AmOrPm="上午";
							}else if (time[1].contains("14")) {
								AmOrPm="下午";
							}
							textView_yuyue_time.setText(time[0]+" "+AmOrPm);
//							textView_yuyue_time.setText(appointment.split(" ")[0]);
							image_change_custome.setVisibility(View.GONE);
							layout_people.setOnClickListener(null);
						}else {
							textView_yuyue_time.setText(date);
						}
					}
					
					if (object.has("orderNum")&&!object.isNull("orderNum")) {
						String orderNum = object.getString("orderNum");
						textView_order_num.setText(orderNum);
					}
					if (object.has("created")&&!object.isNull("created")) {
						String created = object.getString("created");
						textView_order_time.setText(created);
					}
					//倒计时单独抽出去，，影响自动提示，不知道为什么
					TimeCountDown(object);
					if (object.has("petService")&&!object.isNull("petService")) {
						petService = object.getInt("petService");
						mulPetService.serviceId = petService;
					}
					StringBuilder buildertTitle = new StringBuilder();
					if(object.has("pet")&&!object.isNull("pet")){
						JSONObject  object2 = object.getJSONObject("pet");
						if (object2.has("petName")&&!object2.isNull("petName")) {
							petName = object2.getString("petName");
							buildertTitle.append(petName);
						}
						if (object2.has("PetId")&&!object2.isNull("PetId")) {
							PetId = object2.getInt("PetId");
						}
						if (object2.has("petKind")&&!object2.isNull("petKind")) {
							mulPetService.petKind=object2.getInt("petKind");
						}
						mulPetService.petId=PetId;
						mulPetService.petName=petName;
						
					}
					if (object.has("serviceName")&&!object.isNull("serviceName")) {
						mulPetService.serviceName = object.getString("serviceName");
					}
					boolean isOneShow = false;
					if (object.has("extraServiceItems")&&!object.isNull("extraServiceItems")) {
//						JSONArray arrayIds = object.getJSONArray("extraServiceItems");
//						if(arrayIds.length()>0){
//							StringBuffer sb = new StringBuffer();
//							for (int i = 0; i < arrayIds.length(); i++) {
//								JSONObject objectIds = arrayIds.getJSONObject(i);
//								sb.append(objectIds.getInt("ExtraServiceItemId")+",");
//							}
//							mulPetService.addServiceIds = sb.substring(0, sb.toString().length()-1);
//							
//						}
						isOneShow = true;
					}
					StringBuilder builderRoom = new StringBuilder();//寄养房型
					if (object.has("roomType")&&!object.isNull("roomType")) {
						JSONObject objectRoomType = object.getJSONObject("roomType");
						if (objectRoomType.has("name")&&!objectRoomType.isNull("name")) {
//							buildertTitle.append(objectRoomType.getString("name"));
							builderRoom.append(objectRoomType.getString("name")+"  ");
//							textview_foster_home_style.setText(objectRoomType.getString("name"));
						}
						if (objectRoomType.has("RoomTypeId")&&!objectRoomType.isNull("RoomTypeId")) {
							mulPetService.serviceId=objectRoomType.getInt("RoomTypeId");
							roomType = objectRoomType.getInt("RoomTypeId");
						}
					}
					buildertTitle.append("寄养套餐");
					String nickName = "";
					if (object.has("myPet")&&!object.isNull("myPet")) {//寄养类型套餐
						JSONObject objectMyPet = object.getJSONObject("myPet");
						if (objectMyPet.has("nickName")&&!objectMyPet.isNull("nickName")) {
							nickName = objectMyPet.getString("nickName");
							mulPetService.petCustomerName = nickName;
						}
					}
					if (nickName.equals("")) {
						textView_foster_name.setText(buildertTitle);
					}else {
						textView_foster_name.setText(nickName+"寄养套餐");
					}
					if (object.has("room")&&!object.isNull("room")) {
						JSONObject objectRoom = object.getJSONObject("room");
						if (objectRoom.has("num")&&!objectRoom.isNull("num")) {
							builderRoom.append(objectRoom.getString("num"));
						}
					}
					if (object.has("roomPrice")&&!object.isNull("roomPrice")) {
						JSONObject objectPrice = object.getJSONObject("roomPrice");
						if (objectPrice.has("price")&&!objectPrice.isNull("price")) {
							double roomPrice = objectPrice.getDouble("price");
							builderRoom.append(" ¥"+roomPrice+"/天");
						}
						if (objectPrice.has("cameraPrice")&&!objectPrice.isNull("cameraPrice")) {
							cameraPrice = objectPrice.getDouble("cameraPrice");
							layout_foster_shexaingtou.setVisibility(View.VISIBLE);
							if (object.has("ndays")&&!object.isNull("ndays")) {
								textview_shexiangtou_price_and_days.setText(Utils.getTextAndColorCommentsBeau("#FAA04A", "¥ "+Utils.formatDouble(cameraPrice), "#666666", " X "+object.getInt("ndays")+"天"));
							}
						}else {
							layout_foster_shexaingtou.setVisibility(View.GONE);
						}
					}else {
						layout_foster_shexaingtou.setVisibility(View.GONE);
					}
					textview_foster_home_style.setText(builderRoom);
					
					if (object.has("customerId")&&!object.isNull("customerId")) {
						customerId = object.getInt("customerId");
					}
					StringBuilder customerBuilder = new StringBuilder();//寄养联系人Builder
					if (object.has("customerName")&&!object.isNull("customerName")) {
						customerName = object.getString("customerName");
						textView_people_name.setText(customerName);
						customerBuilder.append(customerName+" ");
						textView_people_name.setVisibility(View.VISIBLE);
					}else{
						textView_people_name.setVisibility(View.GONE);
					}
					if (object.has("customerMobile")&&!object.isNull("customerMobile")) {
						customerMobile = object.getString("customerMobile");
						textView_people_phone.setText(customerMobile);
						customerBuilder.append(customerMobile);
					}
					textview_foster_phone_people.setText(customerBuilder);//寄养联系人
					if(object.has("address")&&!object.isNull("address")){
						address = object.getString("address");
						textView_address_detail.setText(address);
						textview_foster_pet_address.setText(address);
					}
					if(object.has("addressId")&&!object.isNull("addressId")){
						addressid = object.getInt("addressId");
					}
					if(object.has("lat")&&!object.isNull("lat")){
						lat = object.getDouble("lat");
					}
					if(object.has("lng")&&!object.isNull("lng")){
						lng = object.getDouble("lng");
					}
					if (object.has("remark")&&!object.isNull("remark")) {
						String remark = object.getString("remark");
						if("".equals(remark.trim())){
							textView_remark_detail.setText("无");
							textView_foster_Remarks.setText("无");
						}else{
							textView_remark_detail.setText(remark);
							textView_foster_Remarks.setText(remark);
						}
					}else{
						textView_remark_detail.setText("无");
						textView_foster_Remarks.setText("无");
					}
					if(object.has("petServicePojo")&&!object.isNull("petServicePojo")){
						JSONObject petServicePojo = object.getJSONObject("petServicePojo");
						if (petServicePojo.has("name")&&!petServicePojo.isNull("name")) {
							name = petServicePojo.getString("name");
							if (!nickName.equals("")) {
								more_pet_name_one.setText(nickName+name);
							}else {
								more_pet_name_one.setText(petName+name);
							}
							if (isUnitedPay==0) {
								mulPetService.serviceName=name;
							}
						}
						if (petServicePojo.has("PetServicePojoId")&&!petServicePojo.isNull("PetServicePojoId")) {
							petService = petServicePojo.getInt("PetServicePojoId");
							
						}
						if (petServicePojo.has("serviceType")&&!petServicePojo.isNull("serviceType")) {
							mulPetService.serviceType=petServicePojo.getInt("serviceType");
						}
						
					}
					
					//单宠物头条宠物套餐名
					if (isUnitedPay==1) {
						if (object.has("serviceName")&&!object.isNull("serviceName")) {
							if (!nickName.equals("")) {
								more_pet_name_one.setText(nickName+object.getString("serviceName"));
							}else {
								more_pet_name_one.setText(petName+object.getString("serviceName"));
							}
						}
					}
					if (type==3) {
						if (!nickName.equals("")) {
							more_pet_name_one.setText(nickName+"游泳");
						}else {
							more_pet_name_one.setText(petName+"游泳");
						}
					}
					if (object.has("totalPrice")&&!object.isNull("totalPrice")) {//单只去付款价格执行此处
						double totalPrice = object.getDouble("totalPrice");
						totalMoney = totalPrice;
						AllTotalMoney = totalPrice;
						if(couponAmount >= totalMoney){
							needMoney = 0;
//							rlPayway.setVisibility(View.GONE);
							paytype = 3;
						}else{
//							rlPayway.setVisibility(View.VISIBLE);
							needMoney = totalMoney - couponAmount;
//							paytype = oldpayway;
						}
//						tvPrice.setText("¥ "+totalPrice);
						more_pet_price_one.setText("¥ "+Utils.formatDouble(totalPrice,2));
						textView_foster_name_price.setText("¥ "+Utils.formatDouble(totalPrice,2));
					}
					if (object.has("totalTotalPrice")&&!object.isNull("totalTotalPrice")) {//多只去付款价格执行
						totalMoney = object.getDouble("totalTotalPrice");
						AllTotalMoney = totalMoney;
						if (couponAmount>=totalMoney) {
							needMoney = 0;
							paytype=3;
						}else {
							needMoney = totalMoney - couponAmount;
						}
					}
					
					setLastPrice();
					//寄养费用
					if (object.has("basicPrice")&&!object.isNull("basicPrice")) {
						baseprice = Utils.formatDouble(object.getDouble("basicPrice"),2);
						if (object.has("cardId")&&!object.isNull("cardId")) {
							int cardId = object.getInt("cardId");
							if (cardId>0) {
								totalMoney-=baseprice;
								CutCardMoney+=baseprice;
							}
						}
						oneList.add("基础服务"+","+"¥ "+baseprice);
						fosterList.add("寄养费用"+","+"¥ "+baseprice);
					}
					
					if (object.has("cardId")&&!object.isNull("cardId")) {
						int cardId = object.getInt("cardId");
						if (cardId>0) {
							listIds.add(cardId);
						}
					}
					/**
					 * 新增入住洗澡
					 * start 2016年3月3日13:55:55 
					 */
					if (object.has("bathRequired")&&!object.isNull("bathRequired")) {
						bathRequired = object.getInt("bathRequired");
					}
					if (object.has("bathPrice")&&!object.isNull("bathPrice")) {
						double bathPrice = Utils.formatDouble(object.getDouble("bathPrice"),2);
						if (bathRequired!=-1) {//支持入住洗澡 
							if (bathPrice>=0) {
								fosterList.add("离店洗澡"+","+"¥ "+bathPrice);
								textview_foster_wash_detail.setText("需要洗澡");
							}else {
								textview_foster_wash_detail.setText("不需要洗澡");
							}
						}else if(bathRequired==-1){//不支持洗澡
							layout_foster_is_wash.setVisibility(View.GONE);
						}
					}else {
						if (bathRequired!=-1){
							textview_foster_wash_detail.setText("不需要洗澡");
						}else if (bathRequired==-1) {
							layout_foster_is_wash.setVisibility(View.GONE);
						}
					}
					// end 2016年3月3日13:56:08 
					
					//宠物粮食
					double foodTotalPrice;
					if (object.has("foodTotalPrice")&&!object.isNull("foodTotalPrice")) {
						foodTotalPrice = object.getDouble("foodTotalPrice");
						if (foodTotalPrice>0) {
//							--- start设置套餐下宠粮费用
							fosterList.add("宠粮费用"+","+"¥ "+foodTotalPrice);
							mulPetService.addservicefee = Utils.formatDouble(foodTotalPrice,2);
//							--- end设置套餐下宠粮费用
							
//							--- start  有宠粮总价 区单价 设置
							if (object.has("foodPrice")&&!object.isNull("foodPrice")) {
								textview_pet_food_detail.setText("宠物家提供  ￥"+object.getDouble("foodPrice")+"/天");
							}
//							--- end   有宠粮总价 区单价 设置
						}else {
							textview_pet_food_detail.setText("自带宠粮或到店购买");
							foodTotalPrice = -100;
						}
					}else{
						textview_pet_food_detail.setText("自带宠粮或到店购买");
						foodTotalPrice = -100;
					}
					int pickUpDown = -1;
					if (object.has("pickUp")&&!object.isNull("pickUp")) {
						pickUpDown = object.getInt("pickUp");
					}
					//接送费用
					if (object.has("pickupPrice")&&!object.isNull("pickupPrice")) {
						pickupprice = Utils.formatDouble(object.getDouble("pickupPrice"),2);
						if (pickupprice>0) {
							fosterList.add("接送费用"+","+"¥ "+pickupprice);
							if (pickUpDown==1) {
								textview_more_pet_pickprice.setText("¥ "+pickupprice);
							}
						}else if(pickupprice==0) {
							if (pickUpDown==1) {
								textview_more_pet_pickprice.setText("免费");
							}
						}else {
							pickupprice=-100;
						}
					}else{
						pickupprice=-100;
					}
					//type
					double totalPickUpPrice = -100;
					if (type==1) {//洗澡美容设置总的接送价格
						if (object.has("totalPickUpPrice")&&!object.isNull("totalPickUpPrice")) {
							totalPickUpPrice = Utils.formatDouble(object.getDouble("totalPickUpPrice"),2);
							if (totalPickUpPrice>0) {
								if (pickUpDown==1) {
									textview_more_pet_pickprice.setText("¥ "+totalPickUpPrice);
								}
							}else if (totalPickUpPrice==0) {
								if (pickUpDown==1) {
									textview_more_pet_pickprice.setText("免费");
								}
							}else {
								totalPickUpPrice=-100;
							}
						}else {
							totalPickUpPrice=-100;
						}
					}
					//接送服务设置界面
					if(pickupprice==-100&&foodTotalPrice==-100){
						layout_order_foster.setVisibility(View.GONE);
					}
					//接送价格集体不存在底部接送条目隐藏   不需要接送和不在接送范围隐藏该条目
					if (pickUpDown==0||pickUpDown==2) {
						rlOrderPikcupPriceShow.setVisibility(View.GONE);
						view_pick_down_line.setVisibility(View.GONE);
					}
					if (pickupprice==-100&&totalPickUpPrice==-100) {
						rlOrderPikcupPriceShow.setVisibility(View.GONE);
						view_pick_down_line.setVisibility(View.GONE);
					}
					
					if (object.has("task")&&!object.isNull("task")) {
						JSONObject objectTask = object.getJSONObject("task");
						if (objectTask.has("startTime")&&!objectTask.isNull("startTime")) {
							textview_foster_come_in_time.setText(Utils.ChangeTime(objectTask.getString("startTime")));
						}
						if (objectTask.has("stopTime")&&!objectTask.isNull("stopTime")) {
							endTime = objectTask.getString("stopTime");
							if (object.has("ndays")&&!object.isNull("ndays")) {
								textview_foster_go_out_time.setText(Utils.ChangeTime(objectTask.getString("stopTime")+" 共"+object.getInt("ndays")+"天"));
							}
						}
						if (objectTask.has("serviceId")&&!objectTask.isNull("serviceId")) {
							mulPetService.serviceId=objectTask.getInt("serviceId");
						}
					}
					if (object.has("extraItemPrice")&&!object.isNull("extraItemPrice")) {
						addserviceprice = Utils.formatDouble(object.getDouble("extraItemPrice"),2);//这个洗美特色服务作为单项费用 如果是寄养的话 作为摄像头费用
						oneList.add("单项服务"+","+"¥ "+addserviceprice);
						mulPetService.addservicefee = addserviceprice;
					}
					if (object.has("serviceType")&&!object.isNull("serviceType")) {
						mulPetService.serviceType=object.getInt("serviceType");
					}
					if (object.has("orderFee")&&!object.isNull("orderFee")) {
						mulPetService.orderFee=object.getInt("orderFee");
						orderFee = object.getInt("orderFee");
					}
					mulPetServiceList.add(mulPetService);
//					if(pickupprice>0&&addserviceprice>0){
//						llFeeItems.setVisibility(View.VISIBLE);
//						llFeeThird.setVisibility(View.VISIBLE);
//						tvFeeBase.setText("￥"+baseprice);
//						tvFeeTwoPre.setText("单项服务");
//						tvFeeAddService.setText("￥"+addserviceprice);
//						tvFeePickUp.setText("￥"+pickupprice);
						
//					}else if(pickupprice<=0&&addserviceprice>0){
//						llFeeItems.setVisibility(View.VISIBLE);
//						llFeeThird.setVisibility(View.GONE);
//						tvFeeBase.setText("￥"+baseprice);
//						tvFeeTwoPre.setText("单项服务");
//						tvFeeAddService.setText("￥"+addserviceprice);
//					}else if(pickupprice>0&&addserviceprice<=0){
//						llFeeItems.setVisibility(View.VISIBLE);
//						llFeeThird.setVisibility(View.GONE);
//						tvFeeBase.setText("￥"+baseprice);
//						tvFeeTwoPre.setText("上门接送");
//						tvFeeAddService.setText("￥"+pickupprice);
//					}else{
//						llFeeItems.setVisibility(View.GONE);
//						llFeeThird.setVisibility(View.GONE);
//					}

					if(object.has("worker")&&!object.isNull("worker")){
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
								String levelName = workLever.getString("name");
								tvBeauticianLevel.setText(levelName);
							}
						}
						if (workerObject.has("avatar")&&!workerObject.isNull("avatar")) {
							String avatar = workerObject.getString("avatar");
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
						if (workerObject.has("WorkerId")&&!workerObject.isNull("WorkerId")) {
							workerid = workerObject.getInt("WorkerId");
						}
						if (workerObject.has("totalOrderAmount")&&!workerObject.isNull("totalOrderAmount")) {
							int totalOrderAmount = workerObject.getInt("totalOrderAmount");
							tvBeauticianOrdernum.setText("接单："+totalOrderAmount);
//							tvBeauticianLevel.setText(Utils.getTextAndColorComments("", "接单："+totalOrderAmount));

						}
						if (workerObject.has("title")&&!workerObject.isNull("title")) {
							String title = workerObject.getString("title");
//							tvBeauticianLevel.setText(title);
						}
						if(workerObject.has("realName")&&!workerObject.isNull("realName")){
							String realName = workerObject.getString("realName");
							tvBeauticianName.setText(realName);
						}
						
					}
					if (object.has("serviceLoc")&&!object.isNull("serviceLoc")) {
						serviceloc = object.getInt("serviceLoc");
						tvAddrtype.setVisibility(View.VISIBLE);
						if(serviceloc==1){
							textViewAddrType.setText("门店服务");
							tvAddrtype.setText("门店");
							tvAddrtype.setBackgroundResource(R.drawable.bg_red_round);
							textViewAddrTypeDes.setVisibility(View.VISIBLE);
							if(object.has("pickUp")&&!object.isNull("pickUp")){
								pickup = object.getInt("pickUp");
								if(pickup==1){
									layout_addrtype.setVisibility(View.VISIBLE);
									textViewAddrTypeDes.setText("(需要接送)");
								}else/* if(pickup==0)*/{
									layout_addrtype.setVisibility(View.VISIBLE);
									textViewAddrTypeDes.setText("(不接送)");
								}
//								else if (pickup==2) {
//									layout_addrtype.setVisibility(View.GONE);
//								}
							}else{
//								layout_addrtype.setVisibility(View.GONE);
								textViewAddrTypeDes.setText("(不接送)");
							}
							
							llPetStore.setVisibility(View.VISIBLE);
							
						}else{
							llPetStore.setVisibility(View.GONE);
							textViewAddrTypeDes.setVisibility(View.GONE);
							textViewAddrType.setText("上门服务");
							tvAddrtype.setText("上门");
							tvAddrtype.setBackgroundResource(R.drawable.bg_blue_round);
						}
						
					}
					//寄养是否接送
					if (object.has("pickUp")&&!object.isNull("pickUp")) {
						int pickUp = object.getInt("pickUp");
						if (pickUp==1) {
							textview_foster_pickup_detail.setText("需要接送");
						}
//						else if (pickUp==2) {//不再接送范围内
//							layout_foster_is_pickup.setVisibility(View.GONE);
//						}
						else/* if (pickUp==0)*/ {
							textview_foster_pickup_detail.setText("不接送");
						}
					}else {
						layout_foster_is_pickup.setVisibility(View.GONE);
					}
					if (object.has("shop")&&!object.isNull("shop")) {
						JSONObject jshop = object.getJSONObject("shop");
						if(jshop.has("shopName")&&!jshop.isNull("shopName")){
							tvPetStoreName.setText(jshop.getString("shopName"));
							tv_petstore_name.setText(jshop.getString("shopName"));
						}
						if(jshop.has("address")&&!jshop.isNull("address")){
							ShopAddress = jshop.getString("address");
							tvPetStoreAddr.setText(jshop.getString("address"));
							tv_petstore_addr.setText(jshop.getString("address"));
						}
						if(jshop.has("phone")&&!jshop.isNull("phone")){
							tvPetStorePhone.setText(jshop.getString("phone"));
							tv_petstore_phone.setText(jshop.getString("phone"));
						}
						if (jshop.has("hotelName")&&!jshop.isNull("hotelName")) {
							textview_foster_home_address.setText(jshop.getString("hotelName"));
							if (type==2) {
								textview_foster_home_address_title.setText("寄养中心  ");
								textview_foster_home_address.setText(jshop.getString("hotelName"));
							}
						}
						if (jshop.has("transferName")&&!jshop.isNull("transferName")) {
							textview_foster_home_address_title.setText("中  转  站  ");
							textview_foster_home_address.setText(jshop.getString("transferName"));
						}
						if(jshop.has("img")&&!jshop.isNull("img")){
							if (type==1||type==3) {
								//洗澡美容图片
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
							}else if(type==2){
								//寄养图片
								ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+jshop.getString("img"),
										sriv_petstore_image, 0,new ImageLoadingListener() {
									
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
					}
					if (type==3) {//单独游泳取原价
						if (object.has("listPrice")&&!object.isNull("listPrice")) {
							double listPrice = object.getDouble("listPrice");
							onePetTotalMoney = listPrice;
//							if (object.has("memberSwimNotice")&&!object.isNull("memberSwimNotice")) {
//								totalMoney = totalMoney /2;
//							}
//							if (couponAmount>=totalMoney) {
//								needMoney = 0;
//								paytype=3;
//							}else {
//								needMoney = totalMoney - couponAmount;
//							}
							more_pet_price_one.setText("¥"+Utils.formatDouble(onePetTotalMoney,2));
						}
					}
					if (object.has("multiPet")&&!object.isNull("multiPet")) {
						JSONArray array = object.getJSONArray("multiPet");
						if (isUnitedPay==1) {
							showPetNum(array.length());
							if (array.length()==1) {
								setTwoLayout(array);
							}else if (array.length()==2) {
								setTwoLayout(array);
								setThrlayout(array);
							}else if (array.length()==3) {
								setTwoLayout(array);
								setThrlayout(array);
								setFourLayout(array);
							}else if (array.length()==4) {
								setTwoLayout(array);
								setThrlayout(array);
								setFourLayout(array);
								setFiveLayout(array);
							}
						}	
						for (int i = 0; i < array.length(); i++) {
							MulPetService petServiceMul = new MulPetService();
							JSONObject objectMul = array.getJSONObject(i);
							if (objectMul.has("mypetId")&&!objectMul.isNull("mypetId")) {
								petServiceMul.petCustomerId = objectMul.getInt("mypetId");
							}
							if (objectMul.has("petKind")&&!objectMul.isNull("petKind")) {
								petServiceMul.petKind=objectMul.getInt("petKind");
							}
							if (objectMul.has("petId")&&!objectMul.isNull("petId")) {
								petServiceMul.petId=objectMul.getInt("petId");
							}
							if (objectMul.has("petName")&&!objectMul.isNull("petName")) {
								petServiceMul.petName=objectMul.getString("petName");
							}
							if (objectMul.has("serviceName")&&!objectMul.isNull("serviceName")) {
								petServiceMul.serviceName = objectMul.getString("serviceName");
							}
							if (objectMul.has("extraItemPrice")&&!objectMul.isNull("extraItemPrice")) {
								petServiceMul.addservicefee = Utils.formatDouble(objectMul.getDouble("extraItemPrice"),2);
							}
							if (objectMul.has("petService")&&!objectMul.isNull("petService")) {
								petServiceMul.serviceId=objectMul.getInt("petService");
							}
							if (objectMul.has("serviceType")&&!objectMul.isNull("serviceType")) {
								petServiceMul.serviceType=objectMul.getInt("serviceType");
							}
							if (objectMul.has("orderFee")&&!objectMul.isNull("orderFee")) {
								petServiceMul.orderFee=objectMul.getInt("orderFee");
							}
							if (objectMul.has("cardId")&&!objectMul.isNull("cardId")) {
								int cardId = objectMul.getInt("cardId");
								if (cardId>0) {
									isHasCard = true;
									listIds.add(cardId);
								}
							}
							mulPetServiceList.add(petServiceMul);
						}
					}else {
						more_pet_one_view.setVisibility(View.GONE);
					}
					
					if (type==1) {//洗澡美容
						if (oneList.size()>1) {
							if (isOneShow) {
								WaitToPayShowItemOne = new WaitToPayShowItem(act,oneList,1);
								gridView_more_pet_one.setAdapter(WaitToPayShowItemOne);
							}
						}
					}else if (type==2) {//寄养
						if (fosterList.size()>1) {//如果只包含寄养费用 则 当前模块隐藏 --产品后期说是要添加多个类似条目判断麻烦直接用控件处理
							payShowItem = new WaitToPayShowItem(act, fosterList);
							gridView_foster_show.setAdapter(payShowItem);
						}
					}
					if (object.has("petService")&&!object.isNull("petService")) {
						int petService = object.getInt("petService");
						if (petService==300) {
							Rlayout_show_beau.setVisibility(View.GONE);
						}
					}
					if (type==3) {
						Rlayout_show_beau.setVisibility(View.GONE);
						textView_address.setText("门店地址：");
						textView_address_detail.setText(ShopAddress);
					}
					if (object.has("orderSource")&&!object.isNull("orderSource")) {
						String orderSource = object.getString("orderSource");
						if (orderSource.equals("vie")) {
							isVie = true;
							if (serviceloc==1) {//到店
								Rlayout_show_beau.setVisibility(View.GONE);//beau
								llPetStore.setVisibility(View.VISIBLE);
							}else {
								Rlayout_show_beau.setVisibility(View.GONE);//beau
								llPetStore.setVisibility(View.GONE);
							}
						}
					}
					if (type==4) {//训练最后走一次
						//start 训练
						textview_show_pet_type.setText("宠物类型  ");
						textview_show_train_content.setText("训练内容  ");
						textview_train_time.setText("入训时间    ");
						textview_foster_home_address_title.setText("中  转  站     ");
						textview_train_address.setText("训练基地  ");
						layout_pet_address.setVisibility(View.GONE);
						view_line.setVisibility(View.VISIBLE);
//						textview_train_name_price.setText("训练费用");
						
						String petName="";
						if (object.has("pet")&&!object.isNull("pet")) {
							JSONObject objectPet = object.getJSONObject("pet");
							
							if (objectPet.has("petName")&&!objectPet.isNull("petName")) {
								petName = objectPet.getString("petName");
								textview_foster_come_in_time.setText(petName);
							}
							if (objectPet.has("PetId")&&!objectPet.isNull("PetId")) {
								PetId = objectPet.getInt("PetId");
							}
						}
						if (object.has("mypetId")&&!object.isNull("mypetId")) {
							customerId = object.getInt("mypetId");
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
							textView_foster_name.setText(petName+ServiceName);
						}else {
							textView_foster_name.setText(nickName+ServiceName);
						}
						if (object.has("appointment")&&!object.isNull("appointment")) {
							textview_foster_home_style.setText(object.getString("appointment").replace("00:00:00", "").trim());
						}
						if (object.has("totalPrice")&&!object.isNull("totalPrice")) {
							double totalPrice = object.getDouble("totalPrice");
							totalMoney = totalPrice;
							if(couponAmount >= totalMoney){
								needMoney = 0;
								paytype = 3;
							}else{
								needMoney = totalMoney - couponAmount;
							}
							more_pet_price_one.setText("¥ "+Utils.formatDouble(totalPrice,2));
							textView_foster_name_price.setText("¥ "+Utils.formatDouble(totalPrice,2));
						}
						setLastPrice();
						if (object.has("shop")&&!object.isNull("shop")) {
							JSONObject jshop = object.getJSONObject("shop");
//							if (jshop.has("hotelName")&&!jshop.isNull("hotelName")) {
//								textview_foster_home_address.setText(jshop.getString("hotelName"));
//								if (type==2) {
//									textview_foster_home_address_title.setText("寄养中心  ");
//									
//								}
//							}
							if(jshop.has("shopName")&&!jshop.isNull("shopName")){
								textview_pet_food_detail.setText(jshop.getString("shopName")+"测试");
							}
							if (jshop.has("transferName")&&!jshop.isNull("transferName")) {
								textview_foster_home_address_title.setText("中  转  站  ");
								textview_foster_home_address.setText(jshop.getString("transferName"));
							}else {
								layout_tarnser.setVisibility(View.GONE);
							}
						}else {//下边是测试 正式的删了
//							textview_pet_food_detail.setText("测试");
////							layout_tarnser.setVisibility(View.GONE);
//							textview_foster_home_address_title.setText("中  转  站  ");
//							textview_foster_home_address.setText("中  转  站  ");
						}
						//end 训练
						
					}
					if (type==2) {//寄养摄像头则显示摄像头底部line
						view_pick_down_line.setVisibility(View.VISIBLE);
					}
					if (type==3) {
						if (object.has("memberSwimNotice")&&!object.isNull("memberSwimNotice")) {
							String memberSwimNotice = object.getString("memberSwimNotice");
							textview_member_cutdown.setVisibility(View.VISIBLE);
							textview_member_cutdown.setText(memberSwimNotice);
							totalMoney = onePetTotalMoney + twoPetTotalMoney+thrPetTotalMoney+fourPetTotalMoney+fivePetTotalMoney;
							totalMoney = totalMoney / 2;
						}else {
							textview_member_cutdown.setVisibility(View.GONE);
						}
						if (couponAmount>=totalMoney) {
							needMoney = 0;
							paytype=3;
						}else {
							needMoney = totalMoney - couponAmount;
						}
					}
					
					if (object.has("extraServiceFeeTag")&&!object.isNull("extraServiceFeeTag")) {
						String extraServiceFeeTag = object.getString("extraServiceFeeTag");
						textview_go_home_service_tag.setText(extraServiceFeeTag);
					}
					if (object.has("extraServicePrice")&&!object.isNull("extraServicePrice")) {
						layout_go_home_service_price.setVisibility(View.VISIBLE);
						view_pick_down_line.setVisibility(View.VISIBLE);
						extraServicePrice = object.getDouble("extraServicePrice");
						textview_go_home_service_price.setText("¥"+extraServicePrice);
						if (extraServicePrice<=0) {
							extraServicePrice = 0;
							layout_go_home_service_price.setVisibility(View.GONE);
						}
					}else {
						layout_go_home_service_price.setVisibility(View.GONE);
					}
					if (object.has("extraReductionFeeTag")&&!object.isNull("extraReductionFeeTag")) {
						String reductionPriceTag = object.getString("extraReductionFeeTag");
						textview_manjianyouhui_left.setText(reductionPriceTag);
					}
					if (object.has("reductionPrice")&&!object.isNull("reductionPrice")) {
						layout_go_home_manjianyouhui.setVisibility(View.VISIBLE);
						reductionPrice = object.getDouble("reductionPrice");
						textview_manjianyouhui_right.setText("-¥"+Math.abs(reductionPrice)+"");
						if (reductionPrice<=0) {
							reductionPrice  = 0;
							layout_go_home_manjianyouhui.setVisibility(View.GONE);
						}
					}else {
						layout_go_home_manjianyouhui.setVisibility(View.GONE);
					}
					if (object.has("cardId")&&!object.isNull("cardId")) {
						int cardId = object.getInt("cardId");
						if (cardId>0) {
							isHasCard = true;
						}
					}
					if (isHasCard) {
						textview_old_price_bottom.setVisibility(View.VISIBLE);
						textview_old_price_andother.setText("总价¥"+(int)(AllTotalMoney+extraServicePrice)/*+"（优惠"+(couponAmount+Math.abs(reductionPrice))+")"*/);
						if (couponAmount+Math.abs(reductionPrice)>0) {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney+"  "+couponCutReductionTag+"¥"+(couponAmount+Math.abs(reductionPrice)));
						}else {
							textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney);
						}
					}else {
						textview_old_price_andother.setText("总价¥"+(int)(AllTotalMoney+extraServicePrice)/*+"（优惠"+(couponAmount+Math.abs(reductionPrice))+")"*/);
						if (couponAmount+Math.abs(reductionPrice)>0) {
							textview_old_price_bottom.setVisibility(View.VISIBLE);
							textview_old_price_bottom.setText(couponCutReductionTag+"¥"+(couponAmount+Math.abs(reductionPrice)));
						}else {
							textview_old_price_bottom.setVisibility(View.GONE);
						}
					}
					needMoney = totalMoney;
					setLastPrice();
					if (needMoney>0) {
						getCouponNeedMoney = needMoney;
						getCoupon();
					}else {
						needMoney = 0;
						paytype=7;
					}
				}else{
					showMain(false);
					if(jsonObject.has("msg")&&jsonObject.isNull("msg")){
						String msg = jsonObject.getString("msg");
						ToastUtil.showToastShort(OrderDetailFromOrderActivity.this,msg);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showMain(false);
			}
		}

		private void setFiveLayout(JSONArray array) throws JSONException {
			//3
			JSONObject objectMulFive = array.getJSONObject(3);
			String petNameFive ="";
			String serviceNameFive ="";
			String nickNameFive="";
			if (objectMulFive.has("petName")&&!objectMulFive.isNull("petName")) {
				petNameFive = objectMulFive.getString("petName");
			}
			if (objectMulFive.has("myPet")&&!objectMulFive.isNull("myPet")) {
				JSONObject objectFive = objectMulFive.getJSONObject("myPet");
				if (objectFive.has("nickName")&&!objectFive.isNull("nickName")) {
					nickNameFive = objectFive.getString("nickName");
				}
			}
			if (objectMulFive.has("serviceName")&&!objectMulFive.isNull("serviceName")) {
				serviceNameFive = objectMulFive.getString("serviceName");
			}
			double payPriceFive = 0 ;
			double basicPriceFive = 0 ;
			double extraItemPriceFive = 0 ;
			if (objectMulFive.has("totalPrice")&&!objectMulFive.isNull("totalPrice")) {
				payPriceFive = Utils.formatDouble(objectMulFive.getDouble("totalPrice"),2);
			}
			if (objectMulFive.has("basicPrice")&&!objectMulFive.isNull("basicPrice")) {
				basicPriceFive = Utils.formatDouble(objectMulFive.getDouble("basicPrice"),2);
				if (objectMulFive.has("cardId")&&!objectMulFive.isNull("cardId")) {
					int cardId = objectMulFive.getInt("cardId");
					if (cardId>0) {
						totalMoney-=basicPriceFive;
						CutCardMoney+=basicPriceFive;
					}
					
				}
			}
			if (objectMulFive.has("extraItemPrice")&&!objectMulFive.isNull("extraItemPrice")) {
				extraItemPriceFive = Utils.formatDouble(objectMulFive.getDouble("extraItemPrice"),2);
			}
			boolean isShowFive = false;
			if (objectMulFive.has("extraServiceItems")&&!objectMulFive.isNull("extraServiceItems")) {
				isShowFive = true;
			}
			String strTotalPriceFive = "¥ "+payPriceFive;
			fiveList.add("基础服务"+","+"¥ "+basicPriceFive);
			fiveList.add("单项服务"+","+"¥ "+extraItemPriceFive);
			if (nickNameFive.equals("")) {
				if (type==3) {
					more_pet_name_five.setText(petNameFive+"游泳");
				}else {
					more_pet_name_five.setText(petNameFive+serviceNameFive);
				}
			}else {
				if (type==3) {
					more_pet_name_five.setText(nickNameFive+"游泳");
				}else {
					more_pet_name_five.setText(nickNameFive+serviceNameFive);
				}
			}
			more_pet_price_five.setText(strTotalPriceFive);
			if (fiveList.size()>1) {
				if (isShowFive) {
					WaitToPayShowItemFive = new WaitToPayShowItem(act, fiveList,1);
					gridView_more_pet_five.setAdapter(WaitToPayShowItemFive);
				}
			}
			if (type==3) {
				if (objectMulFive.has("listPrice")&&!objectMulFive.isNull("listPrice")) {
					fivePetTotalMoney = objectMulFive.getDouble("listPrice");
					more_pet_price_five.setText("¥"+fivePetTotalMoney);
				}
			}
		}

		private void setFourLayout(JSONArray array) throws JSONException {
			//3
			JSONObject objectMulFour = array.getJSONObject(2);
			String petNameFour ="";
			String serviceNameFour ="";
			String nickNameFour="";
			if (objectMulFour.has("petName")&&!objectMulFour.isNull("petName")) {
				petNameFour = objectMulFour.getString("petName");
			}
			if (objectMulFour.has("myPet")&&!objectMulFour.isNull("myPet")) {
				JSONObject objectFour = objectMulFour.getJSONObject("myPet");
				if (objectFour.has("nickName")&&!objectFour.isNull("nickName")) {
					nickNameFour = objectFour.getString("nickName");
				}
			}
			if (objectMulFour.has("serviceName")&&!objectMulFour.isNull("serviceName")) {
				serviceNameFour = objectMulFour.getString("serviceName");
			}
			double payPriceFour = 0 ;
			double basicPriceFour = 0 ;
			double extraItemPriceFour = 0 ;
			if (objectMulFour.has("totalPrice")&&!objectMulFour.isNull("totalPrice")) {
				payPriceFour = Utils.formatDouble(objectMulFour.getDouble("totalPrice"),2);
			}
			if (objectMulFour.has("basicPrice")&&!objectMulFour.isNull("basicPrice")) {
				basicPriceFour = Utils.formatDouble(objectMulFour.getDouble("basicPrice"),2);
				if (objectMulFour.has("cardId")&&!objectMulFour.isNull("cardId")) {
					int cardId = objectMulFour.getInt("cardId");
					if (cardId>0) {
						totalMoney-=basicPriceFour;
						CutCardMoney+=basicPriceFour;
					}
				}
			}
			if (objectMulFour.has("extraItemPrice")&&!objectMulFour.isNull("extraItemPrice")) {
				extraItemPriceFour = Utils.formatDouble(objectMulFour.getDouble("extraItemPrice"),2);
			}
			boolean isShowFour = false;
			if (objectMulFour.has("extraServiceItems")&&!objectMulFour.isNull("extraServiceItems")) {
				isShowFour = true;
			}
			String strTotalPriceFour = "¥ "+payPriceFour;
			fourList.add("基础服务"+","+"¥ "+basicPriceFour);
			fourList.add("单项服务"+","+"¥ "+extraItemPriceFour);
			if (nickNameFour.equals("")) {
				if (type==3) {
					more_pet_name_four.setText(petNameFour+"游泳");
				}else {
					more_pet_name_four.setText(petNameFour+serviceNameFour);
				}
			}else {
				if (type==3) {
					more_pet_name_four.setText(nickNameFour+"游泳");
				}else {
					more_pet_name_four.setText(nickNameFour+serviceNameFour);
				}
			}
			more_pet_price_four.setText(strTotalPriceFour);
			if (fourList.size()>1) {
				if (isShowFour) {
					WaitToPayShowItemFour = new WaitToPayShowItem(act, fourList,1);
					gridView_more_pet_four.setAdapter(WaitToPayShowItemFour);
				}
			}
			if (type==3) {
				if (objectMulFour.has("listPrice")&&!objectMulFour.isNull("listPrice")) {
					fourPetTotalMoney = objectMulFour.getDouble("listPrice");
					more_pet_price_four.setText("¥"+fourPetTotalMoney);
				}
			}
		}

		private void setThrlayout(JSONArray array) throws JSONException {
			//2
			JSONObject objectMulThr = array.getJSONObject(1);
			String petNameThr = "";
			String serviceNameThr = "";
			String nickNameThr="";
			if (objectMulThr.has("petName")&&!objectMulThr.isNull("petName")) {
				petNameThr = objectMulThr.getString("petName");
			}
			if (objectMulThr.has("myPet")&&!objectMulThr.isNull("myPet")) {
				JSONObject objectThr = objectMulThr.getJSONObject("myPet");
				if (objectThr.has("nickName")&&!objectThr.isNull("nickName")) {
					nickNameThr = objectThr.getString("nickName");
				}
			}
			if (objectMulThr.has("serviceName")&&!objectMulThr.isNull("serviceName")) {
				serviceNameThr = objectMulThr.getString("serviceName");
			}
			double payPriceThr = 0;
			double basicPriceThr = 0;
			double extraItemPriceThr = 0;
			if (objectMulThr.has("totalPrice")&&!objectMulThr.isNull("totalPrice")) {
				payPriceThr = Utils.formatDouble(objectMulThr.getDouble("totalPrice"),2);
			}
			if (objectMulThr.has("basicPrice")&&!objectMulThr.isNull("basicPrice")) {
				basicPriceThr = Utils.formatDouble(objectMulThr.getDouble("basicPrice"),2);
				if (objectMulThr.has("cardId")&&!objectMulThr.isNull("cardId")) {
					int cardId = objectMulThr.getInt("cardId");
					if (cardId>0) {
						totalMoney-=basicPriceThr;
						CutCardMoney+=basicPriceThr;
					}
					
				}
			}
			if (objectMulThr.has("extraItemPrice")&&!objectMulThr.isNull("extraItemPrice")) {
				extraItemPriceThr = Utils.formatDouble(objectMulThr.getDouble("extraItemPrice"),2);
			}
			boolean isShowThr = false;
			if (objectMulThr.has("extraServiceItems")&&!objectMulThr.isNull("extraServiceItems")) {
				isShowThr = true;
			}
			String strTotalPriceThr = "¥ "+payPriceThr;
			thrList.add("基础服务"+","+"¥ "+basicPriceThr);
			thrList.add("单项服务"+","+"¥ "+extraItemPriceThr);
			if (nickNameThr.equals("")) {
				if (type==3) {
					more_pet_name_thr.setText(petNameThr+"游泳");
				}else {
					more_pet_name_thr.setText(petNameThr+serviceNameThr);
				}
			}else {
				if (type==3) {
					more_pet_name_thr.setText(nickNameThr+"游泳");
				}else {
					more_pet_name_thr.setText(nickNameThr+serviceNameThr);
				}
			}
			more_pet_price_thr.setText(strTotalPriceThr);
			if (thrList.size()>1) {
				if (isShowThr) {
					WaitToPayShowItemThr = new WaitToPayShowItem(act, thrList,1);
					gridView_more_pet_thr.setAdapter(WaitToPayShowItemThr);
				}
			}
			if (type==3) {
				if (objectMulThr.has("listPrice")&&!objectMulThr.isNull("listPrice")) {
					thrPetTotalMoney = objectMulThr.getDouble("listPrice");
					more_pet_price_thr.setText("¥"+thrPetTotalMoney);
				}
			}
		}

		private void setTwoLayout(JSONArray array) throws JSONException {
			JSONObject objectMulTwo = array.getJSONObject(0);
			String petNameTwo="";
			String serviceNameTwo ="";
			String nickNameTwo = "";
			if (objectMulTwo.has("petName")&&!objectMulTwo.isNull("petName")) {
				petNameTwo = objectMulTwo.getString("petName");
			}
			if (objectMulTwo.has("myPet")&&!objectMulTwo.isNull("myPet")) {
				JSONObject objectTwo = objectMulTwo.getJSONObject("myPet");
				if (objectTwo.has("nickName")&&!objectTwo.isNull("nickName")) {
					nickNameTwo = objectTwo.getString("nickName");
				}
			}
			if (objectMulTwo.has("serviceName")&&!objectMulTwo.isNull("serviceName")) {
				serviceNameTwo = objectMulTwo.getString("serviceName");
			}
			double payPriceTwo = 0;
			double basicPriceTwo = 0;
			double extraItemPriceTwo = 0;
			if (objectMulTwo.has("totalPrice")&&!objectMulTwo.isNull("totalPrice")) {
				payPriceTwo = Utils.formatDouble(objectMulTwo.getDouble("totalPrice"),2);
			}
			if (objectMulTwo.has("basicPrice")&&!objectMulTwo.isNull("basicPrice")) {
				basicPriceTwo = Utils.formatDouble(objectMulTwo.getDouble("basicPrice"),2);
				if (objectMulTwo.has("cardId")&&!objectMulTwo.isNull("cardId")) {
					int cardId = objectMulTwo.getInt("cardId");
					if (cardId>0) {
						totalMoney-=basicPriceTwo;
						CutCardMoney+=basicPriceTwo;
					}
					
				}
			}
			if (objectMulTwo.has("extraItemPrice")&&!objectMulTwo.isNull("extraItemPrice")) {
				extraItemPriceTwo = Utils.formatDouble(objectMulTwo.getDouble("extraItemPrice"),2);
			}
			boolean isShowTwo = false;
			if (objectMulTwo.has("extraServiceItems")&&!objectMulTwo.isNull("extraServiceItems")) {
				isShowTwo = true;
			}
			String strTotalPriceTwo = "¥ "+payPriceTwo;
			twoList.add("基础服务"+","+"¥ "+basicPriceTwo);
			twoList.add("单项服务"+","+"¥ "+extraItemPriceTwo);
			if (nickNameTwo.equals("")) {
				if (type==3) {
					more_pet_name_two.setText(petNameTwo+"游泳");
				}else {
					more_pet_name_two.setText(petNameTwo+serviceNameTwo);
				}
			}else {
				if (type==3) {
					more_pet_name_two.setText(nickNameTwo+"游泳");
				}else {
					more_pet_name_two.setText(nickNameTwo+serviceNameTwo);
				}
			}
			more_pet_price_two.setText(strTotalPriceTwo);
			if (twoList.size()>1) {
				if (isShowTwo) {
					WaitToPayShowItemTwo = new WaitToPayShowItem(act, twoList,1);
					gridView_more_pet_two.setAdapter(WaitToPayShowItemTwo);
				}
			}
			if (type==3) {
				if (objectMulTwo.has("listPrice")&&!objectMulTwo.isNull("listPrice")) {
					twoPetTotalMoney = objectMulTwo.getDouble("listPrice");
					more_pet_price_two.setText("¥"+twoPetTotalMoney);
				}
			}
		}

		private void TimeCountDown(JSONObject object) throws JSONException {
			if (object.has("remainTime")&&!object.isNull("remainTime")) {
				lastOverTime = object.getLong("remainTime");
				timer = new Timer();
				task = new TimerTask() {
					
					@Override
					public void run() {
				        if (lastOverTime>0) {
				        	lastOverTime-=1000;
							Message msg = Message.obtain();
							msg.what=0;
							msg.arg1 = (int) lastOverTime;
							handler.sendMessage(msg);
						}else {
							if(timer!=null){
								handler.sendEmptyMessage(1);
								timer.cancel();
								timer=null;
							}
						}
					}
				};
				timer.schedule(task, 0, 1000);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			prsScrollView.onRefreshComplete();
			pDialog.closeDialog();
			showMain(false);
		}
		
	};
	//优惠券
	private AsyncHttpResponseHandler couponHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("可用的优惠券："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					
					JSONArray jData = jobj.getJSONArray("data");
					for(int i = 0; i < jData.length(); i++){
						JSONObject jcoupon = jData.getJSONObject(i);
						couponList.add(Coupon.jsonToEntity(jcoupon));
					}
					
					if(couponList.size() > 0){
						couponId = couponList.get(0).id;
						tvCoupon.setText("-"+couponList.get(0).amount+"("+couponList.get(0).name+")");
						String camount = couponList.get(0).amount == null || "".equals(couponList.get(0).amount)?"0":couponList.get(0).amount;
						couponAmount = Utils.formatDouble(Double.parseDouble(camount),2);
						tvCoupon.setTextColor(Color.parseColor("#D1494F"));
					}else {
						tvCoupon.setTextColor(Color.parseColor("#666666"));
					}
					
//					if(couponAmount >= totalMoney){
//						paytype = 3;
//						needMoney = 0;
//					}else{
//						needMoney = totalMoney - couponAmount;
//					}
					if (couponAmount+Math.abs(reductionPrice)>=totalMoney) {
						paytype = 3;
						needMoney = 0;
					}else {
						needMoney = totalMoney+extraServicePrice-Math.abs(reductionPrice)-couponAmount;
					}
					if (isHasCard) {//有次卡
						textview_old_price_andother.setText("总价¥"+(AllTotalMoney+extraServicePrice));
						textview_old_price_bottom.setVisibility(View.VISIBLE);
						if (getCouponNeedMoney>couponAmount) {
							if (couponAmount+Math.abs(reductionPrice)<=0) {//无优惠
								textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney);
							}else {//有优惠
								textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney+"  "+couponCutReductionTag+"¥"+(couponAmount+Math.abs(reductionPrice)));
							}
						}else {
							if (couponAmount+Math.abs(reductionPrice)<=0) {//无优惠
								textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney);
							}else {//有优惠
								textview_old_price_bottom.setText(CardTag+"¥"+CutCardMoney+"  "+couponCutReductionTag+"¥"+(getCouponNeedMoney+Math.abs(reductionPrice)));
							}
						}
						
					}else {//无次卡
					//	textview_old_price_andother.setText(""+(int)(AllTotalMoney+extraServicePrice)/*+"（优惠"+(couponAmount+Math.abs(reductionPrice))+")"*/);
						textview_old_price_andother.setText("总价¥"+(AllTotalMoney+extraServicePrice));
						if (getCouponNeedMoney>couponAmount) {
							if (couponAmount+Math.abs(reductionPrice)>0) {//有优惠
								textview_old_price_bottom.setVisibility(View.VISIBLE);
								textview_old_price_bottom.setText(couponCutReductionTag+"¥"+(couponAmount+Math.abs(reductionPrice)));
							}else {//无优惠
								textview_old_price_bottom.setVisibility(View.GONE);
							}
						}else {
							if (couponAmount+Math.abs(reductionPrice)>0) {//有优惠
								textview_old_price_bottom.setVisibility(View.VISIBLE);
								textview_old_price_bottom.setText(couponCutReductionTag+"¥"+(getCouponNeedMoney+Math.abs(reductionPrice)));
							}else {//无优惠
								textview_old_price_bottom.setVisibility(View.GONE);
							}
						}
						
					}
					setLastPrice();
				}
				setLastPrice();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			
		}
		
	};
	
	
	
	private void showSelectDialog() {
		Utils.mLogError("==-->type "+type);
		if (type==1) {
//			CommUtil.orderCareCancelRemindTxt(
//					spUtil.getString("cellphone", ""), 
//					Global.getIMEI(OrderDetailFromOrderActivity.this), 
//					OrderDetailFromOrderActivity.this,
//					OrderId,
//					1,
//					getCancleDetail);
			CommUtil.ReasonCancelOrder(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(OrderDetailFromOrderActivity.this), 
					OrderDetailFromOrderActivity.this, 
					OrderId,ReasonGit,cancelOrder);
		}else if(type==2) {
//			CommUtil.orderHotelCancelRemindTxt(
//					spUtil.getString("cellphone", ""), 
//					Global.getIMEI(OrderDetailFromOrderActivity.this), 
//					OrderDetailFromOrderActivity.this
//					,getCancleDetail);
			CommUtil.ReasonCancelOrder(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(OrderDetailFromOrderActivity.this), 
					OrderDetailFromOrderActivity.this, 
					OrderId,ReasonGit,cancelOrder);
		}else if (type==3) {
//			CommUtil.orderSwimCancelRemindTxt(
//					spUtil.getString("cellphone", ""), 
//					Global.getIMEI(OrderDetailFromOrderActivity.this), 
//					OrderDetailFromOrderActivity.this,
//					OrderId,
//					1,
//					getCancleDetail);
			CommUtil.ReasonCancelOrder(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(OrderDetailFromOrderActivity.this), 
					OrderDetailFromOrderActivity.this, 
					OrderId,ReasonGit,cancelOrder);
		}else if (type==4) {
			CommUtil.ReasonCancelOrder(
					spUtil.getString("cellphone", ""), 
					Global.getIMEI(OrderDetailFromOrderActivity.this), 
					OrderDetailFromOrderActivity.this, 
					OrderId,ReasonGit,cancelOrder);
		}
/*		if (type==2) {
			pWinChoose = null;
			if(pWinChoose==null){
				View view = mInflater.inflate(R.layout.dlg_cancle_order_new, null);
				listview_cancle_notice = (ListView) view.findViewById(R.id.listview_cancle_notice);
				Button button_cancle = (Button) view.findViewById(R.id.button_cancle);
				Button button_ok = (Button) view.findViewById(R.id.button_ok);
				LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_dlg_cancle_main);
//			listview_cancle_notice.setAdapter(cancleAdapter);
				
				pWinChoose = new PopupWindow(view,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
				pWinChoose.setBackgroundDrawable(new BitmapDrawable());
				pWinChoose.setOutsideTouchable(true);
				pWinChoose.setFocusable(true);
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
//			pWinChoose.setWidth(dm.widthPixels - 80);
//			pWinChoose.setHeight(dm.heightPixels - dm.heightPixels/2);
				LayoutParams lp = new LayoutParams(dm.widthPixels - 80, dm.heightPixels/2);
				llMain.setLayoutParams(lp);
				pWinChoose.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//			Utils.setAttribute(OrderDetailFromOrderActivity.this, pWinChoose);
				button_cancle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pos.clear();
//					if (pWinChoose.isShowing()) {
//					}
//					Utils.onDismiss(OrderDetailFromOrderActivity.this);
						pWinChoose.dismiss();
						pWinChoose = null;
					}
				});
				button_ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (pos.size()<=0) {
							ToastUtil.showToastShortCenter(OrderDetailFromOrderActivity.this, "请先选择取消订单的原因哦");
						}else {
							pos.clear();
//						if (pWinChoose.isShowing()) {
//						}
//						Utils.onDismiss(OrderDetailFromOrderActivity.this);
							CommUtil.ReasonCancelOrder(
									spUtil.getString("cellphone", ""), 
									Global.getIMEI(OrderDetailFromOrderActivity.this), 
									OrderDetailFromOrderActivity.this, 
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
						cancleAdapter.notifyDataSetChanged();
					}
				});
//			pWinChoose.setOnDismissListener(new OnDismissListener() {
//				
//				@Override
//				public void onDismiss() {
//					// TODO Auto-generated method stub
//					Utils.onDismiss(OrderDetailFromOrderActivity.this);
//				}
//			});
			}
		}*/

	}
	
	
	public void onDismiss(){
	    WindowManager.LayoutParams lp=getWindow().getAttributes();
	    lp.alpha = 1f;
	    getWindow().setAttributes(lp);	
	}

	// 取消订单
	private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String msg ="";
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						msg =jsonObject.getString("msg");
					}
					showCancleDetail(code,msg);
				}else {
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						msg =jsonObject.getString("msg");
					}
					showCancleDetail(code,msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(OrderDetailFromOrderActivity.this, "连接服务器失败,请检查网络", Toast.LENGTH_SHORT).show();
		}

	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer!=null) {
			timer.cancel();
			timer = null;
		}
		if(receiver!=null){
			unregisterReceiver(receiver);
		}
		if(task!=null){
			task.cancel();
			task=null;
		}
	}
	
	
	private void showCancleDetail(final int code,String msg) {
		pWinCancle = null;
		if (pWinCancle == null) {
			View view = mInflater.inflate(R.layout.dlg_cancle_order_success_or_fail_topay, null);
			RelativeLayout layout_show=	(RelativeLayout) view.findViewById(R.id.layout_show);
			TextView textView_message=	(TextView) view.findViewById(R.id.textView_message);
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_dlg_cancle_main);
			pWinCancle = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWinCancle.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			pWinCancle.setWidth(dm.widthPixels - 50);
			LayoutParams lp = new LayoutParams(dm.widthPixels - 50, 
					Utils.dip2px(OrderDetailFromOrderActivity.this, 100));
			llMain.setLayoutParams(lp);
			pWinCancle.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
			
			if (code == 0 ) {
				layout_show.setBackgroundResource(R.drawable.order_cancle_ok);
				if (msg.equals("操作成功")) {
					textView_message.setText("订单取消成功");
				}else if(msg.equals("")){
					textView_message.setText("订单取消成功");
				}else {
					textView_message.setText(msg);
				}
			}else {
				layout_show.setBackgroundResource(R.drawable.order_cancle_no);
				if (msg.equals("操作失败")) {
					textView_message.setText("订单取消失败");
				}else if(msg.equals("")) {
					textView_message.setText("订单取消失败");
				}else {
					textView_message.setText(msg);
				}
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						Message message = Message.obtain();
						message.arg1=code;
						message.what=2;
						handler.sendMessage(message);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
			
			layout_show.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pWinCancle.dismiss();
					pWinCancle = null;
				}
			});
		}
	}

	private void sendOrder(){
		pDialog.showDialog();
		CommUtil.changeOrderPayMethodV2(
				null,spUtil.getString("cellphone", ""),
				Global.getIMEI(this), 
				this, 
				OrderId, 
				customerId, 
				null,null,null,-1,bathRequired,-1,Utils.formatDouble(needMoney,2),
				paytype,couponId,-1,bathPetIds/*GetPetIds(mulPetServiceList)*/,isVie,getListIds(listIds),orderHanler);
	}
	private void TwoTrainPay(){
		pDialog.showDialog();
		CommUtil.TwoTrainPay(mContext, 
				spUtil.getString("cellphone", ""), 
				couponId,
				customerId, 
				customerMobile, 
				customerName, 
				-1, 
				OrderId, 
				Utils.formatDouble(needMoney, 2), 
				paytype,
				null,
				orderHanler);
	}
	private AsyncHttpResponseHandler orderHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("重新支付订单："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("orderPet")&&!jdata.isNull("orderPet")){
						JSONArray jarr = jdata.getJSONArray("orderPet");
						for(int i=0;i<jarr.length()&&i<mulPetServiceList.size();i++){
							mulPetServiceList.get(i).orderid=jarr.getInt(i);
						}
					}
						if (needMoney==0||(paytype==4&&balance>=needMoney)) {
							//直接跳转到支付成功
							goPayResult();
						}else {
							outorderno = jdata.getString("tradeNo");
						}
				}else if(109 == resultCode){
					//重复提交，不做处理
				}else{
					ToastUtil.showToastShort(OrderDetailFromOrderActivity.this, msg);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
		
	};
	
	private void getPets(){
		pDialog.showDialog();
		CommUtil.queryCustomerPets(this,spUtil.getString("cellphone", ""), 
				Global.getCurrentVersion(this), 
				Global.getIMEI(this), petHandler);
	}
	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("获取宠物列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("pets")&&!jdata.isNull("pets")){
						JSONArray jpets = jdata.getJSONArray("pets");
						if(jpets.length()<20){
							goAddPetHint();
							
						}else{
							finishWithAnimation();
						}
					}else{
						goAddPetHint();
					}
				}else{
					finishWithAnimation();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				finishWithAnimation();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			finishWithAnimation();
		}
		
	};
	
	private void goAddPetHint(){
		ArrayList<MulPetService> mpstmpList = new ArrayList<MulPetService>();
		for(int i=0;i<mulPetServiceList.size();i++){
			if(mulPetServiceList.get(i).petCustomerId<=0)
				mpstmpList.add(mulPetServiceList.get(i));
		}
		if(mpstmpList.size()>0){
			//到添加宠物界面
			Intent intent = new Intent(act, OrderListShowAddMyPet.class);
			intent.putParcelableArrayListExtra("mulpetservice", mpstmpList);
			startActivity(intent);
		}
		finishWithAnimation();
		
	}

	private void sendBroadcastToMainUpdataUserinfo(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY);
		sendBroadcast(intent);
	}

	private void goPayResult(){
		Intent intent = new Intent(act, PaySuccessActivity.class);
		intent.putParcelableArrayListExtra("mpslist",mulPetServiceList);
		intent.putExtra("previous", Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY);
		intent.putExtra("orderId", OrderId);
		intent.putExtra("type", type);
		startActivity(intent);
		finishWithAnimation();
	}


		
		private void goToOrders(){
			Intent intent = new Intent();
			intent.setAction("android.intent.action.mainactivity");
			intent.putExtra("previous", Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY);
			sendBroadcast(intent);
			if(previous==Global.PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY){
				if(ServiceActivity.act!=null)
					ServiceActivity.act.finishWithAnimation();
			}
			if(ServiceFeature.act!=null)
				ServiceFeature.act.finishWithAnimation();
			finishWithAnimation();
		}
		private void sendServiceToChangeOrderStatus(String orderid){
			Utils.mLogError("通知后台-----------");
			CommUtil.confirmOrderStatus(this,spUtil.getString("cellphone", ""),
					Global.getCurrentVersion(this), Global.getIMEI(this), 
					orderid,sendToServiceHanler);
		}
		private AsyncHttpResponseHandler sendToServiceHanler = new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Utils.mLogError("支付成功后通知后台："+new String(responseBody));
				try {
					JSONObject jobj = new JSONObject(new String(responseBody));
					int resultCode = jobj.getInt("code");
					if(0 == resultCode){
						
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
		private MCloseReceiver receiver;
		private SelectableRoundedImageView srivPetStore;
		private TextView tvPetStoreName;
		private TextView tvPetStoreAddr;
		private TextView tvPetStorePhone;
		private LinearLayout llPetStore;
		private TextView tvAddrtype;
		private LinearLayout llHidden;
		private LinearLayout llShowAll;
		private TextView tvShowAll;
		private ImageView ivShowAll;
		private LinearLayout llMain;
		private RelativeLayout rlNull;
		private TextView tvMsg1;
		private Button btRefresh;
		private PullToRefreshScrollView prsScrollView;
		private TextView tvBalanceHint;
		
		
		private void initReceiver() {
			receiver = new MCloseReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.intent.action.orderdetail");
			// 注册广播接收器
			registerReceiver(receiver, filter);
		}
		
		private class MCloseReceiver extends BroadcastReceiver {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				finishWithAnimation();
			}
			
		}

		@Override
		protected void onResume() {
			super.onResume();
		    MobclickAgent.onPageStart("OrderDetailFromOrderActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		    MobclickAgent.onResume(this);          //统计时长
		    if(gotoOrderDetail){
		    	goOrderDetail();
		    }
		}
		@Override
		protected void onPause() {
			super.onPause();
		    MobclickAgent.onPageEnd("OrderDetailFromOrderActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		    MobclickAgent.onPause(this);
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
							}
						}
						cancleAdapter = new CancleAdapter(OrderDetailFromOrderActivity.this,showReason,pos);
						listview_cancle_notice.setAdapter(cancleAdapter);
					}else {
						if (object.has("msg")&&!object.isNull("msg")) {
							ToastUtil.showToastShortCenter(act, object.getString("msg"));
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
		private void showPetNum(int num){
			more_pet_one_view.setVisibility(View.GONE);
			if (num==1) {
				more_pet_one_view.setVisibility(View.VISIBLE);
//				layout_more_pet_one.setVisibility(View.VISIBLE);
				layout_more_pet_two.setVisibility(View.VISIBLE);
				layout_more_pet_thr.setVisibility(View.GONE);
				layout_more_pet_four.setVisibility(View.GONE);
				layout_more_pet_five.setVisibility(View.GONE);
				more_pet_two_view.setVisibility(View.GONE);
			}else if (num==2) {
				more_pet_one_view.setVisibility(View.VISIBLE);
//				layout_more_pet_one.setVisibility(View.VISIBLE);
				layout_more_pet_two.setVisibility(View.VISIBLE);
				layout_more_pet_thr.setVisibility(View.VISIBLE);
				layout_more_pet_four.setVisibility(View.GONE);
				layout_more_pet_five.setVisibility(View.GONE);
				more_pet_thr_view.setVisibility(View.GONE);
			}else if (num==3) {
				more_pet_one_view.setVisibility(View.VISIBLE);
//				layout_more_pet_one.setVisibility(View.VISIBLE);
				layout_more_pet_two.setVisibility(View.VISIBLE);
				layout_more_pet_thr.setVisibility(View.VISIBLE);
				layout_more_pet_four.setVisibility(View.VISIBLE);
				layout_more_pet_five.setVisibility(View.GONE);
				more_pet_four_view.setVisibility(View.GONE);
			}else if (num==4) {
				more_pet_one_view.setVisibility(View.VISIBLE);
//				layout_more_pet_one.setVisibility(View.VISIBLE);
				layout_more_pet_two.setVisibility(View.VISIBLE);
				layout_more_pet_thr.setVisibility(View.VISIBLE);
				layout_more_pet_four.setVisibility(View.VISIBLE);
				layout_more_pet_five.setVisibility(View.VISIBLE);
//				more_pet_five_view.setVisibility(View.GONE);
			}
		}
		
		
		public String getPetId_ServiceId_MyPetId(ArrayList<MulPetService> list){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				MulPetService mps = list.get(i);
				if (i<list.size()-1) {
					sb.append(mps.petId);
					sb.append("_");
					sb.append(mps.serviceId);
					sb.append("_");
					sb.append(mps.petCustomerId);
					sb.append("_");
				}else {
					sb.append(mps.petId);
					sb.append("_");
					sb.append(mps.serviceId);
					sb.append("_");
					sb.append(mps.petCustomerId);
				}
			}
			return sb.toString();
			
		}
		

		public String GetPetIds(ArrayList<MulPetService> list){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				MulPetService mps = list.get(i);
				sb.append(mps.petId+",");
			}
			if (type==2) {
				return sb.toString().substring(0, sb.toString().length()-1);
			}else {
				return null;
			}
			
		}
		private void goNextChangeCustomer(Class clazz, int requestcode){
			Intent intent = new Intent(this, clazz);
			intent.putExtra("previous", Global.ORDER_WAIT_TO_PAY_DETAIL_CHANGE_CUSTOMER);
			intent.putExtra("OrderId", OrderId);
			intent.putExtra("cname", customerName);
			intent.putExtra("cphone", customerMobile);
			startActivityForResult(intent, requestcode);
		}

		private String getListIds(ArrayList<Integer> listIds){
			StringBuilder builder  = new StringBuilder();
			if (listIds.size()>0) {
				for (int i = 0; i < listIds.size(); i++) {
					builder.append(listIds.get(i)+",");
				}
				return builder.substring(0, builder.length()-1);
			}else {
				return null;
			}
		}
}
