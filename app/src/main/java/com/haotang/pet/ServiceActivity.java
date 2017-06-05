package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.Share;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ServiceActivity extends SuperActivity implements OnClickListener {
	private RollPagerView rpvBanner;
	private Button btSubmit;
	private Intent fIntent;
	private int servicetype;
	private int petid;
	private int petkind;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private int previous;
	public static SuperActivity act;
	private ArrayList<String> listBanner = new ArrayList<String>();
	private TextView textView_service_name_show;
	private TextView tvshopdetail;
	private SelectableRoundedImageView shop_icon;
	private TextView textView_service_detail_num_to_be_use;
	private ImageView service_back;
	private LinearLayout layout_service_back;
	private ImageView service_share;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private List<Share> shareList;
	private SelectableRoundedImageView imageview_go_home_icon;
	private TextView service_go_home_show_detail;
	private Drawable bgBackDrawable;
	private Drawable bgBackBelowDrawable;
	private Drawable bgShareDrawable;
	private Drawable bgShareBelowDrawable;
	private Drawable bgNavBarDrawable;
	private int alphaMax = 180;
	private int serviceIdGoHome = 0;
	private String orderDetailH5Url = "";
	private int serviceid;
	private int shopid;
	private int customerpetid;
	private int areaid;
	private String customerpetname;
	private String avatarPath;
	private String petname;
	private double lat;
	private double lng;
	private ArrayList<String> serviceitems = new ArrayList<String>();
	private LinearLayout service_layout_eva_one;
	private LinearLayout service_layout_eva_two;
	private RelativeLayout re_layout_go_home;
	private RelativeLayout re_layout_go_shop;
	private TextView textview_go_home_name_and_price;
	private TextView textview_go_shop_name_and_price;
	private ImageView service_eva_one;
	private ImageView service_eva_two;

	private Button button_go_apponit_home;
	private Button button_go_apponit_shop;

	private TextView textView_service_eva_one;
	private TextView textView_service_eva_two;
	private ImageView service_eva_is_member_one;
	private ImageView service_eva_is_member_two;

	private LinearLayout la_layout_isshow_go_shop;
	private TextView textview_showwhat;
	// private View view_home_and_shop;
	private BannerBathLoopAdapter adapterBanner;
	private boolean isBeauticianGoShop = true;
	private boolean isBeauticianGoHome = true;
	// 上门相关
	private LinearLayout gohome;

	private TextView textview_service_text_price_from;
	private TextView textview_service_text_price_from_detail;
	private ImageView imageview_service_arrow_go_home;
	private LinearLayout service_item_show_go_home;
	private TextView old_listprice;
	private TextView no_old_price;
	// 上门item one
	private LinearLayout item_service_one_go_home;
	private TextView textview_item_service_name_one_go_home;
	private TextView textview_item_service_detail_one_go_home;
	private TextView textview_item_service_price_one_go_home;
	private TextView old_one_price_go_home;
	private TextView no_old_price_one_go_home;
	private Button button_go_apponit_one_go_home;
	private View item_service_line_one_go_home;
	// 上门item two
	private LinearLayout item_service_two_go_home;
	private TextView textview_item_service_name_two_go_home;
	private TextView textview_item_service_detail_two_go_home;
	private TextView textview_item_service_price_two_go_home;
	private TextView old_two_price_go_home;
	private TextView no_old_price_two_go_home;
	private Button button_go_apponit_two_go_home;
	private View item_service_line_two_go_home;
	// 上门item thr
	private LinearLayout item_service_thr_go_home;
	private TextView textview_item_service_name_thr_go_home;
	private TextView textview_item_service_detail_thr_go_home;
	private TextView textview_item_service_price_thr_go_home;
	private TextView old_thr_price_go_home;
	private TextView no_old_price_thr_go_home;
	private Button button_go_apponit_thr_go_home;
	private View item_service_line_thr_go_home;

	// 到店相关
	private LinearLayout goshop;
	// private LinearLayout service_item_show_go_shop_show;
	private TextView textview_service_text_price_from_go_shop;
	private TextView textview_service_text_price_from_detail_go_shop;
	private ImageView imageview_service_arrow_go_shop;
	private LinearLayout service_item_show_go_shop;
	private TextView old_shoplist;
	private TextView no_oldshop_price;

	// 到店item one
	private LinearLayout item_service_one_go_shop;
	private TextView textview_item_service_name_one_go_shop;
	private TextView textview_item_service_detail_one_go_shop;
	private TextView textview_item_service_price_one_go_shop;
	private TextView old_one_price_go_shop;
	private TextView no_old_price_one_go_shop;
	private Button button_go_apponit_one_go_shop;
	private View item_service_line_one_go_shop;
	// 到店item two
	private LinearLayout item_service_two_go_shop;
	private TextView textview_item_service_name_two_go_shop;
	private TextView textview_item_service_detail_two_go_shop;
	private TextView textview_item_service_price_two_go_shop;
	private TextView old_two_price_go_shop;
	private TextView no_old_price_two_go_shop;
	private Button button_go_apponit_two_go_shop;
	private View item_service_line_two_go_shop;
	// 到店item thr
	private LinearLayout item_service_thr_go_shop;
	private TextView textview_item_service_name_thr_go_shop;
	private TextView textview_item_service_detail_thr_go_shop;
	private TextView textview_item_service_price_thr_go_shop;
	private TextView old_thr_price_go_shop;
	private TextView no_old_price_thr_go_shop;
	private Button button_go_apponit_thr_go_shop;
	private View item_service_line_thr_go_shop;

	private boolean isopenHome = false;
	private boolean isopenShop = false;

	private RelativeLayout relayout_service_item_go_home;// 上门非美容师入口进入
	private RelativeLayout relayout_service_item_go_shop;// 到店非美容师入口进入

	private RelativeLayout relayout_service_from_beau_home;// 上门非美容师入口进入
	private RelativeLayout relayout_service_from_beau_shop;// 到店非美容师入口进入
	private int clicksort = 0;

	private TextView textview_service_text_price_beau_home;
	private TextView textview_service_text_price_beau_shop;

	private View service_content_header;
	private int shopPrice10 = -1;
	private int shopPrice20 = -1;
	private int shopPrice30 = -1;
	private RelativeLayout rl_ppllayout_top;
	private TextView textview_give_other_money;
	private LinearLayout layout_top_next_eva;
	private TextView textView_title;
	private TextView textview_good_show;
	private TextView textview_good;
	public static ImageView imageView_isplay;

	private MListview service_scaling_image;
	private JoinWorkAdapter joinWorkAdapter;
	private ArrayList<String> Imagelist = new ArrayList<String>();
	private TextView textview_notice;

	private LinearLayout Home, Shop;
	private LinearLayout layout_hot;
	private TextView textview_go_home_service_price_detail;
	private double extraServiceFee = 0;
	private String extraServiceFeeTag = null;
	private String BuyCardH5Url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servicedetail_new);
		MApplication.listAppoint.add(this);
		act = this;
		shareList = new ArrayList<Share>();
		findView();
		setView();
		configPlatforms();

		CommUtil.getShare(this, System.currentTimeMillis(), getShare);
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		textview_give_other_money.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (servicetype==1) {//洗澡
//					UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Bath_ToRecharge);
//				}else if (servicetype==2) {//美容
//					UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Beauty_ToRecharge);
//				}
//				Intent intent = new Intent(mContext, MyLastMoney.class);
//				startActivity(intent);
				goAd(mContext, BuyCardH5Url, ADActivity.class);
			}
		});
		button_go_apponit_one_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 10;
				goAppoinement(2);// 上门预约跳转界面
			}
		});
		item_service_one_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 10;
				goAppoinement(2);// 上门预约跳转界面
			}
		});
		button_go_apponit_two_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 20;
				goAppoinement(2);// 上门预约跳转界面
			}
		});
		item_service_two_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 20;
				goAppoinement(2);// 上门预约跳转界面
			}
		});
		button_go_apponit_thr_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 30;
				goAppoinement(2);// 上门预约跳转界面
			}
		});
		item_service_thr_go_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 30;
				goAppoinement(2);// 上门预约跳转界面
			}
		});

		button_go_apponit_one_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 10;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
		item_service_one_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 10;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
		button_go_apponit_two_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 20;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
		item_service_two_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 20;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
		button_go_apponit_thr_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 30;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
		item_service_thr_go_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clicksort = 30;
				goAppoinement(1);// 到店预约跳转界面
			}
		});
	}

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		fIntent = getIntent();

		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
		service_back = (ImageView) findViewById(R.id.service_back);
		layout_service_back = (LinearLayout) findViewById(R.id.layout_service_back);
		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
		service_share = (ImageView) findViewById(R.id.service_share);
		service_share_below = (ImageView) findViewById(R.id.service_share_below);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);
		rl_ppllayout_top = (RelativeLayout) findViewById(R.id.rl_ppllayout_top);

		rpvBanner = (RollPagerView) findViewById(R.id.rpv_servicedetail_pet);

		llChangepet = (LinearLayout) findViewById(R.id.ll_servicedetail_changepet);
		tvChangepet = (TextView) findViewById(R.id.tv_servicedetail_changepet);
		llComments = (LinearLayout) findViewById(R.id.ll_servicedetail_comments);
		tvGoodComments = (TextView) findViewById(R.id.tv_servicedetail_goodcomments);
		tvBadComments = (TextView) findViewById(R.id.tv_servicedetail_badcomments);

		// gvServiceItems = (MyGridView)
		// findViewById(R.id.gv_servicedetail_serviceitems);

		btSubmit = (Button) findViewById(R.id.bt_servicedetail_submit);

		textView_service_name_show = (TextView) findViewById(R.id.textView_service_name_show);
		tvshopdetail = (TextView) findViewById(R.id.service_go_shop_show_detail);
		shop_icon = (SelectableRoundedImageView) findViewById(R.id.imageview_go_shop_icon);
		textView_service_detail_num_to_be_use = (TextView) findViewById(R.id.textView_service_detail_num_to_be_use);

		imageview_go_home_icon = (SelectableRoundedImageView) findViewById(R.id.imageview_go_home_icon);
		service_go_home_show_detail = (TextView) findViewById(R.id.service_go_home_show_detail);

		rlMain = (RelativeLayout) findViewById(R.id.rl_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);

		layout_top_next_eva = (LinearLayout) findViewById(R.id.layout_top_next_eva);
		textview_give_other_money = (TextView) findViewById(R.id.textview_give_other_money);
		service_layout_eva_one = (LinearLayout) findViewById(R.id.service_layout_eva_one);
		service_layout_eva_two = (LinearLayout) findViewById(R.id.service_layout_eva_two);
		re_layout_go_home = (RelativeLayout) findViewById(R.id.re_layout_go_home);
		re_layout_go_shop = (RelativeLayout) findViewById(R.id.re_layout_go_shop);
		textview_go_shop_name_and_price = (TextView) findViewById(R.id.textview_go_shop_name_and_price);
		textview_go_home_name_and_price = (TextView) findViewById(R.id.textview_go_home_name_and_price);
		service_eva_one = (ImageView) findViewById(R.id.service_eva_one);
		service_eva_two = (ImageView) findViewById(R.id.service_eva_two);
		button_go_apponit_home = (Button) findViewById(R.id.button_go_apponit_home);
		button_go_apponit_shop = (Button) findViewById(R.id.button_go_apponit_shop);
		textView_service_eva_one = (TextView) findViewById(R.id.textView_service_eva_one);
		textView_service_eva_two = (TextView) findViewById(R.id.textView_service_eva_two);
		service_eva_is_member_one = (ImageView) findViewById(R.id.service_eva_is_member_one);
		service_eva_is_member_two = (ImageView) findViewById(R.id.service_eva_is_member_two);
		la_layout_isshow_go_shop = (LinearLayout) findViewById(R.id.la_layout_isshow_go_shop);
		textview_showwhat = (TextView) findViewById(R.id.textview_showwhat);
		// view_home_and_shop = (View) findViewById(R.id.view_home_and_shop);

		// 上门外侧
		old_listprice = (TextView) findViewById(R.id.old_listprice);
		no_old_price = (TextView) findViewById(R.id.no_old_price);
		service_item_show_go_home = (LinearLayout) findViewById(R.id.service_item_show_go_home);
		imageview_service_arrow_go_home = (ImageView) findViewById(R.id.imageview_service_arrow_go_home);
		// 上门include
		gohome = (LinearLayout) findViewById(R.id.gohome);

		Home = (LinearLayout) gohome.findViewById(R.id.item_service_show);
		// item one
		item_service_one_go_home = (LinearLayout) gohome
				.findViewById(R.id.item_service_one);
		textview_item_service_name_one_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_name_one);
		textview_item_service_detail_one_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_detail_one);
		textview_item_service_price_one_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_price_one);
		old_one_price_go_home = (TextView) gohome
				.findViewById(R.id.old_one_price);
		no_old_price_one_go_home = (TextView) gohome
				.findViewById(R.id.no_old_price_one);
		button_go_apponit_one_go_home = (Button) gohome.findViewById(R.id.button_go_apponit_one);
		item_service_line_one_go_home = (View) gohome.findViewById(R.id.item_service_line_one);
		// item two
		item_service_two_go_home = (LinearLayout) gohome
				.findViewById(R.id.item_service_two);
		textview_item_service_name_two_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_name_two);
		textview_item_service_detail_two_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_detail_two);
		textview_item_service_price_two_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_price_two);
		old_two_price_go_home = (TextView) gohome
				.findViewById(R.id.old_two_price);
		no_old_price_two_go_home = (TextView) gohome
				.findViewById(R.id.no_old_price_two);
		button_go_apponit_two_go_home = (Button) gohome.findViewById(R.id.button_go_apponit_two);
		item_service_line_two_go_home = (View) gohome.findViewById(R.id.item_service_line_two);
		// item thr
		item_service_thr_go_home = (LinearLayout) gohome
				.findViewById(R.id.item_service_thr);
		textview_item_service_name_thr_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_name_thr);
		textview_item_service_detail_thr_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_detail_thr);
		textview_item_service_price_thr_go_home = (TextView) gohome
				.findViewById(R.id.textview_item_service_price_thr);
		old_thr_price_go_home = (TextView) gohome
				.findViewById(R.id.old_thr_price);
		no_old_price_thr_go_home = (TextView) gohome
				.findViewById(R.id.no_old_price_thr);
		button_go_apponit_thr_go_home = (Button) gohome.findViewById(R.id.button_go_apponit_thr);
		item_service_line_thr_go_home = (View) gohome.findViewById(R.id.item_service_line_thr);

		// 到店外侧
		old_shoplist = (TextView) findViewById(R.id.old_shoplist);
		no_oldshop_price = (TextView) findViewById(R.id.no_oldshop_price);
		service_item_show_go_shop = (LinearLayout) findViewById(R.id.service_item_show_go_shop);
		imageview_service_arrow_go_shop = (ImageView) findViewById(R.id.imageview_service_arrow_go_shop);
		// 到店include
		goshop = (LinearLayout) findViewById(R.id.goshop);
		Shop = (LinearLayout) goshop.findViewById(R.id.item_service_show);
		// item one
		item_service_one_go_shop = (LinearLayout) goshop
				.findViewById(R.id.item_service_one);
		textview_item_service_name_one_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_name_one);
		textview_item_service_detail_one_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_detail_one);
		textview_item_service_price_one_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_price_one);
		old_one_price_go_shop = (TextView) goshop
				.findViewById(R.id.old_one_price);
		no_old_price_one_go_shop = (TextView) goshop
				.findViewById(R.id.no_old_price_one);
		button_go_apponit_one_go_shop = (Button) goshop.findViewById(R.id.button_go_apponit_one);
		item_service_line_one_go_shop = (View) goshop.findViewById(R.id.item_service_line_one);
		// item two
		item_service_two_go_shop = (LinearLayout) goshop
				.findViewById(R.id.item_service_two);
		textview_item_service_name_two_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_name_two);
		textview_item_service_detail_two_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_detail_two);
		textview_item_service_price_two_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_price_two);
		old_two_price_go_shop = (TextView) goshop
				.findViewById(R.id.old_two_price);
		no_old_price_two_go_shop = (TextView) goshop
				.findViewById(R.id.no_old_price_two);
		button_go_apponit_two_go_shop = (Button) goshop.findViewById(R.id.button_go_apponit_two);
		item_service_line_two_go_shop = (View) goshop.findViewById(R.id.item_service_line_two);
		// item thr
		item_service_thr_go_shop = (LinearLayout) goshop
				.findViewById(R.id.item_service_thr);
		textview_item_service_name_thr_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_name_thr);
		textview_item_service_detail_thr_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_detail_thr);
		textview_item_service_price_thr_go_shop = (TextView) goshop
				.findViewById(R.id.textview_item_service_price_thr);
		old_thr_price_go_shop = (TextView) goshop
				.findViewById(R.id.old_thr_price);
		no_old_price_thr_go_shop = (TextView) goshop
				.findViewById(R.id.no_old_price_thr);
		button_go_apponit_thr_go_shop = (Button) goshop.findViewById(R.id.button_go_apponit_thr);
		item_service_line_thr_go_shop = (View) goshop.findViewById(R.id.item_service_line_thr);

		relayout_service_item_go_home = (RelativeLayout) findViewById(R.id.relayout_service_item_go_home);
		relayout_service_item_go_shop = (RelativeLayout) findViewById(R.id.relayout_service_item_go_shop);
		relayout_service_from_beau_home = (RelativeLayout) findViewById(R.id.relayout_service_from_beau_home);
		relayout_service_from_beau_shop = (RelativeLayout) findViewById(R.id.relayout_service_from_beau_shop);

		textview_service_text_price_beau_home = (TextView) findViewById(R.id.textview_service_text_price_beau_home);
		textview_service_text_price_beau_shop = (TextView) findViewById(R.id.textview_service_text_price_beau_shop);

		textview_service_text_price_from = (TextView) findViewById(R.id.textview_service_text_price_from);
		textview_service_text_price_from_go_shop = (TextView) findViewById(R.id.textview_service_text_price_from_go_shop);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textview_good_show = (TextView) findViewById(R.id.textview_good_show);
		textview_good = (TextView) findViewById(R.id.textview_good);
		imageView_isplay = (ImageView) findViewById(R.id.imageView_isplay);
		service_scaling_image = (MListview) findViewById(R.id.service_scaling_image);
		textview_notice = (TextView) findViewById(R.id.textview_notice);

		layout_hot = (LinearLayout) findViewById(R.id.layout_hot);
		textview_go_home_service_price_detail = (TextView) findViewById(R.id.textview_go_home_service_price_detail);

		service_content_header = (View) findViewById(R.id.service_content_header);

	}

	private void setView() {
		tvMsg1.setText("网络异常或连接服务器失败");
		btRefresh.setVisibility(View.VISIBLE);
		pDialog = new MProgressDialog(this);
		previous = fIntent.getIntExtra("previous", -1);
		servicetype = fIntent.getIntExtra("servicetype", 0);
		serviceid = fIntent.getIntExtra("serviceid", 0);
		customerpetid = fIntent.getIntExtra("customerpetid", 0);
		areaid = fIntent.getIntExtra("areaid", 0);
		shopid = fIntent.getIntExtra("shopid", 0);
		customerpetname = fIntent.getStringExtra("customerpetname");

		isBeauticianGoShop = fIntent.getBooleanExtra("isshop", true);
		isBeauticianGoHome = fIntent.getBooleanExtra("ishome", true);
		if (spUtil.getInt("tareaid", 0) == 100
				|| spUtil.getInt("tareaid", 0) == 0) {
			isBeauticianGoHome = false;
		}
		if (serviceid == 1 || serviceid == 3) {
			textView_title.setText("洗澡");
		} else if (serviceid == 2 || serviceid == 4) {
			textView_title.setText("美容");
		}
		// if(serviceid==1){
		// //狗洗澡默认背景
		// rpvBanner.setBackgroundResource(R.drawable.bg_bath_dog);
		// }else if(serviceid==2){
		// //狗美容默认背景
		// rpvBanner.setBackgroundResource(R.drawable.bg_beauty_dog);
		// }else if(serviceid==3){
		// //猫洗澡默认背景
		// rpvBanner.setBackgroundResource(R.drawable.bg_bath_cat);
		// }else if(serviceid==4){
		// //猫美容默认背景
		// rpvBanner.setBackgroundResource(R.drawable.bg_beauty_cat);
		// }
		// gvServiceItems.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// siAdapter = new ServiceItemAdapter(this, serviceitems);
		// gvServiceItems.setAdapter(siAdapter);
		// pplLayout.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre()
		// {
		//
		// @Override
		// public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight)
		// {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onSlide(int alpha) {
		// // TODO Auto-generated method stub
		// int alphaReverse = alphaMax - alpha;
		// if (alphaReverse < 0) {
		// alphaReverse = 0;
		// }
		// bgBackDrawable.setAlpha(alphaReverse);
		// bgBackBelowDrawable.setAlpha(alpha);
		// bgShareDrawable.setAlpha(alphaReverse);
		// bgShareBelowDrawable.setAlpha(alpha);
		// bgNavBarDrawable.setAlpha(alpha);
		//
		// }
		// });

		// bgBackDrawable = service_back.getBackground();
		// bgBackBelowDrawable = service_back_blow.getBackground();
		// bgShareDrawable = service_share.getBackground();
		// bgShareBelowDrawable = service_share_below.getBackground();
		// bgNavBarDrawable = rlTitle.getBackground();
		// bgBackDrawable.setAlpha(alphaMax);
		// bgShareDrawable.setAlpha(alphaMax);
		// bgNavBarDrawable.setAlpha(0);
		// bgBackBelowDrawable.setAlpha(0);
		// bgShareBelowDrawable.setAlpha(0);

		adapterBanner = new BannerBathLoopAdapter(rpvBanner, listBanner);
		rpvBanner.setAdapter(adapterBanner);

		joinWorkAdapter = new JoinWorkAdapter<String>(this, Imagelist);
		service_scaling_image.setAdapter(joinWorkAdapter);
		service_scaling_image.setFocusable(false);
		if (servicetype == 1) {
			sendEvent("2");
		} else if (servicetype == 2) {
			sendEvent("3");
		} else {
			sendEvent(servicetype + "");
		}

		lat = Double.parseDouble(spUtil.getString("lat_home", "0"));
		lng = Double.parseDouble(spUtil.getString("lng_home", "0"));

		if (!"".equals(spUtil.getString("newaddr", ""))
				&& !"".equals(spUtil.getString("newlat", ""))
				&& !"".equals(spUtil.getString("newlng", ""))) {
			lat = Double.parseDouble(spUtil.getString("newlat", "0"));
			lng = Double.parseDouble(spUtil.getString("newlng", "0"));
		} else if (spUtil.getInt("addressid", 0) > 0
				&& !"".equals(spUtil.getString("address", ""))) {
			lat = Double.parseDouble(spUtil.getString("lat", "0"));
			lng = Double.parseDouble(spUtil.getString("lng", "0"));
		}

		btSubmit.setOnClickListener(this);
		llComments.setOnClickListener(this);
		llChangepet.setOnClickListener(this);

		service_share.setOnClickListener(this);
		service_back.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		service_layout_eva_one.setOnClickListener(this);
		service_layout_eva_two.setOnClickListener(this);
		re_layout_go_home.setOnClickListener(this);
		re_layout_go_shop.setOnClickListener(this);
		button_go_apponit_home.setOnClickListener(this);
		button_go_apponit_shop.setOnClickListener(this);
		layout_service_back.setOnClickListener(this);
		layout_top_next_eva.setOnClickListener(this);

		getData();
	}

	private void showMain(boolean flag) {
		if (flag) {
			rlMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			rlMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.service_back:
			// 返回
			goBack();
			break;
		case R.id.layout_service_back:
			// 返回
			goBack();
			break;
		case R.id.bt_null_refresh:
			// 刷新
			getServiceData(servicetype, petid, 0);
			break;
		case R.id.service_share:
			/**
			 * QQ空间分享必须有文字和图片否则会报-10001错误
			 */
			setShareContent(shareList.get(0).img, shareList.get(0).txt,
					shareList.get(0).url);
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE,
					SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE);
			mController.openShare(ServiceActivity.this, false);
			break;
		case R.id.re_layout_go_home:
			// goAppoinement(2);// 现上门预约跳转界面
			// 这里判断当时从美容师入口进入时候下边隐藏

			if (fIntent.getIntExtra("beautician_sort", 0) > 0) {
				goAppoinement(2);
			} else {
				if (!isopenHome) {//
					isopenHome = true;
					service_item_show_go_home.setVisibility(View.VISIBLE);
					service_item_show_go_home.setBackgroundColor(Color
							.parseColor("#EFF4F7"));
					imageview_service_arrow_go_home
							.setBackgroundResource(R.drawable.service_icon_arrow_down);
					if (shopPrice10 == -1 && shopPrice20 == -1
							&& shopPrice30 == -1) {
						service_content_header.setVisibility(View.GONE);
					}
				} else {
					isopenHome = false;
					service_item_show_go_home.setVisibility(View.GONE);
					imageview_service_arrow_go_home
							.setBackgroundResource(R.drawable.service_icon_arrow_up);
					if (shopPrice10 == -1 && shopPrice20 == -1
							&& shopPrice30 == -1) {
						service_content_header.setVisibility(View.VISIBLE);
					}
				}
			}
			break;
		case R.id.re_layout_go_shop:
			// goAppoinement(1);// 现到店预约跳转界面
			if (fIntent.getIntExtra("beautician_sort", 0) > 0) {
				goAppoinement(1);
			} else {
				if (!isopenShop) {
					isopenShop = true;
					service_item_show_go_shop.setVisibility(View.VISIBLE);
					service_item_show_go_shop.setBackgroundColor(Color
							.parseColor("#F8F4EB"));
					imageview_service_arrow_go_shop
							.setBackgroundResource(R.drawable.service_icon_arrow_down);
					service_content_header.setVisibility(View.GONE);
				} else {
					isopenShop = false;
					service_item_show_go_shop.setVisibility(View.GONE);
					imageview_service_arrow_go_shop
							.setBackgroundResource(R.drawable.service_icon_arrow_up);
					service_content_header.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.button_go_apponit_home:// 当选中美容师进来的时候用这个
			goAppoinement(2);// 现上门预约跳转界面
			break;
		case R.id.button_go_apponit_shop:
			goAppoinement(1);// 现到店预约跳转界面
			break;
		case R.id.ll_servicedetail_comments:
			// 跳转美容师评价 同上
			goNextForPetComment(BeauticianCommentsListActivity.class, 0);
			break;
		case R.id.ll_servicedetail_changepet:
			// 跳转到宠物列表
			// goNextForData(ChoosePetActivity.class,
			// Global.BOOKINGSERVICEREQUESTCODE_PET,0);
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Bath_SwitchPet);
			goNextForData(ChoosePetActivityNew.class,
					Global.BOOKINGSERVICEREQUESTCODE_PET, 0);
			break;
		case R.id.layout_top_next_eva:
		case R.id.service_layout_eva_one:
			if (servicetype==1) {//洗澡
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Bath_IntoEvaluate);
			}else if (servicetype==2) {//美容
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Beauty_IntoEvaluate);
			}
			goNextForPetComment(BeauticianCommentsListActivity.class, 0);
			break;
		case R.id.service_layout_eva_two:
			if (servicetype==1) {//洗澡
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Bath_IntoEvaluate);
			}else if (servicetype==2) {//美容
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Beauty_IntoEvaluate);
			}
			goNextForPetComment(BeauticianCommentsListActivity.class, 0);
			break;
		}

	}

	private void goAppoinement(int serviceloc) {
		Intent intent;
		// 这里预约需要判断是美容还是洗澡
		if (servicetype == 1) {
			if (serviceloc==1) {//到店
				UmengStatistics.UmengEventStatistics(ServiceActivity.this,Global.UmengEventID.click_Bath_SelectShop);// 洗澡
			}else if (serviceloc==2) {//上门
				UmengStatistics.UmengEventStatistics(ServiceActivity.this,Global.UmengEventID.click_Bath_SelectDoor);// 洗澡
			}
		} else if (servicetype == 2) {
			if (serviceloc==1) {//到店
				UmengStatistics.UmengEventStatistics(ServiceActivity.this,Global.UmengEventID.click_Beauty_SelectShop);// 美容
			}else if (serviceloc==2) {//上门
				UmengStatistics.UmengEventStatistics(ServiceActivity.this,Global.UmengEventID.click_Beauty_SelectDoor);// 美容
			}
		}
		intent = new Intent(this, AppointmentActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("serviceloc", serviceloc);
		intent.putExtra("petid", petid);
		intent.putExtra("petkind", petkind);
		intent.putExtra("petname", petname);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("customerpetname", customerpetname);
		intent.putExtra("avatarPath", avatarPath);
		intent.putExtra("clicksort", clicksort);
		if (serviceloc==2) {
			intent.putExtra("extraServiceFee", extraServiceFee);
			intent.putExtra("extraServiceFeeTag", extraServiceFeeTag);
		}
		if (fIntent.getIntExtra("beautician_id", 0)>0) {
			intent.putExtra("areaid", areaid);
			intent.putExtra("shopid", shopid);
		}else {
			if (spUtil.getInt("tareaid", 0) == 100) {
				intent.putExtra("areaid", areaid);
			} else {
				intent.putExtra("areaid", spUtil.getInt("tareaid", 0));
			}
			if (spUtil.getInt("tshopid", 0) <= 0) {
				intent.putExtra("shopid", shopid);
			} else {
				intent.putExtra("shopid", spUtil.getInt("tshopid", 0));
			}
		}
		
		intent.putExtra("areaname", fIntent.getStringExtra("areaname"));
		intent.putExtra("beautician_id",
				fIntent.getIntExtra("beautician_id", 0));
		int SERVICESEX = fIntent.getIntExtra("beautician_sex", 0);
		intent.putExtra("beautician_gender",
				fIntent.getIntExtra("beautician_sex", 0));
		intent.putExtra("beautician_sort",
				fIntent.getIntExtra("beautician_sort", 0));
		intent.putExtra("beautician_ordernum",
				fIntent.getIntExtra("beautician_ordernum", 0));
		intent.putExtra("beautician_name",
				fIntent.getStringExtra("beautician_name"));
		intent.putExtra("beautician_image",
				fIntent.getStringExtra("beautician_image"));
		intent.putExtra("beautician_sortname",fIntent.getStringExtra("beautician_levelname"));
		intent.putExtra("upgradeTip",fIntent.getStringExtra("upgradeTip"));
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || "".equals(data))
			return;
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.BOOKINGSERVICEREQUESTCODE_PET) {
				// 选择宠物类型返回
				petid = data.getIntExtra("petid", 0);
				customerpetname = data.getStringExtra("customerpetname");
				serviceid = data.getIntExtra("serviceid", 0);
				servicetype = data.getIntExtra("servicetype", 0);
				customerpetid = data.getIntExtra("customerpetid", 0);
				petname = data.getStringExtra("petname");
				petkind = data.getIntExtra("petkind", 0);
				spUtil.removeData("newaddrtype");
				spUtil.removeData("newtime");
				spUtil.removeData("newdate");
				spUtil.saveInt("newpetkindid", data.getIntExtra("petkind", 0));
				spUtil.saveString("newpetname", data.getStringExtra("petname"));
				spUtil.saveString("newpetimg", data.getStringExtra("petimage"));
				spUtil.saveInt("changepet", 1);// 在该页修改过宠物
				// 保存选择的宠物的头像
				spUtil.saveString("petimg", data.getStringExtra("petimage"));
				avatarPath = data.getStringExtra("petimage");
				getServiceData(serviceid, petid, 0);
			} else if (requestCode == Global.SERVICEDETAIL_TO_APPOINTMENT) {

				if (!"".equals(spUtil.getString("newaddr", ""))
						&& !"".equals(spUtil.getString("newlat", ""))
						&& !"".equals(spUtil.getString("newlng", ""))) {
					lat = Double.parseDouble(spUtil.getString("newlat", "0"));
					lng = Double.parseDouble(spUtil.getString("newlng", "0"));
				}

			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& KeyEvent.ACTION_DOWN == event.getAction()) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		clearData();
		finishWithAnimation();
	}

	private void clearData() {
		spUtil.removeData("newpetkindid");
		spUtil.removeData("newpetname");
		spUtil.removeData("newpetimg");
		spUtil.removeData("newtime");
		spUtil.removeData("newdate");
		spUtil.removeData("newaddrtype");
		spUtil.removeData("addserviceids");
		spUtil.removeData("changepet");

	}

	private void goNext(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);

		intent.putExtra("serviceid", servicetype);
		intent.putExtra("petid", petid);
		intent.putExtra("orderDetailH5Url", orderDetailH5Url);
		if (pre == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
			intent.putExtra("petname", fIntent.getStringExtra("petname"));
		}
		startActivity(intent);
	}

	private void goNextForPetComment(Class clazz, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("serviceids", Integer.toString(serviceid));
		intent.putExtra("petid", petid);
		if (pre == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
			intent.putExtra("petname", fIntent.getStringExtra("petname"));
		}
		startActivity(intent);
	}

	private void goNextForData(Class clazz, int requestcode, int pre) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", pre);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("beautician_id",
				fIntent.getIntExtra("beautician_id", 0));
		startActivityForResult(intent, requestcode);
	}

	private void getServiceData(int servicetype, int petid, int serviceloc) {
		if (rpvBanner != null) {
			rpvBanner.pause();
		}
		serviceitems.clear();
		pDialog.showDialog();
		if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST) {
			CommUtil.getPetServicedetail(this, servicetype, petid, serviceloc,
					lat, lng, fIntent.getIntExtra("beautician_id", 0), areaid,
					petServiceHanler);
		} else {
			CommUtil.getPetServicedetail(this, servicetype, petid, serviceloc,
					lat, lng, fIntent.getIntExtra("beautician_id", 0),
					spUtil.getInt("tareaid", 0), petServiceHanler);
		}
	}

	private AsyncHttpResponseHandler petServiceHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {

			showMain(true);
			Utils.mLogError("宠物服务：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("activityCopy")&& !jData.isNull("activityCopy")) {
//							String activityCopy = jData.getString("activityCopy");
//							textview_give_other_money.setText(activityCopy);
							JSONObject objectActivityCopy = jData.getJSONObject("activityCopy");
							if (objectActivityCopy.has("txt")&&!objectActivityCopy.isNull("txt")) {
								textview_give_other_money.setText(objectActivityCopy.getString("txt"));
							}
							if (objectActivityCopy.has("url")&&!objectActivityCopy.isNull("url")) {
								BuyCardH5Url = objectActivityCopy.getString("url");
							}
						} else {
							textview_give_other_money.setVisibility(View.GONE);
						}
						if (jData.has("orderDetailH5Url")
								&& !jData.isNull("orderDetailH5Url")) {
							orderDetailH5Url = jData
									.getString("orderDetailH5Url");
						}
						if (jData.has("banners") && !jData.isNull("banners")
								&& jData.getJSONArray("banners").length() > 0) {
							listBanner.clear();
							JSONArray bannersArray = jData
									.getJSONArray("banners");
							for (int i = 0; i < bannersArray.length(); i++) {
								if (bannersArray.getString(i).startsWith(
										"video")) {
									listBanner.add(bannersArray.getString(i));
								} else {
									listBanner.add(CommUtil
											.getServiceNobacklash()
											+ bannersArray.getString(i));
								}
							}
							if (listBanner.size() > 1) {
								rpvBanner.setHintView(new ColorPointHintView(
										act, Color.parseColor("#FE8A3F"), Color
												.parseColor("#FFE2D0")));
							}
							adapterBanner.notifyDataSetChanged();
						}

						if (jData.has("picIntro") && !jData.isNull("picIntro")) {
							Imagelist.clear();
							JSONArray arrayPic = jData.getJSONArray("picIntro");
							if (arrayPic.length() != 0) {
								for (int i = 0; i < arrayPic.length(); i++) {
									Imagelist.add(CommUtil
											.getServiceNobacklash()
											+ arrayPic.getString(i));
								}
							}
							joinWorkAdapter.notifyDataSetChanged();
						}
						if (jData.has("nearbyDogs")
								&& !jData.isNull("nearbyDogs")) {
							textView_service_detail_num_to_be_use.setText(jData
									.getInt("nearbyDogs") + "");
						}
						if (jData.has("evaluateObj")
								&& !jData.isNull("evaluateObj")) {
							JSONObject objectEva = jData
									.getJSONObject("evaluateObj");
							if (objectEva.has("score")
									&& !objectEva.isNull("score")) {
								textview_good.setText(getTextScore(objectEva
										.getString("score") + "分"));
							}
							if (objectEva.has("percent")
									&& !objectEva.isNull("percent")) {
								textview_good_show.setText("好评率"
										+ objectEva.getString("percent") + ">");
							}
						}
						if (jData.has("positiveComments")
								&& !jData.isNull("positiveComments")) {
							if (jData.getInt("positiveComments") > 999) {
								tvGoodComments
										.setText(Utils.getTextAndColorComments(
												"好评：", "999+条"));
							} else {
								tvGoodComments
										.setText(Utils.getTextAndColorComments(
												"好评：",
												jData.getInt("positiveComments")
														+ "条"));
							}
						}
						if (jData.has("negativeComments")
								&& !jData.isNull("negativeComments")) {
							tvBadComments.setText(Utils
									.getTextAndColorComments("差评：",
											jData.getInt("negativeComments")
													+ "条"));
						}
						if (jData.has("homeServiceDesc")
								&& !jData.isNull("homeServiceDesc")) {
							// 按该字段判断是否显示上门
							service_go_home_show_detail.setText(jData
									.getString("homeServiceDesc"));
							if (jData.has("homeServicePic")
									&& !jData.isNull("homeServicePic")) {
								String homeServicePic = jData
										.getString("homeServicePic");
								ImageLoaderUtil.loadImg(
										CommUtil.getServiceNobacklash()
												+ homeServicePic,
										imageview_go_home_icon, 0, null);
							}
						}
						if (jData.has("shopServiceDesc")
								&& !jData.isNull("shopServiceDesc")) {
							// 按该字段判断是否显示到店
							tvshopdetail.setText(jData
									.getString("shopServiceDesc"));

							if (jData.has("shopServicePic")
									&& !jData.isNull("shopServicePic")) {
								ImageLoaderUtil
										.loadImg(
												CommUtil.getServiceNobacklash()
														+ jData.getString("shopServicePic"),
												shop_icon, 0, null);
							}

						}

						if (jData.has("serviceId")
								&& !jData.isNull("serviceId")) {
							serviceIdGoHome = jData.getInt("serviceId");
						}
						if (jData.has("levelDesc")
								&& !jData.isNull("levelDesc")) {
							JSONObject objectLevelDesc = jData
									.getJSONObject("levelDesc");
							if (objectLevelDesc.has("10")
									&& !objectLevelDesc.isNull("10")) {
								JSONObject object10 = objectLevelDesc
										.getJSONObject("10");
								if (object10.has("title")
										&& !object10.isNull("title")) {
									textview_item_service_name_one_go_home
											.setText(object10
													.getString("title"));

									textview_item_service_name_one_go_shop
											.setText(object10
													.getString("title"));
								}
								if (object10.has("desc")
										&& !object10.isNull("desc")) {
									textview_item_service_detail_one_go_home
											.setText(object10.getString("desc"));

									textview_item_service_detail_one_go_shop
											.setText(object10.getString("desc"));
								}
							}
							if (objectLevelDesc.has("20")
									&& !objectLevelDesc.isNull("20")) {
								JSONObject object20 = objectLevelDesc
										.getJSONObject("20");
								if (object20.has("title")
										&& !object20.isNull("title")) {
									textview_item_service_name_two_go_home
											.setText(object20
													.getString("title"));

									textview_item_service_name_two_go_shop
											.setText(object20
													.getString("title"));
								}
								if (object20.has("desc")
										&& !object20.isNull("desc")) {
									textview_item_service_detail_two_go_home
											.setText(object20.getString("desc"));

									textview_item_service_detail_two_go_shop
											.setText(object20.getString("desc"));
								}
							}
							if (objectLevelDesc.has("30")
									&& !objectLevelDesc.isNull("30")) {
								JSONObject object30 = objectLevelDesc
										.getJSONObject("30");
								if (object30.has("title")
										&& !object30.isNull("title")) {
									textview_item_service_name_thr_go_home
											.setText(object30
													.getString("title"));
									textview_item_service_name_thr_go_shop
											.setText(object30
													.getString("title"));
								}
								if (object30.has("desc")
										&& !object30.isNull("desc")) {
									textview_item_service_detail_thr_go_home
											.setText(object30.getString("desc"));
									textview_item_service_detail_thr_go_shop
											.setText(object30.getString("desc"));
								}
							}
						}
						// 这里是带着美容师等级进来的
						if (fIntent.getIntExtra("beautician_sort", 0) > 0) {
							relayout_service_item_go_home
									.setVisibility(View.GONE);
							relayout_service_from_beau_home
									.setVisibility(View.VISIBLE);

							relayout_service_item_go_shop
									.setVisibility(View.GONE);
							relayout_service_from_beau_shop
									.setVisibility(View.VISIBLE);
						}
						if (fIntent.getIntExtra("beautician_sort", 0) == 10) {
							if (jData.has("price10")
									&& !jData.isNull("price10")) {
								textview_go_home_name_and_price.setText(Utils
										.getTextAndColor("上门服务  ",
												"¥" + jData.getInt("price10")));
								textview_service_text_price_beau_home
										.setText(getTextPrice("¥"
												+ jData.getInt("price10")));
							}
							if (jData.has("listPrice")
									&& !jData.isNull("listPrice")) {
								old_listprice.setText(jData
										.getString("listPrice"));
								no_old_price.setVisibility(View.GONE);
							} else {
								old_listprice.setVisibility(View.GONE);
								textview_service_text_price_beau_home
										.setVisibility(View.GONE);
								no_old_price.setVisibility(View.VISIBLE);
								no_old_price.setText(getTextPrice("¥"
										+ jData.getInt("price10")));
							}
							if (jData.has("petServicePojo")
									&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData
										.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")
										&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo
											.getJSONObject("targetPets");
									if (targetPets.has("home")
											&& !targetPets.isNull("home")) {
										JSONObject home = targetPets
												.getJSONObject("home");
										if (home.has("10")
												&& !home.isNull("10")) {
											JSONObject object10 = home
													.getJSONObject("10");
											if (object10.has("btnTxt")
													&& !object10
															.isNull("btnTxt")) {
												String btnTxt = object10
														.getString("btnTxt");
												button_go_apponit_home
														.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}
							if (jData.has("shopPrice10")
									&& !jData.isNull("shopPrice10")) {
								textview_go_shop_name_and_price
										.setText(Utils.getTextAndColor(
												"到店服务  ",
												"¥"
														+ jData.getInt("shopPrice10")));
								textview_service_text_price_beau_shop
										.setText(getTextPrice("¥"
												+ jData.getInt("shopPrice10")));

							}
							if (jData.has("shopListPrice")
									&& !jData.isNull("shopListPrice")) {
								old_shoplist.setText(jData
										.getString("shopListPrice"));
								no_oldshop_price.setVisibility(View.GONE);
							} else {
								old_shoplist.setVisibility(View.GONE);
								textview_service_text_price_beau_shop
										.setVisibility(View.GONE);
								no_oldshop_price.setVisibility(View.VISIBLE);
								no_oldshop_price.setText(getTextPrice("¥"
										+ jData.getInt("shopPrice10")));
							}

							if (jData.has("petServicePojo")
									&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData
										.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")
										&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo
											.getJSONObject("targetPets");
									if (targetPets.has("shop")
											&& !targetPets.isNull("shop")) {
										JSONObject shop = targetPets
												.getJSONObject("shop");
										if (shop.has("10")
												&& !shop.isNull("10")) {
											JSONObject object10 = shop
													.getJSONObject("10");
											if (object10.has("btnTxt")
													&& !object10
															.isNull("btnTxt")) {
												String btnTxt = object10
														.getString("btnTxt");
												button_go_apponit_shop
														.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}

						} else if (fIntent.getIntExtra("beautician_sort", 0) == 20) {
							if (jData.has("price20")
									&& !jData.isNull("price20")) {
								textview_go_home_name_and_price.setText(Utils
										.getTextAndColor("上门服务  ",
												"¥" + jData.getInt("price20")));
								textview_service_text_price_beau_home
										.setText(getTextPrice("¥"
												+ jData.getInt("price20")));
							}

							if (jData.has("petServicePojo")
									&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData
										.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")
										&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo
											.getJSONObject("targetPets");
									if (targetPets.has("home")
											&& !targetPets.isNull("home")) {
										JSONObject home = targetPets
												.getJSONObject("home");
										if (home.has("20")
												&& !home.isNull("20")) {
											JSONObject object20 = home
													.getJSONObject("20");
											if (object20.has("btnTxt")
													&& !object20
															.isNull("btnTxt")) {
												String btnTxt = object20
														.getString("btnTxt");
												button_go_apponit_home
														.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}
							if (jData.has("listPrice20")
									&& !jData.isNull("listPrice20")) {
								old_listprice.setText(jData
										.getString("listPrice20"));
								no_old_price.setVisibility(View.GONE);
							} else {
								old_listprice.setVisibility(View.GONE);
								textview_service_text_price_beau_home
										.setVisibility(View.GONE);
								no_old_price.setVisibility(View.VISIBLE);
								no_old_price.setText(getTextPrice("¥"
										+ jData.getInt("price20")));
							}
							if (jData.has("shopPrice20")
									&& !jData.isNull("shopPrice20")) {
								textview_go_shop_name_and_price
										.setText(Utils.getTextAndColor(
												"到店服务  ",
												"¥"
														+ jData.getInt("shopPrice20")));
								textview_service_text_price_beau_shop
										.setText(getTextPrice("¥"
												+ jData.getInt("shopPrice20")));
							}
							if (jData.has("petServicePojo")
									&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData
										.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")
										&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo
											.getJSONObject("targetPets");
									if (targetPets.has("shop")
											&& !targetPets.isNull("shop")) {
										JSONObject shop = targetPets
												.getJSONObject("shop");
										if (shop.has("20")
												&& !shop.isNull("20")) {
											JSONObject object20 = shop
													.getJSONObject("20");
											if (object20.has("btnTxt")
													&& !object20
															.isNull("btnTxt")) {
												String btnTxt = object20
														.getString("btnTxt");
												button_go_apponit_shop
														.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}
							if (jData.has("shopListPrice20")
									&& !jData.isNull("shopListPrice20")) {
								old_shoplist.setText(jData
										.getString("shopListPrice20"));
								no_oldshop_price.setVisibility(View.GONE);
							} else {
								old_shoplist.setVisibility(View.GONE);
								textview_service_text_price_beau_shop
										.setVisibility(View.GONE);
								no_oldshop_price.setVisibility(View.VISIBLE);
								no_oldshop_price.setText(getTextPrice("¥"
										+ jData.getInt("shopPrice20")));
							}
						} else if (fIntent.getIntExtra("beautician_sort", 0) == 30) {
							if (jData.has("price30")
									&& !jData.isNull("price30")) {
								textview_go_home_name_and_price.setText(Utils
										.getTextAndColor("上门服务  ",
												"¥" + jData.getInt("price30")));
								textview_service_text_price_beau_home
										.setText(getTextPrice("¥"
												+ jData.getInt("price30")));
							}
							if (jData.has("petServicePojo")
									&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData
										.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")
										&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo
											.getJSONObject("targetPets");
									if (targetPets.has("home")
											&& !targetPets.isNull("home")) {
										JSONObject home = targetPets
												.getJSONObject("home");
										if (home.has("30")
												&& !home.isNull("30")) {
											JSONObject object30 = home
													.getJSONObject("30");
											if (object30.has("btnTxt")
													&& !object30
															.isNull("btnTxt")) {
												String btnTxt = object30
														.getString("btnTxt");
												button_go_apponit_home
														.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}
							if (jData.has("listPrice30")
									&& !jData.isNull("listPrice30")) {
								old_listprice.setText(jData
										.getString("listPrice30"));
								no_old_price.setVisibility(View.GONE);
							} else {
								old_listprice.setVisibility(View.GONE);
								textview_service_text_price_beau_home
										.setVisibility(View.GONE);
								no_old_price.setVisibility(View.VISIBLE);
								no_old_price.setText(getTextPrice("¥"
										+ jData.getInt("price30")));
							}
							if (jData.has("shopPrice30")
									&& !jData.isNull("shopPrice30")) {
								textview_go_shop_name_and_price
										.setText(Utils.getTextAndColor(
												"到店服务  ",
												"¥"
														+ jData.getInt("shopPrice30")));
								textview_service_text_price_beau_shop
										.setText(getTextPrice("¥"
												+ jData.getInt("shopPrice30")));
							}
							if (jData.has("petServicePojo")&& !jData.isNull("petServicePojo")) {
								JSONObject petServicePojo = jData.getJSONObject("petServicePojo");
								if (petServicePojo.has("targetPets")&& !petServicePojo.isNull("targetPets")) {
									JSONObject targetPets = petServicePojo.getJSONObject("targetPets");
									if (targetPets.has("shop")&& !targetPets.isNull("shop")) {
										JSONObject shop = targetPets.getJSONObject("shop");
										if (shop.has("30")&& !shop.isNull("30")) {
											JSONObject object30 = shop.getJSONObject("30");
											if (object30.has("btnTxt")&& !object30.isNull("btnTxt")) {
												String btnTxt = object30.getString("btnTxt");
												button_go_apponit_shop.setText(btnTxt);
											}
										}
									}
								}
								if (petServicePojo.has("reminders")
										&& !petServicePojo.isNull("reminders")) {
									JSONArray arrayReminders = petServicePojo
											.getJSONArray("reminders");
									if (arrayReminders.length() > 0) {
										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < arrayReminders
												.length(); i++) {
											builder.append(arrayReminders
													.getString(i) + "\n");
										}
										textview_notice.setText(builder
												.toString());
									}
								}
							}
							if (jData.has("shopListPrice30")
									&& !jData.isNull("shopListPrice30")) {
								old_shoplist.setText(jData
										.getString("shopListPrice30"));
								no_oldshop_price.setVisibility(View.GONE);
							} else {
								old_shoplist.setVisibility(View.GONE);
								textview_service_text_price_beau_shop
										.setVisibility(View.GONE);
								no_oldshop_price.setVisibility(View.VISIBLE);
								no_oldshop_price.setText(getTextPrice("¥"
										+ jData.getInt("shopPrice30")));
							}
						}

						// if (isShowGoHome != -1 && isShowGoShop != -1)
						// {//上门到店都支持
						// la_layout_isshow_go_shop.setVisibility(View.GONE);
						// view_home_and_shop.setVisibility(View.VISIBLE);
						//
						//
						// if (!isBeauticianGoHome) {
						// re_layout_go_home.setVisibility(View.GONE);
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// view_home_and_shop.setVisibility(View.GONE);
						// textview_showwhat.setText("该项服务仅支持到店哦");
						// }
						// if (!isBeauticianGoShop) {
						// re_layout_go_shop.setVisibility(View.GONE);
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// textview_showwhat.setText("该项服务仅支持上门哦");
						// }
						// } else if (isShowGoHome == -1) {
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// view_home_and_shop.setVisibility(View.GONE);
						// re_layout_go_home.setVisibility(View.GONE);
						// textview_showwhat.setText("该项服务仅支持到店哦");
						// if (!isBeauticianGoShop) {
						// re_layout_go_shop.setVisibility(View.GONE);
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// }
						// } else if (isShowGoShop == -1) {
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// re_layout_go_shop.setVisibility(View.GONE);
						// textview_showwhat.setText("该项服务仅支持上门哦");
						// if (!isBeauticianGoHome) {
						// re_layout_go_home.setVisibility(View.GONE);
						// la_layout_isshow_go_shop.setVisibility(View.VISIBLE);
						// }
						// }
						int homePrice10 = -1;
						if (jData.has("price10") && !jData.isNull("price10")) {
							homePrice10 = Utils.formatDouble(jData
									.getDouble("price10"));
							if (homePrice10 == -1) {
								item_service_one_go_home
										.setVisibility(View.GONE);
							} else {
								item_service_one_go_home
										.setVisibility(View.VISIBLE);
								// String lastThrPrice = "¥" + homePrice10;
								// SpannableString styledText =
								// getTextPrice(lastThrPrice);
								textview_item_service_price_one_go_home
										.setText(getTextPrice("¥" + homePrice10));
								no_old_price_one_go_home
										.setText(getTextPrice("¥" + homePrice10));
								if (jData.has("listPrice")
										&& !jData.isNull("listPrice")) {
									old_one_price_go_home
											.setVisibility(View.VISIBLE);
									old_one_price_go_home.setText(jData
											.getString("listPrice"));
								} else {
									old_one_price_go_home
											.setVisibility(View.GONE);
									textview_item_service_price_one_go_home
											.setVisibility(View.GONE);
									no_old_price_one_go_home
											.setVisibility(View.VISIBLE);
								}
							}
						}
						int homePrice20 = -1;
						if (jData.has("price20") && !jData.isNull("price20")) {
							homePrice20 = Utils.formatDouble(jData
									.getDouble("price20"));
							if (homePrice20 == -1) {
								item_service_two_go_home
										.setVisibility(View.GONE);
							} else {
								item_service_two_go_home
										.setVisibility(View.VISIBLE);
								textview_item_service_price_two_go_home
										.setText(getTextPrice("¥" + homePrice20));
								no_old_price_two_go_home
										.setText(getTextPrice("¥" + homePrice20));
								if (jData.has("listPrice20")
										&& !jData.isNull("listPrice20")) {
									old_two_price_go_home
											.setVisibility(View.VISIBLE);
									old_two_price_go_home.setText(jData
											.getString("listPrice20"));
								} else {
									old_two_price_go_home
											.setVisibility(View.GONE);
									textview_item_service_price_two_go_home
											.setVisibility(View.GONE);
									no_old_price_two_go_home
											.setVisibility(View.VISIBLE);
								}
							}
						}
						int homePrice30 = -1;
						if (jData.has("price30") && !jData.isNull("price30")) {
							homePrice30 = Utils.formatDouble(jData
									.getDouble("price30"));
							if (homePrice30 == -1) {
								item_service_thr_go_home
										.setVisibility(View.GONE);
							} else {
								item_service_thr_go_home
										.setVisibility(View.VISIBLE);
								textview_item_service_price_thr_go_home
										.setText(getTextPrice("¥" + homePrice30));
								no_old_price_thr_go_home
										.setText(getTextPrice("¥" + homePrice30));
								if (jData.has("listPrice30")
										&& !jData.isNull("listPrice30")) {
									old_thr_price_go_home
											.setVisibility(View.VISIBLE);
									old_thr_price_go_home.setText(jData
											.getString("listPrice30"));
								} else {
									old_thr_price_go_home
											.setVisibility(View.GONE);
									textview_item_service_price_thr_go_home
											.setVisibility(View.GONE);
									no_old_price_thr_go_home
											.setVisibility(View.VISIBLE);
								}
							}
						}

						if (jData.has("shopPrice10")
								&& !jData.isNull("shopPrice10")) {
							shopPrice10 = Utils.formatDouble(jData
									.getDouble("shopPrice10"));
							if (shopPrice10 == -1) {
								item_service_one_go_shop
										.setVisibility(View.GONE);
							} else {
								item_service_one_go_shop
										.setVisibility(View.VISIBLE);
								textview_item_service_price_one_go_shop
										.setText(getTextPrice("¥" + shopPrice10));
								no_old_price_one_go_shop
										.setText(getTextPrice("¥" + shopPrice10));
								if (jData.has("shopListPrice")
										&& !jData.isNull("shopListPrice")) {
									old_one_price_go_shop
											.setVisibility(View.VISIBLE);
									old_one_price_go_shop.setText(jData
											.getString("shopListPrice"));
								} else {
									old_one_price_go_shop
											.setVisibility(View.GONE);
									textview_item_service_price_one_go_shop
											.setVisibility(View.GONE);
									no_old_price_one_go_shop
											.setVisibility(View.VISIBLE);
								}
							}
						}

						if (jData.has("shopPrice20")
								&& !jData.isNull("shopPrice20")) {
							shopPrice20 = Utils.formatDouble(jData
									.getDouble("shopPrice20"));
							if (shopPrice20 == -1) {
								item_service_two_go_shop
										.setVisibility(View.GONE);
							} else {
								item_service_two_go_shop
										.setVisibility(View.VISIBLE);
								textview_item_service_price_two_go_shop
										.setText(getTextPrice("¥" + shopPrice20));
								no_old_price_two_go_shop
										.setText(getTextPrice("¥" + shopPrice20));
								if (jData.has("shopListPrice20")
										&& !jData.isNull("shopListPrice20")) {
									old_two_price_go_shop
											.setVisibility(View.VISIBLE);
									old_two_price_go_shop.setText(jData
											.getString("shopListPrice20"));
								} else {
									old_two_price_go_shop
											.setVisibility(View.GONE);
									textview_item_service_price_two_go_shop
											.setVisibility(View.GONE);
									no_old_price_two_go_shop
											.setVisibility(View.VISIBLE);
								}
							}
						}

						if (jData.has("shopPrice30")
								&& !jData.isNull("shopPrice30")) {
							shopPrice30 = Utils.formatDouble(jData
									.getDouble("shopPrice30"));
							if (shopPrice30 == -1) {
								item_service_thr_go_shop
										.setVisibility(View.GONE);
							} else {
								item_service_thr_go_shop
										.setVisibility(View.VISIBLE);
								textview_item_service_price_thr_go_shop
										.setText(getTextPrice("¥" + shopPrice30));
								no_old_price_thr_go_shop
										.setText(getTextPrice("¥" + shopPrice30));
								if (jData.has("shopListPrice30")
										&& !jData.isNull("shopListPrice30")) {
									old_thr_price_go_shop
											.setVisibility(View.VISIBLE);
									old_thr_price_go_shop.setText(jData
											.getString("shopListPrice30"));
								} else {
									old_thr_price_go_shop
											.setVisibility(View.GONE);
									textview_item_service_price_thr_go_shop
											.setVisibility(View.GONE);
									no_old_price_thr_go_shop
											.setVisibility(View.VISIBLE);
								}
							}
						}
						if (homePrice10 != -1) {
							textview_service_text_price_from
									.setText(homePrice10 + "");
						} else {// 就是homePrice10==-1情况
								// 如果homePrice20！=-1
							if (homePrice20 != -1) {
								textview_service_text_price_from
										.setText(homePrice20 + "");
							} else {// homePrice20==-1的情况
								if (homePrice30 != -1) {
									textview_service_text_price_from
											.setText(homePrice30 + "");
								}
							}
						}
						if (shopPrice10 != -1) {
							textview_service_text_price_from_go_shop
									.setText(shopPrice10 + "");
						} else {
							if (shopPrice20 != -1) {
								textview_service_text_price_from_go_shop
										.setText(shopPrice20 + "");
							} else {
								if (shopPrice30 != -1) {
									textview_service_text_price_from_go_shop
											.setText(shopPrice30 + "");
								}
							}
						}
						isopenHome = false;
						isopenShop = false;
						service_content_header.setVisibility(View.VISIBLE);
						imageview_service_arrow_go_home
								.setBackgroundResource(R.drawable.service_icon_arrow_up);
						imageview_service_arrow_go_shop
								.setBackgroundResource(R.drawable.service_icon_arrow_up);
						boolean isCanHome;
						if (homePrice10 == -1 && homePrice20 == -1
								&& homePrice30 == -1) {// 如果上门都不支持
							re_layout_go_home.setVisibility(View.GONE);
							service_item_show_go_home.setVisibility(View.GONE);
							textview_showwhat.setText("该宠物仅支持到店服务哦~");
							isCanHome = false;
						} else {
							isCanHome = true;
							textview_go_home_name_and_price.setText("上门服务");
							re_layout_go_home.setVisibility(View.VISIBLE);
							// service_item_show_go_home.setVisibility(View.VISIBLE);
							service_item_show_go_home.setVisibility(View.GONE);
							if (!isBeauticianGoHome) {// 美容师不支持上门
								re_layout_go_home.setVisibility(View.GONE);
								service_item_show_go_home
										.setVisibility(View.GONE);
								isCanHome = false;
								textview_showwhat.setText("该美容师服务仅支持到店哦~");
							} else {
								re_layout_go_home.setVisibility(View.VISIBLE);
								service_item_show_go_home
										.setVisibility(View.GONE);
								isCanHome = true;
							}
						}
						boolean isCanShop;
						if (shopPrice10 == -1 && shopPrice20 == -1
								&& shopPrice30 == -1) {// 全等级不支持到店
							re_layout_go_shop.setVisibility(View.GONE);
							service_item_show_go_shop.setVisibility(View.GONE);
							textview_showwhat.setText("该宠物仅支持上门服务哦~");
							isCanShop = false;
						} else {// 支持到店
							isCanShop = true;
							textview_go_shop_name_and_price.setText("到店服务");
							re_layout_go_shop.setVisibility(View.VISIBLE);
							// service_item_show_go_shop.setVisibility(View.VISIBLE);
							service_item_show_go_shop.setVisibility(View.GONE);
							if (!isBeauticianGoShop) {
								re_layout_go_shop.setVisibility(View.GONE);
								service_item_show_go_shop
										.setVisibility(View.GONE);
								textview_showwhat.setText("该美容师仅支持上门哦~");
								isCanShop = false;
							} else {
								re_layout_go_shop.setVisibility(View.VISIBLE);
								// service_item_show_go_shop.setVisibility(View.VISIBLE);
								service_item_show_go_shop
										.setVisibility(View.GONE);
								isCanShop = true;
							}
						}

						if (isCanHome && isCanShop) {
							la_layout_isshow_go_shop.setVisibility(View.GONE);
						} else {
							la_layout_isshow_go_shop
									.setVisibility(View.VISIBLE);
							if (isCanHome) {
								// textview_showwhat.setText("该美容师仅支持上门服务哦~");
							} else if (isCanShop) {
								// textview_showwhat.setText("该美容师仅支持到店服务哦~");
							} else if (!isCanShop && !isCanHome) {
								// textview_showwhat.setText("该美容师暂不支持此项服务哦~");
							}

						}
						if (fIntent.getIntExtra("beautician_sort", 0) > 0) {
							if (!isBeauticianGoHome) {
								textview_showwhat.setText("该美容师仅支持到店服务哦");
							}
							if (!isBeauticianGoShop) {
								textview_showwhat.setText("该美容师仅支持上门服务哦");
							}
						}
						if (jData.has("comments") && !jData.isNull("comments")) {
							service_layout_eva_one.setVisibility(View.GONE);
							service_layout_eva_two.setVisibility(View.GONE);
							// setComments(jData);
						} else {
							service_layout_eva_one.setVisibility(View.GONE);
							service_layout_eva_two.setVisibility(View.GONE);
						}
						if (jData.has("listPriceTxt")
								&& !jData.isNull("listPriceTxt")
								&& !"".equals(jData.getString("listPriceTxt")
										.trim())
								&& !"null".equals(jData.getString(
										"listPriceTxt").toLowerCase())) {
						}

						if (jData.has("petServicePojo")&& !jData.isNull("petServicePojo")) {
							JSONObject jService = jData.getJSONObject("petServicePojo");
							if (jService.has("name")&& !jService.isNull("name")) {
								textView_service_name_show.setText(jService.getString("name"));
							}
							if (jService.has("reminders")&& !jService.isNull("reminders")) {
								JSONArray arrayReminders = jService.getJSONArray("reminders");
								if (arrayReminders.length() > 0) {
									StringBuilder builder = new StringBuilder();
									for (int i = 0; i < arrayReminders.length(); i++) {
										builder.append(arrayReminders.getString(i) + "\n");
									}
									textview_notice.setText(builder.toString());
								}
							}
							boolean isShopOneLine = false;
							boolean isShopTwoLine = false;
							boolean isShopThrLine = false;
							if (jService.has("targetPets")&& !jService.isNull("targetPets")) {
								JSONObject targetPet = jService.getJSONObject("targetPets");
								if (targetPet.has("shop")&& !targetPet.isNull("shop")) {
									JSONObject objectLevelDesc = targetPet.getJSONObject("shop");
									if (objectLevelDesc.has("isHot")&& !objectLevelDesc.isNull("isHot")) {
										int isHot = objectLevelDesc.getInt("isHot");
										if (isHot == 0) {
											layout_hot.setVisibility(View.GONE);
										} else if (isHot == 1) {
											layout_hot.setVisibility(View.VISIBLE);
										}
									}
									if (objectLevelDesc.has("10")&& !objectLevelDesc.isNull("10")) {
										isShopOneLine = true;
										JSONObject object10 = objectLevelDesc.getJSONObject("10");
										if (object10.has("title")&& !object10.isNull("title")) {
											textview_item_service_name_one_go_shop.setText(object10.getString("title"));
										}
										if (object10.has("desc")&& !object10.isNull("desc")) {
											textview_item_service_detail_one_go_shop.setText(object10.getString("desc"));
										}
										if (object10.has("btnTxt")&& !object10.isNull("btnTxt")) {
											String btnTxt = object10.getString("btnTxt");
											button_go_apponit_one_go_shop.setText(btnTxt);
										}
									}
									if (objectLevelDesc.has("20")&& !objectLevelDesc.isNull("20")) {
										isShopTwoLine = true;
										JSONObject object20 = objectLevelDesc.getJSONObject("20");
										if (object20.has("title")&& !object20.isNull("title")) {
											textview_item_service_name_two_go_shop.setText(object20.getString("title"));
										}
										if (object20.has("desc")&& !object20.isNull("desc")) {
											textview_item_service_detail_two_go_shop.setText(object20.getString("desc"));
										}
										if (object20.has("btnTxt")&& !object20.isNull("btnTxt")) {
											String btnTxt = object20.getString("btnTxt");
											button_go_apponit_two_go_shop.setText(btnTxt);
										}
									}
									if (objectLevelDesc.has("30")&& !objectLevelDesc.isNull("30")) {
										isShopThrLine = true;
										JSONObject object30 = objectLevelDesc.getJSONObject("30");
										if (object30.has("title")&& !object30.isNull("title")) {
											textview_item_service_name_thr_go_shop.setText(object30.getString("title"));
										}
										if (object30.has("desc")&& !object30.isNull("desc")) {
											textview_item_service_detail_thr_go_shop.setText(object30.getString("desc"));
										}
										if (object30.has("btnTxt")&& !object30.isNull("btnTxt")) {
											String btnTxt = object30.getString("btnTxt");
											button_go_apponit_thr_go_shop.setText(btnTxt);
										}
									}
									
//									if (isShopOneLine&&!isShopTwoLine&&!isShopThrLine) {
//										item_service_line_one_go_shop.setVisibility(View.GONE);
//										item_service_line_two_go_shop.setVisibility(View.GONE);
//										item_service_line_thr_go_shop.setVisibility(View.GONE);
//									}else if (!isShopThrLine&&isShopTwoLine&&!isShopThrLine) {
//										item_service_line_one_go_shop.setVisibility(View.GONE);
//										item_service_line_two_go_shop.setVisibility(View.GONE);
//										item_service_line_thr_go_shop.setVisibility(View.GONE);
//									}else if (!isShopThrLine&&!isShopTwoLine&&isShopThrLine) {
//										item_service_line_one_go_shop.setVisibility(View.GONE);
//										item_service_line_two_go_shop.setVisibility(View.GONE);
//										item_service_line_thr_go_shop.setVisibility(View.GONE);
//									}
								}else {
									item_service_line_one_go_shop.setVisibility(View.GONE);
									item_service_line_two_go_shop.setVisibility(View.GONE);
									item_service_line_thr_go_shop.setVisibility(View.GONE);
									
								}
								if (targetPet.has("home")&& !targetPet.isNull("home")) {
									JSONObject objectLevelDesc = targetPet.getJSONObject("home");
									if (objectLevelDesc.has("extraServiceFee")&& !objectLevelDesc.isNull("extraServiceFee")) {
										extraServiceFee = objectLevelDesc.getDouble("extraServiceFee");
										if (extraServiceFee>0) {
											if (objectLevelDesc.has("extraServiceFeeTag")&&!objectLevelDesc.isNull("extraServiceFeeTag")) {
												extraServiceFeeTag = objectLevelDesc.getString("extraServiceFeeTag");
											}
											textview_go_home_service_price_detail.setText(extraServiceFeeTag);
										}else {
											textview_go_home_service_price_detail.setVisibility(View.GONE);
										}
									} else {
										textview_go_home_service_price_detail.setVisibility(View.GONE);
									}
									if (objectLevelDesc.has("10")&& !objectLevelDesc.isNull("10")) {
										JSONObject object10 = objectLevelDesc.getJSONObject("10");
										if (object10.has("title")&& !object10.isNull("title")) {
											textview_item_service_name_one_go_home.setText(object10.getString("title"));
										}
										if (object10.has("desc")&& !object10.isNull("desc")) {
											textview_item_service_detail_one_go_home.setText(object10.getString("desc"));
										}
										if (object10.has("btnTxt")&& !object10.isNull("btnTxt")) {
											String btnTxt = object10.getString("btnTxt");
											button_go_apponit_one_go_home.setText(btnTxt);
										}
									}
									if (objectLevelDesc.has("20")&& !objectLevelDesc.isNull("20")) {
										JSONObject object20 = objectLevelDesc.getJSONObject("20");
										if (object20.has("title")&& !object20.isNull("title")) {
											textview_item_service_name_two_go_home.setText(object20.getString("title"));
										}
										if (object20.has("desc")&& !object20.isNull("desc")) {
											textview_item_service_detail_two_go_home.setText(object20.getString("desc"));

										}
										if (object20.has("btnTxt")&& !object20.isNull("btnTxt")) {
											String btnTxt = object20.getString("btnTxt");
											button_go_apponit_two_go_home.setText(btnTxt);
										}
									}
									if (objectLevelDesc.has("30")&& !objectLevelDesc.isNull("30")) {
										JSONObject object30 = objectLevelDesc.getJSONObject("30");
										if (object30.has("title")&& !object30.isNull("title")) {
											textview_item_service_name_thr_go_home.setText(object30.getString("title"));
										}
										if (object30.has("desc")&& !object30.isNull("desc")) {
											textview_item_service_detail_thr_go_home.setText(object30.getString("desc"));
										}
										if (object30.has("btnTxt")&& !object30.isNull("btnTxt")) {
											String btnTxt = object30.getString("btnTxt");
											button_go_apponit_thr_go_home.setText(btnTxt);
										}
									}
								}
							}
							// if (jService.has("serviceItem")&&
							// !jService.isNull("serviceItem")) {
							// JSONArray jItems =
							// jService.getJSONArray("serviceItem");
							// for (int i = 0; i < jItems.length(); i++) {
							// JSONObject jItem = jItems.getJSONObject(i);
							// if (jItem.has("itemName")&&
							// !jItem.isNull("itemName")) {
							// serviceitems.add(jItem.getString("itemName"));
							// }
							// }
							// siAdapter.notifyDataSetChanged();
							// }

						}

						if (jData.has("pet") && !jData.isNull("pet")) {
							JSONObject jPet = jData.getJSONObject("pet");
							Pet pet = Pet.json2Entity(jPet);
							petid = pet.id;
							petkind = pet.kindid;
							if (pet.name != null && !"".equals(pet.name)) {
								petname = pet.name;
								if (customerpetid > 0) {
									textView_service_name_show
											.setText(customerpetname
													+ "专属"
													+ textView_service_name_show
															.getText()
															.toString());
								} else {
									if (jPet.has("petName")
											&& !jPet.isNull("petName")) {
										textView_service_name_show.setText(jPet
												.getString("petName").trim()
												+ textView_service_name_show
														.getText().toString());
									}
								}
							}
						}

					} else {
						ToastUtil.showToastShort(ServiceActivity.this,
								"您选择的宠物没有此类服务，请重新选择！");

					}

				} else {
					ToastUtil.showToastShort(ServiceActivity.this, msg);
				}
				pDialog.closeDialog();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showMain(false);
				// ToastUtil.showToastShort(ServiceActivity.this, "网络异常，请重试！");
				pDialog.closeDialog();
			}

		}

		private void setComments(JSONObject jData) throws JSONException {
			try {
				JSONArray array = jData.getJSONArray("comments");
				if (array.length() == 0) {
					service_layout_eva_one.setVisibility(View.INVISIBLE);
					service_layout_eva_two.setVisibility(View.INVISIBLE);
				} else if (array.length() == 1) {
					service_layout_eva_one.setVisibility(View.VISIBLE);
					service_layout_eva_two.setVisibility(View.INVISIBLE);
					JSONObject objectOne = array.getJSONObject(0);
					String realNameOne = "";
					if (objectOne.has("realName")
							&& !objectOne.isNull("realName")) {
						realNameOne = objectOne.getString("realName");
					}
					String contentOne = "";
					if (objectOne.has("content")
							&& !objectOne.isNull("content")) {
						contentOne = objectOne.getString("content");
					}
					textView_service_eva_one.setText(realNameOne + ": "
							+ contentOne);
					if (objectOne.has("avatar") && !objectOne.isNull("avatar")) {
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()
								+ objectOne.getString("avatar"),
								service_eva_one, 0, null);
					}
					if (objectOne.has("user") && !objectOne.isNull("user")) {
						JSONObject objectUser = objectOne.getJSONObject("user");
						if (objectUser.has("userMemberLevel")
								&& !objectUser.isNull("userMemberLevel")) {
							JSONObject objectUserLevel = objectUser
									.getJSONObject("userMemberLevel");
							if (objectUserLevel.has("memberIcon")
									&& !objectUserLevel.isNull("memberIcon")) {
								service_eva_is_member_one
										.setVisibility(View.VISIBLE);
								String memberIcon = objectUserLevel
										.getString("memberIcon");
								if (!memberIcon.startsWith("http")) {
									memberIcon = CommUtil
											.getServiceNobacklash()
											+ memberIcon;
								}
								ImageLoaderUtil.setImage(
										service_eva_is_member_one, memberIcon,
										0);
							} else {
								service_eva_is_member_one
										.setVisibility(View.GONE);
							}
						}
					}
				} else if (array.length() == 2) {
					service_layout_eva_one.setVisibility(View.VISIBLE);
					service_layout_eva_two.setVisibility(View.VISIBLE);
					JSONObject objectOne = array.getJSONObject(0);
					String realNameOne = "";
					if (objectOne.has("realName")
							&& !objectOne.isNull("realName")) {
						realNameOne = objectOne.getString("realName");
					}
					String contentOne = "";
					if (objectOne.has("content")
							&& !objectOne.isNull("content")) {
						contentOne = objectOne.getString("content");
					}
					textView_service_eva_one.setText(realNameOne + ": "
							+ contentOne);
					if (objectOne.has("user") && !objectOne.isNull("user")) {
						JSONObject objectUser = objectOne.getJSONObject("user");
						if (objectUser.has("userMemberLevel")
								&& !objectUser.isNull("userMemberLevel")) {
							JSONObject objectUserLevel = objectUser
									.getJSONObject("userMemberLevel");
							if (objectUserLevel.has("memberIcon")
									&& !objectUserLevel.isNull("memberIcon")) {
								service_eva_is_member_one
										.setVisibility(View.VISIBLE);
								String memberIcon = objectUserLevel
										.getString("memberIcon");
								if (!memberIcon.startsWith("http")) {
									memberIcon = CommUtil
											.getServiceNobacklash()
											+ memberIcon;
								}
								ImageLoaderUtil.setImage(
										service_eva_is_member_one, memberIcon,
										0);
							} else {
								service_eva_is_member_one
										.setVisibility(View.GONE);
							}
						}
					}

					JSONObject objectTwo = array.getJSONObject(1);
					String realNameTwo = "";
					if (objectTwo.has("realName")
							&& !objectTwo.isNull("realName")) {
						realNameTwo = objectTwo.getString("realName");
					}
					String contentTwo = "";
					if (objectTwo.has("content")
							&& !objectTwo.isNull("content")) {
						contentTwo = objectTwo.getString("content");
					}
					textView_service_eva_two.setText(realNameTwo + ": "
							+ contentTwo);
					if (objectOne.has("avatar") && !objectOne.isNull("avatar")) {
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()
								+ objectOne.getString("avatar"),
								service_eva_one, 0, null);
					}
					if (objectTwo.has("avatar") && !objectTwo.isNull("avatar")) {
						ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()
								+ objectTwo.getString("avatar"),
								service_eva_two, 0, null);
					}
					if (objectTwo.has("user") && !objectTwo.isNull("user")) {
						JSONObject objectUser = objectTwo.getJSONObject("user");
						if (objectUser.has("userMemberLevel")
								&& !objectUser.isNull("userMemberLevel")) {
							JSONObject objectUserLevel = objectUser
									.getJSONObject("userMemberLevel");
							if (objectUserLevel.has("memberIcon")
									&& !objectUserLevel.isNull("memberIcon")) {
								service_eva_is_member_two
										.setVisibility(View.VISIBLE);
								String memberIcon = objectUserLevel
										.getString("memberIcon");
								if (!memberIcon.startsWith("http")) {
									memberIcon = CommUtil
											.getServiceNobacklash()
											+ memberIcon;
								}
								ImageLoaderUtil.setImage(
										service_eva_is_member_two, memberIcon,
										0);
							} else {
								service_eva_is_member_two
										.setVisibility(View.GONE);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			showMain(false);
			ToastUtil.showToastShort(ServiceActivity.this, "网络异常，请重试！");
		}

	};

	private void sendEvent(String event) {
		CommUtil.recorder(this, spUtil.getString("cellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this), event, 0,
				spUtil.getInt("userid", 0), 0, 0, eventHanler);
	}

	private AsyncHttpResponseHandler eventHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub

		}

	};

	private void getData() {
		if (previous == Global.PRE_MAINFRAGMENT||previous == Global.MAIN_TO_BEAUTICIANLIST||previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST) {
			// 已经登录且有宠物
			petid = spUtil.getInt("petid", 0);
			petkind = spUtil.getInt("petkind", 0);
			customerpetid = spUtil.getInt("customerpetid", 0);
			customerpetname = spUtil.getString("customerpetname", "");
			String myPetImage = spUtil.getString("mypetImage", "");
			if (myPetImage.equals("")) {
				String strImg = spUtil.getString("petimage", "");
				if (!TextUtils.isEmpty(strImg)) {
					if (strImg.contains("http")) {
						avatarPath = strImg;
					}else {
						avatarPath = CommUtil.getServiceNobacklash()+ spUtil.getString("petimage", "");
					}
				}
			} else {
				if (myPetImage.contains("http")) {
					avatarPath = myPetImage;
				}else {
					avatarPath = CommUtil.getServiceBaseUrl()+myPetImage;
				}
			}
			// avatarPath =
			// CommUtil.getServiceNobacklash()+spUtil.getString("petimage", "");
			Utils.mLogError("customerpetname==" + customerpetname);
			Utils.mLogError("customerpetid==" + customerpetid);
			if (petkind == 1) {
				if (serviceid == 3)
					serviceid = 1;
				if (serviceid == 4)
					serviceid = 2;
			} else if (petkind == 2) {
				if (serviceid == 1)
					serviceid = 3;
				if (serviceid == 2)
					serviceid = 4;
			}
			getServiceData(serviceid, petid, 0);
		} else if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY || previous == Global.BEAUTICIAN_TO_APPOINTMENT) {
			// 没有宠物
			petid = fIntent.getIntExtra("petid", 0);
			petkind = fIntent.getIntExtra("petkind", 0);
			avatarPath = fIntent.getStringExtra("petimage");
			if (petkind == 1) {
				if (serviceid == 3)
					serviceid = 1;
				if (serviceid == 4)
					serviceid = 2;
			} else if (petkind == 2) {
				if (serviceid == 1)
					serviceid = 3;
				if (serviceid == 2)
					serviceid = 4;
			}
			getServiceData(serviceid, petid, 0);
		} else if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
			// 从我的宠物详情跳转下单
			petid = fIntent.getIntExtra("petid", 0);
			petkind = fIntent.getIntExtra("petkind", 0);
			avatarPath = fIntent.getStringExtra("petimage");
			getServiceData(serviceid, petid, 0);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (rpvBanner != null)
			rpvBanner.resume();
		MobclickAgent.onPageStart("ServiceActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);// 统计时长

		int mWidth = Utils.getDisplayMetrics(this)[0];
		LayoutParams layoutParams = rl_ppllayout_top.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = mWidth * 1 / 2;
		rl_ppllayout_top.setLayoutParams(layoutParams);

		tvChangepet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvChangepet.getPaint().setAntiAlias(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (rpvBanner != null)
			rpvBanner.pause();
		MobclickAgent.onPageEnd("ServiceActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
	}

	private AsyncHttpResponseHandler getShare = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->str " + new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						shareList.add(Share.json2Entity(object));
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
	private PullPushLayout pplLayout;
	private RelativeLayout rlTitle;
	private ImageView service_back_blow;
	private ImageView service_share_below;
	private LinearLayout llChangepet;
	private LinearLayout llComments;
	private TextView tvGoodComments;
	private TextView tvBadComments;
	private RelativeLayout rlMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private Button btRefresh;
	// private MyGridView gvServiceItems;
	// private ServiceItemAdapter siAdapter;
	private TextView tvChangepet;

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
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				ServiceActivity.this, appId, appKey);
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
		UMWXHandler wxHandler = new UMWXHandler(ServiceActivity.this, appId,
				appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ServiceActivity.this,
				appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent(String url, String message, String targUrl) {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSinaCallbackUrl("http://www.baidu.com");
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				ServiceActivity.this, "1104724367", "gASimi0oEHprSSxe");
		qZoneSsoHandler.addToSocialSDK();
		mController.setShareContent(message);
		mController.setShareImage(new UMImage(ServiceActivity.this, url));

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setTitle(message);
		circleMedia.setShareContent(message);
		circleMedia.setShareImage(new UMImage(ServiceActivity.this, url));
		circleMedia.setTargetUrl(targUrl);
		mController.setShareMedia(circleMedia);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setTitle(message);
		weixinContent.setShareContent(message);
		weixinContent.setShareImage(new UMImage(ServiceActivity.this, url));
		weixinContent.setTargetUrl(targUrl);
		mController.setShareMedia(weixinContent);

		// QQShareContent qqShareContent = new QQShareContent();
		// qqShareContent.setShareContent(message);
		// qqShareContent.setShareImage(new UMImage(mainActivity, url));
		// mController.setShareMedia(qqShareContent);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTitle(message);
		sinaContent.setShareContent(message + targUrl);
		sinaContent.setShareImage(new UMImage(ServiceActivity.this, url));
		sinaContent.setTargetUrl(targUrl);
		mController.setShareMedia(sinaContent);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setTitle(" ");// 不分享title 但是title处不可为null否则显示分享到QQ空间
		qzone.setShareContent(message);
		qzone.setShareImage(new UMImage(ServiceActivity.this, url));
		qzone.setTargetUrl(targUrl);
		mController.setShareMedia(qzone);

	}

	private SpannableString getTextPrice(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.service_old_service_one), 0, 1,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.service_old_service_two), 1, lastOnePrice.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

	private SpannableString getTextScore(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.service_score_left), 0, lastOnePrice.length() - 1,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.service_score_right), lastOnePrice.length() - 1,
				lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private void goAd(Context context, String url, Class cls) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", url);
		startActivity(intent);
	}
}
