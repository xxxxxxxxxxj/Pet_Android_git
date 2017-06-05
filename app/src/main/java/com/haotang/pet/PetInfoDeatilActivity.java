package com.haotang.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CustomerPetResonAdapter;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.FastBlur;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * <p>
 * Title:PetInfoDeatilActivity
 * </p>
 * <p>
 * Description:宠物信息详情页
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-16 下午5:03:21
 */
public class PetInfoDeatilActivity extends SuperActivity implements OnClickListener {
	private Pet pet;
	private RelativeLayout rl_petinfodetail_title;
	private PullPushLayout ppl_petinfodetail;
	private ImageView iv_petinfodetail_topbg;
	private SelectableRoundedImageView sriv_petinfodetail_img;
	private ImageView iv_petinfodetail_bjzl;
	private TextView tv_petinfodetail_petname;
	private ImageView iv_petinfodetail_petsex;
	private TextView tv_petinfodetail_petage;
	private RelativeLayout rl_petinfodetail_petcard;
	private TextView tv_petinfodetail_petcard;
	private LinearLayout ll_petinfodetail_xz;
	private LinearLayout ll_petinfodetail_mr;
	private LinearLayout ll_petinfodetail_jy;
	private LinearLayout ll_petinfodetail_yy;
	private TextView tv_petinfodetail_xzcount;
	private TextView tv_petinfodetail_mrcount;
	private TextView tv_petinfodetail_jycount;
	private TextView tv_petinfodetail_yycount;
	private TextView tv_petinfodetail_qjzcount;
	private TextView tv_petinfodetail_mlzcount;
	private ImageView iv_petinfodetail_mode;
	private TextView tv_petinfodetail_mode;
	private Drawable bgNavBarDrawable;
	private int alphaMax = 180;
	private RelativeLayout rl_petinfodetail_reson;
	private MListview mlv_petinfodetail_reson;
	private LinearLayout ll_petinfodetail_other;
	private String[] imgUrls = new String[1];
	private SharedPreferenceUtil spUtil;
	private String petCardUrl;
	private ImageView iv_petinfodetail_back;
	public static SuperActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this;
		MApplication.listAppoint.add(this);
		getData();
		initView();
		initLinster();
		setView();
		setData();
	}

	private void getData() {
		Intent intent = getIntent();
		pet = (Pet) intent.getSerializableExtra("pet");
		petCardUrl = intent.getStringExtra("petCardUrl");
		spUtil = SharedPreferenceUtil.getInstance(this);
	}

	private void initView() {
		setContentView(R.layout.activity_pet_info_deatil);
		rl_petinfodetail_title = (RelativeLayout) findViewById(R.id.rl_petinfodetail_title);
		ppl_petinfodetail = (PullPushLayout) findViewById(R.id.ppl_petinfodetail);
		iv_petinfodetail_topbg = (ImageView) findViewById(R.id.iv_petinfodetail_topbg);
		sriv_petinfodetail_img = (SelectableRoundedImageView) findViewById(R.id.sriv_petinfodetail_img);
		iv_petinfodetail_bjzl = (ImageView) findViewById(R.id.iv_petinfodetail_bjzl);
		tv_petinfodetail_petname = (TextView) findViewById(R.id.tv_petinfodetail_petname);
		iv_petinfodetail_petsex = (ImageView) findViewById(R.id.iv_petinfodetail_petsex);
		tv_petinfodetail_petage = (TextView) findViewById(R.id.tv_petinfodetail_petage);
		rl_petinfodetail_petcard = (RelativeLayout) findViewById(R.id.rl_petinfodetail_petcard);
		tv_petinfodetail_petcard = (TextView) findViewById(R.id.tv_petinfodetail_petcard);
		ll_petinfodetail_xz = (LinearLayout) findViewById(R.id.ll_petinfodetail_xz);
		ll_petinfodetail_mr = (LinearLayout) findViewById(R.id.ll_petinfodetail_mr);
		ll_petinfodetail_jy = (LinearLayout) findViewById(R.id.ll_petinfodetail_jy);
		ll_petinfodetail_yy = (LinearLayout) findViewById(R.id.ll_petinfodetail_yy);
		tv_petinfodetail_xzcount = (TextView) findViewById(R.id.tv_petinfodetail_xzcount);
		tv_petinfodetail_mrcount = (TextView) findViewById(R.id.tv_petinfodetail_mrcount);
		tv_petinfodetail_jycount = (TextView) findViewById(R.id.tv_petinfodetail_jycount);
		tv_petinfodetail_yycount = (TextView) findViewById(R.id.tv_petinfodetail_yycount);
		tv_petinfodetail_qjzcount = (TextView) findViewById(R.id.tv_petinfodetail_qjzcount);
		tv_petinfodetail_mlzcount = (TextView) findViewById(R.id.tv_petinfodetail_mlzcount);
		iv_petinfodetail_mode = (ImageView) findViewById(R.id.iv_petinfodetail_mode);
		tv_petinfodetail_mode = (TextView) findViewById(R.id.tv_petinfodetail_mode);
		rl_petinfodetail_reson = (RelativeLayout) findViewById(R.id.rl_petinfodetail_reson);
		mlv_petinfodetail_reson = (MListview) findViewById(R.id.mlv_petinfodetail_reson);
		ll_petinfodetail_other = (LinearLayout) findViewById(R.id.ll_petinfodetail_other);
		iv_petinfodetail_back = (ImageView) findViewById(R.id.iv_petinfodetail_back);
	}

	private void initLinster() {
		iv_petinfodetail_back.setOnClickListener(this);
		sriv_petinfodetail_img.setOnClickListener(this);
		iv_petinfodetail_bjzl.setOnClickListener(this);
		rl_petinfodetail_petcard.setOnClickListener(this);
		ll_petinfodetail_xz.setOnClickListener(this);
		ll_petinfodetail_mr.setOnClickListener(this);
		ll_petinfodetail_jy.setOnClickListener(this);
		ll_petinfodetail_yy.setOnClickListener(this);
		bgNavBarDrawable = rl_petinfodetail_title.getBackground();
		bgNavBarDrawable.setAlpha(0);
		ppl_petinfodetail
				.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {

					@Override
					public void onSlideUp(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlideDwon(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlide(int alpha) {
						int alphaReverse = alphaMax - alpha;
						if (alphaReverse < 0) {
							alphaReverse = 0;
						}
						bgNavBarDrawable.setAlpha(alpha);
					}
				});
	}

	private void setView() {
		rl_petinfodetail_title.bringToFront();
	}

	private void setData() {
		if (pet != null) {
			String gradeTxt = pet.cleanhint;
			String certiOrderStatus = pet.certiOrderStatus;
			CertiOrder certiOrder = pet.certiOrder;
			int emotion = pet.mood;
			int sumBath = pet.bathnum;
			int sumCos = pet.beautynum;
			int sumFos = pet.fosternum;
			int swimCount = pet.sumSwim;
			int cleanGrade = pet.cleannum;
			int beautyGrade = pet.charmnum;
			String statusName = "";
			if (pet.sex == 1) {// 男
				iv_petinfodetail_petsex
						.setImageResource(R.drawable.pet_sex_male);
			} else if (pet.sex == 0) {// 女
				iv_petinfodetail_petsex
						.setImageResource(R.drawable.pet_sex_female);
			}
			if (certiOrder != null) {
				int status = certiOrder.status;
				String[] statusNames = certiOrder.statusName;
				if (statusNames != null && statusNames.length > 0) {
					if (status < statusNames.length) {
						statusName = statusNames[status];
					}
				} else {
					statusName = certiOrderStatus;
				}
			} else {
				statusName = certiOrderStatus;
			}
			Utils.setStringText(tv_petinfodetail_mode, gradeTxt);
			Utils.setStringText(tv_petinfodetail_petcard, statusName);
			if (emotion == 1) {
				iv_petinfodetail_mode
						.setBackgroundResource(R.drawable.icon_mood_1);
			} else if (emotion == 2) {
				iv_petinfodetail_mode
						.setBackgroundResource(R.drawable.icon_mood_2);
			} else if (emotion == 3) {
				iv_petinfodetail_mode
						.setBackgroundResource(R.drawable.icon_mood_3);
			} else if (emotion == 4) {
				iv_petinfodetail_mode
						.setBackgroundResource(R.drawable.icon_mood_4);
			} else {
				iv_petinfodetail_mode
						.setBackgroundResource(R.drawable.icon_mood_1);
			}
			tv_petinfodetail_qjzcount.setText(lessThanZero(beautyGrade) + "分");
			tv_petinfodetail_mlzcount.setText(lessThanZero(cleanGrade) + "分");
			tv_petinfodetail_xzcount.setText(lessThanZero(sumBath) + "次");
			tv_petinfodetail_mrcount.setText(lessThanZero(sumCos) + "次");
			tv_petinfodetail_jycount.setText(lessThanZero(sumFos) + "天");
			tv_petinfodetail_yycount.setText(lessThanZero(swimCount) + "次");
			tv_petinfodetail_yycount.setText(lessThanZero(swimCount) + "次");
			tv_petinfodetail_yycount.setText(lessThanZero(swimCount) + "次");
			Utils.setText(tv_petinfodetail_petname, pet.nickName, "",
					View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_petinfodetail_petage, pet.month, "", View.VISIBLE,
					View.INVISIBLE);
			imgUrls[0] = pet.image;
			ImageLoaderUtil.loadImg(pet.image, sriv_petinfodetail_img,
					R.drawable.user_icon_unnet, null);
			ImageLoaderUtil.loadImg(pet.image, iv_petinfodetail_topbg,
					R.drawable.user_icon_unnet, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String path, View view,
								Bitmap bitmap) {
							if (path != null && bitmap != null) {
								ImageView iv = (ImageView) view;
								int scaleRatio = 3;
								int blurRadius = 8;
								Bitmap scaledBitmap = Bitmap.createScaledBitmap(
										bitmap, bitmap.getWidth() / scaleRatio,
										bitmap.getHeight() / scaleRatio, false);
								Bitmap bip = FastBlur.doBlur(scaledBitmap,
										blurRadius, true);
								iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
								iv.setImageBitmap(bip);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
			setVisOrInVis();
		}
	}

	private int lessThanZero(int num) {
		int currNum = 0;
		if (num <= 0) {
			currNum = 0;
		} else {
			currNum = num;
		}
		return currNum;
	}

	private void setVisOrInVis() {
		if (pet.kindid == 2) {// 猫
			rl_petinfodetail_petcard.setVisibility(View.GONE);
			ll_petinfodetail_yy.setVisibility(View.GONE);
			ll_petinfodetail_other.setVisibility(View.VISIBLE);
		} else {
			ll_petinfodetail_yy.setVisibility(View.VISIBLE);
			ll_petinfodetail_other.setVisibility(View.GONE);
			if (pet.certiDogSize == 1 || pet.certiDogSize == 2) {// 小型犬
				rl_petinfodetail_petcard.setVisibility(View.VISIBLE);
				if (pet.certiOrder != null) {
					switch (pet.certiOrder.status) {
					case 2:// 已认证
						break;
					case 5:// 已领证
						break;
					case 6:// 未通过
						setReson();
						break;
					default:
						break;
					}
				}
			} else {// 大型犬
				rl_petinfodetail_petcard.setVisibility(View.GONE);
			}
		}
	}

	private void setReson() {
		if (pet.certiOrder != null) {
			String remark = pet.certiOrder.remark;
			if (remark != null && !TextUtils.isEmpty(remark)) {
				List<String> resonList = new ArrayList<String>();
				resonList.clear();
				if (remark.contains("&")) {// 多条
					String[] reson = remark.split("&");
					List<String> asList = Arrays.asList(reson);
					resonList.addAll(asList);
				} else {// 一条
					resonList.add(remark);
				}
				rl_petinfodetail_reson.setVisibility(View.VISIBLE);
				mlv_petinfodetail_reson.setAdapter(new CustomerPetResonAdapter(
						this, resonList));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sriv_petinfodetail_img:// 点击头像
			startActivity(new Intent(this, ImagePagerActivity.class).putExtra(
					ImagePagerActivity.EXTRA_IMAGE_URLS, imgUrls).putExtra(
					ImagePagerActivity.EXTRA_IMAGE_INDEX, 0));
			break;
		case R.id.iv_petinfodetail_bjzl:// 编辑资料
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_EditPet);
			startActivity(new Intent(this, PetAddActivity.class).putExtra(
					"customerpetid", pet.customerpetid).putExtra("previous",
					Global.PETINFO_TO_EDITPET));
			break;
		case R.id.rl_petinfodetail_petcard:// 办证
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_DogCard);
			if (pet != null) {
				if (pet.certiOrder != null) {
					switch (pet.certiOrder.status) {
					case 0:// 待支付
						goNext(OrderPayActivity.class,
								Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY, 0,
								0, 0);
						break;
					case 1:// 已支付-待审核
						goNext(ADActivity.class, 0, 0, 0, 1);
						break;
					case 2:// 审核通过-已认证
						goNext(ADActivity.class, 0, 0, 0, 2);
						break;
					case 3:// 邮寄
						goNext(ADActivity.class, 0, 0, 0, 3);
						break;
					case 4:// 上门取件
						goNext(ADActivity.class, 0, 0, 0, 4);
						break;
					case 5:// 完成
						goNext(ADActivity.class, 0, 0, 0, 5);
						break;
					case 6:// 审核不通过
						goNext(ADActivity.class, 0, 0, 0, 6);
						break;
					case 7:// 未办证
						goNext(ADActivity.class, 0, 0, 0, 7);
						break;
					default:
						goNext(ADActivity.class, 0, 0, 0, 7);
						break;
					}
				} else {
					goNext(ADActivity.class, 0, 0, 0, -1);
				}
			} else {
				Intent intent = new Intent(this, ADActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("previous", Global.H5_TO_LOGIN);
				intent.putExtra("url", petCardUrl);
				startActivityForResult(intent, Global.H5_TO_LOGIN);
			}
			break;
		case R.id.ll_petinfodetail_xz:// 洗澡
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_BathSelect);
			if (pet != null) {
				if (pet.availServiceType.contains("1")) {
					// 带对象跳转至对应界面
					Intent intent = new Intent();
					if (spUtil.getInt("tareaid", 0) == 100) {
						intent.setClass(this, ShopListActivity.class);
						intent.putExtra(
								"previous",
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST);
					} else {
						intent.setClass(this, ServiceActivity.class);
						intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					}
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("servicetype", 1);
					intent.putExtra("petkind", pet.kindid);
					intent.putExtra("petid", pet.id);
					if (pet.kindid == 1) {
						intent.putExtra("serviceid", 1);
					} else if (pet.kindid == 2) {
						intent.putExtra("serviceid", 3);
					}
					intent.putExtra("customerpetid", pet.customerpetid);
					Log.e("TAG", "pet.customerpetid = "+pet.customerpetid);
					intent.putExtra("customerpetname", pet.nickName);
					startActivity(intent);
					// 保存选择的宠物的头像
					spUtil.saveString("petimg", pet.image);
				}else{
					ToastUtil.showToastShortBottom(PetInfoDeatilActivity.this, "您的宠物暂不支持洗澡");
				}
			}
			break;
		case R.id.ll_petinfodetail_mr:// 美容
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_BeautySelect);
			if (pet != null) {
				if (pet.availServiceType.contains("2")) {
					// 带对象跳转至对应界面
					Intent mrintent = new Intent();
					if (spUtil.getInt("tareaid", 0) == 100) {
						mrintent.setClass(this, ShopListActivity.class);
						mrintent.putExtra(
								"previous",
								Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST);
					} else {
						mrintent.setClass(this, ServiceActivity.class);
						mrintent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					}
					mrintent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					mrintent.putExtra("servicetype", 2);
					mrintent.putExtra("petkind", pet.kindid);
					mrintent.putExtra("petid", pet.id);
					if (pet.kindid == 1) {
						mrintent.putExtra("serviceid", 2);
					} else if (pet.kindid == 2) {
						mrintent.putExtra("serviceid", 4);
					}
					mrintent.putExtra("customerpetid", pet.customerpetid);
					mrintent.putExtra("customerpetname", pet.nickName);
					startActivity(mrintent);
					// 保存选择的宠物的头像
					spUtil.saveString("petimg", pet.image);
				}else{
					ToastUtil.showToastShortBottom(PetInfoDeatilActivity.this, "您的宠物暂不支持美容");
				}
			}
			break;
		case R.id.ll_petinfodetail_jy:// 寄养
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetHome_FosterSelect);
			goNext(FostercareChooseroomActivity.class,
					Global.PRE_CUSTOMERPETINFO_TO_FOSTERCAREAPPOINTMENT_PET,
					100, 100, 0);
			break;
		case R.id.ll_petinfodetail_yy:// 游泳
			if (pet.kindid == 1) {
				goNext(SwimDetailActivity.class,
						Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET, 100, 100, 0);
			} else {// Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
					// //不仅仅代表未登录
				goNextUnLoginOrCanNotSwim();
			}
			break;
		case R.id.iv_petinfodetail_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void goNext(Class clazz, int previous, int serviceid,
			int servicetype, int status) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("userId", spUtil.getInt("userid", 0));
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("previous", previous);
		if (pet != null) {
			intent.putExtra("petid", pet.id);
			intent.putExtra("petkind", pet.kindid);
			intent.putExtra("petname", pet.name != null ? pet.name : "");
			intent.putExtra("customerpetid", pet.customerpetid);
			intent.putExtra("customerpetname",
					pet.nickName != null ? pet.nickName : "");
			intent.putExtra("petimage", pet.image != null ? pet.image : "");
			if (pet.certiOrder != null) {
				intent.putExtra("CertiOrderId", pet.certiOrder.CertiOrderId);
			}
			String url = "";
			if (status == 1 || status == 2 || status == 3 || status == 4
					|| status == 5) {
				if (pet.certiOrder != null) {
					url = CommUtil.getWebBaseUrl()
							+ "web/petcerti/auditresult?certi_id="
							+ pet.certiOrder.certiId + "&user_petid="
							+ pet.customerpetid;
				} else {
					url = CommUtil.getWebBaseUrl()
							+ "web/petcerti/auditresult?user_petid="
							+ pet.customerpetid;
				}
			}else {
				if (pet.certiUrl.contains("?")) {
					if (pet.certiOrder != null) {
						url = pet.certiUrl + "&certi_id="
								+ pet.certiOrder.certiId + "&user_petid="
								+ pet.customerpetid;
					} else {
						url = pet.certiUrl + "&user_petid=" + pet.customerpetid;
					}
				} else {
					if (pet.certiOrder != null) {
						url = pet.certiUrl + "?certi_id="
								+ pet.certiOrder.certiId + "&user_petid="
								+ pet.customerpetid;
					} else {
						url = pet.certiUrl + "?user_petid=" + pet.customerpetid;
					}
				}
			}
			intent.putExtra("url", url);
		}
		Bundle bundle = new Bundle();
		bundle.putInt("index", 1);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void goNextUnLoginOrCanNotSwim() {
		Intent intent = new Intent(this, ChoosePetActivityNew.class);
		intent.putExtra("previous",
				Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
		startActivity(intent);
	}
}
