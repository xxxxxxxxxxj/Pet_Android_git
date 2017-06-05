package com.haotang.pet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.GoShopStepAdapter;
import com.haotang.pet.adapter.MPagerAdapter;
import com.haotang.pet.entity.ShopEntity;
import com.haotang.pet.entity.ShopServices;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("NewApi")
public class GoShopDetailActivity extends SuperActivity {
	private ImageView service_back;
	private ImageView service_share;
	private PullPushLayout pplLayout;
	private RelativeLayout rlTitle;
	private ImageView service_back_blow;
	private ImageView service_share_below;
	private Drawable bgBackDrawable;
	private Drawable bgBackBelowDrawable;
	private Drawable bgShareDrawable;
	private Drawable bgShareBelowDrawable;
	private Drawable bgNavBarDrawable;
	private int alphaMax = 180;
	private ViewPager ivPet;
	private LinearLayout point;
	private ArrayList<View> dots;
	private String[] pics = new String[0];
	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	private int oldPostion;
	private MPagerAdapter adapter;
	private GridView gridView_go_shop_show_icon;
	private int currentItem;
	private GoShopStepAdapter shopStepAdapter;
	private ShopEntity shopEntity = null;
	private TextView textView_go_shop_title;
	private TextView textView_go_shop;
	private TextView textView_go_shop_address;
	private ImageView imageView_go_shop_phone;
	private ImageView imageview_go_shop_area;
	private int shopid;
	private int serviceid;
	private MProgressDialog pDialog;
	private int previous = 0;
	private Intent fIntent;

	private ScheduledExecutorService scheduledExecutor;
	private RelativeLayout shop_detail_down;
	private int clicksort;
	private int areaid;
	private TextView textView_title;
	private String servicename;
	private int servicetype;
	private int serviceloc;
	private int petid;
	private int petkind;
	private String petname;
	private int customerpetid;
	private String customerpetname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_go_shop_detail);
		fIntent = getIntent();
		shopid = fIntent.getIntExtra("shopid", 0);
		serviceid = fIntent.getIntExtra("serviceid", 0);
		previous = fIntent.getIntExtra("previous", 0);
		clicksort = fIntent.getIntExtra("clicksort", 0);
		areaid = fIntent.getIntExtra("areaid", 0);
		servicename = fIntent.getStringExtra("servicename");
		servicetype = fIntent.getIntExtra("servicetype", 0);
		serviceloc = fIntent.getIntExtra("serviceloc", 0);
		petid = fIntent.getIntExtra("petid", 0);
		petkind = fIntent.getIntExtra("petkind", 0);
		petname = fIntent.getStringExtra("petname");
		customerpetid = fIntent.getIntExtra("customerpetid", 0);
		customerpetname = fIntent.getStringExtra("customerpetname");
		findView();
		setView();
		getData();
		initListener();
	}

	private void initListener() {
		service_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishWithAnimation();
			}
		});
		btSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ShopListActivity.act != null) {
					if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST) {
						Intent intent = new Intent(GoShopDetailActivity.this,
								ServiceActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_RIGHT());
						getIntent().putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_LEFT());
						intent.putExtra("previous", previous);
						intent.putExtra("clicksort", clicksort);
						intent.putExtra("serviceid",
								fIntent.getIntExtra("serviceid", 0));
						intent.putExtra("servicetype",
								fIntent.getIntExtra("servicetype", 0));
						intent.putExtra("serviceloc",
								fIntent.getIntExtra("serviceloc", 0));
						intent.putExtra("petid",
								fIntent.getIntExtra("petid", 0));
						intent.putExtra("petkind",
								fIntent.getIntExtra("petkind", 0));
						intent.putExtra("petname",
								fIntent.getStringExtra("petname"));
						intent.putExtra("shopid",
								fIntent.getIntExtra("shopid", 0));
						intent.putExtra("customerpetid",
								fIntent.getIntExtra("customerpetid", 0));
						intent.putExtra("customerpetname",
								fIntent.getStringExtra("customerpetname"));
						intent.putExtra("shopid", shopid);
						intent.putExtra("shopname", shopEntity.shopName);
						intent.putExtra("shopimg", shopEntity.shopimg);
						intent.putExtra("shopaddr", shopEntity.address);
						intent.putExtra("shoptel", shopEntity.phone);
						intent.putExtra("areaid", areaid);
						startActivity(intent);
					} else if(previous == Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST){
						Intent intent = new Intent(GoShopDetailActivity.this,
								ServiceFeature.class);
						intent.putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_RIGHT());
						getIntent().putExtra(Global.ANIM_DIRECTION(),
								Global.ANIM_COVER_FROM_LEFT());
						intent.putExtra("servicename",
								fIntent.getStringExtra("servicename"));
						intent.putExtra("previous", previous);
						intent.putExtra("clicksort", clicksort);
						intent.putExtra("serviceid",
								fIntent.getIntExtra("serviceid", 0));
						intent.putExtra("servicetype",
								fIntent.getIntExtra("servicetype", 0));
						intent.putExtra("serviceloc",
								fIntent.getIntExtra("serviceloc", 0));
						intent.putExtra("petid",
								fIntent.getIntExtra("petid", 0));
						intent.putExtra("petkind",
								fIntent.getIntExtra("petkind", 0));
						intent.putExtra("petname",
								fIntent.getStringExtra("petname"));
						intent.putExtra("shopid",
								fIntent.getIntExtra("shopid", 0));
						intent.putExtra("customerpetid",
								fIntent.getIntExtra("customerpetid", 0));
						intent.putExtra("customerpetname",
								fIntent.getStringExtra("customerpetname"));
						intent.putExtra("shopid", shopid);
						intent.putExtra("shopname", shopEntity.shopName);
						intent.putExtra("shopimg", shopEntity.shopimg);
						intent.putExtra("shopaddr", shopEntity.address);
						intent.putExtra("shoptel", shopEntity.phone);
						intent.putExtra("areaid", areaid);
						startActivity(intent);
					}else{
						Intent data = new Intent();
						data.putExtra("areaid", areaid);
						data.putExtra("shopid", shopid);
						data.putExtra("shopname", shopEntity.shopName);
						data.putExtra("shopimg", shopEntity.shopimg);
						data.putExtra("shopaddr", shopEntity.address);
						data.putExtra("shoptel", shopEntity.phone);
						ShopListActivity.act.setResult(Global.RESULT_OK, data);
					}
					ShopListActivity.act.finishWithAnimation();
				}
				finishWithAnimation();
			}
		});
		imageView_go_shop_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (shopEntity != null) {
						if (!shopEntity.phone.equals("")
								|| shopEntity.phone != null) {
							MDialog mDialog = new MDialog.Builder(
									GoShopDetailActivity.this).setTitle("提示")
									.setType(MDialog.DIALOGTYPE_CONFIRM)
									.setMessage("是否拨打电话?").setCancelStr("否")
									.setOKStr("是")
									.positiveListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// 确认拨打电话
											Utils.telePhoneBroadcast(
													GoShopDetailActivity.this,
													shopEntity.phone);
										}
									}).build();
							mDialog.show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		imageView_go_shop_nav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GoShopDetailActivity.this,
						ShopLocationActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("shopname", shopEntity.shopName);
				intent.putExtra("shopaddr", shopEntity.address);
				intent.putExtra("shoplat", shopEntity.lat);
				intent.putExtra("shoplng", shopEntity.lng);
				startActivity(intent);
			}
		});

//		gridView_go_shop_show_icon.setOnItemClickListener(new OnItemClickListener() {//这个我先试试这版本做不做先看情况
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				ShopServices shopServicesEvery = (ShopServices) parent.getItemAtPosition(position);
//				goService(shopServicesEvery.point);
//			}
//
//		});
	}
	private void goService(int point) {
		switch (point) {
		case 1://洗澡
			if (petkind==1) {
				serviceid = 1;
			}else {
				serviceid = 3;
			}
			goNext(Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,ServiceActivity.class);
			break;
		case 2://2美容
			if (petkind==1) {
				serviceid = 2;
			}else {
				serviceid = 4;
			}
			goNext(Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY,ServiceActivity.class);
			break;
		case 3://寄养
			goNext(0, FostercareChooseroomActivity.class);
			break;
		case 4://游泳
			goNext(Global.SWIM_MYPET_TO_SWIMAPPOINTMENT,SwimDetailActivity.class);
			break;
		case 5://训犬
			goNext(Global.MAIN_TO_TRAIN_DETAIL,TrainAppointMentActivity.class);
			break;
		case 6://特色服务
			goNext(Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST,CharacteristicServiceActivity.class);
			break;
		}
	}
	private void goNext(int pre,Class cls){
		Intent intent = new Intent(mContext, cls);
		intent.putExtra("previous", pre);
		intent.putExtra("areaid", areaid);
		intent.putExtra("petid", petid);
		intent.putExtra("petkind", petkind);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("customerpetname", customerpetname);
		intent.putExtra("petname", petname);
//		intent.putExtra("petimage", petimage);
		Utils.mLogError("==-->shopDetail areaid:="+areaid+" petid:="+petid+" petkind:= "+petkind+" serviceid:= "+serviceid+" customerpetid:= "+customerpetid+" customerpetname:= "+customerpetname);
		startActivity(intent);
	}
	private void findView() {
		shopEntity = new ShopEntity();
		pDialog = new MProgressDialog(this);
		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
		service_back = (ImageView) findViewById(R.id.service_back);
		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
		service_share = (ImageView) findViewById(R.id.service_share);
		service_share_below = (ImageView) findViewById(R.id.service_share_below);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);

		ivPet = (ViewPager) findViewById(R.id.iv_servicedetail_pet);
		point = (LinearLayout) findViewById(R.id.point);

		gridView_go_shop_show_icon = (GridView) findViewById(R.id.gridView_go_shop_show_icon);
		textView_go_shop_title = (TextView) findViewById(R.id.textView_go_shop_title);
		textView_go_shop = (TextView) findViewById(R.id.textView_go_shop);
		textView_go_shop_address = (TextView) findViewById(R.id.textView_go_shop_address);
		imageView_go_shop_phone = (ImageView) findViewById(R.id.imageView_go_shop_phone);
		imageView_go_shop_nav = (ImageView) findViewById(R.id.imageView_go_shop_nav);
		imageview_go_shop_area = (ImageView) findViewById(R.id.imageview_go_shop_area);
		btSubmit = (Button) findViewById(R.id.bt_shopdetail_submit);
		shop_detail_down = (RelativeLayout) findViewById(R.id.shop_detail_down);
		textView_title = (TextView) findViewById(R.id.textView_title);

	}

	private void setView() {
		textView_title.setText("店铺详情");
		if (previous == Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL) {
			shop_detail_down.setVisibility(View.GONE);
		}
//		styleShow();
		imageViews = new ArrayList<ImageView>();
		dots = new ArrayList<View>();
		shopStepAdapter = new GoShopStepAdapter(GoShopDetailActivity.this,
				shopEntity);
		gridView_go_shop_show_icon.setAdapter(shopStepAdapter);
	}

	private void styleShow() {
		pplLayout.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {

			@Override
			public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSlide(int alpha) {
				// TODO Auto-generated method stub
				int alphaReverse = alphaMax - alpha;
				if (alphaReverse < 0) {
					alphaReverse = 0;
				}
				bgBackDrawable.setAlpha(alphaReverse);
				bgBackBelowDrawable.setAlpha(alpha);
				bgShareDrawable.setAlpha(alphaReverse);
				bgShareBelowDrawable.setAlpha(alpha);
				bgNavBarDrawable.setAlpha(alpha);

			}
		});

		bgBackDrawable = service_back.getBackground();
		bgBackBelowDrawable = service_back_blow.getBackground();
		bgShareDrawable = service_share.getBackground();
		bgShareBelowDrawable = service_share_below.getBackground();
		bgNavBarDrawable = rlTitle.getBackground();
		bgBackDrawable.setAlpha(alphaMax);
		bgShareDrawable.setAlpha(alphaMax);
		bgNavBarDrawable.setAlpha(0);
		bgBackBelowDrawable.setAlpha(0);
		bgShareBelowDrawable.setAlpha(0);
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.GetShopInfo(this, shopid, serviceid, getShopDetail);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutor.scheduleAtFixedRate(new MRunnable(), 3, 3,
				TimeUnit.SECONDS);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scheduledExecutor.shutdown();
	}

	private AsyncHttpResponseHandler getShopDetail = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			setData(responseBody);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}

	};
	private Button btSubmit;

	private void setData(byte[] responseBody) {
		try {
			Utils.mLogError("==-->shop " + new String(responseBody));
			JSONObject jsonObject = new JSONObject(new String(responseBody));
			int code = jsonObject.getInt("code");
			if (code == 0) {
				if (jsonObject.has("data") && !jsonObject.isNull("data")) {
					JSONObject object = jsonObject.getJSONObject("data");
					if (object.has("serviceItems")&& !object.isNull("serviceItems")) {
						JSONArray array = object.getJSONArray("serviceItems");
						for (int i = 0; i < array.length(); i++) {
							ShopServices shopServices = new ShopServices();
							JSONObject objectServices = array.getJSONObject(i);
							if (objectServices.has("point")&& !objectServices.isNull("point")) {
								shopServices.point = objectServices.getInt("point");
							}
							if (objectServices.has("itemName")&& !objectServices.isNull("itemName")) {
								shopServices.name = objectServices.getString("itemName");
							}
							if (objectServices.has("img")&& !objectServices.isNull("img")) {
								if (objectServices.getString("img").contains("http")) {
									shopServices.img = objectServices.getString("img");
								}else {
									shopServices.img = CommUtil.getServiceNobacklash()+ objectServices.getString("img");
								}
							}
							shopEntity.list.add(shopServices);
						}
					}
					shopStepAdapter.notifyDataSetChanged();
					if (object.has("areaImg") && !object.isNull("areaImg")) {
						shopEntity.areaImg = CommUtil.getServiceNobacklash()
								+ object.getString("areaImg");
						;

						ImageLoaderUtil.loadImg(shopEntity.areaImg,
								imageview_go_shop_area, 0,
								new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0,
											View arg1) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingFailed(String arg0,
											View arg1, FailReason arg2) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingComplete(String path,
											View view, Bitmap bitmap) {
										// TODO Auto-generated method stub
										if (view != null && bitmap != null) {
											ImageView iv = (ImageView) view;
											iv.setImageBitmap(bitmap);
										}
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {
										// TODO Auto-generated method stub

									}
								});
					}
					if (object.has("shopName") && !object.isNull("shopName")) {
						shopEntity.shopName = object.getString("shopName");
						textView_go_shop_title.setText(shopEntity.shopName);
					}
					if (object.has("img") && !object.isNull("img")) {
						shopEntity.shopimg = CommUtil.getServiceNobacklash()
								+ object.getString("img");
					}
					if (object.has("operTime") && !object.isNull("operTime")) {
						shopEntity.operTime = object.getString("operTime");
						textView_go_shop.setText("营业时间：" + shopEntity.operTime);
					}
					if (object.has("address") && !object.isNull("address")) {
						shopEntity.address = object.getString("address");
						textView_go_shop_address.setText(shopEntity.address);
						textView_go_shop_address.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (textView_go_shop_address.getWidth() > Utils
										.getDisplayMetrics(GoShopDetailActivity.this)[0]
										- Utils.dip2px(
												GoShopDetailActivity.this, 120)) {

									textView_go_shop_address.setWidth(Utils
											.getDisplayMetrics(GoShopDetailActivity.this)[0]
											- Utils.dip2px(
													GoShopDetailActivity.this,
													120));
								}
							}
						});
					}
					if (object.has("lat") && !object.isNull("lat")) {
						shopEntity.lat = object.getDouble("lat");
					}
					if (object.has("lng") && !object.isNull("lng")) {
						shopEntity.lng = object.getDouble("lng");
					}
					if (object.has("phone") && !object.isNull("phone")) {
						shopEntity.phone = object.getString("phone");
					}
					if (object.has("bannerImg") && !object.isNull("bannerImg")) {
						JSONArray array = object.getJSONArray("bannerImg");
						for (int i = 0; i < array.length(); i++) {
							Utils.mLogError("==-->shoparray "
									+ array.getString(i));
							shopEntity.imgList.add(CommUtil
									.getServiceNobacklash()
									+ array.getString(i));
							ImageView imageView = new ImageView(
									GoShopDetailActivity.this);
							imageView.setScaleType(ScaleType.CENTER_CROP);
							imageView
									.setBackgroundResource(R.drawable.icon_production_default);
							imageViews.add(imageView);
						}
						pics = new String[array.length()];
						for (int i = 0; i < array.length(); i++) {
							pics[i] = CommUtil.getServiceNobacklash()
									+ array.getString(i);
							ImageLoaderUtil.loadImg(
									CommUtil.getServiceNobacklash()
											+ array.getString(i),
									imageViews.get(i), 0,
									new ImageLoadingListener() {

										@Override
										public void onLoadingStarted(
												String arg0, View arg1) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onLoadingFailed(
												String arg0, View arg1,
												FailReason arg2) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onLoadingComplete(
												String path, View view,
												Bitmap bitmap) {
											// TODO Auto-generated method stub
											if (view != null && bitmap != null) {
												ImageView iv = (ImageView) view;
												iv.setImageBitmap(bitmap);
											}
										}

										@Override
										public void onLoadingCancelled(
												String arg0, View arg1) {
											// TODO Auto-generated method stub

										}
									});
						}
						adapter = new MPagerAdapter(mContext);
						ivPet.setAdapter(adapter);
						// adapter.setData(imageViews, pics,1);
						adapter.setData(imageViews, pics, 1);
						ivPet.setOnPageChangeListener(new MPageChangeListener());
						dots.clear();
						point.removeAllViews();
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								15, 15);
						params.leftMargin = 10;
						if (imageViews.size() > 1) {
							for (int i = 0; i < imageViews.size(); i++) {
								if (i == 0) {
									View view = new View(
											GoShopDetailActivity.this);
									view.setBackgroundResource(R.drawable.dot_focused);
									view.setPadding(10, 0, 10, 0);
									view.setLayoutParams(params);
									point.addView(view);
									dots.add(view);
								} else {
									View view = new View(
											GoShopDetailActivity.this);
									view.setBackgroundResource(R.drawable.dot_normal);
									view.setPadding(10, 0, 10, 0);
									view.setLayoutParams(params);

									point.addView(view);
									dots.add(view);
								}
							}
						}
						adapter.notifyDataSetChanged();
					}
				}
			} else {

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// --start 2015年9月28日10:25:40
	private class MPageChangeListener implements OnPageChangeListener {
		/**
		 * 页面状态改变执行的方法
		 * */
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 页面选中以后执行的方法
		 * */
		@Override
		public void onPageSelected(int position) {

			currentItem = position;
			// 圆点更新
			// 更新当前页面为白色的圆点
			dots.get(position % dots.size()).setBackgroundResource(
					R.drawable.dot_focused);
			// 更新上一个页面为灰色的圆点
			dots.get(oldPostion % dots.size()).setBackgroundResource(
					R.drawable.dot_normal);
			// 更新上一个页面的位置
			oldPostion = position;
		}

	}

	public String getCurrentTime() {// 避免特殊字符产生无法调起拍照后无法保存返回
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		String currentTime = df.format(new Date());// new Date()为获取当前系统时间
		return currentTime;
	}

	private class MRunnable implements Runnable {

		@Override
		public void run() {
			// 切换界面
			currentItem = (currentItem + 1) % dots.size();
			;
			// 更新UI
			handler.sendEmptyMessage(0);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 切换到Viewpager 当前的页面
			ivPet.setCurrentItem(currentItem);
		};
	};
	private ImageView imageView_go_shop_nav;

}
