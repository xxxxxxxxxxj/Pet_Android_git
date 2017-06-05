package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.TrainAddressAdapter;
import com.haotang.pet.adapter.TrainServiceOpenAdapter;
import com.haotang.pet.adapter.TrainVideoAdaper;
import com.haotang.pet.entity.TrainData;
import com.haotang.pet.entity.TrainService;
import com.haotang.pet.entity.TrainServiceEvery;
import com.haotang.pet.entity.TrainTransfer;
import com.haotang.pet.entity.TranVideo;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.jc.videoplayer.lib.JCVideoPlayer;
/**
 * 测试提交log本地问题了
 * @author Administrator
 *
 */
public class TrainAppointMentActivity extends SuperActivity implements OnClickListener{
	public static TrainAppointMentActivity trainAppointMentActivity;
	private PullPushLayout pplLayout;
	private int alphaMax = 180;
	private Drawable bgBackDrawable;
	private Drawable bgBackBelowDrawable;
	private Drawable bgShareDrawable;
	private Drawable bgShareBelowDrawable;
	private Drawable bgNavBarDrawable;

	private ImageView service_back;
	private LinearLayout layout_service_back;
	private ImageView service_share;
	private RelativeLayout rlTitle;
	private ImageView service_back_blow;
	private ImageView service_share_below;
	
	//营业时间相关
	private TextView textview_train_tel;
	private TextView textview_swim_time;
	private TextView textview_swim_address;
	private TextView textview_Train_score;
	private TextView textview_Train_score_detail;
	private ImageView ImageView_go_map;
	
	private TrainVideoAdaper trainVideoAdaper;
	private List<TranVideo> videoList = new ArrayList<TranVideo>();
	private MListview train_pets_video_choose;
	private JCVideoPlayer videoplayer_train;
	private RelativeLayout layout_video_header_top;
	public static TextView textview_video_title;
	private RelativeLayout layout_train_choose_pet;
	private SelectableRoundedImageView image_train_logo;
	private TextView textview_train_name;
	public static int petid;
	public static String customerpetname;
	private int serviceid;
	private int servicetype;
	public static int customerpetid;
	public static String petname;
	private int petkind;
	private String avatarPath;
	//中转站
	private LinearLayout layout_train_address;
	private TextView textview_train_address;
	private ImageView image_address_right;
	private MListview train_pets_address_choose;
	private List<TrainTransfer> addressList = new ArrayList<TrainTransfer>();
	private TrainAddressAdapter addressAdapter;
	
	private LinearLayout layout_more_video;
	public static String [] confirm_tip;
	//预约时间
	private LinearLayout layout_pet_time;
	private TextView textView_choose_time;
//	private String [] times;
	private ArrayList<TrainData> times = new ArrayList<TrainData>();;
	public static String apptime="";
	//宠物家训练特色
	private LinearLayout train_feature;
	//宠物家训练小贴士
	private LinearLayout train_dog_notice;
	public static String shopName="";
	private String shopAddress="";
	private String tel="";
	private double ShopLat=0;
	private double ShopLng=0;
	private int previous;
	private MListview train_pets_service_choose;
	private List<TrainService> trainServicesList = new ArrayList<TrainService>();
//	private TrainServiceAdapter trainServiceAdapter;
	
	//第一个条目
	private LinearLayout layout_service_all_train_one;
	private SelectableRoundedImageView imageView_service_icon_one;
	private TextView textView_service_name_one;
	private ImageView imageView_service_right_one;
	private LinearLayout layout_show_low_servie_price_one;
	private TextView textview_layout_service_low_price_one;
	private MListview train_pets_service_choose_open_one;
	//第二个条目
	private LinearLayout layout_service_all_train_two;
	private SelectableRoundedImageView imageView_service_icon_two;
	private TextView textView_service_name_two;
	private ImageView imageView_service_right_two;
	private LinearLayout layout_show_low_servie_price_two;
	private TextView textview_layout_service_low_price_two;
	private MListview train_pets_service_choose_open_two;
	//第三个条目
	private LinearLayout layout_service_all_train_thr;
	private SelectableRoundedImageView imageView_service_icon_thr;
	private TextView textView_service_name_thr;
	private ImageView imageView_service_right_thr;
	private LinearLayout layout_show_low_servie_price_thr;
	private TextView textview_layout_service_low_price_thr;
	private MListview train_pets_service_choose_open_thr;
	public static TrainTransfer transferLast = null;//最终选择的地址
	public static int shopId = 0;
	private TrainServiceOpenAdapter openAdapter= null;
	private TextView textview_pet_un_train;
	private TextView text_train_show_feature;
	private TextView textview_train_notice;
	private LinearLayout right;
	public static String agreementPage;
	private static String desc_img ="";
	public static String noTrainersMsg = null;
	private LinearLayout layout_top_train_time_or_tel;
	private RelativeLayout rl_ppllayout_top;
	private View video_more_top_line;
	private TextView textView_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_appoint);
		trainAppointMentActivity = this;
		addressList.clear();
		trainServicesList.clear();
		try {
			noTrainersMsg = null;
			petid = 0;
			customerpetname = "";
			customerpetid = 0;
			petname = "";
			apptime = "";
			shopName = "";
			transferLast = null;
			shopId = 0;
			agreementPage = "";
			desc_img="";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getIntentData();
		initView();
		setView();
		initListener();
		getServiceData();
	}
	private void getIntentData() {
		previous = getIntent().getIntExtra("previous", -1);
		if (previous == Global.MAIN_TO_TRAIN_DETAIL) {
			if (spUtil.getInt("customerpetid", 0) > 0) {
				customerpetname = spUtil.getString("customerpetname", "");
				customerpetid = spUtil.getInt("customerpetid", 0);
			}
			String myPetImage = spUtil.getString("mypetImage", "");
			if (myPetImage.equals("")) {
				avatarPath = CommUtil.getServiceNobacklash()
						+ spUtil.getString("petimage", "");
			} else {
				avatarPath = myPetImage;
			}
			petkind = spUtil.getInt("petkind", 0);
			petid = spUtil.getInt("petid", 0);
			petname = spUtil.getString("petname", "");
			customerpetid = spUtil.getInt("customerpetid", 0);
			customerpetname = spUtil.getString("customerpetname", "");
		} else if (previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
			petid = getIntent().getIntExtra("petid", 0);
			petkind = getIntent().getIntExtra("petkind", 0);
			petname= getIntent().getStringExtra("petname");
			avatarPath = getIntent().getStringExtra("petimage");
			customerpetname = getIntent().getStringExtra("customerpetname");
			customerpetid = getIntent().getIntExtra("customerpetid", 0);
		}else if (previous == Global.MAIN_TO_TRAIN_DETAIL) {
			petid = getIntent().getIntExtra("petid", 0);
			petkind = getIntent().getIntExtra("petkind", 0);
			petname = getIntent().getStringExtra("petname");
			avatarPath = getIntent().getStringExtra("petimage");
			customerpetid = getIntent().getIntExtra("customerpetid", 0);
			customerpetname = spUtil.getString("customerpetname", "");
		}
	}
	private void getServiceData() {
		// TODO Auto-generated method stub
		layout_service_all_train_one.setVisibility(View.GONE);
		layout_service_all_train_two.setVisibility(View.GONE);
		layout_service_all_train_thr.setVisibility(View.GONE);
		train_pets_service_choose_open_one.setVisibility(View.GONE);
		train_pets_service_choose_open_two.setVisibility(View.GONE);
		train_pets_service_choose_open_thr.setVisibility(View.GONE);
		video_more_top_line.setVisibility(View.VISIBLE);
		imageView_service_right_one.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
		imageView_service_right_two.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
		imageView_service_right_thr.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
		train_pets_address_choose.setVisibility(View.GONE);
		image_address_right.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
		trainServicesList.clear();
		CommUtil.Train(mContext, spUtil.getString("cellphone", ""), spUtil.getInt("tareaid", 0),petid,handler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			Utils.mLogError("==-->训练：= "+ new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("confirm_tip")&&!objectData.isNull("confirm_tip")) {
							JSONArray array = objectData.getJSONArray("confirm_tip");
							if (array.length()>0) {
								confirm_tip = new String[array.length()];
								for (int i = 0; i < array.length(); i++) {
									confirm_tip[i] = array.getString(i);
								}
							}
						}
						if (objectData.has("shop")&&!objectData.isNull("shop")) {
							JSONObject objectShop = objectData.getJSONObject("shop");
							if (objectShop.has("tel")&&!objectShop.isNull("tel")) {
								tel = objectShop.getString("tel");
								textview_train_tel.setText(tel);
							}
							if (objectShop.has("address")&&!objectShop.isNull("address")) {
								String address = objectShop.getString("address");
								textview_swim_address.setText("地址："+address);
								shopAddress = address;
							}
							if (objectShop.has("lng")&&!objectShop.isNull("lng")) {
								double lng = objectShop.getDouble("lng");
								ShopLng = lng;
							}
							if (objectShop.has("lat")&&!objectShop.isNull("lat")) {
								double lat = objectShop.getDouble("lat");
								ShopLat = lat;
							}
							if (objectShop.has("shopId")&&!objectShop.isNull("shopId")) {
								shopId = objectShop.getInt("shopId");
							}
							if (objectShop.has("operTime")&&!objectShop.isNull("operTime")) {
								String operTime = objectShop.getString("operTime");
//								textview_swim_time.setText("营业时间:"+operTime);
							}
							if (objectShop.has("name")&&!objectShop.isNull("name")) {
								shopName = objectShop.getString("name");
							}
							if (objectShop.has("level")&&!objectShop.isNull("level")) {
								JSONObject objectLevel = objectShop.getJSONObject("level");
								if (objectLevel.has("score")&&!objectLevel.isNull("score")) {
									String score = objectLevel.getString("score");
									String lastScore = score+"分";
									textview_Train_score.setText(getGradle(lastScore));
								}
								if (objectLevel.has("desc")&&!objectLevel.isNull("desc")) {
									String desc = objectLevel.getString("desc");
									textview_Train_score_detail.setText(desc);
								}
							}
						}
						if (objectData.has("transfers")&&!objectData.isNull("transfers")) {
							addressList.clear();
							JSONArray arrayTransfer = objectData.getJSONArray("transfers");
							if (arrayTransfer.length()>0) {
								for (int i = 0; i < arrayTransfer.length(); i++) {
									TrainTransfer transfer = new TrainTransfer();
									JSONObject objectEvery = arrayTransfer.getJSONObject(i);
									if (objectEvery.has("address")&&!objectEvery.isNull("address")) {
										transfer.address = objectEvery.getString("address");
									}
									if (objectEvery.has("name")&&!objectEvery.isNull("name")) {
										transfer.name = objectEvery.getString("name");
									}
									if (objectEvery.has("tel")&&!objectEvery.isNull("tel")) {
										transfer.tel = objectEvery.getString("tel");
									}
									if (objectEvery.has("lng")&&!objectEvery.isNull("lng")) {
										transfer.lng = objectEvery.getDouble("lng");
									}
									if (objectEvery.has("lat")&&!objectEvery.isNull("lat")) {
										transfer.lat = objectEvery.getDouble("lat");
									}
									if (objectEvery.has("id")&&!objectEvery.isNull("id")) {
										transfer.id = objectEvery.getInt("id");
									}
									addressList.add(transfer);
								}
							}
						}
						
						if (addressList.size()>0) {
							addressAdapter.notifyDataSetChanged();
							transferLast = addressList.get(0);
							textview_train_address.setText(transferLast.name);
						}
						
						times.clear();
						if (objectData.has("dates")&&!objectData.isNull("dates")) {
							JSONArray arrayDatas = objectData.getJSONArray("dates");
							if (arrayDatas.length()>0) {
								for (int i = 0; i < arrayDatas.length(); i++) {
									TrainData trainData = new TrainData();
									JSONObject objectEveryData = arrayDatas.getJSONObject(i);
									if (objectEveryData.has("date")&&!objectEveryData.isNull("date")) {
										trainData.date = objectEveryData.getString("date");
									}
									if (objectEveryData.has("week")&&!objectEveryData.isNull("week")) {
										trainData.week = objectEveryData.getString("week");
									}
									if (objectEveryData.has("appointment")&&!objectEveryData.isNull("appointment")) {
										trainData.appointment = objectEveryData.getString("appointment");
									}
									times.add(trainData);
								}
							}
						}
						
						if (objectData.has("items")&&!objectData.isNull("items")) {
							JSONArray arrayItems = objectData.getJSONArray("items");
							if (arrayItems.length()>0) {
								textview_pet_un_train.setVisibility(View.GONE);
								layout_service_all_train_one.setVisibility(View.VISIBLE);
								layout_service_all_train_two.setVisibility(View.VISIBLE);
								layout_service_all_train_thr.setVisibility(View.VISIBLE);
								if (arrayItems.length()==1) {
									layout_service_all_train_one.setVisibility(View.VISIBLE);
									layout_service_all_train_two.setVisibility(View.GONE);
									layout_service_all_train_thr.setVisibility(View.GONE);
								}else if (arrayItems.length()==2) {
									layout_service_all_train_one.setVisibility(View.VISIBLE);
									layout_service_all_train_two.setVisibility(View.VISIBLE);
									layout_service_all_train_thr.setVisibility(View.GONE);
								}else if (arrayItems.length()==3) {
									layout_service_all_train_one.setVisibility(View.VISIBLE);
									layout_service_all_train_two.setVisibility(View.VISIBLE);
									layout_service_all_train_thr.setVisibility(View.VISIBLE);
								}
								for (int i = 0; i < arrayItems.length(); i++) {
									TrainService trainService = new TrainService();
									JSONObject objectItems = arrayItems.getJSONObject(i);
									if (objectItems.has("icon")&&!objectItems.isNull("icon")) {
										if (objectItems.getString("icon").contains("http")) {
											trainService.icon = objectItems.getString("icon");
										}else {
											trainService.icon = CommUtil.getServiceNobacklash()+objectItems.getString("icon");
										}
									}
									if (objectItems.has("type")&&!objectItems.isNull("type")) {
										trainService.type = objectItems.getString("type");
									}
									if (objectItems.has("services")&&!objectItems.isNull("services")) {
										JSONArray arrayService = objectItems.getJSONArray("services");
										for (int j = 0; j < arrayService.length(); j++) {
											TrainServiceEvery trainServiceEvery = new TrainServiceEvery();
											JSONObject objectEveryService = arrayService.getJSONObject(j);
											if (objectEveryService.has("price")&&!objectEveryService.isNull("price")) {
												trainServiceEvery.price=objectEveryService.getInt("price");
											}
											if (objectEveryService.has("name")&&!objectEveryService.isNull("name")) {
												trainServiceEvery.name=objectEveryService.getString("name");
											}
											if (objectEveryService.has("id")&&!objectEveryService.isNull("id")) {
												trainServiceEvery.id=objectEveryService.getInt("id");
											}
											if (objectEveryService.has("type")&&!objectEveryService.isNull("type")) {
												trainServiceEvery.type=objectEveryService.getInt("type");
											}
											if (objectEveryService.has("info")&&!objectEveryService.isNull("info")) {
												trainServiceEvery.info=objectEveryService.getString("info");
											}
											trainService.everyTrainServiceList.add(trainServiceEvery);
										}
									}
									trainServicesList.add(trainService);
								}
							}
							if (trainServicesList.size()>0) {
								for (int i = 0; i < trainServicesList.size(); i++) {
									switch (i) {
									case 0:
										TrainService trainService = trainServicesList.get(0);
										ImageLoaderUtil.setImage(imageView_service_icon_one,trainService.icon , R.drawable.user_icon_unnet);
										textView_service_name_one.setText(trainService.type);
										textview_layout_service_low_price_one.setText(trainService.everyTrainServiceList.get(0).price+"");
										break;
									case 1:
										TrainService trainServiceTwo = trainServicesList.get(1);
										ImageLoaderUtil.setImage(imageView_service_icon_two,trainServiceTwo.icon , R.drawable.user_icon_unnet);
										textView_service_name_two.setText(trainServiceTwo.type);
										textview_layout_service_low_price_two.setText(trainServiceTwo.everyTrainServiceList.get(0).price+"");
										break;
									case 2:
										TrainService trainServiceThr = trainServicesList.get(2);
										ImageLoaderUtil.setImage(imageView_service_icon_thr,trainServiceThr.icon , R.drawable.user_icon_unnet);
										textView_service_name_thr.setText(trainServiceThr.type);
										textview_layout_service_low_price_thr.setText(trainServiceThr.everyTrainServiceList.get(0).price+"");
										break;
									}
								}
								
							}
						}else {
							textview_pet_un_train.setVisibility(View.VISIBLE);
							layout_service_all_train_one.setVisibility(View.GONE);
							layout_service_all_train_two.setVisibility(View.GONE);
							layout_service_all_train_thr.setVisibility(View.GONE);
						}
						if (objectData.has("topVideos")&&!objectData.isNull("topVideos")) {
							JSONArray arrayTopVideo = objectData.getJSONArray("topVideos");
							boolean isMore = false;
							if (arrayTopVideo.length()>0) {
								isMore = true;
								if (isMore) {
									JSONObject objectTopOne = arrayTopVideo.getJSONObject(0);
									String TopThumbnail = "";
									if (objectTopOne.has("thumbnail")&&!objectTopOne.isNull("thumbnail")) {
										TopThumbnail = objectTopOne.getString("thumbnail");
									}
									String TopTitle="";
									if (objectTopOne.has("title")&&!objectTopOne.isNull("title")) {
										TopTitle = objectTopOne.getString("title");
									}
									String TopVideoUrl = "";
									if (objectTopOne.has("videoUrl")&&!objectTopOne.isNull("videoUrl")) {
										TopVideoUrl = objectTopOne.getString("videoUrl");
									}
////									//动态设置顶部视频宽高;
//									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*243/375);
									//动态设置顶部视频宽高;
									videoplayer_train.ivThumb.setScaleType(ScaleType.FIT_XY);
									videoplayer_train.setUp(TopVideoUrl, TopTitle,false,1);
									ImageLoaderUtil.setImage(videoplayer_train.ivThumb, TopThumbnail,0);
									textview_video_title.setText(TopTitle);
//									rl_ppllayout_top.setLayoutParams(params);
								}
							}
						}
						if (objectData.has("bottomVideos")&&!objectData.isNull("bottomVideos")) {
							videoList.clear();
							JSONArray arrayBottomVideo = objectData.getJSONArray("bottomVideos");
							boolean isMore = false;
							if (arrayBottomVideo.length()>0) {
								isMore = true;
								if (isMore) {
									TranVideo tranVideo = new TranVideo();
									JSONObject objectTopOne = arrayBottomVideo.getJSONObject(0);
									if (objectTopOne.has("thumbnail")&&!objectTopOne.isNull("thumbnail")) {
										tranVideo.thumbnail = objectTopOne.getString("thumbnail");
									}
									if (objectTopOne.has("title")&&!objectTopOne.isNull("title")) {
										tranVideo.title = objectTopOne.getString("title");
									}
									if (objectTopOne.has("videoUrl")&&!objectTopOne.isNull("videoUrl")) {
										tranVideo.videoUrl = objectTopOne.getString("videoUrl");
									}
									videoList.add(tranVideo);
								}
							}
						}
						if (videoList.size()>0) {
							trainVideoAdaper = new TrainVideoAdaper<TranVideo>(mContext, videoList);
							trainVideoAdaper.setSourse(0, 0);
							train_pets_video_choose.setAdapter(trainVideoAdaper);
							pplLayout.scrollTo(0, 0);
							train_pets_video_choose.setVisibility(View.VISIBLE);
						}
						
						if (objectData.has("feature")&&!objectData.isNull("feature")) {
							JSONObject objectFeature = objectData.getJSONObject("feature");
							if (objectFeature.has("title")&&!objectFeature.isNull("title")) {
								text_train_show_feature.setText(objectFeature.getString("title"));
							}
							if (objectFeature.has("content")&&!objectFeature.isNull("content")) {
								JSONArray arrayFeature  = objectFeature.getJSONArray("content");
								if (arrayFeature.length()>0) {
									train_feature.setVisibility(View.VISIBLE);
									train_feature.removeAllViews();
									for (int i = 0; i < arrayFeature.length(); i++) {
										TextView textView = new TextView(mContext);
										textView.setText(arrayFeature.getString(i));
										textView.setTextColor(getResources().getColor(R.color.a676767));
										textView.setPadding(0, 10, 0, 10);
										train_feature.addView(textView);
									}
								}
							}
						}
						if (objectData.has("notice")&&!objectData.isNull("notice")) {
							JSONObject objectNotice = objectData.getJSONObject("notice");
							if (objectNotice.has("title")&&!objectNotice.isNull("title")) {
								textview_train_notice.setText(objectNotice.getString("title"));
							}
							if (objectNotice.has("content")&&!objectNotice.isNull("content")) {
								JSONArray arrayNotice = objectNotice.getJSONArray("content");
								if (arrayNotice.length()>0) {
									if (arrayNotice.length()>0) {
										train_dog_notice.setVisibility(View.VISIBLE);
										train_dog_notice.removeAllViews();
										for (int i = 0; i < arrayNotice.length(); i++) {
											TextView textView = new TextView(mContext);
											textView.setText(arrayNotice.getString(i));
											textView.setTextColor(getResources().getColor(R.color.a676767));
											textView.setPadding(0, 10, 0, 10);
											train_dog_notice.addView(textView);
										}
									}
								}
							}
						}
						if (objectData.has("desc_img")&&!objectData.isNull("desc_img")) {
							desc_img = "";
							desc_img = objectData.getString("desc_img");
						}
						if (objectData.has("noTrainersMsg")&&!objectData.isNull("noTrainersMsg")) {
							noTrainersMsg = null;
							noTrainersMsg = objectData.getString("noTrainersMsg");
						}else {
							noTrainersMsg=null;
						}
						if (spUtil.getBoolean("isTrainFirst", true)) {
							Intent intent = new Intent(mContext, TrainGuideActivity.class);
							intent.putExtra("tel", tel);
							startActivity(intent);
						}else {
							if (Global.isPop) {
								if (desc_img.startsWith("http")) {
									showNotice(desc_img);
								}else {
									showNotice(CommUtil.getServiceNobacklash()+desc_img);
								}
							}
						}
						if (objectData.has("agreementPage")&&!objectData.isNull("agreementPage")) {
							agreementPage = objectData.getString("agreementPage");
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
			
		}
	};
	private void initListener() {
		train_pets_address_choose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				transferLast = (TrainTransfer) parent.getItemAtPosition(position);
				textview_train_address.setText(transferLast.name);
				train_pets_address_choose.setVisibility(View.GONE);
			}
		});
//		train_pets_service_choose_open_one.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				ToastUtil.showToastShortCenter(mContext, "1"+position);
//			}
//		});
//		train_pets_service_choose_open_two.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				ToastUtil.showToastShortCenter(mContext, "2"+position);
//				
//			}
//		});
//		train_pets_service_choose_open_thr.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				ToastUtil.showToastShortCenter(mContext, "3"+position);
//			}
//		});
	}
	private void setView() {
		textView_title.setText("训犬");
//		topStyleShow();
		if (TextUtils.isEmpty(customerpetname)) {
			textview_train_name.setText(petname+"训练");
		}else {
			textview_train_name.setText(customerpetname+"训练");
		}
		ImageLoaderUtil.setImage(image_train_logo, avatarPath, R.drawable.dog_icon_unnew);
//		//动态设置顶部视频宽高;
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*9/16);
//		layout_video_header_top.setLayoutParams(params);
//		//动态设置顶部视频宽高;
//		videoplayer_train.ivThumb.setScaleType(ScaleType.FIT_XY);
//		videoplayer_train.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4", "测试banner",false,1);
//		ImageLoaderUtil.setImage(videoplayer_train.ivThumb, "http://192.168.0.252/static/orderpic/1479219649772_21832_0.jpg", R.drawable.home_dialog_img);
//		textview_video_title.setText("测试banner");
		videoList.clear();
		addressAdapter = new TrainAddressAdapter<TrainTransfer>(mContext, addressList);
		train_pets_address_choose.setAdapter(addressAdapter);
		train_pets_address_choose.setVisibility(View.GONE);
		
	}
	private void initView() {
		//外侧父控件
		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
		right = (LinearLayout) findViewById(R.id.right);
		service_back = (ImageView) findViewById(R.id.service_back);
		layout_service_back = (LinearLayout) findViewById(R.id.layout_service_back);
		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
		service_share = (ImageView) findViewById(R.id.service_share);
		service_share_below = (ImageView) findViewById(R.id.service_share_below);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);
		//头部banner
		videoplayer_train = (JCVideoPlayer) findViewById(R.id.videoplayer_train);
		textview_video_title = (TextView) findViewById(R.id.textview_video_title);
		layout_video_header_top = (RelativeLayout) findViewById(R.id.layout_video_header_top);
		
		//底部
		textview_train_tel = (TextView) findViewById(R.id.textview_train_tel);
		textview_swim_time = (TextView) findViewById(R.id.textview_swim_time);
		textview_swim_address = (TextView) findViewById(R.id.textview_swim_address);
		ImageView_go_map = (ImageView) findViewById(R.id.ImageView_go_map);
		textview_Train_score = (TextView) findViewById(R.id.textview_Train_score);
		textview_Train_score_detail = (TextView) findViewById(R.id.textview_Train_score_detail);
		
		layout_train_choose_pet = (RelativeLayout) findViewById(R.id.layout_train_choose_pet);
		image_train_logo = (SelectableRoundedImageView) findViewById(R.id.image_train_logo);
		textview_train_name = (TextView) findViewById(R.id.textview_train_name);
		layout_pet_time = (LinearLayout) findViewById(R.id.layout_pet_time);
		textView_choose_time = (TextView) findViewById(R.id.textView_choose_time);
		layout_train_address = (LinearLayout) findViewById(R.id.layout_train_address);
		textview_train_address = (TextView) findViewById(R.id.textview_train_address);
		image_address_right = (ImageView) findViewById(R.id.image_address_right);
		train_pets_address_choose = (MListview) findViewById(R.id.train_pets_address_choose);
		
//		train_pets_service_choose = (MListview) findViewById(R.id.train_pets_service_choose);
		layout_more_video = (LinearLayout) findViewById(R.id.layout_more_video);
		train_pets_video_choose = (MListview) findViewById(R.id.train_pets_video_choose);
		
		train_feature = (LinearLayout) findViewById(R.id.train_feature);
		train_dog_notice = (LinearLayout) findViewById(R.id.train_dog_notice);
		
		textview_pet_un_train = (TextView) findViewById(R.id.textview_pet_un_train);
		//第一个条目
		layout_service_all_train_one = (LinearLayout) findViewById(R.id.layout_service_all_train_one);
		imageView_service_icon_one = (SelectableRoundedImageView) findViewById(R.id.imageView_service_icon_one);
		textView_service_name_one = (TextView) findViewById(R.id.textView_service_name_one);
		imageView_service_right_one = (ImageView) findViewById(R.id.imageView_service_right_one);
		layout_show_low_servie_price_one = (LinearLayout) findViewById(R.id.layout_show_low_servie_price_one);
		textview_layout_service_low_price_one = (TextView) findViewById(R.id.textview_layout_service_low_price_one);
		train_pets_service_choose_open_one = (MListview) findViewById(R.id.train_pets_service_choose_open_one);
		//第二个条目
		layout_service_all_train_two = (LinearLayout) findViewById(R.id.layout_service_all_train_two);
		imageView_service_icon_two = (SelectableRoundedImageView) findViewById(R.id.imageView_service_icon_two);
		textView_service_name_two = (TextView) findViewById(R.id.textView_service_name_two);
		imageView_service_right_two = (ImageView) findViewById(R.id.imageView_service_right_two);
		layout_show_low_servie_price_two = (LinearLayout) findViewById(R.id.layout_show_low_servie_price_two);
		textview_layout_service_low_price_two = (TextView) findViewById(R.id.textview_layout_service_low_price_two);
		train_pets_service_choose_open_two = (MListview) findViewById(R.id.train_pets_service_choose_open_two);
		//第三个条目
		layout_service_all_train_thr = (LinearLayout) findViewById(R.id.layout_service_all_train_thr);
		imageView_service_icon_thr = (SelectableRoundedImageView) findViewById(R.id.imageView_service_icon_thr);
		textView_service_name_thr = (TextView) findViewById(R.id.textView_service_name_thr);
		imageView_service_right_thr = (ImageView) findViewById(R.id.imageView_service_right_thr);
		layout_show_low_servie_price_thr = (LinearLayout) findViewById(R.id.layout_show_low_servie_price_thr);
		textview_layout_service_low_price_thr = (TextView) findViewById(R.id.textview_layout_service_low_price_thr);
		train_pets_service_choose_open_thr = (MListview) findViewById(R.id.train_pets_service_choose_open_thr);
		video_more_top_line = (View) findViewById(R.id.video_more_top_line);
		
		text_train_show_feature = (TextView) findViewById(R.id.text_train_show_feature);
		textview_train_notice = (TextView) findViewById(R.id.textview_train_notice);
		textview_Train_score = (TextView) findViewById(R.id.textview_Train_score);
		layout_top_train_time_or_tel = (LinearLayout) findViewById(R.id.layout_top_train_time_or_tel);
		rl_ppllayout_top = (RelativeLayout) findViewById(R.id.rl_ppllayout_top);

		textView_title = (TextView) findViewById(R.id.textView_title);
		
		layout_pet_time.setOnClickListener(this);
		layout_train_address.setOnClickListener(this);
		layout_more_video.setOnClickListener(this);
		train_pets_video_choose.setFocusable(false);
		layout_train_choose_pet.setOnClickListener(this);
		ImageView_go_map.setOnClickListener(this);
		service_back.setOnClickListener(this);
		layout_service_all_train_one.setOnClickListener(this);
		layout_service_all_train_two.setOnClickListener(this);
		layout_service_all_train_thr.setOnClickListener(this);
		right.setOnClickListener(this);
		textview_Train_score.setOnClickListener(this);
		textview_Train_score_detail.setOnClickListener(this);
		textview_train_tel.setOnClickListener(this);
	}
	private void topStyleShow() {
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
//				JCVideoPlayer.releaseAllVideos();
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		pplLayout.scrollTo(0, 0);
		train_pets_video_choose.setFocusable(true);
		textview_train_tel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		textview_train_tel.getPaint().setAntiAlias(true);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*243/375);
		rl_ppllayout_top.setLayoutParams(params);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			JCVideoPlayer.releaseAllVideos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.service_back:
			finish();
			break;
		case R.id.layout_train_choose_pet:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_SwitchPet);
			goNext(ChoosePetActivityNew.class, Global.TRAIN_TO_CHOOSE_PET_REQUESTCODE, Global.TRAIN_TO_CHOOSE_PET_PRE);
			break;
		case R.id.layout_train_address:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_StationNavigation);
			if (train_pets_address_choose.getVisibility() == View.VISIBLE) {
				train_pets_address_choose.setVisibility(View.GONE);
				image_address_right.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
			}else {
				train_pets_address_choose.setVisibility(View.VISIBLE);
				image_address_right.setImageResource(R.drawable.iv_fostercarechooseroom_fz_up);
			}
			break;
		case R.id.layout_more_video:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_VideoList);
			goNext(VideoMoreActivity.class,0,0);
			break;
		case R.id.layout_pet_time:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_SelectTime);
			if (times.size()<=0) {//时间始终有不可能 < 0
				ToastUtil.showToastShortCenter(mContext, "已满");
			}else {
				goNext(PetDateTrainActivity.class,Global.TRAIN_TO_CHOOSE_TIME,0);
			}
			break;
		case R.id.ImageView_go_map:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_Navigation);
			goNext(ShopLocationActivity.class,0,0);
			break;
		case R.id.layout_service_all_train_one:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_Package);
			if (train_pets_service_choose_open_one.getVisibility()==View.VISIBLE) {
				train_pets_service_choose_open_one.setVisibility(View.GONE);
				imageView_service_right_one.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
			}else {
				openAdapter = new TrainServiceOpenAdapter<TrainServiceEvery>(mContext, trainServicesList.get(0).everyTrainServiceList);
				train_pets_service_choose_open_one.setAdapter(openAdapter);
				train_pets_service_choose_open_one.setVisibility(View.VISIBLE);
				imageView_service_right_one.setImageResource(R.drawable.iv_fostercarechooseroom_fz_up);
			}
			break;
		case R.id.layout_service_all_train_two:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_Package);
			if (train_pets_service_choose_open_two.getVisibility()==View.VISIBLE) {
				train_pets_service_choose_open_two.setVisibility(View.GONE);
				imageView_service_right_two.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
			}else {
				openAdapter = new TrainServiceOpenAdapter<TrainServiceEvery>(mContext, trainServicesList.get(1).everyTrainServiceList);
				train_pets_service_choose_open_two.setAdapter(openAdapter);
				train_pets_service_choose_open_two.setVisibility(View.VISIBLE);
				imageView_service_right_two.setImageResource(R.drawable.iv_fostercarechooseroom_fz_up);
			}
			break;
		case R.id.layout_service_all_train_thr:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_Package);
			if (train_pets_service_choose_open_thr.getVisibility()==View.VISIBLE) {
				train_pets_service_choose_open_thr.setVisibility(View.GONE);
				imageView_service_right_thr.setImageResource(R.drawable.iv_fostercarechooseroom_fz_down);
				video_more_top_line.setVisibility(View.VISIBLE);
			}else {
				openAdapter = new TrainServiceOpenAdapter<TrainServiceEvery>(mContext, trainServicesList.get(2).everyTrainServiceList);
				train_pets_service_choose_open_thr.setAdapter(openAdapter);
				train_pets_service_choose_open_thr.setVisibility(View.VISIBLE);
				imageView_service_right_thr.setImageResource(R.drawable.iv_fostercarechooseroom_fz_up);
				video_more_top_line.setVisibility(View.GONE);
			}
			break;
			
		case R.id.right:
		case R.id.textview_Train_score_detail:
		case R.id.textview_Train_score:
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_TrainingDog_Evaluation);
			goNext(BeauticianCommentsListActivity.class,0,0);
			break;
		case R.id.textview_train_tel:
			MDialog mDialog = new MDialog.Builder(mContext)
			.setTitle("").setType(MDialog.DIALOGTYPE_CONFIRM)
			.setMessage("您确定要拨打电话吗？").setCancelStr("否").setOKStr("是")
			.positiveListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Utils.telePhoneBroadcast(mContext,textview_train_tel.getText().toString());
				}
			}).build();
			mDialog.show();
			break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || "".equals(data))
			return;
		if (resultCode == Global.RESULT_OK) {
			if (requestCode==Global.TRAIN_TO_CHOOSE_PET_REQUESTCODE) {
				Utils.mLogError("==-->+接收返回的宠物");
				noTrainersMsg = null;
				petid = 0;
				customerpetname = "";
				customerpetid = 0;
				petname = "";
				apptime = "";
				shopName = "";
				transferLast = null;
				shopId = 0;
				agreementPage = "";
				desc_img="";
				trainServicesList.clear();
				textView_choose_time.setText("");
				// 选择宠物类型返回
				petid = data.getIntExtra("petid", 0);
				customerpetname = data.getStringExtra("customerpetname");
				serviceid = data.getIntExtra("serviceid", 0);
				servicetype = data.getIntExtra("servicetype", 0);
				customerpetid = data.getIntExtra("customerpetid", 0);
				petname = data.getStringExtra("petname");
				petkind = data.getIntExtra("petkind", 0);
				avatarPath = data.getStringExtra("petimage");
				if (TextUtils.isEmpty(customerpetname)) {
					textview_train_name.setText(petname+"训练");
				}else {
					textview_train_name.setText(customerpetname+"训练");
				}
				ImageLoaderUtil.setImage(image_train_logo, avatarPath,0);
				
				getServiceData();//先不传petid 传了没服务
			}else if (requestCode==Global.TRAIN_TO_CHOOSE_TIME) {
				TrainData trainData = data.getParcelableExtra("apptime");
				int current = data.getIntExtra("current", -1);
				if (current!=-1) {
					String showApptime = "";
					if (current==0) {
						try {
							showApptime=times.get(0).date;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showApptime="今天";
						}
						textView_choose_time.setText(showApptime);
					}else if (current==1) {
						try {
							showApptime=times.get(1).date;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showApptime="明天";
						}
						textView_choose_time.setText(showApptime);
					}else {
						apptime = trainData.appointment.replace("00:00:00", "").trim();
						textView_choose_time.setText(apptime);
					}
				}
				apptime = trainData.appointment.replace("00:00:00", "").trim();
				Utils.mLogError("==-->apptime1:= "+apptime);
			}
		}
	}
	private void goNext(Class cls,int requestCode,int pre){
		Intent intent = new Intent(this, cls);
		intent.putExtra("previous", pre);
//		intent.putExtra("times", times);
		intent.putParcelableArrayListExtra("times", times);
		intent.putExtra("shopname", shopName);
		intent.putExtra("shopaddr", shopAddress);
		intent.putExtra("shoplat", ShopLat);
		intent.putExtra("shoplng", ShopLng);
		intent.putExtra("type", 4);
		startActivityForResult(intent, requestCode);
	}
	private SpannableString getGradle(String str) {
		SpannableString styledText = new SpannableString(str);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.swimdetailshowtext0), 0, str.length() - 1,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(this,
				R.style.swimdetailshowtext1), str.length() - 1, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
	private static void showNotice(String url) {
		Intent intent = new Intent(trainAppointMentActivity, IsCanPickActivity.class);
		intent.putExtra("index", 3);
		intent.putExtra("url", url);
		Global.isPop=false;
		trainAppointMentActivity.startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			noTrainersMsg = null;
			petid = 0;
			customerpetname = "";
			customerpetid = 0;
			petname = "";
			apptime = "";
			shopName = "";
			transferLast = null;
			shopId = 0;
			agreementPage = "";
			desc_img="";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void showImg(boolean isShow){
		if (isShow) {
			if (Global.isPop) {
				if (desc_img.startsWith("http")) {
					showNotice(desc_img);
				}else {
					showNotice(CommUtil.getServiceNobacklash()+desc_img);
				}
			}
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*243/375);
			rl_ppllayout_top.setLayoutParams(params);
		}
	}
	
}
