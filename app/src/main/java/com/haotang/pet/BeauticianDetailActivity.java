package com.haotang.pet;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.Production;
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
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BeauticianDetailActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private ImageView srivProduction1;
	private ImageView srivProduction2;
	private ImageView srivProduction3;
	private ImageView srivProduction4;
	private ImageView srivProduction5;
	private ImageView srivProduction6;
	private SelectableRoundedImageView srivBeauticianImage;
	private TextView tvBeauticianName;
	private TextView tvBeauticianOrderNum;
	// private TextView tvBeauticianSign;
	// private TextView tvBeauticianIntroduction;
	// private TextView tvCommentNum;
	private TextView tvProductionNum;
	private RelativeLayout rlProductionMore;
	private LinearLayout llProductions;
	private LinearLayout llProduction1;
	private LinearLayout llProduction2;
	private LinearLayout llProduction3;
	private Button btBooking;
	private PullToRefreshScrollView prsScrollView;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;

	private int beautician_id;
	private int beautician_level;
	private int beautician_stars;
	private int beautician_ordernum;
	private String beautician_name;
	private String beautician_iamge;
	private String beautician_titlelevel;
	private int gender;
	private int sort;
	private String levelname;
	private double bathmultiple = 1;
	private double beautymultiple = 1;
	private double factormultiple = 1;
	public static ArrayList<Production> productionList = new ArrayList<Production>();
	public static ArrayList<Comment> commentList = new ArrayList<Comment>();
	private int previous = 0;

	private TextView beau_detail_tag;
	private LinearLayout beau_detail_layout_to_app;
	private int isAvail;

	// ------- START 2016-6-4
	private TextView tv_beautician_address;

	private TextView tv_servicedetail_goodcomments;
	private TextView tv_servicedetail_badcomments;
	private LinearLayout service_layout_eva_one;
	private LinearLayout service_layout_eva_two;
	private LinearLayout item_eva_layout;
	private SelectableRoundedImageView service_eva_one;
	private SelectableRoundedImageView service_eva_two;
	private TextView textView_service_eva_one;
	private TextView textView_service_eva_two;

	private View view_productions_line;
	private LinearLayout layout_service_go_home;
	private LinearLayout layout_service_go_shop;

//	private LinearLayout layout_speciality_one;
//	private LinearLayout layout_speciality_two;

//	private TextView textview_speciality_one;
//	private TextView textview_speciality_two;

	private TextView textview_major_grade;
	private TextView textview_service_grade;
	private TextView textview_exchange_grade;
	private int areaid;
	private int shopid;
	private boolean Ishome = false;
	private boolean Isshop = false;
	private int shopServiceSchedule = 0;
	private int homeServiceSchedule = 0;
	private String areaname = "";
	private String areaName = "";
	public static BeauticianDetailActivity act;
	private DecimalFormat df = new DecimalFormat("0.0");

	// 美容师评价第一条
	private LinearLayout beau_layout_coment;
	private ImageView beau_icon;
	private ImageView service_eva_is_member_one;
	private TextView beau_name;
	private TextView beau_custom_eva;
	private ImageView beau_product_one;
	private ImageView beau_product_two;
	private ImageView beau_product_thr;
	// 美容师评价第二条
	private LinearLayout beau_layout_coment_two;
	private ImageView beau_icon_two;
	private ImageView service_eva_is_member_two;
	private TextView beau_name_two;
	private TextView beau_custom_eva_two;
	private ImageView beau_product_one_two;
	private ImageView beau_product_two_two;
	private ImageView beau_product_thr_two;

	private Comment commentOne = null;
	private Comment commentTwo = null;
	private double lng;
	private double lat;

	// ------- END 2016-6-4
	private TextView textview_isgo_update;
	private String upgradeTip = null;
	private int FromAreaid=0;
	private String apptime;
	private TextView relayout_inside_text;
	private ImageView relayout_inside_img;
	private RelativeLayout relayout_out;
	private LinearLayout layout_instro_out;
	
	private boolean isHintShrink;//是否可收缩
	private boolean isShrink=true;//是否收缩

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.beauticiandetail);
		setContentView(R.layout.beauticiandetail_new);
		this.act = this;
		MApplication.listAppoint.add(this);
		findView();
		setView();
	}

	private void findView() {
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);

		prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_beauticiandetail_scrollview);

		srivBeauticianImage = (SelectableRoundedImageView) findViewById(R.id.sriv_beauticiandetail_beautician);
		tvBeauticianName = (TextView) findViewById(R.id.tv_beauticiandetail_beauticianname);
		tvBeauticianOrderNum = (TextView) findViewById(R.id.tv_beauticiandetail_ordernum);
		tvProductionNum = (TextView) findViewById(R.id.tv_beauticiandetail_productions_num);
		rlProductionMore = (RelativeLayout) findViewById(R.id.rl_beauticiandetail_productions_more);
		llProductions = (LinearLayout) findViewById(R.id.ll_beauticiandetail_productions);
		llProduction1 = (LinearLayout) findViewById(R.id.ll_beauticiandetail_productions_1);
		llProduction2 = (LinearLayout) findViewById(R.id.ll_beauticiandetail_productions_2);
		llProduction3 = (LinearLayout) findViewById(R.id.ll_beauticiandetail_productions_3);

		srivProduction1 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_1);
		srivProduction2 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_2);
		srivProduction3 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_3);
		srivProduction4 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_4);
		srivProduction5 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_5);
		srivProduction6 = (ImageView) findViewById(R.id.sriv_beauticiandetail_productions_6);

		btBooking = (Button) findViewById(R.id.bt_beauticiandetail_booking);

		beau_detail_tag = (TextView) findViewById(R.id.beau_detail_tag);
		beau_detail_layout_to_app = (LinearLayout) findViewById(R.id.beau_detail_layout_to_app);

		tv_beautician_address = (TextView) findViewById(R.id.tv_beautician_address);
		tv_servicedetail_goodcomments = (TextView) findViewById(R.id.tv_servicedetail_goodcomments);
		tv_servicedetail_badcomments = (TextView) findViewById(R.id.tv_servicedetail_badcomments);

		service_layout_eva_one = (LinearLayout) findViewById(R.id.service_layout_eva_one);
		service_layout_eva_two = (LinearLayout) findViewById(R.id.service_layout_eva_two);
		item_eva_layout = (LinearLayout) findViewById(R.id.item_eva_layout);

		service_eva_one = (SelectableRoundedImageView) findViewById(R.id.service_eva_one);
		service_eva_two = (SelectableRoundedImageView) findViewById(R.id.service_eva_two);

		textView_service_eva_one = (TextView) findViewById(R.id.textView_service_eva_one);
		textView_service_eva_two = (TextView) findViewById(R.id.textView_service_eva_two);

		view_productions_line = (View) findViewById(R.id.view_productions_line);
		layout_service_go_home = (LinearLayout) findViewById(R.id.layout_service_go_home);
		layout_service_go_shop = (LinearLayout) findViewById(R.id.layout_service_go_shop);
//		layout_speciality_one = (LinearLayout) findViewById(R.id.layout_speciality_one);
//		layout_speciality_two = (LinearLayout) findViewById(R.id.layout_speciality_two);
//		textview_speciality_one = (TextView) findViewById(R.id.textview_speciality_one);
//		textview_speciality_two = (TextView) findViewById(R.id.textview_speciality_two);
		textview_major_grade = (TextView) findViewById(R.id.textview_major_grade);
		textview_service_grade = (TextView) findViewById(R.id.textview_service_grade);
		textview_exchange_grade = (TextView) findViewById(R.id.textview_exchange_grade);
		// 第一条美容师评价
		beau_layout_coment = (LinearLayout) findViewById(R.id.beau_layout_coment);
		beau_icon = (ImageView) findViewById(R.id.beau_icon);
		service_eva_is_member_one = (ImageView) findViewById(R.id.service_eva_is_member_one);
		beau_name = (TextView) findViewById(R.id.beau_name);
		beau_custom_eva = (TextView) findViewById(R.id.beau_custom_eva);
		beau_product_one = (ImageView) findViewById(R.id.beau_product_one);
		beau_product_two = (ImageView) findViewById(R.id.beau_product_two);
		beau_product_thr = (ImageView) findViewById(R.id.beau_product_thr);
		// 第二条美容师评价
		beau_layout_coment_two = (LinearLayout) findViewById(R.id.beau_layout_coment_two);
		beau_icon_two = (ImageView) findViewById(R.id.beau_icon_two);
		service_eva_is_member_two = (ImageView) findViewById(R.id.service_eva_is_member_two);
		beau_name_two = (TextView) findViewById(R.id.beau_name_two);
		beau_custom_eva_two = (TextView) findViewById(R.id.beau_custom_eva_two);
		beau_product_one_two = (ImageView) findViewById(R.id.beau_product_one_two);
		beau_product_two_two = (ImageView) findViewById(R.id.beau_product_two_two);
		beau_product_thr_two = (ImageView) findViewById(R.id.beau_product_thr_two);
		
		textview_isgo_update = (TextView) findViewById(R.id.textview_isgo_update);

		service_layout_eva_one.setVisibility(View.GONE);
		service_layout_eva_two.setVisibility(View.GONE);
		
		
		relayout_inside_text = (TextView) findViewById(R.id.relayout_inside_text);
		relayout_inside_img = (ImageView) findViewById(R.id.relayout_inside_img);
		relayout_out = (RelativeLayout) findViewById(R.id.relayout_out);
		layout_instro_out = (LinearLayout) findViewById(R.id.layout_instro_out);
		
	}

	private void setView() {
		tvTitle.setText("美容师主页");
		fIntent = getIntent();
		apptime = fIntent.getStringExtra("apptime");
		areaName = fIntent.getStringExtra("areaName");
		FromAreaid = fIntent.getIntExtra("areaid", 0);
		beautician_id = fIntent.getIntExtra("beautician_id", 0);
		lng = fIntent.getDoubleExtra("lng", 0);
		lat = fIntent.getDoubleExtra("lat", 0);
		beautician_id = fIntent.getIntExtra("beautician_id", 0);
		isAvail = fIntent.getIntExtra("isavail", -1);
		previous = getIntent().getIntExtra("previous", 0);
		if (previous == Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL) {
			btBooking.setVisibility(View.GONE);
			beau_detail_layout_to_app.setVisibility(View.GONE);
		} else if (previous == Global.MAIN_TO_BEAUTICIANLIST) {
			beautician_id = getIntent().getIntExtra("id", 0);
		}
		btBooking.setText("预约");
		int imageWidth = getDisplayWidth();
		setProductionHeight(srivProduction1, imageWidth);
		setProductionHeight(srivProduction2, imageWidth);
		setProductionHeight(srivProduction3, imageWidth);
		setProductionHeight(srivProduction4, imageWidth);
		setProductionHeight(srivProduction5, imageWidth);
		setProductionHeight(srivProduction6, imageWidth);

		prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 下拉刷新数据
				Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					getData(beautician_id,FromAreaid);
				}
			}
		});

		beau_product_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(0, commentOne.images);
			}
		});
		beau_product_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(1, commentOne.images);
			}
		});
		beau_product_thr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(2, commentOne.images);
			}
		});
		beau_product_one_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(0, commentTwo.images);
			}
		});
		beau_product_two_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(1, commentTwo.images);
			}
		});
		beau_product_thr_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goCommentImage(2, commentTwo.images);
			}
		});
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回
				goBack();
			}
		});
		item_eva_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更多评价
				goNext(beautician_id, BeauticianCommentsListActivity.class);
			}
		});
		beau_layout_coment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更多评价
				goNext(beautician_id, BeauticianCommentsListActivity.class);
			}
		});
		beau_layout_coment_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更多评价
				goNext(beautician_id, BeauticianCommentsListActivity.class);
			}
		});
		rlProductionMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更多作品
				goNext(beautician_id, BeauticianProductuonActivity.class);
			}
		});
		relayout_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//展开收起介绍
				if(isHintShrink){
					setHintExt();
				}
			}
		});
		btBooking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (previous == Global.MAIN_TO_BEAUTICIANLIST) {
					Intent data = new Intent(BeauticianDetailActivity.this,
							BeauToAppointmentActivity.class);
					data.putExtra("areaname", areaname);
					data.putExtra("homeServiceSchedule", homeServiceSchedule);
					data.putExtra("shopServiceSchedule", shopServiceSchedule);
					data.putExtra("isshop", Isshop);
					if (spUtil.getInt("tareaid", 0) <= 0
							|| spUtil.getInt("tareaid", 0) == 100) {
						data.putExtra("ishome", false);
					} else {
						data.putExtra("ishome", Ishome);
					}
					data.putExtra("areaid", areaid);
					data.putExtra("shopid", shopid);
					data.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
					data.putExtra("beautician_id", beautician_id);
					data.putExtra("beautician_name", beautician_name);
					data.putExtra("beautician_image", beautician_iamge);
					data.putExtra("beautician_ordernum", beautician_ordernum);
					data.putExtra("beautician_sex", gender);
					data.putExtra("beautician_sort", sort);
					data.putExtra("clicksort", sort);
					data.putExtra("beautician_levelname", levelname);
					data.putExtra("upgradeTip", upgradeTip);

					startActivity(data);
					overridePendingTransition(R.anim.down_in, R.anim.down_out);

				} else if (isAvail == 3) {
					Intent data = new Intent();
					data.putExtra("beautician_id", beautician_id);
					data.putExtra("beautician_name", beautician_name);
					data.putExtra("beautician_image", beautician_iamge);
					data.putExtra("beautician_level", beautician_level);
					data.putExtra("beautician_stars", beautician_stars);
					data.putExtra("beautician_ordernum", beautician_ordernum);
					data.putExtra("beautician_titlelevel",
							beautician_titlelevel);
					data.putExtra("beautician_sex", gender);
					data.putExtra("beautician_sort", sort);
					data.putExtra("clicksort", sort);
					data.putExtra("beautician_bathmultiple", bathmultiple);
					data.putExtra("beautician_beautymultiple", beautymultiple);
					data.putExtra("beautician_factormultiple", factormultiple);
					data.putExtra("beautician_levelname", levelname);
					data.putExtra("upgradeTip", upgradeTip);
					setResult(Global.RESULT_OK, data);
					finishWithAnimation();
				} else {
					Intent data = new Intent();
					data.putExtra("beautician_id", beautician_id);
					data.putExtra("beautician_name", beautician_name);
					data.putExtra("beautician_image", beautician_iamge);
					data.putExtra("beautician_level", beautician_level);
					data.putExtra("beautician_stars", beautician_stars);
					data.putExtra("beautician_ordernum", beautician_ordernum);
					data.putExtra("beautician_titlelevel",
							beautician_titlelevel);
					data.putExtra("beautician_sex", gender);
					data.putExtra("beautician_sort", sort);
					data.putExtra("clicksort", sort);
					data.putExtra("beautician_bathmultiple", bathmultiple);
					data.putExtra("beautician_beautymultiple", beautymultiple);
					data.putExtra("beautician_factormultiple", factormultiple);
					data.putExtra("beautician_levelname", levelname);
					data.putExtra("upgradeTip", upgradeTip);
					if (BeauticianListActivity.act != null) {
						BeauticianListActivity.act.setResult(Global.RESULT_OK,
								data);
						BeauticianListActivity.act.finishWithAnimation();
					} else {
						setResult(Global.RESULT_OK, data);
					}
					finishWithAnimation();
				}

			}
		});
		srivProduction1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(0, BeauticianProductionDetailActivity.class);
			}
		});
		srivProduction2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(1, BeauticianProductionDetailActivity.class);
			}
		});
		srivProduction3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(2, BeauticianProductionDetailActivity.class);
			}
		});
		srivProduction4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(3, BeauticianProductionDetailActivity.class);
			}
		});
		srivProduction5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(4, BeauticianProductionDetailActivity.class);
			}
		});
		srivProduction6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goProdeuction(5, BeauticianProductionDetailActivity.class);
			}
		});

		getData(beautician_id,FromAreaid);

	}

	// private boolean isLogin(){
	// if(spUtil.getInt("userid", -1) >
	// 0&&!"".equals(spUtil.getString("cellphone", "")))
	// return true;
	// return false;
	// }
	// private boolean hasPet(){
	// if(spUtil.getInt("petid", -1) > 0)
	// return true;
	// return false;
	// }
	private void goNext(int beauticianid, Class clazz) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("beautician_id", beauticianid);
		startActivity(intent);
	}

	private void goCommentImage(int index, String[] pics) {
		Intent intent = new Intent(this, BeauticianCommonPicActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("pics", pics);

		startActivity(intent);
	}

	// private void setImage(String path, ImageView iv){
	// iv.setTag(path);
	// iv.setImageResource(R.drawable.icon_production_default);
	//
	// if(path != null && !"".equals(path)){
	// MainActivity.ilUtil.dispaly(path, iv, new ImageLoadingListener() {
	//
	// @Override
	// public void onLoadingStarted(String arg0, View view) {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onLoadingComplete(String path, View view, Bitmap bitmap) {
	// // TODO Auto-generated method stub
	// if(view!=null&&bitmap!=null){
	// ImageView iv = (ImageView) view;
	// String imagetag = (String) iv.getTag();
	// if(path!=null&&path.equals(imagetag)){
	// iv.setImageBitmap(bitmap);
	// }
	// }
	// }
	//
	// @Override
	// public void onLoadingCancelled(String arg0, View arg1) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// }

	private void goProdeuction(int index, Class clazz) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("index", index);
		intent.putExtra("previous",
				Global.PRE_BEAUTICIANDETAIL_TO_PRODUCTIONDETAIL);
		startActivity(intent);
	}

	private int getDisplayWidth() {
		int[] display = Utils.getDisplayMetrics(this);
		int border = Utils.dip2px(this, 30);
		return display[0] / 2 - border / 2;
	}

	private void setProductionHeight(ImageView iv, int height) {
		LayoutParams lParams = iv.getLayoutParams();
		lParams.width = height;
		lParams.height = height;
	}

	private void getData(int id,int FromAreaid) {
		pDialog.showDialog();
		CommUtil.getBeauticianDetail(this, id,FromAreaid,apptime,BeauticianHanler);
	}

	private AsyncHttpResponseHandler BeauticianHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prsScrollView.onRefreshComplete();
			// pDialog.closeDialog();
			Utils.mLogError("美容师详情：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");

				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jData = jobj.getJSONObject("data");
					if (jData.has("gender") && !jData.isNull("gender")) {
						gender = jData.getInt("gender");

					}
					if (jData.has("areaId") && !jData.isNull("areaId")) {
						areaid = jData.getInt("areaId");
						if (FromAreaid>0) {
							if (areaid!=FromAreaid) {
								areaid = FromAreaid;
							}
						}
					}
					if (jData.has("shopId") && !jData.isNull("shopId")) {
						shopid = jData.getInt("shopId");

					}
					if (jData.has("level") && !jData.isNull("level")) {
						JSONObject objectLevel = jData.getJSONObject("level");
						if (objectLevel.has("name")
								&& !objectLevel.isNull("name")) {
							levelname = objectLevel.getString("name");
							beau_detail_tag.setText(levelname);
						}
						if (objectLevel.has("sort")
								&& !objectLevel.isNull("sort")) {
							sort = objectLevel.getInt("sort");
						}
						if (objectLevel.has("priceFactorShower")
								&& !objectLevel.isNull("priceFactorShower")) {
							bathmultiple = objectLevel
									.getDouble("priceFactorShower");
						}
						if (objectLevel.has("priceFactorBeauty")
								&& !objectLevel.isNull("priceFactorBeauty")) {
							beautymultiple = objectLevel
									.getDouble("priceFactorBeauty");
						}
						if (objectLevel.has("priceFactorSpecial")
								&& !objectLevel.isNull("priceFactorSpecial")) {
							factormultiple = objectLevel
									.getDouble("priceFactorSpecial");
						}
					}
					if (jData.has("avatar") && !jData.isNull("avatar")
							&& !"".equals(jData.getString("avatar").trim())) {
						beautician_iamge = CommUtil.getServiceNobacklash()
								+ jData.getString("avatar").trim();
						ImageLoaderUtil.loadImg(beautician_iamge,
								srivBeauticianImage, 0, null);

					}
					if (jData.has("upgradeTip")&&!jData.isNull("upgradeTip")) {
						upgradeTip = jData.getString("upgradeTip");
						textview_isgo_update.setVisibility(View.VISIBLE);
						textview_isgo_update.setText(upgradeTip);
					}else {
						textview_isgo_update.setVisibility(View.GONE);
					}
					if (jData.has("realName") && !jData.isNull("realName")) {
						tvBeauticianName.setText(jData.getString("realName"));
						beautician_name = jData.getString("realName");
					}
					if (jData.has("title") && !jData.isNull("title")
							&& !"".equals(jData.getString("title").trim())) {
						beautician_titlelevel = jData.getString("title");
					}
					if (jData.has("totalOrderAmount")
							&& !jData.isNull("totalOrderAmount")) {
						tvBeauticianOrderNum.setText("接单:"
								+ jData.getInt("totalOrderAmount"));
						beautician_ordernum = jData.getInt("totalOrderAmount");
					}
					if (jData.has("workerGrade")
							&& !jData.isNull("workerGrade")) {
						int grade = jData.getInt("workerGrade");
						beautician_level = grade / 10;
						beautician_stars = grade % 10;
					}
//					if (jData.has("expertises") && !jData.isNull("expertises")) {
//						JSONArray jexp = jData.getJSONArray("expertises");
//						if (jexp.length() > 0) {
//							if (jexp.length() == 1) {
//								layout_speciality_one.setVisibility(View.VISIBLE);
//								layout_speciality_two.setVisibility(View.GONE);
//								for (int i = 0; i < jexp.length(); i++) {
//									JSONObject objectExp = jexp.getJSONObject(i);
//									if (objectExp.has("itemName")&& !objectExp.isNull("itemName")) {
//										textview_speciality_one.setText(objectExp.getString("itemName"));
//									}
//								}
//							} else if (jexp.length() == 2) {
//								setSpeciality(jexp);
//							} else if (jexp.length() > 2) {
//								setSpeciality(jexp);
//							}
//						} else {
//							layout_speciality_one.setVisibility(View.GONE);
//							layout_speciality_two.setVisibility(View.GONE);
//						}
//					}
					if (jData.has("introduction")&&!jData.isNull("introduction")) {
						String introduction = jData.getString("introduction");
//						introduction="胜多负少的丰盛的水电费水电费是sdf水电费水电费手动sdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdf第三方sdfsdf手动sdfsdfsdf手动胜多负少的丰盛的水电费水电费是sdf水电费水电费手动sdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdf第三方sdfsdf手动sdfsdfsdf手动";
						SpannableString spans = new SpannableString(introduction);
//						spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 
//								startindex, startindex+endindex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						relayout_inside_text.setText(spans);
						setHint();
					}else {
						layout_instro_out.setVisibility(View.GONE);
					}
					if (jData.has("workCount") && !jData.isNull("workCount")) {
						int workCount = jData.getInt("workCount");
						tvProductionNum.setText("作品(" + workCount + ")");
					}
					if (jData.has("works") && !jData.isNull("works")) {
						JSONArray jWorks = jData.getJSONArray("works");
						productionList.clear();
						showBeauticianProductions(jWorks.length());
						for (int i = 0; i < jWorks.length(); i++) {
							Production pr = Production.json2Entity(jWorks
									.getJSONObject(i));
							productionList.add(pr);
							setBeauticianProduction(i + 1, pr);
						}

					} else {
						productionList.clear();
						showBeauticianProductions(0);
					}

					if (jData.has("realGrade1") && !jData.isNull("realGrade1")) {
						double realGrade1 = jData.getDouble("realGrade1");
						textview_major_grade.setText("专业："
								+ df.format(realGrade1));
					}
					if (jData.has("realGrade2") && !jData.isNull("realGrade2")) {
						double realGrade2 = jData.getDouble("realGrade2");
						textview_service_grade.setText("服务："
								+ df.format(realGrade2));
					}
					if (jData.has("realGrade3") && !jData.isNull("realGrade3")) {
						double realGrade3 = jData.getDouble("realGrade3");
						textview_exchange_grade.setText("守时："
								+ df.format(realGrade3));
					}
					if (jData.has("homeServiceSchedule")
							&& !jData.isNull("homeServiceSchedule")) {
						homeServiceSchedule = jData
								.getInt("homeServiceSchedule");
						if (homeServiceSchedule == 0) {
							Ishome = false;
							layout_service_go_home.setVisibility(View.GONE);
						} else if (homeServiceSchedule == 1) {
							layout_service_go_home.setVisibility(View.VISIBLE);
							Ishome = true;
						}
					}
					if (jData.has("shopServiceSchedule")
							&& !jData.isNull("shopServiceSchedule")) {
						shopServiceSchedule = jData
								.getInt("shopServiceSchedule");
						if (shopServiceSchedule == 0) {
							Isshop = false;
							layout_service_go_shop.setVisibility(View.GONE);
						} else if (shopServiceSchedule == 1) {
							layout_service_go_shop.setVisibility(View.VISIBLE);
							Isshop = true;
						}
					}
//					if (jData.has("cs") && !jData.isNull("cs")) {
//						JSONObject csJson = jData.getJSONObject("cs");
//						if (csJson.has("hp") && !csJson.isNull("hp")) {
//							tv_servicedetail_goodcomments.setText(Utils.getTextAndColorComments("好评：",csJson.getInt("hp") + "条"));
//						}
//						if (csJson.has("cp") && !csJson.isNull("cp")) {
//							tv_servicedetail_badcomments.setText(Utils
//									.getTextAndColorComments("差评：",
//											csJson.getInt("cp") + "条"));
//						}
//					}
					if (jData.has("goodRate")&&!jData.isNull("goodRate")) {
						tv_servicedetail_goodcomments.setText("好评率："+(int)(jData.getDouble("goodRate")*100)+"%");
					}
					if (jData.has("grade")&&!jData.isNull("grade")) {
						tv_servicedetail_badcomments.setText("评分："+jData.getDouble("grade"));
					}
					if (jData.has("area") && !jData.isNull("area")) {
						JSONObject object = jData.getJSONObject("area");
						if (object.has("name") && !object.isNull("name")) {
							areaname = object.getString("name");
							if (!TextUtils.isEmpty(areaName)) {
								if (!areaname.equals(areaName)) {
									areaname = areaName;
								}
							}
							tv_beautician_address.setText(object
									.getString("name"));
						} else {
							tv_beautician_address.setVisibility(View.GONE);
						}
					}
					if (jData.has("comments") && !jData.isNull("comments")) {
						setComments(jData);
					} else {
						beau_layout_coment.setVisibility(View.GONE);
						beau_layout_coment_two.setVisibility(View.GONE);
					}

				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						ToastUtil.showToastShort(BeauticianDetailActivity.this,
								msg);
					}
				}
				pDialog.closeDialog();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pDialog.closeDialog();
			}

		}

//		private void setSpeciality(JSONArray jexp) throws JSONException {
//			layout_speciality_one.setVisibility(View.VISIBLE);
//			layout_speciality_two.setVisibility(View.VISIBLE);
//			for (int i = 0; i < jexp.length(); i++) {
//				JSONObject objectExp = jexp.getJSONObject(i);
//				if (objectExp.has("itemName") && !objectExp.isNull("itemName")) {
//					if (i == 0) {
//						textview_speciality_one.setText(objectExp
//								.getString("itemName"));
//					} else if (i == 1) {
//						textview_speciality_two.setText(objectExp
//								.getString("itemName"));
//					}
//				}
//			}
//		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			prsScrollView.onRefreshComplete();
			pDialog.closeDialog();
		}

	};
	private Intent fIntent;

	private void setBeauticianProduction(int index, Production pr) {
		if (pr == null)
			return;
		switch (index) {
		case 1:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction1,
						R.drawable.icon_image_default, null);

			break;
		case 2:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction2,
						R.drawable.icon_image_default, null);

			break;
		case 3:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction3,
						R.drawable.icon_image_default, null);

			break;
		case 4:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction4,
						R.drawable.icon_image_default, null);

			break;
		case 5:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction5,
						R.drawable.icon_image_default, null);

			break;
		case 6:
			if (pr.smallimage != null && !"".equals(pr.smallimage.trim()))
				ImageLoaderUtil.loadImg(pr.smallimage, srivProduction6,
						R.drawable.icon_image_default, null);

			break;
		}
	}

	private void showBeauticianProductions(int productions) {
		llProduction1.setVisibility(View.GONE);
		llProduction2.setVisibility(View.GONE);
		llProduction3.setVisibility(View.GONE);

		switch (productions) {
		case 0:
			llProductions.setVisibility(View.VISIBLE);
			view_productions_line.setVisibility(View.VISIBLE);
			break;
		case 1:
			llProduction1.setVisibility(View.VISIBLE);
			srivProduction2.setBackgroundResource(R.drawable.bk_empty);
			srivProduction2.setVisibility(View.INVISIBLE);
			llProduction2.setVisibility(View.GONE);
			llProduction3.setVisibility(View.GONE);
			break;
		case 2:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.GONE);
			llProduction3.setVisibility(View.GONE);
			break;
		case 3:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.VISIBLE);
			llProduction3.setVisibility(View.GONE);
			srivProduction4.setVisibility(View.INVISIBLE);
			srivProduction4.setBackgroundResource(R.drawable.bk_empty);
			break;
		case 4:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.VISIBLE);
			llProduction3.setVisibility(View.GONE);
			break;
		case 5:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.VISIBLE);
			llProduction3.setVisibility(View.VISIBLE);
			srivProduction6.setVisibility(View.INVISIBLE);
			srivProduction6.setBackgroundResource(R.drawable.bk_empty);
			break;
		case 6:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.VISIBLE);
			llProduction3.setVisibility(View.VISIBLE);
			break;
		default:
			llProduction1.setVisibility(View.VISIBLE);
			llProduction2.setVisibility(View.VISIBLE);
			llProduction3.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		finishWithAnimation();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == Global.RESULT_OK
				&& (requestCode == Global.SERVICEREQUESTCODE_BEAUTICIAN || requestCode == Global.BEAUTICIAN_TO_TIME)) {
			Intent intent = new Intent();
			intent.putExtra("time", data.getStringExtra("time"));
			intent.putExtra("date", data.getStringExtra("date"));
			intent.putExtra("strListWokerId",
					data.getStringExtra("strListWokerId"));
			intent.putExtra("position", data.getIntExtra("position", 0));
			intent.putExtra("beautician_name", beautician_name);
			intent.putExtra("beautician_sex", gender);
			intent.putExtra("beautician_level", beautician_level);
			intent.putExtra("beautician_stars", beautician_stars);
			intent.putExtra("beautician_titlelevel", beautician_titlelevel);
			intent.putExtra("beautician_image", beautician_iamge);
			intent.putExtra("beautician_ordernum", beautician_ordernum);
			intent.putExtra("beautician_id", beautician_id);
			intent.putExtra("beautician_sort", sort);
			intent.putExtra("clicksort",sort);
			intent.putExtra("beautician_levelname", levelname);
			intent.putExtra("beautician_bathmultiple", bathmultiple);
			intent.putExtra("beautician_beautymultiple", beautymultiple);
			intent.putExtra("beautician_factormultiple", factormultiple);

			setResult(Global.RESULT_OK, intent);
			finishWithAnimation();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("BeauticianDetailActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("BeauticianDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
																// onPageEnd
																// 在onPause
																// 之前调用,因为
																// onPause
																// 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& KeyEvent.ACTION_DOWN == event.getAction()) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setComments(JSONObject jData) throws JSONException {
		JSONArray array = jData.getJSONArray("comments");
		if (array.length() == 0) {
			beau_layout_coment.setVisibility(View.GONE);
			beau_layout_coment_two.setVisibility(View.GONE);
		} else if (array.length() == 1) {
			beau_layout_coment.setVisibility(View.VISIBLE);
			beau_layout_coment_two.setVisibility(View.GONE);
			commentOne = Comment.json2Entity(array.getJSONObject(0));
			if (commentOne != null) {
				if (commentOne.images != null) {
					setBeauEvaShow(commentOne, beau_product_one,
							beau_product_two, beau_product_thr);
				}
				beau_name.setText(commentOne.name);
				beau_custom_eva.setText(commentOne.content);
				if (!commentOne.image.equals("")) {
					ImageLoaderUtil.loadImg(commentOne.image, beau_icon, 0,
							null);
				}
				if (!TextUtils.isEmpty(commentOne.memberIcon)&&commentOne.memberIcon!=null) {
					service_eva_is_member_one.setVisibility(View.VISIBLE);
					if (!commentOne.memberIcon.startsWith("http")) {
						commentOne.memberIcon = CommUtil.getServiceNobacklash()+commentOne.memberIcon;
					}
					ImageLoaderUtil.setImage(service_eva_is_member_one, commentOne.memberIcon, 0);
				}else {
					service_eva_is_member_one.setVisibility(View.GONE);
				}
			}
		} else if (array.length() == 2) {
			beau_layout_coment.setVisibility(View.VISIBLE);
			beau_layout_coment_two.setVisibility(View.VISIBLE);
			commentOne = Comment.json2Entity(array.getJSONObject(0));
			if (commentOne != null) {
				if (commentOne.images != null) {
					setBeauEvaShow(commentOne, beau_product_one,
							beau_product_two, beau_product_thr);
				}
				beau_name.setText(commentOne.name);
				beau_custom_eva.setText(commentOne.content);
				if (!commentOne.image.equals("")) {
					ImageLoaderUtil.loadImg(commentOne.image, beau_icon, 0,
							null);
				}
				if (!TextUtils.isEmpty(commentOne.memberIcon)&&commentOne.memberIcon!=null) {
					service_eva_is_member_one.setVisibility(View.VISIBLE);
					if (!commentOne.memberIcon.startsWith("http")) {
						commentOne.memberIcon = CommUtil.getServiceNobacklash()+commentOne.memberIcon;
					}
					ImageLoaderUtil.setImage(service_eva_is_member_one, commentOne.memberIcon, 0);
				}else {
					service_eva_is_member_one.setVisibility(View.GONE);
				}
			}

			commentTwo = Comment.json2Entity(array.getJSONObject(1));
			if (commentTwo != null) {
				if (commentTwo.images != null) {
					setBeauEvaShow(commentTwo, beau_product_one_two,
							beau_product_two_two, beau_product_thr_two);
				}
				beau_name_two.setText(commentTwo.name);
				beau_custom_eva_two.setText(commentTwo.content);
				if (!commentTwo.image.equals("")) {
					ImageLoaderUtil.loadImg(commentTwo.image, beau_icon_two, 0,
							null);
				}
				if (!TextUtils.isEmpty(commentTwo.memberIcon)&&commentTwo.memberIcon!=null) {
					service_eva_is_member_two.setVisibility(View.VISIBLE);
					if (!commentTwo.memberIcon.startsWith("http")) {
						commentTwo.memberIcon = CommUtil.getServiceNobacklash()+commentTwo.memberIcon;
					}
					ImageLoaderUtil.setImage(service_eva_is_member_two, commentTwo.memberIcon, 0);
				}else {
					service_eva_is_member_two.setVisibility(View.GONE);
				}
			}
		}
	}

	private void setBeauEvaShow(Comment comment, ImageView ShowOne,
			ImageView ShowTwo, ImageView ShowThr) {
		if (comment.images.length == 1) {
			ShowOne.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImg(comment.images[0], ShowOne,
					R.drawable.icon_beautician_default, null);
		} else if (comment.images.length == 2) {
			ShowOne.setVisibility(View.VISIBLE);
			ShowTwo.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImg(comment.images[0], ShowOne,
					R.drawable.icon_beautician_default, null);
			ImageLoaderUtil.loadImg(comment.images[1], ShowTwo,
					R.drawable.icon_beautician_default, null);
		} else if (comment.images.length == 3) {
			ShowOne.setVisibility(View.VISIBLE);
			ShowTwo.setVisibility(View.VISIBLE);
			ShowThr.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImg(comment.images[0], ShowOne,
					R.drawable.icon_beautician_default, null);
			ImageLoaderUtil.loadImg(comment.images[1], ShowTwo,
					R.drawable.icon_beautician_default, null);
			ImageLoaderUtil.loadImg(comment.images[2], ShowThr,
					R.drawable.icon_beautician_default, null);
		}
	}
	private void setHintExt(){
		String str = relayout_inside_text.getText().toString().trim();
		if(isShrink){
			isShrink = false;
			relayout_inside_text.setEllipsize(null);
			relayout_inside_text.setSingleLine(false);
			relayout_inside_img.setImageResource(R.drawable.icon_arrow_up_beau);
		}else{
			isShrink = true;
			relayout_inside_img.setImageResource(R.drawable.icon_arrow_down_beau);
			relayout_inside_text.setEllipsize(TruncateAt.END);
			relayout_inside_text.setLines(2);
		}
	}
	private void setHint(){
		relayout_inside_text.post(new Runnable() {
			@Override
			public void run() {
				if(relayout_inside_text.getLineCount() > 2){
					relayout_inside_img.setVisibility(View.VISIBLE);
					relayout_inside_text.setEllipsize(TruncateAt.END);
					relayout_inside_text.setLines(2);
					isHintShrink = true;
				}else{
					isHintShrink = false;
					relayout_inside_img.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
}
